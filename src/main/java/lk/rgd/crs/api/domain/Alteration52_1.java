package lk.rgd.crs.api.domain;

import lk.rgd.common.util.WebUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.BitSet;

/**
 * @author Ashoka Ekanayaka
 *         To model the alterations which comes under secion 52_1 in the Act.
 *         Registration Info, Mother and Informant.
 */
@Embeddable
public class Alteration52_1 {

    public static final int DATE_OF_BIRTH = 21;
    public static final int PLACE_OF_BIRTH = 22;
    public static final int PLACE_OF_BIRTH_ENGLISH = 23;
    public static final int BIRTH_DIVISION = 24;
    public static final int GENDER = 25;

    public static final int MOTHER_FULLNAME = 26;
    public static final int MOTHER_NIC_OR_PIN = 27;
    public static final int MOTHER_BIRTH_DATE = 27;
    public static final int MOTHER_BIRTH_PLACE = 29;
    public static final int MOTHER_COUNTRY = 30;
    public static final int MOTHER_PASSPORT = 31;
    public static final int MOTHER_RACE = 32;
    public static final int MOTHER_AGE_AT_BIRTH = 33;
    public static final int MOTHER_ADDRESS = 34;

    public static final int INFORMANT_TYPE = 35;
    public static final int INFORMANT_NIC_OR_PIN = 36;
    public static final int INFORMANT_NAME = 37;
    public static final int INFORMANT_ADDRESS = 38;


    /**
     * The date of the birth
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfBirth;

    /**
     * The place of birth - usually the village or hospital name
     */
    @Column(nullable = true, length = 255)
    private String placeOfBirth;

    /**
     * The place of birth in English - usually the village or hospital name
     */
    @Column(nullable = true, length = 255)
    private String placeOfBirthEnglish;

    /**
     * The Birth/Death registration division where the birth is registered (Includes District)
     */
    @ManyToOne
    @JoinColumn(name = "bdDivisionUKey", nullable = true)
    private BDDivision birthDivision;

    /**
     * Gender 0 - male, 1 - female, 2 - unknown
     */
    @Column(nullable = true)
    private int childGender;

    @Embedded
    private MotherInfo mother;

    @Embedded
    private AlterationInformatInfo informant;

    @Column(nullable = true, length = 1000)
    private String natureOfError;

    public MotherInfo getMother() {
        return mother;
    }

    public void setMother(MotherInfo mother) {
        this.mother = mother;
    }

    public AlterationInformatInfo getInformant() {
        return informant;
    }

    public void setInformant(AlterationInformatInfo informant) {
        this.informant = informant;
    }

    public String getNatureOfError() {
        return natureOfError;
    }

    public void setNatureOfError(String natureOfError) {
        this.natureOfError = WebUtils.filterBlanksAndToUpper(natureOfError);
    }

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
        this.placeOfBirth = WebUtils.filterBlanksAndToUpper(placeOfBirth);
    }

    public String getPlaceOfBirthEnglish() {
        return placeOfBirthEnglish;
    }

    public void setPlaceOfBirthEnglish(String placeOfBirthEnglish) {
        this.placeOfBirthEnglish = WebUtils.filterBlanksAndToUpper(placeOfBirthEnglish);
    }

    public BDDivision getBirthDivision() {
        return birthDivision;
    }

    public void setBirthDivision(BDDivision birthDivision) {
        this.birthDivision = birthDivision;
    }

    public int getChildGender() {
        return childGender;
    }

    public void setChildGender(int childGender) {
        this.childGender = childGender;
    }
}
