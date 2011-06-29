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
     * Return the location code and location name in selected language
     *
     * @param locationUKey the location unique key
     * @param language     the selected language
     * @return the name and code of the location in selected language
     */
    public String getLocationNameByPK(int locationUKey, String language);

    /**
     * Return cached locations
     *
     * @return map
     */
    public Map<Integer, Location> getPreLoadedLocations();

    /**
     * @param locationCode
     * @return
     */
    public List<Location> getLocationByCode(String locationCode);

    /**
     * Return locations which have matching location name in sinhala, english or tamil, to ensure locations have unique
     * names
     *
     * @param siName the location name in sinhala
     * @param enName the location name in english
     * @param taName the location name in tamil
     * @return
     */
    public List<Location> getLocationByAnyName(String siName, String enName, String taName);

    /**
     * Return the the list of Locations by DS Division unique key and location code
     *
     * @param locationCode the location code
     * @param dsDivisionId the DS Division unique key
     * @return the list of Locations
     */
    public List<Location> getLocationByCodeAndByDSDivisionID(String locationCode, int dsDivisionId);

    /**
     * Return list of locations that have given DSDivision ID
     *
     * @param dsDivisionId
     * @return Map<Integer, String> of locations that have given DSDivision ID
     */
    public Map<Integer, String> getLocationByDSDivisionID(int dsDivisionId, String lang);

    /**
     * Return all Locations in specified DSDivision
     *
     * @param dsDivisionId
     * @return all Locations in DSDivision
     */
    public List<Location> getAllLocationsByDSDivisionKey(int dsDivisionId);
}
