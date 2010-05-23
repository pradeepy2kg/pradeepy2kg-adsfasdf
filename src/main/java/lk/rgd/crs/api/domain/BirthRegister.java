package lk.rgd.crs.api.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * An instance represents information submitted for the declaration of a birth, and the confirmation of changes
 */
@Entity
@Table(name = "birth_register")
public class BirthRegister {

    /**
     * This is an auto generated unique row identifier
     */
    @Id
    private long id;

    /** The district code where the birth is registered */
    private int birthDistrict;
    /** The Birth/Death registration division where the birth is registered */
    private int birthDivision;
    /** This is the serial number captured from the BDF */
    private String bdfSerialNo;

    /** The date of the birth */
    private Date dateOfBirth;
    /** The date when the birth declaration was submitted to the medical registrar or the DS office */
    private Date dateOfSubmission;
    /**
     * 0 - BDF added, 1 - ADR approved, 2 - Confirmation printed
     * 3 - confirmed, 4 - changed (i.e. during the confirmation by parents), 5 - rejected
     */
    private int status;
    /** Status comment - e.g. reason for rejection due to duplicate */
    private String comments;

    /** The place of birth - usually the village or hospital name */
    private String placeOfBirth;
    /** Name in Sinhala or Tamil */
    private String childFullNameOfficialLang;
    /** Name in English */
    private String childFullNameEnglish;
    /** Gender 0 - male, 1 - female, 2 - unknown */
    private int childGender;
    /** Wight in kilogrammes */
    private float childBirthWeight;
    /** Child rank according to the order of live births */
    private int childRank;
    /** Number of children born along with the child being registered */
    private int numberOfChildrenBorn;
    /** Hospial or GN code */
    private String hospitalOrGNCode;

    //----------------------------------------------------
    /** NIC or PIN of father */
    private String fatherNICorPIN;
    /** Passport number if a foreigner */
    private String fatherPassportNo;
    /** Country if a foreigner */
    private String fatherCountry;
    /** Name of father */
    private String fatherFullName;
    /** DOB of father */
    private Date   fatherDOB;
    /** Place of birth of father */
    private String fatherPlaceOfBirth;
    /** Race of father */
    private int    fatherRace;
    //----------------------------------------------------
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

    //-----------------------------------------------------
    /** Were parents married at birth - 0 - no, 1 - yes, 2 - no but married later */
    private int parentsMarried;
    /** Place of marriage */
    private String  placeOfMarriage;
    /** Date of marriage */
    private Date    dateOfMarriage;
    /** If parents are unmarried - Has the mother signed to include fathers details? */
    private boolean motherSigned;
    /** If parents are unmarried - Has the father signed to include fathers details? */
    private boolean fatherSigned;
    //-----------------------------------------------------
    // If grandfather of the child born in Sri Lanka, grandfather's details
    private String grandFatherFullName;
    private String grandFatherBirthYear;
    private String grandFatherBirthPlace;
    //-----------------------------------------------------
    // If the father was not born in Sri Lanka and if great grandfather born in Sri Lanka great grand father's details
    private String greatGrandFatherFullName;
    private String greatGrandFatherBirthYear;
    private String greatGrandFatherBirthPlace;
    //-----------------------------------------------------
    /** 0 - father, 1 - mother, 2 - guardian */
    private String informantType;
    private String informantName;
    private String informantNICorPIN;
    private String informantAddress;
    private String informantPhoneNo;
    private String informantEmail;
    private Date   informantSignDate;
    //-----------------------------------------------------
    /** The notifying authority PIN */
    private String notifyingAuthorityPIN;
    /** The notifying authority Name */
    private String notifyingAuthorityName;
    //-----------------------------------------------------
    /** The PIN of the ADR approving the BDF */
    private String approvePIN;
    /** The date when an ADR or higher approves the BDF */
    private Date   approveDate;

    //-----------------------------------------------------
    /** This represents a system generated serial number for the confirmation by parents */
    private String confirmationSerialNumber;

    /** PIN or NIC of person confirming BDF details */
    private String confirmantNICorPIN;
    /** Name of person confirming BDF details */
    private String confirmantFullName;
    /** Date of the confirmation */
    private Date   confirmantSignDate;
    /** Date confirmation is received */
    private Date   confirmationReceiveDate;
    //-----------------------------------------------------


