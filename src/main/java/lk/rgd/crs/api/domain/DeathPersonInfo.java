package lk.rgd.crs.api.domain;

import javax.persistence.Embeddable;
import javax.persistence.Column;
import java.io.Serializable;

/**
 * @author Indunil Moremada
 *         An instance representing death information of the death person
 */

@Embeddable
public class DeathPersonInfo implements Serializable {

    @Column(nullable = true)
    private String deathPersonPINorNIC;

    @Column(nullable = true)

    private int deathPersonCountryId;

    @Column(nullable = true)
    private String deathPersonPassportNo;

    @Column(nullable = true)
    private String deathPersonAge;

    /**
     * Gender 0 - male, 1 - female, 2 - unknown
     */
    @Column(nullable = false)
    private int deathPersonGender;

    @Column(nullable = true)
    private int deathPersonRace;

    @Column(nullable = true, length = 600)
    private String deathPersonNameOfficialLang;

    @Column(nullable = true, length = 600)
    private String deathPersonNameInEnglish;

    @Column(nullable = true, length = 255)
    private String deathPersonPermanentAddress;

    @Column(nullable = true)
    private String deathPersonFatherPINorNIC;

    @Column(nullable = true)
    private String deathPersonFatherFullName;

    @Column(nullable = true)
    private String deathPersonMotherPINorNIC;

    @Column(nullable = true)
    private String deathPersonMotherFullName;

    public String getDeathPersonPINorNIC() {
        return deathPersonPINorNIC;
    }

    public void setDeathPersonPINorNIC(String deathPersonPINorNIC) {
        this.deathPersonPINorNIC = deathPersonPINorNIC;
    }

    public int getDeathPersonCountryId() {
        return deathPersonCountryId;
    }

    public void setDeathPersonCountryId(int deathPersonCountryId) {
        this.deathPersonCountryId = deathPersonCountryId;
    }

    public String getDeathPersonPassportNo() {
        return deathPersonPassportNo;
    }

    public void setDeathPersonPassportNo(String deathPersonPassportNo) {
        this.deathPersonPassportNo = deathPersonPassportNo;
    }

    public String getDeathPersonAge() {
        return deathPersonAge;
    }

    public void setDeathPersonAge(String deathPersonAge) {
        this.deathPersonAge = deathPersonAge;
    }

    public int getDeathPersonGender() {
        return deathPersonGender;
    }

    public void setDeathPersonGender(int deathPersonGender) {
        this.deathPersonGender = deathPersonGender;
    }

    public int getDeathPersonRace() {
        return deathPersonRace;
    }

    public void setDeathPersonRace(int deathPersonRace) {
        this.deathPersonRace = deathPersonRace;
    }

    public String getDeathPersonNameOfficialLang() {
        return deathPersonNameOfficialLang;
    }

    public void setDeathPersonNameOfficialLang(String deathPersonNameOfficialLang) {
        this.deathPersonNameOfficialLang = deathPersonNameOfficialLang;
    }

    public String getDeathPersonNameInEnglish() {
        return deathPersonNameInEnglish;
    }

    public void setDeathPersonNameInEnglish(String deathPersonNameInEnglish) {
        this.deathPersonNameInEnglish = deathPersonNameInEnglish;
    }

    public String getDeathPersonPermanentAddress() {
        return deathPersonPermanentAddress;
    }

    public void setDeathPersonPermanentAddress(String deathPersonPermanentAddress) {
        this.deathPersonPermanentAddress = deathPersonPermanentAddress;
    }

    public String getDeathPersonFatherPINorNIC() {
        return deathPersonFatherPINorNIC;
    }

    public void setDeathPersonFatherPINorNIC(String deathPersonFatherPINorNIC) {
        this.deathPersonFatherPINorNIC = deathPersonFatherPINorNIC;
    }

    public String getDeathPersonFatherFullName() {
        return deathPersonFatherFullName;
    }

    public void setDeathPersonFatherFullName(String deathPersonFatherFullName) {
        this.deathPersonFatherFullName = deathPersonFatherFullName;
    }

    public String getDeathPersonMotherPINorNIC() {
        return deathPersonMotherPINorNIC;
    }

    public void setDeathPersonMotherPINorNIC(String deathPersonMotherPINorNIC) {
        this.deathPersonMotherPINorNIC = deathPersonMotherPINorNIC;
    }

    public String getDeathPersonMotherFullName() {
        return deathPersonMotherFullName;
    }

    public void setDeathPersonMotherFullName(String deathPersonMotherFullName) {
        this.deathPersonMotherFullName = deathPersonMotherFullName;
    }


}
