package lk.rgd.crs.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.common.core.dao.PreloadableDAO;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.BDDivision;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
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

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<BDDivision> getBDDivisionByCode(int bdDivisionId, DSDivision dsDivision) {
        Query q = em.createNamedQuery("get.bdDivision.by.code");
        q.setParameter("bdDivisionId", bdDivisionId);
        q.setParameter("dsDivision", dsDivision);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BDDivision> getBDDivisionByAnyNameAndDSDivisionKey(BDDivision bdDivision, int dsDivisionUKey) {
        Query q = em.createNamedQuery("get.bdDivision.by.dsDivision.anyName");
        q.setParameter("dsDivisionId", dsDivisionUKey);
        q.setParameter("siName", bdDivision.getSiDivisionName());
        q.setParameter("enName", bdDivision.getEnDivisionName());
        q.setParameter("taName", bdDivision.getTaDivisionName());
        return q.getResultList();
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
    public List<BDDivision> getAllBDDivisionByDsDivisionKey(int dsDivisionId) {
        Query q = em.createNamedQuery("get.all.divisions.by.dsDivisionId");
        q.setParameter("dsDivisionId", dsDivisionId);
        return q.getResultList();
    }

    /**
     * Loads all values from the database table into a cache
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public void preload() {

        Query query = em.createQuery("SELECT d FROM BDDivision d WHERE d.active = TRUE");
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
        final boolean active = r.isActive();

        Map<Integer, String> districtMapSi = siDivisions.get(dsDivisionUKey);
        if (districtMapSi == null) {
            districtMapSi = new TreeMap<Integer, String>();
            if (active) {
                siDivisions.put(dsDivisionUKey, districtMapSi);
            }
        }

        Map<Integer, String> districtMapEn = enDivisions.get(dsDivisionUKey);
        if (districtMapEn == null) {
            districtMapEn = new TreeMap<Integer, String>();
            if (active) {
                enDivisions.put(dsDivisionUKey, districtMapEn);
            }
        }

        Map<Integer, String> districtMapTa = taDivisions.get(dsDivisionUKey);
        if (districtMapTa == null) {
            districtMapTa = new TreeMap<Integer, String>();
            if (active) {
                taDivisions.put(dsDivisionUKey, districtMapTa);
            }
        }

        if (active) {
            bdDivisionsByPK.put(bdDivisionUKey, r);
            districtMapSi.put(bdDivisionUKey, bdDivisionId + SPACER + r.getSiDivisionName());
            districtMapEn.put(bdDivisionUKey, bdDivisionId + SPACER + r.getEnDivisionName());
            districtMapTa.put(bdDivisionUKey, bdDivisionId + SPACER + r.getTaDivisionName());
        } else {
            bdDivisionsByPK.remove(bdDivisionUKey);
            districtMapSi.remove(bdDivisionUKey);
            districtMapEn.remove(bdDivisionUKey);
            districtMapTa.remove(bdDivisionUKey);
        }
    }
}