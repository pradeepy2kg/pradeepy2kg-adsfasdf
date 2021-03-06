package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.util.WebUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Indunil Moremada
 *         An instance representing death information of the death person
 */

@Embeddable
public class DeathPersonInfo implements Serializable, Cloneable {

    @Column(nullable = true, length = 12)
    private String deathPersonPINorNIC;

    @ManyToOne
    @JoinColumn(name = "deathPersonCountryId")
    private Country deathPersonCountry;

    @Column(nullable = true)
    private String deathPersonPassportNo;

    @Column(nullable = true)
    private Integer deathPersonAge;

    @Column(nullable = true)
    private Integer deathPersonAgeMonth;

    @Column(nullable = true)
    private Integer deathPersonAgeDate;

    /**
     * Gender 0 - male, 1 - female, 2 - unknown
     */
    @Column(nullable = true)
    private int deathPersonGender;

    /**
     * Race of death person
     */
    @ManyToOne
    @JoinColumn(name = "deathPersonRace")
    private Race deathPersonRace;

    @Column(nullable = true, length = 600)
    private String deathPersonNameOfficialLang;

    @Column(nullable = true, length = 600)
    private String deathPersonNameInEnglish;

    @Column(nullable = true, length = 255)
    private String deathPersonPermanentAddress;

    @Column(nullable = true, length = 12)
    private String deathPersonFatherPINorNIC;

    @Column(nullable = true)
    private String deathPersonFatherFullName;

    @Column(nullable = true, length = 12)
    private String deathPersonMotherPINorNIC;

    @Column(nullable = true)
    private String deathPersonMotherFullName;

    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date deathPersonDOB;

    @ManyToOne
    @JoinColumn(nullable = true)
    private DSDivision dsDivisionOfPermanentAddress;

    @Column(nullable = true)
    private String lastAddressOfMissingPerson;

    @Column(nullable = true)
    private String rankOrProfession;

    @Column(nullable = true)
    private boolean pensioner;

    //following columns are for female who below age 49

    @Column(nullable = true)
    private Boolean pregnantAtTimeOfDeath = null;

    @Column(nullable = true)
    private Boolean givenABirthWithInPreviouse6Weeks = null;

    @Column(nullable = true)
    private Boolean anAbortionTakenPlace = null;

    @Column(nullable = true)
    private Integer daysBeforeAbortionOrBirth;

    @Column(nullable = true)
    private boolean personIdentified;

    //todo change nullable to false when we have all the data
    @ManyToOne
    @JoinColumn(name = "gnDivisionUKey", nullable = true)
    private GNDivision gnDivision;

    public String getDeathPersonPINorNIC() {
        return deathPersonPINorNIC;
    }

    public void setDeathPersonPINorNIC(String deathPersonPINorNIC) {
        this.deathPersonPINorNIC = WebUtils.filterBlanksAndToUpperAndTrim(deathPersonPINorNIC, 12, "deathPersonPINorNIC");
    }

    public Country getDeathPersonCountry() {
        return deathPersonCountry;
    }

    public void setDeathPersonCountry(Country deathPersonCountry) {
        this.deathPersonCountry = deathPersonCountry;
    }

    public String getDeathPersonPassportNo() {
        return deathPersonPassportNo;
    }

    public void setDeathPersonPassportNo(String deathPersonPassportNo) {
        this.deathPersonPassportNo = WebUtils.filterBlanksAndToUpperAndTrim(deathPersonPassportNo, 255, "deathPersonPassportNo");
    }

    public Integer getDeathPersonAge() {
        return deathPersonAge;
    }

    public void setDeathPersonAge(Integer deathPersonAge) {
        this.deathPersonAge = deathPersonAge;
    }

    public Integer getDeathPersonAgeMonth() {
        return deathPersonAgeMonth;
    }

    public void setDeathPersonAgeMonth(Integer deathPersonAgeMonth) {
        this.deathPersonAgeMonth = deathPersonAgeMonth == 0 ? null : deathPersonAgeMonth;
    }

    public Integer getDeathPersonAgeDate() {
        return deathPersonAgeDate;
    }

    public void setDeathPersonAgeDate(Integer deathPersonAgeDate) {
        this.deathPersonAgeDate = deathPersonAgeDate == 0 ? null : deathPersonAgeDate;
    }

    public int getDeathPersonGender() {
        return deathPersonGender;
    }

    public void setDeathPersonGender(int deathPersonGender) {
        this.deathPersonGender = deathPersonGender;
    }

    public Race getDeathPersonRace() {
        return deathPersonRace;
    }

    public void setDeathPersonRace(Race deathPersonRace) {
        this.deathPersonRace = deathPersonRace;
    }

    public String getDeathPersonNameOfficialLang() {
        return deathPersonNameOfficialLang;
    }

    public void setDeathPersonNameOfficialLang(String deathPersonNameOfficialLang) {
        this.deathPersonNameOfficialLang = WebUtils.filterBlanksAndToUpperAndTrim(deathPersonNameOfficialLang, 600, "deathPersonNameOfficialLang");
    }

    public String getDeathPersonNameInEnglish() {
        return deathPersonNameInEnglish;
    }

    public void setDeathPersonNameInEnglish(String deathPersonNameInEnglish) {
        this.deathPersonNameInEnglish = WebUtils.filterBlanksAndToUpperAndTrim(deathPersonNameInEnglish, 600, "deathPersonNameInEnglish");
    }

    public String getDeathPersonPermanentAddress() {
        return deathPersonPermanentAddress;
    }

