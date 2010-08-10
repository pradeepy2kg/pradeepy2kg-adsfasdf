package lk.rgd.crs.web.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.crs.web.action.deaths.DeathRegisterAction;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: widu
 * Date: Aug 9, 2010
 * Time: 10:30:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class LateDeathRegistrationTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(AdoptionActionTest.class);
    private ActionProxy proxy;
    private User user;
    private DeathRegisterAction deathAction;

    private Map UserLogin(String username, String passwd) throws Exception {
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
        ActionMapping mapping = getActionMapping("/deaths/eprLateDeathDeclaration.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/deaths", mapping.getNamespace());
        assertEquals("eprInitDeathDeclaration", mapping.getName());
        ActionProxy proxy = getActionProxy("/deaths/eprLateDeathDeclaration.do");
        assertNotNull(proxy);
        logger.debug("nameSpace {} and actionName {}", mapping.getNamespace(), proxy.getMethod());

        DeathRegisterAction action = (DeathRegisterAction) proxy.getAction();
        assertNotNull(action);
    }

     public void testLateDeathDeclaration() throws Exception {
      Map session = UserLogin("ashoka", "ashoka");
      initAndExucute("/deaths/eprLateDeathDeclaration.do", session);
      session = deathAction.getSession();

      assertNotNull("Dsdivision list", deathAction.getDsDivisionList());
      assertNotNull("District list", deathAction.getDistrictList());

      assertEquals("Action erros for Adoption Declaration ", 0, deathAction.getActionErrors().size());

     }


}
