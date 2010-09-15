package lk.rgd.common.api.domain;

import java.io.Serializable;

/**
 * @author asankha
 */
public class UserLocationID implements Serializable {

    private String userId;
    private long locationId;

    public UserLocationID(String userId, long locationId) {
        this.userId = userId;
        this.locationId = locationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }
}
