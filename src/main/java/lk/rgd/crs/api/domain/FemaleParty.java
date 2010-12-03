package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.util.WebUtils;
import lk.rgd.prs.api.domain.Person;

import javax.persistence.*;
import java.util.Date;

/**
 * embeddable class for female party
 */
@Embeddable
public class FemaleParty {

    @Column(name = "F_IDENTIFICATION_NUMBER", nullable = true)
    //pin or nic
    private String identificationNumberFemale;

    @Column(name = "F_DOB", nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfBirthFemale;

    @Column(name = "F_AGE_LAST_BD", nullable = true)
    private int ageAtLastBirthDayFemale;

    @Column(name = "F_NAME_OFFICIAL", nullable = true, length = 600)
    private String nameInOfficialLanguageFemale;

    @Column(name = "F_NAME_ENGLISH", nullable = true, length = 600)
    private String nameInEnglishFemale;

    @Column(name = "F_ADDRESS", nullable = true, length = 255)
    private String residentAddressFemale;

    @Column(name = "F_CIVIL_STATE", nullable = true)
    private Person.CivilStatus civilStatusFemale;

    @ManyToOne
    @JoinColumn
    private MRDivision mrDivisionFemale;

    @ManyToOne
    @JoinColumn(name = "F_RACE_IDUKEY", nullable = true)
    private Race femaleRace;

    @Column(name = "F_DURATION", nullable = true)
    private int durationFemale;

    @Column(name = "F_RANK_PROFESSION", nullable = true, length = 255)
    private String rankOrProfessionFemale;

    @Column(name = "F_TP_NUMBER", nullable = true, length = 10)
    private String tpNumberFemale;

    @Column(name = "F_EMAIL", nullable = true)
    private String emailFemale;

    @Column(name = "F_IDENTIFICATION_FATHER", length = 10)
    private String fatherIdentificationNumberFemale;

    @Column(name = "F_FULL_NAME_FATHER", length = 600)
    private String fatherFullNameFemale;

    @Column(name = "F_CONSENT", length = 255)
    private String consentIfAnyFemale;

    //sign

    public String getIdentificationNumberFemale() {
        return identificationNumberFemale;
    }

    public void setIdentificationNumberFemale(String identificationNumberFemale) {
        this.identificationNumberFemale = WebUtils.filterBlanks(identificationNumberFemale);
    }

    public Date getDateOfBirthFemale() {
        return dateOfBirthFemale;
    }

    public void setDateOfBirthFemale(Date dateOfBirthFemale) {
        this.dateOfBirthFemale = dateOfBirthFemale;
    }

    public int getAgeAtLastBirthDayFemale() {
        return ageAtLastBirthDayFemale;
    }

    public void setAgeAtLastBirthDayFemale(int ageAtLastBirthDayFemale) {
        this.ageAtLastBirthDayFemale = ageAtLastBirthDayFemale;
    }

    public String getNameInOfficialLanguageFemale() {
        return nameInOfficialLanguageFemale;
    }

    public void setNameInOfficialLanguageFemale(String nameInOfficialLanguageFemale) {
        this.nameInOfficialLanguageFemale = WebUtils.filterBlanks(nameInOfficialLanguageFemale);
    }

    public String getNameInEnglishFemale() {
        return nameInEnglishFemale;
    }

    public void setNameInEnglishFemale(String nameInEnglishFemale) {
        this.nameInEnglishFemale = WebUtils.filterBlanks(nameInEnglishFemale);
    }

    public String getResidentAddressFemale() {
        return residentAddressFemale;
    }

    public void setResidentAddressFemale(String residentAddressFemale) {
        this.residentAddressFemale = WebUtils.filterBlanks(residentAddressFemale);
    }

    public MRDivision getMrDivisionFemale() {
        return mrDivisionFemale;
    }

    public void setMrDivisionFemale(MRDivision mrDivisionFemale) {
        this.mrDivisionFemale = mrDivisionFemale;
    }

    public int getDurationFemale() {
        return durationFemale;
    }

    public void setDurationFemale(int durationFemale) {
        this.durationFemale = durationFemale;
    }

    public String getRankOrProfessionFemale() {
        return rankOrProfessionFemale;
    }

    public void setRankOrProfessionFemale(String rankOrProfessionFemale) {
        this.rankOrProfessionFemale = WebUtils.filterBlanks(rankOrProfessionFemale);
    }

    public String getTpNumberFemale() {
        return tpNumberFemale;
    }

    public void setTpNumberFemale(String tpNumberFemale) {
        this.tpNumberFemale = WebUtils.filterBlanks(tpNumberFemale);
    }

    public String getEmailFemale() {
        return emailFemale;
    }

    public void setEmailFemale(String emailFemale) {
        this.emailFemale = WebUtils.filterBlanks(emailFemale);
    }

    public String getFatherIdentificationNumberFemale() {
        return fatherIdentificationNumberFemale;
    }

    public void setFatherIdentificationNumberFemale(String fatherIdentificationNumberFemale) {
        this.fatherIdentificationNumberFemale = WebUtils.filterBlanks(fatherIdentificationNumberFemale);
    }

    public String getFatherFullNameFemale() {
        return fatherFullNameFemale;
    }

    public void setFatherFullNameFemale(String fatherFullNameFemale) {
        this.fatherFullNameFemale = WebUtils.filterBlanks(fatherFullNameFemale);
    }

    public String getConsentIfAnyFemale() {
        return consentIfAnyFemale;
    }

    public void setConsentIfAnyFemale(String consentIfAnyFemale) {
        this.consentIfAnyFemale = WebUtils.filterBlanks(consentIfAnyFemale);
    }

    public Race getFemaleRace() {
        return femaleRace;
    }

    public void setFemaleRace(Race femaleRace) {
        this.femaleRace = femaleRace;
    }

    public Person.CivilStatus getCivilStatusFemale() {
        return civilStatusFemale;
    }

    public void setCivilStatusFemale(Person.CivilStatus civilStatusFemale) {
        this.civilStatusFemale = civilStatusFemale;
    }
}
