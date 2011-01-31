package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.Statistics;
import lk.rgd.common.api.service.StatisticsManager;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.util.regex.Pattern;

import lk.rgd.crs.web.WebConstants;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.util.WebUtils;
import lk.rgd.common.util.HashUtil;

/**
 * @author Duminda
 * @author amith jayasekara
 */
public class UserPreferencesAction extends ActionSupport implements SessionAware {

    private String existingPassword;
    private String newPassword;
    private String retypeNewPassword;
    private String prefLanguage;
    private String userType;
    private int birthDistrictId;

    private User user;
    private Map session;

    private Map<Integer, String> dsDivisionList;

    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final StatisticsDAO statisticsDAO;
    private final UserManager userManager;
    private final StatisticsManager statisticsManager;
    private static final Logger logger = LoggerFactory.getLogger(UserPreferencesAction.class);

    private boolean emptyFeild;
    private Pattern pattern;

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;

    private Map<Integer, String> districtList;
    private Map<Integer, String> divisionList;
    private List<String> deoList;
    private List<String> adrList;

    private int dsDivisionId;
    private int districtId;
    private int deoUserId;
    private int adrUserId;

    private String startDate;
    private String endDate;
    private String role;
    private Statistics statistics;

    private String userRole;

    /**
     * following properties are checked by  PASSWORD_PATTERN
     * <p/>
     * Passwords will contain at least (1) upper case letter
     * Passwords will contain at least (1) lower case letter
     * Passwords will contain at least (1) number or special character
     * Passwords will contain at least (8) characters in length
     * Password maximum length is not limited
     */
    private static final String PASSWORD_PATTERN =
        "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";

    public UserPreferencesAction(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, UserManager userManager, RoleDAO roleDAO, UserDAO userDAO,
        StatisticsDAO statisticsDAO, StatisticsManager statisticsManager) {
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.userManager = userManager;
        this.roleDAO = roleDAO;
        this.userDAO = userDAO;
        this.statisticsDAO = statisticsDAO;
        this.statisticsManager = statisticsManager;
    }

    /**
     * Set the Language that the user preffered to work
     * And set preffered language to the session
     */

    public String userPreferenceInit() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
//        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        userType = user.getRole().getRoleId();
        logger.debug("Role of the {} is  :{}", user.getUserName(), userType);
        if (!(userType.equals(WebConstants.SESSION_USER_ADMIN))) {
            districtList = districtDAO.getDistrictNames(language, user);
            if (user.getPrefBDDistrict() != null) {
                birthDistrictId = user.getPrefBDDistrict().getDistrictUKey();
            } else {
                birthDistrictId = districtList.keySet().iterator().next();
            }

            dsDivisionList = dsDivisionDAO.getDSDivisionNames(birthDistrictId, language, user);
            if (user.getPrefBDDSDivision() != null) {
                dsDivisionId = user.getPrefBDDSDivision().getDsDivisionUKey();
            } else {
                dsDivisionId = dsDivisionList.keySet().iterator().next();
            }
        }

        role = userType;
        /*statistics = statisticsDAO.getByUser(user.getUserId());
        if (statistics == null) {
            statistics = new Statistics();
        }

        districtList = districtDAO.getDistrictNames(user.getPrefLanguage(), user);
        if (districtList.size() > 0) {
            districtId = districtList.keySet().iterator().next();
        }
        divisionList = dsDivisionDAO.getAllDSDivisionNames(districtId, user.getPrefLanguage(), user);
        if (divisionList.size() > 0) {
            dsDivisionId = divisionList.keySet().iterator().next();
            deoList = userDAO.getDEOsByDSDivision(user.getPrefLanguage(), user,
                dsDivisionDAO.getDSDivisionByPK(dsDivisionId), roleDAO.getRole(Role.ROLE_DEO));
        }
        deoUserId = 1;*/

