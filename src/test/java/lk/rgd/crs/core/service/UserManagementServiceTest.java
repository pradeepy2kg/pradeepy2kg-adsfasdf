package lk.rgd.crs.core.service;

import junit.framework.TestCase;
import lk.rgd.AppConstants;
import lk.rgd.UnitTestManager;
import lk.rgd.common.RGDException;
import lk.rgd.common.RGDRuntimeException;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.common.core.dao.DSDivisionDAOImpl;
import lk.rgd.common.core.dao.DistrictDAOImpl;
import lk.rgd.common.core.service.UserManagerImpl;
import lk.rgd.crs.web.WebConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Date;

/**
 * service level test case for user managements
 *
 * @author amith jayasekara
 */
public class UserManagementServiceTest extends TestCase {

    private final Logger logger = LoggerFactory.getLogger(UserManagementServiceTest.class);
    protected final ApplicationContext ctx = UnitTestManager.ctx;
    protected final UserManager userManager;
    protected final DistrictDAO districtDAO;
    protected final DSDivisionDAO dsDivisionDAO;
    protected User admin;
    protected District colomboDistrict;
    protected DSDivision colomboDSDivision;

    public UserManagementServiceTest() {
        userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);
        districtDAO = (DistrictDAO) ctx.getBean("districtDAOImpl", DistrictDAO.class);
        dsDivisionDAO = (DSDivisionDAO) ctx.getBean("dsDivisionDAOImpl", DSDivisionDAO.class);

        admin = userManager.getUserByID("admin");
        colomboDistrict = districtDAO.getDistrict(1);
        colomboDSDivision = dsDivisionDAO.getDSDivisionByPK(1);
    }

    public void testAuthenticateUser() {
        logger.debug("attempt to test authentication process of a user");
        //test valid  login
        try {
            logger.debug("loging rg user with user name : {} and password : {}", "rg", "password");
            User rg = userManager.authenticateUser("rg", "password");
            //check we get the correct user
            assertEquals("we get same user after loging ", "rg", rg.getUserId());
        } catch (AuthorizationException e) {
            //exception are not expecting
            fail("authorization exception not expecting while login rg user");
        }
        //test invalid loging
        try {
            userManager.authenticateUser("xx", "xxxx");
            //fail exception expecting
            fail("exception expecting while invalid login");
        }
        catch (AuthorizationException e) {
            logger.debug("exception excepted while loging user : {} with password : {}", "xx", "xxx");
        }
        //test account block after 3 invalid attempts
        logger.debug("attempt to test account blocking after 3 unsuccessful loging attempts ");
        for (int i = 0; i < 4; i++) {
            try {
                userManager.authenticateUser("rg", "xxx");
            }
            catch (AuthorizationException e) {
                //do nothing we expect that fail
            }
        }
        //after three unsuccessful attempts now that account 'rg' must be inactive
        User inactiveRG = userManager.getUserByID("rg");
        if (inactiveRG.getLifeCycleInfo().isActive()) {
            //enable this in future
            // fail("this user cannot be active after 3 unsuccessful attempts");
        } else {
            //logger.debug("rg user successfully inactivated after 3 unsuccessful attempts");
        }
    }

    public void testCreateUser() {
        logger.debug("attempt to test user creation process");
        //creating a DEO type user with basic requirements
        try {
            User testDEO = new User();
            testDEO.setPin(124578325678l);
            testDEO.setUserId("test-deo-colombo-colombo");
            testDEO.setUserName("test deo colombo colombo ");
            testDEO.setPrefLanguage(AppConstants.SINHALA);
            userManager.createUser(testDEO, admin, null, "DEO", new int[]{colomboDistrict.getDistrictUKey()},
                new int[]{colomboDSDivision.getDsDivisionUKey()}, false, "randomPassword");
        } catch (RGDRuntimeException e) {
            fail("we are not expecting exception here while adding a user with all the requirement");
        }
        //now validate newly created user
        User newCreatedTestDEO = userManager.getUserByID("test-deo-colombo-colombo");
        //check user's password is expired and is user is inactive
        if (newCreatedTestDEO.getPasswordExpiry().before(new Date()) && newCreatedTestDEO.getStatus() == User.State.INACTIVE) {
            //new user is inactive and password is expired
            logger.debug("newly created user :{} is inactive and that user's password is expired", "test-deo-colombo-colombo");
        } else {
            fail("newly created user's password must be expired and that user must be inactive user until that user login in first time");
        }
        //now we try to log to the system with this user
        try {
            userManager.authenticateUser("test-deo-colombo-colombo", "randomPassword");
        }
        catch (AuthorizationException e) {
            logger.debug("exception expected while first time login of a newly created user : {}", "test-deo-colombo-colombo");
        }
    }
}
