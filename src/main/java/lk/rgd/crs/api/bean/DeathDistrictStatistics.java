package lk.rgd.crs.api.bean;

/**
 * @author shan
 */
public class DeathDistrictStatistics {

    public static final int NO_OF_MONTHS = 12;
    public static final int UNKNOWN_MONTH_ID = 0;

    private int districtMale;
    private int districtFemale;
    private int districtTotal;

    public DeathMonthlyStatistics deathMonthlyStatistics[];

    DeathDistrictStatistics() {
        deathMonthlyStatistics = new DeathMonthlyStatistics[NO_OF_MONTHS];
        for(int i = 0; i < NO_OF_MONTHS; i++) {
            deathMonthlyStatistics[i] = new DeathMonthlyStatistics();
        }
    }

    public int getDistrictMale() {
        return districtMale;
    }

    public void setDistrictMale(int districtMale) {
        this.districtMale = districtMale;
    }

    public int getDistrictFemale() {
        return districtFemale;
    }

    public void setDistrictFemale(int districtFemale) {
        this.districtFemale = districtFemale;
    }

    public int getDistrictTotal() {
        return districtTotal;
    }

    public void setDistrictTotal(int districtTotal) {
        this.districtTotal = districtTotal;
    }
}
