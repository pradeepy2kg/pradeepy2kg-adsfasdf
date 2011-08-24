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
     *
     * @param userId       user id
     * @param locationUKey location key
     * @return the specific UserLocation or null
     */
    public UserLocation getUserLocation(String userId, int locationUKey);

    /**
     * Add an UserLocation and create relationship to an already existing User and Location
     *
     * @param userLocation the UserLocation to be added
     * @param admin        the user performing the action
     */
    public void save(UserLocation userLocation, User admin);

    /**
     * Update any of the UserLocation properties except the relationship already made against User and Location
     *
     * @param userLocation the UserLocation to be updated
     * @param admin        the user performing the action
     */
    public void update(UserLocation userLocation, User admin);

    /**
     * User Locations inactivation when the associated Location is inactivated. And all the User Locations with the
     * specified unique location key inactivated
     *
     * @param locationUKey the inactivated location unique key
     * @param admin        the user performing the action
     */
    public void inactivateUserLocations(int locationUKey, User admin);

    /**
     * Return all user locations active or inactive
     *
     * @param active filter for results
     * @return filtered user locations
     */
    public List<UserLocation> getAllUserLocations(boolean active);

    /**
     * Return all user locations active or inactive
     *
     * @param userId filter for results
     * @return filtered user locations
     */
    public List<UserLocation> getUserLocationsListByUserId(String userId);

    /**
     * Return users who are authorized to sign birth certificates in active or inactive user locations
     *
     * @param locationId location id
     * @param active     filter for active or inactive results
     * @return list of authorized users who can sign Birth Certificates
     */
    public List<User> getBirthCertSignUsersByLocationId(int locationId, boolean active);

    /**
     * Return Active Locations list of given user
     *
     * @param userId
     * @param active
     * @return list of active locations list of given user
     */
    public List<UserLocation> getActiveUserLocations(String userId, boolean active);

    /**
     * get uses who has permission to sign marriage certificate/license to marriage by location id and
     * filter by users location  activeness
     *
     * @param locationId location id
     * @param active     users activeness (active/inactive)
     * @return list of users who are assign to given user location
     */
    public List<User> getMarriageCertificateSignUsersByLocationId(int locationId, boolean active);
}
