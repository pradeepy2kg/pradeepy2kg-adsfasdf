package lk.rgd.crs.core.service;

import lk.rgd.crs.api.service.DeathRegisterService;
import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.common.api.domain.User;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        deathRegisterDAO.addDeathRegistration(deathRegistration);
    }

    /**
     * @inheritDoc
     */
    public void editDeathRegistration(DeathRegister deathRegistration, User user) {

    }

    /**
     * @inheritDoc
     */
    public DeathRegister getById(long deathRegisterIdUKey, User user) {
        return deathRegisterDAO.getById(deathRegisterIdUKey);
    }

    /**
     * @inheritDoc
     */
    public void deleteDeathRegistration(long deathRegiserIdUKey, User user) {
        deathRegisterDAO.deleteDeathRegistration(deathRegiserIdUKey);
    }

    /**
     * @inheritDoc
     */
    public List<DeathRegister> getPaginatedListForState(int pageNo, int noOfRows, DeathRegister.State status, User user) {
        return deathRegisterDAO.getPaginatedListForState(pageNo, noOfRows, status);
    }

    /**
     * @inheritDoc
     */
    public List<DeathRegister> getPaginatedListForAll(int pageNo, int noOfRows, User user) {
        return deathRegisterDAO.getPaginatedListForAll(pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    public List<DeathRegister> getByDeathSerialNo(String deathSerialNo, User user) {
        return deathRegisterDAO.getByDeathSerialNo(deathSerialNo);
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }
}
