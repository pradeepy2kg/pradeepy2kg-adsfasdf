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
     *
     * @param event
     */
    public void addEvent(Event event);

    /**
     * Get an Event object for a given Id
     *
     * @param eventId the unique id assigned to an event
     * @return the Event or null if none exist
     */
    public Event getById(long eventId);

    /**
     * Get paginated list of all available Events
     *
     * @param pageNo   page number
     * @param noOfRows number of rows
     * @return all records (paginated)
     */
    public List<Event> getPaginatedListForAll(int pageNo, int noOfRows);
}
