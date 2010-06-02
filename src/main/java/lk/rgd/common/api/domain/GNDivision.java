package lk.rgd.common.api.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a Grama Niladhari (G.N) Division
 *
 * @author asankha
 */
@Entity
@Table(name = "GN_DIVISIONS", schema = "COMMON")
@IdClass(GNDivision.GNDivisionPK.class)
public class GNDivision implements Serializable {

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "dsDivisionId", nullable = false, updatable = false),
        @JoinColumn(name = "districtId", nullable = false, updatable = false)
    })
    private DSDivision dsDivision;
    @Id
    private int dsDivisionId;
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

    public static class GNDivisionPK implements Serializable {
        private int districtId;
        private int dsDivisionId;
        private int divisionId;

        public GNDivisionPK() {
        }

        public GNDivisionPK(int districtId, int dsDivisionId, int divisionId) {
            this.districtId = districtId;
            this.dsDivisionId = dsDivisionId;
            this.divisionId = divisionId;
        }

        public int getDistrictId() {
            return districtId;
        }

        public void setDistrictId(int districtId) {
            this.districtId = districtId;
        }

        public int getDsDivisionId() {
            return dsDivisionId;
        }

        public void setDsDivisionId(int dsDivisionId) {
            this.dsDivisionId = dsDivisionId;
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

            GNDivisionPK that = (GNDivisionPK) o;

            if (districtId != that.districtId) return false;
            if (divisionId != that.divisionId) return false;
            if (dsDivisionId != that.dsDivisionId) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = districtId;
            result = 31 * result + dsDivisionId;
            result = 31 * result + divisionId;
            return result;
        }
    }

    public GNDivision() {}

    public GNDivision(DSDivision dsDivision,
        String siDivisionName, String enDivisionName, String taDivisionName, boolean active) {
        this.dsDivision = dsDivision;
        this.siDivisionName = siDivisionName;
        this.enDivisionName = enDivisionName;
        this.taDivisionName = taDivisionName;
        this.active = active;
    }

    public DSDivision getDsDivision() {
        return dsDivision;
    }

    public void setDsDivision(DSDivision dsDivision) {
        this.dsDivision = dsDivision;
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

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }
}