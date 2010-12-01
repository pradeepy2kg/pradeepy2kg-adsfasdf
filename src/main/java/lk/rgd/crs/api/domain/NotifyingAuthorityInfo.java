package lk.rgd.crs.api.domain;

import lk.rgd.common.util.WebUtils;

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
public class NotifyingAuthorityInfo implements Serializable, Cloneable {
    /**
     * The notifying authority PIN
     */
    @Column(nullable = false)
    private String notifyingAuthorityPIN;

    /**
     * The notifying authority Name
     */
    @Column(nullable = false, length = 120)
    private String notifyingAuthorityName;

    /**
     * The notifying authority Address
     */
    @Column(nullable = false, length = 255)
    private String notifyingAuthorityAddress;

    /**
     * date the signature has been put.
     */
    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date notifyingAuthoritySignDate;

    public String getNotifyingAuthorityPIN() {
        return notifyingAuthorityPIN;
    }

    public void setNotifyingAuthorityPIN(String notifyingAuthorityPIN) {
        this.notifyingAuthorityPIN = WebUtils.filterBlanksAndToUpper(notifyingAuthorityPIN);
    }

    public String getNotifyingAuthorityName() {
        return notifyingAuthorityName;
    }

    public void setNotifyingAuthorityName(String notifyingAuthorityName) {
        this.notifyingAuthorityName = WebUtils.filterBlanksAndToUpper(notifyingAuthorityName);
    }

    public String getNotifyingAuthorityAddress() {
        return notifyingAuthorityAddress;
    }

    public void setNotifyingAuthorityAddress(String notifyingAuthorityAddress) {
        this.notifyingAuthorityAddress = WebUtils.filterBlanksAndToUpper(notifyingAuthorityAddress);
    }

    public Date getNotifyingAuthoritySignDate() {
        return notifyingAuthoritySignDate;
    }

    public void setNotifyingAuthoritySignDate(Date notifyingAuthoritySignDate) {
        this.notifyingAuthoritySignDate = notifyingAuthoritySignDate;
    }

    @Override
    protected NotifyingAuthorityInfo clone() throws CloneNotSupportedException {
        return (NotifyingAuthorityInfo) super.clone();
    }
}
