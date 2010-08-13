package lk.rgd.crs.core.dao;

import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.common.core.dao.BaseDAO;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;


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
        DeathRegister dr = null;
        try {
            dr = em.find(DeathRegister.class, deathRegisterIdUKey);
        } catch (NoResultException e) {
            logger.error("No result found for the requested IdUKey", e);
        }
        return dr;
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
    public List<DeathRegister> getPaginatedListForState(BDDivision deathDivision, int pageNo, int noOfRows, DeathRegister.State status) {
        List<DeathRegister> resultList;
        Query q = em.createNamedQuery("death.register.filter.by.and.deathDivision.status.paginated").setFirstResult((pageNo - 1)
            * noOfRows).setMaxResults(noOfRows);
        q.setParameter("deathDivision", deathDivision);
        q.setParameter("status", status);
        try {
            resultList = q.getResultList();
        } catch (NoResultException e) {
            logger.error("No result found for the paginated list with the given state", e);
            resultList = new ArrayList();
        }
        return resultList;
    }

    /**
     * @inheritDoc
     */
    public List<DeathRegister> getPaginatedListForAll(BDDivision deathDivision, int pageNo, int noOfRows) {
        List<DeathRegister> resultList;
        Query q = em.createNamedQuery("get.all.deaths.by.deathDivision").setFirstResult((pageNo - 1)
            * noOfRows).setMaxResults(noOfRows);
        q.setParameter("deathDivision", deathDivision);
        try {
            resultList = q.getResultList();
        } catch (NoResultException e) {
            logger.error("No death result found", e);
            resultList = new ArrayList();
        }
        return resultList;
    }

    /**
     * @inheritDoc
     */
    public DeathRegister getByBDDivisionAndDeathSerialNo(BDDivision bdDivision, long deathSerialNo) {
        DeathRegister dr = null;
        Query q = em.createNamedQuery("get.by.bddivision.and.deathSerialNo");
        q.setParameter("deathSerialNo", deathSerialNo);
        q.setParameter("deathDivision", bdDivision);
        try {
            dr = (DeathRegister) q.getSingleResult();
        } catch (NoResultException e) {
            logger.error("No result for the given bdDivision and serialNo", e);
        }
        return dr;
    }

    /**
     * @inheritDoc
     */
    public List<DeathRegister> getByBDDivisionAndRegistrationDateRange(BDDivision deathDivision,
                                                                       Date startDate, Date endDate, int pageNo, int noOfRows) {
        List<DeathRegister> resultList;
        Query q = em.createNamedQuery("get.by.division.register.date").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("deathDivision", deathDivision);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        try {
            resultList = q.getResultList();
        } catch (NoResultException e) {
            logger.error("No death result found for the given date range and bdDivision", e);
            resultList = new ArrayList();
        }
        return resultList;
    }
}
