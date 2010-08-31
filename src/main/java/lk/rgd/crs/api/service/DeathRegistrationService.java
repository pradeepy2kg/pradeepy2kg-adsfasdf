package lk.rgd.crs.api.service;

import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.DSDivision;

import java.util.List;
import java.util.Date;

/**
 * @author Indunil Moremada
 */
public interface DeathRegistrationService {

    /**
     * Adds a normal or missing death registration to the system. This is a Data Entry operation,
     * and only data entry level validations are performed at this stage.
     *
     * @param deathRegistration the death registration to be added
     * @param user              the user initiating the action
     * @throws lk.rgd.crs.CRSRuntimeException for un-authorized
     *                                        operations and invalid data
     */
    public void addNormalDeathRegistration(DeathRegister deathRegistration, User user);

    /**
     * Adds a late or sudden death registration to the system. This is a Data Entry operation,
     * and only data entry level validations are performed at this stage.
     *
     * @param deathRegistration the late death registration to be added
     * @param user              the user initiating the action
     * @throws lk.rgd.crs.CRSRuntimeException for un-authorized operations
     */
    public void addLateDeathRegistration(DeathRegister deathRegistration, User user);

    /**
     * updated an existing Death registration, CRSRuntimeException will be
     *
     * @param deathRegistration the death registration to be updated
     * @param user              the user initiating the action
     * @throws lk.rgd.crs.CRSRuntimeException for un-authorized
     *                                        operations and invalid data
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
     *
     * @param deathDivision the death division
     * @param pageNo        the page number for the results required (start from 1)
     * @param noOfRows      number of rows to return per page
     * @param status        state of the DeathRegister
     * @param user          the user initiating the action
     * @return list of DeathRegister objects which are in the given state
     * @throws lk.rgd.crs.CRSRuntimeException for un-authorized operations
     */
    public List<DeathRegister> getPaginatedListForState(BDDivision deathDivision, int pageNo, int noOfRows, DeathRegister.State status, User user);

    /**
     * Get the list of all the death registrations which are belonging to the given death division
     *
     * @param deathDivision the death division
     * @param pageNo        the page number for the results required(start from 1)
     * @param noOfRows      number of rows to return per page
     * @param user          the user initiating the action
     * @return list of DeathRegister objects
     * @throws lk.rgd.crs.CRSRuntimeException for un-authorized operations
     */
    public List<DeathRegister> getPaginatedListForAll(BDDivision deathDivision, int pageNo, int noOfRows, User user);

    /**
     * get a death registration based on requested serial number and BDDivision
     *
     * @param bdDivision    the birth death declaration division under which the death serial number should be searched
     * @param user          the user initiating the action
     * @param deathSerialNo serial number of the death registration
     * @return DeathRegister object
     * @throws lk.rgd.crs.CRSRuntimeException for un-authorized operations
     */
    public DeathRegister getByBDDivisionAndDeathSerialNo(BDDivision bdDivision, long deathSerialNo, User user);

    /**
     * Approve a death registration already captured by the DEO by an ADR or higher authority
     * <p/>
     * //@param deathRegisterIdUKey the unique id of the death registration which is to be approved
     *
     * @param user the user initiating the action
     * @throws lk.rgd.crs.CRSRuntimeException for un-authorized operations
     */
    public List<UserWarning> approveDeathRegistration(long deathRegisterIdUKey, User user);

    /**
     * Reject a death registration already captured by the DEO by an ADR or higher authority
     *
     * @param deathRegisterIdUKey the unique id of the death registration which is to be rejected
     * @param user                the user initiating the action
     * @throws lk.rgd.crs.CRSRuntimeException for un-authorized operations
     */
    public void rejectDeathRegistration(long deathRegisterIdUKey, User user);

    /**
     * mark that death certificate for the death registration as printed
     *
     * @param deathRegisterIdUKey the unique id of the death registration
     * @param user                the user initiating the action
     * @throws lk.rgd.crs.CRSRuntimeException for un-authorized operations
     */
    public void markDeathCertificateAsPrinted(long deathRegisterIdUKey, User user);

    /**
     * Returns a limited set of DeathRegister for selected BD Division and selected range of registration dates.
     * Results are ordered on the descending registration date. pageNo  and noOfRows used for pagination
     *
     * @param deathDivision the death division
     * @param startDate     starting date of the range
     * @param endDate       ending date of the range
     * @param pageNo        page number
     * @param noOfRows      number of rows
     * @param user          user initiating the action
     * @return the death registration results
     * @throws lk.rgd.crs.CRSRuntimeException for un-authorized operations
     */
    public List<DeathRegister> getByBDDivisionAndRegistrationDateRange(BDDivision deathDivision,
                                                                       Date startDate, Date endDate, int pageNo, int noOfRows, User user);

    /**
     * Get the list of death registrations for a given state based on given DSDivision
     *
     * @param dsDivision the divisional Secretariat
     * @param pageNo     the page number for the results required (start from 1)
     * @param noOfRows   number of rows to return per page
     * @param status     state of the DeathRegister
     * @param user       the user initiating the action
     * @return list of DeathRegister objects which are in the given state
     * @throws lk.rgd.crs.CRSRuntimeException for un-authorized operations
     */
    public List<DeathRegister> getPaginatedListForStateByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, DeathRegister.State status, User user);

    /**
     * Get the list of all the death registrations which are belonging to the given dsDivision
     *
     * @param dsDivision the divisional Secretariat
     * @param pageNo     the page number for the results required(start from 1)
     * @param noOfRows   number of rows to return per page
     * @param user       the user initiating the action
     * @return list of DeathRegister objects
     * @throws lk.rgd.crs.CRSRuntimeException for un-authorized operations
     */
    public List<DeathRegister> getPaginatedListForAllByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, User user);
}
