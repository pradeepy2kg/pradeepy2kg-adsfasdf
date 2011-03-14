package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a Marriage Registration MRDivision within a D.S. of a District as maintained by the system.
 * A MRDivision has a unique ID, and multiple names in different languages that maps to the same ID
 * <p/>
 * Note: Marriage registration divisions and Birth/Death Registrar divisions are different
 *
 * @author asankha
 */
@Entity
@Table(name = "MR_DIVISIONS", schema = "CRS",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"dsDivisionUKey", "divisionId"})})
@NamedQueries({
        @NamedQuery(name = "findAllMRDivisions", query = "SELECT d FROM MRDivision d"),
        @NamedQuery(name = "get.mrDivision.by.code", query = "SELECT d FROM MRDivision d " +
                "WHERE d.divisionId=:mrDivisionId AND d.dsDivision=:dsDivision")
})
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class MRDivision implements Serializable {

    /**
     * This is a system generated unique key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mrDivisionUKey;

    /**
     * This is the standard M.R. Division ID as per the RGD Code List
     */
    @Column(updatable = false, nullable = false)
    private int divisionId;

    @ManyToOne
    @JoinColumn(name = "dsDivisionUKey", nullable = false, updatable = false)
    private DSDivision dsDivision;

    @Column(nullable = false, length = 60, updatable = false)
    private String siDivisionName;
    @Column(nullable = false, length = 60, updatable = false)
    private String enDivisionName;
    @Column(nullable = false, length = 60, updatable = false)
    private String taDivisionName;

    /**
     * A B.D. Division maybe marked as inactive if one is split into two, or amalgamated to create a new one
     * The UI will only show M.R. Divisions that are currently active for every data entry form
     */
    @Column(name = "active", columnDefinition = "smallint not null default 1")
    private boolean active;

    public MRDivision() {
    }

    public MRDivision(DSDivision dsDivision, int divisionId,
                      String siDivisionName, String enDivisionName, String taDivisionName, boolean active) {
        this.dsDivision = dsDivision;
        this.divisionId = divisionId;
        this.siDivisionName = siDivisionName;
        this.enDivisionName = enDivisionName;
        this.taDivisionName = taDivisionName;
        this.active = active;
    }

    public District getDistrict() {
        return dsDivision.getDistrict();
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

    public int getMrDivisionUKey() {
        return mrDivisionUKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MRDivision that = (MRDivision) o;

        if (mrDivisionUKey != that.mrDivisionUKey) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return mrDivisionUKey;
    }
}
