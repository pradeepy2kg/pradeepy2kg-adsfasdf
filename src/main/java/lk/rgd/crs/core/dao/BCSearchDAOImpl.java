package lk.rgd.crs.core.dao;

import lk.rgd.crs.api.dao.BCSearchDAO;
import lk.rgd.crs.api.domain.BirthCertificateSearch;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.common.api.domain.DSDivision;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Chathuranga Withana
 */
public class BCSearchDAOImpl extends BaseDAO implements BCSearchDAO {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addBirthCertificateSearch(BirthCertificateSearch bcs) {
        em.persist(bcs);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public BirthCertificateSearch getByDSDivisionAndSerialNo(DSDivision dsDivision, String serialNo) {
        Query q = em.createNamedQuery("get.by.serialNo.and.dsDivision");
        q.setParameter("serialNo", serialNo);
        q.setParameter("dsDivision", dsDivision);
        try {
            return (BirthCertificateSearch) q.getSingleResult();
        } catch (NoResultException ignore) {
            return null;
        }
        // TODO Fix me and handle NonUniqueResultException
    }
}
