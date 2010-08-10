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
    @Transactional
    public void deleteDeathRegistration(long deathRegistrationIdUKey) {
        DeathRegister dr = getById(deathRegistrationIdUKey);
        em.remove(dr);
    }

    /**
     * @inheritDoc
     */
    public List<DeathRegister> getPaginatedListForState(int pageNo, int noOfRows, DeathRegister.State status) {
        Query q = em.createNamedQuery("death.register.filter.by.status.paginated").setFirstResult((pageNo - 1)
                * noOfRows).setMaxResults(noOfRows);
        q.setParameter("status", status);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    public List<DeathRegister> getPaginatedListForAll(int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("getAllDeathRegistrations").setFirstResult((pageNo - 1)
                * noOfRows).setMaxResults(noOfRows);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    public DeathRegister getByDeathSerialNo(String deathSerialNo) {
        //todo after finalizing the requirements has to be modified whether to return a single entry or list
        //Query q = em.createNamedQuery("get.by.death.SerailNumber");
        //q.setParameter("deathSerialNo", deathSerialNo);
        return em.find(DeathRegister.class, deathSerialNo);
    }
}
