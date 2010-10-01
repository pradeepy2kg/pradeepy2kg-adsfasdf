package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.Event;

import java.util.List;

/**
 * The DAO to manage Events
 *
 * @author asankha
 */
public interface EventDAO {

    /**
     * Add an event to the database
     */
    public void addEvent(Event e);

    /**
     * Get event list from the database
     *
     */
    public List<Event> getEventsList(long idUKey);

    /**
     * Get one event from the list
     */
    public Event getEvent(long idUKey) ;
}
