package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.util.WebUtils;
import lk.rgd.prs.api.domain.Person;

import javax.persistence.*;
import java.util.Date;

/**
 * embeddable class for male party
 */
@Embeddable
public class MaleParty {

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
    @JoinColumn
    private MRDivision mrDivisionMale;

    @ManyToOne
    @JoinColumn
    private Race maleRace;

    @Column(name = "M_DURATION", nullable = true)
    private int durationMale;

    @Column(name = "M_RANK_PROFESSION", nullable = true, length = 255)
    private String rankOrProfessionMale;

    @Column(name = "M_TP_NUMBER", nullable = true, length = 10)
    private String tpNumberMale;

    @Column(name = "M_CIVIL_STATE", nullable = true)
    private Person.CivilStatus civilStatusMale;

    @Column(name = "M_EMAIL", nullable = true)
    private String emailMale;

    @Column(name = "M_IDENTIFICATION_FATHER", length = 10)
    private String fatherIdentificationNumberMale;

    @Column(name = "M_FULL_NAME_FATHER", length = 600)
    private String fatherFullNameMale;

    @Column(name = "M_CONSENT", length = 255)
    private String consentIfAnyMale;

    //sign

    public String getIdentificationNumberMale() {
        return identificationNumberMale;
    }

    public void setIdentificationNumberMale(String identificationNumberMale) {
        this.identificationNumberMale = WebUtils.filterBlanks(identificationNumberMale);
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
        this.nameInOfficialLanguageMale = WebUtils.filterBlanks(nameInOfficialLanguageMale);
    }

    public String getNameInEnglishMale() {
        return nameInEnglishMale;
    }

    public void setNameInEnglishMale(String nameInEnglishMale) {
        this.nameInEnglishMale = WebUtils.filterBlanks(nameInEnglishMale);
    }

    public String getResidentAddressMale() {
        return residentAddressMale;
    }

    public void setResidentAddressMale(String residentAddressMale) {
        this.residentAddressMale = WebUtils.filterBlanks(residentAddressMale);
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
        this.rankOrProfessionMale = WebUtils.filterBlanks(rankOrProfessionMale);
    }

    public String getTpNumberMale() {
        return tpNumberMale;
    }

    public void setTpNumberMale(String tpNumberMale) {
        this.tpNumberMale = WebUtils.filterBlanks(tpNumberMale);
    }

    public String getEmailMale() {
        return emailMale;
    }

    public void setEmailMale(String emailMale) {
        this.emailMale = WebUtils.filterBlanks(emailMale);
    }

    public String getFatherIdentificationNumberMale() {
        return fatherIdentificationNumberMale;
    }

    public void setFatherIdentificationNumberMale(String fatherIdentificationNumberMale) {
        this.fatherIdentificationNumberMale = WebUtils.filterBlanks(fatherIdentificationNumberMale);
    }

    public String getFatherFullNameMale() {
        return fatherFullNameMale;
    }

    public void setFatherFullNameMale(String fatherFullNameMale) {
        this.fatherFullNameMale = WebUtils.filterBlanks(fatherFullNameMale);
    }

    public String getConsentIfAnyMale() {
        return consentIfAnyMale;
    }

    public void setConsentIfAnyMale(String consentIfAnyMale) {
        this.consentIfAnyMale = WebUtils.filterBlanks(consentIfAnyMale);
    }

    public Race getMaleRace() {
        return maleRace;
    }

    public void setMaleRace(Race maleRace) {
        this.maleRace = maleRace;
    }

    public Person.CivilStatus getCivilStatusMale() {
        return civilStatusMale;
    }

    public void setCivilStatusMale(Person.CivilStatus civilStatusMale) {
        this.civilStatusMale = civilStatusMale;
    }
}
