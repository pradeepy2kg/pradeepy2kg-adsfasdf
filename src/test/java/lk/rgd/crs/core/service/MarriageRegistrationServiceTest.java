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
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.domain.PersonCitizenship;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author amith jayasekara
 */
public class MarriageRegistrationServiceTest extends TestCase {
    private static final Logger logger = LoggerFactory.getLogger(MarriageRegistrationServiceTest.class);
    protected final ApplicationContext ctx = UnitTestManager.ctx;
    protected final MarriageRegistrationService marriageRegistrationService;
    protected final PopulationRegistry populationRegistry;
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
        populationRegistry = (PopulationRegistry) ctx.getBean("ecivilService", PopulationRegistry.class);
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

    private Person createPersonForMarriage() {
        Person person = new Person();
        person.setDateOfRegistration(new Date());
        person.setRace(raceDAO.getRace(1));
        person.setDateOfBirth(new Date());
        person.setPlaceOfBirth("place of birth");
        person.setFullNameInOfficialLanguage("full name in ol");
        person.setFullNameInEnglishLanguage("full name in en");
        person.setCivilStatus(Person.CivilStatus.NEVER_MARRIED);
        person.setPermanentAddress("permanent address");
        person.setStatus(Person.Status.DATA_ENTRY);
        populationRegistry.addExistingPerson(person, Collections.<PersonCitizenship>emptyList(), true, rg);
        // approve person
        populationRegistry.approvePerson(person.getPersonUKey(), true, rg);
        logger.error("generated person and approved");
        return populationRegistry.getByUKey(person.getPersonUKey(), rg);
    }

    public void testAddMinimalMarriageNotice() {
        //adding a marriage notice with minimal requirements
        MarriageRegister notice = getMinimalMarriageNotice(2010012345L, colomboMRDivision, false,
            createPersonForMarriage().getPin(), createPersonForMarriage().getPin(), MarriageNotice.Type.MALE_NOTICE,
            MarriageRegister.LicenseCollectType.MAIL_TO_MALE);
        //assuming this is male notice
        //and male party is expecting the license
        //   notice.setLicenseRequestByMale(true);
        marriageRegistrationService.addMarriageNotice(notice, MarriageNotice.Type.MALE_NOTICE, rg);
        //now try to add a another MALE_NOTICE with same serial number
        MarriageRegister noticeWithSameSerial = getMinimalMarriageNotice(2010012345L, colomboMRDivision, false,
            createPersonForMarriage().getPin(),createPersonForMarriage().getPin(), MarriageNotice.Type.MALE_NOTICE,
            MarriageRegister.LicenseCollectType.MAIL_TO_MALE);
        try {
            marriageRegistrationService.addMarriageNotice(noticeWithSameSerial, MarriageNotice.Type.MALE_NOTICE, rg);
        } catch (CRSRuntimeException exceptionExpected) {
            assertEquals("Exception expected when adding same serial in same MRDivision",
                ErrorCodes.POSSIBLE_MARRIAGE_NOTICE_SERIAL_NUMBER_DUPLICATION, exceptionExpected.getErrorCode());
        }
        //add with same pin numbers
        //todo amith
    }

