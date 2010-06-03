package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.Race;

import javax.persistence.*;
import java.util.Date;

/**
 * Java bean instance to capture parent information as given by page 2 of birth declaration form
 */
@Embeddable
public class ParentInfo {
    /** NIC or PIN of father  */
    @Column(nullable = true, length = 10)
    private String fatherNICorPIN;

    /** Passport number if a foreigner */
    @Column(nullable = true, length = 10)
    private String fatherPassportNo;

    /** Country if a foreigner */
    @ManyToOne
    @JoinColumn(name = "fatherCountryId")
    private Country fatherCountry;

    /** Name of father  */
    @Column(nullable = true, length = 600)
    private String fatherFullName;

    /** DOB of father  */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date fatherDOB;

    /** Place of birth of father */
    @Column(nullable = true, length = 60)
    private String fatherPlaceOfBirth;

    /** Race of father */
    @Column(nullable = true)
    @JoinColumn (name = "fatherRace")
    private Race fatherRace;

    /** NIC or PIN of mother */
    @Column(nullable = true, length = 10)
    private String motherNICorPIN;

    /** Passport number if a foreigner */
    @Column(nullable = true, length = 10)
    private String motherPassportNo;

    /** Country if a foreigner */
    @ManyToOne
    @JoinColumn(name = "motherCountryId")
    private Country motherCountry;

    /** Full name of mother */
    @Column(nullable = true, length = 600)
    private String motherFullName;

    /** DOB of mother  */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date motherDOB;

    /** Place of birth for mother */
    @Column(nullable = true, length = 60)
    private String motherPlaceOfBirth;

    /** Race for mother */
    @Column(nullable = true)
    @JoinColumn (name = "motherRace")
    private Race motherRace;

    /** Age of mother at birth */
    @Column(nullable = true)
    private int motherAgeAtBirth;

    /** Address of mother */
    @Column(nullable = true, length = 255)
    private String motherAddress;

    /** Phone number of mother */
    @Column(nullable = true, length = 30)
    private String motherPhoneNo;

    /** Email of mother */
    @Column(nullable = true, length = 30)
    private String motherEmail;

    /** Mothers admission number to the hospital */
    @Column(nullable = true, length = 15)
    private String motherAdmissionNo;

    /** Date the mother admitted that she was pregnant. */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date motherAdmissionDate;

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

    public Country getFatherCountry() {
        return fatherCountry;
    }

    public void setFatherCountry(Country fatherCountry) {
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

    public Race getFatherRace() {
        return fatherRace;
    }

    public void setFatherRace(Race fatherRace) {
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

    public Country getMotherCountry() {
        return motherCountry;
    }

    public void setMotherCountry(Country motherCountry) {
        this.motherCountry = motherCountry;
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

    public Race getMotherRace() {
        return motherRace;
    }

    public void setMotherRace(Race motherRace) {
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

    public String getMotherAdmissionNo() {
        return motherAdmissionNo;
    }

    public void setMotherAdmissionNo(String motherAdmissionNo) {
        this.motherAdmissionNo = motherAdmissionNo;
    }

    public Date getMotherAdmissionDate() {
        return motherAdmissionDate;
    }

    public void setMotherAdmissionDate(Date motherAdmissionDate) {
        this.motherAdmissionDate = motherAdmissionDate;
    }
}
