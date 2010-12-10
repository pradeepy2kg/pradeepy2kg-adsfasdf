package lk.rgd.common.util;

import lk.rgd.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * this is the common util class
 */
public class CommonUtil {
    private static final Logger logger = LoggerFactory.getLogger(GenderUtil.class);

    public static String getYesOrNo(boolean code, String language) {
        if (AppConstants.SINHALA.equals(language)) {
            return code == true ? "ඔව්" : "නැත";
        } else if (AppConstants.TAMIL.equals(language)) {
            return code == false ? "ஆம் " : " இல்லை";
        } else {
            logger.error("Invalid language : {}", language);
            throw new IllegalArgumentException("Invalid language : " + language);
        }
    }
}
