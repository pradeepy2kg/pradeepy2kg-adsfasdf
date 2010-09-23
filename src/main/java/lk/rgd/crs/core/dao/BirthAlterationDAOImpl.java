package lk.rgd.crs.core.dao;

import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.crs.api.dao.BirthAlterationDAO;
import lk.rgd.crs.api.domain.BirthAlteration;
import lk.rgd.crs.api.domain.BDDivision;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * @author Indunil Moremada
 */
public class BirthAlterationDAOImpl extends BaseDAO implements BirthAlterationDAO {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void addBirthAlteration(BirthAlteration ba, User user) {
        ba.getLifeCycleInfo().setCreatedTimestamp(new Date());
        ba.getLifeCycleInfo().setCreatedUser(user);
        ba.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        ba.getLifeCycleInfo().setLastUpdatedUser(user);
        ba.getLifeCycleInfo().setActiveRecord(true);
        em.persist(ba);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateBirthAlteration(BirthAlteration ba, User user) {
        ba.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        ba.getLifeCycleInfo().setLastUpdatedUser(user);
        em.merge(ba);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteBirthAlteration(long idUKey) {
        em.remove(getById(idUKey));
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public BirthAlteration getById(long idUKey) {
        logger.debug("Get BirthAlteration by ID : {}", idUKey);
        return em.find(BirthAlteration.class, idUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthAlteration> getBulkOfAlterationByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("filter.alteration.by.dsdivision").
                setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        q.setParameter("dsDivision", dsDivision);
        q.setParameter("statusFullyApp", BirthAlteration.State.FULLY_APPROVED);
        q.setParameter("statusPrint", BirthAlteration.State.PRINTED);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthAlteration> getBulkOfAlterationByBDDivision(BDDivision BDDivision, int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("filter.alteration.by.bddivision").
                setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        logger.debug("get Approval pending list from bdDivision Number is :{}", BDDivision.getDivisionId());
        q.setParameter("bdDivision", BDDivision);
        q.setParameter("statusFullyApp", BirthAlteration.State.FULLY_APPROVED);
        q.setParameter("statusPrint", BirthAlteration.State.PRINTED);
        return q.getResultList();
    }
}
