package lk.rgd.common.core.dao;

import lk.rgd.common.api.dao.RoleDAO;
import lk.rgd.common.api.domain.Role;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author asankha
 */
public class RoleDAOImpl extends BaseDAO implements RoleDAO {

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public Role getRole(String roleId) {
        logger.debug("Loading role : {}", roleId);
        return em.find(Role.class, roleId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(Role role) {
        logger.debug("Saving role : {}", role.getRoleId());
        //Role merged = em.merge(role);
        em.merge(role);
    }
}
