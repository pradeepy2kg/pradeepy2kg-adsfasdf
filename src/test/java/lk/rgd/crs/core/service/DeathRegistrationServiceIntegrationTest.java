package lk.rgd.crs.core.service;

import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.domain.DeathRegister;
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

    public void testInfantDeathRegisterWithoutBirthDeclaration() {
        logger.debug("Attempt to test infant death declarations");
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

}
