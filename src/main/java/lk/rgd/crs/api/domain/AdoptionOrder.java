package lk.rgd.crs.api.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * An instance representing a court order granting adoption of a child.
 *
 * @author Ashoka Ekanayaka
 */
@Entity
@Table(name = "ADOPTION_ORDER", schema = "CRS")

@NamedQueries({
//
//    @NamedQuery(name = "get.by.division.status.register.date", query = "SELECT bdf FROM BirthDeclaration bdf " +
//        "WHERE bdf.register.birthDivision = :birthDivision AND bdf.register.status = :status " +
//        "AND (bdf.register.dateOfRegistration BETWEEN :startDate AND :endDate) " +
//        "ORDER BY bdf.register.dateOfRegistration desc"),
//
//    @NamedQuery(name = "get.by.division.status.confirmation.receive.date", query = "SELECT bdf FROM BirthDeclaration bdf " +
//        "WHERE bdf.register.birthDivision = :birthDivision AND bdf.register.status = :status " +
//        "AND (bdf.confirmant.confirmationReceiveDate BETWEEN :startDate AND :endDate) " +
//        "ORDER BY bdf.confirmant.confirmationReceiveDate desc"),
//
//    @NamedQuery(name = "get.by.bddivision", query = "SELECT bdf FROM BirthDeclaration bdf " +
//        "WHERE bdf.register.birthDivision = :birthDivision " +
//        "ORDER BY bdf.register.dateOfRegistration desc"),
//
//    @NamedQuery(name = "get.historical.records.by.bddivision.and.serialNo", query = "SELECT bdf FROM BirthDeclaration bdf " +
//        "WHERE (bdf.register.birthDivision = :birthDivision AND bdf.register.bdfSerialNo = :bdfSerialNo) " +
//        "AND bdf.activeRecord IS FALSE " +
//        "ORDER BY bdf.lastUpdatedTime desc"),
//
//
//    @NamedQuery(name = "get.by.dateOfBirth_range.and.motherNICorPIN", query = "SELECT bdf FROM BirthDeclaration bdf " +
//        "WHERE bdf.child.dateOfBirth BETWEEN :start AND :end AND bdf.parent.motherNICorPIN = :motherNICorPIN "),
//
//    @NamedQuery(name = "filter.by.unconfirmed.by.register.date", query = "SELECT bdf FROM BirthDeclaration bdf " +
//        "WHERE bdf.register.status = 2 " +
//        "AND bdf.register.dateOfRegistration < :date"),
    @NamedQuery(name = "adoption.filter.by.status.paginated", query = "SELECT adoption FROM AdoptionOrder adoption " +
        "WHERE adoption.status = :status " + "ORDER BY adoption.orderIssuedDate desc"),

    @NamedQuery(name = "get.by.courtOrderNumber", query = "SELECT adoption FROM AdoptionOrder adoption " +
        "WHERE adoption.courtOrderNumber = :courtOrderNumber"),

    @NamedQuery(name = "getAllAdoptions", query = "SELECT adoption FROM AdoptionOrder adoption")
})

public class AdoptionOrder implements Serializable {
    public enum State {
        DATA_ENTRY, // 0 - A newly entered Adoption - can be edited by DEO, ADR

        APPROVED, // 1 - An ARG or higher approved Adoption

        NOTICE_LETTER_PRINTED,  // 2 - An  Adoption which is printed for parent confirmation

        REJECTED,  // 3 - An Adoption rejected by the ARG

        CERTIFICATE_ISSUE_REQUEST_CAPTURED, //4 Acertifcate is requested

        ADOPTION_CERTIFICATE_PRINTED, //5 Acertifcate is requested
    }

    public enum ApplicantType {
        FATHER,    // 0

        MOTHER,   // 1

        OTHER,     // 2
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUKey;

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date orderIssuedDate;

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date orderReceivedDate;

    @Column(nullable = false)
    private String courtOrderNumber;

