package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
        "AND deathRegister.activeRecord IS TRUE"),

    @NamedQuery(name = "get.by.division.register.date", query = "SELECT deathRegister FROM DeathRegister deathRegister " +
        "WHERE deathRegister.death.deathDivision = :deathDivision AND (deathRegister.death.dateOfRegistration BETWEEN :startDate AND :endDate) " +
        "ORDER BY deathRegister.death.dateOfRegistration desc")
})
public class DeathRegister implements Serializable {

    public enum State {
        DATA_ENTRY, // 0 - A newly entered death registration - can be edited by DEO, ADR

        APPROVED, // 1 - An ADR or higher approved death registration

        REJECTED,  // 2 - An death registration rejected by the ADR

        DEATH_CERTIFICATE_PRINTED, // 3 A certifcate is printed
    }

    public enum Type {
        NORMAL,  // 0 -  A normal death

        SUDDEN,  // 1 - A sudden death

        LATE,   // 2 - A late death registration

        MISSING   // 3 - A death of a missing person
    }

    /**
     * This is an auto generated unique row identifier
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUKey;

    @Column(nullable = false)
    private boolean activeRecord = true;

    @Column(nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date lastUpdatedTime;

    @OneToOne
    @JoinColumn(name = "lastUpdatedUserId")
    private User lastUpdatedUser;

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
        if (deathPerson == null) {
            deathPerson = new DeathPersonInfo();
        }
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
        if (death == null) {
            death = new DeathInfo();
        }
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

    public boolean isActiveRecord() {
        return activeRecord;
    }

    public void setActiveRecord(boolean activeRecord) {
        this.activeRecord = activeRecord;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public User getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    public void setLastUpdatedUser(User lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }
}
