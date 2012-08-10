package lk.rgd.crs.web.action;

import lk.rgd.common.CustomStrutsTestCase;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.UnitTestManager;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.MRDivisionDAO;

import java.util.Iterator;
import java.util.List;
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
 * @author: Shan Chathuranga
 * Date: Nov 26, 2010
 */
public class AdminTaskTest extends CustomStrutsTestCase {

    private static final String LOGIN_MANAGEMENT_ACTION = "/eprLogin.do";
    private static final String USER_CREATION_ACTION_PAGE_LOAD = "/management/eprInitUserCreation.do";
    private static final String USER_CREATION_ACTION = "/management/eprUserCreation.do";
    private static final String REGISTRAR_CREATION_ACTION_PAGE_LOAD = "/management/eprRegistrarsAdd.do";
    private static final String REGISTRAR_CREATION_ACTION = "/management/eprRegistrarsView.do";
    private static final String ADD_DIVISIONS_AND_DS_DIVISIONS_PAGE_LOAD = "/management/eprInitAddDivisionsAndDsDivisions.do";
    private static final String ADD_DIVISIONS_ACTION = "/management/eprInitDivisionList.do";
    private static final String SEARCH_USERS_PAGE_LOAD = "/management/eprViewUsers.do";
    private static final String SEARCH_USERS_ACTION = "/management/eprViewSelectedUsers.do";
    private static final String MANAGE_ASSIGNMENT_PAGE_LOAD = "/management/eprRegistrarsManagment.do";
    private static final String MANAGE_ASSIGNMENT_ACTION = "/management/eprRegistrarsFilter.do";
    private static final String ADD_LOCATIONS_TO_NEW_USER = "/management/eprInitAssignedUserLocation.do";
    private static final String SEARCH_REGISTRAR_PAGE_LOAD = "/management/eprFindRegistrar.do";
    private static final String SEARCH_REGISTRAR_ACTION = "/management/eprFindRegistrar.do";
    private static final String VIEW_EVENTS_PAGE_LOAD = "/management/eprInitEventsManagement.do";
    private static final String VIEW_EVENTS_ACTION = "/management/eprFilterEventsList.do";
    private static final String USER_PREFERENCES_PAGE_LOAD = "/preferences/eprUserPreferencesInit.do";
    private static final String USER_PREFERENCES_ACTION = "/preferences/eprUserPreferencesAction.do";
    private static final String CHANGE_PASSWORD_PAGE_LOAD = "/preferences/eprpassChangePageLoad.do";
    private static final String CHANGE_PASSWORD_ACTION = "/preferences/eprChangePass.do";

    private UserManagementAction userManagementAction;
    private RegistrarManagementAction registrarManagementAction;
    private EventsViewerAction eventViewerAction;
    private UserPreferencesAction userPreferencesAction;
    private LoginAction loginAction;
    private Registrar registrar;
    private User user;


    private ActionProxy proxy;
    private static final Logger logger = LoggerFactory.getLogger(AdminTaskTest.class);
    protected final static ApplicationContext ctx = UnitTestManager.ctx;

