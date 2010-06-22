package lk.rgd.common.core.service;

import lk.rgd.Permission;
import lk.rgd.AppConstants;
import lk.rgd.common.RGDRuntimeException;
import lk.rgd.common.api.dao.RoleDAO;
import lk.rgd.common.api.dao.UserDAO;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.util.Base64;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.ErrorCodes;
import lk.rgd.crs.web.WebConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;

/**
 * @author asankha
 */
public class UserManagerImpl implements UserManager {

    private static final Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);

    private final UserDAO userDao;
    private final RoleDAO roleDao;
    private final AppParametersDAO appParaDao;

    public UserManagerImpl(UserDAO userDao, RoleDAO roleDao, AppParametersDAO appParaDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.appParaDao = appParaDao;
    }

    public User authenticateUser(String userId, String password) throws AuthorizationException {
        User user = userDao.getUserByPK(userId);
        if (user != null && user.getStatus() == User.State.ACTIVE &&
                password != null && user != null && user.getPasswordHash() != null) {
            if (user.getPasswordHash().equals(hashPassword(password))) {
                return user;
            }
        }
        logger.warn("Invalid user ID, password or user : " + userId);
        throw new AuthorizationException("Invalid user ID, password or user : " + userId,
                ErrorCodes.INVALID_LOGIN);
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
     * @param adminUser    the user initiating the addition - must belong to the ADMIN role
     */
    public void createUser(User userToCreate, User adminUser) {

        // does user has authorization to add a new user
        if (!adminUser.isAuthorized(Permission.USER_MANAGEMENT)) {
            handleRGDRuntimeException(adminUser.getUserName() + " doesn't have permission to create a user",
                    ErrorCodes.AUTHORIZATION_FAILS_USER_MANAGEMENT);
        } else {
            try {
                // get Calendar with current date
                java.util.GregorianCalendar gCal = new GregorianCalendar();
                // get yesterday's date
                gCal.add(Calendar.DATE, -1);
                userToCreate.setPasswordExpiry(gCal.getTime());

                //adding new default password
                userToCreate.setPasswordHash(hashPassword(WebConstants.DEFAULT_PASS));
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

    public void updateUser(User userToUpdate, User adminUser) {
        // does user has authorization to add a update user
        if (!adminUser.isAuthorized(Permission.USER_MANAGEMENT)) {
            handleRGDRuntimeException(adminUser.getUserName() + " doesn't have permission to update a user",
                    ErrorCodes.AUTHORIZATION_FAILS_USER_MANAGEMENT);
        } else {

            // we will not let anyone update deleted user accounts
            User existing = userDao.getUserByPK(userToUpdate.getUserId());
            if (existing.getStatus() == User.State.DELETED) {
                logger.error("Attempt to modify deleted account : " + existing.getUserId() +
                        " by : " + adminUser.getUserId() + " denied");
                handleRGDRuntimeException("Attempt to modify deleted account : " + existing.getUserId() +
                        " by : " + adminUser.getUserId() + " denied", ErrorCodes.AUTHORIZATION_FAILS_USER_MANAGEMENT);
            }
            userDao.updateUser(userToUpdate);
        }
    }

    public void deleteUser(User userToDelete, User adminUser) {
        userToDelete.setStatus(User.State.DELETED);
        updateUser(userToDelete, adminUser);
    }

    /**
     * check if the password of user hasbeen expired
     *
     * @param currentDate
     * @param user
     * @return boolen if current date is after exp date
     */
    public boolean checkPasswordExpiryDate(Date currentDate, User user) {
        //     User userDB = userDao.getPasswordExpiryDate(user.getUserId());
        User userDB = userDao.getUserByPK(user.getUserId());
        Date exp = userDB.getPasswordExpiry();
        return currentDate.after(exp);
    }

    public void updatePassword(String newPass, User user) {
        // setting new password expiry date
        // get Calendar with current date
        java.util.GregorianCalendar gCal = new GregorianCalendar();
        // set 30 days infront
        int resetDays = appParaDao.getIntParameter(AppConstants.PASSWORD_EXPIRY_DAYS);
        gCal.add(Calendar.DATE, resetDays);
        user.setPasswordExpiry(gCal.getTime());
        logger.debug("new date set after password change for user : {} new expirydate : {}", user.getUserName(), gCal.getTime());
        //setting new password
        user.setPasswordHash(hashPassword(newPass));
        userDao.changePassword(user);
        logger.info("updating password for user : {} ", user.getUserName());
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

    public List<User> getUsersByIDMatch(String userId) {
        return userDao.getUsersByIDMatch(userId);
    }

    public List<User> getUsersByNameMatch(String userName) {
        return userDao.getUsersByNameMatch(userName);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
}