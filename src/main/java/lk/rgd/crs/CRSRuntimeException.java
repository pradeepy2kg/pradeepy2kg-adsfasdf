package lk.rgd.crs;

import lk.rgd.common.RGDRuntimeException;

/**
 * @author asankha
 */
public class CRSRuntimeException extends RGDRuntimeException {

    public CRSRuntimeException(String message, int errorCode) {
        super(message, errorCode);
    }

    public CRSRuntimeException(String message, int errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }
}
