package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.BDDivision;

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
     * @param districtId the district ID
     * @param language   the language ID (see AppConstants)
     * @return a Map of the list of known Birth and Death Registration divisions for the given language - with the ID
     */
    public Map<Integer, String> getBDDivisionNames(int districtId, String language, User user);

    /**
     * Return BDDivision by Id
     * @param  districtId Birth Death district ID
     * @param  bdDivisionId Birth Death division ID
     * @return BDDivision
     */
    public BDDivision getBDDivision(int districtId, int bdDivisionId);

    public BDDivision getBDDivisionByPK(int bdDivisionUKey);
}