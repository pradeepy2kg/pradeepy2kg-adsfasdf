package lk.rgd.crs.api.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class InformantInfo implements Serializable {

    /**
     * 0 - father, 1 - mother, 2 - guardian
     */
    public enum InformantType {
        FATHER,
        MOTHER,
        GUARDIAN
    }

    @Column(nullable = false)
    private InformantType informantType;

    @Column(nullable = false, length = 600)
    private String informantName;

    @Column(nullable = true, length = 10)
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

    public int getInformantType() {
        return informantType.ordinal();
    }

    public void setInformantType(int informantType) {
        switch (informantType) {
            case 0: this.informantType = InformantType.FATHER; break;
            case 1: this.informantType = InformantType.MOTHER; break;
            default: this.informantType = InformantType.GUARDIAN;
        }
    }

    public String getInformantName() {
        return informantName;
    }

    public void setInformantName(String informantName) {
        this.informantName = informantName;
    }

    public String getInformantNICorPIN() {
        return informantNICorPIN;
    }

    public void setInformantNICorPIN(String informantNICorPIN) {
        this.informantNICorPIN = informantNICorPIN;
    }

    public String getInformantAddress() {
        return informantAddress;
    }

    public void setInformantAddress(String informantAddress) {
        this.informantAddress = informantAddress;
    }

    public String getInformantPhoneNo() {
        return informantPhoneNo;
    }

    public void setInformantPhoneNo(String informantPhoneNo) {
        this.informantPhoneNo = informantPhoneNo;
    }

    public String getInformantEmail() {
        return informantEmail;
    }

    public void setInformantEmail(String informantEmail) {
        this.informantEmail = informantEmail;
    }

    public Date getInformantSignDate() {
        return informantSignDate;
    }

    public void setInformantSignDate(Date informantSignDate) {
        this.informantSignDate = informantSignDate;
    }
}