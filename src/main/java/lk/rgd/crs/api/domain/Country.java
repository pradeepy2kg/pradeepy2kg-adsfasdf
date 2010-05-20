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
    private int id;
    private int countryId;
    private String countryName;
    private String languageId;

    public Country() {}

    public Country(int countryId, String countryName, String languageId) {
        this.countryId = countryId;
        this.countryName = countryName;
        this.languageId = languageId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }
}