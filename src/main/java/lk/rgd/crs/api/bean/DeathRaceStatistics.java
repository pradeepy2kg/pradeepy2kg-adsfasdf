package lk.rgd.crs.api.bean;

/**
 * @author shan
 */
public class DeathRaceStatistics {

    public static final int NO_OF_AGE_GROUPS = 22;
    public static final int UNKNOWN_AGE_GROUP_ID = 0;

    private int raceMale;
    private int raceFemale;
    private int raceTotal;

    public DeathAgeGroupStatistics deathAgeGroupStatistics[];

    DeathRaceStatistics() {
        deathAgeGroupStatistics = new DeathAgeGroupStatistics[NO_OF_AGE_GROUPS];
        for(int i = 0; i < NO_OF_AGE_GROUPS; i++) {
            deathAgeGroupStatistics[i] = new DeathAgeGroupStatistics();
        }
    }

    public int getRaceMale() {
        return raceMale;
    }

    public void setRaceMale(int raceMale) {
        this.raceMale = raceMale;
    }

    public int getRaceFemale() {
        return raceFemale;
    }

    public void setRaceFemale(int raceFemale) {
        this.raceFemale = raceFemale;
    }

    public int getRaceTotal() {
        return raceTotal;
    }

    public void setRaceTotal(int raceTotal) {
        this.raceTotal = raceTotal;
    }
}
