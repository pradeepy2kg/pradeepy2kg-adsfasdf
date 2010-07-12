package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;

import javax.persistence.*;
import java.util.Date;

/**
 * An instance representing birth registration process specific official information
 * and statuses for a given birth registration/declaration.
 */
@Embeddable
public class BirthRegisterInfo {
    /**
     * The preferred language of for the record
     */
    @Column(nullable = false, columnDefinition = "char(2) default 'si'")
    private String preferredLanguage = "si";

    /**
     * This is the serial number captured from the BDF
     */
    @Column(nullable = false, updatable = false)
    private Long bdfSerialNo;

    /**
     * The Birth/Death registration division where the birth is registered (Includes District)
     */
    @ManyToOne
    @JoinColumn(name = "bdDivisionUKey", nullable = false)
    private BDDivision birthDivision;

    /**
     * The name of the Birth/Death registration division in the preferred language
     */
    @Transient
    private String bdDivisionPrint;

    /**
     * The name of the DS division in the preferred language
     */
    @Transient
    private String dsDivisionPrint;

    /**
     * The name of the District in the preferred language
     */
    @Transient
    private String districtPrint;

    /**
     * The date when the birth declaration was submitted to the medical registrar or the DS office
     */
    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfRegistration;

    /**
     * @see lk.rgd.crs.api.domain.BirthDeclaration.State
     */
    @Enumerated
    private BirthDeclaration.State status;

    /** The ADR or higher approving the record  */
    @OneToOne
    @JoinColumn(name = "approveUser")
    private User approveUser;

    /** The timestamp when an ADR or higher approves the record  */
    @Column(nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date approveDate;

    /** The user printing the confirmation */
    @OneToOne
    @JoinColumn(name = "confirmationPrintUser")
    private User confirmationPrintUser;

    /** The timestamp when confirmation is printed for this record */
    @Column(nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date confirmationPrintDate;

    @Transient
    private Date lastDayForConfirmation;

    /**
     * Status comment - e.g. reason for rejection due to duplicate
     */
    @Lob
    @Column(nullable = true, length = 4096)
    private String comments;

    /** The user printing the original BC */
    @OneToOne
    @JoinColumn(name = "originalBCPrintUser")
    private User originalBCPrintUser;

    /**
     * The date of issue for the original birth certificate - free copy
     */
    @Column(nullable = true, updatable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date originalBCDateOfIssue;

    /**
     * The place of issue for the original birth certificate - free copy (Stores the DS Division ID)
     */
    @Column(nullable = true, updatable = false)
    private Integer originalBCPlaceOfIssue;

    /**
     * The original BC place of issue as a String in the preferred language
     */
    @Transient
    private String originalBCPlaceOfIssuePrint;

    /**
     * if normal birth - true, if still birth - false
     */
    @Column(nullable = false, columnDefinition = "integer default 1")
    private boolean liveBirth = true;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getOriginalBCDateOfIssue() {
        return originalBCDateOfIssue;
    }

    public void setOriginalBCDateOfIssue(Date originalBCDateOfIssue) {
        this.originalBCDateOfIssue = originalBCDateOfIssue;
    }

    public Integer getOriginalBCPlaceOfIssue() {
        return originalBCPlaceOfIssue;
    }

    public void setOriginalBCPlaceOfIssue(Integer originalBCPlaceOfIssue) {
        this.originalBCPlaceOfIssue = originalBCPlaceOfIssue;
    }

    public District getBirthDistrict() {
        return birthDivision.getDistrict();
    }

    public BDDivision getBirthDivision() {
        return birthDivision;
    }

    public void setBirthDivision(BDDivision birthDivision) {
        this.birthDivision = birthDivision;
    }

    public DSDivision getDsDivision() {
        return birthDivision.getDsDivision();
    }

    public long getBdfSerialNo() {
        return bdfSerialNo;
    }

    public void setBdfSerialNo(Long bdfSerialNo) {
        this.bdfSerialNo = bdfSerialNo;
    }

    public Date getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(Date dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public BirthDeclaration.State getStatus() {
        return status;
    }

    public void setStatus(BirthDeclaration.State status) {
        this.status = status;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getOriginalBCPlaceOfIssuePrint() {
        return originalBCPlaceOfIssuePrint;
    }

    public void setOriginalBCPlaceOfIssuePrint(String originalBCPlaceOfIssuePrint) {
        this.originalBCPlaceOfIssuePrint = originalBCPlaceOfIssuePrint;
    }

    public String getBdDivisionPrint() {
        return bdDivisionPrint;
    }

    public void setBdDivisionPrint(String bdDivisionPrint) {
        this.bdDivisionPrint = bdDivisionPrint;
    }

    public String getDsDivisionPrint() {
        return dsDivisionPrint;
    }

    public void setDsDivisionPrint(String dsDivisionPrint) {
        this.dsDivisionPrint = dsDivisionPrint;
    }

    public String getDistrictPrint() {
        return districtPrint;
    }

    public void setDistrictPrint(String districtPrint) {
        this.districtPrint = districtPrint;
    }

    public boolean getLiveBirth() {
        return liveBirth;
    }

    public void setLiveBirth(boolean liveBirth) {
        this.liveBirth = liveBirth;
    }

    public User getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(User approveUser) {
        this.approveUser = approveUser;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public User getConfirmationPrintUser() {
        return confirmationPrintUser;
    }

    public void setConfirmationPrintUser(User confirmationPrintUser) {
        this.confirmationPrintUser = confirmationPrintUser;
    }

    public Date getConfirmationPrintDate() {
        return confirmationPrintDate;
    }

    public void setConfirmationPrintDate(Date confirmationPrintDate) {
        this.confirmationPrintDate = confirmationPrintDate;
    }

    public Date getLastDayForConfirmation() {
        return lastDayForConfirmation;
    }

    public void setLastDayForConfirmation(Date lastDayForConfirmation) {
        this.lastDayForConfirmation = lastDayForConfirmation;
    }

    public User getOriginalBCPrintUser() {
        return originalBCPrintUser;
    }

    public void setOriginalBCPrintUser(User originalBCPrintUser) {
        this.originalBCPrintUser = originalBCPrintUser;
    }
}
