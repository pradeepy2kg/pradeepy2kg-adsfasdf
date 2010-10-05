package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.crs.web.util.WebUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * An instance representing birth registration process specific official information
 * and statuses for a given birth registration/declaration.
 */
@Embeddable
public class BirthRegisterInfo implements Serializable {
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

    /**
     * The user printing the confirmation
     */
    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmationPrintUserId", nullable = true)
    private User confirmationPrintUser;

    /**
     * The timestamp when confirmation is printed for this record
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date confirmationPrintTimestamp;

    @Transient
    private Date lastDayForConfirmation;

    /**
     * Status comment - e.g. reason for rejection due to duplicate
     */
    @Lob
    @Column(nullable = true, length = 4096)
    private String comments;

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
     * @see lk.rgd.crs.api.domain.BirthDeclaration.BirthType
     */
    @Enumerated
    @Column(nullable = false)
    private BirthDeclaration.BirthType birthType = BirthDeclaration.BirthType.LIVE;

    /**
     * The adoption unique key of an adoption order, if only applicable
     */
    @Column(nullable = true)
    private Long adoptionUKey;

    /**
     * For late registrations the case file number that retains the physical file number where supplementary
     * documents have been filed
     */
    @Column(nullable = true)
    private String caseFileNumber;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = WebUtils.filterBlanks(comments);
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

    public String getDateOfRegistrationForPrint() {
       return DateTimeUtils.getISO8601FormattedString(dateOfRegistration);
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

    public BirthDeclaration.BirthType getBirthType() {
        return birthType;
    }

    public void setBirthType(BirthDeclaration.BirthType birthType) {
        this.birthType = birthType;
    }

    public User getConfirmationPrintUser() {
        return confirmationPrintUser;
    }

    public void setConfirmationPrintUser(User confirmationPrintUser) {
        this.confirmationPrintUser = confirmationPrintUser;
    }

    public Date getConfirmationPrintTimestamp() {
        return confirmationPrintTimestamp;
    }

    public void setConfirmationPrintTimestamp(Date confirmationPrintTimestamp) {
        this.confirmationPrintTimestamp = confirmationPrintTimestamp;
    }

    public Date getLastDayForConfirmation() {
        return lastDayForConfirmation;
    }

    public void setLastDayForConfirmation(Date lastDayForConfirmation) {
        this.lastDayForConfirmation = lastDayForConfirmation;
    }

    public Long getAdoptionUKey() {
        return adoptionUKey;
    }

    public void setAdoptionUKey(Long adoptionUKey) {
        this.adoptionUKey = adoptionUKey;
    }

    public String getCaseFileNumber() {
        return caseFileNumber;
    }

    public void setCaseFileNumber(String caseFileNumber) {
        this.caseFileNumber = caseFileNumber;
    }
}
