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
     * @param districtIdUKey the district ID unique key
     * @param language   the language ID (see AppConstants)
     * @return a Map of the list of known Birth and Death Registration divisions for the given language - with the ID
     */
    public Map<Integer, String> getBDDivisionNames(int districtIdUKey, String language, User user);

    /**
     * Return the name of the B.D. Division in the given language
     * @param bdDivisionUKey the unique BD Division ID
     * @param language selected language to return the name
     * @return the name of the BD division in the selected language
     */
    public String getNameByPK(int bdDivisionUKey, String language);

    public BDDivision getBDDivisionByPK(int bdDivisionUKey);
}