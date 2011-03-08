package lk.rgd.crs.core.service;

import junit.framework.TestCase;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.crs.api.dao.GNDivisionDAO;
import lk.rgd.crs.api.domain.GNDivision;
import org.springframework.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.UnitTestManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.service.DeathRegistrationService;
import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.DeclarantInfo;
import lk.rgd.crs.CRSRuntimeException;

import java.util.Date;
import java.util.Calendar;

/**
 * @author Chathuranga Withana
 * @author amith jayasekara
 */
public class DeathRegistrationServiceTest extends TestCase {

    private static final Logger logger = LoggerFactory.getLogger(DeathRegistrationServiceTest.class);

    protected final ApplicationContext ctx = UnitTestManager.ctx;
    protected final DeathRegistrationService deathRegService;
    protected final BDDivisionDAO bdDivisionDAO;
    protected final GNDivisionDAO gnDivisionDAO;
    protected final UserManager userManager;
    protected final BDDivision colomboBDDivision;
    protected final BDDivision negamboBDDivision;
    protected final GNDivision sammantranapuraGNDivision;
    protected DeathRegisterDAO deathRegisterDAO;
    protected User deoColomboColombo;
    protected User deoGampahaNegambo;
    protected User adrColomboColombo;
    protected User adrGampahaNegambo;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deathRegisterDAO = (DeathRegisterDAO) ctx.getBean("deathRegisterDAOImpl", DeathRegisterDAO.class);
    }

    public DeathRegistrationServiceTest() {
        deathRegService = (DeathRegistrationService) ctx.getBean("deathRegisterService", DeathRegistrationService.class);
        bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
        userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);
        gnDivisionDAO = (GNDivisionDAO) ctx.getBean("gnDivisionDAOImpl", GNDivisionDAO.class);

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
        sammantranapuraGNDivision = gnDivisionDAO.getGNDivisionByPK(1);
    }

    public void testAddMinimalNormalDDF() {
        final DeathRegister.Type deathType = DeathRegister.Type.NORMAL;
        Calendar dob = Calendar.getInstance();
        dob.add(Calendar.DATE, -3);

        // test colombo deo adding for colombo
        DeathRegister ddf1 = getMinimalDDF(2010101001, dob.getTime(), colomboBDDivision,sammantranapuraGNDivision);
        ddf1.setDeathType(deathType);
        deathRegService.addNormalDeathRegistration(ddf1, deoColomboColombo);

        // try adding a duplicate
        DeathRegister ddf2 = getMinimalDDF(2010101001, dob.getTime(), colomboBDDivision,sammantranapuraGNDivision);
        ddf2.setDeathType(deathType);
        try {
            deathRegService.addNormalDeathRegistration(ddf2, deoColomboColombo);
            fail("Should not allow addition of duplicate records");
        } catch (Exception expected) {
        }

        // try adding a late death
        DeathRegister ddf3 = getMinimalDDF(20101010, dob.getTime(), colomboBDDivision,sammantranapuraGNDivision);
        ddf3.setDeathType(DeathRegister.Type.LATE);
        try {
            deathRegService.addNormalDeathRegistration(ddf3, deoColomboColombo);
            fail("Should not allow addition of illegal death type records");
        } catch (CRSRuntimeException expected) {
        }

        // reload again to fill all fields as we still only have IDUkey of new record
        ddf1 = deathRegService.getById(ddf1.getIdUKey(), deoColomboColombo);
        assertEquals(2010101001, ddf1.getDeath().getDeathSerialNo());

        // colombo deo cannot approve
        try {
            deathRegService.approveDeathRegistration(ddf1.getIdUKey(), deoColomboColombo, false);
            fail("DEO cannot approve DDFs");
        } catch (CRSRuntimeException expected) {
        }
        // negambo deo cannot approve either
        try {
            deathRegService.approveDeathRegistration(ddf1.getIdUKey(), deoGampahaNegambo, false);
            fail("Negambo DEO cannot approve DDFs of Colombo BD division");
        } catch (CRSRuntimeException expected) {
        }
        // negambo adr cannot approve either
        try {
            deathRegService.approveDeathRegistration(ddf1.getIdUKey(), adrGampahaNegambo, false);
            fail("Negambo ADR cannot approve DDFs of Colombo BD division");
        } catch (CRSRuntimeException expected) {
        }

        // TODO still implementing Death validater doesnt give much warnings uncomment this section after completing DeathValidater
        /*   // colombo ADR approves - should raise warnings
List<UserWarning> warnings = deathRegService.approveDeathRegistration(ddf1.getIdUKey(), adrColomboColombo, false);
Assert.assertTrue("A minimal DDF must trigger warnings that data is incomplete", !warnings.isEmpty());*/

        // ignore warnings should allow approval
        deathRegService.approveDeathRegistration(ddf1.getIdUKey(), adrColomboColombo, true);

    }

    protected DeathRegister getMinimalDDF(long serial, Date dod, BDDivision deathDivision, GNDivision gnDivision) {

        Date today = new Date();
        DeathRegister ddf = new DeathRegister();
        ddf.getDeath().setDeathSerialNo(serial);
        ddf.getDeath().setDateOfRegistration(today);
        ddf.getDeath().setDeathDivision(deathDivision);
        ddf.getDeath().setGnDivision(gnDivision);
        ddf.getDeath().setDateOfDeath(dod);
        ddf.getDeath().setPlaceOfDeath("Place of death person " + serial);
        ddf.getDeath().setPlaceOfBurial("Place of burial " + serial);

        ddf.getDeathPerson().setDeathPersonGender(0);
        ddf.getDeclarant().setDeclarantType(DeclarantInfo.DeclarantType.RELATIVE);
        ddf.getDeclarant().setDeclarantAddress("declarant address ");
        ddf.getDeclarant().setDeclarantEMail("declarant email");
        ddf.getDeclarant().setDeclarantFullName("declarant full name ");
        ddf.getDeclarant().setDeclarantNICorPIN("" + 123456789);

        ddf.getNotifyingAuthority().setNotifyingAuthorityPIN("750010001");
        ddf.getNotifyingAuthority().setNotifyingAuthorityName("Name of the Notifying Authority");
        ddf.getNotifyingAuthority().setNotifyingAuthorityAddress("Address of the Death registrar");
        ddf.getNotifyingAuthority().setNotifyingAuthoritySignDate(today);
        return ddf;
    }
}
