package lk.rgd.prs.api.domain;

import lk.rgd.AppConstants;
import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.util.WebUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ashoka Ekanayaka
 */
@Entity
@Table(name = "PERSON", schema = "PRS")
@NamedQueries({
    @NamedQuery(name = "findAllPersons", query = "SELECT p FROM Person p"),
    @NamedQuery(name = "filter.by.pin", query = "SELECT p FROM Person p WHERE p.pin = :pin AND p.lifeCycleInfo.activeRecord IS TRUE"),
    @NamedQuery(name = "filter.by.temporaryPIN", query = "SELECT p FROM Person p WHERE p.temporaryPin = :temporaryPin AND p.lifeCycleInfo.activeRecord IS TRUE"),
    @NamedQuery(name = "filter.by.nic", query = "SELECT p FROM Person p WHERE p.nic = :nic AND p.lifeCycleInfo.activeRecord IS TRUE"),
    @NamedQuery(name = "findAllChildren", query = "SELECT p FROM Person p WHERE (p.mother = :person OR p.father = :person) AND p.lifeCycleInfo.activeRecord IS TRUE"),
    @NamedQuery(name = "findAllSiblings", query = "SELECT p FROM Person p WHERE (p.mother = :mother OR p.father = :father) AND p.lifeCycleInfo.activeRecord IS TRUE"),
    @NamedQuery(name = "get.by.location", query = "SELECT p FROM Person p WHERE p.submittedLocation = :location AND p.status <= 2 AND p.lifeCycleInfo.activeRecord IS TRUE " +
        "ORDER BY p.lifeCycleInfo.lastUpdatedTimestamp DESC"),
    @NamedQuery(name = "get.by.location.and.pin", query = "SELECT p FROM Person p WHERE p.submittedLocation = :location " +
        "AND p.pin = :pin AND p.status <= 2 AND p.lifeCycleInfo.activeRecord IS TRUE ORDER BY p.lifeCycleInfo.lastUpdatedTimestamp DESC "),
    @NamedQuery(name = "get.by.location.and.nic", query = "SELECT p FROM Person p WHERE p.submittedLocation = :location " +
        "AND p.nic = :nic AND p.status <= 2 AND p.lifeCycleInfo.activeRecord IS TRUE ORDER BY p.lifeCycleInfo.lastUpdatedTimestamp DESC "),
    @NamedQuery(name = "get.by.location.and.tempPin", query = "SELECT p FROM Person p WHERE p.submittedLocation = :location " +
        "AND p.temporaryPin = :tempPin AND p.status <= 2 AND p.lifeCycleInfo.activeRecord IS TRUE ORDER BY p.lifeCycleInfo.lastUpdatedTimestamp DESC "),
    @NamedQuery(
        name = "getPersonByPIN",
        query = "SELECT p FROM Person p WHERE  p.pin = :pin AND p.lifeCycleInfo.activeRecord IS TRUE ORDER BY p.lifeCycleInfo.lastUpdatedTimestamp DESC "
    )
})
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Person implements Serializable {

    /**
     * Record status
     * Verified records are known to be accurate
     * Unverified records may be inaccurate
     */
    public enum Status {
        UNVERIFIED,      /** 0 - Record may be inaccurate - no PIN or NIC (added automatically) */
        SEMI_VERIFIED,   /** 1 - Record may be inaccurate - NIC is available (added automatically) */
        DATA_ENTRY,      /** 2 - Record is added by filling form */
        VERIFIED,        /** 3 - Record is confirmed to be accurate - PIN is available */
        CANCELLED,       /** 4 - Record is cancelled as a duplicate or error */
        DELETED,         /** 5 - Record is deleted before approval by ADR or higher */
        ARCHIVED_ALTERED /** 6 - Record has been altered after approval */
    }

    /**
     * The life status
     */
    public enum LifeStatus {
        UNKNOWN         /** 0 - Current whereabouts are unknown */
        ,
        ALIVE           /** 1 - Is known to be born and not known to be dead */
        ,
        DEAD            /** 2 - Is known to be dead */
        ,
        MISSING         /** 3 - Reported as missing */
        ,
        NON_RESIDENT    /** 4 - Reported as not living within Sri Lanka anymore */
        }

    /**
     * The civil status
     */
    public enum CivilStatus {
        NEVER_MARRIED   /** 0 - Never known to be married */
        ,
        MARRIED         /** 1 - Currently married */
        ,
        ANNULLED        /** 2 - event never happened */
        ,
        SEPARATED       /** 3 - Living separately from spouse. Cannot re-marry - TODO is this necessary? */
        ,
        DIVORCED        /** 4 - Legally divorced from the spouse. Can re-marry */
        ,
        WIDOWED         /** 5 - Spouse has died */
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long personUKey;

    @Embedded
    private PRSLifeCycleInfo lifeCycleInfo = new PRSLifeCycleInfo();

    /**
     * The unique Personal Identification Number  (Made non unique to support history)
     */
    @Column(unique = false, nullable = true)
    private Long pin;
    /**
     * The National ID card number - could be possibly duplicated in rare instances
     */
    @Column(nullable = true, length = 10)
    private String nic;
    /**
     * The temporary PIN assigned to an unverified or semi-verified record. (Made non unique to support history)
     */
    @Column(unique = false, nullable = true)
    private Long temporaryPin;
    /**
     * Date of registration - submitted date of the person registration info
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfRegistration = new Date();

    /**
     * The preferred language of for the record
     */
    @Column(nullable = false, columnDefinition = "char(2) default 'si'")
    private String preferredLanguage = AppConstants.SINHALA;
    /**
     * The full name in the official language
     */
    @Column(nullable = true, length = 600)
    private String fullNameInOfficialLanguage;
    /**
     * The full name in English
     */
    @Column(nullable = true, length = 600)
    private String fullNameInEnglishLanguage;
    /**
     * The phone number of person
     */
    @Column(nullable = true, length = 30)
    private String personPhoneNo;
    /**
     * The email address of person
     */
    @Column(nullable = true, length = 50)
    private String personEmail;
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
     * The Hash value computed from person details. This can be used to verify a given barcode of a person.
     * details include : official lang name, english name, pin, gender, DOB, and record created time.
     * Algorithm will be SHA-1 hence length is 160.
     */
    @Column(nullable = true, length = 160)
    private String hash;
    /**
     * Record status - unverified (default) or verified
     */
    @Column(columnDefinition = "smallint not null default 0")
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
     * Registering persons mothers PIN or NIC
     */
    @Column(nullable = true, length = 12)
    private String motherPINorNIC;
    /**
     * Registering persons fathers PIN or NIC
     */
    @Column(nullable = true, length = 12)
    private String fatherPINorNIC;
    /**
     * Status comment - e.g. reason for deletion or rejection
     */
    @Lob
    @Column(nullable = true, length = 4096)
    private String comments;
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
        joinColumns = @JoinColumn(name = "personUKey"),
        inverseJoinColumns = @JoinColumn(name = "marriageUKey"))
    private Set<Marriage> marriages;
    /**
     * The last marriage for this person for quick access
     */
    @OneToOne
    @JoinColumn(name = "lastMarriageUKey")
    private Marriage lastMarriage;

    @ManyToOne
    @JoinColumn(name = "race")
    private Race race;
    /**
     * The Countries assigned to this person
     */
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private Set<PersonCitizenship> countries = new HashSet<PersonCitizenship>();
    /**
     * The primary location of the user who is adding a person to the PRS
     */
    @ManyToOne
    @JoinColumn(name = "submitLocationUKey")
    private Location submittedLocation;
    /**
     * The permanent address of this person
     */
    @Transient
    private String permanentAddress;
    /**
     * The current address of this person
     */
    @Transient
    private String currentAddress;

    /**
     * Add a record of a marriage. Marks this marriage as the 'last' marriage
     *
     * @param m marriage details
     */
    public void specifyMarriage(Marriage m) {
        if (marriages == null) {
            marriages = new HashSet<Marriage>();
        }
        marriages.add(m);
        lastMarriage = m;
        civilStatus = CivilStatus.MARRIED;
    }

    /**
     * Add a record of an address. Marks this address as the 'last' address
     *
     * @param a address details
     */
    public void specifyAddress(Address a) {
        if (addresses == null) {
            addresses = new HashSet<Address>();
        }
        addresses.add(a);
        lastAddress = a;
        a.setPerson(this);
    }
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
        this.nic = WebUtils.filterBlanksAndToUpper(nic);
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

    public String getPersonPhoneNo() {
        return personPhoneNo;
    }

    public void setPersonPhoneNo(String personPhoneNo) {
        this.personPhoneNo = WebUtils.filterBlanks(personPhoneNo);
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = WebUtils.filterBlanks(personEmail);
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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = WebUtils.filterBlanksAndToUpper(placeOfBirth);
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMotherPINorNIC() {
        return motherPINorNIC;
    }

    public void setMotherPINorNIC(String motherPINorNIC) {
        this.motherPINorNIC = WebUtils.filterBlanksAndToUpper(motherPINorNIC);
    }

    public String getFatherPINorNIC() {
        return fatherPINorNIC;
    }

    public void setFatherPINorNIC(String fatherPINorNIC) {
        this.fatherPINorNIC = WebUtils.filterBlanksAndToUpper(fatherPINorNIC);
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public Long getTemporaryPin() {
        return temporaryPin;
    }

    public void setTemporaryPin(Long temporaryPin) {
        this.temporaryPin = temporaryPin;
    }

    public Date getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(Date dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Set<PersonCitizenship> getCountries() {
        return countries;
    }

    public void setCountries(Set<PersonCitizenship> countries) {
        this.countries = countries;
    }

    public Location getSubmittedLocation() {
        return submittedLocation;
    }

    public void setSubmittedLocation(Location submittedLocation) {
        this.submittedLocation = submittedLocation;
    }

    public PRSLifeCycleInfo getLifeCycleInfo() {
        return lifeCycleInfo;
    }

    public void setLifeCycleInfo(PRSLifeCycleInfo lifeCycleInfo) {
        this.lifeCycleInfo = lifeCycleInfo;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = WebUtils.filterBlanksAndToUpper(permanentAddress);
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = WebUtils.filterBlanksAndToUpper(currentAddress);
    }

    private static final boolean isEmptyString(String s) {
        return s == null || s.trim().length() == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Person person = (Person) o;

        if (personUKey != person.personUKey) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (personUKey ^ (personUKey >>> 32));
    }
}
