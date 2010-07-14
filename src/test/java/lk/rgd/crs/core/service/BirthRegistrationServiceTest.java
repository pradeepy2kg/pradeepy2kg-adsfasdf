package lk.rgd.crs.core.service;

import junit.framework.Assert;
import junit.framework.TestCase;
import lk.rgd.UnitTestManager;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.core.service.UserManagerImpl;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.InformantInfo;
import lk.rgd.crs.api.service.BirthRegistrationService;
import org.springframework.context.ApplicationContext;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author asankha
 */
public class BirthRegistrationServiceTest extends TestCase {

    private final ApplicationContext ctx = UnitTestManager.ctx;
    private final BirthRegistrationService birthRegSvc;
    private final BDDivisionDAO bdDivisionDAO;
    private final UserManagerImpl userManager;
    private final BDDivision colomboBDDivision;
    private final BDDivision negamboBDDivision;
    private BirthDeclarationDAO birthDeclarationDAO;
    private User deoColomboColombo;
    private User deoGampahaNegambo;
    private User adrColomboColombo;
    private User adrGampahaNegambo;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        birthDeclarationDAO = (BirthDeclarationDAO)
            ctx.getBean("birthDeclarationDAOImpl", BirthDeclarationDAO.class);

        // delete records we may have added
        for (int i = 2010101; i < 2010110; i++) {
            deleteBDF(colomboBDDivision, i);
        }
    }

    public BirthRegistrationServiceTest() {

        birthRegSvc = (BirthRegistrationService) ctx.getBean("manageBirthService", BirthRegistrationService.class);
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

    public void testAddMinimalBDF() throws Exception {
        Calendar dob = Calendar.getInstance();
        // test saving of a minimal BDF for colombo by DEO
        dob.add(Calendar.DATE, -3);

        // test colombo deo adding for colombo
        BirthDeclaration bdf1 = getMinimalBDF(2010101, dob.getTime(), colomboBDDivision);
        birthRegSvc.addLiveBirthDeclaration(bdf1, false, deoColomboColombo, null, null);

        // reload again to fill all fields as we still only have IDUkey of new record
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);

        // colombo deo cannot approve
        try {
            birthRegSvc.approveLiveBirthDeclaration(bdf1, false, deoColomboColombo);
            fail("DEO cannot approve BDFs");
        } catch (Exception expected) {
        }
        // negambo deo cannot approve either
        try {
            birthRegSvc.approveLiveBirthDeclaration(bdf1, false, deoGampahaNegambo);
            fail("Negambo DEO cannot approve BDFs of Colombo BD division");
        } catch (Exception expected) {
        }
        // negambo adr cannot approve either
        try {
            birthRegSvc.approveLiveBirthDeclaration(bdf1, false, adrGampahaNegambo);
            fail("Negambo ADR cannot approve BDFs of Colombo BD division");
        } catch (Exception expected) {
        }

        // colombo ADR approves - should raise warnings
        List<UserWarning> warnings = birthRegSvc.approveLiveBirthDeclaration(bdf1, false, adrColomboColombo);
        Assert.assertTrue("A minimal BDF must trigger warnings that data is incomplete", !warnings.isEmpty());

        // ignore warnings should allow approval
        warnings = birthRegSvc.approveLiveBirthDeclaration(bdf1, true, adrColomboColombo);

        // check for existence of warning comment
        BirthDeclaration bdfSaved = birthRegSvc.getById(bdf1.getIdUKey(), adrColomboColombo);
        Assert.assertTrue(bdfSaved.getRegister().getComments().contains("ignoring warnings"));

        deleteBDF(colomboBDDivision, 2010101);
    }

    public void testDuplicateBDF() throws Exception {
        Calendar dob = Calendar.getInstance();
        // test saving of a minimal BDF for colombo by DEO
        dob.add(Calendar.DATE, -3);

        // add another record for the same mother, and check if the duplication warning was issued
        BirthDeclaration bdf1 = getMinimalBDF(2010102, dob.getTime(), colomboBDDivision);
        dob.add(Calendar.DATE, -30 * 27);
        BirthDeclaration bdf2 = getMinimalBDF(2010103, dob.getTime(), colomboBDDivision);
        dob.add(Calendar.DATE, 2 * 30 * 27);
        BirthDeclaration bdf3 = getMinimalBDF(2010104, dob.getTime(), colomboBDDivision);
        dob.add(Calendar.DATE, 30 * 12);
        BirthDeclaration bdf4 = getMinimalBDF(2010105, dob.getTime(), colomboBDDivision);

        bdf1.getParent().setMotherNICorPIN("755010001V");
        bdf2.getParent().setMotherNICorPIN("755010001V");
        bdf3.getParent().setMotherNICorPIN("755010001V");
        bdf4.getParent().setMotherNICorPIN("755010001V");

        birthRegSvc.addLiveBirthDeclaration(bdf1, false, deoColomboColombo, null, null);
        birthRegSvc.addLiveBirthDeclaration(bdf2, false, deoColomboColombo, null, null);
        birthRegSvc.addLiveBirthDeclaration(bdf3, false, deoColomboColombo, null, null);
        birthRegSvc.addLiveBirthDeclaration(bdf4, false, deoColomboColombo, null, null);

        // reload again to fill all fields as we still only have IDUkey of new record
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        bdf2 = birthRegSvc.getById(bdf2.getIdUKey(), deoColomboColombo);
        bdf3 = birthRegSvc.getById(bdf3.getIdUKey(), deoColomboColombo);
        bdf4 = birthRegSvc.getById(bdf4.getIdUKey(), deoColomboColombo);

        // ignore warnings and approve first
        List<UserWarning> warnings = birthRegSvc.approveLiveBirthDeclaration(bdf1, true, adrColomboColombo);

        // check warnings issued for second
        warnings = birthRegSvc.approveLiveBirthDeclaration(bdf2, false, adrColomboColombo);
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
        warnings = birthRegSvc.approveLiveBirthDeclaration(bdf3, false, adrColomboColombo);
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
        warnings = birthRegSvc.approveLiveBirthDeclaration(bdf4, false, adrColomboColombo);
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

        // delete records we may have added
        for (int i = 2010102; i < 2010105; i++) {
            deleteBDF(colomboBDDivision, i);
        }
    }

    public void testConfirmationWithoutChanges() throws Exception {
        Calendar dob = Calendar.getInstance();
        // test saving of a minimal BDF for colombo by DEO
        dob.add(Calendar.DATE, -3);

        // add a record for testing
        BirthDeclaration bdf1 = getMinimalBDF(2010106, dob.getTime(), colomboBDDivision);
        birthRegSvc.addLiveBirthDeclaration(bdf1, false, deoColomboColombo, null, null);

        // reload again to fill all fields as we still only have IDUkey of new record
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.DATA_ENTRY, bdf1.getRegister().getStatus());

        // make an edit as DEO - ignore any warnings
        bdf1.getChild().setChildFullNameEnglish("New name 1 of child");
        birthRegSvc.editLiveBirthDeclaration(bdf1, true, deoColomboColombo);

        // now approve as ADR
        birthRegSvc.approveLiveBirthDeclaration(bdf1, true, adrColomboColombo);
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
        Assert.assertEquals("Person confirming", bdf1.getConfirmant().getConfirmantFullName());
        Assert.assertEquals(BirthDeclaration.State.ARCHIVED_BC_GENERATED, bdf1.getRegister().getStatus());

        // simulate the system generation of the PIN
        bdf1.getChild().setPin(1000100001L);
        bdf1.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_BC_GENERATED);
        birthDeclarationDAO.updateBirthDeclaration(bdf1);

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
        Assert.assertEquals(BirthDeclaration.State.ARCHIVED_BC_PRINTED, bdf1.getRegister().getStatus());

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

        deleteBDF(colomboBDDivision, 2010106);
    }

    public void testConfirmationWithChanges() throws Exception {
        Calendar dob = Calendar.getInstance();
        // test saving of a minimal BDF for colombo by DEO
        dob.add(Calendar.DATE, -3);

        // add a record for testing
        BirthDeclaration bdf1 = getMinimalBDF(2010107, dob.getTime(), colomboBDDivision);
        birthRegSvc.addLiveBirthDeclaration(bdf1, false, deoColomboColombo, null, null);

        // reload again to fill all fields as we still only have IDUkey of new record
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.DATA_ENTRY, bdf1.getRegister().getStatus());

        // now approve as ADR making changes to name
        bdf1.getChild().setChildFullNameEnglish("New name 1 of child");
        birthRegSvc.approveLiveBirthDeclaration(bdf1, true, adrColomboColombo);

        // reload again and check for updated name
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.APPROVED, bdf1.getRegister().getStatus());
        Assert.assertEquals("NEW NAME 1 OF CHILD", bdf1.getChild().getChildFullNameEnglish());

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
        Assert.assertEquals("NEW NAME 1 OF CHILD", bdfOld.getChild().getChildFullNameEnglish());

        // reload new record with confirmation changes
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals("Person confirming", bdf1.getConfirmant().getConfirmantFullName());
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
        Assert.assertEquals(BirthDeclaration.State.ARCHIVED_BC_GENERATED, bdf1.getRegister().getStatus());

        // simulate the system generation of the PIN
        bdf1.getChild().setPin(1000100002L);
        bdf1.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_BC_GENERATED);
        birthDeclarationDAO.updateBirthDeclaration(bdf1);

        // DEO prints BC - mark BC as printed
        birthRegSvc.markLiveBirthCertificateAsPrinted(bdf1, deoColomboColombo);
        // reload again and check for update
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        Assert.assertEquals(BirthDeclaration.State.ARCHIVED_BC_PRINTED, bdf1.getRegister().getStatus());

        deleteBDF(colomboBDDivision, 2010107);
    }

    private BirthDeclaration getMinimalBDF(long serial, Date dob, BDDivision bdDivision) {

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
        bdf.getInformant().setInformantType(InformantInfo.InformantType.FATHER);

        bdf.getNotifyingAuthority().setNotifyingAuthorityAddress("The address of the Birth registrar");
        bdf.getNotifyingAuthority().setNotifyingAuthoritySignDate(today);
        bdf.getNotifyingAuthority().setNotifyingAuthorityName("Name of the Notifying Authority");
        bdf.getNotifyingAuthority().setNotifyingAuthorityPIN("750010001");
        return bdf;
    }

    private void deleteBDF(BDDivision bdDivision, long serial) {
        try {
            birthDeclarationDAO.deleteBirthDeclaration(
                birthDeclarationDAO.getByBDDivisionAndSerialNo(bdDivision, serial).getIdUKey());
        } catch (Exception e) {
        }
    }
}
