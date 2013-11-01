package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.Location;
import lk.rgd.common.util.WebUtils;

import javax.persistence.*;
import java.util.BitSet;
import java.util.Date;


/**
 * @author Ashoka Ekanayaka
 *         The entity to store an alteration of a birth record.
 *         There will be many to one relationship with BDF records. After approval, an alteration will be aplied to the base birth record .
 */
@Entity
@Table(name = "ALT_BIRTH", schema = "CRS")
@NamedQueries({
    @NamedQuery(name = "filter.alteration.by.idUKey", query = "SELECT ba FROM BirthAlteration ba " +
        "WHERE ba.idUKey =:idUKey AND (ba.lifeCycleInfo.activeRecord IS TRUE) " +
        "AND ba.status <=:state ORDER BY ba.lifeCycleInfo.createdTimestamp desc"),
    @NamedQuery(name = "filter.alteration.by.bddivision", query = "SELECT ba " +
        "FROM BirthAlteration ba,BirthDeclaration bdf WHERE ba.bdfIdUKey =bdf.idUKey " +
        "AND (ba.lifeCycleInfo.activeRecord IS TRUE) AND bdf.register.birthDivision = :bdDivision  AND " +
        "ba.status <=:state ORDER BY ba.lifeCycleInfo.createdTimestamp desc"),
    @NamedQuery(name = "filter.alteration.by.user.location", query = "SELECT ba FROM BirthAlteration ba " +
        "WHERE ba.submittedLocation.locationUKey= :locationUKey AND (ba.lifeCycleInfo.activeRecord IS TRUE)" +
        " AND ba.status <=:state ORDER BY ba.lifeCycleInfo.createdTimestamp desc"),
    @NamedQuery(name = "get.alterations.by.birth.idUKey", query = "SELECT ba FROM BirthAlteration ba " +
        "WHERE ba.bdfIdUKey =:idUKey"),
/*    @NamedQuery(name = "filter.birth.alteration.by.birth.certificate.number", query = "SELECT ba " +
        "FROM BirthAlteration ba WHERE( ba.bdfIdUKey=:certificateNumber AND ba.lifeCycleInfo.activeRecord IS TRUE )" )*/
    @NamedQuery(name = "filter.birth.alteration.by.birth.certificate.number", query = "SELECT ba " +
        "FROM BirthAlteration ba WHERE( ba.bdfIdUKey=:certificateNumber AND ba.lifeCycleInfo.activeRecord IS TRUE )" +
        " AND ba.status <=:state ORDER BY ba.lifeCycleInfo.createdTimestamp desc")
})

public class BirthAlteration {

    /**
     * Section 27
     * Name change
     * <p/>
     * Section 27 A
     * Alteration of details
     * <p/>
     * Section 52 - 1
     * a - The event did not occur although recorded [Requires a cancellation of the existing record]
     * b - Has been registered more than once [Requires a cancellation of the existing record]
     * c - Wrong register has been used (e.g. Death register used for a Birth) [Will not occur in the computerized system]
     * d - Wrong registrar / Registration division used (e.g. Reg. Div 1 under DS Div A selected whereas it should have been Reg. Div 2) [Will require a re-registration as the correction ]
     * e - Wrong informant - A person who could should not have been identified as the informant has been specified as the informant - [Will require a re-registration as the correction ]
     * f - A late registration has taken place using the normal form for registration [Will not occur in the computerized system]
     * g - The entry has not been signed (Approved) by the registrar [Will not occur in the computerized system]
     * h & i - Corrections / omissions, Alterations etc.
     * j - Reconstruction of damaged records [Will not occur in the computerized system]
     */
    public enum AlterationType {
        TYPE_27, //0
        TYPE_27A,   //1
        TYPE_52_1_A,
        TYPE_52_1_B,
        TYPE_52_1_D,
        TYPE_52_1_E,
        TYPE_52_1_H,
        TYPE_52_1_I
    }

    public enum State {
        /**
         * 0 - A newly entered BDF - can be edited by DEO or ADR
         */
        DATA_ENTRY,
        /**
         * 1 - An ARG or higher partialy approved ba
         */

        FULLY_APPROVED,
        /**
         * 2- A fully completed alteration for which the BDF has being changed.
         */
        PRINTED,
        /**
         * 3 - A rejected alteration
         */
        REJECT,

    }

    @Enumerated
    private State status;

    @Enumerated
    private AlterationType type;

    /**
     * Contains the approval bit set for each field.
     */
    @Column(nullable = true)
    private BitSet approvalStatuses = new BitSet();

    /**
     * Contains the change bit set for each field.
     */
    @Column(nullable = true)
    private BitSet changedfields = new BitSet();

