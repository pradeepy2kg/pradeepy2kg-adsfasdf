package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.ZonalOffice;
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

    @NamedQuery(name = "getAllAdoptions", query = "SELECT adoption FROM AdoptionOrder adoption"),
    @NamedQuery(name = "count.adoption.court.usage", query = "SELECT COUNT(adoption) FROM AdoptionOrder adoption " +
        "WHERE adoption.court.courtUKey = :courtId AND adoption.status <> 0"),
    @NamedQuery(
        name = "isEntryNoExist",
        query = "SELECT a FROM AdoptionOrder a WHERE a.adoptionEntryNo = :adoptionEntryNo"
    ),
    @NamedQuery(
        name = "getLastEntryNo",
        query = "SELECT MAX(a.adoptionEntryNo) FROM AdoptionOrder a"
    ),
    @NamedQuery(
        name = "getAdoptionsByCourtOrderNumber",
        query = "SELECT a FROM AdoptionOrder a WHERE a.courtOrderNumber LIKE :courtOrderNumber"
    ),
    @NamedQuery(
        name = "getAdoptionByEntryNumber",
        query = "SELECT a FROM AdoptionOrder a WHERE a.adoptionEntryNo = :adoptionEntryNo"
    ),
    @NamedQuery(
        name = "getAdoptionsByCourt",
        query = "SELECT a FROM AdoptionOrder a WHERE a.court.courtUKey = :courtUKey"
    )
})

public class AdoptionOrder implements Serializable {

    public enum State {
        /**
         * 0 - A newly entered Adoption - can be edited by DEO, ADR
         */
        DATA_ENTRY,
        /**
         * 1 - An ARG or higher approved Adoption
         */
        APPROVED,
        /**
         * 2 - An Adoption Order Details were printed.
         */
        ORDER_DETAILS_PRINTED,
        /**
         * 3 - An  Adoption which is printed for parent confirmation
         */
        NOTICE_LETTER_PRINTED,
        /**
         * 4 - An Adoption rejected by the ARG
         */
        REJECTED,
        /**
         * 5 - A certificate is requested
         */
        CERTIFICATE_ISSUE_REQUEST_CAPTURED,
        /**
         * 6 - A certificate is requested
         */
        ADOPTION_CERTIFICATE_PRINTED,
        /**
         * 7 - requesting an adoption re registration
         */
        RE_REGISTRATION_REQUESTED,
        /**
         * 8 - re registered
         */
        RE_REGISTERED
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

    /**
     * Details of the applicant.
     * Applicant can be Father, Mother, Both parents or other person [ONLY ONE] such as matron.
     */
    @Column(nullable = false)
    private String applicantName;

    @Column(nullable = false)
    private String applicantAddress;

    @Column(nullable = true)
    private String applicantSecondAddress;

    @Column(nullable = true)
    private String applicantOccupation;

    @Column(nullable = true, length = 12)
    private String applicantPINorNIC;

    @Column(nullable = true)
    private int applicantCountryId;

    @Column(nullable = true)
    private String applicantPassport;

    /**
     * Only husband and wife can apply as joint applicants.
     * It this is TRUE, then the spouseName should be mandatory; Null otherwise.
     */
    @Column(nullable = false)
    private boolean jointApplicant;

    // TODO remove
    @Column(nullable = false)
    private boolean applicantMother; // false if father is the applicant (Usual case), true if it is the mother

    /**
     * Details of the spouse.
     * These details will be captured only if the court order is given to two parties (husband and wife).
     * If it is a joint application, then the name of the spouse should be mandatory.
     */
    @Column(nullable = true)
    private String spouseName;

    @Column(nullable = true)
    private String spouseOccupation;

    @Column(nullable = true, length = 12)
    private String spousePINorNIC;

    @Column(nullable = true)
    private int spouseCountryId;

    @Column(nullable = true)
    private String spousePassport;


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
    private int birthProvinceUKey;   // if BC number not given

    @Column(nullable = true)
    private int birthDistrictId; // if BC number not given

    @Column(nullable = true)
    private String oldBirthDSName;

    @Column(nullable = true)
    private String oldBirthRegistrationDivisionName;
    
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date oldBirthRegistrationDate;

    @Column(nullable = true)
    private Long oldBirthSLIN;

    @Column(nullable = true)
    private long newBirthCertificateNumber; // idUKey of new birth registration after adoption 

    @Column(nullable = true)
    private String certificateApplicantName;

    @Column(nullable = true)
    private String certificateApplicantAddress;

    @Column(nullable = true)
    private ApplicantType certificateApplicantType;

