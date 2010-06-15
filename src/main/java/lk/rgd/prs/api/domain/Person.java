package lk.rgd.prs.api.domain;

import lk.rgd.common.api.domain.Country;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author asankha
 */
public class Person {

    /**
     * The unique Personal Identification Number
     */
    private String pin;
    /**
     * The National ID card number - could be possibly duplicated in rare instances
     */
    private String nic;
    /**
     * The full name in the official language
     */
    private String fullNameInOfficialLanguage;
    /**
     * The first names in English
     */
    private String firstNamesInEnglish;
    /**
     * The last name in English
     */
    private String lastNameInEnglish;
    /**
     * Gender 0 - male, 1 - female, 2 - unknown
     */
    private int gender;
    /**
     * Date of birth
     */
    private Date dateOfBirth;
    /**
     * Place of Birth
     */
    private String placeOfBirth;
    /**
     * Date of death
     */
    private Date dateOfDeath;
    /**
     * Current civil status
     */
    private int civilStatus;
    /**
     * Life status - alive, missing, dead, migrated etc..
     */
    private int lifeStatus;
    /**
     * Countries of citizenship
     */
    private Set<Country> citizenship;

    /**
     * List of addresses
     */
    private List<Address> addresses;
    //TODO
    // CurrentAddress, PermanentAddress, Contact details
    // spouse, children, parents links



    //----------------------------------- getters and setters -----------------------------------
    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getFullNameInOfficialLanguage() {
        return fullNameInOfficialLanguage;
    }

    public void setFullNameInOfficialLanguage(String fullNameInOfficialLanguage) {
        this.fullNameInOfficialLanguage = fullNameInOfficialLanguage;
    }

    public String getFirstNamesInEnglish() {
        return firstNamesInEnglish;
    }

    public void setFirstNamesInEnglish(String firstNamesInEnglish) {
        this.firstNamesInEnglish = firstNamesInEnglish;
    }

    public String getLastNameInEnglish() {
        return lastNameInEnglish;
    }

    public void setLastNameInEnglish(String lastNameInEnglish) {
        this.lastNameInEnglish = lastNameInEnglish;
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

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public Date getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(Date dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public int getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(int civilStatus) {
        this.civilStatus = civilStatus;
    }

    public int getLifeStatus() {
        return lifeStatus;
    }

    public void setLifeStatus(int lifeStatus) {
        this.lifeStatus = lifeStatus;
    }
}
