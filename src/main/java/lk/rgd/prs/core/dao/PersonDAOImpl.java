package lk.rgd.prs.core.dao;

import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.common.util.HashUtil;
import lk.rgd.prs.api.dao.PersonDAO;
import lk.rgd.prs.api.domain.Address;
import lk.rgd.prs.api.domain.Marriage;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.domain.PersonCitizenship;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Date;
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
    public void addPerson(Person person, User user) {
        person.setHash(HashUtil.hashPerson(person));
        person.getLifeCycleInfo().setCreatedTimestamp(new Date());
        person.getLifeCycleInfo().setCreatedUser(user);
        person.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        person.getLifeCycleInfo().setLastUpdatedUser(user);
        em.persist(person);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void updatePerson(Person person, User user) {
        person.setHash(HashUtil.hashPerson(person));
        person.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        person.getLifeCycleInfo().setLastUpdatedUser(user);
        em.merge(person);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void addMarriage(Marriage m) {
        em.persist(m);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void updateMarriage(Marriage m) {
        em.merge(m);
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public Address getAddressByUKey(long addressUKey) {
        return em.find(Address.class, addressUKey);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Address> getAddressesByPersonUKey(long idUKey) {
        Query q = em.createNamedQuery("addresses.by.idukey");
        q.setParameter("idUKey", idUKey);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<PersonCitizenship> getCitizenshipsByPersonUKey(long idUKey) {
        Query q = em.createNamedQuery("citizenship.by.idukey");
        q.setParameter("idUKey", idUKey);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void addAddress(Address a) {
        a.setStartDate(new Date());
        em.persist(a);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void updateAddress(Address a) {
        em.merge(a);
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
    public Person findPersonByTemporaryPIN(long temporaryPIN) {
        Query q = em.createNamedQuery("filter.by.temporaryPIN");
        q.setParameter("temporaryPin", temporaryPIN);
        try {
            return (Person) q.getSingleResult();
        } catch (NoResultException ignore) {
            return null;
        }
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

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Person> findAllChildren(Person p) {
        Query q = em.createNamedQuery("findAllChildren");
        q.setParameter("person", p);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Person> findAllSiblings(Person p) {
        Query q = em.createNamedQuery("findAllSiblings");
        q.setParameter("mother", p.getMother());
        q.setParameter("father", p.getFather());
        final List<Person> list = q.getResultList();
        list.remove(p);
        return list;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Person> findAll() {
        Query q = em.createNamedQuery("findAllPersons");
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Person> getPaginatedListByLocation(Location location, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("get.by.location").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("location", location);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Person> getByLocationAndPIN(Location location, long pin) {
        Query q = em.createNamedQuery("get.by.location.and.pin");
        q.setParameter("location", location);
        q.setParameter("pin", pin);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Person> getByLocationAndNIC(Location location, String nic) {
        Query q = em.createNamedQuery("get.by.location.and.nic");
        q.setParameter("location", location);
        q.setParameter("nic", nic);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Person> getByLocationAndTempPIN(Location location, long tempPin) {
        Query q = em.createNamedQuery("get.by.location.and.tempPin");
        q.setParameter("location", location);
        q.setParameter("tempPin", tempPin);
        return q.getResultList();
    }
}
