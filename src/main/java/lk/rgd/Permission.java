package lk.rgd;

/**
 * @author asankha
 */
public final class Permission {

    public static final int USER_MANAGEMENT = 2;
    public static final int EDIT_BDF = 3;
    public static final int PRINT_BDF = 4;
    public static final int APPROVE_BDF = 5;
    public static final int APPROVE_BDF_CONFIRMATION = 6;
    public static final int EDIT_ADOPTION = 7;
    public static final int APPROVE_ADOPTION = 8;
    public static final int APPROVE_DEATH = 9;
    public static final int EDIT_DEATH = 10;


    //permission for pages
    public static final int PAGE_CREATE_USER = 25;
    public static final int PAGE_BIRTH_REGISTRATON_INIT = 26;
    public static final int PAGE_BIRTH_CONFIRMATION_REPORT = 34;
    public static final int PAGE_BIRTH_CONFIRMATION_PRINT = 28;
    public static final int PAGE_BIRTH_CONFIRMATION_INIT = 29;
    public static final int PAGE_BIRTH_REGISTRATION_APPROVAL = 27;
    public static final int PAGE_USER_PREFERANCE_SELECT = 31;
    public static final int PAGE_VIEW_USERS = 32;
    public static final int PAGE_BIRTH_CONFIRMATION_SEARCH = 35;
    public static final int PAGE_BIRTH_CONFIRMATION_APPROVAL = 30;
    public static final int PAGE_BIRTH_CERTIFICATE_PRINT = 33;
    public static final int PAGE_STILL_BIRTH_REGISTRATION = 36;
    public static final int PAGE_BIRTH_REGISTRATON = 37;
    public static final int PAGE_BIRTH_CONFIRMATION = 38;
    public static final int PAGE_BIRTH_CONFIRMATION_APPROVAL_REFRESH = 39;
    public static final int PAGE_BIRTH_DECLARATION_APPROVAL_REFRESH = 40;
    public static final int PAGE_BIRTH_CERTIFICATE_PRINT_LIST_REFRESH = 41;
    public static final int PAGE_BIRTH_CONFIRMATION_PRINT_LIST_REFRESH = 42;
    public static final int PAGE_BIRTH_CERTIFICATE_PRINT_SELECTED_ENTRY = 43;
    public static final int PAGE_BIRTH_CONFIRMATION_PRINT_SELECTED_ENTRY = 44;
    public static final int PAGE_BIRTH_CONFIRMATION_BULK_PRINT = 45;
    public static final int PAGE_BIRTH_CERTIFICATE_BULK_PRINT = 46;
    public static final int PAGE_BIRTH_REGISTRATON_HOME = 47;
    public static final int PAGE_BIRTH_REGISTRATON_STILL_BIRTH_HOME = 48;
    public static final int PAGE_BIRTH_REGISTRATION_SEARCH_BY_SERIALNO = 49;
    public static final int PAGE_BIRTH_REGISTRATION_SEARCH_BY_IDUKEY = 70;
    public static final int PAGE_BIRTH_CONFIRMATION_APPROVAL_NEXT = 71;
    public static final int PAGE_BIRTH_CONFIRMATION_APPROVAL_PREVIOUS = 71;
    public static final int PAGE_BIRTH_DECLARATION_APPROVE_BULK = 72;
    public static final int PAGE_BIRTH_CONFIRMATION_APPROVE_BULK = 73;
    public static final int PAGE_BIRTH_DECLARATION_APPROVE_SELECTED = 74;
    public static final int PAGE_BIRTH_REGISTRATION_STILL_BIRTH_CERTIFICATE_PRINT = 75;
    public static final int PAGE_BIRTH_DECLARATION_APPROVAL_NEXT = 76;
    public static final int PAGE_BIRTH_DECLARATION_APPROVAL_PREVIOUS = 77;
    public static final int PAGE_BIRTH_DECLARATION_APPROVAL_DELETE = 78;
    public static final int PAGE_BIRTH_DECLARATION_APPROVAL_REJECT = 79;
    public static final int PAGE_BIRTH_DECLARATION_APPROVAL_IGNORING_WARNING = 80;
    public static final int PAGE_BIRTH_CONFIRMATION_APPROVE_SELECTED = 81;
    public static final int PAGE_BIRTH_CONFIRMATION_APPROVAL_IGNORING_WARNING = 82;
    public static final int PAGE_BIRTH_CONFIRMATION_APPROVAL_REJECT_SELECTED = 83;
    public static final int PAGE_BIRTH_REGISTRATON_DIRECT_APPROVE = 84;
    public static final int PAGE_BIRTH_REGISTRATON_DIRECT_APPROVAL_IGNORING_WARNINGS = 85;
    public static final int PAGE_BIRTH_REGISTRATON_DIRECT_PRINT = 86;
    public static final int PAGE_BIRTH_REGISTRATION_STILL_BIRTH_CERTIFICATE_DIRECT_PRINT = 87;
    public static final int PAGE_BIRTH_CERTIFICATE_PRINT_LIST_NEXT = 88;
    public static final int PAGE_BIRTH_CERTIFICATE_PRINT_LIST_PREVIOUS = 89;
    public static final int PAGE_BIRTH_CONFIRMATION_PRINT_LIST_NEXT = 90;
    public static final int PAGE_BIRTH_CONFIRMATION_PRINT_LIST_PREVIOUS = 91;
    public static final int PAGE_ADVANCE_SEARCH_BIRTHS = 92;
    public static final int DELETE_USER = 93;
    public static final int PAGE_USER_CREATION = 94;
    public static final int VIEW_SELECTED_USERS = 95;
    public static final int CHANGE_PASSWORD = 96;
    public static final int BACK_CHANGE_PASSWORD = 97;
    public static final int CHANGE_PASSWORD_PAGE_LOAD = 98;
    public static final int PAGE_USER_PREFERENCE_INIT = 99;
    public static final int PAGE_BIRTH_DECLARATION_APPROVAL_REJECT_SELECTED = 100;
    public static final int PAGE_BIRTH_CONFIRMATION_SKIP_CONFIRMATIONCHANGES = 101;
    public static final int PAGE_BIRTH_REGISTRATON_DIRECT_HOME = 102;
    public static final int PAGE_BIRTH_CERTIFICATE_SEARCH = 103;
    public static final int PAGE_BIRTH_REGISTRATION_SEARCH_VIEW_NON_EDITABLE_MODE = 104;
    public static final int PAGE_BIRTH_CONFIRMATION_DIRECT_APPROVAL = 105;
    public static final int PAGE_BIRTH_CONFIRMATION_DIRECT_PRINT_BIRTH_CERTIFICATE = 106;
    public static final int PAGE_BIRTH_CONFIRMATION_DIRECT_APPROVAL_IGNORING_WARNINGS = 107;
    public static final int PAGE_BIRTH_CONFIRMATION_FORM_DETAIL_DIRECT_PRINT_BIRTH_CERTIFICATE = 108;

