package lk.rgd.crs.api.domain;

import lk.rgd.common.util.WebUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * Represents Inquirer into Deaths or Judicial Medical Officer for Sudden Deaths.
 *
 * @author Chathuranga Withana
 */
@Embeddable
public class CertifyingAuthority implements Serializable, Cloneable {
    /**
     * The death certifying authority identification number (NIC or PIN)
     */
    @Column(nullable = true, length = 12)
    private String certifyingAuthorityPIN;

    /**
     * The death certifying authority Name
     */

    @Column(nullable = true, length = 120)
    private String certifyingAuthorityName;

    /**
     * The death certifying authority Address
     */
    @Column(nullable = true, length = 255)
    private String certifyingAuthorityAddress;

    /**
     * The death certifying authority sign date
     */
    @Column(nullable = true)
    @Temporal(TemporalType.DATE)
    private Date certifyingAuthoritySignDate;

    public String getCertifyingAuthorityPIN() {
        return certifyingAuthorityPIN;
    }

    public void setCertifyingAuthorityPIN(String certifyingAuthorityPIN) {
        this.certifyingAuthorityPIN = WebUtils.filterBlanksAndToUpper(certifyingAuthorityPIN);
    }

    public String getCertifyingAuthorityName() {
        return certifyingAuthorityName;
    }

    public void setCertifyingAuthorityName(String certifyingAuthorityName) {
        this.certifyingAuthorityName = WebUtils.filterBlanksAndToUpper(certifyingAuthorityName);
    }

    public String getCertifyingAuthorityAddress() {
        return certifyingAuthorityAddress;
    }

    public void setCertifyingAuthorityAddress(String certifyingAuthorityAddress) {
        this.certifyingAuthorityAddress = WebUtils.filterBlanksAndToUpper(certifyingAuthorityAddress);
    }

    public Date getCertifyingAuthoritySignDate() {
        return certifyingAuthoritySignDate;
    }

    public void setCertifyingAuthoritySignDate(Date certifyingAuthoritySignDate) {
        this.certifyingAuthoritySignDate = certifyingAuthoritySignDate;
    }

    @Override
    protected CertifyingAuthority clone() throws CloneNotSupportedException {
        return (CertifyingAuthority) super.clone();
    }
}
