package lk.rgd.crs.web.action;

import lk.rgd.common.CustomStrutsTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.springframework.context.ApplicationContext;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.Action;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.InformantInfo;
import lk.rgd.crs.api.domain.NotifyingAuthorityInfo;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.web.action.births.BirthRegisterAction;
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

    private ActionProxy proxy;
    private BirthRegisterAction action;
    private LoginAction loginAction;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        birthDeclarationDAO = (BirthDeclarationDAO) ctx.getBean("birthDeclarationDAOImpl", BirthDeclarationDAO.class);
        bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
    }

    private String initAndExecute(String mapping, Map session) throws Exception {
        proxy = getActionProxy(mapping);
        action = (BirthRegisterAction) proxy.getAction();
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
        ActionMapping mapping = getActionMapping("/births/eprBirthRegistration.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/births", mapping.getNamespace());
        assertEquals("eprBirthRegistration", mapping.getName());
        ActionProxy proxy = getActionProxy("/births/eprBirthRegistration.do");
        assertNotNull(proxy);
        logger.debug("NameSpace {} and ActionName {}", mapping.getNamespace(), proxy.getMethod());

        BirthRegisterAction action = (BirthRegisterAction) proxy.getAction();
        assertNotNull(action);
    }

    /**
     * Test BirthDeclaration adding in batch mode
     */
    public void testAddNewBDFInBatchMode() throws Exception {
        Map session = login("chathuranga", "chathuranga");

        long serialNum = 19000;
        BDDivision colomboBdDivision = bdDivisionDAO.getBDDivisionByPK(1);

        // get alredy entered BDF entry from DB, BDF with IDUKey 166 and this part used to skip first few steps of Bith
        // Declaration
        BirthDeclaration bdf = getBDFById(166);
        assertNotNull("BirthDeclaration failed to be fetched from the DB", bdf);
        bdf.setIdUKey(0);
        bdf.getRegister().setBdfSerialNo(serialNum);
        bdf.getRegister().setBirthDivision(colomboBdDivision);
        assertEquals("session size incorrect", 3, session.size());
        assertNull("Birth Declaration Bean can not exist in the session", session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));
        session.put(WebConstants.SESSION_BIRTH_DECLARATION_BEAN, bdf);
        assertEquals("session size incorrect", 4, session.size());
        assertNotNull("Birth Declaration Bean does not exist in the session", session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));

        // adding a Birth Declaration in normal mode
        request.setParameter("pageNo", "4");
        request.setParameter("notifyingAuthority.notifyingAuthorityPIN", "685031035V");
        request.setParameter("notifyingAuthority.notifyingAuthorityName", "සංගුණි ෙද්ව ෙග්");
        request.setParameter("notifyingAuthority.notifyingAuthorityAddress", "65 C මල්වත්ත පාර, කොට්ටාව");
        request.setParameter("notifyingAuthority.notifyingAuthoritySignDate", "2010-07-02T00:00:00+05:30");

        initAndExecute("/births/eprBirthRegistration.do", session);

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
        assertEquals("BDF serial number not inceremented for the BDF in seesion", serialNum + 1,
            batchBdf.getRegister().getBdfSerialNo());
        assertEquals("BDF date Of registration in session and DB are not equal", bdf.getRegister().getDateOfRegistration(),
            batchBdf.getRegister().getDateOfRegistration());
        assertEquals("BDF live birth type not matched", bdf.getRegister().isLiveBirth(), batchBdf.getRegister().isLiveBirth());
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
            bdf.getNotifyingAuthority().getNotifyingAuthoritySignDate(), batchBdf.getNotifyingAuthority().getNotifyingAuthoritySignDate());
        logger.debug("Adding BDF auto populating fields populated correctly and Add new in batch mode passed");
        deleteBDF(colomboBdDivision, serialNum);
    }


    public void testBackButtonInBD() throws Exception {
        Map session = login("rg", "password");
        initAndExecute("/births/eprBirthRegistrationInit.do", session);
        session = action.getSession();
        assertEquals("Action erros for 1 of 4 BDF pages", 0, action.getActionErrors().size());

        BirthDeclaration bdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertNotNull("Birth Declaration Bean does not exist in the session", bdf);
        assertEquals("bdId not equal to zero", 0, action.getBdId());
        assertEquals("BDF live birth mis match in session and action", action.isLiveBirth(), bdf.getRegister().isLiveBirth());

        assertNotNull(bdf.getChild());
        assertNotNull(bdf.getRegister());
        assertNotNull(bdf.getParent());
        assertNotNull(bdf.getGrandFather());
        assertNotNull(bdf.getMarriage());
        assertNotNull(bdf.getInformant());
        assertNotNull(bdf.getNotifyingAuthority());

        // TODO check session size
        // Filling BDF form 1 and submitting
        request.setParameter("pageNo", "1");
        request.setParameter("register.bdfSerialNo", "12345");
        request.setParameter("register.dateOfRegistration", "2010-07-14T00:00:00+05:30");
        request.setParameter("child.dateOfBirth", "2010-06-28T00:00:00+05:30");
        request.setParameter("birthDistrictId", "2");
        request.setParameter("birthDivisionId", "13");
        request.setParameter("dsDivisionId", "15");
        request.setParameter("child.placeOfBirth", "ගම්පහ මහ රෝහල");
        request.setParameter("child.placeOfBirthEnglish", "Gampha Cantral Hostipal");
        request.setParameter("child.birthAtHospital", "ture");
        request.setParameter("child.childFullNameOfficialLang", "කමල් සිල්වා");
        request.setParameter("child.childFullNameEnglish", "KAMAL SILVA");
        request.setParameter("register.preferredLanguage", "si");
        request.setParameter("child.childGender", "0");
        request.setParameter("child.childBirthWeight", "6");
        request.setParameter("child.childRank", "1");
        request.setParameter("child.numberOfChildrenBorn", "0");
        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();  //
//        assertEquals("Action erros for 2 of 4BDF", 0, action.getActionErrors().size());
        bdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);

        // Filling BDF form 1 and submitting
        request.setParameter("pageNo", "2");
        //father's information
        request.setParameter("parent.fatherNICorPIN", "530232026V");
        request.setParameter("fatherCountry", "3");
        request.setParameter("parent.fatherPassportNo", "4562354875");
        request.setParameter("parent.fatherFullName", "ලෝගේස්වරන් යුවන් ශන්කර්");
        request.setParameter("parent.fatherDOB", "1953-01-23T00:00:00+05:30");
        request.setParameter("parent.fatherPlaceOfBirth", "birth place");
        request.setParameter("fatherRace", "3");

        //mother's information
        request.setParameter("parent.motherNICorPIN", "685031035V");
        request.setParameter("motherCountry", "0");
        request.setParameter("parent.motherPassportNo", "2586598456");
        request.setParameter("parent.motherFullName", "සංගුණි ෙද්ව ෙග්");
        request.setParameter("parent.motherDOB", "1968-01-03T00:00:00+05:30");
        request.setParameter("parent.motherAgeAtBirth", "42");
        request.setParameter("parent.motherAddress", "mother address");
        request.setParameter("motherDistrictId", "2");
        request.setParameter("motherDSDivisionId", "15");
        request.setParameter("motherRace", "2");
        request.setParameter("parent.motherPlaceOfBirth", "birth place");
        request.setParameter("parent.motherAdmissionNo", "288");
        request.setParameter("parent.motherEmail", "mother@gmail.com");
        request.setParameter("parent.motherAdmissionDate", "2010-06-23T00:00:00+05:30");
        request.setParameter("parent.motherPhoneNo", "0715516541");

        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
