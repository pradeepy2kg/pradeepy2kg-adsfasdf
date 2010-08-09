package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.domain.DSDivision;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * An instance represents information submitted for the birth certificate search
 *
 * @author Chathuranga Withana
 */
@Entity
@Table(name = "BIRTH_CERT_SEARCH", schema = "CRS",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"dsDivisionUKey", "applicationNo"})})

@NamedQueries({
    @NamedQuery(name = "get.by.serialNo.and.dsDivision", query = "SELECT bcs FROM BirthCertificateSearch bcs " +
        "WHERE bcs.applicationNo = :serialNo AND bcs.dsDivision = :dsDivision")
})
public class BirthCertificateSearch implements Serializable {
    /**
     * This is the auto generated unique row identifier
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long searchUKey;

    /**
     * This is the application number of birth certificate search form
     */
    @Column(nullable = false)
    private String applicationNo;

    @ManyToOne
    @JoinColumn(name = "dsDivisionUKey", nullable = false, updatable = false)
    private DSDivision dsDivision;

    /**
     * The date which the birth certificate search form is submitted
     */
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateOfSubmission;

    /**
     * This is the applicants full name
     */
    @Column(nullable = false, length = 600)
    private String applicantFullName;

    /**
     * This is the address of the applicant
     */
    @Column(nullable = false, length = 255)
    private String applicantAddress;

    /**
     * This is the full name of whose birth certificate being searched
     */
    @Column(nullable = true, length = 600)
    private String childFullNameOfficialLang;

    /**
     * This is the full name of whose birth certificate being searched
     */
    @Column(nullable = true, length = 600)
    private String childFullNameEnglish;

    /**
     * Gender 0 - male, 1 - female, 2 - unknown
     */
    @Column(nullable = true)
    private int gender;

    /**
     * Number of birth certificate copies required
     */
    @Transient
    private int noOfCopies;

    /**
     * The number of results returned for the search
     */
    @Column(nullable = false)
    private int resultsFound;

    /**
     * Full name of father
     */
    @Column(nullable = true, length = 600)
    private String fatherFullName;

    /**
     * Full name of mother before marriage
     */
    @Column(nullable = true, length = 600)
    private String motherFullName;

    /**
     * The date of birth
     */
    @Column(nullable = true)
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    /**
     * This is the place of birth - Hospital, House No. and Street, Town or Village or name of Estate
     */
    @Column(nullable = true, length = 255)
    private String placeOfBirth;

    /**
     * This is the number of the birth certificate
     */
    @Column(nullable = true)
    private Long certificateNo;

    /**
     * This is the date of birth certificate issued
     */
    @Column(nullable = true)
    @Temporal(TemporalType.DATE)
    private Date dateOfRegistration;

    /**
     * The user performed the birth certificate search
     */
    @OneToOne
    @JoinColumn(name = "searchUser")
    private User searchUser;

    /**
     * The timestamp when a birth certificate search performed
     */
    @Column(nullable = true, updatable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date searchPerformDate;

    private String filterBlanks(String s) {
        return (s == null) ? null : (s.trim().length() == 0) ? null : s.trim().toUpperCase();
    }

    public long getSearchUKey() {
        return searchUKey;
    }

    public void setSearchUKey(long searchUKey) {
        this.searchUKey = searchUKey;
    }

    public DSDivision getDsDivision() {
        return dsDivision;
    }

    public void setDsDivision(DSDivision dsDivision) {
        this.dsDivision = dsDivision;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = filterBlanks(applicationNo);
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
        this.applicantFullName = filterBlanks(applicantFullName);
    }

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(String applicantAddress) {
        this.applicantAddress = filterBlanks(applicantAddress);
    }

    public String getChildFullNameOfficialLang() {
        return childFullNameOfficialLang;
    }

    public void setChildFullNameOfficialLang(String childFullNameOfficialLang) {
        this.childFullNameOfficialLang = filterBlanks(childFullNameOfficialLang);
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getNoOfCopies() {
        return noOfCopies;
    }

    public void setNoOfCopies(int noOfCopies) {
        this.noOfCopies = noOfCopies;
    }

    public String getFatherFullName() {
        return fatherFullName;
    }

    public void setFatherFullName(String fatherFullName) {
        this.fatherFullName = filterBlanks(fatherFullName);
    }

    public String getMotherFullName() {
        return motherFullName;
    }

    public void setMotherFullName(String motherFullName) {
        this.motherFullName = filterBlanks(motherFullName);
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = filterBlanks(placeOfBirth);
    }

    public Long getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(Long certificateNo) {
        this.certificateNo = certificateNo;
    }

    public Date getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(Date dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
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

    public String getChildFullNameEnglish() {
        return childFullNameEnglish;
    }

    public void setChildFullNameEnglish(String childFullNameEnglish) {
        this.childFullNameEnglish = filterBlanks(childFullNameEnglish);
    }

    public int getResultsFound() {
        return resultsFound;
    }

    public void setResultsFound(int resultsFound) {
        this.resultsFound = resultsFound;
    }
}
