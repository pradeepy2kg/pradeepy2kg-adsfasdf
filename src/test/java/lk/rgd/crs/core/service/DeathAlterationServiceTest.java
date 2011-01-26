package lk.rgd.crs.core.service;

import junit.framework.Assert;
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
        deathRegSvc = (DeathRegistrationService) ctx.getBean("deathRegisterService", DeathRegistrationService.class);
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
            argWesternProvince = userManager.authenticateUser("arg-western", "password");
        } catch (AuthorizationException e) {
            throw new IllegalArgumentException("Cannot authenticate sample users");
        }

        colomboBDDivision = bdDivisionDAO.getBDDivisionByPK(1);
        negamboBDDivision = bdDivisionDAO.getBDDivisionByPK(9);
    }

    public void testAlterations() throws Exception {
        // register death in colombo
        long drIdUKey = registerDeath(2010020100);

        // submit an alteration in Gampaha - added by adr
        DeathAlteration da = new DeathAlteration();
        da.getDeathPerson().setDeathPersonNameInEnglish("X NEW NAME OF PERSON DEPARTED");
        da.getDeathPerson().setDeathPersonNameOfficialLang("X මරණයට පත් මිනිසාගේ අලුත් නම");
        da.getDeclarant().setDeclarantFullName("Declarant name");
        da.getDeclarant().setDeclarantAddress("Declarant address");
        da.getDeclarant().setDeclarantType(DeclarantInfo.DeclarantType.SON_OR_DAUGHTER);
        da.setDeathRecordDivision(colomboBDDivision);
        da.setDateReceived(new Date());
        da.setDeathRegisterIDUkey(drIdUKey);
        da.setType(DeathAlteration.AlterationType.TYPE_52_1_H);
        da.setStatus(DeathAlteration.State.DATA_ENTRY);
        deathAltSvc.addDeathAlteration(da, deoGampahaNegambo);

        // adr Gampaha can edit it, as he is in the same submit location
        // reload record
        da = deathAltSvc.getByIDUKey(da.getIdUKey(), adrGampahaNegambo);
        da.getDeathPerson().setDeathPersonNameInEnglish("NEW NAME OF PERSON DEPARTED");
        deathAltSvc.updateDeathAlteration(da, adrGampahaNegambo);

        // adr Colombo can edit it, as he is assigned access to the BD division where the death is registered
        // reload record
        da = deathAltSvc.getByIDUKey(da.getIdUKey(), adrColomboColombo);
        da.getDeathPerson().setDeathPersonNameOfficialLang(" මරණයට පත් මිනිසාගේ අලුත් නම");
        deathAltSvc.updateDeathAlteration(da, adrColomboColombo);

        Map<Integer, Boolean> fieldsToBeApproved = new HashMap<Integer, Boolean>();
        fieldsToBeApproved.put(DeathAlteration.NAME, true);
        fieldsToBeApproved.put(DeathAlteration.NAME_ENGLISH, false);

        // arg for north western province cannot edit
        // reload record
        da = deathAltSvc.getByIDUKey(da.getIdUKey(), argNorthWesternProvince);
        try {
            deathAltSvc.approveDeathAlteration(da, fieldsToBeApproved, argNorthWesternProvince);
            fail("The north western province ARG should not be able to approve death alteration for western province");
        } catch (Exception e) {
        }

        // arg for western province can approve
        deathAltSvc.approveDeathAlteration(da, fieldsToBeApproved, argWesternProvince);

        long serialNumber = deathRegisterDAO.getById(da.getDeathRegisterIDUkey()).getDeath().getDeathSerialNo();

        // death record must be updated
        DeathRegister ddf = deathRegSvc.getActiveRecordByBDDivisionAndSerialNo(
                da.getDeathRecordDivision(), serialNumber, argWesternProvince);

        Assert.assertNotNull(ddf);
        Assert.assertEquals("DEAD PERSON ORIGINAL NAME", ddf.getDeathPerson().getDeathPersonNameInEnglish());
        Assert.assertEquals("මරණයට පත් මිනිසාගේ අලුත් නම", ddf.getDeathPerson().getDeathPersonNameOfficialLang());
        Assert.assertEquals(DeathRegister.State.ARCHIVED_CERT_GENERATED, ddf.getStatus());

        // old record must be archived
        // old record must still be available as archived

        List<DeathRegister> altered = deathRegSvc.getArchivedCorrectedEntriesForGivenSerialNo(
                da.getDeathRecordDivision(), serialNumber, ddf.getIdUKey(), argWesternProvince);
        Assert.assertEquals(1, altered.size());
        ddf = altered.get(0);
        Assert.assertEquals("DEAD PERSON ORIGINAL NAME", ddf.getDeathPerson().getDeathPersonNameInEnglish());
        Assert.assertEquals("මරණයට පත් පුද්ගලයාගේ නම", ddf.getDeathPerson().getDeathPersonNameOfficialLang());
        Assert.assertEquals(DeathRegister.State.ARCHIVED_ALTERED, ddf.getStatus());
    }

    private long registerDeath(long serial) {

        final DeathRegister.Type deathType = DeathRegister.Type.NORMAL;
        Calendar dod = Calendar.getInstance();
        dod.add(Calendar.DATE, -3);

        DeathRegister ddf = getMinimalDDF(serial, dod.getTime(), colomboBDDivision);
        ddf.setDeathType(deathType);
        deathRegSvc.addNormalDeathRegistration(ddf, deoColomboColombo);
        deathRegSvc.approveDeathRegistration(ddf.getIdUKey(), adrColomboColombo, true);
        return ddf.getIdUKey();
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
        ddf.getDeathPerson().setDeathPersonNameInEnglish("DEAD PERSON ORIGINAL NAME");
        ddf.getDeathPerson().setDeathPersonNameOfficialLang("මරණයට පත් පුද්ගලයාගේ නම");

        ddf.getDeathPerson().setDeathPersonGender(0);
        ddf.getDeclarant().setDeclarantType(DeclarantInfo.DeclarantType.RELATIVE);
        ddf.getDeclarant().setDeclarantAddress("declarant address ");
        ddf.getDeclarant().setDeclarantEMail("declarant email");
        ddf.getDeclarant().setDeclarantFullName("declarant full name ");

        ddf.getNotifyingAuthority().setNotifyingAuthorityPIN("750010001");
        ddf.getNotifyingAuthority().setNotifyingAuthorityName("Name of the Notifying Authority");
        ddf.getNotifyingAuthority().setNotifyingAuthorityAddress("Address of the Death registrar");
        ddf.getNotifyingAuthority().setNotifyingAuthoritySignDate(today);
        return ddf;
    }
}
