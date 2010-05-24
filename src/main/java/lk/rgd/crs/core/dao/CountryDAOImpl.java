package lk.rgd.crs.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.crs.ErrorCodes;
import lk.rgd.crs.api.dao.CountryDAO;
import lk.rgd.crs.api.domain.Country;
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

    /**
     * Loads all values from the database table into a cache
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public void preload() {

        Query query = em.createQuery("SELECT c FROM Country c");
        List<Country> results = query.getResultList();

        for (Country c : results) {
            siCountries.put(c.getCountryId(), c.getSiCountryName());
            enCountries.put(c.getCountryId(), c.getEnCountryName());
            taCountries.put(c.getCountryId(), c.getTaCountryName());
        }

        logger.debug("Loaded : {} countries from the database", results.size());
    }
}