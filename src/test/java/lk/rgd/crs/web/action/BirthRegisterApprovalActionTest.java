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

/**
 * @author amith jayasekara
 */
public class BirthRegisterApprovalActionTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(BirthRegisterApprovalActionTest.class);
    private ActionProxy proxy;
    private BirthRegisterApprovalAction action;
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


}
