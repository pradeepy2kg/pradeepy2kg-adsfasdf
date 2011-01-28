package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.DeathAlteration;

import java.util.List;
import java.util.Date;

/**
 * @author amith jayasekara
 */

public interface DeathAlterationDAO {

    /**
     * adding a new death alteration
     *
     * @param da   the death alteration to be added
     * @param user the user who has permission to add death alteration
     */
    public void addDeathAlteration(DeathAlteration da, User user);

    /**
     * Update a given death alteration
     *
     * @param da   the death alteration to be update
     * @param user the user who has permission to add death alteration
     */
    public void updateDeathAlteration(DeathAlteration da, User user);

    /**
     * remove a requested death alteration based on given idUKey
     *
     * @param idUKey the unique ID of the DeathAlteration to remove
     */
    public void deleteDeathAlteration(long idUKey);

    /**
     * returns a Death alteration object for the given idUKey
     *
     * @param idUKey Death Alteration Id for the given
     *               death alteration
     * @return DeathAlteration or null if none exist
     */
    public DeathAlteration getById(long idUKey);

    /**
     * get list of alterations where it matches to death certifiacte number
     *
     * @param idUKey certificate unique key
     * @return list of death alteration objects
     */
    public List<DeathAlteration> getByCertificateNumber(long idUKey);

    /**
     * @param pageNo
     * @param noOfRows
     * @param divisionId
     * @return
     */
    public List<DeathAlteration> getPaginatedAlterationApprovalListByDeathDivision(int pageNo, int noOfRows, int divisionId);

    /**
     * get list of death alterations by death id
     *
     * @param deathId death unique id
     * @return list of death alteration objects
     */
    public List<DeathAlteration> getAlterationByDeathId(long deathId);

    /**
     * get user locations by location id
     *
     * @param locationUKey location unique key
     * @return list of death alteration done by given location
     */
    public List<DeathAlteration> getDeathAlterationByUserLocation(int locationUKey);

    /**
     * get death alteration by death persons pin number
     *
     * @param pin death persons pin number
     * @return list of death alterations most probably one result
     */
    public List<DeathAlteration> getDeathAlterationByDeathPersonPin(String pin);

}
