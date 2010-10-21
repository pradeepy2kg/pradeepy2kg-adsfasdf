package lk.rgd.crs.core.dao;

import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.crs.api.dao.BirthAlterationDAO;
import lk.rgd.crs.api.domain.BirthAlteration;
import lk.rgd.crs.api.domain.BDDivision;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import javax.persistence.Query;
import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

/**
 * @author Indunil Moremada
 */
public class BirthAlterationDAOImpl extends BaseDAO implements BirthAlterationDAO {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void addBirthAlteration(BirthAlteration ba, User user) {
        ba.getLifeCycleInfo().setCreatedTimestamp(new Date());
        ba.getLifeCycleInfo().setCreatedUser(user);
        ba.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        ba.getLifeCycleInfo().setLastUpdatedUser(user);
        ba.getLifeCycleInfo().setActiveRecord(true);
        em.persist(ba);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateBirthAlteration(BirthAlteration ba, User user) {
        ba.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        ba.getLifeCycleInfo().setLastUpdatedUser(user);
        em.merge(ba);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteBirthAlteration(long idUKey) {
        em.remove(getById(idUKey));
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public BirthAlteration getById(long idUKey) {
        logger.debug("Get BirthAlteration by ID : {}", idUKey);
        return em.find(BirthAlteration.class, idUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthAlteration> getBulkOfAlterationByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("filter.alteration.by.dsdivision").
                setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("dsDivision", dsDivision);
        q.setParameter("statusFullyApp", BirthAlteration.State.FULLY_APPROVED);
        q.setParameter("statusPrint", BirthAlteration.State.PRINTED);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthAlteration> getBulkOfAlterationByBDDivision(BDDivision BDDivision, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("filter.alteration.by.bddivision").
                setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        logger.debug("get Approval pending list from bdDivision Number is :{}", BDDivision.getDivisionId());
        q.setParameter("bdDivision", BDDivision);
        q.setParameter("statusFullyApp", BirthAlteration.State.FULLY_APPROVED);
        q.setParameter("statusPrint", BirthAlteration.State.PRINTED);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public BirthAlteration getActiveRecordByBDDivisionAndSerialNo(BDDivision bdDivision, long alterationSerialNo, boolean isAlt52_1) {
        Query q;
        if (isAlt52_1) {
            q = em.createNamedQuery("get.active.ba.by.bddivision.in.ba.and.alterationSerialNo");
        } else {
            q = em.createNamedQuery("get.active.ba.by.bddivision.in.bdf.and.alterationSerialNo");
        }
        q.setParameter("bdDivision", bdDivision);
        q.setParameter("alterationSerialNo", alterationSerialNo);
        try {
            return (BirthAlteration) q.getSingleResult();
        } catch (NoResultException e) {
            logger.debug("No any duplication with Birth Alteration Serial number with :{}", alterationSerialNo);
            return null;
        }
        // NonUniqueResultException should not occur since only one record for a serial number + BD division will be
        // marked as active at any given point in time
    }

    public List<BirthAlteration> getBulkOfAlterationByIdUKey(long idUKey, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("filter.alteration.by.idUKey").
                setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        logger.debug("get Approval pending list from idUKey number is :{}", idUKey);
        q.setParameter("idUKey", idUKey);
        q.setParameter("statusFullyApp", BirthAlteration.State.FULLY_APPROVED);
        q.setParameter("statusPrint", BirthAlteration.State.PRINTED);
        return q.getResultList();
    }

    public List<BirthAlteration> getBulkOfAlterationByRecivedDate(Date recivedDateFrom, Date recivedDateTo, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("filter.alteration.by.recived.date").
                setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        //logger.debug("get Approval pending list from recived date is :{}", recivedDate);
        q.setParameter("recivedDateFrom", recivedDateFrom);
        q.setParameter("recivedDateTo", recivedDateTo);
        q.setParameter("statusFullyApp", BirthAlteration.State.FULLY_APPROVED);
        q.setParameter("statusPrint", BirthAlteration.State.PRINTED);
        return q.getResultList();
    }

    public List<BirthAlteration> getBulkOfAlterationByBDDivisionAndAlterationSerialNo(BDDivision bdDivision, Long alterationSerialNo, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("filter.alteration.by.bdDivision.and.alteration.serial.number").
                setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        logger.debug("get Approval pending list from Alteration Serial Number  is :{}", alterationSerialNo);
        q.setParameter("bdDivision", bdDivision);
        q.setParameter("alterationSerialNo", alterationSerialNo);
        q.setParameter("statusFullyApp", BirthAlteration.State.FULLY_APPROVED);
        q.setParameter("statusPrint", BirthAlteration.State.PRINTED);
        return q.getResultList();
    }

    public List<BirthAlteration> getBulkOfAlterationByBDDivisionAndBirthSerialNo(BDDivision bdDivision, Long birthSerialNo, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("filter.alteration.by.bdDivision.and.birth.serial.number").
                setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        logger.debug("get Approval pending list from Birth Declaration Serial Number  is :{}", birthSerialNo);
        q.setParameter("bdDivision", bdDivision);
        q.setParameter("birthSerialNo", birthSerialNo);
        q.setParameter("statusFullyApp", BirthAlteration.State.FULLY_APPROVED);
        q.setParameter("statusPrint", BirthAlteration.State.PRINTED);
        return q.getResultList();
    }

    @Override
    public List<BirthAlteration> getBulkOfAlterationByUserLocationIdUKey(int locationUKey, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("filter.alteration.by.user.location").
                setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("locationUKey", locationUKey);
        q.setParameter("statusFullyApp", BirthAlteration.State.FULLY_APPROVED);
        q.setParameter("statusPrint", BirthAlteration.State.PRINTED);
        return q.getResultList();
    }
}
