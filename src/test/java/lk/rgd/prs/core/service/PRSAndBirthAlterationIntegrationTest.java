package lk.rgd.prs.core.service;

import junit.framework.TestCase;
import lk.rgd.UnitTestManager;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.InformantInfo;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.springframework.context.ApplicationContext;

import java.util.Date;

/**
 * @author Chathuranga Withana
 */
public class PRSAndBirthAlterationIntegrationTest extends TestCase {

    private final ApplicationContext ctx = UnitTestManager.ctx;
    private final PopulationRegistry eCivil;
    private final BirthRegistrationService birthRegSvc;
    private final UserManager userManager;

    private final BDDivisionDAO bdDivisionDAO;
    private final CountryDAO countryDAO;
    private final RaceDAO raceDAO;

    private final User argWestern;
    private final BDDivision colomboBDDivision;

    public PRSAndBirthAlterationIntegrationTest() {

        birthRegSvc = (BirthRegistrationService) ctx.getBean("manageBirthService", BirthRegistrationService.class);
        eCivil = (PopulationRegistry) ctx.getBean("ecivilService", PopulationRegistry.class);
        bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
        countryDAO = (CountryDAO) ctx.getBean("countryDAOImpl", CountryDAO.class);
        raceDAO = (RaceDAO) ctx.getBean("raceDAOImpl", RaceDAO.class);
        userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);

        try {
            argWestern = userManager.authenticateUser("arg-western", "password");
        } catch (AuthorizationException e) {
            throw new IllegalArgumentException("Cannot authenticate sample users");
        }

        colomboBDDivision = bdDivisionDAO.getBDDivisionByPK(1);
    }

    public void testPRSOnBirthAlteration() throws Exception {
        // Add live birth declaration with father and mother details
        final long birthSerial = 2011108001;
        try {
            long birthUKey = performBirthRegistrationFullProcess(birthSerial, "1985-12-01", "1985-12-02");
        } catch (Exception e) {
            fail("Birth registration process failed");
        }
        // TODO
    }

    public void testPRSRelationshipConsistentAfterEdit() throws Exception {
        // TODO
    }

    private long performBirthRegistrationFullProcess(long birthSerial, String childDob, String registerDate) {

        BirthDeclaration bdf = getMinimalBDF(birthSerial, DateTimeUtils.getDateFromISO8601String(childDob),
            DateTimeUtils.getDateFromISO8601String(registerDate), colomboBDDivision);
        bdf.getChild().setChildFullNameEnglish("child name english " + birthSerial);
        bdf.getParent().setMotherFullName("Mother full name " + birthSerial);
        bdf.getParent().setFatherFullName("Father full name " + birthSerial);
        bdf.getParent().setFatherDOB(DateTimeUtils.getDateFromISO8601String("1985-12-01"));
        bdf.getParent().setFatherPlaceOfBirth("Father place of birth " + birthSerial);
        bdf.getMarriage().setPlaceOfMarriage("Place of marriage " + birthSerial);
        bdf.getMarriage().setDateOfMarriage(DateTimeUtils.getDateFromISO8601String("1999-12-01"));

        birthRegSvc.addLiveBirthDeclaration(bdf, false, argWestern);
        birthRegSvc.approveLiveBirthDeclaration(bdf.getIdUKey(), true, argWestern);
        birthRegSvc.markLiveBirthConfirmationAsPrinted(bdf, argWestern);
        bdf = birthRegSvc.getById(bdf.getIdUKey(), argWestern);
        birthRegSvc.markLiveBirthDeclarationAsConfirmedWithoutChanges(bdf, argWestern);
        birthRegSvc.markLiveBirthCertificateAsPrinted(bdf, argWestern);

        return bdf.getIdUKey();
    }

    protected BirthDeclaration getMinimalBDF(long serial, Date dob, Date registerDate, BDDivision bdDivision) {

        BirthDeclaration bdf = new BirthDeclaration();
        bdf.getRegister().setBdfSerialNo(serial);
        bdf.getRegister().setDateOfRegistration(registerDate);
        bdf.getRegister().setBirthDivision(bdDivision);
        bdf.getChild().setDateOfBirth(dob);
        bdf.getChild().setPlaceOfBirth("Place of birth for child " + serial);
        bdf.getChild().setChildGender(0);
        bdf.getChild().setChildFullNameOfficialLang("සිංහලෙන් ළමයාගේ නම  " + serial);

        bdf.getInformant().setInformantName("Name of Informant for Child : " + serial);
        bdf.getInformant().setInformantAddress("Address of Informant for Child : " + serial);
        bdf.getInformant().setInformantSignDate(registerDate);
        bdf.getInformant().setInformantType(InformantInfo.InformantType.FATHER);

        bdf.getNotifyingAuthority().setNotifyingAuthorityAddress("The address of the Birth registrar");
        bdf.getNotifyingAuthority().setNotifyingAuthoritySignDate(registerDate);
        bdf.getNotifyingAuthority().setNotifyingAuthorityName("Name of the Notifying Authority");
        bdf.getNotifyingAuthority().setNotifyingAuthorityPIN("750010001");
        return bdf;
    }
}