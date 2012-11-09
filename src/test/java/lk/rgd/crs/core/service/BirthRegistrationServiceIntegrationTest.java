package lk.rgd.crs.core.service;

import junit.framework.Assert;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.CertificateSearch;

import java.util.Date;
import java.util.List;

/**
 * @author asankha
 */
public class BirthRegistrationServiceIntegrationTest extends BirthRegistrationServiceTest {

    public void testBirthRecordIndexing() throws Exception {

        BirthDeclaration bdf1 = getMinimalBDF(2010108, DateTimeUtils.getDateFromISO8601String("2010-07-21"), colomboBDDivision);
        bdf1.getChild().setChildFullNameEnglish("aaaa bbbb cccc zzzz");
        bdf1.getChild().setChildGender(0);
        bdf1.getParent().setMotherFullName("hhhh iiii zzzz");
        birthRegSvc.addLiveBirthDeclaration(bdf1, false, deoColomboColombo);
        birthRegSvc.approveLiveBirthDeclaration(bdf1.getIdUKey(), true, adrColomboColombo);
        birthRegSvc.markLiveBirthConfirmationAsPrinted(bdf1, deoColomboColombo);
        bdf1 = birthRegSvc.getById(bdf1.getIdUKey(), deoColomboColombo);
        birthRegSvc.markLiveBirthDeclarationAsConfirmedWithoutChanges(bdf1, deoColomboColombo);

        BirthDeclaration bdf2 = getMinimalBDF(2010109, DateTimeUtils.getDateFromISO8601String("2010-07-22"), colomboBDDivision);
        bdf2.getChild().setChildFullNameEnglish("eeee ffff gggg zzzz");
        bdf2.getChild().setChildGender(0);
        bdf2.getParent().setMotherFullName("anothermothernameone anothermothernametwo");
        birthRegSvc.addLiveBirthDeclaration(bdf2, false, deoColomboColombo);
        birthRegSvc.approveLiveBirthDeclaration(bdf2.getIdUKey(), true, adrColomboColombo);
        birthRegSvc.markLiveBirthConfirmationAsPrinted(bdf2, deoColomboColombo);
        bdf2 = birthRegSvc.getById(bdf2.getIdUKey(), deoColomboColombo);
        birthRegSvc.markLiveBirthDeclarationAsConfirmedWithoutChanges(bdf2, deoColomboColombo);

        BirthDeclaration bdf3 = getMinimalBDF(2010110, DateTimeUtils.getDateFromISO8601String("2010-07-22"), colomboBDDivision);
        bdf3.getChild().setChildFullNameEnglish("hhhh iiii zzzz");
        bdf3.getChild().setChildGender(0);
        bdf3.getParent().setMotherFullName("anothermothernameone anothermothernametwo");
        birthRegSvc.addLiveBirthDeclaration(bdf3, false, deoColomboColombo);
        birthRegSvc.approveLiveBirthDeclaration(bdf3.getIdUKey(), true, adrColomboColombo);
        birthRegSvc.markLiveBirthConfirmationAsPrinted(bdf3, deoColomboColombo);
        bdf3 = birthRegSvc.getById(bdf3.getIdUKey(), deoColomboColombo);
        birthRegSvc.markLiveBirthDeclarationAsConfirmedWithoutChanges(bdf3, deoColomboColombo);

        CertificateSearch cs = new CertificateSearch();
        cs.getCertificate().setApplicantFullName("applicant name");
        cs.getCertificate().setApplicantAddress("applicant address");
        cs.getCertificate().setDsDivision(colomboBDDivision.getDsDivision());
        cs.getCertificate().setApplicationNo("1");
        cs.getCertificate().setDateOfSubmission(new Date());

        // should find when the search term does occur within the field
        cs.getSearch().setSearchFullNameEnglish("aaaa bbbb");
        List<BirthDeclaration> results = certSearchSvc.performBirthCertificateSearch(cs, adrColomboColombo,1,1);
        Assert.assertEquals(1, results.size());

        // should find when the search term does occur within the field
        cs.getSearch().setSearchFullNameEnglish("ffff zzzz");
        cs.setIdUKey(0);
        cs.getCertificate().setApplicationNo("2");
        results = certSearchSvc.performBirthCertificateSearch(cs, adrColomboColombo,1,1);
        Assert.assertEquals(1, results.size());

        // should not find when the search term does not occur
        cs.getSearch().setSearchFullNameEnglish("xxxx yyyy");
        cs.setIdUKey(0);
        cs.getCertificate().setApplicationNo("3");
        results = certSearchSvc.performBirthCertificateSearch(cs, adrColomboColombo,1,1);
        Assert.assertEquals(0, results.size());

        cs.getSearch().setGender(0);
        cs.getSearch().setSearchFullNameEnglish("zzzz");
        cs.setIdUKey(0);
        cs.getCertificate().setApplicationNo("4");
        results = certSearchSvc.performBirthCertificateSearch(cs, adrColomboColombo,1,1);
        Assert.assertEquals(3, results.size());

        // should not match zzzz for gender = 1 as gender is an exact match
        cs.getSearch().setGender(1);
        cs.setIdUKey(0);
        cs.getCertificate().setApplicationNo("5");
        results = certSearchSvc.performBirthCertificateSearch(cs, adrColomboColombo,1,1);
        Assert.assertEquals(0, results.size());

        cs.getSearch().setGender(0);
        cs.getSearch().setDateOfEvent(null);
        cs.getSearch().setMotherFullName("zzzz");
        cs.getSearch().setDateOfEvent(DateTimeUtils.getDateFromISO8601String("2010-07-22"));

        cs.setIdUKey(0);
        cs.getCertificate().setApplicationNo("6");
        results = certSearchSvc.performBirthCertificateSearch(cs, adrColomboColombo,1,1);
        Assert.assertEquals(2, results.size());

        cs.getSearch().setMotherFullName("zzzz");
        cs.getSearch().setSearchFullNameEnglish(null);
        cs.getSearch().setDateOfEvent(DateTimeUtils.getDateFromISO8601String("2010-07-21"));
        cs.setIdUKey(0);
        cs.getCertificate().setApplicationNo("7");
        results = certSearchSvc.performBirthCertificateSearch(cs, adrColomboColombo,1,1);
        Assert.assertEquals(1, results.size());

        // search on issued date

        cs.getSearch().setDateOfEvent(null);
        cs.getSearch().setMotherFullName(null);
        cs.getSearch().setCertificateIssueDate(new Date()); // TODO
        cs.setIdUKey(0);
        cs.getCertificate().setApplicationNo("8");
        results = certSearchSvc.performBirthCertificateSearch(cs, adrColomboColombo,1,1);
        Assert.assertEquals(3, results.size());
    }
}