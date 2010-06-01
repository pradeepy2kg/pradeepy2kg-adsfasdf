package lk.rgd.crs.web.model;

import java.util.Date;


public class ConfirmantInfo {
    /** This represents a system generated serial number for the confirmation by parents  */
    private String confirmationSerialNumber;

    /** Has the confirmation for parents been printed ?  */
    private boolean confirmationPrinted;

    /** The last date for confirmation - set as 14 days from confirmation print date  */
    private Date lastDateForConfirmation;

    /** PIN or NIC of person confirming BDF details */
    private String confirmantNICorPIN;

    /** Name of person confirming BDF details */
    private String confirmantFullName;

    /** Date of the confirmation  */
    private Date confirmantSignDate;

    /** Date confirmation is received */
    private Date confirmationReceiveDate;

    public String getConfirmationSerialNumber() {
        return confirmationSerialNumber;
    }

    public void setConfirmationSerialNumber(String confirmationSerialNumber) {
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
