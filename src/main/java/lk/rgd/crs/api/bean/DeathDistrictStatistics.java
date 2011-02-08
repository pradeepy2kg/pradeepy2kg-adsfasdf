package lk.rgd.crs.api.bean;

/**
 * @author shan
 */
public class DeathDistrictStatistics {

    public static final int NO_OF_MONTHS = 12;
    public static final int UNKNOWN_MONTH_ID = 0;

    public DeathMonthlyStatistics deathMonthlyStatistics[];

    DeathDistrictStatistics() {
        deathMonthlyStatistics = new DeathMonthlyStatistics[NO_OF_MONTHS];
    }

}
