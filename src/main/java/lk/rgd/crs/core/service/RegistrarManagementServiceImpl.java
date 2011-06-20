package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.IdentificationNumberUtil;
import lk.rgd.common.util.PinAndNicUtils;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.AssignmentDAO;
import lk.rgd.crs.api.dao.RegistrarDAO;
import lk.rgd.crs.api.domain.Assignment;
import lk.rgd.crs.api.domain.Registrar;
import lk.rgd.crs.api.service.RegistrarManagementService;
import lk.rgd.prs.api.domain.Address;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PINGenerator;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manage Registrars and Registration Assignments
 *
 * @author asankha
 */
public class RegistrarManagementServiceImpl implements RegistrarManagementService {

    private static final Logger logger = LoggerFactory.getLogger(RegistrarManagementServiceImpl.class);

    private RegistrarDAO registrarDao;
    private AssignmentDAO assignmentDao;
    private PopulationRegistry ecivil;
    private DSDivisionDAO dsDivisionDAO;
    private final PINGenerator pinGenerator;

    public RegistrarManagementServiceImpl(RegistrarDAO registrarDao, AssignmentDAO assignmentDao,
        PopulationRegistry ecivil, DSDivisionDAO dsDivisionDAO, PINGenerator pinGenerator) {
        this.registrarDao = registrarDao;
        this.assignmentDao = assignmentDao;
        this.ecivil = ecivil;
        this.dsDivisionDAO = dsDivisionDAO;
        this.pinGenerator = pinGenerator;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRegistrar(Registrar registrar, User user) {

        if (!user.isAuthorized(Permission.REGISTRAR_MANAGEMENT)) {
            handleException("User : " + user.getUserId() +
                " is not authorized to manage registrars", ErrorCodes.PERMISSION_DENIED);
        }

        validateMinimalRequirements(registrar);
        // validate pin and add registrar to the PRS
        validatePinAndProcessRegistrarToPRS(registrar, user);

        final String shortName = registrar.getShortName();
        logger.debug("Request to add a new Registrar : {} by : {}", shortName, user.getUserId());
        registrarDao.addRegistrar(registrar, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRegistrar(Registrar registrar, User user) {

        if (!user.isAuthorized(Permission.REGISTRAR_MANAGEMENT)) {
            handleException("User : " + user.getUserId() +
                " is not authorized to manage registrars", ErrorCodes.PERMISSION_DENIED);
        }

        validateMinimalRequirements(registrar);
        // validate pin and add registrar to the PRS
        validatePinAndProcessRegistrarToPRS(registrar, user);

        final String shortName = registrar.getShortName();
        logger.debug("Request to update Registrar : {} by : {}", shortName, user.getUserId());

        registrarDao.updateRegistrar(registrar, user);
    }

    private void validatePinAndProcessRegistrarToPRS(Registrar registrar, User user) {
        if (registrar.getPin() == 0) {
            // specified pin is empty
            Person person = processRegistrarToPRS(registrar, user);
            if (person != null) {
                registrar.setPin(person.getPin());
            }
        } else {
            final String pin = Long.toString(registrar.getPin());

            if (PinAndNicUtils.isValidPIN(pin)) {
                // given pin is valid but registrar not in the PRS
                if (!PinAndNicUtils.isValidPIN(registrar.getPin(), ecivil, user)) {
                    // add registrar to the PRS
                    Person person = processRegistrarToPRS(registrar, user);
                    if (person != null) {
                        registrar.setPin(person.getPin());
                    }
                }
            }
        }
    }

    private Person processRegistrarToPRS(Registrar registrar, User user) {
        Person person = null;
        String nic = registrar.getNic();
        if (IdentificationNumberUtil.isValidNIC(registrar.getNic())) {
            List<Person> records = ecivil.findPersonsByNIC(nic, user);

            if ((records == null || records.isEmpty())) {
                logger.debug("Adding registrar with NIC : {} to the PRS", nic);

                person = new Person();
                person.setFullNameInOfficialLanguage(registrar.getFullNameInOfficialLanguage());
                person.setFullNameInEnglishLanguage(registrar.getFullNameInEnglishLanguage());
                person.setNic(nic);
                person.setGender(registrar.getGender());
                person.setDateOfBirth(registrar.getDateOfBirth());
                person.setPersonPhoneNo(registrar.getPhoneNo());
                person.setPersonEmail(registrar.getEmailAddress());
                person.setStatus(Person.Status.SEMI_VERIFIED);

                // generate pin for registrar
                final long pin = pinGenerator.generatePINNumber(person.getDateOfBirth(), person.getGender() == 0, nic);
                person.setPin(pin);
                // add registrar to the PRS
                ecivil.addPerson(person, user);

                final Address add = new Address(registrar.getCurrentAddress());
                person.specifyAddress(add);
                ecivil.addAddress(add, user);
                ecivil.updatePerson(person, user);
            }
        }

        return person;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addAssignment(Assignment assignment, User user) {

        if (!user.isAuthorized(Permission.REGISTRAR_MANAGEMENT)) {
            handleException("User : " + user.getUserId() +
                " is not authorized to manage registrars", ErrorCodes.PERMISSION_DENIED);
        }

        switch (assignment.getType()) {
            case BIRTH: {
                if (assignment.getBirthDivision() == null) {
                    throw new CRSRuntimeException(
                        "Invalid Birth Registrar Assignment : No Birth Division", ErrorCodes.INVALID_DATA);
                }
                break;
            }
            case DEATH: {
                if (assignment.getDeathDivision() == null) {
                    throw new CRSRuntimeException(
                        "Invalid Death Registrar Assignment : No Death Division", ErrorCodes.INVALID_DATA);
                }
                break;
            }
            default: {
                if (assignment.getMarriageDivision() == null) {
                    throw new CRSRuntimeException(
                        "Invalid Marriage Registrar Assignment : No Marriage Division", ErrorCodes.INVALID_DATA);
                }
                break;
            }
        }

        logger.debug("Request to add new Assignment for registrar : {}",
            assignment.getRegistrar().getShortName());

        assignmentDao.addAssignment(assignment, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateAssignment(Assignment assignment, User user) {

        if (!user.isAuthorized(Permission.REGISTRAR_MANAGEMENT)) {
            handleException("User : " + user.getUserId() + " is not authorized to manage registrars",
                ErrorCodes.PERMISSION_DENIED);
        }

        logger.debug("Request to update Assignment for registrar : {}",
            assignment.getRegistrar().getShortName());

        assignmentDao.updateAssignment(assignment, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAssignment(long assignmentId, User user) {
        if (!user.isAuthorized(Permission.REGISTRAR_DELETE)) {
            handleException("User : " + user.getUserId() + " is not authorized to delete assignments",
                ErrorCodes.PERMISSION_DENIED);
        }
        logger.debug("attempt to delete assignment with unique key : {}", assignmentId);

        Assignment assignment = assignmentDao.getById(assignmentId);
        Assignment.Type type = assignment.getType();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void inactivateAssignment(long assignmentUKey, User user) {

        if (!user.isAuthorized(Permission.REGISTRAR_MANAGEMENT)) {
            handleException("User : " + user.getUserId() +
                " is not authorized to manage registrars", ErrorCodes.PERMISSION_DENIED);
        }

        Assignment assignment = assignmentDao.getById(assignmentUKey);
        logger.debug("Request to inactivate Assignment for registrar : {}",
            assignment.getRegistrar().getShortName());

        assignment.getLifeCycleInfo().setActive(false);
        assignmentDao.updateAssignment(assignment, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void inactivateRegistrar(long registrarUKey, User user) {

        if (!user.isAuthorized(Permission.REGISTRAR_MANAGEMENT)) {
            handleException("User : " + user.getUserId() +
                " is not authorized to manage registrars", ErrorCodes.PERMISSION_DENIED);
        }

        Registrar registrar = registrarDao.getById(registrarUKey);
        logger.debug("Request to inactivate Registrar : {}", registrar.getShortName());

        registrar.getLifeCycleInfo().setActive(false);
        registrarDao.updateRegistrar(registrar, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Assignment> getAssignments(long registrarUKey, User user) {

        if (!user.isAuthorized(Permission.REGISTRAR_MANAGEMENT)) {
            handleException("User : " + user.getUserId() +
                " is not authorized to manage registrars", ErrorCodes.PERMISSION_DENIED);
        }

        Registrar registrar = registrarDao.getById(registrarUKey);
        logger.debug("Request to get assignments of Registrar: {}", registrar.getShortName());

        return assignmentDao.getAssignmentsForRegistrar(registrar);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Registrar getRegistrarById(long idUKey) {
        logger.debug("Requesting registrar id : {}", idUKey);
        return registrarDao.getById(idUKey);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Assignment> getAssignmentsByDSDivision(int dsDivisionUKey, Assignment.Type type, boolean active, User user) {

        if (!user.isAuthorized(Permission.REGISTRAR_MANAGEMENT)) {
            handleException("User : " + user.getUserId() +
                " is not authorized to manage registrars", ErrorCodes.PERMISSION_DENIED);
        }
        //todo edit this log line for type == null
/*        logger.debug("Request to get type : " + type.ordinal() + (active ? " " : " in-") +
                "active Assignments of DS Division : {}", dsDivisionUKey);*/

        return assignmentDao.getAssignmentsByTypeAndDSDivision(dsDivisionUKey, type, active);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Assignment> getAssignmentsByDSDivision(int dsDivisionUKey, boolean active, User user) {

        if (!user.isAuthorized(Permission.REGISTRAR_MANAGEMENT)) {
            handleException("User : " + user.getUserId() +
                " is not authorized to manage registrars", ErrorCodes.PERMISSION_DENIED);
        }

        logger.debug("Request to get " + (active ? " " : " in-") +
            "active Assignments of DS Division : {}", dsDivisionUKey);

        List<Assignment> results = new ArrayList<Assignment>();
        results.addAll(assignmentDao.getAssignmentsByTypeAndDSDivision(dsDivisionUKey, Assignment.Type.BIRTH, active));
        results.addAll(assignmentDao.getAssignmentsByTypeAndDSDivision(dsDivisionUKey, Assignment.Type.DEATH, active));
        results.addAll(assignmentDao.getAssignmentsByTypeAndDSDivision(dsDivisionUKey, Assignment.Type.GENERAL_MARRIAGE, active));
        results.addAll(assignmentDao.getAssignmentsByTypeAndDSDivision(dsDivisionUKey, Assignment.Type.KANDYAN_MARRIAGE, active));
        results.addAll(assignmentDao.getAssignmentsByTypeAndDSDivision(dsDivisionUKey, Assignment.Type.MUSLIM_MARRIAGE, active));
        return results;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Registrar getRegistrarByPin(long pin, User user) {
        if (!user.isAuthorized(Permission.SEARCH_REGISTRAR)) {
            handleException("User : " + user.getUserId() +
                " is not authorized to search registrars", ErrorCodes.PERMISSION_DENIED);
        }
        logger.debug("Requesting a registrar by pin number : {} ", pin);
        return registrarDao.getRegistrarByPin(pin);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Assignment> getAssignmentsByDistrictId(int districtId, Assignment.Type type, boolean active, User user) {
        if (!user.isAuthorized(Permission.REGISTRAR_MANAGEMENT)) {
            handleException("User : " + user.getUserId() +
                " is not authorized to manage manage registrars", ErrorCodes.PERMISSION_DENIED);
        }
        logger.debug("requesting all assignments for given district id : {}", districtId);
        List<DSDivision> divisions = dsDivisionDAO.getAllDSDivisionByDistrictKey(districtId);
        List<Assignment> results = new ArrayList<Assignment>();
        Iterator itr = divisions.iterator();
        while (itr.hasNext()) {
            DSDivision current = (DSDivision) itr.next();
            results.addAll(assignmentDao.getAssignmentsByTypeAndDSDivision(current.getDsDivisionUKey(), type, active));
        }
        return results;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Assignment> getAssignmentsByDistrictId(int districtId, boolean active, User user) {
        if (!user.isAuthorized(Permission.REGISTRAR_MANAGEMENT)) {
            handleException("User : " + user.getUserId() +
                " is not authorized to manage manage registrars", ErrorCodes.PERMISSION_DENIED);
        }
        logger.debug("requesting all assignments filter by state for given district id : {}", districtId);
        List<Assignment> results = new ArrayList<Assignment>();
        List<DSDivision> dsDivisions = dsDivisionDAO.getAllDSDivisionByDistrictKey(districtId);
        Iterator itr = dsDivisions.iterator();
        while (itr.hasNext()) {
            DSDivision current = (DSDivision) itr.next();
            results.addAll(getAssignmentsByDSDivision(current.getDsDivisionUKey(), active, user));
        }
        logger.info("results : {}", results.size());
        return results;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER)
    public List<Assignment> getAllActiveAssignment(boolean active, User user) {
        if (!user.isAuthorized(Permission.REGISTRAR_MANAGEMENT)) {
            handleException("User : " + user.getUserId() +
                " is not authorized to manage manage registrars", ErrorCodes.PERMISSION_DENIED);
        }
        logger.debug("requesting all active assignments");
        return assignmentDao.getAllActiveAssignments(active);
    }

    /**
     * @inheritedoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Registrar> getRegistrarByNameOrPartOfTheName(String name, User user) {
        logger.debug("requesting registrar list by name or part of the name : {} ", name);
        return registrarDao.getRegistrarByNameOrPartOfTheName(name);
    }

    /**
     * @inheritedoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public Assignment getAssignmentById(long assignmentUKey, User user) {
        if (!user.isAuthorized(Permission.REGISTRAR_MANAGEMENT)) {
            handleException("User : " + user.getUserId() +
                " is not authorized to manage manage registrars", ErrorCodes.PERMISSION_DENIED);
        }
        logger.debug("requesting  assignment by assignmentUKey : {}", assignmentUKey);
        return assignmentDao.getById(assignmentUKey);
    }

    private void validateMinimalRequirements(Registrar registrar) {
        if (registrar.getFullNameInEnglishLanguage() == null || registrar.getFullNameInOfficialLanguage() == null ||
            registrar.getNic() == null || registrar.getDateOfBirth() == null || registrar.getCurrentAddress() == null) {
            handleException("Registrar record being processed is incomplete. Check required field values",
                ErrorCodes.INVALID_DATA);
        }
        final String nic = registrar.getNic();
        if (!IdentificationNumberUtil.isValidNIC(nic)) {
            handleException("Registrar nic : " + nic + " is invalid", ErrorCodes.INVALID_DATA);
        }
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }

    private static final boolean isEmptyString(String s) {
        return s == null || s.trim().length() == 0;
    }
}
