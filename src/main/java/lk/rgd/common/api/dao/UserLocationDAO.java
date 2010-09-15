package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.UserLocation;

import java.util.List;
import java.util.Map;

/**
 * @author asankha
 */
public interface UserLocationDAO {

    /**
     * Get the UserLocation entry by PK
     * @param userId user id
     * @param locationUKey  location key
     * @return the specific UserLocation or null
     */
    public UserLocation getUserLocation(String userId, int locationUKey);

    /**
     * Add an UserLocation and create relationship to an already existing User and Location
     * @param userLocation the UserLocation to be added
     * @param admin the user performing the action
     */
    public void save(UserLocation userLocation, User admin);

    /**
     * Update any of the UserLocation properties except the relationship already made against User and Location
     * @param userLocation the UserLocation to be updated
     * @param admin the user performing the action
     */
    public void update(UserLocation userLocation, User admin);

    /**
     * Return all user locations active or inactive
     * @param active filter for results
     * @return filtered user locations
     */
    public List<UserLocation> getAllUserLocations(boolean active);
}
