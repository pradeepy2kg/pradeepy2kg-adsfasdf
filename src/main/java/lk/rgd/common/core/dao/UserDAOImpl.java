package lk.rgd.common.core.dao;

import lk.rgd.common.api.dao.UserDAO;
import lk.rgd.common.api.domain.District;
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

import javax.persistence.Query;
import java.util.List;

/**
 * @author asankha
 */
public class UserDAOImpl extends BaseDAO implements UserDAO {

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public User getUserByPK(String userId) {
        logger.debug("Loading user : {}", userId);
        return em.find(User.class, userId);
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> getUsersByRole(String roleId) {
        Query q = em.createNamedQuery("filter.by.roleid");
        q.setParameter("roleId", roleId);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> getUsersByAssignedBDDistrict(District assignedBDDistrict) {
        Query q = em.createNamedQuery("filter.by.bd_district");
        q.setParameter("assignedBDDistrict", assignedBDDistrict);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> getUsersByAssignedMRDistrict(District assignedMRDistrict) {
        Query q = em.createNamedQuery("filter.by.mr_district");
        q.setParameter("assignedMRDistrict", assignedMRDistrict);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user) {
        em.persist(user);
    }
}

