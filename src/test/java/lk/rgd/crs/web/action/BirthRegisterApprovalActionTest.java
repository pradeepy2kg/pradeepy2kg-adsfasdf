package lk.rgd.crs.web.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.springframework.context.ApplicationContext;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;

import java.util.*;

import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.crs.web.action.births.BirthRegisterApprovalAction;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.UnitTestManager;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.extensions.TestSetup;

/**
 * @author amith jayasekara
 */
public class BirthRegisterApprovalActionTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterApprovalActionTest.class);
    private ActionProxy proxy;
    private BirthRegisterApprovalAction action;
    private LoginAction loginAction;

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
        TestSetup setup = new TestSetup(new TestSuite(BirthRegisterApprovalActionTest.class)) {
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
            register.setBdfSerialNo(new Long(2010012340 + i));
            register.setPreferredLanguage("si");
            //birth division
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
        action = (BirthRegisterApprovalAction) proxy.getAction();
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
        ActionMapping mapping = getActionMapping("/births/eprBirthRegisterApproval.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/births", mapping.getNamespace());
        assertEquals("eprBirthRegisterApproval", mapping.getName());
        ActionProxy proxy = getActionProxy("/births/eprBirthRegisterApproval.do");
        assertNotNull(proxy);
        logger.debug("nameSpace {} and actionName {}", mapping.getNamespace(), proxy.getMethod());

        BirthRegisterApprovalAction action = (BirthRegisterApprovalAction) proxy.getAction();
        assertNotNull(action);
    }

    /**
     * approve a birth declaration
     *
     * @throws Exception
     */

    public void testApprove() throws Exception {
        //to approve must be in DATA_ENTRY mode
        BirthDeclaration bd = birthRegistrationService.getActiveRecordByBDDivisionAndSerialNo(colomboBDDivision, 2010012340, loginSampleUser());
        Map session = login("rg", "password");
        request.setParameter("bdId", "" + bd.getIdUKey());
        initAndExecute("/births/eprApproveBirthDeclaration.do", session);
        assertEquals("No Action errors", 0, action.getActionErrors().size());
        assertNotNull("BDF object ", action.getBdf());
        assertNotNull("User object", session.get(WebConstants.SESSION_USER_BEAN));
        //recode 168 is live birth
        assertEquals("Live birth", BirthDeclaration.BirthType.LIVE, action.getBirthType());

        //   assertEquals("Number of warnings ", 3, action.getWarnings().size());
        //todo improve approval with out warning
        //todo try to approve with a user don't have permission

        logger.info("testApprove completed");
    }

    public void testApproveIgnoringWarning() throws Exception {
        //to approve with out waring must be in DATA_ENTRY ,mode bdId 2 is in DATA_ENTRY mode
        Map session = login("rg", "password");
        request.setParameter("bdId", "2");
        request.setParameter("ignoreWarning", "true");
        request.setParameter("confirmationApprovalFlag", "false");
        initAndExecute("/births/eprIgnoreWarning.do", session);
        assertNotNull(" Action errors", action.getActionErrors().size());
        commonApproval(session);
    }

    public void testApproveIgnoreWarningsDirect() throws Exception {
        //cannot approve it already approved
        //to approve with out waring must be in DATA_ENTRY ,mode bdId 3 is in DATA_ENTRY mode
        Map session = login("rg", "password");
        request.setParameter("bdId", "3");
        request.setParameter("ignoreWarning", "true");
        request.setParameter("confirmationApprovalFlag", "false");
        request.setParameter("directApprovalFlag", "true");
        initAndExecute("/births/eprIgnoreWarning.do", session);
        commonApproval(session);
        assertEquals("Request direct approval", true, action.isDirectApprovalFlag());
    }

    public void testReject() throws Exception {
        //set bd id 4 to State APPROVEd
        User user = loginSampleUser();

        BirthDeclaration bdf = birthRegistrationService.getActiveRecordByBDDivisionAndSerialNo(colomboBDDivision, 2010012348, user);
        birthRegistrationService.approveLiveBirthDeclaration(bdf.getIdUKey(), true, user);
        //now bdId 4 approved so cannot reject this recode so there must be action errors
        Map session = login("rg", "password");
        request.setParameter("bdId", "" + bdf.getIdUKey());
        request.setParameter("comments", "test reject comment");
        initAndExecute("/births/eprRejectBirthDeclaration.do", session);
        assertNotNull("Action errors", action.getActionErrors().size());
        assertNotNull("BDF object ", action.getBdf());
        assertNotNull("User object", session.get(WebConstants.SESSION_USER_BEAN));
        assertNotNull("Request comment", action.getComments());
    }

    public void testDelete() throws Exception {
        //set bd id to State APPROVEd
        User user = loginSampleUser();
        BirthDeclaration bdf = birthRegistrationService.getActiveRecordByBDDivisionAndSerialNo(colomboBDDivision, 2010012349, user);
        birthRegistrationService.approveLiveBirthDeclaration(bdf.getIdUKey(), true, user);
        //now bdId 5 approved so cannot delete this recode so there must be action errors
        Map session = login("rg", "password");
        request.setParameter("bdId", "" + bdf.getIdUKey());
        initAndExecute("/births/eprDeleteApprovalPending.do", session);
        assertNotNull("Action errors", action.getActionErrors().size());
        assertNotNull("BDF object ", action.getBdf());
        assertNotNull("User object", session.get(WebConstants.SESSION_USER_BEAN));
    }

    //todo implement after removing bug of serial numberr duplication


    public void testFilter() throws Exception {
        //filtering by serial number
        //todo filter by date
        Map session = login("rg", "password");
        request.setParameter("confirmationApprovalFlag", "false");
        request.setParameter("bdfSerialNo", "16005");
        request.setParameter("birthDivisionId", "10");
        request.setParameter("birthDistrictId", "2");
        initAndExecute("/births/eprApprovalRefresh.do", session);
        assertEquals("No Action Errors", 0, action.getActionErrors().size());
        populateList(session);

    }

    public void testApproveListOfEntries() throws Exception {
        User user = loginSampleUser();
        Map session = login("rg", "password");
        String index1 = "" + birthRegistrationService.getActiveRecordByBDDivisionAndSerialNo(colomboBDDivision, 2010012344, user).getIdUKey();
        String index2 = "" + birthRegistrationService.getActiveRecordByBDDivisionAndSerialNo(colomboBDDivision, 2010012345, user).getIdUKey();
        String index3 = "" + birthRegistrationService.getActiveRecordByBDDivisionAndSerialNo(colomboBDDivision, 2010012346, user).getIdUKey();
        request.setParameter("index", new String[]{index1, index2, index3});
        initAndExecute("/births/eprApproveBulk.do", session);
        assertEquals("No Action errors ", 0, action.getActionErrors().size());
        assertEquals("Request index", 3, action.getIndex().length);
    }

    private void populateList(Map session) {
        //check users preferred language
        Locale userLan = (Locale) session.get(WebConstants.SESSION_USER_LANG);
        assertNotNull("Session User Local Presence", userLan);
        //check user is not null
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        assertNotNull("Session User", user);
        //check initial district are loaded properly
        assertNotNull("Response User districtList", action.getDistrictList());
        //check initial dsDivision are loaded
        assertNotNull("Response User init DSDivision list", action.getDsDivisionList());
        //check initial bdDivision
        assertNotNull("Response User init BDDivision list", action.getBdDivisionList());
    }

    //todo implement a method to load table
    //todo implement a method to check permission


    private void commonApproval(Map session) {

        assertNotNull("BDF object ", action.getBdf());
        assertNotNull("User object", session.get(WebConstants.SESSION_USER_BEAN));
        assertEquals("Request confirmationFlag", false, action.isConfirmationApprovalFlag());
        assertEquals("Request ignoring warnings", true, action.isIgnoreWarning());
        //this is (167) a live birth
        assertEquals("Is live birth", BirthDeclaration.BirthType.LIVE, action.getBirthType());
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

}
