package lk.rgd.crs.api.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ashoka Ekanayaka
 * Bean to contain all calculated summary statistics for a single month. These will be used to export into CSV format and/or display in JSP
 */
public class BirthMonthlyStatistics extends BirthStatistics  {
    public BirthMonthlyStatistics() {
        int length = totals.size();
        for (int i=0; i<length; i++) {
            totals.set(i, new BirthRaceStatistics());
        }
    }

    public List<BirthRaceStatistics> totals = new ArrayList<BirthRaceStatistics>(20); //todo get this number form race dao
}
