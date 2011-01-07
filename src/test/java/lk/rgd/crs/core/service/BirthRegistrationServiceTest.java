package lk.rgd.crs.core.service;

import junit.framework.Assert;
import junit.framework.TestCase;
import lk.rgd.UnitTestManager;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.InformantInfo;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import lk.rgd.crs.api.service.CertificateSearchService;
import org.hibernate.ejb.EntityManagerFactoryImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author asankha
 */
public class BirthRegistrationServiceTest extends TestCase {

    protected final ApplicationContext ctx = UnitTestManager.ctx;
    protected final BirthRegistrationService birthRegSvc;
    protected final PopulationRegistry eCivil;
    protected final CertificateSearchService certSearchSvc;
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

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        birthDeclarationDAO = (BirthDeclarationDAO)
            ctx.getBean("birthDeclarationDAOImpl", BirthDeclarationDAO.class);
    }

    public BirthRegistrationServiceTest() {

        birthRegSvc = (BirthRegistrationService) ctx.getBean("manageBirthService", BirthRegistrationService.class);
        eCivil = (PopulationRegistry) ctx.getBean("ecivilService", PopulationRegistry.class);
        certSearchSvc = (CertificateSearchService) ctx.getBean("certificateSearchService", CertificateSearchService.class);
        bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
        countryDAO = (CountryDAO) ctx.getBean("countryDAOImpl", CountryDAO.class);
        raceDAO = (RaceDAO) ctx.getBean("raceDAOImpl", RaceDAO.class);
        userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);

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

    public void testAddMinimalBDF() throws Exception {
        Calendar dob = Calendar.getInstance();
        // test saving of a minimal BDF for colombo by DEO
        dob.add(Calendar.DATE, -3);

        // test colombo deo adding for colombo
        BirthDeclaration bdf1 = getMinimalBDF(2010101010, dob.getTime(), colomboBDDivision);
        birthRegSvc.addLiveBirthDeclaration(bdf1, false, deoColomboColombo);

        // try adding a duplicate
        BirthDeclaration bdf2 = getMinimalBDF(2010101010, dob.getTime(), colomboBDDivision);
        try {
            birthRegSvc.addLiveBirthDeclaration(bdf2, false, deoColomboColombo);
            fail("Should not allow addition of duplicate records");
        } catch (Exception expected) {
        }


        // reload again to fill all fields as we still only have IDUkey of new record
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);

        // colombo deo cannot approve
        try {
            birthRegSvc.approveLiveBirthDeclaration(bdf1.getIdUKey(), false, deoColomboColombo);
            fail("DEO cannot approve BDFs");
        } catch (Exception expected) {
        }
        // negambo deo cannot approve either
        try {
            birthRegSvc.approveLiveBirthDeclaration(bdf1.getIdUKey(), false, deoGampahaNegambo);
            fail("Negambo DEO cannot approve BDFs of Colombo BD division");
        } catch (Exception expected) {
        }
        // negambo adr cannot approve either
        try {
            birthRegSvc.approveLiveBirthDeclaration(bdf1.getIdUKey(), false, adrGampahaNegambo);
            fail("Negambo ADR cannot approve BDFs of Colombo BD division");
        } catch (Exception expected) {
        }

        // colombo ADR approves - should raise warnings
        List<UserWarning> warnings = birthRegSvc.approveLiveBirthDeclaration(bdf1.getIdUKey(), false, adrColomboColombo);
        Assert.assertTrue("A minimal BDF must trigger warnings that data is incomplete", !warnings.isEmpty());

        // ignore warnings should allow approval
        warnings = birthRegSvc.approveLiveBirthDeclaration(bdf1.getIdUKey(), true, adrColomboColombo);

        // check for existence of warning comment
        BirthDeclaration bdfSaved = birthRegSvc.getById(bdf1.getIdUKey(), adrColomboColombo);
        Assert.assertTrue(bdfSaved.getRegister().getComments().contains("ignoring warnings"));
    }

    public void testDuplicateBDF() throws Exception {
        Calendar dob = Calendar.getInstance();
        // test saving of a minimal BDF for colombo by DEO
        dob.add(Calendar.DATE, -3);

        // add another record for the same mother, and check if the duplication warning was issued
        BirthDeclaration bdf1 = getMinimalBDF(2010102010, dob.getTime(), colomboBDDivision);
        dob.add(Calendar.DATE, -7 * 27);
        BirthDeclaration bdf2 = getMinimalBDF(2010103010, dob.getTime(), colomboBDDivision);
        dob.add(Calendar.DATE, 2 * 7 * 27);
        BirthDeclaration bdf3 = getMinimalBDF(2010104010, dob.getTime(), colomboBDDivision);
        dob.add(Calendar.DATE, 30 * 12);
        BirthDeclaration bdf4 = getMinimalBDF(2010105010, dob.getTime(), colomboBDDivision);

        bdf1.getParent().setMotherNICorPIN("755010001V");
        bdf2.getParent().setMotherNICorPIN("755010001V");
        bdf3.getParent().setMotherNICorPIN("755010001V");
        bdf4.getParent().setMotherNICorPIN("755010001V");

        birthRegSvc.addLiveBirthDeclaration(bdf1, false, deoColomboColombo);
        birthRegSvc.addLiveBirthDeclaration(bdf2, false, deoColomboColombo);
        birthRegSvc.addLiveBirthDeclaration(bdf3, false, deoColomboColombo);
        birthRegSvc.addLiveBirthDeclaration(bdf4, false, deoColomboColombo);

        // reload again to fill all fields as we still only have IDUkey of new record
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        bdf2 = birthRegSvc.getById(bdf2.getIdUKey(), deoColomboColombo);
        bdf3 = birthRegSvc.getById(bdf3.getIdUKey(), deoColomboColombo);
        bdf4 = birthRegSvc.getById(bdf4.getIdUKey(), deoColomboColombo);

        // ignore warnings and approve first
        List<UserWarning> warnings = birthRegSvc.approveLiveBirthDeclaration(bdf1.getIdUKey(), true, adrColomboColombo);

        // check warnings issued for second
        warnings = birthRegSvc.approveLiveBirthDeclaration(bdf2.getIdUKey(), false, adrColomboColombo);
        boolean found = false;
        for (UserWarning w : warnings) {
            if (w.getMessage().contains(Long.toString(bdf1.getIdUKey()))) {
                found = true;
                break;
            }
        }
        if (!found) {
            fail("Expected a warning for possible duplicate record for same mother in less within 28 weeks");
        }

        // check warnings issued for third
        warnings = birthRegSvc.approveLiveBirthDeclaration(bdf3.getIdUKey(), false, adrColomboColombo);
        found = false;
        for (UserWarning w : warnings) {
            if (w.getMessage().contains(Long.toString(bdf1.getIdUKey()))) {
                found = true;
                break;
            }
        }
        if (!found) {
            fail("Expected a warning for possible duplicate record for same mother within 28 weeks");
        }

        // check warnings issued for fourth
        warnings = birthRegSvc.approveLiveBirthDeclaration(bdf4.getIdUKey(), false, adrColomboColombo);
        found = false;
        for (UserWarning w : warnings) {
            if (w.getMessage().contains(Long.toString(bdf1.getIdUKey()))) {
                found = true;
                break;
            }
        }
        if (found) {
            fail("Did not expect a warning for possible duplicate record after 28 weeks");
        }
    }

    public void testConfirmationWithoutChanges() throws Exception {
        Calendar dob = Calendar.getInstance();
        // test saving of a minimal BDF for colombo by DEO
        dob.add(Calendar.DATE, -3);

        // add a record for testing
        BirthDeclaration bdf1 = getMinimalBDF(2010106010, dob.getTime(), colomboBDDivision);
        birthRegSvc.addLiveBirthDeclaration(bdf1, false, deoColomboColombo);

        // reload again to fill all fields as we still only have IDUkey of new record
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.DATA_ENTRY, bdf1.getRegister().getStatus());

        // make an edit as DEO - ignore any warnings
        bdf1.getChild().setChildFullNameEnglish("New name 1 of child");
        birthRegSvc.editLiveBirthDeclaration(bdf1, true, deoColomboColombo);

        // now approve as ADR
        birthRegSvc.approveLiveBirthDeclaration(bdf1.getIdUKey(), true, adrColomboColombo);
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.APPROVED, bdf1.getRegister().getStatus());

        // an edit attempt now should not be allowed
        try {
            bdf1.getChild().setChildFullNameEnglish("New name 2 of child");
            birthRegSvc.editLiveBirthDeclaration(bdf1, true, deoColomboColombo);
            fail("Should not allow edits for approved records");
        } catch (Exception expected) {
        }

        // reload again and check for updated name
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals("NEW NAME 1 OF CHILD", bdf1.getChild().getChildFullNameEnglish());

        // assert that the confirmed record now exists in the print queue
        List<BirthDeclaration> printList = birthRegSvc.getConfirmationPrintList(colomboBDDivision, 1, 10, false, deoColomboColombo);
        Assert.assertTrue(!printList.isEmpty());
        boolean found = false;
        for (BirthDeclaration b : printList) {
            if (b.getIdUKey() == bdf1.getIdUKey()) {
                found = true;
            }
        }
        Assert.assertTrue("approved record must show up for confirmation printing", found);

        // DEO prints confirmation - mark confirmation as printed
        birthRegSvc.markLiveBirthConfirmationAsPrinted(bdf1, deoColomboColombo);
        // reload again and check for updated status as printed
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.CONFIRMATION_PRINTED, bdf1.getRegister().getStatus());

        // assert that the printed record now does not exist in the print queue
        printList = birthRegSvc.getConfirmationPrintList(colomboBDDivision, 1, 100, false, deoColomboColombo);
        for (BirthDeclaration b : printList) {
            if (b.getIdUKey() == bdf1.getIdUKey()) {
                fail("The record when the confirmation is printed should not appear in the pending print list");
            }
        }
        // assert that the printed record will still appear if printed records are requested
        printList = birthRegSvc.getConfirmationPrintList(colomboBDDivision, 1, 100, true, deoColomboColombo);
        Assert.assertTrue(!printList.isEmpty());
        found = false;
        for (BirthDeclaration b : printList) {
            if (b.getIdUKey() == bdf1.getIdUKey()) {
                found = true;
            }
        }
        Assert.assertTrue("approved record must show up for confirmation printing (again) when explicitly requested", found);

        // capture confirmation by DEO without changes
        bdf1.getConfirmant().setConfirmantFullName("Person confirming");
        birthRegSvc.markLiveBirthDeclarationAsConfirmedWithoutChanges(bdf1, deoColomboColombo);

        // reload again and check for update
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals("Person confirming".toUpperCase(), bdf1.getConfirmant().getConfirmantFullName());
        Assert.assertEquals(BirthDeclaration.State.ARCHIVED_CERT_GENERATED, bdf1.getRegister().getStatus());

        // simulate the system generation of the PIN
        bdf1.getChild().setPin(1000100001L);
        bdf1.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CERT_GENERATED);
        birthDeclarationDAO.updateBirthDeclaration(bdf1, deoColomboColombo);

        // assert that the confirmed record now exists in the print queue for BC
        printList = birthRegSvc.getBirthCertificatePrintList(colomboBDDivision, 1, 100, false, deoColomboColombo);
        Assert.assertTrue(!printList.isEmpty());
        found = false;
        for (BirthDeclaration b : printList) {
            if (b.getIdUKey() == bdf1.getIdUKey()) {
                found = true;
            }
        }
        Assert.assertTrue("confirmed record must now show up for BC printing", found);

        // DEO prints BC - mark BC as printed
        birthRegSvc.markLiveBirthCertificateAsPrinted(bdf1, deoColomboColombo);
        // reload again and check for update
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.ARCHIVED_CERT_PRINTED, bdf1.getRegister().getStatus());

        // assert that the printed record now does not exist in the print queue
        printList = birthRegSvc.getBirthCertificatePrintList(colomboBDDivision, 1, 100, false, deoColomboColombo);
        found = false;
        for (BirthDeclaration b : printList) {
            if (b.getIdUKey() == bdf1.getIdUKey()) {
                found = true;
            }
        }
        Assert.assertTrue("confirmed record must not show up for BC printing", !found);

        // assert that the printed record will still appear if printed records are requested
        printList = birthRegSvc.getBirthCertificatePrintList(colomboBDDivision, 1, 100, true, deoColomboColombo);
        Assert.assertTrue(!printList.isEmpty());
        found = false;
        for (BirthDeclaration b : printList) {
            if (b.getIdUKey() == bdf1.getIdUKey()) {
                found = true;
            }
        }
        Assert.assertTrue("confirmed record must not show up for BC printing again if requested", found);
    }

    public void testConfirmationWithChanges() throws Exception {
        Calendar dob = Calendar.getInstance();
        // test saving of a minimal BDF for colombo by DEO
        dob.add(Calendar.DATE, -3);

        // add a record for testing
        BirthDeclaration bdf1 = getMinimalBDF(2010107010, dob.getTime(), colomboBDDivision);
        birthRegSvc.addLiveBirthDeclaration(bdf1, false, deoColomboColombo);

        // reload again to fill all fields as we still only have IDUkey of new record
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.DATA_ENTRY, bdf1.getRegister().getStatus());

        //todo remove cannot approve and change BDF in same time 
        // now approve as ADR
        //  bdf1.getChild().setChildFullNameEnglish("New name 1 of child");
        birthRegSvc.approveLiveBirthDeclaration(bdf1.getIdUKey(), true, adrColomboColombo);

        // reload again and check for updated name
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.APPROVED, bdf1.getRegister().getStatus());
        //  Assert.assertEquals("NEW NAME 1 OF CHILD", bdf1.getChild().getChildFullNameEnglish());

        // DEO prints confirmation - mark confirmation as printed
        birthRegSvc.markLiveBirthConfirmationAsPrinted(bdf1, deoColomboColombo);
        // reload again and check for updated status as printed
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.CONFIRMATION_PRINTED, bdf1.getRegister().getStatus());

        // capture confirmation by DEO with changes
        bdf1.getConfirmant().setConfirmantFullName("Person confirming");
        bdf1.getChild().setChildFullNameEnglish("New name 2 of child");
        long idUKeyToArchive = bdf1.getIdUKey();
        birthRegSvc.captureLiveBirthConfirmationChanges(bdf1, deoColomboColombo);

        // reload again and check for update of old record which should now be archived
        BirthDeclaration bdfOld = birthRegSvc.getById(idUKeyToArchive, deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.ARCHIVED_CORRECTED, bdfOld.getRegister().getStatus());
        Assert.assertEquals("No Name for Archived data", null, bdfOld.getChild().getChildFullNameEnglish());

        // reload new record with confirmation changes
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals("Person confirming".toUpperCase(), bdf1.getConfirmant().getConfirmantFullName());
        Assert.assertEquals("NEW NAME 2 OF CHILD", bdf1.getChild().getChildFullNameEnglish());
        Assert.assertEquals(BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED, bdf1.getRegister().getStatus());

        // update the child name again for a second time
        bdf1.getChild().setChildFullNameEnglish("New name 3 of child");
        birthRegSvc.captureLiveBirthConfirmationChanges(bdf1, deoColomboColombo);
        // the status should remain as confirmation changes captured with only the name updated
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals("NEW NAME 3 OF CHILD", bdf1.getChild().getChildFullNameEnglish());
        Assert.assertEquals(BirthDeclaration.State.CONFIRMATION_CHANGES_CAPTURED, bdf1.getRegister().getStatus());

        // approve the changes by ADR
        birthRegSvc.approveConfirmationChanges(bdf1, true, adrColomboColombo);
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.ARCHIVED_CERT_GENERATED, bdf1.getRegister().getStatus());

        // simulate the system generation of the PIN
        bdf1.getChild().setPin(1000100002L);
        bdf1.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CERT_GENERATED);
        birthDeclarationDAO.updateBirthDeclaration(bdf1, deoColomboColombo);

        // DEO prints BC - mark BC as printed
        birthRegSvc.markLiveBirthCertificateAsPrinted(bdf1, deoColomboColombo);
        // reload again and check for update
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.ARCHIVED_CERT_PRINTED, bdf1.getRegister().getStatus());
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

    public void testPRSUpdatesFatherInformant() throws Exception {
        Calendar dob = Calendar.getInstance();
        // test saving of a minimal BDF for colombo by DEO
        dob.add(Calendar.DATE, -3);

        // add a record for testing
        BirthDeclaration bdf1 = getMinimalBDF(2010106011, dob.getTime(), colomboBDDivision);
        bdf1.getParent().setMotherFullName("Mother full name");
        bdf1.getParent().setMotherDOB(DateTimeUtils.getDateFromISO8601String("1975-12-07"));
        bdf1.getParent().setMotherAddress("Address of mother");
        bdf1.getParent().setMotherNICorPIN("755011234V");
        bdf1.getParent().setMotherPlaceOfBirth("Mother place of birth");
        bdf1.getParent().setFatherFullName("Father full name");
        bdf1.getParent().setFatherDOB(DateTimeUtils.getDateFromISO8601String("1975-07-29"));
        bdf1.getParent().setFatherNICorPIN("752111430V");
        bdf1.getParent().setFatherPlaceOfBirth("Father place of birth");

        bdf1.getInformant().setInformantName("Father full name");
        bdf1.getInformant().setInformantAddress("Address of father");
        bdf1.getInformant().setInformantNICorPIN("752111430V");
        bdf1.getInformant().setInformantType(InformantInfo.InformantType.FATHER);
        birthRegSvc.addLiveBirthDeclaration(bdf1, false, deoColomboColombo);

        // now approve as ADR
        birthRegSvc.approveLiveBirthDeclaration(bdf1.getIdUKey(), true, adrColomboColombo);
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.APPROVED, bdf1.getRegister().getStatus());

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
        Assert.assertEquals("Person confirming".toUpperCase(), bdf1.getConfirmant().getConfirmantFullName());
        Assert.assertEquals(BirthDeclaration.State.ARCHIVED_CERT_GENERATED, bdf1.getRegister().getStatus());

        Person mother = eCivil.findPersonByPINorNIC("755011234V", adrColomboColombo);
        Assert.assertNotNull(mother);
        Assert.assertEquals("MOTHER FULL NAME", mother.getFullNameInOfficialLanguage());
        Assert.assertEquals("ADDRESS OF MOTHER", mother.getLastAddress().getLine1());
        Assert.assertEquals(DateTimeUtils.getDateFromISO8601String("1975-12-07"), mother.getDateOfBirth());
        Assert.assertEquals("MOTHER PLACE OF BIRTH", mother.getPlaceOfBirth());

        Person father = eCivil.findPersonByPINorNIC("752111430V", adrColomboColombo);
        Assert.assertNotNull(father);
        Assert.assertEquals("FATHER FULL NAME", father.getFullNameInOfficialLanguage());
        Assert.assertEquals("ADDRESS OF FATHER", father.getLastAddress().getLine1());
        Assert.assertEquals(DateTimeUtils.getDateFromISO8601String("1975-07-29"), father.getDateOfBirth());
        Assert.assertEquals("FATHER PLACE OF BIRTH", father.getPlaceOfBirth());
    }

    public void testPRSUpdatesDoNotTakeMothersAddressAsFathersIfUnmarried() throws Exception {
        Calendar dob = Calendar.getInstance();
        // test saving of a minimal BDF for colombo by DEO
        dob.add(Calendar.DATE, -3);

        // add a record for testing
        BirthDeclaration bdf1 = getMinimalBDF(2010106012, dob.getTime(), colomboBDDivision);
        bdf1.getParent().setMotherFullName("Mother full name");
        bdf1.getParent().setMotherDOB(DateTimeUtils.getDateFromISO8601String("1975-12-07"));
        bdf1.getParent().setMotherAddress("Address of mother");
        bdf1.getParent().setMotherNICorPIN("755011235V");
        bdf1.getParent().setMotherPlaceOfBirth("Mother place of birth");
        bdf1.getParent().setFatherFullName("Father full name");
        bdf1.getParent().setFatherDOB(DateTimeUtils.getDateFromISO8601String("1975-07-29"));
        bdf1.getParent().setFatherNICorPIN("752111431V");
        bdf1.getParent().setFatherPlaceOfBirth("Father place of birth");

        bdf1.getInformant().setInformantName("Mother full name");
        bdf1.getInformant().setInformantAddress("Address of mother");
        bdf1.getInformant().setInformantNICorPIN("755011234V");
        bdf1.getInformant().setInformantType(InformantInfo.InformantType.MOTHER);
        birthRegSvc.addLiveBirthDeclaration(bdf1, false, deoColomboColombo);

        // now approve as ADR
        birthRegSvc.approveLiveBirthDeclaration(bdf1.getIdUKey(), true, adrColomboColombo);
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.APPROVED, bdf1.getRegister().getStatus());

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
        Assert.assertEquals("Person confirming".toUpperCase(), bdf1.getConfirmant().getConfirmantFullName());
        Assert.assertEquals(BirthDeclaration.State.ARCHIVED_CERT_GENERATED, bdf1.getRegister().getStatus());

        Person mother = eCivil.findPersonByPINorNIC("755011235V", adrColomboColombo);
        Assert.assertNotNull(mother);
        Assert.assertEquals("MOTHER FULL NAME", mother.getFullNameInOfficialLanguage());
        Assert.assertEquals("ADDRESS OF MOTHER", mother.getLastAddress().getLine1());
        Assert.assertEquals(DateTimeUtils.getDateFromISO8601String("1975-12-07"), mother.getDateOfBirth());
        Assert.assertEquals("MOTHER PLACE OF BIRTH", mother.getPlaceOfBirth());

        Person father = eCivil.findPersonByPINorNIC("752111431V", adrColomboColombo);
        Assert.assertNotNull(father);
        Assert.assertEquals("FATHER FULL NAME", father.getFullNameInOfficialLanguage());
        Assert.assertNull(father.getLastAddress());
        Assert.assertEquals(DateTimeUtils.getDateFromISO8601String("1975-07-29"), father.getDateOfBirth());
        Assert.assertEquals("FATHER PLACE OF BIRTH", father.getPlaceOfBirth());
    }

    public void testPRSUpdatesMothersAddressAsFathersMarried() throws Exception {
        Calendar dob = Calendar.getInstance();
        // test saving of a minimal BDF for colombo by DEO
        dob.add(Calendar.DATE, -3);

        // add a record for testing
        BirthDeclaration bdf1 = getMinimalBDF(2010106013, dob.getTime(), colomboBDDivision);
        bdf1.getParent().setMotherFullName("Mother full name");
        bdf1.getParent().setMotherDOB(DateTimeUtils.getDateFromISO8601String("1975-12-07"));
        bdf1.getParent().setMotherAddress("Address of mother");
        bdf1.getParent().setMotherNICorPIN("755011236V");
        bdf1.getParent().setMotherPlaceOfBirth("Mother place of birth");
        bdf1.getParent().setFatherFullName("Father full name");
        bdf1.getParent().setFatherDOB(DateTimeUtils.getDateFromISO8601String("1975-07-29"));
        bdf1.getParent().setFatherNICorPIN("752111432V");
        bdf1.getParent().setFatherPlaceOfBirth("Father place of birth");

        bdf1.getInformant().setInformantName("Mother full name");
        bdf1.getInformant().setInformantAddress("Address of mother");
        bdf1.getInformant().setInformantNICorPIN("755011234V");
        bdf1.getInformant().setInformantType(InformantInfo.InformantType.MOTHER);

        bdf1.getMarriage().setDateOfMarriage(DateTimeUtils.getDateFromISO8601String("2010-01-21"));
        bdf1.getMarriage().setPlaceOfMarriage("Place of marriage");
        birthRegSvc.addLiveBirthDeclaration(bdf1, false, deoColomboColombo);

        // now approve as ADR
        birthRegSvc.approveLiveBirthDeclaration(bdf1.getIdUKey(), true, adrColomboColombo);
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.APPROVED, bdf1.getRegister().getStatus());

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
        Assert.assertEquals("Person confirming".toUpperCase(), bdf1.getConfirmant().getConfirmantFullName());
        Assert.assertEquals(BirthDeclaration.State.ARCHIVED_CERT_GENERATED, bdf1.getRegister().getStatus());

        Person mother = eCivil.findPersonByPINorNIC("755011236V", adrColomboColombo);
        Assert.assertNotNull(mother);
        Assert.assertEquals("MOTHER FULL NAME", mother.getFullNameInOfficialLanguage());
        Assert.assertEquals("ADDRESS OF MOTHER", mother.getLastAddress().getLine1());
        Assert.assertEquals(DateTimeUtils.getDateFromISO8601String("1975-12-07"), mother.getDateOfBirth());
        Assert.assertEquals("MOTHER PLACE OF BIRTH", mother.getPlaceOfBirth());

        Person father = eCivil.findPersonByPINorNIC("752111432V", adrColomboColombo);
        Assert.assertNotNull(father);
        Assert.assertEquals("FATHER FULL NAME", father.getFullNameInOfficialLanguage());
        Assert.assertEquals("ADDRESS OF MOTHER", father.getLastAddress().getLine1());
        Assert.assertEquals(DateTimeUtils.getDateFromISO8601String("1975-07-29"), father.getDateOfBirth());
        Assert.assertEquals("FATHER PLACE OF BIRTH", father.getPlaceOfBirth());
    }

    public void testGrandFatherUpdateToPRS() throws Exception {
        Calendar dob = Calendar.getInstance();
        // test saving of a minimal BDF for colombo by DEO
        dob.add(Calendar.DATE, -3);

        // add a record for testing
        BirthDeclaration bdf1 = getMinimalBDF(2010106014, dob.getTime(), colomboBDDivision);
        bdf1.getParent().setMotherFullName("Mother full name");
        bdf1.getParent().setMotherDOB(DateTimeUtils.getDateFromISO8601String("1975-12-07"));
        bdf1.getParent().setMotherAddress("Address of mother");
        bdf1.getParent().setMotherNICorPIN("755011236V");
        bdf1.getParent().setMotherPlaceOfBirth("Mother place of birth");
        bdf1.getParent().setFatherFullName("Father full name");
        bdf1.getParent().setFatherDOB(DateTimeUtils.getDateFromISO8601String("1975-07-29"));
        bdf1.getParent().setFatherNICorPIN("752111432V");
        bdf1.getParent().setFatherPlaceOfBirth("Father place of birth");

        bdf1.getInformant().setInformantName("Mother full name");
        bdf1.getInformant().setInformantAddress("Address of mother");
        bdf1.getInformant().setInformantNICorPIN("755011234V");
        bdf1.getInformant().setInformantType(InformantInfo.InformantType.MOTHER);

        bdf1.getMarriage().setDateOfMarriage(DateTimeUtils.getDateFromISO8601String("2010-01-21"));
        bdf1.getMarriage().setPlaceOfMarriage("Place of marriage");

        bdf1.getGrandFather().setGrandFatherBirthPlace("Colombo");
        bdf1.getGrandFather().setGrandFatherNICorPIN("412111345V");
        bdf1.getGrandFather().setGrandFatherFullName("Full name of grandfather");
        bdf1.getGrandFather().setGrandFatherBirthYear(1941);

        bdf1.getGrandFather().setGreatGrandFatherBirthPlace("Kandy");
        bdf1.getGrandFather().setGreatGrandFatherNICorPIN("112111345V");
        bdf1.getGrandFather().setGreatGrandFatherFullName("Full name of great grandfather");
        bdf1.getGrandFather().setGreatGrandFatherBirthYear(1911);

        birthRegSvc.addLiveBirthDeclaration(bdf1, false, deoColomboColombo);

        // now approve as ADR
        birthRegSvc.approveLiveBirthDeclaration(bdf1.getIdUKey(), true, adrColomboColombo);
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.APPROVED, bdf1.getRegister().getStatus());

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
        Assert.assertEquals("Person confirming".toUpperCase(), bdf1.getConfirmant().getConfirmantFullName());
        Assert.assertEquals(BirthDeclaration.State.ARCHIVED_CERT_GENERATED, bdf1.getRegister().getStatus());

        Person mother = eCivil.findPersonByPINorNIC("755011236V", adrColomboColombo);
        Assert.assertNotNull(mother);
        Assert.assertEquals("MOTHER FULL NAME", mother.getFullNameInOfficialLanguage());
        Assert.assertEquals("ADDRESS OF MOTHER", mother.getLastAddress().getLine1());
        Assert.assertEquals(DateTimeUtils.getDateFromISO8601String("1975-12-07"), mother.getDateOfBirth());
        Assert.assertEquals("MOTHER PLACE OF BIRTH", mother.getPlaceOfBirth());

        Person father = eCivil.findPersonByPINorNIC("752111432V", adrColomboColombo);
        Assert.assertNotNull(father);
        Assert.assertEquals("FATHER FULL NAME", father.getFullNameInOfficialLanguage());
        Assert.assertEquals("ADDRESS OF MOTHER", father.getLastAddress().getLine1());
        Assert.assertEquals(DateTimeUtils.getDateFromISO8601String("1975-07-29"), father.getDateOfBirth());
        Assert.assertEquals("FATHER PLACE OF BIRTH", father.getPlaceOfBirth());

        Person grandpa = eCivil.findPersonByPINorNIC("412111345V", adrColomboColombo);
        Assert.assertNotNull(grandpa);
        Assert.assertEquals("FULL NAME OF GRANDFATHER", grandpa.getFullNameInOfficialLanguage());
        Assert.assertEquals("COLOMBO", grandpa.getPlaceOfBirth());

        Person greatgrandpa = eCivil.findPersonByPINorNIC("112111345V", adrColomboColombo);
        Assert.assertNotNull(greatgrandpa);
        Assert.assertEquals("FULL NAME OF GREAT GRANDFATHER", greatgrandpa.getFullNameInOfficialLanguage());
        Assert.assertEquals("KANDY", greatgrandpa.getPlaceOfBirth());
    }

    public void testForeignerUpdateToPRS() throws Exception {
        Calendar dob = Calendar.getInstance();
        // test saving of a minimal BDF for colombo by DEO
        dob.add(Calendar.DATE, -3);

        // add a record for testing
        BirthDeclaration bdf1 = getMinimalBDF(2010106015, dob.getTime(), colomboBDDivision);

        // set child name to one word
        bdf1.getChild().setChildFullNameOfficialLang("සමන් ප්‍රියශාන් යෝෂි");
        bdf1.getChild().setChildFullNameEnglish("SAMAN PRIYASHAN YOSHI");

        bdf1.getParent().setMotherFullName("වේඩික්කාරගේ සුමනාවතී");
        bdf1.getParent().setMotherDOB(DateTimeUtils.getDateFromISO8601String("1984-02-21"));
        bdf1.getParent().setMotherAddress("23 පන්සල් වත්ත පාර, රාහුලගම");
        bdf1.getParent().setMotherNICorPIN("755011236V");
        bdf1.getParent().setMotherPlaceOfBirth("පේරාදෙණිය ශික්ෂණ රෝහල");
        bdf1.getParent().setMotherRace(raceDAO.getRace(1));

        bdf1.getParent().setFatherFullName("ෂින් ක්වාන් යෝෂි");
        bdf1.getParent().setFatherDOB(DateTimeUtils.getDateFromISO8601String("1982-12-23"));
        bdf1.getParent().setFatherCountry(countryDAO.getCountry(2));    // japan
        bdf1.getParent().setFatherPassportNo("M1604104");
        bdf1.getParent().setFatherPlaceOfBirth("යොකොහාමා ජපානය    ");
        bdf1.getParent().setFatherRace(raceDAO.getRace(11)); // other foreigner

        bdf1.getInformant().setInformantName("වේඩික්කාරගේ සුමනාවතී");
        bdf1.getInformant().setInformantAddress("23 පන්සල් වත්ත පාර, රාහුලගම");
        bdf1.getInformant().setInformantType(InformantInfo.InformantType.MOTHER);

        bdf1.getMarriage().setDateOfMarriage(DateTimeUtils.getDateFromISO8601String("2010-01-21"));
        bdf1.getMarriage().setPlaceOfMarriage("යොකොහාමා");

        birthRegSvc.addLiveBirthDeclaration(bdf1, false, deoColomboColombo);

        // now approve as ADR
        birthRegSvc.approveLiveBirthDeclaration(bdf1.getIdUKey(), true, adrColomboColombo);
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.APPROVED, bdf1.getRegister().getStatus());

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
        Assert.assertEquals("Person confirming".toUpperCase(), bdf1.getConfirmant().getConfirmantFullName());
        Assert.assertEquals(BirthDeclaration.State.ARCHIVED_CERT_GENERATED, bdf1.getRegister().getStatus());

        // add a second record for Japan parent
        // add a record for testing
        BirthDeclaration bdf2 = getMinimalBDF(2010106016, dob.getTime(), colomboBDDivision);

        // set child name to one word
        bdf2.getChild().setChildFullNameOfficialLang("උභය පානිනි ජයසිංහ EDIT AGAIN");
        bdf2.getChild().setChildFullNameEnglish("UBHAYA PANINI JAYASINGHE  EDIT AGAIN");

        bdf2.getParent().setMotherFullName("Mother full name");
        bdf2.getParent().setMotherDOB(DateTimeUtils.getDateFromISO8601String("1975-12-07"));
        bdf2.getParent().setMotherAddress("Address of mother");
        bdf2.getParent().setMotherNICorPIN("755011237V");
        bdf2.getParent().setMotherPlaceOfBirth("Mother place of birth");
        bdf2.getParent().setFatherFullName("Japanese Father full name 2");
        bdf2.getParent().setFatherDOB(DateTimeUtils.getDateFromISO8601String("1975-08-29"));
        bdf2.getParent().setFatherCountry(countryDAO.getCountry(2));
        bdf2.getParent().setFatherPassportNo("M1604102");
        bdf2.getParent().setFatherPlaceOfBirth("Father place of birth");

        bdf2.getInformant().setInformantName("Mother full name");
        bdf2.getInformant().setInformantAddress("Address of mother");
        bdf2.getInformant().setInformantNICorPIN("755011235V");
        bdf2.getInformant().setInformantType(InformantInfo.InformantType.MOTHER);

        bdf2.getMarriage().setDateOfMarriage(DateTimeUtils.getDateFromISO8601String("2010-01-21"));
        bdf2.getMarriage().setPlaceOfMarriage("Place of marriage");

        birthRegSvc.addLiveBirthDeclaration(bdf2, false, deoColomboColombo);

        // now approve as ADR
        birthRegSvc.approveLiveBirthDeclaration(bdf2.getIdUKey(), true, adrColomboColombo);
        bdf2 = birthRegSvc.getById(bdf2.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.APPROVED, bdf2.getRegister().getStatus());

        // DEO prints confirmation - mark confirmation as printed
        birthRegSvc.markLiveBirthConfirmationAsPrinted(bdf2, deoColomboColombo);
        // reload again and check for updated status as printed
        bdf2 = birthRegSvc.getById(bdf2.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.CONFIRMATION_PRINTED, bdf2.getRegister().getStatus());

        // capture confirmation by DEO without changes
        bdf2.getConfirmant().setConfirmantFullName("Person confirming");
        birthRegSvc.markLiveBirthDeclarationAsConfirmedWithoutChanges(bdf2, deoColomboColombo);

        // reload again and check for update
        bdf2 = birthRegSvc.getById(bdf2.getIdUKey(), deoColomboColombo);
        Assert.assertEquals("Person confirming".toUpperCase(), bdf2.getConfirmant().getConfirmantFullName());
        Assert.assertEquals(BirthDeclaration.State.ARCHIVED_CERT_GENERATED, bdf2.getRegister().getStatus());

        // asankha some temp code to check if 2nd level caching works
        EntityManagerFactoryInfo emfi = (EntityManagerFactoryInfo) ctx.getBean("entityManagerFactory");
        EntityManagerFactory emf = emfi.getNativeEntityManagerFactory();
        EntityManagerFactoryImpl empImpl = (EntityManagerFactoryImpl)emf;
        System.out.println(empImpl.getSessionFactory().getStatistics());
    }
}
