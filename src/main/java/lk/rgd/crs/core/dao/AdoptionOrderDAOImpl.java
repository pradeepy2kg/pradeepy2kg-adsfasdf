package lk.rgd.crs.core.dao;

import lk.rgd.crs.api.dao.AdoptionOrderDAO;
import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.common.core.dao.BaseDAO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import javax.persistence.Query;
import java.util.List;

/**
 * @author Ashoka Ekanayaka
 */
public class AdoptionOrderDAOImpl extends BaseDAO implements AdoptionOrderDAO {
    @Transactional
    public void addAdoptionOrder(AdoptionOrder adoption) {
        adoption.setStatus(AdoptionOrder.State.DATA_ENTRY);
        em.persist(adoption);
    }

    @Transactional
    public void updateAdoptionOrder(AdoptionOrder adoption) {
        em.merge(adoption);
    }

    @Transactional
    public void deleteAdoptionOrder(long idUKey) {
        em.remove(getById(idUKey));
    }

    public List<AdoptionOrder> getByCourtOrderNumber(String courtOrderNumber) {
        Query q = em.createNamedQuery("get.by.courtOrderNumber");
        q.setParameter("courtOrderNumber", courtOrderNumber);
       // logger.debug("new court order number : {} ",q.getResultList());
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    public List<AdoptionOrder> getPaginatedListForState(int pageNo, int noOfRows, AdoptionOrder.State status) {
        Query q = em.createNamedQuery("adoption.filter.by.status.paginated").setFirstResult((pageNo - 1)
                * noOfRows).setMaxResults(noOfRows);
        q.setParameter("status", status);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    public List<AdoptionOrder> getPaginatedListForAll(int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("getAllAdoptions").setFirstResult((pageNo - 1)
                * noOfRows).setMaxResults(noOfRows);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    public List<AdoptionOrder> findAll() {
        Query q = em.createNamedQuery("getAllAdoptions"); 
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    public AdoptionOrder getById(long adoptionIdUKey) {
        return em.find(AdoptionOrder.class, adoptionIdUKey);
    }

}
