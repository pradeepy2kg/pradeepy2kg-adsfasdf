package lk.rgd.prs.core.service;

import lk.rgd.Permission;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.crs.ErrorCodes;
import lk.rgd.prs.PRSRuntimeException;
import lk.rgd.prs.api.dao.PersonDAO;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * This is the main service interface for the PRS
 *
 * @author asankha
 *
 * TODO Autit calls and log metrics of use
 */
public class PopulationRegistryImpl implements PopulationRegistry {

    private static final Logger logger = LoggerFactory.getLogger(PopulationRegistryImpl.class);

    private static final DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");

    private final PersonDAO personDao;

    public PopulationRegistryImpl(PersonDAO personDao) {
        this.personDao = personDao;
    }

    /**
     * @inheritDoc
     */
    public void addPerson(Person person, User user) {
        if (user.isAuthorized(Permission.PRS_ADD_PERSON)) {
            personDao.addPerson(person);
        } else {
            logger.error("User : " + user.getUserId() + " is not allowed to add persons to the PRS");
            throw new PRSRuntimeException("User : " + user.getUserId() +
                " is not allowed to add entries to the PRS", ErrorCodes.PRS_ADD_RECORD_DENIED);
        }
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
            long pin = Long.parseLong(pinOrNic);
            // this is a pin
            return findPersonByPIN(pin, user);
        } catch (NumberFormatException ignore) {
            // this is a nic, if multiple rows match, just return the first
            List<Person> results = findPersonsByNIC(pinOrNic, user);
            if (results!= null && !results.isEmpty()) {
                return results.get(0);
            }
        }
        return null;
    }
}
