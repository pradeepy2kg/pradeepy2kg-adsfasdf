package lk.rgd.prs.core.service;

import lk.rgd.prs.api.dao.PINNumberDAO;
import lk.rgd.prs.api.domain.PINNumber;
import lk.rgd.prs.api.service.PINGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

/**
 * @author asankha
 */
public class PINGeneratorImpl implements PINGenerator {

    private static final Logger logger = LoggerFactory.getLogger(PINGeneratorImpl .class);

    private final PINNumberDAO pinNumberDAO;

    public PINGeneratorImpl(PINNumberDAO pinNumberDAO) {
        this.pinNumberDAO = pinNumberDAO;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public synchronized long generateTemporaryPINNumber(Date dob, boolean male) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dob);
        cal.add(Calendar.YEAR, 600);
        return generatePINNumber(cal.getTime(), male);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public synchronized long generatePINNumber(Date dob, boolean male) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(dob);
        int year = cal.get(Calendar.YEAR); // returns a 4 digit year e.g. 2010
        int dayOfYear = cal.get(Calendar.DAY_OF_YEAR); // returns the day of the year

        long dateOfBirth;
        if (year < 1900 || year > 2900) {
            logger.error("Unsupported century for year of birth : {}", year);
            throw new IllegalArgumentException("Unsupported century for year of birth : " + year);
        } else if (year >= 1900 && year < 2000) {
            dateOfBirth = 0;
        } else if (year < 2100) {
            dateOfBirth = 100;
        } else if (year < 2200) {
            dateOfBirth = 200;
        } else if (year < 2300) {
            dateOfBirth = 300;
        } else if (year < 2400) {
            dateOfBirth = 400;
        } else if (year < 2500) {
            dateOfBirth = 500;
        } else {
            dateOfBirth = 600;
        }

        // get last two digits of the year
        year = year % 100;
        // year part is now century + last 2 digits of the year
        dateOfBirth += year;

        // now add day of the year + 500 for female
        if (!male) {
            dayOfYear += 500;
        }
        dateOfBirth = (dateOfBirth * 1000) + dayOfYear;

        PINNumber lastPINNumber = pinNumberDAO.getLastPINNumber((int) dateOfBirth);
        long result = 0;

        if (lastPINNumber != null) {
            long newSerial = lastPINNumber.getLastSerial() + 1;
            lastPINNumber.setLastSerial(newSerial);
            pinNumberDAO.updateLastPINNumber(lastPINNumber);
            result = (dateOfBirth * 10000) + newSerial;
        } else {
            lastPINNumber = new PINNumber();
            lastPINNumber.setDateOfBirth((int) dateOfBirth);
            lastPINNumber.setLastSerial(1);
            pinNumberDAO.addLastPINNumber(lastPINNumber);
            result = (dateOfBirth * 10000) + 1;
        }

        logger.debug("Generated PIN number {} for DOB : {}", result, dob);
        return result;
    }
}
