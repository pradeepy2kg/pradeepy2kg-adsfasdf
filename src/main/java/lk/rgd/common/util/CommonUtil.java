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
