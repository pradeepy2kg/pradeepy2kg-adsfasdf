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
    @Transactional(propagation = Propagation.REQUIRED)
    public void addAdoptionOrder(AdoptionOrder adoption) {
        adoption.setStatus(AdoptionOrder.State.DATA_ENTRY);
        em.persist(adoption);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateAdoptionOrder(AdoptionOrder adoption) {
        em.merge(adoption);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAdoptionOrder(long idUKey) {
        em.remove(getById(idUKey));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AdoptionOrder> findAll() {
        Query q = em.createNamedQuery("findAll");
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    public AdoptionOrder getById(long adoptionIdUKey) {
        logger.debug("Get BDF by ID : {}", adoptionIdUKey);
        return em.find(AdoptionOrder.class, adoptionIdUKey);
    }

}
