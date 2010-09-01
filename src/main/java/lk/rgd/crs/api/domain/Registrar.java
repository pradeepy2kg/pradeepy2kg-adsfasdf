package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.BaseLifeCycleInfo;
import lk.rgd.common.util.NameFormatUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Represents a Birth, Death or Marriage Registrar (i.e. the person). A Registrar may have one or more
 * enrollments as a marriage, birth/death registrar for different MR/BD divisions and durations
 *
 * @author asankha
 */
public class Registrar implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long registrarUKey;

    @Embedded
    private BaseLifeCycleInfo lifeCycleInfo = new BaseLifeCycleInfo();
    
    /**
     * The unique Personal Identification Number
     */
    @Column(unique = true, nullable = true)
    private long pin;

    /**
     * The National ID card number - could be possibly duplicated in rare instances
     */
    @Column(nullable = true, length = 10)
    private String nic;

    /**
     * Gender 0 - male, 1 - female, 2 - unknown
     */
    @Column(nullable = false)
    private int gender;

    /**
     * Date of birth
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfBirth;

    /**
     * The full name in the Official language
     */
    @Column(nullable = false, length = 600)
    private String fullNameInOfficialLanguage;

    /**
     * The full name in English
     */
    @Column(nullable = true, length = 600)
    private String fullNameInEnglishLanguage;
    private String currentAddress;

    /**
     * The preferred language of the registrar
     */
    @Column(nullable = false, columnDefinition = "char(2) default 'si'")
    private String preferredLanguage;

    /**
     * Phone number of Registrar
     */
    @Column(nullable = true, length = 30)
    private String phoneNo;

    /**
     * Email address
     */
    @Column(nullable = true, length = 30)
    private String emailAddress;

    /**
     * List of assignments for this registrar
     */
    @OneToMany(mappedBy = "registrar")
    private Set<Assignment> assignments;

    public long getRegistrarUKey() {
        return registrarUKey;
    }

    public void setRegistrarUKey(long registrarUKey) {
        this.registrarUKey = registrarUKey;
    }

    public long getPin() {
        return pin;
    }

    public void setPin(long pin) {
        this.pin = pin;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFullNameInOfficialLanguage() {
        return fullNameInOfficialLanguage;
    }

    public void setFullNameInOfficialLanguage(String fullNameInOfficialLanguage) {
        this.fullNameInOfficialLanguage = fullNameInOfficialLanguage;
    }

    public String getFullNameInEnglishLanguage() {
        return fullNameInEnglishLanguage;
    }

    public void setFullNameInEnglishLanguage(String fullNameInEnglishLanguage) {
        this.fullNameInEnglishLanguage = fullNameInEnglishLanguage;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public Set<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(Set<Assignment> assignments) {
        this.assignments = assignments;
    }

    public void addAssignment(Assignment a) {
        this.assignments.add(a);
    }

    public BaseLifeCycleInfo getLifeCycleInfo() {
        return lifeCycleInfo;
    }

    public void setLifeCycleInfo(BaseLifeCycleInfo lifeCycleInfo) {
        this.lifeCycleInfo = lifeCycleInfo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getShortName() {
        return NameFormatUtil.getDisplayName(fullNameInEnglishLanguage);
    }
}
