package lk.rgd.crs.api.domain;

import lk.rgd.crs.web.util.WebUtils;

import javax.persistence.*;
import java.util.Date;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author Indunil Moremada
 * @author Tharanga Punchihewa
 *         An instance representing death information submitted for the deathRegistration of a death (page 1 of the form)
 */
@Embeddable
public class DeathInfo implements Serializable {

    private static final DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");


    @Column(nullable = false)
    private String deathSerialNo;

    @Column(nullable = true)
    private long deathCertificateNo;

    @Column(nullable = false, length = 255)
    private String placeOfDeath;

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfDeath;

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfRegistration;

    @Column(nullable = true)
    private String placeOfIssue;

    @Column(nullable = true)
    private String timeOfDeath;

    /**
     * The Birth/Death registration division where the death is registered (Includes District)
     */
    @ManyToOne
    @JoinColumn(name = "bdDivisionUKey", nullable = false)
    private BDDivision deathDivision;

    @Column(nullable = true, length = 255)
    private String placeOfDeathInEnglish;

    @Column(nullable = true, length = 255)
    private String placeOfDeathInOfficialLang;

    /**
     * The preferred language of for the record
     */
    @Column(nullable = false, columnDefinition = "char(2) default 'si'")
    private String preferredLanguage = "si";

    /**
     * 1-Yes, 0-No
     */
    @Column(nullable = true)
    private boolean causeOfDeathEstablished;

    /**
     * 1-Yes , 0-No
     */
    @Column(nullable = true)
    private boolean infantLessThan30Days;

    @Column(nullable = true, length = 600)
    private String causeOfDeath;

    @Column(nullable = true)
    private String icdCodeOfCause;

    @Column(nullable = false)
    private String placeOfBurial;

    @Column(nullable = true)
    private String reasonForLateRegistration;

    @Column(nullable = true)
    private String anyOtherInformation;

    public String getAnyOtherInformation() {
        return anyOtherInformation;
    }

    public void setAnyOtherInformation(String anyOtherInformation) {
        this.anyOtherInformation = anyOtherInformation;
    }

    public String getDeathSerialNo() {
        return deathSerialNo;
    }

    public void setDeathSerialNo(String deathSerialNo) {
        this.deathSerialNo = WebUtils.filterBlanksAndToUpper(deathSerialNo);
    }

    public long getDeathCertificateNo() {
        return deathCertificateNo;
    }

    public void setDeathCertificateNo(long deathCertificateNo) {
        this.deathCertificateNo = deathCertificateNo;
    }

    public String getPlaceOfDeath() {
        return placeOfDeath;
    }

    public void setPlaceOfDeath(String placeOfDeath) {
        this.placeOfDeath = WebUtils.filterBlanksAndToUpper(placeOfDeath);
    }

    public Date getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(Date dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public Date getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(Date dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public String getPlaceOfIssue() {
        return placeOfIssue;
    }

    public void setPlaceOfIssue(String placeOfIssue) {
        this.placeOfIssue = WebUtils.filterBlanksAndToUpper(placeOfIssue);
    }

    public String getTimeOfDeath() {
        return timeOfDeath;
    }

    public void setTimeOfDeath(String timeOfDeath) {
        this.timeOfDeath = timeOfDeath;
    }

    public String getPlaceOfDeathInEnglish() {
        return placeOfDeathInEnglish;
    }

    public void setPlaceOfDeathInEnglish(String placeOfDeathInEnglish) {
        this.placeOfDeathInEnglish = WebUtils.filterBlanksAndToUpper(placeOfDeathInEnglish);
    }

    public String getPlaceOfDeathInOfficialLang() {
        return placeOfDeathInOfficialLang;
    }

    public void setPlaceOfDeathInOfficialLang(String placeOfDeathInOfficialLang) {
        this.placeOfDeathInOfficialLang = WebUtils.filterBlanksAndToUpper(placeOfDeathInOfficialLang);
    }

    public boolean isCauseOfDeathEstablished() {
        return causeOfDeathEstablished;
    }

    public void setCauseOfDeathEstablished(boolean causeOfDeathEstablished) {
        this.causeOfDeathEstablished = causeOfDeathEstablished;
    }

    public boolean isInfantLessThan30Days() {
        return infantLessThan30Days;
    }

    public void setInfantLessThan30Days(boolean infantLessThan30Days) {
        this.infantLessThan30Days = infantLessThan30Days;
    }

    public String getCauseOfDeath() {
        return causeOfDeath;
    }

    public void setCauseOfDeath(String causeOfDeath) {
        this.causeOfDeath = WebUtils.filterBlanksAndToUpper(causeOfDeath);
    }

    public String getIcdCodeOfCause() {
        return icdCodeOfCause;
    }

    public void setIcdCodeOfCause(String icdCodeOfCause) {
        this.icdCodeOfCause = WebUtils.filterBlanksAndToUpper(icdCodeOfCause);
    }

    public String getPlaceOfBurial() {
        return placeOfBurial;
    }

    public void setPlaceOfBurial(String placeOfBurial) {
        this.placeOfBurial = WebUtils.filterBlanksAndToUpper(placeOfBurial);
    }

    public BDDivision getDeathDivision() {
        return deathDivision;
    }

    public void setDeathDivision(BDDivision deathDivision) {
        this.deathDivision = deathDivision;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getReasonForLateRegistration() {
        return reasonForLateRegistration;
    }

    public void setReasonForLateRegistration(String reasonForLateRegistration) {
        this.reasonForLateRegistration = WebUtils.filterBlanksAndToUpper(reasonForLateRegistration);
    }
}
