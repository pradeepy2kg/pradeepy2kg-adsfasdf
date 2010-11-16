package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private int dsDivisionId;

    private User user;
    private Map session;

    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;

    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final UserManager userManager;
    private static final Logger logger = LoggerFactory.getLogger(UserPreferencesAction.class);

    private boolean emptyFeild;
    private Pattern pattern;

    /**
     * following properties are checked by  PASSWORD_PATTERN
     * 
     * Passwords will contain at least (1) upper case letter
     * Passwords will contain at least (1) lower case letter
     * Passwords will contain at least (1) number or special character
     * Passwords will contain at least (8) characters in length
     * Password maximum length is not limited
     */
    private static final String PASSWORD_PATTERN =
            "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";

    public UserPreferencesAction(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, UserManager userManager) {
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.userManager = userManager;
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

        return "success";
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

        if(emptyFeild){
            addActionError(getText("emptry.feild.lable"));
            return "error";
        }
        if(!user.getPasswordHash().equals(HashUtil.hashString(existingPassword))){
            addActionError(getText("existing.password.mismatch.lable"));
            return "error";
        }
        if (!validate(newPassword)) {
            addActionError(getText("password.not.Strong.Enough.lable"));
            return "error";
        }
        if (newPassword.equals(retypeNewPassword)) {
            userManager.updatePassword(newPassword, user);
            String userRole = user.getRole().getRoleId();
            logger.debug("return value of change password method is,", "success" + userRole);
            addActionMessage(getText("password.changed.success.label"));
            return "success" + userRole;
        } else {
            addActionError(getText("password.mismatch.lable"));
            return "error";
        }
    }

    public String changePasswordInit() {
        return SUCCESS;
    }

    public String back() {
        return this.userPreferenceInit();
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
}
