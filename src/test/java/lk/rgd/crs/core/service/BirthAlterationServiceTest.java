package lk.rgd.crs.core.service;

import junit.framework.Assert;
import junit.framework.TestCase;
import lk.rgd.UnitTestManager;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.BirthAlterationService;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.springframework.context.ApplicationContext;

import java.util.*;

/**
 * @author asankha
 */
public class BirthAlterationServiceTest extends TestCase {

    protected final ApplicationContext ctx = UnitTestManager.ctx;
    protected final BirthRegistrationService birthRegSvc;
    protected final BirthAlterationService   birthAltSvc;
    protected final PopulationRegistry eCivil;
    protected final BDDivisionDAO bdDivisionDAO;
    protected final CountryDAO countryDAO;
    protected final RaceDAO raceDAO;
    protected final UserManager userManager;
    protected final BDDivision colomboBDDivision;
    protected final BDDivision negamboBDDivision;
    protected BirthDeclarationDAO birthDeclarationDAO;
    protected User deoColomboColombo;
    protected User deoGampahaNegambo;
    protected User adrColomboColombo;
    protected User adrGampahaNegambo;
    protected User argNorthWesternProvince;
    protected User argWesternProvince;

    public BirthAlterationServiceTest() {

        birthDeclarationDAO = (BirthDeclarationDAO)
            ctx.getBean("birthDeclarationDAOImpl", BirthDeclarationDAO.class);
        birthRegSvc = (BirthRegistrationService) ctx.getBean("manageBirthService", BirthRegistrationService.class);
        birthAltSvc = (BirthAlterationService) ctx.getBean("birthAlterationService", BirthAlterationService.class);
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
        // register birth in colombo
        long bdfIdUKey = registerBirth();

        // submit an alteration in Gampaha - added by adr
        BirthAlteration ba = new BirthAlteration();
        Alteration27 alt27 = new Alteration27();
        alt27.setChildFullNameEnglish("X NEW NAME OF CHILD");
        alt27.setChildFullNameOfficialLang("X ළමයාගේ අලුත්  නම");
        ba.setAlt27(alt27);
        ba.getDeclarant().setDeclarantFullName("Declarant name");
        ba.getDeclarant().setDeclarantAddress("Declarant address");
       ba.getDeclarant().setDeclarantType(DeclarantInfo.DeclarantType.FATHER);
        ba.setBirthRecordDivision(colomboBDDivision);
        ba.setDateReceived(new Date());
        ba.setBdfIDUKey(bdfIdUKey);
        ba.setType(BirthAlteration.AlterationType.TYPE_27);
        ba.setStatus(BirthAlteration.State.DATA_ENTRY);
        birthAltSvc.addBirthAlteration(ba, deoGampahaNegambo);

        // adr Gampaha can edit it, as he is in the same submit location
        // reload record
        ba = birthAltSvc.getByIDUKey(ba.getIdUKey(), adrGampahaNegambo);
        ba.getAlt27().setChildFullNameEnglish("NEW NAME OF CHILD");
        birthAltSvc.updateBirthAlteration(ba, adrGampahaNegambo);

        // adr Colombo can edit it, as he is assigned access to the BD division where the birth is registered
        // reload record
        ba = birthAltSvc.getByIDUKey(ba.getIdUKey(), adrColomboColombo);
        ba.getAlt27().setChildFullNameOfficialLang("ළමයාගේ අලුත්  නම");
        birthAltSvc.updateBirthAlteration(ba, adrColomboColombo);

        Map<Integer, Boolean> fieldsToBeApproved = new HashMap<Integer, Boolean>();
        fieldsToBeApproved.put(Alteration27.CHILD_FULL_NAME_ENGLISH, true);
        fieldsToBeApproved.put(Alteration27.CHILD_FULL_NAME_OFFICIAL_LANG, false);

        // arg for north western province cannot edit
        // reload record
        ba = birthAltSvc.getByIDUKey(ba.getIdUKey(), argNorthWesternProvince);
        try {
            birthAltSvc.approveBirthAlteration(ba, fieldsToBeApproved, argNorthWesternProvince);
            fail("The north western province ARG should not be able to approve birth alteration for western province");
        } catch (Exception e) {}

        // arg for western province can approve
        birthAltSvc.approveBirthAlteration(ba, fieldsToBeApproved, argWesternProvince);

        // save BDF serial number
        long regNumber = birthDeclarationDAO.getById(ba.getBdfIDUKey()).getRegister().getBdfSerialNo();
        
        // birth record must be updated
        BirthDeclaration bdf = birthRegSvc.getActiveRecordByBDDivisionAndSerialNo(
            ba.getBirthRecordDivision(), regNumber, argWesternProvince);
        Assert.assertEquals("NEW NAME OF CHILD", bdf.getChild().getChildFullNameEnglish());
        Assert.assertEquals("සිංහලෙන් ළමයාගේ නම  " + regNumber, bdf.getChild().getChildFullNameOfficialLang());
        Assert.assertEquals(BirthDeclaration.State.ARCHIVED_CERT_GENERATED, bdf.getRegister().getStatus());

        // old record must still be available as archived
        List<BirthDeclaration> altered = birthRegSvc.getArchivedCorrectedEntriesForGivenSerialNo(
            ba.getBirthRecordDivision(), regNumber, argWesternProvince);
        Assert.assertEquals(1, altered.size());
        bdf = altered.get(0);
        Assert.assertEquals("CHILDS NAME IN ENGLISH " + regNumber, bdf.getChild().getChildFullNameEnglish());
        Assert.assertEquals("සිංහලෙන් ළමයාගේ නම  " + regNumber, bdf.getChild().getChildFullNameOfficialLang());
        Assert.assertEquals(BirthDeclaration.State.ARCHIVED_ALTERED, bdf.getRegister().getStatus());
    }

