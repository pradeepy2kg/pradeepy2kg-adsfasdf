package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.District;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a birth and death Registration BDDivision within a D.S. BDDivision of a District as maintained by the system.
 * A BDDivision has a unique ID, and multiple names in different languages that maps to the same ID
 *
 * Note: Marriage registration divisions and Birth/Death Registrar divisions are different
 *
 * @author asankha
 */
@Entity
@Table(name = "BD_DIVISIONS", schema = "CRS")
@IdClass (BDDivision.BDDivisionPK.class)
public class BDDivision implements Serializable {

    @ManyToOne
    @JoinColumn(name = "districtId", nullable = false, updatable = false)
    private District district;
    @Id
    private int districtId;
    @Id
    @Column(updatable = false)
    private int divisionId;
    @Column(nullable = false, length = 60, updatable = false)
    private String siDivisionName;
    @Column(nullable = false, length = 60, updatable = false)
    private String enDivisionName;
    @Column(nullable = false, length = 60, updatable = false)
    private String taDivisionName;
    @Column(name="active", columnDefinition="smallint not null default 1")
    private boolean active;

    public static class BDDivisionPK implements Serializable {
        private int districtId;
        private int divisionId;

        public BDDivisionPK() {
        }

        public BDDivisionPK(int districtId, int divisionId) {
            this.districtId = districtId;
            this.divisionId = divisionId;
        }

        public int getDistrictId() {
            return districtId;
        }

        public void setDistrictId(int districtId) {
            this.districtId = districtId;
        }

        public int getDivisionId() {
            return divisionId;
        }

        public void setDivisionId(int divisionId) {
            this.divisionId = divisionId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BDDivisionPK that = (BDDivisionPK) o;

            if (districtId != that.districtId) return false;
            if (divisionId != that.divisionId) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = districtId;
            result = 31 * result + divisionId;
            return result;
        }
    }

    public BDDivision() {}

    public BDDivision(District district, int divisionId,
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
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }
}