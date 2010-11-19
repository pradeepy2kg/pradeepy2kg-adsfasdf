package lk.rgd.common.core.dao;

import lk.rgd.common.api.dao.LocationDAO;
import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Role;
import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import javax.persistence.NoResultException;
import java.util.*;

/**
 * @author asankha
 */
public class LocationDAOImpl extends BaseDAO implements LocationDAO, PreloadableDAO {
    private final Map<Integer, Location> locationsByPK = new HashMap<Integer, Location>();
    private final Map<Integer, String> siLocationName = new TreeMap<Integer, String>();
    private final Map<Integer, String> taLocationName = new TreeMap<Integer, String>();
    private final Map<Integer, String> enLocationName = new TreeMap<Integer, String>();

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

    public Map<Integer, String> getLocationList(String language, User user) {
        if (user == null) {
            logger.error("Error getting Locations Names using null for User");
            throw new IllegalArgumentException("User can not be null");
        }
        Map<Integer, String> result = getAllLocationNames(language);
        return result;
    }

    private Map<Integer, String> getAllLocationNames(String language) {
        Map<Integer, String> result = null;
        if (AppConstants.SINHALA.equals(language)) {
            result = siLocationName;
        } else if (AppConstants.ENGLISH.equals(language)) {
            result = enLocationName;
        } else if (AppConstants.TAMIL.equals(language)) {
            result = taLocationName;
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
        }
        return result;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public Location getLocationByCode(int locationCode) {
        Query q = em.createNamedQuery("get.location.by.code");
        q.setParameter("locationCode", locationCode);
        try {
            return (Location) q.getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Location getLocationByCodeAndByDSDivisionID(int locationCode, int dsDivisionId){
        Query q = em.createNamedQuery("get.location.by.code.and.by.dsDivisionId");
        q.setParameter("locationCode", locationCode);
        q.setParameter("dsDivisionId", dsDivisionId);
        try{
            return (Location) q.getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public void preload() {
         Query q = em.createNamedQuery("getAllLocations");
        List<Location> results = q.getResultList();
        logger.debug("Loaded : {} Locations from the database", results.size());
        for (Location l : results) {
            updateCache(l);
        }

    }

    private void updateCache(Location l) {
        final int locationId = l.getLocationCode();
        final int locationUKey = l.getLocationUKey();
        locationsByPK.put(locationUKey, l);
        siLocationName.put(locationUKey, locationId + SPACER + l.getSiLocationName());
        enLocationName.put(locationUKey, locationId + SPACER + l.getEnLocationName());
        taLocationName.put(locationUKey, locationId + SPACER + l.getTaLocationName());
    }

}
