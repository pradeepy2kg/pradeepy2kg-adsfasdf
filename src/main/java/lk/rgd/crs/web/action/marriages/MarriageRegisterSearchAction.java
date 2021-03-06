package lk.rgd.crs.web.action.marriages;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.common.util.NameFormatUtil;
import lk.rgd.common.util.StateUtil;
import lk.rgd.common.util.WebUtils;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.MRDivisionDAO;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.MarriageNotice;
import lk.rgd.crs.api.domain.MarriageRegister;
import lk.rgd.crs.api.service.MarriageRegistrationService;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.util.CommonUtil;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Chathuranga Withana
 * @author amith jayasekara
 */
public class MarriageRegisterSearchAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(MarriageRegisterSearchAction.class);
    private static final String MR_APPROVAL_ROWS_PER_PAGE = "crs.mr_approval_rows_per_page";
    private static final String WARNING = "warning";

    // services and DAOs
    private final MarriageRegistrationService marriageRegistrationService;
    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final MRDivisionDAO mrDivisionDAO;
    private final AppParametersDAO appParametersDAO;
    private final UserDAO userDAO;
    private final LocationDAO locationDAO;
    private final CommonUtil commonUtil;
    private final UserLocationDAO userLocationDAO;
    private final RaceDAO raceDAO;

    private MarriageRegister marriage;

    private User user;

    private Map session;
    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> mrDivisionList;
    private Map<Integer, String> locationList;
    private Map<String, String> userList;

    private List<MarriageNotice> searchList;
    private List<MarriageRegister> marriageRegisterSearchList;
    private List<UserWarning> warnings;

    private Date searchStartDate;
    private Date searchEndDate;
    private Date dateOfIssueLicense;
    private Date dateOfCancelLicense;
    private Date effectiveDateOfDivorce;

    private String language;
    private String pinOrNic;
    private Long noticeSerialNo;

    private int districtId;
    private int dsDivisionId;
    private int mrDivisionId;
    private int pageNo;
    private int noOfRows;
    private int printStart;
    private int licensePrintedLocationId;
    private int locationId;
    private String mode;

    private long marriageIdUKey;

    private long idUKey;

    private String comment;
    private String licenseIssuedUserId;
    private String licenseIssueUserSignature;
    private String licenseIssuePlace;
    private String maleRaceInOL;
    private String maleRaceInEn;
    private String femaleRaceInOL;
    private String femaleRaceInEn;
    private String licenseIssueDistrictInOL;
    private String licenseIssueDistrictInEN;
    private String licenseIssueDivisionInOL;
    private String licenseIssueDivisionInEN;

    private boolean ignoreWarnings;
    private boolean warningsAtApproval;//use to divide warning JSP page
    private boolean listPage;

    private Map<Integer, String> stateList;
    private int state = -1;

    private MarriageNotice.Type noticeType;

    public MarriageRegisterSearchAction(MarriageRegistrationService marriageRegistrationService, DistrictDAO districtDAO,
        DSDivisionDAO dsDivisionDAO, MRDivisionDAO mrDivisionDAO, AppParametersDAO appParametersDAO,
        CommonUtil commonUtil, UserDAO userDAO, LocationDAO locationDAO, UserLocationDAO userLocationDAO, RaceDAO raceDAO) {
        this.marriageRegistrationService = marriageRegistrationService;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.mrDivisionDAO = mrDivisionDAO;
        this.appParametersDAO = appParametersDAO;
        this.commonUtil = commonUtil;
        this.userDAO = userDAO;
        this.locationDAO = locationDAO;
        this.userLocationDAO = userLocationDAO;
        this.raceDAO = raceDAO;

        districtList = new HashMap<Integer, String>();
        dsDivisionList = new HashMap<Integer, String>();
        mrDivisionList = new HashMap<Integer, String>();
    }

    /**
     * loading init search page for marriage notice search
     */
    public String marriageNoticeSearchInit() {
        logger.debug("Marriage notice search page loaded");
//        commonUtil.populateDynamicListsWithAllOption(districtList, dsDivisionList, mrDivisionList, user, language);
        try {
            populateBasicLists();
            pageNo += 1;
            getApprovalPendingNotices();
            showNoticeSearchResultSize();
            // by doing following previously user entered values will be removed in jsp page
            clearSearchingOptionValues();
        } catch (CRSRuntimeException e) {
            logger.debug("exception while loading page");
            return ERROR;
        }
        return SUCCESS;
    }

    public String marriageNoticeSearchHome() {
        logger.debug("Marriage notice search page loaded");
//        commonUtil.populateDynamicListsWithAllOption(districtList, dsDivisionList, mrDivisionList, user, language);
        try {
            //load search page with preferred district
            dsDivisionId = (user.getPrefBDDSDivision() != null) ? user.getPrefBDDSDivision().getDsDivisionUKey() :
                dsDivisionDAO.getAllDSDivisionByDistrictKey(districtDAO.getAllDistrictNames(language, user).keySet().
                    iterator().next()).iterator().next().getDsDivisionUKey();
            DSDivision prefDSDivision = dsDivisionDAO.getDSDivisionByPK(dsDivisionId);
            districtId = prefDSDivision.getDistrict().getDistrictUKey();

            populateBasicLists();
            pageNo += 1;
            getApprovalPendingNotices();
            showNoticeSearchResultSize();
            // by doing following previously user entered values will be removed in jsp page
            clearSearchingOptionValues();
        } catch (CRSRuntimeException e) {
            logger.debug("exception while loading page");
            return ERROR;
        }
        return SUCCESS;
    }


    /**
     * loading search result page for marriage register
     */
    public String marriageRegisterSearchInit() {
        // commonUtil.populateDynamicListsWithAllOption(districtList, dsDivisionList, mrDivisionList, user, language);
        populateBasicLists();
        stateList = StateUtil.getStateByLanguage(language);
        //  dsDivisionList = dsDivisionDAO.getAllDSDivisionNames(districtId, language, user);
        // mrDivisionList = mrDivisionDAO.getMRDivisionNames(dsDivisionId, language, user);
        pageNo += 1;
        return marriageRegisterSearchResult();
    }

    /**
     * loading search  page for marriage register
     */
    public String marriageRegisterSearchPageLoad() {
        //   commonUtil.populateDynamicListsWithAllOption(districtList, dsDivisionList, mrDivisionList, user, language);
        populateBasicLists();
        stateList = StateUtil.getStateByLanguage(language);
        return SUCCESS;
    }

    /**
     * loading search  page for marriage license
     */
    public String marriageLicenseSearchPageLoad() {
        return SUCCESS;
    }

    /**
     * Marriage Registration - Loding the extract of marriage register for print
     */
    public String marriageExtractInit() {
        marriage = marriageRegistrationService.getMarriageRegisterByIdUKey(idUKey, user, Permission.PRINT_MARRIAGE_EXTRACT);
        populateLocationList(marriage);
        if (locationList == null || locationList.isEmpty()) {
            addActionError(getText("error.nolocations"));
            logger.error("No locations assigned");
            return ERROR;
        }else if(userList == null || userList.isEmpty()){
            addActionError(getText("error.noassigned.user"));
            logger.error("No assigned users");
            return ERROR;
        }
        populateMarriageExtract(marriage);
        return SUCCESS;
    }

    /**
     * Marriage Registration - View Marriage Registration page
     */
    public String marriageRegisterViewInit() {
        marriage = marriageRegistrationService.getMarriageRegisterByIdUKey(idUKey, user, Permission.VIEW_MARRIAGE_REGISTER);
        return SUCCESS;
    }

    /**
     * Marriage Registration - Marriage Registration divorce page
     */
    public String marriageRegisterDivorceInit() {
        marriage = marriageRegistrationService.getMarriageRegisterByIdUKey(idUKey, user, Permission.DIVORCE);
        return SUCCESS;
    }

    /**
     * populating locations and users for Marriage Extract Print
     */
    private void populateLocationList(MarriageRegister register) {
        userList = new HashMap<String, String>();

        locationList = commonUtil.populateActiveUserLocations(user, language);
        int firstLocation = locationList.keySet().iterator().next();
        List<User> users = userLocationDAO.getMarriageCertificateSignUsersByLocationId(firstLocation, true);
        register.setExtractIssuedLocation(locationDAO.getLocation(firstLocation));

        MRDivision mrDivision = register.getMrDivision();
        if (mrDivision != null) {
            for (User u : users) {
                if (user.isAllowedAccessToMRDSDivision(mrDivision.getDsDivision().getDsDivisionUKey())) {
                    userList.put(u.getUserId(), NameFormatUtil.getDisplayName(u.getUserName(), 50));
                }
            }
        }
        if (userList != null && !userList.isEmpty()) {
            register.setExtractCertifiedUser(userDAO.getUserByPK(userList.keySet().iterator().next()));
        }
    }

    public String markMarriageExtractAsPrinted() {
        MarriageRegister marriageRegister = marriageRegistrationService.getMarriageRegisterByIdUKey(idUKey, user,
            Permission.PRINT_MARRIAGE_EXTRACT);

        if (marriageRegister != null &&
            (marriageRegister.getState() == MarriageRegister.State.EXTRACT_PRINTED ||
                marriageRegister.getState() == MarriageRegister.State.DIVORCE ||
                marriageRegister.getState() == MarriageRegister.State.DIVORCE_CERT_PRINTED)) {
            addActionMessage(getText("message.marriagerextract.alreadymarkedasprinted"));
            return SUCCESS;
        } else {
            try {
                //TODO : refactor rename licensePrintedLocationId and licenseIssuedUserId in order to user this attribute for both notice and Extract print
                marriageRegistrationService.markMarriageExtractAsPrinted(idUKey, locationDAO.
                    getLocation(licensePrintedLocationId), userDAO.getUserByPK(licenseIssuedUserId), user);
            } catch (CRSRuntimeException e) {
                switch (e.getErrorCode()) {
                    case ErrorCodes.MARRIAGE_REGISTER_NOT_FOUND:
                        addActionError(getText("error.marriageregister.notfound"));
                        break;
                    case ErrorCodes.INVALID_STATE_OF_MARRIAGE_REGISTER:
                        addActionError(getText("error.marriageregister.invalidstate"));
                        break;
                    case ErrorCodes.PERMISSION_DENIED:
                        addActionError(getText("message.permissiondenied"));
                        break;
                    case ErrorCodes.INVALID_LOCATION_ON_ISSUING_MARRIAGE_EXTRACT:
                        addActionError(getText("message.marriagerextract.markasprintedfailed"));
                        break;
                    case ErrorCodes.INVALID_USER_ON_CERTIFYING_MARRIAGE_EXTRACT:
                        addActionError(getText("message.marriagerextract.markasprintedfailed"));
                        break;
                    default:
                        addActionError(getText("message.marriagerextract.markasprintedfailed"));
                }
                return ERROR;
            }
        }
        addActionMessage(getText("message.marriagerextract.markasprinted"));
        return SUCCESS;
    }

    public String markDivorceExtractAsPrinted() {
        //TODO: action msg and errors have to be refactored
        MarriageRegister marriageRegister = marriageRegistrationService.getMarriageRegisterByIdUKey(idUKey, user,
            Permission.PRINT_MARRIAGE_EXTRACT);

        if (marriageRegister != null && marriageRegister.getState() == MarriageRegister.State.DIVORCE_CERT_PRINTED) {
            addActionMessage(getText("message.divorcerextract.alreadymarkedasprinted"));
            return SUCCESS;
        } else {
            try {
                //TODO : refactor rename licensePrintedLocationId and licenseIssuedUserId in order to user this attribute for both notice and Extract print
                marriageRegistrationService.markDivorceExtractAsPrinted(idUKey, locationDAO.
                    getLocation(licensePrintedLocationId), userDAO.getUserByPK(licenseIssuedUserId), user);
            } catch (CRSRuntimeException e) {
                switch (e.getErrorCode()) {
                    case ErrorCodes.MARRIAGE_REGISTER_NOT_FOUND:
                        addActionError(getText("error.marriageregister.notfound"));
                        break;
                    case ErrorCodes.INVALID_STATE_OF_MARRIAGE_REGISTER:
                        addActionError(getText("error.marriageregister.invalidstate"));
                        break;
                    case ErrorCodes.PERMISSION_DENIED:
                        addActionError(getText("message.permissiondenied"));
                        break;
                    case ErrorCodes.INVALID_LOCATION_ON_ISSUING_MARRIAGE_EXTRACT:
                        addActionError(getText("message.marriagerextract.markasprintedfailed"));
                        break;
                    case ErrorCodes.INVALID_USER_ON_CERTIFYING_MARRIAGE_EXTRACT:
                        addActionError(getText("message.marriagerextract.markasprintedfailed"));
                        break;
                    default:
                        addActionError(getText("message.marriagerextract.markasprintedfailed"));
                        break;
                }
                return ERROR;
            }
        }
        addActionMessage(getText("message.divorceextract.markasprinted"));
        return SUCCESS;
    }

    public String divorce() {
        //todo : add action messages/errors
        MarriageRegister marriageRegister = marriageRegistrationService.getMarriageRegisterByIdUKey(idUKey, user,
            Permission.DIVORCE);
        if (marriageRegister != null && marriageRegister.getState() != MarriageRegister.State.EXTRACT_PRINTED) {
            addActionError(getText("error.marriageregister.invalidstate"));
            return ERROR;
        } else {
            try {
                //TODO : refactor rename licensePrintedLocationId and licenseIssuedUserId in order to user this attribute for both notice and Extract print
                marriageRegistrationService.divorce(idUKey, comment, effectiveDateOfDivorce,
                    MarriageRegister.State.DIVORCE, user);
            } catch (CRSRuntimeException e) {
                addActionError(getText("error.marriageregister.divorcefailed"));
                return ERROR;
            }
        }
        addActionMessage(getText("message.marriageregister.divorced"));
        return SUCCESS;
    }

    /**
     * loading search result for marriage license
     */
    public String marriageLicenseSearchResult() {
        MarriageRegister marriageRegister = marriageRegistrationService.getByIdUKey(marriageIdUKey, user);
        if (marriageRegister != null && marriageRegister.getState() == MarriageRegister.State.LICENSE_PRINTED
            && marriageRegister.getLifeCycleInfo().isActiveRecord()) {
            idUKey = marriageRegister.getIdUKey();
        } else {
            addActionError(getText("error.marriageregister.norecords"));
            return ERROR;
        }
        mode = AppConstants.REGISTER;
        return SUCCESS;
    }

    /**
     * loading search result page for marriage register
     */
    private String marriageRegisterSearchResult() {
        noOfRows = appParametersDAO.getIntParameter(MR_APPROVAL_ROWS_PER_PAGE);
        MarriageRegister.State mrState = null;
        String divisionType = AppConstants.NONE;
        int divisionId = 0;

        if (state != -1) {
            mrState = StateUtil.getStateById(state);
        }

        //Search by marriage registration number - IDukey
        if (marriageIdUKey != 0) {
            marriageRegisterSearchList = new ArrayList<MarriageRegister>();
            MarriageRegister mr = marriageRegistrationService.getMarriageRegisterByIdUKeyAndState(marriageIdUKey, user);
            if (mr != null) {
                marriageRegisterSearchList.add(mr);
            }
        } else if (pinOrNic != null) {
            //search by male/female/registrar identification number
            marriageRegisterSearchList = marriageRegistrationService.getMarriageRegisterByPINNumber(pinOrNic, true, user);

        } else if (noticeSerialNo != null) {
            //search by marriage serial number.
            marriageRegisterSearchList = marriageRegistrationService.getMarriageRegisterBySerialNumber(noticeSerialNo, user);

        } else {
            if (districtId != 0 & dsDivisionId != 0 & mrDivisionId != 0) { // all selected
                //filter by mr division
                divisionId = mrDivisionId;
                divisionType = AppConstants.MARRIAGE;

            } else if (districtId != 0 & dsDivisionId != 0) { // only mr division selected
                //filter by ds division
                divisionId = dsDivisionId;
                divisionType = AppConstants.DS_DIVISION;

            } else if (districtId != 0) { // only district selected
                //filter by district
                divisionId = districtId;
                divisionType = AppConstants.DISTRICT;

            }

            marriageRegisterSearchList = marriageRegistrationService.getMarriageRegisterList(divisionType, divisionId,
                mrState, true, searchStartDate, searchEndDate, pageNo, noOfRows, user);
            //  districtId = 0;
        }

        if (marriageRegisterSearchList.size() == 0) {
            addActionError(getText("error.marriageregister.norecords"));
        }
        return SUCCESS;
    }

    /**
     * Approve noticed/muslim marriages
     */
    public String approveMarriageRegister() {
        try {
            warnings = marriageRegistrationService.approveMarriageRegister(idUKey, user, ignoreWarnings);
        } catch (CRSRuntimeException e) {
            switch (e.getErrorCode()) {
                case ErrorCodes.BRIDES_FATHER_IN_PRS_IS_MISMATCHED_WITH_GIVEN_FATHER:
                    addActionError(getText("error.given.brides.father.details.are.mismatched.with.prs"));
                    break;
                case ErrorCodes.GROOMS_FATHER_IN_PRS_IS_MISMATCHED_WITH_GIVEN_FATHER:
                    addActionError(getText("error.given.grooms.father.details.are.mismatched.with.prs"));
                    break;
                default:
                    addActionError(getText("error.marriageregister.notapproved"));
            }
            return marriageRegisterSearchInit();
        }

        if (warnings.isEmpty()) {
            logger.debug("Approving marriage register with idUKey : {} approves successfully", idUKey);
            addActionMessage(getText("message.marriageregister.approved"));
            return marriageRegisterSearchInit();
        } else {
            logger.debug("Approving marriage register with idUKey : {} gives warnings list of : {} items", idUKey,
                warnings.size());
            return WARNING;
        }
    }

    /**
     * This method navigate back to marriage register search list page
     */
    public String backMarriageRegisterSearch() {
        logger.debug("Returns back to marriage register search list page");
        return marriageRegisterSearchInit();
    }

    /**
     * Reject noticed/muslim marriages
     */
    public String rejectMarriageRegister() {
        try {
            marriageRegistrationService.rejectMarriageRegister(idUKey, comment, user);
        } catch (CRSRuntimeException e) {
            addActionError(getText("error.marriageregister.notrejected"));
            return marriageRegisterSearchInit();
        }
        addActionMessage(getText("message.marriageregister.rejected"));
        return marriageRegisterSearchInit();
    }

    /**
     * action method use to approve a notice this could be male notice or female notice or a single notice type(BOTH)
     */
    public String approveMarriageNotice() {
        if (logger.isDebugEnabled()) {
            logger.debug("approving marriage notice idUKey : " + idUKey + " and notice type : " + noticeType +
                " and with ignore warnings : " + ignoreWarnings);
        }
        //todo remove
        //follow use to display serial number in action message ans action errors
        marriage = marriageRegistrationService.getByIdUKey(idUKey, user);
        String[] actionMassageArray = new String[]{(noticeType == MarriageNotice.Type.FEMALE_NOTICE) ?
            Long.toString(marriage.getSerialOfFemaleNotice()) : Long.toString(marriage.getSerialOfMaleNotice())};
        try {
            warnings = marriageRegistrationService.
                approveMarriageNotice(idUKey, noticeType, ignoreWarnings, user);
            if (warnings.size() > 0) {
                //if warning size is more than 0 we forward in to approval warning page
                warningsAtApproval = true;
                return "warning";
            }
            addActionMessage(getText("message.approve.success", actionMassageArray));
            logger.debug("successfully approved marriage notice idUKey : {} and notice type :{ }", idUKey, noticeType);
        } catch (CRSRuntimeException e) {
            //error happens when approving marriage notice
            switch (e.getErrorCode()) {
                case ErrorCodes.OTHER_PARTY_MUST_APPROVE_FIRST:
                    addActionError(getText("error.other.party.approve.first",
                        new String[]{(noticeType == MarriageNotice.Type.FEMALE_NOTICE) ?
                            (marriage.getSerialOfMaleNotice() != null) ?
                                Long.toString(marriage.getSerialOfMaleNotice()) : getText("message.not.yet.add") :
                            (marriage.getSerialOfFemaleNotice() != null) ?
                                Long.toString(marriage.getSerialOfFemaleNotice()) : getText("message.not.yet.add")}));
                    break;
                case ErrorCodes.UNABLE_APPROVE_MARRIAGE_NOTICE_PROHIBITED_RELATIONSHIP:
                    addActionError(getText("error.approval.failed", actionMassageArray) + " , " + e.getMessage());
                    break;
                case ErrorCodes.BRIDES_FATHER_IN_PRS_IS_MISMATCHED_WITH_GIVEN_FATHER:
                    addActionError(getText("error.given.brides.father.details.are.mismatched.with.prs"));
                    break;
                case ErrorCodes.GROOMS_FATHER_IN_PRS_IS_MISMATCHED_WITH_GIVEN_FATHER:
                    addActionError(getText("error.given.grooms.father.details.are.mismatched.with.prs"));
                    break;
                case ErrorCodes.MORE_THAN_ONE_ACTIVE_LICENSE:
                    addActionError(getText("error.unable.to.approve.already.issued.license"));
                    break;
                default:
                    addActionError(getText("error.approval.failed", actionMassageArray));
            }
            commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList, districtId,
                dsDivisionId, mrDivisionId, AppConstants.MARRIAGE, user, language);
            getApprovalPendingNotices();
            return "failed";
        } catch (Exception e) {
            logger.debug("exception while approving marriage notice :idUKey ");
            return ERROR;
        }
        commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList, districtId, dsDivisionId,
            mrDivisionId, AppConstants.MARRIAGE, user, language);
        getApprovalPendingNotices();
        return SUCCESS;
    }


    /**
     * deleting a marriage notice
     * notes:
     * when removing a notice(have 2 notices) it just updating the data row
     * if there is only one notice (BOTH) delete the row
     */
    public String deleteMarriageNotice() {
        logger.debug("attempt to delete marriage notice : idUKey {} : notice type : {}", idUKey, noticeType);
        try {
            marriageRegistrationService.deleteMarriageNotice(idUKey, noticeType, user);
        } catch (CRSRuntimeException e) {
            commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList, districtId,
                dsDivisionId, mrDivisionId, AppConstants.MARRIAGE, user, language);
            getApprovalPendingNotices();
            addActionError(getText("error.delete.notice"));
            return ERROR;
        }
        addActionMessage(getText("message.delete.successfully"));
        commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList, districtId,
            dsDivisionId, mrDivisionId, AppConstants.MARRIAGE, user, language);
        getApprovalPendingNotices();
        return SUCCESS;
    }

    public String rejectInit() {
        logger.debug("loading commenting page for rejecting marriage notice");
        //do nothing just load get comment page for rejecting marriage notice
        marriage = marriageRegistrationService.getByIdUKey(idUKey, user);
        return SUCCESS;
    }

    public String rejectMarriageNotice() {
        logger.debug("attempt to reject marriage notice : idUKey : {} :notice type : {}", idUKey, noticeType);
        try {
            marriageRegistrationService.rejectMarriageNotice(idUKey, noticeType, comment, user);
            marriage = marriageRegistrationService.getByIdUKey(idUKey, user);
        } catch (CRSRuntimeException e) {
            logger.debug("failed to reject marriage notice idUKey : {} : notice type : {}  ", idUKey, noticeType);
            addActionError(getText("error.rejection.fail", new String[]{Long.
                toString((noticeType == MarriageNotice.Type.FEMALE_NOTICE) ?
                marriage.getSerialOfFemaleNotice() : marriage.getSerialOfMaleNotice())}));
            commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList, districtId,
                dsDivisionId, mrDivisionId, AppConstants.MARRIAGE, user, language);
            getApprovalPendingNotices();
            return ERROR;
        }
        addActionMessage(getText("message.rejection.success", new String[]{Long.
            toString((noticeType == MarriageNotice.Type.FEMALE_NOTICE) ?
            marriage.getSerialOfFemaleNotice() : marriage.getSerialOfMaleNotice())}));
        commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList, districtId,
            dsDivisionId, mrDivisionId, AppConstants.MARRIAGE, user, language);
        getApprovalPendingNotices();
        return SUCCESS;
    }

    /**
     * printing license to marriage
     */
    public String licenseToMarriagePrintInit() {
        logger.debug("attempt to print license to marriage for marriage notice :idUKey : {} and notice type : {}",
            idUKey, noticeType);
        try {
            marriage = marriageRegistrationService.getMarriageNoticeForPrintLicense(idUKey, user);
        } catch (CRSRuntimeException e) {
            switch (e.getErrorCode()) {
                case ErrorCodes.INVALID_STATE_FOR_PRINT_LICENSE:
                    addActionError(getText("error.print.license.failed.invalid.state"));
            }
            addActionError(getText("error.print.license.failed"));
            getApprovalPendingNotices();
            return ERROR;
        }
        //displaying issuing locations and authorized users
        populateLocationsAndIssuingUsersDropDowns(marriage);
        populateIssuingUserAndLocation(marriage, user);
        populateLicense(marriage);
        return SUCCESS;
    }

    public String markLicenseAsPrinted() {
        logger.debug("attempt to mark license to marriage as license printed, marriage notice :idUKey : {} ", idUKey);
        MarriageRegister notice = marriageRegistrationService.getByIdUKey(idUKey, user);
        if (notice != null && notice.getState() == MarriageRegister.State.LICENSE_PRINTED) {
            //do nothing if current state is LICENSE_PRINT
            logger.debug("attempt to re mark license as license printed :idUKey : {} state : {}", idUKey, notice.getState());
            addActionMessage(getText("message.license.already.marked"));
        } else {
            try {
                marriageRegistrationService.markLicenseToMarriageAsPrinted(idUKey, locationDAO.
                    getLocation(licensePrintedLocationId), userDAO.getUserByPK(licenseIssuedUserId), user);
                logger.debug("success fully marked as printed marriage notice idUKey : {}", idUKey);
            } catch (CRSRuntimeException e) {
                logger.debug("failed to mark as print :idUKey : {} ", idUKey);
                switch (e.getErrorCode()) {
                    case ErrorCodes.INVALID_STATE_FOR_PRINT_LICENSE:
                        addActionError(getText("error.license.mark.as.print.failed.invalid.state"));
                    default:
                        addActionError(getText("error.license.mark.as.print"));
                }
                getApprovalPendingNotices();
                return ERROR;
            }
        }
        //if success redirect to license
        commonUtil.populateDynamicLists(districtList, dsDivisionList, mrDivisionList, districtId,
            dsDivisionId, mrDivisionId, AppConstants.MARRIAGE, user, language);
        getApprovalPendingNotices();
        showNoticeSearchResultSize();
        return SUCCESS;
    }

    /**
     * This method is used for pagination(move backward) in marriage notice search list page
     */
    public String previousNoticesPage() {
        logger.debug("Previous page of Marriage notices search list loaded");
        noOfRows = appParametersDAO.getIntParameter(MR_APPROVAL_ROWS_PER_PAGE);
        pageNo = printStart / noOfRows;

        filterAndLoadMarriageNotices();

        printStart -= noOfRows;
        populateBasicLists();
        showNoticeSearchResultSize();
        return SUCCESS;
    }

    /**
     * This method is used for pagination(move forward) in marriage notice search list page
     */
    public String nextNoticesPage() {
        logger.debug("Next page of Marriage notices search list loaded");
        noOfRows = appParametersDAO.getIntParameter(MR_APPROVAL_ROWS_PER_PAGE);
        pageNo = ((printStart + noOfRows) / noOfRows) + 1;

        filterAndLoadMarriageNotices();

        printStart += noOfRows;
        populateBasicLists();
        showNoticeSearchResultSize();
        return SUCCESS;
    }

    public String previousRegisterPage() {
        logger.debug("Previous page of Marriage register search list loaded");
        noOfRows = appParametersDAO.getIntParameter(MR_APPROVAL_ROWS_PER_PAGE);
        pageNo = printStart / noOfRows;
        marriageRegisterSearchResult();
        printStart -= noOfRows;
        // commonUtil.populateDynamicListsWithAllOption(districtList, dsDivisionList, mrDivisionList, user, language);
        populateBasicLists();
        stateList = StateUtil.getStateByLanguage(language);
        return SUCCESS;
    }

    /**
     * This method is used for pagination(move forward) in marriage notice search list page
     */
    public String nextRegisterPage() {
        logger.debug("Next page of Marriage register search list loaded");
        noOfRows = appParametersDAO.getIntParameter(MR_APPROVAL_ROWS_PER_PAGE);
        pageNo = ((printStart + noOfRows) / noOfRows) + 1;
        marriageRegisterSearchResult();
        printStart += noOfRows;
        // commonUtil.populateDynamicListsWithAllOption(districtList, dsDivisionList, mrDivisionList, user, language);
        populateBasicLists();
        stateList = StateUtil.getStateByLanguage(language);
        return SUCCESS;
    }

    /**
     * populating drop downs for user locations and printing users
     * note :only user locations that belong to male party or female party DS divisions are valid divisions
     */
    private void populateLocationsAndIssuingUsersDropDowns(MarriageRegister notice) {
        //get current users location   displaying lists,initial values are set by the service
        locationList = commonUtil.populateActiveUserLocations(user, language);
        userList = new HashMap<String, String>();
        List<User> users = userLocationDAO.getMarriageCertificateSignUsersByLocationId(locationList.keySet().
            iterator().next(), true);
        for (User u : users) {
            MRDivision mrDivisionMaleNotice = notice.getMrDivisionOfMaleNotice();
            MRDivision mrDivisionFemaleNotice = notice.getMrDivisionOfFemaleNotice();
            if ((mrDivisionMaleNotice != null && user.isAllowedAccessToMRDSDivision(mrDivisionMaleNotice.getMrDivisionUKey())) ||
                (mrDivisionFemaleNotice != null && user.isAllowedAccessToMRDSDivision(mrDivisionFemaleNotice.getMrDivisionUKey()))) {
                userList.put(u.getUserId(), NameFormatUtil.getDisplayName(u.getUserName(), 50));
            }
        }
    }

    /**
     * set additional displaying values
     */
    private void populateLicense(MarriageRegister notice) {
        //fill date of issue
        //todo remove race from here and use if else inside JSP amith
        dateOfIssueLicense = new GregorianCalendar().getTime();
        //canceling date is (defined value in data base) days from printed date
        // get Calendar with current date
        java.util.GregorianCalendar gCal = new GregorianCalendar();
        gCal.add(Calendar.DATE, +appParametersDAO.getIntParameter("crs.license_cancel_dates"));
        dateOfCancelLicense = gCal.getTime();
        //setting issuing location and user
        //display values
        DSDivision issuingDSDivision = dsDivisionDAO.getDSDivisionByPK(notice.getLicenseIssueLocation().getDsDivisionId());
        District issuingDistrict = issuingDSDivision.getDistrict();
        if (AppConstants.SINHALA.equals(notice.getPreferredLanguage())) {
            //Sinhala pref lang
            licenseIssuePlace = notice.getLicenseIssueLocation().getSienLocationSignature();
            licenseIssueUserSignature = notice.getLicensePrintUser().getUserSignature(AppConstants.SINHALA);
            maleRaceInOL = notice.getMale().getMaleRace() != null ? notice.getMale().getMaleRace().getSiRaceName() : "";
            femaleRaceInOL = notice.getFemale().getFemaleRace() != null ? notice.getFemale().getFemaleRace().getSiRaceName() : "";
            licenseIssueDistrictInOL = issuingDistrict.getSiDistrictName();
            licenseIssueDivisionInOL = issuingDSDivision.getSiDivisionName();
        } else {
            //tamil pref lang
            licenseIssuePlace = notice.getLicenseIssueLocation().getTaenLocationSignature();
            licenseIssueUserSignature = notice.getLicensePrintUser().getUserSignature(AppConstants.TAMIL);
            maleRaceInOL = notice.getMale().getMaleRace() != null ? notice.getMale().getMaleRace().getTaRaceName() : "";
            femaleRaceInOL = notice.getFemale().getFemaleRace() != null ? notice.getFemale().getFemaleRace().getTaRaceName() : "";
            licenseIssueDistrictInOL = issuingDistrict.getTaDistrictName();
            licenseIssueDivisionInOL = issuingDSDivision.getTaDivisionName();
        }
        //populate race name in sin and race name in en for male and female parties
        maleRaceInEn = notice.getMale().getMaleRace() != null ? notice.getMale().getMaleRace().getEnRaceName() : "";
        femaleRaceInEn = notice.getFemale().getFemaleRace() != null ? notice.getFemale().getFemaleRace().getEnRaceName() : "";
        licenseIssueDistrictInEN = issuingDistrict.getEnDistrictName();
        licenseIssueDivisionInEN = issuingDSDivision.getEnDivisionName();
    }

    /**
     * set additional displaying values
     */
    private void populateMarriageExtract(MarriageRegister marriageRegister) {
        //setting issuing location and user
        //display values
        DSDivision issuingDSDivision = dsDivisionDAO.getDSDivisionByPK(marriageRegister.getExtractIssuedLocation().getDsDivisionId());
        District issuingDistrict = issuingDSDivision.getDistrict();
        if (AppConstants.SINHALA.equals(marriageRegister.getPreferredLanguage())) {
            //Sinhala pref lang
            licenseIssuePlace = marriageRegister.getExtractIssuedLocation().getSienLocationSignature();
            licenseIssueUserSignature = marriageRegister.getExtractCertifiedUser().getUserSignature(AppConstants.SINHALA);
            licenseIssueDistrictInOL = issuingDistrict.getSiDistrictName();
            licenseIssueDivisionInOL = issuingDSDivision.getSiDivisionName();
        } else {
            //tamil pref lang
            licenseIssuePlace = marriageRegister.getExtractIssuedLocation().getTaenLocationSignature();
            licenseIssueUserSignature = marriageRegister.getExtractCertifiedUser().getUserSignature(AppConstants.TAMIL);
            licenseIssueDistrictInOL = issuingDistrict.getTaDistrictName();
            licenseIssueDivisionInOL = issuingDSDivision.getTaDivisionName();
        }
        //populate race name in sin and race name in en for male and female parties
        licenseIssueDistrictInEN = issuingDistrict.getEnDistrictName();
        licenseIssueDivisionInEN = issuingDSDivision.getEnDivisionName();
    }

    /**
     * set printing user location and issuing user(primary values)
     */
    private void populateIssuingUserAndLocation(MarriageRegister notice, User user) {
        List<Location> userLocationList = user.getActiveLocations();
        //set first (primary) location as issuing location
        int locationId = userLocationList.get(0).getLocationUKey();
        Location location = userLocationList.get(0);
        /* issueLocation.getLocationName()*/
        notice.setLicenseIssueLocation(location);
        //get first user from above location
        //no need to check first user's permission for issuing certificate  because any DS office can issue license
        notice.setLicensePrintUser(userLocationDAO.getMarriageCertificateSignUsersByLocationId(locationId, true).get(0));


    }

    /**
     * This method used to load approval pending Marriage Notices list
     */
    private void getApprovalPendingNotices() {
        noOfRows = appParametersDAO.getIntParameter(MR_APPROVAL_ROWS_PER_PAGE);
        filterAndLoadMarriageNotices();
    }

    private void filterAndLoadMarriageNotices() {
        if (noticeSerialNo != null) {
            if (mrDivisionId != 0) {
                // Search by MRDivision and Serial Number of male or female notice
                searchList = WebUtils.populateNoticeList(marriageRegistrationService.getMarriageNoticePendingApprovalByMRDivisionAndSerial(
                    mrDivisionDAO.getMRDivisionByPK(mrDivisionId), noticeSerialNo, true, user));
            } else {
                searchList = Collections.emptyList();
            }
        } else {
            if (isEmpty(pinOrNic) && noticeSerialNo == null) {
                if (mrDivisionId == 0) {
                    if (dsDivisionId == 0) {
                        if (districtId == 0) {
                            // Search by All available Districts
                            // TODO chathuranga not implemented yet
                            searchList = Collections.emptyList();
                        } else {
                            // Search by All DSDivisions
                            searchList = WebUtils.populateNoticeList(marriageRegistrationService.
                                getMarriageNoticeByDistrict(districtDAO.getDistrict(districtId), pageNo, noOfRows, true, user));
                            if (searchStartDate != null && searchEndDate != null) {
                                searchList = WebUtils.populateNoticeList(marriageRegistrationService.
                                    getMarriageNoticeByDistrictAndDateRange(districtDAO.getDistrict(districtId),
                                    searchStartDate, searchEndDate, pageNo, noOfRows, true, user));
                                if (searchList.size() > 0) {
                                    addActionMessage(getText("message.result.to.from",
                                        new String[]{DateTimeUtils.getISO8601FormattedString(searchStartDate),
                                            DateTimeUtils.getISO8601FormattedString(searchEndDate)}));
                                } else {
                                    addActionMessage(getText("no.message.result.to.from",
                                        new String[]{DateTimeUtils.getISO8601FormattedString(searchStartDate),
                                            DateTimeUtils.getISO8601FormattedString(searchEndDate)}));
                                }
                            }
                        }
                    } else {
                        // Search by specific DSDivision
                        if (searchStartDate == null && searchEndDate == null) {
                            // Search by DSDivision
                            searchList = WebUtils.populateNoticeList(marriageRegistrationService.getMarriageNoticePendingApprovalByDSDivision(
                                dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, true, user));
                        } else {
                            // Search by DSDivision and register date range of male or female notice
                            searchList = WebUtils.populateNoticeList(marriageRegistrationService.getMarriageNoticesByDSDivisionAndRegisterDateRange(
                                dsDivisionDAO.getDSDivisionByPK(dsDivisionId), searchStartDate, searchEndDate, pageNo, noOfRows, true, user));
                            if (searchList.size() > 0) {
                                addActionMessage(getText("message.result.to.from",
                                    new String[]{DateTimeUtils.getISO8601FormattedString(searchStartDate),
                                        DateTimeUtils.getISO8601FormattedString(searchEndDate)}));
                            } else {
                                addActionMessage(getText("no.message.result.to.from",
                                    new String[]{DateTimeUtils.getISO8601FormattedString(searchStartDate),
                                        DateTimeUtils.getISO8601FormattedString(searchEndDate)}));
                            }
                        }
                    }
                } else {
                    if (searchStartDate == null && searchEndDate == null) {
                        // Search by MRDivision
                        searchList = WebUtils.populateNoticeList(marriageRegistrationService.getMarriageNoticePendingApprovalByMRDivision(
                            mrDivisionDAO.getMRDivisionByPK(mrDivisionId), pageNo, noOfRows, true, user));
                    } else {
                        // Search by MRDivision and register date range of male or female notice
                        searchList = WebUtils.populateNoticeList(marriageRegistrationService.getMarriageNoticesByMRDivisionAndRegisterDateRange(
                            mrDivisionDAO.getMRDivisionByPK(mrDivisionId), searchStartDate, searchEndDate, pageNo, noOfRows, true, user));
                        if (searchList.size() > 0) {
                            addActionMessage(getText("message.result.to.from",
                                new String[]{DateTimeUtils.getISO8601FormattedString(searchStartDate),
                                    DateTimeUtils.getISO8601FormattedString(searchEndDate)}));
                        } else {
                            addActionMessage(getText("no.message.result.to.from",
                                new String[]{DateTimeUtils.getISO8601FormattedString(searchStartDate),
                                    DateTimeUtils.getISO8601FormattedString(searchEndDate)}));
                        }
                    }
                }
            } else {
                // Search by PIN or NIC of male or female party
                searchList = WebUtils.populateNoticeList(
                    marriageRegistrationService.getMarriageNoticePendingApprovalByPINorNIC(pinOrNic, true, user));
            }
        }
    }

    /**
     * This method is used to show action message if search result list is empty
     */
    private void showNoticeSearchResultSize() {
        if (searchList.size() == 0) {
            addActionMessage(getText("noItemMsg.label"));
        }
        logger.debug("Marriage notice search list loaded with size : {}", searchList.size());
    }

    /**
     * This method used to set searching fields values to empty
     */
    private void clearSearchingOptionValues() {
        noticeSerialNo = null;
        pinOrNic = null;
        searchStartDate = null;
        searchEndDate = null;
    }

    /**
     * populate basic list such as districts, DSDivisions and MRDivision
     */
    private void populateBasicLists() {
        // TODO chathuranga change following
        districtList = districtDAO.getDistrictNames(language, user);
        // TODO chathuranga when search by all district option
        /*     if (districtId == 0) {
            if (!districtList.isEmpty()) {
                districtId = districtList.keySet().iterator().next();
                logger.debug("first allowed district in the list {} was set", districtId);
            }
        }*/
        dsDivisionList = dsDivisionDAO.getDSDivisionNames(districtId, language, user);
        mrDivisionList = mrDivisionDAO.getMRDivisionNames(dsDivisionId, language, user);
    }


    private boolean isEmpty(String s) {
        return s == null || s.trim().length() != 10;
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map session) {
        this.session = session;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        logger.debug("setting User: {} and Language : {}", user.getUserName(), language);
    }

    public Map<Integer, String> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(Map<Integer, String> districtList) {
        this.districtList = districtList;
    }

    public Map<Integer, String> getDsDivisionList() {
        return dsDivisionList;
    }

    public void setDsDivisionList(Map<Integer, String> dsDivisionList) {
        this.dsDivisionList = dsDivisionList;
    }

    public Map<Integer, String> getMrDivisionList() {
        return mrDivisionList;
    }

    public void setMrDivisionList(Map<Integer, String> mrDivisionList) {
        this.mrDivisionList = mrDivisionList;
    }

    public List<MarriageNotice> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<MarriageNotice> searchList) {
        this.searchList = searchList;
    }

    public Date getSearchStartDate() {
        return searchStartDate;
    }

    public void setSearchStartDate(Date searchStartDate) {
        this.searchStartDate = searchStartDate;
    }

    public Date getSearchEndDate() {
        return searchEndDate;
    }

    public void setSearchEndDate(Date searchEndDate) {
        this.searchEndDate = searchEndDate;
    }

    public String getPinOrNic() {
        return pinOrNic;
    }

    public void setPinOrNic(String pinOrNic) {
        this.pinOrNic = WebUtils.filterBlanks(pinOrNic);
    }

    public Long getNoticeSerialNo() {
        return noticeSerialNo;
    }

    public void setNoticeSerialNo(Long noticeSerialNo) {
        this.noticeSerialNo = noticeSerialNo;
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

    public int getMrDivisionId() {
        return mrDivisionId;
    }

    public void setMrDivisionId(int mrDivisionId) {
        this.mrDivisionId = mrDivisionId;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getNoOfRows() {
        return noOfRows;
    }

    public void setNoOfRows(int noOfRows) {
        this.noOfRows = noOfRows;
    }

    public int getPrintStart() {
        return printStart;
    }

    public void setPrintStart(int printStart) {
        this.printStart = printStart;
    }

    public List<MarriageRegister> getMarriageRegisterSearchList() {
        return marriageRegisterSearchList;
    }

    public void setMarriageRegisterSearchList(List<MarriageRegister> marriageRegisterSearchList) {
        this.marriageRegisterSearchList = marriageRegisterSearchList;
    }

    public MarriageNotice.Type getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(MarriageNotice.Type noticeType) {
        this.noticeType = noticeType;
    }

    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public MarriageRegister getMarriage() {
        return marriage;
    }

    public void setMarriage(MarriageRegister marriage) {
        this.marriage = marriage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDateOfIssueLicense() {
        return dateOfIssueLicense;
    }

    public void setDateOfIssueLicense(Date dateOfIssueLicense) {
        this.dateOfIssueLicense = dateOfIssueLicense;
    }

    public Date getDateOfCancelLicense() {
        return dateOfCancelLicense;
    }

    public void setDateOfCancelLicense(Date dateOfCancelLicense) {
        this.dateOfCancelLicense = dateOfCancelLicense;
    }

    public int getLicensePrintedLocationId() {
        return licensePrintedLocationId;
    }

    public void setLicensePrintedLocationId(int licensePrintedLocationId) {
        this.licensePrintedLocationId = licensePrintedLocationId;
    }

    public String getLicenseIssuedUserId() {
        return licenseIssuedUserId;
    }

    public void setLicenseIssuedUserId(String licenseIssuedUserId) {
        this.licenseIssuedUserId = licenseIssuedUserId;
    }

    public Map<Integer, String> getLocationList() {
        return locationList;
    }

    public void setLocationList(Map<Integer, String> locationList) {
        this.locationList = locationList;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public Map<String, String> getUserList() {
        return userList;
    }

    public void setUserList(Map<String, String> userList) {
        this.userList = userList;
    }

    public String getLicenseIssueUserSignature() {
        return licenseIssueUserSignature;
    }

    public void setLicenseIssueUserSignature(String licenseIssueUserSignature) {
        this.licenseIssueUserSignature = licenseIssueUserSignature;
    }

    public String getLicenseIssuePlace() {
        return licenseIssuePlace;
    }

    public void setLicenseIssuePlace(String licenseIssuePlace) {
        this.licenseIssuePlace = licenseIssuePlace;
    }

    public String getMaleRaceInOL() {
        return maleRaceInOL;
    }

    public void setMaleRaceInOL(String maleRaceInOL) {
        this.maleRaceInOL = maleRaceInOL;
    }

    public String getMaleRaceInEn() {
        return maleRaceInEn;
    }

    public void setMaleRaceInEn(String maleRaceInEn) {
        this.maleRaceInEn = maleRaceInEn;
    }

    public String getFemaleRaceInOL() {
        return femaleRaceInOL;
    }

    public void setFemaleRaceInOL(String femaleRaceInOL) {
        this.femaleRaceInOL = femaleRaceInOL;
    }

    public String getFemaleRaceInEn() {
        return femaleRaceInEn;
    }

    public void setFemaleRaceInEn(String femaleRaceInEn) {
        this.femaleRaceInEn = femaleRaceInEn;
    }

    public String getLicenseIssueDistrictInOL() {
        return licenseIssueDistrictInOL;
    }

    public void setLicenseIssueDistrictInOL(String licenseIssueDistrictInOL) {
        this.licenseIssueDistrictInOL = licenseIssueDistrictInOL;
    }

    public String getLicenseIssueDistrictInEN() {
        return licenseIssueDistrictInEN;
    }

    public void setLicenseIssueDistrictInEN(String licenseIssueDistrictInEN) {
        this.licenseIssueDistrictInEN = licenseIssueDistrictInEN;
    }

    public String getLicenseIssueDivisionInOL() {
        return licenseIssueDivisionInOL;
    }

    public void setLicenseIssueDivisionInOL(String licenseIssueDivisionInOL) {
        this.licenseIssueDivisionInOL = licenseIssueDivisionInOL;
    }

    public String getLicenseIssueDivisionInEN() {
        return licenseIssueDivisionInEN;
    }

    public void setLicenseIssueDivisionInEN(String licenseIssueDivisionInEN) {
        this.licenseIssueDivisionInEN = licenseIssueDivisionInEN;
    }

    public Map<Integer, String> getStateList() {
        return stateList;
    }

    public void setStateList(Map<Integer, String> stateList) {
        this.stateList = stateList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isIgnoreWarnings() {
        return ignoreWarnings;
    }

    public void setIgnoreWarnings(boolean ignoreWarnings) {
        this.ignoreWarnings = ignoreWarnings;
    }

    public List<UserWarning> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<UserWarning> warnings) {
        this.warnings = warnings;
    }

    public boolean isWarningsAtApproval() {
        return warningsAtApproval;
    }

    public void setWarningsAtApproval(boolean warningsAtApproval) {
        this.warningsAtApproval = warningsAtApproval;
    }

    public boolean isListPage() {
        return listPage;
    }

    public void setListPage(boolean listPage) {
        this.listPage = listPage;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public long getMarriageIdUKey() {
        return marriageIdUKey;
    }

    public void setMarriageIdUKey(long marriageIdUKey) {
        this.marriageIdUKey = marriageIdUKey;
    }

    public Date getEffectiveDateOfDivorce() {
        return effectiveDateOfDivorce;
    }

    public void setEffectiveDateOfDivorce(Date effectiveDateOfDivorce) {
        this.effectiveDateOfDivorce = effectiveDateOfDivorce;
    }
}
