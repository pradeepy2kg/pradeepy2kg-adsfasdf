package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.User;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Indunil Moremada
 *         An instance represents information submitted for a death
 */
@Entity
@Table(name = "DEATH_REGISTER", schema = "CRS")

@NamedQueries({
    @NamedQuery(name = "death.register.filter.by.and.deathDivision.status.paginated", query = "SELECT deathRegister FROM DeathRegister deathRegister " +
        "WHERE deathRegister.status = :status AND deathRegister.death.deathDivision = :deathDivision " + "ORDER BY deathRegister.death.dateOfRegistration desc"),

    @NamedQuery(name = "get.all.deaths.by.deathDivision", query = "SELECT deathRegister FROM DeathRegister deathRegister WHERE " +
        "deathRegister.death.deathDivision = :deathDivision"),

    @NamedQuery(name = "get.active.by.bddivision.and.deathSerialNo", query = "SELECT deathRegister FROM DeathRegister deathRegister " +
        "WHERE deathRegister.death.deathSerialNo = :deathSerialNo AND deathRegister.death.deathDivision = :deathDivision " +
        "AND deathRegister.lifeCycleInfo.activeRecord IS TRUE"),

    @NamedQuery(name = "get.by.division.register.date", query = "SELECT deathRegister FROM DeathRegister deathRegister " +
        "WHERE deathRegister.death.deathDivision = :deathDivision AND (deathRegister.death.dateOfRegistration BETWEEN :startDate AND :endDate) " +
        "ORDER BY deathRegister.death.dateOfRegistration desc"),

    @NamedQuery(name = "death.register.filter.by.and.dsDivision.status.paginated", query = "SELECT deathRegister FROM DeathRegister deathRegister " +
        "WHERE deathRegister.status = :status AND deathRegister.death.deathDivision.dsDivision = :dsDivision " +
        "AND deathRegister.lifeCycleInfo.activeRecord IS TRUE ORDER BY deathRegister.death.dateOfRegistration desc"),

    @NamedQuery(name = "get.all.deaths.by.dsDivision", query = "SELECT deathRegister FROM DeathRegister deathRegister WHERE " +
        "deathRegister.death.deathDivision.dsDivision = :dsDivision AND deathRegister.lifeCycleInfo.activeRecord IS TRUE " +
        " ORDER BY deathRegister.death.dateOfRegistration desc"),

    @NamedQuery(name = "get.all.deaths.by.deathPersonPIN", query = "SELECT deathRegister FROM DeathRegister deathRegister WHERE " +
        "deathRegister.deathPerson.deathPersonPINorNIC = :pinOrNIC"),

    @NamedQuery(name = "get.historical.death.records.by.bddivision.and.serialNo.deathID", query = "SELECT dr FROM DeathRegister dr " +
        "WHERE (dr.death.deathDivision = :deathDivision AND dr.death.deathSerialNo = :serialNo  AND dr.idUKey < :deathId AND dr.status= 4) " +
        "AND dr.lifeCycleInfo.activeRecord IS FALSE ORDER BY dr.lifeCycleInfo.lastUpdatedTimestamp desc"),

    @NamedQuery(name = "findAllDeaths", query = "SELECT ddf FROM DeathRegister ddf"),

    @NamedQuery(name = "get.dr.count", query = "SELECT COUNT(dr) FROM DeathRegister dr " +
        "WHERE dr.status =:status AND (dr.lifeCycleInfo.createdTimestamp BETWEEN :startDate AND :endDate)"),
    @NamedQuery(name = "get.dr.by.createdUser", query = "SELECT dr FROM DeathRegister dr " +
        " WHERE dr.lifeCycleInfo.createdUser =:user AND (dr.lifeCycleInfo.createdTimestamp BETWEEN :startDate AND :endDate)")
})
public class DeathRegister implements Serializable, Cloneable {

    public enum State {
        DATA_ENTRY, // 0 - A newly entered death registration - can be edited by DEO, ADR

        APPROVED, // 1 - An ADR or higher approved death registration

        REJECTED,  // 2 - An death registration rejected by the ADR

        ARCHIVED_CERT_GENERATED, // 3 A certificate is printed

        ARCHIVED_ALTERED,// 4 record is archived after an alteration

        ARCHIVED_CANCELLED // 5 cancelled due to a duplication or registration of an event that did not occur
    }

    public enum Type {
        NORMAL,  // 0 -  A normal death

        SUDDEN,  // 1 - A sudden death

        LATE_NORMAL,   // 2 - A late death registration for a normal death

        LATE_SUDDEN,   // 3 - A late death registration for a sudden death

        MISSING   // 4 - A death of a missing person
    }

