package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.CommonStatistics;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthAlteration;
import lk.rgd.crs.api.domain.BirthDeclaration;

import java.util.Date;
import java.util.List;

public interface BirthRegistrationService {

    /**
     * Add a Live Birth declaration to the system. This is a Data Entry operation, and only data entry level validations
     * are performed at this stage. The [ADR] Approval will trigger a manual / human approval after validating any
     * warnings by an ADR or higher level authority. Late (e.g. > 90 days) or belated (e.g. > 365 days) registrations
     * are entered using this same method, and will ask for the case file number and additional comments - which should
     * specify the additional documents submitted to assist in the registration
     *
     * @param bdf            the BDF to be added
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user           the user initiating the action
     * @return a list of warnings if applicable for the record - unless the ignoreWarnings option is selected
     *         Warnings maybe if a mother specified is known to be dead etc
     */
    public List<UserWarning> addLiveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Add a Still Birth declaration to the system.
     *
     * @param bdf            the BDF to be added
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user           the user initiating the action
     * @return a list of warnings if applicable for the record - unless the ignoreWarnings option is selected
     *         Warnings maybe if a mother specified is known to be dead etc
     */
    public List<UserWarning> addStillBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Add a bBelated Birth declaration to the system.
     *
     * @param bdf            the BDF to be added
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user           the user initiating the action
     * @return a list of warnings if applicable for the record - unless the ignoreWarnings option is selected
     */
    public List<UserWarning> addBelatedBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);


    /**
     * Add a Adoption Birth declaration to the system.
     *
     * @param bdf            the BDF to be added
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user           the user initiating the action
     * @return a list of warnings if applicable for the record - unless the ignoreWarnings option is selected
     *         Warnings maybe if a mother specified is known to be dead etc
     */
    public List<UserWarning> addAdoptionBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Update an existing BDF for a Live birth by a DEO or ADR <b>before</b> approval
     *
     * @param bdf            the BDF to be updated
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user           the user initiating the action
     */
    public void editLiveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Update an existing BDF for a Still birth by a ADR <b>before</b> approval
     *
     * @param bdf            the BDF to be updated
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user           the user initiating the action
     */
    public void editStillBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Update an existing BDF for a Belated birth by a ADR <b>before</b> approval
     *
     * @param bdf            the BDF to be updated
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user           the user initiating the action
     */
    public void editBelatedBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Update an existing BDF for a adoption birth by a ADR <b>before</b> approval
     *
     * @param bdf            the BDF to be updated
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user           the user initiating the action
     */
    public void editAdoptionBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Remove an existing BDF for a Live birth by a DEO or ADR <b>before</b> approval
     *
     * @param bdf            the BDF to be added
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user           the user initiating the action
     */
    public void deleteLiveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Remove an existing BDF for a Still birth by a ADR <b>before</b> approval
     *
     * @param bdf            the BDF to be added
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user           the user initiating the action
     */
    public void deleteStillBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Remove an existing BDF for a Belated birth by a DEO or ADR <b>before</b> approval
     *
     * @param bdf            the BDF to be added
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user           the user initiating the action
     */
    public void deleteBelatedBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Remove an existing BDF for a adoption birth by a ADR <b>before</b> approval
     *
     * @param bdf            the BDF to be added
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user           the user initiating the action
     */
    public void deleteAdoptionBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Approve a list of BDF forms for Live and Still births. Will only approve those that triggers no warnings. The result
     * will contain information on the warnings returned.
     *
     * @param approveIDList a list of the unique BDF IDs to be approved in batch
     * @param user          the user approving the BDFs
     * @return a list of warnings for those that trigger warnings during approval
     */
    public List<UserWarning> approveBirthDeclarationIdList(long[] approveIDList, User user);

    /**
     * Approve a single BDF for a Live birth(Live and Late birth type) by an ADR or higher authority
     *
     * @param idUKey
     * @param ignoreWarnings an explicit switch that indicates that the record should be approved ignoring warnings
     * @param user           the user initiating the action   @return a list of warnings, if ignoreWarnings is false
     */
    public List<UserWarning> approveLiveBirthDeclaration(long idUKey, boolean ignoreWarnings, User user);

    /**
     * Approve a single BDF for a Still birth by an ADR or higher authority
     *
     * @param idUKey
     * @param ignoreWarnings an explicit switch that indicates that the record should be approved ignoring warnings
     * @param user           the user initiating the action   @return a list of warnings, if ignoreWarnings is false
     */
    public List<UserWarning> approveStillBirthDeclaration(long idUKey, boolean ignoreWarnings, User user);

    /**
     * Approve a single BDF for a Live birth by an ADR or higher authority
     *
     * @param idUKey
     * @param ignoreWarnings an explicit switch that indicates that the record should be approved ignoring warnings
     * @param user           the user initiating the action   @return a list of warnings, if ignoreWarnings is false
     */
    public List<UserWarning> approveBelatedBirthDeclaration(long idUKey, boolean ignoreWarnings, User user);

    /**
     * Approve a single BDF for a child adoption by an ADR or higher authority
     *
     * @param idUKey
     * @param ignoreWarnings an explicit switch that indicates that the record should be approved ignoring warnings
     * @param user           the user initiating the action   @return a list of warnings, if ignoreWarnings is false
     */
    public List<UserWarning> approveAdoptionBirthDeclaration(long idUKey, boolean ignoreWarnings, User user);

    /**
     * Mark that the confirmation form for the BDF has been printed
     *
     * @param bdf  the BDF for which the confirmation form has been printed
     * @param user the user initiating the action
     */
    public void markLiveBirthConfirmationAsPrinted(BirthDeclaration bdf, User user);

    /**
     * Mark that the confirmation forms for the BDF IDs given has been printed
     *
     * @param printedIDList the list of unique BDF IDs that have the parental confirmations now printed
     * @param user          the user initiating the action
     */
    public void markLiveBirthConfirmationIDsAsPrinted(long[] printedIDList, User user);

    /**
     * Mark a live birth declaration as confirmed without any additional changes by parents.
     *
     * @param bdf  the birth declaration confirmed as correct. <b>Only</b> the confirmant information will be
     *             updated into the already existing record
     * @param user user initiating the action
     */
    public void markLiveBirthDeclarationAsConfirmedWithoutChanges(BirthDeclaration bdf, User user);

    /**
     * Captures changes sent by parents during confirmation. This method may be invoked during the data capture of
     * a received confirmation, or by a DEO/ADR subsequently before approval.
     *
     * @param bdf  the birth declaration with changes
     * @param user user initiating the action
     */
    public void captureLiveBirthConfirmationChanges(BirthDeclaration bdf, User user);

    /**
     * Approve changes submitted by parents (or possibly those already captured by a DEO)
     *
     * @param bdf            the BDF containing the updates captured during the confirmation stage
     * @param ignoreWarnings a flag indicating that any warnings are confirmed as checked by the user
     * @param user           user initiating the action
     * @return any warnings generated for the record
     */
    public List<UserWarning> approveConfirmationChanges(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * API to perform approval of a set of confirmations with changes
     *
     * @param approvalDataList the unique IDs of the confirmations with changes to approve
     * @param user             the user making the bulk approval
     * @return a list of warnings, if any are encountered
     */
    public List<UserWarning> approveConfirmationChangesForIDList(long[] approvalDataList, User user);

    /**
     * Mark that the Birth Certificate for the BDF has been printed
     *
     * @param bdf  the BDF for which the BC has been printed
     * @param user the user initiating the action
     */
    public void markLiveBirthCertificateAsPrinted(BirthDeclaration bdf, User user);

    /**
     * Mark that the Still Birth Certificate for the BDF has been printed
     *
     * @param bdf  the BDF for which the BC has been printed
     * @param user the user initiating the action
     */
    public void markStillBirthCertificateAsPrinted(BirthDeclaration bdf, User user);

    /**
     * Mark that the Birth Certificate for the Belated BDF has been printed
     *
     * @param bdf  the BDF for which the BC has been printed
     * @param user the user initiating the action
     */
    public void markBelatedBirthCertificateAsPrinted(BirthDeclaration bdf, User user);

    /**
     * Mark that the Birth Certificate for the BDF has been printed for adopted child
     *
     * @param bdf  the BDF for which the BC has been printed
     * @param user the user initiating the action
     */
    public void markAdoptionBirthCertificateAsPrinted(BirthDeclaration bdf, User user);

    /**
     * Mark that the Birth Certificates(Live/Still) for the BDF IDs given has been printed
     *
     * @param printedIDList the list of unique BDF IDs that have the BC now printed
     * @param user          the user initiating the action
     */
    public void markBirthCertificateIDsAsPrinted(long[] printedIDList, User user);

    /**
     * Reject a birth declaration by an ADR or higher authority. This should be used to reject a BDF even after
     * the confirmation is printed, and is now being rejected
     *
     * @param bdf      the BDF to be marked rejected
     * @param comments comment specifying the reason for rejection (e.g. duplicate record)
     * @param user     the user initiating the rejection
     */
    public void rejectBirthDeclaration(BirthDeclaration bdf, String comments, User user);

    /**
     * Returns the Birth Declaration object for a given Id
     *
     * @param bdId Birth Declaration Id for the given declaration
     * @param user the user making the request
     * @return the BDF if found, and the user has access to the record
     */
    public BirthDeclaration getById(long bdId, User user);

    /**
     * Returns the Birth Declaration object for a given Id
     *
     * @param bdId Birth Declaration Id for the given declaration
     * @return the BDF if found, and the user has access to the record
     */
    public BirthDeclaration getById(long bdId);

    /**
     * Returns the Birth Declaration object with all relationships for a given Id
     *
     * @param bdId Birth Declaration Id for the given declaration
     * @param user the user making the request
     * @return the BDF if found, and the user has access to the record
     */
    public BirthDeclaration getWithRelationshipsById(long bdId, User user);

    /**
     * Returns the active Birth Declaration record for a given bdf serialNo under a selected BD Division
     *
     * @param bdDivision the Birth Death declaration division
     * @param serialNo   serial number to check
     * @param user       the user making the request
     * @return the active record with the given serial number within the BD division
     */
    public BirthDeclaration getActiveRecordByBDDivisionAndSerialNo(BDDivision bdDivision, long serialNo, User user);

    /**
     * Returns the active Birth Declaration record by serial Number
     *
     * @param serialNo serial number of the Birth Declaration
     * @param user     the user who makes the request
     * @param state    Status of the Birth Declaration
     * @return the active Birth Declaration record by serial Number
     */
    public BirthDeclaration getActiveRecordBySerialNo(long serialNo, User user, BirthDeclaration.State state);

    /**
     * Returns a limited set of BirthDeclarations for which confirmation changes captured are awaiting approval
     * by an ADR. Results are ordered on the descending confirmationProcessedTimestamp
     *
     * @param birthDivision the birth division
     * @param pageNo        the page number for the results required (start from 1)
     * @param noOfRows      number of rows to return per page
     * @param user          user initiating the action
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getConfirmationApprovalPending(BDDivision birthDivision, int pageNo, int noOfRows, User user);

    /**
     * Get the list of BDFs for which the Confirmation form should be printed
     *
     * @param birthDivision the birth division
     * @param pageNo        the page number for the results required (start from 1)
     * @param noOfRows      number of rows to return per page
     * @param printed       return already printed items if true, or items pending printing if false
     * @param user          user initiating the action
     * @return approved list for print
     */
    public List<BirthDeclaration> getConfirmationPrintList(BDDivision birthDivision, int pageNo, int noOfRows, boolean printed, User user);

    /**
     * Get the list of BDFs for which the Birth Certificate should be printed
     *
     * @param birthDivision the birth division
     * @param pageNo        the page number for the results required (start from 1)
     * @param noOfRows      number of rows to return per page
     * @param printed       return already printed items if true, or items pending printing if false
     * @param user          user initiating the action
     * @return approved list for print
     */
    public List<BirthDeclaration> getBirthCertificatePrintList(BDDivision birthDivision, int pageNo, int noOfRows, boolean printed, User user);

    /**
     * Returns a limited set of BirthDeclarations for which confirmation changes are not captured yet awaiting approval
     * by an ADR. Results are ordered on the descending confirmationProcessedTimestamp
     *
     * @param birthDivision the birth division
     * @param pageNo        the page number for the results required (start from 1)
     * @param noOfRows      number of rows to return per page
     * @param user          user initiating the action
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getDeclarationApprovalPending(BDDivision birthDivision, int pageNo, int noOfRows, User user);

    /**
     * Returns a limited set of Belated BirthDeclarations for which confirmation changes are not captured yet awaiting approval
     * by an ARG. Results are ordered on the descending dateOfRegistration
     *
     * @param birthDivision the birth division
     * @param pageNo        the page number for the results required (start from 1)
     * @param noOfRows      number of rows to return per page
     * @param user          user initiating the action
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getBelatedDeclarationApprovalPending(BDDivision birthDivision, int pageNo, int noOfRows, User user);

    /**
     * Load names of values for print or display, in the preferred language of the record
     *
     * @param bdf  the BDF to load values for
     * @param user user initiating the action
     * @return the BDF with all print/display string values populated
     */
    public BirthDeclaration loadValuesForPrint(BirthDeclaration bdf, User user);

    /**
     * Returns a limited set of approval pending BirthDeclarations for selected BD Division and selected range of
     * registration dates.
     * Results are ordered on the descending dateOfRegistration. pageNo  and noOfRows used for pagination
     *
     * @param birthDivision the birth division
     * @param startDate     starting date of the range
     * @param endDate       ending date of the range
     * @param pageNo        page number
     * @param noOfRows      number of rows
     * @param user          user initiating the action
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getDeclarationPendingByBDDivisionAndRegisterDateRange(BDDivision birthDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, User user);

    /**
     * Returns a limited set of approval pending Belated BirthDeclarations for selected BD Division and selected range of
     * registration dates.
     * Results are ordered on the descending dateOfRegistration. pageNo  and noOfRows used for pagination
     *
     * @param birthDivision the birth division
     * @param startDate     starting date of the range
     * @param endDate       ending date of the range
     * @param pageNo        page number
     * @param noOfRows      number of rows
     * @param user          user initiating the action
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getBelatedDeclarationPendingByBDDivisionAndRegisterDateRange(BDDivision birthDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, User user);

    /**
     * Returns a limited set of BirthDeclarations for which confirmation changes captured are awaiting approval
     * by an ADR for selected BD Division and selected range of confirmation received dates.
     * Results are ordered on the descending confirmationProcessedTimestamp. pageNo  and noOfRows used for pagination
     *
     * @param birthDivision the birth division
     * @param startDate     starting date of the range
     * @param endDate       ending date of the range
     * @param pageNo        page number
     * @param noOfRows      number of rows
     * @param user          user initiating the action
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getByBDDivisionStatusAndConfirmationReceiveDateRange(BDDivision birthDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, User user);

    /**
     * Returns a list of BirthDeclaration objects for a given birthDivision
     *
     * @param bdDivision the birth division
     * @param user       the user initiating the action
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getByBirthDivision(BDDivision bdDivision, User user);

    /**
     * Entry point invoked by the scheduler to automate scheduled tasks related to birth registration such
     * as the automatic generation of a birth certificate after approximately 28 days without hearing back
     * from parents on confirmation details
     */
    public void triggerScheduledJobs();

    /**
     * Returns historical records for the given BD Division and Serial number
     *
     * @param bdDivision the birth division
     * @param serialNo   the BirthDeclaration serial number
     * @return the related historical records - if any
     */
    public List<BirthDeclaration> getArchivedCorrectedEntriesForGivenSerialNo(BDDivision bdDivision, long serialNo, User user);

    /**
     * Returns a limited set of BirthDeclarations for which confirmation changes are not captured yet awaiting approval
     * by an ADR and based on given DSDivison id. Results are ordered on the descending confirmationProcessedTimestamp
     *
     * @param dsDivision the divisional Secretariat
     * @param pageNo     the page number for the results required (start from 1)
     * @param noOfRows   number of rows to return per page
     * @param user       user initiating the action
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getDeclarationApprovalPendingByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, User user);

    /**
     * Returns a limited set of Belated BirthDeclarations for which confirmation changes are not captured yet awaiting approval
     * by an ADR and based on given DSDivison id. Results are ordered on the descending dateOfRegistration
     *
     * @param dsDivision the divisional Secretariat
     * @param pageNo     the page number for the results required (start from 1)
     * @param noOfRows   number of rows to return per page
     * @param user       user initiating the action
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getBelatedDeclarationApprovalPendingByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, User user);

    /**
     * Returns a limited set of approval pending BirthDeclarations for selected DS Division and selected range of
     * registration dates.
     * Results are ordered on the descending dateOfRegistration. pageNo  and noOfRows used for pagination
     *
     * @param dsDivision the divisional Secretariat
     * @param startDate  starting date of the range
     * @param endDate    ending date of the range
     * @param pageNo     page number
     * @param noOfRows   number of rows
     * @param user       user initiating the action
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getDeclarationPendingByDSDivisionAndRegisterDateRange(DSDivision dsDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, User user);

    /**
     * Returns a limited set of approval pending Belated BirthDeclarations for selected DS Division and selected range
     * of registration dates.
     * Results are ordered on the descending dateOfRegistration. pageNo  and noOfRows used for pagination
     *
     * @param dsDivision the divisional Secretariat
     * @param startDate  starting date of the range
     * @param endDate    ending date of the range
     * @param pageNo     page number
     * @param noOfRows   number of rows
     * @param user       user initiating the action
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getBelatedDeclarationPendingByDSDivisionAndRegisterDateRange(DSDivision dsDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, User user);

    /**
     * Returns a limited set of BirthDeclarations for which confirmation changes captured are awaiting approval
     * by an ADR and based on given DSDivison id. Results are ordered on the descending confirmationProcessedTimestamp
     *
     * @param dsDivision the divisional Secretariat
     * @param pageNo     the page number for the results required (start from 1)
     * @param noOfRows   number of rows to return per page
     * @param user       user initiating the action
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getConfirmationApprovalPendingByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, User user);

    /**
     * Returns a limited set of BirthDeclarations for which confirmation changes captured are awaiting approval
     * by an ADR for selected DS Division and selected range of confirmation received dates.
     * Results are ordered on the descending confirmationProcessedTimestamp. pageNo  and noOfRows used for pagination
     *
     * @param dsDivision the divisional Secretariat
     * @param startDate  starting date of the range
     * @param endDate    ending date of the range
     * @param pageNo     page number
     * @param noOfRows   number of rows
     * @param user       user initiating the action
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getByDSDivisionStatusAndConfirmationReceiveDateRange(DSDivision dsDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, User user);

    /**
     * Get the list of BDFs for which the Confirmation form should be printed
     *
     * @param dsDivision the divisional Secretariat
     * @param pageNo     the page number for the results required (start from 1)
     * @param noOfRows   number of rows to return per page
     * @param printed    return already printed items if true, or items pending printing if false
     * @param user       user initiating the action
     * @return approved list for print
     */
    public List<BirthDeclaration> getConfirmationPrintListByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, boolean printed, User user);

    /**
     * Get the list of BDFs for which the Birth Certificate should be printed
     *
     * @param dsDivision the divisional Secretariat
     * @param pageNo     the page number for the results required (start from 1)
     * @param noOfRows   number of rows to return per page
     * @param printed    return already printed items if true, or items pending printing if false
     * @param user       user initiating the action
     * @return approved list for print
     */
    public List<BirthDeclaration> getBirthCertificatePrintListByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, boolean printed, User user);

    /**
     * Returns a list of BirthDeclaration objects for a given dsDivision
     *
     * @param dsDivision the divisional Secretariat
     * @param user       the user initiating the action
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getByDSDivision(DSDivision dsDivision, User user);

    /**
     * Returns the Birth Declaration object for a given Id
     *
     * @param bdId Birth Declaration Id for the given declaration
     * @param user the user making the request
     * @return the BDF if found, and the user has access to the record
     */
    public BirthDeclaration getByIdForAdoptionLookup(long bdId, User user);

    /**
     * @param PINorNIC PIN or NIC number of child
     * @param user     the user making the request
     * @return
     */
    public BirthDeclaration getByPINorNIC(long PINorNIC, User user);

    /**
     * get historical alteration record for the given birth record
     *
     * @param birthDivision birth recorded division
     * @param serialNo      serial number of the birth certificate
     * @param idUKey        idUKey of the record
     * @param user          user who making the request
     * @return list of historical birth declarations for given birth record.
     */
    public List<BirthDeclaration> getHistoricalBirthDeclarationRecordForBDDivisionAndSerialNo(BDDivision birthDivision, long serialNo, long idUKey, User user);

    /**
     * Returns all records of BirthDeclarations which are within the given range in birth date and belongs to the given DS division and
     * Given status.
     *
     * @param dsDivision the divisional Secretariat
     * @param startDate  starting date of the range (for birth date)
     * @param endDate    ending date of the range (for birth date)
     * @param user       user initiating the action
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getByDSDivisionAndStatusAndBirthDateRange(DSDivision dsDivision,
        Date startDate, Date endDate, BirthDeclaration.State status, User user);

    /**
     * Returns all statistics information related to Births
     *
     * @param user
     * @return CommonStatistics object which encapsulated all the birth statistics information
     */
    public CommonStatistics getCommonBirthCertificateCount(String user);

    /**
     * Returns all statistics information belongs to given user. Return object cannot be null
     *
     * @param user user DEO / ADR
     * @return CommonStatistics object which encapsulated all the birth statistics information
     */
    public CommonStatistics getBirthStatisticsForUser(String user);

    /**
     * get birth alteration by birth certificate number
     * <br>notes
     * <br>only following users can access the birth alteration record
     * <ul>
     * <li> User's from the submitted location(users who belongs to the birth alteration record submitted location)</li>
     * <li>User's who are belongs to the DS Division witch the original BC issued
     * </ul>
     *
     * @param certificateNumber birth certificate number
     * @param user              user who perform the action
     * @return list of active birth alterations
     */
    public List<BirthAlteration> getActiveBirthAlterationByBirthCertificateNumber(long certificateNumber, User user);

    /**
     * get all active birth records for given DSDivision and serial number
     *
     * @param serialNumber serial number
     * @param dsDivision   dsDivision id
     * @param user         user who performs the action
     * @return list of active birth records
     */
    public List<BirthDeclaration> getActiveRecordByDSDivisionAndSerialNumber(long serialNumber, int dsDivision, User user);
}

