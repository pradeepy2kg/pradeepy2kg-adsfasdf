package lk.rgd.crs.api.domain;

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

    public static final int DATE_OF_BIRTH = 0;
    public static final int PLACE_OF_BIRTH = 1;
    public static final int PLACE_OF_BIRTH_ENGLISH = 2;
    public static final int BIRTH_DIVISION = 3;
    public static final int GENDER = 4;

    public static final int MOTHER_FULLNAME = 5;
    public static final int MOTHER_NIC_OR_PIN = 6;
    public static final int MOTHER_BIRTH_DATE = 7;
    public static final int MOTHER_BIRTH_PLACE = 8;
    public static final int MOTHER_COUNTRY = 9;
    public static final int MOTHER_PASSPORT = 10;
    public static final int MOTHER_RACE = 11;
    public static final int MOTHER_AGE_AT_BIRTH = 12;
    public static final int MOTHER_ADDRESS = 13;

    public static final int INFORMANT_TYPE = 14;
    public static final int INFORMANT_NIC_OR_PIN = 15;
    public static final int INFORMANT_NAME = 16;
    public static final int INFORMANT_ADDRESS = 17;




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
        this.natureOfError = natureOfError;
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
        this.placeOfBirth = placeOfBirth;
    }

    public String getPlaceOfBirthEnglish() {
        return placeOfBirthEnglish;
    }

    public void setPlaceOfBirthEnglish(String placeOfBirthEnglish) {
        this.placeOfBirthEnglish = placeOfBirthEnglish;
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