    protected final static RegistrarManagementService registrarMgtService = (RegistrarManagementService) ctx.getBean(
        "registrarManagementService", RegistrarManagementService.class);
    protected final static UserManager userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);
    protected final static BDDivisionDAO bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
    protected final static DSDivisionDAO dsDivisionDAO = (DSDivisionDAO) ctx.getBean("dsDivisionDAOImpl", DSDivisionDAO.class);
    protected final static DistrictDAO districtDAO = (DistrictDAO) ctx.getBean("districtDAOImpl", DistrictDAO.class);
    protected final static MRDivisionDAO mrDivisionDAO = (MRDivisionDAO) ctx.getBean("mrDivisionDAOImpl", MRDivisionDAO.class);

    public static Test suite() {
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

    private void initAndExecute(String mapping, Map session) {
        proxy = getActionProxy(mapping);

        if (ADD_DIVISIONS_AND_DS_DIVISIONS_PAGE_LOAD.equals(mapping)) {
            userManagementAction = (UserManagementAction) proxy.getAction();
            assertNotNull(userManagementAction);
        } else if (REGISTRAR_CREATION_ACTION_PAGE_LOAD.equals(mapping)) {
            registrarManagementAction = (RegistrarManagementAction) proxy.getAction();
            assertNotNull(registrarManagementAction);
        } else if (REGISTRAR_CREATION_ACTION.equals(mapping)) {
            registrarManagementAction = (RegistrarManagementAction) proxy.getAction();
            assertNotNull(registrarManagementAction);
        } else if (USER_CREATION_ACTION_PAGE_LOAD.equals(mapping)) {
            userManagementAction = (UserManagementAction) proxy.getAction();
            assertNotNull(userManagementAction);
        } else if (USER_CREATION_ACTION.equals(mapping)) {
            userManagementAction = (UserManagementAction) proxy.getAction();
            assertNotNull(userManagementAction);
        } else if (ADD_DIVISIONS_ACTION.equals(mapping)) {
            userManagementAction = (UserManagementAction) proxy.getAction();
            assertNotNull(userManagementAction);
        } else if (SEARCH_USERS_PAGE_LOAD.equals(mapping)) {
            userManagementAction = (UserManagementAction) proxy.getAction();
            assertNotNull(userManagementAction);
        } else if (SEARCH_USERS_ACTION.equals(mapping)) {
            userManagementAction = (UserManagementAction) proxy.getAction();
            assertNotNull(userManagementAction);
        } else if (MANAGE_ASSIGNMENT_PAGE_LOAD.equals(mapping)) {
            registrarManagementAction = (RegistrarManagementAction) proxy.getAction();
            assertNotNull(registrarManagementAction);
        } else if (MANAGE_ASSIGNMENT_ACTION.equals(mapping)) {
            registrarManagementAction = (RegistrarManagementAction) proxy.getAction();
            assertNotNull(registrarManagementAction);
        } else if (ADD_LOCATIONS_TO_NEW_USER.equals(mapping)) {
            userManagementAction = (UserManagementAction) proxy.getAction();
            assertNotNull(userManagementAction);
        } else if (SEARCH_REGISTRAR_PAGE_LOAD.equals(mapping)) {
            registrarManagementAction = (RegistrarManagementAction) proxy.getAction();
            assertNotNull(registrarManagementAction);
        } else if (SEARCH_REGISTRAR_ACTION.equals(mapping)) {
            registrarManagementAction = (RegistrarManagementAction) proxy.getAction();
            assertNotNull(registrarManagementAction);
        } else if (VIEW_EVENTS_PAGE_LOAD.equals(mapping)) {
            eventViewerAction = (EventsViewerAction) proxy.getAction();
            assertNotNull(eventViewerAction);
        } else if (VIEW_EVENTS_ACTION.equals(mapping)) {
            eventViewerAction = (EventsViewerAction) proxy.getAction();
            assertNotNull(eventViewerAction);
        } else if (USER_PREFERENCES_PAGE_LOAD.equals(mapping)) {
            userPreferencesAction = (UserPreferencesAction) proxy.getAction();
            assertNotNull(userPreferencesAction);
        } else if (USER_PREFERENCES_ACTION.equals(mapping)) {
            userPreferencesAction = (UserPreferencesAction) proxy.getAction();
            assertNotNull(userPreferencesAction);
        } else if (CHANGE_PASSWORD_PAGE_LOAD.equals(mapping)) {
            userPreferencesAction = (UserPreferencesAction) proxy.getAction();
            assertNotNull(userPreferencesAction);
        } else if (CHANGE_PASSWORD_ACTION.equals(mapping)) {
            userPreferencesAction = (UserPreferencesAction) proxy.getAction();
            assertNotNull(userPreferencesAction);
        }

        logger.debug("Action Method to be executed is {} ", proxy.getMethod());
        ActionContext.getContext().setSession(session);
        try {
            proxy.execute();
        } catch (Exception e) {
            logger.error("This Error cant avoid ! {} : {}", e.getMessage(), e);
        }
    }

    public void testAddEditDivisionTest() throws Exception {
        Map session = UserLogin("admin", "password");

        initAndExecute(ADD_DIVISIONS_AND_DS_DIVISIONS_PAGE_LOAD, session);
        session = userManagementAction.getSession();
        assertEquals("Action errors for AddEditDivisionTest", 0, userManagementAction.getActionErrors().size());

        assertNotNull("District list", userManagementAction.getDistrictList());
        assertNotNull("DSDivision list", userManagementAction.getDsDivisionList());

        request.setParameter("button", "ADD");
        request.setParameter("district.districtId", "11");
        request.setParameter("district.enDistrictName", "New district En");
        request.setParameter("district.siDistrictName", "New district Si");
        request.setParameter("district.taDistrictName", "New district ta");
        request.setParameter("pageNo", "1");

        initAndExecute(ADD_DIVISIONS_ACTION, session);
        session = userManagementAction.getSession();
        assertEquals("Action errors for AddEditDivisionTest", 0, userManagementAction.getActionErrors().size());
        assertNotNull("Added new District", districtDAO.getDistrict(11));

        request.setParameter("button", "BACK");
        request.setParameter("pageNo", "0");

        initAndExecute(ADD_DIVISIONS_AND_DS_DIVISIONS_PAGE_LOAD, session);
        session = userManagementAction.getSession();

        request.setParameter("userDistrictId", "26");
        request.setParameter("pageNo", "2");

        initAndExecute(ADD_DIVISIONS_ACTION, session);
        session = userManagementAction.getSession();

        request.setParameter("userDistrictId", "26");
        request.setParameter("button", "ADD");
        request.setParameter("dsDivision.divisionId", "10");
        request.setParameter("dsDivision.enDivisionName", "New ds name en");
        request.setParameter("dsDivision.siDivisionName", "New ds name si");
        request.setParameter("dsDivision.taDivisionName", "New ds name ta");
        request.setParameter("pageNo", "2");

        initAndExecute(ADD_DIVISIONS_ACTION, session);
        session = userManagementAction.getSession();
        assertEquals("Action errors for AddEditDivisionTest", 0, userManagementAction.getActionErrors().size());
        assertNotNull("Added new dsDivision", dsDivisionDAO.getDSDivisionByPK(10));

        request.setParameter("button", "BACK");
        request.setParameter("pageNo", "0");

        initAndExecute(ADD_DIVISIONS_AND_DS_DIVISIONS_PAGE_LOAD, session);
        session = userManagementAction.getSession();

        request.setParameter("userDistrictId", "26");
        request.setParameter("button", "Division List");
        request.setParameter("dsDivisionId", "41");
        request.setParameter("pageNo", "3");

        initAndExecute(ADD_DIVISIONS_AND_DS_DIVISIONS_PAGE_LOAD, session);
        session = userManagementAction.getSession();

        request.setParameter("userDistrictId", "1");
        request.setParameter("bdDivision.divisionId", "10");
        request.setParameter("bdDivision.enDivisionName", "new registration division EN");
        request.setParameter("bdDivision.siDivisionName", "new registration division si");
        request.setParameter("bdDivision.taDivisionName", "new registration division ta");
        request.setParameter("button", "ADD");
        request.setParameter("dsDivisionId", "1");
        request.setParameter("pageNo", "3");

        initAndExecute(ADD_DIVISIONS_ACTION, session);
        session = userManagementAction.getSession();
        assertEquals("Action errors for AddEditDivisionTest", 0, userManagementAction.getActionErrors().size());
        assertNotNull("Added new bdDivisions", bdDivisionDAO.getBDDivisionByPK(10));

        request.setParameter("button", "BACK");
        request.setParameter("pageNo", "0");

        initAndExecute(ADD_DIVISIONS_AND_DS_DIVISIONS_PAGE_LOAD, session);
        session = userManagementAction.getSession();

        request.setParameter("userDistrictId", "26");
        request.setParameter("button", "MRDivision List");
        request.setParameter("dsDivisionId", "41");
        request.setParameter("pageNo", "4");

        initAndExecute(ADD_DIVISIONS_AND_DS_DIVISIONS_PAGE_LOAD, session);
        session = userManagementAction.getSession();

        request.setParameter("userDistrictId", "1");
        request.setParameter("button", "ADD");
        request.setParameter("dsDivisionId", "1");
        request.setParameter("mrDivision.divisionId", "5");
        request.setParameter("mrDivision.enDivisionName", "new MD en");
        request.setParameter("mrDivision.siDivisionName", "new MD si");
        request.setParameter("mrDivision.taDivisionName", "new MD TA");
        request.setParameter("pageNo", "4");

        initAndExecute(ADD_DIVISIONS_ACTION, session);
        session = userManagementAction.getSession();
        assertEquals("Action errors for AddEditDivisionTest", 0, userManagementAction.getActionErrors().size());
        //assertNotNull("Added new mr division",mrDivisionDAO.getMRDivisionByPK(5));

        request.setParameter("button", "BACK");
        request.setParameter("pageNo", "0");
        initAndExecute(ADD_DIVISIONS_AND_DS_DIVISIONS_PAGE_LOAD, session);
        session = userManagementAction.getSession();
        assertEquals("Action errors for AddEditDivisionTest", 0, userManagementAction.getActionErrors().size());

    }

    public void testCreateUserTest() throws Exception {

        Map session = UserLogin("admin", "password");
        initAndExecute(USER_CREATION_ACTION_PAGE_LOAD, session);
        session = userManagementAction.getSession();
        assertEquals("Action errors for Create User Action", 0, userManagementAction.getActionErrors().size());

        String uID = "Malith";
        request.setParameter("assignedDistricts", "1");
        request.setParameter("assignedDivisions", "1");
        request.setParameter("roleId", "ADR");
        request.setParameter("user.pin", "300");
        request.setParameter("user.prefLanguage", "si");
        request.setParameter("user.userId", uID);
        request.setParameter("user.userName", "Malith");

        initAndExecute(USER_CREATION_ACTION, session);
        user = userManagementAction.getUser();
        assertEquals("Action errors for Create User Action", 0, userManagementAction.getActionErrors().size());

        assertNotNull("User name null", user.getUserName());
        assertNotNull("User prefLanguage null", user.getPrefLanguage());
        assertNotNull("AssignedBDDSDivisions null", userManagementAction.getSession());
        assertNotNull("AssignedBDDistricts null", user.getAssignedBDDistricts());
        assertNotNull("User role null", user.getRole());

        addLocationsToNewUser(session, uID);

    }

    public void testAddRegistrarTest() throws Exception {

        Map session = UserLogin("admin", "password");
        initAndExecute(REGISTRAR_CREATION_ACTION_PAGE_LOAD, session);
        session = registrarManagementAction.getSession();
        assertEquals("Action errors for 1 of 4BDF", 0, registrarManagementAction.getActionErrors().size());

        request.setParameter("registrar.fullNameInOfficialLanguage", "nissan");
        request.setParameter("registrar.fullNameInEnglishLanguage", "nissan");
        request.setParameter("registrar.pin", "2314");
        request.setParameter("registrar.nic", "863357578v");
        request.setParameter("registrar.gender", "0");
        request.setParameter("registrar.dateOfBirth", "1986-12-01");
        request.setParameter("registrar.currentAddress", "aaaaa");
        request.setParameter("registrar.phoneNo", "0773922222");
        request.setParameter("registrar.emailAddress", "dskdks@kdf.cdsj");
        request.setParameter("registrar.preferredLanguage", "si");

        initAndExecute(REGISTRAR_CREATION_ACTION, session);
        registrar = registrarManagementAction.getRegistrar();
        assertEquals("Action errors for Add Registration Action ", 0, registrarManagementAction.getActionErrors().size());

        assertNotNull("Address NULL", registrar.getCurrentAddress());
        assertNotNull("Name NULL", registrar.getFullNameInOfficialLanguage());
        assertNotNull("En Name NULL", registrar.getFullNameInEnglishLanguage());
        assertNotNull("PIN NULL", registrar.getPin());
        assertNotNull("EXISTING REGISTRAR NOT NULL", session.get(WebConstants.SESSION_EXSISTING_REGISTRAR));

    }

    public void testSearchUsers() throws Exception {

        Map session = UserLogin("admin", "password");
        initAndExecute(SEARCH_USERS_PAGE_LOAD, session);
        session = userManagementAction.getSession();
        assertEquals("Action errors for Search Users page load", 0, userManagementAction.getActionErrors().size());

        request.setParameter("roleId", "1");
        request.setParameter("nameOfUser", "shan");
        request.setParameter("userDistrictId", " 1");

        initAndExecute(SEARCH_USERS_ACTION, session);
        session = userManagementAction.getSession();
        assertEquals("Action errors for Search Users Action", 0, userManagementAction.getActionErrors().size());
        //assertNotNull("Search List null", session.get(WebConstants.SEARCH_USERS_LIST));

        List<User> ul = (List<User>) session.get(WebConstants.SEARCH_USERS_LIST);
        Iterator<User> it = ul.iterator();

        while (it.hasNext()) {
            addLocationsToNewUser(session, it.next().getUserId());
            break;
        }

    }

    public void testManageAssignments() throws Exception {

        Map session = UserLogin("admin", "password");
        initAndExecute(MANAGE_ASSIGNMENT_PAGE_LOAD, session);
        session = registrarManagementAction.getSession();
        assertEquals("Errors while Manage Assignments page loading", 0, registrarManagementAction.getActionErrors().size());

        request.setParameter("districtId", "1");
        request.setParameter("dsDivisionId", "1");
        request.setParameter("assignmentState", "1");
        request.setParameter("assignmentType", "1");

        initAndExecute(MANAGE_ASSIGNMENT_ACTION, session);
        assertEquals("Errors for Manage Assignments action", 0, registrarManagementAction.getActionErrors().size());

    }

    public void addLocationsToNewUser(Map session, String uID) {

        request.setParameter("userId", uID);
        request.setParameter("newUser", "true");

        initAndExecute(ADD_LOCATIONS_TO_NEW_USER, session);
        assertEquals("Action errors for Create User Action", 0, userManagementAction.getActionErrors().size());
        assertNotNull("userId null", userManagementAction.getUserId());
        assertNotNull("", userManagementAction.getUserLocationNameList());

    }

    public void testSearchRegistrarsByName() throws Exception {

        Map session = UserLogin("admin", "password");
        initAndExecute(MANAGE_ASSIGNMENT_PAGE_LOAD, session);
        session = registrarManagementAction.getSession();
        assertEquals("Errors while Search Registrar By Name page loading", 0, registrarManagementAction.getActionErrors().size());

        request.setParameter("registrarName", "shan");
        initAndExecute(MANAGE_ASSIGNMENT_ACTION, session);
        session = registrarManagementAction.getSession();
        assertEquals("Action errors for Search Registrar", 0, registrarManagementAction.getActionErrors().size());

    }

    public void testSearchRegistrarsByPin() throws Exception {

        Map session = UserLogin("admin", "password");
        initAndExecute(MANAGE_ASSIGNMENT_PAGE_LOAD, session);
        session = registrarManagementAction.getSession();
        assertEquals("Errors while Search Registrar By Pin page loading", 0, registrarManagementAction.getActionErrors().size());

        request.setParameter("registrarPin", "1010101010");
        initAndExecute(MANAGE_ASSIGNMENT_ACTION, session);
        session = registrarManagementAction.getSession();
        assertEquals("Action errors for Search Registrar", 0, registrarManagementAction.getActionErrors().size());

    }

    public void testViewEvents() throws Exception {

        Map session = UserLogin("admin", "password");
        initAndExecute(VIEW_EVENTS_PAGE_LOAD, session);
        assertEquals("Errors while View Events page loading", 0, eventViewerAction.getActionErrors().size());

        request.setParameter("searchStartDate", "");
        request.setParameter("searchEndDate", "");
        request.setParameter("startTime", "");
        request.setParameter("endTime", "");

        initAndExecute(VIEW_EVENTS_ACTION, session);
        assertEquals("Errors while page loading", 0, eventViewerAction.getActionErrors().size());

    }

    public void testUserPreferences() throws Exception {

        Map session = UserLogin("admin", "password");
        initAndExecute(USER_PREFERENCES_PAGE_LOAD, session);
        session = userPreferencesAction.getSession();
        assertEquals("Errors while View Events page loading", 0, userPreferencesAction.getActionErrors().size());

        request.setParameter("prefLanguage", "en");
        initAndExecute(USER_PREFERENCES_ACTION, session);
        session = userPreferencesAction.getSession();

        assertNotNull("User Preferred Language null", session.get(WebConstants.SESSION_USER_LANG));
        assertNotNull("Form's Preferred Language null", userPreferencesAction.getPrefLanguage());
        assertNotNull("Updated User bean null", session.get(WebConstants.SESSION_USER_BEAN));
        
    }

    public void testChangePassword() throws Exception {

        Map session = UserLogin("admin", "password");
        initAndExecute(CHANGE_PASSWORD_PAGE_LOAD, session);
        session = userPreferencesAction.getSession();
        assertEquals("Errors while Change Password page loading", 0, userPreferencesAction.getActionErrors().size());

        request.setParameter("existingPassword","password");
        request.setParameter("newPassword","Abcd@123");
        request.setParameter("retypeNewPassword","Abcd@123");

        initAndExecute(CHANGE_PASSWORD_ACTION, session);
        session = userPreferencesAction.getSession();
        assertEquals("Errors for Change password action", 0, userPreferencesAction.getActionErrors().size());

    }

}
