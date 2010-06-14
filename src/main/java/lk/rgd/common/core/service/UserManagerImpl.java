package lk.rgd.common.core.service;

import lk.rgd.Permission;
import lk.rgd.common.RGDRuntimeException;
import lk.rgd.common.api.dao.RoleDAO;
import lk.rgd.common.api.dao.UserDAO;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.util.Base64;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.ErrorCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author asankha
 */
public class UserManagerImpl implements UserManager {

    private static final Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);

    private final UserDAO userDao;
    private final RoleDAO roleDao;

    public UserManagerImpl(UserDAO userDao, RoleDAO roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    public User authenticateUser(String userId, String password) throws AuthorizationException {
        User user = userDao.getUserByPK(userId);
        if (password != null && user != null && user.getPasswordHash() != null) {
            if (user.getPasswordHash().equals(hashPassword(password))) {
                return user;
            }
        }
        logger.warn("Invalid user ID or password for user : " + userId);
        throw new AuthorizationException("Invalid user ID or password for user : " + userId,
            AuthorizationException.INVALID_USER_OR_PASSWORD);
    }

    public List<User> getUsersByRole(String roleId) {
        return userDao.getUsersByRole(roleId);
    }

    public final String hashPassword(String password) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            logger.warn("Cannot instantiate a SHA-1 message digest", e);
            throw new RGDRuntimeException("Cannot instantiate a SHA-1 message digest", e);
        }
        sha.reset();
        sha.update(password.getBytes());
        return new String(Base64.encode(sha.digest()));
    }

    /**
     * @param userToCreate the user instance to be added
     * @param adminUser the user initiating the addition - must belong to the ADMIN role
     */
    public void createUser(User userToCreate, User adminUser) {

        // does user has authorization to add a new user
        if (!adminUser.isAuthorized(Permission.CREATE_USER)) {
            handleRGDRuntimeException(adminUser.getUserName() + " doesn't have permission to create a user",
                ErrorCodes.AUTHORIZATION_FAILS_CREATE_USER);
        } else {
            try {
                userDao.addUser(userToCreate);
                logger.debug("New user {} created by : {}", userToCreate.getUserName(), adminUser.getUserName());
            }
            catch (Exception e) {
                logger.error("Error creating a new user : " + userToCreate.getUserId() + " by : " + adminUser.getUserId(), e);
                throw new RGDRuntimeException("Error creating a new user : " + userToCreate.getUserId(),
                    ErrorCodes.PERSISTING_EXCEPTION_COMMON);
            }
        }
    }

    public List<User> getUsersByAssignedBDDistrict(District assignedBDDistrict) {
        return userDao.getUsersByAssignedBDDistrict(assignedBDDistrict);
    }

    private void handleRGDRuntimeException(String message, int code) {
        logger.error(message);
        throw new RGDRuntimeException(message, code);
    }

    public List<User> getUsersByAssignedMRDistrict(District assignedMRDistrict) {
        return userDao.getUsersByAssignedMRDistrict(assignedMRDistrict);
    }

    private void handleCRSRuntimeException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }

    public List<User> getUsersByRoleAndAssignedBDDistrict(Role role, District assignedBDDistrict) {
        return userDao.getUsersByRoleAndAssignedBDDistrict(role, assignedBDDistrict);    
    }

    public List<User> getUsersByRoleAndAssignedMRDistrict(Role role, District assignedMRDistrict) {
        return userDao.getUsersByRoleAndAssignedMRDistrict(role, assignedMRDistrict);
    }

    public List<User> getUsersByID(String userId) {
        return null;
    }

    public List<User> getUsersByName(String userName) {
        return null;
    }
}
