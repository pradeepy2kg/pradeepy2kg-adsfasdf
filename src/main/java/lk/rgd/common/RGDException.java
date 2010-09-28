package lk.rgd.common;

/**
 * @author asankha
 */
public class RGDException extends Exception implements IdentifiableException {

    private int errorCode;
    private long eventId;

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

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }
}