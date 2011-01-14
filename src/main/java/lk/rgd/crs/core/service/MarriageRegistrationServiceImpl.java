package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.AppConstants;
import lk.rgd.common.api.Auditable;
import lk.rgd.common.api.dao.UserLocationDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.domain.*;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.util.StateUtil;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.MarriageRegistrationDAO;
import lk.rgd.crs.api.dao.MRDivisionDAO;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.MarriageNotice;
import lk.rgd.crs.api.domain.MarriageRegister;
import lk.rgd.crs.api.service.MarriageRegistrationService;
import lk.rgd.crs.core.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.io.File;

/**
 * implementation of the marriage registration service interface
 *
 * @author amith jayasekara
 * @author Chathuranga Withana
 */
public class MarriageRegistrationServiceImpl implements MarriageRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(MarriageRegistrationServiceImpl.class);
    private final MarriageRegistrationDAO marriageRegistrationDAO;
    private final MarriageRegistrationValidator marriageRegistrationValidator;
    private final UserLocationDAO userLocationDAO;
    private final UserManager userManager;
    private final String contentRoot;
    private final String contentType;
    private final MRDivisionDAO mrDivisionDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final DistrictDAO districtDAO;
    //TODO: this is to be changed
    private ContentRepository contentRepository = new ContentRepositoryImpl(AppConstants.CONTENT_ROOT);

    public MarriageRegistrationServiceImpl(MarriageRegistrationDAO marriageRegistrationDAO, UserManager userManager,
        MarriageRegistrationValidator marriageRegistrationValidator, UserLocationDAO userLocationDAO,
        String contentRoot, String contentType, MRDivisionDAO mrDivisionDAO, DSDivisionDAO dsDivisionDAO,
        DistrictDAO districtDAO) {
        this.marriageRegistrationDAO = marriageRegistrationDAO;
        this.marriageRegistrationValidator = marriageRegistrationValidator;
        //todo: to be removed
        this.userLocationDAO = userLocationDAO;
        this.userManager = userManager;
        //TODO: to be changed
        this.contentRoot = AppConstants.CONTENT_ROOT;
        this.contentType = contentType;
        this.mrDivisionDAO = mrDivisionDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.districtDAO = districtDAO;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addMarriageNotice(MarriageRegister notice, MarriageNotice.Type type, User user) {
        logger.debug("attempt to add marriage notice male serial :{} female serial : {}", notice.getSerialOfMaleNotice(),
            notice.getSerialOfFemaleNotice() + ": notice type :" + type);
        checkUserPermission(Permission.ADD_MARRIAGE, ErrorCodes.PERMISSION_DENIED, "add second notice to marriage register", user);
        marriageRegistrationValidator.validateMarriageNotice(notice, type);
        populateObjectForPersisting(notice, type);
        notice.setState(MarriageRegister.State.DATA_ENTRY);
        marriageRegistrationDAO.addMarriageRegister(notice, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addMarriageRegister(MarriageRegister marriageRegister, User user, File scannedImage) {
        //TODO: Validate marriage details
        checkUserPermission(Permission.ADD_MARRIAGE, ErrorCodes.PERMISSION_DENIED, " add Muslim Marriages", user);
        marriageRegistrationDAO.addMarriageRegister(marriageRegister, user);
        if (marriageRegister != null && scannedImage != null) {
            logger.debug("Marriage Register IDUKEY : {}", marriageRegister.getIdUKey());
            //TODO: Create a unique id (file name) for the image (dont use marriageRegister.getIdUKey())
            marriageRegister.setScannedImagePath(contentRepository.storeFile(marriageRegister.getMrDivision().getMrDivisionUKey(), marriageRegister.getIdUKey(), scannedImage));
            marriageRegistrationDAO.updateMarriageRegister(marriageRegister, user);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public MarriageRegister getByIdUKey(long idUKey, User user) {
        logger.debug("attempt to get marriage register by idUKey : {} ", idUKey);
        return marriageRegistrationDAO.getByIdUKey(idUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public MarriageRegister getMarriageRegisterByIdUKey(long idUKey, User user) {
        logger.debug("attempt to get marriage register by idUKey : {} ", idUKey);
        if (!user.isAuthorized(Permission.PRINT_MARRIAGE_EXTRACT)) {
            handleException("User : " + user.getUserId() + " is not authorized to search marriage register", ErrorCodes.PERMISSION_DENIED);
        }
        MarriageRegister marriageRegister = marriageRegistrationDAO.getByIdUKey(idUKey);
        ValidationUtils.validateAccessToMRDivision(marriageRegister.getMrDivision(), user);
        return marriageRegister;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getMarriageNoticeByDistrict(District district, int pageNo, int noOfRows,
        boolean active, User user) {
        logger.debug("Get Active : {} MarriageNotices by District : {}", active, district.getDistrictUKey());
        ValidationUtils.validateAccessToMRDistrict(user, district);
        return marriageRegistrationDAO.getPaginatedListByDistrict(district, pageNo, noOfRows, active);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getMarriageNoticePendingApprovalByDSDivision(DSDivision dsDivision, int pageNo,
        int noOfRows, boolean active, User user) {
        logger.debug("Get Active : {} MarriageNotices pending approval by DSDivision : {}", active,
            dsDivision.getDsDivisionUKey());
        ValidationUtils.validateAccessToDSDivision(dsDivision, user);
        return marriageRegistrationDAO.getPaginatedNoticeListByDSDivision(dsDivision, pageNo, noOfRows, active);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getMarriageNoticePendingApprovalByPINorNIC(String pinOrNic, boolean active, User user) {
        logger.debug("Attempt to get marriage notice pending results for identification number : {} ", pinOrNic);
        List<MarriageRegister> results = marriageRegistrationDAO.getNoticeByPINorNIC(pinOrNic, active);
        return removingAccessDeniedItemsFromList(results, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getMarriageNoticePendingApprovalByMRDivisionAndSerial(MRDivision mrDivision, long serialNo
        , boolean active, User user) {
        logger.debug("Get active record by MRDivision : {} and Serial No : {}", mrDivision.getMrDivisionUKey(), serialNo);
        List<MarriageRegister> results = marriageRegistrationDAO.getNoticeByMRDivisionAndSerialNo(mrDivision, serialNo, active);
        return removingAccessDeniedItemsFromList(results, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getMarriageNoticesByMRDivisionAndRegisterDateRange(MRDivision mrDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, boolean active, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get active : " + active + " record by MRDivision : " + mrDivision.getMrDivisionUKey() +
                " and date range : " + startDate + " to " + endDate);
        }
        ValidationUtils.validateAccessToMRDivision(mrDivision, user);
        return marriageRegistrationDAO.getPaginatedNoticesByMRDivisionAndRegisterDateRange(mrDivision, startDate,
            endDate, pageNo, noOfRows, active);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getMarriageNoticesByDSDivisionAndRegisterDateRange(DSDivision dsDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, boolean active, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get active : " + active + " records by DSDivision : " + dsDivision.getDsDivisionUKey() +
                " and date range : " + startDate + " to " + endDate);
        }
        ValidationUtils.validateAccessToDSDivision(dsDivision, user);
        return marriageRegistrationDAO.getPaginatedNoticesByDSDivisionAndRegisterDateRange(dsDivision, startDate,
            endDate, pageNo, noOfRows, active);
    }

    private List<MarriageRegister> removingAccessDeniedItemsFromList(List<MarriageRegister> registerList, User user) {
        List<MarriageRegister> toBeRemoved = new ArrayList<MarriageRegister>();
        for (MarriageRegister mr : registerList) {
            if (!checkUserAccessPermissionToMarriageRecord(mr, user)) {
                toBeRemoved.add(mr);
            }
        }
        //removing
        registerList.removeAll(toBeRemoved);
        return registerList;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getMarriageNoticePendingApprovalByMRDivision(MRDivision mrDivision, int pageNo,
        int noOfRows, boolean active, User user) {
        logger.debug("Get Active : {} MarriageNotices pending approval by MRDivision : {}", active,
            mrDivision.getMrDivisionUKey());
        ValidationUtils.validateAccessToMRDivision(mrDivision, user);
        return marriageRegistrationDAO.getPaginatedNoticeListByMRDivision(mrDivision, pageNo, noOfRows, active);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public MarriageRegister getActiveMarriageNoticeByMaleAndFemaleIdentification(String maleIdentification,
        String femaleIdentification, User user) {
        logger.debug("getting active marriage notice for male identification : {} :and female identification : {}",
            maleIdentification, femaleIdentification);
        //getting latest record
        //note :in some cases legally notice become expired in 3 months but system does not expire it with in exact 3 months
        //in some situations a new notice will be submitted a day after legal expiration so there is a existing active record
        //but new notice is the latest and we take that
        List<MarriageRegister> records = marriageRegistrationDAO.getActiveMarriageNoticeByMaleFemaleIdentification
            (maleIdentification, femaleIdentification);
        if (records.size() > 0) {
            return records.get(0);
        }
        return null;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateMarriageRegister(MarriageRegister marriageRegister, User user) {
        logger.debug("attempt to update marriage register/notice record : idUKey : {}", marriageRegister.getIdUKey());
        checkUserPermission(Permission.EDIT_MARRIAGE, ErrorCodes.PERMISSION_DENIED, " edit  marriage register ", user);
        marriageRegistrationDAO.updateMarriageRegister(marriageRegister, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateMuslimMarriageDetails(MarriageRegister marriageRegister, User user) {
        //TODO: Validate Marriage details
        checkUserPermission(Permission.EDIT_MARRIAGE, ErrorCodes.PERMISSION_DENIED, " update  marriage register ", user);
        marriageRegistrationDAO.updateMarriageRegister(marriageRegister, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateMarriageRegistrationDetails(MarriageRegister marriageRegister, User user) {
        //TODO: Validate Marriage details
        checkUserPermission(Permission.EDIT_MARRIAGE, ErrorCodes.PERMISSION_DENIED, " update  marriage register ", user);
        marriageRegistrationDAO.updateMarriageRegister(marriageRegister, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserWarning> addSecondMarriageNotice(MarriageRegister notice, MarriageNotice.Type type,
        boolean ignoreWarnings, boolean undo, User user) {
        //only MALE and FEMALE notices are allowed to add second notice
        if (type != MarriageNotice.Type.BOTH_NOTICE) {
            if (!undo) {
                logger.debug("attempt to add a second notice for existing record : idUKey : {}", notice.getIdUKey());
                checkUserPermission(Permission.ADD_MARRIAGE, ErrorCodes.PERMISSION_DENIED,
                    " add second notice to marriage register ", user);
                //get user warnings when adding  second notice   and return warnings
                List<UserWarning> warnings = marriageRegistrationValidator.validateAddingSecondNoticeAndEdit(notice, type);
                marriageRegistrationValidator.validateMarriageNotice(notice, type);
                if (warnings != null && warnings.size() > 0 && !ignoreWarnings) {
                    logger.debug("warnings found while adding second notice to the existing marriage notice idUKey : {}",
                        notice.getIdUKey());
                    return warnings;
                }
                if (warnings.size() == 0 || ignoreWarnings) {
                    //   updateMarriageRegister(notice, user);
                    marriageRegistrationDAO.updateMarriageRegister(notice, user);
                }
            } else {
                //undo the first notice state to DATA_ENTRY
                if (logger.isDebugEnabled()) {
                    logger.debug("attempt to roll back notice : idUKey " + notice.getIdUKey() + "state to " +
                        "previous state : current state : " + notice.getState());
                }
                //todo is it more use full if this hardcoded remove ?? amith
                notice.setState(MarriageRegister.State.DATA_ENTRY);
                //updating the marriage register object
                marriageRegistrationDAO.updateMarriageRegister(notice, user);
            }
        } else {
            // not allow to add second notice
            handleException("invalid notice type, can't add second notice for notice type : " + type,
                ErrorCodes.INVALID_NOTICE_TYPE_FOR_ADD_SECOND);
        }
        return Collections.emptyList();
    }


    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteMarriageNotice(long idUKey, MarriageNotice.Type noticeType, User user) {
        logger.debug("attempt to remove marriage notice : idUKey : {} and notice type : {}", idUKey, noticeType);
        checkUserPermission(Permission.DELETE_MARRIAGE, ErrorCodes.PERMISSION_DENIED, "delete marriage notice ", user);
        MarriageRegister notice = marriageRegistrationDAO.getByIdUKey(idUKey);
        if (notice == null) {
            handleException("cannot find record for approval idUKey : " + idUKey, ErrorCodes.CAN_NOT_FIND_MARRIAGE_NOTICE);
        }
        checkUserPermissionForDeleteApproveAndRejectNotice(notice, noticeType, user);
        checkStateForDeleteNotice(notice, noticeType);
        if (noticeType == MarriageNotice.Type.BOTH_NOTICE) {
            //case 1
            marriageRegistrationDAO.deleteMarriageRegister(idUKey);
        } else {
            if ((notice.getSerialOfMaleNotice() != null && notice.getSerialOfMaleNotice() != 0) &&
                (notice.getSerialOfFemaleNotice() != null && notice.getSerialOfFemaleNotice() != 0)) {
                //case 2.2
                if (noticeType == MarriageNotice.Type.MALE_NOTICE) {
                    //clearing male notice related data
                    clearingNoticeDetails(notice, MarriageNotice.Type.MALE_NOTICE);
                } else if (noticeType == MarriageNotice.Type.FEMALE_NOTICE) {
                    //clearing female notice related data
                    clearingNoticeDetails(notice, MarriageNotice.Type.FEMALE_NOTICE);
                }
                marriageRegistrationDAO.updateMarriageRegister(notice, user);
                //notice type BOTH want came here (because it is came with is both submit true)
            } else {
                //case 2.1
                //there can't be a case both of the serial numbers are Zero so we are not handling that case
                marriageRegistrationDAO.deleteMarriageRegister(idUKey);
            }
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public MarriageRegister getActiveRecordByMRDivisionAndSerialNo(MRDivision mrDivision, long serialNo, User user) {
        logger.debug("Get active record by MRDivision : {} and Serial Number : {}", mrDivision.getMrDivisionUKey(),
            serialNo);
        ValidationUtils.validateAccessToMRDivision(mrDivision, user);
        MarriageRegister mr = marriageRegistrationDAO.getActiveRecordByMRDivisionAndSerialNo(mrDivision, serialNo);
        return mr;
    }

    /**
     * @inheritDoc only REGISTER_DATA_ENTRY state records are consider as pending marriage register records
     * only record captured division users are allowed to access the marriage record
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getMarriageRegisterBySerialAndMRDivision(long serialNumber,
        MRDivision mrDivision, int pageNumber, int numOfRows, boolean active, User user) {
        logger.debug("attempt to get pending marriage register record for marriage serial number : {}" +
            " and MRDivision name : {} ", serialNumber, mrDivision.getEnDivisionName());
        //validating user  access to the MR division
        ValidationUtils.validateAccessToMRDivision(mrDivision, user);
        return marriageRegistrationDAO.getByMRDivisionAndSerialNo(mrDivision, MarriageRegister.State.REG_DATA_ENTRY,
            serialNumber, active);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getMarriageRegistersByDSDivision(DSDivision dsDivision, int pageNumber,
        int numOfRows, boolean active, User user) {
        logger.debug("attempt to get pending marriage register list by DSDivision idUKey : {}", dsDivision.getDsDivisionUKey());
        //validate user access to the ds division
        ValidationUtils.validateAccessToDSDivision(dsDivision, user);
        return marriageRegistrationDAO.getPaginatedListForStateByDSDivision(dsDivision,
            MarriageRegister.State.REG_DATA_ENTRY, pageNumber, numOfRows, true);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getMarriageRegistersByDistrict(District district, int pageNumber,
        int numOfRows, boolean active, User user) {
        //TODO : validate user access to the district
        return marriageRegistrationDAO.getPaginatedListByDistrict(district,
            MarriageRegister.State.REG_DATA_ENTRY, pageNumber, numOfRows, true);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getMarriageRegisterByState(MarriageRegister.State state, int pageNumber, int numOfRows, boolean active, User user) {
        //TODO validate user access
        return marriageRegistrationDAO.getPaginatedMarriageRegisterListByState(state, pageNumber, numOfRows, active);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserWarning> approveMarriageNotice(long idUKey, MarriageNotice.Type type, boolean ignoreWarnings, User user) {
        logger.debug("attempt to approve marriage notice with idUKey : {} and notice type : {}", idUKey, type);
        //todo can any ADR approve dis
        //check is user has permission to perform this task
        checkUserPermission(Permission.APPROVE_MARRIAGE, ErrorCodes.PERMISSION_DENIED, "approve marriage notice", user);
        MarriageRegister existingNotice = marriageRegistrationDAO.getByIdUKey(idUKey);
        //check is there a existing record for approving
        if (existingNotice == null) {
            handleException("cannot find record for approval idUKey :" + idUKey, ErrorCodes.CAN_NOT_FIND_MARRIAGE_NOTICE);
        }
        //check is user has permission to deal with this marriage notice
        checkUserPermissionForDeleteApproveAndRejectNotice(existingNotice, type, user);
        //check is pre request are full filled before approve
        isNoticeAllowedToApprove(existingNotice, type);

        checkExistingActiveApprovedNotices(existingNotice, user);
        //check basic warnings
        List<UserWarning> warnings = Collections.emptyList();
        warnings = marriageRegistrationValidator.
            checkUserWarningsForApproveMarriageNotice(existingNotice, type, user);
        if (warnings != null && warnings.size() > 0 && !ignoreWarnings) {
            if (logger.isDebugEnabled()) {
                logger.debug("warnings found while approving marriage notice idUKey :" + existingNotice.getIdUKey() +
                    " notice type :" + type + " current notice state :" + existingNotice.getState());
            }
            return warnings;
        }

        if (existingNotice.isSingleNotice()) {
            //directly change state in to NOTICE_APPROVED
            if (existingNotice.getState() == MarriageRegister.State.DATA_ENTRY) {
                existingNotice.setState(MarriageRegister.State.NOTICE_APPROVED);
            } else {
                handleException("unable to approve single : " + existingNotice.isSingleNotice() + " notice type: " + type +
                    " idUKey: " + idUKey + "  current state: " + existingNotice.getState(),
                    ErrorCodes.INVALID_STATE_FOR_APPROVAL);
            }
        } else {
            if (existingNotice.getState() == MarriageRegister.State.DATA_ENTRY) {
                //case 1 req must in DATA_ENTRY
                switch (type) {
                    case MALE_NOTICE:
                        existingNotice.setState(MarriageRegister.State.MALE_NOTICE_APPROVED);
                        break;
                    case FEMALE_NOTICE:
                        existingNotice.setState(MarriageRegister.State.FEMALE_NOTICE_APPROVED);
                }
            } else {
                existingNotice.setState(MarriageRegister.State.NOTICE_APPROVED);
            }
        }
        //warnings issue if we going to approve second notice
        //if current state is NOTICE_APPROVED that mean we approved single notice or we are approving second notice(license
        // collect party) when notice become NOTICE_APPROVE it eligible for printing License so we have to validate
        // that marriage in this evens as well
        if (existingNotice.getState() == MarriageRegister.State.NOTICE_APPROVED) {
            //todo
        }
        if (warnings.size() == 0 || ignoreWarnings) {
            //if we change notice state to NOTICE_APPROVED  we have to validate more
            marriageRegistrationDAO.updateMarriageRegister(existingNotice, user);
        }
        logger.debug("successfully  approved marriage notice with idUKey : {} and notice type : {}", idUKey, type);
        return Collections.emptyList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void approveMarriageRegister(long idUKey, User user) {
        checkUserPermission(Permission.APPROVE_MARRIAGE, ErrorCodes.PERMISSION_DENIED, "approve marriage register", user);
        MarriageRegister marriageRegister = marriageRegistrationDAO.getByIdUKey(idUKey);
        marriageRegister.setState(MarriageRegister.State.REGISTRATION_APPROVED);
        marriageRegistrationDAO.updateMarriageRegister(marriageRegister, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void rejectMarriageRegister(long idUKey, String comment, User user) {
        checkUserPermission(Permission.APPROVE_MARRIAGE, ErrorCodes.PERMISSION_DENIED, "approve marriage register", user);
        MarriageRegister marriageRegister = marriageRegistrationDAO.getByIdUKey(idUKey);
        marriageRegister.setState(MarriageRegister.State.REGISTRATION_REJECTED);
        //TODO validate comment if needed
        marriageRegister.setRegistrationRejectComment(comment);
        marriageRegistrationDAO.updateMarriageRegister(marriageRegister, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getMarriageRegisterByMRDivision(MRDivision mrDivision, int pageNumber,
        int numOfRows, boolean active, User user) {
        logger.debug("attempt to get pending marriage register list by MRDivision idUKey : {}", mrDivision.getMrDivisionUKey());
        //validate user access to the mr division
        ValidationUtils.validateAccessToMRDivision(mrDivision, user);
        return marriageRegistrationDAO.getPaginatedListForStateByMRDivision(mrDivision,
            MarriageRegister.State.REG_DATA_ENTRY, pageNumber, numOfRows, true);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getMarriageRegisterList(String divisionType, int divisionUKey, MarriageRegister.State state,
        boolean isActive, Date startDate, Date endDate, int pageNumber, int numOfRows, User user) {

        Set<DSDivision> dsDivisionList = null;
        EnumSet<MarriageRegister.State> stateList = StateUtil.getMarriageRegisterStateList(state);
        //validate user access
        if (AppConstants.MARRIAGE.equals(divisionType)) {
            ValidationUtils.validateAccessToMRDivision(mrDivisionDAO.getMRDivisionByPK(divisionUKey), user);

        } else if (AppConstants.DS_DIVISION.equals(divisionType)) {
            ValidationUtils.validateAccessToDSDivision(dsDivisionDAO.getDSDivisionByPK(divisionUKey), user);

        } else if (AppConstants.DISTRICT.equals(divisionType)) {
            ValidationUtils.validateAccessToMRDistrict(user, districtDAO.getDistrict(divisionUKey));

        } else {
            //If no divisions selected find all DS divisions available for user
            //TODO: handle error if user inactive
            if (!Role.ROLE_RG.equals(user.getRole().getRoleId())) {
                //TODO: if DS division list avalable for DR and ARG, this method is ok
                //TODO: else find particular division list based on the user role
                dsDivisionList = user.getAssignedMRDSDivisions();
                divisionType = AppConstants.ALL;
            }
        }

        return marriageRegistrationDAO.getPaginatedMarriageRegisterList(divisionType, divisionUKey, dsDivisionList,
            stateList, isActive, startDate, endDate, pageNumber, numOfRows);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getMarriageRegisterByPINorNIC(String pinOrNic, boolean active, User user) {
        logger.debug("Attempt to get marriage registers results for identification number : {} ", pinOrNic);
        List<MarriageRegister> results =
            marriageRegistrationDAO.getByStateAndPINorNIC(MarriageRegister.State.REG_DATA_ENTRY, pinOrNic, active);
        return removingAccessDeniedItemsFromList(results, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    //license can be make marked as print by any one even deo  if deo selected authorized adr or higher and mark the document
    // as printed  the selected record is persisted but what  if that selected ADR refused to sign :O
    public void markLicenseToMarriageAsPrinted(long idUKey, Location licenseIssueLocation, User issuedUser, User user) {
        logger.debug("attempt to mark marriage notice as idUKey :{} as license printed", idUKey);
        MarriageRegister notice = marriageRegistrationDAO.getByIdUKey(idUKey);
        if (notice == null) {
            handleException("can't find marriage register record for idUKey : " + idUKey + " for mark as print",
                ErrorCodes.CAN_NOT_FIND_MARRIAGE_NOTICE);
        } else {
            if (notice.getState() == MarriageRegister.State.NOTICE_APPROVED) {
                //only allowed user can mark as print
                ValidationUtils.validateAccessToMarriageNotice(notice, user);
                populateNoticeForMarkAsPrint(notice, issuedUser, licenseIssueLocation);
                //updating marriage notice
                marriageRegistrationDAO.updateMarriageRegister(notice, user);
            } else {
                //invalid state for printing license
                handleException("invalid state for mark as print license for idUKey :" + idUKey + " current state :" +
                    notice.getState(), ErrorCodes.INVALID_STATE_FOR_PRINT_LICENSE);
            }
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void markMarriageExtractAsPrinted(long idUKey, Location issuedLocation, User issuedUser, User user) {
        MarriageRegister register = marriageRegistrationDAO.getByIdUKey(idUKey);
        if (register == null) {
            handleException("Marriage Register could not be found - idUKey : "
                + idUKey, ErrorCodes.MARRIAGE_REGISTER_NOT_FOUND);
        } else {
            if (register.getState() == MarriageRegister.State.REGISTRATION_APPROVED) {
                ValidationUtils.validateAccessToMarriageRegister(register, user);
                setMarriageExtractPrintingDetails(register, issuedUser, issuedLocation);
                marriageRegistrationDAO.updateMarriageRegister(register, user);
            } else {
                handleException("Invalid state of marriage register - idUKey :" + idUKey + " Current State is " +
                    register.getState(), ErrorCodes.INVALID_STATE_FOR_PRINT_LICENSE);
            }
        }
    }

    private void populateNoticeForMarkAsPrint(MarriageRegister notice, User issuingUser, Location issuingLocation) {
        if (issuingLocation != null && issuingUser != null) {
            notice.setState(MarriageRegister.State.LICENSE_PRINTED);
            checkUserPermissionForTheLocation(issuingUser, issuingLocation);
            notice.setLicensePrintUser(issuingUser);
            java.util.GregorianCalendar gCal = new GregorianCalendar();
            //set issued time
            notice.setLicensePrintTimestamp(gCal.getTime());
            notice.setLicenseIssueLocation(issuingLocation);
        } else {
            handleException("invalid issuing location " + issuingLocation + " or issuing user " + issuingUser,
                ErrorCodes.INVALID_LICENSE_ISSUE_USER_OR_LOCATION);
        }
    }

    /**
     * Adding certifying user and location to the extract of marriage
     *
     * @param register
     * @param issuingUser
     * @param issuingLocation
     */
    private void setMarriageExtractPrintingDetails(MarriageRegister register, User issuingUser, Location issuingLocation) {
        if (issuingLocation == null) {
            handleException("Invalid issuing location " + issuingLocation,
                ErrorCodes.INVALID_USER_ON_CERTIFYING_MARRIAGE_EXTRACT);
        } else if (issuingUser == null) {
            handleException("Invalid certifying user " + issuingUser,
                ErrorCodes.INVALID_USER_ON_CERTIFYING_MARRIAGE_EXTRACT);
        } else {
            register.setState(MarriageRegister.State.EXTRACT_PRINTED);
            checkUserPermissionForTheLocation(issuingUser, issuingLocation);
            register.setExtractCertifiedUser(issuingUser);
            register.setExtractIssuedLocation(issuingLocation);
            register.setExtractPrintedTimestamp(new GregorianCalendar().getTime());
        }
    }

    /**
     * checking user is belongs to the location
     */
    private void checkUserPermissionForTheLocation(User user, Location licenseIssueLocation) {
        if (!user.isAllowedAccessToLocation(licenseIssueLocation.getLocationUKey())) {
            handleException("user : " + user.getUserId() + " doesn't allowed to user location " +
                licenseIssueLocation.getLocationUKey(), ErrorCodes.USER_IS_NOT_ALLOWED_FOR_LOCATION);
        }
    }

    private void checkUserPermission(int permissionBit, int errorCode, String msg, User user) {
        if (!user.isAuthorized(permissionBit)) {
            handleException("User : " + user.getUserId() + " is not allowed to " + msg, errorCode);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void rejectMarriageNotice(long idUKey, MarriageNotice.Type type, String comment, User user) {
        logger.debug("attempt to reject marriage notice : idUKey {} : notice type : {} ", idUKey, type);
        MarriageRegister notice = marriageRegistrationDAO.getByIdUKey(idUKey);
        if (notice.getState() == MarriageRegister.State.DATA_ENTRY) {
            switch (type) {
                case MALE_NOTICE:
                case FEMALE_NOTICE:
                case BOTH_NOTICE:
                    notice.setState(MarriageRegister.State.NOTICE_REJECTED);
            }
        } else if (notice.getState() == MarriageRegister.State.MALE_NOTICE_APPROVED) {
            switch (type) {
                case FEMALE_NOTICE:
                    notice.setState(MarriageRegister.State.FEMALE_NOTICE_REJECTED);
                    break;
                case MALE_NOTICE:
                case BOTH_NOTICE:
                    handleException("unable to reject male or both type notice  : idUKey " + idUKey + "notice type :" +
                        "it's been approved before" + type, ErrorCodes.UNABLE_TO_REJECT_MALE_NOTICE);
            }
        } else if (notice.getState() == MarriageRegister.State.FEMALE_NOTICE_APPROVED) {
            switch (type) {
                case MALE_NOTICE:
                    notice.setState(MarriageRegister.State.MALE_NOTICE_REJECTED);
                    break;
                //unable to reject already approved
                case FEMALE_NOTICE:
                case BOTH_NOTICE:
                    handleException("unable to reject female or both type notice  : idUKey " + idUKey + "notice type :"
                        + "it's been approved before" + type, ErrorCodes.UNABLE_TO_REJECT_FEMALE_NOTICE);
            }
        } else {
            //invalid state   for reject
            handleException("unable to reject invalid state for a rejection : idUKey " + idUKey + "notice type :" +
                type, ErrorCodes.INVALID_NOTICE_STATE_FOR_REJECT);
        }
        //set comment
        notice.setNoticeRejectionComment(comment);
        //archiving the record
        notice.getLifeCycleInfo().setActiveRecord(false);
        //updating the record
        marriageRegistrationDAO.updateMarriageRegister(notice, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public MarriageRegister getMarriageNoticeForPrintLicense(long idUKey, User user) {
        logger.debug("attempt to get marriage register (notice) idUKey : {} for print license ", idUKey);
        MarriageRegister notice = marriageRegistrationDAO.getByIdUKey(idUKey);
        if (notice == null) {
            handleException("can't find marriage register record for idUKey : " + idUKey + " for print",
                ErrorCodes.CAN_NOT_FIND_MARRIAGE_NOTICE);
        } else {
            ValidationUtils.validateAccessToMarriageNotice(notice, user);
            //only the user from both MRDivision can print license
            if (notice.getState() == MarriageRegister.State.NOTICE_APPROVED ||
                notice.getState() == MarriageRegister.State.LICENSE_PRINTED) {
                return notice;
            } else {
                //invalid state for printing license
                handleException("invalid state for print license for idUKey :" + idUKey + " current state :" +
                    notice.getState(), ErrorCodes.INVALID_STATE_FOR_PRINT_LICENSE);
            }
        }
        return null;
    }

    /**
     * @inheriteDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserWarning> editMarriageNotice(MarriageRegister notice, MarriageNotice.Type type,
        boolean ignoreWarnings, User user) {
        logger.debug("attempt to edit marriage notice idUKey : {}", notice.getIdUKey());
        List<UserWarning> warnings = marriageRegistrationValidator.validateAddingSecondNoticeAndEdit(notice, type);
        if (warnings != null && warnings.size() > 0 && !ignoreWarnings) {
            logger.debug("warnings found while adding second notice to the existing marriage notice idUKey : {}",
                notice.getIdUKey());
            return warnings;
        }
        if (warnings.size() == 0 || ignoreWarnings) {
            //   updateMarriageRegister(notice, user);
            marriageRegistrationDAO.updateMarriageRegister(notice, user);
        }
        return Collections.emptyList();
    }

    /**
     * check notice can be approve(not checking state and user permission for DS or user permission for approving notice)
     * only checking is it allowed to approve
     * scenario
     * FEMALE notice is approving and MALE notice is present and FEMALE party is requesting the license
     * in that scenario unless MALE notice is approved FEMALE notice cannot be approved
     */
    private void isNoticeAllowedToApprove(MarriageRegister register, MarriageNotice.Type type) {
        boolean check = false;
        switch (type) {
            case MALE_NOTICE:
                if (!register.isSingleNotice() &&
                    (register.getLicenseCollectType() == MarriageRegister.LicenseCollectType.MAIL_TO_MALE ||
                        register.getLicenseCollectType() == MarriageRegister.LicenseCollectType.HAND_COLLECT_MALE) &&
                    (register.getState() != MarriageRegister.State.FEMALE_NOTICE_APPROVED)) {
                    //that means state must ne on FEMALE_NOTICE approved or
                    check = true;
                }
                break;
            case FEMALE_NOTICE:
                if (!register.isSingleNotice() &&
                    (register.getLicenseCollectType() == MarriageRegister.LicenseCollectType.MAIL_TO_FEMALE ||
                        register.getLicenseCollectType() == MarriageRegister.LicenseCollectType.HAND_COLLECT_FEMALE) &&
                    (register.getState() != MarriageRegister.State.MALE_NOTICE_APPROVED)) {
                    //that means state must be in MALE_NOTICE
                    check = true;
                }
                break;
            default:
        }

        if (check) {
            handleException("unable to approve single :" + register.isSingleNotice() + "notice type:" + type
                + "License request by male :" + register.getLicenseCollectType() + ",idUKey" +
                register.getIdUKey(), ErrorCodes.OTHER_PARTY_MUST_APPROVE_FIRST);
        }
    }


    /**
     * check user permission for removing marriage notice
     * note : to remove user must in
     * BOTH : mrDivision of male notice
     * MALE : mrDivision of male notice
     * FEMALE : mrDivision of female notice
     */
    private void checkUserPermissionForDeleteApproveAndRejectNotice(MarriageRegister register, MarriageNotice.Type type, User user) {
        switch (type) {
            case BOTH_NOTICE:
            case MALE_NOTICE: {
                ValidationUtils.validateAccessToMRDivision(register.getMrDivisionOfMaleNotice(), user);
            }
            break;
            case FEMALE_NOTICE: {
                ValidationUtils.validateAccessToMRDivision(register.getMrDivisionOfFemaleNotice(), user);
            }
        }
    }

    /**
     * check register state for removal
     */
    private void checkStateForDeleteNotice(MarriageRegister register, MarriageNotice.Type type) {
        boolean validState = false;
        switch (type) {
            case BOTH_NOTICE:
                if (register.getState() == MarriageRegister.State.DATA_ENTRY) {
                    validState = true;
                }
                break;
            case FEMALE_NOTICE:
                if (register.getState() == MarriageRegister.State.DATA_ENTRY ||
                    register.getState() == MarriageRegister.State.MALE_NOTICE_APPROVED) {
                    validState = true;
                }
                break;
            case MALE_NOTICE:
                if (register.getState() == MarriageRegister.State.DATA_ENTRY ||
                    register.getState() == MarriageRegister.State.FEMALE_NOTICE_APPROVED) {
                    validState = true;
                }
                break;
        }
        if (!validState) {
            handleException("invalid state" + register.getState() + "for removing notice idUKey " + register.getIdUKey()
                , ErrorCodes.INVALID_STATE_FOR_REMOVAL);
        }
    }


    /**
     * clearing notice related data for given notice type
     */
    private void clearingNoticeDetails(MarriageRegister notice, MarriageNotice.Type type) {
        switch (type) {
            case BOTH_NOTICE:
            case MALE_NOTICE:
                notice.setMrDivisionOfMaleNotice(null);
                notice.setSerialOfMaleNotice(null);
                notice.setDateOfMaleNotice(null);
                break;
            case FEMALE_NOTICE:
                notice.setMrDivisionOfFemaleNotice(null);
                notice.setSerialOfFemaleNotice(null);
                notice.setDateOfFemaleNotice(null);
        }

    }

    /**
     * populate notice for persisting
     * if notice type is BOTH single notice will be true
     */
    private void populateObjectForPersisting(MarriageRegister marriageRegister, MarriageNotice.Type type) {
        if (type == MarriageNotice.Type.BOTH_NOTICE) {
            marriageRegister.setSingleNotice(true);
        }
    }

    /**
     * private method which check that current user have permission for ds divisions that allowed to
     * edit/approve/delete/reject  marriage notice/register record
     * note :we can not throw an exception (for unauthorized access) here because we just want to remove those unauthorized
     * records from the display list
     */
    private boolean checkUserAccessPermissionToMarriageRecord(MarriageRegister marriageRegister, User user) {
        //logic : if A and B submit two notices from two ds (a and b) only users in ds 'a' and ds 'b'
        // can perform functions to the record
        MRDivision maleMrDivision = marriageRegister.getMrDivisionOfMaleNotice();   //MR division male notice submitted
        MRDivision femaleMrDivision = marriageRegister.getMrDivisionOfFemaleNotice(); //MR division female notice submitted
        boolean haveAccess = ((maleMrDivision != null && user.isAllowedAccessToMRDSDivision(maleMrDivision.getMrDivisionUKey())) ||
            (femaleMrDivision != null && user.isAllowedAccessToMRDSDivision(femaleMrDivision.getMrDivisionUKey())));
        return haveAccess;
    }

    private void checkExistingActiveApprovedNotices(MarriageRegister register, User user) {
        if (register.getState() == MarriageRegister.State.NOTICE_APPROVED) {
            MarriageRegister existingActiveApprovedNotice = getActiveMarriageNoticeByMaleAndFemaleIdentification(
                register.getMale().getIdentificationNumberMale(), register.getFemale().getIdentificationNumberFemale(), user);
            if (existingActiveApprovedNotice != null) {
                handleException("existing active approved notice for male pin" + register.getMale().
                    getIdentificationNumberMale() + "and female pin " + register.getFemale().
                    getIdentificationNumberFemale() + "so can not approve", ErrorCodes.EXISTING_ACTIVE_APPROVED_NOTICE);
            }
        }
    }

    private static void handleException(String msg, int errorCode) {
        logger.error(msg);
        throw new CRSRuntimeException(msg, errorCode);
    }

    public CommonStatistics getCommonMarriageCertificateCount(String user) {

        // TODO Correct state not supplied
        CommonStatistics commonStat = new CommonStatistics();

//        int data_entry = marriageRegistrationDAO.getMarriageCertificateCount(MarriageRegister.State.DATA_ENTRY, new Date(), new Date());
//        int approved = marriageRegistrationDAO.getMarriageCertificateCount(MarriageRegister.State.DATA_ENTRY, new Date(), new Date());
//        int rejected = marriageRegistrationDAO.getMarriageCertificateCount(MarriageRegister.State.DATA_ENTRY, new Date(), new Date());

        commonStat.setTotalSubmissions(/*data_entry + approved + rejected*/23);
        commonStat.setApprovedItems(/*approved*/12);
        commonStat.setRejectedItems(/*rejected*/8);
        commonStat.setTotalPendingItems(/*data_entry*/9);

        logger.debug("BirthRegistrationService Called!");

        //todo call above methods using appropriate Date range

        commonStat.setArrearsPendingItems(0);
        commonStat.setLateSubmissions(0);
        commonStat.setNormalSubmissions(8);
        commonStat.setThisMonthPendingItems(3);

        return commonStat;
    }

    public CommonStatistics getMarriageStatisticsForUser(String user) {
        int data_entry = 0;
        int approved = 0;
        int rejected = 0;

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);

        int data[] = getMarriageDeclarationList(userManager.getUserByID(user), cal.getTime(), new Date());
        if (data.length == 3) {
            data_entry = data[0];
            approved = data[1];
            rejected = data[2];
        }

        CommonStatistics commonStat = new CommonStatistics();
        commonStat.setTotalSubmissions(/*data_entry + approved + rejected*/23);
        commonStat.setApprovedItems(/*approved*/12);
        commonStat.setRejectedItems(/*rejected*/8);
        commonStat.setTotalPendingItems(/*data_entry*/9);

        cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);

        data = getMarriageDeclarationList(userManager.getUserByID(user), cal.getTime(), new Date());
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

    private int[] getMarriageDeclarationList(User user, Date start, Date end) {

        int data[] = {0, 0, 0};
        List<MarriageRegister> mrList = marriageRegistrationDAO.getByCreatedUser(user, start, end);

        for (MarriageRegister mr : mrList) {
            if (mr.getState() == MarriageRegister.State.NOTICE_APPROVED) {
                data[0] += 1;
            } else if (mr.getState() == MarriageRegister.State.NOTICE_REJECTED) {
                data[1] += 1;
            } else if (mr.getState() == MarriageRegister.State.DATA_ENTRY) {
                data[2] += 1;
            }
        }
        return data;
    }

    /**
     * @inheritDoc
     */
    public String getContentRoot() {
        return contentRoot;
    }

    /**
     * @inheritDoc
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @inheritDoc
     */
    @Auditable
    public String getImagePathByIdUKey(long idUKey, User user) {
        MarriageRegister mr = getByIdUKey(idUKey, user);
        if (mr != null) {
            return mr.getScannedImagePath();
        }
        return null;
    }
}
