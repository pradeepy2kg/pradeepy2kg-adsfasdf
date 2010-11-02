package lk.rgd.prs.api.domain;

import lk.rgd.AppConstants;
import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.util.WebUtils;

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
    @NamedQuery(name = "filter.by.pin", query = "SELECT p FROM Person p WHERE p.pin = :pin"),
    @NamedQuery(name = "filter.by.temporaryPIN", query = "SELECT p FROM Person p WHERE p.temporaryPin = :temporaryPin"),
    @NamedQuery(name = "filter.by.nic", query = "SELECT p FROM Person p WHERE p.nic = :nic"),
    @NamedQuery(name = "findAllChildren", query = "SELECT p FROM Person p WHERE p.mother = :person OR p.father = :person"),
    @NamedQuery(name = "findAllSiblings", query = "SELECT p FROM Person p WHERE (p.mother = :mother OR p.father = :father) AND (p <> :person)")
})
public class Person implements Serializable {
    private static String[] EMPTY_NAME = new String[2];

    /**
     * Record status
     * Verified records are known to be accurate
     * Unverified records may be inaccurate
     */
    public enum Status {
        UNVERIFIED,      /** 0 - Record may be inaccurate - no PIN or NIC */
        SEMI_VERIFIED,   /** 1 - Record may be inaccurate - NIC is available */
        VERIFIED,        /** 2 - Record is confirmed to be accurate - PIN is available */
        CANCELLED        /** 3 - Record is is cancelled as a duplicate or error */
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
        ANNULLED        /** 2 - Currently married */
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
     * The temporary PIN assigned to an unverified or semi-verified record
     */
    @Column(unique = true, nullable = true)
    private Long temporaryPin;
    /**
     * Date of registration - submitted date of the person registration info
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfRegistration;
    /**
     * The passport numbers in the format "LK:M1203456 JP:F092434"
     */
    @Column(nullable = true, length = 60)
    private String passportNos;
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
     * The initials in the official language
     */
    @Column(nullable = true, length = 90)
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
    @Column(nullable = true, length = 90)
    private String initialsInEnglish;
    /**
     * The last name in English
     */
    @Column(nullable = true, length = 60)
    private String lastNameInEnglish;
    /**
     * The phone number of person
     */
    @Column(nullable = true, length = 30)
    private String personPhoneNo;
    /**
     * The email address of person
     */
    @Column(nullable = true, length = 30)
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
     *  */
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
     *
     */
    @Column(nullable = true, length = 10)
    private String motherPINorNIC;
    /**
     *
     */
    @Column(nullable = true, length = 10)
    private String fatherPINorNIC;
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
    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
    private Set<PersonCitizenship> countries = new HashSet<PersonCitizenship>();  

    /**
     * Add a record of a marriage. Marks this marriage as the 'last' marriage
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
        setInitialsAndLastnameOfficial(WebUtils.filterBlanksAndToUpper(fullNameInOfficialLanguage));
    }

    public String getFullNameInEnglishLanguage() {
        return fullNameInEnglishLanguage;
    }

    public void setFullNameInEnglishLanguage(String fullNameInEnglishLanguage) {
        this.fullNameInEnglishLanguage = WebUtils.filterBlanksAndToUpper(fullNameInEnglishLanguage);
        setInitialsAndLastnameEnglish(WebUtils.filterBlanksAndToUpper(fullNameInEnglishLanguage));
    }

    public String getLastNameInEnglish() {
        return lastNameInEnglish;
    }

    public void setLastNameInEnglish(String lastNameInEnglish) {
        this.lastNameInEnglish = WebUtils.filterBlanksAndToUpper(lastNameInEnglish);
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

    public String getInitialsInOfficialLanguage() {
        return initialsInOfficialLanguage;
    }

    public void setInitialsInOfficialLanguage(String initialsInOfficialLanguage) {
        this.initialsInOfficialLanguage = WebUtils.filterBlanksAndToUpper(initialsInOfficialLanguage);
    }

    public String getLastNameInOfficialLanguage() {
        return lastNameInOfficialLanguage;
    }

    public void setLastNameInOfficialLanguage(String lastNameInOfficialLanguage) {
        this.lastNameInOfficialLanguage = WebUtils.filterBlanksAndToUpper(lastNameInOfficialLanguage);
    }

    public String getInitialsInEnglish() {
        return initialsInEnglish;
    }

    public void setInitialsInEnglish(String initialsInEnglish) {
        this.initialsInEnglish = WebUtils.filterBlanksAndToUpper(initialsInEnglish);
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

    public String getPassportNos() {
        return passportNos;
    }

    public void addPassportNo(Country country, String passportNo) {
        if (this.passportNos == null) {
            this.passportNos = country.getCountryCode() + ":" + passportNo;
        } else {
            this.passportNos = this.passportNos + " " + country.getCountryCode() + ":" + passportNo;
        }
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
        this.motherPINorNIC = motherPINorNIC;
    }

    public String getFatherPINorNIC() {
        return fatherPINorNIC;
    }

    public void setFatherPINorNIC(String fatherPINorNIC) {
        this.fatherPINorNIC = fatherPINorNIC;
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

    public PRSLifeCycleInfo getLifeCycleInfo() {
        return lifeCycleInfo;
    }

    public void setLifeCycleInfo(PRSLifeCycleInfo lifeCycleInfo) {
        this.lifeCycleInfo = lifeCycleInfo;
    }

    private void setInitialsAndLastnameEnglish(String fullname) {
        if (fullname != null && !isEmptyString(fullname)) {
            String[] names = fullname.split(" ");
            lastNameInEnglish = names[names.length - 1];

            StringBuilder sb = new StringBuilder(16);
            for (int i = 0; i < names.length - 1; i++) {
                if (!isEmptyString(names[i])) {
                    sb.append(names[i].charAt(0)).append(". ");
                }
            }
            initialsInEnglish = sb.toString();
        }
    }

    private void setInitialsAndLastnameOfficial(String fullname) {
        if (fullname != null && !isEmptyString(fullname)) {
            String[] names = fullname.split(" ");
            lastNameInOfficialLanguage = names[names.length - 1];

            StringBuilder sb = new StringBuilder(16);
            for (int i = 0; i < names.length - 1; i++) {
                if (!isEmptyString(names[i])) {
                    sb.append(names[i].charAt(0)).append(". ");
                }
            }
            initialsInOfficialLanguage = sb.toString();
        }
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
