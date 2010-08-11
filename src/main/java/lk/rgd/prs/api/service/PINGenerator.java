package lk.rgd.prs.api.service;

import java.util.Date;

/**
 * The interface to the PIN generation algorithm as documented below
 * 1. N - Century
 *  0 for births in 20th century ( January 1st, 1901 TO December 31st, 2000)
 *  1 for births in 21st century ( January 1st, 2001 TO December 31st, 2100)
 *  2 for births in 22nd century ( January 1st, 2101 TO December 31st, 2200)
 *  3 for births in 23rd century ( January 1st, 2201 TO December 31st, 2300)
 *  4 for births in 24th century ( January 1st, 2301 TO December 31st, 2400)
 *
 * 2. NN - Year of birth
 *
 * 3. NNN - Birth 'day of the year'.
 * For males 1-500 ( Birth day of year ) For females 501 - 999 ( Birth day of year + 500)
 *
 * 4. NNNN - Number of birth unique sequential number assigned for the individuals born on that day
 *
 * @author asankha
 */
public interface PINGenerator {

    /**
     * Generate a new PIN number for the given date. Only supports years from 1900 to 2099 
     *
     * @param dateOfBirth the date of birth
     * @param male true if the PIN is generated for a male, false for a female
     * @return the generated 10 digit PIN number
     */
    public int generatePINNumber(Date dateOfBirth,  boolean male);
}
