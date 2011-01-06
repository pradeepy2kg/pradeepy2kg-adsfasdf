package lk.rgd.prs.core.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.prs.api.dao.PersonCitizenshipDAO;
import lk.rgd.prs.api.domain.PersonCitizenship;
import lk.rgd.prs.api.domain.PersonCitizenshipID;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * @author Chathuranga Withana
 */
public class PersonCitizenshipDAOImpl extends BaseDAO implements PersonCitizenshipDAO {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PersonCitizenship getPersonCitizenship(long personUKey, int countryId, String passportNo) {
        return em.find(PersonCitizenship.class, new PersonCitizenshipID(personUKey, countryId, passportNo));
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void addCitizenship(PersonCitizenship citizenship, User user) {
        citizenship.getPerson().getCountries().add(citizenship);
        citizenship.getLifeCycleInfo().setCreatedUser(user);
        citizenship.getLifeCycleInfo().setCreatedTimestamp(new Date());
        citizenship.getLifeCycleInfo().setLastUpdatedUser(user);
        citizenship.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        em.persist(citizenship);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateCitizenship(PersonCitizenship citizenship, User user) {
        PersonCitizenship existing = em.find(PersonCitizenship.class,
            new PersonCitizenshipID(citizenship.getPersonUKey(), citizenship.getCountryId(), citizenship.getPassportNo()));
        if (existing != null) {
            existing.setPassportNo(citizenship.getPassportNo());
            existing.getLifeCycleInfo().setLastUpdatedUser(user);
            existing.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
            em.merge(existing);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteCitizenship(long personUKey, int countryId, String passportNo) {
        em.remove(em.find(PersonCitizenship.class, new PersonCitizenshipID(personUKey, countryId, passportNo)));
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public List<PersonCitizenship> getCitizenshipsByPersonId(long personUKey) {
        Query q = em.createNamedQuery("get.citizenship.by.personId");
        q.setParameter("personUKey", personUKey);
        return q.getResultList();
    }
}
