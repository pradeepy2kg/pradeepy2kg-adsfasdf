package lk.rgd.common.api.domain;

import org.omg.CORBA.portable.Streamable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author shan
 */

@Entity
@Table(name = "STATISTICS", schema = "COMMON")
@NamedQueries({
    @NamedQuery(name = "get.by.user", query = "SELECT s FROM Statistics s WHERE s.userId = :userId")
})
public class Statistics implements Serializable {

    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUkey;*/

    @Id
    private String userId;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdTimestamp;

    /* Births */
    @Column(name = "b_total_sub")
    private int birthsTotalSubmissions;

    @Column(name = "b_late_sub")
    private int birthsLateSubmissions;

    @Column(name = "b_noml_sub")
    private int birthsNormalSubmissions;

    @Column(name = "b_appr_itm")
    private int birthsApprovedItems;

    @Column(name = "b_rej_itm")
    private int birthsRejectedItems;

    @Column(name = "b_total_pnd_itm")
    private int birthsTotalPendingItems;

    @Column(name = "b_arr_pnd_itm")
    private int birthsArrearsPendingItems;

    @Column(name = "b_month_pnd_itm")
    private int birthsThisMonthPendingItems;

    /* Deaths */
    @Column(name = "d_total_sub")
    private int DeathsTotalSubmissions;

    @Column(name = "d_late_sub")
    private int DeathsLateSubmissions;

    @Column(name = "d_noml_sub")
    private int DeathsNormalSubmissions;

    @Column(name = "d_appr_itm")
    private int DeathsApprovedItems;

    @Column(name = "d_rej_itm")
    private int DeathsRejectedItems;

    @Column(name = "d_total_pen_itm")
    private int DeathsTotalPendingItems;

    @Column(name = "d_arr_pnd_itm")
    private int DeathsArrearsPendingItems;

    @Column(name = "d_month_pnd_itm")
    private int DeathsThisMonthPendingItems;

    /* Marriages */
    @Column(name = "m_total_sub")
    private int MrgTotalSubmissions;

    @Column(name = "m_late_sub")
    private int MrgLateSubmissions;

    @Column(name = "m_noml_sub")
    private int MrgNormalSubmissions;

    @Column(name = "m_appr_itm")
    private int MrgApprovedItems;

    @Column(name = "m_rej_itm")
    private int MrgRejectedItems;

    @Column(name = "m_total_itm")
    private int MrgTotalPendingItems;

    @Column(name = "m_arr_pnd_itm")
    private int MrgArrearsPendingItems;

    @Column(name = "m_month_pnd_itm")
    private int MrgThisMonthPendingItems;

    public Statistics() {

    }

    public int getBirthsArrearsPendingItems() {
        return birthsArrearsPendingItems;
    }

    public void setBirthsArrearsPendingItems(int birthsArrearsPendingItems) {
        this.birthsArrearsPendingItems = birthsArrearsPendingItems;
    }

    public int getBirthsApprovedItems() {
        return birthsApprovedItems;
    }

    public void setBirthsApprovedItems(int birthsApprovedItems) {
        this.birthsApprovedItems = birthsApprovedItems;
    }

    public int getBirthsLateSubmissions() {
        return birthsLateSubmissions;
    }

    public void setBirthsLateSubmissions(int birthsLateSubmissions) {
        this.birthsLateSubmissions = birthsLateSubmissions;
    }

    public int getBirthsNormalSubmissions() {
        return birthsNormalSubmissions;
    }

    public void setBirthsNormalSubmissions(int birthsNormalSubmissions) {
        this.birthsNormalSubmissions = birthsNormalSubmissions;
    }

    public int getBirthsRejectedItems() {
        return birthsRejectedItems;
    }

    public void setBirthsRejectedItems(int birthsRejectedItems) {
        this.birthsRejectedItems = birthsRejectedItems;
    }

    public int getBirthsThisMonthPendingItems() {
        return birthsThisMonthPendingItems;
    }

    public void setBirthsThisMonthPendingItems(int birthsThisMonthPendingItems) {
        this.birthsThisMonthPendingItems = birthsThisMonthPendingItems;
    }

    public int getBirthsTotalPendingItems() {
        return birthsTotalPendingItems;
    }

    public void setBirthsTotalPendingItems(int birthsTotalPendingItems) {
        this.birthsTotalPendingItems = birthsTotalPendingItems;
    }

    public int getDeathsApprovedItems() {
        return DeathsApprovedItems;
    }

    public void setDeathsApprovedItems(int deathsApprovedItems) {
        DeathsApprovedItems = deathsApprovedItems;
    }

