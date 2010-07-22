package lk.rgd.crs.web.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.StrutsSpringTestCase;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.springframework.context.ApplicationContext;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import lk.rgd.crs.web.action.births.BirthRegisterAction;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.UnitTestManager;

/**
 * @author Indunil Moremada
 */
public class BirthRegisterActionTest extends StrutsSpringTestCase {

    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterActionTest.class);
    private ActionProxy proxy;
    private BirthRegisterAction action;
    private LoginAction loginAction;
    private Map session;

    private String initAndExucute(String mapping) throws Exception {
        proxy = getActionProxy(mapping);
        action = (BirthRegisterAction) proxy.getAction();
        logger.debug("Action Method to be executed is {} ", proxy.getMethod());
        ActionContext.getContext().setSession(session);
        String result = null;
        try {
            result = proxy.execute();
        } catch (Exception e) {
            logger.error("proxy execution error", e);
        }
        logger.debug("result for mapping {} is {}", mapping, result);
        return result;
    }

    @Override
    public String getContextLocations() {
        return "unitTest_applicationContext.xml";
    }

    public void testActionMappingProxy() {
        ActionMapping mapping = getActionMapping("/births/eprBirthRegistrationInit.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/births", mapping.getNamespace());
        assertEquals("eprBirthRegistrationInit", mapping.getName());
        ActionProxy proxy = getActionProxy("/births/eprBirthRegistrationInit.do");
        assertNotNull(proxy);
        logger.debug("nameSpace {} and actionName {}", mapping.getNamespace(), proxy.getMethod());

        BirthRegisterAction action = (BirthRegisterAction) proxy.getAction();
        assertNotNull(action);
    }

    public void testBirthDeclaratinInitializer() throws Exception {
        //todo
    }

    public void testBirthDeclarationInitializerInEditMode() throws Exception {
        login("indunil", "indunil");
        request.setParameter("bdId", "166");
        initAndExucute("/births/eprBirthRegistrationInit.do");

        assertEquals("No Action erros.", 0, action.getActionErrors().size());

        BirthDeclaration bd = (BirthDeclaration) session.get(WebConstants.SESSION_BIRTH_DECLARATION_BEAN);
        assertEquals("Data Entry Mode", 0, bd.getRegister().getStatus().ordinal());
        Object obj = session.get(WebConstants.SESSION_USER_LANG);
        assertNotNull("Session User Local Presence", obj);
        assertNotNull("Request Country List Presence", action.getCountryList());
        assertNotNull("Request District List Presence", action.getDistrictList());
        assertNotNull("Request Race List Presence", action.getRaceList());
        assertNotNull("Request Full District List Presence", action.getAllDistrictList());
        assertNotNull("Request Full DS Division List Presence", action.getAllDSDivisionList());

        //check whether all the beans are populated
        assertEquals("Request child Bean is Populated", bd.getChild(), action.getChild());
        assertEquals("Request parent Bean is Populated", bd.getParent(), action.getParent());
        assertEquals("Request grandFather Bean is Populated", bd.getGrandFather(), action.getGrandFather());
        assertEquals("Request marriage Bean is Populated", bd.getMarriage(), action.getMarriage());
        assertEquals("Request informant Bean is Populated", bd.getInformant(), action.getInformant());
        assertEquals("Request confirmant Bean is Populated", bd.getConfirmant(), action.getConfirmant());
        assertEquals("Request register Bean is Populated", bd.getRegister(), action.getRegister());
        assertEquals("Request notifyingAuthority Bean is Populated", bd.getNotifyingAuthority(), action.getNotifyingAuthority());
        logger.debug("IsLiveBirth {}", action.isLiveBirth());

        assertEquals("Request birthDistrictId is set to existing district", action.getRegister().getBirthDistrict().getDistrictUKey(), action.getBirthDistrictId());
        assertEquals("Request birthDivisionId is set to existing birthDivision", action.getRegister().getBirthDivision().getBdDivisionUKey(), action.getBirthDivisionId());
        assertEquals("Request dsDivisionId is set to existing dsDivision", action.getRegister().getDsDivision().getDsDivisionUKey(), action.getDsDivisionId());

        assertEquals("Request father Country", action.getFatherCountry(), bd.getParent().getFatherCountry().getCountryId());
        assertEquals("Request father Race", action.getFatherRace(), bd.getParent().getFatherRace().getRaceId());
        assertEquals("Request Mother Country", action.getMotherCountry(), bd.getParent().getMotherCountry().getCountryId());
        assertEquals("Request Mother Race", action.getMotherRace(), bd.getParent().getMotherRace().getRaceId());

    }

    private void login(String userName, String password) throws Exception {
        request.setParameter("userName", userName);
        request.setParameter("password", password);
        ActionProxy proxy = getActionProxy("/eprLogin.do");
        loginAction = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        proxy.execute();
        session = loginAction.getSession();
    }

    /* private void populateBirthDeclaration(BirthDeclaration bd) {
            BirthRegisterInfo bri = new BirthRegisterInfo();
            bri.setLiveBirth(true);
            bri.setBdfSerialNo(new Long(1986527));
            bri.setDateOfRegistration(convertStingToDate("11-June-10"));
            bri.setStatus(BirthDeclaration.State.DATA_ENTRY);
            bri.setBirthDivision(colomboBDDivision);
            bri.setBdDivisionPrint("Colombo");
            bri.setPreferredLanguage("en");
            bd.setRegister(bri);
            //bd.getRegister().setLiveBirth(true);
            //bd.setMarriage();
            //bd.setConfirmant(new ConfirmantInfo());
            //bd.setParent(new ParentInfo());
            //bd.setNotifyingAuthority(new NotifyingAuthorityInfo());
            //bd.setInformant();
            //bd.setGrandFather();

        }
    */
    private Date convertStingToDate(String str_date) {
        DateFormat formatter;
        Date date = null;
        try {
            formatter = new SimpleDateFormat("yy-MMM-dd");
            date = (Date) formatter.parse(str_date);
            logger.debug("Date is " + date);
        } catch (ParseException e) {
            logger.error("Error {}", e);
        }
        return date;
    }
}
