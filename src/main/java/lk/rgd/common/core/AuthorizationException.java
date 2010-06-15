package lk.rgd.common.core;

import lk.rgd.common.RGDException;

/**
 * @author asankha
 */
public class AuthorizationException extends RGDException {

    public AuthorizationException(String message, int errorCode) {
        super(message, errorCode);
    }

    public AuthorizationException(String message, int errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static final int INVALID_USER_OR_PASSWORD = 1001;
   
}
