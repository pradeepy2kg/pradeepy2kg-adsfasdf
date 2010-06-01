package lk.rgd.crs.web.model;

import java.util.Date;

/**
 * Java bean instance to capture parent information as given by page 2 of birth declaration form
 */
public class ParentInfo {
    /** NIC or PIN of father */
    private String fatherNICorPIN;

    /** Passport number if a foreigner */
    private String fatherPassportNo;

    /** Country if a foreigner */
    private String fatherCountry;

    /** Name of father */
    private String fatherFullName;

    /** DOB of father */
    private Date fatherDOB;

    /** Place of birth of father */
    private String fatherPlaceOfBirth;

    /** Race of father */
    private int    fatherRace;

    /** NIC or PIN of mother */
    private String motherNICorPIN;

    /** Passport number if a foreigner */
    private String motherPassportNo;

    /** Country if a foreigner */
    private String motherCountry;

    /** Hospital admission and date for mother */
    private String motherAdmissionNoAndDate;

    /** Full name of mother */
    private String motherFullName;

    /** DOB of mother */
    private Date   motherDOB;

    /** Place of birth for mother */
    private String motherPlaceOfBirth;

    /** Race for mother */
    private int    motherRace;

    /** Age of mother at birth */
    private int    motherAgeAtBirth;

    /** Address of mother */
    private String motherAddress;

    /** Phone number of mother */
    private String motherPhoneNo;

    /** Email of mother */
    private String motherEmail;

    public String getFatherNICorPIN() {
        return fatherNICorPIN;
    }

    public void setFatherNICorPIN(String fatherNICorPIN) {
        this.fatherNICorPIN = fatherNICorPIN;
    }

    public String getFatherPassportNo() {
        return fatherPassportNo;
    }

    public void setFatherPassportNo(String fatherPassportNo) {
        this.fatherPassportNo = fatherPassportNo;
    }

    public String getFatherCountry() {
        return fatherCountry;
    }

    public void setFatherCountry(String fatherCountry) {
        this.fatherCountry = fatherCountry;
    }

    public String getFatherFullName() {
        return fatherFullName;
    }

    public void setFatherFullName(String fatherFullName) {
        this.fatherFullName = fatherFullName;
    }

    public Date getFatherDOB() {
        return fatherDOB;
    }

    public void setFatherDOB(Date fatherDOB) {
        this.fatherDOB = fatherDOB;
    }

    public String getFatherPlaceOfBirth() {
        return fatherPlaceOfBirth;
    }

    public void setFatherPlaceOfBirth(String fatherPlaceOfBirth) {
        this.fatherPlaceOfBirth = fatherPlaceOfBirth;
    }

    public int getFatherRace() {
        return fatherRace;
    }

    public void setFatherRace(int fatherRace) {
        this.fatherRace = fatherRace;
    }

    public String getMotherNICorPIN() {
        return motherNICorPIN;
    }

    public void setMotherNICorPIN(String motherNICorPIN) {
        this.motherNICorPIN = motherNICorPIN;
    }

    public String getMotherPassportNo() {
        return motherPassportNo;
    }

    public void setMotherPassportNo(String motherPassportNo) {
        this.motherPassportNo = motherPassportNo;
    }

    public String getMotherCountry() {
        return motherCountry;
    }

    public void setMotherCountry(String motherCountry) {
        this.motherCountry = motherCountry;
    }

    public String getMotherAdmissionNoAndDate() {
        return motherAdmissionNoAndDate;
    }

    public void setMotherAdmissionNoAndDate(String motherAdmissionNoAndDate) {
        this.motherAdmissionNoAndDate = motherAdmissionNoAndDate;
    }

    public String getMotherFullName() {
        return motherFullName;
    }

    public void setMotherFullName(String motherFullName) {
        this.motherFullName = motherFullName;
    }

    public Date getMotherDOB() {
        return motherDOB;
    }

    public void setMotherDOB(Date motherDOB) {
        this.motherDOB = motherDOB;
    }

    public String getMotherPlaceOfBirth() {
        return motherPlaceOfBirth;
    }

    public void setMotherPlaceOfBirth(String motherPlaceOfBirth) {
        this.motherPlaceOfBirth = motherPlaceOfBirth;
    }

    public int getMotherRace() {
        return motherRace;
    }

    public void setMotherRace(int motherRace) {
        this.motherRace = motherRace;
    }

    public int getMotherAgeAtBirth() {
        return motherAgeAtBirth;
    }

    public void setMotherAgeAtBirth(int motherAgeAtBirth) {
        this.motherAgeAtBirth = motherAgeAtBirth;
    }

    public String getMotherAddress() {
        return motherAddress;
    }

    public void setMotherAddress(String motherAddress) {
        this.motherAddress = motherAddress;
    }

    public String getMotherPhoneNo() {
        return motherPhoneNo;
    }

    public void setMotherPhoneNo(String motherPhoneNo) {
        this.motherPhoneNo = motherPhoneNo;
    }

    public String getMotherEmail() {
        return motherEmail;
    }

    public void setMotherEmail(String motherEmail) {
        this.motherEmail = motherEmail;
    }
}
