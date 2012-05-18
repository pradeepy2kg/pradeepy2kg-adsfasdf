package lk.rgd.crs.core.dao;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthDeclaration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * @author asankha
 */
public class BirthDeclarationDAOImpl extends BaseDAO implements BirthDeclarationDAO {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void addBirthDeclaration(BirthDeclaration bdf, User user) {
        bdf.getLifeCycleInfo().setCreatedTimestamp(new Date());
        bdf.getLifeCycleInfo().setCreatedUser(user);
        bdf.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        bdf.getLifeCycleInfo().setLastUpdatedUser(user);
        bdf.getLifeCycleInfo().setActiveRecord(true);
        em.persist(bdf);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    // this is not Mandatory as unit tests directly invoke this
    public void updateBirthDeclaration(BirthDeclaration bdf, User user) {
        bdf.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        bdf.getLifeCycleInfo().setLastUpdatedUser(user);
        em.merge(bdf);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteBirthDeclaration(long idUKey) {
        em.remove(getById(idUKey));
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<BirthDeclaration> findAll() {
        Query q = em.createNamedQuery("findAll");
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getConfirmationPrintPending(BDDivision birthDivision,
        int pageNo, int noOfRows, boolean printed) {
        Query q = em.createNamedQuery("filter.by.division.and.status").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("birthDivision", birthDivision);
        q.setParameter("status", printed ? BirthDeclaration.State.APPROVED : BirthDeclaration.State.DATA_ENTRY);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getPaginatedListForState(BDDivision birthDivision, int pageNo, int noOfRows,
        BirthDeclaration.State status) {
        Query q = em.createNamedQuery("filter.by.division.and.status").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("birthDivision", birthDivision);
        q.setParameter("status", status);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getPaginatedListForStateAndBirthType(BDDivision birthDivision, int pageNo,
        int noOfRows, BirthDeclaration.State status, BirthDeclaration.BirthType birthType) {
        Query q = em.createNamedQuery("filter.by.division.status.and.birthType").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("birthDivision", birthDivision);
        q.setParameter("status", status);
        q.setParameter("birthType", birthType);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public BirthDeclaration getById(long bdfidUKey) {
        logger.debug("Get BDF by ID : {}", bdfidUKey);
        return em.find(BirthDeclaration.class, bdfidUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<BirthDeclaration> getByDOBRangeandMotherNICorPIN(Date start, Date end, String motherNICorPIN) {
        Query q = em.createNamedQuery("get.by.dateOfBirth_range.and.motherNICorPIN");
        q.setParameter("start", start, TemporalType.DATE);
        q.setParameter("end", end, TemporalType.DATE);
        q.setParameter("motherNICorPIN", motherNICorPIN);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public BirthDeclaration getActiveRecordByBDDivisionAndSerialNo(BDDivision bdDivision, long bdfSerialNo) {
        Query q = em.createNamedQuery("get.active.by.bddivision.and.serialNo");
        q.setParameter("birthDivision", bdDivision);
        q.setParameter("bdfSerialNo", bdfSerialNo);
        try {
            return (BirthDeclaration) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        // NonUniqueResultException should not occur since only one record for a serial number + BD division will be
        // marked as active at any given point in time
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public BirthDeclaration getActiveRecordBySerialNo(long bdfSerialNo, BirthDeclaration.State state) {
        Query q = em.createNamedQuery("findBirthDeclarationBySerialNo");
        q.setParameter("status", state);
        q.setParameter("bdfSerialNo", bdfSerialNo);
        try {
            return (BirthDeclaration) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getByBDDivisionStatusAndRegisterDateRange(BDDivision birthDivision,
        BirthDeclaration.State status, Date startDate, Date endDate, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("get.by.division.status.register.date").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("birthDivision", birthDivision);
        q.setParameter("status", status);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getByBDDivisionStatusBirthTypeAndRegisterDateRange(BDDivision birthDivision,
        BirthDeclaration.State status, BirthDeclaration.BirthType birthType, Date startDate, Date endDate, int pageNo,
        int noOfRows) {
        Query q = em.createNamedQuery("get.by.division.status.birthType.register.date").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("birthDivision", birthDivision);
        q.setParameter("status", status);
        q.setParameter("birthType", birthType);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getByBDDivisionStatusAndConfirmationReceiveDateRange(BDDivision birthDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("get.by.division.status.confirmation.receive.date").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("birthDivision", birthDivision);
        q.setParameter("status", BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getByBirthDivision(BDDivision birthDivision) {
        Query q = em.createNamedQuery("get.by.bddivision");
        q.setParameter("birthDivision", birthDivision);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getUnconfirmedByRegistrationDate(Date date) {
        Query q = em.createNamedQuery("filter.by.unconfirmed.by.register.date");
        q.setParameter("date", date);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getHistoricalRecordsForBDDivisionAndSerialNo(BDDivision birthDivision, long serialNo) {
        Query q = em.createNamedQuery("get.historical.records.by.bddivision.and.serialNo");
        q.setParameter("birthDivision", birthDivision);
        q.setParameter("bdfSerialNo", serialNo);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getHistoricalAlterationRecordForBDDivisionAndSerialNo(BDDivision birthDivision,
        long serialNo, long idUKey) {
        Query q = em.createNamedQuery("get.historical.alteration.records.by.bdDivision.and.serialNo");
        q.setParameter("birthDivision", birthDivision.getBdDivisionUKey());
        q.setParameter("bdfSerialNo", serialNo);
        q.setParameter("idUKey", idUKey);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getPaginatedListForStateByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows,
        BirthDeclaration.State status) {
        Query q = em.createNamedQuery("filter.by.dsdivision.and.status").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("dsDivision", dsDivision);
        q.setParameter("status", status);
        return q.getResultList();
    }


    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getPaginatedListForStateByDistrict(District district, int pageNo, int noOfRows,
        BirthDeclaration.State state) {
        Query q = em.createNamedQuery("filter.by.district.and.status").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("district", district);
        q.setParameter("status", BirthDeclaration.State.DATA_ENTRY);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getPaginatedListForStateAndBirthTypeByDSDivision(DSDivision dsDivision, int pageNo,
        int noOfRows, BirthDeclaration.State status, BirthDeclaration.BirthType birthType) {
        Query q = em.createNamedQuery("filter.by.dsdivision.status.and.birthType").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("dsDivision", dsDivision);
        q.setParameter("status", status);
        q.setParameter("birthType", birthType);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getByDSDivisionStatusAndRegisterDateRange(DSDivision dsDivision,
        BirthDeclaration.State status, Date startDate, Date endDate, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("get.by.dsdivision.status.register.date").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("dsDivision", dsDivision);
        q.setParameter("status", status);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getByDSDivisionStatusBirthTypeAndRegisterDateRange(DSDivision dsDivision,
        BirthDeclaration.State status, BirthDeclaration.BirthType birthType, Date startDate, Date endDate, int pageNo,
        int noOfRows) {
        Query q = em.createNamedQuery("get.by.dsdivision.status.birthType.register.date").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("dsDivision", dsDivision);
        q.setParameter("status", status);
        q.setParameter("birthType", birthType);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getByDSDivisionStatusAndConfirmationReceiveDateRange(DSDivision dsDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("get.by.dsdivision.status.confirmation.receive.date").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("dsDivision", dsDivision);
        q.setParameter("status", BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getByDSDivision(DSDivision dsDivision) {
        Query q = em.createNamedQuery("get.by.dsdivision");
        q.setParameter("dsDivision", dsDivision);
        return q.getResultList();
    }

    public BirthDeclaration getByPINorNIC(long PINorNIC) {
        Query q = em.createNamedQuery("get.by.NicOrPin");
        q.setParameter("PINorNIC", PINorNIC);
        try {
            return (BirthDeclaration) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getByDSDivisionAndStatusAndBirthDateRange(DSDivision dsDivision,
        Date startDate, Date endDate, BirthDeclaration.State status) {
        Query q = em.createNamedQuery("get.by.dsdivision.and.status");
        q.setParameter("dsDivision", dsDivision);
        q.setParameter("status", status);
        q.setParameter("start", startDate);
        q.setParameter("end", endDate);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public int getBirthCertificateCount(BirthDeclaration.State status, Date startDate, Date endDate) {
        Query q = em.createNamedQuery("get.bc.count");
        q.setParameter("status", status);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getByCreatedUser(User user, Date start, Date end) {
        Query q = em.createNamedQuery("get.bc.by.createdUser");
        q.setParameter("user", user);
        q.setParameter("startDate", start);
        q.setParameter("endDate", end);
        return q.getResultList();
    }

    @Override
    public List<BirthDeclaration> getByCreatedUser(User user, Date start, Date end, int districtId) {
        Query q = em.createNamedQuery("get.bc.by.createdUser.district");
        q.setParameter("user", user);
        q.setParameter("startDate", start);
        q.setParameter("endDate", end);
        q.setParameter("districtId", districtId);
        return q.getResultList();
    }

    @Override
    public List<BirthDeclaration> getByCreatedUser(User user, Date start, Date end, int districtId, int dsDivisionId) {
        Query q = em.createNamedQuery("get.bc.by.createdUser.dsDivision");
        q.setParameter("user", user);
        q.setParameter("startDate", start);
        q.setParameter("endDate", end);
        q.setParameter("dsDivisionId", dsDivisionId);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getActiveBirthRecordByDSDivisionAndSerialNumber(long serialNumber, int dsDivisionId) {
        Query q = em.createNamedQuery("get.by.serial.and.ds.id");
        q.setParameter("serial", serialNumber);
        q.setParameter("dsId", dsDivisionId);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<BirthDeclaration> getListOfLiveBirthsForGivenMother(String motherIdentification) {
        Query q = em.createNamedQuery("get.bdf.by.mother");
        q.setParameter("mother", motherIdentification);
        q.setParameter("type", BirthDeclaration.BirthType.LIVE);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<BirthDeclaration> getBirthsByRegistrarPinOrNicAndDivision(String registrarPin, String registrarNic,
        int bdDivisionUKey) {
        Query q = em.createNamedQuery("get.bdf.by.division.registrarPinOrNic");
        q.setParameter("birthDivision", bdDivisionUKey);
        q.setParameter("registrarPin", registrarPin);
        q.setParameter("registrarNic", registrarNic);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Long findDSDivisionUsageInBirthRecords(int dsDivisionUKey) {
        Query q = em.createNamedQuery("count.birth.dsDivision.usage");
        q.setParameter("dsDivisionId", dsDivisionUKey);
        return (Long) q.getSingleResult();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Long findGNDivisionUsageInBirthRecords(int gnDivisionUKey) {
        Query q = em.createNamedQuery("count.birth.gnDivision.usage");
        q.setParameter("gnDivisionId", gnDivisionUKey);
        return (Long) q.getSingleResult();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Long findBDDivisionUsageInBirthRecords(int bdDivisionUKey) {
        Query q = em.createNamedQuery("count.birth.bdDivision.usage");
        q.setParameter("bdDivisionId", bdDivisionUKey);
        return (Long) q.getSingleResult();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Long findLocationUsageInBirthRecords(int locationUKey) {
        Query q = em.createNamedQuery("count.birth.location.usage");
        q.setParameter("locationId", locationUKey);
        return (Long) q.getSingleResult();
    }
}
