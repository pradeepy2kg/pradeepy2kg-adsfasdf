package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.Statistics;
import lk.rgd.common.api.domain.User;

import java.util.List;

/**
 * @author shan
 */
public interface StatisticsDAO {

    /**
     * Returns Statistics record for given user
     *
     * @param userId User ID
     * @return Statistics object for given user
     */
    public Statistics getByUser(String userId);

}
