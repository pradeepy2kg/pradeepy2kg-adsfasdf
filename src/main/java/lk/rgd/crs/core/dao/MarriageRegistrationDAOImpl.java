package lk.rgd.crs.core.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.crs.api.dao.MarriageRegistrationDAO;
import lk.rgd.crs.api.domain.MarriageRegister;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author amith jayasekara
 */
public class MarriageRegistrationDAOImpl extends BaseDAO implements MarriageRegistrationDAO {

    /**
     * @inheritdoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void addMarriageNotice(MarriageRegister notice, User user) {
        notice.setState(MarriageRegister.State.NOTICE_RECEIVED);
        notice.getLifeCycleInfo().setCreatedTimestamp(new Date());
        notice.getLifeCycleInfo().setCreatedUser(user);
        notice.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        notice.getLifeCycleInfo().setLastUpdatedUser(user);
        em.persist(notice);
    }
}
