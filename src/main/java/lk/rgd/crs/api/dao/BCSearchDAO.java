package lk.rgd.crs.api.dao;

import lk.rgd.crs.api.domain.BirthCertificateSearch;
import lk.rgd.common.api.domain.DSDivision;

import java.util.List;

/**
 * DAO for Birth Certificate Search auditing purposes
 *
 * @author Chathuranga Withana
 */
public interface BCSearchDAO {

    /**
     * Add a birth certificate search entry
     *
     * @param bcs the birth certificate search to be added
     */
    public void addBirthCertificateSearch(BirthCertificateSearch bcs);

    /**
     * Returns the Birth Certificate Search object for a given serial number(application number) and DSDivision
     *
     * @param dsDivision the DS division under which the BirthCertificateSearch serial number should be searched
     * @param serialNo   applicationNo given to the BirthCertificateSearch
     * @return BirthCertificateSearch or null if results not found
     */
    public BirthCertificateSearch getByDSDivisionAndSerialNo(DSDivision dsDivision, String serialNo);
}
