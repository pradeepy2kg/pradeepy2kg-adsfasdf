package lk.rgd.common.core.dao;

import lk.rgd.common.api.dao.StatisticsDAO;
import lk.rgd.common.api.domain.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Date;

/**
 * @author shan
 */
public class StatisticsDAOImpl extends BaseDAO implements StatisticsDAO {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsDAOImpl.class);

    /**
     * @inheritDoc
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Statistics getByUser(String userId) {
        Query query = em.createNamedQuery("get.by.user");
        query.setParameter("userId", userId);
        try {
            return (Statistics) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void addStatistics(Statistics statistics) {
        statistics.setCreatedTimestamp(new Date());
        em.persist(statistics);
        logger.debug("Statistics ID : {}", statistics.getIdUkey());
    }

    /**
     * @inheritDoc
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateStatistics(Statistics statistics) {
        statistics.setCreatedTimestamp(new Date());
        em.merge(statistics);
    }
}
