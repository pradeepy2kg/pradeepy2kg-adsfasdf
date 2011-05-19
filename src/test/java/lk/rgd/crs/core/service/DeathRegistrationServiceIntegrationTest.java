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

    public void testInfantDeathRegister() {
        logger.debug("Attempt to test infant death declarations");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        // death declaration of infant whose birth declaration not captured by the system
        DeathRegister dr = getMinimalDDF(2011078946l, cal.getTime(), colomboBDDivision, sammantranapuraGNDivision);
        dr.setDeathType(DeathRegister.Type.NORMAL);
        cal.add(Calendar.DATE, -20);
        // death person dob and mother PIN/NIC known
        dr.getDeathPerson().setDeathPersonDOB(cal.getTime());
        dr.getDeathPerson().setDeathPersonMotherPINorNIC("661584521V");
        deathRegService.addNewDeathRegistration(dr, adrColomboColombo);
        long idUKey = dr.getIdUKey();

        DeathRegister existDr = deathRegService.getById(idUKey);
        assertTrue("Should be a infant death declaration", existDr.getDeath().isInfantLessThan30Days());
        List<UserWarning> warnings = deathRegService.approveDeathRegistration(idUKey, adrColomboColombo, false);
        assertEquals("Infant birth not declared in the system", 1, warnings.size());

//        cal.add(Calendar.DATE, -15);
//        dr.getDeathPerson().setDeathPersonDOB(cal.getTime());
//        deathRegService.updateDeathRegistration(dr, adrColomboColombo);
//
//        existDr = deathRegService.getById(idUKey);
//        assertTrue("Should be a infant death declaration", existDr.getDeath().isInfantLessThan30Days());
//        warnings = deathRegService.approveDeathRegistration(idUKey, adrColomboColombo, false);
//        assertEquals("Infant birth not declared in the system", 1, warnings.size());

    }

}
