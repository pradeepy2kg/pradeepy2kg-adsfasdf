package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthDeclaration;

import java.util.Date;
import java.util.List;

/**
 * @author asankha
 */
public interface BirthDeclarationDAO {

    /**
     * Add a birth declaration
     *
     * @param bdf  the BDF to be added
     * @param user the User initiating the action
     */
    public void addBirthDeclaration(BirthDeclaration bdf, User user);

    /**
     * Update a birth declaration
     *
     * @param bdf  the updated BDF
     * @param user the User initiating the action
     */
    public void updateBirthDeclaration(BirthDeclaration bdf, User user);

    /**
     * Remove a birth declaration
     *
     * @param idUKey the unique ID of the BDF to remove
     */
    public void deleteBirthDeclaration(long idUKey);

    /**
     * Return all records - for indexing only
     *
     * @return all records
     */
    public List<BirthDeclaration> findAll();

    /**
     * Returns a limited set of BirthDeclarations for which the confirmation form is not yet printed. The
     * results are ordered on the descending dateOfRegistration and optionally already printed records may
     * again be requested for re-print
     *
     * @param birthDivision the birth division
     * @param pageNo        page number
     * @param noOfRows      number of rows
     * @param printed       return already printed items if true, or items pending printing if false
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getConfirmationPrintPending(BDDivision birthDivision,
        int pageNo, int noOfRows, boolean printed);

    /**
     * Get Paginated list of BDFs for the given state
     *
     * @param birthDivision the birth division
     * @param pageNo        page number
     * @param noOfRows      number of rows
     * @param state         the state of the records to be returned
     * @return approved list for print
     */
    public List<BirthDeclaration> getPaginatedListForState(BDDivision birthDivision, int pageNo, int noOfRows,
        BirthDeclaration.State state);

    /**
     * Get Paginated list of BDFs for the given state and birth type
     *
     * @param birthDivision the birth division
     * @param pageNo        page number
     * @param noOfRows      number of rows
     * @param state         the state of the records to be returned
     * @param birthType     the birth type of the records to be returned
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getPaginatedListForStateAndBirthType(BDDivision birthDivision, int pageNo,
        int noOfRows, BirthDeclaration.State state, BirthDeclaration.BirthType birthType);

    /**
     * Returns the Birth Declaration object for a given Id
     *
     * @param bdId Birth Declarion Id for the given declaration
     * @return BirthDeclaration or null if none exist
     */
    public BirthDeclaration getById(long bdId);

    /**
     * Get existing records for the same mother for the given DOB range
     *
     * @param start          start date for DOB range
     * @param end            end date for DOB range
     * @param motherNICorPIN mothers NIC or PIN
     * @return existing records if any
     */
    public List<BirthDeclaration> getByDOBRangeandMotherNICorPIN(Date start, Date end, String motherNICorPIN);

    /**
     * Get the active record by BD Division and Serial number
     *
     * @param bdDivision the Birth Death declaration division
     * @param serialNo   the Serial No within the division
     * @return the BDF marked as active, or null if none exist
     */
    public BirthDeclaration getActiveRecordByBDDivisionAndSerialNo(BDDivision bdDivision, long serialNo);

    /**
     * Get the active record by Serial number
     *
     * @param serialNo Serial Number of the Birth Decleration
     * @param state    Status of the Birth Declaration
     * @return the BDF marked as active, or null if it doesn't exist
     */
    public BirthDeclaration getActiveRecordBySerialNo(long serialNo, BirthDeclaration.State state);

