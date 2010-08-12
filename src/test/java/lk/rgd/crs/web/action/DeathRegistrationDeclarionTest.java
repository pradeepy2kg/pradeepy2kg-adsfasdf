package lk.rgd.crs.web.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import lk.rgd.crs.web.action.deaths.DeathRegisterAction;
import lk.rgd.crs.web.action.births.BirthRegisterAction;
import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.common.CustomStrutsTestCase;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;

import java.util.Map;
import java.util.HashMap;

public class DeathRegistrationDeclarionTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(DeathRegistrationDeclarionTest.class);
    private DeathRegisterAction deathAction;
    private ActionProxy proxy;

    public void testBirthConfirmationInitMappingProxy() throws Exception {
        Map session = userLogin("ashoka", "ashoka");
        ActionMapping mapping = getActionMapping("/deaths/eprInitDeathDeclaration.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/deaths", mapping.getNamespace());
        assertEquals("eprInitDeathDeclaration", mapping.getName());

        request.setParameter("pageNo", "1");
        request.setParameter("death.deathSerialNo", "1");
        request.setParameter("death.dateOfDeath", "2010-07-21T00:00:00+05:30");
        request.setParameter("death.deathDistrictId", "11");
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


        /*request.setParameter("declarant.declarantFullName","Tharanga Punchihewa")
         request.setParameter("declarant.declarantAddress","Erapalamulla,Ruwanwella");
         request.setParameter("declarant.declarantPhone","0718617804V");
         request.setParameter("declarant.declarantEMail","htpunchihewa@gmail.com");
         request.setParameter("declarant.declarantType","3");
         request.setParameter("firstWitness.witnessNICorPIN","782790393");
         request.setParameter("firstWitness.witnessFullName","Sampath Naline");
         request.setParameter("firstWitness.witnessAddress",""Molawaththa,Morawaththa,Ruwanwella);
         request.setParameter("secondWitness.witnessNICorPIN","892790393V");
         request.setParameter("secondWitness.witnessFullName","Rasika Niranjana Punchihewa");
         request.setParameter("secondWitness.witnessAddress","E51 Erapalamulla Ruwanwella");
         request.setParameter("notifyingAuthority.notifyingAuthorityPIN","853303399v");
         request.setParameter("notifyingAuthority.notifyingAuthorityName","Saman kUmara";
         request.setParameter("notifyingAuthority.notifyingAuthorityAddress","කැලුම් කොඩිප්පිලි"");
         request.setParameter("notifyingAuthority.notifyingAuthoritySignDate","2010-07-28T00:00:00+05:30");
        */


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
        assertEquals("Sample data", 1, deathAction.getDeathApprovalAndPrintList().size());

        populate();
        permissionToEditAndApprove();

    }

    public void testFilterByDate() throws Exception {
        Map session = userLogin("rg", "password");
        request.setParameter("deathDivisionId", "1");
        request.setParameter("fromDate", "08/01/2009");
        request.setParameter("endDate", "08/31/2010");
        initAndExucute("/deaths/eprDeathFilterByStatus.do", session);
        //check filter by date
        assertEquals("Search by date", true, deathAction.isSearchByDate());
        //check death divsion is setted properlly
        assertEquals("Death division", 1, deathAction.getDeathDivisionId());
        //there are two sample data for this senario
        assertNotNull("Sample data not null", deathAction.getDeathApprovalAndPrintList());
        assertEquals("Sample data", 2, deathAction.getDeathApprovalAndPrintList().size());
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
        assertNotNull("Death declaration object", deathAction.getDeathRegister());
        assertEquals("Death Declaration is in DATA_ENTRY", DeathRegister.State.DATA_ENTRY, deathAction.getDeathRegister().getStatus());

        //try to edit non editable recode
        //this recode in APPROVE state
        request.setParameter("idUKey", "5");
        initAndExucute("/deaths/eprDeathEditMode.do", session);
        //check user object is retrieved properly
        assertNotNull("User object ", deathAction.getUser());
        //check Death declaration is populated
        assertNotNull("Death declaration object", deathAction.getDeathRegister());
        assertNotSame("Death Declaration is not DATA_ENTRY", DeathRegister.State.DATA_ENTRY, deathAction.getDeathRegister().getStatus());
        //there shoud be an action error
        assertEquals("Action Error", 1, deathAction.getActionErrors().size());
    }

    public void testDeathReject() throws Exception {
/*        Map session = userLogin("rg", "password");
        //try to approve recode in DATA_ENTRY state  this recode in DATA_ENTRY
        request.setParameter("idUKey", "1");
        initAndExucute("/deaths/eprRejectDeath.do", session);
        //check idUkey is populated
        assertEquals("IDUKEY ", 1, deathAction.getIdUKey());
        populate();
        permissionToEditAndApprove();

        //try to reject alreafy approved death declaration
        //idUKey 5 is APPROVED data
        request.setParameter("idUKey", "5");
        initAndExucute("/deaths/eprRejectDeath.do", session);
        //check idUkey is populated
        assertEquals("IDUKEY ", 1, deathAction.getIdUKey());
        //check user object is retrieved properly
        assertNotNull("User object ", deathAction.getUser());
        //there must be an action error
        assertEquals("Action Error", 1, deathAction.getActionErrors().size());
        //check district list is not null for user
        assertNotNull("District list", deathAction.getDistrictList());
        //check country list is not null
        assertNotNull("Country List ", deathAction.getCountryList());
        //check bddivision list is not null for the user
        assertNotNull("BD Division List for User", deathAction.getBdDivisionList());
        permissionToEditAndApprove();*/
    }

    private void populate() {
        assertEquals("No Action Errors", 0, deathAction.getActionErrors().size());
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