    public void testAddSecondNotice() {

        //now test a normal add second notice process
        MarriageRegister simpleFirstNotice = getMinimalMarriageNotice(2010045678L, colomboMRDivision, false,
            createPersonForMarriage().getPin(),createPersonForMarriage().getPin(), MarriageNotice.Type.FEMALE_NOTICE,
            MarriageRegister.LicenseCollectType.MAIL_TO_MALE);
        marriageRegistrationService.addMarriageNotice(simpleFirstNotice, MarriageNotice.Type.FEMALE_NOTICE, rg);
        MarriageRegister simpleSecondNotice = marriageRegistrationService.
            getActiveRecordByMRDivisionAndSerialNo(colomboMRDivision, 2010045678L, rg);
        simpleSecondNotice.setSerialOfMaleNotice(2010045685L);
        simpleSecondNotice.setDateOfMaleNotice(new Date());
        simpleSecondNotice.getMale().setIdentificationNumberMale(createPersonForMarriage().getPin());
        simpleSecondNotice.getMale().setDateOfBirthMale(new Date());
        simpleSecondNotice.setMrDivisionOfMaleNotice(colomboMRDivision);
        //adding
        List<UserWarning> notExpecting = marriageRegistrationService.addSecondMarriageNotice(simpleSecondNotice,
            MarriageNotice.Type.MALE_NOTICE, false, false, rg);
        assertEquals("not expecting warnings while add simple process", notExpecting.size(), 0);

        //try to add a second notice for single notice type that mean no second notice expecting exception
        MarriageRegister singleNotice = getMinimalMarriageNotice(2010012357L, colomboMRDivision, true,
            createPersonForMarriage().getPin(),createPersonForMarriage().getPin(), MarriageNotice.Type.BOTH_NOTICE,
            MarriageRegister.LicenseCollectType.MAIL_TO_MALE);
        marriageRegistrationService.addMarriageNotice(singleNotice, MarriageNotice.Type.BOTH_NOTICE, rg);
        try {
            marriageRegistrationService.addSecondMarriageNotice(marriageRegistrationService.
                getActiveRecordByMRDivisionAndSerialNo(colomboMRDivision, 2010012357L, rg),
                MarriageNotice.Type.BOTH_NOTICE, true, false, rg);
        } catch (CRSRuntimeException expected) {
            //expecting exception     6007
            assertEquals("expecting invalid notice type for add second notice", expected.getErrorCode(),
                ErrorCodes.INVALID_NOTICE_TYPE_FOR_ADD_SECOND);
        }

        //try to add a second notice
        MarriageRegister maleNotice = getMinimalMarriageNotice(2010012347L, colomboMRDivision, false,
            createPersonForMarriage().getPin(),createPersonForMarriage().getPin(), MarriageNotice.Type.MALE_NOTICE,
            MarriageRegister.LicenseCollectType.MAIL_TO_MALE);
        //and male is declaring female as the license owner
        maleNotice.setLicenseCollectType(MarriageRegister.LicenseCollectType.MAIL_TO_FEMALE);
        marriageRegistrationService.addMarriageNotice(maleNotice, MarriageNotice.Type.MALE_NOTICE, rg);
        //colombo adr approving male notice now
        marriageRegistrationService.approveMarriageNotice(marriageRegistrationService.
            getActiveRecordByMRDivisionAndSerialNo(colomboMRDivision, 2010012347L, rg).getIdUKey(),
            MarriageNotice.Type.MALE_NOTICE, true, rg);
        //now female is try to add second notice for above notice but she try to allocate license to male in that
        // case warnings are issue
        MarriageRegister femaleNotice = marriageRegistrationService.
            getActiveRecordByMRDivisionAndSerialNo(colomboMRDivision, 2010012347L, rg);

        femaleNotice.setLicenseCollectType(MarriageRegister.LicenseCollectType.MAIL_TO_MALE);

        femaleNotice.setSerialOfFemaleNotice(2010045687L);
        femaleNotice.setDateOfFemaleNotice(new Date());
        femaleNotice.getFemale().setIdentificationNumberFemale(createPersonForMarriage().getPin());
        femaleNotice.getFemale().setDateOfBirthFemale(new Date());
        femaleNotice.setMrDivisionOfFemaleNotice(colomboMRDivision);

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
        MarriageRegister testUndo = marriageRegistrationService.
            getActiveRecordByMRDivisionAndSerialNo(colomboMRDivision, 2010012347L, rg);

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
            createPersonForMarriage().getPin(), createPersonForMarriage().getPin(),
            MarriageNotice.Type.MALE_NOTICE, MarriageRegister.LicenseCollectType.MAIL_TO_MALE);
        //set female as the license request party
        marriageRegistrationService.addMarriageNotice(malePartySubmittedNotice, MarriageNotice.Type.MALE_NOTICE, rg);
        try {//now try to approve this record
            //this record is not a single record so it is expecting FEMALE notice and unless female party notice is approved
            // this record can't be approved }
            marriageRegistrationService.approveMarriageNotice(marriageRegistrationService.
                getActiveRecordByMRDivisionAndSerialNo(colomboMRDivision, 2010012346L, rg).getIdUKey(),
                MarriageNotice.Type.MALE_NOTICE, true, rg);
        } catch (CRSRuntimeException expected) {
            //expected exception is approve female first 6006
            assertEquals("Other party must approve first", ErrorCodes.OTHER_PARTY_MUST_APPROVE_FIRST,
                expected.getErrorCode());
        }
        //now we are adding second notice for existing male notice
        //getting existing
        MarriageRegister existingMaleNotice = marriageRegistrationService.
            getActiveRecordByMRDivisionAndSerialNo(colomboMRDivision, 2010012346L, rg);
        //now setting mandatory female notice info
        FemaleParty female = new FemaleParty();
        female.setIdentificationNumberFemale(createPersonForMarriage().getPin());
        female.setDateOfBirthFemale(new Date());
        female.setNameInOfficialLanguageFemale("name in official language");
        //add female notice related data
        existingMaleNotice.setFemale(female);
        existingMaleNotice.setDateOfFemaleNotice(new Date());
        existingMaleNotice.setSerialOfFemaleNotice(2010012348L);
        existingMaleNotice.setMrDivisionOfFemaleNotice(colomboMRDivision);
        marriageRegistrationService.addSecondMarriageNotice(existingMaleNotice, MarriageNotice.Type.FEMALE_NOTICE, false,
            false, rg);
        //still male notice cannot be approved
        try {
            marriageRegistrationService.approveMarriageNotice(marriageRegistrationService.
                getActiveRecordByMRDivisionAndSerialNo(colomboMRDivision, 2010012346L, rg).getIdUKey(),
                MarriageNotice.Type.MALE_NOTICE, true, rg);

        } catch (CRSRuntimeException expected) {
            //expected exception is approve female first 6006
            assertEquals("still other party must approve first", ErrorCodes.OTHER_PARTY_MUST_APPROVE_FIRST,
                expected.getErrorCode());
        }

