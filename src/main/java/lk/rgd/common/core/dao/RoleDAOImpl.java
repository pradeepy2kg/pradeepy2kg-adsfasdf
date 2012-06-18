package lk.rgd.common.core.dao;

import lk.rgd.common.api.dao.RoleDAO;
import lk.rgd.common.api.domain.Role;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.Query;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * @author asankha
 */
public class RoleDAOImpl extends BaseDAO implements RoleDAO, PreloadableDAO {
    private final Map<String, String> roleListByRoleId = new HashMap<String, String>();
    private List<Role> roleList = null;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Role getRole(String roleId) {
        logger.debug("Loading role : {}", roleId);
        return em.find(Role.class, roleId);
    }

    @Transactional(propagation = Propagation.REQUIRED)  // this is called by the Database initializer, and hence this propagation
    public void save(Role role) {
        logger.debug("Saving role : {}", role.getRoleId());
        //Role merged = em.merge(role);
        em.merge(role);
    }


    public List<Role> getRoles() {
        return roleList;
    }

    public Map<String, String> getRoleList() {
        return roleListByRoleId;
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public void preload() {
        Query query = em.createQuery("SELECT r FROM Role r");
        List<Role> roleList = query.getResultList();
        for (Role r : roleList) {
            String roleId = r.getRoleId();
            String name = r.getName();
            //BitSet perms = r.getPermBitSet();
            //int status = r.getCurrentStatus();

            roleListByRoleId.put(roleId, name);
        }
    }
}