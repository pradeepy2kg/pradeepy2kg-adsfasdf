package lk.rgd;

/**
 * Defines error codes used by the system. These maybe converted into users' language by the UI layer
 *
 * @author asankha
 */
public class ErrorCodes {

    public static final int UNKNOWN_ERROR = 9999;

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
     * Data requested for a non existant country id
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

    //marriage registration related error codes 6001-6999
    public static final int MR_INCOMPLETE_OBJECT = 6001;
    public static final int INVALID_STATE_FOR_APPROVAL = 6002;
}
