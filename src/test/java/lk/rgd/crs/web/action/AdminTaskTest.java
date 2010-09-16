package lk.rgd.crs.web.action;

import lk.rgd.common.CustomStrutsTestCase;
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
public class AdminTaskTest extends CustomStrutsTestCase {
    private UserManagmentAction userManagementAction;
    private ActionProxy proxy;
    private static final Logger logger = LoggerFactory.getLogger(AdoptionActionTest.class);
    protected final static ApplicationContext ctx = UnitTestManager.ctx;
    protected final static UserManager userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);
    private final static BDDivisionDAO bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
    private final static DSDivisionDAO dsDivisionDAO = (DSDivisionDAO) ctx.getBean("dsDivisionDAOImpl", DSDivisionDAO.class);
    private final static DistrictDAO districtDAO =(DistrictDAO) ctx.getBean("districtDAOImpl",DistrictDAO.class);
    private final static MRDivisionDAO mrDivisionDAO =(MRDivisionDAO) ctx.getBean("mrDivisionDAOImpl",MRDivisionDAO.class);

    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(SampleTest.class)) {
            protected void setUp() throws Exception {
                logger.debug("=> show this once for class - setup");
                super.setUp();
            }

            protected void tearDown() throws Exception {
                logger.debug("=> show this once for class - tear");
                super.tearDown();
            }
        };
        return setup;
    }

    private Map UserLogin(String username, String passwd) throws Exception {
        request.setParameter("userName", username);
        request.setParameter("password", passwd);
        ActionProxy proxy = getActionProxy("/eprLogin.do");
        LoginAction loginAction = (LoginAction) proxy.getAction();
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        proxy.execute();
        return loginAction.getSession();
    }

    public void testActionMappingProxy() {
        ActionMapping mapping = getActionMapping("/management/eprInitAddDivisionsAndDsDivisions.do");
        assertNotNull("Mapping not null {}", mapping);
        assertEquals("/management", mapping.getNamespace());
        assertEquals("eprInitAddDivisionsAndDsDivisions", mapping.getName());
        ActionProxy proxy = getActionProxy("/management/eprInitAddDivisionsAndDsDivisions.do");
        assertNotNull(proxy);
        logger.debug("nameSpace {} and actionName {}", mapping.getNamespace(), proxy.getMethod());

        UserManagmentAction action = (UserManagmentAction) proxy.getAction();
        assertNotNull(action);
    }

    private void initAndExucute(String mapping, Map session) {
        proxy = getActionProxy(mapping);
        userManagementAction = (UserManagmentAction) proxy.getAction();
        assertNotNull(userManagementAction);
        logger.debug("Action Method to be executed is {} ", proxy.getMethod());
        ActionContext.getContext().setSession(session);
        try {
            proxy.execute();
        } catch (Exception e) {
            logger.error("Handle Error {} : {}", e.getMessage(), e);
        }
    }

    public void testAddEditDivisionTest() throws Exception {
        Map session = UserLogin("admin", "password");

        initAndExucute("/management/eprInitAddDivisionsAndDsDivisions.do", session);
        session = userManagementAction.getSession();
        assertEquals("Action erros for Adoption Declaration ", 0, userManagementAction.getActionErrors().size());

        assertNotNull("District list", userManagementAction.getDistrictList());
        assertNotNull("Dsdivision list", userManagementAction.getDsDivisionList());

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

        request.setParameter("UserDistrictId", "26");
        request.setParameter("pageNo", "2");
        initAndExucute("/management/eprInitAddDivisionsAndDsDivisions.do", session);
        session = userManagementAction.getSession();

        request.setParameter("UserDistrictId", "26");
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

        request.setParameter("UserDistrictId", "26");
        request.setParameter("button", "Division List");
        request.setParameter("dsDivisionId", "41");
        request.setParameter("pageNo", "3");
        initAndExucute("/management/eprInitAddDivisionsAndDsDivisions.do", session);
        session = userManagementAction.getSession();

        request.setParameter("UserDistrictId", "1");
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

        request.setParameter("UserDistrictId", "26");
        request.setParameter("button", "MRDivision List");
        request.setParameter("dsDivisionId", "41");
        request.setParameter("pageNo", "4");
        initAndExucute("/management/eprInitAddDivisionsAndDsDivisions.do", session);
        session = userManagementAction.getSession();

        request.setParameter("UserDistrictId", "1");
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

    }

    public void testCreateUserTest() throws Exception {
        Map session = UserLogin("admin", "password");

        request.setParameter("assignedDistricts", "1");
        request.setParameter("assignedDivisions", "1");
        request.setParameter("roleId", "ADR");
        request.setParameter("user.pin", "300");
        request.setParameter("user.prefLanguage", "si");
        request.setParameter("user.userId", "Malith");
        request.setParameter("user.userName", "Malith");
        initAndExucute("/management/eprInitUserCreation.do", session);
        session = userManagementAction.getSession();
        assertEquals("Action erros for Adoption Declaration ", 0, userManagementAction.getActionErrors().size());
        assertNotNull("Added new user",userManager.getUsersByID("Malith"));
        assertNotNull("Added new user",userManager.getUsersByNameMatch("Malith"));

    }
}
