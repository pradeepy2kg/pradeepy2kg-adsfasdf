package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionProxy;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;
import lk.rgd.UnitTestManager;
import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.LocationDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.web.action.births.BirthAlterationAction;
import lk.rgd.crs.web.action.births.BirthRegisterAction;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.*;

/**
 * @author Indunil Moremada
 */
public class BirthAlterationTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(BirthAlterationTest.class);
    private ActionProxy proxy;
    private BirthAlterationAction birthAlterationAction;
    BirthRegisterAction registerAction;
    private LoginAction loginAction;
    protected static BDDivision colomboBDDivision;
    protected static User adrColomboColombo;
    protected static Location adrLocation;
    protected static Country sriLanka;
    protected static Race sinhalese;
    private BirthDeclaration bd;

    protected final static ApplicationContext ctx = UnitTestManager.ctx;
    protected final static UserManager userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);
    protected final static BirthRegistrationService birthRegistrationService = (BirthRegistrationService) ctx.getBean("manageBirthService", BirthRegistrationService.class);
    protected final static BDDivisionDAO bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
    protected final static CountryDAO countryDAO = (CountryDAO) ctx.getBean("countryDAOImpl", CountryDAO.class);
    protected final static RaceDAO raceDOA = (RaceDAO) ctx.getBean("raceDAOImpl", RaceDAO.class);

    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(BirthAlterationTest.class)) {
            protected void setUp() throws Exception {
                logger.info("setup called");
                colomboBDDivision = bdDivisionDAO.getBDDivisionByPK(1);
                sriLanka = countryDAO.getCountry(1);
                sinhalese = raceDOA.getRace(1);
                adrColomboColombo = userManager.authenticateUser("adr-colombo-colombo", "password");
                adrLocation = adrColomboColombo.getPrimaryLocation();

                List birth = sampleBirths();
                User sampleUser = loginSampleUser();
                for (int i = 0; i < birth.size(); i++) {
                    BirthDeclaration temp = (BirthDeclaration) birth.get(i);
                    birthRegistrationService.addLiveBirthDeclaration(temp, false, sampleUser);
                    long idUKey = temp.getIdUKey();
                    //change state to APPROVE
                    birthRegistrationService.approveLiveBirthDeclaration(idUKey, true, sampleUser);
                    temp = birthRegistrationService.getById(idUKey, sampleUser);
                    //change state to CONFIRMATION_PRINTED
                    birthRegistrationService.markLiveBirthConfirmationAsPrinted(temp, sampleUser);
                    temp = birthRegistrationService.getById(idUKey, sampleUser);
                    //change state to ARCHIVED_CERT_GENERATED
                    birthRegistrationService.markLiveBirthDeclarationAsConfirmedWithoutChanges(temp, sampleUser);
                    //change state to ARCHIVED_CERT_PRINTED
                    temp = birthRegistrationService.getById(idUKey, sampleUser);
                    birthRegistrationService.markLiveBirthCertificateAsPrinted(temp, sampleUser);
                    logger.debug("child idUKey is :", temp.getIdUKey());
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

    public void testActionMappingProxy() {
        ActionMapping mapping = getActionMapping("/alteration/eprBirthAlterationInit.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/alteration", mapping.getNamespace());
        assertEquals("eprBirthAlterationInit", mapping.getName());
        ActionProxy proxy = getActionProxy("/alteration/eprBirthAlterationInit.do");
        assertNotNull(proxy);
        logger.debug("nameSpace {} and actionName {}", mapping.getNamespace(), proxy.getMethod());

        BirthAlterationAction birthAlterationAction = (BirthAlterationAction) proxy.getAction();
        assertNotNull(birthAlterationAction);
    }


    private static User loginSampleUser() {
        User rg = null;
        try {
            rg = userManager.authenticateUser("rg", "password");
        } catch (AuthorizationException e) {
            logger.debug("exception when authorizing a user :'rg' ");
        }
        return rg;
    }

    private Map UserLogin(String username, String password) throws Exception {
        request.setParameter("javaScript", "true");
        request.setParameter("userName", username);
        request.setParameter("password", password);
        ActionProxy proxy = getActionProxy("/eprLogin.do");
        loginAction = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        proxy.execute();
        return loginAction.getSession();
    }

    private void initAndExecute(String mapping, Map session) {
        proxy = getActionProxy(mapping);
        birthAlterationAction = (BirthAlterationAction) proxy.getAction();
        logger.debug("Action Method to be executed is {} ", proxy.getMethod());
        ActionContext.getContext().setSession(session);
        try {
            proxy.execute();
        } catch (Exception e) {
            logger.error("Handle Error {} : {}", e.getMessage(), e);
        }
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
            child.setChildFullNameEnglish("RUWAN PERERA");
            child.setChildFullNameOfficialLang("ruwan perera");
            child.setChildRank(1 + i);
            child.setChildBirthWeight(new Float(1 + i));
            //todo warning
            child.setNumberOfChildrenBorn(0);

            //Birth Register info
            BirthRegisterInfo register = new BirthRegisterInfo();
            register.setPreferredLanguage("si");
            register.setBdfSerialNo(new Long(2010012400 + i));
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
            parent.setMotherAgeAtBirth(30 + i);
            parent.setFatherNICorPIN("861481131V");
            parent.setFatherFullName("Jagath Perera");

            //marriage info
            MarriageInfo marriage = new MarriageInfo();
            marriage.setParentsMarried(MarriageInfo.MarriedStatus.MARRIED);
            gCal.add(Calendar.YEAR, -1);
            marriage.setDateOfMarriage(gCal.getTime());
            marriage.setPlaceOfMarriage("Kaduwela");

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
            // birth certificate issue user and issu location added
            register.setOriginalBCIssueUser(adrColomboColombo);
            register.setOriginalBCPlaceOfIssue(adrLocation);

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

    public void testAddBirthAlterationByChangingFatherInfomationWithIdUKey() throws Exception {

        Map session = UserLogin("rg", "password");
        initAndExecute("/alteration/eprBirthAlterationInit.do", session);
        session = birthAlterationAction.getSession();
        BirthDeclaration bd = birthAlterationAction.getService().getActiveRecordByBDDivisionAndSerialNo(
            birthAlterationAction.getBDDivisionDAO().getBDDivisionByPK(1), new Long("2010012401"), (User) session.get(WebConstants.SESSION_USER_BEAN));
        //logger.debug("current state of the record : {} ", bd.getRegister().getStatus());
        request.setParameter("pageNo", "1");
        request.setParameter("sectionOfAct", "3");
        initAndExecute("/alteration/eprBirthAlterationSearch.do", session);
        session = birthAlterationAction.getSession();
        //assertEquals("Action errors after searching the fields to be altered  ", 0, birthAlterationAction.getActionErrors().size());

        //todo
        bd = birthAlterationAction.getService().getActiveRecordByBDDivisionAndSerialNo(
            birthAlterationAction.getBDDivisionDAO().getBDDivisionByPK(1), new Long("2010012401"), (User) session.get(WebConstants.SESSION_USER_BEAN));
        //set alt27A
        request.setParameter("birthAlteration.alt27.childFullNameOfficialLang", "RUWAN PERERA");    //todo get child name from bd
        request.setParameter("birthAlteration.alt27.childFullNameEnglish", "RUWAN PERERA");          //todo get child name from bd
        //setting required data
        request.setParameter("birthAlteration.dateReceived", "2010-09-21");
        request.setParameter("sectionOfAct", "3");

        //setting declarant infomation
        request.setParameter("birthAlteration.declarant.declarantType", "FATHER");
        request.setParameter("birthAlteration.declarant.declarantNICorPIN", "530232026V");
        request.setParameter("birthAlteration.declarant.declarantFullName", "Anuradha Silva");
        request.setParameter("birthAlteration.declarant.declarantAddress", "Galle rd, Colombo 4");

        //setting basic infomation

        request.setParameter("birthAlteration.alt27A.marriage.dateOfMarriage", "2009-09-21");

        //altering father infomation
        request.setParameter("birthAlteration.bdfIdUKey", "1");
        request.setParameter("birthAlteration.alt27A.father.fatherNICorPIN", "530232026V");
        request.setParameter("birthAlteration.alt27A.father.fatherFullName", "Anuradha Silva");
        request.setParameter("birthAlteration.alt27A.father.fatherPlaceOfBirth", "Colombo");

        initAndExecute("/alteration/eprBirthAlteration.do", session);
        session = birthAlterationAction.getSession();

        assertEquals("Action errors after altering father information", 0, birthAlterationAction.getActionErrors().size());
        assertEquals("RUWAN PERERA", birthAlterationAction.getBirthAlteration().getAlt27().getChildFullNameOfficialLang());
        assertEquals("RUWAN PERERA", birthAlterationAction.getBirthAlteration().getAlt27().getChildFullNameEnglish());
        assertEquals("Anuradha Silva", birthAlterationAction.getBirthAlteration().getAlt27A().getFather().getFatherFullName());
    }

    public void testBirthAlterationSearch() throws Exception {

        Map session = UserLogin("rg", "password");
        request.setParameter("pageType", "1");
        request.setParameter("idUKey", "1");
        request.setParameter("sectionOfAct", "2");
        initAndExecute("/alteration/eprBirthAlterationSearch.do", session);
        session = birthAlterationAction.getSession();
        assertNotNull("session set", session);
//        assertEquals("RUWAN PERERA", birthAlterationAction.getBirthAlteration().getAlt27().getChildFullNameOfficialLang());
        //     assertEquals("RUWAN PERERA", birthAlterationAction.getBirthAlteration().getAlt27().getChildFullNameEnglish());
        //check death register is populated
        //check basic list are populated if success

        //  otherLists(birthAlterationAction);
    }

    private void otherLists(BirthAlterationAction birthAlterationAction) {
        assertNotNull("Race List", birthAlterationAction.getRaceList());
        assertNotNull("Country List", birthAlterationAction.getCountryList());
    }

    @Override
    public String getContextLocations() {
        return "unitTest_applicationContext.xml";
    }
}
