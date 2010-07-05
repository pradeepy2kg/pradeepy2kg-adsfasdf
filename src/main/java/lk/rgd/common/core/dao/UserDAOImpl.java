package lk.rgd.common.core.dao;

import lk.rgd.common.api.dao.UserDAO;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> getUsersByRoleAndAssignedBDDistrict(Role role, District assignedBDDistrict) {
        Query q = em.createNamedQuery("filter.by.role_and_bd_district");
        q.setParameter("role", role);
        q.setParameter("assignedBDDistrict", assignedBDDistrict);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> getUsersByRoleAndAssignedMRDistrict(Role role, District assignedMRDistrict) {
        Query q = em.createNamedQuery("filter.by.role_and_mr_district");
        q.setParameter("role", role);
        q.setParameter("assignedMRDistrict", assignedMRDistrict);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> getUsersByIDMatch(String userId) {
        Query q = em.createNamedQuery("filter.by.wildcard_id");
        q.setParameter("userIdMatch", "%" + userId + "%");
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> getUsersByNameMatch(String userName) {
        Query q = em.createNamedQuery("filter.by.wildcard_name");
        q.setParameter("userNameMatch", "%" + userName + "%");
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> getAllUsers() {
        Query q = em.createNamedQuery("filter.non.deleted");
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void changePassword(User user) {
        //todo update  password
        em.merge(user);
        //To change body of implemented methods use File | Settings | File Templates.
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user) {
        em.persist(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        em.merge(user);
    }
}

