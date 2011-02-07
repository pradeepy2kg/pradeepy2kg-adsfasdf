package lk.rgd.common.api.domain;

import lk.rgd.AppConstants;
import lk.rgd.crs.api.domain.BDDivision;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
@NamedQueries({
        @NamedQuery(name = "findAllDistricts", query = "SELECT d FROM District d"),

        @NamedQuery(name = "get.district.by.code", query = "SELECT d FROM District d " + "WHERE d.districtId =:districtId")
})
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class District implements Serializable {

    /**
     * This is a system generated unique key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int districtUKey;

    /**
     * This is the standard District ID as per the location code database
     */
    @Column(updatable = false, unique = true, nullable = false)
    private int districtId;

    @Column(nullable = false, length = 30, unique = true, updatable = false)
    private String siDistrictName;
    @Column(nullable = false, length = 30, unique = true, updatable = false)
    private String enDistrictName;
    @Column(nullable = false, length = 30, unique = true, updatable = false)
    private String taDistrictName;

    /**
     * A District maybe marked as inactive if one is split into two, or amalgamated to create a new one
     * The UI will only show Districts that are currently active for every data entry form
     */
    @Column(name = "active", columnDefinition = "smallint not null default 1")
    private boolean active;

    public District() {
    }

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

    public int getDistrictUKey() {
        return districtUKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        District district = (District) o;

        if (districtUKey != district.districtUKey) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return districtUKey;
    }
}
