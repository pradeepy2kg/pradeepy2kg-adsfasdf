package lk.rgd.crs.api.bean;

/**
 * @author shan
 */
public class BirthDistrictYearStatistics {
    public int before_last_year_month_array[][];
    public int during_last_year_month_array[][];
    public int during_this_year_month_array[][];
    public int total_year_month_array[][];

    public BirthDistrictYearStatistics() {
        before_last_year_month_array = new int[12][3];
        during_last_year_month_array = new int[12][3];
        during_this_year_month_array = new int[12][3];
        total_year_month_array = new int[12][3];
    }
}
