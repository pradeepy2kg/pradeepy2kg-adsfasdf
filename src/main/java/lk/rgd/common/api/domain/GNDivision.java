package lk.rgd.common.api.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a Grama Niladhari (G.N) Division
 *
 * @author asankha
 */
@Entity
@Table(name = "GN_DIVISIONS", schema = "COMMON",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"dsDivisionUKey", "divisionId"})})
public class GNDivision implements Serializable {

    /**
     * This is a system generated unique key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gnDivisionUKey;

    /**
     * This is the standard G.N. Division ID as per the location code database
     */
    @Column(updatable = false, unique = true)
    private int divisionId;

    /**
     * The relationship to D.S. Division to which this G.N. Division belongs
     */
    @ManyToOne
    @JoinColumn(name = "dsDivisionUKey")
    private DSDivision dsDivision;

    @Column(nullable = false, length = 60, updatable = false)
    private String siDivisionName;
    @Column(nullable = false, length = 60, updatable = false)
    private String enDivisionName;
    @Column(nullable = false, length = 60, updatable = false)
    private String taDivisionName;

    /**
     * A G.N. Division maybe marked as inactive if one is split into two, or amalgamated to create a new one
     * The UI will only show G.N. Divisions that are currently active for every data entry form
     */
    @Column(name="active", columnDefinition="smallint not null default 1")
    private boolean active;

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
        return dsDivision.getDivisionId();
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivision.setDivisionId(dsDivisionId);
    }

    public int getDistrictId() {
        return dsDivision.getDistrictId();
    }

    public void setDistrictId(int districtId) {
        this.dsDivision.setDistrictId(districtId);
    }

    public int getGnDivisionUKey() {
        return gnDivisionUKey;
    }

    public void setGnDivisionUKey(int gnDivisionUKey) {
        this.gnDivisionUKey = gnDivisionUKey;
    }
}