        return "pageload";
    }

    public String selectUserPreference() {
        Locale currentLocale = (Locale) session.get(WebConstants.SESSION_USER_LANG);
        Locale newLocale = new Locale(prefLanguage, currentLocale.getCountry());

        session.put(WebConstants.SESSION_USER_LANG, newLocale);
        user.setPrefLanguage(prefLanguage);
        user.setPrefBDDistrict(districtDAO.getDistrict(birthDistrictId));
        user.setPrefBDDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId));
        session.put(WebConstants.SESSION_USER_BEAN, user);
        userManager.updateUser(user, user);

        logger.debug("inside selectUserPreference() : {} passed.", prefLanguage);
        logger.debug("inside selectUserPreference() District : {} and DS division {} passed.", birthDistrictId, dsDivisionId);

        role = user.getRole().getRoleId();

        statistics = statisticsDAO.getByUser(user.getUserId());
        if (statistics == null) {
            statistics = new Statistics();
        }

        districtList = districtDAO.getDistrictNames(user.getPrefLanguage(), user);
        if (districtList.size() > 0) {
            districtId = districtList.keySet().iterator().next();
        }
        divisionList = dsDivisionDAO.getAllDSDivisionNames(districtId, user.getPrefLanguage(), user);
        if (divisionList.size() > 0) {
            dsDivisionId = divisionList.keySet().iterator().next();
            deoList = userDAO.getDEOsByDSDivision(user.getPrefLanguage(), user,
                dsDivisionDAO.getDSDivisionByPK(dsDivisionId), roleDAO.getRole(Role.ROLE_DEO));
        }
        deoUserId = 1;
        userRole = role;

        return "success";
    }

    public String showStatistics () {
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        logger.debug("Logged User's UserName : {}", user.getUserId());
        logger.debug("Logged User's Role : {}", user.getRole());
        role = user.getRole().getRoleId();

        /*statistics = statisticsDAO.getByUser(user.getUserId());    TODO: remove this comments after the performance tests
        if (statistics == null) {
            statistics = statisticsManager.getStatisticsForUser(user);
            if (statistics == null) {
                statistics = new Statistics();
            }
        }*/

        statistics = new Statistics();   // TODO: remove this line after the performance tests

        districtList = districtDAO.getDistrictNames(user.getPrefLanguage(), user);
        if (districtList.size() > 0) {
            districtId = districtList.keySet().iterator().next();
        }
        divisionList = dsDivisionDAO.getAllDSDivisionNames(districtId, user.getPrefLanguage(), user);
        if (divisionList.size() > 0) {
            dsDivisionId = divisionList.keySet().iterator().next();
            deoList = userDAO.getDEOsByDSDivision(user.getPrefLanguage(), user,
                dsDivisionDAO.getDSDivisionByPK(dsDivisionId), roleDAO.getRole(Role.ROLE_DEO));
        }
        deoUserId = 1;
        /*adrList = userDAO.getADRsByDistrictId(districtDAO.getDistrict(districtId), roleDAO.getRole(Role.ROLE_ADR));
        adrUserId = 1;*/

        return SUCCESS;
    }


    /**
     * Validate password with regular expression
     *
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    private boolean validate(final String password) {
        pattern = Pattern.compile(PASSWORD_PATTERN);
        return pattern.matcher(password).matches();
    }

    /**
     * this method use to change a password of a user
     *
     * @return
     */
    public String changePassword() {
        logger.info("requesting a password change.......");

        if (emptyFeild) {
            addActionError(getText("emptry.feild.lable"));
            return "error";
        }
        if (!user.getPasswordHash().equals(HashUtil.hashString(existingPassword))) {
            addActionError(getText("existing.password.mismatch.lable"));
            return "error";
        }
        if (!validate(newPassword)) {
            addActionError(getText("password.not.Strong.Enough.lable"));
            return "error";
        }
        if (newPassword.equals(retypeNewPassword)) {
            userManager.updatePassword(newPassword, user);
            String uRole = user.getRole().getRoleId();
            logger.debug("return value of change password method is success{}", uRole);
            addActionMessage(getText("password.changed.success.label"));
            //return "success" + userRole;

            if (uRole.equals("RG") || uRole.equals("ARG")) {
                populateLists(user, WebConstants.USER_ARG);
            }
            if (uRole.equals("DR") || (uRole).equals("ADR")) {
                populateLists(user, uRole);
            }

            role = userType;

            /*statistics = statisticsDAO.getByUser(user.getUserId());   TODO: remove this comments after the performance tests
            if (statistics == null) {
                statistics = new Statistics();
            }*/

            statistics = new Statistics();  // TODO: remove this line after the performance tests

            districtList = districtDAO.getDistrictNames(user.getPrefLanguage(), user);
            if (districtList.size() > 0) {
                districtId = districtList.keySet().iterator().next();
            }
            divisionList = dsDivisionDAO.getAllDSDivisionNames(districtId, user.getPrefLanguage(), user);
            if (divisionList.size() > 0) {
                dsDivisionId = divisionList.keySet().iterator().next();
                deoList = userDAO.getDEOsByDSDivision(user.getPrefLanguage(), user,
                    dsDivisionDAO.getDSDivisionByPK(dsDivisionId), roleDAO.getRole(Role.ROLE_DEO));
            }
            deoUserId = 1;
            userRole = uRole;

            return "success" + uRole;

        } else {
            addActionError(getText("password.mismatch.lable"));
            return "error";
        }
    }

    public String showPasswordChangeStatistics() {

        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        logger.debug("Logged User's UserName : {}", user.getUserId());
        logger.debug("Logged User's Role : {}", user.getRole());
        role = user.getRole().getRoleId();

        /*statistics = statisticsDAO.getByUser(user.getUserId());       TODO: remove this comments after the performance tests
        if (statistics == null) {
            statistics = statisticsManager.getStatisticsForUser(user);
            if (statistics == null) {
                statistics = new Statistics();
            }
        }*/

        statistics = new Statistics();    // TODO: remove this line after the performance tests

        districtList = districtDAO.getDistrictNames(user.getPrefLanguage(), user);
        if (districtList.size() > 0) {
            districtId = districtList.keySet().iterator().next();
        }
        divisionList = dsDivisionDAO.getAllDSDivisionNames(districtId, user.getPrefLanguage(), user);
        if (divisionList.size() > 0) {
            dsDivisionId = divisionList.keySet().iterator().next();
            deoList = userDAO.getDEOsByDSDivision(user.getPrefLanguage(), user,
                dsDivisionDAO.getDSDivisionByPK(dsDivisionId), roleDAO.getRole(Role.ROLE_DEO));
        }
        deoUserId = 1;
        /*adrList = userDAO.getADRsByDistrictId(districtDAO.getDistrict(districtId), roleDAO.getRole(Role.ROLE_ADR));
        adrUserId = 1;*/

        return SUCCESS;
    }

    void populateLists(User user, String usertype) {

        if (usertype.equals(WebConstants.USER_ARG) || usertype.equals(WebConstants.USER_RG)) {
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

        } else if (usertype.toLowerCase().equals(WebConstants.USER_ADR)) {
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
        } else if (usertype.toLowerCase().equals(WebConstants.USER_DR)) {
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
        }
    }

    public String changePasswordInit() {
        return SUCCESS;
    }

    public String back() {
        //return this.userPreferenceInit();    TODO: remove this comments after the performance tests
        /*statistics = statisticsDAO.getByUser(user.getUserId());
        if (statistics == null) {
            statistics = new Statistics();
        }*/

        statistics = new Statistics();  // TODO: remove this line after the performance tests

        districtList = districtDAO.getDistrictNames(user.getPrefLanguage(), user);
        if (districtList.size() > 0) {
            districtId = birthDistrictId = districtList.keySet().iterator().next();
        }
        divisionList = dsDivisionDAO.getAllDSDivisionNames(districtId, user.getPrefLanguage(), user);
        if (divisionList.size() > 0) {
            dsDivisionId = divisionList.keySet().iterator().next();
            deoList = userDAO.getDEOsByDSDivision(user.getPrefLanguage(), user,
                dsDivisionDAO.getDSDivisionByPK(dsDivisionId), roleDAO.getRole(Role.ROLE_DEO));
        }
        deoUserId = 1;
        role = userRole = user.getRole().getRoleId();
        return SUCCESS;
    }

    public void setSession(Map map) {
        this.session = map;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
    }

    public Map getSession() {
        return session;
    }

    public Map<Integer, String> getDistrictList() {
        return districtList;
    }

    public Map<Integer, String> getDsDivisionList() {
        return dsDivisionList;
    }

    public String getPrefLanguage() {
        return prefLanguage;
    }

    public void setPrefLanguage(String prefLanguage) {
        this.prefLanguage = prefLanguage;
    }

    public int getBirthDistrictId() {
        return birthDistrictId;
    }

    public void setBirthDistrictId(int birthDistrictId) {
        this.birthDistrictId = birthDistrictId;
    }

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        if (WebUtils.filterBlanks(newPassword) == null) {
            setEmptyFeild(true);
        }
        this.newPassword = newPassword;
    }

    public String getRetypeNewPassword() {
        return retypeNewPassword;
    }

    public void setRetypeNewPassword(String retypeNewPassword) {
        if (WebUtils.filterBlanks(retypeNewPassword) == null) {
            setEmptyFeild(true);
        }
        this.retypeNewPassword = retypeNewPassword;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public String getExistingPassword() {
        return existingPassword;
    }

    public void setExistingPassword(String existingPassword) {
        if (WebUtils.filterBlanks(existingPassword) == null) {
            setEmptyFeild(true);
        }
        this.existingPassword = existingPassword;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isEmptyFeild() {
        return emptyFeild;
    }

    public void setEmptyFeild(boolean emptyFeild) {
        this.emptyFeild = emptyFeild;
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

    public List<String> getDeoList() {
        return deoList;
    }

    public void setDeoList(List<String> deoList) {
        this.deoList = deoList;
    }

    public int getDeoUserId() {
        return deoUserId;
    }

    public void setDeoUserId(int deoUserId) {
        this.deoUserId = deoUserId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public Map<Integer, String> getDivisionList() {
        return divisionList;
    }

    public void setDivisionList(Map<Integer, String> divisionList) {
        this.divisionList = divisionList;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
