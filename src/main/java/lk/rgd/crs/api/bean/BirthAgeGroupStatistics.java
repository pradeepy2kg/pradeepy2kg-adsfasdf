package lk.rgd.crs.api.bean;

import java.util.ArrayList;

/**
 * @author Ashoka Ekanayaka
 * Bean to contain all calculated summary statistics age group wise. These will be used to export into CSV format and/or display in JSP
 */
public class BirthAgeGroupStatistics extends BirthStatistics {
    private int totalBirths;
    private int femaleBirths;
    private int maleBirths;

    public int getFemaleBirths() {
        return femaleBirths;
    }

    public void setFemaleBirths(int femaleBirths) {
        this.femaleBirths = femaleBirths;
    }

    public int getMaleBirths() {
        return maleBirths;
    }

    public void setMaleBirths(int maleBirths) {
        this.maleBirths = maleBirths;
    }

    public int getTotalBirths() {
        return totalBirths;
    }

    public void setTotalBirths(int totalBirths) {
        this.totalBirths = totalBirths;
    }
}
