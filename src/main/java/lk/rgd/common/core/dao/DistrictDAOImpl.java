package lk.rgd.common.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
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
public class DistrictDAOImpl extends BaseDAO implements DistrictDAO, PreloadableDAO {

    private final Map<Integer, District> districtsByPK = new HashMap<Integer, District>();
    private final Map<Integer, String> siDistricts = new TreeMap<Integer, String>();
    private final Map<Integer, String> enDistricts = new TreeMap<Integer, String>();
    private final Map<Integer, String> taDistricts = new TreeMap<Integer, String>();

    /**
     * @inheritDoc This method is there for getting all the district names for a given district
     * Without restricting on user/role permissions. This is needed to capture mothers district in
     * Birth dicration data entry.
     */
    public Map<Integer, String> getAllDistrictNames(String language, User user) {
        Map<Integer, String> result = getAllDistrictNames(language);
        // todo auditing for user
        return result;
    }

    /**
     * @inheritDoc
     */
    public List<District> getDistrictsByProvince(int provinceUKey, User user) {
        Query q = em.createNamedQuery("getDistrictsByProvince");
        q.setParameter("provinceUKey", provinceUKey);
        return q.getResultList();
    }

    /**
     * @inheritDoc
     */
    public Map<Integer, String> getDistrictNamesByProvince(String language, int provinceUKey, User user) {
        Map<Integer, String> districts = new TreeMap<Integer, String>();
        List<District> districtList = getDistrictsByProvince(provinceUKey, user);
        if (districtList != null && districtList.size() > 0) {
            if (AppConstants.SINHALA.equals(language)) {
                for (District d : districtList) {
                    districts.put(d.getDistrictUKey(), d.getSiDistrictName());
                }
            } else if (AppConstants.TAMIL.equals(language)) {
                for (District d : districtList) {
                    districts.put(d.getDistrictUKey(), d.getTaDistrictName());
                }
            } else if (AppConstants.ENGLISH.equals(language)) {
                for (District d : districtList) {
                    districts.put(d.getDistrictUKey(), d.getEnDistrictName());
                }
            }
            return districts;
        }
        return getAllDistrictNames(language, user);
    }

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
            logger.error("Error getting DistrictNames using null for User");
            throw new IllegalArgumentException("User can not be null");
        } else if (Role.ROLE_RG.equals(user.getRole().getRoleId()) || Role.ROLE_ADMIN.equals(user.getRole().getRoleId())) {
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

    private Map<Integer, String> getAllDistrictNames(String language) {
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
        return result;
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
        District district = districtsByPK.get(id);
        if (district == null) {
            district = em.find(District.class, id);
        }
        return district;
    }

    public District getDistrictByCode(int districtId) {
        Query q = em.createNamedQuery("get.district.by.code");
        q.setParameter("districtId", districtId);
        try {
            return (District) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void update(District district, User user) {
        em.merge(district);
        updateCache(district);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void add(District district, User user) {
        em.persist(district);
        updateCache(district);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<District> findAll() {
        Query q = em.createNamedQuery("findAllDistricts");
        return q.getResultList();
    }

    /**
     * Loads all values from the database table into a cache
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public void preload() {

        Query query = em.createQuery("SELECT d FROM District d WHERE d.active = TRUE");
        List<District> results = query.getResultList();

        for (District d : results) {
            updateCache(d);
        }

        logger.debug("Loaded : {} districts from the database", results.size());
    }

    private void updateCache(District d) {
        final int districtId = d.getDistrictId();
        final int districtUKey = d.getDistrictUKey();
        final boolean active = d.isActive();
        if (districtId != AppConstants.COLOMBO_CONSULAR_ID) {
            if (active) {
                districtsByPK.put(districtUKey, d);
                siDistricts.put(districtUKey, districtId + SPACER + d.getSiDistrictName());
                enDistricts.put(districtUKey, districtId + SPACER + d.getEnDistrictName());
                taDistricts.put(districtUKey, districtId + SPACER + d.getTaDistrictName());
            } else {
                districtsByPK.remove(districtUKey);
                siDistricts.remove(districtUKey);
                enDistricts.remove(districtUKey);
                taDistricts.remove(districtUKey);
            }
        }
    }
}
