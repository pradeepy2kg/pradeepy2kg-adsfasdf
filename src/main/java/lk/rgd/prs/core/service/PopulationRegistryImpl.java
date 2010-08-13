package lk.rgd.prs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.common.api.domain.User;
import lk.rgd.prs.PRSRuntimeException;
import lk.rgd.prs.api.dao.PersonDAO;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PINGenerator;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

    public PopulationRegistryImpl(PersonDAO personDao, PINGenerator pinGenerator) {
        this.personDao = personDao;
        this.pinGenerator = pinGenerator;
    }

    /**
     * @inheritDoc
     */
    public long addPerson(Person person, User user) {
        long pin = -1;

        if (user.isAuthorized(Permission.PRS_ADD_PERSON)) {
            // generate a PIN for a verified record
            if (person.getStatus() == Person.Status.VERIFIED) {
                pin = pinGenerator.generatePINNumber(person.getDateOfBirth(), person.getGender() == 0);
                person.setPin(pin);
            }
            personDao.addPerson(person);
        } else {
            logger.error("User : " + user.getUserId() + " is not allowed to add persons to the PRS");
            throw new PRSRuntimeException("User : " + user.getUserId() +
                " is not allowed to add entries to the PRS", ErrorCodes.PRS_ADD_RECORD_DENIED);
        }
        return pin;
    }

    /**
     * @inheritDoc
     */
    public void updatePerson(Person person, User user) {
        if (user.isAuthorized(Permission.PRS_ADD_PERSON)) {
            personDao.updatePerson(person);
        } else {
            logger.error("User : " + user.getUserId() + " is not allowed to update persons on the PRS");
            throw new PRSRuntimeException("User : " + user.getUserId() +
                " is not allowed to update entries on the PRS", ErrorCodes.PRS_ADD_RECORD_DENIED);
        }
    }

    /**
     * @inheritDoc
     */
    public Person getByUKey(long personUKey, User user) {
        if (user.isAuthorized(Permission.PRS_LOOKUP_PERSON_BY_KEYS)) {
            return personDao.getByUKey(personUKey);
        } else {
            logger.error("User : " + user.getUserId() + " is not allowed to lookup persons on the PRS by keys (uKey)");
            throw new PRSRuntimeException("User : " + user.getUserId() +
                " is not allowed to lookup entries on the PRS by keys (uKey)", ErrorCodes.PRS_LOOKUP_BY_KEYS_DENIED);
        }
    }

    /**
     * @inheritDoc
     */
    public Person findPersonByPIN(long pin, User user) {
        if (user.isAuthorized(Permission.PRS_LOOKUP_PERSON_BY_KEYS)) {
            return personDao.findPersonByPIN(pin);
        } else {
            logger.error("User : " + user.getUserId() + " is not allowed to lookup persons on the PRS by keys (pin)");
            throw new PRSRuntimeException("User : " + user.getUserId() +
                " is not allowed to lookup entries on the PRS by keys (pin)", ErrorCodes.PRS_LOOKUP_BY_KEYS_DENIED);
        }
    }

    /**
     * @inheritDoc
     */
    public List<Person> findPersonsByNIC(String nic, User user) {
        if (user.isAuthorized(Permission.PRS_LOOKUP_PERSON_BY_KEYS)) {
            return personDao.findPersonsByNIC(nic);
        } else {
            logger.error("User : " + user.getUserId() + " is not allowed to lookup persons on the PRS by keys (nic)");
            throw new PRSRuntimeException("User : " + user.getUserId() +
                " is not allowed to lookup entries on the PRS by keys (nic)", ErrorCodes.PRS_LOOKUP_BY_KEYS_DENIED);
        }
    }

    /**
     * @inheritDoc
     */
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

    private boolean isBlankString(String s) {
        return s != null && s.trim().length() == 0;
    }
}
