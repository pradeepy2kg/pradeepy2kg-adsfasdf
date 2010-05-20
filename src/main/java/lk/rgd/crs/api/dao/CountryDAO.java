package lk.rgd.crs.api.dao;

import lk.rgd.crs.api.domain.Country;

import java.util.List;

/**
 * @author asankha
 */
public interface CountryDAO {

    /**
     * Returns the list of Districts for the given language
     * @param language the language ID (see AppConstants)
     * @return the list of known districts for the given language
     */
    public List<Country> getCountries(String language);
}