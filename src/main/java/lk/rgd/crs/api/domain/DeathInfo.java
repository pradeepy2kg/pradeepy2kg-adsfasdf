package lk.rgd.crs.api.domain;

import java.util.Date;

/**
 * User: tharanga
 * Date: Aug 6, 2010
 * Time: 12:08:12 PM
 */
public class DeathInfo {


    private long deathSerialNo;
    private long deathCertificateNo;
    private String placeOfDeath;
    private Date dateOfDeath;
    private Date dateOfRegistration;
    private Date dateOfIssue;
    private String placeOfIssue;
    private String timeOfDeath;
    private int deathDistrictId;
    private int deathDivisionId;
    private String placeOfDeathInEnglish;
    private String placeOfDeathInOfficialLang;
    private boolean causeOfDeathEstablished;
    private boolean infantLessThan30Days;
    private String causeOfDeath;
    private String icdCodeOfCause;
    private String placeOfBurial;

    public long getDeathSerialNo() {
        return deathSerialNo;
    }

    public void setDeathSerialNo(long deathSerialNo) {
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
