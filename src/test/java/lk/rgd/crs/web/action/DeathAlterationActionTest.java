package lk.rgd.crs.web.action;

import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.crs.web.action.deaths.DeathAlterationAction;
import lk.rgd.crs.web.action.deaths.DeathRegisterAction;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.service.DeathAlterationService;
import lk.rgd.UnitTestManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.extensions.TestSetup;

import java.util.*;

/**
 * @author amith jayasekara test case for death alteration
 */
public class DeathAlterationActionTest extends CustomStrutsTestCase {

    private static final Logger logger = LoggerFactory.getLogger(DeathAlterationActionTest.class);
    private DeathAlterationAction deathAlterationAction;
    private ActionProxy proxy;
    private DeathRegister dr;

    protected static BDDivision colomboBDDivision;
    protected static BDDivision negamboBDDivision;
    protected static Country sriLanka;
    protected static Race sinhalese;

    protected final static ApplicationContext ctx = UnitTestManager.ctx;
    protected final static UserManager userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);
    protected final static BDDivisionDAO bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
    protected final static CountryDAO countryDAO = (CountryDAO) ctx.getBean("countryDAOImpl", CountryDAO.class);
    protected final static RaceDAO raceDOA = (RaceDAO) ctx.getBean("raceDAOImpl", RaceDAO.class);
    protected final static DeathAlterationService deathAlterationService = (DeathAlterationService) ctx.getBean("deathAlterationService", DeathAlterationService.class);

    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(DeathAlterationActionTest.class)) {
            protected void setUp() throws Exception {
                logger.info("setup called");
                colomboBDDivision = bdDivisionDAO.getBDDivisionByPK(1);
                negamboBDDivision = bdDivisionDAO.getBDDivisionByPK(9);
                sriLanka = countryDAO.getCountry(1);
                sinhalese = raceDOA.getRace(1);

                List deaths = sampleDeaths();
                User sampleUser = loginSampleUser();
                for (int i = 0; i < deaths.size(); i++) {
                    deathAlterationService.addDeathAlteration((DeathAlteration) deaths.get(i), sampleUser);
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

    private static List sampleDeaths() {
        List list = new LinkedList();

        for (int i = 0; i < 6; i++) {
            // get Calendar with current date
            java.util.GregorianCalendar gCal = new GregorianCalendar();

            //setting act
            DeathAlteration deathAlteration = new DeathAlteration();
            deathAlteration.setAct(DeathAlteration.Act.ACT_52_1_a);
            deathAlteration.setStatus(DeathAlteration.State.DATA_ENTRY);
            deathAlteration.setDateReceived(gCal.getTime());
            deathAlteration.setDeathId(i);
            deathAlteration.setBcOfFather(true);

            //Death alteration info
            DeathAlterationInfo deathAlterationInfo = new DeathAlterationInfo();
            deathAlterationInfo.setPlaceOfDeath("place of death : " + i);
            deathAlterationInfo.setDateOfDeath(gCal.getTime());
            deathAlterationInfo.setTimeOfDeath("12:30");
            deathAlterationInfo.setPlaceOfDeathInEnglish("place of death in English :" + i);
            deathAlterationInfo.setCauseOfDeathEstablished(true);
            deathAlterationInfo.setCauseOfDeath("cause of death :" + i);
            deathAlterationInfo.setIcdCodeOfCause("ICD code of death :" + i);
            deathAlterationInfo.setPlaceOfBurial("place of burial : " + i);


            //death person info
            DeathPersonInfo person = new DeathPersonInfo();
            person.setDeathPersonGender(0);
            person.setDeathPersonNameOfficialLang("name in offocial lang" + i);
            person.setDeathPersonAge(25);
            person.setDeathPersonCountry(sriLanka);
            person.setDeathPersonFatherFullName("father full name " + i);
            person.setDeathPersonFatherPINorNIC("1234567890");
            person.setDeathPersonMotherFullName("mother full name " + i);
            person.setDeathPersonMotherPINorNIC("1234567890");
            person.setDeathPersonNameInEnglish("name in english" + i);
            person.setDeathPersonPermanentAddress("address" + i);

            //declarant info
            DeclarantInfo declarant = new DeclarantInfo();
            declarant.setDeclarantType(DeclarantInfo.DeclarantType.RELATIVE);
            declarant.setDeclarantAddress("declarant address " + i);
            declarant.setDeclarantEMail("declarant email" + i);
            declarant.setDeclarantFullName("declarant full name " + i);
            declarant.setDeclarantNICorPIN("" + (123456789 + i));

            CRSLifeCycleInfo lifeCycleInfo = new CRSLifeCycleInfo();
            lifeCycleInfo.setCreatedUser(loginSampleUser());
            lifeCycleInfo.setCertificateGeneratedTimestamp(new Date());

            deathAlteration.setDeathPerson(person);
            deathAlteration.setDeathInfo(deathAlterationInfo);
            deathAlteration.setDeclarant(declarant);
            deathAlteration.setLifeCycleInfo(lifeCycleInfo);

            list.add(deathAlteration);
        }
        return list;
    }

    private String initAndExecute(String mapping, Map session) throws Exception {
        proxy = getActionProxy(mapping);
        deathAlterationAction = (DeathAlterationAction) proxy.getAction();
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

    public void testCaptureDeathAlteration() throws Exception {
        Object obj;
        Map session = userLogin("rg", "password");
        initAndExecute("/alteration/eprCaptureDeathAlteration.do", session);
        session = deathAlterationAction.getSession();
        assertNotNull("session set", session);
    }

    private Map userLogin(String username, String passwd) throws Exception {
        request.setParameter("userName", username);
        request.setParameter("password", passwd);
        ActionProxy proxy = getActionProxy("/eprLogin.do");
        LoginAction loginAction = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        proxy.execute();
        return loginAction.getSession();
    }
}
