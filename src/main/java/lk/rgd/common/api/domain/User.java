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
@Table(name = "users")
public class User {

    @Id
    private String userId;
    /** The simple name of the user */
    private String userName;
    /** This is the PIN of the actual user in the PRS */
    private String pin;
    /** The SHA-1 hash of the password */
    private String passwordHash;
    /** The preferred language */
    private String prefLanguage;

    /** The preferred district */
    private String prefDistrict;
    /** The preferred Birth & Death registration division */
    private String prefBDDivision;
    /** The preferred Marriage registration division */
    private String prefMRDivision;

    /** The assigned district - if not null, the user can only work within this district */
    private String assignedDistrict;
    /** The assigned Birth & Death registration division - if not null, the user can only work within this division */
    private String assignedBDDivision;
    /** The assigned Marriage registration division - if not null, the user can only work within this division */
    private String assignedMRDivision;
    /** Is the user account active - 0 : active, 1 - inactive, 2 - locked out */
    private int status;

    @ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "USER_ROLES",
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

    public String getPrefDistrict() {
        return prefDistrict;
    }

    public void setPrefDistrict(String prefDistrict) {
        this.prefDistrict = prefDistrict;
    }

    public String getPrefBDDivision() {
        return prefBDDivision;
    }

    public void setPrefBDDivision(String prefBDDivision) {
        this.prefBDDivision = prefBDDivision;
    }

    public String getPrefMRDivision() {
        return prefMRDivision;
    }

    public void setPrefMRDivision(String prefMRDivision) {
        this.prefMRDivision = prefMRDivision;
    }

    public String getAssignedDistrict() {
        return assignedDistrict;
    }

    public void setAssignedDistrict(String assignedDistrict) {
        this.assignedDistrict = assignedDistrict;
    }

    public String getAssignedBDDivision() {
        return assignedBDDivision;
    }

    public void setAssignedBDDivision(String assignedBDDivision) {
        this.assignedBDDivision = assignedBDDivision;
    }

    public String getAssignedMRDivision() {
        return assignedMRDivision;
    }

    public void setAssignedMRDivision(String assignedMRDivision) {
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
}