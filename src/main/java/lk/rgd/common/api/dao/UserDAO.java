package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;

import java.util.List;
import java.util.Date;

/**
 * @author asankha
 */
public interface UserDAO {

    /**
     * Get user by user id
     *
     * @param userId the unique userId (PK)
     * @return the corresponding user
     */
    public User getUserByPK(String userId);

    /**
     * Return the list of users playing the specified role
     *
     * @param roleId the role ID
     * @return list of users playing the role
     */
    public List<User> getUsersByRole(String roleId);

    /**
     * Add a user
     *
     * @param userToCreate the user to be added
     * @param adminUser the admin user performing the operation
     */
    public void addUser(User userToCreate, User adminUser);

    /**
     * Update a user
     *
     * @param user the updated user
     * @param adminUser the admin user performing the operation
     */
    public void updateUser(User user, User adminUser);

    /**
     * Return the list of users assigned to the Birth / Death Registration district
     *
     * @param assignedBDDistrict the district
     * @return list of users assigned
     */

    List<User> getUsersByAssignedBDDistrict(District assignedBDDistrict);

    /**
     * Return the list of users assigned to the Marriage Registration district
     *
     * @param assignedMRDistrict the district
     * @return list of users assigned
     */
    List<User> getUsersByAssignedMRDistrict(District assignedMRDistrict);

    /**
     * Return the list of users playing the specified role in the specified Birth/Death registration district
     *
     * @param role               the role being played
     * @param assignedBDDistrict the BD district
     * @return list of users playing the role in that district
     */
    List<User> getUsersByRoleAndAssignedBDDistrict(Role role, District assignedBDDistrict);

    /**
     * Return the list of users playing the specified role in the specified Marriage registration district
     *
     * @param role               the role
     * @param assignedMRDistrict the MR district
     * @return list of users playing the role in that district
     */
    public List<User> getUsersByRoleAndAssignedMRDistrict(Role role, District assignedMRDistrict);

    /**
     * Get users by ID - with a wildcard match
     *
     * @param userId the userid to match within the userId field
     * @return the list of users matching the criteria
     */
    List<User> getUsersByIDMatch(String userId);

    /**
     * Get users by name - with a wildcard match
     *
     * @param userName the userid to match within the userId field
     * @return the list of users matching the criteria
     */
    public List<User> getUsersByNameMatch(String userName);

    /**
     * Get a list of all users (who are not deleted) - the results are not paged
     *
     * @return a List of all users
     */
    public List<User> getAllUsers();

    /**
     * @param user
     */
    public void changePassword(User user);

}