    /**
     * Returns a limited set of BirthDeclarations for a selected BD Division, selected range of registration dates in
     * particular status.
     * Results are ordered on the descending dateOfRegistration. pageNo  and noOfRows used for pagination
     *
     * @param birthDivision the birth division
     * @param status        BirthDeclaration state
     * @param startDate     starting date of the range
     * @param endDate       ending date of the range
     * @param pageNo        page number
     * @param noOfRows      number of rows
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getByBDDivisionStatusAndRegisterDateRange(BDDivision birthDivision,
        BirthDeclaration.State status, Date startDate, Date endDate, int pageNo, int noOfRows);

    /**
     * Returns a limited set of BirthDeclarations for a selected BD Division, selected range of registration dates in
     * particular status and birth type.
     * Results are ordered on the descending dateOfRegistration. pageNo  and noOfRows used for pagination
     *
     * @param birthDivision the birth division
     * @param status        BirthDeclaration state
     * @param birthType     BirthDeclaration birth type
     * @param startDate     starting date of the range
     * @param endDate       ending date of the range
     * @param pageNo        page number
     * @param noOfRows      number of rows
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getByBDDivisionStatusBirthTypeAndRegisterDateRange(BDDivision birthDivision,
        BirthDeclaration.State status, BirthDeclaration.BirthType birthType, Date startDate, Date endDate, int pageNo,
        int noOfRows);

    /**
     * Returns a limited set of BirthDeclarations for which confirmation changes captured are awaiting approval
     * by an ADR for selected BD Division and selected range of confirmation received dates.
     * Results are ordered on the descending confirmationProcessedTimestamp
     *
     * @param birthDivision the birth division
     * @param startDate     starting date of the range
     * @param endDate       ending date of the range
     * @param pageNo        page number
     * @param noOfRows      number of rows
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getByBDDivisionStatusAndConfirmationReceiveDateRange(BDDivision birthDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows);

    /**
     * Returns a limited set of BirthDeclarations for a given birthDivision
     *
     * @param birthDivision the birth division
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getByBirthDivision(BDDivision birthDivision);

    /**
     * Get records that were registered prior to the given date - but still pending in the CONFIRMATION_PRINTED state
     *
     * @param date the earliest date to detrmine records for auto confirmation
     * @return list of BDFs selected for auto confirmation and BC issuance
     */
    public List<BirthDeclaration> getUnconfirmedByRegistrationDate(Date date);

    /**
     * Returns historical records for the given BD Division and Serial number
     *
     * @param birthDivision the birth division
     * @param serialNo      the BirthDeclaration serial number
     * @return the related historical records - if any
     */
    public List<BirthDeclaration> getHistoricalRecordsForBDDivisionAndSerialNo(BDDivision birthDivision, long serialNo);

    /**
     * get all the alterations done to this birth recode
     *
     * @param birthDivision birth recorded division
     * @param serialNo      serial number of the birth certificate
     * @param idUKey        idUKey of the record
     * @return list of birth declaration objects .
     */
    public List<BirthDeclaration> getHistoricalAlterationRecordForBDDivisionAndSerialNo(BDDivision birthDivision, long serialNo, long idUKey);

