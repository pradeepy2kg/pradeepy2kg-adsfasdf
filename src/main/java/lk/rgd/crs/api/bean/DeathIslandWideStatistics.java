package lk.rgd.crs.api.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shan
 */
public class DeathIslandWideStatistics {

    public static final int NO_OF_DISTRICTS = 26;

    public DeathDistrictStatistics districtStatisticsList[];
    private static DeathIslandWideStatistics deathIslandWideStatistics;

    private DeathIslandWideStatistics() {
        districtStatisticsList = new DeathDistrictStatistics[NO_OF_DISTRICTS];
    }

    public static DeathIslandWideStatistics getInstance() {
        if(deathIslandWideStatistics == null) {
            deathIslandWideStatistics = new DeathIslandWideStatistics();
        }
        return deathIslandWideStatistics;
    }

}
