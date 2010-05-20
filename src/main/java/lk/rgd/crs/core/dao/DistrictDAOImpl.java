package lk.rgd.crs.core.dao;

import lk.rgd.crs.api.dao.DistrictDAO;
import lk.rgd.crs.api.domain.District;
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
public class DistrictDAOImpl extends BaseDAO implements DistrictDAO, PreloadableDAO {

    private final Map<String, List<District>> districts = new HashMap<String, List<District>>(); 

    /**
     * @inheritDoc
     */
    public List<District> getDistricts(String language) {
        return districts.get(language);
    }

    /**
     * Loads all values from the database table into a cache
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public void preload() {

        Query query = em.createQuery("SELECT d FROM District d");
        List<District> results = query.getResultList();

        for (District p : results) {
            List<District> list = districts.get(p.getLanguageId());
            if (list == null) {
                list = new ArrayList<District>();
                districts.put(p.getLanguageId(), list);
            }
            list.add(p);
        }

        logger.debug("Loaded : {} districts from the database", results.size());
    }
}
