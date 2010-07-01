package lk.rgd.common.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.common.api.dao.GNDivisionDAO;
import lk.rgd.common.api.domain.GNDivision;
import lk.rgd.ErrorCodes;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author asankha
 */
public class GNDivisionDAOImpl extends BaseDAO implements GNDivisionDAO, PreloadableDAO {

    /** A cache of all GNDivisions by PK */
    private final Map<Integer, GNDivision> gnDivisions = new HashMap<Integer, GNDivision>();
    //-- A cache of G.N. Division names in the supported languages --
    private final Map<Integer, Map<Integer, Map<Integer, String>>> siNames =
        new HashMap<Integer, Map<Integer, Map<Integer, String>>>();
    private final Map<Integer, Map<Integer, Map<Integer, String>>> enNames =
        new HashMap<Integer, Map<Integer, Map<Integer, String>>>();
    private final Map<Integer, Map<Integer, Map<Integer, String>>> taNames =
        new HashMap<Integer, Map<Integer, Map<Integer, String>>>();

    /**
     * @inheritDoc
     */
    public Map<Integer, String> getGNDivisionNames(int districtId, int dsDivisionId, String language) {
        if (AppConstants.SINHALA.equals(language)) {
            return getGNDivisionNamesImpl(siNames, districtId, dsDivisionId);
        } else if (AppConstants.ENGLISH.equals(language)) {
            return getGNDivisionNamesImpl(enNames, districtId, dsDivisionId);
        } else if (AppConstants.TAMIL.equals(language)) {
            return getGNDivisionNamesImpl(taNames, districtId, dsDivisionId);
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
            return null;
        }
    }

    private Map<Integer, String> getGNDivisionNamesImpl(
        Map<Integer, Map<Integer, Map<Integer, String>>> namesByDistrict, int districtId, int dsDivisionId) {

        Map<Integer, Map<Integer, String>> namesByDSDivision = namesByDistrict.get(districtId);
        if (namesByDSDivision != null) {
            Map<Integer, String> gnDivisionNames = namesByDSDivision.get(dsDivisionId);
            if (gnDivisionNames != null) {
                return gnDivisionNames;
            }
        }
        logger.warn("No GN Divisions found for District : {} and DS Division : {}", districtId, dsDivisionId);
        return Collections.emptyMap();
    }

    public GNDivision getGNDivisionByPK(int gnDivisionUKey) {
        return gnDivisions.get(gnDivisionUKey);
    }

    /**
     * Loads all values from the database table into a cache
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public void preload() {

        Query query = em.createQuery("SELECT g FROM GNDivision g");
        List<GNDivision> results = query.getResultList();

        Map<Integer, Map<Integer, String>> districtSubMap = null;
        Map<Integer, String> dsDivisionsubMap = null;

        for (GNDivision g : results) {
            int districtId = g.getDistrictId();
            int dsDivisionId = g.getDsDivisionId();
            int gsDivisionId = g.getDivisionId();

            gnDivisions.put(g.getGnDivisionUKey(), g);
            processForLanguage(districtId, dsDivisionId, gsDivisionId, siNames, g.getSiDivisionName());
            processForLanguage(districtId, dsDivisionId, gsDivisionId, enNames, g.getEnDivisionName());
            processForLanguage(districtId, dsDivisionId, gsDivisionId, taNames, g.getTaDivisionName());
        }

        logger.debug("Loaded : {} GNDivisions from the database", results.size());
    }

    private void processForLanguage(int districtId, int dsDivisionId, int gnDivisionId,
        final Map<Integer, Map<Integer, Map<Integer, String>>> namesMap, String nameInLanguage) {

        Map<Integer, Map<Integer, String>> districtSubMap;
        Map<Integer, String> gnDivisionSubMap;

        districtSubMap = namesMap.get(districtId);
        if (districtSubMap == null) {
            districtSubMap = new HashMap<Integer, Map<Integer, String>>();
            namesMap.put(districtId, districtSubMap);
        }
        gnDivisionSubMap = districtSubMap.get(dsDivisionId);
        if (gnDivisionSubMap == null) {
            gnDivisionSubMap = new HashMap<Integer, String>();
            districtSubMap.put(dsDivisionId, gnDivisionSubMap);
        }
        gnDivisionSubMap.put(gnDivisionId, nameInLanguage);
    }
}