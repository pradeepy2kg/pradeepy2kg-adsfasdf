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
     * Return the list of users playing the specified role
     * @param roleId the role ID
     * @return list of users playing the role
     */
    public List<User> getUsersByRole(String roleId);

    /**
     * Return the list of users assigned to the Birth / Death Registration district
     * @param assignedBDDistrict the district
     * @return list of users assigned
     */
    public List<User> getUsersByAssignedBDDistrict(District assignedBDDistrict);

    /**
     * Return the list of users assigned to the Marriage Registration district
     * @param assignedMRDistrict the district
     * @return list of users assigned
     */
    public List<User> getUsersByAssignedMRDistrict(District assignedMRDistrict);

    /**
     * Return the list of users playing the specified role in the specified Birth/Death registration district
     * @param role the role being played
     * @param assignedBDDistrict the BD district
     * @return list of users playing the role in that district
     */
    public List<User> getUsersByRoleAndAssignedBDDistrict(Role role, District assignedBDDistrict);

    /**
     * Return the list of users playing the specified role in the specified Marriage registration district
     * @param role the role
     * @param assignedMRDistrict the MR district
     * @return list of users playing the role in that district
     */
    public List<User> getUsersByRoleAndAssignedMRDistrict(Role role, District assignedMRDistrict);

    /**
     * Get users by ID - with a wildcard match
     * @param userId the userid to match within the userId field
     * @return the list of users matching the criteria
     */
    public List<User> getUsersByIDMatch(String userId);

    /**
     * Get users by name - with a wildcard match
     * @param userName the userid to match within the userId field
     * @return the list of users matching the criteria
     */
    public List<User> getUsersByNameMatch(String userName);

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
}
