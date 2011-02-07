package lk.rgd.common.api.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a District Secretariat (D.S) Division
 *
 * @author asankha
 */
@Entity
@Table(name = "DS_DIVISIONS", schema = "COMMON",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"districtUKey", "divisionId"})})
@NamedQueries({
        @NamedQuery(name = "findAllDSDivisions", query = "SELECT d FROM DSDivision d"),
        @NamedQuery(name = "get.all.divisions.by.districtId", query = "SELECT d FROM DSDivision d WHERE d.district.districtUKey = :districtUKey"),
        @NamedQuery(name = "get.dsDivision.by.code", query = "SELECT d FROM DSDivision d" +
                " WHERE d.divisionId = :dsDivisionId AND d.district=:district")}
)
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class DSDivision implements Serializable {

    /**
     * This is a system generated unique key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dsDivisionUKey;

    /**
     * This is the standard G.N. Division ID as per the location code database
     */
    @Column(updatable = false, nullable = false)
    private int divisionId;

    @ManyToOne
    @JoinColumn(name = "districtUKey", nullable = false, updatable = false)
    private District district;

    @Column(nullable = false, length = 60, updatable = false)
    private String siDivisionName;
    @Column(nullable = false, length = 60, updatable = false)
    private String enDivisionName;
    @Column(nullable = false, length = 60, updatable = false)
    private String taDivisionName;

    /**
     * A D.S. Division maybe marked as inactive if one is split into two, or amalgamated to create a new one
     * The UI will only show D.S. Divisions that are currently active for every data entry form
     */
    @Column(name = "active", columnDefinition = "smallint not null default 1")
    private boolean active;

    public DSDivision() {
    }

    public DSDivision(District district, int divisionId,
                      String siDivisionName, String enDivisionName, String taDivisionName, boolean active) {
        this.district = district;
        this.divisionId = divisionId;
        this.siDivisionName = siDivisionName;
        this.enDivisionName = enDivisionName;
        this.taDivisionName = taDivisionName;
        this.active = active;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getSiDivisionName() {
        return siDivisionName;
    }

    public void setSiDivisionName(String siDivisionName) {
        this.siDivisionName = siDivisionName;
    }

    public String getEnDivisionName() {
        return enDivisionName;
    }

    public void setEnDivisionName(String enDivisionName) {
        this.enDivisionName = enDivisionName;
    }

    public String getTaDivisionName() {
        return taDivisionName;
    }

    public void setTaDivisionName(String taDivisionName) {
        this.taDivisionName = taDivisionName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getDistrictId() {
        return district.getDistrictId();
    }

    public void setDistrictId(int districtId) {
        this.district.setDistrictId(districtId);
    }

    public int getDsDivisionUKey() {
        return dsDivisionUKey;
    }

    public void setDsDivisionUKey(int dsDivisionUKey) {
        this.dsDivisionUKey = dsDivisionUKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DSDivision that = (DSDivision) o;

        if (dsDivisionUKey != that.dsDivisionUKey) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return dsDivisionUKey;
    }
}
