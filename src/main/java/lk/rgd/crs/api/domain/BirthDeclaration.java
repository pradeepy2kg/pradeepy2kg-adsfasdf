package lk.rgd.crs.api.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * An instance represents information submitted for the declaration of a birth, and the confirmation of changes
 */
@Entity
@Table(name = "BIRTH_REGISTER", schema = "CRS",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"birthDistrict", "birthDivision", "bdfSerialNo", "status"})})

@NamedQueries({
        @NamedQuery(name = "filter.by.division.and.status", query =
                "SELECT bdf FROM BirthDeclaration bdf " +
                        "WHERE bdf.child.birthDivision = :birthDivision AND bdf.child.status = :status " +
                        "ORDER BY bdf.child.dateOfRegistration desc"),
        @NamedQuery(name = "confirmation.pending.approval.expired", query =
                "SELECT bdf FROM BirthDeclaration bdf " +
                        "WHERE bdf.child.birthDivision = :birthDivision AND bdf.child.status = 2 " +
                        "AND bdf.confirmant.lastDateForConfirmation < :today " +
                        "ORDER BY bdf.child.dateOfRegistration desc"),
        @NamedQuery(name = "confirmation.pending.approval", query =
                "SELECT bdf FROM BirthDeclaration bdf " +
                        "WHERE bdf.child.birthDivision = :birthDivision AND bdf.child.status = 5 " +
                        "ORDER BY bdf.confirmant.confirmationReceiveDate desc")
})
public class BirthDeclaration {
    /** This is an auto generated unique row identifier  */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** Status comment - e.g. reason for rejection due to duplicate  */
    @Column(nullable = true)
    private String comments;

    /** The date of issue for the original birth certificate - free copy */
    @Column(nullable = true, updatable = false)
    @Temporal (value = TemporalType.DATE)
    private Date originalBCDateOfIssue;

    /** The place of issue for the original birth certificate - free copy (Stores the DS Division ID) */
    @Column(nullable = true, updatable = false)
    private int originalBCPlaceOfIssue;

    @Embedded
    private ChildInfo child = new ChildInfo();

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

    public Date getOriginalBCDateOfIssue() {
        return originalBCDateOfIssue;
    }

    public void setOriginalBCDateOfIssue(Date originalBCDateOfIssue) {
        this.originalBCDateOfIssue = originalBCDateOfIssue;
    }

    public int getOriginalBCPlaceOfIssue() {
        return originalBCPlaceOfIssue;
    }

    public void setOriginalBCPlaceOfIssue(int originalBCPlaceOfIssue) {
        this.originalBCPlaceOfIssue = originalBCPlaceOfIssue;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
}
