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
        adoption.setStatus(AdoptionOrder.State.DATA_ENTRY);
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
        Query q = em.createNamedQuery("getAllAdoptions").setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
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
        } catch (NoResultException e) {
            return 0l;
        }
    }
}
