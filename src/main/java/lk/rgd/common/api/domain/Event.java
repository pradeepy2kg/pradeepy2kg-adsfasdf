package lk.rgd.common.api.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Represents a eCivil Registration system event. An event could be an error, situation or user action.
 *
 * Events are persisted into an event database and audit/reporting results generated against them.
 * @author asankha
 */
@Entity
@Table(name = "EVENT", schema = "COMMON")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUKey;
    @Column(updatable = false, nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date timestamp;
    @Column(updatable = false, nullable = false)
    private int eventType;
    @Column(updatable = false, nullable = false)
    private int eventCode;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = true, updatable = false)
    private User user;
    @Column(updatable = false, nullable = true)
    private long primaryRecordId;
    @Column(updatable = false, nullable = true)
    private String eventData;

    public long getIdUKey() {
        return idUKey;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
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

    public long getPrimaryRecordId() {
        return primaryRecordId;
    }

    public void setPrimaryRecordId(long primaryRecordId) {
        this.primaryRecordId = primaryRecordId;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }
}
