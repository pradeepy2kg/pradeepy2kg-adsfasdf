package lk.rgd.crs.api.domain;

import javax.persistence.Column;
import java.util.Date;

public class InformantInfo {
    /** 0 - father, 1 - mother, 2 - guardian */
    @Column(nullable = false)
    private int informantType;

    @Column(nullable = false)
    private String informantName;

    @Column(nullable = true)
    private String informantNICorPIN;

    @Column(nullable = true)
    private String informantAddress;

    @Column(nullable = true)
    private String informantPhoneNo;

    @Column(nullable = true)
    private String informantEmail;

    @Column(nullable = true)
    private Date informantSignDate;
    private boolean informantSigned;

    public int getInformantType() {
        return informantType;
    }

    public void setInformantType(int informantType) {
        this.informantType = informantType;
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

    public boolean isInformantSigned() {
        return informantSigned;
    }

    public void setInformantSigned(boolean informantSigned) {
        this.informantSigned = informantSigned;
    }
}