    public int getBirthDistrict() {
        return birthDistrict;
    }

    public void setBirthDistrict(int birthDistrict) {
        this.birthDistrict = birthDistrict;
    }

    public int getBirthDivision() {
        return birthDivision;
    }

    public void setBirthDivision(int birthDivision) {
        this.birthDivision = birthDivision;
    }

    public String getBdfSerialNo() {
        return bdfSerialNo;
    }

    public void setBdfSerialNo(String bdfSerialNo) {
        this.bdfSerialNo = bdfSerialNo;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getDateOfSubmission() {
        return dateOfSubmission;
    }

    public void setDateOfSubmission(Date dateOfSubmission) {
        this.dateOfSubmission = dateOfSubmission;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getChildFullNameOfficialLang() {
        return childFullNameOfficialLang;
    }

    public void setChildFullNameOfficialLang(String childFullNameOfficialLang) {
        this.childFullNameOfficialLang = childFullNameOfficialLang;
    }

    public String getChildFullNameEnglish() {
        return childFullNameEnglish;
    }

    public void setChildFullNameEnglish(String childFullNameEnglish) {
        this.childFullNameEnglish = childFullNameEnglish;
    }

    public int getChildGender() {
        return childGender;
    }

    public void setChildGender(int childGender) {
        this.childGender = childGender;
    }

    public float getChildBirthWeight() {
        return childBirthWeight;
    }

    public void setChildBirthWeight(float childBirthWeight) {
        this.childBirthWeight = childBirthWeight;
    }

    public int getChildRank() {
        return childRank;
    }

    public void setChildRank(int childRank) {
        this.childRank = childRank;
    }

    public int getNumberOfChildrenBorn() {
        return numberOfChildrenBorn;
    }

    public void setNumberOfChildrenBorn(int numberOfChildrenBorn) {
        this.numberOfChildrenBorn = numberOfChildrenBorn;
    }

    public String getHospitalOrGNCode() {
        return hospitalOrGNCode;
    }

    public void setHospitalOrGNCode(String hospitalOrGNCode) {
        this.hospitalOrGNCode = hospitalOrGNCode;
    }

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

    public String getGrandFatherBirthYear() {
        return grandFatherBirthYear;
    }

    public void setGrandFatherBirthYear(String grandFatherBirthYear) {
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

    public String getInformantType() {
        return informantType;
    }

    public void setInformantType(String informantType) {
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

    public String getNotifyingAuthorityPIN() {
        return notifyingAuthorityPIN;
    }

    public void setNotifyingAuthorityPIN(String notifyingAuthorityPIN) {
        this.notifyingAuthorityPIN = notifyingAuthorityPIN;
    }

    public String getNotifyingAuthorityName() {
        return notifyingAuthorityName;
    }

    public void setNotifyingAuthorityName(String notifyingAuthorityName) {
        this.notifyingAuthorityName = notifyingAuthorityName;
    }

    public String getApprovePIN() {
        return approvePIN;
    }

    public void setApprovePIN(String approvePIN) {
        this.approvePIN = approvePIN;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getConfirmationSerialNumber() {
        return confirmationSerialNumber;
    }

    public void setConfirmationSerialNumber(String confirmationSerialNumber) {
        this.confirmationSerialNumber = confirmationSerialNumber;
    }

    public String getConfirmantNICorPIN() {
        return confirmantNICorPIN;
    }

    public void setConfirmantNICorPIN(String confirmantNICorPIN) {
        this.confirmantNICorPIN = confirmantNICorPIN;
    }

    public String getConfirmantFullName() {
        return confirmantFullName;
    }

    public void setConfirmantFullName(String confirmantFullName) {
        this.confirmantFullName = confirmantFullName;
    }

    public Date getConfirmantSignDate() {
        return confirmantSignDate;
    }

    public void setConfirmantSignDate(Date confirmantSignDate) {
        this.confirmantSignDate = confirmantSignDate;
    }

    public Date getConfirmationReceiveDate() {
        return confirmationReceiveDate;
    }

    public void setConfirmationReceiveDate(Date confirmationReceiveDate) {
        this.confirmationReceiveDate = confirmationReceiveDate;
    }
}