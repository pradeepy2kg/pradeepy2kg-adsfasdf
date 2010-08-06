package lk.rgd.crs.api.domain;

/**
 * @author Duminda Dharmakeerthi
 */
public class DeclarantInfo {

    private String declarantNICorPIN;
    private String declarantFullName;
    private String declarantAddress;
    private String declarantPhone;
    private String declarantEMail;
    private int declarantType;


    public void setDeclarantNICorPIN(String declarantNICorPIN) {
        this.declarantNICorPIN = declarantNICorPIN;
    }

    public void setDeclarantFullName(String declarantFullName) {
        this.declarantFullName = declarantFullName;
    }

    public void setDeclarantAddress(String declarantAddress) {
        this.declarantAddress = declarantAddress;
    }

    public void setDeclarantPhone(String declarantPhone) {
        this.declarantPhone = declarantPhone;
    }

    public void setDeclarantEMail(String declarantEMail) {
        this.declarantEMail = declarantEMail;
    }

    public void setDeclarantType(int declarantType) {
        this.declarantType = declarantType;
    }

    public String getDeclarantNICorPIN() {
        return declarantNICorPIN;
    }

    public String getDeclarantFullName() {
        return declarantFullName;
    }

    public String getDeclarantAddress() {
        return declarantAddress;
    }

    public String getDeclarantPhone() {
        return declarantPhone;
    }

    public String getDeclarantEMail() {
        return declarantEMail;
    }

    public int getDeclarantType() {
        return declarantType;
    }    
}
