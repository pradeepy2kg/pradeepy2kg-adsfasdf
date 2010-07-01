package lk.rgd.crs.api.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * Java bean instance to contain notifying authority information enterd by page 4 of birth declaration form
 */
@Embeddable
public class NotifyingAuthorityInfo implements Serializable {
    /** The notifying authority PIN */
    @Column(nullable = false)
    private String notifyingAuthorityPIN;

    /** The notifying authority Name */
    @Column(nullable = false, length = 120)
    private String notifyingAuthorityName;

    /** The PIN of the ADR approving the BDF  */
    @Column(nullable = true, length = 10)
    private String approvePIN;

    /** The date when an ADR or higher approves the BDF  */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date approveDate;

    /** The notifying authority Address */
    @Column(nullable = false, length = 255)
    private String notifyingAuthorityAddress;

    /** date the signature has been put. */
    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date notifyingAuthoritySignDate;

    public String getNotifyingAuthorityPIN() {
        return notifyingAuthorityPIN;
    }

    public void setNotifyingAuthorityPIN(String notifyingAuthorityPIN) {
        this.notifyingAuthorityPIN = notifyingAuthorityPIN;
    }

    public String getNotifyingAuthorityName() {
        return notifyingAuthorityName;
    }

    public void setNotifyingAuthorityName(String notifyingAuthorityName) {
        this.notifyingAuthorityName = notifyingAuthorityName;
    }

    public String getNotifyingAuthorityAddress() {
        return notifyingAuthorityAddress;
    }

    public void setNotifyingAuthorityAddress(String notifyingAuthorityAddress) {
        this.notifyingAuthorityAddress = notifyingAuthorityAddress;
    }

    public Date getNotifyingAuthoritySignDate() {
        return notifyingAuthoritySignDate;
    }

    public void setNotifyingAuthoritySignDate(Date notifyingAuthoritySignDate) {
        this.notifyingAuthoritySignDate = notifyingAuthoritySignDate;
    }
}
