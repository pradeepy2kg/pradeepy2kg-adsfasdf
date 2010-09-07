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
    public static final int GRAND_FATHER_FULLNAME = 1;
    public static final int GRAND_FATHER_NIC_OR_PIN = 2;
    public static final int GRAND_FATHER_BIRTH_YEAR = 3;
    public static final int GRAND_FATHER_BIRTH_PLACE = 4;

    public static final int GREAT_GRAND_FATHER_FULLNAME = 5;
    public static final int GREAT_GRAND_FATHER_NIC_OR_PIN = 6;
    public static final int GREAT_GRAND_FATHER_BIRTH_YEAR = 7;
    public static final int GREAT_GRAND_FATHER_BIRTH_PLACE = 8;

    public static final int FATHER_FULLNAME = 9;
    public static final int FATHER_NIC_OR_PIN = 10;
    public static final int FATHER_BIRTH_YEAR = 11;
    public static final int FATHER_BIRTH_PLACE = 12;
    public static final int FATHER_COUNTRY = 13;
    public static final int FATHER_PASSPORT = 14;
    public static final int FATHER_RACE = 15;

    public static final int WERE_PARENTS_MARRIED = 16;
    public static final int PLACE_OF_MARRIAGE = 17;
    public static final int DATE_OF_MARRIAGE = 18;

    public static final int MOTHER_NAME_AFTER_MARRIAGE = 19;

    @Embedded
    private GrandFatherInfo grandFather;

    @Embedded
    private FatherInfo father;

    @Embedded
    private MarriageInfo marriage;

    @Column(nullable = true, length=600)
    private String mothersNameAfterMarriage;

    /**
     * Contains the approval bit set for each field.
     */
    @Column(nullable = false)
    private BitSet approvalStatuses27A;

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

    public BitSet getApprovalStatuses() {
        return approvalStatuses27A;
    }

    public void setApprovalStatuses(BitSet approvalStatuses) {
        this.approvalStatuses27A = approvalStatuses;
    }
}
