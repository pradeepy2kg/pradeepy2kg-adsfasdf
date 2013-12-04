package lk.rgd.common.core.dao;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.dao.HospitalDAO;
import lk.rgd.common.api.domain.Hospital;
import lk.rgd.common.api.domain.User;

import javax.persistence.Query;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: widu
 * Date: Nov 22, 2013
 * Time: 3:01:54 PM
 * To change this template use File | Settings | File Templates.
 */

public class HospitalDAOImpl extends BaseDAO implements HospitalDAO {


    private final Map<Integer, String> hospitalNameSi = new HashMap<Integer, String>();
    private final Map<Integer, String> hospitalNameEn = new HashMap<Integer, String>();
    private final Map<Integer, String> hospitalNameTa = new HashMap<Integer, String>();
    /*   @Override
        public Map<Integer, String> getHospitalsNamesbyBdDivision(int bdDivisionId, String language) {
            Map<Integer, String> hospitals = new TreeMap<Integer, String>();
            List<Hospital> hospitalList = getHospitalsByBdDivision(bdDivisionId, language);
            if (hospitalList != null && hospitalList.size() > 0) {
                 if (AppConstants.SINHALA.equals(language)) {
                    for (Hospital h : hospitalList) {
                        hospitals.put(h.getHospitalUKey(), h.getHospitalNameSi());
                    }
                } else if (AppConstants.TAMIL.equals(language)) {
                    for (Hospital h : hospitalList) {
                        hospitals.put(h.getHospitalUKey(), h.getHospitalNameTa());
                    }
                } else if (AppConstants.ENGLISH.equals(language)) {
                    for (Hospital h : hospitalList) {
                        hospitals.put(h.getHospitalUKey(), h.getHospitalNameEn());
                    }
                }
                return hospitals;
            }
            return getAllHospitalNames(language);
        }
    */

    @Override
    public Map<Integer, String> getHospitalsbyDSDivision(String language, int dsDivisionId, User user) {
        Map<Integer, String> hospitals = new TreeMap<Integer, String>();
        List<Hospital> hospitalList = getHospitalsListByBdsDivision(dsDivisionId, language);
        final String roleId = user.getRole().getRoleId();
        if (hospitalList != null && hospitalList.size() > 0) {
            if (AppConstants.SINHALA.equals(language)) {
                for (Hospital h : hospitalList) {
                    hospitals.put(h.getHospitalUKey(), h.getHospitalNameSi());
                }
            } else if (AppConstants.TAMIL.equals(language)) {
                for (Hospital h : hospitalList) {
                    hospitals.put(h.getHospitalUKey(), h.getHospitalNameTa());
                }
            } else if (AppConstants.ENGLISH.equals(language)) {
                for (Hospital h : hospitalList) {
                    hospitals.put(h.getHospitalUKey(), h.getHospitalNameEn());
                }
            }
            return hospitals;

           /* if (user == null) {
                logger.error("Error getting DSDivisionNames using null for User");
                throw new IllegalArgumentException("User can not be null");
            } else if (Role.ROLE_RG.equals(roleId) || Role.ROLE_ADMIN.equals(roleId)) {
                // Admin, RG and has full access
                return hospitals;
            } else if (
                    (Role.ROLE_ARG.equals(roleId) || Role.ROLE_DR.equals(roleId))
                    ) {
                // the ARG, or DR who has been assigned to this district has full access
                return hospitals;
            } else {
                Map<Integer, String> filteredResult = new HashMap<Integer, String>();

                for (Map.Entry<Integer, String> e : hospitals.entrySet()) {
                    if (user.isAllowedAccessToBDDSDivision(e.getKey())) {
                        filteredResult.put(e.getKey(), e.getValue());
                    }
                }
                return filteredResult;
            }*/
        }
        return Collections.EMPTY_MAP;
    }

    @Override
    public Map<Integer, String> getHospitalsbyDistrict(String language, int districtId, User user) {
        Map<Integer, String> hospitals = new TreeMap<Integer, String>();
        List<Hospital> hospitalList = getHospitalListByDistrict(districtId, language);
        if (hospitalList != null && hospitalList.size() > 0) {
            if (AppConstants.SINHALA.equals(language)) {
                for (Hospital h : hospitalList) {
                    hospitals.put(h.getHospitalUKey(), h.getHospitalNameSi());
                }
            } else if (AppConstants.TAMIL.equals(language)) {
                for (Hospital h : hospitalList) {
                    hospitals.put(h.getHospitalUKey(), h.getHospitalNameTa());
                }
            } else if (AppConstants.ENGLISH.equals(language)) {
                for (Hospital h : hospitalList) {
                    hospitals.put(h.getHospitalUKey(), h.getHospitalNameEn());
                }
            }
            return hospitals;
        }
        return Collections.EMPTY_MAP;
    }

    private List<Hospital> getHospitalListByDistrict(int districtId, String language) {
        Query q = em.createNamedQuery("getHospitalsbyDistrictId");
        q.setParameter("districtId", districtId);
        List<Hospital> hos = (List<Hospital>) q.getResultList();
        return hos;
    }


    private List<Hospital> getHospitalsListByBdsDivision(int dsDivisionId, String language) {
        Query q = em.createNamedQuery("getHospitalsbyDsDivisionId");
        q.setParameter("dsDivisionId", dsDivisionId);
        List<Hospital> hos = (List<Hospital>) q.getResultList();
        return hos;

    }

    /*private Map<Integer, String> getAllHospitalNames(String language) {

           Map<Integer, String> result = getAllHospitalName(language);
        // todo auditing for user
        return result;
    }*/

    private Map<Integer, String> getAllHospitalName(String language) {
        Map<Integer, String> result = null;
        if (AppConstants.SINHALA.equals(language)) {
            result = hospitalNameSi;
        } else if (AppConstants.ENGLISH.equals(language)) {
            result = hospitalNameEn;
        } else if (AppConstants.TAMIL.equals(language)) {
            result = hospitalNameTa;
        } else {
            handleException("Unsupported language : " + language, ErrorCodes.INVALID_LANGUAGE);
        }
        return result;
    }

    /* List<Hospital> getHospitalsByBdDivision (int bdDivisionId, String language){
        Query q = em.createNamedQuery("getHospitalsbyBdDivisionId");
        q.setParameter("bdDivisionId", bdDivisionId);

        List<Hospital> hos = (List<Hospital>) q.getResultList();
        return hos ;
          
    }*/


}
