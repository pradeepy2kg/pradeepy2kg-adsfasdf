package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.Location;

import javax.persistence.*;
import java.util.BitSet;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/**
 * @authar amith jayasekara
 * the entity class to store death alteration recodes
 */
@Entity
@Table(name = "ALT_DEATH", schema = "CRS")
@NamedQueries({
        @NamedQuery(name = "get.alt.by.death.certificate.number", query = "SELECT da FROM DeathAlteration da WHERE da.deathId =:deathCertificateNumber"),
        @NamedQuery(name = "get.alt.by.division.death.division", query = "SELECT da FROM DeathAlteration da,DeathRegister dr" +
                " WHERE da.deathId=dr.idUKey " +
                " AND dr.death.deathDivision.bdDivisionUKey =:deathDivisionUkey"),
        @NamedQuery(name = "get.atl.by.death.id", query = "SELECT da FROM DeathAlteration da WHERE da.deathId=:deathId"),
        @NamedQuery(name = "get.alt.by.user.location", query = "SELECT da FROM DeathAlteration  da WHERE da.submittedLocation.locationUKey =:locationUKey"),
        //todo
        @NamedQuery(name = "get.alt.by.seral.number.death.division", query = "SELECT da FROM DeathAlteration da")
})

public class DeathAlteration {

    public static final Map<Integer, String> indexMap = new HashMap<Integer, String>();

    static {

        indexMap.put(1, "field.sudden.death");
        indexMap.put(2, "field.date.death");
        indexMap.put(3, "field.time.death");
        indexMap.put(4, "field.place.death");
        indexMap.put(5, "field.place.death.english");
        indexMap.put(6, "field.cause.death.established");
        indexMap.put(7, "field.cause.death");
        indexMap.put(8, "field.icd.code");
        indexMap.put(9, "field.burial.place");

        indexMap.put(10, "field.pin.number");
        indexMap.put(11, "field.country");
        indexMap.put(12, "field.passPort");
        indexMap.put(13, "field.age");
        indexMap.put(14, "field.gender");
        indexMap.put(15, "field.race");
        indexMap.put(16, "field.name");
        indexMap.put(17, "field.name.english");
        indexMap.put(18, "field.address");
        indexMap.put(19, "field.pin.father");
        indexMap.put(20, "field.name.father");
        indexMap.put(21, "field.pin.mother");
        indexMap.put(22, "field.name.mother");
    }

    public enum AlterationType {
        TYPE_52_1_A,
        TYPE_52_1_B,
        TYPE_52_1_D,
        TYPE_52_1_E,
        TYPE_52_1_H,
        TYPE_52_1_I,
        TYPE_53
    }

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
         * 3 - A DC for which the parent confirmation form was printed
         */
        PRINTED,
        /**
         * 4 - A DC for which the parent confirmation form was printed
         */
        REJECT,

    }

    public enum Act {
        ACT_52_1_a,
        ACT_52_1_b,
        ACT_52_1_d,
        ACT_52_1_e,
        ACT_52_1_i,
        ACT_52_1_h,
        ACT_53,
    }

    @Enumerated
    private State status;

    @Enumerated
    private AlterationType type;

    @Enumerated
    private Act act;

    // This is an auto generated unique row identifier
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUKey;

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date dateReceived;
    /**
     * Contains the approval bit set for each field.
     */
    @Column(nullable = true)
    private BitSet approvalStatuses;
    //id points to death declaration
    @Column(nullable = false)
    private long deathId;

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

    @ManyToOne
    @JoinColumn(name = "submitedLocationUKey", nullable = false)
    private Location submittedLocation;

    @ManyToOne
    @JoinColumn(name = "deathDivisionUKey", nullable = false)
    private BDDivision deathRecodeDivision;

    @Embedded
    private DeathAlterationInfo deathInfo = new DeathAlterationInfo();

    @Embedded
    private DeathPersonInfo deathPerson = new DeathPersonInfo();

    @Embedded
    private DeclarantInfo declarant = new DeclarantInfo();

    @Embedded
    private CRSLifeCycleInfo lifeCycleInfo = new CRSLifeCycleInfo();

    public State getStatus() {
        return status;
    }

    public void setStatus(State status) {
        this.status = status;
    }

    public BitSet getApprovalStatuses() {
        return approvalStatuses;
    }

    public void setApprovalStatuses(BitSet approvalStatuses) {
        this.approvalStatuses = approvalStatuses;
    }

    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public long getDeathId() {
        return deathId;
    }

    public void setDeathId(long deathId) {
        this.deathId = deathId;
    }

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
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

    public boolean isBcOfMother() {
        return bcOfMother;
    }

    public void setBcOfMother(boolean bcOfMother) {
        this.bcOfMother = bcOfMother;
    }

    public float getStampFee() {
        return stampFee;
    }

    public void setStampFee(float stampFee) {
        this.stampFee = stampFee;
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

    public boolean isMcOfParents() {
        return mcOfParents;
    }

    public void setMcOfParents(boolean mcOfParents) {
        this.mcOfParents = mcOfParents;
    }

    public boolean isBcOfFather() {
        return bcOfFather;
    }

    public void setBcOfFather(boolean bcOfFather) {
        this.bcOfFather = bcOfFather;
    }

    public DeathPersonInfo getDeathPerson() {
        return deathPerson;
    }

    public void setDeathPerson(DeathPersonInfo deathPerson) {
        this.deathPerson = deathPerson;
    }

    public Act getAct() {
        return act;
    }

    public void setAct(Act act) {
        this.act = act;
    }

    public DeathAlterationInfo getDeathInfo() {
        return deathInfo;
    }

    public void setDeathInfo(DeathAlterationInfo deathInfo) {
        this.deathInfo = deathInfo;
    }

    public BDDivision getDeathRecodeDivision() {
        return deathRecodeDivision;
    }

    public void setDeathRecodeDivision(BDDivision deathRecodeDivision) {
        this.deathRecodeDivision = deathRecodeDivision;
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
}
