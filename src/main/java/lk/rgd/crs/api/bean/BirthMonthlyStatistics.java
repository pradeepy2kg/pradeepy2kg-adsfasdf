package lk.rgd.crs.api.bean;

import java.util.ArrayList;

/**
 * @author Ashoka Ekanayaka
 * Bean to contain all calculated summary statistics for a single month. These will be used to export into CSV format and/or display in JSP
 */
public class BirthMonthlyStatistics extends BirthStatistics  {
    private ArrayList <BirthRaceStatistics> totals = new ArrayList<BirthRaceStatistics>(20); //todo get this number form race dao
}