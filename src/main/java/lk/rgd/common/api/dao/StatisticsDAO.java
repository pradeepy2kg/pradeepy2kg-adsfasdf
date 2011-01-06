package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.Statistics;

/**
 * @author shan
 */
public interface StatisticsDAO {

    /**
     * Returns Statistics record for given user
     *
     * @param userId        the User ID
     * @return Statistics   the object for given user
     */
    public Statistics getByUser(String userId);

    /**
     * Add a Statistics
     * 
     * @param statistics    the object to add
     */
    public void addStatistics(Statistics statistics);

    /**
     * Updates given Statistics Record
     *
     * @param statistics
     */
    public void updateStatistics(Statistics statistics);

}
