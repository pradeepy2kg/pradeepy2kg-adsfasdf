package lk.rgd.crs.api.domain;

import javax.persistence.*;

/**
 * @author Ashoka Ekanayaka
 * To model the alterations which comes under secion 27A in the Act.
 * Father Info, Great Grand Father Info, Marriage Info and mothers name after marriage.
 */
@Embeddable
public class Alteration27A {
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
