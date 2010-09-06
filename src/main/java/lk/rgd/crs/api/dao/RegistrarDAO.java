package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.Assignment;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.Registrar;

import java.util.List;

/**
 * Manage Registrars
 *
 * @author asankha
 */
public interface RegistrarDAO {

    /**
     * Get a Registrar record by unique ID
     *
     * @param idUKey the unique id
     * @return the Registrar or null if not found
     */
    public Registrar getById(long idUKey);

    /**
     * Add a new Registrar
     *
     * @param registrar the registrar being added
     * @param user      the user invoking the action
     */
    public void addRegistrar(Registrar registrar, User user);

    /**
     * Update an existing Registrar
     *
     * @param registrar the registrar being updated
     * @param user      the user invoking the action
     */
    public void updateRegistrar(Registrar registrar, User user);

    /**
     * Return the Birth registrars for the selected DS Division
     *
     * @param dsDivision the DS Division of interest
     * @param active     include only currently active registrars
     * @return the list of Registrars for the given DS division
     */
    public List<Registrar> getRegistrarsByTypeAndDSDivision(DSDivision dsDivision, Assignment.Type type, boolean active);

    /**
     * retriew registrars accourding to pin number
     *
     * @param pin unique pin number of registrar
     * @return list of registrars
     */
    public List<Registrar> getRegistrarByPin(long pin);
}
