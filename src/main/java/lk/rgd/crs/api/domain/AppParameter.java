package lk.rgd.crs.api.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents an application parameter used to configure or tune the system - stored as key-value pairs
 *
 * @author asankha
 */
@Entity
@Table(name = "app_parameters")
public class AppParameter {

    @Id
    private String key;
    private String value;

    public AppParameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public AppParameter(String key, int value) {
        this.key = key;
        this.value = Integer.toString(value);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
