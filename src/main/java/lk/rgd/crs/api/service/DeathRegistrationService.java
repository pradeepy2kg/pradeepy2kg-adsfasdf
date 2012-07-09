package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.CommonStatistics;
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
 * @authar amith jayasekara
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
    public void addNewDeathRegistration(DeathRegister deathRegistration, User user);

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
     * get death alteration by given idUKey with out validating user's permission
     *
     * @param deathRegisterIdUKey death register unique key
     * @return Death Register object for given death register unique key
     */
    public DeathRegister getById(long deathRegisterIdUKey);

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
     * @param user           the user initiating the action
     * @param ignoreWarnings
     * @throws lk.rgd.crs.CRSRuntimeException for un-authorized operations
     */
    public List<UserWarning> approveDeathRegistration(long deathRegisterIdUKey, User user, boolean ignoreWarnings);

    /**
     * Reject a death registration already captured by the DEO by an ADR or higher authority
     *
     * @param deathRegisterIdUKey the unique id of the death registration which is to be rejected
     * @param user                the user initiating the action
     * @param comment             reason for rejecting death declaration
     * @throws lk.rgd.crs.CRSRuntimeException for un-authorized operations
     */
    public void rejectDeathRegistration(long deathRegisterIdUKey, User user, String comment);

    /**
     * mark that death certificate for the death registration as printed
     *
     * @param deathRegister death register object to be mark as print
     * @param user          the user initiating the action
     * @throws lk.rgd.crs.CRSRuntimeException for un-authorized operations
     */
    public void markDeathCertificateAsPrinted(DeathRegister deathRegister, User user);

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
     * Returns a limited set of DeathRegister for selected BD Division and selected range of registration dates and state
     * Results are ordered on the descending registration date. pageNo  and noOfRows used for pagination
     *
     * @param deathDivision the death division
     * @param startDate     starting date of the range
     * @param endDate       ending date of the range
     * @param pageNo        page number
     * @param noOfRows      number of rows
     * @param state         state of the record
     * @param user          user initiating the action
     * @return the death registration results
     * @throws lk.rgd.crs.CRSRuntimeException for un-authorized operations
     */
    public List<DeathRegister> getByBDDivisionAndRegistrationDateRangeAndState(BDDivision deathDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, DeathRegister.State state, User user);


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

    /**
     * retriving list of death register objects based on death persons pin or nic number
     * note: list can be more than one in rare cases like duplications of nic number
     *
     * @param pinOrNic pin number or nic number not both
     * @param user     user who has permissions to retrive death register
     * @return give list of death register objects based on death persosns pin or NIC number
     */
    public List<DeathRegister> getByPinOrNic(String pinOrNic, User user);

    /**
     * Returns the active Death Declaration record for a given serialNo under a selected BD Division
     *
     * @param bdDivision the Birth Death declaration division
     * @param serialNo   serial number to check
     * @param user       the user making the request
     * @return the active record with the given serial number within the BD division
     */
    public DeathRegister getActiveRecordByBDDivisionAndSerialNo(BDDivision bdDivision, long serialNo, User user);

    /**
     * Returns the active Death Declaration records for a given serialNo under a selected DS Division
     *
     * @param dsDivision    DS Division
     * @param serialNo      Death Serial Number
     * @param user          User requested the search
     * @return  List of active Death Declaration records with the given serialNo within the given DS Division.
     */
    public List<DeathRegister> getActiveRecordsByDSDivisionAndSerialNo(DSDivision dsDivision, long serialNo, User user);
    
    /**
     * Returns historical records for the given BD Division and Serial number recodes witch are done before given death id
     *
     * @param bdDivision the birth death division
     * @param serialNo   the Death register serial number
     * @param deathId    death certificate idUKey
     * @return the related historical records - if any
     */
    public List<DeathRegister> getArchivedCorrectedEntriesForGivenSerialNo(BDDivision bdDivision, long serialNo, long deathId, User user);

    /**
     * get death register with transient values
     *
     * @param idUKey            the unique key of the death register
     * @param certificateSearch the death register to be loaded in certificate search mode or not
     * @param user              the user making the request  @return death register object
     */
    public DeathRegister getWithTransientValuesById(long idUKey, boolean certificateSearch, User user);

    /**
     * Returns all statistics information related to Deaths
     *
     * @param user
     * @return CommonStatistics object which encapsulated all the death statistics information
     */
    public CommonStatistics getCommonDeathCertificateCount(String user);

    /**
     * Returns all statistics information belongs to given user. Return object cannot be null
     *
     * @param user user DEO / ADR
     */
    public CommonStatistics getDeathStatisticsForUser(String user);

    /**
     * get list of death register objects for given time frame and given DSDivision
     *
     * @param dsDivisionId dsDivision id
     * @param startDate    registrations from
     * @param endDate      registrations to
     * @param active       active record
     * @param pageNo       page number
     * @param numOfRows    number of rows to be fetched
     * @param user         user who performs the action
     * @return list of death register object
     */
    public List<DeathRegister> getPaginatedDeathRegisterListByDSDivisionAndRegistrationDateRange(int dsDivisionId,
        Date startDate, Date endDate, boolean active, int pageNo, int numOfRows, User user);

    /**
     * get list of death register objects for given time frame and given DSDivision and state
     *
     * @param dsDivisionId dsDivision id
     * @param startDate    registrations from
     * @param endDate      registrations to
     * @param active       active record
     * @param pageNo       page number
     * @param numOfRows    number of rows to be fetched
     * @param user         user who performs the action
     * @return
     */
    public List<DeathRegister> getPaginatedDeathRegisterListByDSDivisionAndRegistrationDateRangeAndState(int dsDivisionId,
        Date startDate, Date endDate, boolean active, int pageNo, int numOfRows, DeathRegister.State status, User user);

    /**
     * get list of death register objects for given time frame and given district
     *
     * @param districtId district id
     * @param startDate  registrations from
     * @param endDate    registrations to
     * @param active     active record
     * @param user       user who performs the action
     * @return list of death register object
     */
    public List<DeathRegister> getPaginatedDeathRegisterListByDistrictAndRegistrationDateRange(int districtId,
        Date startDate, Date endDate, boolean active, User user);

    /**
     * get list of death register objects for given time frame and given Death registration division
     *
     * @param deathDivisionId death division id
     * @param startDate       registrations from
     * @param endDate         registrations to
     * @param active          active record
     * @param user            user who performs the action
     * @return list of death register object
     */
    public List<DeathRegister> getPaginatedDeathRegisterListByDeathDivisionAndRegistrationDateRange(int deathDivisionId,
        Date startDate, Date endDate, boolean active, User user);

    /**
     * @param dsDivision death division id
     * @param startDate  registrations from
     * @param endDate    registrations to
     * @param state      active record
     * @param user
     * @return
     */
    public List<DeathRegister> getByDSDivisionAndStatusAndRegistrationDateRange(DSDivision dsDivision, Date startDate, Date endDate, DeathRegister.State state, User user);
}
