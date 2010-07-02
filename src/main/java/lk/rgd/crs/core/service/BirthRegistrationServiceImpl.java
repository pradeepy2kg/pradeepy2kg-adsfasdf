package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The central service managing the CRS Birth Registration process
 */
public class BirthRegistrationServiceImpl implements BirthRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegistrationServiceImpl.class);
    private static final DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    private final BirthDeclarationDAO birthDeclarationDAO;
    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final CountryDAO countryDAO;
    private final RaceDAO raceDAO;

    public BirthRegistrationServiceImpl(
        BirthDeclarationDAO birthDeclarationDAO, DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO,
        BDDivisionDAO bdDivisionDAO, CountryDAO countryDAO, RaceDAO raceDAO) {
        this.birthDeclarationDAO = birthDeclarationDAO;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.countryDAO = countryDAO;
        this.raceDAO = raceDAO;
    }

    /**
     * @inheritDoc
     */
    public List<UserWarning> addLiveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user,
        String caseFileNumber, String additionalDocumentsComment) {

        logger.debug("Adding a new live birth declaration");
        // does the user have access to the BDF being added (i.e. check district and DS division)
        // TODO if a mother is specified, is she alive? etc
        // TODO add case file number and additional document list as comments
        if (!ignoreWarnings) {
            // TODO more validations .. like bdf.getParent().getMotherFullName() != null etc
        }
        validateAccessOfUser(user, bdf);
        bdf.getRegister().setStatus(BirthDeclaration.State.DATA_ENTRY);
        birthDeclarationDAO.addBirthDeclaration(bdf);
        logger.debug("Added a new live birth declaration. IDUKey : {}", bdf.getIdUKey());
        return null;
    }

    /**
     * @inheritDoc
     */
    public List<UserWarning> approveLiveBirthDeclarationIdList(long[] approvalDataList, User user) {

        logger.debug("Request for approval of BDFs with IDUKeys : {}", approvalDataList);

        if (!user.isAuthorized(Permission.APPROVE_BDF)) {
            handleException("The user : " + user.getUserId() +
                " is not authorized to approve birth declarations", ErrorCodes.PERMISSION_DENIED);
        }

        List<UserWarning> warnings = new ArrayList<UserWarning>();
        for (long id : approvalDataList) {
            BirthDeclaration bdf = birthDeclarationDAO.getById(id);
            List<UserWarning> w = approveLiveBirthDeclaration(bdf, false, user);
            if (!w.isEmpty()) {
                warnings.add(new UserWarning("Birth Declaration ID : " + id +
                    " must be approved after validating warnings"));
            }
        }

        if (logger.isDebugEnabled()) {
            if (warnings.isEmpty()) {
                logger.debug("Approved BDFs with IDUKeys : {}", approvalDataList);
            } else {
                logger.debug("Approval of BDFs with IDUKeys : {} encountered warnings for some", approvalDataList);
            }
        }
        return warnings;
    }

    /**
     * @inheritDoc
     */
    public void editLiveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        logger.debug("Attempt to edit live birth declaration record : {}", bdf.getIdUKey());

        // does the user have access to the BDF being updated
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateAccessOfUser(user, existing);

        // TODO check validations as per addLiveBirthDeclaration

        // a BDF can be edited by a DEO or ADR only before being approved
        final BirthDeclaration.State currentState = existing.getRegister().getStatus();
        if (currentState == BirthDeclaration.State.DATA_ENTRY) {
            birthDeclarationDAO.updateBirthDeclaration(bdf);
            logger.debug("Saved edit changes to birth declaration record : {}  in data entry state", bdf.getIdUKey());

        } else {
            handleException("Cannot modify birth declaration : " + existing.getIdUKey() +
                " Illegal state : " + currentState, ErrorCodes.ILLEGAL_STATE);
        }
    }

    /**
     * @inheritDoc
     */
    public void deleteLiveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        logger.debug("Attempt to delete live birth declaration record : {}", bdf.getIdUKey());

        // does the user have access to the BDF being deleted
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateAccessOfUser(user, existing);

        // a BDF can be edited by a DEO or ADR only before being approved
        final BirthDeclaration.State currentState = existing.getRegister().getStatus();
        if (currentState == BirthDeclaration.State.DATA_ENTRY) {
            birthDeclarationDAO.deleteBirthDeclaration(bdf.getIdUKey());
            logger.debug("Deleted live birth declaration record : {} in data entry state", bdf.getIdUKey());

        } else {
            handleException("Cannot delete birth declaration " + existing.getIdUKey() +
                " Illegal state : " + currentState, ErrorCodes.ILLEGAL_STATE);
        }
    }

    /**
     * @inheritDoc
     */
    public List<UserWarning> approveLiveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        logger.debug("Attempt to approve live birth declaration : {} Ignore warnings : {}", bdf.getIdUKey(), ignoreWarnings);

        // load the existing record
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());

        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        validateAccessOfUser(user, existing);

        // check approve permission
        if (!user.isAuthorized(Permission.APPROVE_BDF)) {
            handleException("User : " + user.getUserId() + " is not allowed to approve/reject birth declarations",
                ErrorCodes.PERMISSION_DENIED);
        }

        // is the BDF currently existing in a state for approval
        final BirthDeclaration.State currentState = existing.getRegister().getStatus();
        if (BirthDeclaration.State.DATA_ENTRY != currentState) {
            handleException("Cannot approve confirmation : " + bdf.getIdUKey() + " Illegal state : " + currentState,
                ErrorCodes.INVALID_STATE_FOR_BDF_APPROVAL);
        }

        // validate if the minimum required fields are adequately filled
        BirthDeclarationValidator.validateMinimalRequirements(bdf);

        // validate standard validations anyway, since even if validations are rejected a note of it will be made
        // against the approval for audit requirements
        List<UserWarning> warnings = BirthDeclarationValidator.validateStandardRequirements(birthDeclarationDAO, bdf, user);

        if (!warnings.isEmpty() && ignoreWarnings) {
            StringBuilder sb = new StringBuilder();
            if (existing.getRegister().getComments() != null) {
                sb.append(existing.getRegister().getComments()).append("\n");
            }

            // SimpleDateFormat is not thread-safe
            synchronized (dfm) {
                sb.append(dfm.format(new Date())).append(" - Approved birth declaration ignoring warnings. User : ").
                    append(user.getUserId()).append("\n");
            }

            for (UserWarning w : warnings) {
                sb.append(w.getSeverity());
                sb.append("-");
                sb.append(w.getMessage());
            }
            bdf.getRegister().setComments(sb.toString());
        }

        if (warnings.isEmpty() || ignoreWarnings) {
            bdf.getRegister().setStatus(BirthDeclaration.State.APPROVED);
            birthDeclarationDAO.updateBirthDeclaration(bdf);
            logger.debug("Approved live birth declaration record : {} Ignore warnings : {}", bdf.getIdUKey(), ignoreWarnings);
        } else {
            logger.debug("Approval of live birth declaration record : {} stopped due to warnings", bdf.getIdUKey());
        }
        return warnings;
    }

    /**
     * @inheritDoc
     */
    public void markLiveBirthConfirmationAsPrinted(BirthDeclaration bdf, User user) {

        logger.debug("Attempt to mark confirmation printed for live birth declaration record : {}", bdf.getIdUKey());

        // load the existing record
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());

        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        validateAccessOfUser(user, existing);

        existing.getRegister().setStatus(BirthDeclaration.State.CONFIRMATION_PRINTED);
        birthDeclarationDAO.updateBirthDeclaration(existing);
        logger.debug("Marked confirmation printed for live birth declaration record : {}", bdf.getIdUKey());
    }

    /**
     * @inheritDoc
     */
    public void markLiveBirthConfirmationIDsAsPrinted(long[] printedIDList, User user) {
        logger.debug("Attempt to mark confirmations printed for records : {}", printedIDList);
        for (long l : printedIDList) {
            BirthDeclaration bdf = birthDeclarationDAO.getById(l);
            markLiveBirthConfirmationAsPrinted(bdf, user);
        }
    }

    /**
     * @inheritDoc
     */
    public void markLiveBirthDeclarationAsConfirmedWithoutChanges(BirthDeclaration bdf, User user) {

        logger.debug("Attempt to mark birth record : {} as confirmed without changes", bdf.getIdUKey());

        // does the user have access to the BDF being confirmed (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateAccessOfUser(user, existing);

        // to ensure correctness, modify the existing copy and not update to whats passed to us
        // i.e. only update the confirmant information
        existing.setConfirmant(bdf.getConfirmant());

        final BirthDeclaration.State currentState = bdf.getRegister().getStatus();
        if (BirthDeclaration.State.CONFIRMATION_PRINTED == currentState) {
            bdf.getRegister().setStatus(BirthDeclaration.State.CONFIRMED_WITHOUT_CHANGES);
            birthDeclarationDAO.updateBirthDeclaration(bdf);
            logger.debug("Marked birth record : {} as confirmed without changes", bdf.getIdUKey());

        } else {
            handleException("Cannot approve confirmation : " + bdf.getIdUKey() + " Illegal state : " + currentState,
                ErrorCodes.INVALID_STATE_FOR_BDF_CONFIRMATION);
        }
    }

    /**
     * @inheritDoc
     */
    public void captureLiveBirthConfirmationChanges(BirthDeclaration bdf, User user) {

        logger.debug("Attempt to capture changes for birth record : {} ", bdf.getIdUKey());

        // does the user have access to the BDF being confirmed (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateAccessOfUser(user, existing);

        final BirthDeclaration.State currentState = existing.getRegister().getStatus();
        if (BirthDeclaration.State.CONFIRMATION_PRINTED == currentState) {
            // mark existing as archived with a newer record of corrections
            existing.setConfirmant(bdf.getConfirmant());
            existing.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CORRECTED);
            birthDeclarationDAO.updateBirthDeclaration(existing);

            // add new record
            bdf.setIdUKey(0); // force addition
            bdf.getRegister().setStatus(BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED);
            birthDeclarationDAO.addBirthDeclaration(bdf);
            logger.debug("Changes captured as birth record : {} and the old record : {} archived",
                bdf.getIdUKey(), existing.getIdUKey());

        } else if (BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED == currentState) {
            bdf.getRegister().setStatus(BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED);
            birthDeclarationDAO.updateBirthDeclaration(bdf);
            logger.debug("Changes captured for birth record : {} ", bdf.getIdUKey());

        } else {
            handleException("Cannot capture confirmation : " + bdf.getIdUKey() + " Illegal state : " + currentState,
                ErrorCodes.INVALID_STATE_FOR_CONFIRMATION_CHANGES);
        }    
    }

    /**
     * @inheritDoc
     */
    public List<UserWarning> approveConfirmationChangesForIDList(long[] approvalDataList, User user) {

        logger.debug("Request for approval of confirmation changes for record IDs : {}", approvalDataList);

        if (!user.isAuthorized(Permission.APPROVE_BDF_CONFIRMATION)) {
            handleException("User : " + user.getUserId() + " is not allowed to approve/reject birth confirmation",
                ErrorCodes.PERMISSION_DENIED);
        }

        List<UserWarning> warnings = new ArrayList<UserWarning>();
        for (long id : approvalDataList) {
            BirthDeclaration bdf = birthDeclarationDAO.getById(id);
            List<UserWarning> w = approveConfirmationChanges(bdf, false, user);
            if (!w.isEmpty()) {
                warnings.add(new UserWarning("Birth Declaration Confirmation with ID : " + id +
                    " must be approved after validating warnings"));
            }
        }
        return warnings;
    }

    /**
     * @inheritDoc
     */
    public void markLiveBirthCertificateAsPrinted(BirthDeclaration bdf, User user) {

        logger.debug("Request to mark as Birth certificate printed for record : {}", bdf.getIdUKey());

        // load the existing record
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());

        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        validateAccessOfUser(user, existing);

        existing.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_BC_PRINTED);
        existing.getRegister().setOriginalBCDateOfIssue(new Date());
        // TODO existing.getRegister().setOriginalBCPlaceOfIssue();
        birthDeclarationDAO.updateBirthDeclaration(existing);

        logger.debug("Marked as Birth certificate printed for record : {}", bdf.getIdUKey());
    }

    /**
     * @inheritDoc
     */
    public void markLiveBirthCertificateIDsAsPrinted(long[] printedIDList, User user) {

        logger.debug("Request to mark as Birth certificate printed for records : {}", printedIDList);

        for (long l : printedIDList) {
            BirthDeclaration bdf = birthDeclarationDAO.getById(l);
            markLiveBirthCertificateAsPrinted(bdf, user);
        }
    }

    /**
     * @inheritDoc
     */
    public List<UserWarning> approveConfirmationChanges(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        logger.debug("Request to approve confirmation changes for record : {}", bdf.getIdUKey());

        // check approve permission
        if (!user.isAuthorized(Permission.APPROVE_BDF_CONFIRMATION)) {
            handleException("User : " + user.getUserId() + " is not allowed to approve/reject birth confirmation",
                ErrorCodes.PERMISSION_DENIED);
        }

        final BirthDeclaration.State currentState = bdf.getRegister().getStatus();
        if (BirthDeclaration.State.CONFIRMATION_PRINTED == currentState ||
            BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED == currentState) {

            List<UserWarning> warnings = prepareForConfirmation(bdf, ignoreWarnings, user);
            if (warnings.isEmpty() || ignoreWarnings) {
                bdf.getRegister().setStatus(BirthDeclaration.State.CONFIRMATION_CHANGES_APPROVED);
                birthDeclarationDAO.updateBirthDeclaration(bdf);
                logger.debug("Approved confirmation changes for record : {}", bdf.getIdUKey());
            }
            return warnings;

        } else {
            handleException("Cannot approve confirmation : " + bdf.getIdUKey() + " Illegal state : " + currentState,
                ErrorCodes.INVALID_STATE_FOR_BDF_CONFIRMATION);
        }
        return null;
    }

    private List<UserWarning> prepareForConfirmation(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        // does the user have access to the BDF being confirmed (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateAccessOfUser(user, existing);

        // validate if the minimum required fields are adequately filled
        BirthDeclarationValidator.validateMinimalRequirements(bdf);

        // validate standard validations anyway, since even if validations are rejected a note of it will be made
        // against the approval for audit requirements
        List<UserWarning> warnings = BirthDeclarationValidator.validateStandardRequirements(birthDeclarationDAO, bdf, user);

        if (!warnings.isEmpty() && ignoreWarnings) {
            StringBuilder sb = new StringBuilder();
            if (existing.getRegister().getComments() != null) {
                sb.append(existing.getRegister().getComments()).append("\n");
            }

            // SimpleDateFormat is not thread-safe
            synchronized (dfm) {
                sb.append(dfm.format(new Date())).append(" - Approved birth declaration ignoring warnings. User : ").
                    append(user.getUserId()).append("\n");
            }

            for (UserWarning w : warnings) {
                sb.append(w.getSeverity());
                sb.append("-");
                sb.append(w.getMessage());
            }
            bdf.getRegister().setComments(sb.toString());
        }
        return warnings;
    }

    /**
     * @inheritDoc
     */
    public void rejectBirthDeclaration(BirthDeclaration bdf, String comments, User user) {

        logger.debug("Request to reject birth declaration record : {}", bdf.getIdUKey());

        if (comments == null || comments.trim().length() < 1) {
            handleException("A comment is required to reject a birth declaration",
                ErrorCodes.COMMENT_REQUIRED_BDF_REJECT);
        }

        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateAccessOfUser(user, existing);

        // check state of record
        BirthDeclaration.State currentState = existing.getRegister().getStatus();
        if (BirthDeclaration.State.CONFIRMATION_PRINTED == currentState ||
            BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED == currentState ||
            BirthDeclaration.State.DATA_ENTRY == currentState) {

            // check approve/reject permission
            if (!user.isAuthorized(Permission.APPROVE_BDF)) {
                handleException("User : " + user.getUserId() + " is not allowed to approve/reject birth declarations",
                    ErrorCodes.PERMISSION_DENIED);
            }
            bdf.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_REJECTED);
            birthDeclarationDAO.updateBirthDeclaration(bdf);
            logger.debug("Rejected birth declaration record : {} by user : {}", bdf.getIdUKey(), user.getUserId());

        } else {
            handleException("Cannot reject birth declaration / confirmation : " + bdf.getIdUKey() +
                " Illegal state : " + currentState, ErrorCodes.INVALID_STATE_FOR_BDF_REJECTION);
        }
    }

    private void validateAccessOfUser(User user, BirthDeclaration bdf) {
        BDDivision bdDivision = bdf.getRegister().getBirthDivision();
        if (!user.isAllowedAccessToBDDistrict(bdDivision.getDistrict().getDistrictUKey()) &&
            !user.isAllowedAccessToBDDSDivision(bdDivision.getDsDivision().getDsDivisionUKey())) {

            handleException("User : " + user.getUserId() + " is not allowed access to the District : " +
                bdDivision.getDistrict().getDistrictId() + " and/or DS Division : " +
                bdDivision.getDsDivision().getDivisionId(), ErrorCodes.PERMISSION_DENIED);
        }
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }

    /**
     * @inheritDoc
     */
    public BirthDeclaration getById(long bdId, User user) {
        logger.debug("Load birth declaration record : {}", bdId);
        BirthDeclaration bdf = birthDeclarationDAO.getById(bdId);
        // does the user have access to the BDF (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        return bdf;
    }

    /**
     * @inheritDoc
     */
    public BirthDeclaration getByBDDivisionAndSerialNo(BDDivision bdDivision, long serialNo, User user) {

        logger.debug("Get records by BDDivision ID : {} and Serial No : {}", bdDivision.getBdDivisionUKey(), serialNo);

        BirthDeclaration bdf = birthDeclarationDAO.getByBDDivisionAndSerialNo(bdDivision, serialNo);
        // does the user have access to the BDF (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        return bdf;
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getConfirmationApprovalPending(BDDivision bdDivision, int pageNo, int noOfRows) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get confirmations pending approval by BDDivision ID : " + bdDivision.getBdDivisionUKey()
                + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        return birthDeclarationDAO.getPaginatedListForState(
            bdDivision, pageNo, noOfRows, BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED);
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getConfirmationPrintList(
        BDDivision bdDivision, int pageNo, int noOfRows, boolean printed) {

        if (logger.isDebugEnabled()) {
            logger.debug("Get records pending confirmation printing by BDDivision ID : " +
                bdDivision.getBdDivisionUKey() + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        return birthDeclarationDAO.getPaginatedListForState(bdDivision, pageNo, noOfRows,
            printed ? BirthDeclaration.State.CONFIRMATION_PRINTED : BirthDeclaration.State.APPROVED);
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getBirthCertificatePrintList(
        BDDivision bdDivision, int pageNo, int noOfRows, boolean printed) {

        if (logger.isDebugEnabled()) {
            logger.debug("Get birth certificate list print by BDDivision ID : " +
                bdDivision.getBdDivisionUKey() + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }

        return birthDeclarationDAO.getPaginatedListForState(bdDivision, pageNo, noOfRows,
            printed ? BirthDeclaration.State.ARCHIVED_BC_PRINTED : BirthDeclaration.State.ARCHIVED_BC_GENERATED);
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getDeclarationApprovalPending(BDDivision bdDivision, int pageNo, int noOfRows) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get records pending approval by BDDivision ID : " + bdDivision.getBdDivisionUKey()
                + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        return birthDeclarationDAO.getPaginatedListForState(bdDivision, pageNo, noOfRows, BirthDeclaration.State.DATA_ENTRY);
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getDeclarationPendingByBDDivisionAndRegisterDateRange(BDDivision bdDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows) {

        if (logger.isDebugEnabled()) {
            logger.debug("Get records pending approval by BDDivision ID : " + bdDivision.getBdDivisionUKey() +
                " and date range : " + startDate + " to " + endDate + " Page : " + pageNo +
                " with number of rows per page : " + noOfRows);
        }
        return birthDeclarationDAO.getByBDDivisionStatusAndRegisterDateRange(
            bdDivision, BirthDeclaration.State.DATA_ENTRY, startDate, endDate, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getByBDDivisionStatusAndConfirmationReceiveDateRange(BDDivision bdDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows) {

        if (logger.isDebugEnabled()) {
            logger.debug("Get confirmation records pending approval by BDDivision ID : " +
                bdDivision.getBdDivisionUKey() + " and date range : " + startDate + " to " + endDate +
                " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        return birthDeclarationDAO.getByBDDivisionStatusAndConfirmationReceiveDateRange(
            bdDivision, BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED, startDate, endDate, pageNo, noOfRows);
    }

    /**
     * Populates transient string values for Country, Race, District, Division etc
     *
     * @param bdf the BirthDeclaration to populate transient values
     * @return populated BDF
     */
    public BirthDeclaration loadValuesForPrint(BirthDeclaration bdf) {

        logger.debug("Loading record : {} for printing", bdf.getIdUKey());

        String prefLanguage = bdf.getRegister().getPreferredLanguage();

        ChildInfo child = bdf.getChild();
        child.setChildGenderPrint(GenderUtil.getGender(bdf.getChild().getChildGender(), prefLanguage));

        BirthRegisterInfo brInfo = bdf.getRegister();
        if (brInfo.getOriginalBCPlaceOfIssue() != null) {
            brInfo.setOriginalBCPlaceOfIssuePrint(dsDivisionDAO.getNameByPK(brInfo.getOriginalBCPlaceOfIssue(), prefLanguage));
        }
        if (brInfo.getBirthDivision() != null) {
            brInfo.setDistrictPrint(districtDAO.getNameByPK(brInfo.getBirthDistrict().getDistrictUKey(), prefLanguage));
            brInfo.setDsDivisionPrint(dsDivisionDAO.getNameByPK(brInfo.getDsDivision().getDsDivisionUKey(), prefLanguage));
            brInfo.setBdDivisionPrint(bdDivisionDAO.getNameByPK(brInfo.getBirthDivision().getBdDivisionUKey(), prefLanguage));
        }

        ParentInfo parent = bdf.getParent();
        if (parent != null) {
            if (parent.getFatherCountry() != null) {
                parent.setFatherCountryPrint(
                    countryDAO.getNameByPK(parent.getFatherCountry().getCountryId(), prefLanguage));
            }
            if (parent.getMotherCountry() != null) {
                parent.setMotherCountryPrint(
                    countryDAO.getNameByPK(parent.getMotherCountry().getCountryId(), prefLanguage));
            }
            if (parent.getFatherRace() != null) {
                parent.setFatherRacePrint(
                    raceDAO.getNameByPK(parent.getFatherRace().getRaceId(), prefLanguage));
            }
            if (parent.getMotherRace() != null) {
                parent.setMotherRacePrint(
                    raceDAO.getNameByPK(parent.getMotherRace().getRaceId(), prefLanguage));
            }

            if (parent.getMotherDSDivision() != null) {
                parent.setMotherDistrictPrint(
                    districtDAO.getNameByPK(parent.getMotherDSDivision().getDistrict().getDistrictUKey(), prefLanguage));
                parent.setMotherDsDivisionPrint(
                    dsDivisionDAO.getNameByPK(parent.getMotherDSDivision().getDsDivisionUKey(), prefLanguage));
            }
        }

        return bdf;
    }
}
