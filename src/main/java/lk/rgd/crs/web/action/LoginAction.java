package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.Link;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.core.AuthorizationException;

/**
 * @author Indunil Moremada
 *         amith jayasekara
 *         action class which handles the login and logout actions
 *         of the EPR system
 */
public class LoginAction extends ActionSupport implements SessionAware {
    public static Map<Integer, Link> linkPermission = new HashMap<Integer, Link>();
    private List userRoles;
    private String userName;
    private String password;
    private Map session;
    private final UserManager userManager;
    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);
    //private UserPreferencesAction userPreferencesAction = new UserPreferencesAction();

    public LoginAction(UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * Handles the login process of the EPR system
     * if login is success user is redirected
     * home page otherwise he is redirected to
     * the login page
     *
     * @return String
     */
    public String login() {
        logger.debug("detected useName : {} and password : {}", userName, password);
        try {
            User user = userManager.authenticateUser(userName, password);
            String language = user.getPrefLanguage();
            String country = "LK";
            if (language.equals("en")) {
                country = "US";
            }
            session.put(WebConstants.SESSION_USER_LANG, new Locale(language, country));
            session.put(WebConstants.SESSION_USER_BEAN, user);
            session.put("page_title", "home");
            logger.debug(" user {} logged in. language {}", userName, language);
            return "success";
        } catch (AuthorizationException e) {
            logger.error("{} : {}", e.getMessage(), e);
            return "error";
        }
    }

    /**
     * @param user
     * @return
     */
    private Map<Integer, Link> allowedLinks(User user) {
        Map<Integer, Link> allowed = new HashMap<Integer, Link>();
        Set keySet = linkPermission.keySet();
        Iterator itr = keySet.iterator();
        while (itr.hasNext()) {
            int key = (Integer) itr.next();
            // chek permission to link
            if (user.isAuthorized(key)) {
                Link link = (Link) linkPermission.get(key);
                allowed.put(key, link);
                return allowed;
            }
        }
        return null;
    }

    /**
     * logout action whch invalidate the session of
     * the user
     *
     * @return String
     */
    public String logout() {
        if (session.containsKey(WebConstants.SESSION_USER_BEAN)) {
            logger.debug("Inside logout : {} is going to logout.", ((User) session.get(WebConstants.SESSION_USER_BEAN)).getUserName());
            session.remove(WebConstants.SESSION_USER_BEAN);
            return "success";
        }
        return "error";
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setSession(Map map) {
        this.session = map;
    }

    static {
        linkPermission.put(6, new Link("creat_user.label", "/WEB-INF/pages/CreatUser.jsp"));
    }
}
