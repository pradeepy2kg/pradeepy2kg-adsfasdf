package lk.rgd.crs.api.dao;

import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.common.api.domain.User;

import java.util.List;

/**
 * @author asankha
 */
public interface BirthDeclarationDAO {
    public void addBirthDeclaration(BirthDeclaration bdf);

    /**
     * Returns a limited set of BirthDeclarations for which the confirmation form is not yet printed. The
     * results are ordered on the descending dateOfRegistration and optionally already printed records may
     * again be requested for re-print
     *
     * @param birthDivision the birth division
     * @param printed       return already printed items if true, or items pending printing if false
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getConfirmationPrintPending(BDDivision birthDivision, boolean printed);

    /**
     * Returns a limited set of BirthDeclarations for which confirmation changes captured are awaiting approval
     * by an ADR. Results are ordered on the descending confirmationReceiveDate
     *
     * @param birthDivision the birth division
     * @return the birth declaration results
     */
    public List<BirthDeclaration> getConfirmationApprovalPending(BDDivision birthDivision,int pageNo,int noOfRows);

    /**
     * Returns the Birth Declaration object for a given Id
     * @param  bdId Birth Declarion Id for the given declaration
     * @Return BirthDeclaration
     */
    public BirthDeclaration getById(long bdId);

    /**
     * Returns the Birth Declaration object for a given bdf serialNo
     * @param  serailNo bdfSerialNo given to the Birth Declarion
     * @Return BirthDeclaration
     */
    public BirthDeclaration getBySerialNo(String serailNo);
}

