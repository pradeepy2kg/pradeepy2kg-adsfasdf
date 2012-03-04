package lk.rgd.common.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.dao.LocationDAO;
import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
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
        updateCache(location);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void update(Location location, User admin) {
        location.getLifeCycleInfo().setLastUpdatedUser(admin);
        location.getLifeCycleInfo().setLastUpdatedTimestamp(new Date());
        em.merge(location);
        updateCache(location);
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

    public String getLocationNameByPK(int locationUKey, String language) {
        if (AppConstants.SINHALA.equals(language)) {
            return siLocationName.get(locationUKey);
        } else if (AppConstants.ENGLISH.equals(language)) {
            return enLocationName.get(locationUKey);
        } else if (AppConstants.TAMIL.equals(language)) {
            return taLocationName.get(locationUKey);
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
        }
        return AppConstants.EMPTY_STRING;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Location> getLocationByCode(String locationCode) {
        Query q = em.createNamedQuery("get.location.by.code");
        q.setParameter("locationCode", locationCode);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Location> getLocationByAnyName(Location location) {
        Query q = em.createNamedQuery("get.location.by.anyName");
        q.setParameter("siName", location.getSiLocationName());
        q.setParameter("enName", location.getEnLocationName());
        q.setParameter("taName", location.getTaLocationName());
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Location> getLocationByCodeAndByDSDivisionID(String locationCode, int dsDivisionId) {
        Query q = em.createNamedQuery("get.location.by.code.and.by.dsDivisionId");
        q.setParameter("locationCode", locationCode);
        q.setParameter("dsDivisionId", dsDivisionId);
        return q.getResultList();
    }

    @Transactional(propagation = Propagation.NEVER)
    public Map<Integer, String> getLocationByDSDivisionID(int dsDivisionId, String lang) {
        Query q = em.createNamedQuery("get.location.by.dsDivisionId");
        q.setParameter("dsDivisionId", dsDivisionId);
        List<Location> list = q.getResultList();
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (Location l : list) {
            map.put(l.getLocationUKey(), l.getLocationName(lang));
        }
        return map;
    }

    public Map<Integer, Location> getPreLoadedLocations() {
        return locationsByPK;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Location> getAllLocationsByDSDivisionKey(int dsDivisionId) {
        Query q = em.createNamedQuery("get.location.by.dsDivisionId");
        q.setParameter("dsDivisionId", dsDivisionId);
        return q.getResultList();
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
        final String locationId = l.getLocationCode();
        final int locationUKey = l.getLocationUKey();
        final boolean active = l.getLifeCycleInfo().isActive();

        if (active) {
            locationsByPK.put(locationUKey, l);
            siLocationName.put(locationUKey, locationId + SPACER + l.getSiLocationName());
            enLocationName.put(locationUKey, locationId + SPACER + l.getEnLocationName());
            taLocationName.put(locationUKey, locationId + SPACER + l.getTaLocationName());
        } else {
            locationsByPK.remove(locationUKey);
            siLocationName.remove(locationUKey);
            enLocationName.remove(locationUKey);
            taLocationName.remove(locationUKey);
        }
    }

}
