package lk.rgd.crs.core.service;

import junit.framework.TestCase;
import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.UnitTestManager;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.MRDivisionDAO;
import lk.rgd.crs.api.domain.Assignment;
import lk.rgd.crs.api.domain.Registrar;
import lk.rgd.crs.api.service.RegistrarManagementService;
import org.springframework.context.ApplicationContext;

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

        regMgtSvc = (RegistrarManagementService) ctx.getBean("registrarManagementService", RegistrarManagementService.class);
        bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
        mrDivisionDAO = (MRDivisionDAO) ctx.getBean("mrDivisionDAOImpl", MRDivisionDAO.class);

        UserManager userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);
        try {
            admin = userManager.authenticateUser("admin", "password");
        } catch (AuthorizationException e) {
            throw new IllegalArgumentException("Cannot authenticate sample users");
        }
    }

    public void testAddRegistrar() throws Exception {
        // reg1 is a birth and death registrar for BD division with PK 1
        Registrar reg1 = addSampleRegistrar(5);

        Assignment assign1 = new Assignment();
        assign1.setAppointmentDate(DateTimeUtils.getDateFromISO8601String("2010-01-24"));
        assign1.setBirthDivision(bdDivisionDAO.getBDDivisionByPK(1));
        assign1.setRegistrar(reg1);
        assign1.setType(Assignment.Type.BIRTH);
        regMgtSvc.addAssignment(assign1, admin);

    }

    public void testAddRegistrarWithMinimalRequirements() {
        Registrar registrar = addSampleRegistrar(5);
        assertTrue(registrar.getRegistrarUKey() > 0);

        Registrar reg = new Registrar();
        reg.setFullNameInEnglishLanguage(" ");
        reg.setFullNameInOfficialLanguage("Registrar name in official language");
        reg.setNic("752101118V");
        reg.setDateOfBirth(DateTimeUtils.getDateFromISO8601String("1975-07-28"));
        reg.setCurrentAddress("Registrar address");
        reg.setGender(AppConstants.Gender.MALE.ordinal());

        try {
            regMgtSvc.addRegistrar(reg, admin);
            fail("Registrar cannot be registered with empty name in english");
        } catch (CRSRuntimeException expected) {
            checkErrorCode(expected);
        }
        reg.setFullNameInOfficialLanguage("  ");
        try {
            regMgtSvc.addRegistrar(reg, admin);
            fail("Registrar cannot be registered with empty name in official language");
        } catch (CRSRuntimeException expected) {
            checkErrorCode(expected);
        }
        reg.setNic("  ");
        try {
            regMgtSvc.addRegistrar(reg, admin);
            fail("Registrar cannot be registered with empty nic");
        } catch (CRSRuntimeException expected) {
            checkErrorCode(expected);
        }
        reg.setCurrentAddress("   ");
        try {
            regMgtSvc.addRegistrar(reg, admin);
            fail("Registrar cannot be registered with empty address");
        } catch (CRSRuntimeException expected) {
            checkErrorCode(expected);
        }
        reg.setDateOfBirth(null);
        try {
            regMgtSvc.addRegistrar(reg, admin);
            fail("Registrar cannot be registered with empty date of birth");
        } catch (CRSRuntimeException expected) {
            checkErrorCode(expected);
        }
    }

    private void checkErrorCode(CRSRuntimeException e) {
        if (e.getErrorCode() != ErrorCodes.INVALID_DATA) {
            fail("Invalid error code, should be INVALID DATA");
        }
    }

    private Registrar addSampleRegistrar(int i) {
        Registrar registrar = new Registrar();
        registrar.setFullNameInEnglishLanguage("Name of Registrar " + i + " in English");
        registrar.setFullNameInOfficialLanguage("Name of Registrar " + i + " in Sinhala");
        registrar.setCurrentAddress("Address of the Registrar " + i);
        registrar.setDateOfBirth(DateTimeUtils.getDateFromISO8601String("1975-11-14"));
        registrar.setGender(AppConstants.Gender.MALE.ordinal());
        registrar.setNic("75200111" + i + "V");
        registrar.setPin(197520011110L + i);
        regMgtSvc.addRegistrar(registrar, admin);
        return registrar;
    }
}
