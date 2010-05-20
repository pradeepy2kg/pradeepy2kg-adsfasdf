package lk.rgd.crs;

/**
 * @author asankha
 */
public class CRSRuntimeException extends RuntimeException {

    private int errorCode;

    public CRSRuntimeException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CRSRuntimeException(String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
