package lk.rgd.common.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.api.dao.RaceDAO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author asankha
 */
public class RaceDAOImpl extends BaseDAO implements RaceDAO, PreloadableDAO {

    private final Map<Integer, Race> racesByPK = new HashMap<Integer, Race>();
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

    public String getNameByPK(int raceId, String language) {
        if (AppConstants.SINHALA.equals(language)) {
            return racesByPK.get(raceId).getSiRaceName();
        } else if (AppConstants.ENGLISH.equals(language)) {
            return racesByPK.get(raceId).getEnRaceName();
        } else if (AppConstants.TAMIL.equals(language)) {
            return racesByPK.get(raceId).getTaRaceName();
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
        }
        return AppConstants.EMPTY_STRING;
    }

    public Race getRace(int id) {
        return racesByPK.get(id);
    }

    /**
     * Loads all values from the database table into a cache
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public void preload() {

        Query query = em.createQuery("SELECT r FROM Race r");
        List<Race> results = query.getResultList();

        for (Race r : results) {
            racesByPK.put(r.getRaceId(), r);
            siRaces.put(r.getRaceId(), r.getRaceId() + SPACER + r.getSiRaceName());
            enRaces.put(r.getRaceId(), r.getRaceId() + SPACER + r.getEnRaceName());
            taRraces.put(r.getRaceId(), r.getRaceId() + SPACER + r.getTaRaceName());
        }

        logger.debug("Loaded : {} races from the database", results.size());
    }
}