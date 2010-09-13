package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.Assignment;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.Registrar;

import java.util.List;

/**
 * Manage Assignments to Registrars
 *
 * @author asankha
 */
public interface AssignmentDAO {

    /**
     * Get an assignment record by unique ID
     *
     * @param idUKey the unique id
     * @return the Assignment or null if not found
     */
    public Assignment getById(long idUKey);

    /**
     * Add a new Assignment to a Registrar
     *
     * @param assignment the assignment
     * @param user       the user invoking the action
     */
    public void addAssignment(Assignment assignment, User user);

    /**
     * Update an existing Assignment of a Registrar
     *
     * @param assignment the assignment
     * @param user       the user invoking the action
     */
    public void updateAssignment(Assignment assignment, User user);

    /**
     * Return all Assignments to the given registrar
     *
     * @param registrar the selected registrar
     * @return the list of assignments or an empty List
     */
    public List<Assignment> getAssignmentsForRegistrar(Registrar registrar);

    /**
     * Return the Assignments for the selected DS Division
     * @param dsDivisionUKey the DS Division unique ID of interest
     * @param type the type of assignment (birth, death or marriage)
     * @param active include only currently active assignments
     * @return the list of assignments for the given DS division
     */
    public List<Assignment> getAssignmentsByTypeAndDSDivision(int dsDivisionUKey, Assignment.Type type, boolean active);

    /**
     * get all assignments
     *
     * @param user requesting user
     * @return list of all assignments
     */
    public List<Assignment> getAllAssignments(User user);
}
