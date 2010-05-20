package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import lk.rgd.crs.web.util.LoginBD;
import lk.rgd.crs.web.WebConstants;

/**
 * Created by IntelliJ IDEA.
 * User: duminda
 * Date: May 20, 2010
 * Time: 11:37:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class LoginAction extends ActionSupport implements SessionAware {

    private String userName;
    private String password;
    private Map session;

    private LoginBD loginBD = new LoginBD();
    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterAction.class);
    private UserPreferencesAction userPreferencesAction = new UserPreferencesAction();

    /*
   *  User LoginAction of the EPR System.
   * */
    public String login() {
        if (loginBD.login(userName, password)) {
            userPreferencesAction.setLanguage(loginBD.getLanguage(userName));
            logger.debug("inside login : {} is prefered.", userPreferencesAction.getLanguage());
            session.put(WebConstants.SESSION_USER_LANG, userPreferencesAction.getLanguage());
            session.put(WebConstants.SESSION_USER_NAME, userName);
            return "success";
        }
        return "error";
    }

    /**
     * User Logout of the EPR System.
     * */
    public String logout(){
        if(session.containsKey(WebConstants.SESSION_USER_NAME)){
            logger.debug("Inside logout : {} is going to logout.", session.get(WebConstants.SESSION_USER_NAME).toString());
            session.remove(WebConstants.SESSION_USER_NAME);
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
}
