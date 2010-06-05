package lk.rgd.crs.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.common.core.dao.BaseDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.dao.PreloadableDAO;
import lk.rgd.crs.ErrorCodes;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.BDDivision;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author asankha
 */
public class BDDivisionDAOImpl extends BaseDAO implements BDDivisionDAO, PreloadableDAO {

    private final Map<Integer, Map<Integer, BDDivision>> divisions = new HashMap<Integer, Map<Integer, BDDivision>>();
    private final Map<Integer, Map<Integer, String>> siDivisions = new HashMap<Integer, Map<Integer, String>>();
    private final Map<Integer, Map<Integer, String>> enDivisions = new HashMap<Integer, Map<Integer, String>>();
    private final Map<Integer, Map<Integer, String>> taDivisions = new HashMap<Integer, Map<Integer, String>>();

    /**
     * @inheritDoc
     */
    public Map<Integer, String> getBDDivisionNames(int districtId, String language, User user) {

        Map<Integer, String> result = null;
        if (AppConstants.SINHALA.equals(language)) {
            result = siDivisions.get(districtId);
        } else if (AppConstants.ENGLISH.equals(language)) {
            result = enDivisions.get(districtId);
        } else if (AppConstants.TAMIL.equals(language)) {
            result = taDivisions.get(districtId);
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
        }
        if (result == null) {
            handleException("Invalid district id : " + language, ErrorCodes.INVALID_DISTRICT);
        }
        return result;
    }

    public BDDivision getBDDivision(int districtId, int bdDivisionId) {
        Map<Integer, BDDivision> divMap = divisions.get(districtId);
        if (divMap != null) {
            return divMap.get(bdDivisionId);
        }
        return null;
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public BDDivision getBDDivisionByPK(int bdDivision) {
        return em.find(BDDivision.class, bdDivision);
    }

    /**
     * Loads all values from the database table into a cache
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public void preload() {

        Query query = em.createQuery("SELECT d FROM BDDivision d");
        List<BDDivision> results = query.getResultList();

        for (BDDivision r : results) {
            final int districtId = r.getDistrict().getDistrictId();
            final int divisionId = r.getDivisionId();

            Map<Integer, BDDivision> divMap = divisions.get(districtId);
            if (divMap == null) {
                divMap = new HashMap<Integer, BDDivision>();
                divisions.put(districtId, divMap);
            }
            divMap.put(r.getDivisionId(), r);

            Map<Integer, String> districtMap = siDivisions.get(districtId);
            if (districtMap == null) {
                districtMap = new HashMap<Integer, String>();
                siDivisions.put(districtId, districtMap);
            }
            districtMap.put(divisionId, divisionId + ": " + r.getSiDivisionName());

            districtMap = enDivisions.get(districtId);
            if (districtMap == null) {
                districtMap = new HashMap<Integer, String>();
                enDivisions.put(districtId, districtMap);
            }
            districtMap.put(divisionId, divisionId + SPACER + r.getEnDivisionName());

            districtMap = taDivisions.get(districtId);
            if (districtMap == null) {
                districtMap = new HashMap<Integer, String>();
                taDivisions.put(districtId, districtMap);
            }
            districtMap.put(divisionId, divisionId + SPACER + r.getTaDivisionName());
        }

        logger.debug("Loaded : {} birth and death registration divisions from the database", results.size());
    }
}