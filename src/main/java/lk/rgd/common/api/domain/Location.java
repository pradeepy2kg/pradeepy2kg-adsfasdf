package lk.rgd.common.api.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represents a RGD office location
 * 
 * @author asankha
 */
@Entity
@Table(name = "LOCATIONS", schema = "COMMON")
@NamedQuery(name = "getAllLocations", query = "SELECT l FROM Location l " +
        "WHERE l.lifeCycleInfo.active = :active " +
        "ORDER BY l.enLocationName desc")
public class Location implements Serializable {
    
    /**
     * This is a system generated unique key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int locationUKey;

    @Embedded
    private BaseLifeCycleInfo lifeCycleInfo = new BaseLifeCycleInfo();

    @Column(nullable = false, unique = true, updatable = false)
    private int locationCode;

    @Column(nullable = false, length = 120, unique = true, updatable = false)
    private String siLocationName;
    @Column(nullable = false, length = 120, unique = true, updatable = false)
    private String enLocationName;
    @Column(nullable = false, length = 120, unique = true, updatable = false)
    private String taLocationName;

    /**
     * The users assigned to this location
     */
    @OneToMany(mappedBy = "location")
    private List<UserLocation> users  = new ArrayList<UserLocation>();

    public int getLocationUKey() {
        return locationUKey;
    }

    public BaseLifeCycleInfo getLifeCycleInfo() {
        return lifeCycleInfo;
    }

    public void setLifeCycleInfo(BaseLifeCycleInfo lifeCycleInfo) {
        this.lifeCycleInfo = lifeCycleInfo;
    }

    public int getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(int locationCode) {
        this.locationCode = locationCode;
    }

    public String getSiLocationName() {
        return siLocationName;
    }

    public void setSiLocationName(String siLocationName) {
        this.siLocationName = siLocationName;
    }

    public String getEnLocationName() {
        return enLocationName;
    }

    public void setEnLocationName(String enLocationName) {
        this.enLocationName = enLocationName;
    }

    public String getTaLocationName() {
        return taLocationName;
    }

    public void setTaLocationName(String taLocationName) {
        this.taLocationName = taLocationName;
    }

    public List<UserLocation> getUsers() {
        return users;
    }

    public void setUsers(List<UserLocation> users) {
        this.users = users;
    }
}
