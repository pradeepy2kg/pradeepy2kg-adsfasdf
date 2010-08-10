package lk.rgd.crs.core.dao;

import lk.rgd.crs.api.dao.AdoptionOrderDAO;
import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.crs.api.domain.BirthDeclaration;
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

    /**
     * @inheritDoc
     */
    @Transactional
    public void initiateBirthDeclaration(AdoptionOrder adoption, BirthDeclaration bdf) {
        // mark existing adoption order as archived
        adoption.setStatus(AdoptionOrder.State.ADOPTION_ORDER_ARCHIVED);
        em.merge(adoption);

        // add new adoption order
        adoption.setIdUKey(0);
        adoption.setStatus(AdoptionOrder.State.ADOPTION_CERTIFICATE_PRINTED);
        adoption.setBirthCertificateNumber(bdf.getIdUKey());
        adoption.setBirthCertificateSerial(bdf.getRegister().getBdfSerialNo());
        adoption.setBirthDivisionId(bdf.getRegister().getBirthDivision().getBdDivisionUKey());
        em.persist(adoption);
    }
}
