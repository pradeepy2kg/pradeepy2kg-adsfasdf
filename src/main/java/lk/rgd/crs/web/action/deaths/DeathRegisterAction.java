package lk.rgd.crs.web.action.deaths;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.RGDRuntimeException;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.common.util.NameFormatUtil;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.DeathAlterationService;
import lk.rgd.crs.api.service.DeathRegistrationService;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.util.CommonUtil;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Duminda Dharmakeerthi
 * @authar amith jayasekara
 */
public class DeathRegisterAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(DeathRegisterAction.class);
    private static final String DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE = "crs.dr_rows_per_page";
    private Map session;
    private User user;
    private DeathRegister deathRegister;
    private DeathInfo death;
    private DeathPersonInfo deathPerson;
    private DeclarantInfo declarant;
    private NotifyingAuthorityInfo notifyingAuthority;
    private CRSLifeCycleInfo lifeCycleInfo;

    private int deathDistrictId;
    private int deathDivisionId;
    private int dsDivisionId;
    private int deathPersonCountry;
    private int deathPersonRace;
    private int locationId;
    private int printStart;
    private int deathPersonPermenentAddressDistrictId;
    private int deathPersonPermenentAddressDSDivisionId;

    private final DistrictDAO districtDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final CountryDAO countryDAO;
    private final AppParametersDAO appParametersDAO;
    private final UserLocationDAO userLocationDAO;
    private final LocationDAO locationDAO;
    private final UserDAO userDAO;
    private final DeathRegistrationService service;
    private final DeathAlterationService deathAlterationService;
    private final RaceDAO raceDAO;
    private final CommonUtil commonUtil;

    private BitSet changedFields;

    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> raceList;
    private Map<Integer, String> countryList;
    private Map<Integer, String> locationList;
    private Map<String, String> userList;
    private Map<Integer, String> permenantAddressDsDivisionList;

    private List<DeathRegister> deathApprovalAndPrintList;
    private List<DeathRegister> archivedEntryList;
    private List<UserWarning> warnings;


    private int pageNo;
    private int noOfRows;
    private int currentStatus;
    private int rowNumber;
    private int pageType;

    private long idUKey;
    private long oldIdUKey;

    private boolean allowEditDeath;
    private boolean allowApproveDeath;
    private boolean nextFlag;
    private boolean previousFlag;
    private boolean back;
    private boolean searchByDate;
    private boolean rePrint;
    private boolean lateDeath;
    private boolean directApprove;
    private boolean editMode;
    private boolean reject;

    private String genderEn;
    private String genderSi;
    private String deathPersonDistrict;
    private String deathPersonDsDivision;
    private String deathPersonDeathDivision;
    private String deathPersonDeathDivisionEn;
    private String deathPersonDistrictEn;
    private String deathPersondsDivision;
    private String deathPersondsDivisionEn;
    private String time;
    private String comment;
    private String serialNumber;
    private String nameOfOfficer;
    private String placeOfIssue;
    private String issueUserId;
    private String language;

    private Date fromDate;
    private Date endDate;

    private DeathRegister.State state;
    private DeathRegister.Type deathType;
    private boolean ignoreWarning;
    private boolean allowPrintCertificate;
    private boolean directPrint;
    private boolean addNewMode;
    private boolean certificateSearch;


    public DeathRegisterAction(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, BDDivisionDAO bdDivisionDAO,
        CountryDAO countryDAO, DeathRegistrationService deathRegistrationService, AppParametersDAO appParametersDAO,
        RaceDAO raceDAO, DeathAlterationService deathAlterationService, UserLocationDAO userLocationDAO, UserDAO userDAO,
        LocationDAO locationDAO, CommonUtil commonUtil) {
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.countryDAO = countryDAO;
        this.service = deathRegistrationService;
        this.appParametersDAO = appParametersDAO;
        this.raceDAO = raceDAO;
        this.deathAlterationService = deathAlterationService;
        this.userLocationDAO = userLocationDAO;
        this.userDAO = userDAO;
        this.locationDAO = locationDAO;
        this.commonUtil = commonUtil;
    }


    public String welcome() {
        return SUCCESS;
    }

    public String initDeathHome() {
        return SUCCESS;
    }

    public String initDeathDeclaration() {
        DeathRegister ddf;
        if (!addNewMode) {
            session.remove(WebConstants.SESSION_DEATH_DECLARATION_BEAN);
        }
        ddf = new DeathRegister();
        // ddf.setDeathType(deathType);   bug 2139
        session.put(WebConstants.SESSION_DEATH_DECLARATION_BEAN, ddf);
        populate(ddf);
        //populate district list and ds division list
        districtList = districtDAO.getAllDistrictNames(language, user);
        Set<Integer> districtKey = districtList.keySet();
        //set first key for loading DS Divisions
        dsDivisionList = dsDivisionDAO.getDSDivisionNames(districtKey.iterator().next(), language, user);
        permenantAddressDsDivisionList = dsDivisionList;
        return SUCCESS;
    }

    public String deathDeclaration() {
        logger.debug("Step {} of 2", pageNo);
        DeathRegister ddf;
        if (back) {
            populate((DeathRegister) session.get(WebConstants.SESSION_DEATH_DECLARATION_BEAN));
            if (deathPersonPermenentAddressDistrictId != 0) {
                permenantAddressDsDivisionList = dsDivisionDAO.getAllDSDivisionNames(deathPersonPermenentAddressDistrictId, language, user);
            } else {
                permenantAddressDsDivisionList = dsDivisionDAO.getAllDSDivisionNames(districtList.keySet().iterator().next(),
                    language, user);
            }
            return "form" + pageNo;
        }
        ddf = (DeathRegister) session.get(WebConstants.SESSION_DEATH_DECLARATION_BEAN);
        switch (pageNo) {
            case 1:
                DeathRegister dd = service.getByBDDivisionAndDeathSerialNo(death.getDeathDivision(), death.getDeathSerialNo(), user);
                if (dd != null && dd.getIdUKey() != ddf.getIdUKey()) {
                    addFieldError("duplicateSerialNumberError", getText("p1.duplicateSerialNumber.label"));
                    pageNo = 0;
                }
                switch (pageType) {
                    case 0:
                        deathType = DeathRegister.Type.NORMAL;
                        break;
                    case 1:
                        deathType = DeathRegister.Type.LATE;
                        break;
                    case 2:
                        deathType = DeathRegister.Type.SUDDEN;
                        break;
                    case 3:
                        deathType = DeathRegister.Type.MISSING;
                        break;
                }
                ddf.setDeath(death);
                ddf.setDeathPerson(deathPerson);
                ddf.setDeathType(deathType);
                beanPopulate(ddf);
                break;
            case 2:
                deathType = ddf.getDeathType();
                ddf.setDeclarant(declarant);
                ddf.setNotifyingAuthority(notifyingAuthority);
                idUKey = ddf.getIdUKey();
                if (idUKey == 0) {
                    try {
                        service.addNormalDeathRegistration(ddf, user);
                        idUKey = ddf.getIdUKey();
                        addActionMessage(getText("saveSuccess.label"));
                    } catch (CRSRuntimeException e) {
                        logger.debug("adding death registration fails for death register serial number ");
                        switch (e.getErrorCode()) {
                            case ErrorCodes.INCOMPLETE_DECLERENT:
                                addActionError(getText("error.add.fails.check.declerent.info"));
                                break;
                            default:
                                addActionError(getText("error.add.death.registration"));
                        }
                        pageNo = 0;
                    }
                } else {
                    try {
                        service.updateDeathRegistration(ddf, user);
                        addActionMessage(getText("editDataSaveSuccess.label"));
                    }
                    catch (CRSRuntimeException e) {
                        switch (e.getErrorCode()) {
                            case ErrorCodes.ILLEGAL_STATE:
                                addActionError(getText("error.invalid.state.for.edit"));
                                editMode = true;
                                break;
                        }
                    }
                    catch (NullPointerException e) {
                        addActionError(getText("error.invalid.state.for.edit"));
                        editMode = true;
                    }
                }
        }
        populate(ddf);
        return "form" + pageNo;
    }


    public String deathCertificate() {
        deathRegister = service.getWithTransientValuesById(idUKey, user);
        if ((deathRegister.getStatus() != DeathRegister.State.ARCHIVED_CERT_GENERATED) && (deathRegister.getStatus()
            != DeathRegister.State.ARCHIVED_ALTERED) && (deathRegister.getStatus() != DeathRegister.State.APPROVED)) {
            addActionError(getText("death.error.no.permission.print"));
            logger.debug("Current state of adoption certificate : {}", deathRegister.getStatus());
            return ERROR;
        } else {
            deathPerson = deathRegister.getDeathPerson();
            death = deathRegister.getDeath();
            declarant = deathRegister.getDeclarant();
            notifyingAuthority = deathRegister.getNotifyingAuthority();
            declarant = deathRegister.getDeclarant();

            genderEn = GenderUtil.getGender(deathPerson.getDeathPersonGender(), AppConstants.ENGLISH);
            genderSi = GenderUtil.getGender(deathPerson.getDeathPersonGender(), death.getPreferredLanguage());

            deathPersonDeathDivision = bdDivisionDAO.getNameByPK(deathRegister.getDeath().getDeathDivision().getDivisionId(),
                death.getPreferredLanguage());
            deathPersonDeathDivisionEn = bdDivisionDAO.getNameByPK(deathRegister.getDeath().getDeathDivision().getDivisionId(),
                AppConstants.ENGLISH);
            deathPersondsDivision = dsDivisionDAO.getNameByPK(bdDivisionDAO.getBDDivisionByPK(
                deathRegister.getDeath().getDeathDivision().getDivisionId()).getDsDivision().getDsDivisionUKey(), death.getPreferredLanguage());
            deathPersondsDivisionEn = dsDivisionDAO.getNameByPK(bdDivisionDAO.getBDDivisionByPK(
                deathRegister.getDeath().getDeathDivision().getDivisionId()).getDsDivision().getDsDivisionUKey(), AppConstants.ENGLISH);
            deathPersonDistrict = districtDAO.getNameByPK(bdDivisionDAO.getBDDivisionByPK(
                deathRegister.getDeath().getDeathDivision().getDivisionId()).getDistrict().getDistrictUKey(), death.getPreferredLanguage());
            deathPersonDistrictEn = districtDAO.getNameByPK(bdDivisionDAO.getBDDivisionByPK(
                deathRegister.getDeath().getDeathDivision().getDivisionId()).getDistrict().getDistrictUKey(), AppConstants.ENGLISH);
            initPermissionForApprovalAndPrint();
            //loading historical recodes
            archivedEntryList = service.getArchivedCorrectedEntriesForGivenSerialNo(deathRegister.getDeath().getDeathDivision(),
                deathRegister.getDeath().getDeathSerialNo(), idUKey, user);
            //create changed bit set to display *  marks
            if (archivedEntryList.size() > 0) {
                displayChangesInStarMark(archivedEntryList);
            }

            //display user locations

            locationList = commonUtil.populateActiveUserLocations(user, language);
            if (!locationList.isEmpty()) {
                int selectedLocationId = locationList.keySet().iterator().next();
                userList = new HashMap<String, String>();
                // TODO temporaray solution have to change this after caching done for user locations
                for (User u : userLocationDAO.getBirthCertSignUsersByLocationId(selectedLocationId, true)) {
                    userList.put(u.getUserId(), NameFormatUtil.getDisplayName(u.getUserName(), 50));
                }
            }
            locationId = user.getPrimaryLocation().getLocationUKey();
            issueUserId = user.getUserId();
            return SUCCESS;
        }
    }

    private void displayChangesInStarMark(List<DeathRegister> deathRegisters) {
        changedFields = new BitSet();
        for (int i = 0; i < deathRegisters.size(); i++) {
            DeathAlteration da = deathAlterationService.getAlterationByDeathCertificateNumber(deathRegisters.get(i).getIdUKey(), user).get(0);
            changedFields.or(da.getApprovalStatuses());
        }
        logger.debug("bit sets merge and final bit set : {}", changedFields);
    }

    public String deathApprovalAndPrint() {
        if (pageNo == 2) {
            session.remove(WebConstants.SESSION_DEATH_DECLARATION_BEAN);
        }
        setPageNo(1);
        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        populate();
        initPermissionForApprovalAndPrint();
        if (state != null) {
            deathApprovalAndPrintList = service.getPaginatedListForStateByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId),
                pageNo, noOfRows, state, user);
        } else {
            deathApprovalAndPrintList = service.getPaginatedListForAllByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId),
                pageNo, noOfRows, user);
        }
        paginationHandler(deathApprovalAndPrintList.size());
        previousFlag = false;

        return SUCCESS;
    }

    private void filterDeathRegistrationApprovalList() {

        initPermissionForApprovalAndPrint();

        searchByDate = ((fromDate != null) && (endDate != null));

        if (searchByDate) {
            //search by date in given division deathDivisions and all the status
            if (currentStatus == 0) {
                if (deathDivisionId == 0) {
                    //for all death divisions
                    deathApprovalAndPrintList = service.getPaginatedDeathRegisterListByDSDivisionAndRegistrationDateRange(
                        dsDivisionId, fromDate, endDate, true, pageNo, noOfRows, user);
                } else {
                    deathApprovalAndPrintList = service.getByBDDivisionAndRegistrationDateRange(
                        bdDivisionDAO.getBDDivisionByPK(deathDivisionId), fromDate, endDate, pageNo, noOfRows, user);
                }
            } else {
                if (deathDivisionId == 0) {
                    //for all death divisions
                    deathApprovalAndPrintList = service.getPaginatedDeathRegisterListByDSDivisionAndRegistrationDateRangeAndState(
                        dsDivisionId, fromDate, endDate, true, pageNo, noOfRows, state, user);
                } else {
                    deathApprovalAndPrintList = service.getByBDDivisionAndRegistrationDateRangeAndState(
                        bdDivisionDAO.getBDDivisionByPK(deathDivisionId), fromDate, endDate, pageNo, noOfRows, state, user);
                }
            }
            addActionMessage(getText("message.search.results.form.to", new String[]
                {DateTimeUtils.getISO8601FormattedString(fromDate), DateTimeUtils.getISO8601FormattedString(endDate)}));

        } else {
            if (currentStatus == 0) {
                if (deathDivisionId != 0) {
                    //search by state with all state with in a deathDivision
                    deathApprovalAndPrintList = service.getPaginatedListForAll(bdDivisionDAO.getBDDivisionByPK(deathDivisionId),
                        pageNo, noOfRows, user);
                } else {
                    deathApprovalAndPrintList = service.getPaginatedListForAllByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId),
                        pageNo, noOfRows, user);
                }
            } else {
                if (deathDivisionId != 0) {
                    //search by state with a state with in a deathDivision
                    deathApprovalAndPrintList = service.getPaginatedListForState(bdDivisionDAO.getBDDivisionByPK(deathDivisionId),
                        pageNo, noOfRows, state, user);
                } else {
                    deathApprovalAndPrintList = service.getPaginatedListForStateByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId),
                        pageNo, noOfRows, state, user);
                }
            }
        }
    }

    public String filterByStatus() {
        logger.debug("requested to filter by : {}", currentStatus);
        //     setPageNo(1);
        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        pageNo = 1;
        filterDeathRegistrationApprovalList();
        populate();
        paginationHandler(deathApprovalAndPrintList.size());
        return SUCCESS;
    }


    /**
     * This method is used for pagination(move backward) in marriage notice search list page
     */
    public String previouseDeathRegisterApprovalPage() {
        logger.debug("Previous page of Marriage notices search list loaded");
        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        pageNo = printStart / noOfRows;
        printStart -= noOfRows;
        filterDeathRegistrationApprovalList();
        showNoticeSearchResultSize();
        populate();
        paginationHandler(deathApprovalAndPrintList.size());
        return SUCCESS;
    }

    /**
     * This method is used for pagination(move forward) in marriage notice search list page
     */
    public String nextDeathRegisterApprovalPage() {
        logger.debug("Next page of Marriage notices search list loaded");
        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        pageNo = ((printStart + noOfRows) / noOfRows) + 1;
        printStart += noOfRows;
        filterDeathRegistrationApprovalList();
        showNoticeSearchResultSize();
        populate();
        paginationHandler(deathApprovalAndPrintList.size());
        return SUCCESS;
    }

    /**
     * This method is used to show action message if search result list is empty
     */
    private void showNoticeSearchResultSize() {
        if (deathApprovalAndPrintList.size() == 0) {
            addActionMessage(getText("noItemMsg.label"));
        }
        logger.debug("death register print and approval list loaded with size : {}", deathApprovalAndPrintList.size());
    }

    public String test() {
        return SUCCESS;
    }

    public String deathDeclarationEditMode() {
        logger.debug("death edit mode requested for idUkey : {} ", idUKey);
        DeathRegister deathRegister = service.getById(idUKey, user);

        deathType = deathRegister.getDeathType();
        idUKey = deathRegister.getIdUKey();
        pageTypeGetter(deathType);
        beanPopulate(deathRegister);
        selectDropDownListForEditMode(deathRegister);
        if (deathRegister.getStatus() != DeathRegister.State.DATA_ENTRY) {
            addActionError(getText("death.error.editNotAllowed"));
            return ERROR;
        }
        session.put(WebConstants.SESSION_DEATH_DECLARATION_BEAN, deathRegister);
        populate();
        return SUCCESS;
    }

    private void selectDropDownListForEditMode(DeathRegister deathRegister) {
        BDDivision bddivision = deathRegister.getDeath().getDeathDivision();
        dsDivisionId = bddivision.getDsDivision().getDsDivisionUKey();
        deathDivisionId = bddivision.getBdDivisionUKey();
        deathDistrictId = bddivision.getDistrict().getDistrictUKey();
        DSDivision dsDivisionPermanentAddress = deathRegister.getDeathPerson().getDsDivisionOfPermanentAddress();
        deathPersonPermenentAddressDistrictId = (dsDivisionPermanentAddress != null) ?
            dsDivisionPermanentAddress.getDistrict().getDistrictUKey() : 0;
        deathPersonPermenentAddressDSDivisionId = (dsDivisionPermanentAddress != null) ?
            deathRegister.getDeathPerson().getDsDivisionOfPermanentAddress().getDsDivisionUKey() : 0;
        permenantAddressDsDivisionList = dsDivisionDAO.getAllDSDivisionNames(deathPersonPermenentAddressDistrictId, language, user);
    }

    private void pageTypeGetter(DeathRegister.Type type) {
        switch (type) {
            case NORMAL:
                pageType = 0;
                break;
            case LATE:
                pageType = 1;
                break;
            case SUDDEN:
                pageType = 2;
                break;
            case MISSING:
                pageType = 3;
                break;
        }
    }

    public String approveDeath() {
        logger.debug("requested to approve Death Declaration with idUKey : {} and current state : {} ", idUKey,
            currentStatus);
        approveDeathRegistration();
        if (warnings != null && warnings.size() > 0) {
            return "warning";
        }

        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        if (deathDivisionId != 0) {
            deathApprovalAndPrintList = service.getPaginatedListForAll(bdDivisionDAO.getBDDivisionByPK(deathDivisionId), pageNo, noOfRows, user);
        } else {
            deathApprovalAndPrintList = service.getPaginatedListForAllByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, user);
        }

        initPermissionForApprovalAndPrint();
        populate();
        return SUCCESS;
    }

    public String directApproveDeath() {
        logger.debug("requested to direct approve Death Declaration with idUKey : {}", idUKey);
        try {
            warnings = service.approveDeathRegistration(idUKey, user, ignoreWarning);
            displayApprovalSuccess();
            pageNo = 3;
        } catch (RGDRuntimeException e) {
            switch (e.getErrorCode()) {
                case ErrorCodes.PRS_DUPLICATE_NIC:
                    final DeathRegister dr = service.getById(idUKey, user);
                    addActionError(getText("error.death.duplicateNic.fail",
                        new String[]{Long.toString(dr.getDeath().getDeathSerialNo()), dr.getDeathPerson().getDeathPersonPINorNIC()}));
                    break;
                case ErrorCodes.INVALID_STATE_FOR_APPROVE_DEATH_REGISTRATION:
                    break;
                case ErrorCodes.UNABLE_TO_APPROVE_LATE_DEATH_REGISTRATION_NEED_HIGHER_APPROVAL_THAN_DR:
                    addActionError(getText("error.death.registration.approval.fail.need.higher.approval", new String[]{Long.toString(idUKey)}));
                    break;
                default:
                    addActionError(getText("error.death.registration.approval.fail", new String[]{Long.toString(idUKey)}));
            }
            logger.debug("death register approval fails idUKey : {} ", idUKey);
            pageNo = 2;
        }
        initPermissionForApprovalAndPrint();
        populate();
        directApprove = true;
        return SUCCESS;
    }

    public String directApproveIgnoringWornings() {
        logger.debug("Direct approve death registration with IdUKey : {} IgnoreWarning : {}", idUKey, ignoreWarning);
        if (ignoreWarning) {
            try {
                warnings = service.approveDeathRegistration(idUKey, user, ignoreWarning);
                addActionMessage(getText("message.approve.success"));
                pageNo = 4;
            } catch (RGDRuntimeException e) {
                switch (e.getErrorCode()) {
                    case ErrorCodes.PRS_DUPLICATE_NIC:
                        final DeathRegister dr = service.getById(idUKey, user);
                        addActionError(getText("error.death.duplicateNic.fail",
                            new String[]{Long.toString(dr.getDeath().getDeathSerialNo()), dr.getDeathPerson().getDeathPersonPINorNIC()}));
                        break;
                    default:
                        addActionError(getText("error.death.registration.approval.fail", new String[]{Long.toString(idUKey)}));
                }
                logger.debug("death register approval fails idUKey : {} ", idUKey);
                pageNo = 2;
            }
            initPermissionForApprovalAndPrint();
            populate();
        }
        return SUCCESS;
    }

    public String rejectDeath() {
        if (reject) {
            //load get comment page for rejection
            return "getComment";
        }
        logger.debug("requested to reject Death Declaration with idUKey : {}", idUKey);
        try {
            service.rejectDeathRegistration(idUKey, user, comment);
        } catch (CRSRuntimeException e) {
            //todo
            /*
            * after returning this error comment page doesn't have death object so when submitting it gives exception
            * so set object here before foreword to page
            * */
            addFieldError("errorField", getText("%{death.comment.is.required}"));
            return "getComment";
        }
        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        if (deathDivisionId != 0) {
            deathApprovalAndPrintList = service.getPaginatedListForAll(bdDivisionDAO.getBDDivisionByPK(deathDivisionId), pageNo, noOfRows, user);
        } else {
            deathApprovalAndPrintList = service.getPaginatedListForAllByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, user);
        }
        initPermissionForApprovalAndPrint();
        populate();
        return SUCCESS;
    }

    public String deleteDeath() {
        logger.debug("requested to delete Death Declaration with idUKey : {}", idUKey);
        DeathRegister death = service.getById(idUKey, user);
        if (death.getStatus().equals(DeathRegister.State.DATA_ENTRY)) {
            service.deleteDeathRegistration(idUKey, user);
            addActionMessage(getText("message.successfully.deleted"));
        }
        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        if (deathDivisionId != 0) {
            deathApprovalAndPrintList = service.getPaginatedListForAll(bdDivisionDAO.
                getBDDivisionByPK(deathDivisionId), pageNo, noOfRows, user);
        } else {
            deathApprovalAndPrintList = service.
                getPaginatedListForAllByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, user);
        }
        initPermissionForApprovalAndPrint();
        populate();
        return SUCCESS;
    }

    public String deathDeclarationViewMode() {

        logger.debug("Non Editable Mode Step {} of 2", pageNo);
        DeathRegister ddf;
        if (back) {
            populate((DeathRegister) session.get(WebConstants.SESSION_DEATH_DECLARATION_BEAN));
            return "form" + pageNo;
        } else {
            if (pageNo < 0 || pageNo > 2) {
                addActionError(getText("p1.invalid.Entry"));
                return ERROR;
            }
            if (pageNo == 0) {
                logger.debug("initializing non editable mode for IDUKey {}", idUKey);
                try {
                    ddf = service.getById(idUKey, user);
                    deathType = ddf.getDeathType();
                    session.put(WebConstants.SESSION_DEATH_DECLARATION_BEAN, ddf);

                } catch (Exception e) {
                    handleErrors(e);
                    addActionError(getText("p1.invalid.Entry"));
                    return ERROR;
                }
            }
            if (pageNo == 2) {
                session.remove(WebConstants.SESSION_DEATH_DECLARATION_BEAN);
            }

            return "form" + pageNo;
        }
    }

    public String markDeathDeclarationAsPrinted() {
        logger.debug("requested to mark Death Declaration as printed for idUKey : {} ", idUKey);
        if (directPrint) {
            //todo remove why this ???
            logger.debug("direct requested to mark Death Declaration as printed for idUKey : {} ", idUKey);
            service.markDeathCertificateAsPrinted(deathRegister, user);
        } else {
            logger.debug("not direct requested to mark Death Declaration as printed for idUKey : {} ", idUKey);
            deathRegister = service.getById(idUKey, user);
            if (deathRegister != null && deathRegister.getStatus() == DeathRegister.State.APPROVED) {
                try {
                    if (locationId != 0 && issueUserId != null) {
                        logger.debug("Certificate issued locationId : {} and userId : {}", locationId, issueUserId);
                        deathRegister.setOriginalDCIssueUser(userDAO.getUserByPK(issueUserId));
                        deathRegister.setOriginalDCPlaceOfIssue(locationDAO.getLocation(locationId));
                        service.markDeathCertificateAsPrinted(deathRegister, user);
                        addActionMessage(getText("message.mark.as.print.success",
                            new String[]{Long.toString(deathRegister.getDeath().getDeathSerialNo())}));
                    } else {
                        addActionError(getText("error.mark.as.print",
                            new String[]{Long.toString(deathRegister.getDeath().getDeathSerialNo())}));
                        logger.debug("For the first time Death certificate print issued location and user must be filled");
                    }
                } catch (CRSRuntimeException e) {
                    addActionError("death.error.no.permission.print");
                }
            } else if (deathRegister != null && deathRegister.getStatus() == DeathRegister.State.ARCHIVED_CERT_GENERATED) {
                logger.debug("already marked as printed death register record : idUKey {}", idUKey);
                addActionMessage(getText("message.already.marked.as.print",
                    new String[]{Long.toString(deathRegister.getDeath().getDeathSerialNo())}));
            }
            if (pageNo == 0) {
                pageNo = 1;
                //todo urgent remove this fix issue with pageNo
            }
            noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
            if (deathDivisionId != 0) {
                deathApprovalAndPrintList = service.getPaginatedListForAll(
                    bdDivisionDAO.getBDDivisionByPK(deathDivisionId), pageNo, noOfRows, user);
            } else {
                deathApprovalAndPrintList = service.getPaginatedListForAllByDSDivision(
                    dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, user);
            }
        }

        initPermissionForApprovalAndPrint();
        populate();
        return SUCCESS;
    }

    /**
     * method to ignore death certificate marking as printed and
     * load the previous state of the list page
     *
     * @return
     */
    public String backToPreviousState() {
        noOfRows = appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE);
        if (deathDivisionId != 0) {
            deathApprovalAndPrintList = service.getPaginatedListForAll(bdDivisionDAO.getBDDivisionByPK(deathDivisionId), pageNo, noOfRows, user);
        } else {
            deathApprovalAndPrintList = service.getPaginatedListForAllByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, user);
        }
        initPermissionForApprovalAndPrint();
        populate();
        return SUCCESS;
    }

    /**
     * responsible whether to display the next link in
     * the jsp or not and handles the page number
     *
     * @param recordsFound no of AdoptionOrders found
     */
    public void paginationHandler(int recordsFound) {
        if (recordsFound == appParametersDAO.getIntParameter(DEATH_APPROVAL_AND_PRINT_ROWS_PER_PAGE)) {
            setNextFlag(true);
        } else {
            setNextFlag(false);
        }
    }

    /**
     * This method is used to approve death registrations
     */
    private void approveDeathRegistration() {
        try {
            warnings = service.approveDeathRegistration(idUKey, user, ignoreWarning);
            displayApprovalSuccess();
        } catch (RGDRuntimeException e) {
            handleDeathApprovalException(e);
        }
    }

    /**
     * This method is used to handle death approval related exceptions and show appropriate messages
     */
    private void handleDeathApprovalException(RGDRuntimeException e) {
        switch (e.getErrorCode()) {
            case ErrorCodes.PRS_DUPLICATE_NIC:
                final DeathRegister dr = service.getById(idUKey, user);
                addActionError(getText("error.death.duplicateNic.fail",
                    new String[]{Long.toString(dr.getDeath().getDeathSerialNo()), dr.getDeathPerson().getDeathPersonPINorNIC()}));
                break;
            case ErrorCodes.UNABLE_TO_APPROVE_LATE_DEATH_REGISTRATION_NEED_HIGHER_APPROVAL_THAN_DR:
                addActionError(getText("error.death.registration.approval.fail.need.higher.approval", new String[]{Long.toString(idUKey)}));
                break;
            default:
                addActionError(getText("error.death.registration.approval.fail", new String[]{Long.toString(idUKey)}));
        }
        logger.debug("death register approval fails idUKey : {} ", idUKey);
    }

    private void displayApprovalSuccess() {
        if (warnings != null && warnings.isEmpty()) {
            addActionMessage(getText("message.approve.success"));
        }
    }

    private void populate() {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        populateBasicLists(language);
        populateDynamicLists(language);
    }

    private void populate(DeathRegister ddf) {
        String language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        populateBasicLists(language);
        deathType = ddf.getDeathType();

        if (addNewMode) {
            DeathRegister oldDdf = service.getById(oldIdUKey, user);
            logger.debug("Old IdUkey : {} ", oldIdUKey);
            death = new DeathInfo();
            death.setDeathSerialNo(oldDdf.getDeath().getDeathSerialNo() + 1);
            death.setDateOfRegistration(oldDdf.getDeath().getDateOfRegistration());
            death.setDeathDivision(oldDdf.getDeath().getDeathDivision());

            deathRegister = new DeathRegister();
            deathRegister.setDeathType(DeathRegister.Type.NORMAL);

            deathDistrictId = oldDdf.getDeath().getDeathDivision().getDistrict().getDistrictUKey();
            deathDivisionId = oldDdf.getDeath().getDeathDivision().getBdDivisionUKey();
            dsDivisionId = oldDdf.getDeath().getDeathDivision().getDsDivision().getDsDivisionUKey();

            populateDynamicLists(language);

            notifyingAuthority = new NotifyingAuthorityInfo();
            notifyingAuthority.setNotifyingAuthorityPIN(oldDdf.getNotifyingAuthority().getNotifyingAuthorityPIN());
            notifyingAuthority.setNotifyingAuthorityName(oldDdf.getNotifyingAuthority().getNotifyingAuthorityName());
            notifyingAuthority.setNotifyingAuthorityAddress(oldDdf.getNotifyingAuthority().getNotifyingAuthorityAddress());
            notifyingAuthority.setNotifyingAuthoritySignDate(oldDdf.getNotifyingAuthority().getNotifyingAuthoritySignDate());

            ddf.setNotifyingAuthority(notifyingAuthority);
            ddf.setDeath(death);
            session.put(WebConstants.SESSION_DEATH_DECLARATION_BEAN, ddf);

        }
        populateDynamicLists(language);
        beanPopulate(ddf);

        boolean idsPopulated = false;
        if (death != null) {
            if (death.getDeathDivision() != null) {  //if data present, populate with existing values
                deathDistrictId = death.getDeathDivision().getDistrict().getDistrictUKey();
                deathDivisionId = death.getDeathDivision().getBdDivisionUKey();
                dsDivisionId = death.getDeathDivision().getDsDivision().getDsDivisionUKey();
                idsPopulated = true;
            }
            logger.debug("Districts, DS and BD divisions set from RegisterInfo : {} {}", deathDistrictId, dsDivisionId);
        }

        if (!idsPopulated) {         // populate district and ds div Ids with user preferences or set to 0 temporarily
            if (user.getPrefBDDistrict() != null) {
                deathDistrictId = user.getPrefBDDistrict().getDistrictUKey();
                logger.debug("Preferred district {} set in user {}", deathDistrictId, user.getUserId());
            } else {
                deathDistrictId = 0;
                logger.debug("First district in the list {} was set in user {}", deathDistrictId, user.getUserId());
            }

            if (user.getPrefBDDSDivision() != null) {
                dsDivisionId = user.getPrefBDDSDivision().getDsDivisionUKey();
            } else {
                dsDivisionId = 0;
            }
            logger.debug("Districts, DS and BD divisions set from defaults : {} {}", deathDistrictId, dsDivisionId);
        }
    }

    private void beanPopulate(DeathRegister ddf) {
        //TODO is all needed
        deathPerson = ddf.getDeathPerson();
        death = ddf.getDeath();
        declarant = ddf.getDeclarant();
        notifyingAuthority = ddf.getNotifyingAuthority();
        declarant = ddf.getDeclarant();
        setLifeCycleInfo(ddf.getLifeCycleInfo());
    }

    private void handleErrors(Exception e) {
        logger.error("Handle Error ", e);
        //todo pass the error to the error.jsp page
    }

    public void initPermissionForApprovalAndPrint() {
        allowApproveDeath = user.isAuthorized(Permission.APPROVE_DEATH);
        allowEditDeath = user.isAuthorized(Permission.EDIT_DEATH);
        allowPrintCertificate = user.isAuthorized(Permission.PRINT_DEATH_CERTIFICATE);
    }

    private void populateBasicLists(String language) {
        districtList = districtDAO.getDistrictNames(language, user);
        setCountryList(countryDAO.getCountries(language));
        setRaceList(raceDAO.getRaces(language));
    }

    private void populateDynamicLists(String language) {
        if (getDeathDistrictId() == 0) {
            if (!districtList.isEmpty()) {
                setDeathDistrictId(districtList.keySet().iterator().next());
                logger.debug("first allowed district in the list {} was set", getDeathDistrictId());
            }
        }
        dsDivisionList = dsDivisionDAO.getDSDivisionNames(getDeathDistrictId(), language, user);

        if (getDsDivisionId() == 0) {
            if (!dsDivisionList.isEmpty()) {
                setDsDivisionId(dsDivisionList.keySet().iterator().next());
                logger.debug("first allowed DS Div in the list {} was set", getDsDivisionId());
            }
        }

        bdDivisionList = bdDivisionDAO.getBDDivisionNames(getDsDivisionId(), language, user);
        /*if (getDeathDivisionId() == 0) {
            setDeathDivisionId(bdDivisionList.keySet().iterator().next());
            logger.debug("first allowed BD Div in the list {} was set", getDeathDivisionId());
        }*/
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageNo() {
        return pageNo;
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map map) {
        this.session = map;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        logger.debug("setting User: {}", user.getUserName());
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

    public Map<Integer, String> getBdDivisionList() {
        return bdDivisionList;
    }

    public void setBdDivisionList(Map<Integer, String> bdDivisionList) {
        this.bdDivisionList = bdDivisionList;
    }

    public DistrictDAO getDistrictDAO() {
        return districtDAO;
    }

    public BDDivisionDAO getBdDivisionDAO() {
        return bdDivisionDAO;
    }

    public DSDivisionDAO getDsDivisionDAO() {
        return dsDivisionDAO;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getDeathDistrictId() {
        return deathDistrictId;
    }

    public void setDeathDistrictId(int deathDistrictId) {
        this.deathDistrictId = deathDistrictId;
    }

    public int getDeathDivisionId() {
        return deathDivisionId;
    }

    public void setDeathDivisionId(int deathDivisionId) {
        this.deathDivisionId = deathDivisionId;
        if (death == null) {
            death = new DeathInfo();
        }
        death.setDeathDivision(bdDivisionDAO.getBDDivisionByPK(deathDivisionId));
        logger.debug("setting DeathDivision: {}", death.getDeathDivision().getEnDivisionName());

    }

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
    }

    public Map<Integer, String> getCountryList() {
        return countryList;
    }

    public void setCountryList(Map<Integer, String> countryList) {
        this.countryList = countryList;
    }

    public DeathInfo getDeath() {
        return death;
    }

    public void setDeath(DeathInfo death) {
        this.death = death;
    }

    public DeathPersonInfo getDeathPerson() {
        return deathPerson;
    }

    public void setDeathPerson(DeathPersonInfo deathPerson) {
        this.deathPerson = deathPerson;
    }

    public DeclarantInfo getDeclarant() {
        return declarant;
    }

    public void setDeclarant(DeclarantInfo declarant) {
        this.declarant = declarant;
    }

    public NotifyingAuthorityInfo getNotifyingAuthority() {
        return notifyingAuthority;
    }

    public void setNotifyingAuthority(NotifyingAuthorityInfo notifyingAuthority) {
        this.notifyingAuthority = notifyingAuthority;
    }

    public int getNoOfRows() {
        return noOfRows;
    }

    public void setNoOfRows(int noOfRows) {
        this.noOfRows = noOfRows;
    }

    public boolean isAllowApproveAdoption() {
        return allowApproveDeath;
    }

    public void setAllowApproveAdoption(boolean allowApproveAdoption) {
        this.allowApproveDeath = allowApproveAdoption;
    }

    public int getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
        switch (currentStatus) {
            case 1:
                this.state = DeathRegister.State.DATA_ENTRY;
                break;
            case 2:
                this.state = DeathRegister.State.APPROVED;
                break;
            case 3:
                this.state = DeathRegister.State.REJECTED;
                break;
            case 4:
                this.state = DeathRegister.State.ARCHIVED_CERT_GENERATED;
                //speacial case 0 all status
        }
    }

    public boolean isNextFlag() {
        return nextFlag;
    }

    public boolean isAllowApproveDeath() {
        return allowApproveDeath;
    }

    public void setAllowApproveDeath(boolean allowApproveDeath) {
        this.allowApproveDeath = allowApproveDeath;
    }

    public boolean isAllowEditDeath() {
        return allowEditDeath;
    }

    public void setAllowEditDeath(boolean allowEditDeath) {
        this.allowEditDeath = allowEditDeath;
    }

    public void setNextFlag(boolean nextFlag) {
        this.nextFlag = nextFlag;
    }

    public List<DeathRegister> getDeathApprovalAndPrintList() {
        return deathApprovalAndPrintList;
    }

    public void setDeathApprovalAndPrintList(List<DeathRegister> deathApprovalAndPrintList) {
        this.deathApprovalAndPrintList = deathApprovalAndPrintList;
    }

    public boolean isPreviousFlag() {
        return previousFlag;
    }

    public void setPreviousFlag(boolean previousFlag) {
        this.previousFlag = previousFlag;
    }

    public boolean isBack() {
        return back;
    }

    public void setBack(boolean back) {
        this.back = back;
    }

    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public String getGenderEn() {
        return genderEn;
    }

    public void setGenderEn(String genderEn) {
        this.genderEn = genderEn;
    }

    public String getGenderSi() {
        return genderSi;
    }

    public void setGenderSi(String genderSi) {
        this.genderSi = genderSi;
    }

    public String getDeathPersonDistrict() {
        return deathPersonDistrict;
    }

    public void setDeathPersonDistrict(String deathPersonDistrict) {
        this.deathPersonDistrict = deathPersonDistrict;
    }

    public String getDeathPersonDsDivision() {
        return deathPersonDsDivision;
    }

    public void setDeathPersonDsDivision(String deathPersonDsDivision) {
        this.deathPersonDsDivision = deathPersonDsDivision;
    }

    public String getDeathPersonDeathDivision() {
        return deathPersonDeathDivision;
    }

    public void setDeathPersonDeathDivision(String deathPersonDeathDivision) {
        this.deathPersonDeathDivision = deathPersonDeathDivision;
    }


    public String getDeathPersonDeathDivisionEn() {
        return deathPersonDeathDivisionEn;
    }

    public void setDeathPersonDeathDivisionEn(String deathPersonDeathDivisionEn) {
        this.deathPersonDeathDivisionEn = deathPersonDeathDivisionEn;
    }

    public String getDeathPersonDistrictEn() {
        return deathPersonDistrictEn;
    }

    public void setDeathPersonDistrictEn(String deathPersonDistrictEn) {
        this.deathPersonDistrictEn = deathPersonDistrictEn;
    }

    public String getDeathPersondsDivision() {
        return deathPersondsDivision;
    }

    public void setDeathPersondsDivision(String deathPersondsDivision) {
        this.deathPersondsDivision = deathPersondsDivision;
    }

    public String getDeathPersondsDivisionEn() {
        return deathPersondsDivisionEn;
    }

    public void setDeathPersondsDivisionEn(String deathPersondsDivisionEn) {
        this.deathPersondsDivisionEn = deathPersondsDivisionEn;
    }

    public DeathRegister getDeathRegister() {
        return deathRegister;
    }

    public void setDeathRegister(DeathRegister deathRegister) {
        this.deathRegister = deathRegister;
    }

    public DeathRegister.State getState() {
        return state;
    }

    public void setState(DeathRegister.State state) {
        this.state = state;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isSearchByDate() {
        return searchByDate;
    }

    public void setSearchByDate(boolean searchByDate) {
        this.searchByDate = searchByDate;
    }

    public DeathRegister.Type getDeathType() {
        return deathType;
    }

    public void setDeathType(DeathRegister.Type deathType) {
        this.deathType = deathType;
    }

    public int getDeathPersonCountry() {
        return deathPersonCountry;
    }

    public void setDeathPersonCountry(int deathPersonCountry) {
        this.deathPersonCountry = deathPersonCountry;
        if (deathPerson == null) {
            deathPerson = new DeathPersonInfo();
        }
        deathPerson.setDeathPersonCountry(countryDAO.getCountry(deathPersonCountry));
    }

    public int getDeathPersonRace() {
        return deathPersonRace;
    }

    public void setDeathPersonRace(int deathPersonRace) {
        this.deathPersonRace = deathPersonRace;
        if (deathPerson == null) {
            deathPerson = new DeathPersonInfo();
        }
        deathPerson.setDeathPersonRace(raceDAO.getRace(deathPersonRace));
    }

    public RaceDAO getRaceDAO() {
        return raceDAO;
    }

    public Map<Integer, String> getRaceList() {
        return raceList;
    }

    public void setRaceList(Map<Integer, String> raceList) {
        this.raceList = raceList;
    }

    public boolean isRePrint() {
        return rePrint;
    }

    public void setRePrint(boolean rePrint) {
        this.rePrint = rePrint;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isLateDeath() {
        return lateDeath;
    }

    public void setLateDeath(boolean lateDeath) {
        this.lateDeath = lateDeath;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public CRSLifeCycleInfo getLifeCycleInfo() {
        return lifeCycleInfo;
    }

    public void setLifeCycleInfo(CRSLifeCycleInfo lifeCycleInfo) {
        this.lifeCycleInfo = lifeCycleInfo;
    }

    public boolean isApprove() {
        return ignoreWarning;
    }

    public void setApprove(boolean approve) {
        this.ignoreWarning = approve;
    }

    public boolean isAllowPrintCertificate() {
        return allowPrintCertificate;
    }

    public void setAllowPrintCertificate(boolean allowPrintCertificate) {
        this.allowPrintCertificate = allowPrintCertificate;
    }

    public List<UserWarning> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<UserWarning> warnings) {
        this.warnings = warnings;
    }

    public boolean isDirectApprove() {
        return directApprove;
    }

    public void setDirectApprove(boolean directApprove) {
        this.directApprove = directApprove;
    }

    public boolean isDirectPrint() {
        return directPrint;
    }

    public void setDirectPrint(boolean directPrint) {
        this.directPrint = directPrint;
    }

    public boolean isAddNewMode() {
        return addNewMode;
    }

    public void setAddNewMode(boolean addNewMode) {
        this.addNewMode = addNewMode;
    }

    public long getOldIdUKey() {
        return oldIdUKey;
    }

    public void setOldIdUKey(long oldIdUKey) {
        this.oldIdUKey = oldIdUKey;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isReject() {
        return reject;
    }

    public void setReject(boolean reject) {
        this.reject = reject;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public boolean isIgnoreWarning() {
        return ignoreWarning;
    }

    public void setIgnoreWarning(boolean ignoreWarning) {
        this.ignoreWarning = ignoreWarning;
    }

    public boolean isCertificateSearch() {
        return certificateSearch;
    }

    public void setCertificateSearch(boolean certificateSearch) {
        this.certificateSearch = certificateSearch;
    }

    public List<DeathRegister> getArchivedEntryList() {
        return archivedEntryList;
    }

    public void setArchivedEntryList(List<DeathRegister> archivedEntryList) {
        this.archivedEntryList = archivedEntryList;
    }

    public BitSet getChangedFields() {
        return changedFields;
    }

    public void setChangedFields(BitSet changedFields) {
        this.changedFields = changedFields;
    }

    public Map<Integer, String> getLocationList() {
        return locationList;
    }

    public void setLocationList(Map<Integer, String> locationList) {
        this.locationList = locationList;
    }

    public Map<String, String> getUserList() {
        return userList;
    }

    public void setUserList(Map<String, String> userList) {
        this.userList = userList;
    }

    public String getNameOfOfficer() {
        return nameOfOfficer;
    }

    public void setNameOfOfficer(String nameOfOfficer) {
        this.nameOfOfficer = nameOfOfficer;
    }

    public String getPlaceOfIssue() {
        return placeOfIssue;
    }

    public void setPlaceOfIssue(String placeOfIssue) {
        this.placeOfIssue = placeOfIssue;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getIssueUserId() {
        return issueUserId;
    }

    public void setIssueUserId(String issueUserId) {
        this.issueUserId = issueUserId;
    }

    public int getPageType() {
        return pageType;
    }

    public void setPageType(int pageType) {
        this.pageType = pageType;
    }

    public int getPrintStart() {
        return printStart;
    }

    public void setPrintStart(int printStart) {
        this.printStart = printStart;
    }

    public int getDeathPersonPermenentAddressDSDivisionId() {
        return deathPersonPermenentAddressDSDivisionId;
    }

    public void setDeathPersonPermenentAddressDSDivisionId(int deathPersonPermenentAddressDSDivisionId) {
        this.deathPersonPermenentAddressDSDivisionId = deathPersonPermenentAddressDSDivisionId;
        //setting death with this value for death permanent address
    }

    public int getDeathPersonPermenentAddressDistrictId() {
        return deathPersonPermenentAddressDistrictId;
    }

    public Map<Integer, String> getPermenantAddressDsDivisionList() {
        return permenantAddressDsDivisionList;
    }

    public void setPermenantAddressDsDivisionList(Map<Integer, String> permenantAddressDsDivisionList) {
        this.permenantAddressDsDivisionList = permenantAddressDsDivisionList;
    }

    public void setDeathPersonPermenentAddressDistrictId(int deathPersonPermenentAddressDistrictId) {
        this.deathPersonPermenentAddressDistrictId = deathPersonPermenentAddressDistrictId;
        //setting death with this value for death permanent address
        populateDistrictAndDSOfPermanentAddress();
    }

    private void populateDistrictAndDSOfPermanentAddress() {
        if (deathPerson != null && deathPersonPermenentAddressDistrictId != 0 && deathPersonPermenentAddressDSDivisionId != 0) {
            deathPerson.setDsDivisionOfPermanentAddress(dsDivisionDAO.getDSDivisionByPK(deathPersonPermenentAddressDSDivisionId));
        }
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
