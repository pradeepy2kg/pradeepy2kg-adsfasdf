package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.District;

import javax.persistence.*;
import java.util.Date;

/**
 * An instance represents information submitted for the declaration of a birth, and the confirmation of changes
 */
@Entity
@Table(name = "BIRTH_REGISTER", schema = "CRS",
uniqueConstraints = {
    @UniqueConstraint(columnNames = {"birthDistrict", "birthDivision", "bdfSerialNo", "status"})})

@NamedQueries({
    @NamedQuery(name = "confirmation.print.pending", query =
            "SELECT bdf FROM BirthDeclaration bdf " +
            "WHERE bdf.birthDivision = :birthDivision AND bdf.status = 1 " +
            "ORDER BY bdf.dateOfRegistration desc"),
    @NamedQuery(name = "confirmation.print.completed", query =
            "SELECT bdf FROM BirthDeclaration bdf " +
            "WHERE bdf.birthDivision = :birthDivision AND bdf.status = 2 " +
            "ORDER BY bdf.dateOfRegistration desc")
})
public class BirthDeclaration {

    /**
     * This is an auto generated unique row identifier
     */
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    /** The Birth/Death registration division where the birth is registered (Includes District) */
    @ManyToOne
    @JoinColumns({
        @JoinColumn (name = "birthDistrict"),
        @JoinColumn (name = "birthDivision")
    })
    private int birthDivision;
    /** This is the serial number captured from the BDF */
    @Column (nullable = false)
    private String bdfSerialNo;
    /** The date of the birth */
    @Column (nullable = false)
    private Date dateOfBirth;
    /** The date when the birth declaration was submitted to the medical registrar or the DS office */
    @Column (nullable = false)
    private Date dateOfRegistration;
    /**
     * 0 - BDF added, 1 - ADR approved, 2 - Confirmation printed
     * 3 - confirmed without changes, 14 - Record archived and corrected (i.e. during the confirmation by parents),
     * 5 - confirmation changes captured, 6 - confirmation changes approved
     * 10 - rejected and archived
     */
    @Column (nullable = false)
    private int status;
    /** Status comment - e.g. reason for rejection due to duplicate */
    @Column (nullable = true)
    private String comments;
    /** The place of birth - usually the village or hospital name */
    @Column (nullable = true)
    private String placeOfBirth;
    /** Name in Sinhala or Tamil */
    @Column (nullable = true)
    private String childFullNameOfficialLang;
    /** Name in English */
    @Column (nullable = true)
    private String childFullNameEnglish;
    /** Gender 0 - male, 1 - female, 2 - unknown */
    @Column (nullable = false)
    private int childGender;
    /** Wight in kilogrammes */
    @Column (nullable = true)
    private float childBirthWeight;
    /** Child rank according to the order of live births */
    @Column (nullable = true)
    private int childRank;
    /** Number of children born along with the child being registered */
    @Column (nullable = true)
    private int numberOfChildrenBorn;
    /** Hospial code */
    @Column (nullable = true)
    private String hospitalCode;
    /** Grama Niladhari code */
    @Column (nullable = true)
    private String gnCode;
    // TODO make this an int

    //----------------------------------------------------
    /** NIC or PIN of father */
    @Column (nullable = true)
    private String fatherNICorPIN;
    /** Passport number if a foreigner */
    @Column (nullable = true)
    private String fatherPassportNo;
    /** Country if a foreigner */
    @ManyToOne
    @JoinColumn (name = "fatherCountryId")
    private Country fatherCountry;
    /** Name of father */
    @Column (nullable = true)
    private String fatherFullName;
    /** DOB of father */
    @Column (nullable = true)
    private Date   fatherDOB;
    /** Place of birth of father */
    @Column (nullable = true)
    private String fatherPlaceOfBirth;
    /** Race of father */
    @Column (nullable = true)
    private int    fatherRace;

    //----------------------------------------------------
    /** NIC or PIN of mother */
    @Column (nullable = true)
    private String motherNICorPIN;
    /** Passport number if a foreigner */
    @Column (nullable = true)
    private String motherPassportNo;
    /** Country if a foreigner */
    @ManyToOne
    @JoinColumn (name = "motherCountryId")
    private Country motherCountry;
    /** Hospital admission and date for mother */
    @Column (nullable = true)
    private String motherAdmissionNoAndDate;
    /** Full name of mother */
    @Column (nullable = true)
    private String motherFullName;
    /** DOB of mother */
    @Column (nullable = true)
    private Date   motherDOB;
    /** Place of birth for mother */
    @Column (nullable = true)
    private String motherPlaceOfBirth;
    /** Race for mother */
    @Column (nullable = true)
    private int    motherRace;
    /** Age of mother at birth */
    @Column (nullable = true)
    private int    motherAgeAtBirth;
    /** Address of mother */
    @Column (nullable = true)
    private String motherAddress;
    /** Phone number of mother */
    @Column (nullable = true)
    private String motherPhoneNo;
    /** Email of mother */
    @Column (nullable = true)
    private String motherEmail;

