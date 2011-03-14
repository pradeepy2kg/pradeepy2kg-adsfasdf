package lk.rgd.crs.api.domain;

import lk.rgd.common.util.WebUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class InformantInfo implements Serializable, Cloneable {
    public enum InformantType {
        FATHER,
        MOTHER,
        GUARDIAN,
        RELATIVE
    }

    @Column(nullable = false)
    private InformantType informantType;

    @Column(nullable = false, length = 600)
    private String informantName;

    @Column(nullable = true, length = 12)
    private String informantNICorPIN;

    @Column(nullable = false, length = 255)
    private String informantAddress;

    @Column(nullable = true, length = 30)
    private String informantPhoneNo;

    @Column(nullable = true, length = 30)
    private String informantEmail;

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date informantSignDate;

    public InformantType getInformantType() {
        return informantType;
    }

    public void setInformantType(InformantType informantType) {
        this.informantType = informantType;
    }

    public String getInformantName() {
        return informantName;
    }

    public void setInformantName(String informantName) {
        this.informantName = WebUtils.filterBlanksAndToUpperAndTrim(informantName,600,"informantName");
    }

    public String getInformantNICorPIN() {
        return informantNICorPIN;
    }

    public void setInformantNICorPIN(String informantNICorPIN) {
        this.informantNICorPIN = WebUtils.filterBlanksAndToUpper(informantNICorPIN);
    }

    public String getInformantAddress() {
        return informantAddress;
    }

    public void setInformantAddress(String informantAddress) {
        this.informantAddress = WebUtils.filterBlanksAndToUpperAndTrim(informantAddress,255,"informantAddress");
    }

    public String getInformantPhoneNo() {
        return informantPhoneNo;
    }

    public void setInformantPhoneNo(String informantPhoneNo) {
        this.informantPhoneNo = WebUtils.filterBlanks(informantPhoneNo);
    }

    public String getInformantEmail() {
        return informantEmail;
    }

    public void setInformantEmail(String informantEmail) {
        this.informantEmail = WebUtils.filterBlanks(informantEmail);
    }

    public Date getInformantSignDate() {
        return informantSignDate;
    }

    public void setInformantSignDate(Date informantSignDate) {
        this.informantSignDate = informantSignDate;
    }

    @Override
    protected InformantInfo clone() throws CloneNotSupportedException {
        return (InformantInfo) super.clone();
    }
}