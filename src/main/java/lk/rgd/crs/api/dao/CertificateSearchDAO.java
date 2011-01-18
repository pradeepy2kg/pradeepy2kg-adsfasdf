package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.crs.api.domain.CertificateSearch;

/**
 * DAO for Certificate Search Process of life events
 *
 * @author Chathuranga Withana
 */
public interface CertificateSearchDAO {

    /**
     * Add a birth certificate search entry
     *
     * @param cs the birth certificate search to be added
     */
    public void addCertificateSearch(CertificateSearch cs);

    /**
     * Returns the Certificate Search object for a given application number and DS Division
     *
     * @param dsDivision the DS division under which the BirthCertificateSearch application number should be searched
     * @param serialNo   applicationNo given to the BirthCertificateSearch
     * @return CertificateSearch, or null if none exist
     */
    public CertificateSearch getByDSDivisionAndApplicationNo(DSDivision dsDivision, String serialNo);
}
