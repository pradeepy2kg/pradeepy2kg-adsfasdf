package lk.rgd.crs.core.service;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.AppParameter;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.common.util.MarriedStatusUtil;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.dao.BCSearchDAO;
import lk.rgd.crs.api.dao.AdoptionOrderDAO;

import lk.rgd.prs.api.domain.Address;
import lk.rgd.prs.api.domain.Marriage;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * The central service managing the CRS Birth Registration process
 */
public class BirthRegistrationServiceImpl implements
        BirthRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegistrationServiceImpl.class);
    private final BirthDeclarationDAO birthDeclarationDAO;
    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final BDDivisionDAO bdDivisionDAO;
    private final CountryDAO countryDAO;
    private final RaceDAO raceDAO;
    private final PopulationRegistry ecivil;
    private final AppParametersDAO appParametersDAO;
    private final UserManager userManager;
    private final BirthRecordsIndexer birthRecordsIndexer;
    private final BCSearchDAO bcSearchDAO;
    private final AdoptionOrderDAO adoptionOrderDAO;
    private final BirthDeclarationValidator birthDeclarationValidator;

    public BirthRegistrationServiceImpl(
            BirthDeclarationDAO birthDeclarationDAO, DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO,
            BDDivisionDAO bdDivisionDAO, CountryDAO countryDAO, RaceDAO raceDAO,
            PopulationRegistry ecivil, AppParametersDAO appParametersDAO, UserManager userManager,
            BirthRecordsIndexer birthRecordsIndexer, BCSearchDAO bcSearchDAO, AdoptionOrderDAO adoptionOrderDAO,
            BirthDeclarationValidator birthDeclarationValidator) {
        this.birthDeclarationDAO = birthDeclarationDAO;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.bdDivisionDAO = bdDivisionDAO;
        this.countryDAO = countryDAO;
        this.raceDAO = raceDAO;
        this.ecivil = ecivil;
        this.appParametersDAO = appParametersDAO;
        this.userManager = userManager;
        this.birthRecordsIndexer = birthRecordsIndexer;
        this.bcSearchDAO = bcSearchDAO;
        this.adoptionOrderDAO = adoptionOrderDAO;
        this.birthDeclarationValidator = birthDeclarationValidator;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserWarning> addLiveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {
        logger.debug("Adding a new live birth declaration");
        validateBirthType(bdf, BirthDeclaration.BirthType.LIVE);

        // validate if the minimum required fields are adequately filled
        birthDeclarationValidator.validateMinimalRequirements(bdf);

        // TODO add case file number and additional document list as comments
        addBirthDeclaration(bdf, ignoreWarnings, user);
        logger.debug("Added a new live birth declaration. IDUKey : {}", bdf.getIdUKey());
        return null;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserWarning> addBelatedBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {
        logger.debug("Adding a new belated birth declaration");
        validateBirthType(bdf, BirthDeclaration.BirthType.BELATED);

        // validate if the minimum required fields are adequately filled
        birthDeclarationValidator.validateMinimalRequirements(bdf);

        addBirthDeclaration(bdf, ignoreWarnings, user);
        logger.debug("Added a new belated birth declaration. IDUKey : {}", bdf.getIdUKey());
        return null;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserWarning> addStillBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {
        logger.debug("Adding a new still birth declaration");
        validateBirthType(bdf, BirthDeclaration.BirthType.STILL);

        // validate if the minimum required fields are adequately filled
        birthDeclarationValidator.validateMinimalRequirements(bdf);

        // TODO still birth specific validations
        addBirthDeclaration(bdf, ignoreWarnings, user);
        logger.debug("Added a new still birth declaration. IDUKey : {}", bdf.getIdUKey());
        return null;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserWarning> addAdoptionBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {
        logger.debug("Adding a new adoption birth declaration");
        validateBirthType(bdf, BirthDeclaration.BirthType.ADOPTION);

        // validate if the minimum required fields are adequately filled
        birthDeclarationValidator.validateMinimalRequirements(bdf);

        // TODO adoption specific validations

        AdoptionOrder existing = adoptionOrderDAO.getById(bdf.getRegister().getAdoptionUKey());
        final AdoptionOrder.State currentState = existing.getStatus();
        if (AdoptionOrder.State.ADOPTION_CERTIFICATE_PRINTED == currentState) {
            addBirthDeclaration(bdf, ignoreWarnings, user);
            adoptionOrderDAO.recordNewBirthDeclaration(existing, bdf.getIdUKey(), user);
            logger.debug("Changes captured for adoption record : {} added new birth certificate number : {}",
                    existing.getIdUKey(), bdf.getIdUKey());

        } else {
            handleException("Cannot archive adoption order : " + bdf.getRegister().getAdoptionUKey() +
                    " Illegal state : " + currentState, ErrorCodes.ILLEGAL_STATE);
        }

        logger.debug("Added a new adoption birth declaration. IDUKey : {}", bdf.getIdUKey());
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
        BirthDeclaration existing = birthDeclarationDAO.getActiveRecordByBDDivisionAndSerialNo(
                bdf.getRegister().getBirthDivision(), bdf.getRegister().getBdfSerialNo());
        if (existing != null) {
            handleException("The birth declaration BD Division/Serial number is a duplicate : " +
                    bdf.getRegister().getBirthDivision().getBdDivisionUKey() + " " +
                    bdf.getRegister().getBdfSerialNo(), ErrorCodes.INVALID_DATA);
        }

        bdf.getRegister().setStatus(BirthDeclaration.State.DATA_ENTRY);
        birthDeclarationDAO.addBirthDeclaration(bdf, user);
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
            final BirthDeclaration.BirthType current = bdf.getRegister().getBirthType();
            if (BirthDeclaration.BirthType.LIVE == current) {
                w = approveLiveBirthDeclaration(bdf.getIdUKey(), false, user);
            } else if (BirthDeclaration.BirthType.STILL == current) {
                w = approveStillBirthDeclaration(bdf.getIdUKey(), false, user);
            } else if (BirthDeclaration.BirthType.ADOPTION == current) {
                w = approveAdoptionBirthDeclaration(bdf.getIdUKey(), false, user);
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
    @Transactional(propagation = Propagation.REQUIRED)
    public void editLiveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        validateBirthType(bdf, BirthDeclaration.BirthType.LIVE);
        logger.debug("Attempt to edit live birth declaration record : {}", bdf.getIdUKey());

        // does the user have access to the BDF being updated
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(existing, BirthDeclaration.BirthType.LIVE);
        validateAccessOfUser(user, existing);

        // TODO check validations as per addLiveBirthDeclaration

        // a BDF can be edited by a DEO or ADR only before being approved
        final BirthDeclaration.State currentState = existing.getRegister().getStatus();
        if (currentState == BirthDeclaration.State.DATA_ENTRY) {
            birthDeclarationDAO.updateBirthDeclaration(bdf, user);
            logger.debug("Saved edit changes to live birth declaration record : {}  in data entry state", bdf.getIdUKey());

        } else {
            handleException("Cannot modify live birth declaration : " + existing.getIdUKey() +
                    " Illegal state : " + currentState, ErrorCodes.ILLEGAL_STATE);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void editBelatedBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        validateBirthType(bdf, BirthDeclaration.BirthType.BELATED);
        logger.debug("Attempt to edit belated birth declaration record : {}", bdf.getIdUKey());

        // does the user have access to the BDF being updated
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(existing, BirthDeclaration.BirthType.BELATED);
        validateAccessOfUser(user, existing);

        // TODO check validations as per addBelatedBirthDeclaration

        // a BDF can be edited by a DEO or ADR only before being approved
        final BirthDeclaration.State currentState = existing.getRegister().getStatus();
        if (currentState == BirthDeclaration.State.DATA_ENTRY) {
            birthDeclarationDAO.updateBirthDeclaration(bdf, user);
            logger.debug("Saved edit changes to belated birth declaration record : {}  in data entry state", bdf.getIdUKey());

        } else {
            handleException("Cannot modify belated birth declaration : " + existing.getIdUKey() +
                    " Illegal state : " + currentState, ErrorCodes.ILLEGAL_STATE);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void editStillBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        validateBirthType(bdf, BirthDeclaration.BirthType.STILL);
        logger.debug("Attempt to edit still birth declaration record : {}", bdf.getIdUKey());

        // TODO check user have access to edit still BDF
        // does the user have access to the BDF being updated
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(existing, BirthDeclaration.BirthType.STILL);
        validateAccessOfUser(user, existing);

        // TODO check validations as per addStillBirthDeclaration
        // a still BDF can be edited by a ADR only before being approved
        final BirthDeclaration.State currentState = existing.getRegister().getStatus();
        if (currentState == BirthDeclaration.State.DATA_ENTRY) {
            birthDeclarationDAO.updateBirthDeclaration(bdf, user);
            logger.debug("Saved edit changes to still birth declaration record : {}  in data entry state", bdf.getIdUKey());

        } else {
            handleException("Cannot modify still birth declaration : " + existing.getIdUKey() +
                    " Illegal state : " + currentState, ErrorCodes.ILLEGAL_STATE);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void editAdoptionBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        validateBirthType(bdf, BirthDeclaration.BirthType.ADOPTION);
        logger.debug("Attempt to edit adoption birth declaration record : {}", bdf.getIdUKey());

        // does the user have access to the BDF being updated
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(existing, BirthDeclaration.BirthType.ADOPTION);
        validateAccessOfUser(user, existing);

        // TODO check validations as per addAdoptionBirthDeclaration

        // a BDF can be edited by a DEO or ADR only before being approved
        final BirthDeclaration.State currentState = existing.getRegister().getStatus();
        if (currentState == BirthDeclaration.State.DATA_ENTRY) {
            birthDeclarationDAO.updateBirthDeclaration(bdf, user);
            logger.debug("Saved edit changes to adoption birth declaration record : {}  in data entry state", bdf.getIdUKey());

        } else {
            handleException("Cannot modify adoption birth declaration : " + existing.getIdUKey() +
                    " Illegal state : " + currentState, ErrorCodes.ILLEGAL_STATE);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteLiveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        validateBirthType(bdf, BirthDeclaration.BirthType.LIVE);
        logger.debug("Attempt to delete live birth declaration record : {}", bdf.getIdUKey());

        // does the user have access to the BDF being deleted
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(existing, BirthDeclaration.BirthType.LIVE);
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
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteStillBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        validateBirthType(bdf, BirthDeclaration.BirthType.STILL);
        logger.debug("Attempt to delete still birth declaration record : {}", bdf.getIdUKey());

        // TODO check user have access to edit still BDF
        // does the user have access to the BDF being deleted
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(existing, BirthDeclaration.BirthType.STILL);
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
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteBelatedBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        validateBirthType(bdf, BirthDeclaration.BirthType.BELATED);
        logger.debug("Attempt to delete live birth declaration record : {}", bdf.getIdUKey());

        // does the user have access to the BDF being deleted
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(existing, BirthDeclaration.BirthType.BELATED);
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
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAdoptionBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        validateBirthType(bdf, BirthDeclaration.BirthType.ADOPTION);
        logger.debug("Attempt to delete adoption birth declaration record : {}", bdf.getIdUKey());

        // does the user have access to the BDF being deleted
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(existing, BirthDeclaration.BirthType.ADOPTION);
        validateAccessOfUser(user, existing);

        // a live BDF can be edited by a DEO or ADR only before being approved
        final BirthDeclaration.State currentState = existing.getRegister().getStatus();
        if (currentState == BirthDeclaration.State.DATA_ENTRY) {
            birthDeclarationDAO.deleteBirthDeclaration(bdf.getIdUKey());
            logger.debug("Deleted adoption birth declaration record : {} in data entry state", bdf.getIdUKey());

        } else {
            handleException("Cannot delete adoption birth declaration " + existing.getIdUKey() +
                    " Illegal state : " + currentState, ErrorCodes.ILLEGAL_STATE);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    //todo upgrading
    public List<UserWarning> approveLiveBirthDeclaration(long idUKey, boolean ignoreWarnings, User user) {
        //validate birth type which is send by Action
        BirthDeclaration birth = getById(idUKey, user);
        validateBirthType(birth, BirthDeclaration.BirthType.LIVE);
        logger.debug("Attempt to approve live birth declaration : {} Ignore warnings : {}", idUKey, ignoreWarnings);
        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, birth);
        // check approve permission
        if (!user.isAuthorized(Permission.APPROVE_BDF)) {
            handleException("User : " + user.getUserId() + " is not allowed to approve/reject birth declarations",
                    ErrorCodes.PERMISSION_DENIED);
        }

        // is the BDF currently existing in a state for approval
        final BirthDeclaration.State currentState = birth.getRegister().getStatus();
        if (BirthDeclaration.State.DATA_ENTRY != currentState) {
            handleException("Cannot approve confirmation : " + idUKey + " Illegal state : " + currentState,
                    ErrorCodes.INVALID_STATE_FOR_BDF_APPROVAL);
        }

        // validate if the minimum required fields are adequately filled
        birthDeclarationValidator.validateMinimalRequirements(birth);

        // validate standard validations anyway, since even if validations are rejected a note of it will be made
        // against the approval for audit requirements
        List<UserWarning> warnings = birthDeclarationValidator.validateStandardRequirements(birthDeclarationDAO, birth, user);

        if (!warnings.isEmpty() && ignoreWarnings) {
            StringBuilder sb = new StringBuilder();
            if (birth.getRegister().getComments() != null) {
                sb.append(birth.getRegister().getComments()).append("\n");
            }

            sb.append(DateTimeUtils.getISO8601FormattedString(new Date())).append(" - Approved birth declaration ignoring warnings. User : ").
                    append(user.getUserId()).append("\n");

            for (UserWarning w : warnings) {
                sb.append(w.getSeverity());
                sb.append("-");
                sb.append(w.getMessage());
            }
            birth.getRegister().setComments(sb.toString());
        }

        if (warnings.isEmpty() || ignoreWarnings) {
            birth.getRegister().setStatus(BirthDeclaration.State.APPROVED);
            birth.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
            birth.getLifeCycleInfo().setApprovalOrRejectUser(user);
            birthDeclarationDAO.updateBirthDeclaration(birth, user);
            logger.debug("Approved live birth declaration record : {} Ignore warnings : {}", idUKey, ignoreWarnings);
        } else {
            logger.debug("Approval of live birth declaration record : {} stopped due to warnings", idUKey);
        }
        return warnings;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserWarning> approveStillBirthDeclaration(long idUkey, boolean ignoreWarnings, User user) {
        BirthDeclaration stillBirth = getById(idUkey, user);
        validateBirthType(stillBirth, BirthDeclaration.BirthType.STILL);
        logger.debug("Attempt to approve still birth declaration : {} Ignore warnings : {}", stillBirth.getIdUKey(), ignoreWarnings);

        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, stillBirth);

        // TODO check approve permission for still births
        // check approve permission
        if (!user.isAuthorized(Permission.APPROVE_BDF)) {
            handleException("User : " + user.getUserId() + " is not allowed to approve/reject still birth declarations",
                    ErrorCodes.PERMISSION_DENIED);
        }

        // is the BDF currently existing in a state for approval
        final BirthDeclaration.State currentState = stillBirth.getRegister().getStatus();
        if (BirthDeclaration.State.DATA_ENTRY != currentState) {
            handleException("Cannot approve still birth declaration : " + stillBirth.getIdUKey() + " Illegal state : " + currentState,
                    ErrorCodes.INVALID_STATE_FOR_BDF_APPROVAL);
        }

        // validate if the minimum required fields are adequately filled
        birthDeclarationValidator.validateMinimalRequirements(stillBirth);

        // validate standard validations anyway, since even if validations are rejected a note of it will be made
        // against the approval for audit requirements
        List<UserWarning> warnings = birthDeclarationValidator.validateStandardRequirements(birthDeclarationDAO, stillBirth, user);

        if (!warnings.isEmpty() && ignoreWarnings) {
            StringBuilder sb = new StringBuilder();
            if (stillBirth.getRegister().getComments() != null) {
                sb.append(stillBirth.getRegister().getComments()).append("\n");
            }

            sb.append(DateTimeUtils.getISO8601FormattedString(new Date())).append(" - Approved still birth declaration ignoring warnings. User : ").
                    append(user.getUserId()).append("\n");

            for (UserWarning w : warnings) {
                sb.append(w.getSeverity());
                sb.append("-");
                sb.append(w.getMessage());
            }
            stillBirth.getRegister().setComments(sb.toString());
        }

        if (warnings.isEmpty() || ignoreWarnings) {
            stillBirth.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CERT_GENERATED);
            stillBirth.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
            stillBirth.getLifeCycleInfo().setApprovalOrRejectUser(user);
            birthDeclarationDAO.updateBirthDeclaration(stillBirth, user);
            logger.debug("Approved still birth declaration record : {} Ignore warnings : {}", stillBirth.getIdUKey(), ignoreWarnings);
        } else {
            logger.debug("Approval of still birth declaration record : {} stopped due to warnings", stillBirth.getIdUKey());
        }
        return warnings;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserWarning> approveBelatedBirthDeclaration(long idUKey, boolean ignoreWarnings, User user) {
        //validate birth type which is send by Action
        BirthDeclaration birth = getById(idUKey, user);
        validateBirthType(birth, BirthDeclaration.BirthType.BELATED);
        logger.debug("Attempt to approve belated birth declaration : {} Ignore warnings : {}", idUKey, ignoreWarnings);
        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, birth);
        // check approve permission
        if (!user.isAuthorized(Permission.APPROVE_BDF)) {
            handleException("User : " + user.getUserId() + " is not allowed to approve/reject birth declarations",
                    ErrorCodes.PERMISSION_DENIED);
        }

        // is the BDF currently existing in a state for approval
        final BirthDeclaration.State currentState = birth.getRegister().getStatus();
        if (BirthDeclaration.State.DATA_ENTRY != currentState) {
            handleException("Cannot approve confirmation : " + idUKey + " Illegal state : " + currentState,
                    ErrorCodes.INVALID_STATE_FOR_BDF_APPROVAL);
        }

        // validate if the minimum required fields are adequately filled
        birthDeclarationValidator.validateMinimalRequirements(birth);

        // validate standard validations anyway, since even if validations are rejected a note of it will be made
        // against the approval for audit requirements
        List<UserWarning> warnings = birthDeclarationValidator.validateStandardRequirements(birthDeclarationDAO, birth, user);

        if (!warnings.isEmpty() && ignoreWarnings) {
            StringBuilder sb = new StringBuilder();
            if (birth.getRegister().getComments() != null) {
                sb.append(birth.getRegister().getComments()).append("\n");
            }

            sb.append(DateTimeUtils.getISO8601FormattedString(new Date())).append(" - Approved birth declaration ignoring warnings. User : ").
                    append(user.getUserId()).append("\n");

            for (UserWarning w : warnings) {
                sb.append(w.getSeverity());
                sb.append("-");
                sb.append(w.getMessage());
            }
            birth.getRegister().setComments(sb.toString());
        }

        if (warnings.isEmpty() || ignoreWarnings) {
            birth.getRegister().setStatus(BirthDeclaration.State.APPROVED);
            birth.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
            birth.getLifeCycleInfo().setApprovalOrRejectUser(user);
            birthDeclarationDAO.updateBirthDeclaration(birth, user);
            logger.debug("Approved belated birth declaration record : {} Ignore warnings : {}", idUKey, ignoreWarnings);
        } else {
            logger.debug("Approval of belated birth declaration record : {} stopped due to warnings", idUKey);
        }
        return warnings;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserWarning> approveAdoptionBirthDeclaration(long idUKey, boolean ignoreWarnings, User user) {
        BirthDeclaration adoption = getById(idUKey, user);
        validateBirthType(adoption, BirthDeclaration.BirthType.ADOPTION);
        logger.debug("Attempt to approve adoption birth declaration : {} Ignore warnings : {}", adoption.getIdUKey(), ignoreWarnings);

        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, adoption);

        // check approve permission
        if (!user.isAuthorized(Permission.APPROVE_BDF)) {
            handleException("User : " + user.getUserId() + " is not allowed to approve/reject birth declarations",
                    ErrorCodes.PERMISSION_DENIED);
        }

        // is the BDF currently existing in a state for approval
        final BirthDeclaration.State currentState = adoption.getRegister().getStatus();
        if (BirthDeclaration.State.DATA_ENTRY != currentState) {
            handleException("Cannot approve confirmation : " + adoption.getIdUKey() + " Illegal state : " + currentState,
                    ErrorCodes.INVALID_STATE_FOR_BDF_APPROVAL);
        }

        // validate if the minimum required fields are adequately filled
        birthDeclarationValidator.validateMinimalRequirements(adoption);

        // validate standard validations anyway, since even if validations are rejected a note of it will be made
        // against the approval for audit requirements
        List<UserWarning> warnings = birthDeclarationValidator.validateStandardRequirements(birthDeclarationDAO, adoption, user);

        if (!warnings.isEmpty() && ignoreWarnings) {
            StringBuilder sb = new StringBuilder();
            if (adoption.getRegister().getComments() != null) {
                sb.append(adoption.getRegister().getComments()).append("\n");
            }

            sb.append(DateTimeUtils.getISO8601FormattedString(new Date())).append(" - Approved birth declaration ignoring warnings. User : ").
                    append(user.getUserId()).append("\n");

            for (UserWarning w : warnings) {
                sb.append(w.getSeverity());
                sb.append("-");
                sb.append(w.getMessage());
            }
            adoption.getRegister().setComments(sb.toString());
        }

        if (warnings.isEmpty() || ignoreWarnings) {
            adoption.getRegister().setStatus(BirthDeclaration.State.APPROVED);
            adoption.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
            adoption.getLifeCycleInfo().setApprovalOrRejectUser(user);
            birthDeclarationDAO.updateBirthDeclaration(adoption, user);
            logger.debug("Approved adoption birth declaration record : {} Ignore warnings : {}", adoption.getIdUKey(), ignoreWarnings);
        } else {
            logger.debug("Approval of adoption birth declaration record : {} stopped due to warnings", adoption.getIdUKey());
        }
        return warnings;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void markLiveBirthConfirmationAsPrinted(BirthDeclaration bdf, User user) {

        validateLiveBirth(bdf);
        logger.debug("Attempt to mark confirmation printed for live birth declaration record : {}", bdf.getIdUKey());

        // load the existing record
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateLiveBirth(bdf);

        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        validateAccessOfUser(user, existing);

        existing.getRegister().setConfirmationPrintTimestamp(new Date());
        existing.getRegister().setConfirmationPrintUser(user);
        existing.getRegister().setStatus(BirthDeclaration.State.CONFIRMATION_PRINTED);
        birthDeclarationDAO.updateBirthDeclaration(existing, user);
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
    @Transactional(propagation = Propagation.REQUIRED)
    public void markLiveBirthDeclarationAsConfirmedWithoutChanges(BirthDeclaration bdf, User user) {

        validateLiveBirth(bdf);
        logger.debug("Attempt to mark birth record : {} as confirmed without changes", bdf.getIdUKey());

        // does the user have access to the BDF being confirmed (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateLiveBirth(bdf);
        validateAccessOfUser(user, existing);

        // to ensure correctness, modify the existing copy and not update to whats passed to us
        // i.e. only update the confirmant information
        existing.setConfirmant(bdf.getConfirmant());

        final BirthDeclaration.State currentState = bdf.getRegister().getStatus();
        if (BirthDeclaration.State.CONFIRMATION_PRINTED == currentState) {
            bdf.getRegister().setStatus(BirthDeclaration.State.CONFIRMED_WITHOUT_CHANGES);
            bdf.getConfirmant().setConfirmationProcessedTimestamp(new Date());
            bdf.getConfirmant().setConfirmationProcessedUser(user);
            birthDeclarationDAO.updateBirthDeclaration(bdf, user);
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
    @Transactional(propagation = Propagation.REQUIRED)
    public void captureLiveBirthConfirmationChanges(BirthDeclaration bdf, User user) {

        validateLiveBirth(bdf);
        logger.debug("Attempt to capture changes for birth record : {} ", bdf.getIdUKey());

        // does the user have access to the BDF being confirmed (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateLiveBirth(bdf);
        validateAccessOfUser(user, existing);

        final BirthDeclaration.State currentState = existing.getRegister().getStatus();
        if (BirthDeclaration.State.CONFIRMATION_PRINTED == currentState) {
            // mark existing as archived with a newer record of corrections
            existing.setConfirmant(bdf.getConfirmant());
            existing.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CORRECTED);
            existing.getLifeCycleInfo().setActiveRecord(false);
            birthDeclarationDAO.updateBirthDeclaration(existing, user);

            // add new record
            bdf.setIdUKey(0); // force addition
            bdf.getRegister().setStatus(BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED);
            bdf.getConfirmant().setConfirmationProcessedTimestamp(new Date());
            bdf.getConfirmant().setConfirmationProcessedUser(user);
            birthDeclarationDAO.addBirthDeclaration(bdf, user);
            logger.debug("Changes captured as birth record : {} and the old record : {} archived",
                    bdf.getIdUKey(), existing.getIdUKey());

        } else if (BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED == currentState) {
            bdf.getRegister().setStatus(BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED);
            birthDeclarationDAO.updateBirthDeclaration(bdf, user);
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
    @Transactional(propagation = Propagation.REQUIRED)
    public void markLiveBirthCertificateAsPrinted(BirthDeclaration bdf, User user) {

        validateBirthType(bdf, BirthDeclaration.BirthType.LIVE);
        logger.debug("Request to mark as Birth certificate printed for record : {}", bdf.getIdUKey());

        // load the existing record
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(existing, BirthDeclaration.BirthType.LIVE);

        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        validateAccessOfUser(user, existing);

        existing.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CERT_PRINTED);
        final Date originalBCDateOfIssue = new Date();
        // TODO existing.getRegister().setOriginalBCPlaceOfIssue();
        birthDeclarationDAO.updateBirthDeclaration(existing, user);

        logger.debug("Marked as Birth certificate printed for record : {}", bdf.getIdUKey());
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void markStillBirthCertificateAsPrinted(BirthDeclaration bdf, User user) {

        validateBirthType(bdf, BirthDeclaration.BirthType.STILL);
        logger.debug("Request to mark as Still Birth certificate printed for record : {}", bdf.getIdUKey());

        // load the existing record
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(existing, BirthDeclaration.BirthType.STILL);

        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        validateAccessOfUser(user, existing);

        existing.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CERT_PRINTED);
        final Date originalBCDateOfIssue = new Date();
        // TODO existing.getRegister().setOriginalBCPlaceOfIssue();
        birthDeclarationDAO.updateBirthDeclaration(existing, user);

        logger.debug("Marked as Still Birth certificate printed for record : {}", bdf.getIdUKey());
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void markAdoptionBirthCertificateAsPrinted(BirthDeclaration bdf, User user) {

        validateBirthType(bdf, BirthDeclaration.BirthType.ADOPTION);
        logger.debug("Request to mark as Adoption Birth certificate printed for record : {}", bdf.getIdUKey());

        // load the existing record
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(existing, BirthDeclaration.BirthType.ADOPTION);

        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        validateAccessOfUser(user, existing);

        existing.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CERT_PRINTED);
        final Date originalBCDateOfIssue = new Date();
        // TODO existing.getRegister().setOriginalBCPlaceOfIssue();
        birthDeclarationDAO.updateBirthDeclaration(existing, user);

        logger.debug("Marked as Adoption Birth certificate printed for record : {}", bdf.getIdUKey());
    }

    /**
     * BirthRegistrationServiceImpl
     *
     * @inheritDoc
     */
    public void markBirthCertificateIDsAsPrinted(long[] printedIDList, User user) {
        logger.debug("Request to mark as Birth certificate printed for records : {}", printedIDList);
        for (long l : printedIDList) {
            BirthDeclaration bdf = birthDeclarationDAO.getById(l);
            logger.info("BDF is still or Live : {}", bdf.getRegister().getBirthType());

            final BirthDeclaration.BirthType current = bdf.getRegister().getBirthType();
            if (BirthDeclaration.BirthType.LIVE == current) {
                markLiveBirthCertificateAsPrinted(bdf, user);
            } else if (BirthDeclaration.BirthType.STILL == current) {
                markStillBirthCertificateAsPrinted(bdf, user);
            } else if (BirthDeclaration.BirthType.ADOPTION == current) {
                markAdoptionBirthCertificateAsPrinted(bdf, user);
            }
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserWarning> approveConfirmationChanges(BirthDeclaration bdf, boolean ignoreWarnings, User user) {

        validateLiveBirth(bdf);
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
                bdf.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
                bdf.getLifeCycleInfo().setApprovalOrRejectUser(user);
                birthDeclarationDAO.updateBirthDeclaration(bdf, user);
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

        validateLiveBirth(bdf);
        // does the user have access to the BDF being confirmed (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateLiveBirth(bdf);
        validateAccessOfUser(user, existing);

        // validate if the minimum required fields are adequately filled
        birthDeclarationValidator.validateMinimalRequirements(bdf);

        // validate standard validations anyway, since even if validations are rejected a note of it will be made
        // against the approval for audit requirements
        List<UserWarning> warnings = birthDeclarationValidator.validateStandardRequirements(birthDeclarationDAO, bdf, user);

        if (!warnings.isEmpty() && ignoreWarnings) {
            StringBuilder sb = new StringBuilder();
            if (existing.getRegister().getComments() != null) {
                sb.append(existing.getRegister().getComments()).append("\n");
            }

            sb.append(DateTimeUtils.getISO8601FormattedString(new Date())).append(" - Approved birth declaration ignoring warnings. User : ").
                    append(user.getUserId()).append("\n");

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
    @Transactional(propagation = Propagation.REQUIRED)
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
            bdf.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
            bdf.getLifeCycleInfo().setApprovalOrRejectUser(user);

            birthDeclarationDAO.updateBirthDeclaration(bdf, user);
            logger.debug("Rejected birth declaration record : {} by user : {}", bdf.getIdUKey(), user.getUserId());

        } else {
            handleException("Cannot reject birth declaration / confirmation : " + bdf.getIdUKey() +
                    " Illegal state : " + currentState, ErrorCodes.INVALID_STATE_FOR_BDF_REJECTION);
        }
    }

    private void validateAccessOfUser(User user, BirthDeclaration bdf) {
        if (bdf != null) {
            BDDivision bdDivision = bdf.getRegister().getBirthDivision();
            validateAccessToBDDivision(user, bdDivision);
        }
    }

    private void validateAccessToBDDivision(User user, BDDivision bdDivision) {
        if (!(User.State.ACTIVE == user.getStatus()
                &&
                (Role.ROLE_RG.equals(user.getRole().getRoleId())
                        ||
                        (user.isAllowedAccessToBDDistrict(bdDivision.getDistrict().getDistrictUKey())
                                &&
                                user.isAllowedAccessToBDDSDivision(bdDivision.getDsDivision().getDsDivisionUKey())
                        )
                )
        )) {

            handleException("User : " + user.getUserId() + " is not allowed access to the District : " +
                    bdDivision.getDistrict().getDistrictUKey() + " and/or DS Division : " +
                    bdDivision.getDsDivision().getDsDivisionUKey(), ErrorCodes.PERMISSION_DENIED);
        }
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
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
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public BirthDeclaration getActiveRecordByBDDivisionAndSerialNo(BDDivision bdDivision, long serialNo, User user) {

        logger.debug("Get active record by BDDivision ID : {} and Serial No : {}", bdDivision.getBdDivisionUKey(), serialNo);

        BirthDeclaration bdf = birthDeclarationDAO.getActiveRecordByBDDivisionAndSerialNo(bdDivision, serialNo);
        // does the user have access to the BDF (i.e. check district and DS division)
        //calling validate access iff bdf is not null otherwise it throws null pointer exception
        if (bdf != null)
            validateAccessOfUser(user, bdf);
        return bdf;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
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
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
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
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
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
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
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
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getBelatedDeclarationApprovalPending(BDDivision bdDivision, int pageNo, int noOfRows, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get records Belated pending approval by BDDivision ID : " + bdDivision.getBdDivisionUKey()
                    + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        validateAccessToBDDivision(user, bdDivision);
        return birthDeclarationDAO.getPaginatedListForStateAndBirthType(bdDivision, pageNo, noOfRows,
                BirthDeclaration.State.DATA_ENTRY, BirthDeclaration.BirthType.BELATED);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
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
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getBelatedDeclarationPendingByBDDivisionAndRegisterDateRange(BDDivision bdDivision,
                                                                                               Date startDate, Date endDate, int pageNo, int noOfRows, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get Belated records pending approval by BDDivision ID : " + bdDivision.getBdDivisionUKey() +
                    " and date range : " + startDate + " to " + endDate + " Page : " + pageNo +
                    " with number of rows per page : " + noOfRows);
        }
        validateAccessToBDDivision(user, bdDivision);
        return birthDeclarationDAO.getByBDDivisionStatusBirthTypeAndRegisterDateRange(
                bdDivision, BirthDeclaration.State.DATA_ENTRY, BirthDeclaration.BirthType.BELATED, startDate, endDate,
                pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getByBDDivisionStatusAndConfirmationReceiveDateRange(BDDivision bdDivision,
                                                                                       Date startDate, Date endDate, int pageNo, int noOfRows, User user) {

        if (logger.isDebugEnabled()) {
            logger.debug("Get confirmation records pending approval by BDDivision ID : " +
                    bdDivision.getBdDivisionUKey() + " and date range : " + startDate + " to " + endDate +
                    " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        validateAccessToBDDivision(user, bdDivision);
        return birthDeclarationDAO.getByBDDivisionStatusAndConfirmationReceiveDateRange(
                bdDivision, startDate, endDate, pageNo, noOfRows);
    }

    /**
     * Populates transient string values for Country, Race, District, Division etc
     *
     * @param bdf the BirthDeclaration to populate transient values
     * @return populated BDF
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public BirthDeclaration loadValuesForPrint(BirthDeclaration bdf, User user) {

        logger.debug("Loading record : {} for printing", bdf.getIdUKey());
        validateAccessOfUser(user, bdf);
        String prefLanguage = bdf.getRegister().getPreferredLanguage();

        ChildInfo child = bdf.getChild();
        child.setChildGenderPrint(GenderUtil.getGender(bdf.getChild().getChildGender(), prefLanguage));
        logger.debug("check for certificate");
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

        MarriageInfo marriage = bdf.getMarriage();
        if (marriage != null) {
            marriage.setParentsMarriedPrint(MarriedStatusUtil.getMarriedStatus(marriage.getParentsMarried(), prefLanguage));
        }

        return bdf;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getByBirthDivision(BDDivision bdDivision, User user) {
        logger.debug("Get records birthDivision ID : {}", bdDivision.getBdDivisionUKey());
        return birthDeclarationDAO.getByBirthDivision(bdDivision);
    }

    /**
     * Generates a PIN and adds the record to the PRS
     */
    @Transactional(propagation = Propagation.REQUIRED)
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
            Person mother = processMotherToPRS(user, child, parent, bdf.getRegister().getPreferredLanguage());
            processFatherToPRS(user, child, parent, bdf.getRegister().getPreferredLanguage(), mother, bdf.getMarriage());
        }

        // generate a PIN number
        long pin = ecivil.addPerson(child, user);
        childInfo.setPin(pin);
        bdf.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CERT_GENERATED);

        logger.debug("Generated PIN for record IDUKey : {} issued PIN : {}", bdf.getIdUKey(), pin);
        bdf.getLifeCycleInfo().setCertificateGeneratedTimestamp(new Date());
        bdf.getLifeCycleInfo().setCertificateGeneratedUser(user);
        birthDeclarationDAO.updateBirthDeclaration(bdf, user);

        // index record
        birthRecordsIndexer.add(bdf);

        return warnings;
    }

    private Person processMotherToPRS(User user, Person person, ParentInfo parent, String prefLanguage) {

        String motherNICorPIN = parent.getMotherNICorPIN();
        logger.debug("Processing details of the mother for NIC/PIN : {}", motherNICorPIN);

        Person mother = null;
        if (motherNICorPIN != null) {

            try {
                long pin = Long.parseLong(motherNICorPIN);
                mother = ecivil.findPersonByPIN(pin, user);
                if (mother != null) {
                    logger.debug("Found mother by PIN : {}", pin);
                }
            } catch (NumberFormatException ignore) {
                // this could be an NIC
                List<Person> records = ecivil.findPersonsByNIC(motherNICorPIN, user);
                if (records != null) {
                    if (records.size() == 1) {
                        logger.debug("Found mother by INC : {}", records.get(0).getNic());
                        mother = records.get(0);
                    } else if (records.size() > 1) {
                        logger.debug("Could not locate a unique mother record using : {}", motherNICorPIN);
                        return null;
                    }
                }
            }

            if (mother == null && parent.getMotherFullName() != null) {
                // if we couldn't locate the mother, add an unverified record to the PRS
                mother = new Person();
                mother.setFullNameInOfficialLanguage(parent.getMotherFullName());
                mother.setDateOfBirth(parent.getMotherDOB());
                mother.setGender(AppConstants.Gender.FEMALE.ordinal());
                mother.setPreferredLanguage(prefLanguage);
                mother.setNic(motherNICorPIN);
                mother.setStatus(Person.Status.UNVERIFIED);
                mother.setLifeStatus(Person.LifeStatus.ALIVE);

                if (parent.getMotherAddress() != null) {
                    mother.specifyAddress(new Address(parent.getMotherAddress()));
                }

                ecivil.addPerson(mother, user);
                logger.debug("Added an unverified record for the mother into the PRS : {}", mother.getPersonUKey());
            }

            // mark mother child relationship
            if (mother != null) {
                person.setMother(mother);
            }
        }
        return mother;
    }

    private void processFatherToPRS(User user, Person person, ParentInfo parent, String prefLanguage,
        Person mother, MarriageInfo marriage) {

        String fatherNICorPIN = parent.getFatherNICorPIN();
        if (fatherNICorPIN != null) {
            Person father = null;
            try {
                long pin = Long.parseLong(fatherNICorPIN);
                father = ecivil.findPersonByPIN(pin, user);
                if (father != null) {
                    logger.debug("Found father by PIN : {}", pin);
                }
            } catch (NumberFormatException ignore) {
                // this could be an NIC
                List<Person> records = ecivil.findPersonsByNIC(fatherNICorPIN, user);
                if (records != null && records.size() == 1) {
                    logger.debug("Found father by NIC : {}", records.get(0).getNic());
                    father = records.get(0);
                } else if (records.size() > 1) {
                    logger.debug("Could not locate a unique father record using : {}", fatherNICorPIN);
                }
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
                father.setNic(fatherNICorPIN);

                ecivil.addPerson(father, user);
                logger.debug("Added an unverified record for the father into the PRS : {}", father.getPersonUKey());
            }

            // set father child relationship
            if (father != null) {
                person.setFather(father);
                
                if (mother != null && marriage.getDateOfMarriage() != null) {
                    Marriage m = new Marriage();
                    m.setBride(mother);
                    m.setGroom(father);
                    m.setDateOfMarriage(marriage.getDateOfMarriage());
                    m.setPlaceOfMarriage(marriage.getPlaceOfMarriage());
                    m.setState(Marriage.State.MARRIED);
                    father.specifyMarriage(m);
                    mother.specifyMarriage(m);

                    if (mother.getLastAddress() != null) {
                        father.specifyAddress(mother.getLastAddress());
                    }
                }
            }
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER)
    public void triggerScheduledJobs() {
        logger.info("Start executing Birth registration related scheduled tasks..");

        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.DATE, -1 * appParametersDAO.getIntParameter(AppParameter.CRS_AUTO_CONFIRMATION_DAYS));

        User systemUser = userManager.getSystemUser();
        List<BirthDeclaration> list = birthDeclarationDAO.getUnconfirmedByRegistrationDate(cal.getTime());
        for (BirthDeclaration bdf : list) {
            try {
                //todo
                //bdf.getLifeCycleInfo().setCertificateGeneratedTimestamp(new Date());
                //bdf.getLifeCycleInfo().setCertificateGeneratedUser(user);
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

    private static final boolean isEmptyString(String s) {
        return s == null || s.trim().length() == 0;
    }

    private void validateBirthType(BirthDeclaration bdf, BirthDeclaration.BirthType birthType) {
        if (birthType != bdf.getRegister().getBirthType()) {
            handleException("Live birth : " + bdf.getRegister().getBirthType() + ", BDF : " + bdf.getIdUKey() +
                    " in invalid context", ErrorCodes.ILLEGAL_STATE);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("bith type checking for BDF : " + bdf.getIdUKey() + " passed for birth type as : " +
                    bdf.getRegister().getBirthType());
        }
    }

    private void validateLiveBirth(BirthDeclaration bdf) {
        final BirthDeclaration.BirthType current = bdf.getRegister().getBirthType();
        if (BirthDeclaration.BirthType.LIVE != current && BirthDeclaration.BirthType.ADOPTION != current
            && BirthDeclaration.BirthType.BELATED != current) {
            handleException("Live birth : " + current + ", BDF : " + bdf.getIdUKey() + " in invalid context",
                    ErrorCodes.ILLEGAL_STATE);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("bith type checking for BDF : {}  passed for birth type as : {}", bdf.getIdUKey(), current);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> performBirthCertificateSearch(BirthCertificateSearch bcs, User user) {

        logger.debug("Birth certificate search started");

        List<BirthDeclaration> results = new ArrayList<BirthDeclaration>();
        BirthDeclaration exactRecord = null;
        BirthCertificateSearch existing = null;

        if (bcs.getApplicationNo() != null && bcs.getDsDivision() != null) {
            existing = bcSearchDAO.getByDSDivisionAndSerialNo(bcs.getDsDivision(), bcs.getApplicationNo());
        }

        if (existing == null) {
            if (bcs.getCertificateNo() != null) {
                logger.debug("Search narrowed against certificate IDUKey : {}", bcs.getCertificateNo());
                exactRecord = birthDeclarationDAO.getById(bcs.getCertificateNo());
                if (exactRecord != null) {
                    final BirthDeclaration.State currentState = exactRecord.getRegister().getStatus();
                    if (BirthDeclaration.State.ARCHIVED_CERT_GENERATED == currentState ||
                            BirthDeclaration.State.ARCHIVED_CERT_PRINTED == currentState) {
                        results = new ArrayList<BirthDeclaration>();
                        results.add(exactRecord);
                    }
                }
            }

            // add any matches from Solr search, except for the exact match
            for (BirthDeclaration bdf : birthRecordsIndexer.searchBirthRecords(bcs)) {
                if (exactRecord == null || exactRecord.getIdUKey() != bdf.getIdUKey()) {
                    results.add(bdf);
                }
            }

            // set user perform searching and the timestamp
            bcs.setSearchUser(user);
            bcs.setSearchPerformDate(new Date());
            bcs.setResultsFound(results.size());

            bcSearchDAO.addBirthCertificateSearch(bcs);
            logger.debug("Birth certificate search completed and recorded as SearchUKey : {} Results found : {}",
                    bcs.getSearchUKey(), results.size());

        } else {
            handleException("The birth certificate search DS Division/Application number is a duplicate : " +
                    bcs.getDsDivision().getDsDivisionUKey() + " " + bcs.getApplicationNo(), ErrorCodes.INVALID_DATA);
        }

        return results;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getArchivedCorrectedEntriesForGivenSerialNo(BDDivision bdDivision, long serialNo, User user) {
        logger.debug("Searching for historical records for BD Division : {} and Serial number : {} ",
                bdDivision.getBdDivisionUKey(), serialNo);
        validateAccessToBDDivision(user, bdDivision);
        return birthDeclarationDAO.getHistoricalRecordsForBDDivisionAndSerialNo(bdDivision, serialNo);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getDeclarationApprovalPendingByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, User user) {
        validateAccessToDSDivison(dsDivision, user);
        return birthDeclarationDAO.getPaginatedListForStateByDSDivision(dsDivision, pageNo, noOfRows,
                BirthDeclaration.State.DATA_ENTRY);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getBelatedDeclarationApprovalPendingByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, User user) {
        validateAccessToDSDivison(dsDivision, user);
        return birthDeclarationDAO.getPaginatedListForStateAndBirthTypeByDSDivision(dsDivision, pageNo, noOfRows,
                BirthDeclaration.State.DATA_ENTRY, BirthDeclaration.BirthType.BELATED);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getDeclarationPendingByDSDivisionAndRegisterDateRange(DSDivision dsDivision, Date startDate,
                                                                                        Date endDate, int pageNo, int noOfRows, User user) {
        validateAccessToDSDivison(dsDivision, user);
        return birthDeclarationDAO.getByDSDivisionStatusAndRegisterDateRange(dsDivision, BirthDeclaration.State.DATA_ENTRY,
                startDate, endDate, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getBelatedDeclarationPendingByDSDivisionAndRegisterDateRange(DSDivision dsDivision,
                                                                                               Date startDate, Date endDate, int pageNo, int noOfRows, User user) {
        validateAccessToDSDivison(dsDivision, user);
        return birthDeclarationDAO.getByDSDivisionStatusBirthTypeAndRegisterDateRange(dsDivision,
                BirthDeclaration.State.DATA_ENTRY, BirthDeclaration.BirthType.BELATED, startDate, endDate, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getConfirmationApprovalPendingByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get confirmations pending approval by DSDivision ID : " + dsDivision.getDsDivisionUKey()
                    + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        validateAccessToDSDivison(dsDivision, user);
        return birthDeclarationDAO.getPaginatedListForStateByDSDivision(
                dsDivision, pageNo, noOfRows, BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED);
    }

    private void validateAccessToDSDivison(DSDivision dsDivision, User user) {
        if (!(User.State.ACTIVE == user.getStatus() &&
                (Role.ROLE_RG.equals(user.getRole().getRoleId())
                        || (user.isAllowedAccessToBDDistrict(dsDivision.getDistrict().getDistrictUKey()))
                        || (user.isAllowedAccessToBDDSDivision(dsDivision.getDsDivisionUKey()))
                )
        )) {
            handleException("User : " + user.getUserId() + " is not allowed access to the District : " +
                    dsDivision.getDistrictId(), ErrorCodes.PERMISSION_DENIED);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getByDSDivisionStatusAndConfirmationReceiveDateRange(DSDivision dsDivision, Date startDate, Date endDate, int pageNo, int noOfRows, User user) {

        if (logger.isDebugEnabled()) {
            logger.debug("Get confirmation records pending approval by DSDivision ID : " +
                    dsDivision.getDsDivisionUKey() + " and date range : " + startDate + " to " + endDate +
                    " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        validateAccessToDSDivison(dsDivision, user);
        //setting the time of the endDate to the current time
        Date d = new Date();
        endDate.setHours(d.getHours());
        endDate.setMinutes(d.getMinutes());
        return birthDeclarationDAO.getByDSDivisionStatusAndConfirmationReceiveDateRange(dsDivision, startDate, endDate, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getConfirmationPrintListByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, boolean printed, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get records pending confirmation printing by DSDivision ID : " +
                    dsDivision.getDsDivisionUKey() + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        validateAccessToDSDivison(dsDivision, user);
        return birthDeclarationDAO.getPaginatedListForStateByDSDivision(dsDivision, pageNo, noOfRows,
                printed ? BirthDeclaration.State.CONFIRMATION_PRINTED : BirthDeclaration.State.APPROVED);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getBirthCertificatePrintListByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, boolean printed, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get birth certificate list print by DSDivision ID : " +
                    dsDivision.getDsDivisionUKey() + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        validateAccessToDSDivison(dsDivision, user);
        return birthDeclarationDAO.getPaginatedListForStateByDSDivision(dsDivision, pageNo, noOfRows,
                printed ? BirthDeclaration.State.ARCHIVED_CERT_PRINTED : BirthDeclaration.State.ARCHIVED_CERT_GENERATED);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getByDSDivision(DSDivision dsDivision, User user) {
        logger.debug("Get records belonging to DSDivision ID : {}", dsDivision.getDsDivisionUKey());
        validateAccessToDSDivison(dsDivision, user);
        return birthDeclarationDAO.getByDSDivision(dsDivision);
    }

    /**
     * @inheritDoc
     */
    public BirthDeclaration getByIdForAdoptionLookup(long bdId, User user) {
        //todo if need validations has to be done
        logger.debug("getting BirthDeclaration for the idUKey : {}", bdId);
        return birthDeclarationDAO.getById(bdId);
    }

    public BirthDeclaration getByPINorNIC(long PINorNIC, User user) {
        logger.debug("Get active record by NIC or Pin number : {}", PINorNIC);
        BirthDeclaration bdf = birthDeclarationDAO.getByPINorNIC(PINorNIC);
        //calling validate access iff bdf is not null otherwise it throws null pointer exception
        if (bdf != null)
            validateAccessOfUser(user, bdf);
        return bdf;
    }
}
