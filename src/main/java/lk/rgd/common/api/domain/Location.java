package lk.rgd.common.api.domain;

import lk.rgd.AppConstants;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
@NamedQueries({
        @NamedQuery(name = "getAllLocations", query = "SELECT l FROM Location l " +
                "ORDER BY l.enLocationName desc"),
        @NamedQuery(name = "get.location.by.code", query = "SELECT l FROM Location l " +
                "WHERE l.locationCode=:locationCode"),
        @NamedQuery(name = "get.location.by.code.and.by.dsDivisionId", query = "SELECT l FROM Location l " +
                "WHERE l.locationCode=:locationCode AND l.dsDivisionId=:dsDivisionId "),
        @NamedQuery(name = "get.location.by.dsDivisionId", query = "SELECT l FROM Location l " +
                "WHERE l.dsDivisionId=:dsDivisionId")
})
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
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

    @Column(nullable = true, length = 255, unique = true, updatable = false)
    private String siLocationMailingAddress;
    @Column(nullable = true, length = 255, unique = true, updatable = false)
    private String enLocationMailingAddress;
    @Column(nullable = true, length = 255, unique = true, updatable = false)
    private String taLocationMailingAddress;

    @Column(nullable = true, length = 120, unique = false, updatable = false)
    private String sienLocationSignature;
    @Column(nullable = true, length = 120, unique = false, updatable = false)
    private String taenLocationSignature;

    /**
     * The users assigned to this location
     */
    @OneToMany(mappedBy = "location", fetch = FetchType.EAGER)
    private List<UserLocation> users = new ArrayList<UserLocation>();

    /*
        New field for DSDivisionUKey this should not be a foreign key because there can be locations that no DSDivisions
        ex mobile locations
     */
    @Column(nullable = false, updatable = false)
    private int dsDivisionId;

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

    public String getSiLocationMailingAddress() {
        return siLocationMailingAddress;
    }

    public void setSiLocationMailingAddress(String siLocationMailingAddress) {
        this.siLocationMailingAddress = siLocationMailingAddress;
    }

    public String getEnLocationMailingAddress() {
        return enLocationMailingAddress;
    }

    public void setEnLocationMailingAddress(String enLocationMailingAddress) {
        this.enLocationMailingAddress = enLocationMailingAddress;
    }

    public String getTaLocationMailingAddress() {
        return taLocationMailingAddress;
    }

    public void setTaLocationMailingAddress(String taLocationMailingAddress) {
        this.taLocationMailingAddress = taLocationMailingAddress;
    }

    public String getSienLocationSignature() {
        return sienLocationSignature;
    }

    public void setSienLocationSignature(String sienLocationSignature) {
        this.sienLocationSignature = sienLocationSignature;
    }

    public String getTaenLocationSignature() {
        return taenLocationSignature;
    }

    public void setTaenLocationSignature(String taenLocationSignature) {
        this.taenLocationSignature = taenLocationSignature;
    }

    public int getDsDivisionId() {
        return dsDivisionId;
    }

    public void setDsDivisionId(int dsDivisionId) {
        this.dsDivisionId = dsDivisionId;
    }

    public String getLocationName(String language) {
        StringBuilder sb = new StringBuilder();
        if (AppConstants.SINHALA.equals(language)) {
            sb.append(this.getSiLocationName());
            sb.append(" / ");
            sb.append(this.getEnLocationName());
        } else if (AppConstants.TAMIL.equals(language)) {
            sb.append(this.getTaLocationName());
            sb.append(" / ");
            sb.append(this.getEnLocationName());
        } else if(AppConstants.ENGLISH.equals(language)){
            sb.append(this.getEnLocationName());
        }
        return sb.toString();
    }

    public String getLocationSignature(String language) {
        if (AppConstants.SINHALA.equals(language)) {
            return this.getSienLocationSignature();
        } else if (AppConstants.TAMIL.equals(language)) {
            return this.getTaenLocationSignature();
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Location location = (Location) o;

        if (locationUKey != location.locationUKey) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return locationUKey;
    }
}
