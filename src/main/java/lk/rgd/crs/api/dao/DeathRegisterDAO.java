package lk.rgd.crs.api.dao;

import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.domain.AdoptionOrder;

import java.util.List;

/**
 * @author Indunil Moremada
 */
public interface DeathRegisterDAO {

    /**
     * Add a death registration
     *
     * @param dr the death register to be added
     */
    public void addDeathRegistration(DeathRegister dr);

    /**
     * update death registration
     *
     * @param dr the death registration to be updated
     */
    public void updateDeathRegistration(DeathRegister dr);

    /**
     * returns the death registration object for a given IDUKey
     *
     * @param deathRegisterIdUKey IDUKey Death Registration Id
     * @return DeathRegister
     */
    public DeathRegister getById(long deathRegisterIdUKey);

    /**
     * remove a death registration
     *
     * @param deathRegistrationIdUKey the unique id of the death registration to be removed
     */
    public void deleteDeathRegistration(long deathRegistrationIdUKey);

    /**
     *
     * @param pageNo
     * @param noOfRows
     * @param status
     * @return
     */
    public List<DeathRegister> getPaginatedListForState(int pageNo, int noOfRows, DeathRegister.State status);

    /**
     * 
     * @param pageNo
     * @param noOfRows
     * @return
     */
    public List<DeathRegister> getPaginatedListForAll(int pageNo, int noOfRows);
}
