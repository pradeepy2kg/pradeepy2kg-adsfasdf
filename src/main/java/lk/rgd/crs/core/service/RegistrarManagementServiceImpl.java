package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.AssignmentDAO;
import lk.rgd.crs.api.dao.RegistrarDAO;
import lk.rgd.crs.api.domain.Assignment;
import lk.rgd.crs.api.domain.Registrar;
import lk.rgd.crs.api.service.RegistrarManagementService;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public RegistrarManagementServiceImpl(RegistrarDAO registrarDao, AssignmentDAO assignmentDao,
                                          PopulationRegistry ecivil) {
        this.registrarDao = registrarDao;
        this.assignmentDao = assignmentDao;
        this.ecivil = ecivil;
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

        final String shortName = registrar.getShortName();
        logger.debug("Request to add a new Registrar : {} by : {}", shortName, user.getUserId());
        validateRegistrar(registrar, shortName);

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

        final String shortName = registrar.getShortName();
        logger.debug("Request to update Registrar : {} by : {}", shortName, user.getUserId());
        validateRegistrar(registrar, shortName);

        registrarDao.updateRegistrar(registrar, user);
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
            handleException("User : " + user.getUserId() +
                    " is not authorized to manage registrars", ErrorCodes.PERMISSION_DENIED);
        }

        logger.debug("Request to update Assignment for registrar : {}",
                assignment.getRegistrar().getShortName());

        assignmentDao.updateAssignment(assignment, user);
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

        logger.debug("Request to get type : " + type.ordinal() + (active ? " " : " in-") +
                "active Assignments of DS Division : {}", dsDivisionUKey);

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

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Registrar> getRegistrarByPin(long pin, User user) {
        if (!user.isAuthorized(Permission.REGISTRAR_MANAGEMENT)) {
            handleException("User : " + user.getUserId() +
                    " is not authorized to manage manage registrars", ErrorCodes.PERMISSION_DENIED);
        }
        logger.debug("Requesting List of registrars by pin number : {} ", pin);
        return registrarDao.getRegistrarByPin(pin);
    }

    private static final boolean isEmptyString(String s) {
        return s == null || s.trim().length() == 0;
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Assignment> getAllAssignments(User user) {
        if (!user.isAuthorized(Permission.REGISTRAR_MANAGEMENT)) {
            handleException("User : " + user.getUserId() +
                    " is not authorized to manage manage registrars", ErrorCodes.PERMISSION_DENIED);
        }
        logger.debug("requeting all assignments");
        return assignmentDao.getAllAssignments(user);
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public Assignment getAssignmentById(long assignmentUKey, User user) {
        if (!user.isAuthorized(Permission.REGISTRAR_MANAGEMENT)) {
            handleException("User : " + user.getUserId() +
                    " is not authorized to manage manage registrars", ErrorCodes.PERMISSION_DENIED);
        }
        logger.debug("requeting  assignment by assignmentUKey : {}", assignmentUKey);
        return assignmentDao.getById(assignmentUKey);
    }

    private void validateRegistrar(Registrar registrar, String shortName) {
        if (isEmptyString(registrar.getFullNameInEnglishLanguage())) {
            handleException("Registrar name in English is null", ErrorCodes.INVALID_DATA);
        }
        if (isEmptyString(registrar.getFullNameInOfficialLanguage())) {
            handleException("Registrar name in Official Language is null", ErrorCodes.INVALID_DATA);
        }
        if (isEmptyString(registrar.getCurrentAddress())) {
            handleException("Registrar : " + shortName + " address is null", ErrorCodes.INVALID_DATA);
        }
    }
}
