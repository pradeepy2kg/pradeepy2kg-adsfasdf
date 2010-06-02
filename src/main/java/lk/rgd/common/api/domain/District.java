package lk.rgd.common.api.domain;

import lk.rgd.AppConstants;
import lk.rgd.crs.api.domain.BDDivision;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a District maintained by the system. A district has a unique ID, and multiple names in
 * different languages that maps to the same ID
 *
 * @author chathuranga
 */
@Entity
@Table(name = "DISTRICTS", schema = "COMMON")
public class District implements Serializable {

    @Id
    @Column(updatable = false)
    private int districtId;
    @Column(nullable = false, unique = true, updatable = false)
    private String siDistrictName;
    @Column(nullable = false, length = 30, unique = true, updatable = false)
    private String enDistrictName;
    @Column(nullable = false, length = 30, unique = true, updatable = false)
    private String taDistrictName;
    @Column(name="active", columnDefinition="smallint not null default 1")
    private boolean active;
    @OneToMany(mappedBy = "district", targetEntity = BDDivision.class)
    private Set<BDDivision> bdDivisions = new HashSet<BDDivision>();

    public District() {}

    public District(int districtId, String siDistrictName, String enDistrictName, String taDistrictName, boolean active) {
        this.districtId = districtId;
        this.siDistrictName = siDistrictName;
        this.enDistrictName = enDistrictName;
        this.taDistrictName = taDistrictName;
        this.active = active;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getSiDistrictName() {
        return siDistrictName;
    }

    public void setSiDistrictName(String siDistrictName) {
        this.siDistrictName = siDistrictName;
    }

    public String getEnDistrictName() {
        return enDistrictName;
    }

    public void setEnDistrictName(String enDistrictName) {
        this.enDistrictName = enDistrictName;
    }

    public String getTaDistrictName() {
        return taDistrictName;
    }

    public void setTaDistrictName(String taDistrictName) {
        this.taDistrictName = taDistrictName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<BDDivision> getBdDivisions() {
        return bdDivisions;
    }
}
