package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.User;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: widu
 * Date: Nov 22, 2013
 * Time: 2:57:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface HospitalDAO {

  /*  *//**
     * Returns the list of hospital according to dsDivision
     *
     * @param bdDivisionId the BDDivisionId
     * @param language     the language ID (see AppConstants)
     * @return a list of Hospital that belongs to given BDDivision
     *//*
    public Map<Integer, String> getHospitalsNamesbyBdDivision(int bdDivisionId, String language);*/

    /**
     * Returns the list of hospital according to dsDivision
     *
     * @param dsDivisionId the DSDivisionId
     * @param language     the language ID (see AppConstants)
     * @param user         the user requesting the Hospital name list
     * @return a list of Hospital that belongs to given DSDivision
     */
    public Map<Integer, String> getHospitalsbyDSDivision(String language, int dsDivisionId, User user);

    /**
     * Returns the list of hospital according to dsDivision
     *
     * @param districtId the DistrictId
     * @param language     the language ID (see AppConstants)
     * @param user         the user requesting the Hospital name list
     * @return a list of Hospital that belongs to given District
     */
    public Map<Integer, String> getHospitalsbyDistrict(String language, int districtId, User user);
}
