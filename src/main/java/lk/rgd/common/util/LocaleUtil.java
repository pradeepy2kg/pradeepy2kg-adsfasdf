package lk.rgd.common.util;

import lk.rgd.AppConstants;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mahesha
 */
public class LocaleUtil {
    private static final Logger logger = LoggerFactory.getLogger(LocaleUtil.class);

    private static final ResourceBundle rb_si =
        ResourceBundle.getBundle("lk/rgd/package_si", AppConstants.LK_SI);
    private static final ResourceBundle rb_ta =
        ResourceBundle.getBundle("lk/rgd/package_ta", AppConstants.LK_TA);
    private static final ResourceBundle rb_en =
        ResourceBundle.getBundle("lk/rgd/package_en", AppConstants.LK_EN);


    private static ResourceBundle getResourceBundle(String language) {
        ResourceBundle rb = rb_en;
        if (AppConstants.SINHALA.equals(language)) {
            rb = rb_si;
        } else if (AppConstants.TAMIL.equals(language)) {
            rb = rb_ta;
        } else if (AppConstants.ENGLISH.equals(language)) {
            rb = rb_en;
        } else {
            logger.error("Invalid Language");
        }
        return rb;
    }

    public static String getLocalizedString(String language, String string) {
        ResourceBundle rb = getResourceBundle(language);
        return rb.getString(string);
    }
}
