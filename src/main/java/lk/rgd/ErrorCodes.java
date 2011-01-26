package lk.rgd;

/**
 * Defines error codes used by the system. These maybe converted into users' language by the UI layer
 *
 * @author asankha
 */
public class ErrorCodes {

    public static final int UNKNOWN_ERROR = 9999;
    public static final int CONTENT_REPOSITORY_TOO_LARGE = 9998;

    // error codes from 1000 - 2000 indicate invalid data for a request

    /**
     * Data requested for an unsupported language
     */
    public static final int INVALID_LANGUAGE = 1000;
    /**
     * Record does not satisfy minimum requirements for acceptance for storage
     */
    public static final int INVALID_DATA = 1001;
    /**
     * Data requested for a non existing country id
     */
    public static final int COUNTRY_NOT_FOUND = 1002;
    /**
     * A reason / comment is required for the action
     */
    public static final int COMMENT_REQUIRED_BDF_REJECT = 1003;
    /**
     * The BDF is not in a state where it can be can be approved/rejected
     */
    public static final int INVALID_STATE_FOR_BDF_APPROVAL = 1004;
    /**
     * The BDF is not in a state where bdf / confirmation can be approved
     */
    public static final int INVALID_STATE_FOR_BDF_CONFIRMATION = 1005;
    /**
     * The BDF is not in a state where bdf / confirmation can be rejected
     */
    public static final int INVALID_STATE_FOR_BDF_REJECTION = 1006;
    /**
     * The BDF is not in a state where confirmation changes can be captured
     */
    public static final int INVALID_STATE_FOR_CONFIRMATION_CHANGES = 1007;
    /**
     * non existing DS division
     */
    public static final int INVALID_DSDIVISION = 1008;
    /**
     * invalid PIN number
     */
    public static final int INVALID_PIN = 1009;
    /**
     * in complete death alteration
     */
    public static final int INCOMPLETE_ALTERATION = 1010;
    //duplicate BDF serial number
    public static final int POSSIBLE_DUPLICATION_OF_BDF_SERIAL_NUMBER = 1011;
    public static final int UNABLE_TO_UPDATE_ADOPTION_ORDER = 1012;


    // error codes 2001 - 3000 indicate permission denied errors
    /**
     * User does not have permission to perform the action
     */
    public static final int PERMISSION_DENIED = 2001;
    /**
     * The action cannot be performed on the objects' current state
     */
    public static final int ILLEGAL_STATE = 2002;
    /**
     * Error searching Solr index
     */
    public static final int INDEX_SEARCH_FAILURE = 2003;

    //error code 3001-4000 indicate authorization errors
    public static final int AUTHORIZATION_FAILS_USER_MANAGEMENT = 3001;
    public static final int INVALID_LOGIN = 3002;
    public static final int USER_IS_NOT_ALLOWED_FOR_LOCATION = 3003;

    //error code 4001-5000 indicate persistence exceptions
    public static int ENTITY_ALREADY_EXIST = 4001;
    public static int ENTITY_MANAGER_CLOSED = 4002;
    public static int NOT_AN_ENTITY = 4003;
    public static int PERSISTING_EXCEPTION_COMMON = 4004;

    // PRS error codes
    public static final int PRS_ADD_RECORD_DENIED = 5001;
    public static final int PRS_EDIT_RECORD_DENIED = 5002;
    public static final int PRS_LOOKUP_BY_KEYS_DENIED = 5003;
    public static final int PRS_APPROVE_RECORD_DENIED = 5004;
    public static final int INVALID_STATE_FOR_PRS_APPROVAL = 5005;
    public static final int PRS_DELETE_RECORD_DENIED = 5006;
    public static final int PRS_REJECT_RECORD_DENIED = 5006;
    public static final int COMMENT_REQUIRED_PRS_DELETE = 5007;
    public static final int COMMENT_REQUIRED_PRS_REJECT = 5008;
    public static final int PRS_INDEX_UPDATE_FAILED = 5009;
    public static final int PRS_DUPLICATE_NIC = 5010;
    public static final int PRS_CERT_MARK_AS_PRINTED_DENIED = 5011;
    public static final int PRS_EDIT_RECORD_DENIED_AFTER_APPROVE = 5012;

    //marriage registration related error codes 6001-6999
    public static final int MR_INCOMPLETE_OBJECT = 6001;
    public static final int INVALID_STATE_FOR_APPROVAL = 6002;
    public static final int EXISTING_ACTIVE_APPROVED_NOTICE = 6003;
    public static final int INVALID_STATE_FOR_REMOVAL = 6004;
    public static final int CAN_NOT_FIND_MARRIAGE_NOTICE = 6005;
    public static final int OTHER_PARTY_MUST_APPROVE_FIRST = 6006;
    public static final int INVALID_NOTICE_TYPE_FOR_ADD_SECOND = 6007;
    public static final int INVALID_NOTICE_STATE_FOR_REJECT = 6008;
    public static final int UNABLE_TO_REJECT_FEMALE_NOTICE = 6009;
    public static final int UNABLE_TO_REJECT_MALE_NOTICE = 6010;
    public static final int INVALID_STATE_FOR_PRINT_LICENSE = 6011;
    public static final int POSSIBLE_MARRIAGE_NOTICE_SERIAL_NUMBER_DUPLICATION = 6012;
    public static final int INVALID_SERIAL_NUMBER = 6013;
    public static final int INVALID_LICENSE_ISSUE_USER_OR_LOCATION = 6014;
    public static final int MARRIAGE_REGISTER_NOT_FOUND = 6015;
    public static final int INVALID_STATE_OF_MARRIAGE_REGISTER = 6016;
    public static final int INVALID_USER_ON_CERTIFYING_MARRIAGE_EXTRACT = 6017;
    public static final int INVALID_LOCATION_ON_ISSUING_MARRIAGE_EXTRACT = 6018;
    public static final int DUPLICATE_SERIAL_NUMBER = 6019;

    //birth alteration related error code 7001-7999
    public static final int INVALID_STATE_FOR_REJECT_BIRTH_ALTERATION = 7001;
    public static final int CAN_NOT_FIND_BIRTH_ALTERATION = 7002;
    public static final int CAN_NOT_ADD_A_NEW_ALTERATION_ON_THIS_SECTION = 7003;
    public static final int INVALID_STATE_FOR_DELETE_BIRTH_ALTERATION = 7004;
    public static final int INVALID_STATE_FOR_MARK_AS_PRINT_BIRTH_ALTERATION = 7005;

    //death registration error code 8001-8999
    public static final int POSSIBLE_DEATH_REGISTER_SERIAL_NUMBER_DUPLICATION = 8001;
    public static final int INCOMPLETE_DECLERENT = 8002;

}
