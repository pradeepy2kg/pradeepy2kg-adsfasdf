package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.Location;
import lk.rgd.common.util.WebUtils;
import lk.rgd.common.util.NameFormatUtil;

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
        @NamedQuery(name = "get.alt.by.death.certificate.number", query = "SELECT da FROM DeathAlteration da" +
                " WHERE da.deathId =:deathCertificateNumber"),
        @NamedQuery(name = "get.alt.by.division.death.division", query = "SELECT da FROM DeathAlteration da," +
                "DeathRegister dr WHERE da.deathId=dr.idUKey AND dr.death.deathDivision.bdDivisionUKey =:deathDivisionUkey"),
        @NamedQuery(name = "get.atl.by.death.id", query = "SELECT da FROM DeathAlteration da WHERE da.deathId=:deathId"),
        @NamedQuery(name = "get.alt.by.user.location", query = "SELECT da FROM DeathAlteration  da" +
                " WHERE da.submittedLocation.locationUKey =:locationUKey"),
        @NamedQuery(name = "get.alt.by.death.person.pin", query = "SELECT da FROM DeathAlteration  da " +
                "WHERE da.deathPersonPin =:pin"),
        //todo
        @NamedQuery(name = "get.alt.by.seral.number.death.division", query = "SELECT da FROM DeathAlteration da")
})

public class DeathAlteration {

    public static final Map<Integer, String> indexMap = new HashMap<Integer, String>();

    public static final int SUDDEN_DEATH = 1;
    public static final int DATE_OF_DEATH = 2;
    public static final int TIME_OF_DEATH = 3;
    public static final int PLACE_OF_DEATH = 4;
    public static final int PLACE_OF_DEATH_ENGLISH = 5;
    public static final int CAUSE_OF_DEATH_ESTABLISHED = 6;
    public static final int CAUSE_OF_DEATH = 7;
    public static final int ICD_CODE = 8;
    public static final int BURIAL_PLACE = 9;
    public static final int PIN = 10;
    public static final int COUNTRY = 11;
    public static final int PASSPORT = 12;
    public static final int AGE = 13;
    public static final int GENDER = 14;
    public static final int RACE = 15;
    public static final int NAME = 16;
    public static final int NAME_ENGLISH = 17;
    public static final int ADDRESS = 18;
    public static final int PIN_FATHER = 19;
    public static final int NAME_FATHER = 20;
    public static final int PIN_MOTHER = 21;
    public static final int NAME_MOTHER = 22;

    static {
        indexMap.put(SUDDEN_DEATH, "field.sudden.death");
        indexMap.put(DATE_OF_DEATH, "field.date.death");
        indexMap.put(TIME_OF_DEATH, "field.time.death");
        indexMap.put(PLACE_OF_DEATH, "field.place.death");
        indexMap.put(PLACE_OF_DEATH_ENGLISH, "field.place.death.english");
        indexMap.put(CAUSE_OF_DEATH_ESTABLISHED, "field.cause.death.established");
        indexMap.put(CAUSE_OF_DEATH, "field.cause.death");
        indexMap.put(ICD_CODE, "field.icd.code");
        indexMap.put(BURIAL_PLACE, "field.burial.place");

        indexMap.put(PIN, "field.pin.number");
        indexMap.put(COUNTRY, "field.country");
        indexMap.put(PASSPORT, "field.passPort");
        indexMap.put(AGE, "field.age");
        indexMap.put(GENDER, "field.gender");
        indexMap.put(RACE, "field.race");
        indexMap.put(NAME, "field.name");
        indexMap.put(NAME_ENGLISH, "field.name.english");
        indexMap.put(ADDRESS, "field.address");
        indexMap.put(PIN_FATHER, "field.pin.father");
        indexMap.put(NAME_FATHER, "field.name.father");
        indexMap.put(PIN_MOTHER, "field.pin.mother");
        indexMap.put(NAME_MOTHER, "field.name.mother");
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

    public enum AlterationType {
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

    @Column(name = "deathPersonPinOriginal")
    private String deathPersonPin;

    @ManyToOne
    @JoinColumn(name = "submitedLocationUKey", nullable = false)
    private Location submittedLocation;

    @ManyToOne
    @JoinColumn(name = "deathDivisionUKey", nullable = false)
    private BDDivision deathRecodDivision;

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

    public BDDivision getDeathRecodDivision() {
        return deathRecodDivision;
    }

    public void setDeathRecodDivision(BDDivision deathRecodDivision) {
        this.deathRecodDivision = deathRecodDivision;
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
}
