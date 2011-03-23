package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.Country;
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

    @Column(nullable = true)
    //pin only
    private String identificationNumberMale;

    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfBirthMale;

    @ManyToOne
    @JoinColumn(name = "maleRaceId", nullable = true)
    private Race maleRace;

    @Column(nullable = true)
    private int ageAtLastBirthDayMale;

    @ManyToOne
    @JoinColumn(name = "maleCountryId", nullable = true)
    private Country maleCountry;

    @Column(nullable = true)
    private String malePassport;

    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date maleDateOfArrival;

    @Column(nullable = true, length = 600)
    private String nameInOfficialLanguageMale;

    @Column(nullable = true, length = 600)
    private String nameInEnglishMale;

    @Column(nullable = true, length = 255)
    private String residentAddressMaleInOfficialLang;

    @Column(nullable = true, length = 255)
    private String residentAddressMaleInEnglish;

    @Column(nullable = true, length = 255)
    private String rankOrProfessionMaleInOfficialLang;

    @Column(nullable = true, length = 255)
    private String rankOrProfessionMaleInEnglish;

    @Column(nullable = true)
    private Person.CivilStatus civilStatusMale;

    @Column(length = 12, nullable = true)
    private String fatherIdentificationNumberMale;

    @Column(length = 600, nullable = true)
    private String fatherFullNameMaleInOfficialLang;

    @Column(length = 600, nullable = true)
    private String fatherFullNameMaleInEnglish;

    public String getIdentificationNumberMale() {
        return identificationNumberMale;
    }

    public void setIdentificationNumberMale(String identificationNumberMale) {
        this.identificationNumberMale = identificationNumberMale;
    }

    public String getFatherFullNameMaleInEnglish() {
        return fatherFullNameMaleInEnglish;
    }

    public void setFatherFullNameMaleInEnglish(String fatherFullNameMaleInEnglish) {
        this.fatherFullNameMaleInEnglish = WebUtils.filterBlanksAndToUpper(fatherFullNameMaleInEnglish);
    }

    public Date getDateOfBirthMale() {
        return dateOfBirthMale;
    }

    public void setDateOfBirthMale(Date dateOfBirthMale) {
        this.dateOfBirthMale = dateOfBirthMale;
    }

    public Race getMaleRace() {
        return maleRace;
    }

    public void setMaleRace(Race maleRace) {
        this.maleRace = maleRace;
    }

    public int getAgeAtLastBirthDayMale() {
        return ageAtLastBirthDayMale;
    }

    public void setAgeAtLastBirthDayMale(int ageAtLastBirthDayMale) {
        this.ageAtLastBirthDayMale = ageAtLastBirthDayMale;
    }

    public Country getMaleCountry() {
        return maleCountry;
    }

    public void setMaleCountry(Country maleCountry) {
        this.maleCountry = maleCountry;
    }

    public String getMalePassport() {
        return malePassport;
    }

    public void setMalePassport(String malePassport) {
        this.malePassport = malePassport;
    }

    public Date getMaleDateOfArrival() {
        return maleDateOfArrival;
    }

    public void setMaleDateOfArrival(Date maleDateOfArrival) {
        this.maleDateOfArrival = maleDateOfArrival;
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
        this.nameInEnglishMale = WebUtils.filterBlanksAndToUpper(nameInEnglishMale);
    }

    public String getResidentAddressMaleInOfficialLang() {
        return residentAddressMaleInOfficialLang;
    }

    public void setResidentAddressMaleInOfficialLang(String residentAddressMaleInOfficialLang) {
        this.residentAddressMaleInOfficialLang = WebUtils.filterBlanks(residentAddressMaleInOfficialLang);
    }

    public String getResidentAddressMaleInEnglish() {
        return residentAddressMaleInEnglish;
    }

    public void setResidentAddressMaleInEnglish(String residentAddressMaleInEnglish) {
        this.residentAddressMaleInEnglish = WebUtils.filterBlanksAndToUpper(residentAddressMaleInEnglish);
    }

    public String getRankOrProfessionMaleInOfficialLang() {
        return rankOrProfessionMaleInOfficialLang;
    }

    public void setRankOrProfessionMaleInOfficialLang(String rankOrProfessionMaleInOfficialLang) {
        this.rankOrProfessionMaleInOfficialLang = WebUtils.filterBlanks(rankOrProfessionMaleInOfficialLang);
    }

    public String getRankOrProfessionMaleInEnglish() {
        return rankOrProfessionMaleInEnglish;
    }

    public void setRankOrProfessionMaleInEnglish(String rankOrProfessionMaleInEnglish) {
        this.rankOrProfessionMaleInEnglish = WebUtils.filterBlanksAndToUpper(rankOrProfessionMaleInEnglish);
    }

    public Person.CivilStatus getCivilStatusMale() {
        return civilStatusMale;
    }

    public void setCivilStatusMale(Person.CivilStatus civilStatusMale) {
        this.civilStatusMale = civilStatusMale;
    }

    public String getFatherIdentificationNumberMale() {
        return fatherIdentificationNumberMale;
    }

    public void setFatherIdentificationNumberMale(String fatherIdentificationNumberMale) {
        this.fatherIdentificationNumberMale = WebUtils.filterBlanks(fatherIdentificationNumberMale);
    }

    public String getFatherFullNameMaleInOfficialLang() {
        return fatherFullNameMaleInOfficialLang;
    }

    public void setFatherFullNameMaleInOfficialLang(String fatherFullNameMaleInOfficialLang) {
        this.fatherFullNameMaleInOfficialLang = WebUtils.filterBlanks(fatherFullNameMaleInOfficialLang);
    }
}
