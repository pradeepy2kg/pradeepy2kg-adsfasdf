package lk.rgd.crs.api.domain;

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
@Table(name = "bd_divisions")
public class BDDivision implements Serializable {

    //@Id
    @ManyToOne
    @JoinColumn(name = "districtId")
    private District district;
    @Id
    private int divisionId;
    private String siDivisionName;
    private String enDivisionName;
    private String taDivisionName;
    private boolean active;

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
}