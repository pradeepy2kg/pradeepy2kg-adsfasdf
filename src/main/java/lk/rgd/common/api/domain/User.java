package lk.rgd.common.api.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.BitSet;
import java.util.Set;

/**
 * Represents a User of the system and his groups, preferences and privileges
 *
 * @author asankha
 */
@Entity
@Table(name = "USERS", schema = "COMMON")
public class User {

    private static final Logger logger = LoggerFactory.getLogger(User.class);

    @Id
    @Column(updatable = false, length = 30)
    private String userId;
    /** The simple name of the user */
    @Column(nullable = false, length = 60)
    private String userName;
    /** This is the PIN of the actual user in the PRS */
    @Column(nullable = false, length = 10)
    private String pin;
    /** The SHA-1 hash of the password */
    @Column(length = 60)
    private String passwordHash;
    /** The preferred language */
    @Column(length = 2, nullable = false)
    private String prefLanguage;

    /** The preferred District - when multi-district authorization is available */
    @OneToOne
    @JoinColumn(name = "prefDistrictUKey")
    private District prefDistrict;
    /** The preferred DS division - when multi-DS-Division authorization is available */
    @OneToOne
    @JoinColumn(name = "prefDSDivisionUKey")
    private DSDivision prefDSDivision;

    /** The assigned districts */
    @ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(schema = "COMMON", name = "USER_DISTRICTS",
        joinColumns = @JoinColumn(name="userId"),
        inverseJoinColumns = @JoinColumn(name="districtUKey"))
    private Set<District> assignedDistricts;

    /** The assigned DS Divisions */
    @ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(schema = "COMMON", name = "USER_DSDIVISIONS",
        joinColumns = @JoinColumn(name="userId"),
        inverseJoinColumns = @JoinColumn(name="dsDivisionUKey"))
    private Set<DSDivision> assignedDSDivisions;

    /** Is the user account active - 0 : active, 1 - inactive, 2 - locked out */
    @Column(nullable = false, name="STATUS", columnDefinition="smallint not null default 1")
    private int status;

    @ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(schema = "COMMON", name = "USER_ROLES",
        joinColumns = @JoinColumn(name="userId"),
        inverseJoinColumns = @JoinColumn(name="roleId"))
    private Set<Role> roles;

    @Transient
    private BitSet permissions = null;

    public User() {}

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setPermissions(BitSet permissions) {
        this.permissions = permissions;
    }

    public District getPrefDistrict() {
        return prefDistrict;
    }

    public void setPrefDistrict(District prefDistrict) {
        this.prefDistrict = prefDistrict;
    }

    public DSDivision getPrefDSDivision() {
        return prefDSDivision;
    }

    public void setPrefDSDivision(DSDivision prefDSDivision) {
        this.prefDSDivision = prefDSDivision;
    }

    public Set<District> getAssignedDistricts() {
        return assignedDistricts;
    }

    public void setAssignedDistricts(Set<District> assignedDistricts) {
        this.assignedDistricts = assignedDistricts;
    }

    public Set<DSDivision> getAssignedDSDivisions() {
        return assignedDSDivisions;
    }

    public void setAssignedDSDivisions(Set<DSDivision> assignedDSDivisions) {
        this.assignedDSDivisions = assignedDSDivisions;
    }

    public boolean isAuthorized(int permission) {
        if (permissions == null) {
            permissions = new BitSet();
            for (Role r : roles) {
                if (r.getPermBitSet() != null) {
                    permissions.or(r.getPermBitSet());
                }
            }
        }
        return permissions.get(permission);
    }

    // TODO changed by chathuranga
    public int getInitialDistrict() {
        if (prefDistrict != null) {
            //return prefDistrict.getDistrictId();
            return prefDistrict.getDistrictUKey();
        } else if (assignedDistricts != null && !assignedDistricts.isEmpty()) {
            District d = assignedDistricts.iterator().next();
            if (d != null) {
                //return d.getDistrictId();
                return d.getDistrictUKey();
            }
        }
        logger.error("User {} : does not have access to any District!", userId);
        return -1;
    }

    // TODO changed by chathuranga
    public int getInitialBDDivision() {
        if (prefDSDivision != null) {
            //return prefDSDivision.getDivisionId();
            return prefDSDivision.getDsDivisionUKey();
        } else if (assignedDSDivisions != null && !assignedDSDivisions.isEmpty()) {
            DSDivision d = assignedDSDivisions.iterator().next();
            if (d != null) {
                //return d.getDivisionId();
                return d.getDsDivisionUKey();
            }
        }
        logger.error("User {} : does not have access to any DS Division!", userId);
        return -1;
    }

    public boolean isPlayingRole(String role) {
        for (Role r : roles) {
            if (role.equals(r.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isAllowedAccessToDistrict(int id) {
        if (assignedDistricts == null) {
            return false;
        }

        for (District d : assignedDistricts) {
            if (d.getDistrictUKey() == id) {
                return true;
            }
        }
        return false;
    }

    public boolean isAllowedAccessToDSDivision(int id) {
        if (assignedDSDivisions == null) {
            return false;
        }

        for (DSDivision d : assignedDSDivisions) {
            if (d.getDsDivisionUKey() == id) {
                return true;
            }
        }
        return false;
    }
}