package lk.rgd.crs.core.service;

import lk.rgd.crs.api.domain.AppParameter;
import lk.rgd.crs.api.service.AppParametersManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.annotation.Inherited;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The interface to application parameters. This instance in a singleton, and always caches parameters by reading the
 * complete table at first read, and on any subsequent updates
 *
 * @author asankha
 */
public class AppParametersManagerImpl implements AppParametersManager {

    private static final Logger logger = LoggerFactory.getLogger(AppParametersManagerImpl.class);

    /** The internal Map where parameters read from the table are held */
    private final Map<String, String> parameters = new HashMap<String, String>();
    private final EntityManager em;

    /**
     * Constructs the singleton instance by loading all values from the database table into a cache
     * @param em
     */
    public AppParametersManagerImpl(EntityManager em) {
        this.em = em;

        // load application parameters
        Query query = em.createQuery("SELECT value FROM app_parameters");
        List<AppParameter> params = query.getResultList();

        for (AppParameter p : params) {
            parameters.put(p.getKey(), p.getValue());
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
