package lk.rgd.crs.core.dao;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.crs.api.dao.MarriageRegistrationDAO;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.MarriageRegister;
import lk.rgd.AppConstants;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.*;

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
    public MarriageRegister getMarriageRegisterByIdUKeyAndState(long idUKey, EnumSet<MarriageRegister.State> stateList) {
        Query q = em.createNamedQuery("getMarriageRegisterByIdUKeyAndState");
        q.setParameter("idUKey", idUKey);
        q.setParameter("stateList", stateList);
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return (MarriageRegister) q.getSingleResult();
        }
    }

    /**
     * @inheriteDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateMarriageRegister(MarriageRegister marriageRegister, User user) {
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
    public List<MarriageRegister> getPaginatedListByDistrictAndDateRange(District district, Date startDate, Date endDate,
        int pageNo, int noOfRows, boolean active) {
        Query q = em.createNamedQuery("filter.notice.by.district.date.range");
        q.setParameter("district", district);
        q.setParameter("active", active);
        // q.setParameter("startDate", startDate);
        // q.setParameter("endDate", endDate);
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
    //TODO : to be removed
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
    public List<MarriageRegister> getPaginatedMarriageRegisterList(String divisionType, int divisionUKey, Set<DSDivision> dsDivisionList,
        EnumSet stateList, boolean isActive, Date startDate, Date endDate, int pageNo, int noOfRows) {

        StringBuilder query = new StringBuilder("").append("SELECT mr FROM MarriageRegister mr " +
            "WHERE mr.lifeCycleInfo.activeRecord = :active");

        //query.append(" AND (mr.mrDivision IS NULL OR ");

        String mrDivisionEquals = "";

        if (AppConstants.MARRIAGE.equals(divisionType)) {
            mrDivisionEquals = "mr.mrDivision.mrDivisionUKey = :divisionUKey)";

        } else if (AppConstants.DS_DIVISION.equals(divisionType)) {
            mrDivisionEquals = "mr.mrDivision.dsDivision.dsDivisionUKey = :divisionUKey)";

        } else if (AppConstants.DISTRICT.equals(divisionType)) {
            mrDivisionEquals = "mr.mrDivision.dsDivision.district.districtUKey = :divisionUKey)";

        } else if (AppConstants.ALL.equals(divisionType)) {
            mrDivisionEquals = "mr.mrDivision.dsDivision IN (:dsDivisionList))";
        }

        if (!mrDivisionEquals.isEmpty()) {
            query.append(" AND (mr.mrDivision IS NOT NULL AND ");
            query.append(mrDivisionEquals);
            //query.append(" OR mr.mrDivision IS EMPTY)");
        }

        query.append(" AND mr.state IN (:stateList) ")
            .append((startDate != null & endDate != null) ? "AND (mr.dateOfMarriage IS NOT NULL AND mr.dateOfMarriage " +
                "BETWEEN :startDate AND :endDate) " : " ")
            .append("ORDER BY mr.idUKey DESC ");

        Query q = em.createQuery(query.toString()).setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);

        q.setParameter("active", isActive);
        q.setParameter("stateList", stateList);

        if (divisionUKey != 0) {
            q.setParameter("divisionUKey", divisionUKey);
        }
        if (dsDivisionList != null) {
            q.setParameter("dsDivisionList", dsDivisionList);
        }
        if (startDate != null & endDate != null) {
            q.setParameter("startDate", startDate);
            q.setParameter("endDate", endDate);
        }
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getPaginatedMarriageRegisterListByDistricts(Set<District> districtList, EnumSet stateList, int pageNo,
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
    public List<MarriageRegister> getMarriageRegisterBySerialNumber(long serialNumber,
        EnumSet<MarriageRegister.State> stateList, Set<DSDivision> dsDivisionList) {

        StringBuilder query = new StringBuilder("").append("SELECT mr FROM MarriageRegister mr " +
            "WHERE mr.serialNumber = :serialNumber " +
            "AND mr.state IN (:stateList) ");
        if (dsDivisionList != null) {
            query.append("AND mr.mrDivision.dsDivision IS NOT NULL AND mr.mrDivision.dsDivision IN (:dsDivisionList) ");
        }
        query.append("AND mr.lifeCycleInfo.activeRecord IS TRUE ORDER BY mr.idUKey DESC");

        Query q = em.createQuery(query.toString());
        q.setParameter("serialNumber", serialNumber);
        q.setParameter("stateList", stateList);
        if (dsDivisionList != null) {
            q.setParameter("dsDivisionList", dsDivisionList);
        }
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getMarriageRegisterByIdNumber(String id,
        EnumSet<MarriageRegister.State> stateList, Set<DSDivision> dsDivisionList) {

        StringBuilder query = new StringBuilder("").append("SELECT mr FROM MarriageRegister mr " +
            "WHERE ((mr.male.identificationNumberMale IS NOT NULL AND mr.male.identificationNumberMale = :id) " +
            "OR (mr.female.identificationNumberFemale IS NOT NULL AND mr.female.identificationNumberFemale = :id) " +
            "OR (mr.registrarOrMinisterPIN IS NOT NULL AND mr.registrarOrMinisterPIN = :id)) " +
            "AND mr.state IN (:stateList) ");
        if (dsDivisionList != null) {
            query.append("AND mr.mrDivision.dsDivision IS NOT NULL AND mr.mrDivision.dsDivision IN (:dsDivisionList) ");
        }
        query.append("AND mr.lifeCycleInfo.activeRecord IS TRUE ORDER BY mr.idUKey DESC");

        Query q = em.createQuery(query.toString());
        q.setParameter("id", id);
        q.setParameter("stateList", stateList);
        if (dsDivisionList != null) {
            q.setParameter("dsDivisionList", dsDivisionList);
        }
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

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<MarriageRegister> getMarriageRegisterBySerialAndMRDivision(long serialNumber,
        MRDivision mrDivision) {
        Query q = em.createNamedQuery("getMarriageRegisterBySerialAndMRDivision");
        q.setParameter("serialNumber", serialNumber);
        q.setParameter("mrDivision", mrDivision);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<MarriageRegister> getUnUsedMarriageNotices(Date date) {
        Query q = em.createNamedQuery("filter.by.unused.marriage.notice.date");
        q.setParameter("date", date);
        q.setParameter("state", MarriageRegister.State.LICENSE_PRINTED);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<MarriageRegister> getActiveMarriageLicense(String groomPIN, String bridePIN) {
        Query q = em.createNamedQuery("get.active.marriage.license");
        q.setParameter("bridePIN", bridePIN);
        q.setParameter("groomPIN", groomPIN);
        q.setParameter("state", MarriageRegister.State.NOTICE_APPROVED);
        return q.getResultList();
    }
}
