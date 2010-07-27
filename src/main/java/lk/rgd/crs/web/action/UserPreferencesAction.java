package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Locale;

import lk.rgd.crs.web.WebConstants;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.service.UserManager;

/**
 * @author Duminda
 * @author amith jayasekara
 */
public class UserPreferencesAction extends ActionSupport implements SessionAware {

    private String newPassword;
    private String retypeNewPassword;
    private String prefLanguage;
    private int birthDistrictId;
    private int dsDivisionId;

    private Map session;

    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;

    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final UserManager userManager;
    private static final Logger logger = LoggerFactory.getLogger(UserPreferencesAction.class);

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
    private int pageNo; //pageNo is used to decide the current pageNo of the Birth Registration Form

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
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);

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

        return "pageload";
    }

    public String selectUserPreference() {
        Locale currentLocale = (Locale) session.get(WebConstants.SESSION_USER_LANG);
        Locale newLocale = new Locale(prefLanguage, currentLocale.getCountry());

        session.put(WebConstants.SESSION_USER_LANG, newLocale);
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
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
     * this method use to change a password of a user
     *
     * @return
     */
    public String changePassword() {
        logger.info("requesting a password change.......");
        if (newPassword.equals(retypeNewPassword)) {
            User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
            userManager.updatePassword(newPassword, user);
            String userRole = user.getRole().getRoleId();

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
            logger.debug("return value of change password method is,", "success" + userRole);
            return "success" + userRole;
        }
        return "error";
    }

    public String adoptionDeclaration() {
        logger.info("load the adoptionDeclaration page");
        return "form"+pageNo;
    }

    public String back() {
        return this.userPreferenceInit();
    }

    public void setSession(Map map) {
        this.session = map;
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
        this.newPassword = newPassword;
    }

    public String getRetypeNewPassword() {
        return retypeNewPassword;
    }

    public void setRetypeNewPassword(String retypeNewPassword) {
        this.retypeNewPassword = retypeNewPassword;
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

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
