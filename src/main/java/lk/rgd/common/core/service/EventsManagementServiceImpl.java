package lk.rgd.common.core.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.api.service.EventsMamagementService;

import java.util.List;

/**
 * Manage events.
 */
public class EventsManagementServiceImpl implements EventsMamagementService {

    private static final Logger logger = LoggerFactory.getLogger(EventsManagementServiceImpl.class);

    private final EventDAO eventDAO;


    public EventsManagementServiceImpl(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    public List<Event> getEventsListByIdUKey(long idUKey, User user) {
        return eventDAO.getEventsList(idUKey);
    }
    
    public Event getEventByIdUKey(long idUKey, User user){
        return eventDAO.getEvent(idUKey);
    }


}
