package lk.rgd.common.core.service;

import lk.rgd.common.api.dao.StatisticsDAO;
import lk.rgd.common.api.service.StatisticsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shan
 */
public class StatisticsManagerImpl implements StatisticsManager {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsManagerImpl.class);
    private final StatisticsDAO statisticsDAO;

    public StatisticsManagerImpl(StatisticsDAO statisticsDAO) {
        this.statisticsDAO = statisticsDAO;
    }

    @Override
    public void updateStatistics(String userId) {

    }

}
