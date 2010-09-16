package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.MRDivision;
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
     * @param bdDivision the BD Division to be added
     * @param user the user invoking the action
     */
    void addBDDivision(BDDivision bdDivision, User user);

    /**
     * Mark a BD Division as inactive
     * @param bdDivisionUKey the BD Division to be updated
     * @param user the user invoking the action
     */
    void inactivateBDDivision(int bdDivisionUKey, User user);

    /**
     * Mark a BD Division as active
     * @param bdDivisionUKey the BD Division to be updated
     * @param user the user invoking the action
     */
    void activateBDDivision(int bdDivisionUKey, User user);

    /**
     * Add a new MR Division
     * @param mrDivision the MR Division to be added
     * @param user the user invoking the action
     */
    void addMRDivision(MRDivision mrDivision, User user);

    /**
     * Mark a MR Division as inactive
     * @param mrDivisionUKey the MR Division to be updated
     * @param user the user invoking the action
     */
    void inactivateMRDivision(int mrDivisionUKey, User user);

    /**
     * Mark a MR Division as active
     * @param mrDivisionUKey the MR Division to be updated
     * @param user the user invoking the action
     */
    void activateMRDivision(int mrDivisionUKey, User user);

    /**
     * Add a new DS Division
     * @param dsDivision the DS Division to be added
     * @param user the user invoking the action
     */
    void addDSDivision(DSDivision dsDivision, User user);

    /**
     * Mark a DS Division as inactive
     * @param dsDivisionUKey the DS Division to be updated
     * @param user the user invoking the action
     */
    void inactivateDSDivision(int dsDivisionUKey, User user);

    /**
     * Mark a DS Division as active
     * @param dsDivisionUKey the DS Division to be updated
     * @param user the user invoking the action
     */
    void activateDSDivision(int dsDivisionUKey, User user);

    /**
     * Add a new District
     * @param district the District to be added
     * @param user the user invoking the action
     */
    void addDistrict(District district, User user);

    /**
     * Mark a District as inactive
     * @param districtUKey the District to be updated
     * @param user the user invoking the action
     */
    void inactivateDistrict(int districtUKey, User user);

    /**
     * Mark a District as active
     * @param districtUKey the District to be updated
     * @param user the user invoking the action
     */
    void activateDistrict(int districtUKey, User user);

    /**
     * Add a new Location as active
     * @param location the new location, the name of the location in all three languages must be filled
     * @param user user invoking the action
     */
    public void addLocation(Location location, User user);

    /**
     * Mark a Location as active
     * @param locationUKey the location to mark as active
     * @param user user invoking the action
     */
    public void activateLocation(int locationUKey, User user);

    /**
     * Mark a Location as inactive
     * @param locationUKey the location to mark as inactive
     * @param user user invoking the action
     */
    public void inactivateLocation(int locationUKey, User user);    
}
