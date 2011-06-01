package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.crs.api.domain.MRDivision;

import java.util.List;
import java.util.Map;

/**
 * DAO for Marriage registration divisions
 * <p/>
 * Note: Marriage registration divisions and Birth/Death registration divisions are different
 *
 * @author asankha
 */
public interface MRDivisionDAO {

    /**
     * Returns the list of Marriage Registration Divisions within a selected DS Division for the given language
     *
     * @param dsDivisionUKey the DS Division unique key
     * @param language   the language ID (see AppConstants)
     * @return a Map of the list of known Marriage Registration divisions for the given language - with the ID
     */
    public Map<Integer, String> getMRDivisionNames(int dsDivisionUKey, String language, User user);

    /**
     * Return the name of the Marriage Division in the given language
     * @param mrDivisionUKey the unique Marriage Division ID
     * @param language selected language to return the name
     * @return the name of the Marriage division in the selected language
     */
    public String getNameByPK(int mrDivisionUKey, String language);

    public MRDivision getMRDivisionByPK(int mrDivisionUKey);

    /**
     * Update a Marriage Division
     * @param mrDivision division to be updated
     * @param user user executing the operation
     */
    public void update(MRDivision mrDivision, User user);

    /**
     * Add a new Marriage Division
     * @param mrDivision division to be marked
     * @param user user executing the operation
     */
    public void add(MRDivision mrDivision, User user);

    /**
     * Return all MR Divisions
     * @return all MR Divisions
     */
    public List<MRDivision> findAll();

    public MRDivision getMRDivisionByCode(int mrDivisionId, DSDivision dsDivision);

    /**
     * Return all MRDivisions under the specified DSDivision
     * @param dsDivisionId
     * @return all MRDivisions under the specified DSDivision
     */
    public List<MRDivision> getAllMRDivisionsByDSDivisionKey(int dsDivisionId);
}