    @Column(nullable = false)
    private String court;

    @Column(nullable = false)
    private String judgeName;

    @Column(nullable = false)
    private String applicantName;

    @Column(nullable = false)
    private String applicantAddress;

    @Column(nullable = false)
    private String applicantPINorNIC;

    @Column(nullable = true)
    private int applicantCountryId;

    @Column(nullable = true)
    private String applicantPassport;

    @Column(nullable = false)
    private boolean applicantMother; // false if father is the applicant (Usual case), true if it is the mother

    @Column(nullable = true)
    private String wifeName; // applies only if applicantMother is false (father)

    @Column(nullable = true)
    private String wifePINorNIC;

    @Column(nullable = true)
    private int wifeCountryId;

    @Column(nullable = true)
    private String wifePassport;

    @Column(nullable = true)
    private String childExistingName; // this can not be null if given name is also null

    @Column(nullable = true)
    private String childNewName; // Given name from the courts if no existing name

    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date childBirthDate;

    @Column(nullable = false)
    private int childAgeYears;

    @Column(nullable = true)
    private int childAgeMonths;

    @Column(nullable = false)
    private int childGender;   //  Gender 0 - male, 1 - female, 2 - unknown

    @Column(nullable = true)
    private long birthCertificateNumber; // idukey, not the serial of old birth registration before adoption

    @Column(nullable = true)
    private long birthCertificateSerial; // if BC number not given

    @Column(nullable = true)
    private int birthDivisionId; // if BC number not given

    @Column(nullable = true)
    private long newBirthCertificateNumber; // idUKey of new birth registration after adoption 

    @Column(nullable = true)
    private String certificateApplicantName;

    @Column(nullable = true)
    private String certificateApplicantAddress;

    @Column(nullable = true)
    private ApplicantType certificateApplicantType;

    @Column(nullable = true)
    private String certificateApplicantPINorNIC;

    @Column(length = 2, nullable = false)
    private String languageToTransliterate;

    @Column(nullable = false)
    private State status;

    private String filterBlanks(String s) {
        return (s == null) ? null : (s.trim().length() == 0) ? null : s.trim().toUpperCase();
    }

    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public Date getOrderIssuedDate() {
        return orderIssuedDate;
    }

    public void setOrderIssuedDate(Date orderIssuedDate) {
        this.orderIssuedDate = orderIssuedDate;
    }

    public Date getOrderReceivedDate() {
        return orderReceivedDate;
    }

    public void setOrderReceivedDate(Date orderReceivedDate) {
        this.orderReceivedDate = orderReceivedDate;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = filterBlanks(court);
    }

    public String getJudgeName() {
        return judgeName;
    }

    public void setJudgeName(String judgeName) {
        this.judgeName = filterBlanks(judgeName);
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = filterBlanks(applicantName);
    }

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(String applicantAddress) {
        this.applicantAddress = filterBlanks(applicantAddress);
    }

    public String getApplicantPINorNIC() {
        return applicantPINorNIC;
    }

    public void setApplicantPINorNIC(String applicantPINorNIC) {
        this.applicantPINorNIC = applicantPINorNIC;
    }

    public int getApplicantCountryId() {
        return applicantCountryId;
    }

    public void setApplicantCountryId(int applicantCountryId) {
        this.applicantCountryId = applicantCountryId;
    }

    public String getApplicantPassport() {
        return applicantPassport;
    }

    public void setApplicantPassport(String applicantPassport) {
        this.applicantPassport = applicantPassport;
    }

    public String getWifePINorNIC() {
        return wifePINorNIC;
    }

    public void setWifePINorNIC(String wifePINorNIC) {
        this.wifePINorNIC = wifePINorNIC;
    }

    public int getWifeCountryId() {
        return wifeCountryId;
    }

    public void setWifeCountryId(int wifeCountryId) {
        this.wifeCountryId = wifeCountryId;
    }

