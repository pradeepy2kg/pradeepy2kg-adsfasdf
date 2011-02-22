package lk.rgd.common.util;

import lk.rgd.common.api.domain.User;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates PIN and NIC values
 *
 * @author asankha
 */
public class PinAndNicUtils {

    private static final Logger logger = LoggerFactory.getLogger(PinAndNicUtils.class);

    /**
     * Check if a valid PIN or NIC
     * PIN - <year> <date+gender> <serial-number> <check-digit> e.g. 1975 211 014 3
     * NIC - <year> <day> <number-4-digits> <letter-V-X> e.g. 75 211 1111 V
     *
     * @param pinOrNic the PIN or NIC as a String
     * @param ecivil   Population Registry
     * @param user     user invoking action
     * @return true if null, or valid
     */
    public static boolean isValidPINorNIC(String pinOrNic, PopulationRegistry ecivil, User user) {

        if (pinOrNic == null) {
            return true;
        }

        pinOrNic = pinOrNic.trim();

        if (pinOrNic.length() == 12 && isValidPIN(pinOrNic)) {
            long pin = Long.parseLong(pinOrNic);
            return ecivil.findPersonByPIN(pin, user) != null;
        } else if (pinOrNic.length() == 10) {
            return isValidNIC(pinOrNic);
        } else {
            logger.debug("Invalid NIC (Expected 10 characters) or PIN (Expected 12 numerical characters) : {}",
                pinOrNic);
            return false;
        }
    }

    /**
     * Check if a valid PIN - <century> <year> <day> <number-4-digits> e.g. 1 10 208 0001
     *
     * @param pin    the PIN to validate (Must have been issued by the PRS system and record on file)
     * @param ecivil Population Registry
     * @param user   user invoking action
     * @return true if pin is valid
     */
    public static boolean isValidPIN(long pin, PopulationRegistry ecivil, User user) {
        return ecivil.findPersonByPIN(pin, user) != null;
    }

    /**
     * Check if a valid NIC - <year> <day> <number-4-digits> <letter-V-X> e.g. 75 211 1111 V
     *
     * @param nic NIC to validate
     * @return true if nic is null or valid
     */
    public static boolean isValidNIC(String nic) {
        if (nic == null) {
            return true;
        }

        try {
            int year = Integer.parseInt(nic.substring(0, 2));
            int day = Integer.parseInt(nic.substring(2, 5));
            int number = Integer.parseInt(nic.substring(5, 9));
            String letter = nic.substring(9, 10);

            if ((day >= 367 && day <= 500) || (day >= 867)) {
                return false;
            }
            if (!"V".equals(letter) && !"X".equals(letter)) {
                return false;
            }
            return true;
        } catch (NumberFormatException ne) {
            logger.debug("Invalid NIC : {}", nic);
            return false;
        }
    }

    public static boolean isValidPIN(String pin) {
        if (pin == null) {
            return false;
        }

        try {
            int year = Integer.parseInt(pin.substring(0, 4));
            int date = Integer.parseInt(pin.substring(4, 7));
            int serial = Integer.parseInt(pin.substring(7, 11));
            int check = Integer.parseInt(pin.substring(11, 12));
            long num = Long.parseLong(pin.substring(0, 11));

            if ((year < 1700) || (year > 2200 && year < 6700) || (year > 7200)) {
                return false;
            }
            if ((date >= 367 && date <= 500) || (date >= 867)) {
                return false;
            }
            if (year >= 1994 && serial >= 2000) {
                return false;
            }
            if (check != computeCheckDigit(num)) {
                return false;
            }

            return true;
        } catch (NumberFormatException ne) {
            logger.debug("Invalid PIN (expected 12 numerical characters) : {}", pin);
            return false;
        }
    }

    /**
     * //11-(N1*8+N2*4+N3*3+N4*2+N5*7+N6*6+N7*5+N8*7+N9*4+N10*3+N11*2)%11
     *
     * @param number a 11 digit long
     * @return the 12th check digit
     */
    private static int computeCheckDigit(final long number) {
        char[] chars = Long.toString(number).toCharArray();
        if (chars.length != 11) {
            throw new IllegalArgumentException("Cannot compute check digit for number : " + number + " - not 11 digits");
        }

        int[] N = new int[11];
        for (int i = 0; i < 11; i++) {
            N[i] = (int) chars[i] - 48;
        }
        int check = 11 - ((N[0] * 8 + N[1] * 4 + N[2] * 3 + N[3] * 2 + N[4] * 7 + N[5] * 6 + N[6] * 5 + N[7] * 7 + N[8] * 4 + N[9] * 3 + N[10] * 2) % 11);

        if (check > 9) {
            return check - 10;
        } else {
            return check;
        }
    }
}
