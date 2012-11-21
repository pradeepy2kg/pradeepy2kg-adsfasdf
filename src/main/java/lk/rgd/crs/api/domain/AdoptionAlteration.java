package lk.rgd.crs.api.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Duminda Dharmakeerthi
 *
 * The entity to store an alteration record of a Adoption record.
 *
 */

@Entity
@Table(name = "ALT_ADOPTION", schema = "CRS")
public class AdoptionAlteration {

    public enum State{
        DATA_ENTRY,
        FULL_APPROVED,
        REJECTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUKey;

    @Column(nullable = false)
    private long aoUKey;            // idUKey of the Adoption Order

    @Embedded
    private CRSLifeCycleInfo lifeCycleInfo = new CRSLifeCycleInfo();

    @Column(nullable = false)
    private State status;

    /**
     * Alteration fields for child details
     */
    @Column(nullable = true)
    private String childName;
    
    @Column(nullable = true)
    private int childGender;

    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date childBirthDate;

    /**
     * Alteration fields for applicant details
     */
    @Column(nullable = false)
    private String applicantName;

    @Column(nullable = false)
    private String applicantAddress;

    @Column(nullable = true)
    private String applicantSecondAddress;

    @Column(nullable = true)
    private String applicantOccupation;

    /**
     * Alteration fields for spouse details
     */
    @Column(nullable = true)
    private String spouseName;

    @Column(nullable = true)
    private String spouseOccupation;


    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public long getAoUKey() {
        return aoUKey;
    }

    public void setAoUKey(long aoUKey) {
        this.aoUKey = aoUKey;
    }

    public CRSLifeCycleInfo getLifeCycleInfo() {
        return lifeCycleInfo;
    }

    public void setLifeCycleInfo(CRSLifeCycleInfo lifeCycleInfo) {
        this.lifeCycleInfo = lifeCycleInfo;
    }

    public State getStatus() {
        return status;
    }

    public void setStatus(State status) {
        this.status = status;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public int getChildGender() {
        return childGender;
    }

    public void setChildGender(int childGender) {
        this.childGender = childGender;
    }

    public Date getChildBirthDate() {
        return childBirthDate;
    }

    public void setChildBirthDate(Date childBirthDate) {
        this.childBirthDate = childBirthDate;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(String applicantAddress) {
        this.applicantAddress = applicantAddress;
    }

    public String getApplicantSecondAddress() {
        return applicantSecondAddress;
    }

    public void setApplicantSecondAddress(String applicantSecondAddress) {
        this.applicantSecondAddress = applicantSecondAddress;
    }

    public String getApplicantOccupation() {
        return applicantOccupation;
    }

    public void setApplicantOccupation(String applicantOccupation) {
        this.applicantOccupation = applicantOccupation;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getSpouseOccupation() {
        return spouseOccupation;
    }

    public void setSpouseOccupation(String spouseOccupation) {
        this.spouseOccupation = spouseOccupation;
    }
}
