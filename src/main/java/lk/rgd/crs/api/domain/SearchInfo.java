package lk.rgd.crs.api.domain;

import lk.rgd.common.util.WebUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * An instance representing certificate search process specific search information for life events.
 *
 * @author Chathuranga Withana
 */
@Embeddable
public class SearchInfo implements Serializable {
    /**
     * This is the full name of whose certificate being searched (e.g. birth certificate, death certificate etc.)
     */
    @Column(nullable = true, length = 600)
    private String searchFullNameOfficialLang;

    /**
     * This is the full name of whose birth certificate being searched
     */
    @Column(nullable = true, length = 600)
    private String searchFullNameEnglish;

    /**
     * The Gender of whose certificate being searched. (0 - male, 1 - female, 2 - unknown)
     */
    @Column(nullable = true)
    private int gender;

    @Transient
    private String searchRecordStatus;

    /**
     * This is applicable for death certificate search (cause of death in death certificate search form)
     */
    @Column(nullable = true, length = 1000)
    private String causeOfEvent;

    /**
     * This is the number of results returned for the certificate search
     */
    @Column(nullable = false)
    private int resultsFound;

    /**
     * This is the full name of father whose certificate is being searched
     */
    @Column(nullable = true, length = 600)
    private String fatherFullName;

    /**
     * This is the full name of mother  whose certificate is being searched
     */
    @Column(nullable = true, length = 600)
    private String motherFullName;

    /**
     * The date of searching life event occured - e.g. for births - date of birth, for deaths - date of death
     */
    @Column(nullable = true)
    @Temporal(TemporalType.DATE)
    private Date dateOfEvent;

    /**
     * This is the place of searching life event occured - e.g. for births - place of birth, for deaths - place of death
     */
    @Column(nullable = true, length = 255)
    private String placeOfEvent;

    /**
     * This is the PIN or NIC of whose certificate being searched - e.g. for births - child PIN, for deaths - NIC or PIN
     * of the dead person
     */
    @Column(nullable = true, length = 12)
    private String searchPINorNIC;

    /**
     * This is the searching events declaration serial number - e.g. for births - birth declaration serial number etc.
     */
    @Column(nullable = true, length = 10)
    private Long searchSerialNo;

    /**
     * This is the registration division associated with serial no where life events registered
     * e.g. for births - birth registration division
     */
    @ManyToOne
    @JoinColumn(name = "bdDivisionUKey", nullable = true)
    private BDDivision bdDivision;

    /**
     * This is the number of the searching certificate - e.g. Birth certificate number, Death certificate number etc.
     */
    @Column(nullable = true)
    private Long certificateNo;

    /**
     * This is the searching certificate issued date
     */
    @Column(nullable = true)
    @Temporal(TemporalType.DATE)
    private Date certificateIssueDate;

    /**
     * This is the PIN(Identification Number) whose certificate being searched
     */
    @Column(nullable = true)
    private Long searchPIN;

    public String getSearchFullNameOfficialLang() {
        return searchFullNameOfficialLang;
    }

    public void setSearchFullNameOfficialLang(String searchFullNameOfficialLang) {
        this.searchFullNameOfficialLang = WebUtils.filterBlanksAndToUpper(searchFullNameOfficialLang);
    }

    public String getSearchFullNameEnglish() {
        return searchFullNameEnglish;
    }

    public void setSearchFullNameEnglish(String searchFullNameEnglish) {
        this.searchFullNameEnglish = WebUtils.filterBlanksAndToUpper(searchFullNameEnglish);
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCauseOfEvent() {
        return causeOfEvent;
    }

    public void setCauseOfEvent(String causeOfEvent) {
        this.causeOfEvent = WebUtils.filterBlanksAndToUpper(causeOfEvent);
    }

    public int getResultsFound() {
        return resultsFound;
    }

    public void setResultsFound(int resultsFound) {
        this.resultsFound = resultsFound;
    }

    public String getFatherFullName() {
        return fatherFullName;
    }

    public void setFatherFullName(String fatherFullName) {
        this.fatherFullName = WebUtils.filterBlanksAndToUpper(fatherFullName);
    }

    public String getMotherFullName() {
        return motherFullName;
    }

    public void setMotherFullName(String motherFullName) {
        this.motherFullName = WebUtils.filterBlanksAndToUpper(motherFullName);
    }

    public Date getDateOfEvent() {
        return dateOfEvent;
    }

    public void setDateOfEvent(Date dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }

    public String getPlaceOfEvent() {
        return placeOfEvent;
    }

    public void setPlaceOfEvent(String placeOfEvent) {
        this.placeOfEvent = WebUtils.filterBlanksAndToUpper(placeOfEvent);
    }

    public String getSearchPINorNIC() {
        return searchPINorNIC;
    }

    public void setSearchPINorNIC(String searchPINorNIC) {
        this.searchPINorNIC = WebUtils.filterBlanksAndToUpper(searchPINorNIC);
    }

    public Long getSearchSerialNo() {
        return searchSerialNo;
    }

    public void setSearchSerialNo(Long searchSerialNo) {
        this.searchSerialNo = searchSerialNo;
    }

    public Long getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(Long certificateNo) {
        this.certificateNo = certificateNo;
    }

    public Date getCertificateIssueDate() {
        return certificateIssueDate;
    }

    public void setCertificateIssueDate(Date certificateIssueDate) {
        this.certificateIssueDate = certificateIssueDate;
    }

    public String getSearchRecordStatus() {
        return searchRecordStatus;
    }

    public void setSearchRecordStatus(String searchRecordStatus) {
        this.searchRecordStatus = searchRecordStatus;
    }

    public BDDivision getBdDivision() {
        return bdDivision;
    }

    public void setBdDivision(BDDivision bdDivision) {
        this.bdDivision = bdDivision;
    }

    public Long getSearchPIN() {
        return searchPIN;
    }

    public void setSearchPIN(Long searchPIN) {
        this.searchPIN = searchPIN;
    }
}