package lk.rgd.crs.core.dao;

import lk.rgd.crs.api.dao.DeathAlterationDAO;
import lk.rgd.crs.api.domain.DeathAlteration;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.BaseDAO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @authar amith jayasekara
 * implementation class for death alteration DAO class
 */
public class DeathAlterationDAOImpl extends BaseDAO implements DeathAlterationDAO {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void addBirthAlteration(DeathAlteration da, User user) {
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
    public void updateBirthAlteration(DeathAlteration da, User user) {
        //To change body of implemented methods use File | Settings | File Templates.
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
    public DeathAlteration getById(long idUKey) {
        return em.find(DeathAlteration.class, idUKey);
    }
}
