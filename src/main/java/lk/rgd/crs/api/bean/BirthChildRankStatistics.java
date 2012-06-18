package lk.rgd.crs.api.bean;

public class BirthChildRankStatistics {
    private int rank;
    private int maleTotal;
    private int femaleTotal;
    private int total;

    public int getFemaleTotal() {
        return femaleTotal;
    }

    public void setFemaleTotal(int femaleTotal) {
        this.femaleTotal = femaleTotal;
    }

    public int getMaleTotal() {
        return maleTotal;
    }

    public void setMaleTotal(int maleTotal) {
        this.maleTotal = maleTotal;
    }

    public int getRank() {
        return rank;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
