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

    private int legitimacyBirths;
    private int illegitimacyBirths;
    private int hospitalBirths;

    private float proportion;

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

    public int getHospitalBirths() {
        return hospitalBirths;
    }

    public void setHospitalBirths(int hospitalBirths) {
        this.hospitalBirths = hospitalBirths;
    }

    public int getIllegitimacyBirths() {
        return illegitimacyBirths;
    }

    public void setIllegitimacyBirths(int illegitimacyBirths) {
        this.illegitimacyBirths = illegitimacyBirths;
    }

    public int getLegitimacyBirths() {
        return legitimacyBirths;
    }

    public void setLegitimacyBirths(int legitimacyBirths) {
        this.legitimacyBirths = legitimacyBirths;
    }

    public float getProportion() {
        return proportion;
    }

    public void setProportion(float proportion) {
        this.proportion = proportion;
    }
}
