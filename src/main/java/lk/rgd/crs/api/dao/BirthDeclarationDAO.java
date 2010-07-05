package lk.rgd.crs.api.dao;

import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.common.api.domain.User;

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
     */
    public void addBirthDeclaration(BirthDeclaration bdf);

    /**
     * Update a birth declaration
     *
     * @param bdf the updated BDF
     */
    public void updateBirthDeclaration(BirthDeclaration bdf);

    /**
     * Remove a birth declaration
     *
     * @param idUKey the unique ID of the BDF to remove
     */
    public void deleteBirthDeclaration(long idUKey);

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
    public List<BirthDeclaration> getConfirmationPrintPending(BDDivision birthDivision, int pageNo, int noOfRows, boolean printed);

    /**
     * Get Paginated list of BDFs for the given state
     *
     * @param birthDivision  the birth division
     * @param pageNo  page number
     * @param noOfRows  number of rows
     * @param state the state of the records to be returned
     * @return   approved list for print
     */
    public List<BirthDeclaration> getPaginatedListForState(BDDivision birthDivision, int pageNo, int noOfRows, BirthDeclaration.State state);

    /**
     * Returns the Birth Declaration object for a given Id
     *
     * @param bdId Birth Declarion Id for the given declaration
     * @return BirthDeclaration
     */
    public BirthDeclaration getById(long bdId);

    /**
     * Get existing records for the same mother
     *
     * @param motherNICorPIN mothers NIC or PIN
     * @return existing records if any
     */
    public List<BirthDeclaration> getByDOBRangeandMotherNICorPIN(Date start, Date end, String motherNICorPIN);

    /**
     * Returns the Birth Declaration object for a given bdf serialNo under a selected BD Division
     *
     * @param bdDivision the Birth Death declaration division under which the BDF serial number should be searched
     * @param serialNo   bdfSerialNo given to the Birth Declarion
     * @Return BirthDeclaration
     */
    public BirthDeclaration getByBDDivisionAndSerialNo(BDDivision bdDivision, long serialNo);

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
     * by an ADR for selected BD Division and selected range of confirmation recevied dates.
     * Results are ordered on the descending confirmationReceiveDate. pageNo  and noOfRows used for pagination
     *
     * @param birthDivision the birth division
     * @param startDate     starting date of the range
     * @param endDate       ending date of the range
     * @param pageNo        page number
     * @param noOfRows      number of rows
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getByBDDivisionStatusAndConfirmationReceiveDateRange(BDDivision birthDivision,
        BirthDeclaration.State status, Date startDate, Date endDate, int pageNo, int noOfRows);
}

