package lk.rgd.crs.web.action;

import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.crs.web.action.deaths.DeathAlterationAction;
import lk.rgd.crs.web.action.deaths.DeathRegisterAction;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.service.DeathAlterationService;
import lk.rgd.crs.api.service.DeathRegistrationService;
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
    private DeathRegisterAction deathAction;
    private DeathRegister ddf;
    private ActionProxy proxy;

    protected static BDDivision colomboBDDivision;
    protected static BDDivision ambathalenPahala;
    protected static Country sriLanka;
    protected static Race sinhalese;

    protected final static ApplicationContext ctx = UnitTestManager.ctx;
    protected final static UserManager userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);
    protected final static BDDivisionDAO bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
    protected final static CountryDAO countryDAO = (CountryDAO) ctx.getBean("countryDAOImpl", CountryDAO.class);
    protected final static RaceDAO raceDOA = (RaceDAO) ctx.getBean("raceDAOImpl", RaceDAO.class);
    protected final static DeathAlterationService deathAlterationService = (DeathAlterationService) ctx.getBean("deathAlterationService", DeathAlterationService.class);
    protected final static DeathRegistrationService deathRegistrationService = (DeathRegistrationService) ctx.getBean("deathRegisterService", DeathRegistrationService.class);

    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(DeathAlterationActionTest.class)) {
            protected void setUp() throws Exception {

                logger.info("setup called");
                colomboBDDivision = bdDivisionDAO.getBDDivisionByPK(1);
                ambathalenPahala = bdDivisionDAO.getBDDivisionByPK(9);
                sriLanka = countryDAO.getCountry(1);
                sinhalese = raceDOA.getRace(1);

                List deathAlterations = sampleDeathAlterations();
                User sampleUser = loginSampleUser();
                List deaths = sampleDeaths();

                for (int i = 0; i < deaths.size(); i++) {
                    deathRegistrationService.addNormalDeathRegistration((DeathRegister) deaths.get(i), sampleUser);
                }

                //change state to certificate printed otherwise cannot add alterations
                DeathRegister dr;
                dr = deathRegistrationService.getByBDDivisionAndDeathSerialNo(ambathalenPahala, 2010012361, sampleUser);
                deathRegistrationService.approveDeathRegistration(dr.getIdUKey(), sampleUser, true);
                dr = deathRegistrationService.getByBDDivisionAndDeathSerialNo(ambathalenPahala, 2010012361, sampleUser);
                deathRegistrationService.markDeathCertificateAsPrinted(dr.getIdUKey(), sampleUser);

                for (int i = 0; i < deathAlterations.size(); i++) {
                    deathAlterationService.addDeathAlteration((DeathAlteration) deathAlterations.get(i), sampleUser);
                }
                super.setUp();
            }

            protected void tearDown() throws Exception {
                logger.info("tear down called ");
                /*User sampleUser = loginSampleUser();
                for (int i = 10; i < 20; i++) {
                    DeathRegister dr = deathRegistrationService.getByBDDivisionAndDeathSerialNo(ambathalenPahala, 2010012345 + i, sampleUser);
                    deathRegistrationService.deleteDeathRegistration(dr.getIdUKey(), sampleUser);
                    logger.debug("death recode : serial number {} : deleted", 2010012345 + i);
                }*/
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

    private static List sampleDeathAlterations() {
        List list = new LinkedList();
        User user = loginSampleUser();
        Location location = user.getPrimaryLocation();
        for (int i = 0; i < 6; i++) {
            // get Calendar with current date
            java.util.GregorianCalendar gCal = new GregorianCalendar();

            //setting act
            DeathAlteration deathAlteration = new DeathAlteration();
            deathAlteration.setType(DeathAlteration.AlterationType.TYPE_52_1_A);
            deathAlteration.setStatus(DeathAlteration.State.DATA_ENTRY);
            deathAlteration.setDateReceived(gCal.getTime());
            deathAlteration.setDeathRegisterIDUkey(i);
            deathAlteration.setBcOfFather(true);
            deathAlteration.setDeathRecordDivision(colomboBDDivision);
            deathAlteration.setSubmittedLocation(location);

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
            person.setDeathPersonNameOfficialLang("name in official lang" + i);
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
            declarant.setDeclarantAddress("Declarant  address " + i);
            declarant.setDeclarantEMail("Declarant email" + i);
            declarant.setDeclarantFullName("Declarant full name " + i);
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

    private static List sampleDeaths() {
        List list = new LinkedList();

        for (int i = 10; i < 20; i++) {
            // get Calendar with current date
            java.util.GregorianCalendar gCal = new GregorianCalendar();
            //Death info
            DeathInfo death = new DeathInfo();
            death.setDeathSerialNo(2010012345 + i);
            death.setPlaceOfDeath("place of death :" + i);
            gCal.add(Calendar.DATE, -20);
            death.setDateOfDeath(gCal.getTime());
            gCal.add(Calendar.DATE, -2);
            death.setDateOfRegistration(gCal.getTime());
            death.setPreferredLanguage("si");
            death.setDeathDivision(ambathalenPahala);
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

            //notifu authority info
            NotifyingAuthorityInfo notify = new NotifyingAuthorityInfo();
            notify.setNotifyingAuthorityName("notify name :" + i);
            notify.setNotifyingAuthorityAddress("notify address :" + i);
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

    /**
     * this test case testing loading death alteration capture page ,capturing death alterations and edit mode of a
     * death alteration
     *
     * @throws Exception if fails to login.
     */
    public void testCaptureDeathAlteration() throws Exception {

        Map session = userLogin("rg", "password");
        /*
        check page load(loading death alteration page with existing values)
        * */
        request.setParameter("pageNumber", "0");
        //searching by death serial number
        request.setParameter("serialNumber", "2010012361");
        request.setParameter("divisionUKey", "" + ambathalenPahala.getBdDivisionUKey());
        initAndExecute("/alteration/eprDeathAlterationPageLoad.do", session);
        session = deathAlterationAction.getSession();
        assertNotNull("session set", session);
        //check death register is populated
        assertNotNull("Death Register Object", deathAlterationAction.getDeathRegister());
        //check basic list are populated if success
        otherLists(deathAlterationAction);
    }

    public void testCaptureAlterationNonEdit() throws Exception {

        Map session = userLogin("rg", "password");
        //checking capturing death alteration  with death id 2 non edit mode
        request.setParameter("pageNumber", "1");
        request.setParameter("serialNumber", "2010012361");
        request.setParameter("divisionUKey", "" + ambathalenPahala.getBdDivisionUKey());
        request.setParameter("editMode", "false");
        request.setParameter("deathId", "2");
        request.setParameter("deathAlteration.deathPerson.deathPersonPINorNIC", "7896541230");
        request.setParameter("deathAlteration.deathPerson.deathPersonPassportNo", "963258741D");
        request.setParameter("deathAlteration.deathPerson.deathPersonGender", "0");
        request.setParameter("deathAlteration.deathPerson.deathPersonNameOfficialLang", "name");
        request.setParameter("deathAlteration.deathPerson.deathPersonNameInEnglish", "name english");
        request.setParameter("deathAlteration.deathPerson.deathPersonPermanentAddress", "address");
        request.setParameter("comments", "comments");
        request.setParameter("deathAlteration.deathPerson.deathPersonCountry", "2");
        request.setParameter("deathAlteration.deathPerson.deathPersonRace", "2");
        request.setParameter("deathAlteration.deathPerson.deathPersonAge", "10");
        initAndExecute("/alteration/eprCaptureDeathAlteration.do", session);
        //check bean is populated
        assertEquals("Action Errors", 0, deathAlterationAction.getActionErrors().size());
        assertEquals("Death Person Age", 10, (Object) deathAlterationAction.getDeathAlteration().getDeathPerson().getDeathPersonAge());
        //check basic list are populated if success
        //todo check more
    }

    private void basicLists(DeathAlterationAction deathAlterationAction) {
        assertNotNull("District List", deathAlterationAction.getDistrictList());
        assertNotNull("DSDivision List", deathAlterationAction.getDsDivisionList());
        assertNotNull("Death Division List", deathAlterationAction.getBdDivisionList());
    }

    private void otherLists(DeathAlterationAction deathAlterationAction) {
        assertNotNull("Race List", deathAlterationAction.getRaceList());
        assertNotNull("Country List", deathAlterationAction.getCountryList());
    }

    private Map userLogin(String userName, String passWord) throws Exception {
        request.setParameter("userName", userName);
        request.setParameter("password", passWord);
        ActionProxy proxy = getActionProxy("/eprLogin.do");
        LoginAction loginAction = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        proxy.execute();
        return loginAction.getSession();
    }
}
