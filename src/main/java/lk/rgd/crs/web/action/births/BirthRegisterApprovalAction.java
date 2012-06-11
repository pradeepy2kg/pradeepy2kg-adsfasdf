package lk.rgd.crs.web.action.births;

import com.opensymphony.xwork2.ActionSupport;
import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.web.WebConstants;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * @author Indunil Moremada
 *         struts action class which is used for the BirthDeclaration approval purposes
 */

public class BirthRegisterApprovalAction extends ActionSupport implements SessionAware {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterApprovalAction.class);


    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final AppParametersDAO appParametersDAO;
    private final BirthRegistrationService service;
    private static final String BR_APPROVAL_ROWS_PER_PAGE = "crs.br_approval_rows_per_page";

    private Map session;

    private Map<Integer, String> bdDivisionList;
    private Map<Integer, String> districtList;
    private Map<Integer, String> dsDivisionList;
    private List<UserWarning> warnings;
    private List<BirthDeclaration> approvalPendingList;

    private int birthDistrictId;
    private int birthDivisionId;
    private int dsDivisionId;
    private long[] index;
    private long bdId;
    private long bdfSerialNo;
    private int pageNo;
    private int noOfRows;
    private int recordCounter;
    private String comments;
    private String language;
    private BirthDeclaration bdf;
    private User user;

    private Date searchStartDate;
    private Date searchEndDate;
    // extra startDate, endDate fields used to get date when passing through a url
    private String startDate;
    private String endDate;

    private boolean nextFlag;
    private boolean previousFlag;
    private boolean ignoreWarning;
    private boolean directApprovalFlag;
    private boolean allowEditBDF;
    private boolean allowApproveBDF;
    private boolean allowApproveBDFConfirmation;
    private boolean reject;
    //decides request is from birth confirmation approval if confirmationApprovalFlag is set to true
    private boolean confirmationApprovalFlag;
    private boolean searchDateRangeFlag;
    private BirthDeclaration.BirthType birthType;
    private boolean approved;
    private boolean approveBelated;

    public BirthRegisterApprovalAction(DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO,
        BDDivisionDAO bdDivisionDAO, AppParametersDAO appParametersDAO, BirthRegistrationService service) {
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.appParametersDAO = appParametersDAO;
        this.service = service;
    }

    /**
     * Responsible for loading the birth confirmation approval list
     *
     * @return
     */
    public String initBirthConfirmationApproval() {
        setConfirmationApprovalFlag(true);
        birthRegisterApproval();
        return SUCCESS;
    }

    /**
     * Responsible for loading the birth declaration approval list
     *
     * @return
     */
    public String initBirthDeclarationApproval() {
        birthRegisterApproval();
        return SUCCESS;
    }

    /**
     * if confirmationFlag is set to true gets the BirthDeclarations which were confirmed by the parents else gets the
     * BirthDeclarations which are not confirmed yet by the parents, then set them in the approvalPendingList to get
     * later in the jsp. page navigation can be done only if previousFlag is set to 1 or nextFlag is set to 1 initial
     * data are loaded based on the first district of the allowed district and the first division of the allowed division
     */
    private void birthRegisterApproval() {
        initPermission(approveBelated);
        populate();
        dsDivisionList = dsDivisionDAO.getDSDivisionNames(birthDistrictId, language, user);
        if (!dsDivisionList.isEmpty()) {
            dsDivisionId = dsDivisionList.keySet().iterator().next();
        }
        bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        logger.debug("inside birthRegisterApproval() : dsDivisionId {} initialized ", dsDivisionId);
        /**
         * initially pageNo is set to 1
         */
        setPageNo(1);
        noOfRows = appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE);
        if (confirmationApprovalFlag) {
            approvalPendingList = service.getConfirmationApprovalPendingByDSDivision(
                dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, user);
        } else {
            if (approveBelated) {
                approvalPendingList = service.getBelatedDeclarationApprovalPendingByDSDivision(
                    dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, user);
            } else {
                approvalPendingList = service.getDeclarationApprovalPendingByDSDivision(
                    dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, user);
            }
        }
        if (approvalPendingList.size() == 0) {
            addActionMessage(getText("noItemMsg.label"));
        }
        paginationHandler(approvalPendingList.size());
        setPreviousFlag(false);
    }

    /**
     * handles the approval, edit, delete and reject permission of the user
     */
    private void initPermission(boolean belatedApprove) {
        allowEditBDF = user.isAuthorized(Permission.EDIT_BDF);
        if (confirmationApprovalFlag) {
            setAllowApproveBDFConfirmation(user.isAuthorized(Permission.APPROVE_BDF_CONFIRMATION));
            logger.debug("permissions for Birth Confirmation Approval were populated : edit confirmation {} , Approve/Reject Confirmation {} , "
                , allowEditBDF, allowApproveBDFConfirmation);
        } else {
            if (belatedApprove) {
                allowApproveBDF = user.isAuthorized(Permission.APPROVE_BDF_BELATED);
            } else {
                allowApproveBDF = user.isAuthorized(Permission.APPROVE_BDF);
            }
            logger.debug("permissions for Birth Registration Approval were populated : edit Registration {} , Approve/Reject Registration {} , "
                , allowEditBDF, allowApproveBDF);
        }
    }

    /**
     * filters BirthDeclarations which are not yet approved, according to user selected district and division
     *
     * @return String
     */
    public String filter() {
        //TODO: this method has to be refactored
        logger.debug("confirmation flag : {}", confirmationApprovalFlag);
        //todo
        setPageNo(1);
        initPermission(approveBelated);
        if (logger.isDebugEnabled()) {
            logger.debug("inside filter() : birthDistrictId {} and birthDivisionId {}  ", birthDistrictId, birthDivisionId + " bdfSerialNumber " +
                bdfSerialNo + " selected, requested from page " + pageNo);
        }

        noOfRows = appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE);
        logger.debug("Rows per page {}", noOfRows);
        approvalPendingList = new ArrayList<BirthDeclaration>();
        if (confirmationApprovalFlag) {
            //TODO: search birth confirmation by serial number
            if (bdId > 0) {
                try {
                    bdf = service.getById(bdId, user);
                }
                catch (Exception e) {
                    logger.error("inside filter() : {} ", e);
                }
                if (bdf != null && bdf.getRegister().getStatus() == BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED) {
                    // display only a confirmation changes captured BirthDeclaration
                    approvalPendingList.add(bdf);
                }
            } else if (searchStartDate != null && searchEndDate != null && bdfSerialNo == 0) {
                // searching according to selected date range in BDF Confirmation approval page
                logger.debug("initializing birth confirmation filter based on date range startDate : {} endDate : {}", startDate, endDate);
                searchDateRangeFlag = true;
                if (birthDivisionId != 0) {
                    approvalPendingList = service.getByBDDivisionStatusAndConfirmationReceiveDateRange(
                        bdDivisionDAO.getBDDivisionByPK(birthDivisionId), searchStartDate, searchEndDate, pageNo, noOfRows, user);
                } else {
                    approvalPendingList = service.getByDSDivisionStatusAndConfirmationReceiveDateRange(
                        dsDivisionDAO.getDSDivisionByPK(dsDivisionId), searchStartDate, searchEndDate, pageNo, noOfRows, user);
                }
            } else {
                loadConfirmationApprovalPending();
            }
        } else {
            if (bdfSerialNo > 0) {
                if (birthDivisionId == 0) {
                    try {
                        bdf = service.getActiveRecordBySerialNo(bdfSerialNo, user, BirthDeclaration.State.DATA_ENTRY);
                    } catch (Exception e) {
                        logger.error("Error in finding Birth Decleration by Serial Number");
                        logger.error(e.getMessage());
                    }
                } else {
                    try {
                        //TODO: filter by status - DATA_ENTRY
                        bdf = service.getActiveRecordByBDDivisionAndSerialNo(
                            bdDivisionDAO.getBDDivisionByPK(birthDivisionId), bdfSerialNo, user);
                    }
                    catch (Exception e) {
                        logger.error("inside filter() : {} ", e);
                    }
                }
                if (bdf != null) {
                    if (bdf.getRegister().getStatus() == BirthDeclaration.State.DATA_ENTRY) {
                        approvalPendingList.add(bdf);
                    }
                }
            } else if (searchStartDate != null && searchEndDate != null && bdfSerialNo == 0) {
                // searching according to selected date range in BDF approval page
                logger.debug("initializing birth declaration filter based on date range startDate : {} endDate : {}", startDate, endDate);
                searchDateRangeFlag = true;
                loadDeclarationApprovalPendingByDivisionAndDateRange();
            } else {
                loadDeclarationApprovalPendingList();
            }
        }

        if (approvalPendingList.size() == 0) {
            addActionMessage(getText("noItemMsg.label"));
        }
        paginationHandler(approvalPendingList.size());
        setRecordCounter(0);
        populate();
        //todo
        return SUCCESS;
    }

    /**
     * approve selected pending BirthDeclaration if there are any warnings redirected to warning displaying page
     *
     * @return String
     */
    public String approve() {
        initPermission(approveBelated);
        bdf = service.getById(bdId, user);
        birthType = bdf.getRegister().getBirthType();
        boolean caughtException = false;
        try {
            if (confirmationApprovalFlag) {
                warnings = service.approveConfirmationChanges(bdf, false, user);
                logger.debug("inside approve() : requested to approve birth confirmation bdId {} ", bdId);
            } else {
                if (birthType == BirthDeclaration.BirthType.LIVE) {
                    warnings = service.approveLiveBirthDeclaration(bdf.getIdUKey(), false, user);
                    logger.debug("inside approve() : requested to approve live birth declaration bdId {} ", bdId);
                } else if (birthType == BirthDeclaration.BirthType.STILL) {
                    warnings = service.approveStillBirthDeclaration(bdf.getIdUKey(), false, user);
                    logger.debug("inside approve() : requested to approve still birth declaration bdId {} ", bdId);
                } else if (birthType == BirthDeclaration.BirthType.ADOPTION) {
                    warnings = service.approveAdoptionBirthDeclaration(bdf.getIdUKey(), false, user);
                    logger.debug("inside approve() : requested to approve adoption birth declaration bdId {} ", bdId);
                } else if (birthType == BirthDeclaration.BirthType.BELATED) {
                    warnings = service.approveBelatedBirthDeclaration(bdf.getIdUKey(), false, user);
                    logger.debug("inside approve() : requested to approve belated birth declaration bdId {} ", bdId);
                }
            }
            addActionMessage(getText("message.approval.Success"));
        }
        catch (CRSRuntimeException e) {
            logger.error("inside approve BirthRegistration/BirthConfirmation : {} ", e);
            addActionError(getText("brapproval.approval.error." + e.getErrorCode()));
            addActionMessage(getText("message.approval.Rejected"));
            caughtException = true;
        }

        noOfRows = appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE);
        if (caughtException || (warnings != null && warnings.isEmpty())) {
            if (confirmationApprovalFlag) {
                loadConfirmationApprovalPending();
            } else {
                loadDeclarationApprovalPendingList();
            }
            if (approvalPendingList.size() > 0) {
                paginationHandler(approvalPendingList.size());
            } else {
                setPageNo(getPageNo() - 1);
            }

            logger.debug("remaining approvals : {} ", approvalPendingList.size());
            populate();
            logger.debug("returning success");
            return SUCCESS;
        } else {
            return "approvalRejected";
        }
    }

    /**
     * responsible for approve BDF immediately after filling the form. if confirmationApprovalFlag is set to true it is
     * for Birth Confirmation changes direct approval purposes.
     *
     * @return
     */
    public String directApprove() {
        bdf = service.getById(bdId, user);
        logger.debug("Ds division : {} ", bdf.getRegister().getBirthDivision().getDsDivision().getDivisionId());
        birthType = bdf.getRegister().getBirthType();
        boolean caughtException = false;
        try {
            if (confirmationApprovalFlag) {
                warnings = service.approveConfirmationChanges(bdf, false, user);
            } else {
                if (birthType == BirthDeclaration.BirthType.LIVE) {
                    warnings = service.approveLiveBirthDeclaration(bdf.getIdUKey(), false, user);
                    logger.debug("inside directApprove() : direct approve live birth declaration with bdId : {} ", bdId);
                } else if (birthType == BirthDeclaration.BirthType.STILL) {
                    warnings = service.approveStillBirthDeclaration(bdf.getIdUKey(), false, user);
                    logger.debug("inside directApprove() : direct approve still birth declaration with bdId : {} ", bdId);
                } else if (birthType == BirthDeclaration.BirthType.ADOPTION) {
                    warnings = service.approveAdoptionBirthDeclaration(bdf.getIdUKey(), false, user);
                    logger.debug("inside directApprove() : direct approve adoption birth declaration with bdId : {} ", bdId);
                } else if (birthType == BirthDeclaration.BirthType.BELATED) {
                    warnings = service.approveBelatedBirthDeclaration(bdf.getIdUKey(), false, user);
                    logger.debug("inside directApprove() : direct approve belated birth declaration with bdId : {} ", bdId);
                }
            }
        }
        catch (CRSRuntimeException e) {
            logger.error("inside directApprove() error handle : {} ", e.getMessage());
            addActionError(getText("brapproval.approval.error." + Integer.toString(e.getErrorCode())));
            caughtException = true;
        }
        initPermission(approveBelated);
        if (!caughtException && (warnings != null && warnings.isEmpty())) {
            approved = true;
            if (confirmationApprovalFlag) {
                allowApproveBDF = user.isAuthorized(Permission.APPROVE_BDF_CONFIRMATION);
                addActionMessage((getText("confirmationApprovedSuccess.label")));
            } else {
                birthType = bdf.getRegister().getBirthType();
                setAllowApproveBDF(user.isAuthorized(Permission.APPROVE_BDF));
                addActionMessage((getText("approveSuccess.label")));
            }
        }
        return SUCCESS;
    }

    public String approveIgnoringWarning() {
        if (logger.isDebugEnabled()) {
            logger.debug("inside approveIgnoringWarning() - bdId : " + bdId +
                " , requested isBirthConfirmationConfirmationApproval : " + confirmationApprovalFlag +
                " , IgnoreWarnings " + ignoreWarning);
        }
        initPermission(approveBelated);
        //direct birth approvalIgnoring warnings from birthDeclarationFormDetails page
        if (!ignoreWarning && directApprovalFlag) {
            addActionError(getText("directApproveIgnoreWarning.faild.label"));
            return SUCCESS;
        }
        if (ignoreWarning) {
            bdf = service.getById(bdId, user);
            birthType = bdf.getRegister().getBirthType();
            try {
                if (confirmationApprovalFlag) {
                    service.approveConfirmationChanges(bdf, true, user);
                    if (directApprovalFlag) {
                        logger.debug("inside approveIgnoringWarning() : directApprovalFlag {}", directApprovalFlag);
                        addActionMessage((getText("confirmationApprovedSuccess.label")));
                        setAllowApproveBDF(user.isAuthorized(Permission.APPROVE_BDF_CONFIRMATION));
                        approved = true;
                        return SUCCESS;
                    }
                } else {
                    if (birthType == BirthDeclaration.BirthType.LIVE) {
                        service.approveLiveBirthDeclaration(bdf.getIdUKey(), true, user);
                    } else if (birthType == BirthDeclaration.BirthType.STILL) {
                        service.approveStillBirthDeclaration(bdf.getIdUKey(), true, user);
                    } else if (birthType == BirthDeclaration.BirthType.ADOPTION) {
                        service.approveAdoptionBirthDeclaration(bdf.getIdUKey(), true, user);
                    } else if (birthType == BirthDeclaration.BirthType.BELATED) {
                        service.approveBelatedBirthDeclaration(bdf.getIdUKey(), true, user);
                    }
                    //checks whether the request is from immediately after entering a birth declaration
                    if (directApprovalFlag) {
                        logger.debug("inside approveIgnoringWarning() : directApprovalFlag {}", directApprovalFlag);
                        addActionMessage((getText("approveSuccess.label")));
                        setAllowApproveBDF(user.isAuthorized(Permission.APPROVE_BDF));
                        approved = true;
                        birthType = bdf.getRegister().getBirthType();
                        return SUCCESS;
                    }
                }
                addActionMessage((getText("message.approval.Success")));
            } catch (CRSRuntimeException e) {
                logger.debug("inside approveIgnoringWarning() Error Code : {} ", e.getErrorCode());
                addActionError(getText("brapproval.ignoreWarningApproval.error." + e.getErrorCode()));
            }
        }
        noOfRows = appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE);
        if (confirmationApprovalFlag) {
            loadConfirmationApprovalPending();

        } else {
            loadDeclarationApprovalPendingList();
        }
        paginationHandler(approvalPendingList.size());
        populate();
        return SUCCESS;
    }

    /**
     * @return String which decides the next page
     */
    public String approveListOfEntries() {
        initPermission(approveBelated);
        if (index != null) {
            logger.debug("inside approveListOfEntries() : {} records are requested to approve isBirthConfirmationApproval {}",
                index.length, confirmationApprovalFlag);
            try {
                if (confirmationApprovalFlag) {
                    warnings = service.approveConfirmationChangesForIDList(index, user);
                } else {
                    warnings = service.approveBirthDeclarationIdList(index, user);
                }
                if (warnings != null && warnings.size() > 0) {
                    if (index.length == 1) {
                        addActionMessage(getText("message.approval.faild"));
                    } else {
                        addActionMessage(getText("message.approval.Success.with.some.errors"));
                    }
                } else {
                    addActionMessage(getText("message.approval.Success"));
                }
            }
            catch (CRSRuntimeException e) {
                logger.error("inside approveListOfEntries : {} ", e);
                addActionError(getText("brapproval.approval.error." + e.getErrorCode()));
            }
        } else {
            addActionMessage(getText("messege.noSelected.items"));
        }
        populate();
        logger.debug("district list : {}", districtList.size());
        noOfRows = appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE);
        if (confirmationApprovalFlag) {
            loadConfirmationApprovalPending();
        } else {
            if (birthDivisionId != 0) {
                approvalPendingList = service.getDeclarationApprovalPending(bdDivisionDAO.getBDDivisionByPK(birthDivisionId),
                    pageNo, noOfRows, user);
            } else {
                approvalPendingList = service.getDeclarationApprovalPendingByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId),
                    pageNo, noOfRows, user);
            }
        }
        paginationHandler(approvalPendingList.size());
        return SUCCESS;
    }

    /**
     * reject a BirthDeclaration based on received bdId
     *
     * @return String
     */
    public String reject() {
        bdf = service.getById(bdId, user);
        if (reject) {
            return "rejectGetComments";
        } else {
            logger.debug("inside reject() : bdId {} requested to reject, is birthConfirmationReject {} ", bdId, confirmationApprovalFlag);
            try {
                service.rejectBirthDeclaration(bdf, comments, user);

            } catch (CRSRuntimeException e) {
                logger.debug("failed to reject birth declaration/confirmation : Error Code is {}", e.getErrorCode());

                switch (e.getErrorCode()) {
                    case ErrorCodes.PERMISSION_DENIED:
                    case ErrorCodes.INVALID_STATE_FOR_BDF_REJECTION:
                        addActionError(getText("message.noPermission"));
                        return "rejectGetComments";
                    case ErrorCodes.COMMENT_REQUIRED_BDF_REJECT:
                        addActionError(getText("brapproval.reject.commentRequired"));
                        return "rejectGetComments";
                    default:
                        logger.error("Unhandled Exception : {}", e);
                }
            }
            initPermission(approveBelated);
            populate();
            noOfRows = appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE);
            if (confirmationApprovalFlag) {
                loadConfirmationApprovalPending();
                paginationHandler(approvalPendingList.size());
                addActionMessage(getText("message.success.ConfirmationReject"));
                return "successConfirmationReject";
            } else {
                loadDeclarationApprovalPendingList();
                paginationHandler(approvalPendingList.size());
                addActionMessage(getText("message.success.DeclarationReject"));
                return "successDeclarationReject";
            }
        }
    }

    /**
     * request to delete the selected BirthDeclaration from the database
     *
     * @return String
     */
    public String delete() {
        logger.debug("inside delete() : bdId {} requested to delete", bdId);
        initPermission(approveBelated);
        bdf = service.getById(bdId, user);
        birthType = bdf.getRegister().getBirthType();
        try {
            if (birthType == BirthDeclaration.BirthType.LIVE) {
                service.deleteLiveBirthDeclaration(bdf, false, user);
            } else if (birthType == BirthDeclaration.BirthType.STILL) {
                service.deleteStillBirthDeclaration(bdf, false, user);
            } else if (birthType == BirthDeclaration.BirthType.ADOPTION) {
                service.deleteAdoptionBirthDeclaration(bdf, false, user);
            } else if (birthType == BirthDeclaration.BirthType.BELATED) {
                service.deleteBelatedBirthDeclaration(bdf, false, user);
            }

        } catch (CRSRuntimeException e) {
            logger.debug("failed to delete birth declaration/confirmation : Error Code is {}", e.getErrorCode());
            switch (e.getErrorCode()) {
                case ErrorCodes.PERMISSION_DENIED:
                case ErrorCodes.ILLEGAL_STATE:
                    addActionError(getText("brapproval.delete.error." + e.getErrorCode()));
                    break;
                default:
                    logger.error("Unhandled Exception : {}", e);
            }
        }
        noOfRows = appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE);

        loadDeclarationApprovalPendingList();

        paginationHandler(approvalPendingList.size());
        populate();
        addActionMessage(getText("message.success.Declaration.delete"));
        return SUCCESS;
    }

    /**
     * Populate master data to the UIs
     */
    private void populate() {
        language = ((Locale) session.get(WebConstants.SESSION_USER_LANG)).getLanguage();
        logger.debug("inside populate() : language {} observed ", language);
        setDistrictList(districtDAO.getDistrictNames(language, user));
        //TODO checking district loading
        setBirthDistrictId(birthDistrictId);
        //dsDivisions
        this.dsDivisionList = dsDivisionDAO.getDSDivisionNames(birthDistrictId, language, user);
        //setting bdDivisions
        /*     if (!dsDivisionList.isEmpty()) {
            dsDivisionId = dsDivisionList.keySet().iterator().next();
            bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        }*/
        if (dsDivisionId != 0) {
            bdDivisionList = bdDivisionDAO.getBDDivisionNames(dsDivisionId, language, user);
        } else {
            bdDivisionList = Collections.emptyMap();
        }
        if (pageNo == 0) {
            setInitialDistrict();
        }
    }

    /**
     * handles pagination of BirthDeclarations which are to be displayed in jsp
     *
     * @return String
     */
    public String nextPage() {
        initPermission(approveBelated);
        if (logger.isDebugEnabled()) {
            logger.debug("inside nextPage() : current birthDistrictId {}, birthDivisionId {}", birthDistrictId, birthDivisionId + " requested from pageNo " + pageNo);
        }
        setPageNo(getPageNo() + 1);

        noOfRows = appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE);
        /**
         * gets the user selected district to get the records
         * variable nextFlag is used to handle the pagination link
         * in the jsp page
         */
        if (confirmationApprovalFlag) {
            if (searchDateRangeFlag) {
                searchStartDate = DateTimeUtils.getDateFromISO8601String(startDate);
                searchEndDate = DateTimeUtils.getDateFromISO8601String(endDate);
                if (birthDivisionId != 0) {
                    approvalPendingList = service.getByBDDivisionStatusAndConfirmationReceiveDateRange(
                        bdDivisionDAO.getBDDivisionByPK(birthDivisionId), searchStartDate, searchEndDate, pageNo, noOfRows, user);
                } else {
                    approvalPendingList = service.getByDSDivisionStatusAndConfirmationReceiveDateRange(
                        dsDivisionDAO.getDSDivisionByPK(dsDivisionId), searchStartDate, searchEndDate, pageNo, noOfRows, user);
                }
            } else {
                loadConfirmationApprovalPending();
            }
        } else {
            if (searchDateRangeFlag) {
                searchStartDate = DateTimeUtils.getDateFromISO8601String(startDate);
                searchEndDate = DateTimeUtils.getDateFromISO8601String(endDate);
                loadDeclarationApprovalPendingByDivisionAndDateRange();

            } else {
                loadDeclarationApprovalPendingList();
            }
        }
        paginationHandler(approvalPendingList.size());
        /**
         * when page refreshes BirthDeclaration approval pending list
         * does not get any value pageNo is not incremented
         */
        setPreviousFlag(true);
        setRecordCounter(getRecordCounter() + noOfRows);
        populate();
        return SUCCESS;
    }

    /**
     * handles pagination of BirthDeclaration approval pending data
     *
     * @return String
     */
    public String previousPage() {
        initPermission(approveBelated);
        if (logger.isDebugEnabled()) {
            logger.debug("inside previousPage() : current birthDistrictId {}, birthDivisionId {} ",
                birthDistrictId, birthDivisionId + " requested from pageNo " + pageNo);
        }
        /**
         * UI related. decides whether to display next and previous links
         */
        if (previousFlag && getPageNo() == 2) {
            /**
             * request is coming backward(calls previous to load the very first page
             */
            setPreviousFlag(false);
        } else if (getPageNo() == 1) {
            /**
             * if request is from page one in the next page previous link should be displayed
             */
            setPreviousFlag(false);
        } else {
            setPreviousFlag(true);
        }
        setNextFlag(true);
        if (getPageNo() > 1) {
            setPageNo(getPageNo() - 1);
        }
        noOfRows = appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE);
        if (confirmationApprovalFlag) {
            if (searchDateRangeFlag) {
                searchStartDate = DateTimeUtils.getDateFromISO8601String(startDate);
                searchEndDate = DateTimeUtils.getDateFromISO8601String(endDate);
                if (birthDivisionId != 0) {
                    approvalPendingList = service.getByBDDivisionStatusAndConfirmationReceiveDateRange(
                        bdDivisionDAO.getBDDivisionByPK(birthDivisionId), searchStartDate, searchEndDate, pageNo, noOfRows, user);
                } else {
                    approvalPendingList = service.getByDSDivisionStatusAndConfirmationReceiveDateRange(
                        dsDivisionDAO.getDSDivisionByPK(dsDivisionId), searchStartDate, searchEndDate, pageNo, noOfRows, user);
                }
            } else {
                loadConfirmationApprovalPending();
            }
        } else {
            if (searchDateRangeFlag) {
                searchStartDate = DateTimeUtils.getDateFromISO8601String(startDate);
                searchEndDate = DateTimeUtils.getDateFromISO8601String(endDate);
                loadDeclarationApprovalPendingByDivisionAndDateRange();

            } else {
                loadDeclarationApprovalPendingList();
            }
        }
        if (getRecordCounter() > 0) {
            setRecordCounter(getRecordCounter() - noOfRows);
        }
        populate();
        return SUCCESS;
    }

    private void loadDeclarationApprovalPendingList() {
        if (approveBelated) {
            if (birthDivisionId != 0) {
                approvalPendingList = service.getBelatedDeclarationApprovalPending(
                    bdDivisionDAO.getBDDivisionByPK(birthDivisionId), pageNo, noOfRows, user);
            } else {
                approvalPendingList = service.getBelatedDeclarationApprovalPendingByDSDivision(
                    dsDivisionDAO.getDSDivisionByPK(dsDivisionId), pageNo, noOfRows, user);
            }
        } else {
            if (dsDivisionId == 0) {
                //search by DS
                approvalPendingList = service.getDeclarationApprovalPendingByDistrictId(
                    districtDAO.getDistrict(birthDistrictId), pageNo, noOfRows, user);
            } else if (birthDivisionId != 0) {
                approvalPendingList = service.getDeclarationApprovalPending(bdDivisionDAO.getBDDivisionByPK(birthDivisionId),
                    pageNo, noOfRows, user);
            } else {
                approvalPendingList = service.getDeclarationApprovalPendingByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId),
                    pageNo, noOfRows, user);
            }
        }
    }

    private void loadDeclarationApprovalPendingByDivisionAndDateRange() {
        if (approveBelated) {
            if (birthDivisionId != 0) {
                approvalPendingList = service.getBelatedDeclarationPendingByBDDivisionAndRegisterDateRange(
                    bdDivisionDAO.getBDDivisionByPK(birthDivisionId), searchStartDate, searchEndDate, pageNo, noOfRows, user);
            } else {
                approvalPendingList = service.getBelatedDeclarationPendingByDSDivisionAndRegisterDateRange(
                    dsDivisionDAO.getDSDivisionByPK(dsDivisionId), searchStartDate, searchEndDate, pageNo, noOfRows, user);
            }
        } else {
            if (birthDivisionId != 0) {
                approvalPendingList = service.getDeclarationPendingByBDDivisionAndRegisterDateRange(
                    bdDivisionDAO.getBDDivisionByPK(birthDivisionId), searchStartDate, searchEndDate, pageNo, noOfRows, user);
            } else {
                approvalPendingList = service.getDeclarationPendingByDSDivisionAndRegisterDateRange(dsDivisionDAO.getDSDivisionByPK(dsDivisionId),
                    searchStartDate, searchEndDate, pageNo, noOfRows, user);
            }
        }
    }

    private void loadConfirmationApprovalPending() {
        if (birthDivisionId != 0) {
            approvalPendingList = service.getConfirmationApprovalPending(bdDivisionDAO.getBDDivisionByPK(birthDivisionId),
                pageNo, noOfRows, user);
        } else {
            approvalPendingList = service.getConfirmationApprovalPendingByDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivisionId),
                pageNo, noOfRows, user);
        }
    }

    /**
     * responsible whether to display the next link in the jsp or not and handles the page number
     *
     * @param recordsFound no of birth register approval pending records found
     */
    public void paginationHandler(int recordsFound) {
        if (recordsFound == appParametersDAO.getIntParameter(BR_APPROVAL_ROWS_PER_PAGE)) {
            setNextFlag(true);
        } else {
            setNextFlag(false);
        }
    }

    /**
     * initial district is set to the first district of the allowed district list of a particular user
     */
    public void setInitialDistrict() {
        if (!getDistrictList().isEmpty()) {
            setBirthDistrictId(getDistrictList().keySet().iterator().next());
        }
        logger.debug("inside setInitialDistrict() : birthDistrictId {} selected", birthDistrictId);
    }

    public void setApprovalPendingList(List<BirthDeclaration> approvalPendingList) {
        this.approvalPendingList = approvalPendingList;
    }

    public List<BirthDeclaration> getApprovalPendingList() {
        return approvalPendingList;
    }

    public void setSession(Map map) {
        this.session = map;
        user = (User) session.get(WebConstants.SESSION_USER_BEAN);
    }

    public Map<Integer, String> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(Map<Integer, String> districtList) {
        this.districtList = districtList;
    }

    public Map<Integer, String> getBdDivisionList() {
        return bdDivisionList;
    }

    public void setBdDivisionList(Map<Integer, String> bdDivisionList) {
        this.bdDivisionList = bdDivisionList;
    }

    public int getBirthDivisionId() {
        return birthDivisionId;
    }

    public void setBirthDivisionId(int birthDivisionId) {
        this.birthDivisionId = birthDivisionId;
    }

    public int getBirthDistrictId() {
        return birthDistrictId;
    }

    public void setBirthDistrictId(int birthDistrictId) {
        this.birthDistrictId = birthDistrictId;
    }

    public long[] getIndex() {
        return index;
    }

    public void setIndex(long[] index) {
        this.index = index;
    }

    public long getBdId() {
        return bdId;
    }

    public void setBdId(long bdId) {
        this.bdId = bdId;
    }

    public boolean isIgnoreWarning() {
        return ignoreWarning;
    }

    public void setIgnoreWarning(boolean ignoreWarning) {
        this.ignoreWarning = ignoreWarning;
    }

    public boolean isNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(boolean nextFlag) {
        this.nextFlag = nextFlag;
    }

    public boolean isPreviousFlag() {
        return previousFlag;
    }

    public void setPreviousFlag(boolean previousFlag) {
        this.previousFlag = previousFlag;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public boolean isAllowEditBDF() {
        return allowEditBDF;
    }

    public void setAllowEditBDF(boolean allowEditBDF) {
        this.allowEditBDF = allowEditBDF;
    }

    public boolean isAllowApproveBDF() {
        return allowApproveBDF;
    }

    public void setAllowApproveBDF(boolean allowApproveBDF) {
        this.allowApproveBDF = allowApproveBDF;
    }

    public List<UserWarning> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<UserWarning> warnings) {
        this.warnings = warnings;
    }

    public int getRecordCounter() {
        return recordCounter;
    }

    public void setRecordCounter(int recordCounter) {
        this.recordCounter = recordCounter;
    }

    public boolean getReject() {
        return reject;
    }

    public void setReject(boolean reject) {
        this.reject = reject;
    }

    public BirthDeclaration getBdf() {
        return bdf;
    }

    public void setBdf(BirthDeclaration bdf) {
        this.bdf = bdf;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public long getBdfSerialNo() {
        return bdfSerialNo;
    }

    public void setBdfSerialNo(long bdfSerialNo) {
        this.bdfSerialNo = bdfSerialNo;
    }

    public boolean isConfirmationApprovalFlag() {
        return confirmationApprovalFlag;
    }

    public void setConfirmationApprovalFlag(boolean confirmationApprovalFlag) {
        this.confirmationApprovalFlag = confirmationApprovalFlag;
    }

    public boolean isAllowApproveBDFConfirmation() {
        return allowApproveBDFConfirmation;
    }

    public void setAllowApproveBDFConfirmation(boolean allowApproveBDFConfirmation) {
        this.allowApproveBDFConfirmation = allowApproveBDFConfirmation;
    }

    public Date getSearchStartDate() {
        return searchStartDate;
    }

    public void setSearchStartDate(Date searchStartDate) {
        this.searchStartDate = searchStartDate;
        startDate = DateTimeUtils.getISO8601FormattedString(searchStartDate);
    }

    public Date getSearchEndDate() {
        return searchEndDate;
    }

    public void setSearchEndDate(Date searchEndDate) {
        this.searchEndDate = searchEndDate;
        endDate = DateTimeUtils.getISO8601FormattedString(searchEndDate);
    }

    public boolean isSearchDateRangeFlag() {
        return searchDateRangeFlag;
    }

    public void setSearchDateRangeFlag(boolean searchDateRangeFlag) {
        this.searchDateRangeFlag = searchDateRangeFlag;
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

    public Map<Integer, String> getDsDivisionList() {
        return dsDivisionList;
    }

    public void setDsDivisionList(Map<Integer, String> dsDivisionList) {
        this.dsDivisionList = dsDivisionList;
    }

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
    }

    public boolean isDirectApprovalFlag() {
        return directApprovalFlag;
    }

    public void setDirectApprovalFlag(boolean directApprovalFlag) {
        this.directApprovalFlag = directApprovalFlag;
    }

    public BirthDeclaration.BirthType getBirthType() {
        return birthType;
    }

    public void setBirthType(BirthDeclaration.BirthType birthType) {
        this.birthType = birthType;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public BirthRegistrationService getService() {
        return this.service;
    }

    public Map getSession() {
        return session;
    }

    public boolean isApproveBelated() {
        return approveBelated;
    }

    public void setApproveBelated(boolean approveBelated) {
        this.approveBelated = approveBelated;
    }
}