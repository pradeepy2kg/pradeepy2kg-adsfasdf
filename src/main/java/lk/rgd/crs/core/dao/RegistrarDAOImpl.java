package lk.rgd.crs.core.dao;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.crs.api.dao.RegistrarDAO;
import lk.rgd.crs.api.domain.Assignment;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.Registrar;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Manage Registrars
 *
 * @author asankha
 */
public class RegistrarDAOImpl extends BaseDAO implements RegistrarDAO {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Registrar getById(long registrarUKey) {
        return em.find(Registrar.class, registrarUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void addRegistrar(Registrar registrar, User user) {
        final Date now = new Date();
        registrar.getLifeCycleInfo().setActive(true);
        registrar.getLifeCycleInfo().setCreatedTimestamp(now);
        registrar.getLifeCycleInfo().setCreatedUser(user);
        registrar.getLifeCycleInfo().setLastUpdatedTimestamp(now);
        registrar.getLifeCycleInfo().setLastUpdatedUser(user);
        em.persist(registrar);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateRegistrar(Registrar registrar, User user) {
        registrar.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        registrar.getLifeCycleInfo().setLastUpdatedUser(user);
        em.merge(registrar);
    }

    /**
     * @inheritDoc
     */
    public List<Registrar> getRegistrarsByTypeAndDSDivision(DSDivision dsDivision, Assignment.Type type, boolean active) {
        // TODO
        if (2>1) throw new UnsupportedOperationException("Not yet implemented - please check with Asankha");
        Query q = em.createNamedQuery("get.registrars.by.type.and.dsdivision");
        q.setParameter("dsDivision", dsDivision.getDsDivisionUKey());
        return q.getResultList();
    }
}