    /**
     * This is an auto generated unique row identifier
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUKey;

    @Embedded
    private CRSLifeCycleInfo lifeCycleInfo = new CRSLifeCycleInfo();

    @Embedded
    private DeathInfo death = new DeathInfo();

    @Embedded
    private DeathPersonInfo deathPerson = new DeathPersonInfo();

    @Embedded
    private NotifyingAuthorityInfo notifyingAuthority = new NotifyingAuthorityInfo();

    @Embedded
    private DeclarantInfo declarant = new DeclarantInfo();

    @Enumerated
    @Column(nullable = false)
    private State status;

    @Enumerated
    private Type deathType;

    // comment for rejecting death declaration
    @Column(nullable = true, length = 100, name = "COMMENT")
    private String comment;

    /**
     * The place of issue for the original death certificate - free copy (Stores the Location ID)
     */
    @OneToOne
    @JoinColumn(name = "originalDCPIssuelocationId", nullable = true)
    private Location originalDCPlaceOfIssue;

    /**
     * The original DC issued user
     */
    @OneToOne
    @JoinColumn(name = "originalDCIssueUserId", nullable = true)
    private User originalDCIssueUser;


    /**
     * The original DC place of issue as a String in the preferred language and english - e.g. කොළඹ / Colombo
     */
    @Transient
    private String originalDCPlaceOfIssuePrint;

    /**
     * The original DC place of issue signature as a String in preferred language and english
     * e.g. ප්‍රාදේශීය ලේකම් කොට්ටාශ කාර්යාලය / Divisional Secretariat
     */
    @Transient
    private String originalDCPlaceOfIssueSignPrint;

    /**
     * The original BC issued user signature as a String in preferred language and english
     */
    @Transient
    private String originalDCIssueUserSignPrint;


    public DeathRegister clone() throws CloneNotSupportedException {
        DeathRegister dr = (DeathRegister) super.clone();
        dr.setIdUKey(0);
        dr.setLifeCycleInfo(lifeCycleInfo.clone());
        dr.setDeath(death.clone());
        if (deathPerson != null) {
            dr.setDeathPerson(deathPerson.clone());
        }
        dr.setNotifyingAuthority(notifyingAuthority.clone());
        dr.setDeclarant(declarant.clone());
        return dr;
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

    public DeclarantInfo getDeclarant() {
        if (declarant == null) {
            declarant = new DeclarantInfo();
        }
        return declarant;
    }

    public void setDeclarant(DeclarantInfo declarant) {
        this.declarant = declarant;
    }

    public NotifyingAuthorityInfo getNotifyingAuthority() {
        if (notifyingAuthority == null) {
            notifyingAuthority = new NotifyingAuthorityInfo();
        }
        return notifyingAuthority;
    }

    public void setNotifyingAuthority(NotifyingAuthorityInfo notifyingAuthority) {
        this.notifyingAuthority = notifyingAuthority;
    }

    public DeathPersonInfo getDeathPerson() {
        return deathPerson;
    }

    public void setDeathPerson(DeathPersonInfo deathPerson) {
        this.deathPerson = deathPerson;
    }

    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public DeathInfo getDeath() {
        return death;
    }

    public void setDeath(DeathInfo death) {
        this.death = death;
    }

    public Type getDeathType() {
        return deathType;
    }

    public void setDeathType(Type deathType) {
        this.deathType = deathType;
    }

    public String getCommnet() {
        return comment;
    }

    public void setCommnet(String comment) {
        this.comment = comment;
    }

    public Location getOriginalDCPlaceOfIssue() {
        return originalDCPlaceOfIssue;
    }

    public void setOriginalDCPlaceOfIssue(Location originalDCPlaceOfIssue) {
        this.originalDCPlaceOfIssue = originalDCPlaceOfIssue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getOriginalDCIssueUser() {
        return originalDCIssueUser;
    }

    public void setOriginalDCIssueUser(User originalDCIssueUser) {
        this.originalDCIssueUser = originalDCIssueUser;
    }

    public String getOriginalDCPlaceOfIssuePrint() {
        return originalDCPlaceOfIssuePrint;
    }

    public void setOriginalDCPlaceOfIssuePrint(String originalDCPlaceOfIssuePrint) {
        this.originalDCPlaceOfIssuePrint = originalDCPlaceOfIssuePrint;
    }

    public String getOriginalDCPlaceOfIssueSignPrint() {
        return originalDCPlaceOfIssueSignPrint;
    }

    public void setOriginalDCPlaceOfIssueSignPrint(String originalDCPlaceOfIssueSignPrint) {
        this.originalDCPlaceOfIssueSignPrint = originalDCPlaceOfIssueSignPrint;
    }

    public String getOriginalDCIssueUserSignPrint() {
        return originalDCIssueUserSignPrint;
    }

    public void setOriginalDCIssueUserSignPrint(String originalDCIssueUserSignPrint) {
        this.originalDCIssueUserSignPrint = originalDCIssueUserSignPrint;
    }
}
