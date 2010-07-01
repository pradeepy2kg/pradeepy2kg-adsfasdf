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
import lk.rgd.Permission;

/**
 * @author Indunil Moremada
 *         amith jayasekara
 *         action class which handles the login and logout actions
 *         of the EPR system
 */
public class LoginAction extends ActionSupport implements SessionAware {
    public static Map<Integer, Link> linkPermission = new HashMap<Integer, Link>();
    public Map<String, Map> allowedLinks = new HashMap<String, Map>();
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
    private Map<String, Map> allowedLinks(User user) {
        Map birthLink = new HashMap();
        Map deathLink = new HashMap();
        Map marrageLink = new HashMap();
        Map reportLink = new HashMap();
        Map adminLink = new HashMap();
        Map preferanceLink = new HashMap();

        for (Map.Entry<Integer, Link> e : linkPermission.entrySet()) {
            if (user.isAuthorized(e.getKey())) {
                if (e.getValue().getCategory().equals("0")) {
                    birthLink.put(e.getKey(), e.getValue());
                } else if (e.getValue().getCategory().equals("2")) {
                    deathLink.put(e.getKey(), e.getValue());
                } else if (e.getValue().getCategory().equals("1")) {
                    marrageLink.put(e.getKey(), e.getValue());
                } else if (e.getValue().getCategory().equals("4")) {
                    reportLink.put(e.getKey(), e.getValue());
                } else if (e.getValue().getCategory().equals("5")) {
                    adminLink.put(e.getKey(), e.getValue());
                } else if (e.getValue().getCategory().equals("3")) {
                    preferanceLink.put(e.getKey(), e.getValue());
                }
                //allowedLinks.put(e.getKey(), e.getValue());
            }
        }

        allowedLinks.put("BIRTH", birthLink);
        allowedLinks.put("DEATH", deathLink);
        allowedLinks.put("MARRAGE", marrageLink);
        allowedLinks.put("REPORT", reportLink);
        allowedLinks.put("ADMIN", adminLink);
        allowedLinks.put("PREFERANCE", preferanceLink);

        return allowedLinks;
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


    static {
        //prpertyKey,link,action
        // categories ::::admin task 5 ,reprtts 4 , user preferance 3 ,death 2,marrage 1,birth 0
        linkPermission.put(Permission.PAGE_CREATE_USER, new Link("creat_user.label", "5", "eprInitUserCreation.do"));
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATON, new Link("birth_registration.label", "0", "eprBirthRegistration.do"));
        /*  linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_REPORT, new Link("birth_conformation_report.label", "4", "eprBirthConfirmationReport.do"));*/
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_PRINT, new Link("birth_confirmation_print.label", "4", "eprFilterBirthConfirmPrint.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION, new Link("birth_confirmation.label", "0", "eprBirthConfirmation.do"));
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATION_APPROVAL, new Link("birth_register_approval.label", "0", "eprBirthRegisterApproval.do"));
        linkPermission.put(Permission.PAGE_USER_PREFERANCE_SELECT, new Link("userPreference.label", "3", "eprUserPreferencesInit.do"));
        linkPermission.put(Permission.PAGE_VIEW_USERS, new Link("viewUsers.label", "5", "eprViewUsers.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_SEARCH, new Link("search.label", "0", "eprSearchPageLoad.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_APPROVAL, new Link("birth_confirmation_approval.label", "0", "eprBirthConfirmationApproval.do?confirmationApprovalFlag=true"));
        linkPermission.put(Permission.PAGE_BIRTH_CERTIFICATE_PRINT, new Link("print_birthcertificate.label", "4", "eprBirthCetificateList.do"));

    }
}
