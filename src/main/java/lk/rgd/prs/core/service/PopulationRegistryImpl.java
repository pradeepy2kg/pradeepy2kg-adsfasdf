package lk.rgd.prs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.Auditable;
import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.service.PRSRecordsIndexer;
import lk.rgd.crs.core.service.PopulationRegisterValidator;
import lk.rgd.prs.api.dao.PersonCitizenshipDAO;
import lk.rgd.prs.api.dao.PersonDAO;
import lk.rgd.prs.api.domain.Address;
import lk.rgd.prs.api.domain.Marriage;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.domain.PersonCitizenship;
import lk.rgd.prs.api.service.PINGenerator;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static lk.rgd.prs.core.PRSValidationUtils.*;

/**
 * This is the main service interface for the PRS
 *
 * @author asankha
 * @author Chathuranga Withana
 *         <p/>
 *         TODO Autit calls and log metrics of use
 */
public class PopulationRegistryImpl implements PopulationRegistry {

    private static final Logger logger = LoggerFactory.getLogger(PopulationRegistryImpl.class);

    private final PersonDAO personDao;
    private final PINGenerator pinGenerator;
    private final PRSRecordsIndexer prsIndexer;
    private final PersonCitizenshipDAO citizenshipDAO;
    private final PopulationRegisterValidator populationRegisterValidator;
    private final Random rand = new Random(System.currentTimeMillis());

