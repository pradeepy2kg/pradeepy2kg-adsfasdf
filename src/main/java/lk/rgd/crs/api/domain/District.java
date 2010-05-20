package lk.rgd.crs.api.domain;

import lk.rgd.AppConstants;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(name = "districts")
public class District implements Serializable {

    @Id
    private int districtId;
    private String siDistrictName;
    private String enDistrictName;
    private String taDistrictName;
    private boolean active;
    @OneToMany(mappedBy = "district")
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
