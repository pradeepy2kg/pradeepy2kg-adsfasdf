package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionProxy;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;
import lk.rgd.UnitTestManager;
import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.action.births.BirthRegisterAction;
import lk.rgd.crs.web.action.births.BirthRegisterApprovalAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.*;


/**
 * @author Indunil Moremada
 * @authar amith jayasekara
 * unit test for testing the birth confirmation side flows
 */
public class BirthConfirmationSideFlowTest extends CustomStrutsTestCase {

    private static final Logger logger = LoggerFactory.getLogger(BirthConfirmationSideFlowTest.class);
    private ActionProxy proxy;
    private BirthRegisterAction action;
    private LoginAction loginAction;
    private BirthRegisterApprovalAction approvalAction;
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
        TestSetup setup = new TestSetup(new TestSuite(BirthConfirmationSideFlowTest.class)) {
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
            register.setBdfSerialNo(new Long(2010012330 + i));
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

    private String initAndExecuteApproval(String mapping, Map session) throws Exception {
        proxy = getActionProxy(mapping);
        approvalAction = (BirthRegisterApprovalAction) proxy.getAction();
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
    protected String getContextLocations() {
        return "unitTest_applicationContext.xml";
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

    // confirmation chages captured entries can not confirm by skipping confirmation changes again 
    /*public void testSkipConfirmationChangesForConfirmationChangesCapturedEntry() throws Exception {
        //idUKey 1 has serial number 2010012330
        User user = loginSampleUser();
        long idUKey = 1;
        Object obj;
        Map session = login("rg", "password");
        //initiating action to get the required bdId to start the unit test
        initAndExecute("/births/eprBirthConfirmationInit.do", session);
        //getting the required bdId which is having confirmation changes

        //change state to CONFIRMATION_CHANGES_CAPTURED state
        BirthDeclaration bdTemp = action.getService().getActiveRecordByBDDivisionAndSerialNo(action.getBDDivisionDAO().getBDDivisionByPK(1),
                new Long("2010012330"), (User) session.get(WebConstants.SESSION_USER_BEAN));
        //change state to APPROVE
        birthRegistrationService.approveLiveBirthDeclaration(bdTemp.getIdUKey(), true, user);
        bdTemp = birthRegistrationService.getById(idUKey, user);
        birthRegistrationService.markLiveBirthConfirmationAsPrinted(bdTemp, user);
        bdTemp = birthRegistrationService.getById(idUKey, user);
        birthRegistrationService.captureLiveBirthConfirmationChanges(bdTemp, user);
        //searching the required entry
        Long bdId = bdTemp.getIdUKey();
        logger.debug("Got bdId {}", bdId);
        request.setParameter("bdId", bdId.toString());
        initAndExecute("/births/eprBirthConfirmationInit.do", session);
        session = action.getSession();
        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);

        assertEquals("Action errors Confirmation Search", 0, action.getActionErrors().size());

        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);
        assertNotNull("failed to populate Confirmation session bean", bd);
        assertNotNull("failed to populate Confirmation Database bean",
                session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_DB_BEAN));

        obj = session.get(WebConstants.SESSION_USER_LANG);
        assertNotNull("Session User Local Presence", obj);
        assertNotNull("Request District List Presence", action.getDistrictList());
        assertNotNull("Request Race List Presence", action.getRaceList());

        //skipping changes
        request.setParameter("bdId", bdId.toString());
        request.setParameter("pageNo", "2");
        request.setParameter("skipConfirmationChages", "true");
        initAndExecute("/births/eprBirthConfirmationSkipChanges.do", session);
        session = action.getSession();
        assertEquals("Action errors Confirmation skipping changes", 0, action.getActionErrors().size());

        assertNotNull("failed to initialize confirmant bean", action.getConfirmant());

        request.setParameter("pageNo", "3");
        request.setParameter("skipConfirmationChages", "true");
        request.setParameter("confirmant.confirmantSignDate", "2010-07-20T00:00:00+05:30");
        request.setParameter("confirmant.confirmantNICorPIN", "861481131V");
        request.setParameter("confirmant.confirmantFullName", "Ramya de silva");
        initAndExecute("/births/eprBirthConfirmation.do", session);
        session = action.getSession();
        assertEquals("Action errors Confirmation skipping changes", 0, action.getActionErrors().size());
        assertFalse("failed to set skipConfirmationChanges in confirmation changes captured state", action.isSkipConfirmationChages());

        //direct approval confirmation changes
        request.setParameter("bdId", Long.toString(action.getBdId()));
        request.setParameter("confirmationApprovalFlag", "true");
        initAndExecuteApproval("/births/eprConfrimationChangesDirectApproval.do", session);
        session = approvalAction.getSession();

        logger.debug("current state after direct approval of confirmation changes : {}", (approvalAction.getService().getById(bdId,
                (User) session.get(WebConstants.SESSION_USER_BEAN))).getRegister().getStatus());

        //direct birth certificate print after direct approval
        request.setParameter("bdId", Long.toString(approvalAction.getBdId()));
        request.setParameter("directPrintBirthCertificate", "true");
        initAndExecute("/births/eprBirthCertificatDirectPrint.do", session);
        session = action.getSession();
        logger.debug("current state after direct printing the BC : {}", (action.getService().getById(bdId,
                (User) session.get(WebConstants.SESSION_USER_BEAN))).getRegister().getStatus());
    }*/

    public void testCaptureConfirmationChanges() throws Exception {
        Long bdId;
        //idUkey 2 has serial number 2010012331          
        User user = loginSampleUser();
        long idUKey = 2;
        Map session = login("rg", "password");
        //initiating action to get the required bdId to start the unit test
        initAndExecute("/births/eprBirthConfirmationInit.do", session);
        //getting the required bdId which is having confirmation changes
        BirthDeclaration bdTemp = action.getService().getActiveRecordByBDDivisionAndSerialNo(action.getBDDivisionDAO().getBDDivisionByPK(1),
            new Long("2010012331"), (User) session.get(WebConstants.SESSION_USER_BEAN));
        logger.debug("found bdId : {} and current state : {}", bdTemp.getIdUKey(), bdTemp.getRegister().getStatus());
        //change state to APPROVE
        birthRegistrationService.approveLiveBirthDeclaration(bdTemp.getIdUKey(), true, user);
        bdTemp = birthRegistrationService.getById(idUKey, user);
        birthRegistrationService.markLiveBirthConfirmationAsPrinted(bdTemp, user);
        session = action.getSession();
        bdId = bdTemp.getIdUKey();

        //searches the confirmation which was sent by parent by its idUKey
        request.setParameter("bdId", bdId.toString());
        initAndExecute("/births/eprBirthConfirmationInit.do", session);

        assertEquals("Action errors Confirmation Search", 0, action.getActionErrors().size());
        session = action.getSession();
        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);
        assertEquals("Action errors Confirmation Search", 0, action.getActionErrors().size());
        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);
        assertNotNull("failed to populate Confirmation session bean", bd);
        assertNotNull("failed to populate Confirmation Database bean",
            session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_DB_BEAN));
        //loading the 2 of 3BCFs
        request.setParameter("pageNo", "1");
        request.setParameter("register.bdfSerialNo", "07000805");
        request.setParameter("register.dateOfRegistration", "2010-07-08T00:00:00+05:30");
        request.setParameter("child.dateOfBirth", "2010-07-01T00:00:00+05:30");
        request.setParameter("birthDistrictId", "1");
        request.setParameter("birthDivisionId", "10");
        request.setParameter("birthDivisionId", "1");
        request.setParameter("child.placeOfBirth", "මාතර");
        request.setParameter("child.placeOfBirthEnglish", "Matara");
        request.setParameter("child.placeOfBirth", "කොළඹ කොටුව");
        request.setParameter("child.placeOfBirthEnglish", "colombo port");
        request.setParameter("fatherRace", "3");
        request.setParameter("motherRace", "1");
        request.setParameter("marriage.parentsMarried", MarriageInfo.MarriedStatus.NO_SINCE_MARRIED.toString());
        request.setParameter("parent.fatherNICorPIN", "853303399v");
        request.setParameter("parent.motherNICorPIN", "666666666v");
        request.setParameter("child.childGender", "1");
        initAndExecute("/births/eprBirthConfirmation.do", session);

        assertEquals("Action errors for loading second page", 0, action.getActionErrors().size());
        session = action.getSession();

        assertNotNull("failed to initialize child bean", action.getChild());
        assertNotNull("failed to initialize parent bean", action.getParent());

        //loading the 3 of 3BCFs
        request.setParameter("pageNo", "2");
        request.setParameter("child.childFullNameOfficialLang", "නිශ්ශංක මුදියන්සේලාගේ ජනිත් විදර්ශන නිශ්ශංක");
        request.setParameter("child.childFullNameEnglish", "Nishshanka Mudiyanselage Janith Wiarshana Nishshanka");
        request.setParameter("parent.fatherFullName", "Nishshanka Mudiyanselage Chandrasena Nishshanka");
        request.setParameter("parent.motherFullName", "Periyapperuma Arachchilage Premawathi");
        initAndExecute("/births/eprBirthConfirmation.do", session);

        assertEquals("Action errors for loading third page", 0, action.getActionErrors().size());
        assertNotNull("failed to initialize confirmant bean", action.getConfirmant());
        session = action.getSession();

        //checking for back support
        request.setParameter("pageNo", "1");
        initAndExecute("/births/eprBirthConfirmation.do", session);
        assertEquals("Action errors for loading second page with back support", 0, action.getActionErrors().size());

        assertEquals("failed to populate child full name in official language", "නිශ්ශංක මුදියන්සේලාගේ ජනිත් විදර්ශන නිශ්ශංක", action.getChild().getChildFullNameOfficialLang());
        assertEquals("failed to populate childFullNameEnglish", "Nishshanka Mudiyanselage Janith Wiarshana Nishshanka".toUpperCase(), action.getChild().getChildFullNameEnglish());
        assertEquals("failed to populate Father full name", "Nishshanka Mudiyanselage Chandrasena Nishshanka".toUpperCase(), action.getParent().getFatherFullName());
        assertEquals("failed to populate Mother full name", "Periyapperuma Arachchilage Premawathi".toUpperCase(), action.getParent().getMotherFullName());

        session = action.getSession();
        //loading the 3 of 3BCFs
        request.setParameter("pageNo", "2");
        request.setParameter("child.childFullNameOfficialLang", "නිශ්ශංක මුදියන්සේලාගේ ජනිත් විදර්ශන නිශ්ශංක");
        request.setParameter("child.childFullNameEnglish", "Nishshanka Mudiyanselage Janith Wiarshana Nishshanka");
        request.setParameter("parent.fatherFullName", "Nishshanka Mudiyanselage Chandrasena Nishshanka");
        request.setParameter("parent.motherFullName", "Periyapperuma Arachchilage Premawathi");
        initAndExecute("/births/eprBirthConfirmation.do", session);

        assertEquals("Action errors for loading third page", 0, action.getActionErrors().size());
        assertNotNull("failed to initialize confirmant bean after back support", action.getConfirmant());
        session = action.getSession();

        //loading the confirmation form detail page
        request.setParameter("pageNo", "3");
        request.setParameter("confirmantRadio", "GUARDIAN");
        request.setParameter("confirmant.confirmantNICorPIN", "853303399v");
        request.setParameter("confirmant.confirmantFullName", "කැලුම් කොඩිප්පිලි");
        request.setParameter("confirmant.confirmantSignDate", "2010-07-28T00:00:00+05:30");
        request.setParameter("skipConfirmationChanges", "false");
        initAndExecute("/births/eprBirthConfirmation.do", session);
        session = action.getSession();
        bdId = action.getBdId();
        logger.debug("bdId for the new entry {}", bdId);
        logger.debug("current state after capturing confirmation changes : {}", (action.getService().getById(bdId,
            (User) session.get(WebConstants.SESSION_USER_BEAN))).getRegister().getStatus());

        //dirct approval of confirmation changes
        request.setParameter("bdId", Long.toString(bdId));
        request.setParameter("confirmationApprovalFlag", "true");
        initAndExecuteApproval("/births/eprConfrimationChangesDirectApproval.do", session);
        session = approvalAction.getSession();

        logger.debug("current state after direct approval of confrimation chages : {}", (approvalAction.getService().getById(bdId,
            (User) session.get(WebConstants.SESSION_USER_BEAN))).getRegister().getStatus());

        //direct birth certificate print after direct approval
        request.setParameter("bdId", Long.toString(approvalAction.getBdId()));
        request.setParameter("directPrintBirthCertificate", "true");
        initAndExecute("/births/eprBirthCertificatDirectPrint.do", session);
        session = action.getSession();
        logger.debug("current state after direct printing the BC : {}", (action.getService().getById(bdId,
            (User) session.get(WebConstants.SESSION_USER_BEAN))).getRegister().getStatus());
    }

