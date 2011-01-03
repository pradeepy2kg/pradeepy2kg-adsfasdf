package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionProxy;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;
import lk.rgd.UnitTestManager;
import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.InformantInfo;
import lk.rgd.crs.api.domain.MarriageInfo;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.action.births.BirthRegisterAction;
import lk.rgd.crs.web.action.births.BirthRegisterApprovalAction;
import lk.rgd.crs.web.action.births.PrintAction;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chathuranga Withana
 */
public class BirthRegisterActionSideFlowTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterActionSideFlowTest.class);

    private static final String LIVE_BIRTH_REGISTER_INIT_MAPPING = "/births/eprBirthRegistrationInit.do";
    private static final String STILL_BIRTH_REGISTER_INIT_MAPPING = "/births/eprStillBirthRegistrationInit.do";
    private static final String BIRTH_REGISTER_MAPPING = "/births/eprBirthRegistration.do";
    private static final String BIRTH_APPROVAL_MAPPING = "/births/eprDirectApprove.do";
    private static final String BIRTH_APPROVE_IGNORE_WARNINGS = "/births/eprDirectApproveIgnoreWarning.do";
    private static final String BIRTH_CONFIRM_PRINT = "/births/eprBirthConfirmationPrintPage.do";
    private static final String BIRTH_CONFIRM_DIRECT_PRINT = "/births/eprDirectPrintBirthConfirmation.do";
    private static final String BIRTH_CERT_DIRECT_PRINT = "/births/eprDirectPrintBirthCertificate.do";
    private static final String STILL_BIRTH_CERT_PRINT = "/births/eprStillBirthCertificatePrint.do";

    private final static ApplicationContext ctx = UnitTestManager.ctx;
    private final static BDDivisionDAO bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
    private final static DSDivisionDAO dsDivisionDAO = (DSDivisionDAO) ctx.getBean("dsDivisionDAOImpl", DSDivisionDAO.class);
    private final static RaceDAO raceDOA = (RaceDAO) ctx.getBean("raceDAOImpl", RaceDAO.class);
    private final static BirthDeclarationDAO birthDeclarationDAO = (BirthDeclarationDAO) ctx.getBean("birthDeclarationDAOImpl", BirthDeclarationDAO.class);

    private static BirthDeclaration sampleBD;
    private static BDDivision colomboBDDivision;
    private static DSDivision motherDSDivision;
    private static Race tamil;
    private final static long serialNo = 2010012960L;

    private ActionProxy proxy;
    private BirthRegisterAction action;
    private LoginAction loginAction;
    private BirthRegisterApprovalAction approveAction;
    private PrintAction printAction;

    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(BirthRegisterActionSideFlowTest.class)) {
            protected void setUp() throws Exception {

                colomboBDDivision = bdDivisionDAO.getBDDivisionByPK(2);
                motherDSDivision = dsDivisionDAO.getDSDivisionByPK(3);
                tamil = raceDOA.getRace(2);

                Calendar dob = Calendar.getInstance();
                dob.add(Calendar.DATE, -3);
                sampleBD = createSampleBD(serialNo, dob.getTime(), colomboBDDivision);

                super.setUp();
            }

            protected void tearDown() throws Exception {
                super.tearDown();
            }
        };
        return setup;
    }

    private String initAndExecute(String mapping, Map session) throws Exception {
        proxy = getActionProxy(mapping);

        if (BIRTH_REGISTER_MAPPING.equals(mapping) || LIVE_BIRTH_REGISTER_INIT_MAPPING.equals(mapping) ||
            STILL_BIRTH_REGISTER_INIT_MAPPING.equals(mapping) || STILL_BIRTH_CERT_PRINT.equals(mapping) ||
            BIRTH_CONFIRM_PRINT.equals(mapping)) {
            action = (BirthRegisterAction) proxy.getAction();
        } else if (BIRTH_APPROVAL_MAPPING.equals(mapping) || BIRTH_APPROVE_IGNORE_WARNINGS.equals(mapping)) {
            approveAction = (BirthRegisterApprovalAction) proxy.getAction();
        } else if (BIRTH_CONFIRM_DIRECT_PRINT.equals(mapping) || BIRTH_CERT_DIRECT_PRINT.equals(mapping)) {
            printAction = (PrintAction) proxy.getAction();
        }
        logger.debug("Action Method to be executed is {} ", proxy.getMethod());
        ActionContext.getContext().setSession(session);
        String result = null;
        try {
            result = proxy.execute();
        } catch (Exception e) {
            //logger.error("proxy execution error", e);     TODO
        }
        logger.debug("Result for mapping {} is {}", mapping, result);
        return result;
    }

    @Override
    protected String getContextLocations() {
        return "unitTest_applicationContext.xml";
    }

    public void testBirthRegisterActionMappingProxy() {
        ActionMapping mapping = getActionMapping(BIRTH_REGISTER_MAPPING);
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/births", mapping.getNamespace());
        assertEquals("eprBirthRegistration", mapping.getName());
        ActionProxy proxy = getActionProxy(BIRTH_REGISTER_MAPPING);
        assertNotNull(proxy);
        logger.debug("NameSpace {} and ActionName {}", mapping.getNamespace(), proxy.getMethod());

        BirthRegisterAction action = (BirthRegisterAction) proxy.getAction();
        assertNotNull(action);
    }

    /**
     * Test BirthDeclaration adding in batch mode
     */
    public void testAddNewBDFInBatchMode() throws Exception {
        Map session = login("rg", "password");
        initAndExecute(LIVE_BIRTH_REGISTER_INIT_MAPPING, session);
        session = action.getSession();

        fillBDForm1(sampleBD, session);
        fillBDForm2(session);
        fillBDForm3(sampleBD, session);
        fillBDForm4(sampleBD, session);
        session = action.getSession();

        long persistedBDId = action.getBdId();
        if (persistedBDId == 0) {
            fail("BDF not persisted");
        }

        assertEquals("Action errors found", 0, action.getActionErrors().size());
        assertEquals("Action messages not match", 1, action.getActionMessages().size());
        assertEquals("Invalid session size", 4, session.size());
        assertNull("Birth Declaration Bean can not exist in the session", session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));

        // adding new entry in batch mode
        logger.debug("Testing Add New BDF in batch mode");
        assertEquals("AddNewMode should be false", false, action.isAddNewMode());
        assertEquals("oldBdId should be 0", 0, action.getOldBdId());
        request.setParameter("oldBdId", Long.toString(persistedBDId));
        request.setParameter("addNewMode", "true");
        initAndExecute(LIVE_BIRTH_REGISTER_INIT_MAPPING, session);

        assertEquals("Invalid session size", 5, session.size());
        BirthDeclaration batchBdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertNotNull("Birth Declaration does not exist in the session", batchBdf);

        assertNotNull(batchBdf.getChild());
        assertNotNull(batchBdf.getRegister());
        assertNotNull(batchBdf.getParent());
        assertNotNull(batchBdf.getGrandFather());
        assertNotNull(batchBdf.getMarriage());
        assertNotNull(batchBdf.getInformant());
        assertNotNull(batchBdf.getNotifyingAuthority());

        // check auto populated fields correctly in batch mode
        assertEquals("BDF serial number not incremented for the BDF in session", serialNo + 1, batchBdf.getRegister().getBdfSerialNo());
        assertEquals("BDF date Of registration in session and DB are not equal", DateTimeUtils.getISO8601FormattedString(sampleBD.getRegister().getDateOfRegistration()),
            DateTimeUtils.getISO8601FormattedString(batchBdf.getRegister().getDateOfRegistration()));
        assertEquals("BDF live birth type not matched", sampleBD.getRegister().getBirthType(), batchBdf.getRegister().getBirthType());
        assertEquals("BDF District id miss match in action and session", action.getBirthDistrictId(),
            batchBdf.getRegister().getBirthDistrict().getDistrictUKey());
        assertEquals("BDF DS Division id miss match in action and session", action.getDsDivisionId(),
            batchBdf.getRegister().getDsDivision().getDsDivisionUKey());
        assertEquals("BDF Birth division id miss match in action and session",
            action.getBirthDivisionId(), batchBdf.getRegister().getBirthDivision().getBdDivisionUKey());
        assertEquals("BDF Birth division id miss match in previously added BDF and BDF in session",
            sampleBD.getRegister().getBirthDivision().getBdDivisionUKey(), batchBdf.getRegister().getBirthDivision().getBdDivisionUKey());
        assertEquals("BDF NotifyingAutho PIN miss match in previously added BDF and BDF in session",
            sampleBD.getNotifyingAuthority().getNotifyingAuthorityPIN(), batchBdf.getNotifyingAuthority().getNotifyingAuthorityPIN());
        assertEquals("BDF NotifyingAutho Name miss match in previously added BDF and BDF in session",
            sampleBD.getNotifyingAuthority().getNotifyingAuthorityName(), batchBdf.getNotifyingAuthority().getNotifyingAuthorityName());
        assertEquals("BDF NotifyingAutho Address miss match in previously added BDF and BDF in session",
            sampleBD.getNotifyingAuthority().getNotifyingAuthorityAddress(), batchBdf.getNotifyingAuthority().getNotifyingAuthorityAddress());
        assertEquals("BDF NotifyingAutho Sign Date miss match in previously added BDF and BDF in session",
            DateTimeUtils.getISO8601FormattedString(sampleBD.getNotifyingAuthority().getNotifyingAuthoritySignDate()),
            DateTimeUtils.getISO8601FormattedString(batchBdf.getNotifyingAuthority().getNotifyingAuthoritySignDate()));
        logger.debug("Adding BDF auto populating fields populated correctly and Add new in batch mode passed");
    }

    /**
     * Test back button for live birth in Birth Declaration Form
     */
    public void testBackButtonInBD() throws Exception {
        Map session = login("chathuranga", "chathuranga");
        initAndExecute(LIVE_BIRTH_REGISTER_INIT_MAPPING, session);
        session = action.getSession();
        assertEquals("Action errors for 1 of 4 BDF pages", 0, action.getActionErrors().size());

        BirthDeclaration bdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertNotNull("Birth Declaration Bean does not exist in the session", bdf);
        assertEquals("bdId not equal to zero", 0, action.getBdId());
        assertEquals("BDF live birth mis match in session and action", action.getBirthType(), bdf.getRegister().getBirthType());

        assertNotNull(bdf.getChild());
        assertNotNull(bdf.getRegister());
        assertNotNull(bdf.getParent());
        assertNotNull(bdf.getGrandFather());
        assertNotNull(bdf.getMarriage());
        assertNotNull(bdf.getInformant());
        assertNotNull(bdf.getNotifyingAuthority());

        // Filling BDF 3 pages and submitting
        fillBDForm1(sampleBD, session);
        fillBDForm2(session);
        fillBDForm3(sampleBD, session);
        session = action.getSession();
        assertEquals("session size incorrect", 5, session.size());

        // back to BDF page 2
        request.setParameter("pageNo", "2");
        request.setParameter("back", "true");
        initAndExecute(BIRTH_REGISTER_MAPPING, session);
        session = action.getSession();
        assertEquals("session size incorrect", 5, session.size());

        // back to BDF form 1
        request.setParameter("pageNo", "1");
        request.setParameter("back", "true");
        initAndExecute(BIRTH_REGISTER_MAPPING, session);
        session = action.getSession();
        assertEquals("session size incorrect", 5, session.size());
        bdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);

        logger.debug("Testing Back button");
        assertNotNull(bdf.getChild());
        assertNotNull(bdf.getRegister());
        assertNotNull(bdf.getParent());
        assertNotNull(bdf.getGrandFather());
        assertNotNull(bdf.getMarriage());
        assertNotNull(bdf.getInformant());
        assertNotNull(bdf.getNotifyingAuthority());

        // check BDF form 1 data
        assertEquals(serialNo, bdf.getRegister().getBdfSerialNo());
        assertEquals(DateTimeUtils.getISO8601FormattedString(sampleBD.getRegister().getDateOfRegistration()),
            DateTimeUtils.getISO8601FormattedString(bdf.getRegister().getDateOfRegistration()));
        assertEquals(DateTimeUtils.getISO8601FormattedString(sampleBD.getChild().getDateOfBirth()),
            DateTimeUtils.getISO8601FormattedString(bdf.getChild().getDateOfBirth()));
        assertEquals(sampleBD.getRegister().getBirthDivision().getDistrict().getDistrictUKey(),
            bdf.getRegister().getBirthDivision().getDistrict().getDistrictUKey());
        assertEquals(sampleBD.getRegister().getBirthDivision().getBdDivisionUKey(),
            bdf.getRegister().getBirthDivision().getBdDivisionUKey());
        assertEquals(sampleBD.getRegister().getBirthDivision().getDsDivision().getDsDivisionUKey(),
            bdf.getRegister().getBirthDivision().getDsDivision().getDsDivisionUKey());
        assertEquals(sampleBD.getChild().getPlaceOfBirth(), bdf.getChild().getPlaceOfBirth());
        assertEquals(sampleBD.getChild().getPlaceOfBirthEnglish(), bdf.getChild().getPlaceOfBirthEnglish());
        assertEquals(sampleBD.getChild().getBirthAtHospital(), bdf.getChild().getBirthAtHospital());
        assertEquals(sampleBD.getChild().getChildFullNameOfficialLang(), bdf.getChild().getChildFullNameOfficialLang());
        assertEquals(sampleBD.getChild().getChildFullNameEnglish(), bdf.getChild().getChildFullNameEnglish());
        assertEquals(sampleBD.getRegister().getPreferredLanguage(), bdf.getRegister().getPreferredLanguage());
        assertEquals(sampleBD.getChild().getChildGender(), bdf.getChild().getChildGender());
        assertEquals(sampleBD.getChild().getChildBirthWeight().floatValue(), bdf.getChild().getChildBirthWeight().floatValue());
        assertEquals(sampleBD.getChild().getChildRank().intValue(), bdf.getChild().getChildRank().intValue());

        // check BDF form 2 data        
        assertEquals(sampleBD.getParent().getFatherNICorPIN(), bdf.getParent().getFatherNICorPIN());
        assertEquals(sampleBD.getParent().getFatherFullName(), bdf.getParent().getFatherFullName());
        assertEquals(DateTimeUtils.getISO8601FormattedString(sampleBD.getParent().getFatherDOB()),
            DateTimeUtils.getISO8601FormattedString(bdf.getParent().getFatherDOB()));
        assertEquals(sampleBD.getParent().getFatherPlaceOfBirth(), bdf.getParent().getFatherPlaceOfBirth());
        assertEquals(sampleBD.getParent().getFatherRace().getRaceId(), bdf.getParent().getFatherRace().getRaceId());

        assertEquals(sampleBD.getParent().getMotherNICorPIN(), bdf.getParent().getMotherNICorPIN());
        assertEquals(1, bdf.getParent().getMotherCountry().getCountryId());
        assertEquals("2586598456", bdf.getParent().getMotherPassportNo());
        assertEquals("සංගුණි ෙද්ව ෙග්", bdf.getParent().getMotherFullName());
        assertEquals("1968-01-03", DateTimeUtils.getISO8601FormattedString(bdf.getParent().getMotherDOB()));
        assertEquals(42, bdf.getParent().getMotherAgeAtBirth().intValue());
        assertEquals(sampleBD.getParent().getMotherAddress(), bdf.getParent().getMotherAddress());
        assertEquals(sampleBD.getParent().getMotherDSDivision().getDistrict().getDistrictUKey(),
            bdf.getParent().getMotherDSDivision().getDistrict().getDistrictUKey());
        assertEquals(sampleBD.getParent().getMotherDSDivision().getDsDivisionUKey(),
            bdf.getParent().getMotherDSDivision().getDsDivisionUKey());
        assertEquals(sampleBD.getParent().getMotherRace().getRaceId(), bdf.getParent().getMotherRace().getRaceId());

        assertEquals("MOTHER BIRTH PLACE", bdf.getParent().getMotherPlaceOfBirth());
        assertEquals("288", bdf.getParent().getMotherAdmissionNo());
        assertEquals("mother@gmail.com", bdf.getParent().getMotherEmail());
        assertEquals("2010-06-23", DateTimeUtils.getISO8601FormattedString(bdf.getParent().getMotherAdmissionDate()));
        assertEquals("0715516541", bdf.getParent().getMotherPhoneNo());

        // check BDF form 2 data
        assertEquals(sampleBD.getMarriage().getParentsMarried().ordinal(), bdf.getMarriage().getParentsMarried().ordinal());
        assertEquals(sampleBD.getMarriage().getPlaceOfMarriage(), bdf.getMarriage().getPlaceOfMarriage());
        assertEquals(DateTimeUtils.getISO8601FormattedString(sampleBD.getMarriage().getDateOfMarriage()),
            DateTimeUtils.getISO8601FormattedString(bdf.getMarriage().getDateOfMarriage()));

        assertEquals(sampleBD.getGrandFather().getGrandFatherFullName(), bdf.getGrandFather().getGrandFatherFullName());
        assertEquals(sampleBD.getGrandFather().getGrandFatherNICorPIN(), bdf.getGrandFather().getGrandFatherNICorPIN());
        assertEquals(sampleBD.getGrandFather().getGrandFatherBirthYear().intValue(),
            bdf.getGrandFather().getGrandFatherBirthYear().intValue());
        assertEquals(sampleBD.getGrandFather().getGrandFatherBirthPlace(), bdf.getGrandFather().getGrandFatherBirthPlace());
        assertEquals(sampleBD.getGrandFather().getGreatGrandFatherFullName(), bdf.getGrandFather().getGreatGrandFatherFullName());
        assertEquals(sampleBD.getGrandFather().getGreatGrandFatherNICorPIN(), bdf.getGrandFather().getGreatGrandFatherNICorPIN());
        assertEquals(sampleBD.getGrandFather().getGreatGrandFatherBirthYear().intValue(),
            bdf.getGrandFather().getGreatGrandFatherBirthYear().intValue());
        assertEquals(sampleBD.getGrandFather().getGreatGrandFatherBirthPlace(),
            bdf.getGrandFather().getGreatGrandFatherBirthPlace());

        assertEquals(sampleBD.getInformant().getInformantType().toString(), bdf.getInformant().getInformantType().toString());
        assertEquals(sampleBD.getInformant().getInformantNICorPIN(), bdf.getInformant().getInformantNICorPIN());
        assertEquals(sampleBD.getInformant().getInformantName(), bdf.getInformant().getInformantName());
        assertEquals(sampleBD.getInformant().getInformantAddress(), bdf.getInformant().getInformantAddress());
        assertEquals(sampleBD.getInformant().getInformantPhoneNo(), bdf.getInformant().getInformantPhoneNo());
        assertEquals(sampleBD.getInformant().getInformantEmail(), bdf.getInformant().getInformantEmail());
        assertEquals(DateTimeUtils.getISO8601FormattedString(sampleBD.getInformant().getInformantSignDate()),
            DateTimeUtils.getISO8601FormattedString(bdf.getInformant().getInformantSignDate()));
    }

    /**
     * Test BirthDeclaration direct approve and print birth confirmation for live birth
     */
    public void testApproveAndPrintConfirmation() throws Exception {
        Map session = login("chathuranga", "chathuranga");
        initAndExecute(LIVE_BIRTH_REGISTER_INIT_MAPPING, session);
        session = action.getSession();

        sampleBD.getRegister().setBdfSerialNo(2010012961L);
        // Filling Birth declaration Form
        fillBDForm1(sampleBD, session);
        fillBDForm2(session);
        fillBDForm3(sampleBD, session);
        fillBDForm4(sampleBD, session);

        long persistedBDId = action.getBdId();
        if (persistedBDId == 0) {
            fail("BDF not persisted");
        }
        assertEquals(0, getBDFById(persistedBDId).getRegister().getStatus().ordinal());
        session = action.getSession();

        assertEquals("session size incorrect", 4, session.size());
        assertNull(session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));
        assertNull(session.get(WebConstants.SESSION_OLD_BD_FOR_ADOPTION));
        assertEquals("BDF approval permission failed", true, action.isAllowApproveBDF());

        // trying to approve BDF
        Long approveId = action.getBdId();
        request.setParameter("bdId", approveId.toString());
        initAndExecute(BIRTH_APPROVAL_MAPPING, session);
        session = approveAction.getSession();
        assertEquals("session size incorrect", 4, session.size());
        assertNotNull("Warnings should not be null", approveAction.getWarnings());
        assertEquals(3, approveAction.getWarnings().size());
        assertEquals(0, getBDFById(persistedBDId).getRegister().getStatus().ordinal());

        // approving submit ignore warnings
        approveId = approveAction.getBdId();
        session = approveAction.getSession();
        request.setParameter("ignoreWarning", "true");
        request.setParameter("bdId", approveId.toString());
        request.setParameter("directApprovalFlag", "true");
        initAndExecute(BIRTH_APPROVE_IGNORE_WARNINGS, session);
        session = approveAction.getSession();
        assertEquals(1, getBDFById(persistedBDId).getRegister().getStatus().ordinal());

        logger.debug("BDF direct approved ignoring warnings for IDUKey : " + approveAction.getBdId());
        assertEquals(approveId.longValue(), approveAction.getBdId());
        assertEquals("session size incorrect", 4, session.size());
        assertNull(session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));
        assertNull(session.get(WebConstants.SESSION_OLD_BD_FOR_ADOPTION));
        assertEquals("BDF approval permission failed", true, action.isAllowApproveBDF());

        // printing birth confirmation
        request.setParameter("directPrint", "true");
        request.setParameter("bdId", approveId.toString());
        initAndExecute(BIRTH_CONFIRM_PRINT, session);
        session = action.getSession();

        // mark as confirmation print
        request.setParameter("bdId", approveId.toString());
        request.setParameter("confirmListFlag", "true");
        initAndExecute(BIRTH_CONFIRM_DIRECT_PRINT, session);
        session = printAction.getSession();
        assertEquals(2, getBDFById(persistedBDId).getRegister().getStatus().ordinal());
        logger.debug("Approve and Print Birth Confirmation Completed");
    }

    /**
     * Test BirthDeclaration direct approve and print still birth certificate for still birth
     */
    public void testApproveAndPrintCertificate() throws Exception {
        Map session = login("chathuranga", "chathuranga");
        initAndExecute(STILL_BIRTH_REGISTER_INIT_MAPPING, session);
        session = action.getSession();

        sampleBD.getRegister().setBdfSerialNo(2010012962L);
        // Filling Birth declaration Form
        fillBDForm1(sampleBD, session);
        fillBDForm2(session);
        fillBDForm3(sampleBD, session);
        fillBDForm4(sampleBD, session);

        long persistedBDId = action.getBdId();
        if (persistedBDId == 0) {
            fail("BDF not persisted");
        }
        assertEquals(0, getBDFById(persistedBDId).getRegister().getStatus().ordinal());
        session = action.getSession();

        assertEquals("session size incorrect", 4, session.size());
        assertNull(session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));
        assertNull(session.get(WebConstants.SESSION_OLD_BD_FOR_ADOPTION));
        assertEquals("BDF approval permission failed", true, action.isAllowApproveBDF());

        // trying to approve BDF
        Long approveId = action.getBdId();
        request.setParameter("bdId", approveId.toString());
        initAndExecute(BIRTH_APPROVAL_MAPPING, session);
        session = approveAction.getSession();
        assertEquals("session size incorrect", 4, session.size());
        assertNotNull("Warnings should not be null", approveAction.getWarnings());
        assertEquals(4, approveAction.getWarnings().size());
        assertEquals(0, getBDFById(persistedBDId).getRegister().getStatus().ordinal());

        // approving submit ignore warnings
        approveId = approveAction.getBdId();
        session = approveAction.getSession();
        request.setParameter("ignoreWarning", "true");
        request.setParameter("bdId", approveId.toString());
        request.setParameter("directApprovalFlag", "true");
        initAndExecute(BIRTH_APPROVE_IGNORE_WARNINGS, session);
        session = approveAction.getSession();
        assertEquals(8, getBDFById(persistedBDId).getRegister().getStatus().ordinal());

        logger.debug("BDF direct approved ignoring warnings for IDUKey : " + approveAction.getBdId());
        assertEquals(approveId.longValue(), approveAction.getBdId());
        assertEquals("session size incorrect", 4, session.size());
        assertNull(session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));
        assertNull(session.get(WebConstants.SESSION_OLD_BD_FOR_ADOPTION));
        assertEquals("BDF approval permission failed", true, action.isAllowApproveBDF());

        // printing still birth certificate
        request.setParameter("directPrint", "true");
        request.setParameter("bdId", approveId.toString());
        initAndExecute(STILL_BIRTH_CERT_PRINT, session);
        session = action.getSession();

        // mark as still birth certificate printed
        request.setParameter("bdId", approveId.toString());
        request.setParameter("locationId", "1");
        request.setParameter("issueUserId", "chathuranga");
        initAndExecute(BIRTH_CERT_DIRECT_PRINT, session);
        session = printAction.getSession();
        assertEquals(9, getBDFById(persistedBDId).getRegister().getStatus().ordinal());
        logger.debug("Still Birth Certificate Print Completed ");
    }

    private Map login(String userName, String password) throws Exception {
        request.setParameter("javaScript","true");
        request.setParameter("userName", userName);
        request.setParameter("password", password);
        ActionProxy proxy = getActionProxy("/eprLogin.do");
        loginAction = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        proxy.execute();
        return loginAction.getSession();
    }

    private static BirthDeclaration createSampleBD(long serial, Date dob, BDDivision bdDivision) {
        Date today = new Date();
        BirthDeclaration bdf = new BirthDeclaration();

        bdf.getRegister().setBdfSerialNo(serial);
        bdf.getRegister().setDateOfRegistration(today);
        bdf.getRegister().setBirthDivision(bdDivision);
        bdf.getChild().setDateOfBirth(dob);
        bdf.getChild().setPlaceOfBirth("ළමයාගේ උපන් ස්ථානය" + serial);
        bdf.getChild().setPlaceOfBirthEnglish("Place of birth for child " + serial);
        bdf.getChild().setBirthAtHospital(true);
        bdf.getChild().setChildFullNameOfficialLang("කමල් සිල්වා");
        bdf.getChild().setChildFullNameEnglish("KAMAL SILVA");
        bdf.getRegister().setPreferredLanguage("si");
        bdf.getChild().setChildGender(0);
        bdf.getChild().setChildBirthWeight(6.55f);
        bdf.getChild().setChildRank(1);

        bdf.getParent().setFatherNICorPIN("530232026V");
        bdf.getParent().setFatherFullName("ලෝගේස්වරන් යුවන් ශන්කර්");
        bdf.getParent().setFatherDOB(DateTimeUtils.getDateFromISO8601String("1953-01-23"));
        bdf.getParent().setFatherRace(tamil);
        bdf.getParent().setFatherPlaceOfBirth("father birth place");

        bdf.getParent().setMotherNICorPIN("685031035V");
        bdf.getParent().setMotherFullName("සංගුණි ෙද්ව ෙග්");
        bdf.getParent().setMotherDOB(DateTimeUtils.getDateFromISO8601String("1968-01-03"));
        bdf.getParent().setMotherAgeAtBirth(42);
        bdf.getParent().setMotherDSDivision(motherDSDivision);
        bdf.getParent().setMotherRace(tamil);
        bdf.getParent().setMotherPlaceOfBirth("mother birth place");
        bdf.getParent().setMotherAddress("mother address");
        bdf.getParent().setMotherAdmissionNo("288");
        bdf.getParent().setMotherEmail("mother@gmail.com");
        bdf.getParent().setMotherAdmissionDate(dob);
        bdf.getParent().setMotherPhoneNo("0715516541");

        bdf.getMarriage().setParentsMarried(MarriageInfo.MarriedStatus.MARRIED);
        bdf.getMarriage().setPlaceOfMarriage("Maharagama");
        bdf.getMarriage().setDateOfMarriage(DateTimeUtils.getDateFromISO8601String("2009-04-27"));

        bdf.getGrandFather().setGrandFatherFullName("grand father");
        bdf.getGrandFather().setGrandFatherNICorPIN("481254698V");
        bdf.getGrandFather().setGrandFatherBirthYear(1948);
        bdf.getGrandFather().setGrandFatherBirthPlace("Matara");
        bdf.getGrandFather().setGreatGrandFatherFullName("great grand father");
        bdf.getGrandFather().setGreatGrandFatherNICorPIN("210213654V");
        bdf.getGrandFather().setGreatGrandFatherBirthYear(1869);
        bdf.getGrandFather().setGreatGrandFatherBirthPlace("Galle");

        bdf.getInformant().setInformantName("Name of Informant for Child : " + serial);
        bdf.getInformant().setInformantAddress("Address of Informant for Child : " + serial);
        bdf.getInformant().setInformantSignDate(today);
        bdf.getInformant().setInformantEmail("informant@gmail.com");
        bdf.getInformant().setInformantNICorPIN("685031035V");
        bdf.getInformant().setInformantPhoneNo("0715516541");
        bdf.getInformant().setInformantType(InformantInfo.InformantType.GUARDIAN);

        bdf.getNotifyingAuthority().setNotifyingAuthorityAddress("The address of the Birth registrar");
        bdf.getNotifyingAuthority().setNotifyingAuthoritySignDate(today);
        bdf.getNotifyingAuthority().setNotifyingAuthorityName("Name of the Notifying Authority");
        bdf.getNotifyingAuthority().setNotifyingAuthorityPIN("750010001");
        return bdf;
    }

    private void fillBDForm1(BirthDeclaration bd, Map session) throws Exception {
        request.setParameter("pageNo", "1");
        request.setParameter("register.bdfSerialNo", String.valueOf(bd.getRegister().getBdfSerialNo()));
        request.setParameter("register.dateOfRegistration", DateTimeUtils.getISO8601FormattedString(bd.getRegister().getDateOfRegistration()));
        request.setParameter("child.dateOfBirth", DateTimeUtils.getISO8601FormattedString(bd.getChild().getDateOfBirth()));
        request.setParameter("birthDistrictId", String.valueOf(bd.getRegister().getBirthDivision().getDistrict().getDistrictUKey()));
        request.setParameter("birthDivisionId", String.valueOf(bd.getRegister().getBirthDivision().getBdDivisionUKey()));
        request.setParameter("dsDivisionId", String.valueOf(bd.getRegister().getBirthDivision().getDsDivision().getDsDivisionUKey()));
        request.setParameter("child.placeOfBirth", bd.getChild().getPlaceOfBirth());
        request.setParameter("child.placeOfBirthEnglish", bd.getChild().getPlaceOfBirthEnglish());
        request.setParameter("child.birthAtHospital", String.valueOf(bd.getChild().getBirthAtHospital()));
        request.setParameter("child.childFullNameOfficialLang", bd.getChild().getChildFullNameOfficialLang());
        request.setParameter("child.childFullNameEnglish", bd.getChild().getChildFullNameEnglish());
        request.setParameter("register.preferredLanguage", bd.getRegister().getPreferredLanguage());
        request.setParameter("child.childGender", String.valueOf(bd.getChild().getChildGender()));
        request.setParameter("child.childBirthWeight", String.valueOf(bd.getChild().getChildBirthWeight()));
        request.setParameter("child.childRank", String.valueOf(bd.getChild().getChildRank()));
        initAndExecute(BIRTH_REGISTER_MAPPING, session);
    }

    private void fillBDForm2(Map session) throws Exception {
        request.setParameter("pageNo", "2");
        //father's information
        request.setParameter("parent.fatherNICorPIN", "530232026V");
        request.setParameter("fatherCountry", "3");
        request.setParameter("parent.fatherPassportNo", "4562354875");
        request.setParameter("parent.fatherFullName", "ලෝගේස්වරන් යුවන් ශන්කර්");
        request.setParameter("parent.fatherDOB", "1953-01-23");
        request.setParameter("parent.fatherPlaceOfBirth", "father birth place");
        request.setParameter("fatherRace", "2");

        //mother's information
        request.setParameter("parent.motherNICorPIN", "685031035V");
        request.setParameter("motherCountry", "1");
        request.setParameter("parent.motherPassportNo", "2586598456");
        request.setParameter("parent.motherFullName", "සංගුණි ෙද්ව ෙග්");
        request.setParameter("parent.motherDOB", "1968-01-03");
        request.setParameter("parent.motherAgeAtBirth", "42");
        request.setParameter("parent.motherAddress", "mother address");
        request.setParameter("motherDistrictId", String.valueOf(motherDSDivision.getDistrict().getDistrictUKey()));
        request.setParameter("motherDSDivisionId", String.valueOf(motherDSDivision.getDsDivisionUKey()));
        request.setParameter("motherRace", "2");

        request.setParameter("parent.motherPlaceOfBirth", "mother birth place");
        request.setParameter("parent.motherAdmissionNo", "288");
        request.setParameter("parent.motherEmail", "mother@gmail.com");
        request.setParameter("parent.motherAdmissionDate", "2010-06-23");
        request.setParameter("parent.motherPhoneNo", "0715516541");
        initAndExecute(BIRTH_REGISTER_MAPPING, session);
    }

    private void fillBDForm3(BirthDeclaration bd, Map session) throws Exception {
        request.setParameter("pageNo", "3");
        request.setParameter("marriage.parentsMarried", bd.getMarriage().getParentsMarried().toString());
        request.setParameter("marriage.placeOfMarriage", bd.getMarriage().getPlaceOfMarriage());
        request.setParameter("marriage.dateOfMarriage", DateTimeUtils.getISO8601FormattedString(bd.getMarriage().getDateOfMarriage()));

        request.setParameter("grandFather.grandFatherFullName", bd.getGrandFather().getGrandFatherFullName());
        request.setParameter("grandFather.grandFatherNICorPIN", bd.getGrandFather().getGrandFatherNICorPIN());
        request.setParameter("grandFather.grandFatherBirthYear", bd.getGrandFather().getGrandFatherBirthYear().toString());
        request.setParameter("grandFather.grandFatherBirthPlace", bd.getGrandFather().getGrandFatherBirthPlace());
        request.setParameter("grandFather.greatGrandFatherFullName", bd.getGrandFather().getGreatGrandFatherFullName());
        request.setParameter("grandFather.greatGrandFatherNICorPIN", bd.getGrandFather().getGreatGrandFatherNICorPIN());
        request.setParameter("grandFather.greatGrandFatherBirthYear", bd.getGrandFather().getGreatGrandFatherBirthYear().toString());
        request.setParameter("grandFather.greatGrandFatherBirthPlace", bd.getGrandFather().getGreatGrandFatherBirthPlace());

        request.setParameter("informant.informantType", bd.getInformant().getInformantType().toString());
        request.setParameter("informant.informantNICorPIN", bd.getInformant().getInformantNICorPIN());
        request.setParameter("informant.informantName", bd.getInformant().getInformantName());
        request.setParameter("informant.informantAddress", bd.getInformant().getInformantAddress());
        request.setParameter("informant.informantPhoneNo", bd.getInformant().getInformantPhoneNo());
        request.setParameter("informant.informantEmail", bd.getInformant().getInformantEmail());
        request.setParameter("informant.informantSignDate", DateTimeUtils.getISO8601FormattedString(bd.getInformant().getInformantSignDate()));
        initAndExecute(BIRTH_REGISTER_MAPPING, session);
    }

    private void fillBDForm4(BirthDeclaration bd, Map session) throws Exception {
        request.setParameter("pageNo", "4");
        request.setParameter("notifyingAuthority.notifyingAuthorityPIN", bd.getNotifyingAuthority().getNotifyingAuthorityPIN());
        request.setParameter("notifyingAuthority.notifyingAuthorityName", bd.getNotifyingAuthority().getNotifyingAuthorityName());
        request.setParameter("notifyingAuthority.notifyingAuthorityAddress", bd.getNotifyingAuthority().getNotifyingAuthorityAddress());
        request.setParameter("notifyingAuthority.notifyingAuthoritySignDate", DateTimeUtils.getISO8601FormattedString(
            bd.getNotifyingAuthority().getNotifyingAuthoritySignDate()));
        initAndExecute(BIRTH_REGISTER_MAPPING, session);
    }

    /**
     * Return BirthDeclaration for a given IdUkey
     */
    private BirthDeclaration getBDFById(long serial) {
        return birthDeclarationDAO.getById(serial);
    }
}