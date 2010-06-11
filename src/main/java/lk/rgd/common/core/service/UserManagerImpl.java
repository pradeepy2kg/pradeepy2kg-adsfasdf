package lk.rgd.common.core.service;

import lk.rgd.common.RGDRuntimeException;
import lk.rgd.common.api.dao.RoleDAO;
import lk.rgd.common.api.dao.UserDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.util.Base64;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.ErrorCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;


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

    public User authenticateUser(String userName, String password) throws AuthorizationException {
        User user = userDao.getUser(userName);
        if (password != null && user != null && user.getPasswordHash() != null) {
            MessageDigest sha = null;
            try {
                sha = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                logger.warn("Cannot instantiate a SHA-1 message digest", e);
                throw new RGDRuntimeException("Cannot instantiate a SHA-1 message digest", e);
            }
            sha.reset();
            sha.update(password.getBytes());
            if (user.getPasswordHash().equals(new String(Base64.encode(sha.digest())))) {
                return user;
            }
        }
        logger.warn("Invalid user ID or password for user : " + userName);
        throw new AuthorizationException("Invalid user ID or password for user : " + userName,
                AuthorizationException.INVALID_USER_OR_PASSWORD);
    }

    /**
     * @param user
     * @param authanticatedUser
     */
    public void creatUser(User user, User authanticatedUser) {

        //does user has authraization to add a new user
        checkAuthrization(authanticatedUser);
        //persisting data
        try {
            userDao.addUser(user);
        }
        catch (Exception e) {
            SQLException sql = (SQLException) e.getCause();
            handleRGDRuntimeException("persistance exception catched", ErrorCodes.PERSISTING_EXCEPTION_COMMON);

        }
        logger.info(user.getUserName() + ": inserted to data base by" + authanticatedUser.getUserName());
        logger.info("new user " + user.getUserName() + "created by :" + authanticatedUser.getUserName());
    }


    private void handleRGDRuntimeException(String message, int code) {
        logger.error(message);
        throw new RGDRuntimeException(message, code);
    }

    private void handleCRSRuntimeException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);

    }

    private void checkAuthrization(User user) {
        logger.info(user.getUserName() + ": user authanticated");
        if (!user.isAuthorized(2)) {
            handleRGDRuntimeException(user.getUserName() + " doesnt have permission to creat user", ErrorCodes.AUTHRIZATION_FAILS_CREATE_USER);
        }

    }

}
