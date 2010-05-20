package lk.rgd.crs.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.crs.ErrorCodes;
import lk.rgd.crs.api.dao.DistrictDAO;
import lk.rgd.crs.api.domain.District;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author asankha
 */
public class DistrictDAOImpl extends BaseDAO implements DistrictDAO, PreloadableDAO {

    private final Map<Integer, String> siDistricts = new HashMap<Integer, String>();
    private final Map<Integer, String> enDistricts = new HashMap<Integer, String>();
    private final Map<Integer, String> taDistricts = new HashMap<Integer, String>();

    /**
     * @inheritDoc
     */
    public Map<Integer, String> getDistricts(String language) {
        if (AppConstants.SINHALA.equals(language)) {
            return siDistricts;
        } else if (AppConstants.ENGLISH.equals(language)) {
            return enDistricts;
        } else if (AppConstants.TAMIL.equals(language)) {
            return taDistricts;
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

        Query query = em.createQuery("SELECT d FROM District d");
        List<District> results = query.getResultList();

        for (District d : results) {
            siDistricts.put(d.getDistrictId(), d.getSiDistrictName());
            enDistricts.put(d.getDistrictId(), d.getEnDistrictName());
            taDistricts.put(d.getDistrictId(), d.getTaDistrictName());
        }

        logger.debug("Loaded : {} districts from the database", results.size());
    }
}
