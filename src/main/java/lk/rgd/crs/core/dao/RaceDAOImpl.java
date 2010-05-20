package lk.rgd.crs.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.crs.ErrorCodes;
import lk.rgd.crs.api.dao.RaceDAO;
import lk.rgd.crs.api.domain.Race;
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
public class RaceDAOImpl extends BaseDAO implements RaceDAO, PreloadableDAO {

    private final Map<Integer, String> siRaces = new HashMap<Integer, String>();
    private final Map<Integer, String> enRaces = new HashMap<Integer, String>();
    private final Map<Integer, String> taRraces = new HashMap<Integer, String>();

    /**
     * @inheritDoc
     */
    public Map<Integer, String> getRaces(String language) {
        if (AppConstants.SINHALA.equals(language)) {
            return siRaces;
        } else if (AppConstants.ENGLISH.equals(language)) {
            return enRaces;
        } else if (AppConstants.TAMIL.equals(language)) {
            return taRraces;
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

        Query query = em.createQuery("SELECT r FROM Race r");
        List<Race> results = query.getResultList();

        for (Race r : results) {
            siRaces.put(r.getRaceId(), r.getSiRaceName());
            enRaces.put(r.getRaceId(), r.getEnRaceName());
            taRraces.put(r.getRaceId(), r.getTaRaceName());
        }

        logger.debug("Loaded : {} races from the database", results.size());
    }
}