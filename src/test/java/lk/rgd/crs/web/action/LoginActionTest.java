package lk.rgd.crs.web.action;

import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;
import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.web.WebConstants;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import lk.rgd.Permission;

public class LoginActionTest extends CustomStrutsTestCase {
    private static final Logger logger = LoggerFactory.getLogger(LoginActionTest.class);
    private ActionProxy proxy;
    private LoginAction action;

    private String login() throws Exception {
        proxy = getActionProxy("/eprLogin.do");
        action = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        String result = proxy.execute();

        logger.debug("result for mapping eprLogin.do is {}", result);
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
        request.setParameter("javaScript","true");
        request.setParameter("userName", "rg");
        request.setParameter("password", "password");

        String result = login();
        // todo uncomment this line after fixing testing framework bug -always return 'ERROR' forward.
        //assertEquals("success not returned.", Action.SUCCESS, result);

        assertEquals("No Action errors.", 0, action.getActionErrors().size());

        // check for proper values in session after login
        Map session = action.getSession();
        Object obj = session.get(WebConstants.SESSION_USER_BEAN);
        assertNotNull("Session User object presence.", obj);
        User user = (User) obj;
        assertEquals("Correctness of User object", "rg", user.getUserId());

        obj = session.get(WebConstants.SESSION_USER_LANG);
        assertNotNull("Session User Local presence", obj);


        obj = session.get(WebConstants.SESSION_USER_MENUE_LIST);
        assertNotNull("Session Menu List presence", obj);
        Map menu = (Map) obj;
        assertTrue(menu.containsKey("birth"));
        //todo uncomment after change for the new data structures in Menu.java
        //assertTrue(((Map) menu.get("0birth")).containsKey(Permission.APPROVE_BDF_CONFIRMATION));
    }

    
    public void testADRLogin() throws Exception {
        request.setParameter("javaScript","true");
        request.setParameter("userName", "adr-colombo-colombo");
        request.setParameter("password", "password");
        String result = login();

        Map session = action.getSession();
        Map menu = (Map) session.get(WebConstants.SESSION_USER_MENUE_LIST);
        assertTrue(menu.containsKey("birth"));
        //todo uncomment after change for the new data structures in Menu.java
        //assertTrue(((Map) menu.get("0birth")).containsKey(Permission.APPROVE_BDF_CONFIRMATION));  // check birth confirmation approval link is there
    }

    
    public void testDEOLogin() throws Exception {
        request.setParameter("javaScript","true");
        request.setParameter("userName", "deo-gampaha-negambo");
        request.setParameter("password", "password");
        String result = login();

        Map session = action.getSession();
        Map menu = (Map) session.get(WebConstants.SESSION_USER_MENUE_LIST);
        assertTrue(menu.containsKey("birth"));
        //todo uncomment after change for the new data structures in Menu.java
        //assertTrue(((Map) menu.get("0birth")).containsKey(Permission.EDIT_BDF));             // check birth registration link is there
        //assertFalse(((Map) menu.get("0birth")).containsKey(Permission.APPROVE_BDF_CONFIRMATION));  // check birth registration approval link is not there.
    }

   
    public void testAdminLogin() throws Exception {
        request.setParameter("javaScript","true");
        request.setParameter("userName", "admin");
        request.setParameter("password", "password");
        String result = login();

        Map session = action.getSession();
        Map menu = (Map) session.get(WebConstants.SESSION_USER_MENUE_LIST);
        assertTrue(menu.containsKey("admin"));
        //todo uncomment after change for the new data structures in Menu.java
        //assertTrue(((Map) menu.get("6admin")).containsKey(Permission.USER_MANAGEMENT));        // check admin menu is there
        //assertFalse(((Map) menu.get("0birth")).containsKey(Permission.EDIT_BDF));     // check birth menu links are not there
    }

    
    public void testLogout() throws Exception {
        request.setParameter("javaScript","true");
        request.setParameter("userName", "rg");
        request.setParameter("password", "password");
        String result = login();
        Map session = action.getSession();

        ActionProxy logoutProxy = getActionProxy("/eprLogout.do");
        LoginAction action = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(session);
        result = logoutProxy.execute();
        session = action.getSession();

        assertEquals("Session User is Null : ", null, session.get(WebConstants.SESSION_USER_BEAN));
        assertEquals("No Action erros.", 0, action.getActionErrors().size());
    }

    public void testIncorrectLogins1() throws Exception {
        // with incorrect password
        request.setParameter("javaScript","true");
        request.setParameter("userName", "rg");
        request.setParameter("password", "passward");  // 'a' instead of 'o'
        String result = login();
        Map session = action.getSession();

        assertEquals("One Action error.", 1, action.getActionErrors().size());
        assertEquals("Menu list should be empty", null, session.get(WebConstants.SESSION_USER_MENUE_LIST));
    }

    public void testIncorrectLogins2() throws Exception {
        // with incorrect user name
        request.setParameter("javaScript","true");
        request.setParameter("userName", " rg");       // space before username.
        request.setParameter("password", "password");
        String result = login();
        Map session = action.getSession();

        assertEquals("One Action error.", 1, action.getActionErrors().size());
        assertEquals("Menu list should be empty", null, session.get(WebConstants.SESSION_USER_MENUE_LIST));
    }

    //todo : assert that we get landed in password change page and we can not go to any other page from here.
    public void testFirstTimeLogin() throws Exception {
        request.setParameter("userName", "firstuser");
        request.setParameter("password", "password");

        String result = login();
        assertEquals("error", result);
    }


//        assertTrue("Problem There were no errors present in fieldErrors but there should have been one error present",
//                action.getFieldErrors().size() == 1);
//		assertTrue("Problem field answer not present in fieldErrors but it should have been",
//				action.getFieldErrors().containsKey("answer") );
}


