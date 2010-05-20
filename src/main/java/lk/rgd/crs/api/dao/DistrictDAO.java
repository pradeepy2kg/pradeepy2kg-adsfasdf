package lk.rgd.crs.api.dao;

import lk.rgd.crs.api.domain.District;

import java.util.List;

/**
 * @author asankha
 */
public interface DistrictDAO {

    /**
     * Returns the list of Districts for the given language
     * @param language the language ID (see AppConstants)
     * @return the list of known districts for the given language
     */
    public List<District> getDistricts(String language);
}
