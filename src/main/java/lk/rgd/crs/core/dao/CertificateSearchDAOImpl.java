package lk.rgd.crs.core.dao;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.crs.api.dao.CertificateSearchDAO;
import lk.rgd.crs.api.domain.CertificateSearch;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * @author Chathuranga Withana
 */
public class CertificateSearchDAOImpl extends BaseDAO implements CertificateSearchDAO {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addBirthCertificateSearch(CertificateSearch bcs) {
        em.persist(bcs);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public CertificateSearch getByDSDivisionAndSerialNo(DSDivision dsDivision, String serialNo) {
        Query q = em.createNamedQuery("get.by.serialNo.and.dsDivision");
        q.setParameter("serialNo", serialNo);
        q.setParameter("dsDivision", dsDivision);
        try {
            return (CertificateSearch) q.getSingleResult();
        } catch (NoResultException ignore) {
            return null;
        }
        // A NonUniqueResultException will never be thrown as a unique constraint guards against it for the above case 
    }
}
