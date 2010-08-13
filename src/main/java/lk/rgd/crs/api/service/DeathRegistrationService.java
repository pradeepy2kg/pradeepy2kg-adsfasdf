package lk.rgd.crs.api.service;

import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.common.api.domain.User;

import java.util.List;
import java.util.Date;

/**
 * @author Indunil Moremada
 */
public interface DeathRegistrationService {

    /**
     * Adds a normal death registration to the system. This is a Data Entry operation,
     * and only data entry level validations are performed at this stage.
     * CRSRuntimeException will be thrown for un-authorized operations
     * and invalid data
     *
     * @param deathRegistration the death registration to be added
     * @param user              the user initiating the action
     */
    public void addNormalDeathRegistration(DeathRegister deathRegistration, User user);

    /**
     * Adds a late death registration to the system. This is a Data Entry operation,
     * and only data entry level validations are performed at this stage.
     * CRSRuntimeException will be thrown for invalid operations
     *
     * @param deathRegistration the late death registration to be added
     * @param user              the user initiating the action
     */
    public void addLateDeathRegistration(DeathRegister deathRegistration, User user);

    /**
     * updated an existing Death registration, CRSRuntimeException will be
     * thrown for un-authorized operations and invalid data
     *
     * @param deathRegistration the death registration to be updated
     * @param user              the user initiating the action
     */
    public void updateDeathRegistration(DeathRegister deathRegistration, User user);

    /**
     * returns DeathRegister object for a given IDUKey
     *
     * @param deathRegisterIdUKey idUKey for the given death registration
     * @param user                user who initiating the request
     * @return DeathRegister
     */
    public DeathRegister getById(long deathRegisterIdUKey, User user);

    /**
     * delete a Death registration based on requested Id
     *
     * @param deathRegiserIdUKey the unique id of the death registration to be removed
     * @param user               the user initiating the action
     */
    public void deleteDeathRegistration(long deathRegiserIdUKey, User user);

    /**
     * Get the list of death registrations for a given state based on given death division
     * CRSRuntimeException will be thrown for un-authorized operations
     *
     * @param deathDivision the death division
     * @param pageNo        the page number for the results required (start from 1)
     * @param noOfRows      number of rows to return per page
     * @param status        state of the DeathRegister
     * @param user          the user initiating the action
     * @return list of DeathRegister objects which are in the given state
     */
    public List<DeathRegister> getPaginatedListForState(BDDivision deathDivision, int pageNo, int noOfRows, DeathRegister.State status, User user);

    /**
     * Get the list of all the death registrations which are belonging to the given death division
     * CRSRuntimeException will be thrown for un-authorized operations
     *
     * @param deathDivision the death division
     * @param pageNo        the page number for the results required(start from 1)
     * @param noOfRows      number of rows to return per page
     * @param user          the user initiating the action
     * @return list of DeathRegister objects
     */
    public List<DeathRegister> getPaginatedListForAll(BDDivision deathDivision, int pageNo, int noOfRows, User user);

    /**
     * get a death registration based on requested serial number and BDDivision
     * CRSRuntimeException will be thrown for un-authorized operations
     *
     * @param bdDivision    the birth death declaration division under which the death serial number should be searched
     * @param user          the user initiating the action
     * @param deathSerialNo serial number of the death registration
     * @return DeathRegister object
     */
    public DeathRegister getByBDDivisionAndDeathSerialNo(BDDivision bdDivision, long deathSerialNo, User user);

    /**
     * Approve a death registration already captured by the DEO by an ADR or higher authority
     * CRSRuntimeException will be thrown for un-authorized operations
     *
     * @param deathRegisterIdUKey the unique id of the death registration which is to be approved
     * @param user                the user initiating the action
     */
    public void approveDeathRegistration(long deathRegisterIdUKey, User user);

    /**
     * Reject a death registration already captured by the DEO by an ADR or higher authority
     * CRSRuntimeException will be thrown for un-authorized operations
     *
     * @param deathRegisterIdUKey the unique id of the death registration which is to be rejected
     * @param user                the user initiating the action
     */
    public void rejectDeathRegistration(long deathRegisterIdUKey, User user);

    /**
     * mark that death certificate for the death registration as printed
     * CRSRuntimeException will be thrown for un-authorized operations
     *
     * @param deathRegisterIdUKey the unique id of the death registration
     * @param user                the user initiating the action
     */
    public void markDeathCertificateAsPrinted(long deathRegisterIdUKey, User user);

    /**
     * Returns a limited set of DeathRegister for selected BD Division and selected range of registration dates.
     * Results are ordered on the descending registration date. pageNo  and noOfRows used for pagination
     * CRSRuntimeException will be thrown for un-authorized operations
     *
     * @param deathDivision the death division
     * @param startDate     starting date of the range
     * @param endDate       ending date of the range
     * @param pageNo        page number
     * @param noOfRows      number of rows
     * @param user          user initiating the action
     * @return the death registration results
     */
    public List<DeathRegister> getByBDDivisionAndRegistrationDateRange(BDDivision deathDivision,
                                                                       Date startDate, Date endDate, int pageNo, int noOfRows, User user);
}
