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
     * retrieve registrars according to pin number
     *
     * @param pin unique pin number of registrar
     * @return  registrars object
     */
    public Registrar getRegistrarByPin(long pin);

    /**
     * Find a registrar according to NIC.
     * (As there can be more than one record for a given NIC, the first registrar of the list will be returned.)
     *
     * @param nic NIC of the registrar
     * @return Registrar with the given NIC.
     */
    public Registrar getRegistrarByNIC(String nic);

    /**
     * get registrars by name or part of the name
     *
     * @param name name or part of the name
     * @return list of registrars
     */
    public List<Registrar> getRegistrarByNameOrPartOfTheName(String name);
}
