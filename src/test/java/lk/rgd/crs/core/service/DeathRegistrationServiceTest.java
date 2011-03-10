package lk.rgd.crs.core.service;

import junit.framework.TestCase;
import lk.rgd.ErrorCodes;
import lk.rgd.common.RGDRuntimeException;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.crs.api.bean.UserWarning;
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
import java.util.List;

/**
 * @author amith jayasekara
 * @author Chathuranga Withana
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
        DeathRegister ddf1 = getMinimalDDF(2010101001, dob.getTime(), colomboBDDivision, sammantranapuraGNDivision);
        ddf1.setDeathType(deathType);
        deathRegService.addNewDeathRegistration(ddf1, deoColomboColombo);

        // try adding a duplicate
        DeathRegister ddf2 = getMinimalDDF(2010101001, dob.getTime(), colomboBDDivision, sammantranapuraGNDivision);
        ddf2.setDeathType(deathType);
        try {
            deathRegService.addNewDeathRegistration(ddf2, deoColomboColombo);
            fail("Should not allow addition of duplicate records");
        } catch (Exception expected) {
        }

        // try adding a late death
        DeathRegister ddf3 = getMinimalDDF(20101010, dob.getTime(), colomboBDDivision, sammantranapuraGNDivision);
        ddf3.setDeathType(DeathRegister.Type.LATE);
        try {
            deathRegService.addNewDeathRegistration(ddf3, deoColomboColombo);
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

    public void testAddDeathRegistration() {
        logger.debug("attempt to test death registration");
        DeathRegister normalDeathRegistration = getMinimalDDF(2011012345l, new Date(), colomboBDDivision, sammantranapuraGNDivision);
        normalDeathRegistration.setDeathType(DeathRegister.Type.NORMAL);
        // this is death happens at colombo ds division
        // we add this record as deo-gampaha-negombo  and this user does not have permission to add this death and expecting exception
        try {
            deathRegService.addNewDeathRegistration(normalDeathRegistration, deoGampahaNegambo);
        }
        catch (RGDRuntimeException e) {
            //we expecting permission deny for that even
            switch (e.getErrorCode()) {
                case ErrorCodes.PERMISSION_DENIED:
                    // we only expecting permission deny exception
                    logger.debug("permission deny for user : {}  while adding death registration happens on BDdivision :" +
                        " {}", deoGampahaNegambo.getUserId(), normalDeathRegistration.getDeath().getDeathDivision());
                    break;
                default:
                    fail("we only expecting permission deny exception");
            }
        }
        //now deo-colombo-colombo try to add same registration and he must not get any exception if that user is active
        try {
            deathRegService.addNewDeathRegistration(normalDeathRegistration, deoColomboColombo);
            logger.debug("successfully tested adding death registration happens at colombo bd division and is" +
                " being added by deo-colmbo-colombo");
        }
        catch (RGDRuntimeException e) {
            // we are not expecting any exceptions while adding this record by this user
            fail("not expecting any exception while adding death registration happen is colombo bd by deo-colombo-colombo");
        }

        //now we successfully added the registration and now check for serial number duplication
        DeathRegister normalDRWithDuplicateSerial = getMinimalDDF(2011012345l, new Date(), colomboBDDivision, sammantranapuraGNDivision);
        normalDRWithDuplicateSerial.setDeathType(DeathRegister.Type.NORMAL);
        //expecting exception while adding this record because we are having same serial number for the same BD division
        try {
            deathRegService.addNewDeathRegistration(normalDRWithDuplicateSerial, deoColomboColombo);
            //if it is success it is an error
            fail("expecting a exception while adding duplicate serial number for same bd division");
        }
        catch (RGDRuntimeException e) {
            logger.debug("exception expected while adding duplicate serial number for same bd division");
        }

    }

    public void testDeathRegistrationApprove() {
        //adding a new death registration
        DeathRegister ddr = getMinimalDDF(2011145678l, new Date(), colomboBDDivision, sammantranapuraGNDivision);
        ddr.setDeathType(DeathRegister.Type.NORMAL);
        deathRegService.addNewDeathRegistration(ddr, deoColomboColombo);
        ddr = deathRegService.getByBDDivisionAndDeathSerialNo(colomboBDDivision, 2011145678l, deoColomboColombo);
        //deo colombo colombo must not be able to approve the death registration
        try {
            deathRegService.approveDeathRegistration(ddr.getIdUKey(), deoColomboColombo, true);
            fail("expecting a exception while approving a normal death registration by deo role");
        } catch (RGDRuntimeException e) {
            switch (e.getErrorCode()) {
                case ErrorCodes.PERMISSION_DENIED:
                    logger.debug("got an expected exception while approving death registration by deo user ");
                    break;
                default:
                    fail("only expected permission deny exception");
            }
        }
        // now we try to approve this record with a ADR but that ADR does not have permission to BDDivision
        try {
            deathRegService.approveDeathRegistration(ddr.getIdUKey(), adrGampahaNegambo, true);
            fail("expecting exception while approving death registration by a ADR user who does mot have permission for the BD division");
        }
        catch (RGDRuntimeException e) {
            switch (e.getErrorCode()) {
                case ErrorCodes.PERMISSION_DENIED:
                    logger.debug("Got expected exception while approving by ADR user who does not have permission for the BD division");
                    break;
                default:
                    fail("only expected permission deny exception");
            }
        }
        try {
            //testing warnings while approving death registration
            ddr.getDeathPerson().setDeathPersonFatherPINorNIC("862583692V");
            ddr.getDeathPerson().setDeathPersonMotherPINorNIC("862583692V");
            deathRegService.updateDeathRegistration(ddr, adrColomboColombo);
            List<UserWarning> userWarnings = deathRegService.approveDeathRegistration(ddr.getIdUKey(), adrColomboColombo, false);
            //expecting user 1 warning
            if (!userWarnings.isEmpty() && userWarnings.size() == 1) {
                logger.debug("got expected list of user warnings while approving death registration");
            } else {
                fail("expecting user warning for approving the death register record");
            }

            //try to approve and this time we are not expecting any exceptions
            deathRegService.approveDeathRegistration(ddr.getIdUKey(), adrColomboColombo, true);
            logger.debug("successfully approved death registration by adr-colombo-colombo user");
        }
        catch (RGDRuntimeException e) {
            fail("not expecting any kind of exception while approving death register");
        }
    }

    protected DeathRegister getMinimalDDF(long serial, Date dod, BDDivision deathDivision, GNDivision gnDivision) {

        Date today = new Date();
        DeathRegister ddf = new DeathRegister();
        ddf.getDeath().setDeathSerialNo(serial);
        ddf.getDeath().setDateOfRegistration(today);
        ddf.getDeath().setDeathDivision(deathDivision);
        ddf.getDeath().setDateOfDeath(dod);
        ddf.getDeath().setPlaceOfDeath("Place of death person " + serial);
        ddf.getDeath().setPlaceOfBurial("Place of burial " + serial);

        ddf.getDeathPerson().setDeathPersonGender(0);
        ddf.getDeclarant().setDeclarantType(DeclarantInfo.DeclarantType.RELATIVE);
        ddf.getDeclarant().setDeclarantAddress("declarant address ");
        ddf.getDeclarant().setDeclarantEMail("declarant email");
        ddf.getDeclarant().setDeclarantFullName("declarant full name ");
        ddf.getDeclarant().setDeclarantNICorPIN("" + 123456789);
        ddf.getDeathPerson().setGnDivision(gnDivision);

        ddf.getNotifyingAuthority().setNotifyingAuthorityPIN("750010001");
        ddf.getNotifyingAuthority().setNotifyingAuthorityName("Name of the Notifying Authority");
        ddf.getNotifyingAuthority().setNotifyingAuthorityAddress("Address of the Death registrar");
        ddf.getNotifyingAuthority().setNotifyingAuthoritySignDate(today);
        return ddf;
    }
}
