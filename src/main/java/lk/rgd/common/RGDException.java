package lk.rgd.common;

/**
 * @author asankha
 */
public class RGDException extends Exception {

    private int errorCode;

    public RGDException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public RGDException(String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public RGDException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getErrorCode() {
        return errorCode;
    }
}