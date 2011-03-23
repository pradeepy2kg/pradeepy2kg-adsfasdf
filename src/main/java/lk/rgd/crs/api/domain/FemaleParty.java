package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.Country;
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

    @Column(nullable = true)
    //pin only a pin
    private String identificationNumberFemale;

    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfBirthFemale;

    @ManyToOne
    @JoinColumn(name = "femaleRaceId", nullable = true)
    private Race femaleRace;

    @Column(nullable = true)
    private int ageAtLastBirthDayFemale;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Country femaleCountry;

    @Column(nullable = true, length = 15)
    private String femalePassport;

    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date femaleDateOfArrival;

    @Column(nullable = true, length = 600)
    private String nameInOfficialLanguageFemale;

    @Column(nullable = true, length = 600)
    private String nameInEnglishFemale;

    @Column(nullable = true, length = 255)
    private String residentAddressFemaleInOfficialLang;

    @Column(nullable = true, length = 255)
    private String residentAddressFemaleInEnglish;

    @Column(nullable = true, length = 255)
    private String rankOrProfessionFemaleInOfficialLang;

    @Column(nullable = true, length = 255)
    private String rankOrProfessionFemaleInEnglish;

    @Column(nullable = true)
    private Person.CivilStatus civilStatusFemale;

    @Column(length = 12, nullable = true)
    private String fatherIdentificationNumberFemale;

    @Column(length = 600, nullable = true)
    private String fatherFullNameFemaleInOfficialLang;

    @Column(length = 600, nullable = true)
    private String fatherFullNameFemaleInEnglish;

    public String getIdentificationNumberFemale() {
        return identificationNumberFemale;
    }

    public void setIdentificationNumberFemale(String identificationNumberFemale) {
        this.identificationNumberFemale = identificationNumberFemale;
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

    public Country getFemaleCountry() {
        return femaleCountry;
    }

    public void setFemaleCountry(Country femaleCountry) {
        this.femaleCountry = femaleCountry;
    }

    public Date getFemaleDateOfArrival() {
        return femaleDateOfArrival;
    }

    public void setFemaleDateOfArrival(Date femaleDateOfArrival) {
        this.femaleDateOfArrival = femaleDateOfArrival;
    }

    public String getFemalePassport() {
        return femalePassport;
    }

    public void setFemalePassport(String femalePassport) {
        this.femalePassport = femalePassport;
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
        this.nameInEnglishFemale = WebUtils.filterBlanksAndToUpper(nameInEnglishFemale);
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
        this.residentAddressFemaleInEnglish = WebUtils.filterBlanksAndToUpper(residentAddressFemaleInEnglish);
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
        this.rankOrProfessionFemaleInEnglish = WebUtils.filterBlanksAndToUpper(rankOrProfessionFemaleInEnglish);
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
        this.fatherFullNameFemaleInEnglish = WebUtils.filterBlanksAndToUpper(fatherFullNameFemaleInEnglish);
    }
}
