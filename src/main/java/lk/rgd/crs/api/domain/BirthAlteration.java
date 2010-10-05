package lk.rgd.crs.api.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.BitSet;


/**
 * @author Ashoka Ekanayaka
 *         The entity to store a single field alteration of a death, marriage or a birth record.
 *         There will be many to one relationship with one of those tables. after approval, alterations will be aplied to the base death/birth/marrige
 *         record .
 */
@Entity
@Table(name = "ALT_BIRTH", schema = "CRS")
@NamedQueries({
        @NamedQuery(name = "get.active.ba.by.bddivision.in.bdf.and.alterationSerialNo", query = "SELECT ba FROM BirthAlteration ba, BirthDeclaration bdf " +
                "WHERE bdf.register.birthDivision = :bdDivision AND ba.alterationSerialNo = :alterationSerialNo " +
                "AND ba.lifeCycleInfo.activeRecord IS TRUE"),
        @NamedQuery(name = "get.active.ba.by.bddivision.in.ba.and.alterationSerialNo", query = "SELECT ba FROM BirthAlteration ba " +
                "WHERE ba.alt52_1.birthDivision = :bdDivision AND ba.alterationSerialNo = :alterationSerialNo " +
                "AND ba.lifeCycleInfo.activeRecord IS TRUE"),
        @NamedQuery(name = "filter.alteration.by.dsdivision", query = "SELECT ba FROM BirthAlteration ba , BirthDeclaration bdf " +
                "WHERE ba.bdId =bdf.idUKey AND bdf.register.birthDivision.dsDivision = :dsDivision AND ba.status <> :statusFullyApp AND ba.status <> :statusPrint " +
                "ORDER BY ba.lifeCycleInfo.createdTimestamp desc"),
        @NamedQuery(name = "filter.alteration.by.bddivision", query = "SELECT ba FROM BirthAlteration ba,BirthDeclaration bdf " +
                "WHERE ba.bdId =bdf.idUKey AND bdf.register.birthDivision = :bdDivision AND ba.status <> :statusFullyApp AND ba.status <> :statusPrint " +
                "ORDER BY ba.lifeCycleInfo.createdTimestamp desc")

})

public class 
        BirthAlteration {


    public enum State {
        /**
         * 0 - A newly entered BDF - can be edited by DEO or ADR
         */
        DATA_ENTRY,
        /**
         * 1 - An ADR or higher partialy approved ba
         */
        PARTIALY_APPROVED,
        /*
        * 2 - An ADR or higher fully approved ba
        * */
        FULLY_APPROVED,
        /**
         * 3 - A BDF for which the parent confirmation form was printed
         */
        PRINTED,
        /**
         * 4 - A BDF for which the parent confirmation form was printed
         */
        REJECT,

    }

    @Enumerated
    private State status;
    /**
     * Contains the approval bit set for each field.
     */
    @Column(nullable = true)
    private BitSet approvalStatuses;
    @Id
    // This is an auto generated unique row identifier
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUKey;

    // This is the serial number for the BirthAlteration
    @Column(nullable = false, updatable = false)
    private Long alterationSerialNo;

    @Column(nullable = false)
    // the ID points to Birth Declarition
    private long bdId;

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
    private DeclarantInfo declarant;

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

    public long getBdId() {
        return bdId;
    }

    public void setBdId(long bdId) {
        this.bdId = bdId;
    }

    public Long getAlterationSerialNo() {
        return alterationSerialNo;
    }

    public void setAlterationSerialNo(Long alterationSerialNo) {
        this.alterationSerialNo = alterationSerialNo;
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
        this.comments = comments;
    }

    public String getOtherDocuments() {
        return otherDocuments;
    }

    public void setOtherDocuments(String otherDocuments) {
        this.otherDocuments = otherDocuments;
    }
}
