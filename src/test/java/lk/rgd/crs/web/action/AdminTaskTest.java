package lk.rgd.crs.web.action;

import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.UnitTestManager;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.MRDivisionDAO;

import java.util.Map;
import java.util.HashMap;

import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionContext;
import lk.rgd.crs.api.domain.Registrar;
import lk.rgd.crs.api.service.RegistrarManagementService;
import lk.rgd.crs.web.WebConstants;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.extensions.TestSetup;

/**
 * Created by IntelliJ IDEA.
 * User: widu
 * Date: Sep 2, 2010
 * Time: 10:45:51 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * New Author: Shan Chathuranga
 * Date: Nov 26, 2010
 *
 */
public class AdminTaskTest extends CustomStrutsTestCase {

    private static final String LOGIN_MANAGEMENT_ACTION     = "/eprLogin.do";
    private static final String USER_CREATION_ACTION_PAGE_LOAD = "/management/eprInitUserCreation.do";
    private static final String USER_CREATION_ACTION        = "/management/eprUserCreation.do";
    private static final String REGISTRAR_CREATION_ACTION_PAGE_LOAD = "/management/eprRegistrarsAdd.do";
    private static final String REGISTRAR_CREATION_ACTION = "/management/eprRegistrarsView.do";
    private static final String USER_MANAGEMENT_ACTION      = "/management/eprInitAddDivisionsAndDsDivisions.do";

    private UserManagementAction userManagementAction;
    private RegistrarManagementAction registrarManagementAction;
    private LoginAction loginAction;
    private Registrar registrar;
    private User user;


    private ActionProxy proxy;
    private static final Logger logger = LoggerFactory.getLogger(AdoptionActionTest.class);
    protected final static ApplicationContext ctx = UnitTestManager.ctx;

    protected final static RegistrarManagementService registrarMgtService = (RegistrarManagementService)ctx.getBean(
        "registrarManagmentService", RegistrarManagementService.class);
    protected final static UserManager userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);
    protected final static BDDivisionDAO bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
    protected final static DSDivisionDAO dsDivisionDAO = (DSDivisionDAO) ctx.getBean("dsDivisionDAOImpl", DSDivisionDAO.class);
    protected final static DistrictDAO districtDAO =(DistrictDAO) ctx.getBean("districtDAOImpl",DistrictDAO.class);
    protected final static MRDivisionDAO mrDivisionDAO =(MRDivisionDAO) ctx.getBean("mrDivisionDAOImpl",MRDivisionDAO.class);

    public static Test suite(){
        TestSetup setup = new TestSetup(new TestSuite(AdminTaskTest.class)) {
            protected void setUp() throws Exception {
                logger.info("[MSG] setUp() called.");
                super.setUp();
            }
            protected void tearDown() throws Exception {
                logger.debug("[MSG] tearDown() called.");
                super.tearDown();
            }
        };
        return setup;
    }

    private Map UserLogin(String username, String passwd) throws Exception {
        request.setParameter("javaScript", "true");
        request.setParameter("userName", username);
        request.setParameter("password", passwd);
        proxy = getActionProxy(LOGIN_MANAGEMENT_ACTION);
        loginAction = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        String s = proxy.execute();
        logger.debug("Loggin result {}", s);
        return loginAction.getSession();
    }

    /*public void testActionMappingProxy() {
        ActionMapping mapping = getActionMapping("/management/eprInitAddDivisionsAndDsDivisions.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/management", mapping.getNamespace());
        assertEquals("eprInitAddDivisionsAndDsDivisions", mapping.getName());
        ActionProxy proxy = getActionProxy("/management/eprInitAddDivisionsAndDsDivisions.do");
        assertNotNull(proxy);
        logger.debug("nameSpace {} and actionName {}", mapping.getNamespace(), proxy.getMethod());

        UserManagementAction action = (UserManagementAction) proxy.getAction();
        assertNotNull(action);
    }*/

    private void initAndExucute(String mapping, Map session) {
        proxy = getActionProxy(mapping);

        if(USER_MANAGEMENT_ACTION.equals(mapping)) {
            userManagementAction = (UserManagementAction) proxy.getAction();
            assertNotNull(userManagementAction);
        }else if(REGISTRAR_CREATION_ACTION_PAGE_LOAD.equals(mapping)) {
            registrarManagementAction = (RegistrarManagementAction) proxy.getAction();
            assertNotNull(registrarManagementAction);
        }else if(REGISTRAR_CREATION_ACTION.equals(mapping)) {
            registrarManagementAction = (RegistrarManagementAction) proxy.getAction();
            assertNotNull(registrarManagementAction);
        }else if(USER_CREATION_ACTION_PAGE_LOAD.equals(mapping)) {
            userManagementAction = (UserManagementAction) proxy.getAction();
            assertNotNull(userManagementAction);
        }else if(USER_CREATION_ACTION.equals(mapping)) {
            userManagementAction = (UserManagementAction) proxy.getAction();
            assertNotNull(userManagementAction);
        }
        
        logger.debug("Action Method to be executed is {} ", proxy.getMethod());
        ActionContext.getContext().setSession(session);
        try {
            proxy.execute();
        } catch (Exception e) {
            logger.error("Handle Error {} : {}", e.getMessage(), e);
        }
    }

