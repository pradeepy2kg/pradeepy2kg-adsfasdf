package lk.rgd.crs.api.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ashoka Ekanayaka
 * Bean to contain all calculated summary statistics district wise. These will be used to export into CSV format and/or display in JSP
 */
public class BirthDistrictStatistics extends BirthStatistics {
    private List<BirthMonthlyStatistics> totals = new ArrayList<BirthMonthlyStatistics>(12);
}
