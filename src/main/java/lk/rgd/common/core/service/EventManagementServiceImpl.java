package lk.rgd.common.core.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.service.EventManagementService;
import lk.rgd.crs.CRSRuntimeException;

import java.util.List;
import java.util.Collection;
import java.util.Collections;

/**
 * Manage events.
 */
public class EventManagementServiceImpl implements EventManagementService {

    private static final Logger logger = LoggerFactory.getLogger(EventManagementServiceImpl.class);

    private final EventDAO eventDAO;

    public EventManagementServiceImpl(EventDAO eventDAO) {
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
}
