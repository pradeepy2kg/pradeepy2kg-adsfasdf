package lk.rgd.common.core.dao;

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
