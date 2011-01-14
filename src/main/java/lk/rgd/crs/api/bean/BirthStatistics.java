package lk.rgd.crs.api.bean;

import java.util.List;

/**
 * @author Ashoka Ekanayaka
 * Bean to represent a statistics information of birth record in a given group divided onto it's subgroups.
 * This is the high level class from which all other statistics beans will be inherited from
 */
public abstract class BirthStatistics {
    private int total;
    private int maleTotal;
    private int femaleTotal;
    //private List<BirthStatistics> totals;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getMaleTotal() {
        return maleTotal;
    }

    public void setMaleTotal(int maleTotal) {
        this.maleTotal = maleTotal;
    }

    public int getFemaleTotal() {
        return femaleTotal;
    }

    public void setFemaleTotal(int femaleTotal) {
        this.femaleTotal = femaleTotal;
    }
}
