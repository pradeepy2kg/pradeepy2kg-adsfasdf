package lk.rgd.crs.core.dao;

import lk.rgd.crs.api.dao.AppParametersDAO;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Defines an interface to be implemented by DAO's that requires preloading of data
 *
 * @author asankha
 */
public interface PreloadableDAO {

    /**
     * Defines the method invoked on Spring context initialization to preload DAO's that implements this interface
     */
    public void preload();
}
