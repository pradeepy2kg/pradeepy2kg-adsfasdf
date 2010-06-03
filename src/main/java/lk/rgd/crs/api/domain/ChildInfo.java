package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.GNDivision;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * An instance representing child information submitted for the declaration of a birth (page 1 of the form)
 */
@Embeddable
public class ChildInfo implements Serializable {
    /**
     * The Birth/Death registration division where the birth is registered (Includes District)
     */
    @ManyToOne
    @JoinColumn(name = "bdDivisionUKey", nullable = false)
    private BDDivision birthDivision;

    /**
     * Grama Niladhari division
     */
    @ManyToOne
    @JoinColumn(name = "gnDivisionUKey", nullable = false)
    private GNDivision gnDivision;

    /**
     * This is the serial number captured from the BDF
     */
    @Column(nullable = false, updatable = false)
    private String bdfSerialNo;

    /**
     * This is the PIN number generated to the child
     */
    @Column(nullable = true, length = 10)
    private String pin;

    /**
     * The date of the birth
     */
    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfBirth;

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

    /**
     * The place of birth - usually the village or hospital name
     */
    @Column(nullable = true, length = 255)
    private String placeOfBirth;

    /**
     * Name in Sinhala or Tamil
     */
    @Column(nullable = true, length = 600)
    private String childFullNameOfficialLang;

    /**
     * Name in English
     */
    @Column(nullable = true, length = 600)
    private String childFullNameEnglish;

    /**
     * Gender 0 - male, 1 - female, 2 - unknown
     */
    @Column(nullable = false)
    private int childGender;

    /**
     * Weight in kilogrammes
     */
    @Column(nullable = true)
    private Float childBirthWeight;

    /**
     * Child rank according to the order of live births
     */
    @Column(nullable = true)
    private Integer childRank;

    /**
     * Number of children born along with the child being registered
     */
    @Column(nullable = true)
    private Integer numberOfChildrenBorn;

    /**
     * Hospial code
     */
    @Column(nullable = true, length = 15)
    private String hospitalCode;

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
        return gnDivision.getDsDivision();
    }

    public void setDsDivision(DSDivision dsDivision) {
        this.gnDivision.setDsDivision(dsDivision);
    }

    public String getBdfSerialNo() {
        return bdfSerialNo;
    }

    public void setBdfSerialNo(String bdfSerialNo) {
        this.bdfSerialNo = bdfSerialNo;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getChildFullNameOfficialLang() {
        return childFullNameOfficialLang;
    }

    public void setChildFullNameOfficialLang(String childFullNameOfficialLang) {
        this.childFullNameOfficialLang = childFullNameOfficialLang;
    }

    public String getChildFullNameEnglish() {
        return childFullNameEnglish;
    }

    public void setChildFullNameEnglish(String childFullNameEnglish) {
        this.childFullNameEnglish = childFullNameEnglish;
    }

    public int getChildGender() {
        return childGender;
    }

    public void setChildGender(int childGender) {
        this.childGender = childGender;
    }

    public Float getChildBirthWeight() {
        return childBirthWeight;
    }

    public void setChildBirthWeight(Float childBirthWeight) {
        this.childBirthWeight = childBirthWeight;
    }

    public Integer getChildRank() {
        return childRank;
    }

    public void setChildRank(Integer childRank) {
        this.childRank = childRank;
    }

    public Integer getNumberOfChildrenBorn() {
        return numberOfChildrenBorn;
    }

    public void setNumberOfChildrenBorn(Integer numberOfChildrenBorn) {
        this.numberOfChildrenBorn = numberOfChildrenBorn;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public GNDivision getGnDivision() {
        return gnDivision;
    }

    public void setGnDivision(GNDivision gnDivision) {
        this.gnDivision = gnDivision;
    }
}
