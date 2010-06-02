package lk.rgd.crs.web.model;

import java.util.Date;

/**
 * Java bean instance to capture Other information such as Marriage, GrandFather/Great Grand Father  and the Informant
 * as entered by page 3 of the birth declaration form
 */
public class GrandFatherInfo {
    // If grandfather of the child born in Sri Lanka, grandfather's details
    private String grandFatherFullName;
    private int grandFatherBirthYear;
    private String grandFatherBirthPlace;

    // If the father was not born in Sri Lanka and if great grandfather born in Sri Lanka great grand father's details
    private String greatGrandFatherFullName;
    private String greatGrandFatherBirthYear;
    private String greatGrandFatherBirthPlace;

    public String getGrandFatherFullName() {
        return grandFatherFullName;
    }

    public void setGrandFatherFullName(String grandFatherFullName) {
        this.grandFatherFullName = grandFatherFullName;
    }

    public int getGrandFatherBirthYear() {
        return grandFatherBirthYear;
    }

    public void setGrandFatherBirthYear(int grandFatherBirthYear) {
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

    public String getGreatGrandFatherBirthYear() {
        return greatGrandFatherBirthYear;
    }

    public void setGreatGrandFatherBirthYear(String greatGrandFatherBirthYear) {
        this.greatGrandFatherBirthYear = greatGrandFatherBirthYear;
    }

    public String getGreatGrandFatherBirthPlace() {
        return greatGrandFatherBirthPlace;
    }

    public void setGreatGrandFatherBirthPlace(String greatGrandFatherBirthPlace) {
        this.greatGrandFatherBirthPlace = greatGrandFatherBirthPlace;
    }
}