    public long registerBirth() throws Exception {
        Calendar dob = Calendar.getInstance();
        // test saving of a minimal BDF for colombo by DEO
        dob.add(Calendar.DATE, -3);

        // test colombo deo adding for colombo BD division
        BirthDeclaration bdf1 = getMinimalBDF(2010011010, dob.getTime(), colomboBDDivision);
        birthRegSvc.addLiveBirthDeclaration(bdf1, false, deoColomboColombo);

        // reload again to fill all fields as we still only have IDUkey of new record
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);

        // colombo ADR approves - ignoring warnings
        List<UserWarning> warnings = birthRegSvc.approveLiveBirthDeclaration(bdf1.getIdUKey(), true, adrColomboColombo);

        // DEO prints confirmation - mark confirmation as printed
        birthRegSvc.markLiveBirthConfirmationAsPrinted(bdf1, deoColomboColombo);
        // reload again and check for updated status as printed
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.CONFIRMATION_PRINTED, bdf1.getRegister().getStatus());

        // capture confirmation by DEO without changes
        bdf1.getConfirmant().setConfirmantFullName("Person confirming");
        birthRegSvc.markLiveBirthDeclarationAsConfirmedWithoutChanges(bdf1, deoColomboColombo);

        // reload again and check for update
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);

        // DEO prints BC - mark BC as printed
        birthRegSvc.markLiveBirthCertificateAsPrinted(bdf1, deoColomboColombo);
        // reload again and check for update
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.ARCHIVED_CERT_PRINTED, bdf1.getRegister().getStatus());

        return bdf1.getIdUKey();
    }

    protected BirthDeclaration getMinimalBDF(long serial, Date dob, BDDivision bdDivision) {

        Date today = new Date();
        BirthDeclaration bdf = new BirthDeclaration();
        bdf.getRegister().setBdfSerialNo(serial);
        bdf.getRegister().setDateOfRegistration(today);
        bdf.getRegister().setBirthDivision(bdDivision);
        bdf.getChild().setDateOfBirth(dob);
        bdf.getChild().setPlaceOfBirth("Place of birth for child " + serial);
        bdf.getChild().setChildGender(0);
        bdf.getChild().setChildFullNameOfficialLang("සිංහලෙන් ළමයාගේ නම  " + serial);
        bdf.getChild().setChildFullNameEnglish("CHILDS NAME IN ENGLISH " + serial);

        bdf.getInformant().setInformantName("Name of Informant for Child : " + serial);
        bdf.getInformant().setInformantAddress("Address of Informant for Child : " + serial);
        bdf.getInformant().setInformantSignDate(today);
        bdf.getInformant().setInformantType(InformantInfo.InformantType.FATHER);

        bdf.getNotifyingAuthority().setNotifyingAuthorityAddress("The address of the Birth registrar");
        bdf.getNotifyingAuthority().setNotifyingAuthoritySignDate(today);
        bdf.getNotifyingAuthority().setNotifyingAuthorityName("Name of the Notifying Authority");
        bdf.getNotifyingAuthority().setNotifyingAuthorityPIN("750010001");
        return bdf;
    }
}
