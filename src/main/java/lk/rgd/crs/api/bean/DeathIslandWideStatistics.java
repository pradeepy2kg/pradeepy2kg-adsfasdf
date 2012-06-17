package lk.rgd.crs.api.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shan
 */
public class DeathIslandWideStatistics {

    public static final int NO_OF_DISTRICTS = 26;
    public static final int UNKNOWN_DISTRICT_ID = 0;

    private int islandWideMale;
    private int islandWideFemale;
    private int islandWideTotal;

    public DeathDistrictStatistics districtStatisticsList[];

    public DeathIslandWideStatistics() {
        districtStatisticsList = new DeathDistrictStatistics[NO_OF_DISTRICTS];
        for (int i = 0; i < NO_OF_DISTRICTS; i++) {
            districtStatisticsList[i] = new DeathDistrictStatistics();
        }
    }

    public int getIslandWideMale() {
        return islandWideMale;
    }

    public void setIslandWideMale(int islandWideMale) {
        this.islandWideMale = islandWideMale;
    }

    public int getIslandWideFemale() {
        return islandWideFemale;
    }

    public void setIslandWideFemale(int islandWideFemale) {
        this.islandWideFemale = islandWideFemale;
    }

    public int getIslandWideTotal() {
        return islandWideTotal;
    }

    public void setIslandWideTotal(int islandWideTotal) {
        this.islandWideTotal = islandWideTotal;
    }
}
