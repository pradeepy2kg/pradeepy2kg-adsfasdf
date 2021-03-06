package lk.rgd.common.api.dao;

import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.ZonalOffice;
import lk.rgd.crs.api.domain.Court;

import java.util.List;
import java.util.Map;

/**
 * @author Duminda Dharmakeerthi
 */
public interface ZonalOfficesDAO {
    /**
     * Add a Zonal Office
     *
     * @param zonalOffice   Zonal Office to be added.
     * @param admin         User requesting to add the Zonal Office.
     */
    public void addZonalOffice(ZonalOffice zonalOffice, User admin);

    /**
     * Updating a Zonal Office
     *
     * @param zonalOffice   Zonal Office to be updated.
     * @param admin         User requesting to update the Zonal Office.
     */
    public void updateZonalOffice(ZonalOffice zonalOffice, User admin);

    /**
     * Get a Zonal Office by PK
     *
     * @param zonalOfficeUKey   Primary Key of the Zonal Office.
     * @return                  Zonal Office with the given Primary Key.
     */
    public ZonalOffice getZonalOffice(int zonalOfficeUKey);

    /**
     * Returns the Name of the Zonal Office in the given Language.
     *
     * @param zonalOfficeUKey   Primary Key of the Zonal Office.
     * @param language          Selected Language.
     * @return                  Name of the Zonal Office in the given Language.
     */
    public String getZonalOfficeNameByPK(int zonalOfficeUKey, String language);

    /**
     * Returns the Address of the Zonal Office in the given Language.
     *
     * @param zonalOfficeUKey   Primary Key of the Zonal Office.
     * @param language          Selected Language.
     * @return                  Address of the Zonal Office in the given Language.
     */
    public String getZonalOfficeMailAddressByPK(int zonalOfficeUKey, String language);

    /**
     * Returns all the Active Zonal Offices
     *
     * @return List of active Zonal Offices.
     */
    public List<ZonalOffice> getAllActiveZonalOffices();

    /**
     * Returns a Map of Active Zonal Offices in the given language.
     *
     * @param language  Selected language.
     * @return          Map of all Zonal Offices.
     */
    public Map<Integer, String> getActiveZonalOffices(String language);

    /**
     * Returns all the zonal offices
     *
     * @return  List of all the zonal offices
     */
    public List<ZonalOffice> getAll();
}
