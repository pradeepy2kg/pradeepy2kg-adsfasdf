package lk.rgd.crs.api.service;

import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.common.api.domain.User;

/**
 * @author Indunil Moremada
 */
public interface DeathRegisterService {

    /**
     * Adds a death registration to the system. This is a Data Entry operation, and only data entry level validations
     * are performed at this stage.
     *
     * @param deathRegistration the death registration to be added
     * @param user              the user initiating the action
     */
    public void addDeathRegistration(DeathRegister deathRegistration, User user);

    /**
     * updated an existing Death registration
     *
     * @param deathRegistration the death registration to be updated
     * @param user              the user initiating the action
     */
    public void editDeathRegistration(DeathRegister deathRegistration, User user);

    /**
     * returns DeathRegister object for a given IDUKey
     *
     * @param deathRegisterIdUKey idUKey for the given death registration
     * @param user                user who initiating the request
     * @return DeathRegister
     */
    public DeathRegister getById(long deathRegisterIdUKey, User user);
}
