package lk.rgd.crs.core.dao;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lk.rgd.AppConstants;
import lk.rgd.crs.api.BirthConstants;
import lk.rgd.crs.api.dao.AppParametersDAO;
import lk.rgd.crs.api.dao.DistrictDAO;
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

    static {
        sinhalaDistricts.add("\u0D9A\u0DDC\u0DC5\u0DB9");
        sinhalaDistricts.add("\u0D9C\u0DCF\u0DBD\u0DCA\u0DBD");
        sinhalaDistricts.add("\u0DB8\u0DC4\u0DB1\u0DD4\u0DC0\u0DBB");
    }

    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(DAOImplTest.class)) {

            protected void setUp() throws Exception {
                super.setUp();
                setupDatabase();
                startSpring();
            }

            private void setupDatabase() throws Exception {

                try {
                    logger.info("Starting in-memory database for unit tests");
                    Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                    Connection con = DriverManager.getConnection("jdbc:derby:memory:unit-testing-jpa;create=true;user=epop;password=epop");

                    // --- create sample app_parameters ---
                    con.prepareStatement("CREATE TABLE app_parameters ( \n" +
                        "\tNAME      \tVARCHAR(25),\n" +
                        "\tVALUE     \tVARCHAR(40)\n" +
                        "\t)").executeUpdate();

                    con.prepareStatement("INSERT INTO app_parameters(NAME, VALUE)\n" +
                        "  VALUES('crs.birth.late_reg_days', '90')").executeUpdate();

                    // --- create sample districts ---
                    con.prepareStatement("CREATE TABLE districts ( \n" +
                        "\tID               \tINTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,\n" +
                        "\tDISTRICTID       \tINTEGER,\n" +
                        "\tDISTRICTNAME     \tVARCHAR(25),\n" +
                        "\tLANGUAGEID       \tVARCHAR(5)\n" +
                        "\t)").executeUpdate();

                    con.prepareStatement("INSERT INTO districts (DISTRICTID, DISTRICTNAME, LANGUAGEID) " +
                        "VALUES(1, '\u0D9A\u0DDC\u0DC5\u0DB9', '" + AppConstants.SINHALA + "')").executeUpdate();
                    con.prepareStatement("INSERT INTO districts (DISTRICTID, DISTRICTNAME, LANGUAGEID) " +
                        "VALUES(2, '\u0D9C\u0DCF\u0DBD\u0DCA\u0DBD', '" + AppConstants.SINHALA + "')").executeUpdate();
                    con.prepareStatement("INSERT INTO districts (DISTRICTID, DISTRICTNAME, LANGUAGEID) " +
                        "VALUES(3, '\u0DB8\u0DC4\u0DB1\u0DD4\u0DC0\u0DBB', '" + AppConstants.SINHALA + "')").executeUpdate();

                    con.prepareStatement("INSERT INTO districts (DISTRICTID, DISTRICTNAME, LANGUAGEID) " +
                        "VALUES(1, 'Colombo', '" + AppConstants.ENGLISH + "')").executeUpdate();
                    con.prepareStatement("INSERT INTO districts (DISTRICTID, DISTRICTNAME, LANGUAGEID) " +
                        "VALUES(2, 'Galle', '" + AppConstants.ENGLISH + "')").executeUpdate();
                    con.prepareStatement("INSERT INTO districts (DISTRICTID, DISTRICTNAME, LANGUAGEID) " +
                        "VALUES(3, 'Kandy', '" + AppConstants.ENGLISH + "')").executeUpdate();

                    con.prepareStatement("INSERT INTO districts (DISTRICTID, DISTRICTNAME, LANGUAGEID) " +
                        "VALUES(1, '\u0B95\u0BC6\u0BBE\u0BB4\u0BC1\u0BAE\u0BCD\u0BAA\u0BC1', '" + AppConstants.TAMIL + "')").executeUpdate();
                    con.prepareStatement("INSERT INTO districts (DISTRICTID, DISTRICTNAME, LANGUAGEID) " +
                        "VALUES(2, '\u0B95\u0BBE\u0BB2\u0BBF', '" + AppConstants.TAMIL + "')").executeUpdate();
                    con.prepareStatement("INSERT INTO districts (DISTRICTID, DISTRICTNAME, LANGUAGEID) " +
                        "VALUES(3, '\u0B95\u0BA3\u0BCD\u0B9F\u0BBF', '" + AppConstants.TAMIL + "')").executeUpdate();

                    con.close();

                } catch (Exception e) {
                    handleException("Exception during unit test database startup", e);
                }
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
}
