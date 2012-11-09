package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.*;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.Court;
import lk.rgd.crs.api.domain.GNDivision;
import lk.rgd.crs.api.domain.MRDivision;

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
     * Update BD Division (only BD Division name in sinhala, english, tamil can be updated), if the selected BD Division
     * does not have any mappings by birth, death etc.
     *
     * @param bdDivisionUKey the unique key of BD Division to be updated
     * @param bdDivision     the BD Division to be updated
     * @param user           the user invoking the action
     */
    void updateBDDivision(int bdDivisionUKey, BDDivision bdDivision, User user);

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
     * updated MR Division (only MR Division name in sinhala, english, tamil can be updated), if the selected
     * MR Division does not have any mappings by marriage register
     *
     * @param mrDivisionUKey the unique key of MR Division to be updated
     * @param mrDivision     the MR Division to be updated
     * @param user           the user invoking the action
     */
    void updateMRDivision(int mrDivisionUKey, MRDivision mrDivision, User user);

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
     * Update a DS Division (only DS Division name in sinhala, english, tamil can be updated), if it does not have any
     * mappings by birth, death etc.
     *
     * @param dsDivision the DS Division to be updated
     * @param user       the user invoking the action
     */
    void updateDSDivision(DSDivision dsDivision, User user);

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
     * Update Location (only Location name in sinhala, english, tamil can be updated), if it does not have any mapping
     * db records
     *
     * @param locationUKey the unique key of Location to be updated
     * @param location     the Location to be updated
     * @param user         the user invoking the action
     */
    public void updateLocation(int locationUKey, Location location, User user);

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
     * Update a Court (only Court name in sinhala, english, tamil can be updated), if it does not have any mappings by
     * Adoption etc.
     *
     * @param courtUKey the unique key of Court to be updated
     * @param court     the court to be updated
     * @param user      the user invoking the action
     */
    public void updateCourt(int courtUKey, Court court, User user);

    /**
     * Mark a Court as active
     *
     * @param courtUKey the court to mark as active
     * @param activate  to active inactive court
     * @param user      user invoking the action
     */
    public void activateOrInactivateCourt(int courtUKey, boolean activate, User user);


    /**
     * Add a new GN Division
     *
     * @param gnDivision the GN Division to be added
     * @param user       the user invoking the action
     */
    void addGNDivision(GNDivision gnDivision, User user);

    /**
     * update a GN Division (only GN Division name in sinhala, english, tamil can be updated), if it does not have any
     * mappings
     *
     * @param gnDivisionUKey the unique key of GN Division to be updated
     * @param gnDivision     the GN Division to be updated
     * @param user           the user invoking the action
     */
    void updateGNDivision(int gnDivisionUKey, GNDivision gnDivision, User user);

    void reArrangeGNDivisions(int oldDSDivisionUKey, int newDSDivisionUKey, int[] gnDivisions, User user);

    /**
     * Mark a GN Division as active
     *
     * @param gnDivisionUKey the BD Division to be updated
     * @param active         check active or inactive bdDivision
     * @param user           the user invoking the action
     */
    void activateOrInactiveGNDivision(int gnDivisionUKey, boolean active, User user);

    /**
     * Adding a Zonal Office
     *
     * @param zonalOffice   Zonal Office to be added
     * @param user          User requesting to add the zonal office
     */
    public void addZonalOffice(ZonalOffice zonalOffice, User user);

    /**
     * Updating a Zonal Office
     *
     * @param zonalOffice   Zonal Office to be updated
     * @param user          User requesting to update the zonal office
     */
    public void updateZonalOffice(ZonalOffice zonalOffice, User user);

    /**
     * Activate/ Inactivate Zonal Offices
     *
     * @param zonalOfficeUKey   IdUKey of the zonal office to be activate/ inactivate
     * @param active            Status
     * @param user              User requesting to activate/ inactivate zonal office
     */
    public void activateOrInactivateZonalOffice(int zonalOfficeUKey, boolean active, User user);
}

