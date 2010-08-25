package lk.rgd.common.api.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Represents a eCivil Registration system event. An event could be an error, situation or user action.
 *
 * Events are persisted into an event database and audit/reporting results generated against them.
 * @author asankha
 */
@Entity
@Table(name = "EVENT", schema = "COMMON")
public class Event implements Serializable {

    public enum Type {
        INFO, ERROR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUKey;

    @Column(updatable = false, nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(updatable = false, nullable = false)
    private Type eventType;

    @Column(updatable = false, nullable = false)
    private int eventCode;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = true, updatable = false)
    private User user;

    @Column(updatable = false, nullable = true)
    private long recordId;

    @Column(updatable = false, nullable = true)
    private String methodName;

    @Column(updatable = false, nullable = true)
    private String className;

    @Column(updatable = false, nullable = true)
    private String eventData;

    @Lob
    @Column(nullable = true, length = 25 * 1024)
    private String debug;

    @Lob
    @Column(nullable = true, length = 2048)
    private String stackTrace;

    public Event() {
        timestamp = new Date();
        eventType = Type.INFO;
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
        this.eventData = eventData;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
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
