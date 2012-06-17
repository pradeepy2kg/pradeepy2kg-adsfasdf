package lk.rgd.prs.api.service;

import java.util.Date;

/**
 * The interface to the PIN generation algorithm as documented below
 * <p>
 * 1. NNNN - Year
 * For PIN valid range of years 1700-2200. For Temporary PIN valid range of years 6700-7200.
 * i.e Temporary PIN Year = Year + 5000
 * <p/>
 * 2. NNN - Birth day
 * For males 1-366 ( Birth day ) For females 501 - 866 ( Birth day + 500 )
 * <p/>
 * 3. NNNN - Serial number
 * For date of births before 1994-01-01 serial number starts from 2000
 * <p/>
 * 4. N - Check digit
 * Algorithm to calculate check digit, 11-(N1*8+N2*4+N3*3+N4*2+N5*7+N6*6+N7*5+N8*7+N9*4+N10*3+N11*2)%11
 *
 * @author asankha
 */
public interface PINGenerator {

    /**
     * Generate a new PIN number for the given date
     *
     * @param dateOfBirth the date of birth
     * @param male        true if the PIN is generated for a male, false for a female
     * @param nic         current NIC number - if any
     * @return the generated 12 digit PIN number
     */
    public long generatePINNumber(Date dateOfBirth, boolean male, String nic);

    /**
     * Generate a temporary PIN number for the given date by changing the YEAR = YEAR + 5000
     *
     * @param dateOfBirth the date of birth
     * @param male        true if the PIN is generated for a male, false for a female
     * @return the generated 12 digit PIN number
     */
    public long generateTemporaryPINNumber(Date dateOfBirth, boolean male);
}
