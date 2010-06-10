package lk.rgd.crs.api;

/**
 * Holds constants used in the CRS birth registration module
 *
 * @author asankha
 */
public class BirthConstants {

    /**
     * The parameter key that holds the number of days for a birth to be considered as a late registration
     * A late registration *MUST* be approved by an ADR or higher authority to be entered into the system 
     */
    public static final String CRS_BIRTH_LATE_REG_DAYS = "crs.birth.late_reg_days";

    /**
     * The parameter key that holds the number of days for a birth to be considered as a belated registration.
     * A belated registration *MUST* be approved by an ARG or higher authority
     */
    public static final String CRS_BIRTH_BELATED_REG_DAYS = "crs.birth.belated_reg_days";
}
