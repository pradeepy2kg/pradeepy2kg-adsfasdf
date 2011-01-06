package lk.rgd.crs.core.service;

import junit.framework.Assert;
import junit.framework.TestCase;
import lk.rgd.AppConstants;
import lk.rgd.UnitTestManager;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
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
        Registrar reg1 = addSampleRegistrar(1);

        Assignment assign1 = new Assignment();
        assign1.setAppointmentDate(DateTimeUtils.getDateFromISO8601String("2010-01-24"));
        assign1.setBirthDivision(bdDivisionDAO.getBDDivisionByPK(1));
        assign1.setRegistrar(reg1);
        assign1.setType(Assignment.Type.BIRTH);
        regMgtSvc.addAssignment(assign1, admin);

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
