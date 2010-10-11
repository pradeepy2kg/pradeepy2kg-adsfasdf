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
     * todo
     *
     * @param deathId
     * @param user
     * @return
     */
    public List<DeathAlteration> getAlterationByDeathId(long deathId, User user);

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
     * get list of death alterations based on given period (alteration registered date)
     *
     * @param startDate
     * @param endDate
     * @param user
     * @return
     */
    public List<DeathAlteration> getDeathAlterationByTimePeriod(Date startDate, Date endDate, User user);
}
