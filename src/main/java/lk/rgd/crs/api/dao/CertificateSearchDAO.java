package lk.rgd.crs.api.dao;

import lk.rgd.crs.api.domain.CertificateSearch;
import lk.rgd.common.api.domain.DSDivision;

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
    public void addBirthCertificateSearch(CertificateSearch cs);

    /**
     * Returns the Birth Certificate Search object for a given serial number(application number) and DSDivision
     *
     * @param dsDivision the DS division under which the BirthCertificateSearch serial number should be searched
     * @param serialNo   applicationNo given to the BirthCertificateSearch
     * @return BirthCertificateSearch or null if results not found
     */
    public CertificateSearch getByDSDivisionAndSerialNo(DSDivision dsDivision, String serialNo);
}
