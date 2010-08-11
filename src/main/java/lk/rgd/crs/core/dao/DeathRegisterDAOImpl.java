package lk.rgd.crs.core.dao;

import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.common.core.dao.BaseDAO;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void addDeathRegistration(DeathRegister dr) {
        dr.setStatus(DeathRegister.State.DATA_ENTRY);
        em.persist(dr);
    }

    /**
     * @inheritDoc
     */
    @Transactional
    public void updateDeathRegistration(DeathRegister dr) {
        em.merge(dr);
    }

    /**
     * @inheritDoc
     */
    public DeathRegister getById(long deathRegisterIdUKey) {
        return em.find(DeathRegister.class, deathRegisterIdUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional
    public void deleteDeathRegistration(long deathRegistrationIdUKey) {
        DeathRegister dr = getById(deathRegistrationIdUKey);
        em.remove(dr);
    }

    /**
     * @inheritDoc
     */
    public List<DeathRegister> getPaginatedListForState(BDDivision deathDivision,int pageNo, int noOfRows, DeathRegister.State status) {
        Query q = em.createNamedQuery("death.register.filter.by.and.deathDivision.status.paginated").setFirstResult((pageNo - 1)
            * noOfRows).setMaxResults(noOfRows);
        q.setParameter("deathDivision", deathDivision);
        q.setParameter("status", status);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    public List<DeathRegister> getPaginatedListForAll(BDDivision deathDivision, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("get.all.deaths.by.deathDivision").setFirstResult((pageNo - 1)
            * noOfRows).setMaxResults(noOfRows);
        q.setParameter("deathDivision", deathDivision);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    public DeathRegister getByBDDivisionAndDeathSerialNo(BDDivision bdDivision, String deathSerialNo) {
        Query q = em.createNamedQuery("get.by.bddivision.and.deathSerialNo");
        q.setParameter("deathSerialNo", deathSerialNo);
        q.setParameter("deathDivision", bdDivision);
        return (DeathRegister) q.getSingleResult();
    }

    /**
     * @inheritDoc
     */
    public List<DeathRegister> getByBDDivisionAndRegistrationDateRange(BDDivision deathDivision,
                                                                       Date startDate, Date endDate, int pageNo, int noOfRows){
        Query q = em.createNamedQuery("get.by.division.register.date").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("deathDivision", deathDivision);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return q.getResultList();
    }
}
