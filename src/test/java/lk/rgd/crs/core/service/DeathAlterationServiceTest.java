package lk.rgd.crs.core.service;

import junit.framework.TestCase;
import lk.rgd.UnitTestManager;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.DeathAlterationService;
import lk.rgd.crs.api.service.DeathRegistrationService;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.springframework.context.ApplicationContext;

import java.util.*;

/**
 * @author asankha
 */
public class DeathAlterationServiceTest extends TestCase {

    protected final ApplicationContext ctx = UnitTestManager.ctx;
    protected final DeathRegistrationService deathRegSvc;
    protected final DeathAlterationService deathAltSvc;
    protected final PopulationRegistry eCivil;
    protected final BDDivisionDAO bdDivisionDAO;
    protected final CountryDAO countryDAO;
    protected final RaceDAO raceDAO;
    protected final UserManager userManager;
    protected final BDDivision colomboBDDivision;
    protected final BDDivision negamboBDDivision;
    protected DeathRegisterDAO deathRegisterDAO;
    protected User deoColomboColombo;
    protected User deoGampahaNegambo;
    protected User adrColomboColombo;
    protected User adrGampahaNegambo;
    protected User argNorthWesternProvince;
    protected User argWesternProvince;

    public DeathAlterationServiceTest() {

        deathRegisterDAO = (DeathRegisterDAO) ctx.getBean("deathRegisterDAOImpl", DeathRegisterDAO.class);
        deathRegSvc = (DeathRegistrationService) ctx.getBean("manageDeathService", DeathRegistrationService.class);
        deathAltSvc = (DeathAlterationService) ctx.getBean("deathAlterationService", DeathAlterationService.class);
        eCivil = (PopulationRegistry) ctx.getBean("ecivilService", PopulationRegistry.class);
        bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
        countryDAO = (CountryDAO) ctx.getBean("countryDAOImpl", CountryDAO.class);
        raceDAO = (RaceDAO) ctx.getBean("raceDAOImpl", RaceDAO.class);
        userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);

        try {
            deoColomboColombo = userManager.authenticateUser("deo-colombo-colombo", "password");
            adrColomboColombo = userManager.authenticateUser("adr-colombo-colombo", "password");
            deoGampahaNegambo = userManager.authenticateUser("deo-gampaha-negambo", "password");
            adrGampahaNegambo = userManager.authenticateUser("adr-gampaha-negambo", "password");
            argNorthWesternProvince = userManager.authenticateUser("arg-north-western", "password");
            argWesternProvince      = userManager.authenticateUser("arg-western", "password");
        } catch (AuthorizationException e) {
            throw new IllegalArgumentException("Cannot authenticate sample users");
        }

