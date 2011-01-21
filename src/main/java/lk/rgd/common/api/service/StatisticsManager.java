package lk.rgd.common.api.service;

import lk.rgd.common.api.domain.Statistics;
import lk.rgd.common.api.domain.User;

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
     * Updates statisticsList. invoked by scheduled task only
     *
     */
    public void updateStatisticsList();

    /**
     * This scheduled task runs on every week day at 11.00 PM. it collects
     * statistics of every user and stores in the STATISTICS table.
     */
    public void triggerScheduledStatJobs();

    /**
     * Return Statistics object for given user
     * 
     * @param user  user
     * @return statistics object
     */
    public Statistics getStatisticsForUser(User user);

    /**
     * Save a statistics record
     *
     * @param user
     * @param statistics
     */
    public void addStatistics(User user, Statistics statistics);

    /**
     *  check whether user has a statistics record already
     *
     * @param user
     * @return  true - if exists : false - if not
     */
    public boolean existsStatisticsForUser(User user);

}
