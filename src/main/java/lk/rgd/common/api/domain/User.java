package lk.rgd.common.api.domain;

import javax.persistence.*;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a User of the system and his groups, preferences and privileges
 *
 * @author asankha
 */
@Entity
@Table(name = "USERS", schema = "COMMON")
public class User {

    @Id
    @Column(updatable = false, length = 15)
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

    /** The preferred district */
    @Column(nullable = true)
    private Integer prefDistrict;
    /** The preferred Birth & Death registration division */
    @Column(nullable = true)
    private Integer prefBDDivision;
    /** The preferred Marriage registration division */
    @Column(nullable = true)
    private Integer prefMRDivision;

    /** The assigned district - if not null, the user can only work within this district */
    @Column(nullable = true)
    private Integer assignedDistrict;
    /** The assigned Birth & Death registration division - if not null, the user can only work within this division */
    @Column(nullable = true)
    private Integer assignedBDDivision;
    /** The assigned Marriage registration division - if not null, the user can only work within this division */
    @Column(nullable = true)
    private Integer assignedMRDivision;
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

    public int getPrefDistrict() {
        return prefDistrict;
    }

    public void setPrefDistrict(int prefDistrict) {
        this.prefDistrict = prefDistrict;
    }

    public int getPrefBDDivision() {
        return prefBDDivision;
    }

    public void setPrefBDDivision(int prefBDDivision) {
        this.prefBDDivision = prefBDDivision;
    }

    public int getPrefMRDivision() {
        return prefMRDivision;
    }

    public void setPrefMRDivision(int prefMRDivision) {
        this.prefMRDivision = prefMRDivision;
    }

    public int getAssignedDistrict() {
        return assignedDistrict;
    }

    public void setAssignedDistrict(int assignedDistrict) {
        this.assignedDistrict = assignedDistrict;
    }

    public int getAssignedBDDivision() {
        return assignedBDDivision;
    }

    public void setAssignedBDDivision(int assignedBDDivision) {
        this.assignedBDDivision = assignedBDDivision;
    }

    public int getAssignedMRDivision() {
        return assignedMRDivision;
    }

    public void setAssignedMRDivision(int assignedMRDivision) {
        this.assignedMRDivision = assignedMRDivision;
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

    public int getInitialDistrict() {
        if (assignedDistrict == null) {
            return prefDistrict == null ? 1 : prefDistrict;
        } else {
            return assignedDistrict;
        }
    }

    public int getInitialBDDivision() {
        if (assignedBDDivision == null) {
            return prefBDDivision == null ? 1 : prefBDDivision;
        } else {
            return assignedBDDivision;
        }
    }
}