package lk.rgd.crs.web.model;

import java.util.Date;

public class InformantInfo {
    private String informantAddress;

    /**
     * 0 - father, 1 - mother, 2 - guardian
     */
    private int informantType;

    private String informantName;
    private String informantNICorPIN;
    private String informantPhoneNo;
    private String informantEmail;
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