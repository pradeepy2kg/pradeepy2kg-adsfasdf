package lk.rgd.crs.api.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ashoka Ekanayaka
 *         Bean to contain all calculated summary statistics district wise. These will be used to export into CSV format and/or display in JSP
 */
public class BirthDistrictStatistics extends BirthStatistics {

    private int totalBirthFromDistricts;
    private int maleBirthFromDistricts;
    private int femaleBirthFromDistricts;
    public List<BirthMonthlyStatistics> monthlyTotals;
    public List<BirthChildRankStatistics> birthOrder;

    public static final int NO_OF_MONTHS = 12;

    public BirthDistrictStatistics() {
        monthlyTotals = new ArrayList<BirthMonthlyStatistics>();
        for (int i = 0; i < NO_OF_MONTHS; i++) {
            monthlyTotals.add(new BirthMonthlyStatistics());
        }
        birthOrder = new ArrayList<BirthChildRankStatistics>();
        for (int i = 0; i < 10; i++) {
            BirthChildRankStatistics bcs = new BirthChildRankStatistics();
            bcs.setRank(i);
            birthOrder.add(bcs);
        }
    }

    public int getFemaleBirthFromDistricts() {
        return femaleBirthFromDistricts;
    }

    public void setFemaleBirthFromDistricts(int femaleBirthFromDistricts) {
        this.femaleBirthFromDistricts = femaleBirthFromDistricts;
    }

    public int getMaleBirthFromDistricts() {
        return maleBirthFromDistricts;
    }

    public void setMaleBirthFromDistricts(int maleBirthFromDistricts) {
        this.maleBirthFromDistricts = maleBirthFromDistricts;
    }

    public int getTotalBirthFromDistricts() {
        return totalBirthFromDistricts;
    }

    public void setTotalBirthFromDistricts(int totalBirthFromDistricts) {
        this.totalBirthFromDistricts = totalBirthFromDistricts;
    }
}
