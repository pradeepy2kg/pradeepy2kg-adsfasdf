package lk.rgd.crs.core.service;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import lk.rgd.UnitTestManager;
import lk.rgd.common.core.service.UserManagerImpl;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.service.DeathRegistrationService;
import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.DeclarantInfo;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.CRSRuntimeException;

import java.util.Date;
import java.util.Calendar;
import java.util.List;

/**
 * @author Chathuranga Withana
 */
public class DeathRegistrationServiceTest extends TestCase {

    protected final ApplicationContext ctx = UnitTestManager.ctx;
    protected final DeathRegistrationService deathRegService;
    protected final BDDivisionDAO bdDivisionDAO;
    protected final UserManagerImpl userManager;
    protected final BDDivision colomboBDDivision;
    protected final BDDivision negamboBDDivision;
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

    public void testAddMinimalNormalDDF() {
        final DeathRegister.Type deathType = DeathRegister.Type.NORMAL;
        Calendar dob = Calendar.getInstance();
        dob.add(Calendar.DATE, -3);

        // test colombo deo adding for colombo
        DeathRegister ddf1 = getMinimalDDF(20101010, dob.getTime(), colomboBDDivision);
        ddf1.setDeathType(deathType);
        deathRegService.addNormalDeathRegistration(ddf1, deoColomboColombo);

        // try adding a duplicate
        DeathRegister ddf2 = getMinimalDDF(20101010, dob.getTime(), colomboBDDivision);
        ddf2.setDeathType(deathType);
        try {
            deathRegService.addNormalDeathRegistration(ddf2, deoColomboColombo);
            fail("Should not allow addition of duplicate records");
        } catch (Exception expected) {
        }

        // try adding a late death
        DeathRegister ddf3 = getMinimalDDF(20101010, dob.getTime(), colomboBDDivision);
        ddf3.setDeathType(DeathRegister.Type.LATE);
        try {
            deathRegService.addNormalDeathRegistration(ddf3, deoColomboColombo);
            fail("Should not allow addition of illegal death type records");
        } catch (CRSRuntimeException expected) {
        }

        // reload again to fill all fields as we still only have IDUkey of new record
        ddf1 = deathRegService.getById(ddf1.getIdUKey(), deoColomboColombo);
        assertEquals(20101010, ddf1.getDeath().getDeathSerialNo());

        // colombo deo cannot approve
        try {
            deathRegService.approveDeathRegistration(ddf1.getIdUKey(), deoColomboColombo);
            fail("DEO cannot approve DDFs");
        } catch (CRSRuntimeException expected) {
        }
        // negambo deo cannot approve either
        try {
            deathRegService.approveDeathRegistration(ddf1.getIdUKey(), deoGampahaNegambo);
            fail("Negambo DEO cannot approve DDFs of Colombo BD division");
        } catch (CRSRuntimeException expected) {
        }
        // negambo adr cannot approve either
        try {
            deathRegService.approveDeathRegistration(ddf1.getIdUKey(), adrGampahaNegambo);
            fail("Negambo ADR cannot approve DDFs of Colombo BD division");
        } catch (CRSRuntimeException expected) {
        }

        // TODO still implementing
        // colombo ADR approves - should raise warnings
//        deathRegService.approveDeathRegistration(ddf1.getIdUKey(), adrColomboColombo);

    }

    protected DeathRegister getMinimalDDF(long serial, Date dod, BDDivision deathDivision) {

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

        ddf.getNotifyingAuthority().setNotifyingAuthorityPIN("750010001");
        ddf.getNotifyingAuthority().setNotifyingAuthorityName("Name of the Notifying Authority");
        ddf.getNotifyingAuthority().setNotifyingAuthorityAddress("Address of the Death registrar");
        ddf.getNotifyingAuthority().setNotifyingAuthoritySignDate(today);
        return ddf;
    }
}
