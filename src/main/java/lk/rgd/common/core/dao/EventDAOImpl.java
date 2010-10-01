package lk.rgd.common.core.dao;

import lk.rgd.common.api.dao.EventDAO;
import lk.rgd.common.api.domain.Event;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Id;
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
     *
     */
    public List<Event> getEventsList(long idUKey) {
        Query q = em.createNamedQuery("findAllEvents");
        return q.getResultList();
    }

    public Event getEvent(long idUKey){
        logger.debug("Get Event by IdUKey : {}", idUKey);
        return em.find(Event.class, idUKey);   
    }

}
