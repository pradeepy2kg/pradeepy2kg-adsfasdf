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
    public static Map<Integer, Link> linkPermission = new TreeMap<Integer, Link>();
    public Map<String, Map> allowedLinks = new HashMap<String, Map>();
    private List userRoles;
    private String userName;
    private String password;
    private Map session;
    private final UserManager userManager;
    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);
    //private UserPreferencesAction userPreferencesAction = new UserPreferencesAction();


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
            user = userManager.authenticateUser(userName, password);
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
            HashMap map = (HashMap) allowedLinks(user);

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
            logger.error("Exception {} {} ", e, e.toString());
            return "error";
        }
    }

    /**
     * @param user
     * @return
     */
    private Map<String, Map> allowedLinks(User user) {
        Map birthLink = new TreeMap();
        Map deathLink = new TreeMap();
        Map marrageLink = new TreeMap();
        Map reportLink = new TreeMap();
        Map adminLink = new TreeMap();
        Map preferanceLink = new TreeMap();
        Map prsLink = new TreeMap();
        Map adoptionLink = new TreeMap();

        for (Map.Entry<Integer, Link> e : linkPermission.entrySet()) {
            if (user.isAuthorized(e.getKey())) {
                Link link = e.getValue();
                String category = e.getValue().getCategory();
                Integer key = e.getKey();

                if (category.equals("/popreg/births/")) {
                    birthLink.put(key, link);
                } else if (category.equals("/popreg/deaths/")) {
                    deathLink.put(key, link);
                } else if (category.equals("/popreg/marriages/")) {
                    marrageLink.put(key, link);
                } else if (category.equals("/popreg/reports/")) {
                    reportLink.put(key, link);
                } else if (category.equals("/popreg/management/")) {
                    adminLink.put(key, link);
                } else if (category.equals("/popreg/preferences/")) {
                    preferanceLink.put(key, link);
                } else if (category.equals("/popreg/prs/")) {
                    prsLink.put(key, link);
                } else if (category.equals("/popreg/adoption/")) {
                    adoptionLink.put(key, link);
                }
                logger.debug("put link {} as category {}", e.getValue().getAction(), e.getValue().getCategory());
            }
        }
        allowedLinks.put("0births", birthLink);
        allowedLinks.put("1deaths", deathLink);
        allowedLinks.put("2marriages", marrageLink);
        allowedLinks.put("4reprots", reportLink);
        allowedLinks.put("5management", adminLink);
        allowedLinks.put("3preferences", preferanceLink);
        allowedLinks.put("6prs", prsLink);
        allowedLinks.put("7adoption", adoptionLink);
        return allowedLinks;
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
     *
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

    static {
        //Admin
        linkPermission.put(Permission.PAGE_CREATE_USER, new Link("creat_user.label", "/popreg/management/", "eprInitUserCreation.do"));
        linkPermission.put(Permission.PAGE_VIEW_USERS, new Link("viewUsers.label", "/popreg/management/", "eprViewUsers.do"));
        linkPermission.put(Permission.PAGE_USER_CREATION, new Link(null, "/popreg/management/", "eprUserCreation.do"));
        linkPermission.put(Permission.DELETE_USER, new Link(null, "/popreg/management/", "eprdeleteUsers.do"));
        linkPermission.put(Permission.VIEW_SELECTED_USERS, new Link(null, "/popreg/management/", "eprViewSelectedUsers.do"));

        //Birth Registration
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATON_INIT, new Link("birth_registration.label", "/popreg/births/", "eprBirthRegistrationInit.do"));
        linkPermission.put(Permission.PAGE_STILL_BIRTH_REGISTRATION, new Link("still_birth_registration.label", "/popreg/births/", "eprStillBirthRegistrationInit.do"));
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATON_DIRECT_HOME, new Link(null, "/popreg/births/", "eprHome.do"));
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATON, new Link(null, "/popreg/births/", "eprBirthRegistration.do"));
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATON_HOME, new Link(null, "/popreg/births/", "eprBirthRegistrationHome.do"));
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATON_STILL_BIRTH_HOME, new Link(null, "/popreg/births/", "eprStillBirth.do"));
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATON_DIRECT_PRINT, new Link(null, "/popreg/births/", "eprDirectPrintBirthConfirmation.do"));

        //Birth Confirmation
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_PRINT, new Link("birth_confirmation_print.label", "/popreg/births/", "eprBirthConfirmationPrintList.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_INIT, new Link("birth_confirmation.label", "/popreg/births/", "eprBirthConfirmationInit.do"));
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATION_APPROVAL, new Link("birth_register_approval.label", "/popreg/births/", "eprBirthRegisterApproval.do"));
        linkPermission.put(Permission.PAGE_USER_PREFERANCE_SELECT, new Link("userPreference.label", "/popreg/preferences/", "eprUserPreferencesInit.do"));
        linkPermission.put(Permission.PAGE_VIEW_USERS, new Link("viewUsers.label", "/popreg/management/", "eprViewUsers.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_SEARCH, new Link("search.label", "/popreg/births/", "eprSearchPageLoad.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_APPROVAL, new Link("birth_confirmation_approval.label", "/popreg/births/", "eprBirthConfirmationApproval.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CERTIFICATE_PRINT, new Link("print_birthcertificate.label", "/popreg/births/", "eprBirthCertificateList.do"));
        linkPermission.put(Permission.PAGE_STILL_BIRTH_REGISTRATION, new Link("still_birth_registration.label", "/popreg/births/", "eprStillBirthRegistrationInit.do"));
        linkPermission.put(Permission.PAGE_ADVANCE_SEARCH_BIRTHS, new Link("advanceSearch.label", "/popreg/births/", "eprBirthsAdvancedSearch.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_FORM_DETAIL_DIRECT_PRINT_BIRTH_CERTIFICATE, new Link(null, "/popreg/births/", "eprDirectPrintBirthCertificate.do"));

        //Death Registration
        linkPermission.put(Permission.PAGE_DEATH_REGISTRATION_INIT, new Link("death_registration.label", "/popreg/deaths/", "eprDeathDeclaration.do"));
        linkPermission.put(Permission.PAGE_DEATH_CERTIFICATE, new Link("death_certificate.label", "/popreg/deaths/", "eprDeathCertificate.do"));
        linkPermission.put(Permission.PAGE_LATE_DEATH_REGISTRATION, new Link("late_death_registration.label", "/popreg/deaths/", "eprLateDeathDeclaration.do"));

        //non displayable (in the menu) pages
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATON_DIRECT_HOME, new Link(null, "/popreg/births/", "eprHome.do"));
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATON, new Link(null, "/popreg/births/", "eprBirthRegistration.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION, new Link(null, "/popreg/births/", "eprBirthConfirmation.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_PRINT_LIST_REFRESH, new Link(null, "/popreg/births/", "eprFilterBirthConfirmationPrint.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_PRINT_SELECTED_ENTRY, new Link(null, "/popreg/births/", "eprBirthConfirmationPrintPage.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_BULK_PRINT, new Link(null, "/popreg/births/", "eprBirthConfirmationBulkPrint.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_PRINT_LIST_NEXT, new Link(null, "/popreg/births/", "eprPrintNext.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_PRINT_LIST_PREVIOUS, new Link(null, "/popreg/births/", "eprPrintPrevious.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_SKIP_CONFIRMATIONCHANGES, new Link(null, "/popreg/births/", "eprBirthConfirmationSkipChanges.do"));

        //Approval
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATION_APPROVAL, new Link("birth_register_approval.label", "/popreg/births/", "eprBirthRegisterApproval.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_APPROVAL, new Link("birth_confirmation_approval.label", "/popreg/births/", "eprBirthConfirmationApproval.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_APPROVAL_REFRESH, new Link(null, "/popreg/births/", "eprConfirmationApprovalRefresh.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CERTIFICATE_BULK_PRINT, new Link(null, "/popreg/births/", "eprBirthCertificateBulkPrint.do"));
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATION_SEARCH_BY_SERIALNO, new Link(null, "/popreg/births/", "eprBDFSearchBySerialNo.do"));
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATION_SEARCH_BY_IDUKEY, new Link(null, "/popreg/births/", "eprBDFSearchByIdUKey.do"));
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATION_SEARCH_VIEW_NON_EDITABLE_MODE, new Link(null, "/popreg/births/", "eprViewBDFInNonEditableMode.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_APPROVAL_NEXT, new Link(null, "/popreg/births/", "eprConfirmationApprovalNext.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_APPROVAL_PREVIOUS, new Link(null, "/popreg/births/", "eprConfirmationApprovalPrevious.do"));
        linkPermission.put(Permission.PAGE_BIRTH_DECLARATION_APPROVAL_REFRESH, new Link(null, "/popreg/births/", "eprApprovalRefresh.do"));
        linkPermission.put(Permission.PAGE_BIRTH_DECLARATION_APPROVE_BULK, new Link(null, "/popreg/births/", "eprApproveConfirmationBulk.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_DIRECT_APPROVAL, new Link(null, "/popreg/births/", "eprConfrimationChangesDirectApproval.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_DIRECT_PRINT_BIRTH_CERTIFICATE, new Link(null, "/popreg/births/", "eprBirthCertificatDirectPrint.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_APPROVE_BULK, new Link(null, "/popreg/births/", "eprApproveBulk.do"));
        linkPermission.put(Permission.PAGE_BIRTH_DECLARATION_APPROVE_SELECTED, new Link(null, "/popreg/births/", "eprApproveBirthDeclaration.do"));
        linkPermission.put(Permission.PAGE_BIRTH_DECLARATION_APPROVAL_NEXT, new Link(null, "/popreg/births/", "eprApprovalNext.do"));
        linkPermission.put(Permission.PAGE_BIRTH_DECLARATION_APPROVAL_PREVIOUS, new Link(null, "/popreg/births/", "eprApprovalPrevious.do"));
        linkPermission.put(Permission.PAGE_BIRTH_DECLARATION_APPROVAL_DELETE, new Link(null, "/popreg/births/", "eprDeleteApprovalPending.do"));
        linkPermission.put(Permission.PAGE_BIRTH_DECLARATION_APPROVAL_IGNORING_WARNING, new Link(null, "/popreg/births/", "eprIgnoreWarning.do"));
        linkPermission.put(Permission.PAGE_BIRTH_DECLARATION_APPROVAL_REJECT_SELECTED, new Link(null, "/popreg/births/", "eprRejectBirthDeclaration.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_APPROVE_SELECTED, new Link(null, "/popreg/births/", "eprApproveBirthConfirmation.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_APPROVAL_IGNORING_WARNING, new Link(null, "/popreg/births/", "eprConfirmationIgnoreWarning.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_APPROVAL_REJECT_SELECTED, new Link(null, "/popreg/births/", "eprRejectBirthConfirmation.do"));
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATON_DIRECT_APPROVE, new Link(null, "/popreg/births/", "eprDirectApprove.do"));
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATON_DIRECT_APPROVAL_IGNORING_WARNINGS, new Link(null, "/popreg/births/", "eprDirectApproveIgnoreWarning.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_DIRECT_APPROVAL_IGNORING_WARNINGS, new Link(null, "/popreg/births/", "ConfirmationDirectApprovalIngoreWarning.do"));

        //User Preferance
        linkPermission.put(Permission.PAGE_USER_PREFERANCE_SELECT, new Link("userPreference.label", "/popreg/preferences/", "eprUserPreferencesInit.do"));
        linkPermission.put(Permission.CHANGE_PASSWORD, new Link(null, "/popreg/preferences/", "eprChangePass.do"));
        linkPermission.put(Permission.BACK_CHANGE_PASSWORD, new Link(null, "/popreg/preferences/", "eprBackChangePass.do"));
        linkPermission.put(Permission.CHANGE_PASSWORD_PAGE_LOAD, new Link(null, "/popreg/preferences/", "passChangePageLoad.do"));
        linkPermission.put(Permission.PAGE_USER_PREFERENCE_INIT, new Link(null, "/popreg/preferences/", "eprUserPreferencesAction.do"));

        //Search
        linkPermission.put(Permission.PAGE_BIRTH_CONFIRMATION_SEARCH, new Link("search.label", "/popreg/births/", "eprSearchPageLoad.do"));
        linkPermission.put(Permission.PAGE_ADVANCE_SEARCH_BIRTHS, new Link("birth.advanceSearch.label", "/popreg/births/", "eprBirthsAdvancedSearch.do"));
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATION_SEARCH_BY_SERIALNO, new Link(null, "/popreg/births/", "eprBDFSearchBySerialNo.do"));
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATION_SEARCH_BY_IDUKEY, new Link(null, "/popreg/births/", "eprBDFSearchByIdUKey.do"));
        linkPermission.put(Permission.PAGE_ADVANCE_SEARCH_PRS, new Link("prs.advanceSearch.label", "/popreg/prs/", "eprPRSAdvancedSearch.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CERTIFICATE_SEARCH, new Link("birth_certificate_search.label", "/popreg/births/", "eprBirthCertificateSearch.do"));

        //Birth Certificate
        linkPermission.put(Permission.PAGE_BIRTH_CERTIFICATE_PRINT, new Link("print_birthcertificate.label", "/popreg/births/", "eprBirthCertificateList.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CERTIFICATE_PRINT_LIST_REFRESH, new Link(null, "/popreg/births/", "eprFilterBirthCetificateList.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CERTIFICATE_PRINT_SELECTED_ENTRY, new Link(null, "/popreg/births/", "eprBirthCertificate.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CERTIFICATE_BULK_PRINT, new Link(null, "/popreg/births/", "eprBirthCertificateBulkPrint.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CERTIFICATE_PRINT_LIST_NEXT, new Link(null, "/popreg/births/", "eprCertificatePrintNext.do"));
        linkPermission.put(Permission.PAGE_BIRTH_CERTIFICATE_PRINT_LIST_PREVIOUS, new Link(null, "/popreg/births/", "eprCertificatePrintPrevious.do"));
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATION_STILL_BIRTH_CERTIFICATE_PRINT, new Link(null, "/popreg/births/", "eprStillBirthCertificatePrint.do"));
        linkPermission.put(Permission.PAGE_BIRTH_REGISTRATION_STILL_BIRTH_CERTIFICATE_DIRECT_PRINT, new Link(null, "/popreg/births/", "eprDirectPrintStillBirthCertificate.do"));

        //Adoption Registration
        linkPermission.put(Permission.PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT, new Link("adoption_approval_and_print.lable", "/popreg/adoption/", "eprAdoptionApprovalAndPrint.do"));
        linkPermission.put(Permission.PAGE_ADOPTION_REGISTRATION, new Link(null, "/popreg/adoption/", "eprAdoptionAction.do"));
        linkPermission.put(Permission.PAGE_ADOPTION_REGISTRATION_HOME, new Link(null, "/popreg/adoption/", "eprAdoptionRegistrationHome.do"));
        linkPermission.put(Permission.PAGE_ADOPTION_RE_REGISTRATION, new Link("adoption_re_registration.label", "/popreg/adoption/", "eprAdoptionReRegistrationAction.do"));
        linkPermission.put(Permission.PAGE_ADOPTION_INIT, new Link("adoption_registration.label", "/popreg/adoption/", "eprAdoptionRegistrationAction.do"));
        linkPermission.put(Permission.PAGE_ADOPTION_APPLICANT_INFO, new Link("adoption_applicant.label", "/popreg/adoption/", "eprAdoptionApplicantInfo.do"));
        //    linkPermission.put(Permission.PAGE_ADOPTION_APPLICANT_INFO, new Link("adoption_certificate.label", "/popreg/adoption/", "eprAdoptionCertificate.do"));
        linkPermission.put(Permission.PAGE_ADOPTION_BDF_ENTRY, new Link(null, "/popreg/births/", "eprAdoptionBirthRegistrationInit.do"));
        linkPermission.put(Permission.PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT_VIEW_MODE, new Link(null, "/popreg/adoption/", "eprAdoptionViewMode.do"));
        linkPermission.put(Permission.PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT_APPROVE_SELECTED, new Link(null, "/popreg/adoption/", "eprApproveAdoption.do"));
        linkPermission.put(Permission.PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT_REJECT_SELECTED, new Link(null, "/popreg/adoption/", "eprRejectAdoption.do"));
        linkPermission.put(Permission.PAGE_ADOPTION_BDF_HOME, new Link(null, "/popreg/births/", "eprAdoptionRegistrationHome.do"));

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