    public String getWifePassport() {
        return wifePassport;
    }

    public void setWifePassport(String wifePassport) {
        this.wifePassport = wifePassport;
    }

    public String getWifeName() {
        return wifeName;
    }

    public void setWifeName(String wifeName) {
        this.wifeName = filterBlanks(wifeName);
    }

    public String getCourtOrderNumber() {
        return courtOrderNumber;
    }

    public void setCourtOrderNumber(String courtOrderNumber) {
        this.courtOrderNumber = filterBlanks(courtOrderNumber);
    }

    public String getChildExistingName() {
        return childExistingName;
    }

    public void setChildExistingName(String childExistingName) {
        this.childExistingName = filterBlanks(childExistingName);
    }

    public String getChildNewName() {
        return childNewName;
    }

    public void setChildNewName(String childNewName) {
        this.childNewName = filterBlanks(childNewName);
    }

    public Date getChildBirthDate() {
        return childBirthDate;
    }

    public void setChildBirthDate(Date childBirthDate) {
        this.childBirthDate = childBirthDate;
    }

    public int getChildAgeYears() {
        return childAgeYears;
    }

    public void setChildAgeYears(int childAgeYears) {
        this.childAgeYears = childAgeYears;
    }

    public int getChildAgeMonths() {
        return childAgeMonths;
    }

    public void setChildAgeMonths(int childAgeMonths) {
        this.childAgeMonths = childAgeMonths;
    }

    public int getChildGender() {
        return childGender;
    }

    public void setChildGender(int childGender) {
        this.childGender = childGender;
    }

    public long getBirthCertificateNumber() {
        return birthCertificateNumber;
    }

    public void setBirthCertificateNumber(long birthCertificateNumber) {
        this.birthCertificateNumber = birthCertificateNumber;
    }

    public long getBirthCertificateSerial() {
        return birthCertificateSerial;
    }

    public void setBirthCertificateSerial(long birthCertificateSerial) {
        this.birthCertificateSerial = birthCertificateSerial;
    }

    public boolean isApplicantMother() {
        return applicantMother;
    }

    public void setApplicantMother(boolean applicantMother) {
        this.applicantMother = applicantMother;
    }

    public int getBirthDivisionId() {
        return birthDivisionId;
    }

    public void setBirthDivisionId(int birthDivisionId) {
        this.birthDivisionId = birthDivisionId;
    }

    public long getNewBirthCertificateNumber() {
        return newBirthCertificateNumber;
    }

    public void setNewBirthCertificateNumber(long newBirthCertificateNumber) {
        this.newBirthCertificateNumber = newBirthCertificateNumber;
    }

    public String getCertificateApplicantName() {
        return certificateApplicantName;
    }

    public void setCertificateApplicantName(String certificateApplicantName) {
        this.certificateApplicantName = certificateApplicantName;
    }

    public String getCertificateApplicantAddress() {
        return certificateApplicantAddress;
    }

    public void setCertificateApplicantAddress(String certificateApplicantAddress) {
        this.certificateApplicantAddress = certificateApplicantAddress;
    }

    public ApplicantType getCertificateApplicantType() {
        return certificateApplicantType;
    }

    public void setCertificateApplicantType(ApplicantType certificateApplicantType) {
        this.certificateApplicantType = certificateApplicantType;
    }

    public String getLanguageToTransliterate() {
        return languageToTransliterate;
    }

    public void setLanguageToTransliterate(String languageToTransliterate) {
        this.languageToTransliterate = languageToTransliterate;
    }

    public State getStatus() {
        return status;
    }

    public void setStatus(State status) {
        this.status = status;
    }

    public String getCertificateApplicantPINorNIC() {
        return certificateApplicantPINorNIC;
    }

    public void setCertificateApplicantPINorNIC(String certificateApplicantPINorNIC) {
        this.certificateApplicantPINorNIC = certificateApplicantPINorNIC;
    }
}
