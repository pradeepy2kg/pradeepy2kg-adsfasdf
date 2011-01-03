package lk.rgd.common.util;

import lk.rgd.AppConstants;
import static lk.rgd.crs.api.domain.MarriageInfo.MarriedStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Chathuranga Withana
 */
public class MarriedStatusUtil {

    private static final Logger logger = LoggerFactory.getLogger(MarriedStatusUtil.class);

    /**
     * Return married status string in selected language
     *
     * @param state    the married status enumeration
     * @param language selected language
     * @return the married status string for selected language and enumeration
     * @see lk.rgd.crs.api.domain.MarriageInfo.MarriedStatus
     */
    public static String getMarriedStatus(MarriedStatus state, String language) {

        if (AppConstants.SINHALA.equals(language)) {
            switch (state) {
                case UNKNOWN:
                    return "නොදනී";
                case MARRIED:
                    return "විවාහකයි";
                case UNMARRIED:
                    return "අවිවාහකයි";
                case NO_SINCE_MARRIED:
                    return "නැත, නමුත් පසුව විවාහවී ඇත";
            }
        } else if (AppConstants.TAMIL.equals(language)) {
            switch (state) {
                case UNKNOWN:
                    return "தெரியாது";
                case MARRIED:
                    return "திருமணமாணவர்";
                case UNMARRIED:
                    return "திருமணமாகாதவர்";
                case NO_SINCE_MARRIED:
                    return "இல்லை, பின் விவாகமாணவா்கள்";
            }
        } else if (AppConstants.ENGLISH.equals(language)) {
            switch (state) {
                case UNKNOWN:
                    return "Unknown";
                case MARRIED:
                    return "Married";
                case UNMARRIED:
                    return "Unmarried";
                case NO_SINCE_MARRIED:
                    return "No, but since married";
            }
        } else {
            logger.error("Invalid language : {}", language);
            throw new IllegalArgumentException("Invalid language : " + language);
        }
        logger.error("Invalid married status : {}", state);
        throw new IllegalArgumentException("Invalid married status : " + state);
    }
}
