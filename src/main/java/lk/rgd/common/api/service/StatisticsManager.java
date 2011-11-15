package lk.rgd.common.api.service;

import lk.rgd.common.api.domain.Statistics;
import lk.rgd.common.api.domain.User;

import java.util.Date;

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
     * @param user      user
     * @param startDate
     * @param endDate   @return statistics object
     */
    public Statistics getStatisticsForUser(User user, Date startDate, Date endDate);

    /**
     * Save a statistics record
     *
     * @param user
     * @param statistics
     */
    public void addStatistics(User user, Statistics statistics);

    /**
     * check whether user has a statistics record already
     *
     * @param user
     * @return true - if exists : false - if not
     */
    public boolean existsStatisticsForUser(User user);

    /**
     * delete all from statistics table
     */
    public void deleteOldStatistics();

}