    public PopulationRegistryImpl(PersonDAO personDao, PINGenerator pinGenerator, PRSRecordsIndexer prsIndexer,
        PersonCitizenshipDAO citizenshipDAO, PopulationRegisterValidator populationRegisterValidator) {
        this.personDao = personDao;
        this.pinGenerator = pinGenerator;
        this.prsIndexer = prsIndexer;
        this.citizenshipDAO = citizenshipDAO;
        this.populationRegisterValidator = populationRegisterValidator;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public long addPerson(Person person, User user) {
        long pin = -1;

        if (!user.isAuthorized(Permission.PRS_ADD_PERSON)) {
            handleException("User : " + user.getUserId() + " is not allowed to add entries to the PRS",
                ErrorCodes.PRS_ADD_RECORD_DENIED);
        }

        // generate a PIN for a verified record
        if (person.getStatus() == Person.Status.VERIFIED) {
            pin = pinGenerator.generatePINNumber(person.getDateOfBirth(), person.getGender() == 0, person.getNic());
            person.setPin(pin);
        } else if (person.getStatus() == Person.Status.SEMI_VERIFIED) {
            // adds a semi-verified record
        } else if (person.getStatus() == Person.Status.UNVERIFIED) {
            // generate a temporary PIN number depending on the DOB or a random DOB
            if (person.getDateOfBirth() != null) {
                pin = pinGenerator.generateTemporaryPINNumber(person.getDateOfBirth(), person.getGender() == 0);
                person.setPin(pin);
                person.setTemporaryPin(pin);
            } else {
                // generate random DOB - but do not save it, just use it to generate a random PIN
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, 2800 + rand.nextInt(100));   // 2800 ~ 2899 which is the temporary range for unknown DOB
                cal.set(Calendar.DAY_OF_YEAR, rand.nextInt(364) + 1);
                // TODO is this part complete?
            }
        }
        person.setSubmittedLocation(user.getPrimaryLocation());
        personDao.addPerson(person, user);

        // add to the Solr index
        prsIndexer.updateIndex(person);
        return pin;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Person> addExistingPerson(Person person, List<PersonCitizenship> citizenshipList,
        boolean ignoreDuplicates, User user) {
        logger.debug("Adding an existing person to the PRS by user : {}", user.getUserId());
        long pin = -1;

        populationRegisterValidator.validateMinimalRequirementsOfNewPerson(person);
        if (!user.isAuthorized(Permission.PRS_LOOKUP_PERSON_BY_KEYS)) {
            handleException("User : " + user.getUserId() + " is not allowed to lookup entries on the PRS by keys (nic/temporaryPIN)",
                ErrorCodes.PRS_LOOKUP_BY_KEYS_DENIED);
        }
        if (!user.isAuthorized(Permission.PRS_ADD_PERSON)) {
            handleException("User : " + user.getUserId() + " is not allowed to add entries to the PRS",
                ErrorCodes.PRS_ADD_RECORD_DENIED);
        }

        // TODO add Solr searching results to following list
        final List<Person> exactRecord = new ArrayList<Person>();
        if (person.getTemporaryPin() != null) {
            Person p = personDao.findPersonByTemporaryPIN(person.getTemporaryPin());
            if (p != null && (isRecordInDataEntry(p.getStatus()) || p.getStatus() == Person.Status.VERIFIED)) {
                exactRecord.add(personDao.findPersonByTemporaryPIN(person.getTemporaryPin()));
            }
        } else if (person.getNic() != null) {
            for (Person p : personDao.findPersonsByNIC(person.getNic())) {
                if (isRecordInDataEntry(p.getStatus()) || p.getStatus() == Person.Status.VERIFIED) {
                    exactRecord.add(p);
                }
            }
        }
        if (!exactRecord.isEmpty() && !ignoreDuplicates) {
            logger.debug("Duplicate {} records found for temporary PIN or NIC", exactRecord.size());
            return exactRecord;
        }

        if (exactRecord.isEmpty() || ignoreDuplicates) {
            person.setStatus(Person.Status.DATA_ENTRY);
            person.setLifeStatus(Person.LifeStatus.ALIVE);
            person.setSubmittedLocation(user.getPrimaryLocation());
            // generate a PIN for existing person
            pin = pinGenerator.generatePINNumber(person.getDateOfBirth(), person.getGender() == 0, person.getNic());
            person.setPin(pin);

            personDao.addPerson(person, user);

            final String permanentAddress = person.getPermanentAddress();
            final String currentAddress = person.getCurrentAddress();
            // add permanent address of the person to the PRS
            if (isNotEmpty(permanentAddress)) {
                final Address permanentAdd = new Address(permanentAddress);
                permanentAdd.setPermanent(true);
                person.specifyAddress(permanentAdd);
                personDao.addAddress(permanentAdd);
            }
            // add current address of the person to the PRS
            if (isNotEmpty(currentAddress)) {
                final Address currentAdd = new Address(currentAddress);
                person.specifyAddress(currentAdd);
                personDao.addAddress(currentAdd);
            }
            if (permanentAddress != null || currentAddress != null) {
                personDao.updatePerson(person, user);
            }
            // add citizenship list of the person to the PRS
            if (citizenshipList != null) {
                for (PersonCitizenship pc : citizenshipList) {
                    pc.setPerson(person);
                    citizenshipDAO.addCitizenship(pc, user);
                }
            }
            // add to the Solr index
            prsIndexer.updateIndex(person);

            logger.debug("Added a new person to the PRS with personUKey : {} and generated PIN : {}",
                person.getPersonUKey(), pin);
        }
        return Collections.emptyList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Person loadPersonToEdit(long personUKey, User user) {
        logger.debug("Attempt to load PRS entry to edit with personUKey : {} and user : {}", personUKey, user.getUserId());
        validateAccessOfUserToEdit(user);
        if (!user.isAuthorized(Permission.PRS_LOOKUP_PERSON_BY_KEYS)) {
            handleException("User : " + user.getUserId() + " is not allowed to lookup entries on the PRS by keys (uKey)",
                ErrorCodes.PRS_LOOKUP_BY_KEYS_DENIED);
        }
        final Person person = personDao.getByUKey(personUKey);

        Set<Address> addresses = person.getAddresses();
        Map<Long, Address> permanentMap = new LinkedHashMap<Long, Address>();
        Map<Long, Address> currentMap = new LinkedHashMap<Long, Address>();

        for (Address add : addresses) {
            if (add.isPermanent()) {
                permanentMap.put(add.getAddressUKey(), add);
            } else {
                currentMap.put(add.getAddressUKey(), add);
            }
        }

        // load permanent address to the transient field
        if (addresses != null) {
            Set<Long> keys = permanentMap.keySet();
            TreeSet<Long> addressTreeSet = new TreeSet<Long>(keys);

            Iterator<Long> it = addressTreeSet.descendingIterator();
            while (it.hasNext()) {
                Long addKey = it.next();
                Address permanentAdd = permanentMap.get(addKey);
                person.setPermanentAddress(permanentAdd.getLine1());
                break;
            }
        }

        // load current address to the transient field
        if (addresses != null && currentMap.size() > 0) {
            Set<Long> keys = currentMap.keySet();
            TreeSet<Long> addressTreeSet = new TreeSet<Long>(keys);

            Iterator<Long> it = addressTreeSet.descendingIterator();
            while (it.hasNext()) {
                Long addKey = it.next();
                Address currentAdd = currentMap.get(addKey);
                person.setCurrentAddress(currentAdd.getLine1());
                break;
            }
        }

        logger.debug("Person citizenship list loaded for personUKey: {} with size : {} loaded", personUKey,
            person.getCountries().size());

        return person;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void editExistingPersonBeforeApproval(Person person, List<PersonCitizenship> citizenshipList, User user) {
        final long personUKey = person.getPersonUKey();
        logger.debug("Attempt to edit PRS entry before approval with personUKey : {}", personUKey);
        validateAccessOfUserToEdit(user);
        populationRegisterValidator.validateMinimalRequirementsOfNewPerson(person);
        Person existing = personDao.getByUKey(personUKey);

        final Person.Status currentState = existing.getStatus();
        if (isRecordInDataEntry(currentState)) {
            existing.setStatus(Person.Status.DATA_ENTRY);
            existing.setLifeStatus(Person.LifeStatus.ALIVE);
            existing.setSubmittedLocation(user.getPrimaryLocation());
            // setting updated fields to the existing person
            setChangedFieldsBeforeUpdate(existing, person);

            personDao.updatePerson(existing, user);
            prsIndexer.updateIndex(existing);

            final String permanentAddress = person.getPermanentAddress();
            final String currentAddress = person.getCurrentAddress();

            Set<Address> addresses = existing.getAddresses();
            // update permanent address of the person to the PRS
            if (isNotEmpty(permanentAddress) && addresses != null) {
                for (Address permanentAdd : addresses) {
                    if (permanentAdd.isPermanent() && !permanentAddress.equals(permanentAdd.getLine1())) {
                        permanentAdd.setLine1(permanentAddress);
                        permanentAdd.setStartDate(new Date());
                        personDao.updateAddress(permanentAdd);
                        break;
                    }
                }
            }
            // update current address of the person to the PRS
            if (isNotEmpty(currentAddress) && addresses != null && addresses.size() > 1) {
                final Address currentAdd = existing.getLastAddress();
                if (currentAdd != null && !currentAddress.equals(currentAdd.getLine1())) {
                    currentAdd.setLine1(currentAddress);
                    currentAdd.setStartDate(new Date());
                    existing.setLastAddress(currentAdd);
                    personDao.updateAddress(currentAdd);
                }
            }

            // if currently does not have any addresses add permanent or current address
            // TODO this cannot happen, since permanent address is a required field
            if (isNotEmpty(permanentAddress) && addresses != null && addresses.isEmpty()) {
                final Address permanentAdd = new Address(permanentAddress);
                permanentAdd.setPermanent(true);
                existing.specifyAddress(permanentAdd);
                personDao.addAddress(permanentAdd);
            }
            if (isNotEmpty(currentAddress) && addresses != null && addresses.size() == 1) {
                final Address currentAdd = new Address(currentAddress);
                existing.specifyAddress(currentAdd);
                personDao.addAddress(currentAdd);
            }

            if (permanentAddress != null || currentAddress != null) {
                personDao.updatePerson(existing, user);
                prsIndexer.updateIndex(existing);
            }

            // update citizenship list of the person to the PRS
            if (citizenshipList != null && !citizenshipList.isEmpty()) {
                final Set<PersonCitizenship> existingCitizens = existing.getCountries();
                for (PersonCitizenship pc : existingCitizens) {
                    citizenshipDAO.deleteCitizenship(personUKey, pc.getCountryId(), pc.getPassportNo());
                }
                for (PersonCitizenship pc : citizenshipList) {
                    pc.setPerson(existing);
                    citizenshipDAO.addCitizenship(pc, user);
                }
            }
        } else {
            handleException("Cannot modify PRS record with personUKey : " + personUKey + " Illegal state : " +
                currentState, ErrorCodes.ILLEGAL_STATE);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void editExistingPersonAfterApproval(Person person, List<PersonCitizenship> citizenshipList, User user) {
        final long personUKey = person.getPersonUKey();
        logger.debug("Attempt to edit approved PRS entry with personUKey : {}", personUKey);

        // check whether if user have permission to approve PRS entries
        validateAccessOfUserToApprove(user);
        if (!user.isAuthorized(Permission.PRS_EDIT_PERSON_AFTER_APPROVE)) {
            handleException("User : " + user.getUserId() + " is not allowed to edit approved entries on the PRS by keys (uKey)",
                ErrorCodes.PRS_EDIT_RECORD_DENIED_AFTER_APPROVE);
        }

        Person existing = personDao.getByUKey(personUKey);

        if (existing != null) {
            // validate existing person
            validatePersonState(existing, Person.Status.VERIFIED);
            validateAccessToLocation(existing.getSubmittedLocation(), user);
            // update civil status
            existing.setCivilStatus(person.getCivilStatus());
            existing.setPersonPhoneNo(person.getPersonPhoneNo());
            existing.setPersonEmail(person.getPersonEmail());

            final String permanentAddress = person.getPermanentAddress();
            final String currentAddress = person.getCurrentAddress();

            Set<Address> addresses = existing.getAddresses();
            Map<Long, Address> permanentMap = new LinkedHashMap<Long, Address>();
            Map<Long, Address> currentMap = new LinkedHashMap<Long, Address>();

            for (Address add : addresses) {
                if (add.isPermanent()) {
                    permanentMap.put(add.getAddressUKey(), add);
                } else {
                    currentMap.put(add.getAddressUKey(), add);
                }
            }

            if (isNotEmpty(permanentAddress) && addresses != null) {

                Set<Long> keys = permanentMap.keySet();
                TreeSet<Long> addressTreeSet = new TreeSet<Long>(keys);

                Iterator<Long> it = addressTreeSet.descendingIterator();
                while (it.hasNext()) {
                    Long addKey = it.next();
                    Address permanentAdd = permanentMap.get(addKey);
                    if (permanentAdd.isPermanent() && !permanentAddress.equals(permanentAdd.getLine1())) {
                        permanentAdd.setEndDate(new Date());
                        personDao.updateAddress(permanentAdd);

                        final Address newAddress = new Address(permanentAddress);
                        newAddress.setPermanent(true);
                        existing.specifyAddress(newAddress);
                        personDao.addAddress(newAddress);
                    }
                    break;
                }
            }

            // update current address of the person to the PRS
            if (isNotEmpty(currentAddress) && addresses != null) {

                Set<Long> keys = currentMap.keySet();
                TreeSet<Long> addressTreeSet = new TreeSet<Long>(keys);

                if (currentMap.size() > 0) {

                    Iterator<Long> it = addressTreeSet.descendingIterator();
                    while (it.hasNext()) {
                        Long addKey = it.next();
                        Address currentAdd = currentMap.get(addKey);
                        if (!currentAddress.equals(currentAdd.getLine1())) {
                            currentAdd.setEndDate(new Date());
                            personDao.updateAddress(currentAdd);

                            final Address newAddress = new Address(currentAddress);
                            existing.specifyAddress(newAddress);
                            personDao.addAddress(newAddress);
                        }
                        break;
                    }
                } else {
                    final Address newAddress = new Address(currentAddress);
                    existing.specifyAddress(newAddress);
                    personDao.addAddress(newAddress);
                }
            }

            // TODO need verification, is ok to delete existing and add new citizenship
            // update citizenship list of the person to the PRS
            if (citizenshipList != null && !citizenshipList.isEmpty()) {
                final Set<PersonCitizenship> existingCitizens = existing.getCountries();
                for (PersonCitizenship pc : existingCitizens) {
                    citizenshipDAO.deleteCitizenship(personUKey, pc.getCountryId(), pc.getPassportNo());
                }
                for (PersonCitizenship pc : citizenshipList) {
                    pc.setPerson(existing);
                    citizenshipDAO.addCitizenship(pc, user);
                }
            }

            // update the Solr index
            prsIndexer.updateIndex(existing);
        } else {
            handleException("User : " + user.getUserId() + " is not allowed to update non-existing PRS entry with personUKey : "
                + personUKey, ErrorCodes.PRS_EDIT_RECORD_DENIED_AFTER_APPROVE);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserWarning> approvePerson(long personUKey, boolean ignoreWarnings, User user) {

        // check user permission for approve
        validateAccessOfUserToApprove(user);
        Person existing = personDao.getByUKey(personUKey);
        // does the user have access to existing records submitted location
        validateAccessToLocation(existing.getSubmittedLocation(), user);
        // check all required fields are filled before approval
        populationRegisterValidator.validateMinimalRequirementsOfExistingPerson(existing);

        // is the person record currently existing in a state for approval
        final Person.Status currentState = existing.getStatus();
        if (!isRecordInDataEntry(currentState) && !existing.getLifeCycleInfo().isActiveRecord()) {
            handleException("Cannot approve PRS entry : " + personUKey + " Illegal state : " + currentState,
                ErrorCodes.INVALID_STATE_FOR_PRS_APPROVAL);
        }

        List<UserWarning> warnings = populationRegisterValidator.validateStandardRequirements(personDao, existing, user);

        if (!warnings.isEmpty() && ignoreWarnings) {
            StringBuilder sb = new StringBuilder();
            if (existing.getComments() != null) {
                sb.append(existing.getComments()).append("\n");
            }

            sb.append(DateTimeUtils.getISO8601FormattedString(new Date())).
                append(" - Approved PRS entry ignoring warnings. User : ").append(user.getUserId()).append("\n");

            for (UserWarning w : warnings) {
                sb.append(w.getSeverity());
                sb.append("-");
                sb.append(w.getMessage());
            }
            existing.setComments(sb.toString());
        }

        if (warnings.isEmpty() || ignoreWarnings) {
            // TODO update father/ mother relationship
            manageRelationships(existing, user);

            existing.setStatus(Person.Status.VERIFIED);
            existing.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
            existing.getLifeCycleInfo().setApprovalOrRejectUser(user);

            // if approving person does not have a pin generate pin
            if (existing.getPin() == null) {
                final long pin = pinGenerator.generatePINNumber(
                    existing.getDateOfBirth(), existing.getGender() == 0, existing.getNic());
                existing.setPin(pin);
            }

            personDao.updatePerson(existing, user);
            prsIndexer.updateIndex(existing);
            logger.debug("Approved of PRS entry : {} Ignore warnings : {}", personUKey, ignoreWarnings);
            return Collections.emptyList();
        } else {
            logger.debug("Approval of PRS entry : {} stopped due to warnings", personUKey);
            return warnings;
        }
    }

    // TODO need to complete this
    private void manageRelationships(Person person, User user) {
        final String motherPinOrNIC = person.getMotherPINorNIC();
        final String fatherPinOrNIC = person.getFatherPINorNIC();

        Person mother = null;
        Person father = null;

        try {
            mother = findPersonByPINorNIC(motherPinOrNIC, user);
            father = findPersonByPINorNIC(fatherPinOrNIC, user);
        } catch (IllegalArgumentException e) {
            handleException("Invalid PIN used for fatherPIN : " + fatherPinOrNIC + " or motherPIN : " + motherPinOrNIC,
                ErrorCodes.INVALID_PIN);
        }

        if (mother != null && Person.Status.VERIFIED == mother.getStatus()) {
            person.setMother(mother);
        }
        if (father != null && Person.Status.VERIFIED == father.getStatus()) {
            person.setFather(father);
        }

        if (father != null && mother != null && Person.Status.VERIFIED == mother.getStatus() &&
            Person.Status.VERIFIED == father.getStatus()) {
            //
        }
    }


    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deletePersonBeforeApproval(long personUKey, String comment, User user) {
        logger.debug("Attempt to delete PRS entry with personUKey : {}", personUKey);
        if (!user.isAuthorized(Permission.PRS_DELETE_PERSON)) {
            handleException("User : " + user.getUserId() + " is not allowed to delete entries on the PRS by UKey",
                ErrorCodes.PRS_DELETE_RECORD_DENIED);
        }
        if (comment == null || comment.trim().length() < 1) {
            handleException("A comment is required to delete PRS entry with personUKey : " + personUKey,
                ErrorCodes.COMMENT_REQUIRED_PRS_DELETE);
        }

        final Person existing = personDao.getByUKey(personUKey);
        // does the user have access to existing records submitted location
        validateAccessToLocation(existing.getSubmittedLocation(), user);

        final StringBuilder sb = new StringBuilder();
        if (existing.getComments() == null) {
            sb.append("DELETED\n");
            sb.append(comment);
            existing.setComments(sb.toString());
        } else {
            sb.append(existing.getComments());
            sb.append("\nDELETED\n");
            sb.append(comment);
            existing.setComments(sb.toString());
        }

        // a PRS entry can be deleted by DEO or higher before approval
        final Person.Status currentState = existing.getStatus();
        if (isRecordInDataEntry(currentState)) {
            existing.setStatus(Person.Status.DELETED);
            existing.getLifeCycleInfo().setActiveRecord(false);
            existing.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
            existing.getLifeCycleInfo().setApprovalOrRejectUser(user);
            existing.setSubmittedLocation(user.getPrimaryLocation());
            personDao.updatePerson(existing, user);
            prsIndexer.updateIndex(existing);
            logger.debug("Deleted PRS entry with peronUKey : {} by user : {}", personUKey, user.getUserId());
        } else {
            handleException("Cannot delete PRS entry with personUKey : " + personUKey +
                " Illegal state : " + currentState, ErrorCodes.ILLEGAL_STATE);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void rejectPersonBeforeApproval(long personUKey, String comment, User user) {
        logger.debug("Attempt to reject PRS entry with personUKey : {}", personUKey);
        if (!user.isAuthorized(Permission.PRS_REJECT_PERSON)) {
            handleException("User : " + user.getUserId() + " is not allowed to reject entries on the PRS by UKey",
                ErrorCodes.PRS_REJECT_RECORD_DENIED);
        }
        if (comment == null || comment.trim().length() < 1) {
            handleException("A comment is required to REJECT PRS entry with personUKey : " + personUKey,
                ErrorCodes.COMMENT_REQUIRED_PRS_REJECT);
        }

        final Person existing = personDao.getByUKey(personUKey);
        // does the user have access to existing records submitted location
        validateAccessToLocation(existing.getSubmittedLocation(), user);

        final StringBuilder sb = new StringBuilder();
        if (existing.getComments() == null) {
            sb.append("REJECTED\n");
            sb.append(comment);
            existing.setComments(sb.toString());
        } else {
            sb.append(existing.getComments());
            sb.append("\nREJECTED\n");
            sb.append(comment);
            existing.setComments(sb.toString());
        }

        // a PRS entry can be rejected by ADR or higher before approval
        final Person.Status currentState = existing.getStatus();
        if (isRecordInDataEntry(currentState)) {
            existing.setStatus(Person.Status.CANCELLED);
            existing.getLifeCycleInfo().setActiveRecord(false);
            existing.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
            existing.getLifeCycleInfo().setApprovalOrRejectUser(user);
            existing.setSubmittedLocation(user.getPrimaryLocation());
            personDao.updatePerson(existing, user);
            prsIndexer.updateIndex(existing);
            logger.debug("Rejected PRS entry with personUKey : {} by user : {}", personUKey, user.getUserId());
        } else {
            handleException("Cannot reject PRS entry with personUKey : " + personUKey +
                " Illegal State : " + currentState, ErrorCodes.ILLEGAL_STATE);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    // TODO chathuranga remove this if unnecessary
    public void markPRSCertificateAsPrinted(long personUKey, User user) {
        logger.debug("Attempt to mark PRS certificate as marked for PRS entry with personUKey : {}", personUKey);
        if (!user.isAuthorized(Permission.PRS_MARK_CERT_PRINTED)) {
            handleException("User : " + user.getUserId() + " is not allowed mark to PRS certificate as printed ",
                ErrorCodes.PRS_CERT_MARK_AS_PRINTED_DENIED);
        }

        final Person existing = personDao.getByUKey(personUKey);
        // does th user have access to existing records submitted location
        validateAccessToLocation(existing.getSubmittedLocation(), user);

        final Person.Status currentState = existing.getStatus();
        if (currentState == Person.Status.VERIFIED) {
            logger.debug("Marked PRS certificate as printed for PRS entry with personUKey : {} by user : {}",
                personUKey, user.getUserId());
        } else {
            handleException("Cannot mark PRS certificate as printed for PRS entry with personUKey : " + personUKey +
                " Illegal State : " + currentState, ErrorCodes.ILLEGAL_STATE);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePerson(Person person, User user) {
        if (!user.isAuthorized(Permission.PRS_ADD_PERSON)) {
            handleException("User : " + user.getUserId() + " is not allowed to update entries on the PRS",
                ErrorCodes.PRS_ADD_RECORD_DENIED);
        }

        // if the name in english/official, gender, race or date of birth has changed on a verified record,
        // we need to archive current record and keep it!
        final Person existing = personDao.getByUKey(person.getPersonUKey());
        if (existing != null && existing.getStatus() == Person.Status.VERIFIED &&
            ((existing.getFullNameInEnglishLanguage() != null &&
                !existing.getFullNameInEnglishLanguage().equals(person.getFullNameInEnglishLanguage())) ||
                (existing.getFullNameInOfficialLanguage() != null &&
                    !existing.getFullNameInOfficialLanguage().equals(person.getFullNameInOfficialLanguage())) ||
                (existing.getDateOfBirth() != null &&
                    !existing.getDateOfBirth().equals(person.getDateOfBirth())) ||
                (existing.getRace() != null &&
                    !existing.getRace().equals(person.getRace())) ||
                existing.getGender() != person.getGender())) {

            existing.setStatus(Person.Status.ARCHIVED_ALTERED);
            personDao.updatePerson(existing, user);

            // now add the update as a new record
            person.setPersonUKey(0);
            personDao.addPerson(person, user);
            // add to the Solr index - both archived record and new record
            prsIndexer.updateIndex(existing);
            prsIndexer.updateIndex(person);

        } else {
            personDao.updatePerson(person, user);
            // update the Solr index
            prsIndexer.updateIndex(person);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Auditable
    public Person getByUKey(long personUKey, User user) {
        if (!user.isAuthorized(Permission.PRS_LOOKUP_PERSON_BY_KEYS)) {
            handleException("User : " + user.getUserId() + " is not allowed to lookup entries on the PRS by keys (uKey)",
                ErrorCodes.PRS_LOOKUP_BY_KEYS_DENIED);
        }
        return personDao.getByUKey(personUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Auditable
    public Person getLoadedObjectByUKey(long personUKey, User user) {
        if (!user.isAuthorized(Permission.PRS_LOOKUP_PERSON_BY_KEYS)) {
            handleException("User : " + user.getUserId() + " is not allowed to lookup entries on the PRS by keys (uKey)",
                ErrorCodes.PRS_LOOKUP_BY_KEYS_DENIED);
        }

        Person p = personDao.getByUKey(personUKey);
        if (p != null) {

            if (p.getLifeCycleInfo().getCreatedUser() != null) {
                p.getLifeCycleInfo().getCreatedUser().getUserName();
            }
            if (p.getLifeCycleInfo().getLastUpdatedUser() != null) {
                p.getLifeCycleInfo().getLastUpdatedUser().getUserName();
            }
            if (p.getLifeCycleInfo().getApprovalOrRejectUser() != null) { // this maybe null
                p.getLifeCycleInfo().getApprovalOrRejectUser().getUserName();
            }

            p.getMarriages().size();
            p.getAddresses().size();
            p.getCountries().size();

            logger.debug("Person loaded for personUKey: " + personUKey + " Addresses : " + p.getAddresses().size() +
                " Marriages : " + p.getMarriages().size() + " Citizenships : " + p.getCountries().size());
            return p;
        } else {
            logger.debug("Person with personUKey: {} is not found on the database", personUKey);
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addMarriage(Marriage marriage, User user) {
        if (!user.isAuthorized(Permission.PRS_ADD_PERSON)) {
            handleException("User : " + user.getUserId() + " is not allowed to add marriages to the PRS",
                ErrorCodes.PRS_ADD_RECORD_DENIED);
        }
        personDao.addMarriage(marriage);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateMarriage(Marriage marriage, User user) {
        if (!user.isAuthorized(Permission.PRS_EDIT_PERSON)) {
            handleException("User : " + user.getUserId() + " is not allowed to update marriages to the PRS",
                ErrorCodes.PRS_ADD_RECORD_DENIED);
        }
        personDao.updateMarriage(marriage);
    }

    /**
     * @inheritDoc
     */
    @Auditable
    @Transactional(propagation = Propagation.SUPPORTS)
    public Marriage findMarriageByMRUKey(long mrUKey, User user) {
        if (!user.isAuthorized(Permission.PRS_LOOKUP_PERSON_BY_KEYS)) {
            handleException("User : " + user.getUserId() + " is not allowed to lookup entries from the PRS",
                ErrorCodes.PRS_LOOKUP_BY_KEYS_DENIED);
        }
        return personDao.findMarriageByMRUKey(mrUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addAddress(Address address, User user) {
        if (!user.isAuthorized(Permission.PRS_ADD_PERSON)) {
            handleException("User : " + user.getUserId() + " is not allowed to add addresses to the PRS",
                ErrorCodes.PRS_ADD_RECORD_DENIED);
        }
        personDao.addAddress(address);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateAddress(Address address, User user) {
        if (!user.isAuthorized(Permission.PRS_EDIT_PERSON)) {
            handleException("User : " + user.getUserId() + " is not allowed to update addresses to the PRS",
                ErrorCodes.PRS_ADD_RECORD_DENIED);
        }
        personDao.updateAddress(address);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCitizenship(PersonCitizenship citizenship, User user) {
        if (!user.isAuthorized(Permission.PRS_ADD_PERSON)) {
            handleException("User : " + user.getUserId() + " is not allowed to add citizenships to the PRS",
                ErrorCodes.PRS_ADD_RECORD_DENIED);
        }
        citizenshipDAO.addCitizenship(citizenship, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCitizenship(PersonCitizenship citizenship, User user) {
        if (!user.isAuthorized(Permission.PRS_EDIT_PERSON)) {
            handleException("User : " + user.getUserId() + " is not allowed to update citizenships to the PRS",
                ErrorCodes.PRS_EDIT_RECORD_DENIED);
        }
        citizenshipDAO.updateCitizenship(citizenship, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Auditable
    public Person findPersonByPIN(long pin, User user) {
        if (!user.isAuthorized(Permission.PRS_LOOKUP_PERSON_BY_KEYS)) {
            handleException("User : " + user.getUserId() + " is not allowed to lookup entries on the PRS by keys (pin)",
                ErrorCodes.PRS_LOOKUP_BY_KEYS_DENIED);
        }
        return personDao.findPersonByPIN(pin);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Auditable
    public List<Person> findPersonsByNIC(String nic, User user) {
        if (!user.isAuthorized(Permission.PRS_LOOKUP_PERSON_BY_KEYS)) {
            handleException("User : " + user.getUserId() +
                " is not allowed to lookup entries on the PRS by keys (nic)", ErrorCodes.PRS_LOOKUP_BY_KEYS_DENIED);
        }
        return personDao.findPersonsByNIC(nic);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Auditable
    public List<Person> findAllChildren(Person person, User user) {
        if (!user.isAuthorized(Permission.PRS_LOOKUP_PERSON_BY_KEYS)) {
            handleException("User : " + user.getUserId() + " is not allowed to lookup entries on the PRS for children",
                ErrorCodes.PRS_LOOKUP_BY_KEYS_DENIED);
        }
        return personDao.findAllChildren(person);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Auditable
    public List<Person> findAllSiblings(Person person, User user) {
        if (!user.isAuthorized(Permission.PRS_LOOKUP_PERSON_BY_KEYS)) {
            handleException("User : " + user.getUserId() + " is not allowed to lookup entries on the PRS for siblings",
                ErrorCodes.PRS_LOOKUP_BY_KEYS_DENIED);
        }
        if (person.getFather() == null && person.getMother() == null) {
            logger.debug("Parent information not provided for looking up siblings {}", person.getPersonUKey());
            return Collections.EMPTY_LIST;
        }
        return personDao.findAllSiblings(person);
    }

    /**
     * @inheritDoc
     */
    @Auditable
    @Transactional(propagation = Propagation.SUPPORTS)
    public Person findPersonByPINorNIC(String pinOrNic, User user) {
        try {
            if (!isBlankString(pinOrNic)) {
                long pin = Long.parseLong(pinOrNic);
                // this is a pin
                return findPersonByPIN(pin, user);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (NumberFormatException ignore) {
            // this is a nic, if multiple rows match, just return the first
            List<Person> results = findPersonsByNIC(pinOrNic, user);
            if (results != null && !results.isEmpty()) {
                return results.get(0);
            }
        }
        return null;
    }

    /**
     * @inheritDoc
     */
    @Auditable
    @Transactional(propagation = Propagation.SUPPORTS)
    public Person findUniquePersonByPINorNIC(String pinOrNic, User user) {
        try {
            if (!isBlankString(pinOrNic)) {
                long pin = Long.parseLong(pinOrNic);
                // this is a pin
                return findPersonByPIN(pin, user);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (NumberFormatException ignore) {
            // this is a nic, if multiple rows match, just return the first
            List<Person> results = findPersonsByNIC(pinOrNic, user);
            if (results != null && results.size() == 1) {
                return results.get(0);
            }
        }
        return null;
    }

    /**
     * @inheritDoc
     */
    @Auditable
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Person> findPersonsByDOBGenderAndName(Date dob, int gender, String name) {
        // TODO
        throw new UnsupportedOperationException("TODO method - asankha");
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Person> getPersonsByLocation(Location location, int pageNo, int noOfRows, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get PRS records by LocationId : " + location.getLocationUKey() + " Page : " + pageNo +
                " and with number of rows per page : " + noOfRows);
        }
        validateAccessToLocation(location, user);
        return personDao.getPaginatedListByLocation(location, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Person> getPersonByLocationAndPIN(Location location, long pin, User user) {
        logger.debug("Get PRS record by LocationId : {} and PIN :{}", location.getLocationUKey(), pin);
        validateAccessToLocation(location, user);
        return personDao.getByLocationAndPIN(location, pin);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Person> getPersonsByLocationAndNIC(Location location, String nic, User user) {
        logger.debug("Get PRS records by LocationId : {} and NIC : {}", location.getLocationUKey(), nic);
        validateAccessToLocation(location, user);
        return personDao.getByLocationAndNIC(location, nic);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Person> getPersonByLocationAndTemporaryPIN(Location location, long tempPin, User user) {
        logger.debug("Get PRS record by LocationId : {} and TemporaryPIN : {}", location.getLocationUKey(), tempPin);
        validateAccessToLocation(location, user);
        return personDao.getByLocationAndTempPIN(location, tempPin);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Person> findGrandMother(Person person, User user) {
        logger.debug("get grand mother list for person , pin : {}", (person != null) ? person.getPin() : "null");
        List<Person> grandMother = Collections.emptyList();
        //have maximum two grand mothers (fathers mother and mothers mother)
        Person father = (person != null) ? findPersonByPINorNIC(person.getFatherPINorNIC(), user) : null;
        Person mother = (person != null) ? findPersonByPINorNIC(person.getMotherPINorNIC(), user) : null;
        //add father's mother as grand mother
        if (father != null) {
            Person fathersMother = findPersonByPINorNIC(father.getMotherPINorNIC(), user);
            if (fathersMother != null) {
                grandMother.add(fathersMother);
            }
        }
        //add mother's mother as grand mother
        if (mother != null) {
            Person mothersMother = findPersonByPINorNIC(mother.getMotherPINorNIC(), user);
            if (mothersMother != null) {
                grandMother.add(mothersMother);
            }
        }
        return grandMother;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Person> findGrandFather(Person person, User user) {
        logger.debug("get grand father list for person , pin : {}", (person != null) ? person.getPin() : "null");
        List<Person> grandFather = Collections.emptyList();
        //have maximum two grand fathers (fathers father and mothers father)
        Person father = (person != null) ? findPersonByPINorNIC(person.getFatherPINorNIC(), user) : null;
        Person mother = (person != null) ? findPersonByPINorNIC(person.getMotherPINorNIC(), user) : null;
        //add father's father as grand father
        if (father != null) {
            Person fathersFather = findPersonByPINorNIC(father.getFatherPINorNIC(), user);
            if (fathersFather != null) {
                grandFather.add(fathersFather);
            }
        }
        //add mother's father as grand father
        if (mother != null) {
            Person mothersFather = findPersonByPINorNIC(mother.getFatherPINorNIC(), user);
            if (mothersFather != null) {
                grandFather.add(mothersFather);
            }
        }
        return grandFather;
    }

    /**
     * Checks whether the given state is in data entry level, that is not approved yet
     *
     * @param currentState current status of the record
     * @return if the given state in data entry mode returns true, else false
     */
    private boolean isRecordInDataEntry(Person.Status currentState) {
        return (currentState == Person.Status.SEMI_VERIFIED || currentState == Person.Status.UNVERIFIED ||
            currentState == Person.Status.DATA_ENTRY);
    }

    private void setChangedFieldsBeforeUpdate(final Person existing, final Person passing) {
        logger.debug("Setting edited fields to the existing PRS entry with personUKey : {}", existing.getPersonUKey());
        existing.setDateOfRegistration(passing.getDateOfRegistration());
        existing.setNic(passing.getNic());
        existing.setTemporaryPin(passing.getTemporaryPin());
        existing.setRace(passing.getRace());
        existing.setDateOfBirth(passing.getDateOfBirth());
        existing.setPlaceOfBirth(passing.getPlaceOfBirth());
        existing.setFullNameInOfficialLanguage(passing.getFullNameInOfficialLanguage());
        existing.setFullNameInEnglishLanguage(passing.getFullNameInEnglishLanguage());
        existing.setGender(passing.getGender());
        existing.setCivilStatus(passing.getCivilStatus());
        existing.setFatherPINorNIC(passing.getFatherPINorNIC());
        existing.setMotherPINorNIC(passing.getMotherPINorNIC());
        existing.setPersonPhoneNo(passing.getPersonPhoneNo());
        existing.setPersonEmail(passing.getPersonEmail());
    }
}
