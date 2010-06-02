package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.Role;

/**
 * @author asankha
 */
public interface RoleDAO {

    public Role getRole(String roleId);
    public void save(Role role);
}
