package lk.rgd;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lk.rgd.common.AppParametersTest;
import lk.rgd.common.MasterTablesTest;
import lk.rgd.common.UserManagementTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * The unit tests are dependent on the availability of a deterministic database. This test suite ensures proper
 * DB initialization, and then invokes other unit test cases
 *
 * @author asankha
 */
public class UnitTestSuite extends TestCase {

    private static final Logger logger = LoggerFactory.getLogger(UnitTestSuite.class);
    public static final ApplicationContext ctx = getApplicationContext();

    public static Test suite() throws Exception {

        TestSuite suite = new TestSuite();

        TestSetup setup = new TestSetup(suite) {
            protected void setUp() throws Exception {
                super.setUp();
            }

            protected void tearDown() throws Exception {
            }
        };

        suite.addTest(new AppParametersTest());
        suite.addTest(new MasterTablesTest());
        suite.addTest(new UserManagementTest());

        return setup;
    }

    private static ApplicationContext getApplicationContext() {
        try {
            GenericApplicationContext ctx = new GenericApplicationContext();
            XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
            xmlReader.loadBeanDefinitions(new ClassPathResource("unitTest_applicationContext.xml"));
            ctx.refresh();
            return ctx;
        } catch (Exception e) {
            logger.error("Exception starting Spring for unit-test backend", e);
            throw new IllegalStateException("Couldn't start Spring...", e);
        }
    }
}
