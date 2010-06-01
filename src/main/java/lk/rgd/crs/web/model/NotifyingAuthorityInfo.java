package lk.rgd.crs.web.model;

import java.util.Date;

/**
 * Java bean instance to contain notifying authority information enterd by page 4 of birth declaration form
 */
public class NotifyingAuthorityInfo {
    /** The notifying authority PIN */
    private String notifyingAuthorityPIN;

    /** The notifying authority Name */
    private String notifyingAuthorityName;

    /** The notifying authority Address */
    private String notifyingAuthorityAddress;

    /** Notifying authority. has he/she signed? */
    private boolean notifyingAuthoritySigned;

    /** date the signature has been put. */
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

    public boolean isNotifyingAuthoritySigned() {
        return notifyingAuthoritySigned;
    }

    public void setNotifyingAuthoritySigned(boolean notifyingAuthoritySigned) {
        this.notifyingAuthoritySigned = notifyingAuthoritySigned;
    }

    public Date getNotifyingAuthoritySignDate() {
        return notifyingAuthoritySignDate;
    }

    public void setNotifyingAuthoritySignDate(Date notifyingAuthoritySignDate) {
        this.notifyingAuthoritySignDate = notifyingAuthoritySignDate;
    }
}
