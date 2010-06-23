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
import lk.rgd.common.core.AuthorizationException;

/**
 * @author Indunil Moremada
 *         amith jayasekara
 *         action class which handles the login and logout actions
 *         of the EPR system
 */
public class LoginAction extends ActionSupport implements SessionAware {
    public static Map<Integer, Link> linkPermission = new HashMap<Integer, Link>();
    public Map<Integer, Link> allowed = new HashMap<Integer, Link>();
    private List userRoles;
    private String userName;
    private String password;
    private Map session;
    private final UserManager userManager;
    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);
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
            // change password and continue
            User user = userManager.authenticateUser(userName, password);
            String language = user.getPrefLanguage();
            String country = "LK";
            if (language.equals("en")) {
                country = "US";
            }
            HashMap map = (HashMap) allowedLinks(user);
            if (map != null) {
                session.put(WebConstants.SESSION_USER_MENUE_LIST, map);
            } else {
                return "error";
            }
            session.put(WebConstants.SESSION_USER_LANG, new Locale(language, country));
            session.put(WebConstants.SESSION_USER_BEAN, user);
            session.put("page_title", "home");
            logger.debug(" user {} logged in. language {}", userName, language);
            return checkUserStatus(user);
            // return "success";
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


        for (Map.Entry<Integer, Link> e : linkPermission.entrySet()) {
            if (user.isAuthorized(e.getKey())) {
                allowed.put(e.getKey(), e.getValue());
            }
        }
        return allowed;
    }

    /**
     * use to check user's status ex : blocked ,new user, password expired...
     *
     * @param user
     * @return
     */
    private String checkUserStatus(User user) {
        //todo change :::::user staus chcking goes here
        // get Calendar with current date
        java.util.GregorianCalendar gCal = new GregorianCalendar();

        if (gCal.getTime().after(user.getPasswordExpiry())) {
            logger.warn("password has been expired for user :{}", user.getUserName());
            return "expired";
        }
        // if status are OK
        logger.info("users status OK------");
        return "success";

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


    static {                                                                              //prpertyKey,link,action
        linkPermission.put(6, new Link("creat_user.label", "/WEB-INF/pages/CreatUser.jsp", "eprInitUserCreation.do"));
        linkPermission.put(7, new Link("birth_registration.label", "/WEB-INF/pages/BirthRegistrationForm1.jsp", "eprBirthRegistration.do"));
        linkPermission.put(8, new Link("birth_conformation_report.label", "/WEB-INF/pages/Welcome.jsp", "eprBirthConfirmationReport.do"));
        linkPermission.put(9, new Link("birth_confirmation_print.label", "/WEB-INF/pages/BirthConfirmationPrintForm.jsp", "eprFilterBirthConfirmPrint.do"));
        linkPermission.put(10, new Link("birth_confirmation.label", "/WEB-INF/pages/BirthConfirmationForm1.jsp", "eprBirthConfirmation.do"));
        linkPermission.put(11, new Link("birth_register_approval.label", "/WEB-INF/pages/BirthRegisterApproval.jsp", "eprBirthRegisterApproval.do"));
        linkPermission.put(12, new Link("userPreference.label", "/WEB-INF/pages/UserPreferences.jsp", "eprUserPreferencesInit.do"));
        linkPermission.put(13, new Link("viewUsers.label", "/WEB-INF/pages/viewUsers.jsp", "eprViewUsers.do"));
        linkPermission.put(14, new Link("search.label", "/WEB-INF/pages/SearchBDF.jsp", "eprSearchPageLoad.do"));
        linkPermission.put(15, new Link("birth_confirmation_approval.label", "/WEB-INF/pages/BirthConfirmationApproval.jsp", "eprBirthConfirmationApproval.do?confirmationApprovalFlag=true"));
    }
}
