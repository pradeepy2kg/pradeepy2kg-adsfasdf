package lk.rgd.common.util;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.web.util.MarriageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: TO be removed
public class MarriageTypeUtil {

    private static final Logger logger = LoggerFactory.getLogger(MarriageTypeUtil.class);

    public static String getMarriageTypeInOfficialLanguageAndEnglish(MarriageType marriageType, String language) {
        if (language.equals(AppConstants.SINHALA)) {

            switch (marriageType) {
                case GENERAL:
                    return "සාමාන්‍ය";
                case KANDYAN_DEEGA:
                    return "උඩරට දීග";
                case KANDYAN_BINNA:
                    return "උඩරට බින්න";
                case MUSLIM:
                    return "මුස්ලිම්";
            }
        } else if (language.equals(AppConstants.TAMIL)) {
            switch (marriageType) {
                case GENERAL:
                    return "general in ta";
                case KANDYAN_DEEGA:
                    return "kandyan deega in ta";
                case KANDYAN_BINNA:
                    return "kandyan binna in ta";
                case MUSLIM:
                    return "muslim in ta";
            }
        } else if (language.equals(AppConstants.ENGLISH)) {
            switch (marriageType) {
                case GENERAL:
                    return "General";
                case KANDYAN_DEEGA:
                    return "Kandyan Deega";
                case KANDYAN_BINNA:
                    return "Kandyan Binna";
                case MUSLIM:
                    return "Muslim";
            }
        } else {
            handleException("invalid language " + language, ErrorCodes.INVALID_DATA);
        }
        return null;
    }

    private static void handleException(String msg, int errorCode) {
        logger.error(msg);
        throw new CRSRuntimeException(msg, errorCode);
    }
}

