package lk.rgd.crs.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.common.core.dao.PreloadableDAO;
import lk.rgd.crs.api.dao.GNDivisionDAO;
import lk.rgd.crs.api.domain.GNDivision;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.*;

/**
 * @author amith jayasekara
 */
public class GNDivisionDAOImpl extends BaseDAO implements GNDivisionDAO, PreloadableDAO {

    // direct cache of objects by PK - bdDivisionUKey
    private final Map<Integer, GNDivision> gnDivisionsByPK = new HashMap<Integer, GNDivision>();
    // local caches indexed by dsDivisionUKey and bdDivisionUKey
    private final Map<Integer, Map<Integer, String>> siGNDivisions = new HashMap<Integer, Map<Integer, String>>();
    private final Map<Integer, Map<Integer, String>> enGNDivisions = new HashMap<Integer, Map<Integer, String>>();
    private final Map<Integer, Map<Integer, String>> taGNDivisions = new HashMap<Integer, Map<Integer, String>>();

    /**
     * @inheritDoc
     */
    public Map<Integer, String> getGNDivisionNames(int dsDivisionUKey, String language, User user) {
        Map<Integer, String> result = null;
        if (AppConstants.SINHALA.equals(language)) {
            result = siGNDivisions.get(dsDivisionUKey);
        } else if (AppConstants.ENGLISH.equals(language)) {
            result = enGNDivisions.get(dsDivisionUKey);
        } else if (AppConstants.TAMIL.equals(language)) {
            result = taGNDivisions.get(dsDivisionUKey);
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
        }
        if (result == null) {
            handleException("Invalid DS Division id : " + dsDivisionUKey, ErrorCodes.INVALID_DSDIVISION);
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public GNDivision getGNDivisionByPK(int gnDivisionUKey) {
        return em.find(GNDivision.class, gnDivisionUKey);
    }

    public String getNameByPK(int gnDivisionUKey, String language) {
        if (AppConstants.SINHALA.equals(language)) {
            return gnDivisionsByPK.get(gnDivisionUKey).getSiGNDivisionName();
        } else if (AppConstants.ENGLISH.equals(language)) {
            return gnDivisionsByPK.get(gnDivisionUKey).getEnGNDivisionName();
        } else if (AppConstants.TAMIL.equals(language)) {
            return gnDivisionsByPK.get(gnDivisionUKey).getTaGNDivisionName();
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
        }
        return AppConstants.EMPTY_STRING;
    }

    /**
     * Loads all values from the database table into a cache
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public void preload() {

        Query query = em.createQuery("SELECT d FROM GNDivision d");
        List<GNDivision> results = query.getResultList();

        for (GNDivision r : results) {
            updateCache(r);
        }

        logger.debug("Loaded : {} birth and death registration divisions from the database", results.size());
    }

    private void updateCache(GNDivision r) {
        final int dsDivisionUKey = r.getDsDivision().getDsDivisionUKey();
        final int bdDivisionId = r.getGnDivisionId();
        final int bdDivisionUKey = r.getGnDivisionUKey();

        gnDivisionsByPK.put(bdDivisionUKey, r);

        Map<Integer, String> districtMap = siGNDivisions.get(dsDivisionUKey);
        if (districtMap == null) {
            districtMap = new TreeMap<Integer, String>();
            siGNDivisions.put(dsDivisionUKey, districtMap);
        }
        districtMap.put(bdDivisionUKey, bdDivisionId + ": " + r.getSiGNDivisionName());

        districtMap = enGNDivisions.get(dsDivisionUKey);
        if (districtMap == null) {
            districtMap = new TreeMap<Integer, String>();
            enGNDivisions.put(dsDivisionUKey, districtMap);
        }
        districtMap.put(bdDivisionUKey, bdDivisionId + SPACER + r.getEnGNDivisionName());

        districtMap = taGNDivisions.get(dsDivisionUKey);
        if (districtMap == null) {
            districtMap = new TreeMap<Integer, String>();
            taGNDivisions.put(dsDivisionUKey, districtMap);
        }
        districtMap.put(bdDivisionUKey, bdDivisionId + SPACER + r.getTaGNDivisionName());
    }
}