    public int getBirthsTotalSubmissions() {
        return birthsTotalSubmissions;
    }

    public void setBirthsTotalSubmissions(int birthsTotalSubmissions) {
        this.birthsTotalSubmissions = birthsTotalSubmissions;
    }

    public int getDeathsArrearsPendingItems() {
        return DeathsArrearsPendingItems;
    }

    public void setDeathsArrearsPendingItems(int deathsArrearsPendingItems) {
        DeathsArrearsPendingItems = deathsArrearsPendingItems;
    }

    public int getDeathsNormalSubmissions() {
        return DeathsNormalSubmissions;
    }

    public void setDeathsNormalSubmissions(int deathsNormalSubmissions) {
        DeathsNormalSubmissions = deathsNormalSubmissions;
    }

    public int getDeathsRejectedItems() {
        return DeathsRejectedItems;
    }

    public void setDeathsRejectedItems(int deathsRejectedItems) {
        DeathsRejectedItems = deathsRejectedItems;
    }

    public int getDeathsTateSubmissions() {
        return DeathsLateSubmissions;
    }

    public void setDeathsTateSubmissions(int deathsLateSubmissions) {
        DeathsLateSubmissions = deathsLateSubmissions;
    }

    public int getDeathsThisMonthPendingItems() {
        return DeathsThisMonthPendingItems;
    }

    public void setDeathsThisMonthPendingItems(int deathsThisMonthPendingItems) {
        DeathsThisMonthPendingItems = deathsThisMonthPendingItems;
    }

    public int getDeathsTotalPendingItems() {
        return DeathsTotalPendingItems;
    }

    public void setDeathsTotalPendingItems(int deathsTotalPendingItems) {
        DeathsTotalPendingItems = deathsTotalPendingItems;
    }

    public int getDeathsTotalSubmissions() {
        return DeathsTotalSubmissions;
    }

    public void setDeathsTotalSubmissions(int deathsTotalSubmissions) {
        DeathsTotalSubmissions = deathsTotalSubmissions;
    }

    /*public long getIdUkey() {
        return idUkey;
    }

    public void setIdUkey(long idUkey) {
        this.idUkey = idUkey;
    }*/

    public int getMrgApprovedItems() {
        return MrgApprovedItems;
    }

    public void setMrgApprovedItems(int mrgApprovedItems) {
        MrgApprovedItems = mrgApprovedItems;
    }

    public int getMrgArrearsPendingItems() {
        return MrgArrearsPendingItems;
    }

    public void setMrgArrearsPendingItems(int mrgArrearsPendingItems) {
        MrgArrearsPendingItems = mrgArrearsPendingItems;
    }

    public int getMrgNormalSubmissions() {
        return MrgNormalSubmissions;
    }

    public void setMrgNormalSubmissions(int mrgNormalSubmissions) {
        MrgNormalSubmissions = mrgNormalSubmissions;
    }

    public int getMrgRejectedItems() {
        return MrgRejectedItems;
    }

    public void setMrgRejectedItems(int mrgRejectedItems) {
        MrgRejectedItems = mrgRejectedItems;
    }

    public int getMrgTateSubmissions() {
        return MrgLateSubmissions;
    }

    public void setMrgTateSubmissions(int mrgLateSubmissions) {
        MrgLateSubmissions = mrgLateSubmissions;
    }

    public int getMrgThisMonthPendingItems() {
        return MrgThisMonthPendingItems;
    }

    public void setMrgThisMonthPendingItems(int mrgThisMonthPendingItems) {
        MrgThisMonthPendingItems = mrgThisMonthPendingItems;
    }

    public int getMrgTotalPendingItems() {
        return MrgTotalPendingItems;
    }

    public void setMrgTotalPendingItems(int mrgTotalPendingItems) {
        MrgTotalPendingItems = mrgTotalPendingItems;
    }

    public int getMrgTotalSubmissions() {
        return MrgTotalSubmissions;
    }

    public void setMrgTotalSubmissions(int mrgTotalSubmissions) {
        MrgTotalSubmissions = mrgTotalSubmissions;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public int getDeathsLateSubmissions() {
        return DeathsLateSubmissions;
    }

    public void setDeathsLateSubmissions(int deathsLateSubmissions) {
        DeathsLateSubmissions = deathsLateSubmissions;
    }

    public int getMrgLateSubmissions() {
        return MrgLateSubmissions;
    }

    public void setMrgLateSubmissions(int mrgLateSubmissions) {
        MrgLateSubmissions = mrgLateSubmissions;
    }

    public String getUser() {
        return userId;
    }

    public void setUser(String userId) {
        this.userId = userId;
    }
}
