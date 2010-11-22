package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.User;

import java.util.List;
import java.util.Map;

/**
 * @author asankha
 */
public interface LocationDAO {

    /**
     * Get the Location entry by PK
     *
     * @param locationUKey location key
     * @return the specific Location or null
     */
    public Location getLocation(int locationUKey);

    /**
     * Add a Location and create relationship to an already existing User and Location
     *
     * @param userLocation the UserLocation to be added
     * @param admin        the user performing the action
     */
    public void add(Location userLocation, User admin);

    /**
     * Update a Location
     *
     * @param location the Location to be updated
     * @param admin    the user performing the action
     */
    public void update(Location location, User admin);

    /**
     * Return all Locations that are active or inactive
     *
     * @return filtered list of Locations
     */
    public List<Location> getAllLocations();

    /**
     * Return all Locations that are active or inactive
     *
     * @return filtered list of Locations
     */
    public Map<Integer, String> getLocationList(String language, User user);

    /**
     * @param locationCode
     * @return
     */
    public Location getLocationByCode(int locationCode);

    /**
     * Return all locations that have given location code and DSDivision ID
     *
     * @param locationCode
     * @param dsDivisionId
     * @return
     */
    public Location getLocationByCodeAndByDSDivisionID(int locationCode, int dsDivisionId);

    /**
     * Return list of locations that have given DSDivision ID
     *
     * @param dsDivisionId
     * @return
     */
    public Map<Integer, String> getLocationByDSDivisionID(int dsDivisionId);
}
