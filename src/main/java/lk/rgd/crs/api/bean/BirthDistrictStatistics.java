package lk.rgd.crs.api.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ashoka Ekanayaka
 * Bean to contain all calculated summary statistics district wise. These will be used to export into CSV format and/or display in JSP
 */
public class BirthDistrictStatistics extends BirthStatistics {
    public BirthDistrictStatistics() {
        int length = totals.size();
        for (int i=0; i<length; i++) {
            totals.set(i, new BirthMonthlyStatistics());
        }
    }

    public List<BirthMonthlyStatistics> totals = new ArrayList<BirthMonthlyStatistics>(12);
}
