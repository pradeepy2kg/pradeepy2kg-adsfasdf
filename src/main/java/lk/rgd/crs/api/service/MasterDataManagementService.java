package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.Court;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Perform the management of Master Data
 *
 * @author asankha
 */
public interface MasterDataManagementService {

    /**
     * Add a new BD Division
     *
     * @param bdDivision the BD Division to be added
     * @param user       the user invoking the action
     */
    void addBDDivision(BDDivision bdDivision, User user);

    /**
     * Mark a BD Division as active
     *
     * @param bdDivisionUKey the BD Division to be updated
     * @param active         check active or inactive bdDivision
     * @param user           the user invoking the action
     */
    void activateOrInactiveBDDivision(int bdDivisionUKey, boolean active, User user);

    /**
     * Add a new MR Division
     *
     * @param mrDivision the MR Division to be added
     * @param user       the user invoking the action
     */
    void addMRDivision(MRDivision mrDivision, User user);

    /**
     * Mark a MR Division as inactive
     *
     * @param mrDivisionUKey the MR Division to be updated
     * @param activate       to active inactive mrDivision
     * @param user           the user invoking the action
     */
    void activateOrInactivateMRDivision(int mrDivisionUKey, boolean activate, User user);
    /**
     * Add a new DS Division
     *
     * @param dsDivision the DS Division to be added
     * @param user       the user invoking the action
     */
    void addDSDivision(DSDivision dsDivision, User user);

    /**
     * Mark a DS Division as inactive
     *
     * @param dsDivisionUKey the DS Division to be updated
     * @param active         active or inactive ds division
     * @param user           the user invoking the action
     */
    void activateOrInactivateDSDivision(int dsDivisionUKey, boolean active, User user);
    /**
     * Add a new District
     *
     * @param district the District to be added
     * @param user     the user invoking the action
     */
    void addDistrict(District district, User user);

    /**
     * Mark a District as inactive
     *
     * @param districtUKey the District to be updated
     * @param user         the user invoking the action
     * @param active       check that active or inactive district
     */
    void activateOrInactivateDistrict(int districtUKey, boolean active, User user);
    /**
     * Add a new Location as active
     *
     * @param location the new location, the name of the location in all three languages must be filled
     * @param user     user invoking the action
     */
    public void addLocation(Location location, User user);

    /**
     * Mark a Location as active
     *
     * @param locationUKey the location to mark as active
     * @param activate     to active or inactive location
     * @param user         user invoking the action
     */
    public void activateOrInactivateLocation(int locationUKey, boolean activate, User user);
    /**
     * Add a new Court as active
     *
     * @param court the new court, the name of the court in all three languages must be filled
     * @param user  user invoking the action
     */
    public void addCourt(Court court, User user);

    /**
     * Mark a Court as active
     *
     * @param courtUKey the court to mark as active
     * @param activate  to active inactive court
     * @param user      user invoking the action
     */
    public void activateOrInactivateCourt(int courtUKey, boolean activate, User user);
}

