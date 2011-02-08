package lk.rgd.crs.api.bean;

/**
 * @author shan
 */
public class DeathRaceStatistics {

    public static final int NO_OF_AGE_GROUPS = 6;

    public DeathAgeGroupStatistics deathAgeGroupStatistics[];

    DeathRaceStatistics() {
        deathAgeGroupStatistics = new DeathAgeGroupStatistics[NO_OF_AGE_GROUPS];
    }

}
