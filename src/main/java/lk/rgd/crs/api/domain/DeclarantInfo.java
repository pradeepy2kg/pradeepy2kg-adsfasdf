package lk.rgd.crs.api.domain;

import javax.persistence.Embeddable;
import javax.persistence.Column;
import java.io.Serializable;

/**
 * @author Duminda Dharmakeerthi
 * @author Indunil Moremada
 *         An instance representing death declarant information submitted for the death
 */

@Embeddable
public class DeclarantInfo implements Serializable {

    /**
     * 0 - father, 1 - mother, 2 - brotherOrSister,
     * 3 - sonOrDaughter, 4 - relative, 5 - other
     */
    public enum DeclarantType {
        FATHER,
        MOTHER,
        BORTHER_OR_SISTER,
        SON_OR_DAUGHTER,
        RELATIVE,
        OTHER
    }

    @Column(nullable = true)
    private String declarantNICorPIN;

    @Column(nullable = true)
    private String declarantFullName;

    @Column(nullable = true)
    private String declarantAddress;

    @Column(nullable = true)
    private String declarantPhone;

    @Column(nullable = true)
    private String declarantEMail;

    @Column(nullable = false)
    private DeclarantType declarantType;


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

    public void setDeclarantType(DeclarantType declarantType) {
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

    public DeclarantType getDeclarantType() {
        return declarantType;
    }
}
