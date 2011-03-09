package lk.rgd.common.util;

import lk.rgd.AppConstants;
import lk.rgd.prs.api.domain.Marriage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to help to display localized marriage status in PRS
 *
 * @author Chathuranga Withana
 */
public class MarriageStateUtil {

    private static final Logger logger = LoggerFactory.getLogger(MarriageStateUtil.class);

    /**
     * Return marriage status string in selected language
     *
     * @param state    the marriage status enumeration
     * @param language selected language
     * @return the married status string for selected language and enumeration
     * @see lk.rgd.prs.api.domain.Marriage.State
     */
    public static String getCivilStatus(Marriage.State state, String language) {

        if (AppConstants.SINHALA.equals(language)) {
            switch (state) {
                case MARRIED:
                    return "විවාහක";
                case ANNULLED:
                    return "ශුන්‍ය කර ඇත";
                case DIVORCED:
                    return "දික්කසාද";
            }
        } else if (AppConstants.TAMIL.equals(language)) {
            switch (state) {
                case MARRIED:
                    return "திருமணமாணவர் ";
                case ANNULLED:
                    return "தள்ளிவைத்தல்";
                case DIVORCED:
                    return "DIVORCED TAMIL";
            }
        } else if (AppConstants.ENGLISH.equals(language)) {
            switch (state) {
                case MARRIED:
                    return "MARRIED";
                case ANNULLED:
                    return "ANNULLED";
                case DIVORCED:
                    return "DIVORCED";
            }
        } else {
            logger.error("Invalid language : {}", language);
            throw new IllegalArgumentException("Invalid language : " + language);
        }
        logger.error("Invalid Marriage status : {}", state);
        throw new IllegalArgumentException("Invalid Marriage status : " + state);
    }
}
