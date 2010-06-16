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
}
