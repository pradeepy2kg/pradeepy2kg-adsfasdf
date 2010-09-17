package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.Assignment;
import lk.rgd.crs.api.domain.Registrar;

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
     * @param assignmentUKey
     * @param user           the user invoking the action
     */
    public void inactivateAssignment(long assignmentUKey, User user);

    /**
     * Inactivate a Registrar
     *
     * @param registrarUKey
     * @param user          the user invoking the action
     */
    public void inactivateRegistrar(long registrarUKey, User user);

    /**
     * Return all assignments for a given Registrar
     *
     * @param registrarUKey
     * @param user          the user invoking the action  @return
     */
    public List<Assignment> getAssignments(long registrarUKey, User user);

    /**
     * Get Assignments of a particular type for the given DS Division
     *
     * @param dsDivisionUKey the DS division unique ID whose assignments are required
     * @param type           the type of assignments interested in
     * @param active         return only active assignments if true, else return only inactive assignments
     * @param user           the user invoking the action
     * @return the list of qualifying assignments
     */
    public List<Assignment> getAssignmentsByDSDivision(int dsDivisionUKey, Assignment.Type type, boolean active, User user);

    /**
     * Get all types of Assignments for the given DS Division
     *
     * @param dsDivisionUKey the DS division unique ID whose assignments are required
     * @param active         return only active assignments if true, else return only inactive assignments
     * @param user           the user invoking the action
     * @return the list of qualifying assignments
     */
    public List<Assignment> getAssignmentsByDSDivision(int dsDivisionUKey, boolean active, User user);

    /**
     * get registrar by idUKey
     *
     * @param idUKey
     * @return registrar
     */
    public Registrar getRegistrarById(long idUKey);

    /**
     * retrun registrars by given pin number
     *
     * @param pin  registrars unique pin number
     * @param user user who is requesting registrars
     * @return list of registrars
     */
    public List<Registrar> getRegistrarByPin(long pin, User user);

    /**
     * get all the assignment (in all divisions)
     *
     * @param user requeting user
     * @return
     */
    public List<Assignment> getAllAssignments(User user);

    /**
     * get a assignment by assignentUKey
     *
     * @param assignmentUKey uniquey for assignment
     * @param user           user who has permisiion to request a assignment
     * @return assignment wich has given unique key
     */
    public Assignment getAssignmentById(long assignmentUKey, User user);

    /**
     * get all assignments in given district
     *
     * @param districtId distict ID
     * @param type       assignment type
     * @param active     assignment state
     * @param user       userr who has permiassion to request assignments
     * @return list of assignment filter by given district ,state and type
     */
    public List<Assignment> getAssignmentsByDistrictId(int districtId, Assignment.Type type, boolean active, User user);

}
