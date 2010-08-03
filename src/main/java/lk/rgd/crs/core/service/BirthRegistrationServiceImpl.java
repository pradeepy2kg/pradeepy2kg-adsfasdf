package lk.rgd.crs.core.service;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.AppParameter;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.dao.BCSearchDAO;

import lk.rgd.prs.api.domain.Address;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.NoResultException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private final PopulationRegistry popreg;
    private final AppParametersDAO appParametersDAO;
    private final UserManager userManager;
    private final BirthRecordsIndexer birthRecordsIndexer;
    private final BCSearchDAO bcSearchDAO;

    public BirthRegistrationServiceImpl(
        BirthDeclarationDAO birthDeclarationDAO, DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO,
        BDDivisionDAO bdDivisionDAO, CountryDAO countryDAO, RaceDAO raceDAO,
        PopulationRegistry popreg, AppParametersDAO appParametersDAO, UserManager userManager,
        BirthRecordsIndexer birthRecordsIndexer, BCSearchDAO bcSearchDAO) {
        this.birthDeclarationDAO = birthDeclarationDAO;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.countryDAO = countryDAO;
        this.raceDAO = raceDAO;
        this.popreg = popreg;
        this.appParametersDAO = appParametersDAO;
        this.userManager = userManager;
        this.birthRecordsIndexer = birthRecordsIndexer;
        this.bcSearchDAO = bcSearchDAO;
    }

    /**
     * @inheritDoc
     */
    public List<UserWarning> addLiveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user,
        String caseFileNumber, String additionalDocumentsComment) {
        logger.debug("Adding a new live birth declaration");

        // ensure name is in upper case
        ChildInfo child = bdf.getChild();
        if (child.getChildFullNameEnglish() != null) {
            child.setChildFullNameEnglish(child.getChildFullNameEnglish().toUpperCase());
        }

        // TODO add case file number and additional document list as comments
        addBirthDeclaration(bdf, ignoreWarnings, user);
        logger.debug("Added a new live birth declaration. IDUKey : {}", bdf.getIdUKey());
        return null;
    }

    /**
     * @inheritDoc
     */
    public List<UserWarning> addStillBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {
        logger.debug("Adding a new still birth declaration");

        // TODO still birth specific validations
        addBirthDeclaration(bdf, ignoreWarnings, user);
        logger.debug("Added a new still birth declaration. IDUKey : {}", bdf.getIdUKey());
        return null;
    }

    /**
     * Set of common actions to both Live and Still birth declaration adding
     *
     * @param bdf            the BDF to be added
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user           the user initiating the action
     * @return a list of warnings if applicable for the record
     */
    private List<UserWarning> addBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        // does the user have access to the BDF being added (i.e. check district and DS division)
        // TODO if a mother is specified, is she alive? etc
        if (!ignoreWarnings) {
            // TODO more validations .. like bdf.getParent().getMotherFullName() != null etc
        }
        validateAccessOfUser(user, bdf);

        // has this serial number been used already?
        try {
            BirthDeclaration existing = birthDeclarationDAO.getByBDDivisionAndSerialNo(
                bdf.getRegister().getBirthDivision(), bdf.getRegister().getBdfSerialNo());
            if (existing != null) {
                handleException("The birth declaration BD Division/Serial number is a duplicate : " +
                    bdf.getRegister().getBirthDivision().getBdDivisionUKey() + " " +
                    bdf.getRegister().getBdfSerialNo(), ErrorCodes.INVALID_DATA);
            }
        } catch (NoResultException ignore) {
        }

        bdf.getRegister().setStatus(BirthDeclaration.State.DATA_ENTRY);
        birthDeclarationDAO.addBirthDeclaration(bdf);
        return null;
    }

    /**
     * @inheritDoc
     */
    public List<UserWarning> approveBirthDeclarationIdList(long[] approvalDataList, User user) {

        logger.debug("Request for approval of BDFs with IDUKeys : {}", approvalDataList);

        if (!user.isAuthorized(Permission.APPROVE_BDF)) {
            handleException("The user : " + user.getUserId() +
                " is not authorized to approve birth declarations", ErrorCodes.PERMISSION_DENIED);
        }

        List<UserWarning> warnings = new ArrayList<UserWarning>();
        for (long id : approvalDataList) {
            BirthDeclaration bdf = birthDeclarationDAO.getById(id);

            List<UserWarning> w = null;
            if (isLiveBirth(bdf)) {
                w = approveLiveBirthDeclaration(bdf, false, user);
            } else {
                w = approveStillBirthDeclaration(bdf, false, user);
            }
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

        validateBirthType(bdf, true);
        logger.debug("Attempt to edit live birth declaration record : {}", bdf.getIdUKey());
        // ensure name in english is in upper case
        ChildInfo child = bdf.getChild();
        if (child.getChildFullNameEnglish() != null) {
            child.setChildFullNameEnglish(child.getChildFullNameEnglish().toUpperCase());
        }

        // does the user have access to the BDF being updated
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(bdf, true);
        validateAccessOfUser(user, existing);

        // TODO check validations as per addLiveBirthDeclaration

        // a BDF can be edited by a DEO or ADR only before being approved
        final BirthDeclaration.State currentState = existing.getRegister().getStatus();
        if (currentState == BirthDeclaration.State.DATA_ENTRY) {
            birthDeclarationDAO.updateBirthDeclaration(bdf);
            logger.debug("Saved edit changes to live birth declaration record : {}  in data entry state", bdf.getIdUKey());

        } else {
            handleException("Cannot modify live birth declaration : " + existing.getIdUKey() +
                " Illegal state : " + currentState, ErrorCodes.ILLEGAL_STATE);
        }
    }

    /**
     * @inheritDoc
     */
    public void editStillBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        validateBirthType(bdf, false);
        logger.debug("Attempt to edit still birth declaration record : {}", bdf.getIdUKey());

        // TODO check user have access to edit still BDF
        // does the user have access to the BDF being updated
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(bdf, false);
        validateAccessOfUser(user, existing);

        // TODO check validations as per addStillBirthDeclaration
        // a still BDF can be edited by a ADR only before being approved
        final BirthDeclaration.State currentState = existing.getRegister().getStatus();
        if (currentState == BirthDeclaration.State.DATA_ENTRY) {
            birthDeclarationDAO.updateBirthDeclaration(bdf);
            logger.debug("Saved edit changes to still birth declaration record : {}  in data entry state", bdf.getIdUKey());

        } else {
            handleException("Cannot modify still birth declaration : " + existing.getIdUKey() +
                " Illegal state : " + currentState, ErrorCodes.ILLEGAL_STATE);
        }
    }

    /**
     * @inheritDoc
     */
    public void deleteLiveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        validateBirthType(bdf, true);
        logger.debug("Attempt to delete live birth declaration record : {}", bdf.getIdUKey());

        // does the user have access to the BDF being deleted
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(bdf, true);
        validateAccessOfUser(user, existing);

        // a live BDF can be edited by a DEO or ADR only before being approved
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
    public void deleteStillBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        validateBirthType(bdf, false);
        logger.debug("Attempt to delete still birth declaration record : {}", bdf.getIdUKey());

        // TODO check user have access to edit still BDF
        // does the user have access to the BDF being deleted
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(bdf, false);
        validateAccessOfUser(user, existing);

        // a still BDF can be edited by a ADR only before being approved
        final BirthDeclaration.State currentState = existing.getRegister().getStatus();
        if (currentState == BirthDeclaration.State.DATA_ENTRY) {
            birthDeclarationDAO.deleteBirthDeclaration(bdf.getIdUKey());
            logger.debug("Deleted still birth declaration record : {} in data entry state", bdf.getIdUKey());

        } else {
            handleException("Cannot delete still birth declaration " + existing.getIdUKey() +
                " Illegal state : " + currentState, ErrorCodes.ILLEGAL_STATE);
        }
    }

    /**
     * @inheritDoc
     */
    public List<UserWarning> approveLiveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        validateBirthType(bdf, true);
        logger.debug("Attempt to approve live birth declaration : {} Ignore warnings : {}", bdf.getIdUKey(), ignoreWarnings);
        // ensure name in english is in upper case
        ChildInfo child = bdf.getChild();
        if (child.getChildFullNameEnglish() != null) {
            child.setChildFullNameEnglish(child.getChildFullNameEnglish().toUpperCase());
        }

        // load the existing record
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(bdf, true);

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
            bdf.getRegister().setApproveDate(new Date());
            bdf.getRegister().setApproveUser(user);
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
    public List<UserWarning> approveStillBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        validateBirthType(bdf, false);
        logger.debug("Attempt to approve still birth declaration : {} Ignore warnings : {}", bdf.getIdUKey(), ignoreWarnings);

        // load the existing record
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(bdf, false);

        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        validateAccessOfUser(user, existing);

        // TODO check approve permission for still births
        // check approve permission
        if (!user.isAuthorized(Permission.APPROVE_BDF)) {
            handleException("User : " + user.getUserId() + " is not allowed to approve/reject still birth declarations",
                ErrorCodes.PERMISSION_DENIED);
        }

        // is the BDF currently existing in a state for approval
        final BirthDeclaration.State currentState = existing.getRegister().getStatus();
        if (BirthDeclaration.State.DATA_ENTRY != currentState) {
            handleException("Cannot approve still birth declaration : " + bdf.getIdUKey() + " Illegal state : " + currentState,
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
                sb.append(dfm.format(new Date())).append(" - Approved still birth declaration ignoring warnings. User : ").
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
            bdf.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CERT_GENERATED);
            bdf.getRegister().setApproveDate(new Date());
            bdf.getRegister().setApproveUser(user);
            birthDeclarationDAO.updateBirthDeclaration(bdf);
            logger.debug("Approved still birth declaration record : {} Ignore warnings : {}", bdf.getIdUKey(), ignoreWarnings);
        } else {
            logger.debug("Approval of still birth declaration record : {} stopped due to warnings", bdf.getIdUKey());
        }
        return warnings;
    }

    /**
     * @inheritDoc
     */
    public void markLiveBirthConfirmationAsPrinted(BirthDeclaration bdf, User user) {

        validateBirthType(bdf, true);
        logger.debug("Attempt to mark confirmation printed for live birth declaration record : {}", bdf.getIdUKey());

        // load the existing record
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(bdf, true);

        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        validateAccessOfUser(user, existing);

        existing.getRegister().setConfirmationPrintDate(new Date());
        existing.getRegister().setConfirmationPrintUser(user);
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

        validateBirthType(bdf, true);
        logger.debug("Attempt to mark birth record : {} as confirmed without changes", bdf.getIdUKey());

        // does the user have access to the BDF being confirmed (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(bdf, true);
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

        // generate PIN number and add record to PRS
        generatePINandAddToPRS(bdf, user);
    }

    /**
     * @inheritDoc
     */
    public void captureLiveBirthConfirmationChanges(BirthDeclaration bdf, User user) {

        validateBirthType(bdf, true);
        logger.debug("Attempt to capture changes for birth record : {} ", bdf.getIdUKey());
        // ensure name in english is in upper case
        ChildInfo child = bdf.getChild();
        if (child.getChildFullNameEnglish() != null) {
            child.setChildFullNameEnglish(child.getChildFullNameEnglish().toUpperCase());
        }

        // does the user have access to the BDF being confirmed (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(bdf, true);
        validateAccessOfUser(user, existing);

        final BirthDeclaration.State currentState = existing.getRegister().getStatus();
        if (BirthDeclaration.State.CONFIRMATION_PRINTED == currentState) {
            // mark existing as archived with a newer record of corrections
            existing.setConfirmant(bdf.getConfirmant());
            existing.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CORRECTED);
            existing.setActiveRecord(false);
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

        validateBirthType(bdf, true);
        logger.debug("Request to mark as Birth certificate printed for record : {}", bdf.getIdUKey());

        // load the existing record
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(bdf, true);

        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        validateAccessOfUser(user, existing);

        existing.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CERT_PRINTED);
        final Date originalBCDateOfIssue = new Date();
        existing.getRegister().setOriginalBCDateOfIssue(originalBCDateOfIssue);
        bdf.getRegister().setOriginalBCDateOfIssue(originalBCDateOfIssue);
        existing.getRegister().setOriginalBCPrintUser(user);
        bdf.getRegister().setOriginalBCPrintUser(user);
        // TODO existing.getRegister().setOriginalBCPlaceOfIssue();
        birthDeclarationDAO.updateBirthDeclaration(existing);

        // index record
        birthRecordsIndexer.add(existing);

        logger.debug("Marked as Birth certificate printed for record : {}", bdf.getIdUKey());
    }

    /**
     * @inheritDoc
     */
    public void markStillBirthCertificateAsPrinted(BirthDeclaration bdf, User user) {

        validateBirthType(bdf, false);
        logger.debug("Request to mark as Still Birth certificate printed for record : {}", bdf.getIdUKey());

        // load the existing record
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(bdf, false);

        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        validateAccessOfUser(user, existing);

        existing.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CERT_PRINTED);
        final Date originalBCDateOfIssue = new Date();
        existing.getRegister().setOriginalBCDateOfIssue(originalBCDateOfIssue);
        bdf.getRegister().setOriginalBCDateOfIssue(originalBCDateOfIssue);
        existing.getRegister().setOriginalBCPrintUser(user);
        bdf.getRegister().setOriginalBCPrintUser(user);
        // TODO existing.getRegister().setOriginalBCPlaceOfIssue();
        birthDeclarationDAO.updateBirthDeclaration(existing);

        // index record
        birthRecordsIndexer.add(existing);

        logger.debug("Marked as Still Birth certificate printed for record : {}", bdf.getIdUKey());
    }

    /**
     * BirthRegistrationServiceImpl
     *
     * @inheritDoc
     */
    public void markBirthCertificateIDsAsPrinted(long[] printedIDList, User user) {
        logger.debug("Request to mark as Birth certificate printed for records : {}", printedIDList);
        logger.info("finished1");
        for (long l : printedIDList) {
            BirthDeclaration bdf = birthDeclarationDAO.getById(l);
            logger.info("BDF is still or Live : {}", bdf.getRegister().getBirthType());

            if (isLiveBirth(bdf)) {
                markLiveBirthCertificateAsPrinted(bdf, user);
                logger.info("finished2");
            } else {
                markStillBirthCertificateAsPrinted(bdf, user);
                logger.info("finished3");
            }
        }
    }

    /**
     * @inheritDoc
     */
    public List<UserWarning> approveConfirmationChanges(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        validateBirthType(bdf, true);
        logger.debug("Request to approve confirmation changes for record : {}", bdf.getIdUKey());
        // ensure name in english is in upper case
        ChildInfo child = bdf.getChild();
        if (child.getChildFullNameEnglish() != null) {
            child.setChildFullNameEnglish(child.getChildFullNameEnglish().toUpperCase());
        }
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
                bdf.getRegister().setApproveDate(new Date());
                bdf.getRegister().setApproveUser(user);
                birthDeclarationDAO.updateBirthDeclaration(bdf);
                logger.debug("Approved confirmation changes for record : {}", bdf.getIdUKey());

                // generate PIN number and add record to PRS
                generatePINandAddToPRS(bdf, user);
            }
            return warnings;

        } else {
            handleException("Cannot approve confirmation : " + bdf.getIdUKey() + " Illegal state : " + currentState,
                ErrorCodes.INVALID_STATE_FOR_BDF_CONFIRMATION);
        }
        return null;
    }

    private List<UserWarning> prepareForConfirmation(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        validateBirthType(bdf, true);
        // does the user have access to the BDF being confirmed (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(bdf, true);
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
        // ensure name in english is in upper case
        ChildInfo child = bdf.getChild();
        if (child.getChildFullNameEnglish() != null) {
            child.setChildFullNameEnglish(child.getChildFullNameEnglish().toUpperCase());
        }

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
        validateAccessToBDDivision(user, bdDivision);
    }

    private void validateAccessToBDDivision(User user, BDDivision bdDivision) {
        if (!(User.State.ACTIVE == user.getStatus()
            &&
            (Role.ROLE_RG.equals(user.getRole().getName())
                ||
                (user.isAllowedAccessToBDDistrict(bdDivision.getDistrict().getDistrictUKey())
                    &&
                    user.isAllowedAccessToBDDSDivision(bdDivision.getDsDivision().getDsDivisionUKey())
                )
            )
        )) {

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
    public List<BirthDeclaration> getConfirmationApprovalPending(BDDivision bdDivision, int pageNo, int noOfRows, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get confirmations pending approval by BDDivision ID : " + bdDivision.getBdDivisionUKey()
                + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        validateAccessToBDDivision(user, bdDivision);
        return birthDeclarationDAO.getPaginatedListForState(
            bdDivision, pageNo, noOfRows, BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED);
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getConfirmationPrintList(
        BDDivision bdDivision, int pageNo, int noOfRows, boolean printed, User user) {

        if (logger.isDebugEnabled()) {
            logger.debug("Get records pending confirmation printing by BDDivision ID : " +
                bdDivision.getBdDivisionUKey() + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        validateAccessToBDDivision(user, bdDivision);
        return birthDeclarationDAO.getPaginatedListForState(bdDivision, pageNo, noOfRows,
            printed ? BirthDeclaration.State.CONFIRMATION_PRINTED : BirthDeclaration.State.APPROVED);
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getBirthCertificatePrintList(
        BDDivision bdDivision, int pageNo, int noOfRows, boolean printed, User user) {

        if (logger.isDebugEnabled()) {
            logger.debug("Get birth certificate list print by BDDivision ID : " +
                bdDivision.getBdDivisionUKey() + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        validateAccessToBDDivision(user, bdDivision);
        return birthDeclarationDAO.getPaginatedListForState(bdDivision, pageNo, noOfRows,
            printed ? BirthDeclaration.State.ARCHIVED_CERT_PRINTED : BirthDeclaration.State.ARCHIVED_CERT_GENERATED);
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getDeclarationApprovalPending(BDDivision bdDivision, int pageNo, int noOfRows, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get records pending approval by BDDivision ID : " + bdDivision.getBdDivisionUKey()
                + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        validateAccessToBDDivision(user, bdDivision);
        return birthDeclarationDAO.getPaginatedListForState(bdDivision, pageNo, noOfRows, BirthDeclaration.State.DATA_ENTRY);
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getDeclarationPendingByBDDivisionAndRegisterDateRange(BDDivision bdDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, User user) {

        if (logger.isDebugEnabled()) {
            logger.debug("Get records pending approval by BDDivision ID : " + bdDivision.getBdDivisionUKey() +
                " and date range : " + startDate + " to " + endDate + " Page : " + pageNo +
                " with number of rows per page : " + noOfRows);
        }
        validateAccessToBDDivision(user, bdDivision);
        return birthDeclarationDAO.getByBDDivisionStatusAndRegisterDateRange(
            bdDivision, BirthDeclaration.State.DATA_ENTRY, startDate, endDate, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getByBDDivisionStatusAndConfirmationReceiveDateRange(BDDivision bdDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, User user) {

        if (logger.isDebugEnabled()) {
            logger.debug("Get confirmation records pending approval by BDDivision ID : " +
                bdDivision.getBdDivisionUKey() + " and date range : " + startDate + " to " + endDate +
                " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        validateAccessToBDDivision(user, bdDivision);
        return birthDeclarationDAO.getByBDDivisionStatusAndConfirmationReceiveDateRange(
            bdDivision, BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED, startDate, endDate, pageNo, noOfRows);
    }

    /**
     * Populates transient string values for Country, Race, District, Division etc
     *
     * @param bdf the BirthDeclaration to populate transient values
     * @return populated BDF
     */
    public BirthDeclaration loadValuesForPrint(BirthDeclaration bdf, User user) {

        logger.debug("Loading record : {} for printing", bdf.getIdUKey());
        validateAccessOfUser(user, bdf);
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

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getByBirthDivision(BDDivision bdDivision, User user) {
        logger.debug("Get records birthDivision ID : {}", bdDivision.getBdDivisionUKey());
        validateAccessToBDDivision(user, bdDivision);
        return birthDeclarationDAO.getByBirthDivision(bdDivision);
    }

    /**
     * Generates a PIN and adds the record to the PRS
     */
    private List<UserWarning> generatePINandAddToPRS(BirthDeclaration bdf, User user) {

        logger.debug("Generating a PIN and adding record to the PRS for BDF UKey : {}", bdf.getIdUKey());

        List<UserWarning> warnings = new ArrayList<UserWarning>();
        ChildInfo childInfo = bdf.getChild();
        Person child = new Person();

        if (!isEmptyString(childInfo.getChildFullNameEnglish())) {
            child.setFullNameInEnglishLanguage(childInfo.getChildFullNameEnglish());
            String[] names = childInfo.getChildFullNameEnglish().split(" ");
            child.setLastNameInEnglish(names[names.length - 1]);
            StringBuilder sb = new StringBuilder(16);
            for (int i = 0; i < names.length - 2; i++) {
                sb.append(names[i].charAt(0)).append(". ");
            }
            child.setInitialsInEnglish(sb.toString());
            logger.debug("Derived child English initials as : {} and last name as : {}",
                sb.toString(), names[names.length - 1]);
        }

        if (!isEmptyString(childInfo.getChildFullNameOfficialLang())) {
            child.setFullNameInOfficialLanguage(childInfo.getChildFullNameOfficialLang());
            String[] names = childInfo.getChildFullNameOfficialLang().split(" ");
            child.setLastNameInOfficialLanguage(names[names.length - 1]);
            StringBuilder sb = new StringBuilder(16);
            for (int i = 0; i < names.length - 2; i++) {
                if (!isEmptyString(names[i])) {
                    sb.append(names[i].charAt(0)).append(". ");
                }
            }
            child.setInitialsInOfficialLanguage(sb.toString());
            logger.debug("Derived child Official language initials as : {} and last name as : {}",
                sb.toString(), names[names.length - 1]);
        }

        child.setDateOfBirth(childInfo.getDateOfBirth());
        child.setCivilStatus(Person.CivilStatus.NEVER_MARRIED);
        child.setGender(childInfo.getChildGender());
        child.setLifeStatus(Person.LifeStatus.ALIVE);
        child.setStatus(Person.Status.VERIFIED);
        child.setPreferredLanguage(bdf.getRegister().getPreferredLanguage());

        // check mother and father
        ParentInfo parent = bdf.getParent();
        if (bdf.getParent() != null) {
            processMotherToPRS(user, child, parent, bdf.getRegister().getPreferredLanguage());
            processFatherToPRS(user, child, parent, bdf.getRegister().getPreferredLanguage());
        }

        // generate a PIN number
        long pin = popreg.addPerson(child, user);
        childInfo.setPin(pin);
        bdf.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CERT_GENERATED);

        logger.debug("Generated PIN for record IDUKey : {} issued PIN : {}", bdf.getIdUKey(), pin);
        birthDeclarationDAO.updateBirthDeclaration(bdf);

        return warnings;
    }

    private void processMotherToPRS(User user, Person person, ParentInfo parent, String prefLanguage) {

        String motherNICorPIN = parent.getMotherNICorPIN();
        logger.debug("Processing details of the mother for NIC/PIN : {}", motherNICorPIN);

        if (motherNICorPIN != null) {
            Person mother = null;
            try {
                long pin = Long.parseLong(motherNICorPIN);
                mother = popreg.findPersonByPIN(pin, user);
                if (mother != null) {
                    logger.debug("Found mother by PIN : {}", pin);
                    person.setMother(mother);
                }
            } catch (NumberFormatException ignore) {
                // this could be an NIC
                List<Person> records = popreg.findPersonsByNIC(motherNICorPIN, user);
                if (records != null && records.size() == 1) {
                    logger.debug("Found mother by INC : {}", records.get(0).getNic());
                    mother = records.get(0);
                } else {
                    logger.debug("Could not locate a unique mother record using : {}", motherNICorPIN);
                    // TODO issue a user warning
                }
            } catch (NoResultException ignore) {
            }

            // if we couldn't locate the mother, add an unverified record to the PRS
            if (mother == null && parent.getMotherFullName() != null) {
                mother = new Person();
                mother.setFullNameInOfficialLanguage(parent.getMotherFullName());
                mother.setDateOfBirth(parent.getMotherDOB());
                mother.setGender(AppConstants.Gender.FEMALE.ordinal());
                mother.setPreferredLanguage(prefLanguage);
                if (parent.getMotherAddress() != null) {
                    Address mothersAddress = new Address();
                    mothersAddress.setLine1(parent.getMotherAddress());
                    Set<Address> addresses = new HashSet<Address>();
                    addresses.add(mothersAddress);
                    mother.setAddresses(addresses);
                }
                mother.setStatus(Person.Status.UNVERIFIED);
                mother.setLifeStatus(Person.LifeStatus.ALIVE);
                popreg.addPerson(mother, user);

                logger.debug("Added an unverified record for the mother into the PRS : {}", mother.getPersonUKey());
            }
        }
    }

    private void processFatherToPRS(User user, Person person, ParentInfo parent, String prefLanguage) {

        String fatherNICorPIN = parent.getFatherNICorPIN();
        if (fatherNICorPIN != null) {
            Person father = null;
            try {
                long pin = Long.parseLong(fatherNICorPIN);
                father = popreg.findPersonByPIN(pin, user);
                if (father != null) {
                    logger.debug("Found father by PIN : {}", pin);
                    person.setFather(father);
                }
            } catch (NumberFormatException ignore) {
                // this could be an NIC
                List<Person> records = popreg.findPersonsByNIC(fatherNICorPIN, user);
                if (records != null && records.size() == 1) {
                    logger.debug("Found father by INC : {}", records.get(0).getNic());
                    father = records.get(0);
                } else {
                    // TODO issue a user warning
                    logger.debug("Could not locate a unique father record using : {}", fatherNICorPIN);
                }
            } catch (NoResultException ignore) {
            }

            // if we couldn't locate the father, add an unverified record to the PRS
            if (father == null && parent.getFatherFullName() != null) {
                father = new Person();
                father.setFullNameInOfficialLanguage(parent.getFatherFullName());
                father.setDateOfBirth(parent.getFatherDOB());
                father.setGender(AppConstants.Gender.MALE.ordinal());
                father.setPreferredLanguage(prefLanguage);
                father.setStatus(Person.Status.UNVERIFIED);
                father.setLifeStatus(Person.LifeStatus.ALIVE);
                popreg.addPerson(father, user);

                logger.debug("Added an unverified record for the father into the PRS : {}", father.getPersonUKey());
            }
        }
    }

    /**
     * @inheritDoc
     */
    public void triggerScheduledJobs() {
        logger.info("Start executing Birth registration related scheduled tasks..");

        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.DATE, -1 * appParametersDAO.getIntParameter(AppParameter.CRS_AUTO_CONFIRMATION_DAYS));

        User systemUser = userManager.getSystemUser();
        List<BirthDeclaration> list = birthDeclarationDAO.getUnconfirmedByRegistrationDate(cal.getTime());
        for (BirthDeclaration bdf : list) {
            try {
                List<UserWarning> warnings = generatePINandAddToPRS(bdf, systemUser);
                for (UserWarning w : warnings) {
                    logger.warn(w.getMessage());
                }
            } catch (Exception e) {
                logger.error("Error occurred while auto confirming BDF : " + bdf.getIdUKey() + " by the system", e);
            }
        }
        logger.info("Stopped executing Birth registration related scheduled tasks..");
    }

    private static boolean isEmptyString(String s) {
        return s == null || s.trim().length() == 0;
    }

    private boolean isLiveBirth(BirthDeclaration bdf) {
        return (bdf.getRegister().getBirthType() == BirthDeclaration.BirthType.LIVE) ? true : false;
    }

    private void validateBirthType(BirthDeclaration bdf, boolean liveBirth) {
        boolean valid = (liveBirth ^ isLiveBirth(bdf)) ? false : true;
        if (!valid) {
            handleException("Live birth : " + bdf.getRegister().getBirthType() + ", BDF : " + bdf.getIdUKey() +
                " in invalid context", ErrorCodes.ILLEGAL_STATE);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("bith type checking for BDF : " + bdf.getIdUKey() + " passed for live birth : " +
                bdf.getRegister().getBirthType());
        }
    }

    /**
     * @inheritDoc
     */
    public void addBirthCertificateSearch(BirthCertificateSearch bcs, User user) {
        logger.debug("Adding a new birth certificate search entry");

        // set user perform searching and the timestamp
        bcs.setSearchUser(user);
        bcs.setSearchPerformDate(new Date());

        bcSearchDAO.addBirthCertificateSearch(bcs);
        logger.debug("Added a new birth certificate search entry. SearchUKey : {} by UserID", bcs.getSearchUKey(),
            user.getUserId());
    }

    /**
     * @inheritDoc
     */
    public List<BirthDeclaration> getArchivedCorrectedEntriesForGivenSerialNo(BDDivision bdDivision, long serialNo, User user) {
        logger.debug("Searching for historical records for BD Division : {} and Serial number : {} ",
            bdDivision.getBdDivisionUKey(), serialNo);
        validateAccessToBDDivision(user, bdDivision);
        return birthDeclarationDAO.getHistoricalRecordsForBDDivisionAndSerialNo(bdDivision, serialNo);
    }
}
