package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.District;

import java.util.Map;

/**
 * @author asankha
 */
public interface DistrictDAO {

    /**
     * Returns the list of Districts for the given language
     *
     * @param language the language ID (see AppConstants)
     * @return a Map of known districts for the given language along with the ID
     */
    public Map<Integer, String> getDistricts(String language, User user);

    /**
     * Return District by id
     * @param id the District ID
     * @return the District
     */
    public District getDistrict(int id);
}