    //-----------------------------------------------------
    /** Were parents married at birth - 0 - no, 1 - yes, 2 - no but married later */
    @Column (nullable = true)
    private int parentsMarried;
    /** Place of marriage */
    @Column (nullable = true)
    private String  placeOfMarriage;
    /** Date of marriage */
    @Column (nullable = true)
    private Date    dateOfMarriage;
    /** If parents are unmarried - Has the mother signed to include fathers details? */
    @Column (nullable = true)
    private boolean motherSigned;
    /** If parents are unmarried - Has the father signed to include fathers details? */
    @Column (nullable = true)
    private boolean fatherSigned;

    //-----------------------------------------------------
    // If grandfather of the child born in Sri Lanka, grandfather's details
    @Column (nullable = true)
    private String grandFatherFullName;
    @Column (nullable = true)
    private int grandFatherBirthYear;
    @Column (nullable = true)
    private String grandFatherBirthPlace;

    //-----------------------------------------------------
    // If the father was not born in Sri Lanka and if great grandfather born in Sri Lanka great grand father's details
    @Column (nullable = true)
    private String greatGrandFatherFullName;
    @Column (nullable = true)
    private String greatGrandFatherBirthYear;
    @Column (nullable = true)
    private String greatGrandFatherBirthPlace;

    //-----------------------------------------------------
    /** 0 - father, 1 - mother, 2 - guardian */
    @Column (nullable = false)
    private int informantType;
    @Column (nullable = false)
    private String informantName;
    @Column (nullable = true)
    private String informantNICorPIN;
    @Column (nullable = true)
    private String informantAddress;
    @Column (nullable = true)
    private String informantPhoneNo;
    @Column (nullable = true)
    private String informantEmail;
    @Column (nullable = true)
    private Date   informantSignDate;

    //-----------------------------------------------------
    /** The notifying authority PIN */
    @Column (nullable = false)
    private String notifyingAuthorityPIN;
    /** The notifying authority Name */
    @Column (nullable = false)
    private String notifyingAuthorityName;
    //-----------------------------------------------------
    /** The PIN of the ADR approving the BDF */
    @Column (nullable = true)
    private String approvePIN;
    /** The date when an ADR or higher approves the BDF */
    @Column (nullable = true)
    private Date   approveDate;

    //-----------------------------------------------------
    /** This represents a system generated serial number for the confirmation by parents */
    @Column (nullable = true)
    private String confirmationSerialNumber;
    /** Has the confirmation for parents been printed ? */
    @Column (nullable = true)
    private boolean    confirmationPrinted;
    /** The last date for confirmation - set as 14 days from confirmation print date */
    @Column (nullable = true)
    private Date       lastDateForConfirmation;

    /** PIN or NIC of person confirming BDF details */
    @Column (nullable = true)
    private String confirmantNICorPIN;
    /** Name of person confirming BDF details */
    @Column (nullable = true)
    private String confirmantFullName;
    /** Date of the confirmation */
    @Column (nullable = true)
    private Date   confirmantSignDate;
    /** Date confirmation is received */
    @Column (nullable = true)
    private Date   confirmationReceiveDate;
    //-----------------------------------------------------

    /** The date of issue for the original birth certificate - free copy */
    @Column (nullable = true)
    private Date   originalBCDateOfIssue;
    /** The place of issue for the original birth certificate - free copy (Stores the DS Division ID) */
    @Column (nullable = true)
    private int   originalBCPlaceOfIssue;

    public District getBirthDistrict() {
        return birthDivision.getDistrict();
    }

    public BDDivision getBirthDivision() {
        return birthDivision;
    }

    public void setBirthDivision(BDDivision birthDivision) {
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

    public Date getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(Date dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
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

    public Country getMotherCountry() {
        return motherCountry;
    }

    public void setMotherCountry(Country motherCountry) {
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

    public boolean isConfirmationPrinted() {
        return confirmationPrinted;
    }

    public void setConfirmationPrinted(boolean confirmationPrinted) {
        this.confirmationPrinted = confirmationPrinted;
    }

    public Date getLastDateForConfirmation() {
        return lastDateForConfirmation;
    }

    public void setLastDateForConfirmation(Date lastDateForConfirmation) {
        this.lastDateForConfirmation = lastDateForConfirmation;
    }

    public Date getOriginalBCDateOfIssue() {
        return originalBCDateOfIssue;
    }

    public void setOriginalBCDateOfIssue(Date originalBCDateOfIssue) {
        this.originalBCDateOfIssue = originalBCDateOfIssue;
    }

    public int getOriginalBCPlaceOfIssue() {
        return originalBCPlaceOfIssue;
    }

    public void setOriginalBCPlaceOfIssue(int originalBCPlaceOfIssue) {
        this.originalBCPlaceOfIssue = originalBCPlaceOfIssue;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getGnCode() {
        return gnCode;
    }

    public void setGnCode(String gnCode) {
        this.gnCode = gnCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
