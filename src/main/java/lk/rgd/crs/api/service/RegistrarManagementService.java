package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.Assignment;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.Registrar;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Manage Registrars and Registration Assignments
 *
 * @author asankha
 */
public interface RegistrarManagementService {

    /**
     * Adds a new Registrar to the system
     *
     * @param registrar the instance being added
     * @param user      the user invoking the action
     */
    public void addRegistrar(Registrar registrar, User user);

    /**
     * Update Registrar information
     *
     * @param registrar the instance being updated
     * @param user      the user invoking the action
     */
    public void updateRegistrar(Registrar registrar, User user);

    /**
     * Add a new Registrar assignment to the system
     *
     * @param assignment the assignment being added
     * @param user       the user invoking the action
     */
    public void addAssignment(Assignment assignment, User user);

    /**
     * Update the assignment of a Registrar
     *
     * @param assignment the assignment being updated
     * @param user       the user invoking the action
     */
    public void updateAssignment(Assignment assignment, User user);

    /**
     * Inactivate an assignment of a registrar
     *
     * @param assignment the assignment being deactivated
     * @param user       the user invoking the action
     */
    public void inactivateAssignment(Assignment assignment, User user);

    /**
     * Inactivate a Registrar
     *
     * @param registrar the registrar being inactivated
     * @param user      the user invoking the action
     */
    public void inactivateRegistrar(Registrar registrar, User user);

    /**
     * Return all assignments for a given Registrar
     *
     * @param registrar the registrar whose assignments are required
     * @param user      the user invoking the action
     * @return
     */
    public List<Assignment> getAssignments(Registrar registrar, User user);

    /**
     * Get registrars for the given DS Division
     *
     * @param dsDivision the DS division whose registrars are required
     * @param type       the type of registrars interested in
     * @param active     return only active registrars if true, else return only inactive registrars
     * @param user       the user invoking the action
     * @return the list of qualifying registrars
     */
    public List<Registrar> getRegistrarsByDSDivision(DSDivision dsDivision, Assignment.Type type, boolean active, User user);

    public Registrar getRegistrarById(long idUKey);
}
