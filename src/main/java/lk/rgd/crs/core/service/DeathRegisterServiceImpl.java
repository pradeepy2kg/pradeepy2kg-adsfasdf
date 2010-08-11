package lk.rgd.crs.core.service;

import lk.rgd.crs.api.service.DeathRegisterService;
import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Role;
import lk.rgd.ErrorCodes;
import lk.rgd.Permission;

import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.NoResultException;

/**
 * @author Indunil Moremada
 */
public class DeathRegisterServiceImpl implements DeathRegisterService {

    private static final Logger logger = LoggerFactory.getLogger(DeathRegisterService.class);
    private final DeathRegisterDAO deathRegisterDAO;


    DeathRegisterServiceImpl(DeathRegisterDAO deathRegisterDAO) {
        this.deathRegisterDAO = deathRegisterDAO;
    }

    /**
     * @inheritDoc
     */
    public void addDeathRegistration(DeathRegister deathRegistration, User user) {
        //todo user validation
        logger.debug("adding new death registration");
        businessValidations(deathRegistration);
        DeathRegister dr = deathRegisterDAO.getByBDDivisionAndDeathSerialNo(deathRegistration.getDeath().getBirthDivision(), deathRegistration.getDeath().getDeathSerialNo());
        if (dr != null) {
            handleException("can not add death registration " + deathRegistration.getIdUKey() +
                " deathRegistration number already exists : " + deathRegistration.getStatus(), ErrorCodes.ENTITY_ALREADY_EXIST);
        }
        deathRegistration.setStatus(DeathRegister.State.DATA_ENTRY);
        deathRegisterDAO.addDeathRegistration(deathRegistration);
        logger.debug("added a new death registration with idUKey : {} ", deathRegistration.getIdUKey());
    }

    /**
     * @inheritDoc
     */
    public void updateDeathRegistration(DeathRegister deathRegistration, User user) {
        businessValidations(deathRegistration);
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
    public DeathRegister getById(long deathRegisterIdUKey, User user) {
        logger.debug("Load death registration record : {}", deathRegisterIdUKey);
        DeathRegister deathRegister = null;
        try {
            deathRegister = deathRegisterDAO.getById(deathRegisterIdUKey);
        } catch (NoResultException ignore) {
            logger.error("Requested entry not available ", ignore);
        }
        return deathRegister;
    }

    /**
     * @inheritDoc
     */
    public void approveDeathRegistration(long deathRegisterIdUKey, User user) {
        logger.debug("attempt to approve death registration record : {} ", deathRegisterIdUKey);
        setApprovalStatus(deathRegisterIdUKey, user, DeathRegister.State.APPROVED);
    }

    /**
     * @inheritDoc
     */
    public void rejectDeathRegistration(long deathRegisterIdUKey, User user) {
        logger.debug("attempt to reject death registration record : {}", deathRegisterIdUKey);
        setApprovalStatus(deathRegisterIdUKey, user, DeathRegister.State.REJECTED);
    }

    /**
     * @inheritDoc
     */
    public void markDeathCertificateAsPrinted(long deathRegisterIdUKey, User user) {
        logger.debug("requested to mark death certificate as printed for the record : {} ", deathRegisterIdUKey);
        DeathRegister dr = deathRegisterDAO.getById(deathRegisterIdUKey);
        if (DeathRegister.State.APPROVED != dr.getStatus()) {
            handleException("Cannot change status , " + dr.getIdUKey() +
                " Illegal state : " + dr.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        dr.setStatus(DeathRegister.State.DEATH_CERTIFICATE_PRINTED);
        deathRegisterDAO.updateDeathRegistration(dr);
    }

    private void setApprovalStatus(long idUKey, User user, DeathRegister.State state) {
        DeathRegister dr = deathRegisterDAO.getById(idUKey);
        if (DeathRegister.State.DATA_ENTRY == dr.getStatus()) {
            validateAccess(user);
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
    public void deleteDeathRegistration(long deathRegiserIdUKey, User user) {
        logger.debug("attempt to delete death registration record : {}", deathRegiserIdUKey);
        DeathRegister dr = deathRegisterDAO.getById(deathRegiserIdUKey);
        if (DeathRegister.State.DATA_ENTRY != dr.getStatus()) {
            handleException("Cannot delete death registraion " + deathRegiserIdUKey +
                " Illegal state : " + dr.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        deathRegisterDAO.deleteDeathRegistration(deathRegiserIdUKey);
    }

    /**
     * @inheritDoc
     */
    public List<DeathRegister> getPaginatedListForState(int pageNo, int noOfRows, DeathRegister.State status, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get death registrations with the state : " + status
                + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        try {
            return deathRegisterDAO.getPaginatedListForState(pageNo, noOfRows, status);
        } catch (NoResultException e) {
            logger.error("No result found", e);
            return new ArrayList();
        }
    }

    /**
     * @inheritDoc
     */
    public List<DeathRegister> getPaginatedListForAll(int pageNo, int noOfRows, User user) {
        logger.debug("Get all death registrations   Page : {}  with number of rows per page : {} ", pageNo, noOfRows);
        try {
            return deathRegisterDAO.getPaginatedListForAll(pageNo, noOfRows);
        } catch (NoResultException e) {
            logger.error("No result found ", e);
            return new ArrayList();
        }
    }

    /**
     * @inheritDoc
     */
    public DeathRegister getByBDDivisionAndDeathSerialNo(BDDivision bdDivision, String deathSerialNo, User user) {
        //todo after finalizing the requirements has to be modified whether to return a single entry or list
        try {
            return deathRegisterDAO.getByBDDivisionAndDeathSerialNo(bdDivision, deathSerialNo);
        } catch (NoResultException e) {
            logger.error("No result found", e);
            return null;
        }
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }

    private void businessValidations(DeathRegister deathRegister) {
        if (deathRegister.getStatus() != DeathRegister.State.DATA_ENTRY) {
            handleException("can not update death registration " + deathRegister.getIdUKey() +
                " Illegal State : " + deathRegister.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }

    }

    private void validateAccess(User user) {
        String role = user.getRole().getRoleId();
        if (!(User.State.ACTIVE == user.getStatus()) ||
            !(Role.ROLE_ARG.equals(role) || Role.ROLE_RG.equals(role))) {
            handleException("User : " + user.getUserId() + " of role : " + role +
                " is not allowed access to approve/reject an death registration : ", ErrorCodes.PERMISSION_DENIED);
        }

        if (!user.isAuthorized(Permission.APPROVE_DEATH)) {
            handleException("User : " + user.getUserId() + " is not allowed to approve/reject death registration",
                ErrorCodes.PERMISSION_DENIED);
        }
    }
}
