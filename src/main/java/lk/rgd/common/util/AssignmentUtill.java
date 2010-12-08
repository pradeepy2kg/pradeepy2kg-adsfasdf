package lk.rgd.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.AppConstants;
import lk.rgd.common.api.domain.Role;

/**
 * @authar amith jayasekara
 */
public class AssignmentUtill {
    private static final Logger logger = LoggerFactory.getLogger(GenderUtil.class);
    private static final String ASSIGNMENT_TYPE_BIRTH = "උපත්/birth/@birth@";
    private static final String ASSIGNMENT_TYPE_DEATH = "මරණ/death/@death@";
    private static final String ASSIGNMENT_TYPE_GENERAL_MARRIAGE = "සාමාන්‍ය විවාහ/general marriage/@general marriage@";
    private static final String ASSIGNMENT_TYPE_KANDYAN_MARRIAGE = "උඩරට විවාහ/kandyan marriage/@kandyan marriage@";
    private static final String ASSIGNMENT_TYPE_MUSLIM_MARRIAGE = "මුස්ලිම් විවාහ/muslim marriage/@muslim marriage@";

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
                    return "@birth@";

                case 1:
                    return "@death@";
                case 2:
                    return "@general marriage@";
                case 3:
                    return "@kandyan marriage@";
                case 4:
                    return "@muslim marriage@";
            }
        } else if (lang.endsWith(AppConstants.ENGLISH)) {
            switch (code) {
                case 0:
                    return "birth";
                case 1:
                    return "death";
                case 2:
                    return "general marriage";
                case 3:
                    return "kandyan marriage";
                case 4:
                    return "muslim marriage";
            }
        } else {
            logger.error("Invalid language : {}", lang);
            throw new IllegalArgumentException("Invalid language : " + lang);
            //invalid language code
        }
        logger.error("Invalid type code : {}", code);
        throw new IllegalArgumentException("Invalid gender code : {}");
    }


}
