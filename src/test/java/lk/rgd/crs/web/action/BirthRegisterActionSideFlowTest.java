package lk.rgd.crs.web.action;

import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.util.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.springframework.context.ApplicationContext;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;

import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.InformantInfo;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.web.action.births.BirthRegisterAction;
import lk.rgd.crs.web.action.births.BirthRegisterApprovalAction;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.UnitTestManager;

/**
 * @author Chathuranga Withana
 */
public class BirthRegisterActionSideFlowTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterActionSideFlowTest.class);
    private final ApplicationContext ctx = UnitTestManager.ctx;
    private BirthDeclarationDAO birthDeclarationDAO;
    private BDDivisionDAO bdDivisionDAO;

    private static final String BIRTH_REGISTER_INIT_MAPPING = "/births/eprBirthRegistrationInit.do";
    private static final String BIRTH_REGISTER_MAPPING = "/births/eprBirthRegistration.do";
    private static final String BIRTH_APPROVAL_MAPPING = "/births/eprDirectApprove.do";
    private static final String BIRTH_APPROVE_IGNORE_WARNINGS = "/births/eprDirectApproveIgnoreWarning.do";
    private static final String BIRTH_CONFIRM_PRINT = "/births/eprBirthConfirmationPrintPage.do";
    private ActionProxy proxy;
    private BirthRegisterAction action;
    private LoginAction loginAction;
    private BirthRegisterApprovalAction approveAction;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        birthDeclarationDAO = (BirthDeclarationDAO) ctx.getBean("birthDeclarationDAOImpl", BirthDeclarationDAO.class);
        bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
    }

    private String initAndExecute(String mapping, Map session) throws Exception {
        proxy = getActionProxy(mapping);

        if (BIRTH_REGISTER_MAPPING.equals(mapping) || BIRTH_REGISTER_INIT_MAPPING.equals(mapping)) {
            action = (BirthRegisterAction) proxy.getAction();
        } else if (BIRTH_APPROVAL_MAPPING.equals(mapping) || BIRTH_APPROVE_IGNORE_WARNINGS.equals(mapping)) {
            approveAction = (BirthRegisterApprovalAction) proxy.getAction();
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
    /*public void testAddNewBDFInBatchMode() throws Exception {
        Map session = login("chathuranga", "chathuranga");

//        long serialNum = 19000;
//        BDDivision colomboBdDivision = bdDivisionDAO.getBDDivisionByPK(1);
//
//        // get alredy entered BDF entry from DB, BDF with IDUKey 166 and this part used to skip first few steps of Bith
//        // Declaration
//        BirthDeclaration bdf = getBDFById(166);
//        assertNotNull("BirthDeclaration failed to be fetched from the DB", bdf);
//        bdf.setIdUKey(0);
//        bdf.getRegister().setBdfSerialNo(serialNum);
//        bdf.getRegister().setBirthDivision(colomboBdDivision);
//        assertEquals("session size incorrect", 3, session.size());
//        assertNull("Birth Declaration Bean can not exist in the session", session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));
//        session.put(WebConstants.SESSION_BIRTH_DECLARATION_BEAN, bdf);
//        assertEquals("session size incorrect", 4, session.size());
//        assertNotNull("Birth Declaration Bean does not exist in the session", session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));
        Calendar dob = Calendar.getInstance();
        dob.add(Calendar.DATE, -3);
        BDDivision colombo = bdDivisionDAO.getBDDivisionByPK(1);
        BirthDeclaration bdf = createBDF(990090, dob.getTime(), colombo);
        fillBDForm1("990090", session);
        fillBDForm2(session);
        fillBDForm3(session);
        fillBDForm4(session);
        session = action.getSession();

        assertNotNull("BDF NotifyingAuthorityInfo object is null", action.getNotifyingAuthority());
        logger.debug("Notifying Authoruty PIN : {}", action.getNotifyingAuthority().getNotifyingAuthorityPIN());
        logger.debug("Notifying Authoruty Name : {}", action.getNotifyingAuthority().getNotifyingAuthorityName());
        logger.debug("Notifying Authoruty Address : {}", action.getNotifyingAuthority().getNotifyingAuthorityAddress());
        logger.debug("Notifying Authoruty Sign Date : {}", action.getNotifyingAuthority().getNotifyingAuthoritySignDate());

        assertNotNull("Birth Declaration registration faild to persist", action.getBdId());
        long persistedBDId = action.getBdId();
        logger.debug("BDF persisted but not in batch mode with IDUKey : {}", persistedBDId);
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
        initAndExecute("/births/eprBirthRegistrationInit.do", session);

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
        assertEquals("BDF serial number not inceremented for the BDF in seesion", 990091, batchBdf.getRegister().getBdfSerialNo());
        assertEquals("BDF date Of registration in session and DB are not equal", bdf.getRegister().getDateOfRegistration(),
            batchBdf.getRegister().getDateOfRegistration());
        assertEquals("BDF live birth type not matched", bdf.getRegister().getBirthType(), batchBdf.getRegister().getBirthType());
        assertEquals("BDF District id miss match in action and session", action.getBirthDistrictId(),
            batchBdf.getRegister().getBirthDistrict().getDistrictUKey());
        assertEquals("BDF DS Division id miss match in action and session", action.getDsDivisionId(),
            batchBdf.getRegister().getDsDivision().getDsDivisionUKey());
        assertEquals("BDF Birth division id miss match in action and session",
            action.getBirthDivisionId(), batchBdf.getRegister().getBirthDivision().getBdDivisionUKey());
        assertEquals("BDF Birth division id miss match in previously added BDF and BDF in session",
            bdf.getRegister().getBirthDivision().getBdDivisionUKey(), batchBdf.getRegister().getBirthDivision().getBdDivisionUKey());
        assertEquals("BDF NotifyingAutho PIN miss match in previously added BDF and BDF in session",
            bdf.getNotifyingAuthority().getNotifyingAuthorityPIN(), batchBdf.getNotifyingAuthority().getNotifyingAuthorityPIN());
        assertEquals("BDF NotifyingAutho Name miss match in previously added BDF and BDF in session",
            bdf.getNotifyingAuthority().getNotifyingAuthorityName(), batchBdf.getNotifyingAuthority().getNotifyingAuthorityName());
        assertEquals("BDF NotifyingAutho Address miss match in previously added BDF and BDF in session",
            bdf.getNotifyingAuthority().getNotifyingAuthorityAddress(), batchBdf.getNotifyingAuthority().getNotifyingAuthorityAddress());
        assertEquals("BDF NotifyingAutho Sign Date miss match in previously added BDF and BDF in session",
            DateTimeUtils.getISO8601FormattedString(bdf.getNotifyingAuthority().getNotifyingAuthoritySignDate()),
            DateTimeUtils.getISO8601FormattedString(batchBdf.getNotifyingAuthority().getNotifyingAuthoritySignDate()));
        logger.debug("Adding BDF auto populating fields populated correctly and Add new in batch mode passed");
        deleteBDF(bdf);
    }

    *//**
     * Test back button for live birth in Birth Declaration Form
     *//*
    public void testBackButtonInBD() throws Exception {
        Map session = login("chathuranga", "chathuranga");
        initAndExecute("/births/eprBirthRegistrationInit.do", session);
        session = action.getSession();
        assertEquals("Action erros for 1 of 4 BDF pages", 0, action.getActionErrors().size());

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

        // Filling BDF form 1 and submitting
        fillBDForm1("12346", session);
        session = action.getSession();

        // Filling BDF form 2 and submitting
        fillBDForm2(session);
        session = action.getSession();

        // Filling BDF form 3 and submitting
        fillBDForm3(session);
        session = action.getSession();
        assertEquals("session size incorrect", 5, session.size());

        request.setParameter("pageNo", "2");
        request.setParameter("back", "true");
        initAndExecute(BIRTH_REGISTER_MAPPING, session);
        session = action.getSession();
        assertEquals("session size incorrect", 5, session.size());

        // back in BDF form 1
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
        assertEquals(12346, bdf.getRegister().getBdfSerialNo());
        assertEquals("2010-07-14", DateTimeUtils.getISO8601FormattedString(bdf.getRegister().getDateOfRegistration()));
        assertEquals("2010-06-28", DateTimeUtils.getISO8601FormattedString(bdf.getChild().getDateOfBirth()));
        assertEquals(2, bdf.getRegister().getBirthDivision().getDistrict().getDistrictUKey());
        assertEquals(13, bdf.getRegister().getBirthDivision().getBdDivisionUKey());
        assertEquals(15, bdf.getRegister().getBirthDivision().getDsDivision().getDsDivisionUKey());
        assertEquals("ගම්පහ මහ රෝහල", bdf.getChild().getPlaceOfBirth());
        assertEquals("GAMPAHA CENTRAL HOSPITAL", bdf.getChild().getPlaceOfBirthEnglish());
        assertEquals(true, bdf.getChild().getBirthAtHospital());
        assertEquals("කමල් සිල්වා", bdf.getChild().getChildFullNameOfficialLang());
        assertEquals("KAMAL SILVA", bdf.getChild().getChildFullNameEnglish());
        assertEquals("si", bdf.getRegister().getPreferredLanguage());
        assertEquals(0, bdf.getChild().getChildGender());
        assertEquals(6.5F, bdf.getChild().getChildBirthWeight().floatValue());
        assertEquals(1, bdf.getChild().getChildRank().intValue());
        assertEquals(0, bdf.getChild().getNumberOfChildrenBorn().intValue());

        // check BDF form 2 data        
        assertEquals("530232026V", bdf.getParent().getFatherNICorPIN());
        assertEquals(3, bdf.getParent().getFatherCountry().getCountryId());
        assertEquals("4562354875", bdf.getParent().getFatherPassportNo());
        assertEquals("ලෝගේස්වරන් යුවන් ශන්කර්", bdf.getParent().getFatherFullName());
        assertEquals("1953-01-23", DateTimeUtils.getISO8601FormattedString(bdf.getParent().getFatherDOB()));
        assertEquals("FATHER BIRTH PLACE", bdf.getParent().getFatherPlaceOfBirth());
        assertEquals(3, bdf.getParent().getFatherRace().getRaceId());

        assertEquals("685031035V", bdf.getParent().getMotherNICorPIN());
        assertEquals(1, bdf.getParent().getMotherCountry().getCountryId());
        assertEquals("2586598456", bdf.getParent().getMotherPassportNo());
        assertEquals("සංගුණි ෙද්ව ෙග්", bdf.getParent().getMotherFullName());
        assertEquals("1968-01-03", DateTimeUtils.getISO8601FormattedString(bdf.getParent().getMotherDOB()));
        assertEquals(42, bdf.getParent().getMotherAgeAtBirth().intValue());
        assertEquals("MOTHER ADDRESS", bdf.getParent().getMotherAddress());
        assertEquals(2, bdf.getParent().getMotherDSDivision().getDistrict().getDistrictUKey());
        assertEquals(15, bdf.getParent().getMotherDSDivision().getDsDivisionUKey());
        assertEquals(2, bdf.getParent().getMotherRace().getRaceId());

        assertEquals("MOTHER BIRTH PLACE", bdf.getParent().getMotherPlaceOfBirth());
        assertEquals("288", bdf.getParent().getMotherAdmissionNo());
        assertEquals("mother@gmail.com", bdf.getParent().getMotherEmail());
        assertEquals("2010-06-23", DateTimeUtils.getISO8601FormattedString(bdf.getParent().getMotherAdmissionDate()));
        assertEquals("0715516541", bdf.getParent().getMotherPhoneNo());

        // check BDF form 2 data
        assertEquals(1, bdf.getMarriage().getParentsMarried().intValue());
        assertEquals("MAHARAGAMA", bdf.getMarriage().getPlaceOfMarriage());
        assertEquals("2009-04-27", DateTimeUtils.getISO8601FormattedString(bdf.getMarriage().getDateOfMarriage()));

        assertEquals("GRAND FATHER", bdf.getGrandFather().getGrandFatherFullName());
        assertEquals("481254698V", bdf.getGrandFather().getGrandFatherNICorPIN());
        assertEquals(1948, bdf.getGrandFather().getGrandFatherBirthYear().intValue());
        assertEquals("MATARA", bdf.getGrandFather().getGrandFatherBirthPlace());
        assertEquals("GREAT GRAND FATHER", bdf.getGrandFather().getGreatGrandFatherFullName());
        assertEquals("210213654V", bdf.getGrandFather().getGreatGrandFatherNICorPIN());
        assertEquals(1869, bdf.getGrandFather().getGreatGrandFatherBirthYear().intValue());
        assertEquals("GALLE", bdf.getGrandFather().getGreatGrandFatherBirthPlace());

        assertEquals("MOTHER", bdf.getInformant().getInformantType().toString());
        assertEquals("685031035V", bdf.getInformant().getInformantNICorPIN());
        assertEquals("සංගුණි ෙද්ව ෙග්", bdf.getInformant().getInformantName());
        assertEquals("MOTHER ADDRESS", bdf.getInformant().getInformantAddress());
        assertEquals("0715516541", bdf.getInformant().getInformantPhoneNo());
        assertEquals("mother@gmail.com", bdf.getInformant().getInformantEmail());
        assertEquals("2010-01-08", DateTimeUtils.getISO8601FormattedString(bdf.getInformant().getInformantSignDate()));
        deleteBDF(bdf);
    }

    *//**
     * Test BirthDeclaration direct approve and print birth confirmation for live birth
     *//*
    public void testApproveAndPrintConfirmation() throws Exception {
        Map session = login("chathuranga", "chathuranga");

        // Filling Birth declaration Form
        fillBDForm1("12347", session);
        fillBDForm2(session);
        fillBDForm3(session);
        fillBDForm4(session);
        BirthDeclaration bdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);

        assertEquals("session size incorrect", 4, session.size());
        assertNull(session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));
        assertNull(session.get(WebConstants.SESSION_OLD_BD_FOR_ADOPTION));
        assertEquals("BDF approval permission failed", true, action.isAllowApproveBDF());

        Long approveId = action.getBdId();
        request.setParameter("bdId", approveId.toString());
        initAndExecute(BIRTH_APPROVAL_MAPPING, session);
        session = approveAction.getSession();
        assertEquals("session size incorrect", 4, session.size());
        assertNotNull("Warnings should not be null", approveAction.getWarnings());
        assertEquals(2, approveAction.getWarnings().size());

<<<<<<< local
        initAndExecute(BIRTH_REGISTER_MAPPING, session);
        session = action.getSession();
=======
        // submit ignore 2 warnings
        approveId = approveAction.getBdId();
        session = approveAction.getSession();
        request.setParameter("ignoreWarning", "true");
        request.setParameter("bdId", approveId.toString());
        initAndExecute(BIRTH_APPROVE_IGNORE_WARNINGS, session);
        assertEquals(1, getBDFById(approveAction.getBdId()).getRegister().getStatus().ordinal());
        logger.debug("BDF direct approved ignoring warnings for IDUKey : " + approveAction.getBdId());
        assertEquals(approveId.longValue(), approveAction.getBdId());
>>>>>>> other

<<<<<<< local
        assertEquals("session size incorrect", 5, session.size());      // TODO continue from here problems in session size         4 but 5
        assertNull(session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));
        assertNull(session.get(WebConstants.SESSION_OLD_BD_FOR_ADOPTION));
        assertEquals("BDF approval permission failed", true, action.isAllowApproveBDF());

        Long approveId = action.getBdId();
=======
        // print birth confirmation
        approveId = approveAction.getBdId();
        session = approveAction.getSession();
>>>>>>> other
        request.setParameter("bdId", approveId.toString());
<<<<<<< local
        initAndExecute(BIRTH_APPROVAL_MAPPING, session);
        session = approveAction.getSession();
        assertEquals("session size incorrect", 4, session.size());      // TODO continue from here problems in session size         4 but 5

=======
        request.setParameter("directPrint", "true");
        initAndExecute(BIRTH_CONFIRM_PRINT, session);
        assertEquals(2, getBDFById(approveAction.getBdId()).getRegister().getStatus().ordinal());
        logger.debug("Birth Confirmation Printed for IDUKey : " + action.getBdId());
        deleteBDF(bdf);
>>>>>>> other
    }

    *//**
     * Test BirthDeclaration direct approve and print still birth certificate for still birth
     *//*
    public void testApproveAndPrintCertificate() {

    }

    *//**
     * Used to test login to the system
     *
     * @param userName the user Id
     * @param password the password
     * @return user session
     * @throws Exception
     *//*
    private Map login(String userName, String password) throws Exception {
        request.setParameter("userName", userName);
        request.setParameter("password", password);
        ActionProxy proxy = getActionProxy("/eprLogin.do");
        loginAction = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        proxy.execute();
        return loginAction.getSession();
    }

    private BirthDeclaration createBDF(long serial, Date dob, BDDivision bdDivision) {
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

    private void fillBDForm1(String serial, Map session) throws Exception {
        request.setParameter("pageNo", "1");
        request.setParameter("register.bdfSerialNo", serial);
        request.setParameter("register.dateOfRegistration", "2010-07-14");
        request.setParameter("child.dateOfBirth", "2010-06-28");
        request.setParameter("birthDistrictId", "1");
        request.setParameter("birthDivisionId", "2");
        request.setParameter("dsDivisionId", "1");
        request.setParameter("child.placeOfBirth", "ගම්පහ මහ රෝහල");
        request.setParameter("child.placeOfBirthEnglish", "Gampaha Central Hospital");
        request.setParameter("child.birthAtHospital", "true");
        request.setParameter("child.childFullNameOfficialLang", "කමල් සිල්වා");
        request.setParameter("child.childFullNameEnglish", "KAMAL SILVA");
        request.setParameter("register.preferredLanguage", "si");
        request.setParameter("child.childGender", "0");
        request.setParameter("child.childBirthWeight", "6.5");
        request.setParameter("child.childRank", "1");
        request.setParameter("child.numberOfChildrenBorn", "0");
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
        request.setParameter("fatherRace", "3");

        //mother's information
        request.setParameter("parent.motherNICorPIN", "685031035V");
        request.setParameter("motherCountry", "1");
        request.setParameter("parent.motherPassportNo", "2586598456");
        request.setParameter("parent.motherFullName", "සංගුණි ෙද්ව ෙග්");
        request.setParameter("parent.motherDOB", "1968-01-03");
        request.setParameter("parent.motherAgeAtBirth", "42");
        request.setParameter("parent.motherAddress", "mother address");
        request.setParameter("motherDistrictId", "2");
        request.setParameter("motherDSDivisionId", "15");
        request.setParameter("motherRace", "2");

        request.setParameter("parent.motherPlaceOfBirth", "mother birth place");
        request.setParameter("parent.motherAdmissionNo", "288");
        request.setParameter("parent.motherEmail", "mother@gmail.com");
        request.setParameter("parent.motherAdmissionDate", "2010-06-23");
        request.setParameter("parent.motherPhoneNo", "0715516541");

        initAndExecute(BIRTH_REGISTER_MAPPING, session);
    }

    private void fillBDForm3(Map session) throws Exception {
        request.setParameter("pageNo", "3");
        request.setParameter("marriage.parentsMarried", "1");
        request.setParameter("marriage.placeOfMarriage", "Maharagama");
        request.setParameter("marriage.dateOfMarriage", "2009-04-27");

        request.setParameter("grandFather.grandFatherFullName", "grand father");
        request.setParameter("grandFather.grandFatherNICorPIN", "481254698V");
        request.setParameter("grandFather.grandFatherBirthYear", "1948");
        request.setParameter("grandFather.grandFatherBirthPlace", "Matara");
        request.setParameter("grandFather.greatGrandFatherFullName", "great grand father");
        request.setParameter("grandFather.greatGrandFatherNICorPIN", "210213654V");
        request.setParameter("grandFather.greatGrandFatherBirthYear", "1869");
        request.setParameter("grandFather.greatGrandFatherBirthPlace", "Galle");

        request.setParameter("informant.informantType", "MOTHER");
        request.setParameter("informant.informantNICorPIN", "685031035V");
        request.setParameter("informant.informantName", "සංගුණි ෙද්ව ෙග්");
        request.setParameter("informant.informantAddress", "mother address");
        request.setParameter("informant.informantPhoneNo", "0715516541");
        request.setParameter("informant.informantEmail", "mother@gmail.com");
        request.setParameter("informant.informantSignDate", "2010-01-08");

        initAndExecute(BIRTH_REGISTER_MAPPING, session);
    }

    private void fillBDForm4(Map session) throws Exception {
        request.setParameter("pageNo", "4");
        request.setParameter("notifyingAuthority.notifyingAuthorityPIN", "685031035V");
        request.setParameter("notifyingAuthority.notifyingAuthorityName", "සංගුණි ෙද්ව ෙග්");
        request.setParameter("notifyingAuthority.notifyingAuthorityAddress", "65 C මල්වත්ත පාර, කොට්ටාව");
        request.setParameter("notifyingAuthority.notifyingAuthoritySignDate", "2010-07-02");

        initAndExecute(BIRTH_REGISTER_MAPPING, session);
    }*/

    /**
     * Return BirthDeclaration for a given IdUkey
     *
     * @param serial Birth Declarion Id for the given declaration
     * @return the BDF if found, and the user has access to the record
     */
    private BirthDeclaration getBDFById(long serial) {
        return birthDeclarationDAO.getById(serial);
    }

    /**
     * Delete Birth Declaration by BDDivision and serial number
     *
     * @param bdf
     */
    private void deleteBDF(BirthDeclaration bdf) {
        try {
            birthDeclarationDAO.deleteBirthDeclaration(bdf.getIdUKey());
        } catch (Exception e) {
        }
    }
}