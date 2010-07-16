package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * An instance represents information submitted for the birth certificate search
 *
 * @author Chathuranga Withana
 */
@Entity
@Table(name = "BIRTH_CERT_SEARCH", schema = "CRS")
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
    private Long applicationNo;

    /**
     * The date which the birth certificate search performed
     */
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateOfSearch;

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
    private String searchFullName;

    /**
     * Gender 0 - male, 1 - female, 2 - unknown
     */
    @Column(nullable = true)
    private int searchGender;

    /**
     * Number of birth certificate copies required
     */
    @Transient
    private int noOfCopies;

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
    private Date searchDateOfBirth;

    /**
     * This is the place of birth - Hospital, House No. and Street, Town or Village or name of Estate
     */
    @Column(nullable = true, length = 255)
    private String searchPlaceOfBirth;

    /**
     * The Birth/Death registration division where the birth is registered
     */
    @ManyToOne
    @JoinColumn(name = "bdDivisionUKey", nullable = false)
    private BDDivision birthDivision;

    /**
     * This is the number of the birth certificate
     */
    @Column(nullable = true)
    private long searchCertificateNo;

    /**
     * This is the date of birth certificate issued
     */
    @Column(nullable = true)
    @Temporal(TemporalType.DATE)
    private Date searchDateOfIssue;

    /**
     * The user searching the birth certificate
     */
    @OneToOne
    @JoinColumn(name = "searchUser")
    private User searchUser;

    public long getSearchUKey() {
        return searchUKey;
    }

    public void setSearchUKey(long searchUKey) {
        this.searchUKey = searchUKey;
    }

    public Long getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(Long applicationNo) {
        this.applicationNo = applicationNo;
    }

    public Date getDateOfSearch() {
        return dateOfSearch;
    }

    public void setDateOfSearch(Date dateOfSearch) {
        this.dateOfSearch = dateOfSearch;
    }

    public String getApplicantFullName() {
        return applicantFullName;
    }

    public void setApplicantFullName(String applicantFullName) {
        this.applicantFullName = applicantFullName;
    }

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(String applicantAddress) {
        this.applicantAddress = applicantAddress;
    }

    public String getSearchFullName() {
        return searchFullName;
    }

    public void setSearchFullName(String searchFullName) {
        this.searchFullName = searchFullName;
    }

    public int getSearchGender() {
        return searchGender;
    }

    public void setSearchGender(int searchGender) {
        this.searchGender = searchGender;
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
        this.fatherFullName = fatherFullName;
    }

    public String getMotherFullName() {
        return motherFullName;
    }

    public void setMotherFullName(String motherFullName) {
        this.motherFullName = motherFullName;
    }

    public Date getSearchDateOfBirth() {
        return searchDateOfBirth;
    }

    public void setSearchDateOfBirth(Date searchDateOfBirth) {
        this.searchDateOfBirth = searchDateOfBirth;
    }

    public String getSearchPlaceOfBirth() {
        return searchPlaceOfBirth;
    }

    public void setSearchPlaceOfBirth(String searchPlaceOfBirth) {
        this.searchPlaceOfBirth = searchPlaceOfBirth;
    }

    public BDDivision getBirthDivision() {
        return birthDivision;
    }

    public void setBirthDivision(BDDivision birthDivision) {
        this.birthDivision = birthDivision;
    }

    public long getSearchCertificateNo() {
        return searchCertificateNo;
    }

    public void setSearchCertificateNo(long searchCertificateNo) {
        this.searchCertificateNo = searchCertificateNo;
    }

    public Date getSearchDateOfIssue() {
        return searchDateOfIssue;
    }

    public void setSearchDateOfIssue(Date searchDateOfIssue) {
        this.searchDateOfIssue = searchDateOfIssue;
    }

    public User getSearchUser() {
        return searchUser;
    }

    public void setSearchUser(User searchUser) {
        this.searchUser = searchUser;
    }
}
