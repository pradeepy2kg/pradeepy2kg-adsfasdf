package lk.rgd;

/**
 * @author asankha
 */
public final class Permission {
    public static final int USER_MANAGEMENT = 2;
    public static final int EDIT_BDF = 3;
    public static final int PRINT_BDF = 4;
    public static final int PRINT_BIRTH_CERTIFICATE = 5;
    public static final int APPROVE_BDF = 6;
    public static final int APPROVE_BDF_CONFIRMATION = 7;
    public static final int EDIT_ADOPTION = 8;
    public static final int APPROVE_ADOPTION = 9;
    public static final int APPROVE_DEATH = 10;
    public static final int EDIT_DEATH = 11;
    public static final int PRINT_DEATH_CERTIFICATE = 12;
    public static final int REGISTRAR_MANAGEMENT = 13;
    public static final int EVENTS_MANAGEMENT = 14;
    public static final int SERVICE_MASTER_DATA_MANAGEMENT = 15;
    public static final int USER_PREFERENCES = 16;
    public static final int SEARCH_PRS = 17;
    public static final int SEARCH_BDF = 18;
    public static final int EDIT_BDF_CONFIRMATION = 19;
    public static final int APPROVE_BDF_BELATED = 20;
    public static final int EDIT_BIRTH_ALTERATION = 21;
    public static final int APPROVE_BIRTH_ALTERATION = 22;
    public static final int SEARCH_DDF = 23;
    public static final int PRINT_DDF = 24;
    public static final int INDEX_RECORDS = 25;
    public static final int REGISTRAR_MANAGEMENT_ADR = 26;
    public static final int EDIT_MARRIAGE = 27;
    public static final int SEARCH_MARRIAGE = 28;
    public static final int PRINT_MARRIAGE_CERTIFICATE = 29;
    public static final int APPROVE_MARRIAGE = 30;
    public static final int ADD_MARRIAGE = 31;
    public static final int DELETE_MARRIAGE = 32;
    public static final int PRINT_MARRIAGE_LICENSE = 33; //can use this permission bit for printing license/certificate ...
    public static final int PRINT_MARRIAGE_EXTRACT = 34;
    public static final int VIEW_SCANNED_MARRIAGE_CERT = 35;
    public static final int SEARCH_REGISTRAR = 36;

    // permission for PRS
    public static final int PRS_ADD_PERSON = 50;
    public static final int PRS_EDIT_PERSON = 51;
    public static final int PRS_LOOKUP_PERSON_BY_KEYS = 52;
    public static final int PRS_VIEW_PERSON = 53;
    public static final int PRS_APPROVE_PERSON = 54;
    public static final int PRS_DELETE_PERSON = 55;
    public static final int PRS_REJECT_PERSON = 56;
    public static final int PRS_PRINT_CERT = 57;
    public static final int PRS_MARK_CERT_PRINTED = 58;
    public static final int PRS_EDIT_PERSON_AFTER_APPROVE = 59;
}
