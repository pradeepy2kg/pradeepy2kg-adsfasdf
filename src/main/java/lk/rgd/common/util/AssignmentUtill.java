package lk.rgd.common.util;

import lk.rgd.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @authar amith jayasekara
 */
public class AssignmentUtill {
    private static final Logger logger = LoggerFactory.getLogger(GenderUtil.class);
    private static final String ASSIGNMENT_TYPE_BIRTH = "උපත්/ Birth/ பிறப்பு";
    private static final String ASSIGNMENT_TYPE_DEATH = "මරණ/ Death/ இறப்பு";
    private static final String ASSIGNMENT_TYPE_GENERAL_MARRIAGE = "සාමාන්‍ය විවාහ/ General Marriage/ பொது விவாகம் ";
    private static final String ASSIGNMENT_TYPE_KANDYAN_MARRIAGE = "උඩරට විවාහ/ Kandyan Marriage/ கண்டிய விவாகம் ";
    private static final String ASSIGNMENT_TYPE_MUSLIM_MARRIAGE = "මුස්ලිම් විවාහ/ Muslim Marriage/ முஸ்லிம் விவாகம் ";

    public static String getAssignmentType(int code) {
        switch (code) {
            case 0:
                return ASSIGNMENT_TYPE_BIRTH;
            case 1:
                return ASSIGNMENT_TYPE_DEATH;
            case 2:
                return ASSIGNMENT_TYPE_GENERAL_MARRIAGE;
            case 3:
                return ASSIGNMENT_TYPE_KANDYAN_MARRIAGE;
            case 4:
                return ASSIGNMENT_TYPE_MUSLIM_MARRIAGE;
        }
        logger.error("Invalid gender code : {}", code);
        throw new IllegalArgumentException("Invalid type code : {}");
    }


    public static String getAssignmentType(int code, String lang) {

        if (lang.endsWith(AppConstants.SINHALA)) {
            switch (code) {
                case 0:
                    return "උපත්";
                case 1:
                    return "මරණ";
                case 2:
                    return "සාමාන්‍ය විවාහ";
                case 3:
                    return "උඩරට විවාහ";
                case 4:
                    return "මුස්ලිම් විවාහ";
            }
        } else if (lang.endsWith(AppConstants.TAMIL)) {
            switch (code) {
                case 0:
                    return "பிறப்பு";
                case 1:
                    return "இறப்பு";
                case 2:
                    return "பொது விவாகம் ";
                case 3:
                    return "கண்டிய விவாகம் ";
                case 4:
                    return "முஸ்லிம் விவாகம் ";
            }
        } else if (lang.endsWith(AppConstants.ENGLISH)) {
            switch (code) {
                case 0:
                    return "Birth";
                case 1:
                    return "Death";
                case 2:
                    return "General Marriage";
                case 3:
                    return "Kandyan Marriage";
                case 4:
                    return "Muslim Marriage";
            }
        } else {
            logger.error("Invalid language : {}", lang);
            throw new IllegalArgumentException("Invalid language : " + lang);
            //invalid language code
        }
        logger.error("Invalid type code : {}", code);
        throw new IllegalArgumentException("Invalid gender code : " + code);
    }


}
