package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.domain.CommonStatistics;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.service.DeathRegistrationService;
import lk.rgd.crs.core.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Indunil Moremada
 * @author amith jayasekara
 */
public class DeathRegistrationServiceImpl implements DeathRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(DeathRegistrationService.class);
    private final DeathRegisterDAO deathRegisterDAO;
    private final UserManager userManager;

    DeathRegistrationServiceImpl(DeathRegisterDAO deathRegisterDAO, UserManager userManager) {
        this.deathRegisterDAO = deathRegisterDAO;
        this.userManager = userManager;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addLateDeathRegistration(DeathRegister deathRegistration, User user) {
        logger.debug("adding late/missing death registration");
        DeathDeclarationValidator.validateMinimalRequirments(deathRegistration);
        if (deathRegistration.getDeathType() != DeathRegister.Type.LATE_NORMAL && deathRegistration.getDeathType() != DeathRegister.Type.MISSING) {
            handleException("Invalid death type : " + deathRegistration.getDeathType(), ErrorCodes.ILLEGAL_STATE);
        }
        addDeathRegistration(deathRegistration, user);
        logger.debug("added a late/missing registration with idUKey : {} ", deathRegistration.getIdUKey());
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addNormalDeathRegistration(DeathRegister deathRegistration, User user) {
        logger.debug("adding normal/sudden death registration");
        DeathDeclarationValidator.validateMinimalRequirments(deathRegistration);
        if (deathRegistration.getDeathType() != DeathRegister.Type.NORMAL && deathRegistration.getDeathType() != DeathRegister.Type.SUDDEN) {
            handleException("Invalid death type : " + deathRegistration.getDeathType(), ErrorCodes.ILLEGAL_STATE);
        }
        addDeathRegistration(deathRegistration, user);
        logger.debug("added a normal/sudden registration with idUKey : {} ", deathRegistration.getIdUKey());
    }

    private void addDeathRegistration(DeathRegister deathRegistration, User user) {
        validateAccessOfUser(user, deathRegistration);
        //validate minimul requirments
        DeathDeclarationValidator.validateMinimalRequirments(deathRegistration);
        // has this serial number been used already?
        DeathRegister existing = deathRegisterDAO.getActiveRecordByBDDivisionAndDeathSerialNo(deathRegistration.getDeath().getDeathDivision(),
            deathRegistration.getDeath().getDeathSerialNo());
        if (existing != null) {
            handleException("can not add death registration " + existing.getIdUKey() +
                " deathRegistration number already exists : " + existing.getStatus(), ErrorCodes.ENTITY_ALREADY_EXIST);
        }
        deathRegistration.setStatus(DeathRegister.State.DATA_ENTRY);
        deathRegisterDAO.addDeathRegistration(deathRegistration, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDeathRegistration(DeathRegister deathRegistration, User user) {
        businessValidations(deathRegistration, user);
        DeathRegister dr = deathRegisterDAO.getById(deathRegistration.getIdUKey());
        if (DeathRegister.State.DATA_ENTRY != dr.getStatus()) {
            handleException("Cannot update death registration " + deathRegistration.getIdUKey() +
                " Illegal state at target : " + dr.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        deathRegisterDAO.updateDeathRegistration(deathRegistration, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public DeathRegister getById(long deathRegisterIdUKey, User user) {
        logger.debug("Load death registration record : {}", deathRegisterIdUKey);
        DeathRegister deathRegister;
        try {
            deathRegister = deathRegisterDAO.getById(deathRegisterIdUKey);
            validateAccessOfUser(user, deathRegister);
            return deathRegister;
        } catch (NullPointerException e) {
            logger.debug("no results found for death id : {}", deathRegisterIdUKey);
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public DeathRegister getWithTransientValuesById(long idUKey, User user) {
        logger.debug("laod deth register record with transient values : idUKey{}", idUKey);
        DeathRegister dr = deathRegisterDAO.getById(idUKey);
        loadValues(dr, user);
        return dr;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public DeathRegister getById(long deathRegisterIdUKey) {
        logger.debug("Load death registration record : {}", deathRegisterIdUKey);
        DeathRegister deathRegister;
        try {
            deathRegister = deathRegisterDAO.getById(deathRegisterIdUKey);
            return deathRegister;
        } catch (NullPointerException e) {
            logger.debug("no results found for death id : {}", deathRegisterIdUKey);
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    //todo do validation warnings
    public List<UserWarning> approveDeathRegistration(long deathRegisterIdUKey, User user, boolean ignoreWarnings) {
        //  logger.debug("attempt to approve death registration record : {} ", deathRegisterIdUKey);
        DeathDeclarationValidator.validateMinimalRequirments(getById(deathRegisterIdUKey, user));
        List<UserWarning> warnings = DeathDeclarationValidator.validateStandardRequirements(
            deathRegisterDAO, getById(deathRegisterIdUKey, user), user);
        if (warnings.isEmpty() || ignoreWarnings) {
            setApprovalStatus(deathRegisterIdUKey, user, DeathRegister.State.APPROVED, null);
        }
        return warnings;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void rejectDeathRegistration(long deathRegisterIdUKey, User user, String comment) {
        logger.debug("attempt to reject death registration record : {}", deathRegisterIdUKey);

        if (comment == null || comment.trim().length() < 1) {
            handleException("A comment is required to reject a birth declaration",
                ErrorCodes.COMMENT_REQUIRED_BDF_REJECT);
        }
        setApprovalStatus(deathRegisterIdUKey, user, DeathRegister.State.REJECTED, comment);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void markDeathCertificateAsPrinted(DeathRegister deathRegister, User user) {
        logger.debug("requested to mark death certificate as printed for the record : {} ", deathRegister.getIdUKey());
        validateAccessOfUser(user, deathRegister);
        if (DeathRegister.State.APPROVED != deathRegister.getStatus()) {
            handleException("Cannot change status , " + deathRegister.getIdUKey() +
                " Illegal state : " + deathRegister.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        deathRegister.setStatus(DeathRegister.State.ARCHIVED_CERT_GENERATED);
        deathRegisterDAO.updateDeathRegistration(deathRegister, user);
    }

    private void setApprovalStatus(long idUKey, User user, DeathRegister.State state, String comment) {
        // check approve permission
        if (!user.isAuthorized(Permission.APPROVE_DEATH)) {
            handleException("User : " + user.getUserId() + " is not allowed to approve/reject death declarations",
                ErrorCodes.PERMISSION_DENIED);
        }
        DeathRegister dr = deathRegisterDAO.getById(idUKey);
        if (DeathRegister.State.DATA_ENTRY == dr.getStatus()) {
            validateAccessOfUser(user, dr);
            dr.setStatus(state);
            dr.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
            dr.getLifeCycleInfo().setApprovalOrRejectUser(user);
            if (state == DeathRegister.State.APPROVED) {
                dr.getLifeCycleInfo().setCertificateGeneratedTimestamp(new Date());
                dr.getLifeCycleInfo().setCertificateGeneratedUser(user);
            }
        } else {
            handleException("Cannot approve/reject death registration " + dr.getIdUKey() +
                " Illegal state : " + dr.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        //setting comment, this is relative only to death rejaction
        dr.setCommnet(comment);
        //updating
        deathRegisterDAO.updateDeathRegistration(dr, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteDeathRegistration(long deathRegiserIdUKey, User user) {
        logger.debug("attempt to delete death registration record : {}", deathRegiserIdUKey);
        DeathRegister dr = deathRegisterDAO.getById(deathRegiserIdUKey);
        validateAccessOfUser(user, dr);
        if (DeathRegister.State.DATA_ENTRY != dr.getStatus()) {
            handleException("Cannot delete death registraion " + deathRegiserIdUKey +
                " Illegal state : " + dr.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        deathRegisterDAO.deleteDeathRegistration(deathRegisterDAO.getById(deathRegiserIdUKey), user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getPaginatedListForState(BDDivision deathDivision, int pageNo, int noOfRows, DeathRegister.State status, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get death registrations with the state : " + status
                + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        ValidationUtils.validateAccessToBDDivision(user, deathDivision);
        return deathRegisterDAO.getPaginatedListForState(deathDivision, pageNo, noOfRows, status);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getByBDDivisionAndRegistrationDateRange(BDDivision deathDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, User user) {
        ValidationUtils.validateAccessToBDDivision(user, deathDivision);
        return deathRegisterDAO.getByBDDivisionAndRegistrationDateRange(deathDivision, startDate, endDate, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getPaginatedListForAll(BDDivision deathDivision, int pageNo, int noOfRows, User user) {
        logger.debug("Get all death registrations   Page : {}  with number of rows per page : {} ", pageNo, noOfRows);
        ValidationUtils.validateAccessToBDDivision(user, deathDivision);
        return deathRegisterDAO.getPaginatedListForAll(deathDivision, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public DeathRegister getByBDDivisionAndDeathSerialNo(BDDivision bdDivision, long deathSerialNo, User user) {
        DeathRegister dr = deathRegisterDAO.getActiveRecordByBDDivisionAndDeathSerialNo(bdDivision, deathSerialNo);
        validateAccessOfUser(user, dr);
        return dr;
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }

    private void businessValidations(DeathRegister deathRegister, User user) {
        validateAccessOfUser(user, deathRegister);
        if (deathRegister.getStatus() != DeathRegister.State.DATA_ENTRY) {
            handleException("can not update death registration " + deathRegister.getIdUKey() +
                " Illegal State : " + deathRegister.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }

    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getPaginatedListForStateByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, DeathRegister.State status, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get death registrations with the state : " + status
                + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        ValidationUtils.validateAccessToDSDivision(dsDivision, user);
        return deathRegisterDAO.getPaginatedListForStateByDSDivision(dsDivision, pageNo, noOfRows, status);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getPaginatedListForAllByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, User user) {
        logger.debug("Get all death registrations   Page : {}  with number of rows per page : {} ", pageNo, noOfRows);
        ValidationUtils.validateAccessToDSDivision(dsDivision, user);
        return deathRegisterDAO.getPaginatedListForAllByDSDivision(dsDivision, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<DeathRegister> getByPinOrNic(long pinOrNic, User user) {
        List<DeathRegister> deathRegisterList = deathRegisterDAO.getDeathRegisterByDeathPersonPINorNIC("" + pinOrNic);
        DeathRegister deathRegister = null;
        if (deathRegisterList.size() > 0) {
            deathRegister = deathRegisterList.get(0);
            validateAccessOfUser(user, deathRegister);
            return deathRegisterList;
        } else {
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public DeathRegister getActiveRecordByBDDivisionAndSerialNo(BDDivision bdDivision, long serialNo, User user) {

        logger.debug("Get active record by BDDivision ID : {} and Serial No : {}", bdDivision.getBdDivisionUKey(), serialNo);

        DeathRegister ddf = deathRegisterDAO.getActiveRecordByBDDivisionAndDeathSerialNo(bdDivision, serialNo);
        // does the user have access to the DR (i.e. check district and DS division)
        validateAccessOfUser(user, ddf);
        return ddf;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getArchivedCorrectedEntriesForGivenSerialNo(BDDivision bdDivision, long serialNo, long deathId, User user) {
        logger.debug("Searching for historical records for BD Division : {} and Serial number : {} ",
            bdDivision.getBdDivisionUKey(), serialNo);
        ValidationUtils.validateAccessToBDDivision(user, bdDivision);
        return deathRegisterDAO.getHistoricalRecordsForBDDivisionAndSerialNo(bdDivision, serialNo, deathId);
    }

    private void validateAccessOfUser(User user, DeathRegister dr) {
        if (dr != null) {
            BDDivision bdDivision = dr.getDeath().getDeathDivision();
            ValidationUtils.validateAccessToBDDivision(user, bdDivision);
        }
    }

    private void loadValues(DeathRegister deathRegister, User user) {
        logger.debug("loading transient values for death record : {}", deathRegister.getIdUKey());
        deathRegister.setOriginalDCIssueUser(user);
        deathRegister.setOriginalDCPlaceOfIssue(user.getPrimaryLocation());
        deathRegister.setOriginalDCPlaceOfIssueSignPrint(user.getUserSignature(deathRegister.getDeath().getPreferredLanguage()));
        deathRegister.setOriginalDCPlaceOfIssuePrint(user.getPrimaryLocation().getLocationName(deathRegister.getDeath().getPreferredLanguage()));
    }

    /**
     * @inheritDoc
     */
    public CommonStatistics getCommonDeathCertificateCount(String user) {
        CommonStatistics commonStat = new CommonStatistics();

        int data_entry = deathRegisterDAO.getDeathCertificateCount(DeathRegister.State.DATA_ENTRY, new Date(), new Date());
        int approved = deathRegisterDAO.getDeathCertificateCount(DeathRegister.State.APPROVED, new Date(), new Date());
        int rejected = deathRegisterDAO.getDeathCertificateCount(DeathRegister.State.REJECTED, new Date(), new Date());

        commonStat.setTotalSubmissions(34/*data_entry + approved + rejected*/);
        commonStat.setApprovedItems(/*approved*/15);
        commonStat.setRejectedItems(/*rejected*/12);
        commonStat.setTotalPendingItems(/*data_entry*/2);

        logger.debug("DeathRegistrationService Called!");

        //todo call above methods using appropriate Date range

        commonStat.setArrearsPendingItems(0);
        commonStat.setLateSubmissions(0);
        commonStat.setNormalSubmissions(8);
        commonStat.setThisMonthPendingItems(3);

        return commonStat;
    }

    public CommonStatistics getDeathStatisticsForUser(String user) {

        int data_entry = 0;
        int approved = 0;
        int rejected = 0;

        List<DeathRegister> deathList = deathRegisterDAO.getByCreatedUser(userManager.getUserByID(user));
        for (DeathRegister dr : deathList) {
            if (dr.getStatus() == DeathRegister.State.APPROVED) {
                approved += 1;
            } else if (dr.getStatus() == DeathRegister.State.REJECTED) {
                rejected += 1;
            } else if (dr.getStatus() == DeathRegister.State.DATA_ENTRY) {
                data_entry += 1;
            }
        }

        CommonStatistics commonStat = new CommonStatistics();
        commonStat.setTotalSubmissions(/*data_entry + approved + rejected*/23);
        commonStat.setApprovedItems(/*approved*/12);
        commonStat.setRejectedItems(/*rejected*/8);
        commonStat.setTotalPendingItems(/*data_entry*/9);

        //todo call above methods using appropriate Date range

        commonStat.setArrearsPendingItems(0);
        commonStat.setLateSubmissions(0);
        commonStat.setNormalSubmissions(8);
        commonStat.setThisMonthPendingItems(3);

        return commonStat;
    }
}
