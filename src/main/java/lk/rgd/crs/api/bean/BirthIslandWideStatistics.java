package lk.rgd.crs.api.bean;

import java.util.ArrayList;

/**
 * @author Ashoka Ekanayaka
 * Bean to represent a statistics information of birth records island wide. 
 */
public class BirthIslandWideStatistics extends BirthStatistics {
    private static BirthIslandWideStatistics instance = new BirthIslandWideStatistics();

    public static BirthIslandWideStatistics getInstance() {
        return instance;    
    }

    private BirthIslandWideStatistics() {
        // todo initialization in here. instance.setTotal(total) etc
    }

    private ArrayList<BirthDistrictStatistics> totals = new ArrayList<BirthDistrictStatistics>(26);
}
