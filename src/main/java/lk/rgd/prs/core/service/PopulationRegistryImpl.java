package lk.rgd.prs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.Auditable;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.index.SolrIndexManager;
import lk.rgd.prs.PRSRuntimeException;
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

/**
 * This is the main service interface for the PRS
 *
 * @author asankha
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
            }
        }
        personDao.addPerson(person, user);
        return pin;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Person> addExistingPerson(Person person, String permanentAddress, String currentAddress,
        List<PersonCitizenship> citizenshipList, boolean ignoreDuplicates, User user) {
        long pin = -1;
        logger.debug("Adding an existing person to the PRS");

        validateRequiredFields(person, permanentAddress);
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
            // add permanent address of the person to the PRS
            if (permanentAddress != null && permanentAddress.trim().length() > 0) {
                final Address permanentAdd = new Address(permanentAddress);
                permanentAdd.setPermanent(true);
                person.specifyAddress(permanentAdd);
                personDao.addAddress(permanentAdd);
            }
            // add current address of the person to the PRS
            if (currentAddress != null && currentAddress.trim().length() > 0) {
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
        }
        logger.debug("Added a new person to the PRS with PersonUKey : {} and PIN : {}", person.getPersonUKey(),
            person.getPin());
        return Collections.emptyList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void editExistingPerson(Person person, String permanentAddress, String currentAddress,
        List<PersonCitizenship> citizenshipList, User user) {

        validateAccessOfUserToEdit(person, user);
        validateRequiredFields(person, permanentAddress);
        Person existing = personDao.getByUKey(person.getPersonUKey());

        final Person.Status currentState = person.getStatus();
        if (currentState == Person.Status.SEMI_VERIFIED || currentState == Person.Status.UNVERIFIED) {
            person.setStatus(Person.Status.VERIFIED);
            person.setLifeStatus(Person.LifeStatus.ALIVE);
            person.setSubmittedLocation(user.getPrimaryLocation());
            final long pin = pinGenerator.generatePINNumber(person.getDateOfBirth(), person.getGender() == 0);
            person.setPin(pin);

            personDao.updatePerson(person, user);
            // update permanent address of the person to the PRS
            if (permanentAddress != null && permanentAddress.trim().length() > 0) {
                Set<Address> addresses = person.getAddresses();
                for (Address permanentAdd : addresses) {
                    if (permanentAdd.isPermanent()) {
                        permanentAdd.setLine1(permanentAddress);
                        personDao.updateAddress(permanentAdd);
                        break;
                    }
                }
            }
            // update current address of the person to the PRS
            if (currentAddress != null && currentAddress.trim().length() > 0) {
                final Address currentAdd = person.getLastAddress();
                currentAdd.setLine1(currentAddress);
                person.setLastAddress(currentAdd);
                personDao.updateAddress(currentAdd);
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

    private void handleException(String message, int code) {
        logger.error(message);
        throw new PRSRuntimeException(message, code);
    }

    private void validatePersonState(Person person, Person.Status state) {
        if (state != person.getStatus()) {
            handleException("Person with personUKey : " + person.getPersonUKey() + " , in invalid state : " +
                person.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Person with personUKey : " + person.getPersonUKey() + " , in valid state : " +
                person.getStatus());
        }
    }

    private void validateAccessOfUser(Person person, User user) {
        // TODO chathuranga
        if (person != null && user != null) {
            if (!(User.State.ACTIVE == user.getStatus()
                &&
                (Role.ROLE_RG.equals(user.getRole().getRoleId())
                    ||
                    user.isAllowedAccessToLocation(person.getSubmittedLocation().getLocationUKey())
                )
            )) {
                handleException("User : " + user.getUserId() + " is not allowed access to the Location : " +
                    person.getSubmittedLocation().getLocationUKey(), ErrorCodes.PERMISSION_DENIED);
            }
        } else {
            handleException("Person or User performing the action not complete", ErrorCodes.INVALID_DATA);
        }
    }

    private void validateAccessOfUserToEdit(Person person, User user) {
        if (person != null && user != null) {
            if (!(User.State.ACTIVE == user.getStatus() && !Role.ROLE_ADMIN.equals(user.getRole().getRoleId()))) {
                handleException("User : " + user.getUserId() + " is not allowed edit entries on the PRS",
                    ErrorCodes.PERMISSION_DENIED);
            }
        } else {
            handleException("Person or User performing the action not complete", ErrorCodes.INVALID_DATA);
        }
    }

    private void validateRequiredFields(Person person, String permanentAddress) {
        if (person.getDateOfBirth() == null || permanentAddress == null || person.getRace() == null ||
            isEmptyString(person.getPlaceOfBirth()) || isEmptyString(person.getFullNameInOfficialLanguage()) ||
            isEmptyString(person.getFullNameInEnglishLanguage()) || person.getDateOfRegistration() == null) {
            handleException("Adding person is incomplete, Check required field values", ErrorCodes.INVALID_DATA);
        }
    }

    private boolean isBlankString(String s) {
        return s != null && s.trim().length() == 0;
    }

    private static final boolean isEmptyString(String s) {
        return s == null || s.trim().length() == 0;
    }
}
