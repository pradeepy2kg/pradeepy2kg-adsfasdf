package lk.rgd.common.util;

import lk.rgd.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * this is the util class to load death type
 */
public class DeathTypeUtil {
    private static final Logger logger = LoggerFactory.getLogger(MarriedStatusUtil.class);

    /**
     * return death type in given language
     *
     * @param type     DeathRegister type
     * @param language si/ta
     * @return death type in given language
     */
    public static String getDeathType(int type, String language) {

        if (AppConstants.SINHALA.equals(language)) {
            switch (type) {
                case 0:
                    return "සාමාන්‍ය මරණ/Normal Death";
                case 1:
                    return "හදිසි මරණ/Sudden Death ";
                case 2:
                    return "කාලය ඉකුත් වූ සාමාන්‍ය මරණ /Late registration,Normal Death ";
                case 3:
                    return "කාලය ඉකුත් වූ හදිසි මරණ/Late registration,Sudden Death ";
                case 4:
                    return "නැතිවුණු පුද්ගලයෙකුගේ මරණ/Death of missing person";
            }
        } else if (AppConstants.TAMIL.equals(language)) {
            switch (type) {
                case 0:
                    return "சாதாரண மரணம்/Normal Death";
                case 1:
                    return "திடீா் மரணம் /Sudden Death";
                case 2:
                    return "Late registration,Normal Death in tamil";
                case 3:
                    return "Late registration,Sudden Death  in tamil";
                case 4:
                    return "Death of missing person in tamil";
            }
        }  else {
            logger.error("Invalid language : {}", language);
            throw new IllegalArgumentException("Invalid language : " + language);
        }
        logger.error("Invalid married status : {}", type);
        throw new IllegalArgumentException("Invalid married status : " + type);
    }
}
