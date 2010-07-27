package lk.rgd.crs.web.action;

import lk.rgd.common.CustomStrutsTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import lk.rgd.crs.web.action.births.BirthRegisterAction;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.domain.*;

/**
 * @author Indunil Moremada
 * @author amith jayasekara
 */
public class BirthRegisterActionTest extends CustomStrutsTestCase {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterActionTest.class);
    private ActionProxy proxy;
    private BirthRegisterAction action;
    private LoginAction loginAction;
    private BirthDeclaration bd;

    private String initAndExecute(String mapping, Map session) throws Exception {
        proxy = getActionProxy(mapping);
        action = (BirthRegisterAction) proxy.getAction();
        logger.debug("Action Method to be executed is {} ", proxy.getMethod());
        ActionContext.getContext().setSession(session);
        String result = null;
        try {
            result = proxy.execute();
        } catch (NullPointerException e) {
            logger.error("non fatal proxy execution error", e.getMessage());
        }
        logger.debug("result for mapping {} is {}", mapping, result);
        return result;
    }

    @Override
    public String getContextLocations() {
        return "unitTest_applicationContext.xml";
    }

    public void testActionMappingProxy() {
        ActionMapping mapping = getActionMapping("/births/eprBirthRegistrationInit.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/births", mapping.getNamespace());
        assertEquals("eprBirthRegistrationInit", mapping.getName());
        ActionProxy proxy = getActionProxy("/births/eprBirthRegistrationInit.do");
        assertNotNull(proxy);
        logger.debug("nameSpace {} and actionName {}", mapping.getNamespace(), proxy.getMethod());

        BirthRegisterAction action = (BirthRegisterAction) proxy.getAction();
        assertNotNull(action);
    }

    public void testBirthDeclaratinInitializer() throws Exception {
        Object obj;
        Map session = login("indunil", "indunil");

        initAndExecute("/births/eprBirthRegistrationInit.do", session);
        session = action.getSession();
        assertEquals("Action erros for 1 of 4BDF", 0, action.getActionErrors().size());

        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);

        obj = session.get(WebConstants.SESSION_USER_LANG);
        assertNotNull("Session User Local Presence", obj);
        assertNotNull("Request District List Presence", action.getDistrictList());
        assertNotNull("Request Race List Presence", action.getRaceList());

        //check whether required beans are populated
        assertEquals("Request child Bean is Populated", bd.getChild(), action.getChild());
        assertEquals("Request register Bean is Populated", bd.getRegister(), action.getRegister());

        //todo
    }

    public void testBirthDeclarationInitializerInEditMode() throws Exception {
        Map session = login("indunil", "indunil");

        request.setParameter("bdId", "166");
        initAndExecute("/births/eprBirthRegistrationInit.do", session);
        session = action.getSession();
        assertEquals("Action erros for 1 of 4BDF", 0, action.getActionErrors().size());

        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertEquals("Data Entry Mode Faild", 0, bd.getRegister().getStatus().ordinal());

        Object obj = session.get(WebConstants.SESSION_USER_LANG);
        assertNotNull("Session User Local Presence", obj);
        assertNotNull("Request District List Presence", action.getDistrictList());
        assertNotNull("Request Race List Presence", action.getRaceList());

        //todo set date values to request

        //check whether required beans are populated
        assertEquals("Request child Bean is Populated", bd.getChild(), action.getChild());
        assertEquals("Request register Bean is Populated", bd.getRegister(), action.getRegister());

        logger.debug("IsLiveBirth {}", action.isLiveBirth());

        assertEquals("Request birthDistrictId is set to existing district", action.getRegister().getBirthDistrict().getDistrictUKey(),
            action.getBirthDistrictId());
        assertEquals("Request birthDivisionId is set to existing birthDivision", action.getRegister().getBirthDivision().getBdDivisionUKey(),
            action.getBirthDivisionId());
        assertEquals("Request dsDivisionId is set to existing dsDivision", action.getRegister().getDsDivision().getDsDivisionUKey(),
            action.getDsDivisionId());

        assertEquals("Request father Country", action.getFatherCountry(), bd.getParent().getFatherCountry().getCountryId());
        assertEquals("Request father Race", action.getFatherRace(), bd.getParent().getFatherRace().getRaceId());
        assertEquals("Request Mother Country", action.getMotherCountry(), bd.getParent().getMotherCountry().getCountryId());
        assertEquals("Request Mother Race", action.getMotherRace(), bd.getParent().getMotherRace().getRaceId());
        //String date=convertDateToString(bd.getRegister().getDateOfRegistration());
        //convertStingToDate(date);
        //for 2 of 4BDF
        //todo handle null pointer in birthregister action register bean
        request.setParameter("pageNo", "1");
        //long serialNo = bd.getRegister().getBdfSerialNo();
        request.setParameter("register.bdfSerialNo", "12345");
        //request.setParameter("register.dateOfRegistration", convertDateToString(bd.getRegister().getDateOfRegistration()));

        /*BirthRegisterInfo bri=bd.getRegister();
        bri.setDateOfRegistration(bd.getRegister().getDateOfRegistration());
        action.setRegister(bri);
*/
        request.setParameter("parent.motherAgeAtBirth", bd.getParent().getMotherAgeAtBirth().toString());
        //todo request.setAttribute("child.dateOfBirth", convertStingToDate("2010-07-10"));
        action.getRegister().setDateOfRegistration(bd.getRegister().getDateOfRegistration());

        request.setParameter("birthDistrictId", "1");
        request.setParameter("child.placeOfBirth", "මාතර");
        request.setParameter("child.placeOfBirthEnglish", "Matara");
        request.setParameter("child.birthAtHospital", "ture");
        request.setParameter("child.childFullNameOfficialLang", bd.getChild().getChildFullNameOfficialLang());
        request.setParameter("child.childFullNameEnglish", bd.getChild().getChildFullNameEnglish());
        request.setParameter("register.preferredLanguage", "si");
        request.setParameter("child.childGender", "1");
        request.setParameter("child.childBirthWeight", "2");
        request.setParameter("child.childRank", "1");
        request.setParameter("child.numberOfChildrenBorn", "1");
        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        assertEquals("Action erros for 2 of 4BDF", 0, action.getActionErrors().size());

        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertEquals("bdId matches with the previous faild", 166, bd.getIdUKey());

        obj = session.get(WebConstants.SESSION_USER_LANG);
        assertNotNull("Session User Local Presence Faild", obj);
        assertNotNull("Request Country List Presence Faild", action.getCountryList());
        assertNotNull("Request Race List Presence Faild", action.getRaceList());
        assertNotNull("Request Country List Presence Faild", action.getCountryList());

        assertNotNull("Request Full District List Presence Faild", action.getAllDistrictList());
        assertNotNull("Request Full DS Division List Presence Faild", action.getAllDSDivisionList());

        assertEquals("Request parent Bean population faild", bd.getParent(), action.getParent());

        //for 3 of 4BDF
        //father's information
        request.setParameter("pageNo", "2");
        request.setParameter("parent.fatherNICorPIN", "530232026V");
        request.setParameter("fatherCountry", "1");
        request.setParameter("parent.fatherPassportNo", "4832");
        request.setParameter("parent.fatherFullName", "ලෝගේස්වරන් යුවන් ශන්කර්");
        //request.setParameter("parent.fatherDOB","");
        request.setParameter("parent.fatherPlaceOfBirth", "Kandy");
        request.setParameter("fatherRace", "1");

        //mother's information
        request.setParameter("parent.motherNICorPIN", "685031035V");
        request.setParameter("motherCountry", "2");
        request.setParameter("parent.motherPassportNo", "5999");
        request.setParameter("parent.motherFullName", "සංගුණි ෙද්ව ෙග්");
        //request.setParameter("parent.motherDOB", "1968-03-01");
        request.setParameter("parent.motherAgeAtBirth", "43");
        request.setParameter("parent.motherAddress", "65 C මල්වත්ත පාර, කොට්ටාව");
        request.setParameter("motherDistrictId", "1");
        //request.setParameter("motherDSDivisionId", "");
        request.setParameter("motherRace", "1");
        request.setParameter("parent.motherPlaceOfBirth", "kandana");
        request.setParameter("parent.motherAdmissionNo", "125");
        request.setParameter("parent.motherEmail", "sanguni@gmail.com");
        //request.setParameter("parent.motherAdmissionDate", "2010-06-27");
        request.setParameter("parent.motherPhoneNo", "0112345678");

        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        assertEquals("Action erros for 3 of 4BDF", 0, action.getActionErrors().size());

        assertEquals("Request marriage Bean population faild", bd.getMarriage(), action.getMarriage());
        assertEquals("Request grandFather Bean population faild", bd.getGrandFather(), action.getGrandFather());
        assertEquals("Request informant Bean population faild", bd.getInformant(), action.getInformant());

        //for 4 of 4BDF
        request.setParameter("pageNo", "3");
        request.setParameter("marriage.parentsMarried", "1");
        request.setParameter("marriage.placeOfMarriage", "Kaduwela");
        //request.setParameter("marriage.dateOfMarriage", "2008-09-02");
        request.setParameter("marriage.motherSigned", "0");
        request.setParameter("marriage.fatherSigned", "0");
        request.setParameter("grandFather.grandFatherFullName", "Grand Father Full Name");
        request.setParameter("grandFather.grandFatherNICorPIN", "2238485f85V");
        request.setParameter("grandFather.grandFatherBirthYear", "1922");
        request.setParameter("grandFather.grandFatherBirthPlace", "Kandy");

        request.setParameter("grandFather.greatGrandFatherFullName", "Great Grand Father Full Name");
        request.setParameter("grandFather.greatGrandFatherNICorPIN", "100232026V");
        request.setParameter("grandFather.greatGrandFatherBirthYear", "1890");
        request.setParameter("grandFather.greatGrandFatherBirthPlace", "Galle");
        request.setParameter("informant.informantType", "MOTHER");

        request.setParameter("informant.informantNICorPIN", "685031035V");
        request.setParameter("informant.informantName", "සංගුණි ෙද්ව ෙග්");
        request.setParameter("informant.informantAddress", "Kandy Road Matale");
        request.setParameter("informant.informantPhoneNo", "081234567");
        request.setParameter("informant.informantEmail", "sanguni@gmail.com");
        //request.setParameter("informant.informantSignDate", "2010-07-21");

        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        assertEquals("Action erros for 4 of 4BDF", 0, action.getActionErrors().size());

        //assertEquals("Request notifyingAuthority Bean population faild", bd.getNotifyingAuthority(), action.getNotifyingAuthority());

        //BirthDeclaration Form Details
        request.setParameter("pageNo", "4");
        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        assertEquals("Action erros for Birth Declaration Form Details", 0, action.getActionErrors().size());
        assertNotNull("Approval Permission Faild for the user", action.isAllowApproveBDF());
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

    private Date convertStingToDate(String str_date) {
        DateFormat formatter;
        Date date = null;
        try {
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            date = (Date) formatter.parse(str_date);
            logger.debug("Date in Date format : " + date);
        } catch (ParseException e) {
            logger.error("Error parsing String to Date", e);
        }
        return date;
    }

    private String convertDateToString(Date date) {
        DateFormat formatter;
        String str_date;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        str_date = formatter.format(date);
        logger.debug("Date in String : " + str_date);
        return str_date;
    }

    /**
     * testing birth cetficate print
     *
     * @author amith jayasekara
     */
    //todo create a sample data that can be print
    /*
 public void testBirthCetificatePrint() throws Exception {
     //loggin as rg
     login("rg", "password");

     //setting buid to find BD for live biirth
     request.setParameter("bdId", "165");
     initAndExucute("/births/eprBirthCertificate.do");
    assertEquals("No Action Errors", 0, action.getActionErrors());
     //check BDF data
     assertNotNull("Child object", action.getChild());
     assertNotNull("Parent object", action.getParent());
     assertNotNull("GrandFather object", action.getGrandFather());
     assertNotNull("Marrage object", action.getMarriage());
     assertNotNull("Informant object", action.getInformant());
     assertNotNull("Confirmant object", action.getConfirmant());
     assertNotNull("Register object", action.getRegister());
     assertNotNull("Notify Authority", action.getNotifyingAuthority());

     //testing bdid =0
     request.setParameter("bdId", "0");
     initAndExucute("/births/eprBirthCertificate.do");

     assertNotNull("Action Errors", action.getActionErrors());
     assertNull("BirthDeclaration ", action.getChild());

 }
    */

    public void testStillBirthDeclarationInit() throws Exception {
        Map session = login("duminda", "duminda");

        BirthDeclaration bdOld = new BirthDeclaration();
        ChildInfo ciOld = new ChildInfo();
        ciOld.setChildFullNameEnglish("old name");
        bdOld.setChild(ciOld);
        request.setAttribute(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN, bdOld);
        initAndExecute("/births/eprStillBirthRegistrationInit.do", session);
        session = action.getSession();

        BirthDeclaration bdNew = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertFalse("Not a live birth: ",bdNew.getRegister().isLiveBirth());
        assertNotSame(bdOld.getChild().getChildFullNameEnglish(), bdNew.getChild().getChildFullNameEnglish());

        // Still birth page one
        request.setParameter("register.bdfSerialNo", "123");
        request.setParameter("register.dateOfRegistration", "2010-07-23");
        request.setParameter("child.dateOfBirth", "2010-07-20");
        request.setParameter("birthDistrictId","1");
        request.setParameter("dsDivisionId", "2");
        request.setParameter("birthDivisionId", "1");
        request.setParameter("child.placeOfBirth", "Colombo Fort (Medical)");
        request.setParameter("child.birthAtHospital", "true");
        request.setParameter("child.childGender", "0");
        request.setParameter("child.weeksPregnant", "15");
        request.setParameter("child.childRank", "1");
        request.setParameter("child.numberOfChildrenBorn", "1");
        request.setParameter("pageNo", "1");
        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        assertFalse("Not a live birth:", action.isLiveBirth());
        assertSame("Child ds division id: ", 2, action.getDsDivisionId());

        // todo Still birth page two
    }
}
