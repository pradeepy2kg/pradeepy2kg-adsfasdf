package lk.rgd.crs.core.service;

import lk.rgd.crs.api.service.BirthAlterationService;
import lk.rgd.crs.api.domain.BirthAlteration;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.dao.BirthAlterationDAO;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.ErrorCodes;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * The central service managing the CRS Birth Alteration process
 *
 * @author Indunil Moremada
 */
public class BirthAlterationServiceImpl implements BirthAlterationService {

    private static final Logger logger = LoggerFactory.getLogger(BirthAlterationServiceImpl.class);
    private final BirthAlterationDAO birthAlterationDAO;

    public BirthAlterationServiceImpl(BirthAlterationDAO birthAlterationDAO) {
        this.birthAlterationDAO = birthAlterationDAO;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addBirthAlteration(BirthAlteration ba, User user) {
        //todo set data entry state
        logger.debug("adding new birth alteration");
        validateAccessOfUser(ba, user);
        //ba.setStatus(BirthAlteration.State.DATA_ENTRY);
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
        //final BirthAlteration.State currentState = existing.getStatus();
        //if (currentState==BirthAlteration.State.DATA_ENTRY){
        birthAlterationDAO.updateBirthAlteration(ba, user);
        logger.debug("Saved edit changes to birth alteration record : {}  in data entry state", ba.getIdUKey());
        //} else
        /*{
           handleException("Cannot modify birth alteration : " + existing.getIdUKey() +
                " Illegal state : " + currentState, ErrorCodes.ILLEGAL_STATE);
        }*/
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
        /* final BirthAlteration.State currentState = ba.getStatus();
        if (currentState!=BirthAlteration.State.DATA_ENTRY){
        handleException("Cannot delete birth alteration : " + ba.getIdUKey() +
                " Illegal state : " + currentState, ErrorCodes.ILLEGAL_STATE);
        }*/
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
    public void approveBirthAlteration(BirthAlteration ba, boolean isAlteration27A, Hashtable<Integer, Boolean> fieldsToBeApproved, User user) {
        logger.debug("Attempt to approve birth alteration record : {} ", ba.getIdUKey());
        validateAccessOfUser(ba, user);
        BirthAlteration existing = birthAlterationDAO.getById(ba.getIdUKey());
        validateAccessOfUser(existing, user);

        Enumeration<Integer> fieldList = fieldsToBeApproved.keys();
        if (isAlteration27A) {
            while (fieldList.hasMoreElements()) {
                Integer aKey = fieldList.nextElement();
                if (fieldsToBeApproved.get(aKey) == true) {
                    if (existing.getAlt27A().getApprovalStatuses().get(aKey) == false) {
                        logger.debug("setting status as approval for the alteration statement 27A");
                        existing.getAlt27A().getApprovalStatuses().set(aKey, WebConstants.BIRTH_ALTERATION_APPROVE);
                    } else {
                        handleException("Cannot approve alteration according to the alteration statement 27A : " + ba.getIdUKey() +
                            " Illegal state : Approved", ErrorCodes.ILLEGAL_STATE);
                    }
                }
            }
        } else {
            while (fieldList.hasMoreElements()) {
                Integer aKey = fieldList.nextElement();
                if (fieldsToBeApproved.get(aKey) == true) {
                    if (existing.getAlt52_1().getApprovalStatuses().get(aKey) == false) {
                        logger.debug("setting status as approval for the alteration statement 52_1");
                        existing.getAlt52_1().getApprovalStatuses().set(aKey, WebConstants.BIRTH_ALTERATION_APPROVE);
                    } else {
                        handleException("Cannot approve alteration according to the alteration statement 52_1 : " + ba.getIdUKey() +
                            " Illegal state : Approved", ErrorCodes.ILLEGAL_STATE);
                    }
                }
            }
        }
        existing.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
        existing.getLifeCycleInfo().setApprovalOrRejectUser(user);
        birthAlterationDAO.updateBirthAlteration(existing, user);
        logger.debug("Updated birth alteration : {}", existing.getIdUKey());
    }

    private void validateAccessOfUser(BirthAlteration ba, User user) {
        if (ba != null) {
            BDDivision bdDivision = ba.getAlt52_1().getBirthDivision();
            validateAccessToBDDivision(user, bdDivision);
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

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }
}
