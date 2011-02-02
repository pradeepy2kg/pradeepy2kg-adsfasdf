package lk.rgd.crs.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.common.core.dao.PreloadableDAO;
import lk.rgd.crs.api.dao.MRDivisionDAO;
import lk.rgd.crs.api.domain.MRDivision;
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
public class MRDivisionDAOImpl extends BaseDAO implements MRDivisionDAO, PreloadableDAO {

    // direct cache of objects by PK - mrDivisionUKey
    private final Map<Integer, MRDivision> mrDivisionsByPK = new HashMap<Integer, MRDivision>();
    // local caches indexed by dsDivisionUKey and mrDivisionUKey
    private final Map<Integer, Map<Integer, String>> siDivisions = new HashMap<Integer, Map<Integer, String>>();
    private final Map<Integer, Map<Integer, String>> enDivisions = new HashMap<Integer, Map<Integer, String>>();
    private final Map<Integer, Map<Integer, String>> taDivisions = new HashMap<Integer, Map<Integer, String>>();

    /**
     * @inheritDoc
     */
    public Map<Integer, String> getMRDivisionNames(int dsDivisionUKey, String language, User user) {

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
            //handleException("Invalid DS Division id : " + dsDivisionUKey, ErrorCodes.INVALID_DSDIVISION);
            result = new HashMap<Integer, String>();
        }
        return result;
    }

    public String getNameByPK(int mrDivisionUKey, String language) {
        if (AppConstants.SINHALA.equals(language)) {
            return mrDivisionsByPK.get(mrDivisionUKey).getSiDivisionName();
        } else if (AppConstants.ENGLISH.equals(language)) {
            return mrDivisionsByPK.get(mrDivisionUKey).getEnDivisionName();
        } else if (AppConstants.TAMIL.equals(language)) {
            return mrDivisionsByPK.get(mrDivisionUKey).getTaDivisionName();
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
        }
        return AppConstants.EMPTY_STRING;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public MRDivision getMRDivisionByPK(int mrDivision) {
        return em.find(MRDivision.class, mrDivision);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void add(MRDivision mrDivision, User user) {
        em.persist(mrDivision);
        updateCache(mrDivision);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void update(MRDivision mrDivision, User user) {
        em.merge(mrDivision);
        updateCache(mrDivision);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<MRDivision> findAll() {
        Query q = em.createNamedQuery("findAllMRDivisions");
        return q.getResultList();
    }

    @Override
    public MRDivision getMRDivisionByCode(int mrDivisionId, DSDivision dsDivision) {
       Query q = em.createNamedQuery("get.mrDivision.by.code");
        q.setParameter("mrDivisionId",mrDivisionId);
        q.setParameter("dsDivision", dsDivision);
        try {
            return (MRDivision) q.getSingleResult();
        }
        catch (NoResultException e) {
            logger.debug("No id duplication of id :{}",mrDivisionId);
            return null;
        }
    }

    /**
     * Loads all values from the database table into a cache
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public void preload() {

        Query query = em.createQuery("SELECT d FROM MRDivision d");
        List<MRDivision> results = query.getResultList();

        for (MRDivision r : results) {
            updateCache(r);
        }

        logger.debug("Loaded : {} marriage registration divisions from the database", results.size());
    }

    private void updateCache(MRDivision r) {
        final int dsDivisionUKey = r.getDsDivision().getDsDivisionUKey();
        final int mrDivisionId   = r.getDivisionId();
        final int mrDivisionUKey = r.getMrDivisionUKey();

        mrDivisionsByPK.put(mrDivisionUKey, r);

        Map<Integer, String> districtMap = siDivisions.get(dsDivisionUKey);
        if (districtMap == null) {
            districtMap = new TreeMap<Integer, String>();
            siDivisions.put(dsDivisionUKey, districtMap);
        }
        districtMap.put(mrDivisionUKey, mrDivisionId + ": " + r.getSiDivisionName());

        districtMap = enDivisions.get(dsDivisionUKey);
        if (districtMap == null) {
            districtMap = new TreeMap<Integer, String>();
            enDivisions.put(dsDivisionUKey, districtMap);
        }
        districtMap.put(mrDivisionUKey, mrDivisionId + SPACER + r.getEnDivisionName());

        districtMap = taDivisions.get(dsDivisionUKey);
        if (districtMap == null) {
            districtMap = new TreeMap<Integer, String>();
            taDivisions.put(dsDivisionUKey, districtMap);
        }
        districtMap.put(mrDivisionUKey, mrDivisionId + SPACER + r.getTaDivisionName());
    }
}