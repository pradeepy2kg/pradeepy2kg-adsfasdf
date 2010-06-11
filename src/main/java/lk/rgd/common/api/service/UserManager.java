package lk.rgd.common.api.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.core.AuthorizationException;

import javax.servlet.http.HttpSession;
import java.util.Set;

/**
 * @author asankha
 */
public interface UserManager {
    User authenticateUser(String userName, String password) throws AuthorizationException;

    void creatUser(User user, User authanticatedUser) throws AuthorizationException;
}
