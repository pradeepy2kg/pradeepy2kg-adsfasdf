package lk.rgd.crs.api.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * An instance represents information submitted for the declaration of a birth, and the confirmation of changes
 */
@Entity
@Table(name = "BIRTH_REGISTER", schema = "CRS",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"bdDivisionUKey", "bdfSerialNo", "status"})})

@NamedQueries({
        @NamedQuery(name = "filter.by.division.and.status", query =
                "SELECT bdf FROM BirthDeclaration bdf " +
                        "WHERE bdf.register.birthDivision = :birthDivision AND bdf.register.status = :status " +
                        "ORDER BY bdf.register.dateOfRegistration desc"),
        @NamedQuery(name = "confirmation.pending.approval.expired", query =
                "SELECT bdf FROM BirthDeclaration bdf " +
                        "WHERE bdf.register.birthDivision = :birthDivision AND bdf.register.status = 2 " +
                        "AND bdf.confirmant.lastDateForConfirmation < :today " +
                        "ORDER BY bdf.register.dateOfRegistration desc"),
        @NamedQuery(name = "confirmation.pending.approval", query =
                "SELECT bdf FROM BirthDeclaration bdf " +
                        "WHERE bdf.register.birthDivision = :birthDivision AND bdf.register.status = 5 " +
                        "ORDER BY bdf.confirmant.confirmationReceiveDate desc"),
        @NamedQuery(name = "get.by.id.pending.approval", query =
                "SELECT bdf FROM BirthDeclaration bdf " +
                        "WHERE bdf.idUKey = :bdfidUKey AND bdf.register.status = 5 "),
        @NamedQuery(name = "get.by.serialNo.pending.approval", query =
                "SELECT bdf FROM BirthDeclaration bdf " +
                        "WHERE bdf.register.bdfSerialNo = :bdfSerialNo AND bdf.register.status = 5 ")
})
public class BirthDeclaration implements Serializable {
    /** This is an auto generated unique row identifier  */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUKey;

    @Embedded
    private ChildInfo child = new ChildInfo();

    @Embedded
    private BirthRegisterInfo register = new BirthRegisterInfo();

    @Embedded
    private ParentInfo parent = new ParentInfo();

    @Embedded
    private MarriageInfo marriage = new MarriageInfo();

    @Embedded
    private GrandFatherInfo grandFather = new GrandFatherInfo();

    @Embedded
    private NotifyingAuthorityInfo notifyingAuthority = new NotifyingAuthorityInfo();

    @Embedded
    private InformantInfo informant = new InformantInfo();

    @Embedded
    private ConfirmantInfo confirmant = new ConfirmantInfo();

    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public ChildInfo getChild() {
        return child;
    }

    public void setChild(ChildInfo child) {
        this.child = child;
    }

    public ParentInfo getParent() {
        return parent;
    }

    public void setParent(ParentInfo parent) {
        this.parent = parent;
    }

    public MarriageInfo getMarriage() {
        return marriage;
    }

    public void setMarriage(MarriageInfo marriage) {
        this.marriage = marriage;
    }

    public GrandFatherInfo getGrandFather() {
        return grandFather;
    }

    public void setGrandFather(GrandFatherInfo grandFather) {
        this.grandFather = grandFather;
    }

    public NotifyingAuthorityInfo getNotifyingAuthority() {
        return notifyingAuthority;
    }

    public void setNotifyingAuthority(NotifyingAuthorityInfo notifyingAuthority) {
        this.notifyingAuthority = notifyingAuthority;
    }

    public InformantInfo getInformant() {
        return informant;
    }

    public void setInformant(InformantInfo informant) {
        this.informant = informant;
    }

    public ConfirmantInfo getConfirmant() {
        return confirmant;
    }

    public void setConfirmant(ConfirmantInfo confirmant) {
        this.confirmant = confirmant;
    }

    public BirthRegisterInfo getRegister() {
        return register;
    }

    public void setRegister(BirthRegisterInfo register) {
        this.register = register;
    }
}
