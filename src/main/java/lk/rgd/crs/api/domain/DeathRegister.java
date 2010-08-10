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
    @NamedQuery(name = "death.register.filter.by.status.paginated", query = "SELECT deathRegister FROM DeathRegister deathRegister " +
        "WHERE deathRegister.status = :status " + "ORDER BY deathRegister.death.dateOfRegistration desc"),

    @NamedQuery(name = "getAllDeathRegistrations", query = "SELECT deathRegister FROM DeathRegister deathRegister")/*,

    @NamedQuery(name = "get.by.death.SerailNumber", query = "SELECT deathRegister FROM DeathRegister deathRegister " +
        "WHERE deathRegister.death.deathSerialNo = :deathSerialNo")*/
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

    @Embedded
    private WitnessInfo witness = new WitnessInfo();

    @Column(nullable = false)
    private State status;

    @Column(nullable= false)
    private Type deathType;

    /**
     * The preferred language of for the record
     */
    @Column(nullable = false, columnDefinition = "char(2) default 'si'")
    private String preferredLanguage = "si";

    public State getStatus() {
        return status;
    }

    public void setStatus(State status) {
        this.status = status;
    }

    public WitnessInfo getWitness() {
        if (witness == null) {
            witness = new WitnessInfo();
        }
        return witness;
    }

    public void setWitness(WitnessInfo witness) {
        this.witness = witness;
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

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }
    
    public Type getDeathType() {
        return deathType;
    }

    public void setDeathType(Type deathType) {
        this.deathType = deathType;
    }
}
