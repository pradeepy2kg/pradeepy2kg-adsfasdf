package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.Role;

import java.util.List;
import java.util.Map;


/**
 * @author asankha
 */
public interface RoleDAO {

    public Role getRole(String roleId);
    public void save(Role role);

    public List<Role> getRoles();
    public Map<String, String> getRoleList();
}
