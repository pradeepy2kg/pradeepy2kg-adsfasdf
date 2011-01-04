package lk.rgd.common.api.service;

/**
 * @author shan
 */
public interface StatisticsManager {

    /**
     * Updates statistics record for given user
     *
     * @param userId
     */
    public void updateStatistics(String userId);

    /**
     * This scheduled task runs on every week day at 11.00 PM. it collects
     * statistics of every user and stores in the STATISTICS table.
     */
    public void triggerScheduledStatJobs();

}
