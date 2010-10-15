package lk.rgd.prs.api.domain;

import lk.rgd.AppConstants;
import lk.rgd.common.api.domain.Country;
import lk.rgd.common.api.domain.Race;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author asankha
 */
@Entity
@Table(name = "PERSON", schema = "PRS")
@NamedQueries({
    @NamedQuery(name = "findAllPersons", query = "SELECT p FROM Person p"),
    @NamedQuery(name = "filter.by.pin", query = "SELECT p FROM Person p WHERE p.pin = :pin"),
    @NamedQuery(name = "filter.by.nic", query = "SELECT p FROM Person p WHERE p.nic = :nic")
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
        SEMI_VERIFIED, /** 0 - Record may be inaccurate - NIC is available */
        VERIFIED         /** 1 - Record is confirmed to be accurate - PIN is available */
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
    @ManyToMany
    @JoinTable(schema = "PRS", name = "PERSON_CITIZENSHIP",
        joinColumns = @JoinColumn(name = "personUKey"),
        inverseJoinColumns = @JoinColumn(name = "countryId"))
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
        this.nic = nic;
    }

    public String getFullNameInOfficialLanguage() {
        return fullNameInOfficialLanguage;
    }

    public void setFullNameInOfficialLanguage(String fullNameInOfficialLanguage) {
        this.fullNameInOfficialLanguage = fullNameInOfficialLanguage;
        setInitialsAndLastnameOfficial(fullNameInOfficialLanguage);
    }

    public String getFullNameInEnglishLanguage() {
        return fullNameInEnglishLanguage;
    }

    public void setFullNameInEnglishLanguage(String fullNameInEnglishLanguage) {
        this.fullNameInEnglishLanguage = fullNameInEnglishLanguage;
        setInitialsAndLastnameEnglish(fullNameInEnglishLanguage);
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

    public void addCitizenship(Country country) {
        if (this.citizenship == null) {
            this.citizenship = new HashSet<Country>();
        }
        this.citizenship.add(country);
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

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
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
}
