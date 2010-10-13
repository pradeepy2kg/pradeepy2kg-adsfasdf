package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.Link;
import lk.rgd.crs.web.Menu;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.Permission;

/**
 * @author Indunil Moremada
 * @authar amith jayasekara
 * action class which handles the login and logout actions
 * of the EPR system
 */
public class
        LoginAction extends ActionSupport implements SessionAware {
    private String userName;
    private String password;
    private Map session;
    private final UserManager userManager;
    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);

    //for home page charts
    private int totalDeclarations;
    private int totalDecArrivals;
    private int approvalPendings;
    private int totalConfirmChages;
    private int confirmApproved;
    private int confirmApprovedPending;
    private int confirmPrinted;
    private int confimPrintingPending;
    private int BCprinted;
    private int BCPrintPendings;
    private int stillBirths;
    private int SBPendingApprovals;

    public LoginAction(UserManager userManager) {
        this.userManager = userManager;
    }

    public Locale getLocale() {
        return (Locale) session.get(WebConstants.SESSION_USER_LANG);
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

        logger.debug("detected userName : {} ", userName);
        User user;
        try {
            user = userManager.authenticateUser(userName, userManager.hashPassword(password));
        } catch (AuthorizationException e) {
            addActionError("Incorrect username or password.");
            logger.error("{} : {}", e.getMessage(), e);
            return "error";
        }

        try {
            String language = user.getPrefLanguage();
            String country = "LK";
            if (language.equals("en")) {
                country = "US";
            }
            Map map = Menu.getAllowedLinks(user.getRole());
            logger.debug("size of allowed links map : {}", map.size());

            if (map != null) {
                session.put(WebConstants.SESSION_USER_MENUE_LIST, map);
            } else {
                return "error";
            }

            session.put(WebConstants.SESSION_USER_LANG, new Locale(language, country));
            session.put(WebConstants.SESSION_USER_BEAN, user);
            logger.debug(" user {} logged in. language {}", userName, language);
            String userRole = user.getRole().getRoleId();
            logger.debug("Role of the {} is :{}", user.getUserName(), userRole);

            totalDeclarations = 10;
            totalDecArrivals = 11;
            approvalPendings = 12;
            totalConfirmChages = 13;
            confirmApproved = 14;
            confirmApprovedPending = 15;
            confirmPrinted = 16;
            confimPrintingPending = 17;
            BCprinted = 18;
            BCPrintPendings = 19;
            stillBirths = 20;
            SBPendingApprovals = 21;

            String result = checkUserExpiry(user);
            if (result.equals(SUCCESS))
                return result + userRole;
            else
                return result;
        } catch (Exception e) {
            logger.error("Exception is :P {} {} ", e, e.toString());
            return "error";
        }
    }

    /**
     * use to check user's status ex : blocked ,new user, password expired...
     *
     * @param user
     * @return
     */
    private String checkUserExpiry(User user) {
        //todo change :::::user staus chcking goes here
        // get Calendar with current date
        java.util.GregorianCalendar gCal = new GregorianCalendar();

        if (gCal.getTime().after(user.getPasswordExpiry())) {
            logger.warn("password has been expired for user :{}", user.getUserName());
            return "expired";
        }
        // if status are OK
        logger.debug("users status OK");
        return SUCCESS;

    }

    /**
     * logout action which invalidates the session of the user
     *                   https://192.168.1.6:9080
     * @return String
     */
    public String logout() {
        if (session.containsKey(WebConstants.SESSION_USER_BEAN)) {
            logger.debug("Inside logout : {} is going to logout.", ((User) session.get(WebConstants.SESSION_USER_BEAN)).getUserName());

            session.put(WebConstants.SESSION_USER_BEAN, null);
            if (session instanceof org.apache.struts2.dispatcher.SessionMap) {
                try {
                    ((org.apache.struts2.dispatcher.SessionMap) session).invalidate();
                    logger.debug("Session invalidated");
                } catch (IllegalStateException e) {
                    logger.error("Incorrect Session", e);
                }
            } else {
                session = null;
            }
            userManager.logoutUser(userName);
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
        logger.debug("Set session {}", map);
        this.session = map;
    }

    public Map getSession() {
        return session;
    }

    public int getTotalDeclarations() {
        return totalDeclarations;
    }

    public void setTotalDeclarations(int totalDeclarations) {
        this.totalDeclarations = totalDeclarations;
    }

    public int getTotalDecArrivals() {
        return totalDecArrivals;
    }

    public void setTotalDecArrivals(int totalDecArrivals) {
        this.totalDecArrivals = totalDecArrivals;
    }

    public int getApprovalPendings() {
        return approvalPendings;
    }

    public void setApprovalPendings(int approvalPendings) {
        this.approvalPendings = approvalPendings;
    }

    public int getTotalConfirmChages() {
        return totalConfirmChages;
    }

    public void setTotalConfirmChages(int totalConfirmChages) {
        this.totalConfirmChages = totalConfirmChages;
    }

    public int getConfirmApproved() {
        return confirmApproved;
    }

    public void setConfirmApproved(int confirmApproved) {
        this.confirmApproved = confirmApproved;
    }

    public int getConfirmApprovedPending() {
        return confirmApprovedPending;
    }

    public void setConfirmApprovedPending(int confirmApprovedPending) {
        this.confirmApprovedPending = confirmApprovedPending;
    }

    public int getConfirmPrinted() {
        return confirmPrinted;
    }

    public void setConfirmPrinted(int confirmPrinted) {
        this.confirmPrinted = confirmPrinted;
    }

    public int getConfimPrintingPending() {
        return confimPrintingPending;
    }

    public void setConfimPrintingPending(int confimPrintingPending) {
        this.confimPrintingPending = confimPrintingPending;
    }

    public int getBCprinted() {
        return BCprinted;
    }

    public void setBCprinted(int BCprinted) {
        this.BCprinted = BCprinted;
    }

    public int getBCPrintPendings() {
        return BCPrintPendings;
    }

    public void setBCPrintPendings(int BCPrintPendings) {
        this.BCPrintPendings = BCPrintPendings;
    }

    public int getStillBirths() {
        return stillBirths;
    }

    public void setStillBirths(int stillBirths) {
        this.stillBirths = stillBirths;
    }

    public int getSBPendingApprovals() {
        return SBPendingApprovals;
    }

    public void setSBPendingApprovals(int SBPendingApprovals) {
        this.SBPendingApprovals = SBPendingApprovals;
    }
}
