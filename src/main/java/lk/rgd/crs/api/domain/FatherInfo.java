package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.util.WebUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Ashoka Ekanayaka
 * Encapsulate all information for a father in the context of registration of births/deaths and marriages.
 */
@Embeddable
public class FatherInfo {
    /**
     * NIC or PIN of father
     */
    @Column(nullable = true, length = 12)
    private String fatherNICorPIN;

    /**
     * Passport number if a foreigner
     */
    @Column(nullable = true, length = 15)
    private String fatherPassportNo;

    /**
     * Country if a foreigner
     */
    @ManyToOne
    @JoinColumn(name = "fatherCountryId")
    private Country fatherCountry;

    /**
     * Fathers country name as a String in the preferred language
     */
    @Transient
    private String fatherCountryPrint;

    /**
     * Name of father
     */
    @Column(nullable = true, length = 600)
    private String fatherFullName;

    /**
     * DOB of father
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date fatherDOB;

    /**
     * Place of birth of father
     */
    @Column(nullable = true, length = 60)
    private String fatherPlaceOfBirth;

    /**
     * Race of father
     */
    @ManyToOne
    @JoinColumn(name = "fatherRace")
    private Race fatherRace;

    /**
     * Fathers race name as a String in the preferred language
     */
    @Transient
    private String fatherRacePrint;

    public String getFatherNICorPIN() {
        return fatherNICorPIN;
    }

    public void setFatherNICorPIN(String fatherNICorPIN) {
        this.fatherNICorPIN = WebUtils.filterBlanks(fatherNICorPIN);
    }

    public String getFatherPassportNo() {
        return fatherPassportNo;
    }

    public void setFatherPassportNo(String fatherPassportNo) {
        this.fatherPassportNo = WebUtils.filterBlanks(fatherPassportNo);
    }

    public Country getFatherCountry() {
        return fatherCountry;
    }

    public void setFatherCountry(Country fatherCountry) {
        this.fatherCountry = fatherCountry;
    }

    public String getFatherCountryPrint() {
        return fatherCountryPrint;
    }

    public void setFatherCountryPrint(String fatherCountryPrint) {
        this.fatherCountryPrint = WebUtils.filterBlanks(fatherCountryPrint);
    }

    public String getFatherFullName() {
        return fatherFullName;
    }

    public void setFatherFullName(String fatherFullName) {
        this.fatherFullName = WebUtils.filterBlanks(fatherFullName);
    }

    public Date getFatherDOB() {
        return fatherDOB;
    }

    public void setFatherDOB(Date fatherDOB) {
        this.fatherDOB = fatherDOB;
    }

    public String getFatherPlaceOfBirth() {
        return fatherPlaceOfBirth;
    }

    public void setFatherPlaceOfBirth(String fatherPlaceOfBirth) {
        this.fatherPlaceOfBirth = WebUtils.filterBlanks(fatherPlaceOfBirth);
    }

    public Race getFatherRace() {
        return fatherRace;
    }

    public void setFatherRace(Race fatherRace) {
        this.fatherRace = fatherRace;
    }

    public String getFatherRacePrint() {
        return fatherRacePrint;
    }

    public void setFatherRacePrint(String fatherRacePrint) {
        this.fatherRacePrint = WebUtils.filterBlanks(fatherRacePrint);
    }
}
