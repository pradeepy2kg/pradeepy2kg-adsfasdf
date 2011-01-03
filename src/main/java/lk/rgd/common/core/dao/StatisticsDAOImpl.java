package lk.rgd.common.core.dao;

import lk.rgd.common.api.dao.StatisticsDAO;
import lk.rgd.common.api.domain.Statistics;
import lk.rgd.common.api.domain.User;

import javax.persistence.Query;
import java.util.List;

/**
 * @author shan
 */
public class StatisticsDAOImpl extends BaseDAO implements StatisticsDAO {

    /**
     * @inheritDoc
     */
    @Override
    public Statistics getByUser(String userId) {
        Query query = em.createNamedQuery("get.by.user");
        query.setParameter("userId", userId);
        return (Statistics) query.getSingleResult();
    }
}
