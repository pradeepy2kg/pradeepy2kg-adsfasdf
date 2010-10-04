package lk.rgd.common.core.dao;

import lk.rgd.common.api.dao.EventDAO;
import lk.rgd.common.api.domain.Event;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;

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

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Event getById(long eventIdUKey) {
        logger.debug("Get Event by ID : {}", eventIdUKey);
        return em.find(Event.class, eventIdUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Event> getPaginatedListForAll(int pageNo, int noOfRows) {
        Query q = em.createNamedQuery("findAllEvents").setFirstResult((pageNo - 1) * noOfRows).setMaxResults(noOfRows);
        return q.getResultList();
    }
}
