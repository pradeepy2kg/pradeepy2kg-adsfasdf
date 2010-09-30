package lk.rgd.common.api.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Event;

/**
 * Manage events
 *
 * @author Janith
 */
public interface EventsMamagementService {

    /**
     * Get events form event table
     */
    public Event getEventsByIdUkey(long idUKey, User user);


}