    @Id
    // This is an auto generated unique row identifier
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUKey;

    @Column(nullable = false)
    // the ID points to Birth Declarations idUKey
    private long bdfIdUKey;

    @ManyToOne
    @JoinColumn(name = "submitedLocationUKey", nullable = false)
    private Location submittedLocation;

    @ManyToOne
    @JoinColumn(name = "birthDivisionUKey", nullable = false)
    private BDDivision birthRecordDivision;

    @Column(nullable = false)
    // The date when the alteration request was received
    @Temporal(value = TemporalType.DATE)
    private Date dateReceived;

    @Embedded
    private Alteration27 alt27;

    @Embedded
    private Alteration27A alt27A;

    @Embedded
    private Alteration52_1 alt52_1;

    @Embedded
    private DeclarantInfo declarant = new DeclarantInfo();

    @Embedded
    private CRSLifeCycleInfo lifeCycleInfo = new CRSLifeCycleInfo();

    @Column
    private boolean bcOfFather;

    @Column
    private boolean bcOfMother;

    @Column
    private boolean mcOfParents;
    @Column
    private String otherDocuments;
    @Column
    private String comments;
    @Column
    private float stampFee;
    @Transient
    private String childNameInOfficialLanguage;

    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public Alteration27 getAlt27() {
        return alt27;
    }

    public void setAlt27(Alteration27 alt27) {
        this.alt27 = alt27;
    }

    public Alteration27A getAlt27A() {
        return alt27A;
    }

    public void setAlt27A(Alteration27A alt27A) {
        this.alt27A = alt27A;
    }

    public Alteration52_1 getAlt52_1() {
        return alt52_1;
    }

    public void setAlt52_1(Alteration52_1 alt52_1) {
        this.alt52_1 = alt52_1;
    }

    public DeclarantInfo getDeclarant() {
        return declarant;
    }

    public void setDeclarant(DeclarantInfo declarant) {
        this.declarant = declarant;
    }

    public CRSLifeCycleInfo getLifeCycleInfo() {
        return lifeCycleInfo;
    }

    public void setLifeCycleInfo(CRSLifeCycleInfo lifeCycleInfo) {
        this.lifeCycleInfo = lifeCycleInfo;
    }

    public boolean isBcOfFather() {
        return bcOfFather;
    }

    public void setBcOfFather(boolean bcOfFather) {
        this.bcOfFather = bcOfFather;
    }

    public boolean isBcOfMother() {
        return bcOfMother;
    }

    public void setBcOfMother(boolean bcOfMother) {
        this.bcOfMother = bcOfMother;
    }

    public boolean isMcOfParents() {
        return mcOfParents;
    }

    public void setMcOfParents(boolean mcOfParents) {
        this.mcOfParents = mcOfParents;
    }

    public float getStampFee() {
        return stampFee;
    }

    public void setStampFee(float stampFee) {
        this.stampFee = stampFee;
    }

    public long getBdfIdUKey() {
        return bdfIdUKey;
    }

    public void setBdfIdUKey(long bdfIdUKey) {
        this.bdfIdUKey = bdfIdUKey;
    }

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public BitSet getApprovalStatuses() {
        return approvalStatuses;
    }

    public void setApprovalStatuses(BitSet approvalStatuses) {
        this.approvalStatuses = approvalStatuses;
    }

    public State getStatus() {
        return status;
    }

    public void setStatus(State status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = WebUtils.filterBlanks(comments);
    }

    public String getOtherDocuments() {
        return otherDocuments;
    }

    public void setOtherDocuments(String otherDocuments) {
        this.otherDocuments = otherDocuments;
    }

    public Location getSubmittedLocation() {
        return submittedLocation;
    }

    public void setSubmittedLocation(Location submittedLocation) {
        this.submittedLocation = submittedLocation;
    }

    public AlterationType getType() {
        return type;
    }

    public void setType(AlterationType type) {
        this.type = type;
    }

    public BDDivision getBirthRecordDivision() {
        return birthRecordDivision;
    }

    public void setBirthRecordDivision(BDDivision birthRecordDivision) {
        this.birthRecordDivision = birthRecordDivision;
    }

    public BitSet getChangedfields() {
        return changedfields;
    }

    public void setChangedfields(BitSet changedfields) {
        this.changedfields = changedfields;
    }

    public String getChildNameInOfficialLanguage() {
        return childNameInOfficialLanguage;
    }

    public void setChildNameInOfficialLanguage(String childNameInOfficialLanguage) {
        this.childNameInOfficialLanguage = childNameInOfficialLanguage;
    }
}
