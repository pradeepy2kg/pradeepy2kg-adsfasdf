package lk.rgd.crs.core.service;

import junit.framework.TestCase;
import lk.rgd.AppConstants;
import lk.rgd.UnitTestManager;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.core.service.UserManagerImpl;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.service.BirthRegistrationService;
import org.springframework.context.ApplicationContext;

import java.util.Calendar;
import java.util.Date;

/**
 * @author asankha
 */
public class BirthRegistrationServiceTest extends TestCase {

    private final ApplicationContext ctx = UnitTestManager.ctx;
    private final BirthRegistrationService birthRegSvc;
    private final BDDivisionDAO bdDivisionDAO;
    private final UserManagerImpl userManager;
    private User deo;
    private User adrColomboColombo;

    public BirthRegistrationServiceTest() {
        birthRegSvc = (BirthRegistrationService) ctx.getBean("manageBirthService", BirthRegistrationService.class);
        bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
        userManager = (UserManagerImpl) ctx.getBean("userManagerService", UserManagerImpl.class);
        try {
            deo = userManager.authenticateUser("deo-colombo-colombo", "password");
            adrColomboColombo = userManager.authenticateUser("adr-colombo-colombo", "password");
        } catch (AuthorizationException e) {
            throw new IllegalArgumentException("Cannot authenticate sample users");
        }
    }

    public void testAddNormalBirthDeclaration() throws Exception {
        Calendar dob = Calendar.getInstance();

        // test saving of a minimal BDF for colombo by DEO
        dob.add(Calendar.DATE, -3);
        BirthDeclaration bdf = getMinimalBDF(2010101, dob.getTime(), bdDivisionDAO.getBDDivisionByPK(1));
        birthRegSvc.addNormalBirthDeclaration(bdf, false, deo, null, null);
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
        bdf.getInformant().setInformantType(0);

        bdf.getNotifyingAuthority().setNotifyingAuthorityAddress("The address of the Birth registrar");
        bdf.getNotifyingAuthority().setNotifyingAuthoritySignDate(today);
        bdf.getNotifyingAuthority().setNotifyingAuthorityName("Name of the Notifying Authority");
        bdf.getNotifyingAuthority().setNotifyingAuthorityPIN("750010001");
        return bdf;
    }
}
