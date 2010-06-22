package lk.rgd;

/**
 * Defines application-wide constants shared by both CRS and PRS
 *
 * @author asankha
 */
public class AppConstants {

    /** The parameter key that holds the number of days until a password expires */
    public static final String PASSWORD_EXPIRY_DAYS = "rgd.password_expiry_days";

    public static final String EMPTY_STRING = "";

    public static final int GENDER_MALE = 0;
    public static final int GENDER_FEMALE = 1;
    public static final int GENDER_UNKNOWN = 2;

    public static final int INFORMANT_MOTHER = 0;
    public static final int INFORMANT_FATHER = 1;
    public static final int INFORMANT_GUARDIAN = 2;

    public static final String HOME_DIRECTORY = System.getProperty("POPREG_HOME");

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

}
