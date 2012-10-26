package lk.rgd;

import java.util.Locale;

/**
 * Defines application-wide constants shared by both CRS and PRS
 *
 * @author asankha
 */
public class AppConstants {

    public enum Gender {
        MALE,
        FEMALE,
        UNKNOWN
    }

    public static final String EMPTY_STRING = "";
    public static final String FULL_STOP = ".";
    public static final String NEW_LINE = "\n";
    public static final String SPACE = " ";
    public static final String DASH = "-";
    public static final String SLASH = " / ";
    /**
     * Locale for Sri Lankan Sinhalese
     */
    public static final Locale LK_SI = new Locale("si", "lk");
    /**
     * Locale for Sri Lankan Tamil
     */
    public static final Locale LK_TA = new Locale("ta", "lk");
    /**
     * Locale for Sri Lankan English - defaults to en-US
     */
    public static final Locale LK_EN = new Locale("en", "US");

    /**
     * Language identifier for English
     */
    public static final String ENGLISH = "en";
    /**
     * Language identifier for Sinhala
     */
    public static final String SINHALA = "si";
    /**
     * Language identifier for Tamil
     */
    public static final String TAMIL = "ta";
    /**
     * Register Type for Birth
     */
    public static final String BIRTH = "Birth";
    /**
     * Register Type for Death
     */
    public static final String DEATH = "Death";
    /**
     * Register Type for Marriage
     */
    public static final String MARRIAGE = "Marriage";
    /**
     * DS Division of Division Types
     */
    public static final String DS_DIVISION = "DS";
    /**
     * District type
     */
    public static final String DISTRICT = "District";
    /**
     * None - nothing specified
     */
    public static final String NONE = "None";
    /**
     * All
     */
    public static final String ALL = "all";
    /**
     * Registration Mode of the Marriage Register
     */
    public static final String REGISTER = "register";
    /**
     * District id of the Colombo Consular and IdUKey of it.
     * Used for adoption of foreign children.
     */
    public static final int COLOMBO_CONSULAR_ID = 14;
    public static final int COLOMBO_CONSULAR_IDUKEY = 26;
}
