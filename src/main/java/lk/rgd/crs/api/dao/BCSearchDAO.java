package lk.rgd.crs.api.dao;

import lk.rgd.crs.api.domain.BirthCertificateSearch;

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
}
