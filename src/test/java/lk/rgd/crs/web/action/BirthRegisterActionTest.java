package lk.rgd.crs.web.action;

import lk.rgd.common.CustomStrutsTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.StrutsSpringTestCase;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.springframework.context.ApplicationContext;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import lk.rgd.crs.web.action.births.BirthRegisterAction;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.UnitTestManager;
import lk.rgd.Permission;

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
            logger.error("non fatal proxy execution error", e.getMessage(),e);
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

    public void testAddBirthDeclaration() throws Exception {
        Object obj;
        Map session = login("rg", "password");
        initAndExecute("/births/eprBirthRegistrationInit.do", session);
        session = action.getSession();
        assertEquals("Action erros for 1 of 4BDF", 0, action.getActionErrors().size());

        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertNotNull("Faild to put Birth Declaration to the session", bd);
        obj = session.get(WebConstants.SESSION_USER_LANG);
        assertNotNull("Session User Local Presence", obj);
        assertNotNull("Request District List Presence", action.getDistrictList());
        assertNotNull("Request Race List Presence", action.getRaceList());
        assertEquals("Error of populating a value to bdId", 0, action.getBdId());
        assertTrue("Not a Live birth", action.isLiveBirth());
        //check whether required beans are populated
        assertEquals("Request child Bean is Populated", bd.getChild(), action.getChild());
        assertEquals("Request register Bean is Populated", bd.getRegister(), action.getRegister());

        //for 2 of 4BDF
        request.setParameter("pageNo", "1");
        request.setParameter("register.bdfSerialNo", "12345");
        request.setParameter("register.dateOfRegistration", "2010-07-02T00:00:00+05:30");

        request.setParameter("parent.motherAgeAtBirth", "42");
        request.setParameter("child.dateOfBirth", "2010-07-01T00:00:00+05:30");
        request.setParameter("birthDistrictId", "1");
        request.setParameter("birthDivisionId", "10");
        request.setParameter("child.placeOfBirth", "මාතර");
        request.setParameter("child.placeOfBirthEnglish", "Matara");
        request.setParameter("child.birthAtHospital", "ture");
        request.setParameter("child.childFullNameOfficialLang", "kamal silva");
        request.setParameter("child.childFullNameEnglish", "KAMAL SILVA");
        request.setParameter("register.preferredLanguage", "si");
        request.setParameter("child.childGender", "1");
        request.setParameter("child.childBirthWeight", "2");
        request.setParameter("child.childRank", "1");
        request.setParameter("child.numberOfChildrenBorn", "1");
        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();

        assertEquals("Action erros for 2 of 4BDF", 0, action.getActionErrors().size());

        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        obj = session.get(WebConstants.SESSION_USER_LANG);
        assertNotNull("Session User Local Presence Faild", obj);
        assertNotNull("Request Country List Presence Faild", action.getCountryList());
        assertNotNull("Request Race List Presence Faild", action.getRaceList());
        assertNotNull("Request Country List Presence Faild", action.getCountryList());
        assertNotNull("Request Full District List Presence Faild", action.getAllDistrictList());
        assertNotNull("Request Full DS Division List Presence Faild", action.getAllDSDivisionList());
        assertEquals("Request parent Bean population faild", bd.getParent(), action.getParent());

        //check whether the session bean is updated with the previous request values
        assertEquals("Failed to update the birth declaration session with child full name in english", "KAMAL SILVA", bd.getChild().getChildFullNameEnglish());
        assertEquals("Failed to update the birth declaration session with serial number", 12345, bd.getRegister().getBdfSerialNo());
        assertEquals("Failed to update the birth declaration session with preffered language", "si", bd.getRegister().getPreferredLanguage());
        assertEquals("Failed to update the birth declaration session with birth place", "මාතර", bd.getChild().getPlaceOfBirth());

        //for 3 of 4BDF
        request.setParameter("pageNo", "2");
        //father's information
        request.setParameter("parent.fatherNICorPIN", "530232026V");
        request.setParameter("fatherCountry", "1");
        request.setParameter("parent.fatherPassportNo", "4832");
        request.setParameter("parent.fatherFullName", "ලෝගේස්වරන් යුවන් ශන්කර්");
        request.setParameter("parent.fatherDOB", "1964-07-02T00:00:00+05:30");
        request.setParameter("parent.fatherPlaceOfBirth", "Kandy");
        request.setParameter("fatherRace", "1");

        //mother's information
        request.setParameter("parent.motherNICorPIN", "685031035V");
        request.setParameter("motherCountry", "2");
        request.setParameter("parent.motherPassportNo", "5999");
        request.setParameter("parent.motherFullName", "සංගුණි ෙද්ව ෙග්");
        request.setParameter("parent.motherDOB", "1968-03-01T00:00:00+05:30");
        request.setParameter("parent.motherAgeAtBirth", "43");
        request.setParameter("parent.motherAddress", "65 C මල්වත්ත පාර, කොට්ටාව");
        request.setParameter("motherDistrictId", "1");
        request.setParameter("motherRace", "1");
        request.setParameter("parent.motherPlaceOfBirth", "kandana");
        request.setParameter("parent.motherAdmissionNo", "125");
        request.setParameter("parent.motherEmail", "info@gmail.com");
        request.setParameter("parent.motherAdmissionDate", "2010-06-28T00:00:00+05:30");
        request.setParameter("parent.motherPhoneNo", "0112345678");

        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        assertEquals("Action erros for 3 of 4BDF", 0, action.getActionErrors().size());

        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        //convertStringDateToDate("1964-June-02");
        //check whether the birth declaration session bean is updated with the previous request values
        assertEquals("failed to update birth declaration session with father NIC/PIN", "530232026V", bd.getParent().getFatherNICorPIN());
        assertEquals("failed to update birth declaration session with father country", 1, bd.getParent().getFatherCountry().getCountryId());
        assertEquals("failed to update birth declaration session with father passport number", "4832", bd.getParent().getFatherPassportNo());
        assertEquals("failed to update birth declaration session with father full name", "ලෝගේස්වරන් යුවන් ශන්කර්", bd.getParent().getFatherFullName());
        assertEquals("failed to update birth declaration session with father DOB", convertStringDateToDate("1964-july-02"), bd.getParent().getFatherDOB());
        assertEquals("failed to update birth declaration session with father place of birth", "Kandy", bd.getParent().getFatherPlaceOfBirth());
        assertEquals("failed to update birth declaration session with father Race", 1, bd.getParent().getFatherRace().getRaceId());

        assertEquals("failed to update birth declaration session with mother NIC/PIN", "685031035V", bd.getParent().getMotherNICorPIN());
        assertEquals("failed to update birth declaration session with mother country", 2, bd.getParent().getMotherCountry().getCountryId());
        assertEquals("failed to update birth declaration session with mother passport number", "5999", bd.getParent().getMotherPassportNo());
        assertEquals("failed to update birth declaration session with mother full Name", "සංගුණි ෙද්ව ෙග්", bd.getParent().getMotherFullName());
        assertEquals("failed to update birth declaration session with mother mother DOB", convertStringDateToDate("1968-march-01"), bd.getParent().getMotherDOB());
        assertEquals("failed to update birth declaration session with mother age at birth", 43, bd.getParent().getMotherAgeAtBirth().intValue());
        assertEquals("failed to update birth declaration session with mother address", "65 C මල්වත්ත පාර, කොට්ටාව", bd.getParent().getMotherAddress());
        //assertEquals("failed to update birth declaration session with mother district id",1, bd.getParent().getMotherDSDivision().getDistrict().getDistrictUKey());
        assertEquals("failed to update birth declaration session with mother race id", 1, bd.getParent().getMotherRace().getRaceId());
        assertEquals("failed to update birth declaration session with mother place of birth", "kandana", bd.getParent().getMotherPlaceOfBirth());
        assertEquals("failed to update birth declaration session with mother admission number", "125", bd.getParent().getMotherAdmissionNo());
        assertEquals("failed to update birth declaration session with mother email", "info@gmail.com", bd.getParent().getMotherEmail());
        assertEquals("failed to update birth declaration session with mother admission date", convertStringDateToDate("2010-june-28"), bd.getParent().getMotherAdmissionDate());
        assertEquals("failed to update birth declaration session with mother phone number", "0112345678", bd.getParent().getMotherPhoneNo());

        assertNotNull("marriage Bean population faild", action.getMarriage());
        assertNotNull("grandFather Bean population faild", action.getGrandFather());
        assertNotNull("informant Bean population faild", action.getInformant());

        //for 4 of 4BDF
        request.setParameter("pageNo", "3");
        request.setParameter("marriage.parentsMarried", "1");
        request.setParameter("marriage.placeOfMarriage", "Kaduwela");
        request.setParameter("marriage.dateOfMarriage", "2007-09-02T00:00:00+05:30");

        request.setParameter("informant.informantType", "MOTHER");

        request.setParameter("informant.informantNICorPIN", "685031035V");
        request.setParameter("informant.informantName", "සංගුණි ෙද්ව ෙග්");
        request.setParameter("informant.informantAddress", "Kandy Road Matale");
        request.setParameter("informant.informantPhoneNo", "081234567");
        request.setParameter("informant.informantEmail", "info@gmail.com");
        request.setParameter("informant.informantSignDate", "2010-07-20T00:00:00+05:30");

        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        assertEquals("Action erros for 4 of 4BDF", 0, action.getActionErrors().size());
        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);

        assertEquals("failed to update birth declaration session with parent married", 1, bd.getMarriage().getParentsMarried().intValue());
        assertEquals("failed to update birth declaration session with place of marriage", "Kaduwela", bd.getMarriage().getPlaceOfMarriage());
        assertEquals("failed to update birth declaration session with date of marriage", convertStringDateToDate("2007-sep-02"), bd.getMarriage().getDateOfMarriage());
        assertEquals("failed to update birth declaration session with informant type", 1, bd.getInformant().getInformantType().ordinal());
        assertEquals("failed to update birth declaration session with informant NIC/PIN", "685031035V", bd.getInformant().getInformantNICorPIN());
        assertEquals("failed to update birth declaration session with informant name", "සංගුණි ෙද්ව ෙග්", bd.getInformant().getInformantName());
        assertEquals("failed to update birth declaration session with informant address", "Kandy Road Matale", bd.getInformant().getInformantAddress());
        assertEquals("failed to update birth declaration session with informant phone number", "081234567", bd.getInformant().getInformantPhoneNo());
        assertEquals("failed to update birth declaration session with informant Email", "info@gmail.com", bd.getInformant().getInformantEmail());
        assertEquals("failed to update birth declaration session with informant signed date", convertStringDateToDate("2010-jul-20"), bd.getInformant().getInformantSignDate());
        assertNotNull("notifyingAuthority Bean population faild", action.getNotifyingAuthority());

        //BirthDeclaration Form Details
        request.setParameter("pageNo", "4");
        request.setParameter("notifyingAuthority.notifyingAuthorityPIN", "685031035V");
        request.setParameter("notifyingAuthority.notifyingAuthorityName", "සංගුණි ෙද්ව ෙග්");
        request.setParameter("notifyingAuthority.notifyingAuthorityAddress", "65 C මල්වත්ත පාර, කොට්ටාව");
        request.setParameter("notifyingAuthority.notifyingAuthoritySignDate", "2010-07-02T00:00:00+05:30");
        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        assertEquals("Action erros for Birth Declaration Form Details", 0, action.getActionErrors().size());
        logger.debug("approval permission for the user : {}", action.isAllowApproveBDF());
        assertEquals("Faild to remove BirthDeclaration", null, session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));
        logger.debug("successfully persisted with the bdId :{}", action.getBdId());

    }

    public void testBirthDeclarationEditMode() throws Exception {
        Map session = login("rg", "password");
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
        assertTrue("Not a live birth", action.isLiveBirth());

        //check whether required beans are populated
        assertEquals("Request child Bean is Populated", bd.getChild(), action.getChild());
        assertEquals("Request register Bean is Populated", bd.getRegister(), action.getRegister());

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

        //for 2 of 4BDF
        request.setParameter("pageNo", "1");
        request.setParameter("register.bdfSerialNo", "12345");
        request.setParameter("register.dateOfRegistration", "2010-07-02T00:00:00+05:30");
        request.setParameter("child.dateOfBirth", "2010-07-01T00:00:00+05:30");
        request.setParameter("birthDistrictId", "1");
        request.setParameter("birthDivisionId", "10");
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
        request.setParameter("pageNo", "2");
        //father's information
        request.setParameter("parent.fatherNICorPIN", "530232026V");
        request.setParameter("fatherCountry", "1");
        request.setParameter("parent.fatherPassportNo", "4832");
        request.setParameter("parent.fatherFullName", "ලෝගේස්වරන් යුවන් ශන්කර්");
        request.setParameter("parent.fatherDOB", "1964-07-02T00:00:00+05:30");
        request.setParameter("parent.fatherPlaceOfBirth", "Kandy");
        request.setParameter("fatherRace", "1");

        //mother's information
        request.setParameter("parent.motherNICorPIN", "685031035V");
        request.setParameter("motherCountry", "2");
        request.setParameter("parent.motherPassportNo", "5999");
        request.setParameter("parent.motherFullName", "සංගුණි ෙද්ව ෙග්");
        request.setParameter("parent.motherDOB", "1968-03-01T00:00:00+05:30");
        request.setParameter("parent.motherAgeAtBirth", "43");
        request.setParameter("parent.motherAddress", "65 C මල්වත්ත පාර, කොට්ටාව");
        request.setParameter("motherDistrictId", "1");
        request.setParameter("motherRace", "1");
        request.setParameter("parent.motherPlaceOfBirth", "kandana");
        request.setParameter("parent.motherAdmissionNo", "125");
        request.setParameter("parent.motherEmail", "info@gmail.com");
        request.setParameter("parent.motherAdmissionDate", "2010-06-28T00:00:00+05:30");
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
        request.setParameter("marriage.dateOfMarriage", "2007-09-02T00:00:00+05:30");
        request.setParameter("marriage.motherSigned", "0");
        request.setParameter("marriage.fatherSigned", "0");
        request.setParameter("grandFather.grandFatherFullName", "Grand Father Full Name");
        request.setParameter("grandFather.grandFatherNICorPIN", "223848585V");
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
        request.setParameter("informant.informantEmail", "info@gmail.com");
        request.setParameter("informant.informantSignDate", "2010-07-20T00:00:00+05:30");

        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        assertEquals("Action erros for 4 of 4BDF", 0, action.getActionErrors().size());

        assertEquals("Request notifyingAuthority Bean population faild", bd.getNotifyingAuthority(), action.getNotifyingAuthority());

        //BirthDeclaration Form Details
        request.setParameter("pageNo", "4");
        request.setParameter("notifyingAuthority.notifyingAuthorityPIN", "685031035V");
        request.setParameter("notifyingAuthority.notifyingAuthorityName", "සංගුණි ෙද්ව ෙග්");
        request.setParameter("notifyingAuthority.notifyingAuthorityAddress", "65 C මල්වත්ත පාර, කොට්ටාව");
        request.setParameter("notifyingAuthority.notifyingAuthoritySignDate", "2010-07-02T00:00:00+05:30");
        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        assertEquals("Action erros for Birth Declaration Form Details", 0, action.getActionErrors().size());
        logger.debug("approval permission for the user : {}", action.isAllowApproveBDF());
        assertEquals("Faild to remove BirthDeclaration", null, session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));
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

        String result = initAndExecute("/births/eprStillBirthRegistrationInit.do", session);
        session = action.getSession();
        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertFalse("Not a live birth failed ", bd.getRegister().isLiveBirth());
        //assertEquals("form0", result);

        // Still birth page one
        request.setParameter("register.bdfSerialNo", "123");
        request.setParameter("register.dateOfRegistration", "2010-07-14T00:00:00+05:30");
        request.setParameter("child.dateOfBirth", "2010-07-14T00:00:00+05:30");
        request.setParameter("birthDistrictId", "1");
        request.setParameter("dsDivisionId", "2");
        request.setParameter("birthDivisionId", "6");
        request.setParameter("child.placeOfBirth", "Colombo Fort (Medical)");
        request.setParameter("child.birthAtHospital", "true");
        request.setParameter("child.childGender", "0");
        request.setParameter("child.weeksPregnant", "15");
        request.setParameter("child.childRank", "1");
        request.setParameter("child.numberOfChildrenBorn", "1");
        request.setParameter("pageNo", "1");
        result = initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertFalse("Not a live birth failed", bd.getRegister().isLiveBirth());
        assertEquals("Register bdf serial no failed", (long)123, bd.getRegister().getBdfSerialNo());
        assertEquals("Date of registration failed", "Wed Jul 14 00:00:00 IST 2010", bd.getRegister().getDateOfRegistration().toString());
        assertEquals("Date of birth failed", "Wed Jul 14 00:00:00 IST 2010", bd.getChild().getDateOfBirth().toString());
        assertEquals("Birth district Id failed", 1, bd.getRegister().getBirthDistrict().getDistrictUKey());
        assertEquals("DSDivision Id failed", 2, bd.getRegister().getBirthDivision().getDsDivision().getDsDivisionUKey());
        assertEquals("Child Birth Division failed", 6, bd.getRegister().getBirthDivision().getBdDivisionUKey());
        assertEquals("Child Place of birth failed", "Colombo Fort (Medical)", bd.getChild().getPlaceOfBirth());
        assertEquals("No action errors", 0, action.getActionErrors().size());
        //assertEquals("form1", result);


        // Still birth page two
        request.setParameter("parent.fatherNICorPIN", "11111111v");
        request.setParameter("parent.fatherFullName", "father full name");
        request.setParameter("parent.fatherDOB", "1965-07-14T00:00:00+05:30");
        request.setParameter("parent.fatherPlaceOfBirth", "father birth place");
        request.setParameter("fatherRace", "1");
        request.setParameter("parent.motherNICorPIN", "22222222v");
        request.setParameter("parent.motherFullName", "mother full name");
        request.setParameter("parent.motherDOB", "1970-07-14T00:00:00+05:30");
        request.setParameter("parent.motherAgeAtBirth", "40");
        request.setParameter("parent.motherAddress", "mother address");
        request.setParameter("motherDistrictId", "1");
        request.setParameter("motherDSDivisionId", "1");
        request.setParameter("motherRace", "1");
        request.setParameter("motherPlaceOfBirth", "mother birth place");
        request.setParameter("parent.motherAdmissionNo", "222");
        request.setParameter("parent.motherPhoneNo", "0789456123");
        request.setParameter("pageNo", "2");
        result = initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertFalse("Not a live birth:", bd.getRegister().isLiveBirth());
        assertEquals("Father NIC", "11111111v", bd.getParent().getFatherNICorPIN());
        assertNull("Father passport no:", bd.getParent().getFatherPassportNo());
        assertEquals("No action errors", 0, action.getActionErrors().size());

        // Still birth page three.
        request.setParameter("marriage.parentsMarried", "1");
        request.setParameter("informant.informantType", "GUARDIAN");
        request.setParameter("informant.informantNICorPIN", "33333333v");
        request.setParameter("informant.informantName", "informant name");
        request.setParameter("informant.informantAddress", "informant address");
        request.setParameter("informant.informantPhoneNo", "0123456789");
        request.setParameter("informant.informantEmail", "informant@email.mail");
        request.setParameter("informant.informantSignDate", "2010-07-14T00:00:00+05:30");
        request.setParameter("pageNo", "3");
        result = initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertFalse("Not a live birth:", bd.getRegister().isLiveBirth());
        assertEquals("Father NIC", "11111111v", bd.getParent().getFatherNICorPIN());
        assertNull("Father passport no:", bd.getParent().getFatherPassportNo());
        assertEquals("Informant NIC: ", "33333333v", bd.getInformant().getInformantNICorPIN());
        assertEquals("No action errors", 0, action.getActionErrors().size());

        // Still birth page four.
        request.setParameter("notifyingAuthority.notifyingAuthorityPIN", "44444444v");
        request.setParameter("notifyingAuthority.notifyingAuthorityName", "notifier name");
        request.setParameter("notifyingAuthority.notifyingAuthorityAddress", "notifier address");
        request.setParameter("notifyingAuthority.notifyingAuthoritySignDate", "2010-07-14T00:00:00+05:30");
        request.setParameter("pageNo", "4");
        result = initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertFalse("Not a live birth:", bd.getRegister().isLiveBirth());
        assertEquals("Father NIC", "11111111v", bd.getParent().getFatherNICorPIN());
        assertNull("Father passport no:", bd.getParent().getFatherPassportNo());
        assertEquals("Informant NIC: ", "33333333v", bd.getInformant().getInformantNICorPIN());
        assertEquals("Notifier NIC: ", "44444444v", bd.getNotifyingAuthority().getNotifyingAuthorityPIN());
        assertEquals("Notifier sign date: ", "Wed Jul 14 00:00:00 IST 2010", bd.getNotifyingAuthority().getNotifyingAuthoritySignDate().toString());
        assertEquals("No action errors", 0, action.getActionErrors().size());

    }

    private Date convertStringDateToDate(String str_date) {
        DateFormat formatter;
        Date date = null;
        try {
            formatter = new SimpleDateFormat("yy-MMM-dd");
            date = formatter.parse(str_date);
            logger.debug("date is {}", date);
        } catch (ParseException e) {
            logger.error("Exception ", e);
        }
        return date;
    }
}
