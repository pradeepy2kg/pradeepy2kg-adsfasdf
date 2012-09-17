package lk.rgd.prs.core.service;

import lk.rgd.prs.api.dao.PINNumberDAO;
import lk.rgd.prs.api.dao.PersonDAO;
import lk.rgd.prs.api.domain.PINNumber;
import lk.rgd.prs.api.service.PINGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author asankha
 */
public class PINGeneratorImpl implements PINGenerator {

    private static final Logger logger = LoggerFactory.getLogger(PINGeneratorImpl .class);

    private static final GregorianCalendar gregCalendar = new GregorianCalendar();

    private final PINNumberDAO pinNumberDAO;
    private final PersonDAO personDAO;
    private final Date cutOverDate;
    private final int cutOverSerial;

    public PINGeneratorImpl(PINNumberDAO pinNumberDAO, PersonDAO personDAO, String cutOverDate, int cutOverSerial)
        throws ParseException {
        this.pinNumberDAO = pinNumberDAO;
        this.personDAO = personDAO;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.cutOverDate = sdf.parse(cutOverDate);
        this.cutOverSerial = cutOverSerial;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public synchronized long generateTemporaryPINNumber(Date dob, boolean male) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dob);
        cal.add(Calendar.YEAR, 5000);
        return generatePINNumber(cal.getTime(), male, null);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public synchronized long generatePINNumber(final Date dob, final boolean male, final String nic) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(dob);
        int year      = cal.get(Calendar.YEAR);        // returns a 4 digit year e.g. 2010
        int dayOfYear = cal.get(Calendar.DAY_OF_YEAR); // returns the day of the year
        int month     = cal.get(Calendar.MONTH);       // returns the month of the year

        // increment day of year, if not a leap year, and month is after February
        if (!gregCalendar.isLeapYear(year) && month > 2) {   // 0 - jan, 1 - feb, 2 - mar...
            dayOfYear++;
        }
        // now add day of the year + 500 for female
        if (!male) {
            dayOfYear += 500;
        }

        long dayOfBirth = (year * 1000) + dayOfYear;

        // decide on the serial portion of the PIN
        // for persons born after cutover date, PIN generation is totally in our control
        long result = 0;

        if (dob.after(cutOverDate)) {

            PINNumber lastPINNumber = pinNumberDAO.getLastPINNumber((int) dayOfBirth);
            if (lastPINNumber != null) {
                long newSerial = lastPINNumber.getLastSerial() + 1;
                lastPINNumber.setLastSerial(newSerial);
                pinNumberDAO.updateLastPINNumber(lastPINNumber);
                result = (dayOfBirth * 10000) + newSerial;
            } else {
                lastPINNumber = new PINNumber();
                lastPINNumber.setDateOfBirth((int) dayOfBirth);
                lastPINNumber.setLastSerial(1);
                pinNumberDAO.addLastPINNumber(lastPINNumber);
                result = (dayOfBirth * 10000) + 1;
            }

            // append check digit
            int check = computeCheckDigit(result);
            result = (result * 10) + check;

        } else {

            // does the person have an existing NIC, and if so does its DOB match?
            if (nic != null && nic.length() == 10) {
                String dayBorn = "19" + nic.substring(0, 5);
                if (dayOfBirth == Integer.parseInt(dayBorn)) {
                    // we can re-use this serial number
                    result = (dayOfBirth * 10000) + Integer.parseInt(nic.substring(5,8));
                }
            }

            // if we found a possible PIN, check if its usable
            if (result > 0) {
                // is this PIN somehow already used (e.g. duplicate?)
                int check = computeCheckDigit(result);
                result = (result * 10) + check;

                if (personDAO.findPersonByPIN(result) != null) {
                    result = 0;
                }
            }

            // if we couldn't find a PIN, start a new serial from cutOverSerial
            if (result == 0) {
                PINNumber lastPINNumber = pinNumberDAO.getLastPINNumber((int) dayOfBirth);
                if (lastPINNumber != null) {
                    long newSerial = lastPINNumber.getLastSerial() + 1;
                    lastPINNumber.setLastSerial(newSerial);
                    pinNumberDAO.updateLastPINNumber(lastPINNumber);
                    result = (dayOfBirth * 10000) + newSerial;
                } else {
                    lastPINNumber = new PINNumber();
                    lastPINNumber.setDateOfBirth((int) dayOfBirth);
                    lastPINNumber.setLastSerial(cutOverSerial);
                    pinNumberDAO.addLastPINNumber(lastPINNumber);
                    result = (dayOfBirth * 10000) + cutOverSerial;
                }

                // append check digit
                int check = computeCheckDigit(result);
                result = (result * 10) + check;
            }
        }

        logger.debug("Generated PIN number {} for DOB : {}", result, dob);
        return result;
    }

    /**
     * //11-(N1*8+N2*4+N3*3+N4*2+N5*7+N6*6+N7*5+N8*7+N9*4+N10*3+N11*2)%11
     * @param number a 11 digit long
     * @return the 12th check digit
     */
    private static int computeCheckDigit(final long number) {
        char[] chars = Long.toString(number).toCharArray();
        if (chars.length != 11) {
            throw new IllegalArgumentException("Cannot compute check digit for number : " + number + " - not 11 digits");
        }

        int [] N = new int[11];
        for (int i=0; i<11; i++) {
            N[i] = (int) chars[i] - 48;
        }
        int check = 11 - ((N[0]*8+N[1]*4+N[2]*3+N[3]*2+N[4]*7+N[5]*6+N[6]*5+N[7]*7+N[8]*4+N[9]*3+N[10]*2) % 11);

        if (check > 9) {
            return check - 10;
        } else {
            return check;
        }
    }

    public static void main(String[] args) {
        System.out.println("CD : " + computeCheckDigit(19752110143L)); // 0
        System.out.println("CD : " + computeCheckDigit(19641040275L)); // 7
        System.out.println("CD : " + computeCheckDigit(19752110008L)); // 7
        System.out.println("CD : " + computeCheckDigit(19752110006L)); // 7
        System.out.println("CD : " + computeCheckDigit(19752110004L)); // 7
    }
}
