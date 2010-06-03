package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;

import java.util.Map;

/**
 * @author asankha
 */
public interface DSDivisionDAO {

    /**
     * Returns the list of DSDivisions for the given language
     *
     * @param districtId the District ID
     * @param language the language ID (see AppConstants)
     * @return a Map of known D.S. Divisions for the given language along with the ID
     */
    public Map<Integer, String> getDSDivisionNames(int districtId, String language);

    /**
     * Return DSDivision by id
     * @param dsDivisionUKey the unique internal ID (PK)
     * @return the DSDivision
     */
    public DSDivision getDSDivisionByPK(int dsDivisionUKey);
}