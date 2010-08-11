package lk.rgd.crs.api.domain;

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

    @NamedQuery(name = "get.all.deaths.by.deathDivision", query = "SELECT deathRegister FROM DeathRegister deathRegister WHERE "+
    "deathRegister.death.deathDivision = :deathDivision"),

    @NamedQuery(name = "get.by.bddivision.and.deathSerialNo", query = "SELECT deathRegister FROM DeathRegister deathRegister " +
        "WHERE deathRegister.death.deathSerialNo = :deathSerialNo AND deathRegister.death.deathDivision = :deathDivision")
})
public class DeathRegister implements Serializable {

    public enum State {
        DATA_ENTRY, // 0 - A newly entered death registration - can be edited by DEO, ADR

        APPROVED, // 1 - An ARG or higher approved death registration

        REJECTED,  // 2 - An death registration rejected by the ARG

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

    @Embedded
    private DeathInfo death = new DeathInfo();

    @Embedded
    private DeathPersonInfo deathPerson = new DeathPersonInfo();

    @Embedded
    private NotifyingAuthorityInfo notifyingAuthority = new NotifyingAuthorityInfo();

    @Embedded
    private DeclarantInfo declarant = new DeclarantInfo();

    @Column(nullable = false)
    private State status;

    @Column(nullable= false)
    private Type deathType;

    public String getReasonForLateRegistration() {
        return reasonForLateRegistration;
    }

    public void setReasonForLateRegistration(String reasonForLateRegistration) {
        this.reasonForLateRegistration = reasonForLateRegistration;
    }

    @Column(nullable = true)
    private String reasonForLateRegistration;


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
}
