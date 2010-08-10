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
     * Get the list of death registrations for a given state
     *
     * @param pageNo   the page number for the results required (start from 1)
     * @param noOfRows number of rows to return per page
     * @param status   state of the DeathRegister
     * @return list of DeathRegister objects which are in the given state
     */
    public List<DeathRegister> getPaginatedListForState(int pageNo, int noOfRows, DeathRegister.State status);

    /**
     * Get the list of all the death registrations
     *
     * @param pageNo   the page number for the results required(start from 1)
     * @param noOfRows number of rows to return per page
     * @return list of DeathRegister objects
     */
    public List<DeathRegister> getPaginatedListForAll(int pageNo, int noOfRows);

    /**
     * get a list of death registration based on requested serial number
     *
     * @param deathSerialNo serial number of the death registration
     * @return list of DeathRegister objects
     */
    public List <DeathRegister> getByDeathSerialNo(String deathSerialNo);
}
