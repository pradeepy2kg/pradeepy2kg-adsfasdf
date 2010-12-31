package lk.rgd.prs.api.domain;

import lk.rgd.common.api.domain.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * An instance represents the core life cycle aspects for a registration record in the CRS
 *
 * For a Birth record for data entry state, these fields mean the data entry date/user and the approval user. For a
 * record in confirmation state
 */
@Embeddable
public class PRSLifeCycleInfo implements Serializable, Cloneable {

    // ----- Record approval or rejection information - optional ---------
    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date approvalOrRejectTimestamp;

    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "approvalOrRejectUserId")
    private User approvalOrRejectUser;

    // The database row level created timestamp (i.e. when a user first enters the record to the system)
    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdTimestamp;

    // The database row level created user (i.e. user entering the record to the system)
    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "createdUserId", nullable = true)
    private User createdUser;

    // The database row level last updated time
    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date lastUpdatedTimestamp;

    // The database row level last updated user
    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "lastUpdatedUserId", nullable = true)
    private User lastUpdatedUser;

    @Column(nullable = false, columnDefinition = "integer default 1")
    private boolean activeRecord = true;

    @Override
    protected PRSLifeCycleInfo clone() throws CloneNotSupportedException {
        return (PRSLifeCycleInfo) super.clone();
    }

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

    public Date getApprovalOrRejectTimestamp() {
        return approvalOrRejectTimestamp;
    }

    public void setApprovalOrRejectTimestamp(Date approvalOrRejectTimestamp) {
        this.approvalOrRejectTimestamp = approvalOrRejectTimestamp;
    }

    public User getApprovalOrRejectUser() {
        return approvalOrRejectUser;
    }

    public void setApprovalOrRejectUser(User approvalOrRejectUser) {
        this.approvalOrRejectUser = approvalOrRejectUser;
    }

    public boolean isActiveRecord() {
        return activeRecord;
    }

    public void setActiveRecord(boolean activeRecord) {
        this.activeRecord = activeRecord;
    }
}