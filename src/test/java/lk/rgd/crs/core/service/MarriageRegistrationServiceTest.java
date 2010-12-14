package lk.rgd.crs.core.service;

import junit.framework.TestCase;
import lk.rgd.ErrorCodes;
import lk.rgd.UnitTestManager;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.MRDivisionDAO;
import lk.rgd.crs.api.dao.MarriageRegistrationDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.MarriageRegistrationService;
import lk.rgd.crs.web.util.MarriageType;
import lk.rgd.crs.web.util.TypeOfMarriagePlace;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.List;

/**
 * @author amith jayasekara
 */
public class MarriageRegistrationServiceTest extends TestCase {

    protected final ApplicationContext ctx = UnitTestManager.ctx;
    protected final MarriageRegistrationService marriageRegistrationService;
    protected final CountryDAO countryDAO;
    protected final RaceDAO raceDAO;
    protected final UserManager userManager;
    protected final MRDivision colomboMRDivision;
    protected final MRDivisionDAO mrDivisionDAO;
    protected MarriageRegistrationDAO marriageRegistrationDAO;
    protected User deoColomboColombo;
    protected User deoGampahaNegambo;
    protected User adrColomboColombo;
    protected User adrGampahaNegambo;
    protected User rg;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        marriageRegistrationDAO = (MarriageRegistrationDAO)
            ctx.getBean("marriageRegistrationDAOImpl", MarriageRegistrationDAO.class);
    }

    public MarriageRegistrationServiceTest() {
        countryDAO = (CountryDAO) ctx.getBean("countryDAOImpl", CountryDAO.class);
        raceDAO = (RaceDAO) ctx.getBean("raceDAOImpl", RaceDAO.class);
        userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);
        mrDivisionDAO = (MRDivisionDAO) ctx.getBean("mrDivisionDAOImpl", MRDivisionDAO.class);
        marriageRegistrationService = (MarriageRegistrationService) ctx.getBean("marriageRegistrationService",
            MarriageRegistrationService.class);
        try {
            deoColomboColombo = userManager.authenticateUser("deo-colombo-colombo", "password");
            adrColomboColombo = userManager.authenticateUser("adr-colombo-colombo", "password");
            deoGampahaNegambo = userManager.authenticateUser("deo-gampaha-negambo", "password");
            adrGampahaNegambo = userManager.authenticateUser("adr-gampaha-negambo", "password");
            rg = userManager.authenticateUser("rg", "password");
        } catch (AuthorizationException e) {
            throw new IllegalArgumentException("Cannot authenticate sample users");
        }
        colomboMRDivision = mrDivisionDAO.getMRDivisionByPK(1);
    }

    public void testAddMinimalMarriageNotice() {
        //adding a marriage notice with minimal requirements
        MarriageRegister notice = getMinimalMarriageNotice(2010012345L, colomboMRDivision, false, "1234567890",
            "1234567899", MarriageNotice.Type.MALE_NOTICE, MarriageRegister.LicenseCollectType.MAIL_TO_MALE);
        //assuming this is male notice
        //and male party is expecting the license
        //   notice.setLicenseRequestByMale(true);
        marriageRegistrationService.addMarriageNotice(notice, MarriageNotice.Type.MALE_NOTICE, rg);
        //add with same pin numbers
        //todo amith
    }

    public void testAddSecondNotice() {

        //now test a normal add second notice process
        MarriageRegister simpleFirstNotice = getMinimalMarriageNotice(2010045678L, colomboMRDivision, false, "4567891230",
            "1234569875", MarriageNotice.Type.FEMALE_NOTICE, MarriageRegister.LicenseCollectType.MAIL_TO_MALE);
        marriageRegistrationService.addMarriageNotice(simpleFirstNotice, MarriageNotice.Type.FEMALE_NOTICE, rg);
        MarriageRegister simpleSecondNotice = marriageRegistrationService.
            getMarriageNoticePendingApprovalByMRDivisionAndSerial(colomboMRDivision, 2010045678L, true, rg).get(0);
        simpleSecondNotice.setSerialOfMaleNotice(2010045685L);
        //adding
        List<UserWarning> notExpecting = marriageRegistrationService.addSecondMarriageNotice(simpleSecondNotice,
            MarriageNotice.Type.MALE_NOTICE, false, false, rg);
        assertEquals("not expecting warnings while add simple process", notExpecting.size(), 0);

        //try to add a second notice for single notice type that mean no second notice expecting exception
        MarriageRegister singleNotice = getMinimalMarriageNotice(2010012347L, colomboMRDivision, true, "1234567893",
            "1234567892", MarriageNotice.Type.BOTH_NOTICE, MarriageRegister.LicenseCollectType.MAIL_TO_MALE);
        marriageRegistrationService.addMarriageNotice(singleNotice, MarriageNotice.Type.BOTH_NOTICE, rg);
        try {
            marriageRegistrationService.addSecondMarriageNotice(marriageRegistrationService.
                getMarriageNoticePendingApprovalByMRDivisionAndSerial(colomboMRDivision, 2010012347L, true, rg).get(0),
                MarriageNotice.Type.BOTH_NOTICE, true, false, rg);
        } catch (CRSRuntimeException expected) {
            //expecting exception     6007
            assertEquals("expecting invalid notice type for add second notice", expected.getErrorCode(),
                ErrorCodes.INVALID_NOTICE_TYPE_FOR_ADD_SECOND);
        }

        //try to add a second notice
        MarriageRegister maleNotice = getMinimalMarriageNotice(2010012347L, colomboMRDivision, false, "1234567893",
            "1234567892", MarriageNotice.Type.MALE_NOTICE, MarriageRegister.LicenseCollectType.MAIL_TO_MALE);
        //and male is declaring female as the license owner
        maleNotice.setLicenseCollectType(MarriageRegister.LicenseCollectType.MAIL_TO_FEMALE);
        marriageRegistrationService.addMarriageNotice(maleNotice, MarriageNotice.Type.MALE_NOTICE, rg);
        //colombo adr approving male notice now
        marriageRegistrationService.approveMarriageNotice(marriageRegistrationService.
            getMarriageNoticePendingApprovalByMRDivisionAndSerial(colomboMRDivision, 2010012347L, true, rg).get(0).
            getIdUKey(), MarriageNotice.Type.MALE_NOTICE, rg);
        //now female is try to add second notice for above notice but she try to allocate license to male in that
        // case warnings are issue
        MarriageRegister femaleNotice = marriageRegistrationService.getMarriageNoticePendingApprovalByMRDivisionAndSerial
            (colomboMRDivision, 2010012347L, true, rg).get(0);
        femaleNotice.setLicenseCollectType(MarriageRegister.LicenseCollectType.MAIL_TO_MALE);
        List<UserWarning> warnings = marriageRegistrationService.addSecondMarriageNotice(femaleNotice,
            MarriageNotice.Type.FEMALE_NOTICE, false, false, rg);
        //expecting warnings
        if (warnings.size() != 1) {
            fail("expecting a warning");
        }
        //now we have two options either undo the state of the first notice or proceed with the current license
        //collecting party in that case female has to accept male's declaration.
        //now we are undo the state of the first notice
        marriageRegistrationService.addSecondMarriageNotice(femaleNotice, MarriageNotice.Type.FEMALE_NOTICE, true, true, rg);
        //now state must be in DE and license collected party must be MAIL_TO_MALE
        MarriageRegister testUndo = marriageRegistrationService.getMarriageNoticePendingApprovalByMRDivisionAndSerial
            (colomboMRDivision, 2010012347L, true, rg).get(0);
        if (testUndo.getState() != MarriageRegister.State.DATA_ENTRY) {
            fail("state must be in DATA_ENTRY after undo");
        }
        if (testUndo.getLicenseCollectType() != MarriageRegister.LicenseCollectType.MAIL_TO_MALE) {
            fail("license collecting party must be the MAIL_TO_MALE");
        }
        //todo check the other process proceed
    }

    public void testMarriageNoticeApproval() {
        //add male notice license is expecting by male party
        MarriageRegister malePartySubmittedNotice = getMinimalMarriageNotice(2010012346L, colomboMRDivision, false,
            "1234567899", "1234567898", MarriageNotice.Type.MALE_NOTICE, MarriageRegister.LicenseCollectType.MAIL_TO_MALE);
        //set female as the license request party
        marriageRegistrationService.addMarriageNotice(malePartySubmittedNotice, MarriageNotice.Type.MALE_NOTICE, rg);
        try {//now try to approve this record
            //this record is not a single record so it is expecting FEMALE notice and unless female party notice is approved
            // this record can't be approved }
            List<MarriageRegister> noticeList = marriageRegistrationService.
                getMarriageNoticePendingApprovalByMRDivisionAndSerial(colomboMRDivision, 2010012346L, true, rg);
            marriageRegistrationService.approveMarriageNotice(noticeList.get(0).getIdUKey(),
                MarriageNotice.Type.MALE_NOTICE, rg);
        }
        catch (CRSRuntimeException expected) {
            //expected exception is approve female first 6006
            assertEquals("Other party must approve first", ErrorCodes.OTHER_PARTY_MUST_APPROVE_FIRST,
                expected.getErrorCode());
        }
        //now we are adding second notice for existing male notice
        //getting existing
        MarriageRegister existingMaleNotice = marriageRegistrationService.
            getMarriageNoticePendingApprovalByMRDivisionAndSerial(colomboMRDivision, 2010012346L, true, rg).get(0);
        //now setting mandatory female notice info
        FemaleParty female = new FemaleParty();
        female.setIdentificationNumberFemale("1234567897");
        female.setDateOfBirthFemale(new Date());
        female.setNameInOfficialLanguageFemale("name in official language");
        //add female notice related data
        existingMaleNotice.setFemale(female);
        existingMaleNotice.setDateOfFemaleNotice(new Date());
        existingMaleNotice.setSerialOfFemaleNotice(2010012347L);
        marriageRegistrationService.addSecondMarriageNotice(existingMaleNotice, MarriageNotice.Type.FEMALE_NOTICE, false,
            false, rg);
        //still male notice cannot be approved
        try {
            marriageRegistrationService.approveMarriageNotice(marriageRegistrationService.
                getMarriageNoticePendingApprovalByMRDivisionAndSerial(colomboMRDivision, 2010012346L, true, rg).get(0).
                getIdUKey(), MarriageNotice.Type.MALE_NOTICE, rg);
        } catch (CRSRuntimeException expected) {
            //expected exception is approve female first 6006
            assertEquals("still other party must approve first", ErrorCodes.OTHER_PARTY_MUST_APPROVE_FIRST,
                expected.getErrorCode());
        }

        //now we are approving female notice and it does not have any restriction only need to be in DATA entry state
        MarriageRegister existingFemaleNotice = marriageRegistrationService.
            getMarriageNoticePendingApprovalByMRDivisionAndSerial(colomboMRDivision, 2010012347L, true, rg).get(0);
        try {
            //approving female notice
            marriageRegistrationService.approveMarriageNotice(existingFemaleNotice.getIdUKey(),
                MarriageNotice.Type.FEMALE_NOTICE, rg);
        } catch (CRSRuntimeException notExpecting) {
            //we are not expecting exceptions here
            fail("exception not expecting while approve female notice");
        }
        //now there is no restriction for approving male notice
        MarriageRegister existingMaleNoticeCanApprove = marriageRegistrationService.
            getMarriageNoticePendingApprovalByMRDivisionAndSerial(colomboMRDivision, 2010012346L, true, rg).get(0);
        try {
            marriageRegistrationService.approveMarriageNotice(existingMaleNoticeCanApprove.getIdUKey(),
                MarriageNotice.Type.MALE_NOTICE, rg);
        } catch (CRSRuntimeException notExpected) {
            //we are not expecting exceptions here
            fail("exception not expecting while approve male notice");
        }

    }


    private MarriageRegister getMinimalMarriageNotice(long serialMale, MRDivision mrDivision, boolean isSingleNotice,
        String malePin, String femalePin, MarriageNotice.Type type, MarriageRegister.LicenseCollectType licenseCollectType) {

        MarriageRegister notice = new MarriageRegister();
        //male party
        MaleParty male = new MaleParty();
        male.setIdentificationNumberMale(malePin);
        male.setDateOfBirthMale(new Date());
        male.setNameInEnglishMale("name in english" + malePin);
        //female party
        FemaleParty female = new FemaleParty();
        female.setIdentificationNumberFemale(femalePin);
        female.setDateOfBirthFemale(new Date());
        female.setNameInEnglishFemale("name in english" + femalePin);

        notice.setSingleNotice(isSingleNotice);
        notice.setTypeOfMarriage(MarriageType.GENERAL);
        notice.setTypeOfMarriagePlace(TypeOfMarriagePlace.REGISTRAR_OFFICE);
        notice.setLicenseCollectType(licenseCollectType);
        switch (type) {
            case BOTH_NOTICE:
                notice.setFemale(female);
            case MALE_NOTICE:
                notice.setMale(male);
                notice.setDateOfMaleNotice(new Date());
                notice.setMrDivisionOfMaleNotice(mrDivision);
                notice.setSerialOfMaleNotice(serialMale);
                break;
            case FEMALE_NOTICE:
                notice.setFemale(female);
                notice.setDateOfFemaleNotice(new Date());
                notice.setMrDivisionOfFemaleNotice(mrDivision);
                notice.setSerialOfFemaleNotice(serialMale);
        }
        return notice;
    }

}
