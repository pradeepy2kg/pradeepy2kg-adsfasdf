package lk.rgd.crs.api.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * An instance representing child information submitted for the declaration of a birth (page 1 of the form)
 */
@Embeddable
public class ChildInfo implements Serializable {
    /**
     * This is the PIN number generated to the child
     */
    @Column(nullable = true)
    private Long pin;

    /**
     * The date of the birth
     */
    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfBirth;

    /**
     * The place of birth - usually the village or hospital name
     */
    @Column(nullable = true, length = 255)
    private String placeOfBirth;

    /**
     * 0-Yes, 1-No
     */
    @Column(nullable = true)
    private Integer birthAtHospital;

    /**
     * Name in Sinhala or Tamil
     */
    @Column(nullable = true, length = 600)
    private String childFullNameOfficialLang;

    /**
     * Name in English
     */
    @Column(nullable = true, length = 600)
    private String childFullNameEnglish;

    /**
     * Gender 0 - male, 1 - female, 2 - unknown
     */
    @Column(nullable = false)
    private int childGender;

    /**
     * The child gender as a String in the preferred language
     */
    @Transient
    private String childGenderPrint;

    /**
     * Weight in kilogrammes
     */
    @Column(nullable = true)
    private Float childBirthWeight;

    /**
     * Child rank according to the order of live births
     */
    @Column(nullable = true)
    private Integer childRank;

    /**
     * Number of children born along with the child being registered
     */
    @Column(nullable = true)
    private Integer numberOfChildrenBorn;

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getChildFullNameOfficialLang() {
        return childFullNameOfficialLang;
    }

    /**
     * Returns the full name in the official language, limited to the maxLength. e.g. as "අසංඛ චමත් පෙ..."
     * @param maxLength the maximum length of the name to return
     * @return length limited name in the official language
     */
    public String getChildFullNameOfficialLangToLength(int maxLength) {
        if (childFullNameOfficialLang != null && childFullNameOfficialLang.length() > maxLength) {
            return childFullNameOfficialLang.substring(0, maxLength - 3) + "...";
        }
        return childFullNameOfficialLang;
    }

    public void setChildFullNameOfficialLang(String childFullNameOfficialLang) {
        this.childFullNameOfficialLang = childFullNameOfficialLang;
    }

    public String getChildFullNameEnglish() {
        return childFullNameEnglish;
    }

    /**
     * Returns the full name in English language, limited to the maxLength. e.g. as "ASANKHA CHAMATH P..."
     * @param maxLength maxLength the maximum length of the name to return
     * @return length limited name in the English language
     */
    public String getChildFullNameEnglishLangToLength(int maxLength) {
        if (childFullNameEnglish != null && childFullNameEnglish.length() > maxLength) {
            return childFullNameEnglish.substring(0, maxLength-3) + "...";
        }
        return childFullNameEnglish;
    }

    public void setChildFullNameEnglish(String childFullNameEnglish) {
        this.childFullNameEnglish = childFullNameEnglish;
    }

    public int getChildGender() {
        return childGender;
    }

    public void setChildGender(int childGender) {
        this.childGender = childGender;
    }

    public Float getChildBirthWeight() {
        return childBirthWeight;
    }

    public void setChildBirthWeight(Float childBirthWeight) {
        this.childBirthWeight = childBirthWeight;
    }

    public Integer getChildRank() {
        return childRank;
    }

    public void setChildRank(Integer childRank) {
        this.childRank = childRank;
    }

    public Integer getNumberOfChildrenBorn() {
        return numberOfChildrenBorn;
    }

    public void setNumberOfChildrenBorn(Integer numberOfChildrenBorn) {
        this.numberOfChildrenBorn = numberOfChildrenBorn;
    }

    public Integer getBirthAtHospital() {
        return birthAtHospital;
    }

    public void setBirthAtHospital(Integer birthAtHospital) {
        this.birthAtHospital = birthAtHospital;
    }

    public Long getPin() {
        return pin;
    }

    public void setPin(Long pin) {
        this.pin = pin;
    }

    public String getChildGenderPrint() {
        return childGenderPrint;
    }

    public void setChildGenderPrint(String childGenderPrint) {
        this.childGenderPrint = childGenderPrint;
    }
}
