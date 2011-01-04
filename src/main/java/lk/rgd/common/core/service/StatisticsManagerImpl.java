package lk.rgd.common.core.service;

import lk.rgd.common.api.dao.StatisticsDAO;
import lk.rgd.common.api.service.StatisticsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(propagation = Propagation.NEVER)
    public void triggerScheduledStatJobs() {
        logger.info("Start executing Statistics related scheduled tasks..");
        //todo ...
    }

}
