package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.District;

import java.util.List;
import java.util.Map;

/**
 * @author asankha
 */
public interface DistrictDAO {

    /**
     * Returns the list of Districts for the given language
     *
     * @param language the language ID (see AppConstants)
     * @return a Map of known districts for the given language along with the ID
     */
    public Map<Integer, String> getDistrictNames(String language, User user);
    /**
     * Returns All Districts
     *
     * @param language the selected language
     * @param user     the user requesting the full District name list
     * @return a Map of all Districts for the given language along with the ID
     */
    public Map<Integer, String> getAllDistrictNames(String language, User user);
    /**
     * Return the name of the district in the selected language
     *
     * @param districtUKey the district unique key
     * @param language     the selected language
     * @return the name of the district in the selected language
     */
    public String getNameByPK(int districtUKey, String language);
    /**
     * Return District by id
     *
     * @param id the District ID
     * @return the District
     */
    public District getDistrict(int id);

    /**
     * Return District by id
     *
     * @param districtId the District code
     * @return the District
     */
    public District getDistrictByCode(int districtId);

    /**
     * Update a district - ie only activate or inactivate
     *
     * @param district the district updated
     * @param user     user performing the action
     */
    public void update(District district, User user);

    /**
     * Add a district
     *
     * @param district the district added
     * @param user     user performing the action
     */
    public void add(District district, User user);

    /**
     * Return all Districts in the system
     *
     * @return all Districts
     */
    public List<District> findAll();
}
