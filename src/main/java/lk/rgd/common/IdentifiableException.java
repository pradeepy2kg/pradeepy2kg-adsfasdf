package lk.rgd.common;

/**
 * Exceptions that retain an event ID for debugging and reporting
 * 
 * @author asankha
 */
public interface IdentifiableException {

    public long getEventId();

    public void setEventId(long eventId);

    public int getErrorCode();
}
