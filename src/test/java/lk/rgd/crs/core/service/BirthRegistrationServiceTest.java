package lk.rgd.crs.core.service;

import junit.framework.Assert;
import junit.framework.TestCase;
import lk.rgd.UnitTestManager;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.core.service.UserManagerImpl;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.InformantInfo;
import lk.rgd.crs.api.service.BirthRegistrationService;
import org.springframework.context.ApplicationContext;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author asankha
 */
public class BirthRegistrationServiceTest extends TestCase {

    private final ApplicationContext ctx = UnitTestManager.ctx;
    private final BirthRegistrationService birthRegSvc;
    private final BDDivisionDAO bdDivisionDAO;
    private final UserManagerImpl userManager;
    private final BDDivision colomboBDDivision;
    private final BDDivision negamboBDDivision;
    private User deoColomboColombo;
    private User deoGampahaNegambo;
    private User adrColomboColombo;
    private User adrGampahaNegambo;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        BirthDeclarationDAO birthDeclarationDAO = (BirthDeclarationDAO)
                ctx.getBean("birthDeclarationDAOImpl", BirthDeclarationDAO.class);

        // delete records we may have added
        try {
            birthDeclarationDAO.deleteBirthDeclaration(
                birthDeclarationDAO.getByBDDivisionAndSerialNo(colomboBDDivision, 2010101).getIdUKey());
        } catch (Exception e) {}
        try {
            birthDeclarationDAO.deleteBirthDeclaration(
                birthDeclarationDAO.getByBDDivisionAndSerialNo(colomboBDDivision, 2010102).getIdUKey());
        } catch (Exception e) {}
        try {
            birthDeclarationDAO.deleteBirthDeclaration(
                birthDeclarationDAO.getByBDDivisionAndSerialNo(colomboBDDivision, 2010103).getIdUKey());
        } catch (Exception e) {}
        try {
            birthDeclarationDAO.deleteBirthDeclaration(
                birthDeclarationDAO.getByBDDivisionAndSerialNo(colomboBDDivision, 2010104).getIdUKey());
        } catch (Exception e) {}
        try {
            birthDeclarationDAO.deleteBirthDeclaration(
                birthDeclarationDAO.getByBDDivisionAndSerialNo(colomboBDDivision, 2010105).getIdUKey());
        } catch (Exception e) {}
    }

    public BirthRegistrationServiceTest() {

        birthRegSvc = (BirthRegistrationService) ctx.getBean("manageBirthService", BirthRegistrationService.class);
        bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
        userManager = (UserManagerImpl) ctx.getBean("userManagerService", UserManagerImpl.class);

        try {
            deoColomboColombo = userManager.authenticateUser("deo-colombo-colombo", "password");
            adrColomboColombo = userManager.authenticateUser("adr-colombo-colombo", "password");
            deoGampahaNegambo = userManager.authenticateUser("deo-gampaha-negambo", "password");
            adrGampahaNegambo = userManager.authenticateUser("adr-gampaha-negambo", "password");
        } catch (AuthorizationException e) {
            throw new IllegalArgumentException("Cannot authenticate sample users");
        }

        colomboBDDivision = bdDivisionDAO.getBDDivisionByPK(1);
        negamboBDDivision = bdDivisionDAO.getBDDivisionByPK(9);
    }

    public void testAddMinimalBDF() throws Exception {
        Calendar dob = Calendar.getInstance();
        // test saving of a minimal BDF for colombo by DEO
        dob.add(Calendar.DATE, -3);

        // test colombo deo adding for colombo
        BirthDeclaration bdf1 = getMinimalBDF(2010101, dob.getTime(), colomboBDDivision);
        birthRegSvc.addLiveBirthDeclaration(bdf1, false, deoColomboColombo, null, null);

        // reload again to fill all fields as we still only have IDUkey of new record
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);

        // colombo deo cannot approve
        try {
            birthRegSvc.approveLiveBirthDeclaration(bdf1, false, deoColomboColombo);
            fail("DEO cannot approve BDFs");
        } catch (Exception expected) {}
        // negambo deo cannot approve either
        try {
            birthRegSvc.approveLiveBirthDeclaration(bdf1, false, deoGampahaNegambo);
            fail("Negambo DEO cannot approve BDFs of Colombo BD division");
        } catch (Exception expected) {}
        // negambo adr cannot approve either
        try {
            birthRegSvc.approveLiveBirthDeclaration(bdf1, false, adrGampahaNegambo);
            fail("Negambo ADR cannot approve BDFs of Colombo BD division");
        } catch (Exception expected) {}

        // colombo ADR approves - should raise warnings
        List<UserWarning> warnings = birthRegSvc.approveLiveBirthDeclaration(bdf1, false, adrColomboColombo);
        Assert.assertTrue("A minimal BDF must trigger warnings that data is incomplete", !warnings.isEmpty());

        // ignore warnings should allow approval
        warnings = birthRegSvc.approveLiveBirthDeclaration(bdf1, true, adrColomboColombo);

        // check for existence of warning comment
        BirthDeclaration bdfSaved = birthRegSvc.getById(bdf1.getIdUKey(), adrColomboColombo);
        Assert.assertTrue(bdfSaved.getRegister().getComments().contains("ignoring warnings"));
    }

    public void testDuplicateBDF() throws Exception {
        Calendar dob = Calendar.getInstance();
        // test saving of a minimal BDF for colombo by DEO
        dob.add(Calendar.DATE, -3);

        // add another record for the same mother, and check if the duplication warning was issued
        BirthDeclaration bdf1 = getMinimalBDF(2010102, dob.getTime(), colomboBDDivision);
        dob.add(Calendar.DATE, -30*27);
        BirthDeclaration bdf2 = getMinimalBDF(2010103, dob.getTime(), colomboBDDivision);
        dob.add(Calendar.DATE, 2*30*27);
        BirthDeclaration bdf3 = getMinimalBDF(2010104, dob.getTime(), colomboBDDivision);
        dob.add(Calendar.DATE, 30*12);
        BirthDeclaration bdf4 = getMinimalBDF(2010105, dob.getTime(), colomboBDDivision);

        bdf1.getParent().setMotherNICorPIN("755010001V");
        bdf2.getParent().setMotherNICorPIN("755010001V");
        bdf3.getParent().setMotherNICorPIN("755010001V");
        bdf4.getParent().setMotherNICorPIN("755010001V");

        birthRegSvc.addLiveBirthDeclaration(bdf1, false, deoColomboColombo, null, null);
        birthRegSvc.addLiveBirthDeclaration(bdf2, false, deoColomboColombo, null, null);
        birthRegSvc.addLiveBirthDeclaration(bdf3, false, deoColomboColombo, null, null);
        birthRegSvc.addLiveBirthDeclaration(bdf4, false, deoColomboColombo, null, null);

        // reload again to fill all fields as we still only have IDUkey of new record
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        bdf2 = birthRegSvc.getById(bdf2.getIdUKey(), deoColomboColombo);
        bdf3 = birthRegSvc.getById(bdf3.getIdUKey(), deoColomboColombo);
        bdf4 = birthRegSvc.getById(bdf4.getIdUKey(), deoColomboColombo);

        // ignore warnings and approve first
        List<UserWarning> warnings = birthRegSvc.approveLiveBirthDeclaration(bdf1, true, adrColomboColombo);

        // check warnings issued for second
        warnings = birthRegSvc.approveLiveBirthDeclaration(bdf2, false, adrColomboColombo);
        boolean found = false;
        for (UserWarning w : warnings) {
            if (w.getMessage().contains(Long.toString(bdf1.getIdUKey()))) {
                found = true;
                break;
            }
        }
        if (!found) {
            fail("Expected a warning for possible duplicate record for same mother in less within 28 weeks");
        }

        // check warnings issued for third
        warnings = birthRegSvc.approveLiveBirthDeclaration(bdf3, false, adrColomboColombo);
        found = false;
        for (UserWarning w : warnings) {
            if (w.getMessage().contains(Long.toString(bdf1.getIdUKey()))) {
                found = true;
                break;
            }
        }
        if (!found) {
            fail("Expected a warning for possible duplicate record for same mother within 28 weeks");
        }

        // check warnings issued for fourth
        warnings = birthRegSvc.approveLiveBirthDeclaration(bdf4, false, adrColomboColombo);
        found = false;
        for (UserWarning w : warnings) {
            if (w.getMessage().contains(Long.toString(bdf1.getIdUKey()))) {
                found = true;
                break;
            }
        }
        if (found) {
            fail("Did not expect a warning for possible duplicate record after 28 weeks");
        }
    }

    private BirthDeclaration getMinimalBDF(int serial, Date dob, BDDivision bdDivision) {

        Date today = new Date();
        BirthDeclaration bdf = new BirthDeclaration();
        bdf.getRegister().setBdfSerialNo(serial);
        bdf.getRegister().setDateOfRegistration(today);
        bdf.getRegister().setBirthDivision(bdDivision);
        bdf.getChild().setDateOfBirth(dob);
        bdf.getChild().setPlaceOfBirth("Place of birth for child " + serial);
        bdf.getChild().setChildGender(0);

        bdf.getInformant().setInformantName("Name of Informant for Child : " + serial);
        bdf.getInformant().setInformantAddress("Address of Informant for Child : " + serial);
        bdf.getInformant().setInformantSignDate(today);
        bdf.getInformant().setInformantType(InformantInfo.InformantType.FATHER);

        bdf.getNotifyingAuthority().setNotifyingAuthorityAddress("The address of the Birth registrar");
        bdf.getNotifyingAuthority().setNotifyingAuthoritySignDate(today);
        bdf.getNotifyingAuthority().setNotifyingAuthorityName("Name of the Notifying Authority");
        bdf.getNotifyingAuthority().setNotifyingAuthorityPIN(750010001);
        return bdf;
    }
}
