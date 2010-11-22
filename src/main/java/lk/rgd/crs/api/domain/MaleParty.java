package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.District;

import javax.persistence.*;
import java.util.Date;

/**
 * embeddable class for male party
 */
@Embeddable
public class MaleParty {

    public enum CivilStatusMale {
        NEVER_MARRIED,
        DIVORCED,
        WIDOWED,
        A_NULLED
    }


    @Column(name = "M_IDENTIFICATION_NUMBER", nullable = true)
    //pin or nic
    private String identificationNumberMale;

    @Column(name = "M_DOB", nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfBirthMale;

    @Column(name = "M_AGE_LAST_BD", nullable = true)
    private int ageAtLastBirthDayMale;

    @Column(name = "M_NAME_OFFICIAL", nullable = true, length = 600)
    private String nameInOfficialLanguageMale;

    @Column(name = "M_NAME_ENGLISH", nullable = true, length = 600)
    private String nameInEnglishMale;

    @Column(name = "M_ADDRESS", nullable = true, length = 255)
    private String residentAddressMale;

    @ManyToOne
    @JoinColumn()
    private District districtMale;

    @ManyToOne
    @JoinColumn
    private MRDivision mrDivisionMale;

    @Column(name = "M_DURATION", nullable = true)
    private int durationMale;

    @Column(name = "M_RANK_PROFESSION", nullable = true, length = 255)
    private String rankOrProfessionMale;

    @Column(name = "M_TP_NUMBER", nullable = true, length = 10)
    private String tpNumberMale;

    @Column(name = "M_EMAIL", nullable = true)
    private String emailMale;

    @Column(name = "M_IDENTIFICATION_FATHER", length = 10)
    private String fatherIdentificationNumberMale;

    @Column(name = "M_FULL_NAME_FATHER", length = 600)
    private String fatherFullNameMale;

    @Column(name = "M_FATHER_RANK_PROFESSION", length = 255)
    private String fatherRankOrProfessionMale;

    @Column(name = "M_CONSENT", length = 255)
    private String consentIfAnyMale;

    //sign

    public String getIdentificationNumberMale() {
        return identificationNumberMale;
    }

    public void setIdentificationNumberMale(String identificationNumberMale) {
        this.identificationNumberMale = identificationNumberMale;
    }

    public Date getDateOfBirthMale() {
        return dateOfBirthMale;
    }

    public void setDateOfBirthMale(Date dateOfBirthMale) {
        this.dateOfBirthMale = dateOfBirthMale;
    }

    public int getAgeAtLastBirthDayMale() {
        return ageAtLastBirthDayMale;
    }

    public void setAgeAtLastBirthDayMale(int ageAtLastBirthDayMale) {
        this.ageAtLastBirthDayMale = ageAtLastBirthDayMale;
    }

    public String getNameInOfficialLanguageMale() {
        return nameInOfficialLanguageMale;
    }

    public void setNameInOfficialLanguageMale(String nameInOfficialLanguageMale) {
        this.nameInOfficialLanguageMale = nameInOfficialLanguageMale;
    }

    public String getNameInEnglishMale() {
        return nameInEnglishMale;
    }

    public void setNameInEnglishMale(String nameInEnglishMale) {
        this.nameInEnglishMale = nameInEnglishMale;
    }

    public String getResidentAddressMale() {
        return residentAddressMale;
    }

    public void setResidentAddressMale(String residentAddressMale) {
        this.residentAddressMale = residentAddressMale;
    }

    public District getDistrictMale() {
        return districtMale;
    }

    public void setDistrictMale(District districtMale) {
        this.districtMale = districtMale;
    }

    public MRDivision getMrDivisionMale() {
        return mrDivisionMale;
    }

    public void setMrDivisionMale(MRDivision mrDivisionMale) {
        this.mrDivisionMale = mrDivisionMale;
    }

    public int getDurationMale() {
        return durationMale;
    }

    public void setDurationMale(int durationMale) {
        this.durationMale = durationMale;
    }

    public String getRankOrProfessionMale() {
        return rankOrProfessionMale;
    }

    public void setRankOrProfessionMale(String rankOrProfessionMale) {
        this.rankOrProfessionMale = rankOrProfessionMale;
    }

    public String getTpNumberMale() {
        return tpNumberMale;
    }

    public void setTpNumberMale(String tpNumberMale) {
        this.tpNumberMale = tpNumberMale;
    }

    public String getEmailMale() {
        return emailMale;
    }

    public void setEmailMale(String emailMale) {
        this.emailMale = emailMale;
    }

    public String getFatherIdentificationNumberMale() {
        return fatherIdentificationNumberMale;
    }

    public void setFatherIdentificationNumberMale(String fatherIdentificationNumberMale) {
        this.fatherIdentificationNumberMale = fatherIdentificationNumberMale;
    }

    public String getFatherFullNameMale() {
        return fatherFullNameMale;
    }

    public void setFatherFullNameMale(String fatherFullNameMale) {
        this.fatherFullNameMale = fatherFullNameMale;
    }

    public String getFatherRankOrProfessionMale() {
        return fatherRankOrProfessionMale;
    }

    public void setFatherRankOrProfessionMale(String fatherRankOrProfessionMale) {
        this.fatherRankOrProfessionMale = fatherRankOrProfessionMale;
    }

    public String getConsentIfAnyMale() {
        return consentIfAnyMale;
    }

    public void setConsentIfAnyMale(String consentIfAnyMale) {
        this.consentIfAnyMale = consentIfAnyMale;
    }
}
