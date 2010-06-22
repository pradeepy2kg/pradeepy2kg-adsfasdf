package lk.rgd.common.api.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Set;
import java.util.Date;

/**
 * Represents a User of the system and his groups, preferences and privileges
 *
 * @author asankha
 */
@Entity
@Table(name = "USERS", schema = "COMMON")
@NamedQueries({
        @NamedQuery(name = "filter.by.roleid", query = "SELECT u FROM User u " +
                "WHERE u.status != 9 AND u.role.roleId = :roleId " +
                "ORDER BY u.userId"),
        @NamedQuery(name = "filter.by.bd_district", query = "SELECT u FROM User u " +
                "WHERE u.status != 9 AND :assignedBDDistrict MEMBER OF u.assignedBDDistricts " +
                "ORDER BY u.userId"),
        @NamedQuery(name = "filter.by.mr_district", query = "SELECT u FROM User u " +
                "WHERE u.status != 9 AND :assignedMRDistrict MEMBER OF u.assignedMRDistricts " +
                "ORDER BY u.userId"),
        @NamedQuery(name = "filter.by.role_and_bd_district", query = "SELECT u FROM User u " +
                "WHERE u.status != 9 AND u.role = :role AND :assignedBDDistrict MEMBER OF u.assignedBDDistricts " +
                "ORDER BY u.userId"),
        @NamedQuery(name = "filter.by.role_and_mr_district", query = "SELECT u FROM User u " +
                "WHERE u.status != 9 AND u.role = :role AND :assignedMRDistrict MEMBER OF u.assignedMRDistricts " +
                "ORDER BY u.userId"),
        @NamedQuery(name = "filter.by.wildcard_id", query = "SELECT u FROM User u " +
                "WHERE u.status != 9 AND u.userId LIKE :userIdMatch " +
                "ORDER BY u.userId"),
        @NamedQuery(name = "filter.by.wildcard_name", query = "SELECT u FROM User u " +
                "WHERE u.status != 9 AND u.userName LIKE :userNameMatch " +
                "ORDER BY u.userName"),
        @NamedQuery(name = "filter.non.deleted", query = "SELECT u FROM User u " +
                "WHERE u.status != 9 " +
                "ORDER BY u.userName")

})
public class User {

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
        DELETED /** 9 - state. Permanently deleted cannot be edited or login */
    }

    private static final Logger logger = LoggerFactory.getLogger(User.class);

    @Id
    @Column(updatable = false, length = 30)
    private String userId;
    /**
     * The simple name of the user
     */
    @Column(nullable = false, length = 60)
    private String userName;
    /**
     * This is the PIN of the actual user in the PRS
     */
    @Column(nullable = false, length = 10)
    private String pin;
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
     * @see State
     */
    @Column(nullable = false, name = "STATUS", columnDefinition = "smallint not null default 1")
    private State status;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "roleId")
    private Role role;

    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        if (userId != null ? !userId.equals(user.userId) : user.userId != null) return false;
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

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
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

    public Date getPasswordExpiry() {
        return passwordExpiry;
    }

    public void setPasswordExpiry(Date passwordExpiry) {
        this.passwordExpiry = passwordExpiry;
    }

    public boolean isAuthorized(int permission) {
        return role == null ? false : role.getPermBitSet().get(permission);
    }

    public boolean isAllowedAccessToBDDistrict(int id) {
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
}