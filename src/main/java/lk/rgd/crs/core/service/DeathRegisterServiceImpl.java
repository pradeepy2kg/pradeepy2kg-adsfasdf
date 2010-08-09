package lk.rgd.crs.core.service;

import lk.rgd.crs.api.service.DeathRegisterService;
import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.common.api.domain.User;

/**
 * @author Indunil Moremada
 */
public class DeathRegisterServiceImpl implements DeathRegisterService {


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
}
