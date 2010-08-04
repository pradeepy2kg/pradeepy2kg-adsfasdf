package lk.rgd.crs.api.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Java bean instance to capture Other information such as Marriage, GrandFather/Great Grand Father  and the Informant
 * as entered by page 3 of the birth declaration form
 */
@Embeddable
public class GrandFatherInfo implements Serializable {
    // If grandfather of the child born in Sri Lanka, grandfather's details

    @Column(nullable = true, length = 600)
    private String grandFatherFullName;

    @Column(nullable = true)
    private Integer grandFatherBirthYear;

    @Column(nullable = true, length = 60)
    private String grandFatherBirthPlace;

    @Column(nullable = true, length = 10)
    private String grandFatherNICorPIN;

    // If the father was not born in Sri Lanka and if great grandfather born in Sri Lanka great grand father's details
    @Column(nullable = true, length = 600)
    private String greatGrandFatherFullName;

    @Column(nullable = true)
    private Integer greatGrandFatherBirthYear;

    @Column(nullable = true, length = 60)
    private String greatGrandFatherBirthPlace;

    @Column(nullable = true, length = 10)
    private String greatGrandFatherNICorPIN;

    public String getGrandFatherFullName() {
        return grandFatherFullName;
    }

    public void setGrandFatherFullName(String grandFatherFullName) {
        this.grandFatherFullName = grandFatherFullName;
    }

    public Integer getGrandFatherBirthYear() {
        return grandFatherBirthYear;
    }

    public void setGrandFatherBirthYear(Integer grandFatherBirthYear) {
        this.grandFatherBirthYear = grandFatherBirthYear;
    }

    public String getGrandFatherBirthPlace() {
        return grandFatherBirthPlace;
    }

    public void setGrandFatherBirthPlace(String grandFatherBirthPlace) {
        this.grandFatherBirthPlace = grandFatherBirthPlace;
    }

    public String getGreatGrandFatherFullName() {
        return greatGrandFatherFullName;
    }

    public void setGreatGrandFatherFullName(String greatGrandFatherFullName) {
        this.greatGrandFatherFullName = greatGrandFatherFullName;
    }

    public Integer getGreatGrandFatherBirthYear() {
        return greatGrandFatherBirthYear;
    }

    public void setGreatGrandFatherBirthYear(Integer greatGrandFatherBirthYear) {
        this.greatGrandFatherBirthYear = greatGrandFatherBirthYear;
    }

    public String getGreatGrandFatherBirthPlace() {
        return greatGrandFatherBirthPlace;
    }

    public void setGreatGrandFatherBirthPlace(String greatGrandFatherBirthPlace) {
        this.greatGrandFatherBirthPlace = greatGrandFatherBirthPlace;
    }

    public String getGrandFatherNICorPIN() {
        return grandFatherNICorPIN;
    }

    public void setGrandFatherNICorPIN(String grandFatherNICorPIN) {
        this.grandFatherNICorPIN = grandFatherNICorPIN;
    }

    public String getGreatGrandFatherNICorPIN() {
        return greatGrandFatherNICorPIN;
    }

    public void setGreatGrandFatherNICorPIN(String greatGrandFatherNICorPIN) {
        this.greatGrandFatherNICorPIN = greatGrandFatherNICorPIN;
    }
}
