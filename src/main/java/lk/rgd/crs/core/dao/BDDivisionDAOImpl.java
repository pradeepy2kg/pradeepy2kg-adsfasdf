package lk.rgd.crs.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.core.dao.PreloadableDAO;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.BDDivision;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import javax.persistence.NoResultException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public BDDivision getBDDivisionByPK(int bdDivision) {
        return em.find(BDDivision.class, bdDivision);
    }

    @Override
    public BDDivision getBDDivisionByCode(int bdDivisionId, DSDivision dsDivision) {
        Query q = em.createNamedQuery("get.bdDivision.by.code");
        q.setParameter("bdDivisionId", bdDivisionId);
        q.setParameter("dsDivision", dsDivision);
        try {
            return (BDDivision) q.getSingleResult();
        }
        catch (NoResultException e) {
            logger.debug("No id duplication of id :{}", bdDivisionId);
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void add(BDDivision bdDivision, User user) {
        em.persist(bdDivision);
        updateCache(bdDivision);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void update(BDDivision bdDivision, User user) {
        em.merge(bdDivision);
        updateCache(bdDivision);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<BDDivision> findAll() {
        Query q = em.createNamedQuery("findAllBDDivisions");
        return q.getResultList();
    }

     @Transactional(propagation = Propagation.SUPPORTS)
    public List<BDDivision> getAllDSDivisionByDsDivisionKey(int dsDivisionId) {
        Query q = em.createNamedQuery("get.all.divisions.by.dsDivisionId");
        q.setParameter("dsDivisionId",dsDivisionId);
        return q.getResultList();
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
        final int bdDivisionId = r.getDivisionId();
        final int bdDivisionUKey = r.getBdDivisionUKey();

        bdDivisionsByPK.put(bdDivisionUKey, r);

        Map<Integer, String> districtMap = siDivisions.get(dsDivisionUKey);
        if (districtMap == null) {
            districtMap = new TreeMap<Integer, String>();
            siDivisions.put(dsDivisionUKey, districtMap);
        }
        districtMap.put(bdDivisionUKey, bdDivisionId + ": " + r.getSiDivisionName());

        districtMap = enDivisions.get(dsDivisionUKey);
        if (districtMap == null) {
            districtMap = new TreeMap<Integer, String>();
            enDivisions.put(dsDivisionUKey, districtMap);
        }
        districtMap.put(bdDivisionUKey, bdDivisionId + SPACER + r.getEnDivisionName());

        districtMap = taDivisions.get(dsDivisionUKey);
        if (districtMap == null) {
            districtMap = new TreeMap<Integer, String>();
            taDivisions.put(dsDivisionUKey, districtMap);
        }
        districtMap.put(bdDivisionUKey, bdDivisionId + SPACER + r.getTaDivisionName());
    }
}