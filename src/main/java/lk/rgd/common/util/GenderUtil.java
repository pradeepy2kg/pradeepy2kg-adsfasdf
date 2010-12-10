package lk.rgd.common.util;

import lk.rgd.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author asankha
 */
public class GenderUtil {

    private static final Logger logger = LoggerFactory.getLogger(GenderUtil.class);
    private static final String GENDER_MALE_STRING = "පිරිමි / ஆண் / Male";
    private static final String GENDER_FEMALE_STRING = "ගැහැණු / பெண் / Female";
    private static final String GENDER_UNKNOWN_STRING = "නොදත් / தெரியாது / Unknown";

    public static String getGenderCharacter(int code) {
        switch (code) {
            case 0: return "M"; //'\u2642';
            case 1: return "F"; //'\u2640';
            case 2: return "U";
        }
        logger.error("Invalid gender code : {}", code);
        throw new IllegalArgumentException("Invalid gender code : {}");
    }

    public static String getGenderString(int code) {
        switch (code) {
            case 0: return GENDER_MALE_STRING;
            case 1: return GENDER_FEMALE_STRING;
            case 2: return GENDER_UNKNOWN_STRING;
        }
        logger.error("Invalid gender code : {}", code);
        throw new IllegalArgumentException("Invalid gender code : {}");
    }

    public static int getGenderCode(String langString) {
        if (GENDER_MALE_STRING.equals(langString)) return 0;
        if (GENDER_FEMALE_STRING.equals(langString)) return 1;
        return 2;
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
                case 0: return "ஆண்";
                case 1: return "பெண்";
                case 2: return "தெரியாது";
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
