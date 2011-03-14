package lk.rgd.common.core.dao;

import lk.rgd.common.api.dao.UserDAO;
import lk.rgd.common.api.domain.*;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * @author asankha
 */
public class UserDAOImpl extends BaseDAO implements UserDAO {

    @Transactional(propagation = Propagation.SUPPORTS)
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

    @Transactional(propagation = Propagation.MANDATORY)
    public void changePassword(User user) {
        em.merge(user);
    }


    @Transactional(propagation = Propagation.MANDATORY)
    public void addUser(User user, User admin) {
        user.getLifeCycleInfo().setCreatedUser(admin);
        user.getLifeCycleInfo().setLastUpdatedUser(admin);
        user.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        user.getLifeCycleInfo().setCreatedTimestamp(new Date());
        em.persist(user);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void updateUser(User user, User admin) {
        user.getLifeCycleInfo().setLastUpdatedUser(admin);
        user.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        em.merge(user);
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<String> getDEOsByDSDivision(String language, User user, DSDivision dsDivision, Role role) {
        Query q = em.createNamedQuery("filter.deo.by.dsdivision");
        q.setParameter("assignedBDDSDivisions", dsDivision);
        q.setParameter("role", role);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<String> getADRsByDistrictId(District district, Role role) {
        Query q = em.createNamedQuery("filter.adr.by.district");
        q.setParameter("assignedBDDistricts", district);
        q.setParameter("role", role);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> getUserByUserIdOrName(String name) {
        Query q = em.createNamedQuery("get.user.by.name.id");
        q.setParameter("name", "%" + name + "%");
        return q.getResultList();
    }
}

