package lk.rgd.crs.api.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shan
 */
public class DeathIslandWideStatistics {

    public static final int NO_OF_DISTRICTS = 26;
    public static final int UNKNOWN_DISTRICT_ID = 0;

    public DeathDistrictStatistics districtStatisticsList[];
    private static DeathIslandWideStatistics deathIslandWideStatistics;

    public DeathIslandWideStatistics() {
        districtStatisticsList = new DeathDistrictStatistics[NO_OF_DISTRICTS];
        for (int i = 0; i < NO_OF_DISTRICTS; i++) {
            districtStatisticsList[i] = new DeathDistrictStatistics();
        }
    }
}
