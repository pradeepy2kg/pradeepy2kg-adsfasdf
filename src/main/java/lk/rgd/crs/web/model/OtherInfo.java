package lk.rgd.crs.web.model;

import java.util.Date;

/**
 * Java bean instance to capture Other information such as Marriage, GrandFather/Great Grand Father  and the Informant
 *  as entered by page 3 of the birth declaration form 
 */
public class OtherInfo {
    /** Were parents married at birth - 0 - no, 1 - yes, 2 - no but married later */
    private int parentsMarried;

    /** Place of marriage */
    private String  placeOfMarriage;

    /** Date of marriage */
    private Date dateOfMarriage;

    /** If parents are unmarried - Has the mother signed to include fathers details? */
    private boolean motherSigned;

    /** If parents are unmarried - Has the father signed to include fathers details? */
    private boolean fatherSigned;

    //-----------------------------------------------------
    // If grandfather of the child born in Sri Lanka, grandfather's details
    private String grandFatherFullName;
    private int grandFatherBirthYear;
    private String grandFatherBirthPlace;

    //-----------------------------------------------------
    // If the father was not born in Sri Lanka and if great grandfather born in Sri Lanka great grand father's details
    private String greatGrandFatherFullName;
    private String greatGrandFatherBirthYear;
    private String greatGrandFatherBirthPlace;

    //-----------------------------------------------------
    /** 0 - father, 1 - mother, 2 - guardian */
    private int informantType;
    private String informantName;
    private String informantNICorPIN;
    private String informantAddress;
    private String informantPhoneNo;
    private String informantEmail;
    private Date   informantSignDate;

    public int getParentsMarried() {
        return parentsMarried;
    }

    public void setParentsMarried(int parentsMarried) {
        this.parentsMarried = parentsMarried;
    }

    public String getPlaceOfMarriage() {
        return placeOfMarriage;
    }

    public void setPlaceOfMarriage(String placeOfMarriage) {
        this.placeOfMarriage = placeOfMarriage;
    }

    public Date getDateOfMarriage() {
        return dateOfMarriage;
    }

    public void setDateOfMarriage(Date dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
    }

    public boolean isMotherSigned() {
        return motherSigned;
    }

    public void setMotherSigned(boolean motherSigned) {
        this.motherSigned = motherSigned;
    }

    public boolean isFatherSigned() {
        return fatherSigned;
    }

    public void setFatherSigned(boolean fatherSigned) {
        this.fatherSigned = fatherSigned;
    }

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

    public int getInformantType() {
        return informantType;
    }

    public void setInformantType(int informantType) {
        this.informantType = informantType;
    }

    public String getInformantName() {
        return informantName;
    }

    public void setInformantName(String informantName) {
        this.informantName = informantName;
    }

    public String getInformantNICorPIN() {
        return informantNICorPIN;
    }

    public void setInformantNICorPIN(String informantNICorPIN) {
        this.informantNICorPIN = informantNICorPIN;
    }

    public String getInformantAddress() {
        return informantAddress;
    }

    public void setInformantAddress(String informantAddress) {
        this.informantAddress = informantAddress;
    }

    public String getInformantPhoneNo() {
        return informantPhoneNo;
    }

    public void setInformantPhoneNo(String informantPhoneNo) {
        this.informantPhoneNo = informantPhoneNo;
    }

    public String getInformantEmail() {
        return informantEmail;
    }

    public void setInformantEmail(String informantEmail) {
        this.informantEmail = informantEmail;
    }

    public Date getInformantSignDate() {
        return informantSignDate;
    }

    public void setInformantSignDate(Date informantSignDate) {
        this.informantSignDate = informantSignDate;
    }
}
