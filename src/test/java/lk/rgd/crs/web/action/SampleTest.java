package lk.rgd.crs.web.action;

import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class SampleTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        System.out.println("show this once for class - setup");
    }

    @AfterClass
    public static void oneTimeTearDown() {
        System.out.println("show this once for class - teardown");
    }

    @Before
    public void setUp() {
        System.out.println("show this once for each method - setup");
    }

    @After
    public void tearDown() {
        System.out.println("show this once for each method - teardown");
    }

    @Test
    public void testMethod1() {
        System.out.println("run test method 1");
    }

    @Test
    public void testMethod2() {
        System.out.println("run test method 2");
    }
}

