package lk.rgd.crs.core.dao;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.DeathRegister;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;


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
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<DeathRegister> findAll() {
        Query q = em.createNamedQuery("findAllDeaths");
        return q.getResultList();
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
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getHistoricalRecordsForBDDivisionAndSerialNo(BDDivision deathDivision, long serialNo, long deathId) {
        Query q = em.createNamedQuery("get.historical.death.records.by.bddivision.and.serialNo.deathID");
        q.setParameter("deathDivision", deathDivision);
        q.setParameter("serialNo", serialNo);
        q.setParameter("deathId", deathId);
        return q.getResultList();
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
    public List<DeathRegister> getByBDDivisionAndRegistrationDateRangeAndState(BDDivision deathDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, DeathRegister.State state) {

        Query q = em.createNamedQuery("get.by.division.register.date.state").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("deathDivision", deathDivision);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        q.setParameter("state", state);
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

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<DeathRegister> getDeathRegisterByDeathPersonPINorNIC(String pinOrNIC) {
        Query q = em.createNamedQuery("get.all.deaths.by.deathPersonPIN");
        q.setParameter("pinOrNIC", pinOrNIC);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public int getDeathCertificateCount(DeathRegister.State status, Date startDate, Date endDate) {
        Query q = em.createNamedQuery("get.dr.count");
        q.setParameter("status", status);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getByCreatedUser(User user, Date start, Date end) {
        Query q = em.createNamedQuery("get.dr.by.createdUser");
        q.setParameter("user", user);
        q.setParameter("startDate", start);
        q.setParameter("endDate", end);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getPaginatedDeathRegisterListByDSDivisionAndRegistrationDateRange(int dsDivisionId,
        Date startDate, Date endDate, int pageNo, int numOfRows, boolean active) {
        Query q = em.createNamedQuery("get.all.by.death.division.time.frame").setFirstResult((pageNo - 1)
            * numOfRows).setMaxResults(numOfRows);
        q.setParameter("dsDivisionId", dsDivisionId);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        q.setParameter("active", active);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getPaginatedDeathRegisterListByDSDivisionAndRegistrationDateRangeAndState(int dsDivisionId,
        Date startDate, Date endDate, int pageNo, int numOfRows, boolean active, DeathRegister.State state) {
        Query q = em.createNamedQuery("get.all.by.death.division.time.frame.state").setFirstResult((pageNo - 1)
            * numOfRows).setMaxResults(numOfRows);
        q.setParameter("dsDivisionId", dsDivisionId);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        q.setParameter("active", active);
        q.setParameter("state", state);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getDeathRegisterByDivisionAndStatusAndDate(DSDivision deathDivision, DeathRegister.State status, Date startDate, Date endDate) {
        Query q = em.createNamedQuery("get.by.division.register.date.state");
        q.setParameter("deathDivision", deathDivision);
        q.setParameter("state", status);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<DeathRegister> getDeathRecordsByRegistrarPinOrNic(long registrarPin, String registrarNic) {
        Query q = em.createNamedQuery("get.dr.by.registrarPinOrNic");
        q.setParameter("registrarPin", registrarPin);
        q.setParameter("registrarNic", registrarNic);
        return q.getResultList();
    }
}
