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
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.*;

/**
 * @author Janith Widarshana.
 */
public class BirthConfirmationTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(BirthConfirmationTest.class);
    private BirthRegisterAction registerAction;
    BirthRegisterApprovalAction approvalAction;
    private ActionProxy proxy;

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
        TestSetup setup = new TestSetup(new TestSuite(BirthConfirmationTest.class)) {
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
            register.setBdfSerialNo(new Long(2010012350 + i));
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

            ConfirmantInfo confermant = new ConfirmantInfo();

            bd.setChild(child);
            bd.setRegister(register);
            bd.setParent(parent);
            bd.setMarriage(marriage);
            bd.setGrandFather(granFather);
            bd.setNotifyingAuthority(notification);
            bd.setInformant(informant);
            bd.setConfirmant(confermant);

            list.add(bd);

        }
        return list;
    }

    private Map UserLogin(String username, String passwd) throws Exception {
        request.setParameter("javaScript","true");
        request.setParameter("userName", username);
        request.setParameter("password", passwd);
        ActionProxy proxy = getActionProxy("/eprLogin.do");
        LoginAction loginAction = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        proxy.execute();
        return loginAction.getSession();
    }

    private void initAndExecute(String mapping, Map session) {
        proxy = getActionProxy(mapping);
        registerAction = (BirthRegisterAction) proxy.getAction();
        logger.debug("Action Method to be executed is {} ", proxy.getMethod());
        ActionContext.getContext().setSession(session);
        try {
            proxy.execute();
        } catch (Exception e) {
            logger.error("Handle Error {} : {}", e.getMessage(), e);
        }
    }

    @Override
    public String getContextLocations() {
        return "unitTest_applicationContext.xml";
    }

    public void testBirthConfirmationInitMappingProxy() throws Exception {

        //idUKey 1 has serial number 2010012350
        User user = loginSampleUser();
        long idUKey = 1;

        BirthDeclaration bd = birthRegistrationService.getActiveRecordByBDDivisionAndSerialNo(colomboBDDivision, 2010012350, user);
        //change state to APPROVE
        idUKey = bd.getIdUKey();
        birthRegistrationService.approveLiveBirthDeclaration(bd.getIdUKey(), true, user);
        bd = birthRegistrationService.getById(idUKey, user);
        birthRegistrationService.markLiveBirthConfirmationAsPrinted(bd, user);
/*        bd = birthRegistrationService.getById(idUKey, user);
        birthRegistrationService.captureLiveBirthConfirmationChanges(bd, user);*/


        Map session = UserLogin("ashoka", "ashoka");
        ActionMapping mapping = getActionMapping("/births/eprBirthConfirmationInit.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/births", mapping.getNamespace());
        assertEquals("eprBirthConfirmationInit", mapping.getName());

        request.setParameter("bdId", "" + bd.getIdUKey());
        initAndExecute("/births/eprBirthConfirmationInit.do", session);
        assertEquals("No Action errors.", 0, registerAction.getActionErrors().size());
        session = registerAction.getSession();

        BirthDeclaration bdf, bcf;
        bdf = (BirthDeclaration) (session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN));
        bcf = (BirthDeclaration) (session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_DB_BEAN));
        //request.setAttribute("",bdf);
        assertNotNull("Session bdf presence", bdf);
        assertNotNull("Session bcf presence", bcf);
        assertEquals("confirmation changes has been captured", 2, bdf.getRegister().getStatus().ordinal());

        assertNotNull("dsDivisionList {} ", registerAction.getDsDivisionList());
        assertNotNull("dsDivisionList {} ", registerAction.getBdDivisionList());

        assertEquals("Found District", 11, bdf.getRegister().getBirthDistrict().getDistrictId());
        assertEquals("Found Division ", 1, bdf.getRegister().getBirthDivision().getDivisionId());
        logger.debug("getBd division {} ", bdf.getRegister().getBirthDivision().getSiDivisionName());

        assertEquals("Father Country", 1, bdf.getParent().getFatherCountry().getCountryId());

        assertNotNull("Mother country presence", registerAction.getMotherCountry());
        assertNotNull("Father Race precence", registerAction.getFatherRace());
        assertNotNull("Mother Race precence", registerAction.getMotherRace());
        assertNotNull("Mother Dsdivision Id presence ", registerAction.getMotherDSDivisionId());
        assertNotNull("Mother District Id presence ", registerAction.getMotherDistrictId());

        assertEquals("Serial no ", 2010012350, bdf.getRegister().getBdfSerialNo());

        assertNotNull("Child date of birth ", bdf.getChild().getDateOfBirth());
        assertEquals("Child place of birth ", "මාතර", bdf.getChild().getPlaceOfBirth());
        assertEquals("Child place of birth ", "MATARA", bdf.getChild().getPlaceOfBirthEnglish());

        session = registerAction.getSession();
        request.setParameter("pageNo", "1");
        request.setParameter("child.dateOfBirth", "2010-07-21T00:00:00+05:30");
        request.setParameter("birthDistrictId", "11");
        request.setParameter("dsDivisionId", "3");
        request.setParameter("birthDivisionId", "1");
        request.setParameter("child.placeOfBirth", "කොළඹ කොටුව");
        request.setParameter("child.placeOfBirthEnglish", "colombo port");
        request.setParameter("fatherRace", "3");
        request.setParameter("marriage.parentsMarried", MarriageInfo.MarriedStatus.NO_SINCE_MARRIED.toString());
        request.setParameter("parent.fatherNICorPIN", "853303399v");
        request.setParameter("parent.motherNICorPIN", "666666666v");
        request.setParameter("child.childGender", "1");
        initAndExecute("/births/eprBirthConfirmation.do", session);
        session = registerAction.getSession();
        bdf = (BirthDeclaration) (session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN));
        assertEquals("No Action errors.", 0, registerAction.getActionErrors().size());

        assertNotNull("child date of birth", registerAction.getChild().getDateOfBirth());
        assertNotNull("child gender ", registerAction.getChild().getChildGender());
        logger.debug("child date of birth : {}", registerAction.getChild().getDateOfBirth());

        assertNotNull("Session bdf presence", bdf);
        assertEquals("confirmation changes captured", 2, bdf.getRegister().getStatus().ordinal());

        request.setParameter("pageNo", "2");
        request.setParameter("child.childFullNameOfficialLang", "නිශ්ශංක මුදියන්සේලාගේ ජනිත් විදර්ශන නිශ්ශංක");
        request.setParameter("child.childFullNameEnglish", "Nishshanka Mudiyanselage Janith Wiarshana Nishshanka");
        request.setParameter("parent.fatherFullName", "Nishshanka Mudiyanselage Chandrasena Nishshanka");
        request.setParameter("parent.motherFullName", "Periyapperuma Arachchilage Premawathi");
        initAndExecute("/births/eprBirthConfirmation.do", session);
        session = registerAction.getSession();
        bdf = (BirthDeclaration) (session.get(WebConstants.SESSION_BIRTH_CONFIRMATION_BEAN));

        request.setParameter("pageNo", "3");
        assertEquals("No Action errors.", 0, registerAction.getActionErrors().size());
        request.setParameter("confirmantRadio", "GUARDIAN");
        request.setParameter("confirmant.confirmantNICorPIN", "853303399v");
        request.setParameter("confirmant.confirmantFullName", "කැලුම් කොඩිප්පිලි");
        request.setParameter("confirmant.confirmantSignDate", "2010-07-28T00:00:00+05:30");
        request.setParameter("skipConfirmationChanges", "false");
        initAndExecute("/births/eprBirthConfirmation.do", session);
        session = registerAction.getSession();
        assertNotNull("confirment full name ", registerAction.getConfirmant().getConfirmantFullName());

        request.setParameter("confirmationApprovalFlag", "true");
        request.setParameter("bdId", "165");
        request.setParameter("directApprovalFlag", "true");
        approvalInit("/births/eprConfrimationChangesDirectApproval.do", session);
        session = registerAction.getSession();

        assertNotNull("Ds division ", bdf.getRegister().getBirthDivision().getDsDivision().getDivisionId());
    }

    private void approvalInit(String mapping, Map session) {
        proxy = getActionProxy(mapping);
        approvalAction = (BirthRegisterApprovalAction) proxy.getAction();
        ActionContext.getContext().setSession(session);
        try {
            proxy.execute();
        } catch (Exception e) {
            logger.error("Handle Error {} : {}", e.getMessage(), e);
        }
    }
}