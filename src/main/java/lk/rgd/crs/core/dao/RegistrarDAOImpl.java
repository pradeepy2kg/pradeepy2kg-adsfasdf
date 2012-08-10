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

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
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
    @Transactional(propagation = Propagation.SUPPORTS)
    public Registrar getRegistrarByPin(long pin) {
        Query q = em.createNamedQuery("get.registrars.by.pin");
        q.setParameter("pin", pin);
        try {
            return (Registrar) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Registrar getRegistrarByNIC(String nic) {
        Query q = em.createNamedQuery("getRegistrarByNIC");
        q.setParameter("nic", nic);
        List<Registrar> registrarList = q.getResultList();
        if (registrarList != null && registrarList.size() > 0) {
            return registrarList.get(0);
        }
        return null;
    }

    /**
     * @inheritedoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Registrar> getRegistrarByNameOrPartOfTheName(String name) {
        Query q = em.createNamedQuery("get.registrar.by.name.or.part.of.name");
        q.setParameter("name", "%" + name + "%");
        return q.getResultList();
    }
}