    public void testSkipConfirmationChangesForConfirmationPrintedEntry() throws Exception {
        //idUkey  has serial number 2010012332
        User user = loginSampleUser();
        long idUKey = 3;
        Object obj;
        Map session = login("rg", "password");
        //initiating action to get the required bdId to start the unit test
        initAndExecute("/births/eprBirthConfirmationInit.do", session);
        //getting the required bdId which is not having confirmation changes
        BirthDeclaration bdTemp = action.getService().getActiveRecordByBDDivisionAndSerialNo(action.getBDDivisionDAO().getBDDivisionByPK(1),
            new Long("2010012332"), (User) session.get(WebConstants.SESSION_USER_BEAN));
        //searching the required entry for which confirmation changes to be skipped

        //change state to APPROVE
        birthRegistrationService.approveLiveBirthDeclaration(bdTemp.getIdUKey(), true, user);
        bdTemp = birthRegistrationService.getById(idUKey, user);
        birthRegistrationService.markLiveBirthConfirmationAsPrinted(bdTemp, user);

        Long bdId = bdTemp.getIdUKey();
        logger.debug("Got confirmation printed Entry with bdId : {}", bdId);
        request.setParameter("bdId", bdId.toString());
        initAndExecute("/births/eprBirthConfirmationInit.do", session);
        session = action.getSession();
        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);

