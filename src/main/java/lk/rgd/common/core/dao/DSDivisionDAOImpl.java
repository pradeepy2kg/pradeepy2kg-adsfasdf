package lk.rgd.common.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
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

    private final Map<Integer, DSDivision> dsDivisions = new HashMap<Integer, DSDivision>();
    private final Map<Integer, Map<Integer,DSDivision>> dsDivisionsByDistrictAndDiv =
        new HashMap<Integer, Map<Integer,DSDivision>>();
    private final Map<Integer, Map<Integer,String>> siNames = new HashMap<Integer, Map<Integer,String>>();
    private final Map<Integer, Map<Integer,String>> enNames = new HashMap<Integer, Map<Integer,String>>();
    private final Map<Integer, Map<Integer,String>> taNames = new HashMap<Integer, Map<Integer,String>>();

    /**
     * @inheritDoc
     */
    public Map<Integer, String> getDSDivisionNames(int districtId, String language, User user) {

        Map<Integer, String> result = null;
        if (AppConstants.SINHALA.equals(language)) {
            result = getDSDivisionNamesImpl(siNames, districtId);
        } else if (AppConstants.ENGLISH.equals(language)) {
            result = getDSDivisionNamesImpl(enNames, districtId);
        } else if (AppConstants.TAMIL.equals(language)) {
            result = getDSDivisionNamesImpl(taNames, districtId);
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
        }

        if (user == null) {
            return result;
        } else if (user.isPlayingRole(Role.ROLE_ADMIN) || user.isPlayingRole(Role.ROLE_RG)) {
            // Admin, RG and has full access
            return result;
        } else if ((user.isPlayingRole(Role.ROLE_ARG) || user.isPlayingRole(Role.ROLE_DR)) && user.isAllowedAccessToDistrict(districtId)) {
            // the ARG, or DR who has been assigned to this district has full access
            return result;
        } else {
            Map<Integer, String> filteredResult = new HashMap<Integer, String>();

            for (Map.Entry<Integer, String> e : result.entrySet()) {
                if (user.isAllowedAccessToDSDivision(e.getKey())) {
                    filteredResult.put(e.getKey(), e.getValue());
                }
            }
            return filteredResult;
        }
    }

    private Map<Integer, String> getDSDivisionNamesImpl(
        Map<Integer, Map<Integer,String>> namesByDistrict, int districtId) {

        Map<Integer, String> dsDivisionNames = namesByDistrict.get(districtId);
        if (dsDivisionNames != null) {
            return dsDivisionNames;
        }
        logger.warn("No DS Divisions found for District : {}", districtId);
        return Collections.emptyMap();
    }

    public DSDivision getDSDivisionByPK(int dsDivisionUKey) {
        return dsDivisions.get(dsDivisionUKey);
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
            int districtUKey = d.getDistrict().getDistrictUKey();
            int divisionId   = d.getDivisionId();
            int divisionUKey = d.getDsDivisionUKey();

            dsDivisions.put(d.getDsDivisionUKey() , d);

            Map<Integer, DSDivision> divisionMap = dsDivisionsByDistrictAndDiv.get(districtUKey);
            if (divisionMap == null) {
                divisionMap = new HashMap<Integer, DSDivision>();
                dsDivisionsByDistrictAndDiv.put(districtUKey, divisionMap);
            }
            divisionMap.put(divisionUKey, d);

            subMap = siNames.get(districtUKey);
            if (subMap == null) {
                subMap = new HashMap<Integer, String>();
                siNames.put(districtUKey, subMap);
            }
            subMap.put(divisionUKey, divisionId + SPACER + d.getSiDivisionName());

            subMap = enNames.get(districtUKey);
            if (subMap == null) {
                subMap = new HashMap<Integer, String>();
                enNames.put(districtUKey, subMap);
            }
            subMap.put(divisionUKey, divisionId + SPACER + d.getEnDivisionName());

            subMap = taNames.get(districtUKey);
            if (subMap == null) {
                subMap = new HashMap<Integer, String>();
                taNames.put(districtUKey, subMap);
            }
            subMap.put(divisionUKey, divisionId + SPACER + d.getTaDivisionName());
        }

        logger.debug("Loaded : {} DSDivisions from the database", results.size());
    }
}