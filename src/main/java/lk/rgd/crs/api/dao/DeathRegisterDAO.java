package lk.rgd.crs.api.dao;

import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.crs.api.domain.BDDivision;

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
     * Get the list of death registrations for a given state based on given death division
     *
     * @param deathDivision the death division
     * @param pageNo   the page number for the results required (start from 1)
     * @param noOfRows number of rows to return per page
     * @param status   state of the DeathRegister
     * @return list of DeathRegister objects which are in the given state
     */
    public List<DeathRegister> getPaginatedListForState(BDDivision deathDivision,int pageNo, int noOfRows, DeathRegister.State status);

    /**
     * Get the list of all the death registrations which are belonging to the given death division
     *
     * @param deathDivision the death division
     * @param pageNo   the page number for the results required(start from 1)
     * @param noOfRows number of rows to return per page
     * @return list of DeathRegister objects
     */
    public List<DeathRegister> getPaginatedListForAll(BDDivision deathDivision,int pageNo, int noOfRows);

    /**
     * get a death registration based on requested serial number and BDDivision
     *
     * @param bdDivision    the birth death declaration division under which the death serial number should be searched
     * @param deathSerialNo serial number of the death registration
     * @return DeathRegister object
     */
    public DeathRegister getByBDDivisionAndDeathSerialNo(BDDivision bdDivision, String deathSerialNo);
}
