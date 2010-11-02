package lk.rgd.prs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.Auditable;
import lk.rgd.common.core.index.SolrIndexManager;
import lk.rgd.common.api.domain.User;
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
        personDao.addPerson(person);
        return pin;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Person> addExistingPerson(Person person, String permanentAddress, String currentAddress, User user,
        List<PersonCitizenship> citizenshipList) {
        long pin = -1;

        // TODO validate inputs. NIC, permanent and current address, DOB
        if (!user.isAuthorized(Permission.PRS_LOOKUP_PERSON_BY_KEYS)) {
            handleException("User : " + user.getUserId() + " is not allowed to lookup entries on the PRS by keys (nic/temporaryPIN)",
                ErrorCodes.PRS_LOOKUP_BY_KEYS_DENIED);
        }

        if (!user.isAuthorized(Permission.PRS_ADD_PERSON)) {
            handleException("User : " + user.getUserId() + " is not allowed to add entries to the PRS",
                ErrorCodes.PRS_ADD_RECORD_DENIED);
        }

        final List<Person> exactRecord = new ArrayList<Person>();
        if (person.getTemporaryPin() != null) {
            exactRecord.add(personDao.findPersonByTemporaryPIN(person.getTemporaryPin()));
        } else if (person.getNic() != null) {
            for (Person p : personDao.findPersonsByNIC(person.getNic())) {
                exactRecord.add(p);
            }
        }
        if (!exactRecord.isEmpty()) {
            return exactRecord;
        }

        if (exactRecord.isEmpty()) {
            person.setStatus(Person.Status.VERIFIED);
            person.setLifeStatus(Person.LifeStatus.ALIVE);
            // generate a PIN for existing person
            pin = pinGenerator.generatePINNumber(person.getDateOfBirth(), person.getGender() == 0);
            person.setPin(pin);

            personDao.addPerson(person);
            // add permanent address of the person to the PRS
            if (permanentAddress != null && permanentAddress.trim().length() > 0) {
                final Address permanentAdd = new Address(permanentAddress);
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
                personDao.updatePerson(person);
            }
            // add citizenship list of the person to the PRS
            if (citizenshipList != null) {
                for (PersonCitizenship pc : citizenshipList) {
                    pc.setPerson(person);
                    citizenshipDAO.addCitizenship(pc, user);
                }
            }
        }
        return Collections.emptyList();
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
        personDao.updatePerson(person);
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

    private boolean isBlankString(String s) {
        return s != null && s.trim().length() == 0;
    }
}
