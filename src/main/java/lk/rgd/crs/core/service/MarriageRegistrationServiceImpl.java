package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.MarriageRegistrationDAO;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.MarriageNotice;
import lk.rgd.crs.api.domain.MarriageRegister;
import lk.rgd.crs.api.service.MarriageRegistrationService;
import lk.rgd.crs.core.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public MarriageRegistrationServiceImpl(MarriageRegistrationDAO marriageRegistrationDAO, MarriageRegistrationValidator marriageRegistrationValidator) {
        this.marriageRegistrationDAO = marriageRegistrationDAO;
        this.marriageRegistrationValidator = marriageRegistrationValidator;
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
        marriageRegistrationDAO.addMarriageNotice(notice, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addMarriageRegister(MarriageRegister marriageRegister, User user) {
        marriageRegistrationDAO.addMarriageNotice(marriageRegister, user);
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
        return removingAccessDeniedNoticesFromList(results, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getMarriageNoticePendingApprovalByMRDivisionAndSerial(MRDivision mrDivision, long serialNo
        , boolean active, User user) {
        logger.debug("Get active record by MRDivision : {} and Serial No : {}", mrDivision.getMrDivisionUKey(), serialNo);
        List<MarriageRegister> results = marriageRegistrationDAO.getNoticeByMRDivisionAndSerialNo(mrDivision, serialNo, true);
        return removingAccessDeniedNoticesFromList(results, user);
    }

    private List<MarriageRegister> removingAccessDeniedNoticesFromList(List<MarriageRegister> registerList, User user) {
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
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
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
        checkUserPermission(Permission.EDIT_MARRIAGE, ErrorCodes.PERMISSION_DENIED, "edit  marriage register", user);
        marriageRegistrationDAO.updateMarriageRegister(marriageRegister, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSecondMarriageNotice(MarriageRegister notice, boolean isMale, User user) {
        logger.debug("attempt to add a second notice for existing record : idUKey : {}", notice.getIdUKey());
        checkUserPermission(Permission.ADD_MARRIAGE, ErrorCodes.PERMISSION_DENIED, "add second notice to marriage register", user);
        updateMarriageRegister(notice, user);
    }

    /**
     * @inheritDoc <br> notes :
     * <u>delete operation works as follows </u>
     * there are three cases in removing a marriage notice
     * case 1: isBothSubmitted is true that means only one notice is available for delete
     * in that case we can simple remove the data base row
     * case 2 : isBothSubmitted is false that means there can be more than one marriage notices(at most 2)
     * case 2.1:
     * having only one marriage notice is available(it could be male party submitted one or female party submitted one)
     * in this case also we can just remove data base row
     * <p/>
     * case 2.2 : there are two notices are remaining in the marriage register row so we cannot simple remove the data
     * base row because it is removing the other notice as well.
     * So we have to update the data base row for that removing
     * <i>as an example :
     * if both male and female party submitted notices are available and you just need to remove female party
     * submitted notice in that case we have to update the data base row by removing female party notice related columns
     * note:states regarding to removal
     * case 1:  record must be in DATA_ENTRY
     * for removal of male notice record must be in either DATA_ENTRY or FEMALE_NOTICE_APPROVE state
     * for removal of female notice record must in either DATA_ENTRY or MALE_NOTICE_APPROVE state
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteMarriageNotice(long idUKey, MarriageNotice.Type noticeType, User user) {
        logger.debug("attempt to remove marriage notice : idUKey : {} and notice type : {}", idUKey, noticeType);
        checkUserPermission(Permission.DELETE_MARRIAGE, ErrorCodes.PERMISSION_DENIED, "delete marriage notice ", user);
        MarriageRegister notice = marriageRegistrationDAO.getByIdUKey(idUKey);
        if (notice == null) {
            handleException("cannot find record for approval" + idUKey, ErrorCodes.CAN_NOT_FIND_MARRIAGE_NOTICE);
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
     * @inheritDoc<br> <h3><i><b>approving process work as follow</i></b> </h3>
     * <p>
     * if the <b>notice is single</b> that means (both parties only submit one marriage notice) record <b><u>must</b></u>
     * be in DATA_ENTRY state and after approving notice change it's state in to NOTICE_APPROVED state that mean register
     * record is completely approved and editing for that notice is not allowed </p>
     * <p/>
     * else
     * there are two approving cases<br>
     * <i>(do not care about how many notices are available there can be 1 or 2 notices)</i>
     * </p>
     * case 1;
     * no notice has been approved,
     * as an example there are 2 notices available but both are not approved
     * assume we are trying to approve MALE_NOTICE
     * in this case this method change state of the marriage notice(register) into MALE_NOTICE_APPROVED and same
     * same scenario for approving FEMALE_NOTICE(actually vise-versa)
     * case 2:
     * one notice has been approved
     * assume case 1 is completed that mean MALE_NOTICE is approved and register in state MALE_NOTICE_APPROVED
     * and now we are trying to approve FEMALE_NOTICE
     * in this scenario we change notice state in to NOTICE_APPROVED state directly <b><u>not in to  FEMALE_NOTICE_APPROVED
     * now notice is fully approved
     * <p/>
     * note:if any case change its state in to notice approved it means this is the only active notice that can be have
     * for that couple.
     * the other fact we have to consider when approving marriage notice is weather other party has approved
     * (not the license requesting party)
     * it is looks like this
     * <p/>
     * if(SINGLE NOTICE)
     * no need to check
     * else
     * if(male party request license)
     * then
     * to approve male notice and complete approval female party must be approved
     * and vise-versa
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void approveMarriageNotice(long idUKey, MarriageNotice.Type type, User user) {
        logger.debug("attempt to approve marriage notice with idUKey : {} and notice type : {}", idUKey, type);
        //todo check way to improve readability and maintainability :( :( (amith)
        //check is user has permission to perform this task
        checkUserPermission(Permission.APPROVE_MARRIAGE, ErrorCodes.PERMISSION_DENIED, "approve marriage notice", user);
        MarriageRegister existingNotice = marriageRegistrationDAO.getByIdUKey(idUKey);
        //check is there a existing record for approving
        if (existingNotice == null) {
            handleException("cannot find record for approval" + idUKey, ErrorCodes.CAN_NOT_FIND_MARRIAGE_NOTICE);
        }
        //check is user has permission to deal with this marriage notice
        checkUserPermissionForDeleteApproveAndRejectNotice(existingNotice, type, user);
        //check is pre request are full filled before approve
        isNoticeAllowedToApprove(existingNotice, type);

        if (existingNotice.isSingleNotice()) {
            //directly change state in to NOTICE_APPROVED
            if (existingNotice.getState() == MarriageRegister.State.DATA_ENTRY) {
                existingNotice.setState(MarriageRegister.State.NOTICE_APPROVED);
            } else {
                handleException("unable to approve single :" + existingNotice.isSingleNotice() + "notice type:" + type +
                    ",idUKey" + idUKey, ErrorCodes.INVALID_STATE_FOR_APPROVAL);
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
            } else if ((existingNotice.getState() == MarriageRegister.State.MALE_NOTICE_APPROVED) ||
                (existingNotice.getState() == MarriageRegister.State.FEMALE_NOTICE_APPROVED)) {
                //case 2     change state in to NOTICE_APPROVED
                existingNotice.setState(MarriageRegister.State.NOTICE_APPROVED);
            } else {
                handleException("unable to approve single :" + existingNotice.isSingleNotice() + "notice type:" + type +
                    ",idUKey" + idUKey, ErrorCodes.INVALID_STATE_FOR_APPROVAL);
            }
        }
        //now we change state in to approving but if we changed state in to notice approve state there can't be another
        //record that have same state for same male party and female party pin numbers
        checkExistingActiveApprovedNotices(existingNotice, user);
        marriageRegistrationDAO.updateMarriageRegister(existingNotice, user);
        logger.debug("successfully  approved marriage notice with idUKey : {} and notice type : {}", idUKey, type);
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
    public List<MarriageRegister> getMarriageRegisterByPINorNIC(String pinOrNic, boolean active, User user) {
        logger.debug("Attempt to get marriage registers results for identification number : {} ", pinOrNic);
        List<MarriageRegister> results =
            marriageRegistrationDAO.getByStateAndPINorNIC(MarriageRegister.State.REG_DATA_ENTRY, pinOrNic, active);
        for (MarriageRegister reg : results) {
            if (!checkUserAccessPermissionToMarriageRecord(reg, user)) {
                results.remove(reg);
            }
        }
        return results;
    }

    private void checkUserPermission(int permissionBit, int errorCode, String msg, User user) {
        if (!user.isAuthorized(permissionBit)) {
            handleException("User : " + user.getUserId() + " is not allowed to " + msg, errorCode);
        }
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
                if (!register.isSingleNotice() && register.isLicenseRequestByMale() &&
                    (register.getState() != MarriageRegister.State.FEMALE_NOTICE_APPROVED)) {
                    //that means state must ne on FEMALE_NOTICE approved or
                    check = true;
                }
                break;
            case FEMALE_NOTICE:
                if (!register.isSingleNotice() && register.isLicenseRequestByMale() &&
                    (register.getState() == MarriageRegister.State.MALE_NOTICE_APPROVED)) {
                    //that means state must be in MALE_NOTICE
                    check = true;
                }
                break;
            default:
        }

        if (check) {
            handleException("unable to approve single :" + register.isSingleNotice() + "notice type:" + type
                + "License request by male :" + register.isLicenseRequestByMale() + ",idUKey" +
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
}
