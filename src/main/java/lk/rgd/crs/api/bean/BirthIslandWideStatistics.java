package lk.rgd.crs.api.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ashoka Ekanayaka
 *         Bean to represent a statistics information of birth records island wide.
 */
public class BirthIslandWideStatistics extends BirthStatistics {
    //todo remove the singleton pattern and let Spring handle it.
    public List<BirthDistrictStatistics> totals; //todo get this from district dao
    public static final int NO_OF_DISTRICTS = 26;
    private static BirthIslandWideStatistics instance = new BirthIslandWideStatistics();

    public static BirthIslandWideStatistics getInstance() {
        return instance;
    }

    private BirthIslandWideStatistics() {
        totals = new ArrayList<BirthDistrictStatistics>();
        // todo initialization in here. instance.setTotal(total) etc
        // int length = totals.size();
        for (int i = 0; i < NO_OF_DISTRICTS; i++) {
            totals.add(new BirthDistrictStatistics());
        }
    }
}
