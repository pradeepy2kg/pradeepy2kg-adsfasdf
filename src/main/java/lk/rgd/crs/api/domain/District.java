package lk.rgd.crs.api.domain;

import lk.rgd.AppConstants;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a District maintained by the system. A district has a unique ID, and multiple names in
 * different languages that maps to the same ID
 *
 * @author chathuranga
 */
@Entity
@Table(name = "districts")
public class District {

    @Id
    private int id;
    private int districtId;
    private String districtName;
    private String languageId;

    public District() {}

    public District(int districtId, String districtName) {
        this(districtId, districtName, AppConstants.ENGLISH);
    }

    public District(int districtId, String districtName, String languageId) {
        this.districtId = districtId;
        this.districtName = districtName;
        this.languageId = languageId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }
}
