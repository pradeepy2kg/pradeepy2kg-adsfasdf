package lk.rgd.crs.api.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ashoka Ekanayaka
 * Bean to contain all calculated summary statistics for a single month. These will be used to export into CSV format and/or display in JSP
 */
public class BirthMonthlyStatistics extends BirthStatistics  {

    private int totalBirthFromMonths;
    private int maleBirthFromMonths;
    private int femaleBirthFromMonths;
    public List<BirthRaceStatistics> raceTotals; //todo get this number form race dao

    public static final int NO_OF_RACES = 13;

    public BirthMonthlyStatistics() {
        raceTotals = new ArrayList<BirthRaceStatistics>();
        for (int i=0; i<NO_OF_RACES; i++) {
            raceTotals.add(new BirthRaceStatistics());
        }
    }

    public int getFemaleBirthFromMonths() {
        return femaleBirthFromMonths;
    }

    public void setFemaleBirthFromMonths(int femaleBirthFromMonths) {
        this.femaleBirthFromMonths = femaleBirthFromMonths;
    }

    public int getMaleBirthFromMonths() {
        return maleBirthFromMonths;
    }

    public void setMaleBirthFromMonths(int maleBirthFromMonths) {
        this.maleBirthFromMonths = maleBirthFromMonths;
    }

    public int getTotalBirthFromMonths() {
        return totalBirthFromMonths;
    }

    public void setTotalBirthFromMonths(int totalBirthFromMonths) {
        this.totalBirthFromMonths = totalBirthFromMonths;
    }
}
