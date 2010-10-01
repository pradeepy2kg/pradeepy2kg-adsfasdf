package lk.rgd.crs.api.domain;

import javax.persistence.*;
import java.util.BitSet;
import java.util.Date;

/**
 * @authar amith jayasekara
 * the entity class to store death alteration recodes
 */
@Entity
@Table(name = "ALT_DEATH", schema = "CRS")

public class DeathAlteration {

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

    //id points to death declaration
    @Column(nullable = false)
    private long deathId;

    @Column(nullable = false)
    // The date when the alteration request was received
    @Temporal(value = TemporalType.DATE)
    private Date dateReceived;
/*
    @Embedded
    private DeathInfo death = new DeathInfo();*/

    @Embedded
    private DeathPersonInfo deathPerson = new DeathPersonInfo();

    @Embedded
    private DeclarantInfo declarant = new DeclarantInfo();

    @Embedded
    private CRSLifeCycleInfo lifeCycleInfo = new CRSLifeCycleInfo();

    @Column(nullable = false, length = 255)
    private String placeOfDeath;

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfDeath;

    @Column(nullable = true)
    private String timeOfDeath;

    @Column(nullable = true, length = 255)
    private String placeOfDeathInEnglish;

    /**
     * 1-Yes, 0-No
     */
    @Column(nullable = true)
    private boolean causeOfDeathEstablished;

    @Column(nullable = true, length = 600)
    private String causeOfDeath;

    @Column(nullable = true)
    private String icdCodeOfCause;

    @Column(nullable = false)
    private String placeOfBurial;


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

    /**
     * The Birth/Death registration division where the birth is registered (Includes District)
     */
    @ManyToOne
    @JoinColumn(name = "bdDivisionUKey", nullable = true, insertable = false, updatable = false)
    private BDDivision deathDivision;

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

    public Long getAlterationSerialNo() {
        return alterationSerialNo;
    }

    public void setAlterationSerialNo(Long alterationSerialNo) {
        this.alterationSerialNo = alterationSerialNo;
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

    public BDDivision getDeathDivision() {
        return deathDivision;
    }

    public void setDeathDivision(BDDivision deathDivision) {
        this.deathDivision = deathDivision;
    }

    public String getPlaceOfDeath() {
        return placeOfDeath;
    }

    public void setPlaceOfDeath(String placeOfDeath) {
        this.placeOfDeath = placeOfDeath;
    }

    public Date getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(Date dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public String getTimeOfDeath() {
        return timeOfDeath;
    }

    public void setTimeOfDeath(String timeOfDeath) {
        this.timeOfDeath = timeOfDeath;
    }

    public String getPlaceOfDeathInEnglish() {
        return placeOfDeathInEnglish;
    }

    public void setPlaceOfDeathInEnglish(String placeOfDeathInEnglish) {
        this.placeOfDeathInEnglish = placeOfDeathInEnglish;
    }

    public boolean isCauseOfDeathEstablished() {
        return causeOfDeathEstablished;
    }

    public void setCauseOfDeathEstablished(boolean causeOfDeathEstablished) {
        this.causeOfDeathEstablished = causeOfDeathEstablished;
    }

    public String getCauseOfDeath() {
        return causeOfDeath;
    }

    public void setCauseOfDeath(String causeOfDeath) {
        this.causeOfDeath = causeOfDeath;
    }

    public String getIcdCodeOfCause() {
        return icdCodeOfCause;
    }

    public void setIcdCodeOfCause(String icdCodeOfCause) {
        this.icdCodeOfCause = icdCodeOfCause;
    }

    public String getPlaceOfBurial() {
        return placeOfBurial;
    }

    public void setPlaceOfBurial(String placeOfBurial) {
        this.placeOfBurial = placeOfBurial;
    }
}
