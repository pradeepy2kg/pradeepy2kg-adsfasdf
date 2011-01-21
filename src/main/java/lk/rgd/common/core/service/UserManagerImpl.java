package lk.rgd.common.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.RGDRuntimeException;
import lk.rgd.common.api.Auditable;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.*;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.util.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author asankha
 */
public class UserManagerImpl implements UserManager {

    private static final Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);

    private final UserDAO userDao;
    private final LocationDAO locationDao;
    private final UserLocationDAO userLocationDao;
    private final RoleDAO roleDao;
    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final AppParametersDAO appParaDao;
    private static final String SYSTEM_USER_NAME = "system";

    public UserManagerImpl(UserDAO userDao, RoleDAO roleDao, AppParametersDAO appParaDao,
        LocationDAO locationDao, UserLocationDAO userLocationDao, DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.appParaDao = appParaDao;
        this.locationDao = locationDao;
        this.userLocationDao = userLocationDao;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Auditable
    public User authenticateUser(String userId, String password) throws AuthorizationException {
        User user = userDao.getUserByPK(userId);
        if (user != null && user.getStatus() == User.State.ACTIVE && user.getLifeCycleInfo().isActive()
            && password != null && user.getPasswordHash() != null
            && !SYSTEM_USER_NAME.equalsIgnoreCase(userId)) {
            if (user.getPasswordHash().equals(password) || user.getPasswordHash().equals(hashPassword(password))) {
                return user;
            } else {
                logger.debug("Password mismatch for user : {}", userId);
            }
        }
        logger.warn("Attempt to authenticate non-existent or inactive user : {} or empty password", userId);
        throw new AuthorizationException("Invalid user ID, password or user : " + userId, ErrorCodes.INVALID_LOGIN);
    }

    /**
     * @inheritDoc
     */
    @Auditable
    public void logoutUser(String userId) {
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
        return HashUtil.hashString(password);
    }

    /**
     * todo remove
     *
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void createUser(User userToCreate, User adminUser) {

        // does user has authorization to add a new user
        if (!adminUser.isAuthorized(Permission.USER_MANAGEMENT)) {
            handleException(adminUser.getUserName() + " doesn't have permission to create a user",
                ErrorCodes.AUTHORIZATION_FAILS_USER_MANAGEMENT);

        } else {
            // get Calendar with current date
            java.util.GregorianCalendar gCal = new GregorianCalendar();
            // get yesterday's date
            gCal.add(Calendar.DATE, -1);
            userToCreate.setPasswordExpiry(gCal.getTime());

            // adding new default password
            User user = userDao.getUserByPK(userToCreate.getUserId());
            if (user != null) {
                handleException("User Name is already assigned", ErrorCodes.ENTITY_ALREADY_EXIST);
                logger.debug("User already assigned");
            } else {
                userDao.addUser(userToCreate, adminUser);
            }
            logger.debug("New user {} created by : {}", userToCreate.getUserName(), adminUser.getUserName());

        }
    }

    /**
     * @param user
     * @param userId
     * @param roleId
     * @param adminUser
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean createUser(User user, User adminUser, String userId, String roleId, int[] assignedDistricts, int[] assDivisions, boolean changePassword, String randomPassword) {

        boolean isNewUser = false;

        if (!adminUser.isAuthorized(Permission.USER_MANAGEMENT)) {
            handleException(adminUser.getUserName() + " doesn't have permission to create a user",
                ErrorCodes.AUTHORIZATION_FAILS_USER_MANAGEMENT);

        } else {
            Set<District> assDistrict = new HashSet<District>();
            Set<DSDivision> assDSDivision = new HashSet<DSDivision>();

            for (int districtUKey : assignedDistricts) {
                assDistrict.add(districtDAO.getDistrict(districtUKey));
            }

            if (roleId.equals(Role.ROLE_DEO) || roleId.equals(Role.ROLE_ADR)) {
                for (int dsDivisionId : assDivisions) {
                    assDSDivision.add(dsDivisionDAO.getDSDivisionByPK(dsDivisionId));
                }
            }
            if (roleId.equals(Role.ROLE_DR) || roleId.equals(Role.ROLE_ARG)) {
                for (int districtUKey : assignedDistricts) {
                    assDSDivision.addAll(dsDivisionDAO.getAllDSDivisionByDistrictKey(districtUKey));
                }
            }

            user.setStatus(User.State.INACTIVE);

            if (userId == null) {
                isNewUser = true;
                /*randomPassword = getRandomPassword(randomPasswordLength);*/
                user.setPasswordHash(hashPassword(randomPassword));
                user.setAssignedBDDistricts(assDistrict);
                user.setAssignedMRDistricts(assDistrict);
                user.setAssignedBDDSDivisions(assDSDivision);
                user.setAssignedMRDSDivisions(assDSDivision);
                user.setRole(roleDao.getRole(roleId));

                java.util.GregorianCalendar gCal = new GregorianCalendar();
                gCal.add(Calendar.DATE, -1);
                user.setPasswordExpiry(gCal.getTime());

                User checkDuplicate = userDao.getUserByPK(user.getUserId());
                if (checkDuplicate != null) {
                    handleException("User Name is already assigned", ErrorCodes.ENTITY_ALREADY_EXIST);
                } else {
                    userDao.addUser(user, adminUser);
                }

            } else {
                isNewUser = false;
                User updatedUser = userDao.getUserByPK(userId);

                updatedUser.setUserName(user.getUserName());
                updatedUser.setPin(user.getPin());
                updatedUser.setSienSignatureText(user.getSienSignatureText());
                updatedUser.setTaenSignatureText(user.getTaenSignatureText());
                updatedUser.setPrefLanguage(user.getPrefLanguage());

                updatedUser.setAssignedBDDistricts(assDistrict);
                updatedUser.setAssignedMRDistricts(assDistrict);
                updatedUser.setAssignedBDDSDivisions(assDSDivision);
                updatedUser.setAssignedMRDSDivisions(assDSDivision);
                updatedUser.setRole(roleDao.getRole(roleId));

                if (!updatedUser.getLocations().isEmpty()) {
                    updatedUser.setStatus(User.State.ACTIVE);
                } else {
                    updatedUser.setStatus(User.State.INACTIVE);
                }
                if (changePassword) {
                    logger.debug("Change password {}", userDao.getUserByPK(userId).getUserName());
                    /*randomPassword = getRandomPassword(randomPasswordLength);*/
                    updatedUser.setPasswordHash(hashPassword(randomPassword));
                }

                User existing = userDao.getUserByPK(userId);
                if (existing.getStatus() == User.State.DELETED) {
                    handleException("Attempt to modify deleted account : " + existing.getUserId() +
                        " by : " + adminUser.getUserId() + " denied", ErrorCodes.AUTHORIZATION_FAILS_USER_MANAGEMENT);
                }
                userDao.updateUser(updatedUser, adminUser);

            }
        }
        return isNewUser;

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

    @Transactional(propagation = Propagation.REQUIRED)
    public void activeUserLocation(String userId, int locationId, User adminUser) {
        UserLocation existing = userLocationDao.getUserLocation(userId, locationId);
        if (!adminUser.isAuthorized(Permission.USER_MANAGEMENT)) {
            handleException("User : " + adminUser.getUserId() + " is not allowed to Active user Location",
                ErrorCodes.PERMISSION_DENIED);
        }
        if (existing != null) {
            existing.getLifeCycleInfo().setActive(true);
            updateUserLocation(existing, adminUser);
            logger.debug("Active user location of location id {} of user id :{}", locationId, userId);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void inactiveUserLocation(String userId, int locationId, User adminUser) {
        UserLocation existing = userLocationDao.getUserLocation(userId, locationId);
        if (!adminUser.isAuthorized(Permission.USER_MANAGEMENT)) {
            handleException("User : " + adminUser.getUserId() + " is not allowed to Inactive user Location",
                ErrorCodes.PERMISSION_DENIED);
        }
        existing.getLifeCycleInfo().setActive(false);
        updateUserLocation(existing, adminUser);
        logger.debug("Inactive user location of location id {} of user id :{}", locationId, userId);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User userToUpdate, User adminUser) {
        // if one user tries to update another user, does the former have authorization to update a user
        if (!((adminUser.equals(userToUpdate) || adminUser.isAuthorized(Permission.USER_MANAGEMENT)))) {
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

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User userToUpdate) {
        User adminUser = getSystemUser();
        User existing = userDao.getUserByPK(userToUpdate.getUserId());
        if (existing.getStatus() == User.State.DELETED) {
            handleException("Attempt to modify deleted account : " + existing.getUserId() +
                " by : " + adminUser.getUserId() + " denied", ErrorCodes.AUTHORIZATION_FAILS_USER_MANAGEMENT);
        }
        userDao.updateUser(userToUpdate, adminUser);
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
            userDao.updateUser(userToDelete, adminUser);
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