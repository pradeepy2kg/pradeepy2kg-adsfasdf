package lk.rgd.common.core.dao;

import lk.rgd.common.api.dao.LocationDAO;
import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * @author asankha
 */
public class LocationDAOImpl extends BaseDAO implements LocationDAO {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Location getLocation(int locationUKey) {
        return em.find(Location.class, locationUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void add(Location location, User admin) {
        location.getLifeCycleInfo().setCreatedUser(admin);
        location.getLifeCycleInfo().setCreatedTimestamp(new Date());
        location.getLifeCycleInfo().setLastUpdatedUser(admin);
        location.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        em.persist(location);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void update(Location location, User admin) {
        location.getLifeCycleInfo().setLastUpdatedUser(admin);
        location.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        em.merge(location);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Location> getAllLocations() {
        Query q = em.createNamedQuery("getAllLocations");
        return q.getResultList();

    }
}
