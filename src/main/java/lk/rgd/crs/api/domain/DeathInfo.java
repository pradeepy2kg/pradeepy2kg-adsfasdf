package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.District;
import lk.rgd.common.util.WebUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Indunil Moremada
 * @author Tharanga Punchihewa
 * @author amith jayasekara
 *         An instance representing death information submitted for the deathRegistration of a death (page 1 of the form)
 */
@Embeddable
public class DeathInfo implements Serializable, Cloneable {

    @Column(nullable = false)
    private long deathSerialNo;

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
    private boolean deathOccurAtaHospital;

    public long getDeathSerialNo() {
        return deathSerialNo;
    }

    public void setDeathSerialNo(long deathSerialNo) {
        this.deathSerialNo = deathSerialNo;
    }

    public String getPlaceOfDeath() {
        return placeOfDeath;
    }

    public void setPlaceOfDeath(String placeOfDeath) {
        this.placeOfDeath = WebUtils.filterBlanksAndToUpperAndTrim(placeOfDeath,255,"placeOfDeath");
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
        this.placeOfIssue = WebUtils.filterBlanksAndToUpperAndTrim(placeOfIssue,255,"placeOfIssue");
    }

    public String getTimeOfDeath() {
        return timeOfDeath;
    }

    public void setTimeOfDeath(String timeOfDeath) {
        this.timeOfDeath = WebUtils.filterBlanksAndToUpperAndTrim(timeOfDeath,255,"timeOfDeath");
    }

    public String getPlaceOfDeathInEnglish() {
        return placeOfDeathInEnglish;
    }

    public void setPlaceOfDeathInEnglish(String placeOfDeathInEnglish) {
        this.placeOfDeathInEnglish = WebUtils.filterBlanksAndToUpperAndTrim(placeOfDeathInEnglish,255,"placeOfDeathInEnglish");
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
        this.causeOfDeath = WebUtils.filterBlanksAndToUpperAndTrim(causeOfDeath,600,"causeOfDeath");
    }

    public String getIcdCodeOfCause() {
        return icdCodeOfCause;
    }

    public void setIcdCodeOfCause(String icdCodeOfCause) {
        this.icdCodeOfCause = WebUtils.filterBlanks(icdCodeOfCause);
    }

    public String getPlaceOfBurial() {
        return placeOfBurial;
    }

    public void setPlaceOfBurial(String placeOfBurial) {
        this.placeOfBurial = WebUtils.filterBlanksAndToUpperAndTrim(placeOfBurial,255,"placeOfBurial");
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
        this.reasonForLateRegistration = WebUtils.filterBlanksAndToUpperAndTrim(reasonForLateRegistration,255,"reasonForLateRegistration");
    }

    public District getDeathDistrict() {
        return deathDivision.getDistrict();
    }

    public boolean isDeathOccurAtaHospital() {
        return deathOccurAtaHospital;
    }

    public void setDeathOccurAtaHospital(boolean deathOccurAtaHospital) {
        this.deathOccurAtaHospital = deathOccurAtaHospital;
    }

    @Override
    protected DeathInfo clone() throws CloneNotSupportedException {
        return (DeathInfo) super.clone();
    }
}
