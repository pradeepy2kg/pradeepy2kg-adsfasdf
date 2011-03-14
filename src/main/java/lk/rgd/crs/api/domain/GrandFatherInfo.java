package lk.rgd.crs.api.domain;

import lk.rgd.common.util.WebUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Java bean instance to capture Other information such as Marriage, GrandFather/Great Grand Father  and the Informant
 * as entered by page 3 of the birth declaration form
 * If the database column sizes are modified the setter methods must be modified 
 */
@Embeddable
public class GrandFatherInfo implements Serializable, Cloneable {
    // If grandfather of the child born in Sri Lanka, grandfather's details

    @Column(nullable = true, length = 600)
    private String grandFatherFullName;

    @Column(nullable = true)
    private Integer grandFatherBirthYear;

    @Column(nullable = true, length = 60)
    private String grandFatherBirthPlace;

    @Column(nullable = true, length = 12)
    private String grandFatherNICorPIN;

    // If the father was not born in Sri Lanka and if great grandfather born in Sri Lanka great grand father's details
    @Column(nullable = true, length = 600)
    private String greatGrandFatherFullName;

    @Column(nullable = true)
    private Integer greatGrandFatherBirthYear;

    @Column(nullable = true, length = 60)
    private String greatGrandFatherBirthPlace;

    @Column(nullable = true, length = 12)
    private String greatGrandFatherNICorPIN;

    public String getGrandFatherFullName() {
        return grandFatherFullName;
    }

    public void setGrandFatherFullName(String grandFatherFullName) {
        this.grandFatherFullName = WebUtils.filterBlanksAndToUpperAndTrim(grandFatherFullName,600,"grandFatherFullName");
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
        this.grandFatherBirthPlace = WebUtils.filterBlanksAndToUpper(grandFatherBirthPlace);
    }

    public String getGreatGrandFatherFullName() {
        return greatGrandFatherFullName;
    }

    public void setGreatGrandFatherFullName(String greatGrandFatherFullName) {
        this.greatGrandFatherFullName = WebUtils.filterBlanksAndToUpperAndTrim(greatGrandFatherFullName,600,"greatGrandFatherFullName");
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
        this.greatGrandFatherBirthPlace = WebUtils.filterBlanksAndToUpper(greatGrandFatherBirthPlace);
    }

    public String getGrandFatherNICorPIN() {
        return grandFatherNICorPIN;
    }

    public void setGrandFatherNICorPIN(String grandFatherNICorPIN) {
        this.grandFatherNICorPIN = WebUtils.filterBlanksAndToUpperAndTrim(grandFatherNICorPIN,12,"grandFatherNICorPIN");
    }

    public String getGreatGrandFatherNICorPIN() {
        return greatGrandFatherNICorPIN;
    }

    public void setGreatGrandFatherNICorPIN(String greatGrandFatherNICorPIN) {
        this.greatGrandFatherNICorPIN = WebUtils.filterBlanksAndToUpperAndTrim(greatGrandFatherNICorPIN,12,"greatGrandFatherNICorPI");
    }

    @Override
    protected GrandFatherInfo clone() throws CloneNotSupportedException {
        return (GrandFatherInfo) super.clone();
    }
}
