package lk.rgd.crs.api.domain;

import javax.persistence.*;
import java.util.BitSet;

/**
 * @author Ashoka Ekanayaka
 * To model the alterations which comes under secion 27A in the Act.
 * Father Info, Great Grand Father Info, Marriage Info and mothers name after marriage.
 */
@Embeddable
public class Alteration27A {
    public static final int GRAND_FATHER_FULLNAME = 0;
    public static final int GRAND_FATHER_NIC_OR_PIN = 1;
    public static final int GRAND_FATHER_BIRTH_YEAR = 2;
    public static final int GRAND_FATHER_BIRTH_PLACE = 3;

    public static final int GREAT_GRAND_FATHER_FULLNAME = 4;
    public static final int GREAT_GRAND_FATHER_NIC_OR_PIN = 5;
    public static final int GREAT_GRAND_FATHER_BIRTH_YEAR = 6;
    public static final int GREAT_GRAND_FATHER_BIRTH_PLACE = 7;

    public static final int FATHER_FULLNAME = 8;
    public static final int FATHER_NIC_OR_PIN = 9;
    public static final int FATHER_BIRTH_DATE = 10;
    public static final int FATHER_BIRTH_PLACE = 11;
    public static final int FATHER_COUNTRY = 12;
    public static final int FATHER_PASSPORT = 13;
    public static final int FATHER_RACE = 14;

    public static final int WERE_PARENTS_MARRIED = 15;
    public static final int PLACE_OF_MARRIAGE = 16;
    public static final int DATE_OF_MARRIAGE = 17;

    public static final int MOTHER_NAME_AFTER_MARRIAGE = 18;

    @Embedded
    private GrandFatherInfo grandFather;

    @Embedded
    private FatherInfo father;

    @Embedded
    private MarriageInfo marriage;

    @Column(nullable = true, length=600)
    private String mothersNameAfterMarriage;

    public GrandFatherInfo getGrandFather() {
        return grandFather;
    }

    public void setGrandFather(GrandFatherInfo grandFather) {
        this.grandFather = grandFather;
    }

    public FatherInfo getFather() {
        return father;
    }

    public void setFather(FatherInfo father) {
        this.father = father;
    }

    public MarriageInfo getMarriage() {
        return marriage;
    }

    public void setMarriage(MarriageInfo marriage) {
        this.marriage = marriage;
    }

    public String getMothersNameAfterMarriage() {
        return mothersNameAfterMarriage;
    }

    public void setMothersNameAfterMarriage(String mothersNameAfterMarriage) {
        this.mothersNameAfterMarriage = mothersNameAfterMarriage;
    }

}
