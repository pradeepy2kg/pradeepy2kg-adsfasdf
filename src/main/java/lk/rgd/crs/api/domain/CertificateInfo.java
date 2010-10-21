package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.WebUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * An instance representing certificate search prosess specific certificate information for life events such as births,
 * deaths etc.
 *
 * @author Chathuranga Withana
 */
@Embeddable
public class CertificateInfo implements Serializable {
    /**
     * This is the application number of the certificate search form
     */
    @Column(nullable = false)
    private String applicationNo;

    /**
     * This is the registrar's DS Division who is submitting the certificate search form
     */
    @ManyToOne
    @JoinColumn(name = "dsDivisionUKey", nullable = false, updatable = false)
    private DSDivision dsDivision;

    /**
     * The date which the certificate search form is submitted
     */
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateOfSubmission;

    /**
     * This is the applicants full name who is submitting the certificate search form
     */
    @Column(nullable = false, length = 600)
    private String applicantFullName;

    /**
     * This is the address of the applicant who is submitting the certificate search form
     */
    @Column(nullable = false, length = 255)
    private String applicantAddress;

    /**
     * Number of certificate copies required
     */
    @Transient
    private int noOfCopies;

    /**
     * This is the stamps supplied in payment of charges for the certificate search
     */
    @Column(nullable = false)
    private Float stampCharges;

    /**
     * @see lk.rgd.crs.api.domain.CertificateSearch.CertificateType
     */
    @Enumerated
    private CertificateSearch.CertificateType certificateType;

    /**
     * This is the user performing the certificate search
     */
    @OneToOne
    @JoinColumn(name = "searchUser")
    private User searchUser;

    /**
     * The timestamp when the certificate search performed
     */
    @Column(nullable = true, updatable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date searchPerformDate;

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = WebUtils.filterBlanksAndToUpper(applicationNo);
    }

    public DSDivision getDsDivision() {
        return dsDivision;
    }

    public void setDsDivision(DSDivision dsDivision) {
        this.dsDivision = dsDivision;
    }

    public Date getDateOfSubmission() {
        return dateOfSubmission;
    }

    public void setDateOfSubmission(Date dateOfSubmission) {
        this.dateOfSubmission = dateOfSubmission;
    }

    public String getApplicantFullName() {
        return applicantFullName;
    }

    public void setApplicantFullName(String applicantFullName) {
        this.applicantFullName = WebUtils.filterBlanksAndToUpper(applicantFullName);
    }

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(String applicantAddress) {
        this.applicantAddress = WebUtils.filterBlanksAndToUpper(applicantAddress);
    }

    public int getNoOfCopies() {
        return noOfCopies;
    }

    public void setNoOfCopies(int noOfCopies) {
        this.noOfCopies = noOfCopies;
    }

    public Float getStampCharges() {
        return stampCharges;
    }

    public void setStampCharges(Float stampCharges) {
        this.stampCharges = stampCharges;
    }

    public User getSearchUser() {
        return searchUser;
    }

    public void setSearchUser(User searchUser) {
        this.searchUser = searchUser;
    }

    public Date getSearchPerformDate() {
        return searchPerformDate;
    }

    public void setSearchPerformDate(Date searchPerformDate) {
        this.searchPerformDate = searchPerformDate;
    }

    public CertificateSearch.CertificateType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(CertificateSearch.CertificateType certificateType) {
        this.certificateType = certificateType;
    }
}
