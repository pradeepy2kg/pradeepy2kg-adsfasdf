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
    /**
     * Data requested for a non existant country id
     */
    public static final int COUNTRY_NOT_FOUND = 1002;

    // error codes 2001 - 3000 indicate permission denied errors
    /**
     * User does not have permission to perform the action
     */
    public static final int PERMISSION_DENIED = 2001;
    /**
     * The action cannot be performed on the objects' current state
     */
    public static final int ILLEGAL_STATE = 2002;

    //error code 3001-4000 indicate autharization errors
    public static final int AUTHRIZATION_FAILS_CREATE_USER = 3001;

    //erro code 4001-5000 indicate persistance exceptions
    public static int ENTITY_ALREADY_EXIST = 4001;
    public static int ENTITY_MANAGER_CLOSED = 4002;
    public static int NOT_AN_ENTITY = 4003;
    public static int PERSISTING_EXCEPTION_COMMON=4004;
}
