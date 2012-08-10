package lk.rgd.prs;

import lk.rgd.common.RGDRuntimeException;

/**
 * @author asankha
 */
public class PRSRuntimeException extends RGDRuntimeException {

    public PRSRuntimeException(String message, int errorCode) {
        super(message, errorCode);
    }

    public PRSRuntimeException(String message, int errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }
}