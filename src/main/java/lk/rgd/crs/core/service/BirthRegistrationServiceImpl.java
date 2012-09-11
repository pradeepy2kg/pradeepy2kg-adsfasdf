package lk.rgd.crs.core.service;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.*;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.common.util.IdentificationNumberUtil;
import lk.rgd.common.util.MarriedStatusUtil;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.AdoptionOrderDAO;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.dao.GNDivisionDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.AdoptionOrderService;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.core.ValidationUtils;
import lk.rgd.prs.api.domain.Address;
import lk.rgd.prs.api.domain.Marriage;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.domain.PersonCitizenship;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * The central service managing the CRS Birth Registration process
 */
public class BirthRegistrationServiceImpl implements BirthRegistrationService {

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
    private final AdoptionOrderDAO adoptionOrderDAO;
    private final BirthDeclarationValidator birthDeclarationValidator;
    private final AdoptionOrderService adoptionOrderService;
    private final GNDivisionDAO gnDivisionDAO;

    public BirthRegistrationServiceImpl(
        BirthDeclarationDAO birthDeclarationDAO, DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO,
        BDDivisionDAO bdDivisionDAO, CountryDAO countryDAO, RaceDAO raceDAO, PopulationRegistry ecivil,
        AppParametersDAO appParametersDAO, UserManager userManager, BirthRecordsIndexer birthRecordsIndexer,
        AdoptionOrderDAO adoptionOrderDAO, BirthDeclarationValidator birthDeclarationValidator,
        AdoptionOrderService adoptionOrderService, GNDivisionDAO gnDivisionDAO) {
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
        this.adoptionOrderDAO = adoptionOrderDAO;
        this.birthDeclarationValidator = birthDeclarationValidator;
        this.adoptionOrderService = adoptionOrderService;
        this.gnDivisionDAO = gnDivisionDAO;
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
        addBirthDeclaration(bdf, ignoreWarnings, user);
        //updating adoption record

        AdoptionOrder order = adoptionOrderService.getById(bdf.getRegister().getAdoptionUKey(), user);
        if (order != null) {
            order.setStatus(AdoptionOrder.State.RE_REGISTRATION_REQUESTED);
            adoptionOrderService.updateAdoptionOrder(order, user);
        } else {
            handleException("Unable to update adoption order while adding re registration of birth decleration " +
                "for adoption order idUKey " + bdf.getRegister().getAdoptionUKey(),
                ErrorCodes.UNABLE_TO_UPDATE_ADOPTION_ORDER);
        }
        // TODO adoption specific validations
/*
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
        }*/

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
            //updating adoption order
            updatingAdoptionOrder(bdf, user);
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
    public List<UserWarning> approveStillBirthDeclaration(long idUKey, boolean ignoreWarnings, User user) {
        BirthDeclaration stillBirth = getById(idUKey, user);
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
        if (!user.isAuthorized(Permission.APPROVE_BDF_BELATED)) {
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
            //updating corresponding adoption order and clear any other existing registrations
            AdoptionOrder adoptionOrder = adoptionOrderService.getById(adoption.getRegister().getAdoptionUKey(), user);
            adoptionOrder.setStatus(AdoptionOrder.State.RE_REGISTERED);
            adoptionOrder.setBirthCertificateNumber(adoption.getIdUKey());
            adoptionOrder.setBirthDivisionId(adoption.getRegister().getBirthDivision().getBdDivisionUKey());
            adoptionOrderService.updateAdoptionOrder(adoptionOrder, user);
            logger.debug("Approved adoption birth declaration record : {} Ignore warnings : {}", adoption.getIdUKey(), ignoreWarnings);
        } else {
            logger.debug("Approval of adoption birth declaration record : {} stopped due to warnings", adoption.getIdUKey());
        }
        return warnings;
    }

    private void clearingExistingAdoptionReRegistrations(AdoptionOrder adoptionOrder) {

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

        logger.debug("Request to mark as Birth certificate printed for record : {}", bdf.getIdUKey());
        validateBirthType(bdf, BirthDeclaration.BirthType.LIVE);
        if (!user.isAuthorized(Permission.MARK_BIRTH_CERT_PRINTED)) {
            handleException("User : " + user.getUserId() + " is not allowed to mark Live Birth Certificate as printed",
                ErrorCodes.PERMISSION_DENIED);
        }
        // validate access for certificate issue user to the certificate issuing location
        ValidationUtils.validateAccessToLocation(bdf.getRegister().getOriginalBCPlaceOfIssue(),
            bdf.getRegister().getOriginalBCIssueUser());

        // load the existing record
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(existing, BirthDeclaration.BirthType.LIVE);

        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        validateAccessOfUser(user, existing);

        existing.getRegister().setOriginalBCPlaceOfIssue(bdf.getRegister().getOriginalBCPlaceOfIssue());
        existing.getRegister().setOriginalBCIssueUser(bdf.getRegister().getOriginalBCIssueUser());
        existing.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CERT_PRINTED);
        birthDeclarationDAO.updateBirthDeclaration(existing, user);

        logger.debug("Marked as Birth certificate printed for record : {}", bdf.getIdUKey());
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void markStillBirthCertificateAsPrinted(BirthDeclaration bdf, User user) {

        logger.debug("Request to mark as Still Birth certificate printed for record : {}", bdf.getIdUKey());
        validateBirthType(bdf, BirthDeclaration.BirthType.STILL);
        if (!user.isAuthorized(Permission.MARK_BIRTH_CERT_PRINTED)) {
            handleException("User : " + user.getUserId() + " is not allowed to mark Still Birth Certificate as printed",
                ErrorCodes.PERMISSION_DENIED);
        }
        // validate access for certificate issue user to the certificate issuing location
        ValidationUtils.validateAccessToLocation(bdf.getRegister().getOriginalBCPlaceOfIssue(),
            bdf.getRegister().getOriginalBCIssueUser());

        // load the existing record
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(existing, BirthDeclaration.BirthType.STILL);

        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        validateAccessOfUser(user, existing);

        existing.getRegister().setOriginalBCPlaceOfIssue(bdf.getRegister().getOriginalBCPlaceOfIssue());
        existing.getRegister().setOriginalBCIssueUser(bdf.getRegister().getOriginalBCIssueUser());
        existing.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CERT_PRINTED);
        birthDeclarationDAO.updateBirthDeclaration(existing, user);

        logger.debug("Marked as Still Birth certificate printed for record : {}", bdf.getIdUKey());
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void markBelatedBirthCertificateAsPrinted(BirthDeclaration bdf, User user) {

        logger.debug("Request to mark as Birth certificate printed for belated record : {}", bdf.getIdUKey());
        validateBirthType(bdf, BirthDeclaration.BirthType.BELATED);
        if (!user.isAuthorized(Permission.MARK_BIRTH_CERT_PRINTED)) {
            handleException("User : " + user.getUserId() + " is not allowed to mark Belated Birth Certificate as printed",
                ErrorCodes.PERMISSION_DENIED);
        }
        // validate access for certificate issue user to the certificate issuing location
        ValidationUtils.validateAccessToLocation(bdf.getRegister().getOriginalBCPlaceOfIssue(),
            bdf.getRegister().getOriginalBCIssueUser());

        // load the existing record
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(existing, BirthDeclaration.BirthType.BELATED);

        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        validateAccessOfUser(user, existing);

        existing.getRegister().setOriginalBCPlaceOfIssue(bdf.getRegister().getOriginalBCPlaceOfIssue());
        existing.getRegister().setOriginalBCIssueUser(bdf.getRegister().getOriginalBCIssueUser());
        existing.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CERT_PRINTED);
        birthDeclarationDAO.updateBirthDeclaration(existing, user);

        logger.debug("Marked as Birth certificate printed for belated record : {}", bdf.getIdUKey());
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void markAdoptionBirthCertificateAsPrinted(BirthDeclaration bdf, User user) {

        logger.debug("Request to mark as Adoption Birth certificate printed for record : {}", bdf.getIdUKey());
        validateBirthType(bdf, BirthDeclaration.BirthType.ADOPTION);
        if (!user.isAuthorized(Permission.MARK_BIRTH_CERT_PRINTED)) {
            handleException("User : " + user.getUserId() + " is not allowed to mark Adoption Birth Certificate as printed",
                ErrorCodes.PERMISSION_DENIED);
        }
        // validate access for certificate issue user to the certificate issuing location
        ValidationUtils.validateAccessToLocation(bdf.getRegister().getOriginalBCPlaceOfIssue(),
            bdf.getRegister().getOriginalBCIssueUser());

        // load the existing record
        BirthDeclaration existing = birthDeclarationDAO.getById(bdf.getIdUKey());
        validateBirthType(existing, BirthDeclaration.BirthType.ADOPTION);

        // does the user have access to the BDF being added (i.e. check district and DS division)
        validateAccessOfUser(user, bdf);
        // does the user have access to the existing BDF (if district and division is changed somehow)
        validateAccessOfUser(user, existing);

        existing.getRegister().setOriginalBCPlaceOfIssue(bdf.getRegister().getOriginalBCPlaceOfIssue());
        existing.getRegister().setOriginalBCIssueUser(bdf.getRegister().getOriginalBCIssueUser());
        existing.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CERT_PRINTED);
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
        } else {
            if (bdf.getRegister().getComments() == null) {
                bdf.getRegister().setComments("REJECTED\n" + comments);
            } else {
                bdf.getRegister().setComments(bdf.getRegister().getComments() + "\nREJECTED\n" + comments);
            }
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
            bdf.getRegister().setBdfSerialNo(changeSerialNo(bdf.getRegister().getBdfSerialNo()));
            bdf.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
            bdf.getLifeCycleInfo().setApprovalOrRejectUser(user);

            birthDeclarationDAO.updateBirthDeclaration(bdf, user);
            updatingAdoptionOrder(bdf, user);
            logger.debug("Rejected birth declaration record : {} by user : {}", bdf.getIdUKey(), user.getUserId());

        } else {
            handleException("Cannot reject birth declaration / confirmation : " + bdf.getIdUKey() +
                " Illegal state : " + currentState, ErrorCodes.INVALID_STATE_FOR_BDF_REJECTION);
        }
    }

    private Long changeSerialNo(Long serialNo) {
        logger.debug("Attempt to change the serial : {} to {}", serialNo, (serialNo + 800000));
        return (serialNo + 800000);
    }

    private void updatingAdoptionOrder(BirthDeclaration birthDeclaration, User user) {
        //if we rejecting adoption we have to change the adoption order state for prev
        if (birthDeclaration.getRegister().getBirthType() == BirthDeclaration.BirthType.ADOPTION) {
            logger.debug("attempt to change state of the adoption order : {} ,birth record idUKey : {}",
                birthDeclaration.getRegister().getAdoptionUKey(), birthDeclaration.getIdUKey());
            try {
                AdoptionOrder adoptionOrder = adoptionOrderService.getById(birthDeclaration.getRegister().getAdoptionUKey(), user);
                adoptionOrder.setStatus(AdoptionOrder.State.ADOPTION_CERTIFICATE_PRINTED);
                adoptionOrderService.updateAdoptionOrder(adoptionOrder, user);
            } catch (CRSRuntimeException e) {
                handleException("Cannot update adoption order idUKey : " +
                    birthDeclaration.getRegister().getAdoptionUKey() + " birth record idUKey :" +
                    birthDeclaration.getIdUKey(), ErrorCodes.UNABLE_TO_UPDATE_ADOPTION_ORDER);
            }
        }
    }

    private void validateAccessOfUser(User user, BirthDeclaration bdf) {
        if (bdf != null) {
            BDDivision bdDivision = bdf.getRegister().getBirthDivision();
            ValidationUtils.validateAccessToBDDivision(user, bdDivision);
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

    @Transactional(propagation = Propagation.SUPPORTS)
    public BirthDeclaration getById(long bdId) {
        logger.debug("Load birth declaration record : {}", bdId);
        BirthDeclaration bdf = birthDeclarationDAO.getById(bdId);
        // does the user have access to the BDF (i.e. check district and DS division)
        return bdf;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public BirthDeclaration getWithRelationshipsById(long bdId, User user) {
        logger.debug("Load birth declaration record : {}", bdId);
        BirthDeclaration bdf = birthDeclarationDAO.getById(bdId);
        // does the user have access to the BDF (i.e. check district and DS division)
        //trigger lazy loader handler by calling this lazy loading object
        bdf.getLifeCycleInfo().getApprovalOrRejectUser().getUserName();
        logger.debug(bdf.getLifeCycleInfo().getApprovalOrRejectUser().getUserName());
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
        validateAccessOfUser(user, bdf);
        return bdf;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getActiveRecordsBySerialNoAndDSDivision(long serialNo, User user, BirthDeclaration.State state) {
        return birthDeclarationDAO.getActiveRecordsBySerialNoAndDSDivision(serialNo, state, user.getPrefBDDSDivision());
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
        ValidationUtils.validateAccessToBDDivision(user, bdDivision);
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
        ValidationUtils.validateAccessToBDDivision(user, bdDivision);
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
        ValidationUtils.validateAccessToBDDivision(user, bdDivision);
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
        ValidationUtils.validateAccessToBDDivision(user, bdDivision);
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
        ValidationUtils.validateAccessToBDDivision(user, bdDivision);
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
        ValidationUtils.validateAccessToBDDivision(user, bdDivision);
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
        ValidationUtils.validateAccessToBDDivision(user, bdDivision);
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
        ValidationUtils.validateAccessToBDDivision(user, bdDivision);
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
        String prefLanguage = bdf.getRegister().getPreferredLanguage();

        ChildInfo child = bdf.getChild();
        child.setChildGenderPrint(GenderUtil.getGender(bdf.getChild().getChildGender(), prefLanguage));
        logger.debug("check for certificate");
        BirthRegisterInfo brInfo = bdf.getRegister();

        if (brInfo.getOriginalBCPlaceOfIssue() != null && brInfo.getOriginalBCIssueUser() != null &&
            BirthDeclaration.State.ARCHIVED_CERT_PRINTED == brInfo.getStatus()) {
            brInfo.setOriginalBCPlaceOfIssuePrint(brInfo.getOriginalBCPlaceOfIssue().getLocationName(prefLanguage));
            brInfo.setOriginalBCPlaceOfIssueSignPrint(brInfo.getOriginalBCPlaceOfIssue().getLocationSignature(prefLanguage));
            brInfo.setOriginalBCIssueUserSignPrint(brInfo.getOriginalBCIssueUser().getUserSignature(prefLanguage));

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
            if (parent.getMotherGNDivision() != null) {
                parent.setMotherGNDivisionPrint(gnDivisionDAO.
                    getNameByPK(parent.getMotherGNDivision().getGnDivisionUKey(), prefLanguage));
            }
        }

        MarriageInfo marriage = bdf.getMarriage();
        if (marriage != null) {
            if (marriage.getParentsMarried() != null) {
                marriage.setParentsMarriedPrint(MarriedStatusUtil.getMarriedStatus(marriage.getParentsMarried(), prefLanguage));
            }
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
        }

        if (!isEmptyString(childInfo.getChildFullNameOfficialLang())) {
            child.setFullNameInOfficialLanguage(childInfo.getChildFullNameOfficialLang());
        }

        child.setDateOfBirth(childInfo.getDateOfBirth());
        child.setPlaceOfBirth(childInfo.getPlaceOfBirth());
        child.setCivilStatus(Person.CivilStatus.NEVER_MARRIED);
        child.setGender(childInfo.getChildGender());
        child.setLifeStatus(Person.LifeStatus.ALIVE);
        child.setStatus(Person.Status.VERIFIED);
        child.setPreferredLanguage(bdf.getRegister().getPreferredLanguage());
        child.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
        child.getLifeCycleInfo().setApprovalOrRejectUser(userManager.getSystemUser());

        // check mother and father
        final ParentInfo parent = bdf.getParent();
        Person mother = null;
        Person father = null;

        if (parent != null) {
            logger.debug("Processing mother of child : {} for BDF UKey : {}", bdf.getIdUKey());
            mother = processMotherToPRS(user, child, parent, bdf.getRegister().getPreferredLanguage());

            logger.debug("Processing father of child : {} for BDF UKey : {}", bdf.getIdUKey());
            father = processFatherToPRS(user, child, parent, bdf.getRegister().getPreferredLanguage(),
                mother, bdf.getMarriage(), bdf.getInformant());

            // child inherits fathers race if a father exists, else mothers if a mother exists
            if (!isEmptyString(parent.getFatherFullName()) && parent.getFatherRace() != null) {
                child.setRace(parent.getFatherRace());
            } else if (parent.getMotherRace() != null) {
                if (parent.getMotherRace() != null) {
                    child.setRace(parent.getMotherRace());
                }
            }
        }

        final GrandFatherInfo gfInfo = bdf.getGrandFather();
        if (gfInfo != null) {

            logger.debug("Processing great grand father of child : {} for BDF UKey : {}", bdf.getIdUKey());
            Person greatGrandFather = processPersonToPRS(
                gfInfo.getGreatGrandFatherNICorPIN(), gfInfo.getGreatGrandFatherFullName(),
                gfInfo.getGreatGrandFatherBirthPlace(), null, user);
            if (greatGrandFather != null && greatGrandFather.getPin() != null) {
                gfInfo.setGreatGrandFatherNICorPIN(greatGrandFather.getPin().toString());
            }

            Person grandFather = processPersonToPRS(
                gfInfo.getGrandFatherNICorPIN(), gfInfo.getGrandFatherFullName(),
                gfInfo.getGrandFatherBirthPlace(), null, user);
            if (grandFather != null && grandFather.getPin() != null) {
                gfInfo.setGrandFatherNICorPIN(grandFather.getPin().toString());
            }

            if (grandFather != null) {
                logger.debug("Processing great/grand father of child : {} for BDF UKey : {}", bdf.getIdUKey());

                // TODO use an enumeration for marriage
                if (bdf.getMarriage().getParentsMarried() != null &&
                    (bdf.getMarriage().getParentsMarried() == MarriageInfo.MarriedStatus.MARRIED ||
                        bdf.getMarriage().getParentsMarried() == MarriageInfo.MarriedStatus.NO_SINCE_MARRIED) &&
                    father != null) {
                    // grand father of child is fathers, father
                    father.setFather(grandFather);
                    ecivil.updatePerson(father, user);
                } else if (mother != null) {
                    // grand father of child is mothers father
                    mother.setFather(grandFather);
                    ecivil.updatePerson(mother, user);
                }

                if (greatGrandFather != null) {
                    grandFather.setFather(greatGrandFather);
                    ecivil.updatePerson(grandFather, user);
                }
            }
        }

        // generate a PIN number
        long pin = ecivil.addPerson(child, user);
        // setting child address
        if (mother != null && parent.getMotherAddress() != null) {
            final Address address = new Address(parent.getMotherAddress());
            address.setPermanent(true);
            child.specifyAddress(address);
            // save new address to PRS
            ecivil.addAddress(address, user);
            // update mother to reflect new address
            ecivil.updatePerson(child, user);
        }

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

    /**
     * Process the Grand father, Great grand father, Informant (if a Guardian) or the Registrar into the PRS
     *
     * @param nicOrPIN   nic of the person. This method ignores those using PIN numbers
     * @param fullName   full name of the person to be added
     * @param birthPlace place of birth
     * @param address    address if known
     * @param user       the user processing the transaction
     * @return the Person added, if any or null
     */
    private Person processPersonToPRS(String nicOrPIN, String fullName, String birthPlace, String address, User user) {

        if (IdentificationNumberUtil.isValidNIC(nicOrPIN)) {
            List<Person> records = ecivil.findPersonsByNIC(nicOrPIN, user);

            if ((records == null || records.isEmpty()) && !isEmptyString(fullName)) {
                logger.debug("Adding person with NIC : {} to the PRS", nicOrPIN);

                Person person = new Person();
                person.setFullNameInOfficialLanguage(fullName);
                person.setNic(nicOrPIN);
                person.setGender(AppConstants.Gender.MALE.ordinal());
                if (birthPlace != null) {
                    person.setPlaceOfBirth(birthPlace);
                }
                person.setStatus(Person.Status.SEMI_VERIFIED);
                // add person to PRS
                ecivil.addPerson(person, user);

                if (address != null) {
                    final Address add = new Address(address);
                    person.specifyAddress(add);
                    // save new address to PRS
                    ecivil.addAddress(add, user);
                    // update person to reflect new address
                    ecivil.updatePerson(person, user);
                }

                return person;
            }
        }
        return null;
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
                        logger.debug("Found mother by NIC : {}", records.get(0).getNic());
                        mother = records.get(0);
                    } else if (records.size() > 1) {
                        logger.debug("Could not locate a unique mother record using : {}", motherNICorPIN);
                        return null;
                    }
                }
            }
        }

        if (mother == null && parent.getMotherFullName() != null) {
            // if we couldn't locate the mother, add an unverified record to the PRS
            mother = new Person();
            mother.setFullNameInOfficialLanguage(parent.getMotherFullName());
            mother.setFullNameInEnglishLanguage(parent.getMotherFullNameInEnglish());
            mother.setDateOfBirth(parent.getMotherDOB());
            mother.setGender(AppConstants.Gender.FEMALE.ordinal());
            mother.setPreferredLanguage(prefLanguage);
            mother.setNic(motherNICorPIN);
            if (motherNICorPIN != null) {
                mother.setNic(motherNICorPIN);
                mother.setStatus(Person.Status.SEMI_VERIFIED);
            } else {
                mother.setStatus(Person.Status.UNVERIFIED);
            }
            mother.setLifeStatus(Person.LifeStatus.ALIVE);
            mother.setPlaceOfBirth(parent.getMotherPlaceOfBirth());

            // set mother race
            mother.setRace(parent.getMotherRace());

            // add mother to PRS
            ecivil.addPerson(mother, user);
            // set mothers passport info
            if (!isEmptyString(parent.getMotherPassportNo()) && parent.getMotherCountry() != null) {
                ecivil.addCitizenship(
                    getPersonCitizenship(parent.getMotherCountry(), parent.getMotherPassportNo(), mother), user);
            }
            if (mother.getPin() != null) {
                person.setMotherPINorNIC(mother.getPin().toString());
            }

            if (parent.getMotherAddress() != null) {
                final Address address = new Address(parent.getMotherAddress());
                address.setPermanent(true);
                mother.specifyAddress(address);
                // save new address to PRS
                ecivil.addAddress(address, user);
                // update mother to reflect new address
                ecivil.updatePerson(mother, user);
            }

            logger.debug("Added an unverified record for the mother into the PRS : {}", mother.getPersonUKey());
        }

        // mark mother child relationship
        if (mother != null) {
            person.setMother(mother);
        }

        return mother;
    }

    private Person processFatherToPRS(User user, Person person, ParentInfo parent, String prefLanguage,
        Person mother, MarriageInfo marriage, InformantInfo informant) {

        logger.debug("Processing details of father to the PRS");
        Person father = null;

        // try to lookup the father from verified and semi-verified records list
        String fatherNICorPIN = parent.getFatherNICorPIN();
        if (fatherNICorPIN != null) {
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
        }

        // if we fail to locate the farther in PRS by PIN or NIC, look at unverified records using DOB, and name
        if (father == null) {
            // TODO
            logger.debug("Couldn't locate a verified or semi-verified record for the father from the PRS");
        }

        // if we couldn't locate the father, add a semi_verified or an unverified record to the PRS
        if (father == null && parent.getFatherFullName() != null) {
            father = new Person();
            father.setFullNameInOfficialLanguage(parent.getFatherFullName());
            father.setFullNameInEnglishLanguage(parent.getFatherFullNameInEnglish());
            father.setDateOfBirth(parent.getFatherDOB());
            father.setGender(AppConstants.Gender.MALE.ordinal());
            father.setPreferredLanguage(prefLanguage);
            father.setLifeStatus(Person.LifeStatus.ALIVE);
            if (fatherNICorPIN != null) {
                father.setNic(fatherNICorPIN);
                father.setStatus(Person.Status.SEMI_VERIFIED);
            } else {
                father.setStatus(Person.Status.UNVERIFIED);
            }
            father.setPlaceOfBirth(parent.getFatherPlaceOfBirth());

            // set fathers race
            father.setRace(parent.getFatherRace());

            // add father to the PRS
            ecivil.addPerson(father, user);
            // set fathers passport info
            if (!isEmptyString(parent.getFatherPassportNo()) && parent.getFatherCountry() != null) {
                ecivil.addCitizenship(
                    getPersonCitizenship(parent.getFatherCountry(), parent.getFatherPassportNo(), father), user);
            }
            if (father.getPin() != null) {
                person.setFatherPINorNIC(father.getPin().toString());
            }

            // locate address of father if he is the informant
            if (InformantInfo.InformantType.FATHER.equals(informant.getInformantType())) {
                final Address address = new Address(informant.getInformantAddress());
                address.setPermanent(true);
                father.specifyAddress(address);
                // add new address to the PRS
                ecivil.addAddress(address, user);
                // update father to reflect new address
                ecivil.updatePerson(father, user);
            }

            // set father as married to mother if marriage exists
            if (mother != null && marriage.getDateOfMarriage() != null && marriage.getPlaceOfMarriage() != null) {
                Marriage m = new Marriage();
                m.setBride(mother);
                m.setGroom(father);
                m.setDateOfMarriage(marriage.getDateOfMarriage());
                m.setPlaceOfMarriage(marriage.getPlaceOfMarriage());
                m.setState(Marriage.State.MARRIED);
                m.setPreferredLanguage(prefLanguage);
                father.specifyMarriage(m);
                mother.specifyMarriage(m);

                // add marriage to the PRS
                ecivil.addMarriage(m, user);
                ecivil.updatePerson(mother, user);
                ecivil.updatePerson(father, user);


                // if informant is not father, and we have mothers address, assume that as the
                // unverified address of father due to marriage
                if (!InformantInfo.InformantType.FATHER.equals(informant.getInformantType()) &&
                    parent.getMotherAddress() != null) {
                    final Address address = new Address(parent.getMotherAddress());
                    address.setPermanent(true);
                    father.specifyAddress(address);
                    // add new address
                    ecivil.addAddress(address, user);
                    // update father to reflect new address
                    ecivil.updatePerson(father, user);
                }
            }

            logger.debug("Added an unverified record for the father into the PRS : {}", father.getPersonUKey());
        }

        if (father != null) {
            // set father child relationship
            person.setFather(father);
        }
        return father;
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
            logger.debug("birth type checking for BDF : " + bdf.getIdUKey() + " passed for birth type as : " +
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
            logger.debug("birth type checking for BDF : {}  passed for birth type as : {}", bdf.getIdUKey(), current);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getArchivedCorrectedEntriesForGivenSerialNo(BDDivision bdDivision, long serialNo, User user) {
        logger.debug("Searching for historical records for BD Division : {} and Serial number : {} ",
            bdDivision.getBdDivisionUKey(), serialNo);
        ValidationUtils.validateAccessToBDDivision(user, bdDivision);
        return birthDeclarationDAO.getHistoricalRecordsForBDDivisionAndSerialNo(bdDivision, serialNo);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getDeclarationApprovalPendingByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, User user) {
        ValidationUtils.validateAccessToDSDivision(dsDivision, user);
        return birthDeclarationDAO.getPaginatedListForStateByDSDivision(dsDivision, pageNo, noOfRows,
            BirthDeclaration.State.DATA_ENTRY);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getAllRejectedBirthsByUser(User user) {
        logger.debug("Loading all rejected birth records by {}", user.getUserId());
        return birthDeclarationDAO.getAllRejectedBirthsByUser(user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getAllRejectedBirthsByDistrict(District district, User user) {
        logger.debug("Loading rejected birth records in {} by {}", district.getEnDistrictName(), user.getUserId());
//        ValidationUtils.validateAccessToBDDistrict(user, district);
        return birthDeclarationDAO.getAllRejectedBirthsByDistrict(district);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getAllRejectedBirthsByDSDivision(DSDivision dsDivision, User user) {
        logger.debug("Loading rejected birth records in {} by {}", dsDivision.getEnDivisionName(), user.getUserId());
//        ValidationUtils.validateAccessToDSDivision(dsDivision, user);
        return birthDeclarationDAO.getAllRejectedBirthsByDSDivision(dsDivision);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getDeclarationApprovalPendingByDistrictId(District district, int pageNo, int noOfRows, User user) {
        ValidationUtils.validateAccessToBDDistrict(user, district);
        return birthDeclarationDAO.getPaginatedListForStateByDistrict(district, pageNo, noOfRows,
            BirthDeclaration.State.DATA_ENTRY);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getBelatedDeclarationApprovalPendingByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, User user) {
        ValidationUtils.validateAccessToDSDivision(dsDivision, user);
        return birthDeclarationDAO.getPaginatedListForStateAndBirthTypeByDSDivision(dsDivision, pageNo, noOfRows,
            BirthDeclaration.State.DATA_ENTRY, BirthDeclaration.BirthType.BELATED);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getDeclarationPendingByDSDivisionAndRegisterDateRange(DSDivision dsDivision, Date startDate,
        Date endDate, int pageNo, int noOfRows, User user) {
        ValidationUtils.validateAccessToDSDivision(dsDivision, user);
        return birthDeclarationDAO.getByDSDivisionStatusAndRegisterDateRange(dsDivision, BirthDeclaration.State.DATA_ENTRY,
            startDate, endDate, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getBelatedDeclarationPendingByDSDivisionAndRegisterDateRange(DSDivision dsDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, User user) {
        ValidationUtils.validateAccessToDSDivision(dsDivision, user);
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
        ValidationUtils.validateAccessToDSDivision(dsDivision, user);
        return birthDeclarationDAO.getPaginatedListForStateByDSDivision(
            dsDivision, pageNo, noOfRows, BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED);
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
        ValidationUtils.validateAccessToDSDivision(dsDivision, user);
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
        ValidationUtils.validateAccessToDSDivision(dsDivision, user);
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
        ValidationUtils.validateAccessToDSDivision(dsDivision, user);
        return birthDeclarationDAO.getPaginatedListForStateByDSDivision(dsDivision, pageNo, noOfRows,
            printed ? BirthDeclaration.State.ARCHIVED_CERT_PRINTED : BirthDeclaration.State.ARCHIVED_CERT_GENERATED);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getByDSDivision(DSDivision dsDivision, User user) {
        logger.debug("Get records belonging to DSDivision ID : {}", dsDivision.getDsDivisionUKey());
        ValidationUtils.validateAccessToDSDivision(dsDivision, user);
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
        if (bdf != null) {
            validateAccessOfUser(user, bdf);
        }
        return bdf;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getHistoricalBirthDeclarationRecordForBDDivisionAndSerialNo(BDDivision birthDivision, long serialNo, long idUKey, User user) {
        BirthDeclaration bdf = birthDeclarationDAO.getById(idUKey);
        if (bdf != null) {
            validateAccessOfUser(user, bdf);
            return birthDeclarationDAO.getHistoricalAlterationRecordForBDDivisionAndSerialNo(birthDivision, serialNo, idUKey);
        }
        return null;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthDeclaration> getByDSDivisionAndStatusAndBirthDateRange(DSDivision dsDivision,
        Date startDate, Date endDate, BirthDeclaration.State status, User user) {
        logger.debug("Get records belonging to DSDivision ID, Status : {}", dsDivision.getDsDivisionUKey(), status);
        ValidationUtils.validateAccessToDSDivision(dsDivision, user);
        return birthDeclarationDAO.getByDSDivisionAndStatusAndBirthDateRange(dsDivision, startDate,
            endDate, status);
    }

    /**
     * @inheritDoc
     */
    public List<BirthAlteration> getActiveBirthAlterationByBirthCertificateNumber(long certificateNumber, User user) {
        logger.debug("attempt to get active birth alterations by birth certificate number : {} ", certificateNumber);
        //todo check user permission to get
        //to do filter results set
        throw new UnsupportedOperationException("unsupported contact amith");
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER)
    public List<BirthDeclaration> getActiveRecordByDSDivisionAndSerialNumber(long serialNumber, int dsDivision, User user) {
        logger.debug("attempt to get birth records by ds division: {} and serial number: {}", dsDivision, serialNumber);
        ValidationUtils.validateAccessToDSDivision(dsDivisionDAO.getDSDivisionByPK(dsDivision), user);
        return birthDeclarationDAO.getActiveBirthRecordByDSDivisionAndSerialNumber(serialNumber, dsDivision);
    }

    private PersonCitizenship getPersonCitizenship(Country country, String passportNo, Person person) {
        PersonCitizenship pc = new PersonCitizenship();
        pc.setCountry(country);
        pc.setPassportNo(passportNo);
        pc.setPerson(person);
        return pc;
    }

    /**
     * @inheritDoc
     */
    public CommonStatistics getCommonBirthCertificateCount(String user) {
        CommonStatistics commonStat = new CommonStatistics();

        int data_entry = birthDeclarationDAO.getBirthCertificateCount(BirthDeclaration.State.DATA_ENTRY, new Date(), new Date());
        int approved = birthDeclarationDAO.getBirthCertificateCount(BirthDeclaration.State.APPROVED, new Date(), new Date());
        int rejected = birthDeclarationDAO.getBirthCertificateCount(BirthDeclaration.State.ARCHIVED_REJECTED, new Date(), new Date());

        commonStat.setTotalSubmissions(data_entry + approved + rejected);
        commonStat.setApprovedItems(approved);
        commonStat.setRejectedItems(rejected);
        commonStat.setTotalPendingItems(data_entry);

        logger.debug("BirthRegistrationService Called!");

        //todo call above methods using appropriate Date range

        commonStat.setArrearsPendingItems(0);
        commonStat.setLateSubmissions(0);
        commonStat.setNormalSubmissions(8);
        commonStat.setThisMonthPendingItems(3);

        return commonStat;
    }

    /**
     * @inheritDoc
     */
    public CommonStatistics getBirthStatisticsForUser(String user) {

        int data_entry = 0;
        int approved = 0;
        int rejected = 0;

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);

        int data[] = getBirthDeclarationList(userManager.getUserByID(user), cal.getTime(), new Date());
        if (data.length == 3) {
            data_entry = data[0];
            approved = data[1];
            rejected = data[2];
        }

        CommonStatistics commonStat = new CommonStatistics();
        commonStat.setTotalSubmissions(data_entry + approved + rejected);
        commonStat.setApprovedItems(approved);
        commonStat.setRejectedItems(rejected);
        commonStat.setTotalPendingItems(data_entry);

        cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -5);

        data = getBirthDeclarationList(userManager.getUserByID(user), cal.getTime(), new Date());
        if (data.length == 3) {
            data_entry = data[0];
            approved = data[1];
            rejected = data[2];
        }

        commonStat.setArrearsPendingItems(0);
        commonStat.setLateSubmissions(0);
        commonStat.setNormalSubmissions(8);
        commonStat.setThisMonthPendingItems(3);

        return commonStat;
    }

    private int[] getBirthDeclarationList(User user, Date start, Date end) {

        int data[] = {0, 0, 0};
        List<BirthDeclaration> bdfList = birthDeclarationDAO.getByCreatedUser(user, start, end);

        for (BirthDeclaration bdf : bdfList) {
            BirthDeclaration.State state = bdf.getRegister().getStatus();
            switch (state) {
                case APPROVED:
                    data[0] += 1;
                    break;
                case ARCHIVED_REJECTED:
                    data[1] += 1;
                    break;
                case DATA_ENTRY:
                    data[2] += 1;
                    break;
            }
        }
        return data;
    }
}
