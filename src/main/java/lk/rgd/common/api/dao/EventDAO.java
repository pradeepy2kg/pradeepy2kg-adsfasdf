package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.Event;

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
     * Get event from the database
     *
     */
    public Event getEvent(long idUKey);
}
