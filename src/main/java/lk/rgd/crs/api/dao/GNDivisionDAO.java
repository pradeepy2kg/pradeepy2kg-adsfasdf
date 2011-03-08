package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.crs.api.domain.GNDivision;
import lk.rgd.crs.api.domain.BDDivision;

import java.util.Map;
import java.util.List;

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
     * @param gnDivisionId the unique GN Division ID
     * @param dsDivision
     * @return the BD division object
     */
    public GNDivision getGNDivisionByCode(int gnDivisionId, DSDivision dsDivision);

    /**
      * Update a GN Division
     *
     * @param gnDivision division to be updated
     * @param user       user executing the operation
     */
    public void update(GNDivision gnDivision, User user);

    /**
     * 
     * @param gnDivision division to be added
     * @param user user executing the operation
     */
    public void add(GNDivision gnDivision,User user);

    public List<GNDivision> getAllGNDivisionByDsDivisionKey(int dsDivisionId);

}


