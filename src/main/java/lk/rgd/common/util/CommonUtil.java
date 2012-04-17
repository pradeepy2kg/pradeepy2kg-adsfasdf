package lk.rgd.common.util;

import lk.rgd.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * this is the common util class
 */
public class CommonUtil {
    private static final Logger logger = LoggerFactory.getLogger(GenderUtil.class);
    private static final int DEFAULT_BUFFER_SIZE = 10240; // ..bytes = 10KB.

    private static final String SI_UNKNOWN = "නොදනී";
    private static final String TA_UNKNOWN = "தெரியாது";
    private static final String EN_UNKNOWN = "UNKNOWN";

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

    public static String getMailingAddress(String language) {
        return LocaleUtil.getLocalizedString(language, "officeMailAddress");
    }

    public static String getOfficeSignature(String language) {
        return LocaleUtil.getLocalizedString(language, "officeSignature");
    }

    public static String getUnknownForCertificate(String language) {
        if (AppConstants.SINHALA.equals(language)) {
            return SI_UNKNOWN;
        } else if (AppConstants.TAMIL.equals(language)) {
            return TA_UNKNOWN;
        } else if (AppConstants.ENGLISH.equals(language)) {
            return EN_UNKNOWN;
        } else {
            logger.error("Invalid language : {}. Only si and ta applicable", language);
            throw new IllegalArgumentException("Invalid language : " + language);
        }
    }

    public static void copyStreams(InputStream in, OutputStream out) throws IOException {
        try {
            byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignore) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
}
