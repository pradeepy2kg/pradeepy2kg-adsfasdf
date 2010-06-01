package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.User;

import java.util.Map;

/**
 * DAO for Birth and Death registration divisions
 * <p/>
 * Note: Marriage registration divisions and Birth/Death registration divisions are different
 *
 * @author asankha
 */
public interface BDDivisionDAO {

    /**
     * Returns the list of Birth & Death Registration Divisions within a selected district for the given language
     *
     * @param language   the language ID (see AppConstants)
     * @param districtId the district ID
     * @return a Map of the list of known Birth and Death Registration divisions for the given language - with the ID
     */
    public Map<Integer, String> getDivisions(String language, int districtId, User user);
}