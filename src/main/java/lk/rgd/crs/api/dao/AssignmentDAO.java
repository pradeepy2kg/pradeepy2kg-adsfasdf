package lk.rgd.crs.api.dao;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.Assignment;
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
     *
     * @param dsDivisionUKey the DS Division unique ID of interest
     * @param type           the type of assignment (birth, death or marriage)
     * @param active         include only currently active assignments
     * @return the list of assignments for the given DS division
     */
    public List<Assignment> getAssignmentsByTypeAndDSDivision(int dsDivisionUKey, Assignment.Type type, boolean active);

    /**
     * get all assignment on given district
     *
     * @param districtId district id
     * @param type       assignment type
     * @param active     assignment state
     * @return list of assignment
     */
    public List<Assignment> getAllAssignmentByDistricAndType(int districtId, Assignment.Type type, boolean active);

    /**
     * Return the active/inactive Assignments for the selected birth, death or marriage division
     *
     * @param divisionUKey the BD Division or MR Division unique id
     * @param type         the type of the assignment (birth, death or marriage)
     * @param active       include only currently active or inactive assignments
     * @param acting       include acting or non-acting assignments
     * @return the list of assignments
     */
    public List<Assignment> getAllAssignmentsByBDorMRDivisionAndType(int divisionUKey, Assignment.Type type,
                                                                     boolean active, boolean acting);

    /**
     * get all active assignments
     *
     * @param active active recode or inactive
     * @return list of assignments
     */
    public List<Assignment> getAllActiveAssignments(boolean active);
}
