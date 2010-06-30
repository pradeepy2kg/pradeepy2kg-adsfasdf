package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.BDDivision;

import java.util.List;
import java.util.Date;

public interface BirthRegistrationService {

    /**
     * Add a Live Birth declaration to the system. This is a Data Entry operation, and only data entry level validations
     * are performed at this stage. The [ADR] Approval will trigger a manual / human approval after validating any
     * warnings by an ADR or higher level authority. Late (e.g. > 90 days) or belated (e.g. > 365 days) registrations
     * are entered using this same method, and will ask for the case file number and additional comments - which should
     * specify the additional documents submitted to assist in the registration
     *
     * @param bdf                        the BDF to be added
     * @param ignoreWarnings             an explicit switch to disable optional validations
     * @param user                       the user initiating the action
     * @param caseFileNumber             the case file number for a late or belated registration
     * @param additionalDocumentsComment a comment specifying the list of additional document supplied
     *                                   (for a late registration)
     * @return a list of warnings if applicable for the record - unless the ignoreWarnings option is selected
     *         Warnings maybe if a mother specified is known to be dead etc
     */
    public List<UserWarning> addLiveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user,
        String caseFileNumber, String additionalDocumentsComment);

    /**
     * Update an existing BDF for a Live birth by a DEO or ADR <b>before</b> approval
     *
     * @param bdf            the BDF to be updated
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user           the user initiating the action
     */
    public void editLiveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Remove an existing BDF by a DEO or ADR <b>before</b> approval
     *
     * @param bdf            the BDF to be added
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user           the user initiating the action
     */
    public void deleteLiveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Approve a list of BDF forms for Live births. Will only approve those that triggers no warnings. The result
     * will contain information on the warnings returned.
     *
     * @param approveIDList a list of the unique BDF IDs to be approved in batch
     * @param user             the user approving the BDFs
     * @return a list of warnings for those that trigger warnings during approval
     */
    public List<UserWarning> approveLiveBirthDeclarationIdList(long[] approveIDList, User user);

    /**
     * Approve a single BDF for a Live birth by an ADR or higher authority
     *
     * @param bdf            the BDF to be approved
     * @param ignoreWarnings an explicit switch that indicates that the record should be approved ignoring warnings
     * @param user           the user initiating the action
     * @return a list of warnings, if ignoreWarnings is false
     */
    public List<UserWarning> approveLiveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Mark that the confirmation form for the BDF has been printed
     *
     * @param bdf the BDF for which the confirmation form has been printed
     * @param user the user initiating the action
     */
    public void markLiveBirthConfirmationAsPrinted(BirthDeclaration bdf, User user);

    /**
     * Mark that the confirmation forms for the BDF IDs given has been printed
     *
     * @param printedIDList the list of unique BDF IDs that have the parental confirmations now printed
     * @param user the user initiating the action
     */
    public void markLiveBirthConfirmationIDsAsPrinted(long[] printedIDList, User user);

    /**
     * Mark a live birth declaration as confirmed without any additional changes by parents.
     *
     * @param bdf  the birth declaration confirmed as correct. <b>Only</b> the confirmant information will be
     * updated into the already existing record
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
     * @param bdf the BDF for which the BC has been printed
     * @param user the user initiating the action
     */
    public void markLiveBirthCertificateAsPrinted(BirthDeclaration bdf, User user);

    /**
     * Mark that the Birth Certificates for the BDF IDs given has been printed
     * 
     * @param printedIDList the list of unique BDF IDs that have the BC now printed
     * @param user the user initiating the action
     */
    public void markLiveBirthCertificateIDsAsPrinted(long[] printedIDList, User user);

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
     * @param bdId Birth Declarion Id for the given declaration
     * @param user the user making the request
     * @return the BDF if found, and the user has access to the record
     */
    public BirthDeclaration getById(long bdId, User user);

    /**
     * Returns the Birth Declaration object for a given bdf serialNo under a selected BD Division
     *
     * @param bdDivision the Birth Death declaration division under which the BDF serial number should be searched
     * @param serialNo   bdfSerialNo given to the Birth Declarion
     * @param user       the user making the request
     * @return the BDF if found, and the user has access to the record
     */
    public BirthDeclaration getByBDDivisionAndSerialNo(BDDivision bdDivision, long serialNo, User user);

    /**
     * Returns a limited set of BirthDeclarations for which confirmation changes captured are awaiting approval
     * by an ADR. Results are ordered on the descending confirmationReceiveDate
     *
     * @param birthDivision the birth division
     * @param pageNo        the page number for the results required (start from 1)
     * @param noOfRows      number of rows to return per page
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getConfirmationApprovalPending(BDDivision birthDivision, int pageNo, int noOfRows);

    /**
     * Get the list of BDFs for which the Confirmation form should be printed
     * 
     * @param birthDivision the birth division
     * @param pageNo   the page number for the results required (start from 1)
     * @param noOfRows  number of rows to return per page
     * @param printed return already printed items if true, or items pending printing if false
     * @return  approved list for print
     */
    public List<BirthDeclaration> getConfirmationPrintList(BDDivision birthDivision, int pageNo, int noOfRows, boolean printed);

    /**
     * Get the list of BDFs for which the Birth Certificate should be printed
     *
     * @param birthDivision the birth division
     * @param pageNo   the page number for the results required (start from 1)
     * @param noOfRows  number of rows to return per page
     * @param printed return already printed items if true, or items pending printing if false
     * @return  approved list for print
     */
    public List<BirthDeclaration> getBirthCertificatePrintList(BDDivision birthDivision, int pageNo, int noOfRows, boolean printed);

    /**
     * Returns a limited set of BirthDeclarations for which confirmation changes are not captured yet awaiting approval
     * by an ADR. Results are ordered on the descending confirmationReceiveDate
     *
     * @param birthDivision the birth division
     * @param pageNo        the page number for the results required (start from 1)
     * @param noOfRows      number of rows to return per page
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getDeclarationApprovalPending(BDDivision birthDivision, int pageNo, int noOfRows);

    /**
     * Load names of values for print or display, in the preferred language of the record
     *
     * @param bdf the BDF to load values for
     * @return the BDF with all print/display string values populated
     */
    public BirthDeclaration loadValuesForPrint(BirthDeclaration bdf);

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
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getDeclarationPendingByBDDivisionAndRegisterDateRange(BDDivision birthDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows);

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
        Date startDate, Date endDate, int pageNo, int noOfRows);
}

