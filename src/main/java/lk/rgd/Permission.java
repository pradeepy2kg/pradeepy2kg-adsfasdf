package lk.rgd;

/**
 * @author asankha
 */
public final class Permission {

    public static final int USER_MANAGEMENT =2;
    public static final int EDIT_BDF = 3;
    public static final int PRINT_BDF = 4;
    public static final int APPROVE_BDF = 5;
    public static final int APPROVE_BDF_CONFIRMATION = 6;

    //permission for pages
    public static final int CREATE_USER_PAGE = 25;
    public static final int BIRTH_REGISTRATON_PAGE = 26;
    public static final int BIRTH_CONFIRMATION_REPORT_PAGE = 27;
    public static final int BIRTH_CONFIRMATION_PRINT_PAGE = 28;
    public static final int BIRTH_CONFIRMATION_PAGE = 29;
    public static final int BIRTH_REGISTRATION_APPROVAL_PAGE = 30;
    public static final int USER_PREFERANCE_SELECT_PAGE = 31;
    public static final int VIEW_USERS = 32;
    public static final int BIRTH_CONFIRMATION_SEARCH = 33;
    public static final int BIRTH_CONFIRMATION_APPROVAL_PAGE = 34;

    // permission for PRS
    public static final int PRS_ADD_PERSON  = 50;
    public static final int PRS_EDIT_PERSON = 51;
    public static final int PRS_LOOKUP_PERSON_BY_KEYS = 52;

}
