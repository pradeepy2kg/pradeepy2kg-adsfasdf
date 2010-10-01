package lk.rgd.common.api.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Event;

import java.util.List;

/**
 * Manage events
 *
 * @author Janith
 */
public interface EventsMamagementService {

    /**
     * Get events list form event table
     */
    public List<Event> getEventsListByIdUKey(long idUKey, User user);

    /**
     * Get event from table
     */
    public Event getEventByIdUKey(long idUKey, User user);

}
