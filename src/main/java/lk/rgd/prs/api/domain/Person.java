package lk.rgd.prs.api.domain;

import lk.rgd.common.api.domain.Country;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author asankha
 */
@Entity
@Table(name = "PERSON", schema = "PRS")
@NamedQueries({
    @NamedQuery(name = "filter.by.pin", query = "SELECT p FROM Person p WHERE p.pin = :pin"),
    @NamedQuery(name = "filter.by.nic", query = "SELECT p FROM Person p WHERE p.nic = :nic")
})
public class Person {

    /**
     * Record status
     * Verified records are known to be accurate
     * Unverified records may be inaccurate
     */
    public enum Status {
        UNVERIFIED      /** 0 - Record may be inaccurate */,        
        VERIFIED        /** 1 - Record is confirmed to be accurate */
    }

    /**
     * The life status
     */
    public enum LifeStatus {
        UNKNOWN         /** 0 - Current whereabouts are unknown */,
        ALIVE           /** 1 - Is known to be born and not known to be dead */,
        DEAD            /** 2 - Is known to be dead */,
        MISSING         /** 3 - Reported as missing */,
        NON_RESIDENT    /** 4 - Reported as not living within Sri Lanka anymore */
    }

    /**
     * The civil status
     */
    public enum CivilStatus {
        NEVER_MARRIED   /** 0 - Never known to be married */,
        MARRIED         /** 1 - Currently married */,
        ANNULLED        /** 2 - Currently married */,
        SEPARATED       /** 3 - Living separately from spouse. Cannot re-marry - TODO is this necessary? */,
        DIVORCED        /** 4 - Legally divorced from the spouse. Can re-marry */,
        WIDOWED         /** 5 - Spouse has died */
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long personUKey;
    /**
     * The unique Personal Identification Number
     */
    @Column(unique = true, nullable = true)
    private Long pin;
    /**
     * The National ID card number - could be possibly duplicated in rare instances
     */
    @Column(nullable = true, length = 10)
    private String nic;
    /**
     * The current passport number
     */
    @Column(nullable = true, length = 20)
    private String passportNo;
    /**
     * The preferred language of for the record
     */
    @Column (nullable = false, columnDefinition="char(2) default 'si'")
    private String preferredLanguage;
    /**
     * The full name in the official language
     */
    @Column(nullable = true, length = 600)
    private String fullNameInOfficialLanguage;
    /**
     * The initials in the official language
     */
    @Column(nullable = true, length = 60)
    private String initialsInOfficialLanguage;
    /**
     * The last name in English
     */
    @Column(nullable = true, length = 60)
    private String lastNameInOfficialLanguage;
    /**
     * The full name in English
     */
    @Column(nullable = true, length = 600)
    private String fullNameInEnglishLanguage;
    /**
     * The initials in English
     */
    @Column(nullable = true, length = 60)
    private String initialsInEnglish;
    /**
     * The last name in English
     */
    @Column(nullable = true, length = 60)
    private String lastNameInEnglish;
    /**
     * Gender 0 - male, 1 - female, 2 - unknown
     */
    @Column(nullable = false)
    private int gender;
    /**
     * Date of birth - optional in the PRS
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfBirth;
    /**
     * Place of Birth
     */
    @Column(nullable = true)
    private String placeOfBirth;
    /**
     * Date of death
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfDeath;
    /**
     * Record status - unverified (default) or verified
     */
    @Column(columnDefinition="smallint not null default 0")
    private Status status;
    /**
     * Current civil status - maybe unknown / null
     */
    @Column(nullable = true)
    private CivilStatus civilStatus;
    /**
     * Life status - alive, missing, dead, migrated etc.., maybe unknown / null
     */
    @Column(nullable = true)
    private LifeStatus lifeStatus;
    /**
     * The mother of this person
     */
    @OneToOne
    @JoinColumn(name = "motherUKey")
    private Person mother;
    /**
     * The father of this person
     */
    @OneToOne
    @JoinColumn(name = "fatherUKey")
    private Person father;
    /**
     * Countries of citizenship
     */
    @OneToMany
    @JoinTable(schema = "PRS", name = "PERSON_CITIZENSHIP",
        joinColumns = @JoinColumn(name="personUKey"),
        inverseJoinColumns = @JoinColumn(name="countryId"))
    private Set<Country> citizenship;
    /**
     * List of addresses
     */
    @OneToMany(mappedBy = "person")
    private Set<Address> addresses;
    /**
     * The last known address for this person for quick access
     */
    @OneToOne
    @JoinColumn(name = "lastAddressUKey")
    private Address lastAddress;
    /**
     * List of marriages
     */
    @ManyToMany
	@JoinTable(schema = "PRS", name = "PERSON_MARRIAGE",
        joinColumns = @JoinColumn(name="personUKey"),
        inverseJoinColumns = @JoinColumn(name="marriageUKey"))
    private Set<Marriage> marriages;
    /**
     * The last marriage for this person for quick access
     */
    @OneToOne
    @JoinColumn(name = "lastMarriageUKey")
    private Marriage lastMarriage;


    //----------------------------------- getters and setters -----------------------------------
    public Long getPin() {
        return pin;
    }

    public void setPin(Long pin) {
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

    public String getFullNameInEnglishLanguage() {
        return fullNameInEnglishLanguage;
    }

    public void setFullNameInEnglishLanguage(String fullNameInEnglishLanguage) {
        this.fullNameInEnglishLanguage = fullNameInEnglishLanguage;
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

    public CivilStatus getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(CivilStatus civilStatus) {
        this.civilStatus = civilStatus;
    }

    public LifeStatus getLifeStatus() {
        return lifeStatus;
    }

    public void setLifeStatus(LifeStatus lifeStatus) {
        this.lifeStatus = lifeStatus;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getInitialsInOfficialLanguage() {
        return initialsInOfficialLanguage;
    }

    public void setInitialsInOfficialLanguage(String initialsInOfficialLanguage) {
        this.initialsInOfficialLanguage = initialsInOfficialLanguage;
    }

    public String getLastNameInOfficialLanguage() {
        return lastNameInOfficialLanguage;
    }

    public void setLastNameInOfficialLanguage(String lastNameInOfficialLanguage) {
        this.lastNameInOfficialLanguage = lastNameInOfficialLanguage;
    }

    public String getInitialsInEnglish() {
        return initialsInEnglish;
    }

    public void setInitialsInEnglish(String initialsInEnglish) {
        this.initialsInEnglish = initialsInEnglish;
    }

    public Set<Country> getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(Set<Country> citizenship) {
        this.citizenship = citizenship;
    }

    public long getPersonUKey() {
        return personUKey;
    }

    public void setPersonUKey(long personUKey) {
        this.personUKey = personUKey;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Set<Marriage> getMarriages() {
        return marriages;
    }

    public void setMarriages(Set<Marriage> marriages) {
        this.marriages = marriages;
    }

    public Marriage getLastMarriage() {
        return lastMarriage;
    }

    public void setLastMarriage(Marriage lastMarriage) {
        this.lastMarriage = lastMarriage;
    }

    public Address getLastAddress() {
        return lastAddress;
    }

    public void setLastAddress(Address lastAddress) {
        this.lastAddress = lastAddress;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Person getMother() {
        return mother;
    }

    public void setMother(Person mother) {
        this.mother = mother;
    }

    public Person getFather() {
        return father;
    }

    public void setFather(Person father) {
        this.father = father;
    }
}
