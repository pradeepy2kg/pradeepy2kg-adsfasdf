package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.BDDivision;

import java.util.List;

public interface BirthRegistrationService {

    /**
     * Add a Birth declaration to the system. This is a Data Entry operation, and only data entry level validations
     * are performed at this stage. The [ADR] Approval will trigger a manual / human approval after validating any
     * warnings by an ADR or higher level authority
     *
     * @param bdf            the BDF to be added
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user           the user initiating the action
     * @param caseFileNumber the case file number for a late or belated registration
     * @param additionalDocumentsComment a comment specifying the list of additional document supplied
     * (for a late registration)
     */
    public void addNormalBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user,
        String caseFileNumber, String additionalDocumentsComment);

    /**
     * Update an existing BDF by a DEO or ADR before approval
     *
     * @param bdf            the BDF to be updated
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user           the user initiating the action
     */
    public void updateNormalBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Remove an existing BDF by a DEO or ADR before approval
     *
     * @param bdf            the BDF to be added
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user           the user initiating the action
     */
    public void deleteNormalBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Approve a list of BDF forms. Will only approve those that triggers no warnings. The result will contain
     * information on the warnings returned.
     *
     * @param approvalDataList a list of the unique BDF IDs to be approved in batch
     * @param user             the user approving the BDFs
     * @return a list of warnings for those that trigger warnings during approval
     */
    public List<UserWarning> approveBirthDeclarationIdList(long[] approvalDataList, User user);

    /**
     * Approve a single BDF by an ADR or higher authority
     *
     * @param bdf            the BDF to be approved
     * @param ignoreWarnings an explicit switch that indicates that the record should be approved ignoring warnings
     * @param user           the user initiating the action
     * @return a list of warnings, if ignoreWarnings is false
     */
    public List<UserWarning> approveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Reject a birth declaration by an ADR or higher authority
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
     * @param user the user making the request
     * @return the BDF if found, and the user has access to the record
     */
    public BirthDeclaration getByBDDivisionAndSerialNo(BDDivision bdDivision, long serialNo, User user);

    /**
     * Returns a limited set of BirthDeclarations for which confirmation changes captured are awaiting approval
     * by an ADR. Results are ordered on the descending confirmationReceiveDate
     *
     * @param birthDivision the birth division
     * @param pageNo the page number for the results required (start from 1)
     * @param noOfRows number of rows to return per page
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getConfirmationApprovalPending(BDDivision birthDivision, int pageNo, int noOfRows);

    /**
     * Returns a limited set of BirthDeclarations for which confirmation changes are not captured yet awaiting approval
     * by an ADR. Results are ordered on the descending confirmationReceiveDate
     *
     * @param birthDivision the birth division
     * @param pageNo the page number for the results required (start from 1)
     * @param noOfRows number of rows to return per page
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

}

