package lk.rgd.crs.web;

import java.util.List;

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
    public static final String SESSION_USER_MENUE_LIST = "allowed_menue";

    public static final String SESSION_PRINT_COUNT = "printCount";

    public static final String SESSION_BIRTH_DECLARATION_BEAN = "birthRegister";
    public static final String SESSION_BIRTH_CONFIRMATION_BEAN = "birthConfirmation";
    public static final String SESSION_BIRTH_ALTERATION_BEAN = "birthAlteration";

    public static final String DEFAULT_PASS = "password";
    public static final String SESSION_REQUEST_CONTEXT = "context";
    public static final String SESSION_BIRTH_CONFIRMATION_DB_BEAN = "birthConfirmation_db";
    public static final String REQUEST_PIN_NIC = "pinOrNic";
    public static final String DIVISION_ID = "id";
    public static final String MODE = "mode";
    public static final String USER_LOCATION_ID = "userLocationId";
    public static final String SESSION_ADOPTION_ORDER = "adoption_order";
    public static final String SESSION_OLD_BD_FOR_ADOPTION = "oldBdfForAdoption";

    public static final String SESSION_DEATH_DECLARATION_BEAN = "deathRegister";
    public static final String SESSION_USER_ADMIN = "ADMIN";

    public static final String SESSION_EXSISTING_REGISTRAR = "exsisting_registrar";
    public static final String SESSION_UPDATED_ASSIGNMENT_REGISTRAR = "updated_assignment";
    public static final String SESSION_REGISTRAR = "current_updating_registrar";

    public static final int BIRTH_ALTERATION_APPROVE_ALT27A = 19;
    public static final int BIRTH_ALTERATION_APPROVE_ALT52_1 = 18;
    public static final int BIRTH_ALTERATION_APPROVE_ALT27 = 2;
    public static final boolean BIRTH_ALTERATION_APPROVE_TRUE = true;

    //to track wtich has change in death alterations
    public static final int DEATH_ALTERATION_APPROVE = 23;

    public static final String SESSION_UPDATED_USER = "updated_user";
    public static final String USER_ID = "userId";
    public static final String CERTIFICATE_ID = "certificateId";
    public static final String PERSON_ID = "personId";
    public static final String SEARCH_USERS_LIST = "viewUsers";

    //to indicate time durations for statistics
    public static final int WEEK = 1;
    public static final int MONTH = 2;
    public static final int YEAR = 3;
    public static final long DAY_IN_MILLISECONDS = 1000 * 60 * 60 * 24;
   

}
