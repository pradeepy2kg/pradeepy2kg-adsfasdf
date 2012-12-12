package lk.rgd.crs.api.domain;

import javax.persistence.*;
import java.util.BitSet;
import java.util.Date;

/**
 * @author Duminda Dharmakeerthi
 *
 * The entity to store an alteration record of a Adoption record.
 *
 */

@Entity
@Table(name = "ALT_ADOPTION", schema = "CRS")
@NamedQueries({
    @NamedQuery(
        name = "getAdoptionAlterationByIdUKey",
        query = "SELECT a FROM AdoptionAlteration a WHERE a.idUKey = :idUKey"
    ),
    @NamedQuery(
        name = "getAllAdoptionAlterationRecords",
        query = "SELECT a FROM AdoptionAlteration a"
    ),
    @NamedQuery(
        name = "getAdoptionAlterationsByStatus",
        query = "SELECT a FROM AdoptionAlteration a WHERE a.status = :state"
    )
})
public class AdoptionAlteration {
    public static final int CHILD_NAME                  = 0;
    public static final int CHILD_GENDER                = 1;
    public static final int CHILD_DOB                   = 2;
    public static final int APPLICANT_NAME              = 3;
    public static final int APPLICANT_ADDRESS           = 4;
    public static final int APPLICANT_SECOND_ADDRESS    = 5;
    public static final int APPLICANT_OCCUPATION        = 6;
    public static final int SPOUSE_NAME                 = 7;
    public static final int SPOUSE_OCCUPATION           = 8;

    public enum State{
        /**
         * 0 - Newly entered adoption alteration. Can be edit by DEO
         */
        DATA_ENTRY,
        /**
         * 1 - ARG or higher approved the alteration.
         */
        FULL_APPROVED,
        /**
         * 2 - ARG or higher rejected the alteration.
         */
        REJECTED
    }

    public enum Method{
        /**
         * 0 - Adoption alteration according to the form filled by (parents, guardian, etc).
         *  Common way of applying for adoption alteration.
         */
        BY_APPLICATION,
        /**
         * 1- Adoption alteration according to a court order.
         */
        BY_COURT_ORDER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUKey;

    @Column(nullable = false)
    private long aoUKey;            // idUKey of the Adoption Order

    @Embedded
    private CRSLifeCycleInfo lifeCycleInfo = new CRSLifeCycleInfo();

    @Column(nullable = false)
    private State status;

    @Column(nullable = false)
    private Method method;

    @Column(nullable = true)
    private BitSet changedFields = new BitSet();

    @Column(nullable = true)
    private BitSet approvalStatuses = new BitSet();

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date dateReceived;

    @Column(nullable = false)
    private String placeReceived;

    /**
     * Alteration fields for child details
     */
    @Column(nullable = true)
    private String childName;
    
    @Column(nullable = true)
    private int childGender;

    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date childBirthDate;

    /**
     * Alteration fields for applicant details
     */
    @Column(nullable = true)
    private String applicantName;

    @Column(nullable = true)
    private String applicantAddress;

    @Column(nullable = true)
    private String applicantSecondAddress;

    @Column(nullable = true)
    private String applicantOccupation;

    /**
     * Alteration fields for spouse details
     */
    @Column(nullable = true)
    private String spouseName;

    @Column(nullable = true)
    private String spouseOccupation;

    @Column
    private String comments;

    /**
     * When the alteration is according to a court order, The TYPE of the declarant will be OTHER.
     */
    @Embedded
    private DeclarantInfo declarant = new DeclarantInfo();

    /**
     * If the alteration is done according to alteration request by court order, these fields will be captured.
     */
    @OneToOne
    @JoinColumn(name = "courtUKey", nullable = true)
    private Court court;

    @Column(nullable = true)
    private String courtOrderNumber;

    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public long getAoUKey() {
        return aoUKey;
    }

    public void setAoUKey(long aoUKey) {
        this.aoUKey = aoUKey;
    }

    public CRSLifeCycleInfo getLifeCycleInfo() {
        return lifeCycleInfo;
    }

    public void setLifeCycleInfo(CRSLifeCycleInfo lifeCycleInfo) {
        this.lifeCycleInfo = lifeCycleInfo;
    }

    public State getStatus() {
        return status;
    }

    public void setStatus(State status) {
        this.status = status;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public int getChildGender() {
        return childGender;
    }

    public void setChildGender(int childGender) {
        this.childGender = childGender;
    }

    public Date getChildBirthDate() {
        return childBirthDate;
    }

    public void setChildBirthDate(Date childBirthDate) {
        this.childBirthDate = childBirthDate;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(String applicantAddress) {
        this.applicantAddress = applicantAddress;
    }

    public String getApplicantSecondAddress() {
        return applicantSecondAddress;
    }

    public void setApplicantSecondAddress(String applicantSecondAddress) {
        this.applicantSecondAddress = applicantSecondAddress;
    }

    public String getApplicantOccupation() {
        return applicantOccupation;
    }

    public void setApplicantOccupation(String applicantOccupation) {
        this.applicantOccupation = applicantOccupation;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getSpouseOccupation() {
        return spouseOccupation;
    }

    public void setSpouseOccupation(String spouseOccupation) {
        this.spouseOccupation = spouseOccupation;
    }

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getPlaceReceived() {
        return placeReceived;
    }

    public void setPlaceReceived(String placeReceived) {
        this.placeReceived = placeReceived;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public DeclarantInfo getDeclarant() {
        return declarant;
    }

    public void setDeclarant(DeclarantInfo declarant) {
        this.declarant = declarant;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public BitSet getChangedFields() {
        return changedFields;
    }

    public void setChangedFields(BitSet changedFields) {
        this.changedFields = changedFields;
    }

    public BitSet getApprovalStatuses() {
        return approvalStatuses;
    }

    public void setApprovalStatuses(BitSet approvalStatuses) {
        this.approvalStatuses = approvalStatuses;
    }

    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;
    }

    public String getCourtOrderNumber() {
        return courtOrderNumber;
    }

    public void setCourtOrderNumber(String courtOrderNumber) {
        this.courtOrderNumber = courtOrderNumber;
    }
}
