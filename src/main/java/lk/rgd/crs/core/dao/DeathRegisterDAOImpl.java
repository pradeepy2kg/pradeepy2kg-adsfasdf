package lk.rgd.crs.core.dao;

import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.common.core.dao.BaseDAO;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;


/**
 * @author Indunil Moremada
 */
public class DeathRegisterDAOImpl extends BaseDAO implements DeathRegisterDAO {

    /**
     * @inheritDoc
     */
    @Transactional
    public void addDeathRegistration(DeathRegister dr) {
        dr.setStatus(DeathRegister.State.DATA_ENTRY);
        em.persist(dr);
    }

    /**
     * @inheritDoc
     */
    @Transactional
    public void updateDeathRegistration(DeathRegister dr) {
        em.merge(dr);
    }

    /**
     * @inheritDoc
     */
    public DeathRegister getById(long deathRegisterIdUKey) {
        return em.find(DeathRegister.class, deathRegisterIdUKey);
    }

    /**
     * @inheritDoc
     */
    public void deleteDeathRegistration(long deathRegistrationIdUKey) {
        DeathRegister dr = getById(deathRegistrationIdUKey);
        em.remove(dr);
    }

    public List<DeathRegister> getPaginatedListForState(int pageNo, int noOfRows, DeathRegister.State status) {
        Query q = em.createNamedQuery("death.register.filter.by.status.paginated").setFirstResult((pageNo - 1)
            * noOfRows).setMaxResults(noOfRows);
        q.setParameter("status", status);
        return q.getResultList();
    }
}