    public void setDeathPersonPermanentAddress(String deathPersonPermanentAddress) {
        this.deathPersonPermanentAddress = WebUtils.filterBlanksAndToUpperAndTrim(deathPersonPermanentAddress, 255, "deathPersonPermanentAddress");
    }

    public String getDeathPersonFatherPINorNIC() {
        return deathPersonFatherPINorNIC;
    }

    public void setDeathPersonFatherPINorNIC(String deathPersonFatherPINorNIC) {
        this.deathPersonFatherPINorNIC = WebUtils.filterBlanksAndToUpperAndTrim(deathPersonFatherPINorNIC, 12, "deathPersonFatherPINorNIC");
    }

    public String getDeathPersonFatherFullName() {
        return deathPersonFatherFullName;
    }

    public void setDeathPersonFatherFullName(String deathPersonFatherFullName) {
        this.deathPersonFatherFullName = WebUtils.filterBlanksAndToUpperAndTrim(deathPersonFatherFullName, 255, "deathPersonFatherFullName");
    }

    public String getDeathPersonMotherPINorNIC() {
        return deathPersonMotherPINorNIC;
    }

    public void setDeathPersonMotherPINorNIC(String deathPersonMotherPINorNIC) {
        this.deathPersonMotherPINorNIC = WebUtils.filterBlanksAndToUpperAndTrim(deathPersonMotherPINorNIC, 12, "deathPersonMotherPINorNIC");
    }

    public String getDeathPersonMotherFullName() {
        return deathPersonMotherFullName;
    }

    public void setDeathPersonMotherFullName(String deathPersonMotherFullName) {
        this.deathPersonMotherFullName = WebUtils.filterBlanksAndToUpperAndTrim(deathPersonMotherFullName, 255, "deathPersonMotherFullName");
    }

    public Date getDeathPersonDOB() {
        return deathPersonDOB;
    }

    public void setDeathPersonDOB(Date deathPersonDOB) {
        this.deathPersonDOB = deathPersonDOB;
    }

    public DSDivision getDsDivisionOfPermanentAddress() {
        return dsDivisionOfPermanentAddress;
    }

    public void setDsDivisionOfPermanentAddress(DSDivision dsDivisionOfPermanentAddress) {
        this.dsDivisionOfPermanentAddress = dsDivisionOfPermanentAddress;
    }

    public String getLastAddressOfMissingPerson() {
        return lastAddressOfMissingPerson;
    }

    public void setLastAddressOfMissingPerson(String lastAddressOfMissingPerson) {
        this.lastAddressOfMissingPerson = WebUtils.filterBlanksAndToUpperAndTrim(lastAddressOfMissingPerson, 255, "lastAddressOfMissingPerson");
    }

    public String getRankOrProfession() {
        return rankOrProfession;
    }

    public void setRankOrProfession(String rankOrProfession) {
        this.rankOrProfession = WebUtils.filterBlanksAndToUpperAndTrim(rankOrProfession, 255, "rankOrProfession");
    }

    public boolean isPensioner() {
        return pensioner;
    }

    public void setPensioner(boolean pensioner) {
        this.pensioner = pensioner;
    }

    public Boolean isPregnantAtTimeOfDeath() {
        return pregnantAtTimeOfDeath;
    }

    public void setPregnantAtTimeOfDeath(Boolean pregnantAtTimeOfDeath) {
        this.pregnantAtTimeOfDeath = pregnantAtTimeOfDeath;
    }

    public Boolean isGivenABirthWithInPreviouse6Weeks() {
        return givenABirthWithInPreviouse6Weeks;
    }

    public void setGivenABirthWithInPreviouse6Weeks(Boolean givenABirthWithInPreviouse6Weeks) {
        this.givenABirthWithInPreviouse6Weeks = givenABirthWithInPreviouse6Weeks;
    }

    public Boolean isAnAbortionTakenPlace() {
        return anAbortionTakenPlace;
    }

    public void setAnAbortionTakenPlace(Boolean anAbortionTakenPlace) {
        this.anAbortionTakenPlace = anAbortionTakenPlace;
    }

    public Integer getDaysBeforeAbortionOrBirth() {
        return daysBeforeAbortionOrBirth;
    }

    public void setDaysBeforeAbortionOrBirth(Integer daysBeforeAbortionOrBirth) {
        this.daysBeforeAbortionOrBirth = daysBeforeAbortionOrBirth;
    }

    public boolean isPersonIdentified() {
        return personIdentified;
    }

    public void setPersonIdentified(boolean personIdentified) {
        this.personIdentified = personIdentified;
    }

    /**
     * Returns the full name in the official language, limited to the maxLength.
     *
     * @param maxLength the maximum length of the name to return
     * @return length limited name in the official language
     */
    public String getDeathPersonNameOfficialLangToLength(int maxLength) {
        if (deathPersonNameOfficialLang != null && deathPersonNameOfficialLang.length() > maxLength) {
            return "..." + deathPersonNameOfficialLang.substring(deathPersonNameOfficialLang.length() - maxLength + 3,
                deathPersonNameOfficialLang.length());
        }
        return deathPersonNameOfficialLang;
    }

    @Override
    protected DeathPersonInfo clone() throws CloneNotSupportedException {
        return (DeathPersonInfo) super.clone();
    }

    public GNDivision getGnDivision() {
        return gnDivision;
    }

    public void setGnDivision(GNDivision gnDivision) {
        this.gnDivision = gnDivision;
    }
}
