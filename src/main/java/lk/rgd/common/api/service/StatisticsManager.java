package lk.rgd.common.api.service;

import lk.rgd.common.api.domain.Statistics;

/**
 * @author shan
 */
public interface StatisticsManager {

    /**
     * Updates statistics record for given user
     *
     * @param userId
     * @param statistics
     */
    public void updateStatistics(String userId, Statistics statistics);

    /**
     * This scheduled task runs on every week day at 11.00 PM. it collects
     * statistics of every user and stores in the STATISTICS table.
     */
    public void triggerScheduledStatJobs();

}
