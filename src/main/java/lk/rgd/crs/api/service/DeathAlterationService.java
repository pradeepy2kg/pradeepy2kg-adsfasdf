package lk.rgd.crs.api.service;

import lk.rgd.crs.api.domain.DeathAlteration;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthAlteration;
import lk.rgd.common.api.domain.User;

import java.util.List;
import java.util.Hashtable;
import java.util.Date;

/**
 * @authar amith jayasekara
 * service interface for death alteration related services
 */
public interface DeathAlterationService {
    /**
     * adding a new death alteration
     *
     * @param da   the death alteration to be add
     * @param user User who has permission to add alteration
     */
    public void addDeathAlteration(DeathAlteration da, User user);

    /**
     * updating a death alteration
     *
     * @param da   death alteration to be updated
     * @param user User who has permission to update a death alteration
     */
    public void updateDeathAlteration(DeathAlteration da, User user);

    /**
     * removing a current death alteration for given death alteration unique key.
     *
     * @param idUKey unique key of death alteration to be deleted.
     * @param user   User who has permission to delete a death alteration.
     */
    public void deleteDeathAlteration(long idUKey, User user);

    /**
     * reject selected death alteration
     *
     * @param idUKey  death alteration unique key
     * @param user    user who has permission to reject a death alteration
     * @param comment cause for rejection
     */
    public void rejectDeathAlteration(long idUKey, User user, String comment);

    /**
     * retrive a DeathAlteration object for a given idUKey
     *
     * @param idUKey Unique key for death alteration.
     * @param user   User who has permission to retrive a death alteration.
     * @return retrive a unique death alteration object for given unique key.
     */
    public DeathAlteration getById(long idUKey, User user);

    /**
     * get list of DeathAlterations based on given death certificate number
     *
     * @param idUKey death certificate number
     * @param user   user who has permission to get alterations
     * @return list of death alterations
     */
    public List<DeathAlteration> getAlterationByDeathCertificateNumber(long idUKey, User user);

    /**
     * @param pageNo
     * @param numRows
     * @param divisionId
     * @return
     */
    public List<DeathAlteration> getAlterationApprovalListByDeathDivision(int pageNo, int numRows, int divisionId);

    /**
     * get death alteration object list by death id(forign key for death alteration)
     *
     * @param deathId
     * @param user
     * @return
     */
    public List<DeathAlteration> getAlterationByDeathId(long deathId, User user);

    /**
     * get list of death alterations by location key
     *
     * @param locationUKey location primary key
     * @return list of death alterations submitted by given user location
     */
    public List<DeathAlteration> getDeathAlterationByUserLocation(int locationUKey);

    /**
     * get death alteration by death persons pin number
     *
     * @param pin  death person pin number
     * @param user user who has permission to performe task
     * @return list of death alteration object most probably only one result give if no pin duplications
     */
    public List<DeathAlteration> getAlterationByDeathPersonPin(String pin, User user);

    /**
     * approve death alteration and set bit set base on state partially or fully
     *
     * @param deathAlterationUKey
     * @param fieldsToBeApproved
     * @param appStatus
     * @param user
     */
    public void approveDeathAlteration(long deathAlterationUKey, Hashtable<Integer, Boolean> fieldsToBeApproved, boolean appStatus, User user);

    /**
     * filling transiant value death person name
     *
     * @param da death alteration
     */
    public void loadValuesToDeathAlterationObject(DeathAlteration da);

}
