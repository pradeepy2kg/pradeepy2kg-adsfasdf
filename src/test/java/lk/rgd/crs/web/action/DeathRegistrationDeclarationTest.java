package lk.rgd.crs.web.action;

import lk.rgd.crs.api.dao.GNDivisionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import lk.rgd.crs.web.action.deaths.DeathRegisterAction;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.service.DeathRegistrationService;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.UnitTestManager;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;

import java.util.*;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.extensions.TestSetup;


//todo test case for /next /previous/

public class DeathRegistrationDeclarationTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(DeathRegistrationDeclarationTest.class);
    private DeathRegisterAction deathAction;
    private ActionProxy proxy;
    private DeathRegister ddf;


    protected static BDDivision colomboBDDivision;
    protected static BDDivision negamboBDDivision;
    protected static GNDivision samantranapuraGNDivision;
    protected static Country sriLanka;
    protected static Race sinhalese;

    protected final static ApplicationContext ctx = UnitTestManager.ctx;
    protected final static UserManager userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);
    protected final static BirthRegistrationService birthRegistrationService = (BirthRegistrationService) ctx.getBean("manageBirthService", BirthRegistrationService.class);
    protected final static BDDivisionDAO bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
    protected final static CountryDAO countryDAO = (CountryDAO) ctx.getBean("countryDAOImpl", CountryDAO.class);
    protected final static RaceDAO raceDOA = (RaceDAO) ctx.getBean("raceDAOImpl", RaceDAO.class);
    protected final static DeathRegistrationService deathRegistrationService = (DeathRegistrationService) ctx.getBean("deathRegisterService", DeathRegistrationService.class);
    protected final static GNDivisionDAO gnDivisionDAO = (GNDivisionDAO) ctx.getBean("gnDivisionDAOImpl", GNDivisionDAO.class);

    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(DeathRegistrationDeclarationTest.class)) {
            protected void setUp() throws Exception {
                logger.info("setup called");
                colomboBDDivision = bdDivisionDAO.getBDDivisionByPK(1);
                negamboBDDivision = bdDivisionDAO.getBDDivisionByPK(9);
                sriLanka = countryDAO.getCountry(1);
                sinhalese = raceDOA.getRace(1);
                samantranapuraGNDivision = gnDivisionDAO.getGNDivisionByPK(1);

                List deaths = sampleDeaths();
                User sampleUser = loginSampleUser();
                for (int i = 0; i < deaths.size(); i++) {
                    deathRegistrationService.addNormalDeathRegistration((DeathRegister) deaths.get(i), sampleUser);
                }

                //setting serial number 2010012349 colombo to APPROVED
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

    private static List sampleDeaths() {
        List list = new LinkedList();

        for (int i = 0; i < 10; i++) {
            // get Calendar with current date
            java.util.GregorianCalendar gCal = new GregorianCalendar();
            //Death info
            DeathInfo death = new DeathInfo();
            death.setDeathSerialNo(2010091045 + i);
            death.setPlaceOfDeath("place of death :" + i);
            gCal.add(Calendar.DATE, -20);
            death.setDateOfDeath(gCal.getTime());
            gCal.add(Calendar.DATE, -2);
            death.setDateOfRegistration(gCal.getTime());
            death.setPreferredLanguage("si");
            death.setDeathDivision(colomboBDDivision);
            death.setGnDivision(samantranapuraGNDivision);
            death.setPlaceOfBurial("place of burial : " + i);

            //death person info
            DeathPersonInfo person = new DeathPersonInfo();
            person.setDeathPersonGender(0);
            person.setDeathPersonNameOfficialLang("name in official lang" + i);
            person.setDeathPersonAge(25);
            person.setDeathPersonCountry(sriLanka);
            person.setDeathPersonFatherFullName("father full name " + i);
            person.setDeathPersonFatherPINorNIC("1234567890");
            person.setDeathPersonMotherFullName("mother full name " + i);
            person.setDeathPersonMotherPINorNIC("1234567890");
            person.setDeathPersonNameInEnglish("name in english" + i);
            person.setDeathPersonPermanentAddress("address" + i);

            //notifying authority info
            NotifyingAuthorityInfo notify = new NotifyingAuthorityInfo();
            notify.setNotifyingAuthorityName("notify name :" + i);
            notify.setNotifyingAuthorityAddress("notifying address :" + i);
            notify.setNotifyingAuthorityPIN("" + 123456789 + i);
            gCal.add(Calendar.DATE, -1);
            notify.setNotifyingAuthoritySignDate(gCal.getTime());
            //declarant info
            DeclarantInfo declarant = new DeclarantInfo();
            declarant.setDeclarantType(DeclarantInfo.DeclarantType.MOTHER);
            declarant.setDeclarantAddress("declarant address " + i);
            declarant.setDeclarantEMail("declarant email" + i);
            declarant.setDeclarantFullName("declarant full name " + i);
            declarant.setDeclarantNICorPIN("" + (123456789 + i));

            DeathRegister deathRegister = new DeathRegister();
            deathRegister.setStatus(DeathRegister.State.DATA_ENTRY);
            deathRegister.setDeathType(DeathRegister.Type.NORMAL);
            deathRegister.setDeath(death);
            deathRegister.setDeathPerson(person);
            deathRegister.setDeclarant(declarant);
            deathRegister.setNotifyingAuthority(notify);

            list.add(deathRegister);
        }
        return list;
    }


    private String initAndExecute(String mapping, Map session) throws Exception {
        proxy = getActionProxy(mapping);
        deathAction = (DeathRegisterAction) proxy.getAction();
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


    public void testDeathRegister() throws Exception {
        Object obj;
        Map session = userLogin("duminda", "duminda");
        initAndExecute("/deaths/eprInitDeathDeclaration.do", session);
        session = deathAction.getSession();
        assertEquals("Action erros for 1 of 2DDF", 0, deathAction.getActionErrors().size());
        ddf = (DeathRegister) session.get(WebConstants.SESSION_DEATH_DECLARATION_BEAN);
        assertNotNull("Faild to put Death Declaration to the session", ddf);
        obj = session.get(WebConstants.SESSION_USER_LANG);
        assertNotNull("Session User Local Presence", obj);
        assertNotNull("Request District List Presence", deathAction.getDistrictList());
        assertNotNull("Request Race List Presence", deathAction.getRaceList());


        request.setParameter("pageNo", "1");
        request.setParameter("death.deathSerialNo", "120");
        request.setParameter("death.dateOfDeath", "2010-07-21");
        request.setParameter("death.deathDsDivisionId", "3");
        request.setParameter("death.placeOfDeath", "කොළඹ කොටුව");
        request.setParameter("death.placeOfDeathInEnglish", "colombo port");
        request.setParameter("death.causeOfDeathEstablished", "true");
        request.setParameter("death.infantLessThan30Days", "false");
        request.setParameter("death.causeOfDeath", "cancer");
        request.setParameter("death.placeOfBurial", "public ");
        request.setParameter("deathPerson.deathPersonPINorNIC", "707433191V");
        request.setParameter("deathPerson.deathPersonCountryId", "3");
        request.setParameter("deathPerson.deathPersonPassportNo", "1200");
        request.setParameter("deathPerson.deathPersonAge", "86");
        request.setParameter("deathPerson.deathPersonGender", "female");
        request.setParameter("deathPerson.deathPersonNameOfficialLang", "සෙල්ලදොරේයි කනවති අමිමා");
        request.setParameter("deathPerson.deathPersonNameInEnglish", "selverdorei kanawathi amma");
        request.setParameter("deathPerson.deathPersonPermanentAddress", "Erapalamulla Ruwanwella");
        request.setParameter("deathPerson.deathPersonFatherPINorNIC", "707433191V");
        request.setParameter("deathPerson.deathPersonFatherFullName", "selverdorei kanawathi amma");
        request.setParameter("deathPerson.deathPersonMotherPINorNIC", "707433191V");
        request.setParameter("deathPerson.deathPersonMotherFullNamed", "selverdorei kanawathi amma");

        initAndExecute("/deaths/eprDeathDeclaration.do", session);
        assertEquals("Failed to set the pageNo: ", 1, deathAction.getPageNo());
        assertEquals("Failed to set the deathSerialNo: ", (long) 120, deathAction.getDeath().getDeathSerialNo());

        session = deathAction.getSession();
        request.setParameter("pageNo", "2");

        request.setParameter("declarant.declarantFullName", "Tharanga Punchihewa");
        request.setParameter("declarant.declarantAddress", "Erapalamulla,Ruwanwella");
        request.setParameter("declarant.declarantPhone", "0718617804V");
        request.setParameter("declarant.declarantEMail", "htpunchihewa@gmail.com");
        request.setParameter("declarant.declarantType", "3");

        request.setParameter("notifyingAuthority.notifyingAuthorityPIN", "853303399v");
        request.setParameter("notifyingAuthority.notifyingAuthorityName", "Saman kUmara");
        request.setParameter("notifyingAuthority.notifyingAuthorityAddress", "කැලුම් කොඩිප්පිලි");
        request.setParameter("notifyingAuthority.notifyingAuthoritySignDate", "2010-07-28T00:00:00+05:30");

        initAndExecute("/deaths/eprDeathDeclaration.do", session);
        session = deathAction.getSession();


        assertEquals("Action erros for Death Declaration Form Details", 0, deathAction.getActionErrors().size());
        logger.debug("approval permission for the user : {}", deathAction.isAllowApproveDeath());
        assertNotNull("notifyingAuthority Bean population faild", deathAction.getNotifyingAuthority());
        assertEquals("Faild to remove DeathDeclaration", ddf, session.get(WebConstants.SESSION_DEATH_DECLARATION_BEAN));
        logger.debug("successfully persisted with the IdUKey :{}", deathAction.getIdUKey());


    }


    private Map userLogin(String username, String passwd) throws Exception {
        request.setParameter("javaScript", "true");
        request.setParameter("userName", username);
        request.setParameter("password", passwd);
        ActionProxy proxy = getActionProxy("/eprLogin.do");
        LoginAction loginAction = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        proxy.execute();
        return loginAction.getSession();
    }

    private void logOut() throws Exception {
        ActionProxy proxy = getActionProxy("/eprLogout.do");
        proxy.execute();
    }

    private void initAndExucute(String mapping, Map session) {
        proxy = getActionProxy(mapping);
        deathAction = (DeathRegisterAction) proxy.getAction();
        logger.debug("Action Method to be executed is {} ", proxy.getMethod());
        ActionContext.getContext().setSession(session);
        try {
            proxy.execute();
        } catch (Exception e) {
            logger.error("Handle Error {} : {}", e.getMessage(), e);
        }
    }

    //todo check paginations are working fine

    public void testDeathApprovalAndPrint() throws Exception {
        Map session = userLogin("rg", "password");
        initAndExucute("/deaths/eprDeathApprovalAndPrint.do", session);
        assertEquals("Num of row", 50, deathAction.getNoOfRows());
        populate();
        permissionToEditAndApprove();
        //check print list is not null (crash if null)
        assertNotNull("Print List is not null ", deathAction.getDeathApprovalAndPrintList());

    }

/*
    public void testDeleteDeath() throws Exception {
//to delete must be in DATA_ENTRY state
        //idukey 1 is in DATA_ENTRY
        Map session = userLogin("rg", "password");
        request.setParameter("idUKey", "1");
        initAndExucute("/deaths/eprDeleteDeath.do", session);
    }
*/


    public void testFilterByStatus() throws Exception {
        Map session = userLogin("rg", "password");
        //testing search by status
        //setting state to filtering
        request.setParameter("currentStatus", "2");
        //setting death division as 1(colombo fort)
        request.setParameter("deathDivisionId", "1");
        initAndExucute("/deaths/eprDeathFilterByStatus.do", session);

        //check state enum
        assertEquals("State APPROVED", DeathRegister.State.APPROVED, deathAction.getState());
        //check death division is properlly setted
        assertEquals("Death division", 1, deathAction.getDeathDivisionId());
        //check is this a search by state
        assertEquals("Search by date ", false, deathAction.isSearchByDate());
        //there is  1 sample data for approved in colmbo fort
        assertNotNull("Sample data not null", deathAction.getDeathApprovalAndPrintList());
        assertEquals("Sample data", 0, deathAction.getDeathApprovalAndPrintList().size());
        assertEquals("No Action Errors", 0, deathAction.getActionErrors().size());
        populate();
        permissionToEditAndApprove();
        logger.info("testing death filter by state completed");
    }

    public void testFilterByDate() throws Exception {
        Map session = userLogin("rg", "password");
        request.setParameter("deathDivisionId", "1");
        request.setParameter("fromDate", "2009-08-01");
        request.setParameter("endDate", "2011-09-31");
        initAndExucute("/deaths/eprDeathFilterByStatus.do", session);
        //check filter by date
        assertEquals("Search by date", true, deathAction.isSearchByDate());
        //check death divsion is setted properlly
        assertEquals("Death division", 1, deathAction.getDeathDivisionId());
        //there are 20 sample data for this senario
        assertNotNull("Sample data not null", deathAction.getDeathApprovalAndPrintList());
        //todo assertEquals("Sample data", 10, deathAction.getDeathApprovalAndPrintList().size());
        logger.info("testing death filter by date completed");
    }

    public void testDeathDeclarationEditMode() throws Exception {

        User sampleUser = loginSampleUser();
        DeathRegister death = null;
        //DATA_ENTRY mode
        death = deathRegistrationService.getByBDDivisionAndDeathSerialNo(colomboBDDivision, 2010091047, sampleUser);

        Map session = userLogin("rg", "password");
        //this is a DATA_ENTRY state recode
        request.setParameter("idUKey", "" + death.getIdUKey());
        initAndExucute("/deaths/eprDeathEditMode.do", session);
        assertEquals("No Action Errors", 0, deathAction.getActionErrors().size());
        //check user object is retrieved properly
        assertNotNull("User object ", deathAction.getUser());

        //try to edit non editable recode
        //this recode in APPROVE state
        death = deathRegistrationService.getByBDDivisionAndDeathSerialNo(colomboBDDivision, 2010091051, sampleUser);
        deathRegistrationService.approveDeathRegistration(death.getIdUKey(), sampleUser, true);
        death = deathRegistrationService.getByBDDivisionAndDeathSerialNo(colomboBDDivision, 2010091051, sampleUser);

        request.setParameter("idUKey", "" + death.getIdUKey());
        initAndExucute("/deaths/eprDeathEditMode.do", session);
        //check user object is retrieved properly
        assertNotNull("User object ", deathAction.getUser());
        //there should be an action error
        assertEquals("Action Error", 1, deathAction.getActionErrors().size());
        logger.info("testing death edit mode  completed");
    }

    public void testDeathReject() throws Exception {
        Map session = userLogin("rg", "password");
        User sampleUser = loginSampleUser();
        DeathRegister death = null;
        death = deathRegistrationService.getByBDDivisionAndDeathSerialNo(colomboBDDivision, 2010091049, sampleUser);
        approveRejectComman("/deaths/eprRejectDeath.do", session, death.getIdUKey());
        logger.info("testing death reject completed");
    }

    public void testDeathApprove() throws Exception {
        User sampleUser = loginSampleUser();
        DeathRegister death = null;
        death = deathRegistrationService.getByBDDivisionAndDeathSerialNo(colomboBDDivision, 2010091050, sampleUser);
        Map session = userLogin("rg", "password");
        approveRejectComman("/deaths/eprApproveDeath.do", session, death.getIdUKey());
        logger.info("testing death approve completed");
    }

    public void testDeathDelete() throws Exception {
        User sampleUser = loginSampleUser();
        DeathRegister death = null;
        death = deathRegistrationService.getByBDDivisionAndDeathSerialNo(colomboBDDivision, 2010091051, sampleUser);
        Map session = userLogin("rg", "password");
        approveRejectComman("/deaths/eprDeleteDeath.do", session, death.getIdUKey());
        logger.info("testing death delete completed");
    }

    public void testDeathVeiwMode() throws Exception {
        User sampleUser = loginSampleUser();
        DeathRegister death = null;
        death = deathRegistrationService.getByBDDivisionAndDeathSerialNo(colomboBDDivision, 2010091048, sampleUser);
        Map session = userLogin("rg", "password");
        request.setParameter("idUKey", "" + death.getIdUKey());
        initAndExucute("/deaths/eprDeathViewMode.do", session);
        assertEquals("No Action errors", 0, deathAction.getActionErrors().size());
        assertNotNull("Death Register", deathAction.getDeathType());

    }

    private void approveRejectComman(String action, Map session, long correctData) throws Exception {
        // this recode in DATA_ENTRY
        settingApproveAndReject(Long.toString(correctData));
        initAndExucute(action, session);
        //check request is populated
        compareApproveAndReject(correctData);
        assertEquals("No Action Errors", 0, deathAction.getActionErrors().size());
        if (deathAction.getWarnings() != null && deathAction.getWarnings().size() == 0) {
            populate();
            permissionToEditAndApprove();
        }
        //check process when warnings
    }

    private void settingApproveAndReject(String idUKey) {
        request.setParameter("idUKey", idUKey);
        request.setParameter("deathDivisionId", "1");
        request.setParameter("pageNo", "1");
        request.setParameter("comment", "this is comment");
    }

    private void compareApproveAndReject(long idUKey) {
        assertEquals("IDUKEY ", idUKey, deathAction.getIdUKey());
        assertEquals("deathDivisionId ", 1, deathAction.getDeathDivisionId());
        assertEquals("pageNo ", 1, deathAction.getPageNo());
    }

    private void populate() {

        //check user object is retrieved properly
        assertNotNull("User object ", deathAction.getUser());
        //check district list is not null for user
        assertNotNull("District list", deathAction.getDistrictList());
        //check country list is not null
        assertNotNull("Country List ", deathAction.getCountryList());
        //check bddivision list is not null for the user
        assertNotNull("BD Division List for User", deathAction.getBdDivisionList());
    }

    private void permissionToEditAndApprove() {
        //rg has permission to both edit and approve
        assertEquals("Edit permission", true, deathAction.isAllowEditDeath());
        assertEquals("Approve permission", true, deathAction.isAllowApproveDeath());

    }

    @Override
    public String getContextLocations() {
        return "unitTest_applicationContext.xml";
    }


    public DeathRegisterAction getDeathAction() {
        return deathAction;
    }

    public void setDeathAction(DeathRegisterAction deathAction) {
        this.deathAction = deathAction;
    }

    public ActionProxy getProxy() {
        return proxy;
    }

    public void setProxy(ActionProxy proxy) {
        this.proxy = proxy;
    }
}
