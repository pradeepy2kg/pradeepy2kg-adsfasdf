package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.DSDivision;

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
    @Column (nullable = false, columnDefinition="char(2) default 'si'")
    private String preferredLanguage;

    /**
     * This is the serial number captured from the BDF
     */
    @Column(nullable = false, updatable = false)
    private long bdfSerialNo;

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

    /** Status comment - e.g. reason for rejection due to duplicate  */
    @Column(nullable = true)
    private String comments;

    /** The date of issue for the original birth certificate - free copy */
    @Column(nullable = true, updatable = false)
    @Temporal(value = TemporalType.DATE)
    private Date originalBCDateOfIssue;

    /** The place of issue for the original birth certificate - free copy (Stores the DS Division ID) */
    @Column(nullable = true, updatable = false)
    private Integer originalBCPlaceOfIssue;

    /**
     * The original BC place of issue as a String in the preferred language
     */
    @Transient
    private String originalBCPlaceOfIssuePrint;

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

    public void setBdfSerialNo(long bdfSerialNo) {
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
}
