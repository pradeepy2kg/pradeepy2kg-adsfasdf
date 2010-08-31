package lk.rgd.crs.web.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.crs.web.action.deaths.DeathRegisterAction;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.common.CustomStrutsTestCase;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;

import java.util.Map;
import java.util.HashMap;

public class DeathRegistrationDeclarationTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(DeathRegistrationDeclarationTest.class);
    private DeathRegisterAction deathAction;
    private ActionProxy proxy;
    private DeathRegister ddf;


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
        assertEquals("Failed to set the deathSerialNo: ", (long)120, deathAction.getDeath().getDeathSerialNo());

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
        //there are two sample data for this senario
        assertNotNull("Sample data not null", deathAction.getDeathApprovalAndPrintList());
        assertEquals("Sample data", 0, deathAction.getDeathApprovalAndPrintList().size());
        logger.info("testing death filter by date completed");
    }

    public void testDeathDeclarationEditMode() throws Exception {
        Map session = userLogin("rg", "password");
        //this is a DATA_ENTRY state recode
        request.setParameter("idUKey", "1");
        initAndExucute("/deaths/eprDeathEditMode.do", session);
        assertEquals("No Action Errors", 0, deathAction.getActionErrors().size());
        //check user object is retrieved properly
        assertNotNull("User object ", deathAction.getUser());
        //check Death declaration is populated
        //assertNotNull("Death declaration object", deathAction.getDeathRegister());
        //assertEquals("Death Declaration is in DATA_ENTRY", DeathRegister.State.DATA_ENTRY, deathAction.getDeathRegister().getStatus());

        //try to edit non editable recode
        //this recode in APPROVE state
        request.setParameter("idUKey", "5");
        initAndExucute("/deaths/eprDeathEditMode.do", session);
        //check user object is retrieved properly
        assertNotNull("User object ", deathAction.getUser());
        //check Death declaration is populated
        //assertNotNull("Death declaration object", deathAction.getDeathRegister());
        //assertNotSame("Death Declaration is not DATA_ENTRY", DeathRegister.State.DATA_ENTRY, deathAction.getDeathRegister().getStatus());
        //there shoud be an action error
        //assertEquals("Action Error", 1, deathAction.getActionErrors().size());
        logger.info("testing death edit mode  completed");
    }

    public void testDeathReject() throws Exception {
        // in this test use idUKey 1 to reject a DATA_ENTRY mode and try to reject a data which is alredy approved (idUKey 5)
        Map session = userLogin("rg", "password");
        //approveRejectComman("/deaths/eprRejectDeath.do", session, 1, 5);
        logger.info("testing death reject completed");
    }

    public void testDeathApprove() throws Exception {
        // in this test use idUKey 1 to approve a DATA_ENTRY mode and try to reject a data which is alredy approved (idUKey 5)
        Map session = userLogin("rg", "password");
        //approveRejectComman("/deaths/eprApproveDeath.do", session, 2, 5);
        logger.info("testing death approve completed");
    }

    private void approveRejectComman(String action, Map session, int correctData, int incorrectData) throws Exception {
        // this recode in DATA_ENTRY
        settingApproveAndReject(Integer.toString(correctData));
        initAndExucute(action, session);
        //check request is populated
        compareApproveAndReject(correctData);
        assertEquals("No Action Errors", 0, deathAction.getActionErrors().size());
        populate();
        permissionToEditAndApprove();
        //idUKey 5 is APPROVED data
        settingApproveAndReject(Integer.toString(incorrectData));
        initAndExucute(action, session);
        //check idUkey is populated
        compareApproveAndReject(incorrectData);
        //there must be an action error
        assertEquals("Action Error", 1, deathAction.getActionErrors().size());
        populate();
        permissionToEditAndApprove();
    }

    private void settingApproveAndReject(String idUKey) {
        request.setParameter("idUKey", idUKey);
        request.setParameter("deathDivisionId", "1");
        request.setParameter("pageNo", "1");
    }

    private void compareApproveAndReject(int idUKey) {
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
