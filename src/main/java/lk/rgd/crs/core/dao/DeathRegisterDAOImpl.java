package lk.rgd.crs.core.dao;

import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.DSDivision;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Date;


/**
 * @author Indunil Moremada
 */
public class DeathRegisterDAOImpl extends BaseDAO implements DeathRegisterDAO {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void addDeathRegistration(DeathRegister dr, User user) {
        dr.getLifeCycleInfo().setCreatedTimestamp(new Date());
        dr.getLifeCycleInfo().setCreatedUser(user);
        dr.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        dr.getLifeCycleInfo().setLastUpdatedUser(user);
        dr.getLifeCycleInfo().setActiveRecord(true);
        em.persist(dr);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateDeathRegistration(DeathRegister dr, User user) {
        dr.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        dr.getLifeCycleInfo().setLastUpdatedUser(user);
        em.merge(dr);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public DeathRegister getById(long deathRegisterIdUKey) {
        return em.find(DeathRegister.class, deathRegisterIdUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteDeathRegistration(DeathRegister dr, User user) {
        em.remove(dr);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getPaginatedListForState(
        BDDivision deathDivision, int pageNo, int noOfRows, DeathRegister.State status) {
        Query q = em.createNamedQuery("death.register.filter.by.and.deathDivision.status.paginated").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("deathDivision", deathDivision);
        q.setParameter("status", status);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getPaginatedListForAll(BDDivision deathDivision, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("get.all.deaths.by.deathDivision").setFirstResult((pageNo - 1)
            * noOfRows).setMaxResults(noOfRows);
        q.setParameter("deathDivision", deathDivision);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public DeathRegister getActiveRecordByBDDivisionAndDeathSerialNo(BDDivision bdDivision, long deathSerialNo) {
        Query q = em.createNamedQuery("get.active.by.bddivision.and.deathSerialNo");
        q.setParameter("deathSerialNo", deathSerialNo);
        q.setParameter("deathDivision", bdDivision);
        try {
            return (DeathRegister) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        // NonUniqueResultException should not occur since only one record for a serial number + BD division will be
        // marked as active at any given point in time
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getByBDDivisionAndRegistrationDateRange(
        BDDivision deathDivision, Date startDate, Date endDate, int pageNo, int noOfRows) {

        Query q = em.createNamedQuery("get.by.division.register.date").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("deathDivision", deathDivision);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getPaginatedListForStateByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, DeathRegister.State status) {
        Query q = em.createNamedQuery("death.register.filter.by.and.dsDivision.status.paginated").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("dsDivision", dsDivision);
        q.setParameter("status", status);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getPaginatedListForAllByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("get.all.deaths.by.dsDivision").setFirstResult((pageNo - 1)
            * noOfRows).setMaxResults(noOfRows);
        q.setParameter("dsDivision", dsDivision);
        return q.getResultList();
    }
}
