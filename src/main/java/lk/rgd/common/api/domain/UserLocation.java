package lk.rgd.common.api.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;
import java.io.Serializable;

/**
 * Represents a User assignment to a Location
 *
 * @author asankha
 */
@Entity
@Table(schema = "COMMON", name = "USER_LOCATIONS")
@IdClass(UserLocationID.class)
@NamedQueries({
    @NamedQuery(name = "getAllUserLocations", query = "SELECT ul FROM UserLocation ul " +
        "WHERE ul.lifeCycleInfo.active = :active ORDER BY ul.location.enLocationName desc"),
    @NamedQuery(name = "getUserLocationsByUserId", query = "SELECT ul FROM UserLocation ul " +
        "WHERE ul.userId = :userId ORDER BY ul.location.enLocationName desc"),
    @NamedQuery(name = "get.birthCertSign.user.by.locationId", query = "SELECT ul.user FROM UserLocation ul " +
        "WHERE ul.locationId = :locationId AND ul.lifeCycleInfo.active = :active AND ul.signBirthCert IS TRUE"),
    @NamedQuery(name = "get.marriage.certificateSign.user.by.locationId.and.active", query = "SELECT ul.user FROM UserLocation ul" +
        " WHERE ul.locationId =:locationId AND ul.lifeCycleInfo.active = :active AND ul.signMarriageCert IS TRUE"),
    @NamedQuery(name = "get.active.locations.by.userId", query = "SELECT ul FROM UserLocation ul " +
        "WHERE ul.lifeCycleInfo.active = :active AND ul.userId = :userId ORDER BY ul.location.enLocationName desc")
})
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class UserLocation implements Serializable {
    @Id
    private String userId;
    @Id
    private int locationId;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "locationUKey")
    private Location location;

    @Embedded
    private BaseLifeCycleInfo lifeCycleInfo = new BaseLifeCycleInfo();

    @Column
    private boolean signBirthCert;

    @Column
    private boolean signDeathCert;

    @Column
    private boolean signMarriageCert;

    @Column
    private boolean signAdoptionCert;

    @Column
    @Temporal(value = TemporalType.DATE)
    private Date startDate;

    @Column
    @Temporal(value = TemporalType.DATE)
    private Date endDate;

    public String getUserId() {
        if (user != null) {
            return user.getUserId();
        }
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLocationId() {
        if (location != null) {
            return location.getLocationUKey();
        }
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user.getUserId();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        this.locationId = location.getLocationUKey();
    }

    public BaseLifeCycleInfo getLifeCycleInfo() {
        return lifeCycleInfo;
    }

    public void setLifeCycleInfo(BaseLifeCycleInfo lifeCycleInfo) {
        this.lifeCycleInfo = lifeCycleInfo;
    }

    public boolean isSignBirthCert() {
        return signBirthCert;
    }

    public void setSignBirthCert(boolean signBirthCert) {
        this.signBirthCert = signBirthCert;
    }

    public boolean isSignDeathCert() {
        return signDeathCert;
    }

    public void setSignDeathCert(boolean signDeathCert) {
        this.signDeathCert = signDeathCert;
    }

    public boolean isSignMarriageCert() {
        return signMarriageCert;
    }

    public void setSignMarriageCert(boolean signMarriageCert) {
        this.signMarriageCert = signMarriageCert;
    }

    public boolean isSignAdoptionCert() {
        return signAdoptionCert;
    }

    public void setSignAdoptionCert(boolean signAdoptionCert) {
        this.signAdoptionCert = signAdoptionCert;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
