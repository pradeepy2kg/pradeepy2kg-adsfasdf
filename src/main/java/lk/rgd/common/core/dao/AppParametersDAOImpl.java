package lk.rgd.common.core.dao;

import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.api.domain.AppParameter;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The application parameter DAO. This always caches parameters by reading the complete table at first read,
 * and on any subsequent updates
 *
 * @author asankha
 */
public class AppParametersDAOImpl extends BaseDAO implements AppParametersDAO, PreloadableDAO {

    /**
     * The internal Map where parameters read from the table are held
     */
    private final Map<String, String> parameters = new HashMap<String, String>();

    /**
     * Loads all values from the database table into a cache
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public void preload() {

        // load application parameters
        Query query = em.createQuery("SELECT p FROM AppParameter p");
        List<AppParameter> params = query.getResultList();

        for (AppParameter p : params) {
            parameters.put(p.getName(), p.getValue());
        }

        logger.debug("Loaded : {} parameters from the database", params.size());
    }

    /**
     * @inheritDoc
     */
    public int getIntParameter(String key) {
        String value = parameters.get(key);
        if (value != null) {
            try {
                return Integer.valueOf(value);
            } catch (NumberFormatException ignore) {}
        }
        return -1;
    }

    /**
     * @inheritDoc
     */
    public String getStringParameter(String key) {
        return parameters.get(key);
    }

    /**
     * @inheritDoc
     */
    public void setIntParameter(String key, int value) {
        // persist to the database
        em.persist(new AppParameter(key, value));

        // update cache
        parameters.put(key, Integer.toString(value));
    }

    /**
     * @inheritDoc
     */
    public void setStringParameter(String key, String value) {
        // persist to the database
        em.persist(new AppParameter(key, value));

        // update cache
        parameters.put(key, value);
    }
}
