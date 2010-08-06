package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.Country;

import java.util.Map;

/**
 * @author asankha
 */
public interface CountryDAO {
    /**
     * Returns the list of Countries for the given language
     * @param language the language ID (see AppConstants)
     * @return a Map of known countries for the given language along with the ID
     */
    public Map<Integer, String> getCountries(String language);

    /**
     * Return the name of the country in the given language
     * @param countryUKey the country unique id
     * @param language language selected
     * @return name of the country in the selected language
     */
    public String getNameByPK(int countryUKey, String language);

    /**
     * Returns the Country for given Id
     * @param id the country ID (see AppConstants)
     * @return a Country 
     */
    public Country getCountry(int id);
}