        colomboBDDivision = bdDivisionDAO.getBDDivisionByPK(1);
        negamboBDDivision = bdDivisionDAO.getBDDivisionByPK(9);
    }

    public void testAlterations() throws Exception {
        // register death in colombo
        long bdfIdUKey = registerDeath();

        // submit an alteration in Gampaha - added by adr
        DeathAlteration ba = new DeathAlteration();
        /*Alteration27 alt27 = new Alteration27();
        alt27.setChildFullNameEnglish("X NEW NAME OF CHILD");
        alt27.setChildFullNameOfficialLang("X ළමයාගේ අලුත්  නම");
        ba.setAlt27(alt27);
        ba.getDeclarant().setDeclarantFullName("Declarant name");
        ba.getDeclarant().setDeclarantAddress("Declarant address");
        ba.getDeclarant().setDeclarantType(DeclarantInfo.DeclarantType.FATHER);
        ba.setDeathRecordDivision(negamboBDDivision);
        ba.setDateReceived(new Date());
        ba.setBdfIDUKey(bdfIdUKey);
        ba.setType(DeathAlteration.AlterationType.TYPE_27);
        ba.setStatus(DeathAlteration.State.DATA_ENTRY);
        deathAltSvc.addDeathAlteration(ba, deoGampahaNegambo);

        // adr Gampaha can edit it, as he is in the same submit location
        // reload record
        ba = deathAltSvc.getByIDUKey(ba.getIdUKey(), adrGampahaNegambo);
        ba.getAlt27().setChildFullNameEnglish("NEW NAME OF CHILD");
        deathAltSvc.updateDeathAlteration(ba, adrGampahaNegambo);

        // adr Colombo can edit it, as he is assigned access to the BD division where the death is registered
        // reload record
        ba = deathAltSvc.getByIDUKey(ba.getIdUKey(), adrColomboColombo);
        ba.getAlt27().setChildFullNameOfficialLang("ළමයාගේ අලුත්  නම");
        deathAltSvc.updateDeathAlteration(ba, adrColomboColombo);

        Map<Integer, Boolean> fieldsToBeApproved = new HashMap<Integer, Boolean>();
        fieldsToBeApproved.put(Alteration27.CHILD_FULL_NAME_ENGLISH, true);
        fieldsToBeApproved.put(Alteration27.CHILD_FULL_NAME_OFFICIAL_LANG, false);

        // arg for north western province cannot edit
        // reload record
        ba = deathAltSvc.getByIDUKey(ba.getIdUKey(), argNorthWesternProvince);
        try {
            deathAltSvc.approveDeathAlteration(ba, fieldsToBeApproved, true, argNorthWesternProvince);
            fail("The north western province ARG should not be able to approve death alteration for western province");
        } catch (Exception e) {}

        // arg for western province can approve
        deathAltSvc.approveDeathAlteration(ba, fieldsToBeApproved, true, argWesternProvince);

        // death record must be updated
        //DeathDeclaration bdf = deathRegSvc.getActiveRecordByBDDivisionAndSerialNo(
        //    ba.get, ba.getBdfIDUKey(), argWesternProvince);
*/
    }

    public long registerDeath() throws Exception {
        /*Calendar dob = Calendar.getInstance();
        // test saving of a minimal BDF for colombo by DEO
        dob.add(Calendar.DATE, -3);

        // test colombo deo adding for colombo BD division
        DeathDeclaration bdf1 = getMinimalBDF(2010011010, dob.getTime(), colomboBDDivision);
        deathRegSvc.addLiveDeathDeclaration(bdf1, false, deoColomboColombo);

        // reload again to fill all fields as we still only have IDUkey of new record
        bdf1 = deathRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);

        // colombo ADR approves - ignoring warnings
        List<UserWarning> warnings = deathRegSvc.approveLiveDeathDeclaration(bdf1.getIdUKey(), true, adrColomboColombo);

        // DEO prints confirmation - mark confirmation as printed
        deathRegSvc.markLiveDeathConfirmationAsPrinted(bdf1, deoColomboColombo);
        // reload again and check for updated status as printed
        bdf1 = deathRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(DeathDeclaration.State.CONFIRMATION_PRINTED, bdf1.getRegister().getStatus());

        // capture confirmation by DEO without changes
        bdf1.getConfirmant().setConfirmantFullName("Person confirming");
        deathRegSvc.markLiveDeathDeclarationAsConfirmedWithoutChanges(bdf1, deoColomboColombo);

        // reload again and check for update
        bdf1 = deathRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);

        // DEO prints BC - mark BC as printed
        deathRegSvc.markLiveDeathCertificateAsPrinted(bdf1, deoColomboColombo);
        // reload again and check for update
        bdf1 = deathRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(DeathDeclaration.State.ARCHIVED_CERT_PRINTED, bdf1.getRegister().getStatus());

        return bdf1.getIdUKey();*/
        return 1;
    }

    protected DeathRegister getMinimalDR(long serial, Date dob, BDDivision bdDivision) {

        Date today = new Date();
        DeathRegister dr = new DeathRegister();
        /*bdf.getRegister().setBdfSerialNo(serial);
        bdf.getRegister().setDateOfRegistration(today);
        bdf.getRegister().setDeathDivision(bdDivision);
        bdf.getChild().setDateOfDeath(dob);
        bdf.getChild().setPlaceOfDeath("Place of death for child " + serial);
        bdf.getChild().setChildGender(0);
        bdf.getChild().setChildFullNameOfficialLang("සිංහලෙන් ළමයාගේ නම  " + serial);

        bdf.getInformant().setInformantName("Name of Informant for Child : " + serial);
        bdf.getInformant().setInformantAddress("Address of Informant for Child : " + serial);
        bdf.getInformant().setInformantSignDate(today);
        bdf.getInformant().setInformantType(InformantInfo.InformantType.FATHER);

        bdf.getNotifyingAuthority().setNotifyingAuthorityAddress("The address of the Death registrar");
        bdf.getNotifyingAuthority().setNotifyingAuthoritySignDate(today);
        bdf.getNotifyingAuthority().setNotifyingAuthorityName("Name of the Notifying Authority");
        bdf.getNotifyingAuthority().setNotifyingAuthorityPIN("750010001");*/
        return dr;
    }
}
