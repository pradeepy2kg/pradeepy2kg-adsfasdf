package lk.rgd.crs.api.service;

import lk.rgd.crs.api.domain.DeathAlteration;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthAlteration;
import lk.rgd.common.api.domain.User;

import java.util.List;
import java.util.Hashtable;
import java.util.Date;
import java.util.Map;

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
     * retrieve a DeathAlteration object for a given idUKey
     *
     * @param idUKey Unique key for death alteration.
     * @param user   User who has permission to retrieve a death alteration.
     * @return retrieve a unique death alteration object for given unique key.
     */
    public DeathAlteration getByIDUKey(long idUKey, User user);

    /**
     * get death alteration object list by death id(foreign key for death alteration)
     *
     * @param deathId death register idUKey
     * @param user    ser who has permission to retrieve a death alteration.
     * @return list of death alterations
     */
    public List<DeathAlteration> getAlterationByDeathId(long deathId, User user);

    /**
     * get list of DeathAlterations based on given death certificate number
     *
     * @param idUKey death certificate number
     * @param user   user who has permission to get alterations
     * @return list of death alterations
     */
    public List<DeathAlteration> getAlterationByDeathCertificateNumber(long idUKey, User user);

    /**
     * get death alteration approval list by original death certificate registered death division
     *
     * @param pageNo     current page
     * @param numRows    number of results rows
     * @param divisionId death division id
     * @return list of paginated death alteration objects
     */
    public List<DeathAlteration> getAlterationApprovalListByDeathDivision(int pageNo, int numRows, int divisionId, User user);

    /**
     * get death alteration approval list by DSDivision
     *
     * @param pageNo       current page
     * @param numRows      number of rows
     * @param dsDivisionId dsDivision idUKey
     * @param user         user who performs the action
     * @return list of death alterations
     */
    public List<DeathAlteration> getAlterationApprovalListByDeathDSDivision(int pageNo, int numRows, int dsDivisionId, User user);

    /**
     * get list of death alterations by location key
     *
     * @param locationUKey location primary key
     * @param user
     * @return list of death alterations submitted by given user location
     */
    public List<DeathAlteration> getDeathAlterationByUserLocation(int locationUKey, User user);

    /**
     * get death alteration by death persons pin number
     *
     * @param pin  death person pin number
     * @param user user who has permission to retrieve a death alteration.
     * @return list of death alteration object most probably only one result give if no pin duplications
     */
    public List<DeathAlteration> getAlterationByDeathPersonPin(String pin, User user);

    /**
     * Approve requested fields of death alteration statement 52_1 or 53 by an ARG or higher authority
     *
     * @param da                 the death alteration to be approved
     * @param fieldsToBeApproved the list of field indexes to be approved
     * @param user               the user initiating the action
     */
    public void approveDeathAlteration(DeathAlteration da, Map<Integer, Boolean> fieldsToBeApproved, User user);

}
