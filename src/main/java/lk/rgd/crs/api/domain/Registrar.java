package lk.rgd.crs.api.domain;

import lk.rgd.AppConstants;
import lk.rgd.common.api.domain.BaseLifeCycleInfo;
import lk.rgd.common.util.NameFormatUtil;
import lk.rgd.common.util.WebUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
@NamedQueries({
    @NamedQuery(name = "get.registrars.by.pin", query = "SELECT registrar FROM Registrar registrar WHERE registrar.pin = :pin "),
    @NamedQuery(name = "get.registrar.by.name.or.part.of.name", query = "SELECT registrar FROM Registrar registrar WHERE " +
        "(registrar.fullNameInEnglishLanguage LIKE :name OR registrar.fullNameInOfficialLanguage LIKE :name)")
})
@Entity
@Table(name = "REGISTRAR", schema = "CRS")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
     * Date of appointment
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfAppointment;

    /**
     * Date of termination
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfTermination;

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
    private String preferredLanguage = AppConstants.SINHALA;

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
     * Job status is acting?
     */
    @Column(nullable = false)
    private boolean acting;

    /**
     * Resigned ?
     */
    @Column(nullable = false)
    private boolean resigned;

    /**
     * Is this a Medical Registrar ?
     */
    @Column(nullable = false)
    private boolean medical;

    /**
     * Terminated ?
     */
    @Column(nullable = false)
    private boolean employmentTerminated;

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
        this.nic = WebUtils.filterBlanksAndToUpper(nic);
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
        this.fullNameInOfficialLanguage = WebUtils.filterBlanksAndToUpper(fullNameInOfficialLanguage);
    }

    public String getFullNameInEnglishLanguage() {
        return fullNameInEnglishLanguage;
    }

    public void setFullNameInEnglishLanguage(String fullNameInEnglishLanguage) {
        this.fullNameInEnglishLanguage = WebUtils.filterBlanksAndToUpper(fullNameInEnglishLanguage);
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = WebUtils.filterBlanksAndToUpper(currentAddress);
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
        this.phoneNo = WebUtils.filterBlanks(phoneNo);
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = WebUtils.filterBlanks(emailAddress);
    }

    public String getShortName() {
        return NameFormatUtil.getDisplayName(fullNameInEnglishLanguage);
    }

    public Date getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(Date dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }

    public Date getDateOfTermination() {
        return dateOfTermination;
    }

    public void setDateOfTermination(Date dateOfTermination) {
        this.dateOfTermination = dateOfTermination;
    }

    public boolean isActing() {
        return acting;
    }

    public void setActing(boolean acting) {
        this.acting = acting;
    }

    public boolean isResigned() {
        return resigned;
    }

    public void setResigned(boolean resigned) {
        this.resigned = resigned;
    }

    public boolean isEmploymentTerminated() {
        return employmentTerminated;
    }

    public void setEmploymentTerminated(boolean employmentTerminated) {
        this.employmentTerminated = employmentTerminated;
    }

    public boolean isMedical() {
        return medical;
    }

    public void setMedical(boolean medical) {
        this.medical = medical;
    }
}
