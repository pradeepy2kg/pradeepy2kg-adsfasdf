package lk.rgd.crs.web.action;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lk.rgd.common.CustomStrutsTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleTest extends CustomStrutsTestCase {

    private static final Logger logger = LoggerFactory.getLogger(SampleTest.class);
    
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

    @Override
    protected void setUp() throws Exception {
        logger.debug("method setup");
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        logger.debug("method teardown");
        super.tearDown();
    }

    public void testMethod1() {
        logger.debug("run test method 1");
    }

    public void testMethod2() {
        logger.debug("run test method 2");
    }

    public void testMethod3() {
        logger.debug("run test method 3");
    }

    public void testMethod4() {
        logger.debug("run test method 45");
    }

}

