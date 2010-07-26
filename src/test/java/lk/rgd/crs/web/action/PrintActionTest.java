package lk.rgd.crs.web.action;

import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.web.action.births.PrintAction;
import lk.rgd.crs.web.WebConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;

import java.util.Map;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author amith jayasekara
 */
public class PrintActionTest extends CustomStrutsTestCase {

    private static final Logger logger = LoggerFactory.getLogger(PrintActionTest.class);
    private ActionProxy proxy;
    private PrintAction action;
    private Map session = new HashMap<String, Object>();
    private LoginAction loginAction;

    private String initAndExucute(String mapping) throws Exception {

        proxy = getActionProxy(mapping);
        action = (PrintAction) proxy.getAction();
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
        ActionMapping mapping = getActionMapping("/births/eprBirthConfirmationBulkPrint.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/births", mapping.getNamespace());
        assertEquals("eprBirthConfirmationBulkPrint", mapping.getName());
        ActionProxy proxy = getActionProxy("/births/eprBirthConfirmationBulkPrint.do");
        assertNotNull(proxy);
        logger.debug("nameSpace {} and actionName {}", mapping.getNamespace(), proxy.getMethod());

        PrintAction action = (PrintAction) proxy.getAction();
        assertNotNull(action);
    }


    public void testLoadBirthCertificatePrintList() throws Exception {
        //loggin as a rg
        login("rg", "password");
        initAndExucute("/births/eprBirthCertificateList.do");
        //check no action errors
        assertEquals("No Action erros.", 0, action.getActionErrors().size());
        populateList();

        logger.info("testing LoadBirthCertificatePrintList completed");

    }

    public void testFilterPrintList() throws Exception {
        //login and init as rg
        login("rg", "password");
        //setting data to execute method
        //set bdDivision id =1 (Colombo Fort (Medical))  and printed false
        request.setParameter("birthDivisionId", "1");
        request.setParameter("printed", "false");
        initAndExucute("/births/eprFilterBirthCetificateList.do");
        comman();
        //testing invalide data
        filterPrintListWithInvalideData();

        logger.info("testing FilterPrintList completed");
    }

    public void testPrintBulkOfEntries() throws Exception {
        login("rg", "password");
        request.setParameter("printed", "false");
        String[] index = new String[]{"164"};
        request.setParameter("index", index);
        request.setParameter("birthDivisionId", "1");
        initAndExucute("/births/eprBirthCertificateBulkPrint.do");
        comman();
        //check for invalide data

        logger.info("testing LoadBirthCertificatePrintList completed");
    }

    public void testNext() throws Exception {
        login("rg", "password");
        commanPreNext();
        initAndExucute("/births/eprPrintNext.do");
        commanPreNextCheck();
    }

    public void testPrevious() throws Exception {
        login("rg", "password");
        commanPreNext();
        initAndExucute("/births/eprPrintPrevious.do");
        commanPreNextCheck();
    }

    private void commanPreNext() {
        request.setParameter("pageNo", "2");
        request.setParameter("birthDivisionId", "1");
        request.setParameter("printed", "true");
        request.setParameter("printStart", "5");
    }

    private void commanPreNextCheck() {
        assertEquals("No Action Errors", 0, action.getActionErrors().size());
        assertNotNull("Request Page number", action.getPageNo());
        assertNotNull("Request BirthDivisionID", action.getBirthDivisionId());
        assertNotNull("Request Printed", action.isPrinted());
        assertNotNull("Request Print Start", action.getPrintStart());
        logger.info("page number : {}", action.getPageNo());
    }

    private void comman() {
        //check no action errors
        assertEquals("No Action erros.", 0, action.getActionErrors().size());
        //test cases
        //check no action errors
        assertEquals("No Action erros.", 0, action.getActionErrors().size());
        //check user is set
        assertNotNull("User is not null ", action.getUser());
        assertEquals("Request birthDivisionId", 1, action.getBirthDivisionId());
        assertEquals("Request printed", false, action.isPrinted());

        //check list population
        populateList();
    }

    private void filterPrintListWithInvalideData() throws Exception {
        request.setParameter("birthDivisionId", "0");
        request.setParameter("printed", "false");
        initAndExucute("/births/eprFilterBirthCetificateList.do");
        //check is there any action errors
        assertNotNull("Action errors ", action.getActionErrors());
    }

    /**
     * this method test page loads after refresing or at begining
     */
    private void populateList() {
        //check users preferd language
        Locale userLan = (Locale) session.get(WebConstants.SESSION_USER_LANG);
        assertNotNull("Session User Local Presence", userLan);
        //check user is not null
        User user = (User) session.get(WebConstants.SESSION_USER_BEAN);
        assertNotNull("Session User", user);
        //check initial district are loaded properly
        assertNotNull("Response User districtList", action.getDistrictList());
        //check initial dsDivision are loaded
        assertNotNull("Response User init dsdivision list", action.getDsDivisionList());
        //check intial bdDivision
        assertNotNull("Response User init bddivision list", action.getBdDivisionList());
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


}
