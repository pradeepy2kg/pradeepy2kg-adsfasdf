package lk.rgd.crs;

/**
 * Defines error codes used by the system. These maybe converted into users' language by the UI layer
 *
 * @author asankha
 */
public class ErrorCodes {

    // error codes from 1000 - 200 indicate invalid data for a request

    /** Data requested for an unsupported language */
    public static final int INVALID_LANGUAGE = 1000;
    /** Data requested for a non-existent district */
    public static final int INVALID_DISTRICT = 1001;
}
