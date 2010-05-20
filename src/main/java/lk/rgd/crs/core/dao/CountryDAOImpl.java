package lk.rgd.crs.core.dao;

import lk.rgd.crs.api.dao.CountryDAO;
import lk.rgd.crs.api.domain.Country;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author asankha
 */
public class CountryDAOImpl extends BaseDAO implements CountryDAO, PreloadableDAO {

    private final Map<String, List<Country>> countries = new HashMap<String, List<Country>>();

    /**
     * @inheritDoc
     */
    public List<Country> getCountries(String language) {
        return countries.get(language);
    }

    /**
     * Loads all values from the database table into a cache
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public void preload() {

        Query query = em.createQuery("SELECT c FROM Country c");
        List<Country> results = query.getResultList();

        for (Country p : results) {
            List<Country> list = countries.get(p.getLanguageId());
            if (list == null) {
                list = new ArrayList<Country>();
                countries.put(p.getLanguageId(), list);
            }
            list.add(p);
        }

        logger.debug("Loaded : {} countries from the database", results.size());
    }
}