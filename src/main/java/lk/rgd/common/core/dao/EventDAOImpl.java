package lk.rgd.common.core.dao;

import lk.rgd.common.api.dao.EventDAO;
import lk.rgd.common.api.domain.Event;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author asankha
 */
public class EventDAOImpl extends BaseDAO implements EventDAO {

    /**
     * @inheritDoc
     */
    // This method should save the event data within a *NEW* transaction irrespective of the business txn which may fail
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addEvent(Event e) {
        em.persist(e);
    }
}
