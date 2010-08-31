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
    public int generatePINNumber(Date dob, boolean male) {

        logger.debug("Generating a PIN number for DOB : {}", dob);

        Calendar cal = Calendar.getInstance();
        cal.setTime(dob);
        int year = cal.get(Calendar.YEAR); // returns a 4 digit year e.g. 2010
        int dayOfYear = cal.get(Calendar.DAY_OF_YEAR); // returns the day of the year

        int dateOfBirth;
        if (year >= 2000 && year < 2100) {
            dateOfBirth = 100;
        } else if (year >= 1900 && year < 2000) {
            dateOfBirth = 0;
        } else {
            logger.error("Unsupported century : {}", year);
            throw new IllegalArgumentException("Unsupported century : " + year);
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

        PINNumber lastPINNumber = pinNumberDAO.getLastPINNumber(dateOfBirth);
        if (lastPINNumber != null) {
            int newSerial = lastPINNumber.getLastSerial() + 1;
            lastPINNumber.setLastSerial(newSerial);
            pinNumberDAO.updateLastPINNumber(lastPINNumber);
            return (dateOfBirth * 10000) + newSerial;
        } else {
            lastPINNumber = new PINNumber();
            lastPINNumber.setDateOfBirth(dateOfBirth);
            lastPINNumber.setLastSerial(1);
            pinNumberDAO.addLastPINNumber(lastPINNumber);
            return (dateOfBirth * 10000) + 1;
        }
    }
}
