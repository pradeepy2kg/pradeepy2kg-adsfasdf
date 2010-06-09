package lk.rgd.crs;

/**
 * Defines error codes used by the system. These maybe converted into users' language by the UI layer
 *
 * @author asankha
 */
public class ErrorCodes {

    // error codes from 1000 - 2000 indicate invalid data for a request

    /** Data requested for an unsupported language */
    public static final int INVALID_LANGUAGE = 1000;
    /** Data requested for a non-existent district */
    public static final int INVALID_DISTRICT = 1001;
    /** Data requested for a non existant country id */
    public static final int COUNTRY_NOT_FOUND = 1002;

    // error codes 2001 - 3000 indicate permission denied errors
    /** User does not have permission to perform the action */
    public static final int PERMISSION_DENIED = 2001;
    /** The action cannot be performed on the objects' current state */
    public static final int ILLEGAL_STATE = 2002;
}
