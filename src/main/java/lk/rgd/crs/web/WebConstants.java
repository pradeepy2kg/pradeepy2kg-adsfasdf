package lk.rgd.crs.web;

/**
 * This class defines all constants used in the Web modules
 *
 * @author Chathuranga
 * @author Indunil
 */
public class WebConstants {

    /**
     * session related constant values
     */
    public static final String SESSION_USER_LANG = "WW_TRANS_I18N_LOCALE";
    public static final String SESSION_USER_BEAN = "user_bean";

    public static final String SESSION_PRINT_START = "printStart";
    public static final String SESSION_PRINT_COUNT = "printCount";
    public static final String SESSION_PRINT_LIST = "printList";

    public static final String SESSION_BIRTH_DECLARATION_BEAN = "birthRegister";
    public static final String SESSION_BIRTH_CONFIRMATION_BEAN = "birthConfirmation";

    public static final String RETURN_TYPE_ERROR = "error";
    public static final String RETURN_TYPE_SUCCESS = "success";
    public static final String DATEFORMAT = "yyyy-MM-dd";

    public static final String RADIO_ALREADY_PRINT = "Printed";
    /**
     * no of rows per page in birth registration
     * approval pending
     */
    public static final int BR_APPROVAL_NO_OF_ROWS=4;
}
