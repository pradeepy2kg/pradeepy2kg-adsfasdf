package lk.rgd.crs.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.PreloadableDAO;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.BDDivision;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author asankha
 */
public class BDDivisionDAOImpl extends BaseDAO implements BDDivisionDAO, PreloadableDAO {

    // direct cache of objects by PK - bdDivisionUKey
    private final Map<Integer, BDDivision> bdDivisionsByPK = new HashMap<Integer, BDDivision>();
    // local caches indexed by dsDivisionUKey and bdDivisionUKey
    private final Map<Integer, Map<Integer, String>> siDivisions = new HashMap<Integer, Map<Integer, String>>();
    private final Map<Integer, Map<Integer, String>> enDivisions = new HashMap<Integer, Map<Integer, String>>();
    private final Map<Integer, Map<Integer, String>> taDivisions = new HashMap<Integer, Map<Integer, String>>();

    /**
     * @inheritDoc
     */
    public Map<Integer, String> getBDDivisionNames(int dsDivisionUKey, String language, User user) {

        Map<Integer, String> result = null;
        if (AppConstants.SINHALA.equals(language)) {
            result = siDivisions.get(dsDivisionUKey);
        } else if (AppConstants.ENGLISH.equals(language)) {
            result = enDivisions.get(dsDivisionUKey);
        } else if (AppConstants.TAMIL.equals(language)) {
            result = taDivisions.get(dsDivisionUKey);
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
        }
        if (result == null) {
            handleException("Invalid DS Division id : " + dsDivisionUKey, ErrorCodes.INVALID_DSDIVISION);
        }
        return result;
    }

    public String getNameByPK(int bdDivisionUKey, String language) {
        if (AppConstants.SINHALA.equals(language)) {
            return bdDivisionsByPK.get(bdDivisionUKey).getSiDivisionName();
        } else if (AppConstants.ENGLISH.equals(language)) {
            return bdDivisionsByPK.get(bdDivisionUKey).getEnDivisionName();
        } else if (AppConstants.TAMIL.equals(language)) {
            return bdDivisionsByPK.get(bdDivisionUKey).getTaDivisionName();
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
        }
        return AppConstants.EMPTY_STRING;
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public BDDivision getBDDivisionByPK(int bdDivision) {
        return em.find(BDDivision.class, bdDivision);
    }

    /**
     * Add a new BDDivision and cache
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void add(BDDivision bdDivision, User user) {

        if (isEmptyString(bdDivision.getEnDivisionName()) ||
            isEmptyString(bdDivision.getEnDivisionName()) ||
            isEmptyString(bdDivision.getEnDivisionName())) {
            throw new CRSRuntimeException("One or more names of the BD Division is invalid", ErrorCodes.INVALID_DATA);
        }

        if (user.isAuthorized(Permission.ADD_EDIT_DIVISIONS)) {
            try {
                em.persist(bdDivision);
                updateCache(bdDivision);
                logger.info("New BD Division added : {} by : {}", bdDivision.getEnDivisionName(), user.getUserId());
            } catch (Exception e) {
                logger.error("Attempt to add BD Division : " + bdDivision.getEnDivisionName() + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                " was not allowed to add a new BD Division : " + bdDivision.getEnDivisionName());
        }
    }

    /**
     * Inactivate a BDDivision and update cache
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void inactivate(BDDivision bdDivision, User user) {
        if (user.isAuthorized(Permission.ADD_EDIT_DIVISIONS)) {
            try {
                BDDivision existing = em.find(BDDivision.class, bdDivision.getBdDivisionUKey());
                if (existing != null) {
                    existing.setActive(false);
                    em.merge(existing);
                    updateCache(existing);
                }
                logger.info("BD Division : {} inactivated by : {}", bdDivision.getEnDivisionName(), user.getUserId());
            } catch (Exception e) {
                logger.error("Attempt to inactivate BD Division : " + bdDivision.getEnDivisionName() + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                " was not allowed to inactivate BD Division : " + bdDivision.getEnDivisionName());
        }
    }

    /**
     * Activate a BDDivision and update cache
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void activate(BDDivision bdDivision, User user) {
        if (user.isAuthorized(Permission.ADD_EDIT_DIVISIONS)) {
            try {
                BDDivision existing = em.find(BDDivision.class, bdDivision.getBdDivisionUKey());
                if (existing != null) {
                    existing.setActive(true);
                    em.merge(existing);
                    updateCache(existing);
                }
                logger.info("BD Division : {} activated by : {}", bdDivision.getEnDivisionName(), user.getUserId());
            } catch (Exception e) {
                logger.error("Attempt to activate BD Division : " + bdDivision.getEnDivisionName() + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                " was not allowed to activate BD Division : " + bdDivision.getEnDivisionName());
        }
    }

    /**
     * Loads all values from the database table into a cache
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public void preload() {

        Query query = em.createQuery("SELECT d FROM BDDivision d");
        List<BDDivision> results = query.getResultList();

        for (BDDivision r : results) {
            updateCache(r);
        }

        logger.debug("Loaded : {} birth and death registration divisions from the database", results.size());
    }

    private void updateCache(BDDivision r) {
        final int dsDivisionUKey = r.getDsDivision().getDsDivisionUKey();
        final int bdDivisionId   = r.getDivisionId();
        final int bdDivisionUKey = r.getBdDivisionUKey();

        bdDivisionsByPK.put(bdDivisionUKey, r);

        Map<Integer, String> districtMap = siDivisions.get(dsDivisionUKey);
        if (districtMap == null) {
            districtMap = new HashMap<Integer, String>();
            siDivisions.put(dsDivisionUKey, districtMap);
        }
        districtMap.put(bdDivisionUKey, bdDivisionId + ": " + r.getSiDivisionName());

        districtMap = enDivisions.get(dsDivisionUKey);
        if (districtMap == null) {
            districtMap = new HashMap<Integer, String>();
            enDivisions.put(dsDivisionUKey, districtMap);
        }
        districtMap.put(bdDivisionUKey, bdDivisionId + SPACER + r.getEnDivisionName());

        districtMap = taDivisions.get(dsDivisionUKey);
        if (districtMap == null) {
            districtMap = new HashMap<Integer, String>();
            taDivisions.put(dsDivisionUKey, districtMap);
        }
        districtMap.put(bdDivisionUKey, bdDivisionId + SPACER + r.getTaDivisionName());
    }
}