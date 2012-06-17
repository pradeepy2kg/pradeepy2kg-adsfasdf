package lk.rgd.crs.api.domain;

/**
 * An instance representing old birth declaration information used for declaration of a birth of adopted child
 *
 * @author Chathuranga Withana
 */
public class OldBDInfo {
    private String districtName;
    private String dsDivisionName;
    private String bdDivisionName;
    private String gnDivisionName;
    private Long serialNumber;

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDsDivisionName() {
        return dsDivisionName;
    }

    public void setDsDivisionName(String dsDivisionName) {
        this.dsDivisionName = dsDivisionName;
    }

    public String getBdDivisionName() {
        return bdDivisionName;
    }

    public void setBdDivisionName(String bdDivisionName) {
        this.bdDivisionName = bdDivisionName;
    }

    public Long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getGnDivisionName() {
        return gnDivisionName;
    }

    public void setGnDivisionName(String gnDivisionName) {
        this.gnDivisionName = gnDivisionName;
    }
}
