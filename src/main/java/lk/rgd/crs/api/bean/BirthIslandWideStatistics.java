package lk.rgd.crs.api.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ashoka Ekanayaka
 * Bean to represent a statistics information of birth records island wide. 
 */
public class BirthIslandWideStatistics extends BirthStatistics {
    //todo remove the singleton pattern and let Spring handle it.
    private static BirthIslandWideStatistics instance = new BirthIslandWideStatistics();

    public static BirthIslandWideStatistics getInstance() {
        return instance;    
    }

    private BirthIslandWideStatistics() {
        // todo initialization in here. instance.setTotal(total) etc
        int length = totals.size();
        for (int i=0; i<length; i++) {
            totals.set(i, new BirthDistrictStatistics());
        }
    }

    public List<BirthDistrictStatistics> totals = new ArrayList<BirthDistrictStatistics>(26); //todo get this from district dao
}
