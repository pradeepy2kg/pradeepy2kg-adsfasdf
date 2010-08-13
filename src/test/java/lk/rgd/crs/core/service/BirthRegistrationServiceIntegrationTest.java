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
import lk.rgd.crs.api.domain.BirthCertificateSearch;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.InformantInfo;
import lk.rgd.crs.api.service.BirthRegistrationService;
import org.springframework.context.ApplicationContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author asankha
 */
public class BirthRegistrationServiceIntegrationTest extends BirthRegistrationServiceTest {

    public void testBirthRecordIndexing() throws Exception {

        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
        BirthDeclaration bdf1 = getMinimalBDF(2010108, dfm.parse("2010-07-21"), colomboBDDivision);
        bdf1.getChild().setChildFullNameEnglish("aaaa bbbb cccc zzzz");
        bdf1.getChild().setChildGender(0);
        bdf1.getParent().setMotherFullName("hhhh iiii zzzz");
        birthRegSvc.addLiveBirthDeclaration(bdf1, false, deoColomboColombo, null, null);
        birthRegSvc.approveLiveBirthDeclaration(bdf1, true, adrColomboColombo);
        birthRegSvc.markLiveBirthConfirmationAsPrinted(bdf1, deoColomboColombo);
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        birthRegSvc.markLiveBirthDeclarationAsConfirmedWithoutChanges(bdf1, deoColomboColombo);

        BirthDeclaration bdf2 = getMinimalBDF(2010109, dfm.parse("2010-07-22"), colomboBDDivision);
        bdf2.getChild().setChildFullNameEnglish("eeee ffff gggg zzzz");
        bdf2.getChild().setChildGender(0);
        bdf2.getParent().setMotherFullName("anothermothernameone anothermothernametwo");
        birthRegSvc.addLiveBirthDeclaration(bdf2, false, deoColomboColombo, null, null);
        birthRegSvc.approveLiveBirthDeclaration(bdf2, true, adrColomboColombo);
        birthRegSvc.markLiveBirthConfirmationAsPrinted(bdf2, deoColomboColombo);
        bdf2 = birthRegSvc.getById(bdf2.getIdUKey(), deoColomboColombo);
        birthRegSvc.markLiveBirthDeclarationAsConfirmedWithoutChanges(bdf2, deoColomboColombo);

        BirthDeclaration bdf3 = getMinimalBDF(2010110, dfm.parse("2010-07-22"), colomboBDDivision);
        bdf3.getChild().setChildFullNameEnglish("hhhh iiii zzzz");
        bdf3.getChild().setChildGender(0);
        bdf3.getParent().setMotherFullName("anothermothernameone anothermothernametwo");
        birthRegSvc.addLiveBirthDeclaration(bdf3, false, deoColomboColombo, null, null);
        birthRegSvc.approveLiveBirthDeclaration(bdf3, true, adrColomboColombo);
        birthRegSvc.markLiveBirthConfirmationAsPrinted(bdf3, deoColomboColombo);
        bdf3 = birthRegSvc.getById(bdf3.getIdUKey(), deoColomboColombo);
        birthRegSvc.markLiveBirthDeclarationAsConfirmedWithoutChanges(bdf3, deoColomboColombo);

        BirthCertificateSearch bcs = new BirthCertificateSearch();
        bcs.setApplicantFullName("applicant name");
        bcs.setApplicantAddress("applicant address");
        bcs.setDsDivision(colomboBDDivision.getDsDivision());
        bcs.setApplicationNo("1");
        bcs.setDateOfSubmission(new Date());

        // should find when the search term does occur within the field
        bcs.setChildFullNameEnglish("aaaa bbbb");
        List<BirthDeclaration> results = birthRegSvc.performBirthCertificateSearch(bcs, adrColomboColombo);
        Assert.assertEquals(1, results.size());

        // should find when the search term does occur within the field
        bcs.setChildFullNameEnglish("ffff zzzz");
        bcs.setSearchUKey(0);
        bcs.setApplicationNo("2");
        results = birthRegSvc.performBirthCertificateSearch(bcs, adrColomboColombo);
        Assert.assertEquals(1, results.size());

        // should not find when the search term does not occur
        bcs.setChildFullNameEnglish("xxxx yyyy");
        bcs.setSearchUKey(0);
        bcs.setApplicationNo("3");
        results = birthRegSvc.performBirthCertificateSearch(bcs, adrColomboColombo);
        Assert.assertEquals(0, results.size());

        bcs.setGender(0);
        bcs.setChildFullNameEnglish("zzzz");
        bcs.setSearchUKey(0);
        bcs.setApplicationNo("4");
        results = birthRegSvc.performBirthCertificateSearch(bcs, adrColomboColombo);
        Assert.assertEquals(3, results.size());

        // should not match zzzz for gender = 1 as gender is an exact match
        bcs.setGender(1);
        bcs.setSearchUKey(0);
        bcs.setApplicationNo("5");
        results = birthRegSvc.performBirthCertificateSearch(bcs, adrColomboColombo);
        Assert.assertEquals(0, results.size());

        bcs.setGender(0);
        bcs.setDateOfBirth(null);
        bcs.setMotherFullName("zzzz");
        bcs.setDateOfBirth(dfm.parse("2010-07-22"));

        bcs.setSearchUKey(0);
        bcs.setApplicationNo("6");
        results = birthRegSvc.performBirthCertificateSearch(bcs, adrColomboColombo);
        Assert.assertEquals(2, results.size());

        bcs.setMotherFullName("zzzz");
        bcs.setChildFullNameEnglish(null);
        bcs.setDateOfBirth(dfm.parse("2010-07-21"));
        bcs.setSearchUKey(0);
        bcs.setApplicationNo("7");
        results = birthRegSvc.performBirthCertificateSearch(bcs, adrColomboColombo);
        Assert.assertEquals(1, results.size());

        // search on issued date

        bcs.setDateOfBirth(null);
        bcs.setMotherFullName(null);
        bcs.setDateOfRegistration(new Date()); // TODO
        bcs.setSearchUKey(0);
        bcs.setApplicationNo("8");
        results = birthRegSvc.performBirthCertificateSearch(bcs, adrColomboColombo);
        Assert.assertEquals(3, results.size());
    }
}