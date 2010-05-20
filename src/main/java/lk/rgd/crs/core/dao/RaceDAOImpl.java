package lk.rgd.crs.core.dao;

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

    private final Map<String, List<Race>> races = new HashMap<String, List<Race>>();

    /**
     * @inheritDoc
     */
    public List<Race> getRaces(String language) {
        return races.get(language);
    }

    /**
     * Loads all values from the database table into a cache
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public void preload() {

        Query query = em.createQuery("SELECT r FROM Race r");
        List<Race> results = query.getResultList();

        for (Race r : results) {
            List<Race> list = races.get(r.getLanguageId());
            if (list == null) {
                list = new ArrayList<Race>();
                races.put(r.getLanguageId(), list);
            }
            list.add(r);
        }

        logger.debug("Loaded : {} races from the database", results.size());
    }
}