    // permission for PRS
    public static final int PRS_ADD_PERSON = 50;
    public static final int PRS_EDIT_PERSON = 51;
    public static final int PRS_LOOKUP_PERSON_BY_KEYS = 52;
    public static final int PAGE_ADVANCE_SEARCH_PRS = 53;

    //permission for Adoptions
    public static final int PAGE_ADOPTION_REGISTRATION_HOME = 109;
    public static final int PAGE_ADOPTION_REGISTRATION = 110;
    public static final int PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT = 111;
    public static final int PAGE_ADOPTION_RE_REGISTRATION = 112;
    public static final int PAGE_ADOPTION_INIT = 113;
    public static final int PAGE_ADOPTION_APPLICANT_INFO = 114;
    public static final int PAGE_ADOPTION_CERTIFICATE = 115;
    public static final int PAGE_ADOPTION_BDF_ENTRY = 116;
    public static final int PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT_VIEW_MODE = 117;
    public static final int PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT_APPROVE_SELECTED = 118;
    public static final int PAGE_ADOPTION_REGISTRATION_APPROVAL_AND_PRINT_REJECT_SELECTED = 119;
    public static final int PAGE_ADOPTION_BDF_HOME = 120;


    //permission for death
    public static final int PAGE_DEATH_REGISTRATION_INIT = 121;
    public static final int PAGE_DEATH_REGISTRATION = 122;
    public static final int PAGE_LATE_DEATH_REGISTRATION = 123;
    public static final int PAGE_DEATH_CERTIFICATE = 124;
    public static final int PAGE_LATE_DEATH_HOME = 125;
	public static final int PAGE_LATE_DEATH_REGISTRATION_INIT = 126;
    public static final int PAGE_DEATH_APPROVAL_PRINT = 127;
    public static final int PAGE_DEATH_APPROVAL_PRINT_LIST_REFRESH = 128;
    public static final int PAGE_DEATH_EDIT_MODE = 129;
    public static final int PAGE_DEATH_APPROVE = 130;
    public static final int PAGE_DEATH_REJECT = 131;
    public static final int PAGE_DEATH_DELETE = 132;
    public static final int PAGE_DEATH_VEIW_MODE = 133;
    public static final int PAGE_DEATH_PRINT = 134;


}                                                                   
