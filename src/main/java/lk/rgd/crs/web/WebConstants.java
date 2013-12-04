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
    public static final String SESSION_USER_MENUE_LIST = "allowed_menue";
    public static final String SESSION_NOTICE_WARNINGS = "notice_warnings";
    public static final String SESSION_NOTICE_SERIAL = "notice_serial";
    public static final String SESSION_NOTICE_MR_DIVISION_KEY = "notice_mr_idUKey";
    public static final String SESSION_NOTICE_RECEIVED_DATE = "notice_receive_date";


    public static final String SESSION_PRINT_COUNT = "printCount";

    public static final String SESSION_BIRTH_DECLARATION_BEAN = "birthRegister";
    public static final String SESSION_BIRTH_CONFIRMATION_BEAN = "birthConfirmation";
    public static final String SESSION_BIRTH_ALTERATION_BEAN = "birthAlteration";

    public static final String DEFAULT_PASS = "password";
    public static final String SESSION_REQUEST_CONTEXT = "context";
    public static final String SESSION_BIRTH_CONFIRMATION_DB_BEAN = "birthConfirmation_db";
    public static final String REQUEST_PIN_NIC = "pinOrNic";
    public static final String DIVISION_ID = "id";
    public static final String ZONAL_OFFICE_ID = "zonalOfficeId";
    public static final String MODE = "mode";
    public static final String TYPE = "type";
    public static final String WITHALL = "withAll";
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

    //TODO : to be removed
    //public static final String SESSION_UPDATED_USER = "updated_user";
    public static final String USER_ID = "userId";
    public static final String CERTIFICATE_ID = "certificateId";
    public static final String PERSON_ID = "personUKey";
    public static final String SEARCH_USERS_LIST = "viewUsers";
    public static final String CERTIFICATE_TYPE = "certificateType";
    public static final String PRS_CERTIFICATE = "prs";
    public static final String BIRTH_CERTIFICATE = "birth";
    public static final String DEATH_CERTIFICATE = "death";
    public static final String ADOPTION_CERTIFICATE = "adoption";

    //to indicate time durations for statistics
    public static final int WEEK = 1;
    public static final int MONTH = 2;
    public static final int YEAR = 3;
    public static final long DAY_IN_MILLISECONDS = 1000 * 60 * 60 * 24;

    //for statistics
    public static final String USER_TYPE = "userType";
    public static final String USER_ADR = "adr";
    public static final String USER_ARG = "arg";
    public static final String USER_DEO = "deo";
    public static final String USER_DR = "dr";
    public static final String USER_RG = "rg";
    public static final String USER_STAT="stat";

    //for statistics
    public static final String STAT_TYPE = "statType";
    public static final String STAT_ALL = "all";
    public static final String STAT_BIRTH = "birth";
    public static final String STAT_DEATH = "death";
    public static final String STAT_MARRIAGE = "marriage";

    public static final String TEXT_TO_TRANS = "text";
    public static final String GENDER = "gender";
    public static final String TEXT_TRANSLATED = "translated";

    public static final int STAMP_CHARGES = 25;
    // LocationUKey of the Head office.
    public static final int HEAD_OFFICE_UKEY = 1;
}
