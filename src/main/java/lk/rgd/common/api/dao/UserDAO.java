package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.User;

import java.util.List;

/**
 * @author asankha
 */
public interface UserDAO {

    public User getUser(String username);

    public List<User> getUsersByRole(String roleId);

    public void addUser(User user);
}
