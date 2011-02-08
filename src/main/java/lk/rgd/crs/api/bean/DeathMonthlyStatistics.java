package lk.rgd.crs.api.bean;

/**
 * @author shan
 */
public class DeathMonthlyStatistics {

    public static final int NO_OF_RACES = 13;
    public static final int UNKNOWN_RACE_ID = 0;

    private int monthMale;
    private int monthFemale;
    private int monthTotal;

    public DeathRaceStatistics deathRaceStatistics[];

    DeathMonthlyStatistics() {
        deathRaceStatistics = new DeathRaceStatistics[NO_OF_RACES];
        for(int i = 0; i < NO_OF_RACES; i++) {
            deathRaceStatistics[i] = new DeathRaceStatistics();
        }
    }

    public int getMonthMale() {
        return monthMale;
    }

    public void setMonthMale(int monthMale) {
        this.monthMale = monthMale;
    }

    public int getMonthFemale() {
        return monthFemale;
    }

    public void setMonthFemale(int monthFemale) {
        this.monthFemale = monthFemale;
    }

    public int getMonthTotal() {
        return monthTotal;
    }

    public void setMonthTotal(int monthTotal) {
        this.monthTotal = monthTotal;
    }
}
