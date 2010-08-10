package lk.rgd.crs.core.service;

import lk.rgd.crs.api.service.DeathRegisterService;
import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.common.api.domain.User;
import lk.rgd.ErrorCodes;

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
        logger.debug("adding new death registration");
        businessValidations(deathRegistration);
        DeathRegister dr = getByDeathSerialNo(deathRegistration.getDeath().getDeathSerialNo(), user).get(0);
        if (dr != null) {
            handleException("can not add death registration " + deathRegistration.getIdUKey() +
                " deathRegistration number already exists : " + deathRegistration.getStatus(), ErrorCodes.ENTITY_ALREADY_EXIST);
        }
        deathRegisterDAO.addDeathRegistration(deathRegistration);
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
    public List<DeathRegister> getByDeathSerialNo(String deathSerialNo, User user) {
        //todo after finalizing the requirements has to be modified whether to return a single entry or list
        try {
            return deathRegisterDAO.getByDeathSerialNo(deathSerialNo);
        } catch (NoResultException e) {
            logger.error("No result found", e);
            return new ArrayList();
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
}
