package lk.rgd.crs.api.domain;

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
public class CRSLifeCycleInfo implements Serializable, Cloneable {

    // ----- Record approval or rejection information - optional ---------
    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date approvalOrRejectTimestamp;

    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "approvalOrRejectUserId")
    private User approvalOrRejectUser;

    // ----- Certificate print information - optional ---------
    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date certificateGeneratedTimestamp;

    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "certificateGeneratedUserId")
    private User certificateGeneratedUser;

    // ----- Database Row level information - not null ---------
    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    /**
     * The database row level created timestamp (i.e. when a user first enters the record to the system)
     */
    private Date createdTimestamp;

    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "createdUserId", nullable = false)
    /**
     * The database row level created user (i.e. user entering the record to the system)
     */
    private User createdUser;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    /**
     * The database row level last updated time
     */
    private Date lastUpdatedTimestamp;

    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "lastUpdatedUserId", nullable = false)
    /**
     * The database row level last updated user
     */
    private User lastUpdatedUser;

    @Column(nullable = false, columnDefinition = "integer default 1")
    private boolean activeRecord = true;

    @Override
    protected CRSLifeCycleInfo clone() throws CloneNotSupportedException {
        return (CRSLifeCycleInfo) super.clone();
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

    public Date getCertificateGeneratedTimestamp() {
        return certificateGeneratedTimestamp;
    }

    public void setCertificateGeneratedTimestamp(Date certificateGeneratedTimestamp) {
        this.certificateGeneratedTimestamp = certificateGeneratedTimestamp;
    }

    public User getCertificateGeneratedUser() {
        return certificateGeneratedUser;
    }

    public void setCertificateGeneratedUser(User certificateGeneratedUser) {
        this.certificateGeneratedUser = certificateGeneratedUser;
    }

    public boolean isActiveRecord() {
        return activeRecord;
    }

    public void setActiveRecord(boolean activeRecord) {
        this.activeRecord = activeRecord;
    }
}
