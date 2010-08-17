package lk.rgd.crs.api.domain;

import javax.persistence.Embeddable;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * Java bean instance to capture basic meta info for any entity. Info on created and last updated events for auditing purposes
 *  */
@Embeddable
public class BaseMetaInfo implements Serializable {
    /**
     * Last Updated Date
     */
    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date lastUpdatedDate;

    /**
     * Last Updated by User
     */
    @Column(nullable = false)
    private long userId;

    /**
     * Created Date
     */
    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdDate;

    /**
     * Created User
     */
    @Column(nullable = false)
    private long createdUser;

    /**
     * Created Date
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date approvedDate;

    /**
     * Created User
     */
    @Column(nullable = true)
    private long approvedUser;

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public long getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(long createdUser) {
        this.createdUser = createdUser;
    }
}
