package lk.rgd.crs.web.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;

import java.util.Map;
import java.util.HashMap;

import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.crs.web.action.births.BirthRegisterApprovalAction;
import lk.rgd.crs.web.WebConstants;

/**
 * @author amith jayasekara
 */
public class BirthRegisterApprovalActionTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterApprovalActionTest.class);
    private ActionProxy proxy;
    private BirthRegisterApprovalAction action;
    private LoginAction loginAction;
    private Map session = new HashMap<String, Object>();


    private String initAndExucute(String mapping) throws Exception {
        proxy = getActionProxy(mapping);
        action = (BirthRegisterApprovalAction) proxy.getAction();
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
        ActionMapping mapping = getActionMapping("/births/eprBirthRegisterApproval.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/births", mapping.getNamespace());
        assertEquals("eprBirthRegisterApproval", mapping.getName());
        ActionProxy proxy = getActionProxy("/births/eprBirthRegisterApproval.do");
        assertNotNull(proxy);
        logger.debug("nameSpace {} and actionName {}", mapping.getNamespace(), proxy.getMethod());

        BirthRegisterApprovalAction action = (BirthRegisterApprovalAction) proxy.getAction();
        assertNotNull(action);
    }

    /**
     * approve a birth declaration
     *
     * @throws Exception
     */
    public void testApprove() throws Exception {
        login("rg", "password");
        request.setParameter("bdId", "167");
        initAndExucute("/births/eprApproveBirthDeclaration.do");
        assertEquals("No Action errors", 0, action.getActionErrors().size());
        assertNotNull("BDF object ", action.getBdf());
        assertNotNull("User object", session.get(WebConstants.SESSION_USER_BEAN));
        //recode 167 is live birth
        assertEquals("Live birth", true, action.isLiveBirth());
        //this recode have 3 warnings
        //todo fix this : assertEquals("Number of warnings ", 3, action.getWarnings().size());
        //todo improve approval with out warning
        //todo try to approve with a user dont have permission

        logger.info("testApprove completed");
    }

    public void testApproveIgnoringWarning() throws Exception {
        login("rg", "password");
        request.setParameter("bdId", "167");
        request.setParameter("ignoreWarning", "true");
        request.setParameter("confirmationApprovalFlag", "false");
        initAndExucute("/births/eprIgnoreWarning.do");
        assertNotNull(" Action errors", action.getActionErrors().size());
        commanApproval();
    }

    public void testApproveIgnoreWarningsDirect() throws Exception {
        //cannot approve it already aproved
        login("rg", "password");
        request.setParameter("bdId", "167");
        request.setParameter("ignoreWarning", "true");
        request.setParameter("confirmationApprovalFlag", "false");
        request.setParameter("directApprovalFlag", "true");
        initAndExucute("/births/eprIgnoreWarning.do");
        commanApproval();
        assertEquals("Request direct approval", true, action.isDirectApprovalFlag());
    }

    public void testReject() throws Exception {
        //cannot reject 167 is APROVED
        login("rg", "password");
        request.setParameter("bdId", "167");
        request.setParameter("comments", "test reject comment");
        initAndExucute("/births/eprRejectBirthDeclaration.do");
        assertNotNull("Action errors", action.getActionErrors().size());
        assertNotNull("BDF object ", action.getBdf());
        assertNotNull("User object", session.get(WebConstants.SESSION_USER_BEAN));
        assertNotNull("Request comment", action.getComments());
    }

    public void testDelete() throws Exception {
        //cannot delete recode 167 so there should be error
        login("rg", "password");
        request.setParameter("bdId", "167");
        initAndExucute("/births/eprDeleteApprovalPending.do");
        assertNotNull("Action errors", action.getActionErrors().size());
        assertNotNull("BDF object ", action.getBdf());
        assertNotNull("User object", session.get(WebConstants.SESSION_USER_BEAN));
    }

    //todo implement after removing bug of serial numberr duplication
/*
    public void testFilter() throws Exception {
        //foltering by serail number
        //todo filter by date 
        login("rg", "password");
        request.setParameter("confirmationApprovalFlag", "false");
        request.setParameter("bdfSerialNo", "15034");
        request.setParameter("birthDivisionId", "10");
        initAndExucute("/births/eprApprovalRefresh.do");
        assertEquals("No Action Errors", 0, action.getActionErrors().size());
    }
*/

    //todo implement a method to load table
    //todo implement a mothod to check permission


    private void commanApproval() {

        assertNotNull("BDF object ", action.getBdf());
        assertNotNull("User object", session.get(WebConstants.SESSION_USER_BEAN));
        assertEquals("Request confermationFlag", false, action.isConfirmationApprovalFlag());
        assertEquals("Request ignoring warnings", true, action.isIgnoreWarning());
        //this is (167) a live birth
        assertEquals("Is live birth", true, action.isLiveBirth());
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
