package lk.rgd.crs.web.action;

import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.util.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.springframework.context.ApplicationContext;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;

import java.util.*;

import lk.rgd.crs.web.action.births.BirthRegisterAction;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.UnitTestManager;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.extensions.TestSetup;

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

    protected static BDDivision colomboBDDivision;
    protected static BDDivision negamboBDDivision;
    protected static Country sriLanka;
    protected static Race sinhalese;

    protected final static ApplicationContext ctx = UnitTestManager.ctx;
    protected final static UserManager userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);
    protected final static BirthRegistrationService birthRegistrationService = (BirthRegistrationService) ctx.getBean("manageBirthService", BirthRegistrationService.class);
    protected final static BDDivisionDAO bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
    protected final static CountryDAO countryDAO = (CountryDAO) ctx.getBean("countryDAOImpl", CountryDAO.class);
    protected final static RaceDAO raceDOA = (RaceDAO) ctx.getBean("raceDAOImpl", RaceDAO.class);


    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(BirthRegisterActionTest.class)) {
            protected void setUp() throws Exception {
                logger.info("setup called");
                colomboBDDivision = bdDivisionDAO.getBDDivisionByPK(1);
                negamboBDDivision = bdDivisionDAO.getBDDivisionByPK(9);
                sriLanka = countryDAO.getCountry(1);
                sinhalese = raceDOA.getRace(1);

                List birth = sampleBirths();
                User sampleUser = loginSampleUser();
                for (int i = 0; i < birth.size(); i++) {
                    birthRegistrationService.addLiveBirthDeclaration((BirthDeclaration) birth.get(i), false, sampleUser);
                }

                super.setUp();
            }

            protected void tearDown() throws Exception {
                logger.info("tear down called ");
                super.tearDown();
            }
        };
        return setup;
    }

    private static User loginSampleUser() {
        User rg = null;
        try {
            rg = userManager.authenticateUser("rg", "password");
        }
        catch (AuthorizationException e) {
            logger.debug("exception when authorizing a user :'rg' ");
        }
        return rg;
    }

    private static List sampleBirths() {
        List list = new LinkedList();

        for (int i = 0; i < 10; i++) {
            // get Calendar with current date
            java.util.GregorianCalendar gCal = new GregorianCalendar();

            BirthDeclaration bd = new BirthDeclaration();
            //child info
            ChildInfo child = new ChildInfo();
            //set birth date 20 days before today
            gCal.add(Calendar.DATE, -20);
            child.setDateOfBirth(gCal.getTime());
            child.setChildGender(0);
            child.setPlaceOfBirth("මාතර");
            child.setPlaceOfBirthEnglish("Matara");
            child.setBirthAtHospital(true);
            child.setChildFullNameEnglish("KAMAL SILVA");
            child.setChildFullNameOfficialLang("kamal silva");
            child.setChildRank(1 + i);
            child.setChildBirthWeight(new Float(1 + i));
            //todo warning
            child.setNumberOfChildrenBorn(0);

            //Birth Register info
            BirthRegisterInfo register = new BirthRegisterInfo();
            register.setPreferredLanguage("si");
            register.setBdfSerialNo(new Long(2010012360 + i));
            register.setPreferredLanguage("si");
            //birth devision
            register.setBirthDivision(colomboBDDivision);
            register.setDateOfRegistration(gCal.getTime());
            register.setBirthType(BirthDeclaration.BirthType.LIVE);
            //todo
            //parent info
            ParentInfo parent = new ParentInfo();
            parent.setFatherCountry(sriLanka);
            parent.setFatherRace(sinhalese);
            parent.setMotherCountry(sriLanka);
            parent.setMotherRace(sinhalese);
            parent.setMotherAgeAtBirth(42 + i);
            parent.setFatherNICorPIN("530232026V");
            parent.setFatherFullName("ලෝගේස්වරන් යුවන් ශන්කර්");

            //marriage info
            MarriageInfo marriage = new MarriageInfo();

            //grand father info
            GrandFatherInfo granFather = new GrandFatherInfo();

            //notification authority
            NotifyingAuthorityInfo notification = new NotifyingAuthorityInfo();
            notification.setNotifyingAuthorityPIN("pin notification" + i);
            notification.setNotifyingAuthorityName("notification authority name" + i);
            notification.setNotifyingAuthorityAddress("notification authority address" + i);
            //set notification date tomorrow from today
            gCal.add(Calendar.DATE, +1);
            notification.setNotifyingAuthoritySignDate(gCal.getTime());

            //informant info
            InformantInfo informant = new InformantInfo();
            informant.setInformantType(InformantInfo.InformantType.GUARDIAN);
            informant.setInformantName("informant name" + i);
            informant.setInformantAddress("informant address" + i);
            informant.setInformantSignDate(gCal.getTime());

            ConfirmantInfo confirmant = new ConfirmantInfo();

            bd.setChild(child);
            bd.setRegister(register);
            bd.setParent(parent);
            bd.setMarriage(marriage);
            bd.setGrandFather(granFather);
            bd.setNotifyingAuthority(notification);
            bd.setInformant(informant);
            bd.setConfirmant(confirmant);

            list.add(bd);

        }
        return list;
    }

    private String initAndExecute(String mapping, Map session) throws Exception {
        proxy = getActionProxy(mapping);
        action = (BirthRegisterAction) proxy.getAction();
        logger.debug("Action Method to be executed is {} ", proxy.getMethod());
        ActionContext.getContext().setSession(session);
        String result = null;
        try {
            result = proxy.execute();
        } catch (NullPointerException e) {
            logger.error("non fatal proxy execution error", e.getMessage(), e);
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
        assertEquals("Action errors for 1 of 4BDF", 0, action.getActionErrors().size());

        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertNotNull("Failed to put Birth Declaration to the session", bd);
        obj = session.get(WebConstants.SESSION_USER_LANG);
        assertNotNull("Session User Local Presence", obj);
        assertNotNull("Request District List Presence", action.getDistrictList());
        assertNotNull("Request Race List Presence", action.getRaceList());
        assertEquals("Error of populating a value to bdId", 0, action.getBdId());
        assertEquals("Not a Live birth", BirthDeclaration.BirthType.LIVE, action.getBirthType());
        //check whether required beans are populated
        assertEquals("Request child Bean is Populated", bd.getChild(), action.getChild());
        assertEquals("Request register Bean is Populated", bd.getRegister(), action.getRegister());

        //for 2 of 4BDF
        request.setParameter("pageNo", "1");
        request.setParameter("register.bdfSerialNo", "12345");
        request.setParameter("register.dateOfRegistration", "08/09/2010");

        request.setParameter("parent.motherAgeAtBirth", "42");
        request.setParameter("child.dateOfBirth", "01/09/2010");
        request.setParameter("birthDistrictId", "1");
        request.setParameter("birthDivisionId", "10");
        request.setParameter("child.placeOfBirth", "මාතර");
        request.setParameter("child.placeOfBirthEnglish", "Matara");
        request.setParameter("child.birthAtHospital", "true");
        request.setParameter("child.childFullNameOfficialLang", "kamal silva");
        request.setParameter("child.childFullNameEnglish", "KAMAL SILVA");
        request.setParameter("register.preferredLanguage", "si");
        request.setParameter("child.childGender", "1");
        request.setParameter("child.childBirthWeight", "2");
        request.setParameter("child.childRank", "1");
        request.setParameter("child.numberOfChildrenBorn", "1");
        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();

        assertEquals("Action errors for 2 of 4BDF", 0, action.getActionErrors().size());

        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        obj = session.get(WebConstants.SESSION_USER_LANG);
        assertNotNull("Session User Local Presence Failed", obj);
        assertNotNull("Request Country List Presence Failed", action.getCountryList());
        assertNotNull("Request Race List Presence Failed", action.getRaceList());
        assertNotNull("Request Country List Presence Failed", action.getCountryList());
        assertNotNull("Request Full District List Presence Failed", action.getAllDistrictList());
        assertNotNull("Request Full DS Division List Presence Failed", action.getAllDSDivisionList());
        assertEquals("Request parent Bean population failed", bd.getParent(), action.getParent());

        //check whether the session bean is updated with the previous request values
        assertEquals("Failed to update the birth declaration session with child full name in english", "KAMAL SILVA", bd.getChild().getChildFullNameEnglish());
        assertEquals("Failed to update the birth declaration session with serial number", 12345, bd.getRegister().getBdfSerialNo());
        assertEquals("Failed to update the birth declaration session with preferred language", "si", bd.getRegister().getPreferredLanguage());
        assertEquals("Failed to update the birth declaration session with birth place", "මාතර", bd.getChild().getPlaceOfBirth());

        //todo
        //for 3 of 4BDF
        request.setParameter("pageNo", "2");
        //father's information
        request.setParameter("parent.fatherNICorPIN", "530232026V");
        request.setParameter("fatherCountry", "1");
        request.setParameter("parent.fatherPassportNo", "4832");
        request.setParameter("parent.fatherFullName", "ලෝගේස්වරන් යුවන් ශන්කර්");
        request.setParameter("parent.fatherDOB", "1964-08-09");
        request.setParameter("parent.fatherPlaceOfBirth", "Kandy");
        request.setParameter("fatherRace", "1");

        //mother's information
        request.setParameter("parent.motherNICorPIN", "685031035V");
        request.setParameter("motherCountry", "2");
        request.setParameter("parent.motherPassportNo", "5999");
        request.setParameter("parent.motherFullName", "සංගුණි ෙද්ව ෙග්");
        request.setParameter("parent.motherDOB", "1968-08-09");
        request.setParameter("parent.motherAgeAtBirth", "43");
        request.setParameter("parent.motherAddress", "65 C මල්වත්ත පාර, කොට්ටාව");
        request.setParameter("motherDistrictId", "1");
        request.setParameter("motherRace", "1");
        request.setParameter("parent.motherPlaceOfBirth", "kandana");
        request.setParameter("parent.motherAdmissionNo", "125");
        request.setParameter("parent.motherEmail", "info@gmail.com");
        request.setParameter("parent.motherAdmissionDate", "2010-01-09");
        request.setParameter("parent.motherPhoneNo", "0112345678");

        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        assertEquals("Action errors for 3 of 4BDF", 0, action.getActionErrors().size());

        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);

        //check whether the birth declaration session bean is updated with the previous request values
        assertEquals("failed to update birth declaration session with father NIC/PIN", "530232026V", bd.getParent().getFatherNICorPIN());
        assertEquals("failed to update birth declaration session with father country", 1, bd.getParent().getFatherCountry().getCountryId());
        assertEquals("failed to update birth declaration session with father passport number", "4832", bd.getParent().getFatherPassportNo());
        assertEquals("failed to update birth declaration session with father full name", "ලෝගේස්වරන් යුවන් ශන්කර්", bd.getParent().getFatherFullName());
        assertEquals("failed to update birth declaration session with father DOB", "1964-08-09",
            DateTimeUtils.getISO8601FormattedString(bd.getParent().getFatherDOB()));
        assertEquals("failed to update birth declaration session with father place of birth", "Kandy".toUpperCase(), bd.getParent().getFatherPlaceOfBirth());
        assertEquals("failed to update birth declaration session with father Race", 1, bd.getParent().getFatherRace().getRaceId());

        assertEquals("failed to update birth declaration session with mother NIC/PIN", "685031035V", bd.getParent().getMotherNICorPIN());
        assertEquals("failed to update birth declaration session with mother country", 2, bd.getParent().getMotherCountry().getCountryId());
        assertEquals("failed to update birth declaration session with mother passport number", "5999", bd.getParent().getMotherPassportNo());
        assertEquals("failed to update birth declaration session with mother full Name", "සංගුණි ෙද්ව ෙග්", bd.getParent().getMotherFullName());
        assertEquals("failed to update birth declaration session with mother mother DOB", "1968-08-09", DateTimeUtils.getISO8601FormattedString(bd.getParent().getMotherDOB()));
        assertEquals("failed to update birth declaration session with mother age at birth", 43, bd.getParent().getMotherAgeAtBirth().intValue());
        assertEquals("failed to update birth declaration session with mother address", "65 C මල්වත්ත පාර, කොට්ටාව", bd.getParent().getMotherAddress());
        assertEquals("failed to update birth declaration session with mother race id", 1, bd.getParent().getMotherRace().getRaceId());
        assertEquals("failed to update birth declaration session with mother place of birth", "KANDANA", bd.getParent().getMotherPlaceOfBirth());
        assertEquals("failed to update birth declaration session with mother admission number", "125", bd.getParent().getMotherAdmissionNo());
        assertEquals("failed to update birth declaration session with mother email", "info@gmail.com", bd.getParent().getMotherEmail());
        assertEquals("failed to update birth declaration session with mother admission date", "2010-01-09", DateTimeUtils.getISO8601FormattedString(bd.getParent().getMotherAdmissionDate()));
        assertEquals("failed to update birth declaration session with mother phone number", "0112345678", bd.getParent().getMotherPhoneNo());

        assertNotNull("marriage Bean population failed", action.getMarriage());
        assertNotNull("grandFather Bean population failed", action.getGrandFather());
        assertNotNull("informant Bean population failed", action.getInformant());

        //for 4 of 4BDF
        request.setParameter("pageNo", "3");
        request.setParameter("marriage.parentsMarried", MarriageInfo.MarriedStatus.MARRIED.toString());
        request.setParameter("marriage.placeOfMarriage", "Kaduwela");
        request.setParameter("marriage.dateOfMarriage", "2008-08-09");

        request.setParameter("informant.informantType", "MOTHER");

        request.setParameter("informant.informantNICorPIN", "685031035V");
        request.setParameter("informant.informantName", "සංගුණි ෙද්ව ෙග්");
        request.setParameter("informant.informantAddress", "Kandy Road Matale");
        request.setParameter("informant.informantPhoneNo", "081234567");
        request.setParameter("informant.informantEmail", "info@gmail.com");
        request.setParameter("informant.informantSignDate", "2010-08-09");

        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        assertEquals("Action errors for 4 of 4BDF", 0, action.getActionErrors().size());
        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);

        assertEquals("failed to update birth declaration session with parent married", 1, bd.getMarriage().getParentsMarried().ordinal());
        assertEquals("failed to update birth declaration session with place of marriage", "KADUWELA", bd.getMarriage().getPlaceOfMarriage());
        assertEquals("failed to update birth declaration session with date of marriage", "2008-08-09", DateTimeUtils.getISO8601FormattedString(bd.getMarriage().getDateOfMarriage()));
        assertEquals("failed to update birth declaration session with informant type", 1, bd.getInformant().getInformantType().ordinal());
        assertEquals("failed to update birth declaration session with informant NIC/PIN", "685031035V", bd.getInformant().getInformantNICorPIN());
        assertEquals("failed to update birth declaration session with informant name", "සංගුණි ෙද්ව ෙග්", bd.getInformant().getInformantName());
        assertEquals("failed to update birth declaration session with informant address", "KANDY ROAD MATALE", bd.getInformant().getInformantAddress());
        assertEquals("failed to update birth declaration session with informant phone number", "081234567", bd.getInformant().getInformantPhoneNo());
        assertEquals("failed to update birth declaration session with informant Email", "info@gmail.com", bd.getInformant().getInformantEmail());
        assertEquals("failed to update birth declaration session with informant signed date", "2010-08-09", DateTimeUtils.getISO8601FormattedString(bd.getInformant().getInformantSignDate()));

        //BirthDeclaration Form Details
        request.setParameter("pageNo", "4");
        request.setParameter("notifyingAuthority.notifyingAuthorityPIN", "685031035V");
        request.setParameter("notifyingAuthority.notifyingAuthorityName", "සංගුණි ෙද්ව ෙග්");
        request.setParameter("notifyingAuthority.notifyingAuthorityAddress", "65 C මල්වත්ත පාර, කොට්ටාව");
        request.setParameter("notifyingAuthority.notifyingAuthoritySignDate", "2010-08-09");
        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        assertEquals("Action errors for Birth Declaration Form Details", 0, action.getActionErrors().size());
        logger.debug("approval permission for the user : {}", action.isAllowApproveBDF());
        assertNotNull("notifyingAuthority Bean population failed", action.getNotifyingAuthority());
        assertEquals("Failed to remove BirthDeclaration", bd, session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));
        logger.debug("successfully persisted with the bdId :{}", action.getBdId());

    }

    public void testBirthDeclarationEditMode() throws Exception {
        Map session = login("rg", "password");
        initAndExecute("/births/eprBirthRegistrationInit.do", session);
        session = action.getSession();
        BirthDeclaration bdTemp = action.getService().getActiveRecordByBDDivisionAndSerialNo(action.getBDDivisionDAO().getBDDivisionByPK(1),
            new Long("2010012368"), (User) session.get(WebConstants.SESSION_USER_BEAN));
        Long idUKey = bdTemp.getIdUKey();
        request.setParameter("bdId", idUKey.toString());
        initAndExecute("/births/eprBirthRegistrationInit.do", session);
        session = action.getSession();
        assertEquals("Action errors for 1 of 4BDF", 0, action.getActionErrors().size());

        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertEquals("Data Entry Mode Failed", 0, bd.getRegister().getStatus().ordinal());

        Object obj = session.get(WebConstants.SESSION_USER_LANG);
        assertNotNull("Session User Local Presence", obj);
        assertNotNull("Request District List Presence", action.getDistrictList());
        assertNotNull("Request Race List Presence", action.getRaceList());
        assertEquals("Not a live birth", BirthDeclaration.BirthType.LIVE, action.getBirthType());

        //check whether required beans are populated
        assertEquals("Request child Bean is Populated", bd.getChild(), action.getChild());
        assertEquals("Request register Bean is Populated", bd.getRegister(), action.getRegister());

        assertEquals("Request birthDistrictId is set to existing district", action.getRegister().getBirthDistrict().getDistrictUKey(),
            action.getBirthDistrictId());
        assertEquals("Request birthDivisionId is set to existing birthDivision", action.getRegister().getBirthDivision().getBdDivisionUKey(),
            action.getBirthDivisionId());
        assertEquals("Request dsDivisionId is set to existing dsDivision", action.getRegister().getDsDivision().getDsDivisionUKey(),
            action.getDsDivisionId());
        //todo check again
        /*   assertEquals("Request father Country", action.getFatherCountry(), bd.getParent().getFatherCountry().getCountryId());
         assertEquals("Request father Race", action.getFatherRace(), bd.getParent().getFatherRace().getRaceId());
         assertEquals("Request Mother Country", action.getMotherCountry(), bd.getParent().getMotherCountry().getCountryId());
         assertEquals("Request Mother Race", action.getMotherRace(), bd.getParent().getMotherRace().getRaceId());
        */
        //for 2 of 4BDF
        request.setParameter("pageNo", "1");
        request.setParameter("register.bdfSerialNo", "12345");
        request.setParameter("register.dateOfRegistration", "2010-08-09");
        request.setParameter("child.dateOfBirth", "2010-01-09");
        request.setParameter("birthDistrictId", "1");
        request.setParameter("birthDivisionId", "10");
        request.setParameter("child.placeOfBirth", "මාතර");
        request.setParameter("child.placeOfBirthEnglish", "Matara");
        request.setParameter("child.birthAtHospital", "true");
        request.setParameter("child.childFullNameOfficialLang", bd.getChild().getChildFullNameOfficialLang());
        request.setParameter("child.childFullNameEnglish", bd.getChild().getChildFullNameEnglish());
        request.setParameter("register.preferredLanguage", "si");
        request.setParameter("child.childGender", "1");
        request.setParameter("child.childBirthWeight", "2");
        request.setParameter("child.childRank", "1");
        request.setParameter("child.numberOfChildrenBorn", "1");
        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        assertEquals("Action errors for 2 of 4BDF", 0, action.getActionErrors().size());

        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertEquals("bdId matches with the previous failed", idUKey.longValue(), bd.getIdUKey());
        obj = session.get(WebConstants.SESSION_USER_LANG);
        assertNotNull("Session User Local Presence Failed", obj);
        assertNotNull("Request Country List Presence Failed", action.getCountryList());
        assertNotNull("Request Race List Presence Failed", action.getRaceList());
        assertNotNull("Request Country List Presence Failed", action.getCountryList());

        assertNotNull("Request Full District List Presence Failed", action.getAllDistrictList());
        assertNotNull("Request Full DS Division List Presence Failed", action.getAllDSDivisionList());

        assertEquals("Request parent Bean population failed", bd.getParent(), action.getParent());

        //for 3 of 4BDF
        request.setParameter("pageNo", "2");
        //father's information
        request.setParameter("parent.fatherNICorPIN", "530232026V");
        request.setParameter("fatherCountry", "1");
        request.setParameter("parent.fatherPassportNo", "4832");
        request.setParameter("parent.fatherFullName", "ලෝගේස්වරන් යුවන් ශන්කර්");
        request.setParameter("parent.fatherDOB", "1964-08-09");
        request.setParameter("parent.fatherPlaceOfBirth", "Kandy");
        request.setParameter("fatherRace", "1");

        //mother's information
        request.setParameter("parent.motherNICorPIN", "685031035V");
        request.setParameter("motherCountry", "2");
        request.setParameter("parent.motherPassportNo", "5999");
        request.setParameter("parent.motherFullName", "සංගුණි ෙද්ව ෙග්");
        request.setParameter("parent.motherDOB", "08/09/1968");
        request.setParameter("parent.motherAgeAtBirth", "43");
        request.setParameter("parent.motherAddress", "65 C මල්වත්ත පාර, කොට්ටාව");
        request.setParameter("motherDistrictId", "1");
        request.setParameter("motherRace", "1");
        request.setParameter("parent.motherPlaceOfBirth", "kandana");
        request.setParameter("parent.motherAdmissionNo", "125");
        request.setParameter("parent.motherEmail", "info@gmail.com");
        request.setParameter("parent.motherAdmissionDate", "2010-01-09");
        request.setParameter("parent.motherPhoneNo", "0112345678");

        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        assertEquals("Action errors for 3 of 4BDF", 0, action.getActionErrors().size());

        assertEquals("Request marriage Bean population failed", bd.getMarriage(), action.getMarriage());
        assertEquals("Request grandFather Bean population failed", bd.getGrandFather(), action.getGrandFather());
        assertEquals("Request informant Bean population failed", bd.getInformant(), action.getInformant());

        //for 4 of 4BDF
        request.setParameter("pageNo", "3");
        request.setParameter("marriage.parentsMarried", MarriageInfo.MarriedStatus.MARRIED.toString());
        request.setParameter("marriage.placeOfMarriage", "Kaduwela");
        request.setParameter("marriage.dateOfMarriage", "2008-08-09");

        request.setParameter("informant.informantType", "MOTHER");

        request.setParameter("informant.informantNICorPIN", "685031035V");
        request.setParameter("informant.informantName", "සංගුණි ෙද්ව ෙග්");
        request.setParameter("informant.informantAddress", "Kandy Road Matale");
        request.setParameter("informant.informantPhoneNo", "081234567");
        request.setParameter("informant.informantEmail", "info@gmail.com");
        request.setParameter("informant.informantSignDate", "2010-08-09");

        initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        assertEquals("Action errors for 4 of 4BDF", 0, action.getActionErrors().size());
        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);

        assertEquals("failed to update birth declaration session with parent married", 1, bd.getMarriage().getParentsMarried().ordinal());
        assertEquals("failed to update birth declaration session with place of marriage", "KADUWELA", bd.getMarriage().getPlaceOfMarriage());
        assertEquals("failed to update birth declaration session with date of marriage", "2008-08-09", DateTimeUtils.getISO8601FormattedString(bd.getMarriage().getDateOfMarriage()));
        assertEquals("failed to update birth declaration session with informant type", 1, bd.getInformant().getInformantType().ordinal());
        assertEquals("failed to update birth declaration session with informant NIC/PIN", "685031035V", bd.getInformant().getInformantNICorPIN());
        assertEquals("failed to update birth declaration session with informant name", "සංගුණි ෙද්ව ෙග්", bd.getInformant().getInformantName());
        assertEquals("failed to update birth declaration session with informant address", "KANDY ROAD MATALE", bd.getInformant().getInformantAddress());
        assertEquals("failed to update birth declaration session with informant phone number", "081234567", bd.getInformant().getInformantPhoneNo());
        assertEquals("failed to update birth declaration session with informant Email", "info@gmail.com", bd.getInformant().getInformantEmail());
        assertEquals("failed to update birth declaration session with informant signed date", "2010-08-09", DateTimeUtils.getISO8601FormattedString(bd.getInformant().getInformantSignDate()));

        //BirthDeclaration Form Details
        request.setParameter("pageNo", "4");
        request.setParameter("notifyingAuthority.notifyingAuthorityPIN", "685031035V");
        request.setParameter("notifyingAuthority.notifyingAuthorityName", "සංගුණි ෙද්ව ෙග්");
        request.setParameter("notifyingAuthority.notifyingAuthorityAddress", "65 C මල්වත්ත පාර, කොට්ටාව");
        request.setParameter("notifyingAuthority.notifyingAuthoritySignDate", "2010-08-09");
        initAndExecute("/births/eprBirthRegistration.do", session);
        assertNotNull("notifyingAuthority Bean population failed", action.getNotifyingAuthority());
        session = action.getSession();
        assertEquals("Action errors for Birth Declaration Form Details", 0, action.getActionErrors().size());
        logger.debug("approval permission for the user : {}", action.isAllowApproveBDF());
        assertEquals("Failed to remove BirthDeclaration", null, session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN));
        logger.debug("successfully persisted with the bdId :{}", action.getBdId());

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

    /**
     * testing birth cetficate print
     *
     * @author amith jayasekara
     */
    //todo create a sample data that can be print

    /*public void testBirthCetificatePrint() throws Exception {
        //loggin as rg
        Map session = login("rg", "password");

        //setting buid to find BD for live biirth
        request.setParameter("bdId", "165");
        initAndExecute("/births/eprBirthCertificate.do",session);
        session = action.getSession();
        //assertEquals("No Action Errors", 0, action.getActionErrors());
        //check BDF data
        assertNotNull("Child object", action.getChild());
        assertNotNull("Parent object", action.getParent());
        assertNotNull("GrandFather object", action.getGrandFather());
        assertNotNull("Marrage object", action.getMarriage());
        assertNotNull("Informant object", action.getInformant());
        assertNotNull("Confirmant object", action.getConfirmant());
        assertNotNull("Register object", action.getRegister());
        assertNotNull("Notify Authority", action.getNotifyingAuthority());

        testing bdid =0
        request.setParameter("bdId", "0");
        initAndExecute("/births/eprBirthCertificate.do");

        assertNotNull("Action Errors", action.getActionErrors());
        assertNull("BirthDeclaration ", action.getChild());

    }*/
    public void testStillBirthDeclarationInit() throws Exception {
        Map session = login("duminda", "duminda");

        String result = initAndExecute("/births/eprStillBirthRegistrationInit.do", session);
        session = action.getSession();
        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertEquals("Not a still birth failed ", BirthDeclaration.BirthType.STILL, bd.getRegister().getBirthType());
        //assertEquals("form0", result);

        // Still birth page one
        request.setParameter("register.bdfSerialNo", "123");
        request.setParameter("register.dateOfRegistration", "2010-08-10");
        request.setParameter("child.dateOfBirth", "2010-07-10");
        request.setParameter("birthDistrictId", "1");
        request.setParameter("dsDivisionId", "2");
        request.setParameter("birthDivisionId", "6");
        request.setParameter("child.placeOfBirth", "Colombo Fort (Medical)");
        request.setParameter("child.placeOfBirthEnglish", "In Hospital");
        request.setParameter("child.birthAtHospital", "true");
        request.setParameter("child.childGender", "1");
        request.setParameter("child.weeksPregnant", "15");
        request.setParameter("child.childRank", "1");
        request.setParameter("child.numberOfChildrenBorn", "1");
        request.setParameter("pageNo", "1");
        request.setParameter("rowNumber", "8");
        request.setParameter("register.preferredLanguage", "si");


        result = initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertEquals("Not a still birth failed ", BirthDeclaration.BirthType.STILL, bd.getRegister().getBirthType());
        assertEquals("Register bdf serial no failed", (long) 123, bd.getRegister().getBdfSerialNo());
        assertEquals("Date of registration failed", "2010-08-10", DateTimeUtils.getISO8601FormattedString(bd.getRegister().getDateOfRegistration()));
        assertEquals("Date of birth failed", "2010-07-10", DateTimeUtils.getISO8601FormattedString(bd.getChild().getDateOfBirth()));
        assertEquals("Birth district Id failed", 1, bd.getRegister().getBirthDistrict().getDistrictUKey());
        assertEquals("DSDivision Id failed", 1, bd.getRegister().getBirthDivision().getDsDivision().getDsDivisionUKey());
        assertEquals("Child Birth Division failed", 6, bd.getRegister().getBirthDivision().getBdDivisionUKey());
        assertEquals("Child Place of birth failed", "Colombo Fort (Medical)".toUpperCase(), bd.getChild().getPlaceOfBirth());
        assertEquals("No action errors", 0, action.getActionErrors().size());

        // Still birth page two
        request.setParameter("parent.fatherNICorPIN", "11111111v");
        request.setParameter("parent.fatherFullName", "father full name");
        request.setParameter("parent.fatherDOB", "1965-07-14");
        request.setParameter("parent.fatherPlaceOfBirth", "father birth place");
        request.setParameter("fatherRace", "1");
        request.setParameter("parent.motherNICorPIN", "22222222v");
        request.setParameter("parent.motherFullName", "mother full name");
        request.setParameter("parent.motherDOB", "1970-08-09");
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
        assertEquals("Not a still birth failed ", BirthDeclaration.BirthType.STILL, bd.getRegister().getBirthType());
        assertEquals("Father NIC", "11111111V", bd.getParent().getFatherNICorPIN());
        assertNull("Father passport no:", bd.getParent().getFatherPassportNo());
        assertEquals("No action errors", 0, action.getActionErrors().size());

        // Still birth page three.
        request.setParameter("marriage.parentsMarried", MarriageInfo.MarriedStatus.MARRIED.toString());
        request.setParameter("informant.informantType", "GUARDIAN");
        request.setParameter("informant.informantNICorPIN", "33333333v");
        request.setParameter("informant.informantName", "informant name");
        request.setParameter("informant.informantAddress", "informant address");
        request.setParameter("informant.informantPhoneNo", "0123456789");
        request.setParameter("informant.informantEmail", "informant@email.mail");
        request.setParameter("informant.informantSignDate", "2010-08-09");
        request.setParameter("pageNo", "3");
        result = initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertEquals("Not a still birth failed ", BirthDeclaration.BirthType.STILL, bd.getRegister().getBirthType());
        assertEquals("Father NIC", "11111111V", bd.getParent().getFatherNICorPIN());
        assertNull("Father passport no:", bd.getParent().getFatherPassportNo());
        assertEquals("Informant NIC: ", "33333333V", bd.getInformant().getInformantNICorPIN());
        assertEquals("No action errors", 0, action.getActionErrors().size());

        // Still birth page four.
        request.setParameter("notifyingAuthority.notifyingAuthorityPIN", "44444444V");
        request.setParameter("notifyingAuthority.notifyingAuthorityName", "notifier name");
        request.setParameter("notifyingAuthority.notifyingAuthorityAddress", "notifier address");
        request.setParameter("notifyingAuthority.notifyingAuthoritySignDate", "2010-08-09");
        request.setParameter("pageNo", "4");
        result = initAndExecute("/births/eprBirthRegistration.do", session);
        session = action.getSession();
        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertEquals("Not a still birth failed ", BirthDeclaration.BirthType.STILL, bd.getRegister().getBirthType());
        assertEquals("Father NIC", "11111111V", bd.getParent().getFatherNICorPIN());
        assertNull("Father passport no:", bd.getParent().getFatherPassportNo());
        assertEquals("Informant NIC: ", "33333333V", bd.getInformant().getInformantNICorPIN());
        assertEquals("Notifier NIC: ", "44444444V", bd.getNotifyingAuthority().getNotifyingAuthorityPIN());
        assertEquals("Notifier sign date: ", "2010-08-09", DateTimeUtils.getISO8601FormattedString(bd.getNotifyingAuthority().getNotifyingAuthoritySignDate()));
        assertEquals("No action errors", 0, action.getActionErrors().size());

    }
}
