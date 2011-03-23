package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.District;

import java.util.List;
import java.util.Map;

/**
 * @author asankha
 */
public interface DSDivisionDAO {

    /**
     * Returns the list of DSDivisions for the given language
     *
     * @param districtId the District ID
     * @param language   the language ID (see AppConstants)
     * @param user       the user requesting the Division name list
     * @return a Map of known D.S. Divisions for the given language along with the ID
     */
    public Map<Integer, String> getDSDivisionNames(int districtId, String language, User user);

    /**
     * Returns All the DS Divisions.
     *
     * @param districtUKey the unique DS Division ID
     * @param language     the selected language
     * @param user         the user requesting the full District name list
     * @return a Map of all D.S. Divisions for the given language along with the ID
     */
    public Map<Integer, String> getAllDSDivisionNames(int districtUKey, String language, User user);

    /**
     * Return the name of the D.S Division in the given language
     *
     * @param dsDivisionUKey the unique DS Division ID
     * @param language       selected language to return the name
     * @return the name of the DS division in the selected language
     */
    public String getNameByPK(int dsDivisionUKey, String language);

    /**
     * Return DSDivision by id
     *
     * @param dsDivisionUKey the unique internal ID (PK)
     * @return the DSDivision
     */
    public DSDivision getDSDivisionByPK(int dsDivisionUKey);

    /**
     * Update a DS Division
     *
     * @param dsDivision division to be updated
     * @param user       user executing the operation
     */
    public void update(DSDivision dsDivision, User user);

    /**
     * Add a new DS Division
     *
     * @param dsDivision division to be marked
     * @param user       user executing the operation
     */
    public void add(DSDivision dsDivision, User user);

    /**
     * Return all DS Divisions
     *
     * @return all DS Divisions
     */
    public List<DSDivision> findAll();

    /**
     * get all DSDivsions for given district
     *
     * @param districtUKey districtUKey
     * @return List of DSDivisons
     */
    public List<DSDivision> getAllDSDivisionByDistrictKey(int districtUKey);

    /**
     * @param dsDivisionId the unique DS Division ID
     * @param district     District of the  DS Division
     * @return the DS division object
     */
    public DSDivision getDSDivisionByCode(int dsDivisionId, District district);
}