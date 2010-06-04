package lk.rgd.common.api.domain;

import javax.persistence.*;
import java.util.BitSet;
import java.util.Set;

/**
 * Represents a Role defined in the system. A user may belong to one or more Roles
 *
 * @author asankha
 */
@Entity
@Table(name = "ROLES", schema = "COMMON")
public class Role {

    @Id
    @Column(updatable = false)
    private String roleId;
    /** Is the role status? - 0 : active, 1 - inactive */
    @Column(nullable = false, name="STATUS", columnDefinition="smallint not null default 1")
    private int status;
    /** Role name */
    @Column(nullable = false, length = 30, unique = true)
    private String name;

    @Transient
    /** The permission BitSet used at runtime - loaded from the permissions byte[] */
    private BitSet permBitSet;

    /** Used to persist the permission BitSet */
    @Lob
    @Column (length = 128)
    @Basic (fetch = FetchType.EAGER)
    private byte[] permissions;

    @ManyToMany(mappedBy = "roles")
    private Set<User> roles;

    public Role() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        if (roleId != null ? !roleId.equals(role.roleId) : role.roleId != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return roleId != null ? roleId.hashCode() : 0;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BitSet getPermBitSet() {
        return permBitSet;
    }

    public void setPermBitSet(BitSet permBitSet) {
        this.permBitSet = permBitSet;
        byte[] bytes = new byte[permBitSet.length()/8+1];
        for (int i=0; i<permBitSet.length(); i++) {
            if (permBitSet.get(i)) {
                bytes[bytes.length-i/8-1] |= 1<<(i%8);
            }
        }
        permissions = bytes;
    }

    public byte[] getPermissions() {
        return permissions;
    }

    public void setPermissions(byte[] permissions) {
        this.permissions = permissions;
        if (permissions != null) {
            for (int i=0; i<permissions.length*8; i++) {
                if ((permissions[permissions.length-i/8-1]&(1<<(i%8))) > 0) {
                    if (permBitSet == null) {
                        permBitSet = new BitSet();
                    }
                    permBitSet.set(i);
                }
            }
        }
    }

    @PostLoad
    private void init() {
        setPermissions(permissions);
    }
}