package lk.rgd.common.api.service;

import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.AuthorizationException;

import java.util.List;

/**
 * @author asankha
 */
public interface UserManager {
    /**
     * Authenticate user credentials
     * @param userName the user name
     * @param password the password
     * @return the User object if authorization succeeds
     * @throws AuthorizationException if authorization fails
     */
    User authenticateUser(String userName, String password) throws AuthorizationException;

    /**
     * Return the list of non-deleted users playing the specified role
     * @param roleId the role ID
     * @return list of users playing the role
     */
    public List<User> getUsersByRole(String roleId);

    /**
     * Return the list of non-deleted users assigned to the Birth / Death Registration district
     * @param assignedBDDistrict the district
     * @return list of users assigned
     */
    public List<User> getUsersByAssignedBDDistrict(District assignedBDDistrict);

    /**
     * Return the list of non-deleted users assigned to the Marriage Registration district
     * @param assignedMRDistrict the district
     * @return list of users assigned
     */
    public List<User> getUsersByAssignedMRDistrict(District assignedMRDistrict);

    /**
     * Return the list of non-deleted users playing the specified role in the specified Birth/Death registration district
     * @param role the role being played
     * @param assignedBDDistrict the BD district
     * @return list of users playing the role in that district
     */
    public List<User> getUsersByRoleAndAssignedBDDistrict(Role role, District assignedBDDistrict);

    /**
     * Return the list of non-deleted users playing the specified role in the specified Marriage registration district
     * @param role the role
     * @param assignedMRDistrict the MR district
     * @return list of users playing the role in that district
     */
    public List<User> getUsersByRoleAndAssignedMRDistrict(Role role, District assignedMRDistrict);

    /**
     * Get non-deleted users by ID - with a wildcard match
     * @param userId the userid to match within the userId field
     * @return the list of users matching the criteria
     */
    public List<User> getUsersByIDMatch(String userId);

    /**
     * Get non-deleted users by name - with a wildcard match
     * @param userName the userid to match within the userId field
     * @return the list of users matching the criteria
     */
    public List<User> getUsersByNameMatch(String userName);

    /**
     * Get all users (non-deleted accounts)
     * @return all non-deleted Users
     */
    public List<User> getAllUsers();

    /**
     * Hash the password
     * @param password pasword in clear text
     * @return hashed password
     */
    public String hashPassword(String password);

    /**
     * Create a new user
     * @param userToCreate the User instance to create
     * @param adminUser the user account creating the new user - must belong to role ADMIN
     */
    void createUser(User userToCreate, User adminUser);

    /**
     * Update a user
     * @param userToUpdate the User instance to update
     * @param adminUser the user account creating the new user - must belong to role ADMIN
     */
    void updateUser(User userToUpdate, User adminUser);

    /**
     * Delete a user. Will mark this record as permanently removed, and then maintained for historical
     * and reporting/auditing purposes
     *
     * @param userToDelete the User instance to delete
     * @param adminUser the user account creating the new user - must belong to role ADMIN
     */
    void deleteUser(User userToDelete, User adminUser);
}
