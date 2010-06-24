package lk.rgd.crs;

/**
 * Defines error codes used by the system. These maybe converted into users' language by the UI layer
 *
 * @author asankha
 */
public class ErrorCodes {

    // error codes from 1000 - 2000 indicate invalid data for a request

    /**
     * Data requested for an unsupported language
     */
    public static final int INVALID_LANGUAGE = 1000;
    /**
     * Data requested for a non-existent district
     */
    public static final int INVALID_DISTRICT = 1001;
    /** non existing DS division */
    public static final int INVALID_DSDIVISION = 1004;
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

    // error codes 2001 - 3000 indicate permission denied errors
    /**
     * User does not have permission to perform the action
     */
    public static final int PERMISSION_DENIED = 2001;
    /**
     * The action cannot be performed on the objects' current state
     */
    public static final int ILLEGAL_STATE = 2002;

    //error code 3001-4000 indicate authorization errors
    public static final int AUTHORIZATION_FAILS_USER_MANAGEMENT = 3001;
    public static final int INVALID_LOGIN = 3002;

    //error code 4001-5000 indicate persistence exceptions
    public static int ENTITY_ALREADY_EXIST = 4001;
    public static int ENTITY_MANAGER_CLOSED = 4002;
    public static int NOT_AN_ENTITY = 4003;
    public static int PERSISTING_EXCEPTION_COMMON=4004;

    // PRS error codes
    public static int PRS_ADD_RECORD_DENIED = 5001;
    public static int PRS_EDIT_RECORD_DENIED = 5002;
    public static int PRS_LOOKUP_BY_KEYS_DENIED = 5003;
    public static int PRS_SEARCH_DENIED = 5003;
}
