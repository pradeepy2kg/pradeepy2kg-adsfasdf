package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.CertificateSearch;
import lk.rgd.crs.api.domain.DeathRegister;

import java.util.List;

/**
 * Perform certificate search for all life events
 *
 * @author Chathuranga Withana
 */
public interface CertificateSearchService {

    /**
     * Checks selected application number is unique for the selected DS Division
     *
     * @param dsDivision    the Divisional Secretariat division
     * @param applicationNo application number to check
     * @return true if the application number is unique and not used at present
     */
    public boolean isValidCertificateSearchApplicationNo(DSDivision dsDivision, String applicationNo);

    /**
     * Perform a birth certificate search, and add an entry on the search performed
     *
     * @param cs   the birth certificate search details
     * @param user the user performing the action
     * @return the list of birth records
     */
    public List<BirthDeclaration> performBirthCertificateSearch(CertificateSearch cs, User user);

    /**
     * Perform a death certificate search, and add an entry on the search performed
     *
     * @param cs   the death certificate search details
     * @param user the user performing the action
     * @return the list of death records
     */
    public List<DeathRegister> performDeathCertificateSearch(CertificateSearch cs, User user);
}
