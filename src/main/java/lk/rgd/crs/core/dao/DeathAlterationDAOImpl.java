package lk.rgd.crs.core.dao;

import lk.rgd.crs.api.dao.DeathAlterationDAO;
import lk.rgd.crs.api.domain.DeathAlteration;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.BaseDAO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * @authar amith jayasekara
 * implementation class for death alteration DAO class
 */
public class DeathAlterationDAOImpl extends BaseDAO implements DeathAlterationDAO {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void addDeathAlteration(DeathAlteration da, User user) {
        da.getLifeCycleInfo().setCreatedTimestamp(new Date());
        da.getLifeCycleInfo().setCreatedUser(user);
        da.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        da.getLifeCycleInfo().setLastUpdatedUser(user);
        da.getLifeCycleInfo().setActiveRecord(true);
        em.persist(da);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDeathAlteration(DeathAlteration da, User user) {
        da.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        da.getLifeCycleInfo().setLastUpdatedUser(user);
        em.merge(da);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteDeathAlteration(long idUKey) {
        em.remove(getById(idUKey));
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public DeathAlteration getById(long idUKey) {
        return em.find(DeathAlteration.class, idUKey);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<DeathAlteration> getByCertificateNumber(long idUKey) {
        Query q = em.createNamedQuery("get.alt.by.death.certificate.number");
        q.setParameter("deathCertificateNumber", idUKey);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<DeathAlteration> getPaginatedAlterationApprovalListByDeathDivision(int pageNo, int noOfRows, int divisionId) {
        Query q = em.createNamedQuery("get.alt.by.division.death.division");
        q.setParameter("deathDivisionUkey", divisionId);
        q.setFirstResult((pageNo - 1) * noOfRows);
        q.setMaxResults(noOfRows);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<DeathAlteration> getPaginatedAlterationApprovalListByDeathDSDivision(int pageNo, int noOfRows, int dsDivisionId) {
        Query q = em.createNamedQuery("get.alt.by.death.dsdivision");
        q.setParameter("deathDSdivisionUkey", dsDivisionId);
        q.setFirstResult((pageNo - 1) * noOfRows);
        q.setMaxResults(noOfRows);
        return q.getResultList();
    }

    public List<DeathAlteration> getAlterationByDeathId(long deathId) {
        //todo
        Query q = em.createNamedQuery("get.atl.by.death.id");
        q.setParameter("deathId", deathId);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<DeathAlteration> getDeathAlterationByUserLocation(int locationUKey) {
        Query q = em.createNamedQuery("get.alt.by.user.location");
        q.setParameter("locationUKey", locationUKey);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<DeathAlteration> getDeathAlterationByDeathPersonPin(String pin) {
        Query q = em.createNamedQuery("get.alt.by.death.person.pin");
        q.setParameter("pin", pin);
        return q.getResultList();
    }
}
