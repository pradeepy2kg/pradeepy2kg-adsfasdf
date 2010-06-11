package lk.rgd.common.core.dao;

import lk.rgd.common.api.dao.UserDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.RGDRuntimeException;
import lk.rgd.crs.ErrorCodes;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;

/**
 * @author asankha
 */
public class UserDAOImpl extends BaseDAO implements UserDAO {

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public User getUser(String userName) {
        logger.debug("Loading user : {}", userName);
        return em.find(User.class, userName);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DataAccessException.class)
    public void addUser(User user) {
        logger.info("Persisting a new user.......");
        em.persist(user);
    }

}