    @Column(nullable = true, length = 12)
    private String certificateApplicantPINorNIC;

    @Column(columnDefinition = "char(2) default 'si'", nullable = false)
    private String languageToTransliterate = "si";

    @Column(nullable = false)
    private State status;

    /**
     * To track the order of capture information of the adoption from parents.
     * Format: Year in 4 digits followed by a sequence number 00001- 99999.
     * eg: Serial number of 125th record in year 2012 is 201200125
     */
    @Column(nullable = true)
    private Long adoptionSerialNo;

    /**
     * Entry number of the adoption. Mentioned in the Adoption Order Details.
     */
    @Column(nullable = false)
    private Long adoptionEntryNo;

    @ManyToOne
    @JoinColumn(name = "noticingZonalOffice", nullable = false)
    private ZonalOffice noticingZonalOffice;

    public Long getAdoptionEntryNo() {
        return adoptionEntryNo;
    }

    public void setAdoptionEntryNo(Long adoptionEntryNo) {
        this.adoptionEntryNo = adoptionEntryNo;
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

    public String getSpousePINorNIC() {
        return spousePINorNIC;
    }

    public void setSpousePINorNIC(String spousePINorNIC) {
        this.spousePINorNIC = WebUtils.filterBlanksAndToUpper(spousePINorNIC);
    }

    public int getSpouseCountryId() {
        return spouseCountryId;
    }

    public void setSpouseCountryId(int spouseCountryId) {
        this.spouseCountryId = spouseCountryId;
    }

    public String getSpousePassport() {
        return spousePassport;
    }

    public void setSpousePassport(String spousePassport) {
        this.spousePassport = WebUtils.filterBlanksAndToUpper(spousePassport);
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = WebUtils.filterBlanksAndToUpper(spouseName);
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

    public boolean isApplicantMother() {
        return applicantMother;
    }

    public void setApplicantMother(boolean applicantMother) {
        this.applicantMother = applicantMother;
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

    public Long getAdoptionSerialNo() {
        return adoptionSerialNo;
    }

    public void setAdoptionSerialNo(Long adoptionSerialNo) {
        this.adoptionSerialNo = adoptionSerialNo;
    }

    public String getApplicantOccupation() {
        return applicantOccupation;
    }

    public void setApplicantOccupation(String applicantOccupation) {
        this.applicantOccupation = applicantOccupation;
    }

    public String getSpouseOccupation() {
        return spouseOccupation;
    }

    public void setSpouseOccupation(String spouseOccupation) {
        this.spouseOccupation = spouseOccupation;
    }

    public ZonalOffice getNoticingZonalOffice() {
        return noticingZonalOffice;
    }

    public void setNoticingZonalOffice(ZonalOffice noticingZonalOffice) {
        this.noticingZonalOffice = noticingZonalOffice;
    }

    public int getBirthProvinceUKey() {
        return birthProvinceUKey;
    }

    public void setBirthProvinceUKey(int birthProvinceUKey) {
        this.birthProvinceUKey = birthProvinceUKey;
    }

    public int getBirthDistrictId() {
        return birthDistrictId;
    }

    public void setBirthDistrictId(int birthDistrictId) {
        this.birthDistrictId = birthDistrictId;
    }

    public String getOldBirthDSName() {
        return oldBirthDSName;
    }

    public void setOldBirthDSName(String oldBirthDSName) {
        this.oldBirthDSName = oldBirthDSName;
    }

    public String getOldBirthRegistrationDivisionName() {
        return oldBirthRegistrationDivisionName;
    }

    public void setOldBirthRegistrationDivisionName(String oldBirthRegistrationDivisionName) {
        this.oldBirthRegistrationDivisionName = oldBirthRegistrationDivisionName;
    }

    public Date getOldBirthRegistrationDate() {
        return oldBirthRegistrationDate;
    }

    public void setOldBirthRegistrationDate(Date oldBirthRegistrationDate) {
        this.oldBirthRegistrationDate = oldBirthRegistrationDate;
    }

    public long getOldBirthSLIN() {
        return oldBirthSLIN;
    }

    public void setOldBirthSLIN(long oldBirthSLIN) {
        this.oldBirthSLIN = oldBirthSLIN;
    }

    public String getApplicantSecondAddress() {
        return applicantSecondAddress;
    }

    public void setApplicantSecondAddress(String applicantSecondAddress) {
        this.applicantSecondAddress = applicantSecondAddress;
    }

    public boolean isJointApplicant() {
        return jointApplicant;
    }

    public void setJointApplicant(boolean jointApplicant) {
        this.jointApplicant = jointApplicant;
    }
}
