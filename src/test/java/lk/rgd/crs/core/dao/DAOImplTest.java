package lk.rgd.crs.core.dao;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lk.rgd.AppConstants;
import lk.rgd.Permission;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.crs.api.BirthConstants;
import lk.rgd.crs.api.dao.*;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.service.BirthRegistrationService;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Tests various low level DAOs
 *
 * @author asankha
 */
public class DAOImplTest extends TestCase {

    private static final Logger logger = LoggerFactory.getLogger(DAOImplTest.class);
    private static final DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");

    private static GenericApplicationContext ctx = null;

    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(DAOImplTest.class)) {

            protected void setUp() throws Exception {
                super.setUp();
                startSpring();
            }

            private void startSpring() throws Exception {
                try {
                    ctx = new GenericApplicationContext();
                    XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
                    xmlReader.loadBeanDefinitions(new ClassPathResource("applicationContext.xml"));
                    ctx.refresh();
                } catch (Exception e) {
                    handleException("Exception starting Spring for unit-test backend", e);
                }
            }

            private void handleException(String msg, Exception e) throws Exception {
                logger.error(msg, e);
                fail(msg);
                throw e;
            }
        };
        return setup;
    }

    public void testAppParameters() throws Exception {
        AppParametersDAO bean = (AppParametersDAO) ctx.getBean("appParametersDAOImpl", AppParametersDAO.class);
        Assert.assertEquals(bean.getIntParameter(BirthConstants.CRS_BIRTH_LATE_REG_DAYS), 90);

        bean.setIntParameter("test_int", 3);
        Assert.assertEquals(bean.getIntParameter("test_int"), 3);

        bean.setIntParameter("test_int", 4);
        Assert.assertEquals(bean.getIntParameter("test_int"), 4);

        bean.setStringParameter("test_string", "hello");
        Assert.assertEquals(bean.getStringParameter("test_string"), "hello");
    }

    public void testDistrictsAndBDDivisionListsReturnedForUsers() throws Exception {
        DistrictDAO districtDAO = (DistrictDAO) ctx.getBean("districtDAOImpl", DistrictDAO.class);
        DSDivisionDAO dsDivisionDAO = (DSDivisionDAO) ctx.getBean("dsDivisionDAOImpl", DSDivisionDAO.class);
        BDDivisionDAO bdDivisionDAO = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
        UserDAO userDAO = (UserDAO) ctx.getBean("userDAOImpl", UserDAO.class);

        // RG must see all districts and all BDDivisions for a selected district
        User rg = userDAO.getUserByPK("rg");
        Map<Integer, String> districts = districtDAO.getDistrictNames(AppConstants.SINHALA, rg);
        Assert.assertTrue(districts.size() == 25);
        Map<Integer, String> dsDivisions = dsDivisionDAO.getDSDivisionNames(1, AppConstants.SINHALA, rg);
        Assert.assertTrue(dsDivisions.size() > 1);
        Map<Integer, String> bdDivisions = bdDivisionDAO.getBDDivisionNames(1, AppConstants.SINHALA, rg);
        Assert.assertTrue(bdDivisions.size() > 1);
        bdDivisions = bdDivisionDAO.getBDDivisionNames(2, AppConstants.SINHALA, rg);
        Assert.assertTrue(bdDivisions.size() > 1);
        bdDivisions = bdDivisionDAO.getBDDivisionNames(3, AppConstants.SINHALA, rg);
        Assert.assertTrue(bdDivisions.size() > 1);

        // ARG-Western Province sees all 3 districts in province
        User arg = userDAO.getUserByPK("arg-western");
        districts = districtDAO.getDistrictNames(AppConstants.SINHALA, arg);
        Assert.assertTrue(districts.size() == 3);
        dsDivisions = dsDivisionDAO.getDSDivisionNames(1, AppConstants.SINHALA, arg);
        Assert.assertTrue(dsDivisions.size() > 1);
        bdDivisions = bdDivisionDAO.getBDDivisionNames(1, AppConstants.SINHALA, arg);
        Assert.assertTrue(bdDivisions.size() > 1);
        bdDivisions = bdDivisionDAO.getBDDivisionNames(2, AppConstants.SINHALA, rg);
        Assert.assertTrue(bdDivisions.size() > 1);
        bdDivisions = bdDivisionDAO.getBDDivisionNames(3, AppConstants.SINHALA, rg);
        Assert.assertTrue(bdDivisions.size() > 1);

        // DR-colombo must see only colombo, but all BDDivisions within it
        User dr = userDAO.getUserByPK("dr-colombo");
        districts = districtDAO.getDistrictNames(AppConstants.SINHALA, dr);
        Assert.assertTrue(districts.size() == 1);
        Assert.assertTrue("11 : කොළඹ".equals(districts.values().iterator().next()));
        dsDivisions = dsDivisionDAO.getDSDivisionNames(1, AppConstants.SINHALA, dr);
        Assert.assertTrue(dsDivisions.size() > 1);
        bdDivisions = bdDivisionDAO.getBDDivisionNames(1, AppConstants.SINHALA, dr);
        Assert.assertTrue(bdDivisions.size() > 1);
        bdDivisions = bdDivisionDAO.getBDDivisionNames(2, AppConstants.SINHALA, dr);
        Assert.assertTrue(bdDivisions.size() > 1);
        bdDivisions = bdDivisionDAO.getBDDivisionNames(3, AppConstants.SINHALA, dr);
        Assert.assertTrue(bdDivisions.size() > 1);

        // ADR-colombo must see only colombo, and fort BD division
        User adr = userDAO.getUserByPK("adr-colombo-colombo");
        districts = districtDAO.getDistrictNames(AppConstants.SINHALA, adr);
        Assert.assertTrue(districts.size() == 1);
        Assert.assertTrue("11 : කොළඹ".equals(districts.values().iterator().next()));
        dsDivisions = dsDivisionDAO.getDSDivisionNames(1, AppConstants.SINHALA, adr);
        Assert.assertTrue(dsDivisions.size() == 1);
        bdDivisions = bdDivisionDAO.getBDDivisionNames(1, AppConstants.SINHALA, adr);
        Assert.assertTrue(bdDivisions.size() > 1);
    }

    public void testCountries() throws Exception {
        CountryDAO bean = (CountryDAO) ctx.getBean("countryDAOImpl", CountryDAO.class);
        Map<Integer, String> countries = bean.getCountries(AppConstants.SINHALA);
        Assert.assertTrue(countries.size() > 0);
    }

    public void testRaces() throws Exception {
        RaceDAO bean = (RaceDAO) ctx.getBean("raceDAOImpl", RaceDAO.class);
        Map<Integer, String> races = bean.getRaces(AppConstants.SINHALA);
        Assert.assertTrue(races.size() > 0);
    }

    public void testUsersAndRoles() throws Exception {
        UserManager userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);
        User user = userManager.authenticateUser("asankha", "asankha");
        Assert.assertNotNull(user);
        try {
            user = userManager.authenticateUser("asankha", "wrong");
            fail("Should fail for 'wrong' password");
        } catch (AuthorizationException e) {
        }
        try {
            user = userManager.authenticateUser("asankha", null);
            fail("Should fail for null password");
        } catch (AuthorizationException e) {
        }

        Assert.assertTrue(!userManager.getUsersByIDMatch("sank").isEmpty());
        Assert.assertTrue(userManager.getUsersByIDMatch("432").isEmpty());
        Assert.assertTrue(!userManager.getUsersByNameMatch("Perera").isEmpty());
        Assert.assertTrue(userManager.getUsersByNameMatch("2345").isEmpty());

        RoleDAO roleDAO = (RoleDAO) ctx.getBean("roleDAOImpl", RoleDAO.class);

        User rg = userManager.authenticateUser("rg", "password");
        Assert.assertTrue(rg.getRole().equals(roleDAO.getRole("RG")));
        Assert.assertFalse(rg.getRole().equals(roleDAO.getRole("ADR")));
        Assert.assertFalse(rg.getRole().equals(roleDAO.getRole("DEO")));
        Assert.assertTrue(rg.isAuthorized(Permission.APPROVE_BDF));

        User asankha = userManager.authenticateUser("asankha", "asankha");
        Assert.assertFalse(asankha.getRole().equals(roleDAO.getRole("RG")));
        Assert.assertTrue(asankha.getRole().equals(roleDAO.getRole("ADR")));
        Assert.assertFalse(asankha.getRole().equals(roleDAO.getRole("DEO")));
        Assert.assertTrue(asankha.isAuthorized(Permission.APPROVE_BDF));

        List<User> rgs = userManager.getUsersByRole("RG");
        Assert.assertEquals(1, rgs.size());

        DistrictDAO districtDAO = (DistrictDAO) ctx.getBean("districtDAOImpl", DistrictDAO.class);
        List<User> usersByAssignedBDDistrict = userManager.getUsersByAssignedBDDistrict(districtDAO.getDistrict(1));
        Assert.assertEquals(10, usersByAssignedBDDistrict.size());

        List<User> usersByRoleAndAssignedBDDistrict = userManager.getUsersByRoleAndAssignedBDDistrict(
            roleDAO.getRole("DR"), districtDAO.getDistrict(1));
        Assert.assertEquals(1, usersByRoleAndAssignedBDDistrict.size());

        List<User> usersByAssignedMRDistrict = userManager.getUsersByAssignedMRDistrict(districtDAO.getDistrict(1));
        Assert.assertEquals(9, usersByAssignedMRDistrict.size());

        List<User> usersByRoleAndAssignedMRDistrict = userManager.getUsersByRoleAndAssignedMRDistrict(
            roleDAO.getRole("DR"), districtDAO.getDistrict(1));
        Assert.assertEquals(1, usersByRoleAndAssignedMRDistrict.size());

        User admin = userManager.authenticateUser("admin", "password");
        User newUser1 = new User();
        newUser1.setUserId("newUser1");
        newUser1.setUserName("newUser1 Name");
        newUser1.setPin("1");
        newUser1.setPrefLanguage("si");
        newUser1.setRole(roleDAO.getRole("DEO"));
        newUser1.setPasswordHash(userManager.hashPassword("newUser1"));
        newUser1.setStatus(User.State.ACTIVE);
        userManager.createUser(newUser1, admin);

        User newUser2 = new User();
        newUser2.setUserId("newUser2");
        newUser2.setUserName("newUser2 Name");
        newUser2.setPin("2");
        newUser2.setPrefLanguage("si");
        newUser2.setRole(roleDAO.getRole("DEO"));
        newUser2.setPasswordHash(userManager.hashPassword("newUser2"));
        newUser2.setStatus(User.State.ACTIVE);
        userManager.createUser(newUser2, admin);

        try {
            User newUser3 = new User();
            newUser3.setUserId("newUser2");
            newUser3.setUserName("newUser3 Name");
            newUser3.setPin("2");
            newUser3.setPrefLanguage("si");
            newUser3.setRole(roleDAO.getRole("DEO"));
            newUser3.setPasswordHash(userManager.hashPassword("newUser3"));
            newUser3.setStatus(User.State.ACTIVE);
            userManager.createUser(newUser3, admin);
            fail("Should not be able to create users with duplicate IDs");
        } catch (Exception e) {
            logger.debug("Caught expected exception", e);
        }

        // try to delete user 2
        userManager.deleteUser(newUser2, admin);
        // now user2 cannot login
        try {
            userManager.authenticateUser("newUser2", "newUser2");
            fail("Deleted user allowed to login");
        } catch (Exception e) {
            // expected
        }
    }

    public void testBirthDeclaration() throws Exception {
        BirthRegistrationService birthRegistrationService = (BirthRegistrationService) ctx.getBean("manageBirthService", BirthRegistrationService.class);
        BirthDeclarationDAO birthDeclarationDAO = (BirthDeclarationDAO) ctx.getBean("birthDeclarationDAOImpl", BirthDeclarationDAO.class);
//        BDDivisionDAO bdDao = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);

        BirthDeclaration disp = birthRegistrationService.loadValuesForPrint(birthDeclarationDAO.getById(1));
        Assert.assertTrue("කොළඹ".equals(disp.getRegister().getDistrictPrint()));

        //List<BirthDeclaration> r = dao.getConfirmationPrintPending(bdDao.getBDDivision(11, 1), false);
        //Assert.assertEquals(2, r.size());
        
        //r = dao.getConfirmationPrintPending(bdDao.getBDDivision(11, 1), true);
        //Assert.assertEquals(1, r.size());

        //List<BirthDeclaration> r = dao.getConfirmationApprovalPending(bdDao.getBDDivision(11, 1), 1, 10);
//        List<BirthDeclaration> r = dao.getConfirmationApprovalPending(bdDao.getBDDivisionByPK(11), 1, 10);
//        Assert.assertEquals(10, r.size());
    }
}
