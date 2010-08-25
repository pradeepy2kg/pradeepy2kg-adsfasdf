package lk.rgd.crs.web.action;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lk.rgd.common.CustomStrutsTestCase;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import lk.rgd.common.CustomStrutsTestCase;

public class SampleTest extends CustomStrutsTestCase {

    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(SampleTest.class)) {
            protected void setUp() throws Exception {
                System.out.println("===============> show this once for class - setup");
                super.setUp();
            }

            protected void tearDown() throws Exception {
                System.out.println("===============> show this once for class - tear");
                super.tearDown();
            }
        };
        return setup;
    }

    @Override
    protected void setUp() throws Exception {
        System.out.println("setup");
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        System.out.println("tear");
        super.tearDown();
    }

    public void testMethod1() {
        System.out.println("run test method 1");
    }

    public void testMethod2() {
        System.out.println("run test method 2");
    }

    public void testMethod3() {
        System.out.println("run test method 3");
    }

    public void testMethod4() {
        System.out.println("run test method 45");
    }

}

