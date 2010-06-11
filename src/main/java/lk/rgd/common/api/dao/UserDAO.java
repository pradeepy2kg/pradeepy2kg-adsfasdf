package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.User;

/**
 * @author asankha
 */
public interface UserDAO {

    public User getUser(String username);

    public void addUser(User user);


}
