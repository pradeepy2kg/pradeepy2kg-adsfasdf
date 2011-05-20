package lk.rgd.crs.core.service;

import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.domain.InformantInfo;
import lk.rgd.crs.api.service.BirthRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Chathuranga Withana
 */
public class DeathRegistrationServiceIntegrationTest extends DeathRegistrationServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(DeathRegistrationServiceIntegrationTest.class);

    protected final BirthRegistrationService birthRegSvc;

    public DeathRegistrationServiceIntegrationTest() {
        birthRegSvc = (BirthRegistrationService) ctx.getBean("manageBirthService", BirthRegistrationService.class);
    }

    /**
     * Test death declarations of infants(children death less than 30 days) not registered in birth registration
     */
    public void testInfantDeathRegisterWithoutBirthDeclaration() {
        logger.debug("Attempt to test infant death declarations whose birth declarations does not exist");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        Date dod = new Date();

        // death declaration of infant whose birth declaration not captured by the system and dob, mother PIN/NIC known
        DeathRegister dr = getMinimalDDF(2011078946l, dod, colomboBDDivision, sammantranapuraGNDivision);
        dr.setDeathType(DeathRegister.Type.NORMAL);
        cal.add(Calendar.DATE, -20);
        // death person dob and mother PIN/NIC known
        dr.getDeathPerson().setDeathPersonDOB(cal.getTime());
        dr.getDeathPerson().setDeathPersonMotherPINorNIC("661584521V");
        deathRegService.addNewDeathRegistration(dr, adrColomboColombo);
        long idUKey = dr.getIdUKey();

        DeathRegister existing = deathRegService.getById(idUKey);
        assertTrue("Should be an infant death declaration", existing.getDeath().isInfantLessThan30Days());
        List<UserWarning> warnings = deathRegService.approveDeathRegistration(idUKey, adrColomboColombo, false);
        assertEquals("Infant birth not declared in the system", 1, warnings.size());
        // update existing death declaration
        cal.add(Calendar.DATE, -15);
        dr.getDeathPerson().setDeathPersonDOB(cal.getTime());
        deathRegService.updateDeathRegistration(dr, adrColomboColombo);

        existing = deathRegService.getById(idUKey);
        assertFalse("Not an infant death", existing.getDeath().isInfantLessThan30Days());
        warnings = deathRegService.approveDeathRegistration(idUKey, adrColomboColombo, false);
        assertTrue("Since this is not a infant death no need to find in birth declarations", warnings.isEmpty());

        // death declaration of infant whose birth declaration not captured by the system and age, mother PIN/NIC known
        dr = getMinimalDDF(2011078947l, dod, colomboBDDivision, sammantranapuraGNDivision);
        dr.setDeathType(DeathRegister.Type.NORMAL);
        // death person age and mother PIN/NIC known
        dr.getDeathPerson().setDeathPersonAgeDate(20);
        dr.getDeathPerson().setDeathPersonMotherPINorNIC("661594521V");
        deathRegService.addNewDeathRegistration(dr, adrColomboColombo);
        idUKey = dr.getIdUKey();

        existing = deathRegService.getById(idUKey);
        assertTrue("Should be an infant death declaration", existing.getDeath().isInfantLessThan30Days());
        warnings = deathRegService.approveDeathRegistration(idUKey, adrColomboColombo, false);
        assertEquals("Infant birth not declared in the system", 1, warnings.size());
        // update existing death declaration
        dr.getDeathPerson().setDeathPersonAgeDate(45);
        deathRegService.updateDeathRegistration(dr, adrColomboColombo);

        existing = deathRegService.getById(idUKey);
        assertFalse("Not an infant death", existing.getDeath().isInfantLessThan30Days());
        warnings = deathRegService.approveDeathRegistration(idUKey, adrColomboColombo, false);
        assertTrue("Since this is not a infant death no need to find in birth declarations", warnings.isEmpty());
    }

    public void testInfantDeathRegisterWithBirthDeclaration() {
        logger.debug("Attempt to test infant death declarations whose birth declarations exist");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -20);
        Date dob = cal.getTime();
        Date dod = new Date();

        // death declaration of infant whose birth declaration in data entry and dob, mother PIN/NIC known
        BirthDeclaration bdf = getMinimalBDF(2010107711, dob, colomboBDDivision);
        bdf.getParent().setMotherNICorPIN("661604521V");
        birthRegSvc.addLiveBirthDeclaration(bdf, false, adrColomboColombo);
        long bdUKey = bdf.getIdUKey();
        assertEquals(BirthDeclaration.State.DATA_ENTRY, bdf.getRegister().getStatus());

        DeathRegister dr = getMinimalDDF(2011078948l, dod, colomboBDDivision, sammantranapuraGNDivision);
        dr.setDeathType(DeathRegister.Type.NORMAL);
        // death person dob and mother PIN/NIC known
        dr.getDeathPerson().setDeathPersonDOB(dob);
        dr.getDeathPerson().setDeathPersonMotherPINorNIC("661604521V");
        deathRegService.addNewDeathRegistration(dr, adrColomboColombo);
        long idUKey = dr.getIdUKey();

        DeathRegister existing = deathRegService.getById(idUKey);
        assertTrue("Should be an infant death declaration", existing.getDeath().isInfantLessThan30Days());
        List<UserWarning> warnings = deathRegService.approveDeathRegistration(idUKey, adrColomboColombo, false);
        assertEquals("Infant birth should be in data entry level", 1, warnings.size());
        // update existing death declaration
        cal.add(Calendar.DATE, -15);
        dr.getDeathPerson().setDeathPersonDOB(cal.getTime());
        deathRegService.updateDeathRegistration(dr, adrColomboColombo);

        existing = deathRegService.getById(idUKey);
        assertFalse("Not an infant death", existing.getDeath().isInfantLessThan30Days());
        warnings = deathRegService.approveDeathRegistration(idUKey, adrColomboColombo, false);
        assertTrue("Since this is not a infant death no need to find in birth declarations", warnings.isEmpty());
        existing = deathRegService.getById(idUKey);
        assertEquals(DeathRegister.State.APPROVED, existing.getStatus());

        // todo
        // birth declaration in approved state