        //now we are approving female notice and it does not have any restriction only need to be in DATA entry state
        MarriageRegister existingFemaleNotice = marriageRegistrationService.getActiveRecordByMRDivisionAndSerialNo(colomboMRDivision, 2010012348L, rg);
        try {
            //approving female notice
            marriageRegistrationService.approveMarriageNotice(existingFemaleNotice.getIdUKey(),
                MarriageNotice.Type.FEMALE_NOTICE, true, rg);
        } catch (CRSRuntimeException notExpecting) {
            //we are not expecting exceptions here
            fail("exception not expecting while approve female notice");
        }
        //now there is no restriction for approving male notice
        MarriageRegister existingMaleNoticeCanApprove = marriageRegistrationService.
            getActiveRecordByMRDivisionAndSerialNo(colomboMRDivision, 2010012346L, rg);

        try {
            marriageRegistrationService.approveMarriageNotice(existingMaleNoticeCanApprove.getIdUKey(),
                MarriageNotice.Type.MALE_NOTICE, true, rg);
        } catch (CRSRuntimeException notExpected) {
            //we are not expecting exceptions here
            fail("exception not expecting while approve male notice");
        }

    }
    //testing rejection of a marriage notice

    public void testMarriageNoticeReject() {
        //check process
        //rejecting when record in DATA_ENTRY stage (no matter how many notices are available)
        MarriageRegister noticeInDE = getMinimalMarriageNotice(2010012586L, colomboMRDivision, false,
            createPersonForMarriage().getPin(),createPersonForMarriage().getPin(),
            MarriageNotice.Type.MALE_NOTICE, MarriageRegister.LicenseCollectType.MAIL_TO_FEMALE);
        marriageRegistrationService.addMarriageNotice(noticeInDE, MarriageNotice.Type.MALE_NOTICE, rg);
        //now noticeInDE is in DE and it can be rejected
        marriageRegistrationService.rejectMarriageNotice(marriageRegistrationService.
            getMarriageNoticePendingApprovalByMRDivisionAndSerial(colomboMRDivision, 2010012586L, true, rg).get(0).
            getIdUKey(), MarriageNotice.Type.MALE_NOTICE, "testing reject for DE mode", rg);
        //check it is being rejected properly
        MarriageRegister noticeInDERejected = marriageRegistrationService.getByIdUKey(noticeInDE.getIdUKey(), rg);
        assertEquals("Active false", false, noticeInDERejected.getLifeCycleInfo().isActiveRecord());
        assertEquals("State in REJECTED", MarriageRegister.State.NOTICE_REJECTED, noticeInDERejected.getState());
        assertEquals("Rejection comment", "testing reject for DE mode", noticeInDERejected.getNoticeRejectionComment());

        //rejecting a notice with state MALE_NOTICE_APPROVED
        MarriageRegister noticeInMNA = getMinimalMarriageNotice(2010012587L, colomboMRDivision, false,
            createPersonForMarriage().getPin(), createPersonForMarriage().getPin(),
            MarriageNotice.Type.MALE_NOTICE, MarriageRegister.LicenseCollectType.MAIL_TO_FEMALE);
        noticeInMNA.setLicenseCollectType(MarriageRegister.LicenseCollectType.MAIL_TO_FEMALE);
        marriageRegistrationService.addMarriageNotice(noticeInMNA, MarriageNotice.Type.MALE_NOTICE, rg);
        //approving male notice
        marriageRegistrationService.approveMarriageNotice(marriageRegistrationService.
            getMarriageNoticePendingApprovalByMRDivisionAndSerial(colomboMRDivision, 2010012587L, true, rg).get(0).
            getIdUKey(), MarriageNotice.Type.MALE_NOTICE, true, rg);

        MarriageRegister noticeInMNAApproved = marriageRegistrationService.
            getMarriageNoticePendingApprovalByMRDivisionAndSerial(colomboMRDivision, 2010012587L, true, rg).get(0);
        //adding second notice (female notice)
        noticeInMNAApproved.setSerialOfFemaleNotice(2010078954L);
        noticeInMNAApproved.setMrDivisionOfFemaleNotice(colomboMRDivision);
        noticeInMNAApproved.setDateOfFemaleNotice(new Date());
        noticeInMNAApproved.getFemale().setIdentificationNumberFemale(createPersonForMarriage().getPin());
        noticeInMNAApproved.getFemale().setDateOfBirthFemale(new Date());

        marriageRegistrationService.addSecondMarriageNotice(noticeInMNAApproved, MarriageNotice.Type.FEMALE_NOTICE,
            true, false, rg);
        //now it's being approved and second being added
        //now try to reject female notice
        MarriageRegister noticeInMNSecondAdded = marriageRegistrationService.
            getMarriageNoticePendingApprovalByMRDivisionAndSerial(colomboMRDivision, 2010012587L, true, rg).get(0);
        marriageRegistrationService.rejectMarriageNotice(noticeInMNSecondAdded.getIdUKey(),
            MarriageNotice.Type.FEMALE_NOTICE, "second add female notice is rejected", rg);
        //now its being rejected
        MarriageRegister noticeInMNSecondRejected = marriageRegistrationService.getByIdUKey(noticeInMNAApproved.getIdUKey(), rg);

        assertEquals("Active false", false, noticeInMNSecondRejected.getLifeCycleInfo().isActiveRecord());
        assertEquals("State in REJECTED", MarriageRegister.State.FEMALE_NOTICE_REJECTED, noticeInMNSecondRejected.getState());
        assertEquals("Rejection comment", "second add female notice is rejected", noticeInMNSecondRejected.getNoticeRejectionComment());
        //check exceptions
        // exceptions can happen in two ways
        //1>invalid state for rejection
        //2>invalid notice type for reject
        //case 1: we try to reject a notice which is in NOTICE_APPROVED state
        MarriageRegister noticeInNoticeApproved = getMinimalMarriageNotice(2010012575L, colomboMRDivision, false,
            createPersonForMarriage().getPin(),createPersonForMarriage().getPin(),
            MarriageNotice.Type.MALE_NOTICE, MarriageRegister.LicenseCollectType.MAIL_TO_FEMALE);
        //adding
        marriageRegistrationService.addMarriageNotice(noticeInNoticeApproved, MarriageNotice.Type.MALE_NOTICE, rg);
        MarriageRegister noticeInNoticeApprovedAdded = marriageRegistrationService.
            getMarriageNoticePendingApprovalByMRDivisionAndSerial(colomboMRDivision, 2010012575L, true, rg).get(0);
        //use a short cut way to change state in to notice approved this is not a valid process use for test purposes
        noticeInNoticeApprovedAdded.setState(MarriageRegister.State.NOTICE_APPROVED);
        //now we are try to reject but we expecting a exception in valid state
        try {
            marriageRegistrationService.rejectMarriageNotice(marriageRegistrationService.
                getMarriageNoticePendingApprovalByMRDivisionAndSerial(colomboMRDivision, 2010012575L, true, rg).get(0).
                getIdUKey(), MarriageNotice.Type.MALE_NOTICE, "try to reject notice approved record", rg);
        } catch (CRSRuntimeException expected) {
            assertEquals("expecting exception while rejecting notice approved record",
                ErrorCodes.INVALID_NOTICE_STATE_FOR_REJECT, expected.getErrorCode());
        }
        //check UNABLE_TO_REJECT_FEMALE
        MarriageRegister noticeMaleToBeApprove = getMinimalMarriageNotice(2010012585L, colomboMRDivision, false,
            createPersonForMarriage().getPin(),createPersonForMarriage().getPin(), MarriageNotice.Type.MALE_NOTICE,
            MarriageRegister.LicenseCollectType.MAIL_TO_FEMALE);
        //adding
        marriageRegistrationService.addMarriageNotice(noticeMaleToBeApprove, MarriageNotice.Type.MALE_NOTICE, rg);
        MarriageRegister noticeMaleToBeApproveAdd = marriageRegistrationService.
            getMarriageNoticePendingApprovalByMRDivisionAndSerial(colomboMRDivision, 2010012585L, true, rg).get(0);
        marriageRegistrationService.approveMarriageNotice(noticeMaleToBeApproveAdd.getIdUKey(),
            MarriageNotice.Type.MALE_NOTICE, true, rg);
        //now only female can be rejected
        //we try to reject male again
        try {
            marriageRegistrationService.rejectMarriageNotice(noticeMaleToBeApproveAdd.getIdUKey(),
                MarriageNotice.Type.MALE_NOTICE, "try to reject approved male notice", rg);
        } catch (CRSRuntimeException expected) {
            assertEquals("expecting exception while rejecting try to reject approved male notice",
                ErrorCodes.UNABLE_TO_REJECT_MALE_NOTICE, expected.getErrorCode());
        }
    }

    public void testPrintLicense() {
        //adding a marriage notice
        //male notice
        MarriageRegister maleNotice = getMinimalMarriageNotice(2010036985L, colomboMRDivision, false,
            createPersonForMarriage().getPin(),createPersonForMarriage().getPin(), MarriageNotice.Type.MALE_NOTICE,
            MarriageRegister.LicenseCollectType.MAIL_TO_FEMALE);
        //adding male notice
        marriageRegistrationService.addMarriageNotice(maleNotice, MarriageNotice.Type.MALE_NOTICE, rg);
        //get added male notice
        MarriageRegister registerRecord = marriageRegistrationService.
            getMarriageNoticePendingApprovalByMRDivisionAndSerial(colomboMRDivision, 2010036985L, true, rg).get(0);
        //add second notice female
        registerRecord.setSerialOfFemaleNotice(2010056458L);
        registerRecord.setLicenseCollectType(MarriageRegister.LicenseCollectType.MAIL_TO_FEMALE);
        registerRecord.setDateOfFemaleNotice(new Date());
        registerRecord.getFemale().setIdentificationNumberFemale(createPersonForMarriage().getPin());
        registerRecord.getFemale().setDateOfBirthFemale(new Date());

        registerRecord.setMrDivisionOfFemaleNotice(colomboMRDivision);
        //now adding female
        marriageRegistrationService.addSecondMarriageNotice(registerRecord, MarriageNotice.Type.FEMALE_NOTICE, true, false, rg);
        //approving male and state become Male notice approved
        marriageRegistrationService.approveMarriageNotice(registerRecord.getIdUKey(), MarriageNotice.Type.MALE_NOTICE, true, rg);
        //check expected exception invalid state for print license
        try {
            marriageRegistrationService.getMarriageNoticeForPrintLicense(registerRecord.getIdUKey(), rg);
        } catch (CRSRuntimeException expected) {
            //exception code 6011 expected
            assertEquals("Invalid state for print license expected",
                ErrorCodes.INVALID_STATE_FOR_PRINT_LICENSE, expected.getErrorCode());
        }
        //now we are approving female notice as well then sate become notice approved
        marriageRegistrationService.approveMarriageNotice(registerRecord.getIdUKey(), MarriageNotice.Type.FEMALE_NOTICE, true, rg);
        //now all are OK so we are not expecting any exceptions
        try {
            MarriageRegister printLicense = marriageRegistrationService.
                getMarriageNoticeForPrintLicense(registerRecord.getIdUKey(), rg);
            //state must be in LICENSE_PRINT
            assertEquals("State in NOTICE_APPROVE", MarriageRegister.State.NOTICE_APPROVED, printLicense.getState());
        } catch (CRSRuntimeException notExpecting) {
            fail("not expecting exception at printing license at this stage");
        }
    }


    private MarriageRegister getMinimalMarriageNotice(long serialMale, MRDivision mrDivision, boolean isSingleNotice,
        long malePin, long femalePin, MarriageNotice.Type type, MarriageRegister.LicenseCollectType licenseCollectType) {

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
