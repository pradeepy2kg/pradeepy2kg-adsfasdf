package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.AppConstants;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.*;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.crs.web.Menu;
import lk.rgd.crs.web.WebConstants;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Indunil Moremada
 * @author amith jayasekara
 *         action class which handles the login and logout actions
 *         of the EPR system
 */
public class LoginAction extends ActionSupport implements SessionAware {

    private final AppParametersDAO appParaDao;
    private String userName;
    private String javaScript;
    private String password;
    private Map session;
    private final UserManager userManager;
    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);

    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;

    //for home page charts
    private int totalDeclarations;
    private int totalDecArrivals;
    private int approvalPending;
    private int totalConfirmChanges;
    private int confirmApproved;
    private int confirmApprovedPending;
    private int confirmPrinted;
    private int confirmPrintingPending;
    private int BCprinted;
    private int BCPrintPending;
    private int stillBirths;
    private int SBPendingApprovals;

    private Map<Integer, String> districtList;
    private Map<Integer, String> divisionList;

    private List<String> deoList;
    private List<String> adrList;

    private int dsDivisionId;
    private int districtId;
    private int deoUserId;
    private int adrUserId;
    private boolean fromLogin;

    private String startDate;
    private String endDate;

    private Statistics statistics;
    private String role;

    public LoginAction(UserManager userManager, AppParametersDAO appParaDao, DistrictDAO districtDAO,
                       DSDivisionDAO dsDivisionDAO, UserDAO userDAO, RoleDAO roleDAO) {
        this.userManager = userManager;
        this.appParaDao = appParaDao;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }

    public Locale getLocale() {
        return (Locale) session.get(WebConstants.SESSION_USER_LANG);
    }

    /**
     * Handles the login process of the EPR system
     * if login is success user is redirected to the
     * his own home page otherwise he is redirected
     * to the login page again
     *
     * @return String ERROR or (SUCCESS + USER)
     */
    public String login() {

        if ("false".equals(javaScript)) {
            addActionError("Please enable javaScript in your web browser");
            return ERROR;
        }

        logger.debug("Detected userName : {} ", userName);
        User user;
        try {
            user = userManager.authenticateUser(userName, userManager.hashPassword(password));
            if (user != null) {
                user.setLoginAttempts(1);
                userManager.updateUser(user);
            }
            if (!Role.ROLE_ADMIN.equals(user.getRole().getRoleId()) && !Role.ROLE_RG.equals(user.getRole().getRoleId())) {
                if (user.getAssignedBDDSDivisions() != null && user.getAssignedBDDSDivisions().size() == 0) {
                    addActionError("You are not assign to any DS Division.Contact admin for resolve problem");
                    logger.error("User : {} , doesn't allocate to any DS Division", user.getUserName());
                    return ERROR;
                }
            }
        } catch (AuthorizationException e) {
            user = userManager.getUserByID(userName);
            if (user != null) {
                int loginAttempts = user.getLoginAttempts();
                logger.debug("value of login attempts :{}", loginAttempts);
                int maxLoginAttempts = appParaDao.getIntParameter(AppParameter.MAX_NUMBER_OF_LOGIN_ATTEMPTS);
                logger.debug("value of login attempts :{}", maxLoginAttempts);
                User.State currentState = user.getStatus();
                if (loginAttempts > maxLoginAttempts) {
                    user.getLifeCycleInfo().setActive(false);
                    addActionError("Please contact Admin to Active your Account");
                } else {
                    if (User.State.DELETED.equals(currentState)) {
                        addActionError("Your user account has been deleted. Please contact Administrator");
                    } else {
                        addActionError("Incorrect user name or password");
                        user.setLoginAttempts(loginAttempts + 1);
                    }
                }
                if (!User.State.DELETED.equals(currentState)) {
                    userManager.updateUser(user);
                }
            } else {
                addActionError("Incorrect user name or password");
            }
            logger.error("{} : {}", e.getMessage(), e);
            return ERROR;
        }
        /* user authorization is ok. then ... */
        try {
            String language = user.getPrefLanguage();
            String country = "LK";
            if (AppConstants.ENGLISH.equals(language)) {
                country = "US";
            }
            Map map = Menu.getAllowedLinks(user.getRole());     /* get allowed operations for user */
            logger.debug("size of allowed links map : {}", map.size());

            if (map != null) {
                session.put(WebConstants.SESSION_USER_MENUE_LIST, map);
            } else {
                return ERROR;
            }

            session.put(WebConstants.SESSION_USER_LANG, new Locale(language, country));

            /* stores current user in session */
            session.put(WebConstants.SESSION_USER_BEAN, user);
            logger.debug(" user {} logged in. language {}", userName, language);
            String userRole = user.getRole().getRoleId();
            logger.debug("Role of the {} is :{}", user.getUserName(), userRole);

            /* check if password is expired */
            final String result = checkUserExpiry(user);

            return result;
        } catch (Exception e) {
            logger.error("Exception is :P {} {} ", e, e.toString());
            return ERROR;
        }
    }

    void populateLists(User user, String userType) {

        if (WebConstants.USER_ARG.equals(userType) || WebConstants.USER_RG.equals(userType)) {
            if (districtList == null) {
                districtList = districtDAO.getAllDistrictNames(user.getPrefLanguage(), user);
                logger.debug("district List : {}", districtList.size());
            }

            if (divisionList == null && districtList != null) {
                divisionList = dsDivisionDAO.getAllDSDivisionNames(
                        districtList.keySet().iterator().next(),
                        user.getPrefLanguage(),
                        user);
                logger.debug("division List : {}", divisionList.size());
            } else {
                if (districtList == null) {
                    logger.debug("DistrictList null for user : {}", user.getUserId());
                }
            }

        } else if (WebConstants.USER_ADR.equals(userType.toLowerCase())) {
            if (districtList == null) {
                districtList = districtDAO.getAllDistrictNames(user.getPrefLanguage(), user);
                logger.debug("district List : {}", districtList.size());
            }

            if (divisionList == null && districtList != null) {
                divisionList = dsDivisionDAO.getAllDSDivisionNames(
                        districtList.keySet().iterator().next(),
                        user.getPrefLanguage(),
                        user);
                logger.debug("division List : {}", divisionList.size());
            } else {
                if (districtList == null) {
                    logger.debug("DistrictList null for user : {}", user.getUserId());
                }
            }

            if (deoList == null && divisionList != null) {
                logger.debug("Role = {}", user.getRole());
                deoList = userDAO.getDEOsByDSDivision(
                        user.getPrefLanguage(), user, dsDivisionDAO.getDSDivisionByPK(divisionList.keySet().iterator().next()), roleDAO.getRole("DEO"));
                logger.debug("DEO List : {}", deoList.size());
            }
        } else if (userType.toLowerCase().equals(WebConstants.USER_DR)) {
            if (districtList == null) {
                districtList = districtDAO.getAllDistrictNames(user.getPrefLanguage(), user);
                logger.debug("district List : {}", districtList.size());
            }

            if (divisionList == null && districtList != null) {
                divisionList = dsDivisionDAO.getAllDSDivisionNames(
                        districtList.keySet().iterator().next(),
                        user.getPrefLanguage(),
                        user);
                logger.debug("division List : {}", divisionList.size());
            } else {
                if (districtList == null) {
                    logger.debug("DistrictList null for user : {}", user.getUserId());
                }
            }

            if (adrList == null && divisionList != null) {
                logger.debug("Role = {}", user.getRole());
                adrList = userDAO.getADRsByDistrictId(
                        districtDAO.getDistrict(districtList.keySet().iterator().next()), roleDAO.getRole("ADR"));
                logger.debug("ADR List : {}", adrList.size());
            }
        } else if (userType.toLowerCase().equals(WebConstants.USER_STAT)) {
            if (districtList == null) {
                districtList = districtDAO.getAllDistrictNames(user.getPrefLanguage(), user);
                logger.debug("district List : {}", districtList.size());
            }

            if (divisionList == null && districtList != null) {
                divisionList = dsDivisionDAO.getAllDSDivisionNames(
                        districtList.keySet().iterator().next(),
                        user.getPrefLanguage(),
                        user);
                logger.debug("division List : {}", divisionList.size());
            } else {
                if (districtList == null) {
                    logger.debug("DistrictList null for user : {}", user.getUserId());
                }
            }
        }
    }

    /**
     * use to check user's status ex : blocked ,new user, password expired...
     *
     * @param user
     * @return
     */
    private String checkUserExpiry(User user) {

        User.State currentState = userManager.getUserByID(user.getUserId()).getStatus();
        if (User.State.INACTIVE.equals(currentState) || User.State.DELETED.equals(currentState) ||
                User.State.LOCKEDOUT.equals(currentState)) {
            logger.warn("User Status is : {} for user :{}", user.getStatus(), user.getUserName());
            return ERROR;
        }

        java.util.GregorianCalendar gCal = new GregorianCalendar();

        if (gCal.getTime().after(user.getPasswordExpiry())) {
            logger.warn("password has been expired for user :{}", user.getUserName());
            fromLogin = true;
            return "expired";
        }

        logger.debug("users status OK");
        return SUCCESS;

    }

    /**
     * logout action which invalidates the session of the user
     * https://192.168.1.6:9080
     *
     * @return String
     */
    public String logout() {
        /* if there is a logged user */
        if (session.containsKey(WebConstants.SESSION_USER_BEAN)) {
            logger.debug("Inside logout : {} is going to logout.", ((User) session.get(WebConstants.SESSION_USER_BEAN)).getUserName());

            /* remove user from session */
            session.put(WebConstants.SESSION_USER_BEAN, null);
            if (session instanceof org.apache.struts2.dispatcher.SessionMap) {
                try {
                    ((org.apache.struts2.dispatcher.SessionMap) session).invalidate();
                    logger.debug("Session invalidated");
                } catch (IllegalStateException e) {
                    logger.error("Incorrect Session", e);
                }
            } else {
                session = null;     /* destroy session */
            }
            userManager.logoutUser(userName);
            return SUCCESS;  /* logout succeeded */
        }
        return ERROR;   /* there is no logged user */
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

    public String getJavaScript() {
        return javaScript;
    }

    public void setJavaScript(String javaScript) {
        this.javaScript = javaScript;
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

    public int getApprovalPending() {
        return approvalPending;
    }

    public void setApprovalPending(int approvalPending) {
        this.approvalPending = approvalPending;
    }

    public int getTotalConfirmChanges() {
        return totalConfirmChanges;
    }

    public void setTotalConfirmChanges(int totalConfirmChanges) {
        this.totalConfirmChanges = totalConfirmChanges;
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

    public int getConfirmPrintingPending() {
        return confirmPrintingPending;
    }

    public void setConfirmPrintingPending(int confirmPrintingPending) {
        this.confirmPrintingPending = confirmPrintingPending;
    }

    public int getBCprinted() {
        return BCprinted;
    }

    public void setBCprinted(int BCprinted) {
        this.BCprinted = BCprinted;
    }

    public int getBCPrintPending() {
        return BCPrintPending;
    }

    public void setBCPrintPending(int BCPrintPending) {
        this.BCPrintPending = BCPrintPending;
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

    public Map<Integer, String> getDivisionList() {
        return divisionList;
    }

    public void setDivisionList(Map<Integer, String> divisionList) {
        this.divisionList = divisionList;
    }

    public Map<Integer, String> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(Map<Integer, String> districtList) {
        this.districtList = districtList;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getDeoUserId() {
        return deoUserId;
    }

    public void setDeoUserId(int deoUserId) {
        this.deoUserId = deoUserId;
    }

    public List<String> getDeoList() {
        return deoList;
    }

    public void setDeoList(List<String> deoList) {
        this.deoList = deoList;
    }

    public List<String> getAdrList() {
        return adrList;
    }

    public void setAdrList(List<String> adrList) {
        this.adrList = adrList;
    }

    public int getAdrUserId() {
        return adrUserId;
    }

    public void setAdrUserId(int adrUserId) {
        this.adrUserId = adrUserId;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isFromLogin() {
        return fromLogin;
    }

    public void setFromLogin(boolean fromLogin) {
        this.fromLogin = fromLogin;
    }
}
