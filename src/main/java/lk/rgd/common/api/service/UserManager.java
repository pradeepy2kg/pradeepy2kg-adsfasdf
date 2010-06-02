package lk.rgd.common.api.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.AuthorizationException;

/**
 * @author asankha
 */
public interface UserManager {
    User authenticateUser(String userName, String password) throws AuthorizationException;
}