    /**
     * Get Paginated list of BDFs for the given state based on given DSDivision
     *
     * @param dsDivision the divisional Secretariat
     * @param pageNo     page number
     * @param noOfRows   number of rows
     * @param state      the state of the records to be returned
     * @return approved list for print
     */
    public List<BirthDeclaration> getPaginatedListForStateByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows,
        BirthDeclaration.State state);

    /**
     * Get Paginated list of BDFs for the given state based on given district
     *
     * @param district district
     * @param pageNo   page number
     * @param noOfRows number of rows
     * @param state    the state of the records to be returned
     * @return approved list for print
     */
    public List<BirthDeclaration> getPaginatedListForStateByDistrict(District district, int pageNo, int noOfRows,
        BirthDeclaration.State state);

    /**
     * Get Paginated list of BDFs for the given state based on given DSDivision ,birth type and state
     *
     * @param dsDivision the divisional Secretariat
     * @param pageNo     page number
     * @param noOfRows   number of rows
     * @param state      the state of the records to be returned
     * @param birthType  the birth type of the record to be returned
     * @return list of birth declarations
     */
    public List<BirthDeclaration> getPaginatedListForStateAndBirthTypeByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows,
        BirthDeclaration.State state, BirthDeclaration.BirthType birthType);

    /**
     * Returns a limited set of BirthDeclarations for a selected BD Division, selected range of registration dates in
     * particular status.
     * Results are ordered on the descending dateOfRegistration. pageNo  and noOfRows used for pagination
     *
     * @param dsDivision the divisional Secretariat
     * @param status     BirthDeclaration state
     * @param startDate  starting date of the range
     * @param endDate    ending date of the range
     * @param pageNo     page number
     * @param noOfRows   number of rows
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getByDSDivisionStatusAndRegisterDateRange(DSDivision dsDivision,
        BirthDeclaration.State status, Date startDate, Date endDate, int pageNo, int noOfRows);

    /**
     * Returns a limited set of BirthDeclarations for a selected BD Division, selected range of registration dates in
     * particular status and birth type.
     * Results are ordered on the descending dateOfRegistration. pageNo  and noOfRows used for pagination
     *
     * @param dsDivision the divisional Secretariat
     * @param status     BirthDeclaration state
     * @param birthType  BirthDeclaration birthType
     * @param startDate  starting date of the range
     * @param endDate    ending date of the range
     * @param pageNo     page number
     * @param noOfRows   number of rows
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getByDSDivisionStatusBirthTypeAndRegisterDateRange(DSDivision dsDivision,
        BirthDeclaration.State status, BirthDeclaration.BirthType birthType, Date startDate, Date endDate, int pageNo,
        int noOfRows);

    /**
     * Returns a limited set of BirthDeclarations for which confirmation changes captured are awaiting approval
     * by an ADR for selected DS Division and selected range of confirmation received dates.
     * Results are ordered on the descending confirmationProcessedTimestamp
     *
     * @param dsDivision the divisional Secretariat
     * @param startDate  starting date of the range
     * @param endDate    ending date of the range
     * @param pageNo     page number
     * @param noOfRows   number of rows
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getByDSDivisionStatusAndConfirmationReceiveDateRange(DSDivision dsDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows);

    /**
     * Returns a limited set of BirthDeclarations for a given dsDivision
     *
     * @param dsDivision the divisional Secretariat
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getByDSDivision(DSDivision dsDivision);

    /**
     * @param PINorNIC PIN or NIC number of child
     * @return BirthDeclaration or null if none exist
     */
    public BirthDeclaration getByPINorNIC(long PINorNIC);

    /**
     * Returns all records of BirthDeclarations which are within the given range in birth date and belongs to the given DS division and
     * Given status.
     *
     * @param dsDivision the divisional Secretariat
     * @param startDate  starting date of the range (for birth date)
     * @param endDate    ending date of the range (for birth date)
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getByDSDivisionAndStatusAndBirthDateRange(DSDivision dsDivision,
        Date startDate, Date endDate, BirthDeclaration.State status);

    /**
     * Returns count of all records which are in given state
     *
     * @param status    Birth Declaration form's state
     * @param startDate start Date
     * @param endDate   end Date
     * @return the count of records
     */
    public int getBirthCertificateCount(BirthDeclaration.State status, Date startDate, Date endDate);

    /**
     * Returns all Birth Declaration records which are created by given User
     *
     * @param user  created user
     * @param start
     * @param end   @return list of birth declarations
     */
    public List<BirthDeclaration> getByCreatedUser(User user, Date start, Date end);

    /**
     * get active birth records by dsDivision and serial number
     *
     * @param serialNumber serial number
     * @param dsDivisionId dsDivision id
     * @return list of birth records
     */
    public List<BirthDeclaration> getActiveBirthRecordByDSDivisionAndSerialNumber(long serialNumber, int dsDivisionId);

    /**
     * get list of active live birth declaration for given mother
     *
     * @param motherIdentification mother pin or nic
     * @return list of birth records
     */
    public List<BirthDeclaration> getListOfLiveBirthsForGivenMother(String motherIdentification);

    /**
     * Returns all Birth Declarations registered by the specified Registrar(by registrar pin or nic)
     *
     * @param registrarPin   the pin of registrar
     * @param registrarNic   the nic of registrar
     * @param bdDivisionUKey the birth division unique key
     * @return list of matching birth declarations
     */
    public List<BirthDeclaration> getBirthsByRegistrarPinOrNicAndDivision(String registrarPin, String registrarNic,
        int bdDivisionUKey);

    public Long findDSDivisionUsageInBirthRecords(int dsDivisionUKey);

    public Long findGNDivisionUsageInBirthRecords(int gnDivisionUKey);

    public Long findBDDivisionUsageInBirthRecords(int bdDivisionUKey);
}

