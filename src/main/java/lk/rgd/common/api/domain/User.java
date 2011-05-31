package lk.rgd.common.api.domain;

import lk.rgd.AppConstants;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Represents a User of the system and his groups, preferences and privileges
 *
 * @author asankha
 */
@Entity
@Table(name = "USERS", schema = "COMMON")
@NamedQueries({
    @NamedQuery(name = "filter.by.roleid", query = "SELECT u FROM User u " +
        "WHERE u.status != 3 AND u.role.roleId = :roleId " +
        "ORDER BY u.userId"),
    @NamedQuery(name = "filter.by.bd_district", query = "SELECT u FROM User u " +
        "WHERE u.status != 3 AND :assignedBDDistrict MEMBER OF u.assignedBDDistricts " +
        "ORDER BY u.userId"),
    @NamedQuery(name = "filter.by.mr_district", query = "SELECT u FROM User u " +
        "WHERE u.status != 3 AND :assignedMRDistrict MEMBER OF u.assignedMRDistricts " +
        "ORDER BY u.userId"),
    @NamedQuery(name = "filter.by.role_and_bd_district", query = "SELECT u FROM User u " +
        "WHERE u.status != 3 AND u.role = :role AND :assignedBDDistrict MEMBER OF u.assignedBDDistricts " +
        "ORDER BY u.userId"),
    @NamedQuery(name = "filter.by.role_and_mr_district", query = "SELECT u FROM User u " +
        "WHERE u.status != 3 AND u.role = :role AND :assignedMRDistrict MEMBER OF u.assignedMRDistricts " +
        "ORDER BY u.userId"),
    @NamedQuery(name = "filter.by.wildcard_id", query = "SELECT u FROM User u " +
        "WHERE u.status != 3 AND u.userId LIKE :userIdMatch " +
        "ORDER BY u.userId"),
    @NamedQuery(name = "filter.by.wildcard_name", query = "SELECT u FROM User u " +
        "WHERE u.status != 3 AND u.userName LIKE :userNameMatch " +
        "ORDER BY u.userName"),
    @NamedQuery(name = "filter.non.deleted", query = "SELECT u FROM User u " +
        "WHERE u.status != 3 " +
        "ORDER BY u.userName"),
    @NamedQuery(name = "filter.deo.by.dsdivision", query = "SELECT u.userId FROM User u " +
        "WHERE u.role = :role AND :assignedBDDSDivisions MEMBER OF u.assignedBDDSDivisions"),
    @NamedQuery(name = "filter.adr.by.district", query = "SELECT u.userId FROM User u " +
        "WHERE u.role = :role AND :assignedBDDistricts MEMBER OF u.assignedBDDistricts"),
    @NamedQuery(name = "get.user.by.name.id", query = "SELECT u FROM User u WHERE u.status !=3 " +
        "AND (u.userName LIKE :name OR u.userId LIKE :name )")
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User implements Serializable {

    public enum State {
        INACTIVE, /**
         * 0 - state. Cannot login
         */
        ACTIVE, /**
         * 1 - state. Can login
         */
        LOCKEDOUT, /**
         * 2 - state. Cannot login
         */
        DELETED /** 3 - state. Permanently deleted cannot be edited or login */
    }

    private static final Logger logger = LoggerFactory.getLogger(User.class);

    @Id
    @Column(updatable = false, length = 30)
    private String userId;

    @Embedded
    private BaseLifeCycleInfo lifeCycleInfo = new BaseLifeCycleInfo();

    /**
     * The simple name of the user
     */
    @Column(nullable = false, length = 60)
    private String userName;
    /**
     * This is the PIN of the actual user in the PRS
     */
    @Column(nullable = false)
    private long pin;
    /**
     * The SHA-1 hash of the password
     */
    @Column(length = 60)
    private String passwordHash;
    /**
     * The preferred language
     */
    @Column(length = 2, nullable = false)
    private String prefLanguage;

    /**
     * The password expiry date, after which the user is not allowed to login without changing the password
     */
    @Column(nullable = true)
    private Date passwordExpiry;

    /**
     * The preferred Marriage District - when multi-district authorization is available
     */
    @OneToOne
    @JoinColumn(name = "prefMRDistrictUKey")
    private District prefMRDistrict;

    /**
     * The preferred BD District - when multi-district authorization is available
     */
    @OneToOne
    @JoinColumn(name = "prefBDDistrictUKey")
    private District prefBDDistrict;

    /**
     * The preferred DS division - when multi-DS-Division authorization is available
     */
    @OneToOne
    @JoinColumn(name = "prefBDDSDivisionUKey")
    private DSDivision prefBDDSDivision;

    /**
     * The assigned Birth Registration districts
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(schema = "COMMON", name = "USER_BDDISTRICTS",
        joinColumns = @JoinColumn(name = "userId"),
        inverseJoinColumns = @JoinColumn(name = "districtUKey"))
    private Set<District> assignedBDDistricts;

    /**
     * The assigned Marriage Registration districts
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(schema = "COMMON", name = "USER_MRDISTRICTS",
        joinColumns = @JoinColumn(name = "userId"),
        inverseJoinColumns = @JoinColumn(name = "districtUKey"))
    private Set<District> assignedMRDistricts;

    /**
     * The assigned Birth Registration DS Divisions
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(schema = "COMMON", name = "USER_BDDSDIVISIONS",
        joinColumns = @JoinColumn(name = "userId"),
        inverseJoinColumns = @JoinColumn(name = "dsDivisionUKey"))
    private Set<DSDivision> assignedBDDSDivisions;

    /**
     * The assigned Marriage Registration DSDivisions
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(schema = "COMMON", name = "USER_MRDSDIVISIONS",
        joinColumns = @JoinColumn(name = "userId"),
        inverseJoinColumns = @JoinColumn(name = "dsDivisionUKey"))
    private Set<DSDivision> assignedMRDSDivisions;

    /**
     * @see State
     */
    @Column(nullable = false)
    private State status;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "roleId")
    private Role role;

    /**
     * The Locations assigned to this user
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserLocation> locations = new HashSet<UserLocation>();

    /**
     * The active primary location of the user
     */
    @OneToOne
    @JoinColumn(name = "primaryLocationUKey")
    private Location primaryLocation;

    @Column
    private String sienSignatureText;

    @Column
    private String taenSignatureText;

    @Column(columnDefinition = "smallint not null default 1")
    private int loginAttempts;

    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        if (userId != null ? !userId.equals(user.userId) : user.userId != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getPin() {
        return pin;
    }

    public void setPin(long pin) {
        this.pin = pin;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPrefLanguage() {
        return prefLanguage;
    }

    public void setPrefLanguage(String prefLanguage) {
        this.prefLanguage = prefLanguage;
    }

    public State getStatus() {
        return status;
    }

    public void setStatus(State status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public District getPrefMRDistrict() {
        return prefMRDistrict;
    }

    public void setPrefMRDistrict(District prefMRDistrict) {
        this.prefMRDistrict = prefMRDistrict;
    }

    public District getPrefBDDistrict() {
        return prefBDDistrict;
    }

    public void setPrefBDDistrict(District prefBDDistrict) {
        this.prefBDDistrict = prefBDDistrict;
    }

    public DSDivision getPrefBDDSDivision() {
        return prefBDDSDivision;
    }

    public void setPrefBDDSDivision(DSDivision prefBDDSDivision) {
        this.prefBDDSDivision = prefBDDSDivision;
    }

    public Set<District> getAssignedMRDistricts() {
        return assignedMRDistricts;
    }

    public void setAssignedMRDistricts(Set<District> assignedMRDistricts) {
        this.assignedMRDistricts = assignedMRDistricts;
    }

    public Set<District> getAssignedBDDistricts() {
        return assignedBDDistricts;
    }

    public void setAssignedBDDistricts(Set<District> assignedBDDistricts) {
        this.assignedBDDistricts = assignedBDDistricts;
    }

    public Set<DSDivision> getAssignedBDDSDivisions() {
        return assignedBDDSDivisions;
    }

    public void setAssignedBDDSDivisions(Set<DSDivision> assignedBDDSDivisions) {
        this.assignedBDDSDivisions = assignedBDDSDivisions;
    }

    public Set<DSDivision> getAssignedMRDSDivisions() {
        return assignedMRDSDivisions;
    }

    public void setAssignedMRDSDivisions(Set<DSDivision> assignedMRDSDivisions) {
        this.assignedMRDSDivisions = assignedMRDSDivisions;
    }

    public Date getPasswordExpiry() {
        return passwordExpiry;
    }

    public void setPasswordExpiry(Date passwordExpiry) {
        this.passwordExpiry = passwordExpiry;
    }

    public boolean isAuthorized(int permission) {
        logger.debug("User Role : {}  ", role.getRoleId());
        return role == null ? false : role.getPermBitSet().get(permission);
    }

    public BaseLifeCycleInfo getLifeCycleInfo() {
        return lifeCycleInfo;
    }

    public void setLifeCycleInfo(BaseLifeCycleInfo lifeCycleInfo) {
        this.lifeCycleInfo = lifeCycleInfo;
    }

    public Set<UserLocation> getLocations() {
        return locations;
    }

    public void setLocations(Set<UserLocation> locations) {
        this.locations = locations;
    }

    public String getSienSignatureText() {
        return sienSignatureText;
    }

    public void setSienSignatureText(String sienSignatureText) {
        this.sienSignatureText = sienSignatureText;
    }

    public String getTaenSignatureText() {
        return taenSignatureText;
    }

    public void setTaenSignatureText(String taenSignatureText) {
        this.taenSignatureText = taenSignatureText;
    }

    public Location getPrimaryLocation() {
        return primaryLocation;
    }

    public void setPrimaryLocation(Location primaryLocation) {
        this.primaryLocation = primaryLocation;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public boolean isAllowedAccessToBDDistrict(int id) {
        // RG has full access without limitation
        if (Role.ROLE_RG.equals(role.getRoleId())) {
            return true;
        }

        if (assignedBDDistricts == null) {
            return false;
        }

        for (District d : assignedBDDistricts) {
            if (d.getDistrictUKey() == id) {
                return true;
            }
        }
        return false;
    }

    public boolean isAllowedAccessToBDDSDivision(int id) {
        if (Role.ROLE_RG.equals(role.getRoleId())) {
            // RG has full access without limitation
            return true;
        }

        if (assignedBDDSDivisions == null) {
            return false;
        }

        for (DSDivision d : assignedBDDSDivisions) {
            if (d.getDsDivisionUKey() == id) {
                return true;
            }
        }
        return false;
    }

    public boolean isAllowedAccessToMRDistrict(int id) {
        // RG has full access without limitation
        if (Role.ROLE_RG.equals(role.getRoleId())) {
            return true;
        }

        if (assignedMRDistricts == null) {
            return false;
        }

        for (District d : assignedMRDistricts) {
            if (d.getDistrictUKey() == id) {
                return true;
            }
        }
        return false;
    }

    public boolean isAllowedAccessToMRDSDivision(int id) {
        if (Role.ROLE_RG.equals(role.getRoleId())) {
            // RG has full access without limitation
            return true;
        }

        if (assignedMRDSDivisions == null) {
            return false;
        }

        for (DSDivision d : assignedMRDSDivisions) {
            if (d.getDsDivisionUKey() == id) {
                return true;
            }
        }
        return false;
    }

    public boolean isAllowedAccessToLocation(int id) {
        if (Role.ROLE_RG.equals(role.getRoleId())) {
            // RG has full access without limitation
            return true;
        }

        if (locations == null || locations.isEmpty()) {
            return false;
        }

        for (UserLocation ul : locations) {
            if (ul.getLocationId() == id && ul.getLifeCycleInfo().isActive()) {
                return true;
            }
        }
        return false;
    }

    public List<Location> getActiveLocations() {
        LinkedList<Location> al = new LinkedList<Location>();
        for (UserLocation ul : locations) {
            if (ul.getLifeCycleInfo().isActive()) {
                al.add(ul.getLocation());
            }
        }
        // add primary location as the first element
        al.remove(primaryLocation);
        al.addFirst(primaryLocation);

        return al;
    }

    public String getUserSignature(String language) {
        if (AppConstants.SINHALA.equals(language)) {
            return this.getSienSignatureText();
        } else if (AppConstants.TAMIL.equals(language)) {
            return this.getTaenSignatureText();
        } else {
            logger.error("No valid UserSignature, invalid language type : {}", language);
            return null;
        }
    }
}