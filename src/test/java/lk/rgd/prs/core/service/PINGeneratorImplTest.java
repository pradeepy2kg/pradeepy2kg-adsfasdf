package lk.rgd.prs.core.service;

import junit.framework.Assert;
import junit.framework.TestCase;
import lk.rgd.UnitTestManager;
import lk.rgd.prs.api.dao.PINNumberDAO;
import lk.rgd.prs.api.service.PINGenerator;
import org.springframework.context.ApplicationContext;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author asankha
 */
public class PINGeneratorImplTest extends TestCase {

    private final ApplicationContext ctx = UnitTestManager.ctx;
    private final PINGenerator pinGenerator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        PINNumberDAO pinNumberDAO = (PINNumberDAO) ctx.getBean("pinNumberDAOImpl", PINNumberDAO.class);

        try {
            pinNumberDAO.deleteLastPINNumber(pinNumberDAO.getLastPINNumber(75210));
        } catch (Exception ignore) {}
        try {
            pinNumberDAO.deleteLastPINNumber(pinNumberDAO.getLastPINNumber(75710));
        } catch (Exception ignore) {}
        try {
            pinNumberDAO.deleteLastPINNumber(pinNumberDAO.getLastPINNumber(110210));
        } catch (Exception ignore) {}
        try {
            pinNumberDAO.deleteLastPINNumber(pinNumberDAO.getLastPINNumber(110710));
        } catch (Exception ignore) {}
    }

    public PINGeneratorImplTest() {
        pinGenerator = (PINGenerator) ctx.getBean("pinGeneratorService", PINGenerator.class);
    }

    public void testPINGeneration() throws Exception {

        // generate a PIN for a birth in 1800's
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -200);
            long pin = pinGenerator.generatePINNumber(cal.getTime(), true);
            fail("Should only generate PINs for 1900 to 2099");
        } catch (Exception expected) {}

        // generate a PIN for a birth in 2110's
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, + 1000);
            long pin = pinGenerator.generatePINNumber(cal.getTime(), true);
            fail("Should only generate PINs for 1900 to 2999");
        } catch (Exception expected) {}

        // generate a PIN for a birth in 1900's
        try {
            GregorianCalendar cal = new GregorianCalendar();
            cal.set(1975, Calendar.JULY, 29, 20, 15, 00);
            
            long pin = pinGenerator.generatePINNumber(cal.getTime(), true);
            // should be 752100001
            Assert.assertEquals("wrong pin generated", 752100001, pin);
            pin = pinGenerator.generatePINNumber(cal.getTime(), true);
            // should be 0752100002
            Assert.assertEquals("wrong pin generated", 752100002, pin);
            pin = pinGenerator.generatePINNumber(cal.getTime(), false);
            // should be 0757100001
            Assert.assertEquals("wrong pin generated", 757100001, pin);
            pin = pinGenerator.generatePINNumber(cal.getTime(), false);
            // should be 0757100002
            Assert.assertEquals("wrong pin generated", 757100002, pin);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Did not expect a failure");
        }

        // generate a PIN for a birth in 2000's
        try {
            GregorianCalendar cal = new GregorianCalendar();
            cal.set(2010, Calendar.JULY, 29, 20, 15, 00);

            long pin = pinGenerator.generatePINNumber(cal.getTime(), true);
            // should be 0752110001
            Assert.assertEquals("wrong pin generated", 1102100001, pin);
            pin = pinGenerator.generatePINNumber(cal.getTime(), true);
            // should be 0752110002
            Assert.assertEquals("wrong pin generated", 1102100002, pin);
            pin = pinGenerator.generatePINNumber(cal.getTime(), false);
            // should be 0757110001
            Assert.assertEquals("wrong pin generated", 1107100001, pin);
            pin = pinGenerator.generatePINNumber(cal.getTime(), false);
            // should be 0757110002
            Assert.assertEquals("wrong pin generated", 1107100002, pin);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Did not expect a failure");
        }

        // generate temporary PINs for a birth in 2000's
        try {
            GregorianCalendar cal = new GregorianCalendar();
            cal.set(2010, Calendar.JULY, 29, 20, 15, 00);

            long pin = pinGenerator.generateTemporaryPINNumber(cal.getTime(), true);
            // should be 6102100001
            Assert.assertEquals("wrong pin generated", 6102100001L, pin);
            pin = pinGenerator.generateTemporaryPINNumber(cal.getTime(), true);
            // should be 6102100002
            Assert.assertEquals("wrong pin generated", 6102100002L, pin);
            pin = pinGenerator.generateTemporaryPINNumber(cal.getTime(), false);
            // should be 6107100001
            Assert.assertEquals("wrong pin generated", 6107100001L, pin);
            pin = pinGenerator.generateTemporaryPINNumber(cal.getTime(), false);
            // should be 6107100002
            Assert.assertEquals("wrong pin generated", 6107100002L, pin);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Did not expect a failure");
        }
    }
}
