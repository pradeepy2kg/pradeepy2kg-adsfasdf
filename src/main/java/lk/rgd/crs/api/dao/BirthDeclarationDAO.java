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

    public BirthDeclaration getBirthDeclaration(BDDivision birthDivision, String bdfSerialNo);

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

    public List<BirthDeclaration> getBirthRegistrationPending(int division, boolean isExpired);
}

