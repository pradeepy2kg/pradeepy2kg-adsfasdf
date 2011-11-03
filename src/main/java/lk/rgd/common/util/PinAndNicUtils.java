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
     * PIN - <century> <year> <day> <number-4-digits> e.g. 1 10 208 0001
     * NIC - <year> <day> <number-4-digits> <letter-V-X> e.g. 75 211 1111 V
     *
     * @param pinOrNic the PIN or NIC as a String
     * @param ecivil Population Registry
     * @param user user invoking action
     * @return true if null, or valid
     */
    public static boolean isValidPINorNIC(String pinOrNic, PopulationRegistry ecivil, User user) {

        if (pinOrNic == null) return true;

        pinOrNic = pinOrNic.trim();
        if (pinOrNic.length() == 10) {
            try {
                long pin = Long.parseLong(pinOrNic);
                return ecivil.findPersonByPIN(pin, user) != null;
            } catch (NumberFormatException e) {
                return isValidNIC(pinOrNic);
            }
        } else {
            logger.debug("Invalid NIC or PIN (Expected 10 characters) : {}", pinOrNic);
            return false;
        }
    }

    /**
     * Check if a valid PIN - <century> <year> <day> <number-4-digits> e.g. 1 10 208 0001
     * @param pin the PIN to validate (Must have been issued by the PRS system and record on file)
     * @param ecivil Population Registry
     * @param user user invoking action
     * @return true if pin is valid
     */
    public static boolean isValidPIN(long pin, PopulationRegistry ecivil, User user) {
        return ecivil.findPersonByPIN(pin, user) != null;
    }

    /**
     * Check if a valid NIC - <year> <day> <number-4-digits> <letter-V-X> e.g. 75 211 1111 V
     * @param nic NIC to validate
     * @return true if nic is null or valid
     */
    public static boolean isValidNIC(String nic) {
        if (nic == null) return true;

        try {
            int year = Integer.parseInt(nic.substring(0, 2));
            int day  = Integer.parseInt(nic.substring(2, 5));
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
}
