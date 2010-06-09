package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.BDDivision;

import java.util.List;

public interface BirthRegistrationService {

    /**
     * Add a new BDF by a DEO or ADR
     * @param bdf the BDF to be added
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user the user initiating the action
     */
    public void addNormalBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Update an existing BDF by a DEO or ADR before approval
     * @param bdf the BDF to be updated
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user the user initiating the action
     */
    public void updateNormalBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Remove an existing BDF by a DEO or ADR before approval
     * @param bdf the BDF to be added
     * @param ignoreWarnings an explicit switch to disable optional validations
     * @param user the user initiating the action
     */
    public void deleteNormalBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Approve a list of BDF forms. Will only approve those that triggers no warnings. The result will contain
     * information on the warnings returned.
     * @param approvalDataList a list of the unique BDF IDs to be approved in batch
     * @param user the user approving the BDFs
     * @return a list of warnings for those that trigger warnings during approval
     */
    public List<UserWarning> approveBirthDeclarationIdList(long[] approvalDataList, User user);

    /**
     * Approve a single BDF by an ADR or higher authority
     * @param bdf the BDF to be approved
     * @param ignoreWarnings an explicit switch that indicates that the record should be approved ignoring warnings
     * @param user the user initiating the action
     * @return a list of warnings, if ignoreWarnings is false
     */
    public List<UserWarning> approveBirthDeclaration(BirthDeclaration bdf, boolean ignoreWarnings, User user);

    /**
     * Returns the Birth Declaration object for a given Id
     * @param  bdId Birth Declarion Id for the given declaration
     * @Return BirthDeclaration
     */
    public BirthDeclaration getById(long bdId);

    /**
     * Returns the Birth Declaration object for a given bdf serialNo
     * @param  serialNo bdfSerialNo given to the Birth Declarion
     * @Return BirthDeclaration
     */
    public BirthDeclaration getBySerialNo(String serialNo);

    /**
        * Returns a limited set of BirthDeclarations for which confirmation changes captured are awaiting approval
        * by an ADR. Results are ordered on the descending confirmationReceiveDate
        *
        * @param birthDivision the birth division
        * @return the birth declaration results
        */
       public List<BirthDeclaration> getConfirmationApprovalPending(BDDivision birthDivision,int pageNo,int noOfRows);

    /**
        * Returns a limited set of BirthDeclarations for which confirmation changes are not captured yet awaiting approval
        * by an ADR. Results are ordered on the descending confirmationReceiveDate
        *
        * @param birthDivision the birth division
        * @return the birth declaration results
        */
       public List<BirthDeclaration> getDeclarationApprovalPending(BDDivision birthDivision,int pageNo,int noOfRows);
    
}

