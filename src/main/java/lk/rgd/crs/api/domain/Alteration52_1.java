package lk.rgd.crs.api.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Ashoka Ekanayaka
 * To model the alterations which comes under secion 52_1 in the Act.
 * Registration Info, Mother and Informant.
 */
@Embeddable
public class Alteration52_1 {
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
     * The place of birth in English - usually the village or hospital name
     */
    @Column(nullable = true, length = 255)
    private String placeOfBirthEnglish;


    /**
     * The Birth/Death registration division where the birth is registered (Includes District)
     */
    @ManyToOne
    @JoinColumn(name = "bdDivisionUKey", nullable = false)
    private BDDivision birthDivision;

    /**
     * Gender 0 - male, 1 - female, 2 - unknown
     */
    @Column(nullable = false)
    private int childGender;

    @Embedded
    private MotherInfo mother;

    @Embedded
    private InformantInfo informant;

    @Column(nullable = true, length = 1000)
    private String natureOfError;

    public MotherInfo getMother() {
        return mother;
    }

    public void setMother(MotherInfo mother) {
        this.mother = mother;
    }

    public InformantInfo getInformant() {
        return informant;
    }

    public void setInformant(InformantInfo informant) {
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
