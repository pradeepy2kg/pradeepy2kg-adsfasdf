package lk.rgd.crs.api.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * An instance represents information submitted for the declaration of a birth, and the confirmation of changes
 */
@Entity
@Table(name = "BIRTH_REGISTER", schema = "CRS")

@NamedQueries({
    @NamedQuery(name = "filter.by.division.and.status", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.register.birthDivision = :birthDivision AND bdf.register.status = :status " +
        "ORDER BY bdf.register.dateOfRegistration desc"),

    @NamedQuery(name = "filter.by.division.status.and.birthType", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.register.birthDivision = :birthDivision AND bdf.register.status = :status " +
        "AND bdf.register.birthType = :birthType ORDER BY bdf.register.dateOfRegistration desc"),

    @NamedQuery(name = "get.by.division.status.register.date", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.register.birthDivision = :birthDivision AND bdf.register.status = :status " +
        "AND (bdf.register.dateOfRegistration BETWEEN :startDate AND :endDate) " +
        "ORDER BY bdf.register.dateOfRegistration desc"),

    @NamedQuery(name = "get.by.division.status.birthType.register.date", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.register.birthDivision = :birthDivision AND bdf.register.status = :status " +
        "AND bdf.register.birthType = :birthType AND (bdf.register.dateOfRegistration BETWEEN :startDate AND :endDate) " +
        "ORDER BY bdf.register.dateOfRegistration desc"),

    @NamedQuery(name = "get.by.division.status.confirmation.receive.date", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.register.birthDivision = :birthDivision AND bdf.register.status = :status " +
        "AND (bdf.confirmant.confirmationProcessedTimestamp BETWEEN :startDate AND :endDate) " +
        "ORDER BY bdf.confirmant.confirmationProcessedTimestamp desc"),

    @NamedQuery(name = "get.by.bddivision", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.register.birthDivision = :birthDivision " +
        "ORDER BY bdf.register.dateOfRegistration desc"),
    @NamedQuery(name = "get.historical.records.by.bddivision.and.serialNo", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE (bdf.register.birthDivision = :birthDivision AND bdf.register.bdfSerialNo = :bdfSerialNo) " +
        "AND bdf.lifeCycleInfo.activeRecord IS FALSE " +
        "ORDER BY bdf.lifeCycleInfo.lastUpdatedTimestamp desc"),

    @NamedQuery(name = "get.active.by.bddivision.and.serialNo", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.register.birthDivision = :birthDivision AND bdf.register.bdfSerialNo = :bdfSerialNo " +
        "AND bdf.lifeCycleInfo.activeRecord IS TRUE"),

    @NamedQuery(name = "getActiveRecordsBySerialNoAndDSDivision", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.register.bdfSerialNo = :bdfSerialNo " +
        "AND bdf.register.status = :status AND bdf.register.birthDivision.dsDivision = :dsDivision " +
        "AND bdf.lifeCycleInfo.activeRecord IS TRUE"),

    @NamedQuery(name = "get.by.dateOfBirth_range.and.motherNICorPIN", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.child.dateOfBirth BETWEEN :start AND :end AND bdf.parent.motherNICorPIN = :motherNICorPIN "),

    @NamedQuery(name = "filter.by.unconfirmed.by.register.date", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.register.status = 2 " +
        "AND bdf.register.confirmationPrintTimestamp < :date"),

    @NamedQuery(name = "filter.by.dsdivision.and.status", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.register.birthDivision.dsDivision = :dsDivision AND bdf.register.status = :status " +
        "ORDER BY bdf.register.dateOfRegistration desc"),

    @NamedQuery(name = "filter.by.district.and.status", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.register.birthDivision.dsDivision.district = :district AND bdf.register.status = :status " +
        "ORDER BY bdf.register.dateOfRegistration desc"),

    @NamedQuery(name = "get.by.dsdivision.and.status", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.register.birthDivision.dsDivision = :dsDivision AND bdf.register.status = :status " +
        "AND bdf.child.dateOfBirth BETWEEN :start AND :end"),

    @NamedQuery(name = "filter.by.dsdivision.status.and.birthType", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.register.birthDivision.dsDivision = :dsDivision AND bdf.register.status = :status " +
        "AND bdf.register.birthType = :birthType ORDER BY bdf.register.dateOfRegistration desc"),

    @NamedQuery(name = "get.by.dsdivision.status.register.date", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.register.birthDivision.dsDivision = :dsDivision AND bdf.register.status = :status " +
        "AND (bdf.register.dateOfRegistration BETWEEN :startDate AND :endDate) " +
        "ORDER BY bdf.register.dateOfRegistration desc"),

    @NamedQuery(name = "get.by.dsdivision.status.birthType.register.date", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.register.birthDivision.dsDivision = :dsDivision AND bdf.register.status = :status " +
        "AND bdf.register.birthType = :birthType AND (bdf.register.dateOfRegistration BETWEEN :startDate AND :endDate) " +
        "ORDER BY bdf.register.dateOfRegistration desc"),

    @NamedQuery(name = "get.by.dsdivision.status.confirmation.receive.date", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.register.birthDivision.dsDivision = :dsDivision AND bdf.register.status = :status " +
        "AND (bdf.confirmant.confirmationProcessedTimestamp BETWEEN :startDate AND :endDate) " +
        "ORDER BY bdf.confirmant.confirmationProcessedTimestamp desc"),

    @NamedQuery(name = "findAll", query = "SELECT bdf FROM BirthDeclaration bdf"),

    @NamedQuery(name = "get.by.dsdivision", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.register.birthDivision.dsDivision = :dsDivision " +
        "ORDER BY bdf.register.dateOfRegistration desc"),

    @NamedQuery(name = "get.by.district", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.register.birthDivision.dsDivision.district = :district " +
        "ORDER BY bdf.register.dateOfRegistration desc"),

    @NamedQuery(name = "get.by.NicOrPin", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.child.pin = :PINorNIC AND bdf.lifeCycleInfo.activeRecord=true "),
    @NamedQuery(name = "get.historical.alteration.records.by.bdDivision.and.serialNo", query = "SELECT bdf FROM BirthDeclaration bdf" +
        " WHERE( bdf.register.status= 10 AND bdf.register.birthDivision.bdDivisionUKey =:birthDivision " +
        " AND bdf.register.bdfSerialNo =:bdfSerialNo AND bdf.idUKey < :idUKey AND bdf.lifeCycleInfo.activeRecord IS FALSE )" +
        " ORDER BY bdf.register.dateOfRegistration desc"),
    @NamedQuery(name = "get.bc.count", query = "SELECT COUNT(bdf) FROM BirthDeclaration bdf" +
        " WHERE bdf.register.status =:status AND (bdf.lifeCycleInfo.createdTimestamp BETWEEN :startDate AND :endDate)"),
    @NamedQuery(name = "get.bc.by.createdUser", query = "SELECT bdf FROM BirthDeclaration bdf " +
        " WHERE bdf.lifeCycleInfo.createdUser =:user AND (bdf.lifeCycleInfo.createdTimestamp BETWEEN :startDate AND :endDate)"),
    @NamedQuery(name = "get.by.serial.and.ds.id", query = "SELECT bdf FROM BirthDeclaration bdf" +
        " WHERE bdf.register.bdfSerialNo =:serial AND bdf.register.birthDivision.dsDivision.dsDivisionUKey =:dsId " +
        "AND bdf.lifeCycleInfo.activeRecord IS TRUE "),
    @NamedQuery(name = "get.bdf.by.mother", query = "SELECT bdf FROM BirthDeclaration bdf WHERE " +
        "(bdf.parent.motherNICorPIN =:mother AND bdf.register.birthType =:type AND bdf.lifeCycleInfo.activeRecord IS TRUE) "),

    @NamedQuery(name = "get.bdf.by.division.registrarPinOrNic", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.register.birthDivision.bdDivisionUKey = :birthDivision " +
        "AND (bdf.notifyingAuthority.notifyingAuthorityPIN = :registrarPin OR bdf.notifyingAuthority.notifyingAuthorityPIN = :registrarNic)"),

    @NamedQuery(name = "count.birth.dsDivision.usage", query = "SELECT COUNT(bdf) FROM BirthDeclaration bdf " +
        "WHERE (bdf.register.birthDivision.dsDivision.dsDivisionUKey = :dsDivisionId OR bdf.parent.motherDSDivision.dsDivisionUKey = :dsDivisionId) " +
        "AND bdf.register.status <> 0"),
    @NamedQuery(name = "count.birth.gnDivision.usage", query = "SELECT COUNT(bdf) FROM BirthDeclaration bdf " +
        "WHERE bdf.parent.motherGNDivision.gnDivisionUKey = :gnDivisionId AND bdf.register.status <> 0"),
    @NamedQuery(name = "count.birth.bdDivision.usage", query = "SELECT COUNT(bdf) FROM BirthDeclaration bdf " +
        "WHERE bdf.register.birthDivision.bdDivisionUKey = :bdDivisionId AND bdf.register.status <> 0"),
    @NamedQuery(name = "count.birth.location.usage", query = "SELECT COUNT(bdf) FROM BirthDeclaration bdf " +
        "WHERE bdf.register.originalBCPlaceOfIssue.locationUKey = :locationId AND bdf.register.status <> 0"),
    @NamedQuery(
        name = "get.bc.by.createdUser.dsDivision",
        query = "SELECT bdf FROM BirthDeclaration bdf WHERE bdf.lifeCycleInfo.createdUser =:user AND (bdf.lifeCycleInfo.createdTimestamp BETWEEN :startDate AND :endDate) AND bdf.register.birthDivision.dsDivision.dsDivisionUKey = :dsDivisionId"
    ),
    @NamedQuery(
        name = "get.bc.by.createdUser.district",
        query = "SELECT bdf FROM BirthDeclaration bdf WHERE bdf.lifeCycleInfo.createdUser =:user AND (bdf.lifeCycleInfo.createdTimestamp BETWEEN :startDate AND :endDate) AND bdf.register.birthDivision.dsDivision.district.districtUKey = :districtId"
    ),
    @NamedQuery(
        name = "getAllRejectedBirthsByUser",
        query = "SELECT bdf FROM BirthDeclaration bdf WHERE bdf.lifeCycleInfo.lastUpdatedUser =:user AND bdf.register.status = 7"
    ),
    @NamedQuery(
        name = "getAllRejectedBirthsByDistrict",
        query = "SELECT bdf FROM BirthDeclaration bdf WHERE bdf.register.birthDivision.dsDivision.district = :district AND bdf.register.status = 7"
    ),
    @NamedQuery(
        name = "getAllRejectedBirthsByDSDivision",
        query = "SELECT bdf FROM BirthDeclaration bdf WHERE bdf.register.birthDivision.dsDivision = :dsDivision AND bdf.register.status = 7"
    ),
    @NamedQuery(name = "get.by.division.status.entered.date", query = "SELECT bdf FROM BirthDeclaration bdf " +
        "WHERE bdf.register.birthDivision = :birthDivision AND bdf.register.status = :status " +
        "AND (bdf.lifeCycleInfo.createdTimestamp BETWEEN :startDate AND :endDate) " +
        "ORDER BY bdf.register.bdfSerialNo asc")
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BirthDeclaration implements Serializable, Cloneable {

    /**
     * The Enumeration defining the state of the record. Any change of position of an element will have a serious
     * impact on any existing records
     */
    public enum State {
        /**
         * 0 - A newly entered BDF - can be edited by DEO or ADR
         */
        DATA_ENTRY,
        /**
         * 1 - An ADR or higher approved BDF - ready for confirmation or auto BC generation
         */
        APPROVED,
        /**
         * 2 - A BDF for which the parent confirmation form was printed
         */
        CONFIRMATION_PRINTED,
        /**
         * 3 - A BDF with no changes during confirmation by parents
         */
        CONFIRMED_WITHOUT_CHANGES,
        /**
         * 4 - A BDF archived due to corrections by parents. This is the State of the old
         * record. A new record is added with State as CONFIRMATION_CHANGES_CAPTURED
         */
        ARCHIVED_CORRECTED,
        /**
         * 5 - A BDF for which the parent c
         */
        CONFIRMATION_CHANGES_CAPTURED,
        /**
         * 6 - A BDF for which the parent confirmation changes has been approved
         */
        CONFIRMATION_CHANGES_APPROVED,
        /**
         * 7 - A BDF rejected [e.g. as a duplicate] at any stage during processing
         */
        ARCHIVED_REJECTED,

        // Normal states after initial BC printing
        /**
         * 8 - A BDF for which a PIN is generated
         */
        ARCHIVED_CERT_GENERATED,
        /**
         * 9 - A BDF for which the BC is printed
         */
        ARCHIVED_CERT_PRINTED,
        /**
         * 10 - A BDF archived after an alteration is performed after initial BC. New
         * record is captured as ARCHIVED_BC_GENERATED
         */
        ARCHIVED_ALTERED,
        /**
         * 11 - A BDF that should not have been captured by the system (e.g. birth did not happen, or a the double
         * registration of a birth etc) will cause the record to be cancelled. This state marks a record as such
         */
        ARCHIVED_CANCELLED
    }

    /**
     * The Enumeration defining the type of the birth declaration.
     */
    public enum BirthType {
        /**
         * 0 - still birth
         */
        STILL,
        /**
         * 1 - live birth
         */
        LIVE,
        /**
         * 2 - child adoption
         */
        ADOPTION,
        /**
         * 3 - belated birth
         */
        BELATED
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

    public BirthDeclaration clone() throws CloneNotSupportedException {
        BirthDeclaration newBDF = (BirthDeclaration) super.clone();

        lifeCycleInfo.getApprovalOrRejectUser().getUserName();
        lifeCycleInfo.getCertificateGeneratedUser().getUserName();
        lifeCycleInfo.getCreatedUser().getUserName();
        lifeCycleInfo.getLastUpdatedUser().getUserName();

        newBDF.setIdUKey(0);
        newBDF.setLifeCycleInfo(lifeCycleInfo.clone());
        newBDF.setRegister(register.clone());
        newBDF.setChild(child.clone());
        if (parent != null) {
            newBDF.setParent(parent.clone());
        }
        if (marriage != null) {
            newBDF.setMarriage(marriage.clone());
        }
        if (grandFather != null) {
            newBDF.setGrandFather(grandFather.clone());
        }
        newBDF.setNotifyingAuthority(notifyingAuthority.clone());
        newBDF.setInformant(informant.clone());
        if (confirmant != null) {
            newBDF.setConfirmant(confirmant.clone());
        }
        return newBDF;
    }

    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public ChildInfo getChild() {
        if (child == null) {
            child = new ChildInfo();
        }
        return child;
    }

    public void setChild(ChildInfo child) {
        this.child = child;
    }

    public ParentInfo getParent() {
        if (parent == null) {
            parent = new ParentInfo();
        }
        return parent;
    }

    public void setParent(ParentInfo parent) {
        this.parent = parent;
    }

    public MarriageInfo getMarriage() {
        if (marriage == null) {
            marriage = new MarriageInfo();
        }
        return marriage;
    }

    public void setMarriage(MarriageInfo marriage) {
        this.marriage = marriage;
    }

    public GrandFatherInfo getGrandFather() {
        if (grandFather == null) {
            grandFather = new GrandFatherInfo();
        }
        return grandFather;
    }

    public void setGrandFather(GrandFatherInfo grandFather) {
        this.grandFather = grandFather;
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

    public InformantInfo getInformant() {
        if (informant == null) {
            informant = new InformantInfo();
        }
        return informant;
    }

    public void setInformant(InformantInfo informant) {
        this.informant = informant;
    }

    public ConfirmantInfo getConfirmant() {
        if (confirmant == null) {
            confirmant = new ConfirmantInfo();
        }
        return confirmant;
    }

    public void setConfirmant(ConfirmantInfo confirmant) {
        this.confirmant = confirmant;
    }

    public BirthRegisterInfo getRegister() {
        if (register == null) {
            register = new BirthRegisterInfo();
        }
        return register;
    }

    public void setRegister(BirthRegisterInfo register) {
        this.register = register;
    }

    public CRSLifeCycleInfo getLifeCycleInfo() {
        if (lifeCycleInfo == null) {
            return new CRSLifeCycleInfo();
        }
        return lifeCycleInfo;
    }

    public void setLifeCycleInfo(CRSLifeCycleInfo lifeCycleInfo) {
        this.lifeCycleInfo = lifeCycleInfo;
    }
}
