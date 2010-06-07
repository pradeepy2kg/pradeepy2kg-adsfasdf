package lk.rgd.common.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.ErrorCodes;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author asankha
 */
public class DistrictDAOImpl extends BaseDAO implements DistrictDAO, PreloadableDAO {

    private final Map<Integer, District> districts = new HashMap<Integer, District>();
    private final Map<Integer, String> siDistricts = new HashMap<Integer, String>();
    private final Map<Integer, String> enDistricts = new HashMap<Integer, String>();
    private final Map<Integer, String> taDistricts = new HashMap<Integer, String>();

    /**
     * @inheritDoc
     */
    public Map<Integer, String> getDistrictNames(String language, User user) {

        Map<Integer, String> result = null;
        if (AppConstants.SINHALA.equals(language)) {
            result = siDistricts;
        } else if (AppConstants.ENGLISH.equals(language)) {
            result = enDistricts;
        } else if (AppConstants.TAMIL.equals(language)) {
            result = taDistricts;
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
        }

        if (user.isPlayingRole(Role.ROLE_ADMIN) || user.isPlayingRole(Role.ROLE_RG)) {
            // admins and RG has full access
            return result;
        } else {
            Map<Integer, String> filteredResult = new HashMap<Integer, String>();

            for (Map.Entry<Integer, String> e : result.entrySet()) {
                if (user.isAllowedAccessToDistrict(e.getKey())) {
                    filteredResult.put(e.getKey(), e.getValue());
                }
            }
            return filteredResult;
        }
    }

    public District getDistrict(int id) {
        return districts.get(id);   
    }

    /**
     * Loads all values from the database table into a cache
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public void preload() {

        Query query = em.createQuery("SELECT d FROM District d");
        List<District> results = query.getResultList();

        for (District d : results) {
            final int districtId = d.getDistrictId();
            districts.put(districtId, d);
            siDistricts.put(districtId, districtId + SPACER + d.getSiDistrictName());
            enDistricts.put(districtId, districtId + SPACER + d.getEnDistrictName());
            taDistricts.put(districtId, districtId + SPACER + d.getTaDistrictName());
        }

        logger.debug("Loaded : {} districts from the database", results.size());
    }
}
