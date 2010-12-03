package lk.rgd.crs.core.service;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.MarriageRegistrationDAO;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.MarriageNotice;
import lk.rgd.crs.api.domain.MarriageRegister;
import lk.rgd.crs.api.domain.Witness;
import lk.rgd.crs.api.service.MarriageRegistrationService;
import lk.rgd.crs.core.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * implementation of the marriage registration service interface
 *
 * @author amith jayasekara
 * @author Chathuranga Withana
 *         todo check user permissions for performing tasks
 */
public class MarriageRegistrationServiceImpl implements MarriageRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(MarriageRegistrationServiceImpl.class);

    private final MarriageRegistrationDAO marriageRegistrationDAO;

    public MarriageRegistrationServiceImpl(MarriageRegistrationDAO marriageRegistrationDAO) {
        this.marriageRegistrationDAO = marriageRegistrationDAO;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addMarriageNotice(MarriageRegister notice, boolean isMale, User user) {
        logger.debug("adding new marriage notice :male pin number  {}", notice.getMale().getIdentificationNumberMale());
        //TODO check users permission to add marriage
        //persisting witness
        addMaleOrFemaleWitnesses(notice, isMale);
        marriageRegistrationDAO.addMarriageNotice(notice, user);
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
        return marriageRegistrationDAO.getPaginatedListForStateByDSDivision(dsDivision,
            MarriageRegister.State.DATA_ENTRY, pageNo, noOfRows, active);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getMarriageNoticePendingApprovalByPINorNIC(String pinOrNic, boolean active, User user) {
        logger.debug("Attempt to get marriage notice pending results for identification number : {} ", pinOrNic);
        List<MarriageRegister> results = marriageRegistrationDAO.getByStateAndPINorNIC(
            MarriageRegister.State.DATA_ENTRY, pinOrNic, active);
        for (MarriageRegister reg : results) {
            if (!checkUserAccessPermissionToMarriageRecord(reg, user)) {
                logger.debug("User : {} :does not have permission to edit/approve marriage record idUKey : {} ",
                    user.getUserName(), reg.getIdUKey());
                results.remove(reg);
            }
        }
        return results;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<MarriageRegister> getMarriageNoticePendingApprovalByMRDivisionAndSerial(MRDivision mrDivision, long serialNo,
        User user) {
        logger.debug("Get active record by MRDivision : {} and Serial No : {}", mrDivision.getMrDivisionUKey(), serialNo);
        List<MarriageRegister> results = marriageRegistrationDAO.getByMRDivisionAndSerialNo(mrDivision,
            MarriageRegister.State.DATA_ENTRY, serialNo, true);
        for (MarriageRegister mr : results) {
            if (!checkUserAccessPermissionToMarriageRecord(mr, user)) {
                results.remove(mr);
            }
        }
        return results;
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
        return marriageRegistrationDAO.getPaginatedListForStateByMRDivision(mrDivision,
            MarriageRegister.State.DATA_ENTRY, pageNo, noOfRows, active);
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
        //todo check user permissions
        logger.debug("attempt to update marriage register/notice record : idUKey : {}", marriageRegister.getIdUKey());
        if (marriageRegister.getState() == MarriageRegister.State.REG_DATA_ENTRY) {
            addWitness(marriageRegister.getWitness1());
            addWitness(marriageRegister.getWitness2());
        }
        marriageRegistrationDAO.updateMarriageRegister(marriageRegister, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSecondMarriageNotice(MarriageRegister notice, boolean isMale, User user) {
        logger.debug("attempt to add a second notice for existing record : idUKey : {}", notice.getIdUKey());
        addMaleOrFemaleWitnesses(notice, isMale);
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
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteMarriageNotice(long idUKey, MarriageNotice.Type noticeType, User user) {
        logger.debug("attempt to remove marriage notice : idUKey : {} and notice type : {}", idUKey, noticeType);
        //todo AMITH check user permission for removing data
        MarriageRegister notice = marriageRegistrationDAO.getByIdUKey(idUKey);
        boolean isBothSubmitted = notice.isBothPartySubmitted();
        if (isBothSubmitted) {
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
    public List<MarriageRegister> getMarriageRegisterPendingApprovalBySerialAndMRDivision(long serialNumber,
        MRDivision mrDivision, int pageNumber, int numOfRows, boolean active, User user) {
        logger.debug("attempt to get pending marriage register record for marriage serial number : {}" +
            " and MRDivision name : {} ", serialNumber, mrDivision.getEnDivisionName());
        //validating user  access to the MR division
        ValidationUtils.validateAccessToMRDivision(mrDivision, user);
        return marriageRegistrationDAO.getByMRDivisionAndSerialNo(mrDivision, MarriageRegister.State.REG_DATA_ENTRY,
            serialNumber, active);
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
                notice.setMaleNoticeWitness_1(null);
                notice.setMaleNoticeWitness_2(null);
                break;
            case FEMALE_NOTICE:
                notice.setMrDivisionOfFemaleNotice(null);
                notice.setSerialOfFemaleNotice(null);
                notice.setDateOfFemaleNotice(null);
                notice.setFemaleNoticeWitness_1(null);
                notice.setFemaleNoticeWitness_2(null);
        }

    }

    private void addWitness(Witness witness) {
        marriageRegistrationDAO.addWitness(witness);
    }

    private void addMaleOrFemaleWitnesses(MarriageRegister marriageRegister, boolean isMale) {
        marriageRegister.setWitness1(null);
        marriageRegister.setWitness2(null);
        if (isMale) {
            //persisting male witnesses
            if (marriageRegister.getFemaleNoticeWitness_1().getIdUKey() == 0 &
                marriageRegister.getFemaleNoticeWitness_2().getIdUKey() == 0) {
                //usage of the check (by default those object are created when marriage notice object create, in some cases
                // like there is a already added witness ,then we have to keep those witness objects other wise it can be
                // null)
                marriageRegister.setFemaleNoticeWitness_1(null);
                marriageRegister.setFemaleNoticeWitness_2(null);
            }
            marriageRegistrationDAO.addWitness(marriageRegister.getMaleNoticeWitness_1());
            marriageRegistrationDAO.addWitness(marriageRegister.getMaleNoticeWitness_2());

        } else {
            if (marriageRegister.getMaleNoticeWitness_1().getIdUKey() == 0 &
                marriageRegister.getMaleNoticeWitness_2().getIdUKey() == 0) {
                //persisting female notice witnesses
                marriageRegister.setMaleNoticeWitness_1(null);
                marriageRegister.setMaleNoticeWitness_2(null);
            }
            marriageRegistrationDAO.addWitness(marriageRegister.getFemaleNoticeWitness_1());
            marriageRegistrationDAO.addWitness(marriageRegister.getFemaleNoticeWitness_2());
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
}
