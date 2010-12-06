package lk.rgd.prs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.Auditable;
import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.index.SolrIndexManager;
import lk.rgd.crs.api.bean.UserWarning;
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
    private final SolrIndexManager solrIndexManager;
    private final PersonCitizenshipDAO citizenshipDAO;
    private final Random rand = new Random(System.currentTimeMillis());

    public PopulationRegistryImpl(PersonDAO personDao, PINGenerator pinGenerator, SolrIndexManager solrIndexManager,
        PersonCitizenshipDAO citizenshipDAO) {
        this.personDao = personDao;
        this.pinGenerator = pinGenerator;
        this.solrIndexManager = solrIndexManager;
        this.citizenshipDAO = citizenshipDAO;
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
            pin = pinGenerator.generatePINNumber(person.getDateOfBirth(), person.getGender() == 0);
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
        return pin;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Person> addExistingPerson(Person person, List<PersonCitizenship> citizenshipList,
        boolean ignoreDuplicates, User user) {
        logger.debug("Adding an existing person to the PRS");
        long pin = -1;

        validateRequiredFields(person);
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
            exactRecord.add(personDao.findPersonByTemporaryPIN(person.getTemporaryPin()));
        } else if (person.getNic() != null) {
            for (Person p : personDao.findPersonsByNIC(person.getNic())) {
                exactRecord.add(p);
            }
        }
        if (!exactRecord.isEmpty() && !ignoreDuplicates) {
            logger.debug("Duplicate {} records found for temporary PIN or NIC", exactRecord.size());
            return exactRecord;
        }

        if (exactRecord.isEmpty() || ignoreDuplicates) {
            person.setStatus(Person.Status.SEMI_VERIFIED);
            person.setLifeStatus(Person.LifeStatus.ALIVE);
            person.setSubmittedLocation(user.getPrimaryLocation());
            // generate a PIN for existing person
            pin = pinGenerator.generatePINNumber(person.getDateOfBirth(), person.getGender() == 0);
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
            logger.debug("Added a new person to the PRS with PersonUKey : {} and generated PIN : {}",
                person.getPersonUKey(), pin);
        }
        return Collections.emptyList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Person loadPersonToEdit(long personUKey, User user) {
        validateAccessOfUserToEdit(user);
        if (!user.isAuthorized(Permission.PRS_LOOKUP_PERSON_BY_KEYS)) {
            handleException("User : " + user.getUserId() + " is not allowed to lookup entries on the PRS by keys (uKey)",
                ErrorCodes.PRS_LOOKUP_BY_KEYS_DENIED);
        }
        if (!user.isAuthorized(Permission.PRS_EDIT_PERSON)) {
            handleException("User : " + user.getUserId() + " is not allowed to update person to the PRS",
                ErrorCodes.PRS_EDIT_RECORD_DENIED);
        }
        final Person person = personDao.getByUKey(personUKey);

        // load permanent address to the transient field
        final Set<Address> addresses = person.getAddresses();
        for (Address add : addresses) {
            if (add.isPermanent()) {
                person.setPermanentAddress(add.getLine1());
                break;
            }
        }
        // load current address to the transient field
        final Address currentAdd = person.getLastAddress();
        if (currentAdd != null && !isEmptyString(currentAdd.getLine1())) {
            person.setCurrentAddress(currentAdd.getLine1());
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

        validateAccessOfUserToEdit(user);
        validateRequiredFields(person);
        Person existing = personDao.getByUKey(person.getPersonUKey());

        // TODO if birth exist can not edit through this, throw exception (use alteration process)

        final Person.Status currentState = existing.getStatus();
        if (currentState == Person.Status.SEMI_VERIFIED || currentState == Person.Status.UNVERIFIED) {
            person.setStatus(Person.Status.SEMI_VERIFIED);
            person.setLifeStatus(Person.LifeStatus.ALIVE);
            person.setSubmittedLocation(user.getPrimaryLocation());

            personDao.updatePerson(person, user);

            final String permanentAddress = person.getPermanentAddress();
            final String currentAddress = person.getCurrentAddress();
            // update permanent address of the person to the PRS
            if (isNotEmpty(permanentAddress)) {
                Set<Address> addresses = existing.getAddresses();
                if (addresses != null) {
                    for (Address permanentAdd : addresses) {
                        if (permanentAdd.isPermanent()) {
                            permanentAdd.setLine1(permanentAddress);
                            personDao.updateAddress(permanentAdd);
                            break;
                        }
                    }
                }
            }
            // update current address of the person to the PRS
            if (isNotEmpty(currentAddress)) {
                final Address currentAdd = existing.getLastAddress();
                if (currentAdd != null) {
                    currentAdd.setLine1(currentAddress);
                    existing.setLastAddress(currentAdd);
                    personDao.updateAddress(currentAdd);
                }
            }
            if (permanentAddress != null || currentAddress != null) {
                personDao.updatePerson(person, user);
            }
            // update citizenship list of the person to the PRS
            // TODO need to find a better solution
            if (citizenshipList != null && !citizenshipList.isEmpty()) {
                final Set<PersonCitizenship> existingCitizens = person.getCountries();
                for (PersonCitizenship pc : existingCitizens) {
                    citizenshipDAO.deleteCitizenship(pc.getPersonUKey(), pc.getCountryId());
                }
                for (PersonCitizenship pc : citizenshipList) {
                    pc.setPerson(person);
                    citizenshipDAO.addCitizenship(pc, user);
                }
            }
        } else {
            handleException("Cannot modify PRS record with personUKey : " + existing.getPersonUKey() +
                " Illegal state : " + currentState, ErrorCodes.ILLEGAL_STATE);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void editExistingPersonAfterApproval(Person person, User user) {
        // TODO chathuranga
        throw new UnsupportedOperationException("Edit person after approval operation not implemented yet...");
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserWarning> approvePerson(long personUKey, boolean ignoreWarnings, User user) {
        // TODO still implementing don't review
        // TODO validate access(location??) and (required fields) minimum requirements
        // check approve permission
        if (!user.isAuthorized(Permission.PRS_APPROVE_PERSON)) {
            handleException("User : " + user.getUserId() + " is not allowed to approve entries on the PRS by keys (uKey)",
                ErrorCodes.PRS_APPROVE_RECORD_DENIED);
        }

        Person existing = getByUKey(personUKey, user);
        // is the person record currently existing in a state for approval
        final Person.Status currentState = existing.getStatus();
        if (Person.Status.SEMI_VERIFIED != currentState) {
            handleException("Cannot approve PRS entry : " + personUKey + " Illegal state : " + currentState,
                ErrorCodes.INVALID_STATE_FOR_PRS_APPROVAL);
        }
        // TODO validate and output warning list
        List<UserWarning> warnings = Collections.emptyList();

        if (!warnings.isEmpty() && ignoreWarnings) {
            logger.debug("inside warning list");

        }

        if (warnings.isEmpty() || ignoreWarnings) {
            existing.setStatus(Person.Status.VERIFIED);
            existing.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
            existing.getLifeCycleInfo().setApprovalOrRejectUser(user);
            // TODO change this
//            addPerson(existing, user);
            updatePerson(existing, user);
            logger.debug("Approved of PRS entry : {} Ignore warnings : {}", personUKey, ignoreWarnings);
        } else {
            logger.debug("Approval of PRS entry : {} stopped due to warnings", personUKey);
        }
        return warnings;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deletePersonBeforeApproval(long personUKey, User user) {
        // TODO chathuranga
        throw new UnsupportedOperationException();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void rejectPerson(long personUKey, User user) {
        // TODO chathuranga
        throw new UnsupportedOperationException();
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
        personDao.updatePerson(person, user);
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
        for (Address a : p.getAddresses()) {
            a.isPermanent();
        }
        for (Marriage m : p.getMarriages()) {
            m.getMarriageUKey();
        }
        return p;
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
        personDao.addMarriage(marriage);
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
    public List<Person> findPersonsByDOBGenderAndName(Date dob, int gender, String name) {
        // TODO
        throw new UnsupportedOperationException("TODO method - asankha");
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Person> getPRSPendingApprovalByLocation(Location location, int pageNo, int noOfRows, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get PRS records pending approval by LocationId : " + location.getLocationUKey() + " Page : "
                + pageNo + " and with number of rows per page : " + noOfRows);
        }
        validateAccessToLocation(location, user);
        return personDao.getApprovalPendingPersonsByLocation(location, pageNo, noOfRows);
    }
}
