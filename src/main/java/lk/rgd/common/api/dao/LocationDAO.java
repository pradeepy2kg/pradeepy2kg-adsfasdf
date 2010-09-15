package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.User;

import java.util.List;

/**
 * @author asankha
 */
public interface LocationDAO {

    /**
     * Get the Location entry by PK
     * @param locationUKey  location key
     * @return the specific Location or null
     */
    public Location getLocation(int locationUKey);

    /**
     * Add a Location and create relationship to an already existing User and Location
     * @param userLocation the UserLocation to be added
     * @param admin the user performing the action
     */
    public void save(Location userLocation, User admin);

    /**
     * Update a Location
     * @param location the Location to be updated
     * @param admin the user performing the action
     */
    public void update(Location location, User admin);

    /**
     * Return all Locations that are active or inactive
     * @param active flag to filter locations
     * @return filtered list of Locations
     */
    public List<Location> getAllLocations(boolean active);
}
