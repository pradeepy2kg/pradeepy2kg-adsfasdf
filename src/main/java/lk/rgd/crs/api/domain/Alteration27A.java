package lk.rgd.crs.api.domain;

import lk.rgd.common.util.WebUtils;

import javax.persistence.*;
import java.util.BitSet;

/**
 * @author Ashoka Ekanayaka
 * To model the alterations which comes under secion 27A in the Act.
 * Father Info, Great Grand Father Info, Marriage Info and mothers name after marriage.
 */
@Embeddable
public class Alteration27A {
    public static final int GRAND_FATHER_FULLNAME = 2;
    public static final int GRAND_FATHER_NIC_OR_PIN = 3;
    public static final int GRAND_FATHER_BIRTH_YEAR = 4;
    public static final int GRAND_FATHER_BIRTH_PLACE = 5;

    public static final int GREAT_GRAND_FATHER_FULLNAME = 6;
    public static final int GREAT_GRAND_FATHER_NIC_OR_PIN = 7;
    public static final int GREAT_GRAND_FATHER_BIRTH_YEAR = 8;
    public static final int GREAT_GRAND_FATHER_BIRTH_PLACE = 9;

    public static final int FATHER_FULLNAME = 10;
    public static final int FATHER_NIC_OR_PIN = 11;
    public static final int FATHER_BIRTH_DATE = 12;
    public static final int FATHER_BIRTH_PLACE = 13;
    public static final int FATHER_COUNTRY = 14;
    public static final int FATHER_PASSPORT = 15;
    public static final int FATHER_RACE = 16;

    public static final int WERE_PARENTS_MARRIED = 17;
    public static final int PLACE_OF_MARRIAGE = 18;
    public static final int DATE_OF_MARRIAGE = 19;

    public static final int MOTHER_NAME_AFTER_MARRIAGE = 20;

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
        this.mothersNameAfterMarriage = WebUtils.filterBlanksAndToUpper(mothersNameAfterMarriage);
    }

}
