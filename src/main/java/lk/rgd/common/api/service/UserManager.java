package lk.rgd.common.api.service;

import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.UserLocation;
import lk.rgd.common.core.AuthorizationException;

import java.util.List;
import java.util.Date;

/**
 * @author asankha
 */
public interface UserManager {
    /**
     * Authenticate user credentials
     *
     * @param userId   the user Id
     * @param password the password
     * @return the User object if authorization succeeds
     * @throws AuthorizationException if authorization fails
     */
    User authenticateUser(String userId, String password) throws AuthorizationException;

    /**
     * This is a marker method to record the event into the event database
     *
     * @param userId the user logging out
     */
    public void logoutUser(String userId);

    /**
     * Internal API to access the System internal user
     *
     * @return the internal system user
     */
    public User getSystemUser();

    /**
     * Return the list of non-deleted users playing the specified role
     *
     * @param roleId the role ID
     * @return list of users playing the role
     */
    public List<User> getUsersByRole(String roleId);

    /**
     * Return the list of non-deleted users assigned to the Birth / Death Registration district
     *
     * @param assignedBDDistrict the district
     * @return list of users assigned
     */
    public List<User> getUsersByAssignedBDDistrict(District assignedBDDistrict);

    /**
     * Return the list of non-deleted users assigned to the Marriage Registration district
     *
     * @param assignedMRDistrict the district
     * @return list of users assigned
     */
    public List<User> getUsersByAssignedMRDistrict(District assignedMRDistrict);

    /**
     * Return the list of non-deleted users playing the specified role in the specified Birth/Death registration district
     *
     * @param role               the role being played
     * @param assignedBDDistrict the BD district
     * @return list of users playing the role in that district
     */
    public List<User> getUsersByRoleAndAssignedBDDistrict(Role role, District assignedBDDistrict);

    /**
     * Return the list of non-deleted users playing the specified role in the specified Marriage registration district
     *
     * @param role               the role
     * @param assignedMRDistrict the MR district
     * @return list of users playing the role in that district
     */
    public List<User> getUsersByRoleAndAssignedMRDistrict(Role role, District assignedMRDistrict);

    /**
     * Get non-deleted users by ID - with a wildcard match
     *
     * @param userId the userid to match within the userId field
     * @return the list of users matching the criteria
     */
    public List<User> getUsersByIDMatch(String userId);

    /**
     * Get the user by ID or null
     *
     * @param userId user id to lookup
     * @return the corresponding user or null
     */
    public User getUserByID(String userId);

    /**
     * Get non-deleted users by name - with a wildcard match
     *
     * @param userName the userid to match within the userId field
     * @return the list of users matching the criteria
     */
    public List<User> getUsersByNameMatch(String userName);

    /**
     * Get all users (non-deleted accounts)
     *
     * @return all non-deleted Users
     */
    public List<User> getAllUsers();

    /**
     * Hash the password
     *
     * @param password pasword in clear text
     * @return hashed password
     */
    public String hashPassword(String password);

    /**
     * Create a new user
     *
     * @param userToCreate the User instance to create
     * @param adminUser    the user account creating the new user - must belong to role ADMIN
     */
    void createUser(User userToCreate, User adminUser);


    /**
     * Create a new user
     *
     * @param user
     * @param userId
     * @param roleId
     * @param admin
     */
    public boolean createUser(User user, User admin, String userId, String roleId, int[] assDistricts, int[] assDivisions, boolean changePassword, String randomPassword);

    /**
     * Update a user
     *
     * @param userToUpdate the User instance to update
     * @param adminUser    the user account creating the new user - must belong to role ADMIN
     */
    void updateUser(User userToUpdate, User adminUser);

    /**
     * Update a user
     *
     * @param userToUpdate the User instance to update
     */
    void updateUser(User userToUpdate);


    /**
     * Delete a user. Will mark this record as permanently removed, and then maintained for historical
     * and reporting/auditing purposes
     *
     * @param userToDelete the User instance to delete
     * @param adminUser    the user account creating the new user - must belong to role ADMIN
     */
    void deleteUser(User userToDelete, User adminUser);

    /**
     * Update the password of the user, along with a new password expiry date
     *
     * @param newPass the new password
     * @param user    user account
     */
    void updatePassword(String newPass, User user);

    /**
     * Add a user to a location or vice versa
     *
     * @param userLocation user location object to be saved
     * @param adminUser    user performing the action
     */
    public void addUserLocation(UserLocation userLocation, User adminUser);

    /**
     * Update a user location assignment - but not its existing relationships to user and location tables
     *
     * @param userLocation user location object to be saved
     * @param adminUser    user performing the action
     */
    public void updateUserLocation(UserLocation userLocation, User adminUser);

    /**
     * @param userId     id of the user
     * @param locationId id of the location
     * @param adminUser  user performing the action
     */
    public void activeUserLocation(String userId, int locationId, User adminUser);

    /**
     * @param userId     id of the user
     * @param locationId id of the location
     * @param adminUser  user performing the action
     */
    public void inactiveUserLocation(String userId, int locationId, User adminUser);

    /**
     * get users by user id or user name "wild card matching"
     *
     * @param name name or user id
     * @return list of users
     */
    public List<User> getUserByIdOrName(String name);

}
