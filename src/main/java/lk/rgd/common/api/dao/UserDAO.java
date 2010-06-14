package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;

import java.util.List;

/**
 * @author asankha
 */
public interface UserDAO {

    /**
     * Get user by user id
     * @param userId the unique userId (PK)
     * @return the corresponding user
     */
    public User getUserByPK(String userId);

    /**
     * Return the list of users playing the specified role
     * @param roleId the role ID
     * @return list of users playing the role
     */
    public List<User> getUsersByRole(String roleId);

    /**
     * Add a user
     * @param user the user to be addes
     */
    public void addUser(User user);

    /**
     * Return the list of users assigned to the Birth / Death Registration district
     * @param assignedBDDistrict the district
     * @return list of users assigned
     */

    List<User> getUsersByAssignedBDDistrict(District assignedBDDistrict);

    /**
     * Return the list of users assigned to the Marriage Registration district
     * @param assignedMRDistrict the district
     * @return list of users assigned
     */
    List<User> getUsersByAssignedMRDistrict(District assignedMRDistrict);

    /**
     * Return the list of users playing the specified role in the specified Birth/Death registration district
     * @param role the role being played
     * @param assignedBDDistrict the BD district
     * @return list of users playing the role in that district
     */
    List<User> getUsersByRoleAndAssignedBDDistrict(Role role, District assignedBDDistrict);

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
    List<User> getUsersByIDMatch(String userId);

    /**
     * Get users by name - with a wildcard match
     * @param userName the userid to match within the userId field
     * @return the list of users matching the criteria
     */
    public List<User> getUsersByNameMatch(String userName);
}
