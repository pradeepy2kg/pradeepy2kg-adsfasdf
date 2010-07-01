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

/**
 * @author asankha
 */
public class DistrictDAOImpl extends BaseDAO implements DistrictDAO, PreloadableDAO {

    private final Map<Integer, District> districtsByPK = new HashMap<Integer, District>();
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

        if (user == null) {
            return result;
        } else if (Role.ROLE_RG.equals(user.getRole().getRoleId())) {
            // admins and RG has full access
            return result;
        } else {
            Map<Integer, String> filteredResult = new HashMap<Integer, String>();

            for (Map.Entry<Integer, String> e : result.entrySet()) {
                if (user.isAllowedAccessToBDDistrict(e.getKey())) {
                    filteredResult.put(e.getKey(), e.getValue());
                }
            }
            return filteredResult;
        }
    }

    public String getNameByPK(int districtUKey, String language) {
        if (AppConstants.SINHALA.equals(language)) {
            return districtsByPK.get(districtUKey).getSiDistrictName();
        } else if (AppConstants.ENGLISH.equals(language)) {
            return districtsByPK.get(districtUKey).getEnDistrictName();
        } else if (AppConstants.TAMIL.equals(language)) {
            return districtsByPK.get(districtUKey).getTaDistrictName();
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
        }
        return AppConstants.EMPTY_STRING;
    }

    public District getDistrict(int id) {
        return districtsByPK.get(id);
    }

    /**
     * Loads all values from the database table into a cache
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public void preload() {

        Query query = em.createQuery("SELECT d FROM District d");
        List<District> results = query.getResultList();

        for (District d : results) {
            final int districtId = d.
                    getDistrictId();
            final int districtUKey = d.getDistrictUKey();
            districtsByPK.put(districtUKey, d);
            siDistricts.put(districtUKey, districtId + SPACER + d.getSiDistrictName());
            enDistricts.put(districtUKey, districtId + SPACER + d.getEnDistrictName());
            taDistricts.put(districtUKey, districtId + SPACER + d.getTaDistrictName());
        }

        logger.debug("Loaded : {} districts from the database", results.size());
    }
}
