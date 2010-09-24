package lk.rgd.crs.api.dao;

import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.domain.AdoptionOrder;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.DSDivision;

import java.util.List;
import java.util.Date;

/**
 * @author Indunil Moremada
 * @author amith jayasekara
 */
public interface DeathRegisterDAO {

    /**
     * Add a death registration
     *
     * @param dr   the death register to be added
     * @param user the user who perform the action
     */
    public void addDeathRegistration(DeathRegister dr, User user);

    /**
     * update death registration
     *
     * @param dr   the death registration to be updated
     * @param user the user who perform the action
     */
    public void updateDeathRegistration(DeathRegister dr, User user);

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
     * @param dr   the entry to be removed
     * @param user the user who perform the action
     */
    public void deleteDeathRegistration(DeathRegister dr, User user);

    /**
     * Return all records - for indexing only
     *
     * @return all records
     */
    public List<DeathRegister> findAll();

    /**
     * Get the list of death registrations for a given state based on given death division
     *
     * @param deathDivision the death division
     * @param pageNo        the page number for the results required (start from 1)
     * @param noOfRows      number of rows to return per page
     * @param status        state of the DeathRegister
     * @return list of DeathRegister objects which are in the given state
     */
    public List<DeathRegister> getPaginatedListForState(BDDivision deathDivision, int pageNo, int noOfRows, DeathRegister.State status);

    /**
     * Get the list of all the death registrations which are belonging to the given death division
     *
     * @param deathDivision the death division
     * @param pageNo        the page number for the results required(start from 1)
     * @param noOfRows      number of rows to return per page
     * @return list of DeathRegister objects
     */
    public List<DeathRegister> getPaginatedListForAll(BDDivision deathDivision, int pageNo, int noOfRows);

    /**
     * get a death registration based on requested serial number and BDDivision
     *
     * @param bdDivision    the birth death declaration division under which the death serial number should be searched
     * @param deathSerialNo serial number of the death registration
     * @return DeathRegister object
     */
    public DeathRegister getActiveRecordByBDDivisionAndDeathSerialNo(BDDivision bdDivision, long deathSerialNo);

    /**
     * Returns a limited set of DeathRegister for selected BD Division and selected range of registration dates.
     * Results are ordered on the descending registration date. pageNo  and noOfRows used for pagination
     *
     * @param deathDivision the death division
     * @param startDate     starting date of the range
     * @param endDate       ending date of the range
     * @param pageNo        page number
     * @param noOfRows      number of rows
     * @return the death registration results
     */
    public List<DeathRegister> getByBDDivisionAndRegistrationDateRange(BDDivision deathDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows);

    /**
     * Get the list of death registrations for a given state based on given dsDivision
     *
     * @param dsDivision the divisional Secretariat
     * @param pageNo     the page number for the results required (start from 1)
     * @param noOfRows   number of rows to return per page
     * @param status     state of the DeathRegister
     * @return list of DeathRegister objects which are in the given state
     */
    public List<DeathRegister> getPaginatedListForStateByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, DeathRegister.State status);

    /**
     * Get the list of all the death registrations which are belonging to the given dsDivision
     *
     * @param dsDivision the divisional Secretariat
     * @param pageNo     the page number for the results required(start from 1)
     * @param noOfRows   number of rows to return per page
     * @return list of DeathRegister objects
     */
    public List<DeathRegister> getPaginatedListForAllByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows);

    /**
     * get all Death records witch have given death persons PIN or NIC number
     *
     * @param pinOrNIC PIN number or NIC number
     * @return list of DeathRegister entries witch include given pin
     */
    public List<DeathRegister> getDeathRegisterByDeathPersenPINorNIC(String pinOrNIC);
}


