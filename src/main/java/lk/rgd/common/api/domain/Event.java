package lk.rgd.common.api.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Represents a eCivil Registration system event. An event could be an error, situation or user action.
 * <p/>
 * Events are persisted into an event database and audit/reporting results generated against them.
 *
 * @author asankha
 */
@Entity
@Table(name = "EVENT", schema = "COMMON")

@NamedQueries({
    @NamedQuery(name = "findAllEvents", query = "SELECT event FROM Event event ORDER BY event.timestamp DESC"),
    @NamedQuery(name = "filter.by.recorded.timestamp", query = "SELECT event FROM Event event " +
        "WHERE event.timestamp BETWEEN :startTime AND :endTime AND event.eventType= :eventType " +
        "ORDER BY event.timestamp DESC")
})
public class Event implements Serializable {
    private static final int STACK_TRACE_LEN = 2048;
    private static final int EVENT_DATA_LEN = 80;
    private static final int DEBUG_CLOB_LEN = 25 * 1024;

    public enum Type {
        AUDIT, ERROR
    }

    /**
     * A unique ID assigned to each recorded Event
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUKey;

    /**
     * Timestamp of recording
     */
    @Column(updatable = false, nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date timestamp;

    /**
     * Type - AUDIT or ERROR
     */
    @Column(updatable = false, nullable = false)
    private Type eventType;

    /**
     * Is the error code encountered for an exception event, or 0 for an audit event
     */
    @Column(updatable = false, nullable = false)
    private int eventCode;

    /**
     * User invoking action
     */
    @ManyToOne
    @JoinColumn(name = "userId", nullable = true, updatable = false)
    private User user;

    /**
     * The primary key of the main object on the database
     */
    @Column(updatable = false, nullable = true)
    private long recordId;

    /**
     * Invoked method of the service class
     */
    @Column(updatable = false, nullable = true)
    private String methodName;

    /**
     * Invoked service class
     */
    @Column(updatable = false, nullable = true)
    private String className;

    /**
     * Some information (possibly trimmed) of primitive or base input parameters
     */
    @Column(updatable = false, nullable = true, length = EVENT_DATA_LEN)
    private String eventData;

    /**
     * Debug information - populated only if enabled by the configuration, or on error
     */
    @Lob
    @Column(nullable = true, length = DEBUG_CLOB_LEN)
    private String debug;

    /**
     * The stack trace encountered, if any, for an exception
     */
    @Lob
    @Column(nullable = true, length = STACK_TRACE_LEN)
    private String stackTrace;

    public Event() {
        timestamp = new Date();
        eventType = Type.AUDIT;
    }

    public long getIdUKey() {
        return idUKey;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Type getEventType() {
        return eventType;
    }

    public void setEventType(Type eventType) {
        this.eventType = eventType;
    }

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        if (eventData != null && eventData.length() > EVENT_DATA_LEN) {
            this.eventData = eventData.substring(0, EVENT_DATA_LEN);
        } else {
            this.eventData = eventData;
        }
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        if (debug != null && debug.length() > DEBUG_CLOB_LEN) {
            this.debug = debug.substring(0, DEBUG_CLOB_LEN);
        } else {
            this.debug = debug;
        }
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        if (stackTrace != null && stackTrace.length() > STACK_TRACE_LEN) {
            this.stackTrace = stackTrace.substring(0, STACK_TRACE_LEN);
        } else {
            this.stackTrace = stackTrace;
        }
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
