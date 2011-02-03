package lk.rgd.common.core.dao;

import lk.rgd.common.api.dao.UserLocationDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.UserLocation;
import lk.rgd.common.api.domain.UserLocationID;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * @author asankha
 */
public class UserLocationDAOImpl extends BaseDAO implements UserLocationDAO {

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserLocation getUserLocation(String userId, int locationUKey) {
        return em.find(UserLocation.class, new UserLocationID(userId, locationUKey));
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void save(UserLocation userLocation, User adminUser) {
        userLocation.getUser().getLocations().add(userLocation);
        userLocation.getLocation().getUsers().add(userLocation);
        userLocation.getLifeCycleInfo().setCreatedUser(adminUser);
        userLocation.getLifeCycleInfo().setCreatedTimestamp(new Date());
        userLocation.getLifeCycleInfo().setLastUpdatedUser(adminUser);
        userLocation.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        em.persist(userLocation);
        em.merge(userLocation.getUser());
        em.merge(userLocation.getLocation());
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void update(UserLocation userLocation, User adminUser) {
        UserLocation existing =
            em.find(UserLocation.class, new UserLocationID(userLocation.getUserId(), userLocation.getLocationId()));
        if (existing != null) {
            existing.setEndDate(userLocation.getEndDate());
            existing.setStartDate(userLocation.getStartDate());
            existing.setSignAdoptionCert(userLocation.isSignAdoptionCert());
            existing.setSignBirthCert(userLocation.isSignBirthCert());
            existing.setSignDeathCert(userLocation.isSignDeathCert());
            existing.setSignMarriageCert(userLocation.isSignMarriageCert());
            existing.getLifeCycleInfo().setActive(userLocation.getLifeCycleInfo().isActive());
            existing.getLifeCycleInfo().setLastUpdatedUser(adminUser);
            existing.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
            em.merge(existing);

            logger.debug("End date of the existing is :{}", existing.getEndDate());
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<UserLocation> getAllUserLocations(boolean active) {
        Query q = em.createNamedQuery("getAllUserLocations");
        q.setParameter("active", active);
        return q.getResultList();
    }

    @Override
    public List<UserLocation> getUserLocationsListByUserId(String userId) {
        Query q = em.createNamedQuery("getUserLocationsByUserId");
        q.setParameter("userId", userId);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> getBirthCertSignUsersByLocationId(int locationId, boolean active) {
        Query q = em.createNamedQuery("get.birthCertSign.user.by.locationId");
        q.setParameter("locationId", locationId);
        q.setParameter("active", active);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> getMarriageCertificateSignUsersByLocationId(int locationId, boolean active) {
        Query q = em.createNamedQuery("get.marriage.certificateSign.user.by.locationId.and.active");
        q.setParameter("locationId", locationId);
        q.setParameter("active", active);
        return q.getResultList();
    }

    /**
     * Returns currently active UserLocations list for given user id
     *
     * @param userId
     * @param active
     * @return List<UserLocation> for given user id and active = true
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<UserLocation> getActiveUserLocations(String userId, boolean active) {
        Query q = em.createNamedQuery("get.active.locations.by.userId");
        q.setParameter("userId", userId);
        q.setParameter("active", active);
        return q.getResultList();
    }
}
