package lk.rgd.common.api.service;

import lk.rgd.common.api.domain.Event;
import lk.rgd.common.api.domain.User;

import java.util.List;
import java.util.Date;

/**
 * Manage events
 *
 * @author Chathuranga Withana
 * @author Janith
 */
public interface EventViewerService {

    /**
     * Returns the Event object for a given Id
     *
     * @param eventId the unique id of the Event
     * @param user    user invoking the action
     * @return the event if found
     */
    public Event getEventById(long eventId, User user);

    /**
     * Returns paginated list of all events
     *
     * @param pageNo   pagenumber
     * @param noOfRows number of rows
     * @param user     user invoking the action
     * @return matching records
     */
    public List<Event> getPaginatedListForAll(int pageNo, int noOfRows, User user);

   /**
     *
     * @param pageNo
     * @param noOfRows
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Event> getPaginatedListByTimestampRange(int pageNo, int noOfRows, Date startTime, Date endTime,Event.Type eventType);
}