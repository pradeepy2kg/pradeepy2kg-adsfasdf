package lk.rgd.common.api.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Base class for life cycle information about an entity
 *
 * @author asankha
 */
@Embeddable
public class BaseLifeCycleInfo implements Serializable {

    // ----- Database Row level information - not null ---------
    /**
     * Is this record currently considered as active?
     */
    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = true)
    //(nullable = false, columnDefinition = "TIMESTAMP default '0000-00-00 00:00:00'")
    @Temporal(value = TemporalType.TIMESTAMP)
    /**
     * The database row level created timestamp (i.e. when a user first enters the record to the system)
     */
    private Date createdTimestamp;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdUserId", nullable = true)
    /**
     * The database row level created user (i.e. user entering the record to the system)
     */
    private User createdUser;

    @Column(nullable = false, columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
    @Temporal(value = TemporalType.TIMESTAMP)
    /**
     * The database row level last updated time
     */
    private Date lastUpdatedTimestamp;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lastUpdatedUserId", nullable = true)
    /**
     * The database row level last updated user
     */
    private User lastUpdatedUser;

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }

    public Date getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(Date lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    public User getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    public void setLastUpdatedUser(User lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
