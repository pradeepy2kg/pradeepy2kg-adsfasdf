package lk.rgd.crs.api.domain;

import lk.rgd.common.util.WebUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Duminda Dharmakeerthi
 * @author Indunil Moremada
 *         An instance representing death declarant information submitted for the death
 */

@Embeddable
public class DeclarantInfo implements Serializable, Cloneable {

    /**
     * 0 - father, 1 - mother, 2 - brotherOrSister,
     * 3 - sonOrDaughter, 4 - relative, 5 - other ,6 -spouse
     */
    public enum DeclarantType {
        FATHER,
        MOTHER,
        BORTHER_OR_SISTER,
        SON_OR_DAUGHTER,
        RELATIVE,
        OTHER,
        SPOUSE //husband_or_wife
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


    public String getDeclarantNICorPIN() {
        return declarantNICorPIN;
    }

    public void setDeclarantNICorPIN(String declarantNICorPIN) {
        this.declarantNICorPIN = WebUtils.filterBlanksAndToUpper(declarantNICorPIN);
    }

    public String getDeclarantFullName() {
        return declarantFullName;
    }

    public void setDeclarantFullName(String declarantFullName) {
        this.declarantFullName = WebUtils.filterBlanksAndToUpper(declarantFullName);
    }

    public String getDeclarantAddress() {
        return declarantAddress;
    }

    public void setDeclarantAddress(String declarantAddress) {
        this.declarantAddress = WebUtils.filterBlanksAndToUpper(declarantAddress);
    }

    public String getDeclarantPhone() {
        return declarantPhone;
    }

    public void setDeclarantPhone(String declarantPhone) {
        this.declarantPhone = WebUtils.filterBlanks(declarantPhone);
    }

    public String getDeclarantEMail() {
        return declarantEMail;
    }

    public void setDeclarantEMail(String declarantEMail) {
        this.declarantEMail = WebUtils.filterBlanks(declarantEMail);
    }

    public DeclarantType getDeclarantType() {
        return declarantType;
    }

    public void setDeclarantType(DeclarantType declarantType) {
        this.declarantType = declarantType;
    }

    @Override
    protected DeclarantInfo clone() throws CloneNotSupportedException {
        return (DeclarantInfo) super.clone();
    }
}
