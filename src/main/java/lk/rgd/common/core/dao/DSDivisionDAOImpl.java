package lk.rgd.common.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.crs.ErrorCodes;
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
public class DSDivisionDAOImpl extends BaseDAO implements DSDivisionDAO, PreloadableDAO {

    private final Map<DSDivision.DSDivisionPK, DSDivision> dsDivisions = new HashMap<DSDivision.DSDivisionPK, DSDivision>();
    private final Map<Integer, Map<Integer,String>> siNames = new HashMap<Integer, Map<Integer,String>>();
    private final Map<Integer, Map<Integer,String>> enNames = new HashMap<Integer, Map<Integer,String>>();
    private final Map<Integer, Map<Integer,String>> taNames = new HashMap<Integer, Map<Integer,String>>();

    /**
     * @inheritDoc
     */
    public Map<Integer, String> getDSDivisionNames(int districtId, String language) {
        if (AppConstants.SINHALA.equals(language)) {
            return getDSDivisionNamesImpl(siNames, districtId);
        } else if (AppConstants.ENGLISH.equals(language)) {
            return getDSDivisionNamesImpl(enNames, districtId);
        } else if (AppConstants.TAMIL.equals(language)) {
            return getDSDivisionNamesImpl(taNames, districtId);
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
            return null;
        }
    }

    private Map<Integer, String> getDSDivisionNamesImpl(
        Map<Integer, Map<Integer,String>> namesByDistrict, int districtId) {

        Map<Integer, String> dsDivisionNames = namesByDistrict.get(districtId);
        if (dsDivisionNames != null) {
            return dsDivisionNames;
        }
        logger.warn("No GN Divisions found for District : {}", districtId);
        return Collections.emptyMap();
    }

    public DSDivision getDSDivision(int districtId, int divisionId) {
        return dsDivisions.get(new DSDivision.DSDivisionPK(districtId, divisionId));
    }

    /**
     * Loads all values from the database table into a cache
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public void preload() {

        Query query = em.createQuery("SELECT d FROM DSDivision d");
        List<DSDivision> results = query.getResultList();

        Map<Integer, String> subMap = null;

        for (DSDivision d : results) {
            int districtId = d.getDistrictId();
            int divisionId = d.getDivisionId();

            dsDivisions.put(new DSDivision.DSDivisionPK(districtId, divisionId) , d);

            subMap = siNames.get(districtId);
            if (subMap == null) {
                subMap = new HashMap<Integer, String>();
                siNames.put(districtId, subMap);
            }
            subMap.put(divisionId, d.getSiDivisionName());

            subMap = enNames.get(districtId);
            if (subMap == null) {
                subMap = new HashMap<Integer, String>();
                enNames.put(districtId, subMap);
            }
            subMap.put(divisionId, d.getEnDivisionName());

            subMap = taNames.get(districtId);
            if (subMap == null) {
                subMap = new HashMap<Integer, String>();
                taNames.put(districtId, subMap);
            }
            subMap.put(divisionId, d.getTaDivisionName());
        }

        logger.debug("Loaded : {} DSDivisions from the database", results.size());
    }
}