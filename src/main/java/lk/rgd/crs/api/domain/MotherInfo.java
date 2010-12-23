package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.util.WebUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * @author   Ashoka Ekanayaka
 *  Encapsulate all information for a mother in the context of Birth/Death/Marriage registration
 */
@Embeddable
public class MotherInfo {
    /**
     * NIC or PIN of mother
     */
    @Column(nullable = true, length = 10)
    private String motherNICorPIN;

    /**
     * Passport number if a foreigner
     */
    @Column(nullable = true, length = 15)
    private String motherPassportNo;

    /**
     * Country if a foreigner
     */
    @ManyToOne
    @JoinColumn(name = "motherCountryId")
    private Country motherCountry;

    /**
     * Mothers country name as a String in the preferred language
     */
    @Transient
    private String motherCountryPrint;

    /**
     * Full name of mother
     */
    @Column(nullable = true, length = 600)
    private String motherFullName;

    /**
     * DOB of mother
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date motherDOB;

    /**
     * Place of birth for mother
     */
    @Column(nullable = true, length = 60)
    private String motherPlaceOfBirth;

    /**
     * Race for mother
     */
    @ManyToOne
    @JoinColumn(name = "motherRace")
    private Race motherRace;

    /**
     * Mothers race name as a String in the preferred language
     */
    @Transient
    private String motherRacePrint;

    /**
     * Age of mother at birth
     */
    @Column(nullable = true)
    private Integer motherAgeAtBirth;

    /**
     * Address of mother
     */
    @Column(nullable = true, length = 255)
    private String motherAddress;

    public String getMotherNICorPIN() {
        return motherNICorPIN;
    }

    public void setMotherNICorPIN(String motherNICorPIN) {
        this.motherNICorPIN = WebUtils.filterBlanksAndToUpper(motherNICorPIN);
    }

    public String getMotherPassportNo() {
        return motherPassportNo;
    }

    public void setMotherPassportNo(String motherPassportNo) {
        this.motherPassportNo = WebUtils.filterBlanksAndToUpper(motherPassportNo);
    }

    public Country getMotherCountry() {
        return motherCountry;
    }

    public void setMotherCountry(Country motherCountry) {
        this.motherCountry = motherCountry;
    }

    public String getMotherFullName() {
        return motherFullName;
    }

    public void setMotherFullName(String motherFullName) {
        this.motherFullName = WebUtils.filterBlanksAndToUpper(motherFullName);
    }

    public Date getMotherDOB() {
        return motherDOB;
    }

    public void setMotherDOB(Date motherDOB) {
        this.motherDOB = motherDOB;
    }

    public String getMotherPlaceOfBirth() {
        return motherPlaceOfBirth;
    }

    public void setMotherPlaceOfBirth(String motherPlaceOfBirth) {
        this.motherPlaceOfBirth = WebUtils.filterBlanksAndToUpper(motherPlaceOfBirth);
    }

    public Race getMotherRace() {
        return motherRace;
    }

    public void setMotherRace(Race motherRace) {
        this.motherRace = motherRace;
    }

    public Integer getMotherAgeAtBirth() {
        return motherAgeAtBirth;
    }

    public void setMotherAgeAtBirth(Integer motherAgeAtBirth) {
        this.motherAgeAtBirth = motherAgeAtBirth;
    }

    public String getMotherAddress() {
        return motherAddress;
    }

    public void setMotherAddress(String motherAddress) {
        this.motherAddress = WebUtils.filterBlanksAndToUpper(motherAddress);
    }

    public String getMotherRacePrint() {
        return motherRacePrint;
    }

    public void setMotherRacePrint(String motherRacePrint) {
        this.motherRacePrint = motherRacePrint;
    }

    public String getMotherCountryPrint() {
        return motherCountryPrint;
    }

    public void setMotherCountryPrint(String motherCountryPrint) {
        this.motherCountryPrint = motherCountryPrint;
    }
}
