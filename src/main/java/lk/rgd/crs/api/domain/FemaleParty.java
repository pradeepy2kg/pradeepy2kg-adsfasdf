package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.Country;
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

    @ManyToOne
    @JoinColumn(name = "F_RACE_IDUKEY", nullable = true)
    private Race femaleRace;

    @Column(name = "F_AGE_LAST_BD", nullable = true)
    private int ageAtLastBirthDayFemale;

    @ManyToOne
    @JoinColumn(name = "F_COUNTRY_IDUKEY", nullable = true)
    private Country country;

    @Column(name = "F_PASSPORT", nullable = true, length = 15)
    private String passport;

    @Column(name = "F_DATE_ARRIVAL", nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfArrival;

    @Column(name = "F_NAME_OFFICIAL", nullable = true, length = 600)
    private String nameInOfficialLanguageFemale;

    @Column(name = "F_NAME_ENGLISH", nullable = true, length = 600)
    private String nameInEnglishFemale;

    @Column(name = "F_ADDRESS_OFFICIAL", nullable = true, length = 255)
    private String residentAddressFemaleInOfficialLang;

    @Column(name = "F_ADDRESS_ENGLISH", nullable = true, length = 255)
    private String residentAddressFemaleInEnglish;

    @Column(name = "F_RANK_PROFESSION_OFFICIAL", nullable = true, length = 255)
    private String rankOrProfessionFemaleInOfficialLang;

    @Column(name = "F_RANK_PROFESSION_ENGLISH", nullable = true, length = 255)
    private String rankOrProfessionFemaleInEnglish;

    @Column(name = "F_CIVIL_STATE", nullable = true)
    private Person.CivilStatus civilStatusFemale;

    @Column(name = "F_IDENTIFICATION_FATHER", length = 10, nullable = true)
    private String fatherIdentificationNumberFemale;

    @Column(name = "F_FULL_NAME_FATHER_OFFICIAL", length = 600, nullable = true)
    private String fatherFullNameFemaleInOfficialLang;

    @Column(name = "F_FULL_NAME_FATHER_ENGLISH", length = 600, nullable = true)
    private String fatherFullNameFemaleInEnglish;

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

    public Race getFemaleRace() {
        return femaleRace;
    }

    public void setFemaleRace(Race femaleRace) {
        this.femaleRace = femaleRace;
    }

    public int getAgeAtLastBirthDayFemale() {
        return ageAtLastBirthDayFemale;
    }

    public void setAgeAtLastBirthDayFemale(int ageAtLastBirthDayFemale) {
        this.ageAtLastBirthDayFemale = ageAtLastBirthDayFemale;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Date getDateOfArrival() {
        return dateOfArrival;
    }

    public void setDateOfArrival(Date dateOfArrival) {
        this.dateOfArrival = dateOfArrival;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = WebUtils.filterBlanks(passport);
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

    public String getResidentAddressFemaleInOfficialLang() {
        return residentAddressFemaleInOfficialLang;
    }

    public void setResidentAddressFemaleInOfficialLang(String residentAddressFemaleInOfficialLang) {
        this.residentAddressFemaleInOfficialLang = WebUtils.filterBlanks(residentAddressFemaleInOfficialLang);
    }

    public String getResidentAddressFemaleInEnglish() {
        return residentAddressFemaleInEnglish;
    }

    public void setResidentAddressFemaleInEnglish(String residentAddressFemaleInEnglish) {
        this.residentAddressFemaleInEnglish = WebUtils.filterBlanks(residentAddressFemaleInEnglish);
    }

    public String getRankOrProfessionFemaleInOfficialLang() {
        return rankOrProfessionFemaleInOfficialLang;
    }

    public void setRankOrProfessionFemaleInOfficialLang(String rankOrProfessionFemaleInOfficialLang) {
        this.rankOrProfessionFemaleInOfficialLang = WebUtils.filterBlanks(rankOrProfessionFemaleInOfficialLang);
    }

    public String getRankOrProfessionFemaleInEnglish() {
        return rankOrProfessionFemaleInEnglish;
    }

    public void setRankOrProfessionFemaleInEnglish(String rankOrProfessionFemaleInEnglish) {
        this.rankOrProfessionFemaleInEnglish = WebUtils.filterBlanks(rankOrProfessionFemaleInEnglish);
    }

    public Person.CivilStatus getCivilStatusFemale() {
        return civilStatusFemale;
    }

    public void setCivilStatusFemale(Person.CivilStatus civilStatusFemale) {
        this.civilStatusFemale = civilStatusFemale;
    }

    public String getFatherIdentificationNumberFemale() {
        return fatherIdentificationNumberFemale;
    }

    public void setFatherIdentificationNumberFemale(String fatherIdentificationNumberFemale) {
        this.fatherIdentificationNumberFemale = WebUtils.filterBlanks(fatherIdentificationNumberFemale);
    }

    public String getFatherFullNameFemaleInOfficialLang() {
        return fatherFullNameFemaleInOfficialLang;
    }

    public void setFatherFullNameFemaleInOfficialLang(String fatherFullNameFemaleInOfficialLang) {
        this.fatherFullNameFemaleInOfficialLang = WebUtils.filterBlanks(fatherFullNameFemaleInOfficialLang);
    }

    public String getFatherFullNameFemaleInEnglish() {
        return fatherFullNameFemaleInEnglish;
    }

    public void setFatherFullNameFemaleInEnglish(String fatherFullNameFemaleInEnglish) {
        this.fatherFullNameFemaleInEnglish = WebUtils.filterBlanks(fatherFullNameFemaleInEnglish);
    }
}
