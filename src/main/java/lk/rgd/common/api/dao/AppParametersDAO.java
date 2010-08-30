package lk.rgd.common.api.dao;

/**
 * Stores application configuration parameters such as the number of days between the birth and submission date to
 * consider the registration as a 'late registration'.
 *
 * Parameters are held in a simple table as key - value pairs, where values are held as strings. However, this class
 * allows callers to request values as other data types after conversion - e.g. as integers.
 *
 * @author asankha
 */
public interface AppParametersDAO {

    /**
     * Returns the parameter value as an integer if found, or returns -1
     * @param key the key of the parameter
     * @return value of the parameter as an integer
     */
    public int getIntParameter(String key);

    /**
     * Returns the parameter value as a String if found, or returns null
     * @param key the key of the parameter
     * @return value of the parameter as a String
     */
    public String getStringParameter(String key);

    /**
     * Set an integer application parameter - mey be used to add a new one or update
     * @param key key to associate with
     * @param value value to be associated
     */
    public void setIntParameter(String key, int value);

    /**
     * Set a String application parameter - mey be used to add a new one or update
     * @param key key to associate with
     * @param value value to be associated
     */
    public void setStringParameter(String key, String value);
}
