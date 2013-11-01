package lk.rgd.crs.api.bean;

/**
 * @author Sisiruwan Senadeera
 *         Bean to represent a bd serial number range for a bdDivision and for a give period.
 */
public class BirthDivisionSerialNumberStatistics {
    private int bdDivisionUkey;
    private String bdDivisionName;
    private String serialNumberRange;

    public int getBdDivisionUkey() {
        return bdDivisionUkey;
    }

    public void setBdDivisionUkey(int bdDivisionUkey) {
        this.bdDivisionUkey = bdDivisionUkey;
    }

    public String getBdDivisionName() {
        return bdDivisionName;
    }

    public void setBdDivisionName(String bdDivisionName) {
        this.bdDivisionName = bdDivisionName;
    }

    public String getSerialNumberRange() {
        return serialNumberRange;
    }

    public void setSerialNumberRange(String serialNumberRange) {
        this.serialNumberRange = serialNumberRange;
    }
}
