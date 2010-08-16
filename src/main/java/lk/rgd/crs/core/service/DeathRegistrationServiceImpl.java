package lk.rgd.crs.core.service;

import lk.rgd.crs.api.service.DeathRegistrationService;
import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Role;
import lk.rgd.ErrorCodes;
import lk.rgd.Permission;

import java.util.List;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Indunil Moremada
 */
public class DeathRegistrationServiceImpl implements DeathRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(DeathRegistrationService.class);
    private final DeathRegisterDAO deathRegisterDAO;

    DeathRegistrationServiceImpl(DeathRegisterDAO deathRegisterDAO) {
        this.deathRegisterDAO = deathRegisterDAO;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addLateDeathRegistration(DeathRegister deathRegistration, User user) {
        logger.debug("adding late/missing death registration");
        if (deathRegistration.getDeathType() != DeathRegister.Type.LATE && deathRegistration.getDeathType() != DeathRegister.Type.MISSING) {
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
        if (deathRegistration.getDeathType() != DeathRegister.Type.NORMAL && deathRegistration.getDeathType() != DeathRegister.Type.SUDDEN) {
            handleException("Invalid death type : " + deathRegistration.getDeathType(), ErrorCodes.ILLEGAL_STATE);
        }
        addDeathRegistration(deathRegistration, user);
        logger.debug("added a normal/sudden registration with idUKey : {} ", deathRegistration.getIdUKey());
    }

    private void addDeathRegistration(DeathRegister deathRegistration, User user) {
        validateAccessToBDDivision(user, deathRegistration.getDeath().getDeathDivision());
        // has this serial number been used already?
        DeathRegister existing = deathRegisterDAO.getActiveRecordByBDDivisionAndDeathSerialNo(deathRegistration.getDeath().getDeathDivision(),
            deathRegistration.getDeath().getDeathSerialNo());
        if (existing != null) {
            handleException("can not add death registration " + existing.getIdUKey() +
                " deathRegistration number already exists : " + existing.getStatus(), ErrorCodes.ENTITY_ALREADY_EXIST);
        }
        deathRegistration.setStatus(DeathRegister.State.DATA_ENTRY);
        deathRegisterDAO.addDeathRegistration(deathRegistration);
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
        deathRegisterDAO.updateDeathRegistration(deathRegistration);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public DeathRegister getById(long deathRegisterIdUKey, User user) {
        logger.debug("Load death registration record : {}", deathRegisterIdUKey);
        DeathRegister deathRegister;
        deathRegister = deathRegisterDAO.getById(deathRegisterIdUKey);
        validateAccessToBDDivision(user, deathRegister.getDeath().getDeathDivision());
        return deathRegister;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void approveDeathRegistration(long deathRegisterIdUKey, User user) {
        logger.debug("attempt to approve death registration record : {} ", deathRegisterIdUKey);
        setApprovalStatus(deathRegisterIdUKey, user, DeathRegister.State.APPROVED);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void rejectDeathRegistration(long deathRegisterIdUKey, User user) {
        logger.debug("attempt to reject death registration record : {}", deathRegisterIdUKey);
        setApprovalStatus(deathRegisterIdUKey, user, DeathRegister.State.REJECTED);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void markDeathCertificateAsPrinted(long deathRegisterIdUKey, User user) {
        logger.debug("requested to mark death certificate as printed for the record : {} ", deathRegisterIdUKey);
        DeathRegister dr = deathRegisterDAO.getById(deathRegisterIdUKey);
        validateAccessToBDDivision(user, dr.getDeath().getDeathDivision());
        if (DeathRegister.State.APPROVED != dr.getStatus()) {
            handleException("Cannot change status , " + dr.getIdUKey() +
                " Illegal state : " + dr.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        dr.setStatus(DeathRegister.State.DEATH_CERTIFICATE_PRINTED);
        deathRegisterDAO.updateDeathRegistration(dr);
    }

    private void setApprovalStatus(long idUKey, User user, DeathRegister.State state) {
        DeathRegister dr = deathRegisterDAO.getById(idUKey);
        if (!user.isAuthorized(Permission.APPROVE_DEATH)) {
            handleException("User : " + user.getUserId() + " is not allowed to approve/reject death registration",
                ErrorCodes.PERMISSION_DENIED);
        }
        if (DeathRegister.State.DATA_ENTRY == dr.getStatus()) {
            validateAccessToBDDivision(user, dr.getDeath().getDeathDivision());
            dr.setStatus(state);
        } else {
            handleException("Cannot approve/reject death registration " + dr.getIdUKey() +
                " Illegal state : " + dr.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        deathRegisterDAO.updateDeathRegistration(dr);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteDeathRegistration(long deathRegiserIdUKey, User user) {
        logger.debug("attempt to delete death registration record : {}", deathRegiserIdUKey);
        DeathRegister dr = deathRegisterDAO.getById(deathRegiserIdUKey);
        validateAccessToBDDivision(user, dr.getDeath().getDeathDivision());
        if (DeathRegister.State.DATA_ENTRY != dr.getStatus()) {
            handleException("Cannot delete death registraion " + deathRegiserIdUKey +
                " Illegal state : " + dr.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        deathRegisterDAO.deleteDeathRegistration(deathRegisterDAO.getById(deathRegiserIdUKey));
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
        validateAccessToBDDivision(user, deathDivision);
        return deathRegisterDAO.getPaginatedListForState(deathDivision, pageNo, noOfRows, status);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getByBDDivisionAndRegistrationDateRange(BDDivision deathDivision,
                                                                       Date startDate, Date endDate, int pageNo, int noOfRows, User user) {
        validateAccessToBDDivision(user, deathDivision);
        return deathRegisterDAO.getByBDDivisionAndRegistrationDateRange(deathDivision, startDate, endDate, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getPaginatedListForAll(BDDivision deathDivision, int pageNo, int noOfRows, User user) {
        logger.debug("Get all death registrations   Page : {}  with number of rows per page : {} ", pageNo, noOfRows);
        validateAccessToBDDivision(user, deathDivision);
        return deathRegisterDAO.getPaginatedListForAll(deathDivision, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public DeathRegister getByBDDivisionAndDeathSerialNo(BDDivision bdDivision, long deathSerialNo, User user) {
        DeathRegister dr = null;
        dr = deathRegisterDAO.getActiveRecordByBDDivisionAndDeathSerialNo(bdDivision, deathSerialNo);
        validateAccessToBDDivision(user, dr.getDeath().getDeathDivision());
        return dr;
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }

    private void businessValidations(DeathRegister deathRegister, User user) {
        validateAccessToBDDivision(user, deathRegister.getDeath().getDeathDivision());
        if (deathRegister.getStatus() != DeathRegister.State.DATA_ENTRY) {
            handleException("can not update death registration " + deathRegister.getIdUKey() +
                " Illegal State : " + deathRegister.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }

    }

    private void validateAccessToBDDivision(User user, BDDivision bdDivision) {
        final String role = user.getRole().getRoleId();
        if (!(User.State.ACTIVE == user.getStatus()
            &&
            ((Role.ROLE_RG.equals(role) || Role.ROLE_ARG.equals(role) || Role.ROLE_ADR.equals(role))
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
}
