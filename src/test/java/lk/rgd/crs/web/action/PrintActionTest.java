package lk.rgd.crs.web.action;

import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.crs.web.action.births.PrintAction;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.UnitTestManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.springframework.context.ApplicationContext;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;

import java.util.*;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.extensions.TestSetup;

/**
 * @author amith jayasekara
 */
public class PrintActionTest extends CustomStrutsTestCase {

    private static final Logger logger = LoggerFactory.getLogger(PrintActionTest.class);
    private ActionProxy proxy;
    private PrintAction action;
    private LoginAction loginAction;

    protected static BDDivision colomboBDDivision;
    protected static BDDivision negamboBDDivision;

    protected final static ApplicationContext ctx = UnitTestManager.ctx;
    protected final static UserManager userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);
    protected final static BirthRegistrationService birthRegistrationService = (BirthRegistrationService) ctx.getBean("manageBirthService", BirthRegistrationService.class);
    protected final static BDDivisionDAO bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);

    public static Test suite() {

        TestSetup setup = new TestSetup(new TestSuite(PrintActionTest.class)) {

            protected void setUp() throws Exception {
                logger.info("setup called");
                colomboBDDivision = bdDivisionDAO.getBDDivisionByPK(1);
                negamboBDDivision = bdDivisionDAO.getBDDivisionByPK(9);

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
            logger.debug("exception when autharizing a user :'rg' ");
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
            child.setPlaceOfBirth("Place of birth " + i);

            //Birth Register info
            BirthRegisterInfo register = new BirthRegisterInfo();
            register.setPreferredLanguage("si");
            register.setBdfSerialNo(new Long(2010012380 + i));
            //birth devision
            register.setBirthDivision(colomboBDDivision);
            register.setDateOfRegistration(gCal.getTime());
            register.setBirthType(BirthDeclaration.BirthType.LIVE);

            //parent info
            ParentInfo parent = new ParentInfo();

            //marrage info
            MarriageInfo marrage = new MarriageInfo();

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

            //confermant info
            ConfirmantInfo confermant = new ConfirmantInfo();

            bd.setChild(child);
            bd.setRegister(register);
            bd.setParent(parent);
            bd.setMarriage(marrage);
            bd.setGrandFather(granFather);
            bd.setNotifyingAuthority(notification);
            bd.setInformant(informant);
            bd.setConfirmant(confermant);

            list.add(bd);

        }
        return list;
    }

    private String initAndExecute(String mapping, Map session) throws Exception {
        proxy = getActionProxy(mapping);
        action = (PrintAction) proxy.getAction();
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
        ActionMapping mapping = getActionMapping("/births/eprBirthConfirmationBulkPrint.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/births", mapping.getNamespace());
        assertEquals("eprBirthConfirmationBulkPrint", mapping.getName());
        ActionProxy proxy = getActionProxy("/births/eprBirthCertificateList.do");
        assertNotNull(proxy);
        logger.debug("nameSpace {} and actionName {}", mapping.getNamespace(), proxy.getMethod());

        PrintAction action = (PrintAction) proxy.getAction();
        assertNotNull(action);
    }


    public void testLoadBirthCertificatePrintList() throws Exception {
        //loggin as a rg
        Map session = login("rg", "password");
        initAndExecute("/births/eprBirthCertificateList.do", session);
        //check no action errors
        assertEquals("No Action erros.", 0, action.getActionErrors().size());
        populateList(session);

        logger.info("testing LoadBirthCertificatePrintList completed");

    }

    public void testBirthConfirmationPrintList() throws Exception {
        //loggin as a rg
        Map session = login("rg", "password");
        initAndExecute("/births/eprBirthConfirmationPrintList.do", session);
        //check no action errors
        assertEquals("No Action erros.", 0, action.getActionErrors().size());
        populateList(session);

        logger.info("testing LoadBirthCertificatePrintList completed");

    }

    public void testFilterPrintList() throws Exception {
        //login and init as rg
        Map session = login("rg", "password");
        //setting data to execute method
        //set bdDivision id =1 (Colombo Fort (Medical))  and printed false
        request.setParameter("birthDivisionId", "1");
        request.setParameter("printed", "false");
        initAndExecute("/births/eprFilterBirthCetificateList.do", session);
        comman(session);
        //testing invalide data
        filterPrintListWithInvalideData(session);

        logger.info("testing FilterPrintList completed");
    }

/*    public void testPrintBulkOfEntries() throws Exception {
        Map session = login("rg", "password");
        System.out.println("aaaa");
        request.setParameter("printed", "false");
        request.setParameter("pageNo", "2");
        String[] index = new String[]{"1"};
        request.setParameter("index", index);
        request.setParameter("birthDivisionId", "1");
        initAndExecute("/births/eprBirthCertificateBulkPrint.do", session);
        comman(session);
        //check for invalide data

        logger.info("testing LoadBirthCertificatePrintList completed");
    }*/

    public void testNext() throws Exception {
        Map session = login("rg", "password");
        commanPreNext();
        initAndExecute("/births/eprPrintNext.do", session);
        commanPreNextCheck();
    }

    public void testPrevious() throws Exception {
        Map session = login("rg", "password");
        commanPreNext();
        initAndExecute("/births/eprPrintPrevious.do", session);
        commanPreNextCheck();
    }

    private void commanPreNext() {
        request.setParameter("pageNo", "2");
        request.setParameter("birthDivisionId", "1");
        request.setParameter("printed", "true");
        request.setParameter("printStart", "5");
    }

    private void commanPreNextCheck() {
        assertEquals("No Action Errors", 0, action.getActionErrors().size());
        assertNotNull("Request Page number", action.getPageNo());
        assertNotNull("Request BirthDivisionID", action.getBirthDivisionId());
        assertNotNull("Request Printed", action.isPrinted());
        assertNotNull("Request Print Start", action.getPrintStart());
    }

    private void comman(Map session) {
        //check no action errors
        assertEquals("No Action erros.", 0, action.getActionErrors().size());
        //test cases
        //check no action errors
        assertEquals("No Action erros.", 0, action.getActionErrors().size());
        //check user is set
        assertNotNull("User is not null ", action.getUser());
        assertEquals("Request birthDivisionId", 1, action.getBirthDivisionId());
        assertEquals("Request printed", false, action.isPrinted());

        //check list population
        populateList(session);
    }

    private void filterPrintListWithInvalideData(Map session) throws Exception {
        request.setParameter("birthDivisionId", "0");
        request.setParameter("printed", "false");
        initAndExecute("/births/eprFilterBirthCetificateList.do", session);
        //check is there any action errors
        assertNotNull("Action errors ", action.getActionErrors());
    }

    /**
     * this method test page loads after refresing or at begining
     */
    private void populateList(Map session) {
        //check users preferd language
        Locale userLan = (Locale) session.get(WebConstants.SESSION_USER_LANG);
        assertNotNull("Session User Local Presence", userLan);
        //check user is not null
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        assertNotNull("Session User", user);
        //check initial district are loaded properly
        assertNotNull("Response User districtList", action.getDistrictList());
        //check initial dsDivision are loaded
        assertNotNull("Response User init dsdivision list", action.getDsDivisionList());
        //check intial bdDivision
        assertNotNull("Response User init bddivision list", action.getBdDivisionList());
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
