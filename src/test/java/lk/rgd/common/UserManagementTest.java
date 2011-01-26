package lk.rgd.common;

import junit.framework.TestCase;
import lk.rgd.Permission;
import lk.rgd.UnitTestManager;
import lk.rgd.common.api.dao.*;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.UserLocation;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.core.AuthorizationException;
import lk.rgd.crs.api.service.MasterDataManagementService;
import org.junit.Assert;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * @author asankha
 */
public class UserManagementTest extends TestCase {

    private final ApplicationContext ctx = UnitTestManager.ctx;

    public void testUsersAndRoles() throws Exception {
        UserManager userManager = (UserManager) ctx.getBean("userManagerService", UserManager.class);
        LocationDAO locationDAO = (LocationDAO) ctx.getBean("locationDAOImpl", LocationDAO.class);
        MasterDataManagementService masterDataManagementService =
            (MasterDataManagementService) ctx.getBean("masterDataManagementService", MasterDataManagementService.class);

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
        //ADR cannot approve ADOPTION
        Assert.assertFalse(asankha.isAuthorized(Permission.APPROVE_ADOPTION));

        List<User> rgs = userManager.getUsersByRole("RG");
        Assert.assertEquals(2, rgs.size());

        DistrictDAO districtDAO = (DistrictDAO) ctx.getBean("districtDAOImpl", DistrictDAO.class);
        List<User> usersByAssignedBDDistrict = userManager.getUsersByAssignedBDDistrict(districtDAO.getDistrict(1));
        Assert.assertEquals(18, usersByAssignedBDDistrict.size());

        List<User> usersByRoleAndAssignedBDDistrict = userManager.getUsersByRoleAndAssignedBDDistrict(
                roleDAO.getRole("DR"), districtDAO.getDistrict(1));
        Assert.assertEquals(1, usersByRoleAndAssignedBDDistrict.size());

        List<User> usersByAssignedMRDistrict = userManager.getUsersByAssignedMRDistrict(districtDAO.getDistrict(1));
        Assert.assertEquals(17, usersByAssignedMRDistrict.size());

        List<User> usersByRoleAndAssignedMRDistrict = userManager.getUsersByRoleAndAssignedMRDistrict(
                roleDAO.getRole("DR"), districtDAO.getDistrict(1));
        Assert.assertEquals(1, usersByRoleAndAssignedMRDistrict.size());

        User admin = userManager.authenticateUser("admin", "password");
        User newUser1 = new User();
        newUser1.setUserId("newUser1");
        newUser1.setUserName("newUser1 Name");
        newUser1.setPin(1);
        newUser1.setPrefLanguage("si");
        newUser1.setRole(roleDAO.getRole("DEO"));
        newUser1.setPasswordHash(userManager.hashPassword("newUser1"));
        newUser1.setStatus(User.State.ACTIVE);
        userManager.createUser(newUser1, admin);

        User newUser2 = new User();
        newUser2.setUserId("newUser2");
        newUser2.setUserName("newUser2 Name");
        newUser2.setPin(2);
        newUser2.setPrefLanguage("si");
        newUser2.setRole(roleDAO.getRole("DEO"));
        newUser2.setPasswordHash(userManager.hashPassword("newUser2"));
        newUser2.setStatus(User.State.ACTIVE);
        userManager.createUser(newUser2, admin);

        /*No real need to check duplicates as it will fail at DB level..
        try {
            User newUser3 = new User();
            newUser3.setUserId("newUser2");
            newUser3.setUserName("newUser3 Name");
            newUser3.setPin("2");
            newUser3.setPrefLanguage("si");
            newUser3.setRole(roleDAO.getRole("DEO"));
            newUser3.setPasswordHash(userManager.hashPassword("newUser3"));
            newUser3.setCurrentStatus(User.State.ACTIVE);
            userManager.createUser(newUser3, admin);
            fail("Should not be able to create users with duplicate IDs");
        } catch (Exception ignore) {
            // Caught expected exception - ignore
        }*/

        // try to delete user 2
        userManager.deleteUser(newUser2, admin);
        // now user2 cannot login
        try {
            userManager.authenticateUser("newUser2", "newUser2");
            fail("Deleted user allowed to login");
        } catch (Exception ignore) {
            // Caught expected exception - ignore
        }

        // user locations testing
        // TODO 
        /*for (int i=0; i<10; i++) {
            Location location = new Location();
            location.setLocationCode(i);
            location.setEnLocationName("Location " + i);
            location.setSiLocationName("@Location " + i + "@");
            location.setTaLocationName("#Location " + i + "#");
            masterDataManagementService.addLocation(location, admin);
        }*/

        User newUser4 = new User();
        newUser4.setUserId("newUser4");
        newUser4.setUserName("newUser4 Name");
        newUser4.setPin(2);
        newUser4.setPrefLanguage("si");
        newUser4.setRole(roleDAO.getRole("DEO"));
        newUser4.setPasswordHash(userManager.hashPassword("newUser4"));
        newUser4.setStatus(User.State.ACTIVE);
        userManager.createUser(newUser4, admin);

        UserLocation ul = new UserLocation();
        ul.setUser(newUser4);
        ul.setLocation(locationDAO.getLocation(1));
        ul.setSignBirthCert(true);
        userManager.addUserLocation(ul, admin);

        newUser4 = userManager.getUserByID("newUser4");
        Assert.assertEquals(1, newUser4.getLocations().size());
        Assert.assertEquals(ul.getLocation().getLocationUKey(), newUser4.getLocations().iterator().next().getLocation().getLocationUKey());
    }
}