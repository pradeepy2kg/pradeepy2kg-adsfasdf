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
     * This is the serial number captured from the BDF
     */
    @Column(nullable = false, updatable = false)
    private String bdfSerialNo;

    /**
     * The Birth/Death registration division where the birth is registered (Includes District)
     */
    @ManyToOne
    @JoinColumn(name = "bdDivisionUKey", nullable = false)
    private BDDivision birthDivision;

    /**
     * The date when the birth declaration was submitted to the medical registrar or the DS office
     */
    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfRegistration;

    /**
     * 0 - BDF added, 1 - ADR approved, 2 - Confirmation printed
     * 3 - confirmed without changes, 14 - Record archived and corrected (i.e. during the confirmation by parents),
     * 5 - confirmation changes captured, 6 - confirmation changes approved
     * 10 - rejected and archived
     */
    @Column(nullable = false)
    private int status;

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

    public String getBdfSerialNo() {
        return bdfSerialNo;
    }

    public void setBdfSerialNo(String bdfSerialNo) {
        this.bdfSerialNo = bdfSerialNo;
    }
    
    public Date getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(Date dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
