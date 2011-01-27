package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.Country;
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
    //pin only
    private String identificationNumberMale;

    @Column(name = "M_DOB", nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfBirthMale;

    @ManyToOne
    @JoinColumn(name = "M_RACE_IDUKEY", nullable = true)
    private Race maleRace;

    @Column(name = "M_AGE_LAST_BD", nullable = true)
    private int ageAtLastBirthDayMale;

    @ManyToOne
    @JoinColumn(name = "M_COUNTRY_IDUKEY", nullable = true)
    private Country country;

    @Column(name = "M_PASSPORT", nullable = true)
    private String passport;

    @Column(name = "M_DATE_ARRIVAL", nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfArrival;

    @Column(name = "M_NAME_OFFICIAL", nullable = true, length = 600)
    private String nameInOfficialLanguageMale;

    @Column(name = "M_NAME_ENGLISH", nullable = true, length = 600)
    private String nameInEnglishMale;

    @Column(name = "M_ADDRESS_OFFICIAL", nullable = true, length = 255)
    private String residentAddressMaleInOfficialLang;

    @Column(name = "M_ADDRESS_ENGLISH", nullable = true, length = 255)
    private String residentAddressMaleInEnglish;

    @Column(name = "M_RANK_PROFESSION_OFFICIAL", nullable = true, length = 255)
    private String rankOrProfessionMaleInOfficialLang;

    @Column(name = "M_RANK_PROFESSION_ENGLISh", nullable = true, length = 255)
    private String rankOrProfessionMaleInEnglish;

    @Column(name = "M_CIVIL_STATE", nullable = true)
    private Person.CivilStatus civilStatusMale;

    @Column(name = "M_IDENTIFICATION_FATHER", length = 10, nullable = true)
    private String fatherIdentificationNumberMale;

    @Column(name = "M_FULL_NAME_FATHER_OFFICIAL", length = 600, nullable = true)
    private String fatherFullNameMaleInOfficialLang;

    @Column(name = "M_FULL_NAME_FATHER_ENGLISH", length = 600, nullable = true)
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
        this.fatherFullNameMaleInEnglish = WebUtils.filterBlanks(fatherFullNameMaleInEnglish);
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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = WebUtils.filterBlanks(passport);
    }

    public Date getDateOfArrival() {
        return dateOfArrival;
    }

    public void setDateOfArrival(Date dateOfArrival) {
        this.dateOfArrival = dateOfArrival;
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
        this.residentAddressMaleInEnglish = WebUtils.filterBlanks(residentAddressMaleInEnglish);
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
        this.rankOrProfessionMaleInEnglish = WebUtils.filterBlanks(rankOrProfessionMaleInEnglish);
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
