package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.util.WebUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Java bean instance to capture parent information as given by page 2 of birth declaration form
 * If the database column sizes are modified the setter methods must be modified 
 */
@Embeddable
public class ParentInfo implements Serializable, Cloneable {
    /**
     * NIC or PIN of father
     */
    @Column(nullable = true, length = 12)
    private String fatherNICorPIN;

    /**
     * Passport number if a foreigner
     */
    @Column(nullable = true, length = 15)
    private String fatherPassportNo;

    /**
     * Country if a foreigner
     */
    @ManyToOne
    @JoinColumn(name = "fatherCountryId")
    private Country fatherCountry;

    /**
     * Fathers country name as a String in the preferred language
     */
    @Transient
    private String fatherCountryPrint;

    /**
     * Name of father
     */
    @Column(nullable = true, length = 600)
    private String fatherFullName;

    /**
     * Name of father in English
     */
    @Column(nullable = true, length = 600)
    private String fatherFullNameInEnglish;

    /**
     * DOB of father
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date fatherDOB;

    /**
     * Place of birth of father
     */
    @Column(nullable = true, length = 60)
    private String fatherPlaceOfBirth;

    /**
     * Race of father
     */
    @ManyToOne
    @JoinColumn(name = "fatherRace")
    private Race fatherRace;

    /**
     * Fathers race name as a String in the preferred language
     */
    @Transient
    private String fatherRacePrint;

    /**
     * NIC or PIN of mother
     */
    @Column(nullable = true, length = 12)
    private String motherNICorPIN;

    /**
     * Passport number if a foreigner
     */
    @Column(nullable = true, length = 15)
    private String motherPassportNo;

    /**
     * Country if a foreigner
     */
    @ManyToOne
    @JoinColumn(name = "motherCountryId")
    private Country motherCountry;

    /**
     * Mothers country name as a String in the preferred language
     */
    @Transient
    private String motherCountryPrint;

    /**
     * Full name of mother
     */
    @Column(nullable = true, length = 600)
    private String motherFullName;

     /**
     * Full name of mother in English
     */
    @Column(nullable = true, length = 600)
    private String motherFullNameInEnglish;

    /**
     * DOB of mother
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date motherDOB;

    /**
     * Place of birth for mother
     */
    @Column(nullable = true, length = 60)
    private String motherPlaceOfBirth;

    /**
     * Race for mother
     */
    @ManyToOne
    @JoinColumn(name = "motherRace")
    private Race motherRace;

    /**
     * Mothers race name as a String in the preferred language
     */
    @Transient
    private String motherRacePrint;

    /**
     * Age of mother at birth
     */
    @Column(nullable = true)
    private Integer motherAgeAtBirth;

    /**
     * Mother's DS division ID
     */
    @OneToOne
    @JoinColumn(name = "motherDSDivisionUKey")
    private DSDivision motherDSDivision;

    /**
     * Mothers DS Division name as a String in the preferred language
     */
    @Transient
    private String motherDsDivisionPrint;

    /**
     * Mothers district name as a String in the preferred language
     */
    @Transient
    private String motherDistrictPrint;

    /**
     * Address of mother
     */
    @Column(nullable = true, length = 255)
    private String motherAddress;

    /**
     * Phone number of mother
     */
    @Column(nullable = true, length = 30)
    private String motherPhoneNo;

    /**
     * Email of mother
     */
    @Column(nullable = true, length = 30)
    private String motherEmail;

    /**
     * Mothers admission number to the hospital
     */
    @Column(nullable = true, length = 15)
    private String motherAdmissionNo;

    /**
     * Date the mother admitted that she was pregnant. -- just kidding, the date she got addmitted to the hospital of course. -- 
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date motherAdmissionDate;

    /**
     * The GN  division where the birth is registered (Includes District)
     */
    //todo remove nullable to false when we have all the data
    @ManyToOne
    @JoinColumn(name = "motherGNDivisionUKey", nullable = true)
    private GNDivision motherGNDivision;


    @Transient
    private String motherGNDivisionPrint;

    public String getFatherNICorPIN() {
        return fatherNICorPIN;
    }

    public void setFatherNICorPIN(String fatherNICorPIN) {
        this.fatherNICorPIN = WebUtils.filterBlanksAndToUpperAndTrim(fatherNICorPIN,12,"fatherNICorPIN");
    }

    public String getFatherPassportNo() {
        return fatherPassportNo;
    }

