package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionProxy;
import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.web.WebConstants;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class LoginActionTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(LoginActionTest.class);
    private ActionProxy proxy;
    private LoginAction action;

    private String initAndExucute(String mapping) throws Exception {
        proxy = getActionProxy(mapping);
        action = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        String result = proxy.execute();

        logger.debug("result for mapping {} is {}", mapping, result);
        return result;
    }


    public void testActionMappingProxy() {
        ActionMapping mapping = getActionMapping("/eprLogin.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/", mapping.getNamespace());
        assertEquals("eprLogin", mapping.getName());

        ActionProxy proxy = getActionProxy("/eprLogin.do");
        assertNotNull(proxy);

        LoginAction action = (LoginAction) proxy.getAction();
        assertNotNull(action);
    }

    public void testRGLogin() throws Exception {
        //set parameters before calling getActionProxy
        request.setParameter("userName", "rg");
        request.setParameter("password", "password");

        String result = initAndExucute("/eprLogin.do");
        // todo uncomment this line after fixing testing framework bug -always return 'ERROR' forward.
        //assertEquals("success not returned.", Action.SUCCESS, result);

        assertEquals("No Action erros.", 0, action.getActionErrors().size());

        // check for proper values in session after login
        Map session = action.getSession();
        Object obj = session.get(WebConstants.SESSION_USER_BEAN);
        assertNotNull("Session User object peresence.", obj);
        User user = (User) obj;
        assertEquals("Correctness of User object", "rg", user.getUserId());

        obj = session.get(WebConstants.SESSION_USER_LANG);
        assertNotNull("Session User Local presence", obj);
        //todo get RG language preference from DB and assert with the value of obj

        obj = session.get(WebConstants.SESSION_USER_MENUE_LIST);
        assertNotNull("Session Menu List presence", obj);
        Map menu = (Map) obj;
        // todo assert for an birth approval link within this menu, since this is RG
    }

    //todo
    public void testADRLogin() throws Exception {
    }

    //todo
    public void testDEOLogin() throws Exception {
    }

    //todo
    public void testAdminLogin() throws Exception {
    }

    //todo
    public void testLogout() throws Exception {
    }

    //todo
    public void testIncorrectLogins() throws Exception {
    }

    //todo : assert that we get landed in password change page and we can not go to any other page from here.
    public void testFirstTimeLogin() throws Exception {
    }


//        assertTrue("Problem There were no errors present in fieldErrors but there should have been one error present",
//                action.getFieldErrors().size() == 1);
//		assertTrue("Problem field answer not present in fieldErrors but it should have been",
//				action.getFieldErrors().containsKey("answer") );
}


