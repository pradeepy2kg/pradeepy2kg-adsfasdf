package lk.rgd.crs.api.domain;

import javax.persistence.Embeddable;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfIssue;

    @Column(nullable = true)
    private String placeOfIssue;

    @Column(nullable = false)
    private String timeOfDeath;

    @Column(nullable = false)
    private int deathDistrictId;

    @Column(nullable = false)
    private int deathDivisionId;

    @Column(nullable = true, length = 255)
    private String placeOfDeathInEnglish;

    @Column(nullable = true, length = 255)
    private String placeOfDeathInOfficialLang;

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

    public String getDeathSerialNo() {
        return deathSerialNo;
    }

    public void setDeathSerialNo(String deathSerialNo) {
        this.deathSerialNo = deathSerialNo;
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
        this.placeOfDeath = placeOfDeath;
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

    public Date getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(Date dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public String getPlaceOfIssue() {
        return placeOfIssue;
    }

    public void setPlaceOfIssue(String placeOfIssue) {
        this.placeOfIssue = placeOfIssue;
    }

    public String getTimeOfDeath() {
        return timeOfDeath;
    }

    public void setTimeOfDeath(String timeOfDeath) {
        this.timeOfDeath = timeOfDeath;
    }

    public int getDeathDistrictId() {
        return deathDistrictId;
    }

    public void setDeathDistrictId(int deathDistrictId) {
        this.deathDistrictId = deathDistrictId;
    }

    public int getDeathDivisionId() {
        return deathDivisionId;
    }

    public void setDeathDivisionId(int deathDivisionId) {
        this.deathDivisionId = deathDivisionId;
    }

    public String getPlaceOfDeathInEnglish() {
        return placeOfDeathInEnglish;
    }

    public void setPlaceOfDeathInEnglish(String placeOfDeathInEnglish) {
        this.placeOfDeathInEnglish = placeOfDeathInEnglish;
    }

    public String getPlaceOfDeathInOfficialLang() {
        return placeOfDeathInOfficialLang;
    }

    public void setPlaceOfDeathInOfficialLang(String placeOfDeathInOfficialLang) {
        this.placeOfDeathInOfficialLang = placeOfDeathInOfficialLang;
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
        this.causeOfDeath = causeOfDeath;
    }

    public String getIcdCodeOfCause() {
        return icdCodeOfCause;
    }

    public void setIcdCodeOfCause(String icdCodeOfCause) {
        this.icdCodeOfCause = icdCodeOfCause;
    }

    public String getPlaceOfBurial() {
        return placeOfBurial;
    }

    public void setPlaceOfBurial(String placeOfBurial) {
        this.placeOfBurial = placeOfBurial;
    }
}
