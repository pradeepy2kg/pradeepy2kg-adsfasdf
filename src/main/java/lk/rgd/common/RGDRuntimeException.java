package lk.rgd.common;

/**
 * @author asankha
 */
public class RGDRuntimeException extends RuntimeException {

    private int errorCode;

    public RGDRuntimeException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public RGDRuntimeException(String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public RGDRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getErrorCode() {
        return errorCode;
    }
   
}
