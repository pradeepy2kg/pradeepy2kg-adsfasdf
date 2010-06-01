package lk.rgd.crs.api.dao;

import lk.rgd.crs.api.domain.BirthDeclaration;

import java.util.List;

/**
 * @author asankha
 */
public interface BirthDeclarationDAO {

    public void addBirthDeclaration(BirthDeclaration bdf);

    public BirthDeclaration getBirthDeclaration(int birthDistrict, int birthDivision, String bdfSerialNo);

    /**
     * Returns a limited set of BirthDeclarations for which the confirmation form is not yet printed. The
     * results are ordered on the descending dateOfRegistration and optionally already printed records may
     * again be requested for re-print
     *
     * @param birthDistrict
     * @param birthDivision
     * @param includeAlreadyPrinted
     * @return
     */
    public List<BirthDeclaration> getConfirmationPrintPending(int birthDistrict, int birthDivision, boolean includeAlreadyPrinted);

    public List<BirthDeclaration> getBirthRegistrationPending(int birthDistrict,int birthDivision,boolean isExpired);
    
}

