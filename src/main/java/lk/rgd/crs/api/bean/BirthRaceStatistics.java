package lk.rgd.crs.api.bean;

import java.util.ArrayList;

/**
 * @author Ashoka Ekanayaka
 *         Bean to contain all calculated summary statistics race wise. These will be used to export into CSV format and/or display in JSP
 */
public class BirthRaceStatistics extends BirthStatistics {

    private int totalBirthFromRaces;
    private int maleBirthFromRaces;
    private int femaleBirthFromRaces;
    public ArrayList<BirthAgeGroupStatistics> ageGroupTotals;

    public static final int NO_OF_AGE_GROUPS = 9;

    public BirthRaceStatistics() {
        ageGroupTotals = new ArrayList<BirthAgeGroupStatistics>();
        for (int i = 2; i < NO_OF_AGE_GROUPS + 2; i++) {
            ageGroupTotals.add(new BirthAgeGroupStatistics());
        }
    }

    public int getFemaleBirthFromRaces() {
        return femaleBirthFromRaces;
    }

    public void setFemaleBirthFromRaces(int femaleBirthFromRaces) {
        this.femaleBirthFromRaces = femaleBirthFromRaces;
    }

    public int getMaleBirthFromRaces() {
        return maleBirthFromRaces;
    }

    public void setMaleBirthFromRaces(int maleBirthFromRaces) {
        this.maleBirthFromRaces = maleBirthFromRaces;
    }

    public int getTotalBirthFromRaces() {
        return totalBirthFromRaces;
    }

    public void setTotalBirthFromRaces(int totalBirthFromRaces) {
        this.totalBirthFromRaces = totalBirthFromRaces;
    }
}
