package lk.rgd.common.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.District;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import javax.persistence.NoResultException;
import java.util.*;

/**
 * @author asankha
 */
public class DSDivisionDAOImpl extends BaseDAO implements DSDivisionDAO, PreloadableDAO {

    // direct cache of objects by PK - dsDivisionUKey
    private final Map<Integer, DSDivision> dsDivisionsByPK = new HashMap<Integer, DSDivision>();
    // local caches indexed by districtUKey and dsDivisionUKey
    private final Map<Integer, Map<Integer, String>> siNames = new HashMap<Integer, Map<Integer, String>>();
    private final Map<Integer, Map<Integer, String>> enNames = new HashMap<Integer, Map<Integer, String>>();
    private final Map<Integer, Map<Integer, String>> taNames = new HashMap<Integer, Map<Integer, String>>();


    /**
     * @inheritDoc This method is there for getting all the DS division names for a given district
     * Without restricting on user/role permissions. This is needed to capture mothers DS division in
     * Birth dicration data entry.
     */
    public Map<Integer, String> getAllDSDivisionNames(int districtUKey, String language, User user) {
        Map<Integer, String> result = getAllDSDivisionNames(districtUKey, language);
        // todo auditing for user
        return result;
    }


    /**
     * @inheritDoc
     */
    public Map<Integer, String> getDSDivisionNames(int districtUKey, String language, User user) {

        Map<Integer, String> result = getAllDSDivisionNames(districtUKey, language);

        if (user == null) {
            logger.error("Error getting DSDivisionNames using null for User");
            throw new IllegalArgumentException("User can not be null");
        } else if (Role.ROLE_RG.equals(user.getRole().getRoleId())) {
            // Admin, RG and has full access
            return result;
        } else if (
                (Role.ROLE_ARG.equals(user.getRole().getRoleId()) || Role.ROLE_DR.equals(user.getRole().getRoleId()))
                        && user.isAllowedAccessToBDDistrict(districtUKey)) {
            // the ARG, or DR who has been assigned to this district has full access
            return result;
        } else {
            Map<Integer, String> filteredResult = new HashMap<Integer, String>();

            for (Map.Entry<Integer, String> e : result.entrySet()) {
                if (user.isAllowedAccessToBDDSDivision(e.getKey())) {
                    filteredResult.put(e.getKey(), e.getValue());
                }
            }
            return filteredResult;
        }
    }

    private Map<Integer, String> getAllDSDivisionNames(int districtUKey, String language) {
        Map<Integer, String> result = null;
        if (AppConstants.SINHALA.equals(language)) {
            result = getDSDivisionNamesImpl(siNames, districtUKey);
        } else if (AppConstants.ENGLISH.equals(language)) {
            result = getDSDivisionNamesImpl(enNames, districtUKey);
        } else if (AppConstants.TAMIL.equals(language)) {
            result = getDSDivisionNamesImpl(taNames, districtUKey);
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
        }
        return result;
    }

    public String getNameByPK(int dsDivisionUKey, String language) {
        if (AppConstants.SINHALA.equals(language)) {
            return dsDivisionsByPK.get(dsDivisionUKey).getSiDivisionName();
        } else if (AppConstants.ENGLISH.equals(language)) {
            return dsDivisionsByPK.get(dsDivisionUKey).getEnDivisionName();
        } else if (AppConstants.TAMIL.equals(language)) {
            return dsDivisionsByPK.get(dsDivisionUKey).getTaDivisionName();
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
        }
        return AppConstants.EMPTY_STRING;
    }

    private Map<Integer, String> getDSDivisionNamesImpl(
            Map<Integer, Map<Integer, String>> namesByDistrict, int districtUKey) {

        Map<Integer, String> dsDivisionNames = namesByDistrict.get(districtUKey);
        if (dsDivisionNames != null) {
            return dsDivisionNames;
        }
        logger.warn("No DS Divisions found for District : {}", districtUKey);
        return Collections.emptyMap();
    }

    public DSDivision getDSDivisionByPK(int dsDivisionUKey) {
        return dsDivisionsByPK.get(dsDivisionUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void update(DSDivision dsDivision, User user) {
        em.merge(dsDivision);
        updateCache(dsDivision);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void add(DSDivision dsDivision, User user) {
        em.persist(dsDivision);
        updateCache(dsDivision);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER,readOnly = true)
    public List<DSDivision> findAll() {
        Query q = em.createNamedQuery("findAllDSDivisions");
        return q.getResultList();
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
            updateCache(d);
        }

        logger.debug("Loaded : {} DSDivisions from the database", results.size());
    }

    //todo
    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<DSDivision> getAllDSDivisionByDistrictKey(int districtUKey) {
        Query q = em.createNamedQuery("get.all.divisions.by.districtId");
        q.setParameter("districtUKey", districtUKey);
        return q.getResultList();
    }

    @Override
    public DSDivision getDSDivisionByCode(int dsDivisionId, District district) {
        Query q = em.createNamedQuery("get.dsDivision.by.code");
        q.setParameter("dsDivisionId", dsDivisionId);
        q.setParameter("district", district);
        try {
            return (DSDivision) q.getSingleResult();
        }
        catch (NoResultException e) {
            logger.debug("No DS division id duplication of id :{}", dsDivisionId);
            return null;
        }
    }


    private void updateCache(DSDivision d) {
        Map<Integer, String> subMap;
        int districtUKey = d.getDistrict().getDistrictUKey();
        int divisionId = d.getDivisionId();
        int divisionUKey = d.getDsDivisionUKey();

        dsDivisionsByPK.put(d.getDsDivisionUKey(), d);

        subMap = siNames.get(districtUKey);
        if (subMap == null) {
            subMap = new TreeMap<Integer, String>();
            siNames.put(districtUKey, subMap);
        }
        subMap.put(divisionUKey, divisionId + SPACER + d.getSiDivisionName());

        subMap = enNames.get(districtUKey);
        if (subMap == null) {
            subMap = new TreeMap<Integer, String>();
            enNames.put(districtUKey, subMap);
        }
        subMap.put(divisionUKey, divisionId + SPACER + d.getEnDivisionName());

        subMap = taNames.get(districtUKey);
        if (subMap == null) {
            subMap = new TreeMap<Integer, String>();
            taNames.put(districtUKey, subMap);
        }
        subMap.put(divisionUKey, divisionId + SPACER + d.getTaDivisionName());
    }
}