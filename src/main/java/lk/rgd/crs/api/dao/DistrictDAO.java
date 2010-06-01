package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.User;

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
}
