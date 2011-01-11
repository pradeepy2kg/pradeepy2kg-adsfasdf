package lk.rgd.crs.web.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.springframework.context.ApplicationContext;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.api.dao.CountryDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.crs.web.action.deaths.DeathRegisterAction;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.service.DeathRegistrationService;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.UnitTestManager;

import java.util.*;
import java.text.DateFormat;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.extensions.TestSetup;

public class LateDeathRegistrationTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(LateDeathRegistrationTest.class);
    private ActionProxy proxy;
    private User user;
    private DeathRegisterAction deathAction;

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
    protected final static DeathRegistrationService deathRegistrationService = (DeathRegistrationService) ctx.getBean("deathRegisterService", DeathRegistrationService.class);


    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(LateDeathRegistrationTest.class)) {
            protected void setUp() throws Exception {
                logger.info("setup called");
                colomboBDDivision = bdDivisionDAO.getBDDivisionByPK(1);
                negamboBDDivision = bdDivisionDAO.getBDDivisionByPK(9);
                sriLanka = countryDAO.getCountry(1);
                sinhalese = raceDOA.getRace(1);

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
            logger.debug("exception when autharizing a user :'rg' ");
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
            death.setDeathSerialNo(2010012445 + i);
            death.setPlaceOfDeath("place of death :" + i);
            gCal.add(Calendar.DATE, -20);
            death.setDateOfDeath(gCal.getTime());                   
            gCal.add(Calendar.DATE, -2);
            death.setDateOfRegistration(gCal.getTime());
            death.setPreferredLanguage("si");
            death.setDeathDivision(colomboBDDivision);
            death.setPlaceOfBurial("place of burial : " + i);

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

            //notifu authority info
            NotifyingAuthorityInfo notify = new NotifyingAuthorityInfo();
            notify.setNotifyingAuthorityName("notify name :" + i);
            notify.setNotifyingAuthorityAddress("notifi address :" + i);
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

    private void initAndExucute(String mapping, Map session) {
        proxy = getActionProxy(mapping);
        deathAction = (DeathRegisterAction) proxy.getAction();
        assertNotNull(deathAction);
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

    public void testActionMappingProxy() {
        ActionMapping mapping = getActionMapping("/deaths/eprInitLateDeathDeclaration.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/deaths", mapping.getNamespace());
        assertEquals("eprInitLateDeathDeclaration", mapping.getName());
        ActionProxy proxy = getActionProxy("/deaths/eprInitLateDeathDeclaration.do");
        assertNotNull(proxy);
        logger.debug("nameSpace {} and actionName {}", mapping.getNamespace(), proxy.getMethod());

        DeathRegisterAction action = (DeathRegisterAction) proxy.getAction();
        assertNotNull(action);
    }

    public void testLateDeathDeclaration() throws Exception {
        Map session = UserLogin("ashoka", "ashoka");
        initAndExucute("/deaths/eprInitLateDeathDeclaration.do", session);
        assertEquals("Action errors for Adoption Declaration ", 0, deathAction.getActionErrors().size());

        DeathRegister ddf;
        session = deathAction.getSession();
        assertNotNull("Dsdivision list", deathAction.getDsDivisionList());
        assertNotNull("District list", deathAction.getDistrictList());
        ddf = (DeathRegister) session.get(WebConstants.SESSION_DEATH_DECLARATION_BEAN);

        request.setParameter("death.causeOfDeath", "Bus accident");
        request.setParameter("death.causeOfDeathEstablished", "false");
        request.setParameter("death.dateOfDeath", "2010-08-01");
        request.setParameter("death.dateOfRegistration", "2010-08-17");
        request.setParameter("death.deathSerialNo", "123");
        request.setParameter("death.icdCodeOfCause", "33EE");
        request.setParameter("death.infantLessThan30Days", "false");
        request.setParameter("death.placeOfBurial", "මහරගම");
        request.setParameter("death.placeOfDeath", "මහරගම මහරෝහල");
        request.setParameter("death.placeOfDeathInEnglish.", "Maharagama Hospital");
        request.setParameter("death.reasonForLateRegistration", "No reason");
        request.setParameter("death.timeOfDeath", "12:30");
        request.setParameter("deathDistrictId", "1");
        request.setParameter("deathDivisionId", "1");
        request.setParameter("deathPerson.deathPersonAge", "34");
        request.setParameter("deathPerson.deathPersonFatherFullName", "Samarakone P.");
        request.setParameter("deathPerson.deathPersonFatherPINorNIC", "34343434");
        request.setParameter("deathPerson.deathPersonGender", "0");
        request.setParameter("deathPerson.deathPersonMotherFullName", "Silawathi S.");
        request.setParameter("deathPerson.deathPersonMotherPINorNIC", "34354455");
        request.setParameter("deathPerson.deathPersonNameOfficialLang", "සෝමතිලක ජයසූරිය");
        request.setParameter("deathPerson.deathPersonNameInEnglish", "Somathilaka Jayasooriya");
        request.setParameter("deathPerson.deathPersonPINorNIC", "333333");
        request.setParameter("deathPerson.deathPersonPassportNo", "343434");
        request.setParameter("deathPerson.deathPersonPermanentAddress", "Wijayarama Rd,Egodawaththa.");
        request.setParameter("deathPersonCountry", "1");
        request.setParameter("deathPersonRace", "0");
        request.setParameter("deathType", "MISSING");
        request.setParameter("dsDivisionId", "1");
        request.setParameter("pageNo", "1");

        initAndExucute("/deaths/eprDeathDeclaration.do", session);
        session = deathAction.getSession();
        assertEquals("Action erros for Adoption Declaration ", 0, deathAction.getActionErrors().size());

        assertEquals("Caurse of Death", "BUS ACCIDENT", ddf.getDeath().getCauseOfDeath());
        assertEquals("Date of Death", "2010-08-01", DateTimeUtils.getISO8601FormattedString(ddf.getDeath().getDateOfDeath()));
        assertEquals("date of Registration", "2010-08-17", DateTimeUtils.getISO8601FormattedString(ddf.getDeath().getDateOfRegistration()));
        assertEquals("Cause Of Death Established", false, ddf.getDeath().isCauseOfDeathEstablished());
        assertEquals("Infant Less Than 30 Days", false, ddf.getDeath().isInfantLessThan30Days());
        assertEquals("Icd Code Of Cause", "33EE", ddf.getDeath().getIcdCodeOfCause());
        assertEquals("Place of Death", "මහරගම මහරෝහල", ddf.getDeath().getPlaceOfDeath());
        assertEquals("Place of Burial", "මහරගම", ddf.getDeath().getPlaceOfBurial());
        assertEquals("Time of daath", "12:30", ddf.getDeath().getTimeOfDeath());
        assertEquals("deathDistrictId", 11, ddf.getDeath().getDeathDivision().getDistrict().getDistrictId());
        assertEquals("deathDivisionId", 1, ddf.getDeath().getDeathDivision().getDivisionId());
        assertEquals("deathPersonFatherFullName", "SAMARAKONE P.", ddf.getDeathPerson().getDeathPersonFatherFullName());
        assertEquals("deathPersonMotherFullName", "SILAWATHI S.", ddf.getDeathPerson().getDeathPersonMotherFullName());
        assertEquals("deathPersonNameOfficialLang", "සෝමතිලක ජයසූරිය", ddf.getDeathPerson().getDeathPersonNameOfficialLang());
        assertEquals("deathPersonNameInEnglish", "SOMATHILAKA JAYASOORIYA", ddf.getDeathPerson().getDeathPersonNameInEnglish());
        assertEquals("deathPersonPermanentAddress", "WIJAYARAMA RD,EGODAWATHTHA.", ddf.getDeathPerson().getDeathPersonPermanentAddress());

        request.setParameter("declarant.declarantAddress", "Egodawaththa,Maharagama.");
        request.setParameter("declarant.declarantEMail", "wwwww@gmail.com");
        request.setParameter("declarant.declarantFullName", "Rangith Kumara");
        request.setParameter("declarant.declarantNICorPIN", "333333333");
        request.setParameter("declarant.declarantPhone", "3434343434");
        request.setParameter("declarant.declarantType", "RELATIVE");
        request.setParameter("notifyingAuthority.notifyingAuthorityName", "Rajapaksha M.");
        request.setParameter("notifyingAuthority.notifyingAuthorityAddress", "GANGODAVILA,EGODAWATHTHA");
        request.setParameter("notifyingAuthority.notifyingAuthorityPIN", "852012132V");
        request.setParameter("notifyingAuthority.notifyingAuthoritySignDate", "2010-08-17");
        request.setParameter("pageNo", "2");

        initAndExucute("/deaths/eprDeathDeclaration.do", session);
        session = deathAction.getSession();
        assertEquals("Action erros for Adoption Declaration ", 0, deathAction.getActionErrors().size());

        assertEquals("Declarent Address", "EGODAWATHTHA,MAHARAGAMA.", ddf.getDeclarant().getDeclarantAddress());
        assertEquals("Declarent E-Mail Address", "wwwww@gmail.com", ddf.getDeclarant().getDeclarantEMail());
        assertEquals("Declarent Name", "RANGITH KUMARA", ddf.getDeclarant().getDeclarantFullName());
        assertEquals("NotifyingAuthority Name", "RAJAPAKSHA M.", ddf.getNotifyingAuthority().getNotifyingAuthorityName());
        assertEquals("NotifyingAuthority Address", "GANGODAVILA,EGODAWATHTHA", ddf.getNotifyingAuthority().getNotifyingAuthorityAddress());
        logger.debug("New late death declaration successfuly persist S idUKey : {}", ddf.getIdUKey());
    }


}