/*    public void testAddEditDivisionTest() throws Exception {
        String result = UserLogin("admin", "password");
        Map session = loginAction.getSession();

        initAndExucute(USER_MANAGEMENT_ACTION, session);
        session = userManagementAction.getSession();
        assertEquals("Action errors for Adoption Declaration ", 0, userManagementAction.getActionErrors().size());

        assertNotNull("District list", userManagementAction.getDistrictList());
        assertNotNull("DSDivision list", userManagementAction.getDsDivisionList());

        request.setParameter("button", "ADD");
        request.setParameter("district.districtId", "11");
        request.setParameter("district.enDistrictName", "New district En");
        request.setParameter("district.siDistrictName", "New district Si");
        request.setParameter("district.taDistrictName", "New district ta");
        request.setParameter("pageNo", "1");
        initAndExucute("/management/eprInitDivisionList.do", session);
        session = userManagementAction.getSession();
        assertEquals("Action erros for Adoption Declaration ", 0, userManagementAction.getActionErrors().size());
        assertNotNull("Added new District",districtDAO.getDistrict(11));

        request.setParameter("button", "BACK");
        request.setParameter("pageNo", "0");
        initAndExucute("/management/eprAddDivisionsAndDsDivisions.do", session);
        session = userManagementAction.getSession();

        request.setParameter("userDistrictId", "26");
        request.setParameter("pageNo", "2");
        initAndExucute("/management/eprInitAddDivisionsAndDsDivisions.do", session);
        session = userManagementAction.getSession();

        request.setParameter("userDistrictId", "26");
        request.setParameter("button", "ADD");
        request.setParameter("dsDivision.divisionId", "10");
        request.setParameter("dsDivision.enDivisionName", "New ds name en");
        request.setParameter("dsDivision.siDivisionName", "New ds name si");
        request.setParameter("dsDivision.taDivisionName", "New ds name ta");
        request.setParameter("pageNo", "2");
        initAndExucute("/management/eprInitDivisionList.do", session);
        session = userManagementAction.getSession();
        assertEquals("Action erros for Adoption Declaration ", 0, userManagementAction.getActionErrors().size());
        assertNotNull("Added new dsDivision",dsDivisionDAO.getDSDivisionByPK(10)); 

        request.setParameter("button", "BACK");
        request.setParameter("pageNo", "0");
        initAndExucute("/management/eprAddDivisionsAndDsDivisions.do", session);
        session = userManagementAction.getSession();

        request.setParameter("userDistrictId", "26");
        request.setParameter("button", "Division List");
        request.setParameter("dsDivisionId", "41");
        request.setParameter("pageNo", "3");
        initAndExucute("/management/eprInitAddDivisionsAndDsDivisions.do", session);
        session = userManagementAction.getSession();

        request.setParameter("userDistrictId", "1");
        request.setParameter("bdDivision.divisionId", "10");
        request.setParameter("bdDivision.enDivisionName", "new rggistration division EN");
        request.setParameter("bdDivision.siDivisionName", "new rggistration division si");
        request.setParameter("bdDivision.taDivisionName", "new rggistration division ta");
        request.setParameter("button", "ADD");
        request.setParameter("dsDivisionId", "1");
        request.setParameter("pageNo", "3");
        initAndExucute("/management/eprInitDivisionList.do", session);
        session = userManagementAction.getSession();
        assertEquals("Action erros for Adoption Declaration ", 0, userManagementAction.getActionErrors().size());
        assertNotNull("Added new bdDivisions",bdDivisionDAO.getBDDivisionByPK(10)); 

        request.setParameter("button", "BACK");
        request.setParameter("pageNo", "0");
        initAndExucute("/management/eprAddDivisionsAndDsDivisions.do", session);
        session = userManagementAction.getSession();

        request.setParameter("userDistrictId", "26");
        request.setParameter("button", "MRDivision List");
        request.setParameter("dsDivisionId", "41");
        request.setParameter("pageNo", "4");
        initAndExucute("/management/eprInitAddDivisionsAndDsDivisions.do", session);
        session = userManagementAction.getSession();

        request.setParameter("userDistrictId", "1");
        request.setParameter("button", "ADD");
        request.setParameter("dsDivisionId", "1");
        request.setParameter("mrDivision.divisionId", "5");
        request.setParameter("mrDivision.enDivisionName", "new MD en");
        request.setParameter("mrDivision.siDivisionName", "new MD si");
        request.setParameter("mrDivision.taDivisionName", "new MD TA");
        request.setParameter("pageNo", "4");

        initAndExucute("/management/eprInitDivisionList.do", session);
        session = userManagementAction.getSession();
        assertEquals("Action erros for Adoption Declaration ", 0, userManagementAction.getActionErrors().size());
        //assertNotNull("Added new mr division",mrDivisionDAO.getMRDivisionByPK(5));

        request.setParameter("button", "BACK");
        request.setParameter("pageNo", "0");
        initAndExucute("/management/eprAddDivisionsAndDsDivisions.do", session);
        session = userManagementAction.getSession();
        assertEquals("Action erros for Adoption Declaration ", 0, userManagementAction.getActionErrors().size());

    }*/

    public void testCreateUserTest() throws Exception {

        Map session= UserLogin("admin", "password");
        initAndExucute(USER_CREATION_ACTION_PAGE_LOAD, session);
        session = userManagementAction.getSession();

        request.setParameter("assignedDistricts", "1");
        request.setParameter("assignedDivisions", "1");
        request.setParameter("roleId", "ADR");
        request.setParameter("user.pin", "300");
        request.setParameter("user.prefLanguage", "si");
        request.setParameter("user.userId", "Malith");
        request.setParameter("user.userName", "Malith");

        initAndExucute(USER_CREATION_ACTION, session);
        user = userManagementAction.getUser();
        Map ss = userManagementAction.getSession();
        
        assertEquals("Action errors for Adoption Declaration ", 0, userManagementAction.getActionErrors().size());
        assertNotNull("User name null",user.getUserName());
        assertNotNull("User prefLanguage null",user.getPrefLanguage());
        assertNotNull("AssignedBDDSDivisions null",userManagementAction.getSession());
        assertNotNull("AssignedBDDistricts null",user.getAssignedBDDistricts());
        assertNotNull("User role null",user.getRole());

    }

    public void testAddRegistrarTest() throws Exception {

        Map session = UserLogin("admin", "password");
        initAndExucute(REGISTRAR_CREATION_ACTION_PAGE_LOAD, session);
        session = registrarManagementAction.getSession();

        request.setParameter("registrar.fullNameInOfficialLanguage","nissan");
        request.setParameter("registrar.fullNameInEnglishLanguage", "nissan");
        request.setParameter("registrar.pin", "2314");
        request.setParameter("registrar.nic", "863357578v");
        request.setParameter("registrar.gender", "0");
        request.setParameter("registrar.dateOfBirth", "1986-12-01");
        request.setParameter("registrar.currentAddress", "aaaaa");
        request.setParameter("registrar.phoneNo", "0773922222");
        request.setParameter("registrar.emailAddress", "dskdks@kdf.cdsj");
        request.setParameter("registrar.preferredLanguage", "si");

        initAndExucute(REGISTRAR_CREATION_ACTION, session);

        registrar = registrarManagementAction.getRegistrar();

        assertEquals("Action errors for Adoption Declaration ", 0, registrarManagementAction.getActionErrors().size());
        assertNotNull("Address NULL",registrar.getCurrentAddress());
        assertNotNull("Name NULL", registrar.getFullNameInOfficialLanguage());
        assertNotNull("En Name NULL", registrar.getFullNameInEnglishLanguage());
        assertNotNull("PIN NULL", registrar.getPin());
        assertNotNull("EXISTING REGISTRAR NOT NULL", session.get(WebConstants.SESSION_EXSISTING_REGISTRAR));

    }
    
}
