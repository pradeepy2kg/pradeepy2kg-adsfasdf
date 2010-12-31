package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.WebUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class ConfirmantInfo implements Serializable, Cloneable {
    //TODO: to be generalized
    public enum ConfirmantType {
        FATHER,
        MOTHER,
        GUARDIAN,
        RELATIVE
    }

    /**
     * The last date for confirmation - set as 14 days from confirmation print date
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date lastDateForConfirmation;

    /**
     * Confirmant Type
     */
    @Column(nullable = true)
    private ConfirmantType confirmantType;

    /**
     * PIN or NIC of person confirming BDF details
     */
    @Column(nullable = true, length = 10)
    private String confirmantNICorPIN;

    /**
     * Name of person confirming BDF details
     */
    @Column(nullable = true, length = 600)
    private String confirmantFullName;

    /**
     * Date of the confirmation
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date confirmantSignDate;

    /**
     * Date confirmation is entered into the system
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date confirmationProcessedTimestamp;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmationProcessedUserId", nullable = true)
    private User confirmationProcessedUser;

    public Date getLastDateForConfirmation() {
        return lastDateForConfirmation;
    }

    public void setLastDateForConfirmation(Date lastDateForConfirmation) {
        this.lastDateForConfirmation = lastDateForConfirmation;
    }

    public String getConfirmantNICorPIN() {
        return confirmantNICorPIN;
    }

    public void setConfirmantNICorPIN(String confirmantNICorPIN) {
        this.confirmantNICorPIN = WebUtils.filterBlanksAndToUpper(confirmantNICorPIN);
    }

    public String getConfirmantFullName() {
        return confirmantFullName;
    }

    public void setConfirmantFullName(String confirmantFullName) {
        this.confirmantFullName = WebUtils.filterBlanksAndToUpper(confirmantFullName);
    }

    public Date getConfirmantSignDate() {
        return confirmantSignDate;
    }

    public void setConfirmantSignDate(Date confirmantSignDate) {
        this.confirmantSignDate = confirmantSignDate;
    }

    public Date getConfirmationProcessedTimestamp() {
        return confirmationProcessedTimestamp;
    }

    public void setConfirmationProcessedTimestamp(Date confirmationProcessedTimestamp) {
        this.confirmationProcessedTimestamp = confirmationProcessedTimestamp;
    }

    public User getConfirmationProcessedUser() {
        return confirmationProcessedUser;
    }

    public void setConfirmationProcessedUser(User confirmationProcessedUser) {
        this.confirmationProcessedUser = confirmationProcessedUser;
    }

    public ConfirmantType getConfirmantType() {
        return confirmantType;
    }

    public void setConfirmantType(ConfirmantType confirmantType) {
        this.confirmantType = confirmantType;
    }

    @Override
    protected ConfirmantInfo clone() throws CloneNotSupportedException {
        return (ConfirmantInfo) super.clone();
    }
}
