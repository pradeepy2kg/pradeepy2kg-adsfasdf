package lk.rgd.crs.core.dao;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lk.rgd.AppConstants;
import lk.rgd.crs.api.BirthConstants;
import lk.rgd.crs.api.dao.AppParametersDAO;
import lk.rgd.crs.api.dao.CountryDAO;
import lk.rgd.crs.api.dao.DistrictDAO;
import lk.rgd.crs.api.domain.Country;
import lk.rgd.crs.api.domain.District;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tests various low level DAOs
 *
 * @author asankha
 */
public class DAOImplTest extends TestCase {

    private static final Logger logger = LoggerFactory.getLogger(DAOImplTest.class);
    private static GenericApplicationContext ctx = null;

    private static Set<String> sinhalaDistricts = new HashSet<String>();
    private static Set<String> sinhalaCountries = new HashSet<String>();

    static {
        sinhalaDistricts.add("කොළඹ");
        sinhalaDistricts.add("ගාල්ල");
        sinhalaDistricts.add("මහනුවර");

        sinhalaCountries.add("ශ්‍රී ලංකාව");
        sinhalaCountries.add("ජපානය");
        sinhalaCountries.add("ඉන්දියාව");
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
                    xmlReader.loadBeanDefinitions(new ClassPathResource("unit-test-spring.xml"));
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
        List<District> districts = bean.getDistricts(AppConstants.SINHALA);
        Assert.assertEquals(3, districts.size());
        for (District d : districts) {
            Assert.assertTrue(sinhalaDistricts.contains(d.getDistrictName()));
        }
    }

    public void testCountries() throws Exception {
        CountryDAO bean = (CountryDAO) ctx.getBean("countryDAOImpl", CountryDAO.class);
        List<Country> countries = bean.getCountries(AppConstants.SINHALA);
        Assert.assertEquals(3, countries.size());
        for (Country d : countries) {
            Assert.assertTrue(sinhalaCountries.contains(d.getCountryName()));
        }
    }
}
