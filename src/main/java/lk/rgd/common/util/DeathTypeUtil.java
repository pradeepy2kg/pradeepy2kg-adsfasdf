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
                    return "සාමාන්‍ය මරණ / Normal Death";
                case 1:
                    return "හදිසි මරණ / Sudden Death";
                case 2:
                    return "කාලය ඉකුත් වූ සාමාන්‍ය මරණ / Late Registration, Normal Death";
                case 3:
                    return "කාලය ඉකුත් වූ හදිසි මරණ / Late Registration, Sudden Death";
                case 4:
                    return "නැතිවුණු පුද්ගලයෙකුගේ මරණ / Death of Missing Person";
            }
        } else if (AppConstants.TAMIL.equals(language)) {
            switch (type) {
                case 0:
                    return "இயற்கை மரணம்  / Normal Death";
                case 1:
                    return "திடீர்  மரணம்  / Sudden Death";
                case 2:
                    return "காலங்கடந்த பதிவு, இயற்கை மரணம் / Late Registration, Normal Death";
                case 3:
                    return "காலங்கடந்த பதிவு, திடீர்  மரணம் / Late Registration, Sudden Death";
                case 4:
                    return "காணாமற் போன நபரின் இறப்பு / Death of Missing Person";
            }
        } else {
            logger.error("Invalid language : {}", language);
            throw new IllegalArgumentException("Invalid language : " + language);
        }
        logger.error("Invalid married status : {}", type);
        throw new IllegalArgumentException("Invalid married status : " + type);
    }
}
