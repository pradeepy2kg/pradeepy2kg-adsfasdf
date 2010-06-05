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
        User rg = userDAO.getUser("rg");
        Map<Integer, String> districts = districtDAO.getDistricts(AppConstants.SINHALA, rg);
        Assert.assertTrue(districts.size() == 25);
        Map<Integer, String> dsDivisions = dsDivisionDAO.getDSDivisionNames(11, AppConstants.SINHALA, rg);
        Assert.assertTrue(dsDivisions.size() > 1);
        Map<Integer, String> bdDivisions = bdDivisionDAO.getDivisions(AppConstants.SINHALA, 11, rg);
        Assert.assertTrue(bdDivisions.size() > 1);

        // ARG-Western Province sees all 3 districts in province
        User arg = userDAO.getUser("arg-western");
        districts = districtDAO.getDistricts(AppConstants.SINHALA, arg);
        Assert.assertTrue(districts.size() == 3);
        dsDivisions = dsDivisionDAO.getDSDivisionNames(11, AppConstants.SINHALA, arg);
        Assert.assertTrue(dsDivisions.size() > 1);
        bdDivisions = bdDivisionDAO.getDivisions(AppConstants.SINHALA, 11, arg);
        Assert.assertTrue(bdDivisions.size() > 1);

        // DR-colombo must see only colombo, but all BDDivisions within it
        User dr = userDAO.getUser("dr-colombo");
        districts = districtDAO.getDistricts(AppConstants.SINHALA, dr);
        Assert.assertTrue(districts.size() == 1);
        Assert.assertTrue("11 : කොළඹ".equals(districts.values().iterator().next()));
        dsDivisions = dsDivisionDAO.getDSDivisionNames(11, AppConstants.SINHALA, dr);
        Assert.assertTrue(dsDivisions.size() > 1);
        bdDivisions = bdDivisionDAO.getDivisions(AppConstants.SINHALA, 11, dr);
        Assert.assertTrue(bdDivisions.size() > 1);

        // ADR-colombo must see only colombo, and fort BD division
        User adr = userDAO.getUser("adr-colombo-colombo");
        districts = districtDAO.getDistricts(AppConstants.SINHALA, adr);
        Assert.assertTrue(districts.size() == 1);
        Assert.assertTrue("11 : කොළඹ".equals(districts.values().iterator().next()));
        dsDivisions = dsDivisionDAO.getDSDivisionNames(11, AppConstants.SINHALA, adr);
        Assert.assertTrue(dsDivisions.size() == 1);
        bdDivisions = bdDivisionDAO.getDivisions(AppConstants.SINHALA, 11, adr);
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
        UserManager bean = (UserManager) ctx.getBean("userManagerService", UserManager.class);
        User user = bean.authenticateUser("asankha", "asankha");
        Assert.assertNotNull(user);
        try {
            user = bean.authenticateUser("asankha", "wrong");
            fail("Should fail for 'wrong' password");
        } catch (AuthorizationException e) {
        }
        try {
            user = bean.authenticateUser("asankha", null);
            fail("Should fail for null password");
        } catch (AuthorizationException e) {
        }

        RoleDAO roleDAO = (RoleDAO) ctx.getBean("roleDAOImpl", RoleDAO.class);

        User rg = bean.authenticateUser("rg", "password");
        Assert.assertTrue(rg.getRoles().contains(roleDAO.getRole("RG")));
        Assert.assertFalse(rg.getRoles().contains(roleDAO.getRole("ADR")));
        Assert.assertFalse(rg.getRoles().contains(roleDAO.getRole("DEO")));
        Assert.assertTrue(rg.isAuthorized(Permission.APPROVE_BDF));
        Assert.assertTrue(rg.isAuthorized(Permission.DISTRICT_WIDE_ACCESS));

        User asankha = bean.authenticateUser("asankha", "asankha");
        Assert.assertFalse(asankha.getRoles().contains(roleDAO.getRole("RG")));
        Assert.assertTrue(asankha.getRoles().contains(roleDAO.getRole("ADR")));
        Assert.assertFalse(asankha.getRoles().contains(roleDAO.getRole("DEO")));
        Assert.assertTrue(asankha.isAuthorized(Permission.APPROVE_BDF));
        Assert.assertFalse(asankha.isAuthorized(Permission.DISTRICT_WIDE_ACCESS));
    }

    public void testBirthDeclaration() throws Exception {
        BirthDeclarationDAO dao = (BirthDeclarationDAO) ctx.getBean("birthDeclarationDAOImpl", BirthDeclarationDAO.class);
        BDDivisionDAO bdDao = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);

        //List<BirthDeclaration> r = dao.getConfirmationPrintPending(bdDao.getBDDivision(11, 1), false);
        //Assert.assertEquals(2, r.size());
        
        //r = dao.getConfirmationPrintPending(bdDao.getBDDivision(11, 1), true);
        //Assert.assertEquals(1, r.size());

        List<BirthDeclaration> r = dao.getConfirmationApprovalPending(bdDao.getBDDivision(11, 1), 1, 10);
        Assert.assertEquals(10, r.size());
    }
}