//        BirthDeclaration bdf1 = getMinimalBDF(2010107712, dob, colomboBDDivision);
//        bdf1.getParent().setMotherNICorPIN("661614521V");
//        birthRegSvc.addLiveBirthDeclaration(bdf1, false, adrColomboColombo);
//        bdUKey = bdf1.getIdUKey();
//        assertEquals(BirthDeclaration.State.DATA_ENTRY, bdf1.getRegister().getStatus());
//        BirthDeclaration bdfEx1 = birthRegSvc.getById(bdUKey);
//        warnings = birthRegSvc.approveLiveBirthDeclaration(bdfEx1.getIdUKey(), true, adrColomboColombo);
//        bdfEx1 = birthRegSvc.getById(bdUKey);
//        assertEquals(BirthDeclaration.State.APPROVED, bdfEx1.getRegister().getStatus());
//
//        DeathRegister dr1 = getMinimalDDF(2011078949l, dod, colomboBDDivision, sammantranapuraGNDivision);
//        dr1.setDeathType(DeathRegister.Type.NORMAL);
//        // death person dob and mother PIN/NIC known
//        dr1.getDeathPerson().setDeathPersonDOB(dob);
//        dr1.getDeathPerson().setDeathPersonMotherPINorNIC("661604521V");
//        deathRegService.addNewDeathRegistration(dr1, adrColomboColombo);
//        long idUKey1 = dr1.getIdUKey();
//
//        DeathRegister existing1 = deathRegService.getById(idUKey);
//        assertTrue("Should be an infant death declaration", existing1.getDeath().isInfantLessThan30Days());
//        warnings = deathRegService.approveDeathRegistration(idUKey, adrColomboColombo, false);
//        assertEquals("Infant birth should be in data entry level", 1, warnings.size());
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
