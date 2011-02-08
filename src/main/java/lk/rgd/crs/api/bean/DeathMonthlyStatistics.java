package lk.rgd.crs.api.bean;

/**
 * @author shan
 */
public class DeathMonthlyStatistics {

    public static final int NO_OF_RACES = 13;

    public DeathRaceStatistics deathRaceStatistics[];

    DeathMonthlyStatistics() {
        deathRaceStatistics = new DeathRaceStatistics[NO_OF_RACES];
    }

}
