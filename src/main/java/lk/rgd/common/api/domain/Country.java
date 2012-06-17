package lk.rgd.common.api.domain;

import lk.rgd.prs.api.domain.PersonCitizenship;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Country maintained by the system. A country has a unique ID, and multiple names in
 * different languages that maps to the same ID
 *
 * @author asankha
 */
@Entity
@Table(name = "COUNTRIES", schema = "COMMON")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Country implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private int countryId;
    @Column(nullable = false, length = 2, unique = true, updatable = false)
    private String countryCode;
    @Column(nullable = false, length = 60, unique = true, updatable = false)
    private String siCountryName;
    @Column(nullable = false, length = 60, unique = true, updatable = false)
    private String enCountryName;
    @Column(nullable = false, length = 60, unique = true, updatable = false)
    private String taCountryName;
    @Column(name = "active", columnDefinition = "smallint not null default 1")
    private boolean active;

    public Country() {
    }

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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Country country = (Country) o;

        if (countryId != country.countryId) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return countryId;
    }
}