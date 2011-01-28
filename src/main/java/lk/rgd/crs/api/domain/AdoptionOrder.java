package lk.rgd.crs.api.domain;

import lk.rgd.common.util.WebUtils;

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
// TODO add a constraint to ensure court ID and court order number combination is unique

@NamedQueries({
    @NamedQuery(name = "adoption.filter.by.status.paginated", query = "SELECT adoption FROM AdoptionOrder adoption " +
        "WHERE adoption.status = :status " + "ORDER BY adoption.orderIssuedDate desc"),

    @NamedQuery(name = "get.by.court.and.courtOrderNumber", query = "SELECT adoption FROM AdoptionOrder adoption " +
        "WHERE adoption.courtOrderNumber = :courtOrderNumber"),
    // TODO fix this to use the courtUKey

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
        RE_REGISTRATION_REQUESTED,// 6 requesting an adoption re registration
        RE_REGISTERED // 7 re registered
    }

    public enum ApplicantType {
        FATHER,    // 0

        MOTHER,   // 1

        OTHER,     // 2
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUKey;

    @Embedded
    private CRSLifeCycleInfo lifeCycleInfo = new CRSLifeCycleInfo();

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date orderIssuedDate;

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date orderReceivedDate;

    @Column(nullable = false)
    private String courtOrderNumber;

    @OneToOne
    @JoinColumn(name = "courtUKey", nullable = true)
    private Court court;

    @Column(nullable = true)
    private String judgeName;

    @Column(nullable = false)
    private String applicantName;

    @Column(nullable = false)
    private String applicantAddress;

    @Column(nullable = true)
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
    private String childPIN;

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
    private long birthCertificateNumber; // idukey, not the serial of old birth certificate issued before adoption

    @Column(nullable = true)
    private long birthRegistrationSerial; // if BC number not given

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

    @Column(columnDefinition = "char(2) default 'si'", nullable = false)
    private String languageToTransliterate = "si";

    @Column(nullable = false)
    private State status;

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

    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;// WebUtils.filterBlanksAndToUpper(court);
    }

    public String getJudgeName() {
        return judgeName;
    }

    public void setJudgeName(String judgeName) {
        this.judgeName = WebUtils.filterBlanksAndToUpper(judgeName);
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = WebUtils.filterBlanksAndToUpper(applicantName);
    }

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(String applicantAddress) {
        this.applicantAddress = WebUtils.filterBlanksAndToUpper(applicantAddress);
    }

    public String getApplicantPINorNIC() {
        return applicantPINorNIC;
    }

    public void setApplicantPINorNIC(String applicantPINorNIC) {
        this.applicantPINorNIC = WebUtils.filterBlanksAndToUpper(applicantPINorNIC);
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
        this.applicantPassport = WebUtils.filterBlanksAndToUpper(applicantPassport);
    }

    public String getWifePINorNIC() {
        return wifePINorNIC;
    }

    public void setWifePINorNIC(String wifePINorNIC) {
        this.wifePINorNIC = WebUtils.filterBlanksAndToUpper(wifePINorNIC);
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
        this.wifePassport = WebUtils.filterBlanksAndToUpper(wifePassport);
    }

    public String getWifeName() {
        return wifeName;
    }

    public void setWifeName(String wifeName) {
        this.wifeName = WebUtils.filterBlanksAndToUpper(wifeName);
    }

    public String getCourtOrderNumber() {
        return courtOrderNumber;
    }

    public void setCourtOrderNumber(String courtOrderNumber) {
        this.courtOrderNumber = WebUtils.filterBlanks(courtOrderNumber);
    }

    public String getChildExistingName() {
        return childExistingName;
    }

    public String getChildExistingNameToLength(int maxLength) {
        if (childExistingName != null && childExistingName.length() > maxLength) {
            return "..." + childExistingName.substring(childExistingName.length() - maxLength + 3,
                childExistingName.length());
        }
        return childExistingName;
    }

    public void setChildExistingName(String childExistingName) {
        this.childExistingName = WebUtils.filterBlanksAndToUpper(childExistingName);
    }

    public String getChildNewName() {
        return childNewName;
    }

    public String getChildNewNameToLength(int maxLength) {
        if (childNewName != null && childNewName.length() > maxLength) {
            return "..." + childNewName.substring(childNewName.length() - maxLength + 3,
                childNewName.length());
        }
        return childNewName;
    }

    public void setChildNewName(String childNewName) {
        this.childNewName = WebUtils.filterBlanksAndToUpper(childNewName);
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

    public long getBirthRegistrationSerial() {
        return birthRegistrationSerial;
    }

    public void setBirthRegistrationSerial(long birthRegistrationSerial) {
        this.birthRegistrationSerial = birthRegistrationSerial;
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
        this.certificateApplicantName = WebUtils.filterBlanksAndToUpper(certificateApplicantName);
    }

    public String getCertificateApplicantAddress() {
        return certificateApplicantAddress;
    }

    public void setCertificateApplicantAddress(String certificateApplicantAddress) {
        this.certificateApplicantAddress = WebUtils.filterBlanksAndToUpper(certificateApplicantAddress);
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
        this.certificateApplicantPINorNIC = WebUtils.filterBlanksAndToUpper(certificateApplicantPINorNIC);
    }

    public CRSLifeCycleInfo getLifeCycleInfo() {
        return lifeCycleInfo;
    }

    public void setLifeCycleInfo(CRSLifeCycleInfo lifeCycleInfo) {
        this.lifeCycleInfo = lifeCycleInfo;
    }

    public String getChildPIN() {
        return childPIN;
    }

    public void setChildPIN(String childPIN) {
        this.childPIN = childPIN;
    }
}
