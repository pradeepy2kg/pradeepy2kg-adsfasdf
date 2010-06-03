package lk.rgd.crs.api.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class ConfirmantInfo implements Serializable {
    /** This represents a system generated serial number for the confirmation by parents  */
    @Column(nullable = true)
    private int confirmationSerialNumber;

    /** Has the confirmation for parents been printed ? */
    @Column(nullable = true)
    private boolean confirmationPrinted;

    /** The last date for confirmation - set as 14 days from confirmation print date */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date lastDateForConfirmation;

    /** PIN or NIC of person confirming BDF details */
    @Column(nullable = true, length = 10)
    private String confirmantNICorPIN;

    /** Name of person confirming BDF details  */
    @Column(nullable = true, length = 600)
    private String confirmantFullName;

    /** Date of the confirmation */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date confirmantSignDate;

    /** Date confirmation is received */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date confirmationReceiveDate;

    public int getConfirmationSerialNumber() {
        return confirmationSerialNumber;
    }

    public void setConfirmationSerialNumber(int confirmationSerialNumber) {
        this.confirmationSerialNumber = confirmationSerialNumber;
    }

    public boolean isConfirmationPrinted() {
        return confirmationPrinted;
    }

    public void setConfirmationPrinted(boolean confirmationPrinted) {
        this.confirmationPrinted = confirmationPrinted;
    }

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
        this.confirmantNICorPIN = confirmantNICorPIN;
    }

    public String getConfirmantFullName() {
        return confirmantFullName;
    }

    public void setConfirmantFullName(String confirmantFullName) {
        this.confirmantFullName = confirmantFullName;
    }

    public Date getConfirmantSignDate() {
        return confirmantSignDate;
    }

    public void setConfirmantSignDate(Date confirmantSignDate) {
        this.confirmantSignDate = confirmantSignDate;
    }

    public Date getConfirmationReceiveDate() {
        return confirmationReceiveDate;
    }

    public void setConfirmationReceiveDate(Date confirmationReceiveDate) {
        this.confirmationReceiveDate = confirmationReceiveDate;
    }
}
