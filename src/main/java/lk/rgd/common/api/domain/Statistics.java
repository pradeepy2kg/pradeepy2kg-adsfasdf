package lk.rgd.common.api.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author shan
 */

@Entity
@Table(name = "STATISTICS", schema = "COMMON")
public class Statistics implements Serializable {

    @Id
    private String userId;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdTimestamp;

    /* Births */
    private int birthsTotalSubmissions;

    private int birthsLateSubmissions;

    private int birthsNormalSubmissions;

    @Transient
    private int birthsStillSubmissions;

    private int birthsApprovedItems;

    private int birthsRejectedItems;

    private int birthsTotalPendingItems;

    private int birthsArrearsPendingItems;

    private int birthsThisMonthPendingItems;

    /* Deaths */
    private int DeathsTotalSubmissions;

    private int DeathsLateSubmissions;

    private int DeathsNormalSubmissions;

    private int DeathsApprovedItems;

    private int DeathsRejectedItems;

    private int DeathsTotalPendingItems;

    private int DeathsArrearsPendingItems;

    private int DeathsThisMonthPendingItems;

    /* Marriages */
    private int MrgTotalSubmissions;

    private int MrgLateSubmissions;

    private int MrgNormalSubmissions;

    private int MrgApprovedItems;

    private int MrgRejectedItems;

    private int MrgTotalPendingItems;

    private int MrgArrearsPendingItems;

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

    public int getBirthsStillSubmissions() {
        return birthsStillSubmissions;
    }

    public void setBirthsStillSubmissions(int birthsStillSubmissions) {
        this.birthsStillSubmissions = birthsStillSubmissions;
    }
}