//        assertEquals("Action erros found for 3 of 4 BDF", 0, action.getActionErrors().size());
        bdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
//        
        //for 4 of 4BDF
        request.setParameter("pageNo", "3");
        request.setParameter("marriage.parentsMarried", "1");
        request.setParameter("marriage.placeOfMarriage", "Maharagama");
        request.setParameter("marriage.dateOfMarriage", "2009-04-27T00:00:00+05:30");

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
        request.setParameter("informant.informantSignDate", "2010-08-01T00:00:00+05:30");

        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
//        assertEquals("Action erros for 4 of 4BDF", 0, action.getActionErrors().size());
        bdf = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
//
        // TODO notifier information
//        request.set
//        assertEquals("failed to update birth declaration session with parent married", 1, bdf.getMarriage().getParentsMarried().intValue());
//        assertEquals("failed to update birth declaration session with place of marriage", "Kaduwela", bdf.getMarriage().getPlaceOfMarriage());
//        assertEquals("failed to update birth declaration session with date of marriage", convertStringDateToDate("2007-sep-02"), bdf.getMarriage().getDateOfMarriage());
//        assertEquals("failed to update birth declaration session with informant type", 1, bdf.getInformant().getInformantType().ordinal());
//        assertEquals("failed to update birth declaration session with informant NIC/PIN", "685031035V", bdf.getInformant().getInformantNICorPIN());
//        assertEquals("failed to update birth declaration session with informant name", "සංගුණි ෙද්ව ෙග්", bdf.getInformant().getInformantName());
//        assertEquals("failed to update birth declaration session with informant address", "Kandy Road Matale", bdf.getInformant().getInformantAddress());
//        assertEquals("failed to update birth declaration session with informant phone number", "081234567", bdf.getInformant().getInformantPhoneNo());
//        assertEquals("failed to update birth declaration session with informant Email", "info@gmail.com", bdf.getInformant().getInformantEmail());
//        assertEquals("failed to update birth declaration session with informant signed date", convertStringDateToDate("2010-jul-20"), bdf.getInformant().getInformantSignDate());
//        assertNotNull("notifyingAuthority Bean population faild", action.getNotifyingAuthority());
//
//        //BirthDeclaration Form Details
//        request.setParameter("pageNo", "4");
//        request.setParameter("notifyingAuthority.notifyingAuthorityPIN", "685031035V");
//        request.setParameter("notifyingAuthority.notifyingAuthorityName", "සංගුණි ෙද්ව ෙග්");
//        request.setParameter("notifyingAuthority.notifyingAuthorityAddress", "65 C මල්වත්ත පාර, කොට්ටාව");
//        request.setParameter("notifyingAuthority.notifyingAuthoritySignDate", "2010-07-02T00:00:00+05:30");
//        initAndExecute("/births/eprBirthRegistration.do", session);
//        session = action.getSession();
//        assertEquals("Action erros for Birth Declaration Form Details", 0, action.getActionErrors().size());
//        logger.debug("approval permission for the user : {}", action.isAllowApproveBDF());
//        assertEquals("Faild to remove BirthDeclaration", null, session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));
//        logger.debug("successfully persisted with the bdId :{}", action.getBdId());

    }

    /**
     * Used to test login to the system
     *
     * @param userName the user Id
     * @param password the password
     * @return user session
     * @throws Exception
     */
    private Map login(String userName, String password) throws Exception {
        request.setParameter("userName", userName);
        request.setParameter("password", password);
        ActionProxy proxy = getActionProxy("/eprLogin.do");
        loginAction = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        proxy.execute();
        return loginAction.getSession();
    }

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
     * @param bdDivision
     * @param serial
     */
    private void deleteBDF(BDDivision bdDivision, long serial) {
        try {
            birthDeclarationDAO.deleteBirthDeclaration(
                birthDeclarationDAO.getByBDDivisionAndSerialNo(bdDivision, serial).getIdUKey());
        } catch (Exception e) {
        }
    }
}