package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.IdentificationNumberUtil;
import lk.rgd.common.util.PinAndNicUtils;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.*;
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
import java.util.Set;

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
    private final BirthDeclarationDAO birthDeclarationDAO;
    private final DeathRegisterDAO deathRegisterDAO;
    private final MarriageRegistrationDAO marriageRegistrationDAO;

    public RegistrarManagementServiceImpl(RegistrarDAO registrarDao, AssignmentDAO assignmentDao,
        PopulationRegistry ecivil, DSDivisionDAO dsDivisionDAO, PINGenerator pinGenerator,
        BirthDeclarationDAO birthDeclarationDAO, DeathRegisterDAO deathRegisterDAO,
        MarriageRegistrationDAO marriageRegistrationDAO) {
        this.registrarDao = registrarDao;
        this.assignmentDao = assignmentDao;
        this.ecivil = ecivil;
        this.dsDivisionDAO = dsDivisionDAO;
        this.pinGenerator = pinGenerator;
        this.birthDeclarationDAO = birthDeclarationDAO;
        this.deathRegisterDAO = deathRegisterDAO;
        this.marriageRegistrationDAO = marriageRegistrationDAO;
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
        validatePinAndProcessRegistrarToPRS(0, registrar, user);

        final String shortName = registrar.getShortName();
        registrar.setState(Registrar.State.ACTIVE);
        logger.debug("Request to add a new Registrar : {} by : {}", shortName, user.getUserId());
        registrarDao.addRegistrar(registrar, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRegistrar(long registrarUKey, User user) {
        logger.debug("Attempt to delete registrar with unique key : {} by user : {}", registrarUKey, user.getUserId());
        if (!user.isAuthorized(Permission.REGISTRAR_DELETE)) {
            handleException("User : " + user.getUserId() + " is not authorized to delete registrars",
                ErrorCodes.PERMISSION_DENIED);
        }

        Registrar registrar = registrarDao.getById(registrarUKey);

        if (registrar.getAssignments() == null || registrar.getAssignments().isEmpty() || isRegistrarEligibleToDelete(registrar)) {
            registrar.setState(Registrar.State.DELETED);
            registrar.getLifeCycleInfo().setActive(false);
            registrarDao.updateRegistrar(registrar, user);
            logger.debug("Deleted registrar with registrarUKey : {} by user : {}", registrarUKey, user.getUserId());
        } else {
            handleException("Registrar with registrarUKey : " + registrarUKey + " is not allowed to delete since " +
                "he/she has mapping registrations", ErrorCodes.INVALID_STATE_FOR_REMOVAL);
        }
    }

    private boolean isRegistrarEligibleToDelete(Registrar registrar) {
        Set<Assignment> assignmentSet = registrar.getAssignments();
        List<Object> list = new ArrayList<Object>();
        for (Assignment assignment : assignmentSet) {
            if (!isAssignmentEligibleToDelete(assignment)) {
                return false;
            }
        }
        return list.size() == 0;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRegistrar(long previousPin, Registrar registrar, User user) {

        if (!user.isAuthorized(Permission.REGISTRAR_MANAGEMENT)) {
            handleException("User : " + user.getUserId() +
                " is not authorized to manage registrars", ErrorCodes.PERMISSION_DENIED);
        }

        validateMinimalRequirements(registrar);
        // validate pin and add registrar to the PRS
        validatePinAndProcessRegistrarToPRS(previousPin, registrar, user);

        final String shortName = registrar.getShortName();
        logger.debug("Request to update Registrar : {} by : {}", shortName, user.getUserId());

        registrarDao.updateRegistrar(registrar, user);
    }

    private void validatePinAndProcessRegistrarToPRS(long previousPin, Registrar registrar, User user) {
        Person person = null;
        if (registrar.getPin() == 0) {
            // specified pin is empty
            person = processRegistrarToPRS(registrar, user);
        } else {
            final String pin = Long.toString(registrar.getPin());

            if (PinAndNicUtils.isValidPIN(pin)) {
                // given pin is valid but registrar not in the PRS
                if (!PinAndNicUtils.isValidPIN(registrar.getPin(), ecivil, user)) {
                    // add registrar to the PRS
                    person = processRegistrarToPRS(registrar, user);
                }
            }
        }

        if (person != null) {
            registrar.setPin(person.getPin());
        } else {
            registrar.setPin(previousPin);
        }
        logger.debug("validate pin and process registrar to PRS with previousPin : {} and currentPin : {}", previousPin,
            registrar.getPin());
    }

    private Person processRegistrarToPRS(Registrar registrar, User user) {
        logger.debug("processing registrar to PRS");
        Person person = null;
        String nic = registrar.getNic();
        if (IdentificationNumberUtil.isValidNIC(registrar.getNic())) {
            // TODO this is a temporary solution have to fix immediately
//            List<Person> records = ecivil.findPersonsByNIC(nic, user);

//            if ((records == null || records.isEmpty())) {
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
//            }
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

        logger.debug("Request to add new Assignment for registrar : {}", assignment.getRegistrar().getShortName());
        assignment.setState(Assignment.State.ACTIVE);
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

        logger.debug("Request to update Assignment for registrar : {}", assignment.getRegistrar().getShortName());

        assignmentDao.updateAssignment(assignment, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAssignment(long assignmentUKey, User user) {
        logger.debug("Attempt to delete assignment with unique key : {} by user : {}", assignmentUKey, user.getUserId());
        if (!user.isAuthorized(Permission.REGISTRAR_DELETE)) {
            handleException("User : " + user.getUserId() + " is not authorized to delete assignments",
                ErrorCodes.PERMISSION_DENIED);
        }

        Assignment assignment = assignmentDao.getById(assignmentUKey);

        if (isAssignmentEligibleToDelete(assignment)) {
            assignment.setState(Assignment.State.DELETED);
            assignment.getLifeCycleInfo().setActive(false);
            assignmentDao.updateAssignment(assignment, user);
            logger.debug("Deleted assignment with assignmentUKey : {} by user : {}", assignmentUKey, user.getUserId());
        } else {
            handleException("Assignment : " + assignmentUKey +
                " is not allowed to delete since it have mapping registrations", ErrorCodes.INVALID_STATE_FOR_REMOVAL);
        }
    }

    /**
     * Checks whether a given assignment is eligible to delete
     *
     * @param assignment the assignment to be deleted
     * @return if eligible return true
     */
    private boolean isAssignmentEligibleToDelete(Assignment assignment) {
        Assignment.Type type = assignment.getType();
        final String pin = Long.toString(assignment.getRegistrar().getPin());
        final String nic = assignment.getRegistrar().getNic();
        int divisionUKey = 0;

        List<Object> list = new ArrayList<Object>();
        switch (type) {
            case BIRTH:
                divisionUKey = assignment.getBirthDivision().getBdDivisionUKey();
                list.addAll(birthDeclarationDAO.getBirthsByRegistrarPinOrNicAndDivision(pin, nic, divisionUKey));
                break;
            case DEATH:
                divisionUKey = assignment.getDeathDivision().getBdDivisionUKey();
                list.addAll(deathRegisterDAO.getDeathsByRegistrarPinOrNicAndDivision(pin, nic, divisionUKey));
                break;
            case GENERAL_MARRIAGE:
            case KANDYAN_MARRIAGE:
            case MUSLIM_MARRIAGE:
                divisionUKey = assignment.getMarriageDivision().getMrDivisionUKey();
                list.addAll(marriageRegistrationDAO.getMarriagesByRegistrarPinOrNicAndDivision(pin, nic, divisionUKey));
                break;
        }
        return list.size() == 0;
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
        logger.debug("Request to inactivate Assignment for registrar : {}", assignment.getRegistrar().getShortName());

        assignment.setState(Assignment.State.INACTIVE);
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

        registrar.setState(Registrar.State.INACTIVE);
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
    @Transactional(propagation = Propagation.SUPPORTS)
    public Registrar getRegistrarByNIC(String nic, User user) {
        logger.debug("Looking for registrar with the nic: {} by user: {}", nic, user.getUserId());
        if (!user.isAuthorized(Permission.SEARCH_REGISTRAR)) {
            handleException("User : " + user.getUserId() +
                " is not authorized to search registrars", ErrorCodes.PERMISSION_DENIED);
        }
        return registrarDao.getRegistrarByNIC(nic);
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
