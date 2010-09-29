package lk.rgd.crs.core.service;

import lk.rgd.crs.api.service.BirthAlterationService;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.dao.BirthAlterationDAO;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * The central service managing the CRS Birth Alteration process
 *
 * @author Indunil Moremada
 */
public class BirthAlterationServiceImpl implements BirthAlterationService {

    private static final Logger logger = LoggerFactory.getLogger(BirthAlterationServiceImpl.class);
    private final BirthAlterationDAO birthAlterationDAO;
    private final BirthRegistrationService birthRegistrationService;

    public BirthAlterationServiceImpl(BirthAlterationDAO birthAlterationDAO, BirthRegistrationService birthRegistrationService) {
        this.birthAlterationDAO = birthAlterationDAO;
        this.birthRegistrationService = birthRegistrationService;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addBirthAlteration(BirthAlteration ba, User user) {
        logger.debug("adding new birth alteration");
        validateAccessOfUser(ba, user);
        birthAlterationDAO.addBirthAlteration(ba, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateBirthAlteration(BirthAlteration ba, User user) {
        //todo validations
        logger.debug("Attempt to edit birth alteration record : {}", ba.getIdUKey());
        validateAccessOfUser(ba, user);
        BirthAlteration existing = birthAlterationDAO.getById(ba.getIdUKey());
        validateAccessOfUser(existing, user);
        birthAlterationDAO.updateBirthAlteration(ba, user);
        logger.debug("Saved edit changes to birth alteration record : {}  in data entry state", ba.getIdUKey());
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteBirthAlteration(long idUKey, User user) {
        //todo validations
        logger.debug("Attempt to delete birth alteration record : {}", idUKey);
        BirthAlteration ba = birthAlterationDAO.getById(idUKey);
        validateAccessOfUser(ba, user);
        birthAlterationDAO.deleteBirthAlteration(idUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public BirthAlteration getById(long idUKey, User user) {
        logger.debug("loading birth alteration record : {}", idUKey);
        BirthAlteration ba = birthAlterationDAO.getById(idUKey);
        validateAccessOfUser(ba, user);
        return ba;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void approveBirthAlteration(BirthAlteration ba, Hashtable<Integer, Boolean> fieldsToBeApproved,
                                       boolean appStatus, User user) {
        logger.debug("Attempt to approve birth alteration record : {} ", ba.getIdUKey());
        validateAccessOfUser(ba, user);
        BirthAlteration existing = birthAlterationDAO.getById(ba.getIdUKey());
        validateAccessOfUser(existing, user);

        if (!user.isAuthorized(Permission.APPROVE_BIRTH_ALTERATION)) {
            handleException("User : " + user.getUserId() + " is not allowed to approve/reject birth alteration",
                    ErrorCodes.PERMISSION_DENIED);
        }

        Enumeration<Integer> fieldList = fieldsToBeApproved.keys();
        while (fieldList.hasMoreElements()) {
            Integer aKey = fieldList.nextElement();
            if (fieldsToBeApproved.get(aKey) == true) {
                logger.debug("setting status as approval for the alteration statement");
                existing.getApprovalStatuses().set(aKey, WebConstants.BIRTH_ALTERATION_APPROVE_TRUE);

            }
            /*else {
                handleException("Cannot approve alteration according to the alteration statement  : " + ba.getIdUKey() +
                        " Illegal state : Approved", ErrorCodes.ILLEGAL_STATE);
            }*/
        }
        if (appStatus) {
            existing.setStatus(BirthAlteration.State.FULLY_APPROVED);
        }
        existing.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
        existing.getLifeCycleInfo().setApprovalOrRejectUser(user);
        birthAlterationDAO.updateBirthAlteration(existing, user);
        logger.debug("Updated birth alteration : {}", existing.getIdUKey());
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthAlteration> getApprovalPendingByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, User user) {
        //todo check this by after adding data 
        if (logger.isDebugEnabled()) {
            logger.debug("Get birth alteration pending approval by DSDivision ID : " + dsDivision.getDsDivisionUKey()
                    + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        validateAccessToDSDivison(dsDivision, user);
        List<BirthAlteration> alterationList = birthAlterationDAO.getBulkOfAlterationByDSDivision(dsDivision, pageNo, noOfRows);
        return getApprovalPendingAlterations(alterationList);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthAlteration> getApprovalPendingByBDDivision
            (BDDivision bdDivision, int pageNo, int noOfRows, User user) {
        //todo check this by after adding data
        if (logger.isDebugEnabled()) {
            logger.debug("Get birth alteration pending approval by BDDivision ID : " + bdDivision.getBdDivisionUKey()
                    + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        validateAccessToBDDivision(user, bdDivision);
        List<BirthAlteration> alterationList = birthAlterationDAO.getBulkOfAlterationByBDDivision(bdDivision, pageNo, noOfRows);
        return getApprovalPendingAlterations(alterationList);
    }

    private List<BirthAlteration> getApprovalPendingAlterations(List<BirthAlteration> alterationList) {
        List<BirthAlteration> pendingApprovalList = new ArrayList<BirthAlteration>();
        Boolean alreadyAdded;

        for (BirthAlteration ba : alterationList) {
            pendingApprovalList.add(ba);
            /* alreadyAdded = false;
            Alteration27 alt27 = ba.getAlt27();
            if (alt27 != null) {
                if (!(alt27.isFullNameEnglishApproved() && alt27.isFullNameOfficialLangApproved())) {
                    pendingApprovalList.add(ba);
                    alreadyAdded = true;
                }
            }
            Alteration27A alt27A = ba.getAlt27A();
            if (alt27A != null) {
                BitSet bs = alt27A.getApprovalStatuses();
                if (!alreadyAdded) {
                    for (int i = 1; i <= bs.size(); i++) {
                        if (!bs.get(i)) {
                            pendingApprovalList.add(ba);
                            alreadyAdded = true;
                        }
                    }
                }
            }
            if (!alreadyAdded) {
                BitSet bs = ba.getAlt52_1().getApprovalStatuses();
                logger.debug("Size of the bit set of the Alt52_1 is :{}",bs.size());
                for (int i = 1; i <= bs.size(); i++) {
                    if (!bs.get(i)) {
                        pendingApprovalList.add(ba);
                        alreadyAdded = true;
                    }
                }
            }*/
        }

        return pendingApprovalList;
    }

    private void validateAccessOfUser(BirthAlteration ba, User user) {
        if (ba.getAlt52_1() != null) {
            BDDivision bdDivision = ba.getAlt52_1().getBirthDivision();
            validateAccessToBDDivision(user, bdDivision);
        } else {
            BirthDeclaration bdf = birthRegistrationService.getById(ba.getBdId(), user);
            validateAccessToBDDivision(user, bdf.getRegister().getBirthDivision());
        }
    }

    private void validateAccessToBDDivision(User user, BDDivision bdDivision) {
        if (!(User.State.ACTIVE == user.getStatus()
                &&
                (Role.ROLE_ARG.equals(user.getRole().getRoleId())
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

    private void validateAccessToDSDivison(DSDivision dsDivision, User user) {
        if (!(User.State.ACTIVE == user.getStatus() &&
                (Role.ROLE_ARG.equals(user.getRole().getRoleId())
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
    public BirthAlteration getActiveRecordByBDDivisionAndSerialNo(BDDivision bdDivision, long alterationSerialNo, User user,boolean isAlt52_1) {
        logger.debug("Get active Birth Alteration record by BDDivision ID : {} and Serial No : {}", bdDivision.getBdDivisionUKey(), alterationSerialNo);

        BirthAlteration ba = birthAlterationDAO.getActiveRecordByBDDivisionAndSerialNo(bdDivision,alterationSerialNo,isAlt52_1);
        // does the user have access to the BA (i.e. check district and DS division)
        //calling validate access iff ba is not null otherwise it throws null pointer exception
        if (ba != null)
            validateAccessOfUser(ba,user);
        return ba;
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }
}
