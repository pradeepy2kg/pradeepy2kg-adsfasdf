package lk.rgd.prs.core.dao;

import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.prs.api.dao.PersonDAO;
import lk.rgd.prs.api.domain.Person;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

/**
 * The Person DAO should not be used for most of the queries as we do not want the DB to be loaded. Instead, a
 * record should be first searched for through the Lucene index, and the row numbers found. Subsequently, the user
 * may request for complete details of these objects via the DB - using the optimal unique key lookup 
 *
 * @author asankha
 */
public class PersonDAOImpl extends BaseDAO implements PersonDAO {

    @Transactional(propagation = Propagation.MANDATORY)
    public void addPerson(Person person) {
        em.persist(person);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void updatePerson(Person person) {
        em.merge(person);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Person getByUKey(long personUKey) {
        return em.find(Person.class, personUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Person findPersonByPIN(long pin) {
        Query q = em.createNamedQuery("filter.by.pin");
        q.setParameter("pin", pin);
        try {
            return (Person) q.getSingleResult();
        } catch (NoResultException ignore) {
            return null;
        }
        // A NonUniqueResultException will not be thrown since pin is a unique column
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Person> findPersonsByNIC(String nic) {
        Query q = em.createNamedQuery("filter.by.nic");
        q.setParameter("nic", nic);
        return q.getResultList();
    }
}
