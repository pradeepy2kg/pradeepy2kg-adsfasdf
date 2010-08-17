package lk.rgd.crs.web.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;

import java.util.Map;
import java.util.HashMap;
import java.util.Locale;

import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.web.action.births.BirthRegisterApprovalAction;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.crs.api.domain.BirthDeclaration;

/**
 * @author amith jayasekara
 */
public class BirthRegisterApprovalActionTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterApprovalActionTest.class);
    private ActionProxy proxy;
    private BirthRegisterApprovalAction action;
    private LoginAction loginAction;


    private String initAndExecute(String mapping, Map session) throws Exception {
        proxy = getActionProxy(mapping);
        action = (BirthRegisterApprovalAction) proxy.getAction();
        logger.debug("Action Method to be executed is {} ", proxy.getMethod());
        ActionContext.getContext().setSession(session);
        String result = null;
        try {
            result = proxy.execute();
        } catch (NullPointerException e) {
            logger.error("non fatal proxy execution error", e.getMessage());
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
        Map session = login("rg", "password");
        request.setParameter("bdId", "168");
        initAndExecute("/births/eprApproveBirthDeclaration.do", session);
        assertEquals("No Action errors", 0, action.getActionErrors().size());
        assertNotNull("BDF object ", action.getBdf());
        assertNotNull("User object", session.get(WebConstants.SESSION_USER_BEAN));
        //recode 168 is live birth
        assertEquals("Live birth", BirthDeclaration.BirthType.LIVE, action.getBirthType());

        //   assertEquals("Number of warnings ", 3, action.getWarnings().size());
        //todo improve approval with out warning
        //todo try to approve with a user dont have permission

        logger.info("testApprove completed");
    }

    public void testApproveIgnoringWarning() throws Exception {
        Map session = login("rg", "password");
        request.setParameter("bdId", "167");
        request.setParameter("ignoreWarning", "true");
        request.setParameter("confirmationApprovalFlag", "false");
        initAndExecute("/births/eprIgnoreWarning.do", session);
        assertNotNull(" Action errors", action.getActionErrors().size());
        commanApproval(session);
    }

    public void testApproveIgnoreWarningsDirect() throws Exception {
        //cannot approve it already aproved
        Map session = login("rg", "password");
        request.setParameter("bdId", "167");
        request.setParameter("ignoreWarning", "true");
        request.setParameter("confirmationApprovalFlag", "false");
        request.setParameter("directApprovalFlag", "true");
        initAndExecute("/births/eprIgnoreWarning.do", session);
        commanApproval(session);
        assertEquals("Request direct approval", true, action.isDirectApprovalFlag());
    }

    public void testReject() throws Exception {
        //cannot reject 167 is APROVED
        Map session = login("rg", "password");
        request.setParameter("bdId", "167");
        request.setParameter("comments", "test reject comment");
        initAndExecute("/births/eprRejectBirthDeclaration.do", session);
        assertNotNull("Action errors", action.getActionErrors().size());
        assertNotNull("BDF object ", action.getBdf());
        assertNotNull("User object", session.get(WebConstants.SESSION_USER_BEAN));
        assertNotNull("Request comment", action.getComments());
    }

    public void testDelete() throws Exception {
        //cannot delete recode 167 so there should be error
        Map session = login("rg", "password");
        request.setParameter("bdId", "167");
        initAndExecute("/births/eprDeleteApprovalPending.do", session);
        assertNotNull("Action errors", action.getActionErrors().size());
        assertNotNull("BDF object ", action.getBdf());
        assertNotNull("User object", session.get(WebConstants.SESSION_USER_BEAN));
    }

    //todo implement after removing bug of serial numberr duplication


    public void testFilter() throws Exception {
        //foltering by serail number
        //todo filter by date
        Map session = login("rg", "password");
        request.setParameter("confirmationApprovalFlag", "false");
        request.setParameter("bdfSerialNo", "16005");
        request.setParameter("birthDivisionId", "10");
        request.setParameter("birthDistrictId", "2");
        initAndExecute("/births/eprApprovalRefresh.do", session);
        assertEquals("No Action Errors", 0, action.getActionErrors().size());
        populateList(session);

    }

    public void testApproveListOfEntries() throws Exception {
        Map session = login("rg", "password");
        request.setParameter("index", new String[]{"167", "168", "169"});
        initAndExecute("/births/eprApproveBulk.do", session);
        assertEquals("Action errors ", 1, action.getActionErrors().size());
        assertEquals("Request index", 3, action.getIndex().length);
    }

    private void populateList(Map session) {
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

    //todo implement a method to load table
    //todo implement a mothod to check permission


    private void commanApproval(Map session) {

        assertNotNull("BDF object ", action.getBdf());
        assertNotNull("User object", session.get(WebConstants.SESSION_USER_BEAN));
        assertEquals("Request confermationFlag", false, action.isConfirmationApprovalFlag());
        assertEquals("Request ignoring warnings", true, action.isIgnoreWarning());
        //this is (167) a live birth
        assertEquals("Is live birth", BirthDeclaration.BirthType.LIVE, action.getBirthType());
    }

    private Map login(String userName, String password) throws Exception {
        request.setParameter("userName", userName);
        request.setParameter("password", password);
        ActionProxy proxy = getActionProxy("/eprLogin.do");
        loginAction = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        proxy.execute();
        return loginAction.getSession();
    }

}
