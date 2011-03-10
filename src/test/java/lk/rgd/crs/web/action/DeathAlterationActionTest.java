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
 *         TODO with action test print list
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
                    deathRegistrationService.addNewDeathRegistration((DeathRegister) deaths.get(i), sampleUser);
                }
                //change state to certificate printed otherwise cannot add alterations
                DeathRegister dr;
                dr = deathRegistrationService.getByBDDivisionAndDeathSerialNo(ambathalenPahala, 2010012361, sampleUser);
                deathRegistrationService.approveDeathRegistration(dr.getIdUKey(), sampleUser, true);
                dr = deathRegistrationService.getByBDDivisionAndDeathSerialNo(ambathalenPahala, 2010012361, sampleUser);
                deathRegistrationService.markDeathCertificateAsPrinted(dr, sampleUser);

                for (int i = 0; i < deathAlterations.size(); i++) {
                    deathAlterationService.addDeathAlteration((DeathAlteration) deathAlterations.get(i), sampleUser);
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
            logger.debug("exception when authorizing  user :'rg' ");
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

    public void testCaptureDeathAlterationInit() throws Exception {
        Map session = userLogin("rg", "password");
        //loading death register for alteration by serial number and death division
        //loading a recoded death at 'ambathalen pahala' (idUKey 9 ) and serial number 2010012361
        request.setParameter("serialNumber", "2010012361");
        request.setParameter("divisionUKey", "9");
        initAndExecute("/alteration/eprDeathAlterationPageLoad.do", session);
        //check death register is populated without action errors
        assertEquals("Action errors", 0, deathAlterationAction.getActionErrors().size());
        assertNotNull("Death register object found ", deathAlterationAction.getDeathRegister());
        //check alteration object populated
        assertNotNull("Death alteration object populated", deathAlterationAction.getDeathAlteration());
        //check other lists are populated
        otherLists(deathAlterationAction);
        //check other fields(death division name/serial number/district name /ds division name)
        assertNotNull("District name ", deathAlterationAction.getDistrict());
        assertNotNull("DS Division name", deathAlterationAction.getDsDivision());
        assertNotNull("Division name", deathAlterationAction.getDeathDivision());
        assertNotNull("Serial number", deathAlterationAction.getSerialNumber());

        //now we load the page with the information form the death register and page about to submit with changes to
    }

    public void testDeathAlterationCapture() throws Exception {
        Map session = userLogin("rg", "password");
        User user = loginSampleUser();
        DeathRegister dr = deathRegistrationService.getByBDDivisionAndDeathSerialNo(ambathalenPahala, 2010012361, user);
        //setting changed values
        request.setParameter("deathId", "" + dr.getIdUKey());
        request.setParameter("editDeathInfo", "true");
        request.setParameter("editDeathPerson", "true");
        request.setParameter("deathAlteration.dateReceived", "2010-11-16");

        request.setParameter("deathAlteration.deathInfo.dateOfDeath", "2010-11-09");
        request.setParameter("deathAlteration.deathInfo.placeOfDeath", "place of death changed value");

        request.setParameter("deathAlteration.deathPerson.deathPersonNameOfficialLang", "name official language changed value");
        request.setParameter("deathAlteration.deathPerson.deathPersonNameInEnglish", "name english changed value");

        request.setParameter("deathAlteration.declarant.declarantFullName", "declerant name");
        request.setParameter("deathAlteration.declarant.declarantType", "RELATIVE");

        initAndExecute("/alteration/eprCaptureDeathAlteration.do", session);
        assertEquals("Action errors", 0, deathAlterationAction.getActionErrors().size());
        assertEquals("Action message", 1, deathAlterationAction.getActionMessages().size());
        //check death alteration is populated
        assertNotNull("death person ", deathAlterationAction.getDeathAlteration().getDeathPerson());
        assertNotNull("death alteration info", deathAlterationAction.getDeathAlteration().getDeathInfo());
        assertNotNull("declarent info", deathAlterationAction.getDeathAlteration().getDeclarant());
        basicLists(deathAlterationAction);
    }

    /**
     * test case check loading page for death alteration edit mode
     * death record serial 2010012361 and death division idUKey 9 now have an alteration
     * this alteration record have idUKey 7
     */
    public void testDeathAlterationEditModeInit() throws Exception {
        Map session = userLogin("rg", "password");
        request.setParameter("deathAlterationId", "7");
        initAndExecute("/alteration/eprDeathAlterationEditInit.do", session);
        //check death alteration object is populated
        assertEquals("Action error", 0, deathAlterationAction.getActionErrors().size());
        assertEquals("Death alteration idUKey", 7, deathAlterationAction.getDeathAlterationId());
        assertNotNull("Death alteration object", deathAlterationAction.getDeathAlteration());
        assertNotNull("Death register object", deathAlterationAction.getDeathRegister());
        assertEquals("edit mode", true, deathAlterationAction.isEditMode());
        otherLists(deathAlterationAction);
    }

    public void testDeathAlterationEdit() throws Exception {
        Map session = userLogin("rg", "password");
        request.setParameter("deathAlterationId", "7");
        request.setParameter("editDeathInfo", "true");
        request.setParameter("editDeathPerson", "true");
        request.setParameter("deathAlteration.dateReceived", "2010-11-16");

        request.setParameter("deathAlteration.deathInfo.dateOfDeath", "2010-11-10");
        request.setParameter("deathAlteration.deathInfo.placeOfDeath", "place of death changed value edited");

        request.setParameter("deathAlteration.deathPerson.deathPersonNameOfficialLang", "name official language changed value edited");
        request.setParameter("deathAlteration.deathPerson.deathPersonNameInEnglish", "name english changed value edited");

        request.setParameter("deathAlteration.declarant.declarantFullName", "declerant name edited");
        request.setParameter("deathAlteration.declarant.declarantType", "RELATIVE");
        initAndExecute("/alteration/eprEditDeathAlteration.do", session);
        assertEquals("Action errors", 0, deathAlterationAction.getActionErrors().size());
        assertEquals("Action message", 1, deathAlterationAction.getActionMessages().size());
        //check death alteration is populated
        assertNotNull("death person ", deathAlterationAction.getDeathAlteration().getDeathPerson());
        assertNotNull("death alteration info", deathAlterationAction.getDeathAlteration().getDeathInfo());
        assertNotNull("declarent info", deathAlterationAction.getDeathAlteration().getDeclarant());
    }

    public void testRejectDeathAlteration() throws Exception {
        Map session = userLogin("rg", "password");
        request.setParameter("pageNumber", "1");
        //rejecting pre populated data entry mode alteration record
        request.setParameter("deathAlterationId", "2");
        initAndExecute("/alteration/eprDeathAlterationReject.do", session);
        basicLists(deathAlterationAction);
    }

    public void testDeleteDeathAlteration() throws Exception {
        Map session = userLogin("rg", "password");
        //deleting pre populated data entry mode alteration record  3
        request.setParameter("deathAlterationId", "3");
        initAndExecute("/alteration/eprDeathAlterationDelete.do", session);
        basicLists(deathAlterationAction);
    }

    /**
     * this test function test the process of generating changes list
     */
    public void testDisplayChangesForApproval() throws Exception {
        //display changes between  alteration record 7 and  its death register
        Map session = userLogin("rg", "password");
        request.setParameter("deathAlterationId", "7");
        initAndExecute("/alteration/eprApproveDeathAlterationsDirect.do", session);
        //checking no action errors
        assertEquals("Action errors", 0, deathAlterationAction.getActionErrors().size());
        //death alteration is populated and it's state is DATA_ENTRY
        assertNotNull("Death alteration", deathAlterationAction.getDeathAlteration());
        assertEquals("Death alteration state", DeathAlteration.State.DATA_ENTRY, deathAlterationAction.getDeathAlteration().getStatus());
        //check death register is populated
        assertNotNull("Death register", deathAlterationAction.getDeathRegister());
        //if it is correct death register object
        assertEquals("Correct objects to compare", deathAlterationAction.getDeathAlteration().getDeathRegisterIDUkey(),
            deathAlterationAction.getDeathRegister().getIdUKey());
        assertEquals("Approval page", true, deathAlterationAction.isApprovalPage());
    }

    public void testApproveAndApplyChanges() throws Exception {
        //TODO check this test is not working
        Map session = userLogin("rg", "password");
        //    request.setParameter("approvedIndex", "{10,12}");
        initAndExecute("/alteration/eprDeathAlterationPrintLetter.do", session);
    }

    public void testPrintLetter() throws Exception {
        Map session = userLogin("rg", "password");
        request.setParameter("deathAlterationId", "7");
        initAndExecute("/alteration/eprDeathAlterationPrintLetter.do", session);
        //checking no action errors
        assertEquals("Action errors", 0, deathAlterationAction.getActionErrors().size());
        //death alteration is populated and it's state is DATA_ENTRY
        assertNotNull("Death alteration", deathAlterationAction.getDeathAlteration());
        assertEquals("Death alteration state", DeathAlteration.State.DATA_ENTRY, deathAlterationAction.getDeathAlteration().getStatus());
        //check death register is populated
        assertNotNull("Death register", deathAlterationAction.getDeathRegister());
        //if it is correct death register object
        assertEquals("Correct objects to compare", deathAlterationAction.getDeathAlteration().getDeathRegisterIDUkey(),
            deathAlterationAction.getDeathRegister().getIdUKey());
        assertEquals("letter page", false, deathAlterationAction.isApprovalPage());
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
        request.setParameter("javaScript","true");
        request.setParameter("userName", userName);
        request.setParameter("password", passWord);
        ActionProxy proxy = getActionProxy("/eprLogin.do");
        LoginAction loginAction = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        proxy.execute();
        return loginAction.getSession();
    }
}
