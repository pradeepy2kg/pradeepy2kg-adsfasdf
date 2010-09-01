package lk.rgd.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.AppConstants;

/**
 * @author Chathuranga Withana
 */
public class MarriedStatusUtil {

    private static final Logger logger = LoggerFactory.getLogger(MarriedStatusUtil.class);

    public static String getMarriedStatus(int code, String language) {

        if (AppConstants.SINHALA.equals(language)) {
            switch (code) {
                case 0: return "නොදනී";
                case 1: return "විවාහිකයි";
                case 2: return "අවිවාහකයි";
                case 3: return "පසු විවාහයකි";
            }
        } else if (AppConstants.TAMIL.equals(language)) {
            switch (code) {
                case 0: return "Unknown";
                case 1: return "Married Tamil";
                case 2: return "Unmarried Tamil";
                case 3: return "Married Later Tamil";
            }
        } else if (AppConstants.ENGLISH.equals(language)) {
            switch (code) {
                case 0: return "Unknown";
                case 1: return "Married";
                case 2: return "Unmarried";
                case 3: return "Married Later";
            }
        } else {
            logger.error("Invalid language : {}", language);
            throw new IllegalArgumentException("Invalid language : " + language);
        }
        logger.error("Invalid gender code : {}", code);
        throw new IllegalArgumentException("Invalid gender code : {}");
    }
}