    public void setFatherPassportNo(String fatherPassportNo) {
        this.fatherPassportNo = WebUtils.filterBlanksAndToUpperAndTrim(fatherPassportNo,15,"fatherPassportNo");
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

    public String getFatherFullNameInEnglish() {
        return fatherFullNameInEnglish;
    }

    public void setFatherFullName(String fatherFullName) {
        this.fatherFullName = WebUtils.filterBlanksAndToUpperAndTrim(fatherFullName,600,"fatherFullName");
    }

     public void setFatherFullNameInEnglish(String fatherFullNameInEnglish) {
        this.fatherFullNameInEnglish = WebUtils.filterBlanksAndToUpperAndTrim(fatherFullNameInEnglish,600,"fatherFullNameInEnglish");
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
        this.fatherPlaceOfBirth = WebUtils.filterBlanksAndToUpperAndTrim(fatherPlaceOfBirth,60,"fatherPlaceOfBirth");
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
        this.motherNICorPIN = WebUtils.filterBlanksAndToUpperAndTrim(motherNICorPIN,12,"motherNICorPIN");
    }

    public String getMotherPassportNo() {
        return motherPassportNo;
    }

    public void setMotherPassportNo(String motherPassportNo) {
        this.motherPassportNo = WebUtils.filterBlanksAndToUpperAndTrim(motherPassportNo,15,"motherPassportNo");
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

    public String getMotherFullNameInEnglish() {
        return motherFullNameInEnglish;
    }

    public void setMotherFullName(String motherFullName) {
        this.motherFullName = WebUtils.filterBlanksAndToUpperAndTrim(motherFullName,600,"motherFullName");
    }

    public void setMotherFullNameInEnglish(String motherFullNameInEnglish) {
        this.motherFullNameInEnglish = WebUtils.filterBlanksAndToUpperAndTrim(motherFullNameInEnglish,600,"motherFullNameInEnglish");
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
        this.motherPlaceOfBirth = WebUtils.filterBlanksAndToUpperAndTrim(motherPlaceOfBirth,60,"motherPlaceOfBirth");
    }

    public Race getMotherRace() {
        return motherRace;
    }

    public void setMotherRace(Race motherRace) {
        this.motherRace = motherRace;
    }

    public Integer getMotherAgeAtBirth() {
        return motherAgeAtBirth;
    }

    public void setMotherAgeAtBirth(Integer motherAgeAtBirth) {
        this.motherAgeAtBirth = motherAgeAtBirth;
    }

    public String getMotherAddress() {
        return motherAddress;
    }

    public void setMotherAddress(String motherAddress) {
        this.motherAddress = WebUtils.filterBlanksAndToUpperAndTrim(motherAddress,255,"motherAddress");
    }

    public String getMotherPhoneNo() {
        return motherPhoneNo;
    }

    public void setMotherPhoneNo(String motherPhoneNo) {
        this.motherPhoneNo = WebUtils.filterBlanksAndToUpperAndTrim(motherPhoneNo,30,"motherPhoneNo");
    }

    public String getMotherEmail() {
        return motherEmail;
    }

    public void setMotherEmail(String motherEmail) {
        this.motherEmail = WebUtils.filterBlanksAndToUpperAndTrim(motherEmail,30,"motherEmail");
    }

    public String getMotherAdmissionNo() {
        return motherAdmissionNo;
    }

    public void setMotherAdmissionNo(String motherAdmissionNo) {
        this.motherAdmissionNo = WebUtils.filterBlanksAndToUpperAndTrim(motherAdmissionNo,15,"motherAdmissionNo");
    }

    public Date getMotherAdmissionDate() {
        return motherAdmissionDate;
    }

    public void setMotherAdmissionDate(Date motherAdmissionDate) {
        this.motherAdmissionDate = motherAdmissionDate;
    }

    public DSDivision getMotherDSDivision() {
        return motherDSDivision;
    }

    public void setMotherDSDivision(DSDivision motherDSDivision) {
        this.motherDSDivision = motherDSDivision;
    }

    public String getFatherCountryPrint() {
        return fatherCountryPrint;
    }

    public void setFatherCountryPrint(String fatherCountryPrint) {
        this.fatherCountryPrint = fatherCountryPrint;
    }

    public String getMotherRacePrint() {
        return motherRacePrint;
    }

    public void setMotherRacePrint(String motherRacePrint) {
        this.motherRacePrint = motherRacePrint;
    }

    public String getFatherRacePrint() {
        return fatherRacePrint;
    }

    public void setFatherRacePrint(String fatherRacePrint) {
        this.fatherRacePrint = fatherRacePrint;
    }

    public String getMotherCountryPrint() {
        return motherCountryPrint;
    }

    public void setMotherCountryPrint(String motherCountryPrint) {
        this.motherCountryPrint = motherCountryPrint;
    }

    public String getMotherDsDivisionPrint() {
        return motherDsDivisionPrint;
    }

    public void setMotherDsDivisionPrint(String motherDsDivisionPrint) {
        this.motherDsDivisionPrint = motherDsDivisionPrint;
    }

    public String getMotherDistrictPrint() {
        return motherDistrictPrint;
    }

    public void setMotherDistrictPrint(String motherDistrictPrint) {
        this.motherDistrictPrint = motherDistrictPrint;
    }

    public GNDivision getMotherGNDivision() {
        return motherGNDivision;
    }

    public void setMotherGNDivision(GNDivision motherGNDivision) {
        this.motherGNDivision = motherGNDivision;
    }

    @Override
    protected ParentInfo clone() throws CloneNotSupportedException {
        return (ParentInfo) super.clone();
    }

    public String getMotherGNDivisionPrint() {
        return motherGNDivisionPrint;
    }

    public void setMotherGNDivisionPrint(String motherGNDivisionPrint) {
        this.motherGNDivisionPrint = motherGNDivisionPrint;
    }
}
