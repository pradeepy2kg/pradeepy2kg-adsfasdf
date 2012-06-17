package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.BDDivision;

import java.util.List;
import java.util.Map;

/**
 * DAO for Birth and Death registration divisions
 * <p/>
 * Note: Marriage registration divisions and Birth/Death registration divisions are different
 *
 * @author asankha
 */
public interface BDDivisionDAO {

    /**
     * Returns the list of Birth & Death Registration Divisions within a selected DS Division for the given language
     *
     * @param dsDivisionUKey the DS Division unique key
     * @param language       the language ID (see AppConstants)
     * @return a Map of the list of known Birth and Death Registration divisions for the given language - with the ID
     */
    public Map<Integer, String> getBDDivisionNames(int dsDivisionUKey, String language, User user);

    /**
     * Return the name of the B.D. Division in the given language
     *
     * @param bdDivisionUKey the unique BD Division ID
     * @param language       selected language to return the name
     * @return the name of the BD division in the selected language
     */
    public String getNameByPK(int bdDivisionUKey, String language);

    public BDDivision getBDDivisionByPK(int bdDivisionUKey);

    /**
     * Return the list of Birth & Death Registration Divisions within a selected DS Division by division id/code
     *
     * @param bdDivisionId the unique BD Division ID
     * @param dsDivision   DS Division of the  BD Division
     * @return the list of BD divisions
     */
    public List<BDDivision> getBDDivisionByCode(int bdDivisionId, DSDivision dsDivision);

    /**
     * Return the list of Birth & Death Registration Divisions within a selected DS Division by any division name
     *
     * @param bdDivision     the division
     * @param dsDivisionUKey the unique DS Division key
     * @return the list of BD Divisions
     */
    public List<BDDivision> getBDDivisionByAnyNameAndDSDivisionKey(BDDivision bdDivision, int dsDivisionUKey);

    /**
     * Update a BD Division
     *
     * @param bdDivision division to be updated
     * @param user       user executing the operation
     */
    public void update(BDDivision bdDivision, User user);

    /**
     * Add a new BD Division
     *
     * @param bdDivision division to be marked
     * @param user       user executing the operation
     */
    public void add(BDDivision bdDivision, User user);

    /**
     * Return all BD Divisions
     *
     * @return all BD Divisions
     */
    public List<BDDivision> findAll();

    /**
     * @return BD Divisions
     */
    public List<BDDivision> getAllBDDivisionByDsDivisionKey(int dsDivisionId);

}