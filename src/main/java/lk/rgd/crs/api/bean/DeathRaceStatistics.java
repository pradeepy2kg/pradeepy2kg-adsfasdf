package lk.rgd.crs.api.bean;

/**
 * @author shan
 */
public class DeathRaceStatistics {

    public static final int NO_OF_AGE_GROUPS = 22;
    public static final int UNKNOWN_AGE_GROUP_ID = 0;

    public DeathAgeGroupStatistics deathAgeGroupStatistics[];

    DeathRaceStatistics() {
        deathAgeGroupStatistics = new DeathAgeGroupStatistics[NO_OF_AGE_GROUPS];
        for(int i = 0; i < NO_OF_AGE_GROUPS; i++) {
            deathAgeGroupStatistics[i] = new DeathAgeGroupStatistics();
        }
    }

}
