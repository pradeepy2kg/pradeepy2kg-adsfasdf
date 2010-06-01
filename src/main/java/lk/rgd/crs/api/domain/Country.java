package lk.rgd.crs.api.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a Country maintained by the system. A country has a unique ID, and multiple names in
 * different languages that maps to the same ID
 *
 * @author asankha
 */
@Entity
@Table(name = "countries")
public class Country {

    @Id
    private int countryId;
    private String siCountryName;
    private String enCountryName;
    private String taCountryName;
    private boolean active;

    public Country() {}

    public Country(int countryId, String siCountryName, String enCountryName, String taCountryName, boolean active) {
        this.countryId = countryId;
        this.siCountryName = siCountryName;
        this.enCountryName = enCountryName;
        this.taCountryName = taCountryName;
        this.active = active;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getSiCountryName() {
        return siCountryName;
    }

    public void setSiCountryName(String siCountryName) {
        this.siCountryName = siCountryName;
    }

    public String getEnCountryName() {
        return enCountryName;
    }

    public void setEnCountryName(String enCountryName) {
        this.enCountryName = enCountryName;
    }

    public String getTaCountryName() {
        return taCountryName;
    }

    public void setTaCountryName(String taCountryName) {
        this.taCountryName = taCountryName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}