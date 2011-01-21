package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.Location;
import lk.rgd.common.util.NameFormatUtil;

import javax.persistence.*;
import java.util.BitSet;
import java.util.Date;

/**
 * @authar amith jayasekara
 * the entity class to store death alteration recodes
 */
@Entity
@Table(name = "ALT_DEATH", schema = "CRS")
@NamedQueries({
    @NamedQuery(name = "get.alt.by.death.certificate.number", query = "SELECT da FROM DeathAlteration da" +
        " WHERE da.deathRegisterIDUkey =:deathCertificateNumber"),
    @NamedQuery(name = "get.alt.by.division.death.division", query = "SELECT da FROM DeathAlteration da," +
        "DeathRegister dr WHERE da.deathRegisterIDUkey=dr.idUKey " +
        "AND dr.death.deathDivision.bdDivisionUKey =:deathDivisionUkey AND da.lifeCycleInfo.activeRecord = true"),
    @NamedQuery(name = "get.atl.by.death.id", query = "SELECT da FROM DeathAlteration da WHERE da.deathRegisterIDUkey=:deathId"),
    @NamedQuery(name = "get.alt.by.user.location", query = "SELECT da FROM DeathAlteration  da" +
        " WHERE da.submittedLocation.locationUKey =:locationUKey"),
    @NamedQuery(name = "get.alt.by.death.person.pin", query = "SELECT da FROM DeathAlteration  da " +
        "WHERE da.deathPersonPin =:pin"),
    //todo
    @NamedQuery(name = "get.alt.by.seral.number.death.division", query = "SELECT da FROM DeathAlteration da")
})

public class DeathAlteration {

    public static final int SUDDEN_DEATH = 0;
    public static final int DATE_OF_DEATH = 1;
    public static final int TIME_OF_DEATH = 2;
    public static final int PLACE_OF_DEATH = 3;
    public static final int PLACE_OF_DEATH_ENGLISH = 4;
    public static final int CAUSE_OF_DEATH_ESTABLISHED = 5;
    public static final int CAUSE_OF_DEATH = 6;
    public static final int ICD_CODE = 7;
    public static final int BURIAL_PLACE = 8;
    public static final int PIN = 9;
    public static final int COUNTRY = 10;
    public static final int PASSPORT = 11;
    public static final int AGE = 12;
    public static final int GENDER = 13;
    public static final int RACE = 14;
    public static final int NAME = 15;
    public static final int NAME_ENGLISH = 16;
    public static final int ADDRESS = 17;
    public static final int PIN_FATHER = 18;
    public static final int NAME_FATHER = 19;
    public static final int PIN_MOTHER = 20;
    public static final int NAME_MOTHER = 21;

    public enum State {
        /**
         * 0 - A newly entered BDF - can be edited by DEO or ADR
         */
        DATA_ENTRY,
        /**
         * 1 - An ADR or higher partially approved ba
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

    /**
     * Section 52 1 changes for normal deaths
     * a - The event did not occur although recorded [Requires a cancellation of the existing record]
     * b - Has been registered more than once [Requires a cancellation of the existing record]
     * c - Wrong register has been used (e.g. Death register used for a Birth) [Will not occur in the computerized system]
     * d - Wrong registrar / Registration division used (e.g. Reg. Div 1 under DS Div A selected whereas it should have been Reg. Div 2) [Will require a re-registration as the correction ]
     * e - Wrong informant - A person who could should not have been identified as the informant has been specified as the informant - [Will require a re-registration as the correction ]
     * f - A late registration has taken place using the normal form for registration [Will not occur in the computerized system]
     * g - The entry has not been signed (Approved) by the registrar [Will not occur in the computerized system]
     * h & i - Corrections / omissions, Alterations etc.
     * j - Reconstruction of damaged records [Will not occur in the computerized system]
     * <p/>
     * Section 53 changes for sudden deaths
     */
    public enum AlterationType {
        TYPE_52_1_A,
        TYPE_52_1_B,
        TYPE_52_1_D,
        TYPE_52_1_E,
        TYPE_52_1_I,
        TYPE_52_1_H,
        TYPE_53
    }

    @Enumerated
    private State status;

    @Enumerated
    private AlterationType type;

    // This is an auto generated unique row identifier
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUKey;

    //id points to death declaration
    @Column(nullable = false)
    private long deathRegisterIDUkey;

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date dateReceived;
    /**
     * Contains the approval bit set for each field.
     */
    @Column(nullable = true)
    private BitSet approvalStatuses = new BitSet();

    /**
     * contains requested bit set
     */
    @Column(nullable = true)
    private BitSet requestedAlterations = new BitSet();

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
    private String howErrorHappen;

    @Column(name = "deathPersonPinOriginal")
    private String deathPersonPin;

    @ManyToOne
    @JoinColumn(name = "submitedLocationUKey", nullable = false)
    private Location submittedLocation;

    @ManyToOne
    @JoinColumn(name = "deathDivisionUKey", nullable = false)
    private BDDivision deathRecordDivision;

    @Embedded
    private DeathAlterationInfo deathInfo = new DeathAlterationInfo();

    @Embedded
    private DeathPersonInfo deathPerson = new DeathPersonInfo();

    @Embedded
    private DeclarantInfo declarant = new DeclarantInfo();

    @Embedded
    private CRSLifeCycleInfo lifeCycleInfo = new CRSLifeCycleInfo();

    @Transient
    private String deathPersonName;

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

    public long getDeathRegisterIDUkey() {
        return deathRegisterIDUkey;
    }

    public void setDeathRegisterIDUkey(long deathRegisterIDUkey) {
        this.deathRegisterIDUkey = deathRegisterIDUkey;
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

    public AlterationType getType() {
        return type;
    }

    public void setType(AlterationType type) {
        this.type = type;
    }

    public DeathAlterationInfo getDeathInfo() {
        return deathInfo;
    }

    public void setDeathInfo(DeathAlterationInfo deathInfo) {
        this.deathInfo = deathInfo;
    }

    public BDDivision getDeathRecordDivision() {
        return deathRecordDivision;
    }

    public void setDeathRecordDivision(BDDivision deathRecordDivision) {
        this.deathRecordDivision = deathRecordDivision;
    }

    public Location getSubmittedLocation() {
        return submittedLocation;
    }

    public void setSubmittedLocation(Location submittedLocation) {
        this.submittedLocation = submittedLocation;
    }

    public String getDeathPersonPin() {
        return deathPersonPin;
    }

    public void setDeathPersonPin(String deathPersonPin) {
        this.deathPersonPin = deathPersonPin;
    }

    public String getDeathPersonName() {
        return deathPersonName;
    }

    public void setDeathPersonName(String deathPersonName) {
        this.deathPersonName = NameFormatUtil.getDisplayName(deathPersonName, 80);
    }

    public BitSet getRequestedAlterations() {
        return requestedAlterations;
    }

    public void setRequestedAlterations(BitSet requestedAlterations) {
        this.requestedAlterations = requestedAlterations;
    }

    public String getHowErrorHappen() {
        return howErrorHappen;
    }

    public void setHowErrorHappen(String howErrorHappen) {
        this.howErrorHappen = howErrorHappen;
    }

}
