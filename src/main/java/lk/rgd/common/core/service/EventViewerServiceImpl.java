package lk.rgd.common.core.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.service.EventViewerService;

import java.util.List;
import java.util.Collections;
import java.util.Date;

/**
 * Manage events.
 */
public class EventViewerServiceImpl implements EventViewerService {

    private static final Logger logger = LoggerFactory.getLogger(EventViewerServiceImpl.class);

    private final EventDAO eventDAO;

    public EventViewerServiceImpl(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Event getEventById(long eventId, User user) {
        return eventDAO.getById(eventId);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Event> getPaginatedListForAll(int pageNo, int noOfRows, User user) {
        try {
            return eventDAO.getPaginatedListForAll(pageNo, noOfRows);
        } catch (Exception e) {
            logger.error("Error occurred while loading paginated Event list", e);
            return Collections.emptyList();
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Event> getPaginatedListByTimestampRange(int pageNo, int noOfRows, Date startTime, Date endTime,Event.Type eventType){
        try {
            return eventDAO.getPaginatedListByTimestampRange(pageNo, noOfRows,startTime,endTime,eventType);
        } catch (Exception e) {
            logger.error("Error occurred while loading paginated Event list", e);
            return Collections.emptyList();
        }
    }
}
