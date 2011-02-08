package lk.rgd.crs.api.bean;

/**
 * @author shan
 */
public class DeathMonthlyStatistics {

    public static final int NO_OF_RACES = 13;
    public static final int UNKNOWN_RACE_ID = 0;

    public DeathRaceStatistics deathRaceStatistics[];

    DeathMonthlyStatistics() {
        deathRaceStatistics = new DeathRaceStatistics[NO_OF_RACES];
        for(int i = 0; i < NO_OF_RACES; i++) {
            deathRaceStatistics[i] = new DeathRaceStatistics();
        }
    }

}
