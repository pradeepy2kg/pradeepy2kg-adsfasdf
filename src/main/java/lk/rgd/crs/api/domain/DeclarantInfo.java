package lk.rgd.crs.api.domain;

import lk.rgd.common.util.WebUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

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
        FATHER,//0
        MOTHER,//1
        BORTHER_OR_SISTER,//2
        SON_OR_DAUGHTER,//3
        RELATIVE,//4
        OTHER,//5
        SPOUSE //husband_or_wife 6
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

    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date declarantSignDate;

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

    public Date getDeclarantSignDate() {
        return declarantSignDate;
    }

    public void setDeclarantSignDate(Date declarantSignDate) {
        this.declarantSignDate = declarantSignDate;
    }

    @Override
    protected DeclarantInfo clone() throws CloneNotSupportedException {
        return (DeclarantInfo) super.clone();
    }
}
