package lk.rgd.crs.api.bean;

import java.util.ArrayList;

/**
 * @author Ashoka Ekanayaka
 * Bean to contain all calculated summary statistics race wise. These will be used to export into CSV format and/or display in JSP
 */
public class BirthRaceStatistics extends BirthStatistics  {
    public BirthRaceStatistics() {
        int length = totals.size();
        for (int i=0; i<length; i++) {
            totals.set(i, new BirthAgeGroupStatistics());
        }
    }

    private ArrayList <BirthAgeGroupStatistics> totals = new ArrayList<BirthAgeGroupStatistics>(5); //todo decide on the supported age groups and count
}
