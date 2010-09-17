package lk.rgd.common.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.RGDRuntimeException;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.*;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.util.Base64;
import lk.rgd.crs.web.WebConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.GregorianCalendar;
import java.util.Calendar;

/**
 * @author asankha
 */
public class UserManagerImpl implements UserManager {

    private static final Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);

    private final UserDAO userDao;
    private final LocationDAO locationDao;
    private final UserLocationDAO userLocationDao;
    private final RoleDAO roleDao;
    private final AppParametersDAO appParaDao;
    private static final String SYSTEM_USER_NAME = "system";

    public UserManagerImpl(UserDAO userDao, RoleDAO roleDao, AppParametersDAO appParaDao,
        LocationDAO locationDao, UserLocationDAO userLocationDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.appParaDao = appParaDao;
        this.locationDao = locationDao;
        this.userLocationDao = userLocationDao;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public User authenticateUser(String userId, String password) throws AuthorizationException {
        User user = userDao.getUserByPK(userId);
        if (user != null && user.getStatus() == User.State.ACTIVE
            && password != null && user.getPasswordHash() != null
            && !SYSTEM_USER_NAME.equalsIgnoreCase(userId)) {
            if (user.getPasswordHash().equals(hashPassword(password))) {
                return user;
            } else {
                logger.debug("Password mismatch for user : {}", userId);
            }
        }
        logger.warn("Attempt to authenticate non-existant or inactive user : {} or empty password", userId);
        throw new AuthorizationException("Invalid user ID, password or user : " + userId, ErrorCodes.INVALID_LOGIN);        
    }

    /**
     * @inheritDoc
     */
    public User getSystemUser() {
        return userDao.getUserByPK(SYSTEM_USER_NAME);
    }

    /**
     * @inheritDoc
     */
    public List<User> getUsersByRole(String roleId) {
        return userDao.getUsersByRole(roleId);
    }

    /**
     * @inheritDoc
     */
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
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void createUser(User userToCreate, User adminUser) {

        // does user has authorization to add a new user
        if (!adminUser.isAuthorized(Permission.USER_MANAGEMENT)) {
            handleException(adminUser.getUserName() + " doesn't have permission to create a user",
                ErrorCodes.AUTHORIZATION_FAILS_USER_MANAGEMENT);

        } else {
            try {
                // get Calendar with current date
                java.util.GregorianCalendar gCal = new GregorianCalendar();
                // get yesterday's date
                gCal.add(Calendar.DATE, -1);
                userToCreate.setPasswordExpiry(gCal.getTime());

                // adding new default password
                userToCreate.setPasswordHash(hashPassword(WebConstants.DEFAULT_PASS));
                userDao.addUser(userToCreate, adminUser);
                logger.debug("New user {} created by : {}", userToCreate.getUserName(), adminUser.getUserName());
                
            } catch (Exception e) {
                handleException("Error creating a new user : " + userToCreate.getUserId() +
                    " by : " + adminUser.getUserId(), ErrorCodes.PERSISTING_EXCEPTION_COMMON, e);
            }
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUserLocation(UserLocation userLocation, User adminUser) {
        if (!adminUser.isAuthorized(Permission.USER_MANAGEMENT)) {
            handleException(adminUser.getUserName() + " doesn't have permission to add user locations",
                ErrorCodes.AUTHORIZATION_FAILS_USER_MANAGEMENT);
        } else {
            if (userDao.getUserByPK(userLocation.getUserId()) == null ||
                locationDao.getLocation(userLocation.getLocationId()) == null) {
                handleException("Non-existing User : " + userLocation.getUserId() +
                    " or location : " + userLocation.getLocationId(), ErrorCodes.INVALID_DATA);
            } else {
                userLocationDao.save(userLocation, adminUser);
            }
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserLocation(UserLocation userLocation, User adminUser) {
        if (!adminUser.isAuthorized(Permission.USER_MANAGEMENT)) {
            handleException(adminUser.getUserName() + " doesn't have permission to update user locations",
                ErrorCodes.AUTHORIZATION_FAILS_USER_MANAGEMENT);
        } else {
            userLocationDao.update(userLocation, adminUser);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User userToUpdate, User adminUser) {

        // if one user tries to update another user, does the former have authorization to update a user
        if (! ((adminUser.equals(userToUpdate) || adminUser.isAuthorized(Permission.USER_MANAGEMENT))) ) {
            handleException(adminUser.getUserName() + " doesn't have permission to update a user",
                ErrorCodes.AUTHORIZATION_FAILS_USER_MANAGEMENT);

        } else {
            // we will not let anyone update deleted user accounts
            User existing = userDao.getUserByPK(userToUpdate.getUserId());
            if (existing.getStatus() == User.State.DELETED) {
                handleException("Attempt to modify deleted account : " + existing.getUserId() +
                    " by : " + adminUser.getUserId() + " denied", ErrorCodes.AUTHORIZATION_FAILS_USER_MANAGEMENT);
            }
            userDao.updateUser(userToUpdate, adminUser);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(User userToDelete, User adminUser) {
        // does user have authorization to update a user
        if (!adminUser.isAuthorized(Permission.USER_MANAGEMENT)) {
            handleException(adminUser.getUserName() + " doesn't have permission to delete a user",
                ErrorCodes.AUTHORIZATION_FAILS_USER_MANAGEMENT);

        } else {
            userToDelete.setStatus(User.State.DELETED);
            updateUser(userToDelete, adminUser);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassword(String newPass, User user) {
        // setting new password expiry date get Calendar with current date
        java.util.GregorianCalendar gCal = new GregorianCalendar();
        int resetDays = appParaDao.getIntParameter(AppParameter.PASSWORD_EXPIRY_DAYS);
        gCal.add(Calendar.DATE, resetDays);
        user.setPasswordExpiry(gCal.getTime());
        // setting new password
        user.setPasswordHash(hashPassword(newPass));
        userDao.changePassword(user);
        logger.debug("Password updated for user : {} Expires on : {}", user.getUserName(), gCal.getTime());
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> getUsersByAssignedBDDistrict(District assignedBDDistrict) {
        return userDao.getUsersByAssignedBDDistrict(assignedBDDistrict);
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> getUsersByAssignedMRDistrict(District assignedMRDistrict) {
        return userDao.getUsersByAssignedMRDistrict(assignedMRDistrict);
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> getUsersByRoleAndAssignedBDDistrict(Role role, District assignedBDDistrict) {
        return userDao.getUsersByRoleAndAssignedBDDistrict(role, assignedBDDistrict);
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> getUsersByRoleAndAssignedMRDistrict(Role role, District assignedMRDistrict) {
        return userDao.getUsersByRoleAndAssignedMRDistrict(role, assignedMRDistrict);
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> getUsersByIDMatch(String userId) {
        return userDao.getUsersByIDMatch(userId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public User getUserByID(String userId) {
        return userDao.getUserByPK(userId);
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> getUsersByNameMatch(String userName) {
        return userDao.getUsersByNameMatch(userName);
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new RGDRuntimeException(message, code);
    }

    private void handleException(String message, int code, Exception e) {
        logger.error(message, e);
        throw new RGDRuntimeException(message, code, e);
    }
}