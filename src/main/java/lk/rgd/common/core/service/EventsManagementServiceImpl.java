package lk.rgd.common.core.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.api.service.EventsMamagementService;

/**
 * Manage events.
 */
public class EventsManagementServiceImpl implements EventsMamagementService {

    private static final Logger logger = LoggerFactory.getLogger(EventsManagementServiceImpl.class);

    private final EventDAO eventDAO;


    public EventsManagementServiceImpl(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    public Event getEventsByIdUkey(long idUKey, User user) {

        return eventDAO.getEvent(idUKey);
    }

}
