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
    private BirthDeclaration sessionBD;

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
        logger.debug("BDF added not in batch mode with IDUKey : {}", persistedBDId);
        assertEquals("Action errors found", 0, action.getActionErrors().size());
        assertEquals("Action messages not match", 1, action.getActionMessages().size());
        assertEquals("Invalid session size", 4, session.size());
        assertNull("Birth Declaration Bean can not exist in the session", session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));

        // adding new entry in batch mode 
        assertEquals("AddNewMode should be false", false, action.isAddNewMode());
        assertEquals("oldBdId should be 0", 0, action.getOldBdId());
//        bdf = getBDFById(167);
//        assertNotNull("BDF not exist in the DB", bdf);
//        assertEquals("BDF not exist in the DB", bdf.getIdUKey(), 167);
//        sessionBD = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
//        // BD currently existing in the session
//        assertEquals("BDF serial number not inceremented for the BDF in seesion", sessionBD.getRegister().getBdfSerialNo(),
//            bdf.getRegister().getBdfSerialNo());
//        assertEquals("BDF dateOdRegistration in session and DB not equal", sessionBD.getRegister().getDateOfRegistration(),
//            bdf.getRegister().getDateOfRegistration());

        // TODO
//
//        request.setParameter("bdId", "0");
//        initAndExecute("/births/eprBirthRegistrationInit.do");
////
//        BirthDeclaration bdf1 = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
//        assertNotNull("BDF not available in the session", bdf1);
////
////        //check whether required beans are populated
////        assertEquals("Request child Bean is Populated", bdf1.getChild(), action.getChild());
////        assertEquals("Request register Bean is Populated", bdf1.getRegister(), action.getRegister());
////
////        logger.debug("IsLiveBirth {}", action.isLiveBirth());
////
////        assertEquals("Request birthDistrictId is set to existing district", action.getRegister().getBirthDistrict().getDistrictUKey(),
////            action.getBirthDistrictId());
////        assertEquals("Request birthDivisionId is set to existing birthDivision", action.getRegister().getBirthDivision().getBdDivisionUKey(),
////            action.getBirthDivisionId());
////        assertEquals("Request dsDivisionId is set to existing dsDivision", action.getRegister().getDsDivision().getDsDivisionUKey(),
////            action.getDsDivisionId());
////
////        assertEquals("Request father Country", action.getFatherCountry(), bd.getParent().getFatherCountry().getCountryId());
////        assertEquals("Request father Race", action.getFatherRace(), bd.getParent().getFatherRace().getRaceId());
////        assertEquals("Request Mother Country", action.getMotherCountry(), bd.getParent().getMotherCountry().getCountryId());
////        assertEquals("Request Mother Race", action.getMotherRace(), bd.getParent().getMotherRace().getRaceId());
////
////        //for 2 of 4BDF
////        //todo handle null pointer in birthregister action register bean
//        request.setParameter("pageNo", "1");
//        request.setParameter("register.bdfSerialNo", "19000");
//        request.setParameter("register.dateOfRegistration", "2010-07-26");
////        request.setParameter("parent.motherAgeAtBirth", bdf1.getParent().getMotherAgeAtBirth().toString());
//        request.setAttribute("child.dateOfBirth", "2010-07-10");
////
//        request.setParameter("birthDistrictId", "1");
//        request.setParameter("child.placeOfBirth", "මාතර");
//        request.setParameter("child.placeOfBirthEnglish", "Matara");
//        request.setParameter("child.birthAtHospital", "ture");
//        request.setParameter("child.childFullNameOfficialLang", "චතුරංග විතාන");
//        request.setParameter("child.childFullNameEnglish", "Chathuarnga Withana");
//        request.setParameter("register.preferredLanguage", "si");
//        request.setParameter("child.childGender", "1");
//        request.setParameter("child.childBirthWeight", "2");
//        request.setParameter("child.childRank", "1");
//        request.setParameter("child.numberOfChildrenBorn", "1");
//        initAndExecute("/births/eprBirthRegistration.do");
////        assertEquals("Action erros for 2 of 4BDF", 0, action.getActionErrors().size());
////
//        bdf1 = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
//        assertEquals("Place of birth incorrect", "මාතර", bdf1.getChild().getPlaceOfBirth());
////        assertEquals("bdId matches with the previous faild", 167, bdf1.getIdUKey());
////
////        obj = session.get(WebConstants.SESSION_USER_LANG);
////        assertNotNull("Session User Local Presence Faild", obj);
////        assertNotNull("Request Country List Presence Faild", action.getCountryList());
////        assertNotNull("Request Race List Presence Faild", action.getRaceList());
////        assertNotNull("Request Country List Presence Faild", action.getCountryList());
////
////        assertNotNull("Request Full District List Presence Faild", action.getAllDistrictList());
////        assertNotNull("Request Full DS Division List Presence Faild", action.getAllDSDivisionList());
////
////        assertEquals("Request parent Bean population faild", bd.getParent(), action.getParent());
////
////        //for 3 of 4BDF
////        //father's information
////        request.setParameter("pageNo", "2");
////        request.setParameter("parent.fatherNICorPIN", "530232026V");
////        request.setParameter("fatherCountry", "1");
////        request.setParameter("parent.fatherPassportNo", "4832");
////        request.setParameter("parent.fatherFullName", "ලෝගේස්වරන් යුවන් ශන්කර්");
////        //request.setParameter("parent.fatherDOB","");
////        request.setParameter("parent.fatherPlaceOfBirth", "Kandy");
////        request.setParameter("fatherRace", "1");
////
////        //mother's information
////        request.setParameter("parent.motherNICorPIN", "685031035V");
////        request.setParameter("motherCountry", "2");
////        request.setParameter("parent.motherPassportNo", "5999");
////        request.setParameter("parent.motherFullName", "සංගුණි ෙද්ව ෙග්");
////        //request.setParameter("parent.motherDOB", "1968-03-01");
////        request.setParameter("parent.motherAgeAtBirth", "43");
////        request.setParameter("parent.motherAddress", "65 C මල්වත්ත පාර, කොට්ටාව");
////        request.setParameter("motherDistrictId", "1");
////        //request.setParameter("motherDSDivisionId", "");
////        request.setParameter("motherRace", "1");
////        request.setParameter("parent.motherPlaceOfBirth", "kandana");
////        request.setParameter("parent.motherAdmissionNo", "125");
////        request.setParameter("parent.motherEmail", "sanguni@gmail.com");
////        //request.setParameter("parent.motherAdmissionDate", "2010-06-27");
////        request.setParameter("parent.motherPhoneNo", "0112345678");
////
////        initAndExecute("/births/eprBirthRegistration.do");
////        assertEquals("Action erros for 3 of 4BDF", 0, action.getActionErrors().size());
////
////        assertEquals("Request marriage Bean population faild", bd.getMarriage(), action.getMarriage());
////        assertEquals("Request grandFather Bean population faild", bd.getGrandFather(), action.getGrandFather());
////        assertEquals("Request informant Bean population faild", bd.getInformant(), action.getInformant());
////
////        //for 4 of 4BDF
////        request.setParameter("pageNo", "3");
////        request.setParameter("marriage.parentsMarried", "1");
////        request.setParameter("marriage.placeOfMarriage", "Kaduwela");
////        //request.setParameter("marriage.dateOfMarriage", "2008-09-02");
////        request.setParameter("marriage.motherSigned", "0");
////        request.setParameter("marriage.fatherSigned", "0");
////        request.setParameter("grandFather.grandFatherFullName", "Grand Father Full Name");
////        request.setParameter("grandFather.grandFatherNICorPIN", "2238485f85V");
////        request.setParameter("grandFather.grandFatherBirthYear", "1922");
////        request.setParameter("grandFather.grandFatherBirthPlace", "Kandy");
////
////        request.setParameter("grandFather.greatGrandFatherFullName", "Great Grand Father Full Name");
////        request.setParameter("grandFather.greatGrandFatherNICorPIN", "100232026V");
////        request.setParameter("grandFather.greatGrandFatherBirthYear", "1890");
////        request.setParameter("grandFather.greatGrandFatherBirthPlace", "Galle");
////        request.setParameter("informant.informantType", "MOTHER");
////
////        request.setParameter("informant.informantNICorPIN", "685031035V");
////        request.setParameter("informant.informantName", "සංගුණි ෙද්ව ෙග්");
////        request.setParameter("informant.informantAddress", "Kandy Road Matale");
////        request.setParameter("informant.informantPhoneNo", "081234567");
////        request.setParameter("informant.informantEmail", "sanguni@gmail.com");
////        request.setParameter("informant.informantSignDate", "2010-07-21");
////
////        initAndExecute("/births/eprBirthRegistration.do");
////        assertEquals("Action erros for 4 of 4BDF", 0, action.getActionErrors().size());


    }

    private Map login(String userName, String password) throws Exception {
        request.setParameter("userName", userName);
        request.setParameter("password", password);
        ActionProxy proxy = getActionProxy("/eprLogin.do");
        loginAction = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        proxy.execute();
        return loginAction.getSession();
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

    private BirthDeclaration getBDFById(long serial) {
        return birthDeclarationDAO.getById(serial);
    }

    private BirthDeclaration getBDFByBDDivisionAndSerial(BDDivision bdDivision, long serial) {
        return birthDeclarationDAO.getByBDDivisionAndSerialNo(bdDivision, serial);
    }
}