package lk.rgd.common.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.dao.CountryDAO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author asankha
 */
public class CountryDAOImpl extends BaseDAO implements CountryDAO, PreloadableDAO {

    private final Map<Integer, String> siCountries = new HashMap<Integer, String>();
    private final Map<Integer, String> enCountries = new HashMap<Integer, String>();
    private final Map<Integer, String> taCountries = new HashMap<Integer, String>();
    private final Map<Integer, Country> countriesByPK = new HashMap<Integer, Country>();

    /**
     * @inheritDoc
     */
    public Map<Integer, String> getCountries(String language) {
        if (AppConstants.SINHALA.equals(language)) {
            return siCountries;
        } else if (AppConstants.ENGLISH.equals(language)) {
            return enCountries;
        } else if (AppConstants.TAMIL.equals(language)) {
            return taCountries;
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
        }
        return null;
    }

    public String getNameByPK(int countryUKey, String language) {
        if (AppConstants.SINHALA.equals(language)) {
            return countriesByPK.get(countryUKey).getSiCountryName();
        } else if (AppConstants.ENGLISH.equals(language)) {
            return countriesByPK.get(countryUKey).getEnCountryName();
        } else if (AppConstants.TAMIL.equals(language)) {
            return countriesByPK.get(countryUKey).getTaCountryName();
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
        }
        return AppConstants.EMPTY_STRING;
    }

    public Country getCountry(int id) {
        Country c = countriesByPK.get(id);

        if (c==null) {
            handleException("Country not found for id : " + id, ErrorCodes.COUNTRY_NOT_FOUND);
        }

        return c;
    }

    /**
     * Loads all values from the database table into a cache
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public void preload() {

        Query query = em.createQuery("SELECT c FROM Country c");
        List<Country> results = query.getResultList();

        for (Country c : results) {
            siCountries.put(c.getCountryId(), c.getCountryCode() + SPACER + c.getSiCountryName());
            enCountries.put(c.getCountryId(), c.getCountryCode() + SPACER + c.getEnCountryName());
            taCountries.put(c.getCountryId(), c.getCountryCode() + SPACER + c.getTaCountryName());
            countriesByPK.put(c.getCountryId(), c);
        }

        logger.debug("Loaded : {} countries from the database", results.size());
    }
}