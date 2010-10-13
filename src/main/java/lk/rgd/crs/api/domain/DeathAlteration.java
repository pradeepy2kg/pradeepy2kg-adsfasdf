package lk.rgd.crs.api.domain;

import javax.persistence.*;
import java.util.BitSet;
import java.util.Date;

/**
 * @authar amith jayasekara
 * the entity class to store death alteration recodes
 */     //SELECT * FROM CRS.ALT_DEATH as a, CRS.DEATH_REGISTER as d WHERE a.deathid=d.idukey AND d.deathserialno=2010014566 AND d.bddivisionukey=1;
@Entity
@Table(name = "ALT_DEATH", schema = "CRS")
@NamedQueries({
        @NamedQuery(name = "get.alt.by.death.certificate.number", query = "SELECT da FROM DeathAlteration da WHERE da.deathId =:deathCertificateNumber"),
        @NamedQuery(name = "get.alt.by.division.death.division", query = "SELECT da FROM DeathAlteration da,DeathRegister dr" +
                " WHERE da.deathId=dr.idUKey " +
                " AND dr.death.deathDivision.bdDivisionUKey =:deathDivisionUkey"),
        @NamedQuery(name = "get.atl.by.death.id", query = "SELECT da FROM DeathAlteration da WHERE da.deathId=:deathId"),
        @NamedQuery(name = "get.alt.by.date.period", query = "SELECT da FROM DeathAlteration da WHERE da.dateReceived " +
                " BETWEEN :startDate AND :endDate ORDER BY da.dateReceived desc")
})

public class DeathAlteration {

    public static final int SUDDEN_DEATH = 10;
    public static final int DATE_OF_DEATH = 11;
    public static final int TIME_OF_DEATH = 12;
    public static final int PLACE_OF_DEATH_OFFICIAL = 4;
    public static final int PLACE_OF_DEATH_ENGLISH = 5;
    public static final int CAUSE_OF_DEATH_ESTABLISHED = 6;
    public static final int CAUSE_OF_DEATH = 7;
    public static final int ICD_CODE = 8;
    public static final int BURIAL_PLACE = 9;

    public static final int DEATH_PERSON_PIN = 10;
    public static final int DEATH_PERSON_COUNTRY = 11;
    public static final int DEATH_PERSON_PASSPORT = 12;
    public static final int DEATH_PERSON_AGE = 13;
    public static final int DEATH_PERSON_GENDER = 14;
    public static final int DEATH_PERSON_RACE = 15;
    public static final int DEATH_PERSON_NAME_OFFICIAL = 16;
    public static final int DEATH_PERSON_NAME_ENGLISH = 16;
    public static final int DEATH_PERSON_NAME = 17;
    public static final int DEATH_PERSON_ADDRESS = 18;

    public static final int DEATH_PERSON_FATHER_NAME = 19;
    public static final int DEATH_PERSON_FATHER_PIN = 20;
    public static final int DEATH_PERSON_MOTHER_NAME = 21;
    public static final int DEATH_PERSON_MOTHER_PIN = 22;

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
    private Act act;
    @Id
    // This is an auto generated unique row identifier
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUKey;

    @Column(nullable = false)
    // The date when the alteration request was received
    @Temporal(value = TemporalType.DATE)
    private Date dateReceived;
    // This is the serial number for the BirthAlteration
    @Column(nullable = false, updatable = false)
    private Long alterationSerialNo;
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

    @Embedded
    private DeathAlterationInfo deathInfo = new DeathAlterationInfo();

    @Embedded
    private DeathPersonInfo deathPerson = new DeathPersonInfo();

    @Embedded
    private DeclarantInfo declarant = new DeclarantInfo();

    @Embedded
    private CRSLifeCycleInfo lifeCycleInfo = new CRSLifeCycleInfo();

    /**
     * The Birth/Death registration division where the birth is registered (Includes District)
     */
    @Transient
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

    public BDDivision getDeathDivision() {
        return deathDivision;
    }

    public void setDeathDivision(BDDivision deathDivision) {
        this.deathDivision = deathDivision;
    }
}
