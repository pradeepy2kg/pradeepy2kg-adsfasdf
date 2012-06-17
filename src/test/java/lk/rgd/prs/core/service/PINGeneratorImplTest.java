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

        // generate a PIN for a birth in 1900's
        try {
            GregorianCalendar cal = new GregorianCalendar();
            cal.set(1975, Calendar.JULY, 29, 20, 15, 00);
            
            long pin = pinGenerator.generatePINNumber(cal.getTime(), true, null);
            Assert.assertEquals("wrong pin generated", 197521120007L, pin);
            pin = pinGenerator.generatePINNumber(cal.getTime(), true, null);
            Assert.assertEquals("wrong pin generated", 197521120015L, pin);
            pin = pinGenerator.generatePINNumber(cal.getTime(), false, null);
            Assert.assertEquals("wrong pin generated", 197571120005L, pin);
            pin = pinGenerator.generatePINNumber(cal.getTime(), false, null);
            Assert.assertEquals("wrong pin generated", 197571120013L, pin);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Did not expect a failure");
        }

        // generate a PIN for a birth in 2000's
        try {
            GregorianCalendar cal = new GregorianCalendar();
            cal.set(2010, Calendar.JULY, 29, 20, 15, 00);

            long pin = pinGenerator.generatePINNumber(cal.getTime(), true, null);
            Assert.assertEquals("wrong pin generated", 201021100019L, pin);
            pin = pinGenerator.generatePINNumber(cal.getTime(), true, null);
            Assert.assertEquals("wrong pin generated", 201021100027L, pin);
            pin = pinGenerator.generatePINNumber(cal.getTime(), false, null);
            Assert.assertEquals("wrong pin generated", 201071100017L, pin);
            pin = pinGenerator.generatePINNumber(cal.getTime(), false, null);
            Assert.assertEquals("wrong pin generated", 201071100025L, pin);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Did not expect a failure");
        }

        // generate a PIN for a birth in 2000's - check 28th February 2011 and 31 January 2011
        try {
            GregorianCalendar cal = new GregorianCalendar();
            cal.set(2011, Calendar.FEBRUARY, 28, 20, 15, 00);

            long pin = pinGenerator.generatePINNumber(cal.getTime(), true, null);
            Assert.assertEquals("wrong pin generated", 201105900011L, pin);

            cal.set(2011, Calendar.JANUARY, 31, 20, 15, 00);
            pin = pinGenerator.generatePINNumber(cal.getTime(), true, null);
            Assert.assertEquals("wrong pin generated", 201103100019L, pin);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Did not expect a failure");
        }

        // generate temporary PINs for a birth in 2000's
        try {
            GregorianCalendar cal = new GregorianCalendar();
            cal.set(2010, Calendar.JULY, 29, 20, 15, 00);

            long pin = pinGenerator.generateTemporaryPINNumber(cal.getTime(), true);
            Assert.assertEquals("wrong pin generated", 701021100012L, pin);
            pin = pinGenerator.generateTemporaryPINNumber(cal.getTime(), true);
            Assert.assertEquals("wrong pin generated", 701021100021L, pin);
            pin = pinGenerator.generateTemporaryPINNumber(cal.getTime(), false);
            Assert.assertEquals("wrong pin generated", 701071100011L, pin);
            pin = pinGenerator.generateTemporaryPINNumber(cal.getTime(), false);
            Assert.assertEquals("wrong pin generated", 701071100029L, pin);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Did not expect a failure");
        }

        // generate a PIN for someone with a NIC, but born before cutOverDate
        try {
            GregorianCalendar cal = new GregorianCalendar();

            cal.set(1971, Calendar.MARCH, 1, 20, 15, 00);
            long pin = pinGenerator.generatePINNumber(cal.getTime(), false, "715601231V");
            Assert.assertEquals("wrong pin generated", 197156001231L, pin);

            // will get similar PIN
            cal.set(1971, Calendar.FEBRUARY, 1, 20, 15, 00);
            pin = pinGenerator.generatePINNumber(cal.getTime(), false, "715322366V");
            Assert.assertEquals("wrong pin generated", 197153202366L, pin);

            cal.set(1971, Calendar.JANUARY, 31, 20, 15, 00);
            pin = pinGenerator.generatePINNumber(cal.getTime(), false, "715311232V");
            Assert.assertEquals("wrong pin generated", 197153101232L, pin);

            // check a duplicate - must get a number in the new range
            cal.set(1978, Calendar.JANUARY, 17, 20, 15, 00);
            pin = pinGenerator.generatePINNumber(cal.getTime(), false, "785170903V");
            Assert.assertEquals("wrong pin generated", 197851720005L, pin);

            cal.set(1975, Calendar.JULY, 29, 20, 15, 00);
            // check for generation of a new PIN for wrong ID (e.g. DOB)
            pin = pinGenerator.generatePINNumber(cal.getTime(), true, "752101430V");
            Assert.assertEquals("wrong pin generated", 197521120023L, pin);

            // check for generation of a new PIN for wrong ID (e.g. gender)
            pin = pinGenerator.generatePINNumber(cal.getTime(), true, "757111430V");
            Assert.assertEquals("wrong pin generated", 197521120031L, pin);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Did not expect a failure");
        }
    }
}
