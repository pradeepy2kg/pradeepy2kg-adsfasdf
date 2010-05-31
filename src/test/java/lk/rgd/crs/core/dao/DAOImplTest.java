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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tests various low level DAOs
 *
 * @author asankha
 */
public class DAOImplTest extends TestCase {

    private static final Logger logger = LoggerFactory.getLogger(DAOImplTest.class);
    private static final DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");

    private static GenericApplicationContext ctx = null;

    private static Set<String> sinhalaDistricts = new HashSet<String>();
    private static Set<String> sinhalaCountries = new HashSet<String>();
    private static Set<String> sinhalaRaces = new HashSet<String>();
    private static Set<String> sinhalaBDDivisions = new HashSet<String>();

    static {
        sinhalaDistricts.add("කොළඹ");
        sinhalaDistricts.add("ගාල්ල");
        sinhalaDistricts.add("මහනුවර");

        sinhalaCountries.add("ශ්‍රී ලංකාව");
        sinhalaCountries.add("ජපානය");
        sinhalaCountries.add("ඉන්දියාව");

        sinhalaRaces.add("සිංහල");
        sinhalaRaces.add("ශ්‍රී ලංකික දෙමල");
        sinhalaRaces.add("ඉන්දියානු දෙමල");

        sinhalaBDDivisions.add("කොළඹ කොටුව (Medical)");
        sinhalaBDDivisions.add("කොම්පන්න වීදීය (Medical)");
        sinhalaBDDivisions.add("අලුත් කඩේ (Medical)");
    }

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

    public void testDistricts() throws Exception {
        DistrictDAO bean = (DistrictDAO) ctx.getBean("districtDAOImpl", DistrictDAO.class);
        // TODO User should be added
        Map<Integer, String> districts = bean.getDistricts(AppConstants.SINHALA, null);
        Assert.assertEquals(3, districts.size());
        for (String d : districts.values()) {
            Assert.assertTrue(sinhalaDistricts.contains(d));
        }
    }

    public void testCountries() throws Exception {
        CountryDAO bean = (CountryDAO) ctx.getBean("countryDAOImpl", CountryDAO.class);
        Map<Integer, String> countries = bean.getCountries(AppConstants.SINHALA);
        Assert.assertEquals(3, countries.size());
        for (String d : countries.values()) {
            Assert.assertTrue(sinhalaCountries.contains(d));
        }
    }

    public void testRaces() throws Exception {
        RaceDAO bean = (RaceDAO) ctx.getBean("raceDAOImpl", RaceDAO.class);
        Map<Integer, String> races = bean.getRaces(AppConstants.SINHALA);
        Assert.assertEquals(3, races.size());
        for (String r : races.values()) {
            Assert.assertTrue(sinhalaRaces.contains(r));
        }
    }

    public void testBDDivisions() throws Exception {
        BDDivisionDAO bean = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);
        // TODO User should be added
        Map<Integer, String> bdDivisions = bean.getDivisions(AppConstants.SINHALA, 11, null);
        Assert.assertEquals(3, bdDivisions.size());
        for (String bd : bdDivisions.values()) {
            Assert.assertTrue(sinhalaBDDivisions.contains(bd));
        }
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

        RoleDAO role = (RoleDAO) ctx.getBean("roleDAOImpl", RoleDAO.class);

        user = bean.authenticateUser("rg", "password");
        Assert.assertTrue(user.getRoles().contains(role.getRole("RG")));
        Assert.assertTrue(user.getRoles().contains(role.getRole("ADR")));
        Assert.assertFalse(user.getRoles().contains(role.getRole("DEO")));
        Assert.assertTrue(user.isAuthorized(Permission.APPROVE_BDF));
        Assert.assertTrue(user.isAuthorized(Permission.DISTRICT_WIDE_ACCESS));

        user = bean.authenticateUser("asankha", "asankha");
        Assert.assertFalse(user.getRoles().contains(role.getRole("RG")));
        Assert.assertTrue(user.getRoles().contains(role.getRole("ADR")));
        Assert.assertFalse(user.getRoles().contains(role.getRole("DEO")));
        Assert.assertTrue(user.isAuthorized(Permission.APPROVE_BDF));
        Assert.assertFalse(user.isAuthorized(Permission.DISTRICT_WIDE_ACCESS));
    }

    public void testBirthDeclaration() throws Exception {
        BirthDeclarationDAO dao = (BirthDeclarationDAO) ctx.getBean("birthDeclarationDAOImpl", BirthDeclarationDAO.class);
        BDDivisionDAO bdDao = (BDDivisionDAO) ctx.getBean("bdDivisionDAOImpl", BDDivisionDAO.class);

        List<BirthDeclaration> r = dao.getConfirmationPrintPending(bdDao.getBDDivision(11, 1), false);
        Assert.assertEquals(2, r.size());
        
        r = dao.getConfirmationPrintPending(bdDao.getBDDivision(11, 1), true);
        Assert.assertEquals(1, r.size());
    }
}
