package lk.rgd.crs.core.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.crs.api.dao.AdoptionOrderDAO;
import lk.rgd.crs.api.domain.AdoptionOrder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * @author Ashoka Ekanayaka
 */
public class AdoptionOrderDAOImpl extends BaseDAO implements AdoptionOrderDAO {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void addAdoptionOrder(AdoptionOrder adoption, User user) {
        adoption.getLifeCycleInfo().setCreatedTimestamp(new Date());
        adoption.getLifeCycleInfo().setCreatedUser(user);
        adoption.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        adoption.getLifeCycleInfo().setLastUpdatedUser(user);
        adoption.getLifeCycleInfo().setActiveRecord(true);
        em.persist(adoption);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateAdoptionOrder(AdoptionOrder adoption, User user) {
        adoption.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        adoption.getLifeCycleInfo().setLastUpdatedUser(user);
        em.merge(adoption);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteAdoptionOrder(long idUKey) {
        em.remove(getById(idUKey));
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public AdoptionOrder getById(long adoptionIdUKey) {
        return em.find(AdoptionOrder.class, adoptionIdUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void recordNewBirthDeclaration(AdoptionOrder adoption, long serialNumber, User user) {
        em.merge(adoption);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AdoptionOrder getByCourtAndCourtOrderNumber(int courtUKey, String courtOrderNumber) {
        Query q = em.createNamedQuery("get.by.court.and.courtOrderNumber");
        q.setParameter("courtOrderNumber", courtOrderNumber);
        //q.setParameter("courtUKey", courtUKey); // TODO FIX ME
        try {
            return (AdoptionOrder) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        // NonUniqueResultException should not occur since only one record for a court + order number should exist
        // this is enforced with a unique constraint on the AdoptionOrder table
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<AdoptionOrder> getPaginatedListForState(int pageNo, int noOfRows, AdoptionOrder.State status) {
        Query q = em.createNamedQuery("adoption.filter.by.status.paginated").
            setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("status", status);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<AdoptionOrder> getPaginatedListForAll(int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("getPaginatedListForAll").setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("state", AdoptionOrder.State.RE_REGISTRATION_REQUESTED);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<AdoptionOrder> findAll() {
        Query q = em.createNamedQuery("getAllAdoptions");
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Long findCourtUsageInAdoptions(int courtUKey) {
        Query q = em.createNamedQuery("count.adoption.court.usage");
        q.setParameter("courtId", courtUKey);
        return (Long) q.getSingleResult();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean isEntryNoExist(long adoptionEntryNo) {
        Query q = em.createNamedQuery("isEntryNoExist");
        q.setParameter("adoptionEntryNo", adoptionEntryNo);
        if (q.getResultList() != null && q.getResultList().size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Long getLastEntryNo() {
        Query q = em.createNamedQuery("getLastEntryNo");
        try {
            return (Long) q.getSingleResult();
        } catch (Exception e) {
            return 0l;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<AdoptionOrder> getAdoptionsByCourtOrderNumber(String courtOrderNumber) {
        Query q = em.createNamedQuery("getAdoptionsByCourtOrderNumber");
        q.setParameter("courtOrderNumber", "%"+courtOrderNumber+"%");
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<AdoptionOrder> getAdoptionsByCourtOrderNumberAndState(String courtOrderNumber, AdoptionOrder.State state) {
        Query q = em.createNamedQuery("getAdoptionsByCourtOrderNumberAndState");
        q.setParameter("courtOrderNumber", "%"+courtOrderNumber+"%");
        q.setParameter("state", state);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<AdoptionOrder> getAdoptionsByCourtOrderNumberForAlteration(String courtOrderNumber, AdoptionOrder.State minState, AdoptionOrder.State maxState) {
        Query q = em.createNamedQuery("getAdoptionsByCourtOrderNumberForAlteration");
        q.setParameter("courtOrderNumber", "%"+courtOrderNumber+"%");
        q.setParameter("minState", minState);
        q.setParameter("maxState", maxState);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public AdoptionOrder getAdoptionByEntryNumber(long adoptionEntryNo) {
        Query q = em.createNamedQuery("getAdoptionByEntryNumber");
        q.setParameter("adoptionEntryNo", adoptionEntryNo);
        try{
            return (AdoptionOrder) q.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<AdoptionOrder> getHistoryRecords(long adoptionEntryNo) {
        Query q = em.createNamedQuery("getHistoryRecords");
        q.setParameter("adoptionEntryNo", adoptionEntryNo);
        return q.getResultList();
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    public AdoptionOrder getAdoptionByEntryNumberAndState(long adoptionEntryNo, AdoptionOrder.State state) {
        Query q = em.createNamedQuery("getAdoptionByEntryNumberAndState");
        q.setParameter("adoptionEntryNo", adoptionEntryNo);
        q.setParameter("state", state);
        try{
            return (AdoptionOrder) q.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public AdoptionOrder getAdoptionByEntryNumberForAlteration(long adoptionEntryNo, AdoptionOrder.State minState, AdoptionOrder.State maxState) {
        Query q = em.createNamedQuery("getAdoptionByEntryNumberForAlteration");
        q.setParameter("adoptionEntryNo", adoptionEntryNo);
        q.setParameter("minState", minState);
        q.setParameter("maxState", maxState);
        try{
            return (AdoptionOrder) q.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<AdoptionOrder> getAdoptionsByCourt(int courtUKey) {
        Query q = em.createNamedQuery("getAdoptionsByCourt");
        q.setParameter("courtUKey", courtUKey);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public AdoptionOrder getAdoptionByCourtOrderNumberAndEntryNumber(String courtOrderNumber, long adoptionEntryNo) {
        Query q = em.createNamedQuery("getAdoptionByCourtOrderNumberAndEntryNumber");
        q.setParameter("courtOrderNumber", courtOrderNumber);
        q.setParameter("adoptionEntryNo", adoptionEntryNo);
        try{
            return (AdoptionOrder)q.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<AdoptionOrder> getAdoptionsWithSameChildName(String childName, long adoptionEntryNo) {
        Query q = em.createNamedQuery("getAdoptionsWithSameChildName");
        q.setParameter("childName", childName);
        q.setParameter("adoptionEntryNo", adoptionEntryNo);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<AdoptionOrder> getAdoptionsWithSameParentNames(String applicantName, String spouseName, long adoptionEntryNo) {
        Query q = em.createNamedQuery("getAdoptionsWithSameParentNames");
        q.setParameter("applicantName", applicantName);
        q.setParameter("spouseName", spouseName);
        q.setParameter("adoptionEntryNo", adoptionEntryNo);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<AdoptionOrder> getAdoptionsWithSameApplicantName(String applicantName, long adoptionEntryNo) {
        Query q = em.createNamedQuery("getAdoptionsWithSameApplicantName");
        q.setParameter("applicantName", applicantName);
        q.setParameter("adoptionEntryNo", adoptionEntryNo);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<AdoptionOrder> searchAdoptionRecords(long adoptionEntryNo, String courtOrderNumber, int courtUKey, String childName, Date childBirthDate) {
        Query q = em.createNamedQuery("searchAdoptionRecords");
        q.setParameter("adoptionEntryNo", adoptionEntryNo);
        q.setParameter("courtOrderNumber", courtOrderNumber);
        q.setParameter("courtUKey", courtUKey);
        if (childName != null && childName.length()>0){
            q.setParameter("childName", "%"+childName+"%");
        }
        else{
            q.setParameter("childName","");
        }
        q.setParameter("childBirthDate", childBirthDate);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
   public List<AdoptionOrder> getAdoptionsByCourtIdAndDataEntryPeriod(int courtId,Date dataEntryDateFrom,Date dataEntryDateTo){
        Query q = em.createNamedQuery("getAllAdoptionsForAGivenCourtAndForADataEntryPeriod");
        q.setParameter("courtId", courtId);
        q.setParameter("dataEntryDateFrom", dataEntryDateFrom);
        q.setParameter("dataEntryDateTo", dataEntryDateTo);
        return q.getResultList();
    }
    
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<AdoptionOrder> getAdoptionsByDataEntryPeriod(Date dataEntryDateFrom,Date dataEntryDateTo){
        Query q = em.createNamedQuery("getAllAdoptionsForADataEntryPeriod");
        q.setParameter("dataEntryDateFrom", dataEntryDateFrom);
        q.setParameter("dataEntryDateTo", dataEntryDateTo);
        return q.getResultList();
    }
}
