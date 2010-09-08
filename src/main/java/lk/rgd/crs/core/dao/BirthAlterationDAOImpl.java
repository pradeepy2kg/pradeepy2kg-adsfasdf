package lk.rgd.crs.core.dao;

import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.BirthAlterationDAO;
import lk.rgd.crs.api.domain.BirthAlteration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.Date;

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
}
