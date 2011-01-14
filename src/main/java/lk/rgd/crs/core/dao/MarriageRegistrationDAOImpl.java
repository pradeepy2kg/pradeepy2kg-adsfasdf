package lk.rgd.crs.core.dao;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.crs.api.dao.MarriageRegistrationDAO;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.MarriageRegister;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.EnumSet;

/**
 * @author amith jayasekara
 */
public class MarriageRegistrationDAOImpl extends BaseDAO implements MarriageRegistrationDAO {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void addMarriageRegister(MarriageRegister marriageRegister, User user) {
        marriageRegister.getLifeCycleInfo().setCreatedTimestamp(new Date());
        marriageRegister.getLifeCycleInfo().setCreatedUser(user);
        marriageRegister.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        marriageRegister.getLifeCycleInfo().setLastUpdatedUser(user);
        em.persist(marriageRegister);
    }

    /**
     * @inheriteDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public MarriageRegister getByIdUKey(long idUKey) {
        return em.find(MarriageRegister.class, idUKey);
    }

    /**
     * @inheriteDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateMarriageRegister(MarriageRegister marriageRegister, User user) {
        marriageRegister.getLifeCycleInfo().setCreatedTimestamp(new Date());
        marriageRegister.getLifeCycleInfo().setCreatedUser(user);
        marriageRegister.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        marriageRegister.getLifeCycleInfo().setLastUpdatedUser(user);
        em.merge(marriageRegister);
    }

    /**
     * @inheriteDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public MarriageRegister getActiveRecordByMRDivisionAndSerialNo(MRDivision mrDivision, long serialNo) {
        Query q = em.createNamedQuery("get.active.by.mrDivision.and.serialNo");
        q.setParameter("mrDivision", mrDivision);
        q.setParameter("serialNo", serialNo);
        try {
            return (MarriageRegister) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        // NonUniqueResultException cannot occur since serial number unique for given MRDivision
    }

    /**
     * @inheriteDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<MarriageRegister> getNoticeByMRDivisionAndSerialNo(MRDivision mrDivision,
        long serialNo, boolean active) {
        Query q = em.createNamedQuery("get.notice.by.mrDivision.and.serial");
        q.setParameter("mrDivision", mrDivision);
        q.setParameter("serialNo", serialNo);
        q.setParameter("active", active);
        return q.getResultList();
    }

    /**
     * @inheriteDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<MarriageRegister> getByMRDivisionAndSerialNo(MRDivision mrDivision, MarriageRegister.State state,
        long serialNo, boolean active) {
        Query q = em.createNamedQuery("get.register.by.mrDivision.and.serial");
        q.setParameter("mrDivision", mrDivision);
        q.setParameter("serialNo", serialNo);
        q.setParameter("state", state);
        q.setParameter("active", active);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getPaginatedListByDistrict(District district, int pageNo, int noOfRows,
        boolean active) {
        Query q = em.createNamedQuery("filter.notice.by.district");
        q.setParameter("district", district);
        q.setParameter("active", active);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getPaginatedNoticeListByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows,
        boolean active) {
        Query q = em.createNamedQuery("filter.notice.by.dsDivision").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("dsDivision", dsDivision);
        q.setParameter("active", active);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getPaginatedListForStateByDSDivision(DSDivision dsDivision,
        MarriageRegister.State state, int pageNo, int noOfRows, boolean active) {
        Query q = em.createNamedQuery("filter.by.dsDivision.and.state").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("dsDivision", dsDivision);
        q.setParameter("state", state);
        q.setParameter("active", active);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getPaginatedListByDistrict(District district,
        MarriageRegister.State state, int pageNo, int noOfRows, boolean active) {
        Query q = em.createNamedQuery("findMarriageRegisterByDistrict").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("district", district);
        q.setParameter("state", state);
        q.setParameter("active", active);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getPaginatedListByDistrictAndDate(District district,
        MarriageRegister.State state, int pageNo, int noOfRows, boolean active,
        Date startDate, Date endDate) {
        Query q = em.createNamedQuery("findMarriageRegisterByDistrictAndDate").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("district", district);
        q.setParameter("state", state);
        q.setParameter("active", active);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getPaginatedMarriageRegisterListByState(MarriageRegister.State state, int pageNo,
        int noOfRows, boolean active) {
        Query q = em.createNamedQuery("findMarriageRegisterByState").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("state", state);
        q.setParameter("active", active);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getPaginatedMarriageRegisterList(EnumSet stateList, int pageNo,
        int noOfRows, boolean isActive) {
        Query q = em.createNamedQuery("findMarriageRegister").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("active", isActive);
        q.setParameter("stateList", stateList);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getPaginatedMarriageRegisterListByDSDivision(DSDivision dsDivision, EnumSet stateList, int pageNo,
        int noOfRows, boolean isActive) {
        Query q = em.createNamedQuery("findMarriageRegisterByDSDivision").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("active", isActive);
        q.setParameter("stateList", stateList);
        q.setParameter("dsDivision", dsDivision);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getPaginatedMarriageRegisterListByDistricts(District districtList, EnumSet stateList, int pageNo,
        int noOfRows, boolean isActive) {
        Query q = em.createNamedQuery("findMarriageRegisterByDistricts").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("active", isActive);
        q.setParameter("stateList", stateList);
        q.setParameter("districtList", districtList);
        return q.getResultList();
    }


    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getPaginatedNoticeListByMRDivision(MRDivision mrDivision, int pageNo, int noOfRows,
        boolean active) {
        Query q = em.createNamedQuery("filter.notice.by.mrDivision").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("mrDivision", mrDivision);
        q.setParameter("active", active);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getPaginatedListForStateByMRDivision(MRDivision mrDivision,
        MarriageRegister.State state, int pageNo, int noOfRows, boolean active) {
        Query q = em.createNamedQuery("filter.by.mrDivision.and.state").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("mrDivision", mrDivision);
        q.setParameter("state", state);
        q.setParameter("active", active);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getNoticeByPINorNIC(String id, boolean active) {
        Query q = em.createNamedQuery("filter.notice.by.pinOrNic");
        q.setParameter("id", id);
        q.setParameter("active", active);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getByStateAndPINorNIC(MarriageRegister.State state, String id, boolean active) {
        Query q = em.createNamedQuery("filter.by.pinOrNic.and.state");
        q.setParameter("id", id);
        q.setParameter("state", state);
        q.setParameter("active", active);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getPaginatedNoticesByMRDivisionAndRegisterDateRange(MRDivision mrDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, boolean active) {
        Query q = em.createNamedQuery("get.notice.by.mrDivision.and.registerDate").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("mrDivision", mrDivision);
        q.setParameter("active", active);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getPaginatedNoticesByDSDivisionAndRegisterDateRange(DSDivision dsDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, boolean active) {
        Query q = em.createNamedQuery("get.notice.by.dsDivision.and.registerDate").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("dsDivision", dsDivision);
        q.setParameter("active", active);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<MarriageRegister> getActiveMarriageNoticeByMaleFemaleIdentification(String maleIdentification,
        String femaleIdentification) {
        Query q = em.createNamedQuery("get.notice.by.male.and.female.identification");
        q.setParameter("male", maleIdentification);
        q.setParameter("female", femaleIdentification);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteMarriageRegister(long idUKey) {
        em.remove(getByIdUKey(idUKey));
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public int getMarriageCertificateCount(MarriageRegister.State status, Date startDate, Date endDate) {
        // TODO Correct Query not supplied
        Query q = em.createNamedQuery("get.notice.by.male.and.female.identification");
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getByCreatedUser(User user, Date start, Date end) {
        Query q = em.createNamedQuery("get.mr.by.createdUser");
        q.setParameter("user", user);
        q.setParameter("startDate", start);
        q.setParameter("endDate", end);
        return q.getResultList();
    }
}
