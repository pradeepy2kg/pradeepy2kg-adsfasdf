package lk.rgd.crs.core.service;

import junit.framework.Assert;
import junit.framework.TestCase;
import lk.rgd.AppConstants;
import lk.rgd.UnitTestManager;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.core.service.UserManagerImpl;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.MRDivisionDAO;
import lk.rgd.crs.api.domain.Assignment;
import lk.rgd.crs.api.domain.Registrar;
import lk.rgd.crs.api.service.RegistrarManagementService;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * @author asankha
 */
public class RegistrarManagementServiceTest extends TestCase {

    private final ApplicationContext ctx = UnitTestManager.ctx;
    private final RegistrarManagementService regMgtSvc;
    private final BDDivisionDAO bdDivisionDAO;
    private final MRDivisionDAO mrDivisionDAO;
    private final User admin;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public RegistrarManagementServiceTest() {

        regMgtSvc = (RegistrarManagementService) ctx.getBean("registrarManagmentService", RegistrarManagementService.class);
        bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
        mrDivisionDAO = (MRDivisionDAO) ctx.getBean("mrDivisionDAOImpl", MRDivisionDAO.class);

        UserManagerImpl userManager = (UserManagerImpl) ctx.getBean("userManagerService", UserManagerImpl.class);
        try {
            admin = userManager.authenticateUser("admin", "password");
        } catch (AuthorizationException e) {
            throw new IllegalArgumentException("Cannot authenticate sample users");
        }
    }

    public void testAddRegistrar() throws Exception {
        // reg1 is a birth and death registrar for BD division with PK 1
        Registrar reg1 = addSampleRegistrar(1);

        Assignment assign1 = new Assignment();
        assign1.setAppointmentDate(DateTimeUtils.getDateFromISO8601String("2010-01-24"));
        assign1.setBirthDivision(bdDivisionDAO.getBDDivisionByPK(1));
        assign1.setRegistrar(reg1);
        assign1.setType(Assignment.Type.BIRTH);
        regMgtSvc.addAssignment(assign1, admin);

        Assignment assign2 = new Assignment();
        assign2.setAppointmentDate(DateTimeUtils.getDateFromISO8601String("2010-01-24"));
        assign2.setDeathDivision(bdDivisionDAO.getBDDivisionByPK(1));
        assign2.setRegistrar(reg1);
        assign2.setType(Assignment.Type.DEATH);
        regMgtSvc.addAssignment(assign2, admin);

        // reg2 is a birth registrar for BD division with PK 2
        Registrar reg2 = addSampleRegistrar(2);

        Assignment assign3 = new Assignment();
        assign3.setAppointmentDate(DateTimeUtils.getDateFromISO8601String("2010-01-24"));
        assign3.setBirthDivision(bdDivisionDAO.getBDDivisionByPK(2));
        assign3.setRegistrar(reg2);
        assign3.setType(Assignment.Type.BIRTH);
        regMgtSvc.addAssignment(assign3, admin);

        // reg3 is a death registrar for BD division with PK 2
        Registrar reg3 = addSampleRegistrar(3);

        Assignment assign4 = new Assignment();
        assign4.setAppointmentDate(DateTimeUtils.getDateFromISO8601String("2010-01-24"));
        assign4.setDeathDivision(bdDivisionDAO.getBDDivisionByPK(2));
        assign4.setRegistrar(reg3);
        assign4.setType(Assignment.Type.DEATH);
        regMgtSvc.addAssignment(assign4, admin);

        // reg4 is a marriage registrar for MR division with PK 1
        Registrar reg4 = addSampleRegistrar(4);

        Assignment assign5 = new Assignment();
        assign5.setAppointmentDate(DateTimeUtils.getDateFromISO8601String("2010-01-24"));
        assign5.setMarriageDivision(mrDivisionDAO.getMRDivisionByPK(1));
        assign5.setRegistrar(reg4);
        assign5.setType(Assignment.Type.MARRIAGE);
        regMgtSvc.addAssignment(assign5, admin);

        // query
        List<Assignment> results = regMgtSvc.getAssignmentsByDSDivision(
            bdDivisionDAO.getBDDivisionByPK(2).getDsDivision().getDsDivisionUKey(),
            Assignment.Type.BIRTH,
            true /* active */,
            admin);

        Assert.assertEquals(2, results.size());

        // inactivate assignment
        regMgtSvc.inactivateAssignment(assign3, admin);

        results = regMgtSvc.getAssignmentsByDSDivision(
            bdDivisionDAO.getBDDivisionByPK(2).getDsDivision().getDsDivisionUKey(),
            Assignment.Type.BIRTH,
            true /* active */,
            admin);

        Assert.assertEquals(1, results.size());

        results = regMgtSvc.getAssignmentsByDSDivision(
            bdDivisionDAO.getBDDivisionByPK(2).getDsDivision().getDsDivisionUKey(),
            Assignment.Type.BIRTH,
            false /* inactive */,
            admin);

        Assert.assertEquals(1, results.size());

        // check death registrars
        results = regMgtSvc.getAssignmentsByDSDivision(
            bdDivisionDAO.getBDDivisionByPK(2).getDsDivision().getDsDivisionUKey(),
            Assignment.Type.DEATH,
            true /* active */,
            admin);

        Assert.assertEquals(2, results.size());

        results = regMgtSvc.getAssignmentsByDSDivision(
            bdDivisionDAO.getBDDivisionByPK(2).getDsDivision().getDsDivisionUKey(),
            Assignment.Type.DEATH,
            false /* inactive */,
            admin);

        Assert.assertEquals(0, results.size());

        results = regMgtSvc.getAssignmentsByDSDivision(
            bdDivisionDAO.getBDDivisionByPK(2).getDsDivision().getDsDivisionUKey(),
            Assignment.Type.MARRIAGE,
            true /* active */,
            admin);

        Assert.assertEquals(1, results.size());

        results = regMgtSvc.getAssignmentsByDSDivision(
            bdDivisionDAO.getBDDivisionByPK(2).getDsDivision().getDsDivisionUKey(),
            Assignment.Type.MARRIAGE,
            false /* inactive */,
            admin);

        Assert.assertEquals(0, results.size());
    }

    private Registrar addSampleRegistrar(int i) {
        Registrar registrar = new Registrar();
        registrar.setFullNameInEnglishLanguage("Name of Registrar " + i + " in English");
        registrar.setFullNameInOfficialLanguage("Name of Registrar " + i + " in Sinhala");
        registrar.setCurrentAddress("Address of the Registrar " + i);
        registrar.setDateOfBirth(DateTimeUtils.getDateFromISO8601String("1980-11-14"));
        registrar.setGender(AppConstants.Gender.MALE.ordinal());
        registrar.setNic("75200111" + i + "V");
        registrar.setPin(802001110L + i);
        regMgtSvc.addRegistrar(registrar, admin);
        return registrar;
    }
}
