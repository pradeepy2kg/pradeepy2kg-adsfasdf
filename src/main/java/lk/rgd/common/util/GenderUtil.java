package lk.rgd.common.util;

import lk.rgd.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author asankha
 */
public class GenderUtil {

    private static final Logger logger = LoggerFactory.getLogger(GenderUtil.class);

    public static String getGenderString(int code) {
        switch (code) {
            case 0: return "පිරිමි / #Male# / Male";
            case 1: return "ගැහැණු / #Female# / Female";
            case 2: return "නොදත් / #Unknown# / Unknown";
        }
        logger.error("Invalid gender code : {}", code);
        throw new IllegalArgumentException("Invalid gender code : {}");
    }

    public static String getGender(int code, String language) {

        if (AppConstants.SINHALA.equals(language)) {
            switch (code) {
                case 0: return "පිරිමි";
                case 1: return "ගැහැණු";
                case 2: return "නොදත්";
            }
        } else if (AppConstants.TAMIL.equals(language)) {
            switch (code) {
                case 0: return "#Male#ි";
                case 1: return "#Female#";
                case 2: return "#Unknown#";
            }
        } else if (AppConstants.ENGLISH.equals(language)) {
            switch (code) {
                case 0: return "Male";
                case 1: return "Female";
                case 2: return "Unknown";
            }
        } else {
            logger.error("Invalid language : {}", language);
            throw new IllegalArgumentException("Invalid language : " + language);
        }
        logger.error("Invalid gender code : {}", code);
        throw new IllegalArgumentException("Invalid gender code : {}");
    }
}
