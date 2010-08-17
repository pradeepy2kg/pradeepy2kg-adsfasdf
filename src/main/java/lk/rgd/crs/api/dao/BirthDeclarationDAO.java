package lk.rgd.crs.api.dao;

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
     * @param bdf the BDF to be added
     * @param user the User initiating the action
     */
    public void addBirthDeclaration(BirthDeclaration bdf, User user);

    /**
     * Update a birth declaration
     *
     * @param bdf the updated BDF
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
    public List<BirthDeclaration> getPaginatedListForState(BDDivision birthDivision,
        int pageNo, int noOfRows, BirthDeclaration.State state);

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
     * @param start start date for DOB range
     * @param end end date for DOB range
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

}

