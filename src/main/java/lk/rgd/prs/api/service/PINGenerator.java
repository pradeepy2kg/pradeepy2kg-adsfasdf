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
 *  5 for births in 25th century ( January 1st, 2401 TO December 31st, 2500)
 *
 *  These three are special cases to define temporary PIN numbers for the 20th, 21st and 22nd centuries
 *  6 for temporary records with births in 19th century ( January 1st, 1901 TO December 31st, 2000) - assumes year of birth ~ 2501 - 2600
 *  7 for temporary records with births in 20th century ( January 1st, 2001 TO December 31st, 2100) - assumes year of birth ~ 2601 - 2700
 *  8 for temporary records with births in 21th century ( January 1st, 2101 TO December 31st, 2200) - assumes year of birth ~ 2701 - 2800
 *  9 for temporary records for those without known dates of birth - assumes year of birth ~ 2801 - 2900
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
     * Generate a new PIN number for the given date
     *
     * @param dateOfBirth the date of birth
     * @param male true if the PIN is generated for a male, false for a female
     * @param  nic current NIC number - if any
     * @return the generated 10 digit PIN number
     */
    public long generatePINNumber(Date dateOfBirth,  boolean male, String nic);

    /**
     * Generate a temporary PIN number for the given date by changing the DOB = DOB + 600
     *
     * @param dateOfBirth the date of birth
     * @param male true if the PIN is generated for a male, false for a female
     * @return the generated 10 digit PIN number
     */
    public long generateTemporaryPINNumber(Date dateOfBirth,  boolean male);
}
