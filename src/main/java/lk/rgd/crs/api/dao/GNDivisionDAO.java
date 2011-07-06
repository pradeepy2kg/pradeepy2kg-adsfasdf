package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.GNDivision;

import java.util.List;
import java.util.Map;

/**
 * DAO for GN Division
 *
 * @author amith jayasekara
 * @author aloka munasinghe
 */
public interface GNDivisionDAO {
    /**
     * get list of GN Divisions for given DSDivision for given language
     *
     * @param dsDivisionUKey
     * @param language
     * @param user
     * @return
     */
    public Map<Integer, String> getGNDivisionNames(int dsDivisionUKey, String language, User user);

    /**
     * get GN Division by primary key
     *
     * @param gnDivisionUKey gn division primary key
     * @return GN division
     */
    public GNDivision getGNDivisionByPK(int gnDivisionUKey);

    /**
     * Return the name of the G.N. Division in the given language
     *
     * @param gnDivisionUKey the unique GN Division ID
     * @param language       selected language to return the name
     * @return the name of the GN division in the selected language
     */
    public String getNameByPK(int gnDivisionUKey, String language);

    /**
     * Return the list of GN Divisions by GN Division code and DS Division unique key and
     *
     * @param gnDivisionId the unique GN Division ID
     * @param dsDivision   the DS Division
     * @return the BD division object
     */
    public List<GNDivision> getGNDivisionByCodeAndDSDivision(int gnDivisionId, DSDivision dsDivision);

    /**
     * Return the list of GN Divisions by DS Division unique key and matching DN Division name in any language
     *
     * @param gnDivision
     * @param dsDivisionUKey the unique DS Division key
     * @param user           the user invoking the action
     * @return the list of matching GN Divisions
     */
    public List<GNDivision> getGNDivisionByAnyNameAndDSDivision(GNDivision gnDivision, int dsDivisionUKey, User user);

    /**
     * Update a GN Division
     *
     * @param gnDivision division to be updated
     * @param user       user executing the operation
     */
    public void update(GNDivision gnDivision, User user);

    /**
     * @param gnDivision division to be added
     * @param user       user executing the operation
     */
    public void add(GNDivision gnDivision, User user);

    public List<GNDivision> getAllGNDivisionByDsDivisionKey(int dsDivisionId);

}