        assertEquals("Action errors Confirmation Search", 0, action.getActionErrors().size());

        bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN);
        assertNotNull("failed to populate Confirmation session bean", bd);
        assertNotNull("failed to populate Confirmation Database bean", session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_DB_BEAN));

        obj = session.get(WebConstants.SESSION_USER_LANG);
        assertNotNull("Session User Local Presence", obj);
        assertNotNull("Request District List Presence", action.getDistrictList());
        assertNotNull("Request Race List Presence", action.getRaceList());

        //skipping changes
        request.setParameter("bdId", bdId.toString());
        request.setParameter("pageNo", "2");
        request.setParameter("skipConfirmationChages", "true");
        initAndExecute("/births/eprBirthConfirmationSkipChanges.do", session);
        session = action.getSession();
        assertEquals("Action errors Confirmation skipping changes", 0, action.getActionErrors().size());

        assertNotNull("failed to initialize confirmant bean", action.getConfirmant());

        request.setParameter("pageNo", "3");
        request.setParameter("skipConfirmationChages", "true");
        request.setParameter("confirmant.confirmantSignDate", "2010-07-22T00:00:00+05:30");
        request.setParameter("confirmant.confirmantNICorPIN", "861481137V");
        request.setParameter("confirmant.confirmantFullName", "samara jayawardane");
        initAndExecute("/births/eprBirthConfirmation.do", session);
        session = action.getSession();
        assertEquals("Action errors Confirmation skipping changes", 0, action.getActionErrors().size());
        logger.debug("SkipConfirmationChanges : {} ", action.isSkipConfirmationChages());

        logger.debug("current state after skipping confirmation changes : {}", (action.getService().getById(bdId,
            (User) session.get(WebConstants.SESSION_USER_BEAN))).getRegister().getStatus());

        //direct birth certificate print after skipping confirmation changes
        request.setParameter("bdId", Long.toString(action.getBdId()));
        request.setParameter("directPrintBirthCertificate", "true");
        initAndExecute("/births/eprBirthCertificatDirectPrint.do", session);
        session = action.getSession();
        logger.debug("current state after direct printing the BC : {}", (action.getService().getById(bdId,
            (User) session.get(WebConstants.SESSION_USER_BEAN))).getRegister().getStatus());
    }
}
