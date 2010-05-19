package lk.rgd.crs.core.dao;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lk.rgd.crs.api.BirthConstants;
import lk.rgd.crs.api.dao.AppParametersDAO;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author asankha
 */
public class AppParametersDAOImplTest extends TestCase {

    private static final Logger logger = LoggerFactory.getLogger(AppParametersDAOImplTest.class);
    private static GenericApplicationContext ctx = null;

    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(AppParametersDAOImplTest.class)) {

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

                    con.prepareStatement("CREATE TABLE app_parameters ( \n" +
                        "\tNAME      \tVARCHAR(25),\n" +
                        "\tVALUE     \tVARCHAR(40)\n" +
                        "\t)").executeUpdate();

                    con.prepareStatement("INSERT INTO app_parameters(NAME, VALUE)\n" +
                        "  VALUES('crs.birth.late_reg_days', '90')").executeUpdate();
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

    public void testInsertAndQuery() throws Exception {
        AppParametersDAO bean = (AppParametersDAO) ctx.getBean("appParametersDAOImpl", AppParametersDAO.class);
        Assert.assertEquals(bean.getIntParameter(BirthConstants.CRS_BIRTH_LATE_REG_DAYS), 90);

        bean.setIntParameter("test_int", 3);
        Assert.assertEquals(bean.getIntParameter("test_int"), 3);

        bean.setIntParameter("test_int", 4);
        Assert.assertEquals(bean.getIntParameter("test_int"), 4);

        bean.setStringParameter("test_string", "hello");
        Assert.assertEquals(bean.getStringParameter("test_string"), "hello");
    }
}
