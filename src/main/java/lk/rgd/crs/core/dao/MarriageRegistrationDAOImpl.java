package lk.rgd.crs.core.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.crs.api.dao.MarriageRegistrationDAO;
import lk.rgd.crs.api.domain.MarriageRegister;
import lk.rgd.crs.api.domain.Witness;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author amith jayasekara
 */
public class MarriageRegistrationDAOImpl extends BaseDAO implements MarriageRegistrationDAO {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void addMarriageNotice(MarriageRegister notice, User user) {
        notice.setState(MarriageRegister.State.DATA_ENTRY);
        notice.getLifeCycleInfo().setCreatedTimestamp(new Date());
        notice.getLifeCycleInfo().setCreatedUser(user);
        notice.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        notice.getLifeCycleInfo().setLastUpdatedUser(user);
        em.persist(notice);
    }

    /**
     * @inheriteDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public MarriageRegister getByIdUKey(long idUKey) {
        return em.find(MarriageRegister.class, idUKey);
    }

    /**
     * @inheriteDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void addWitness(Witness witness) {
        em.persist(witness);
    }

    /**
     * @inheriteDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateMarriageRegister(MarriageRegister marriageRegister, User user) {
        marriageRegister.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        marriageRegister.getLifeCycleInfo().setLastUpdatedUser(user);
        em.merge(marriageRegister);
    }
}
