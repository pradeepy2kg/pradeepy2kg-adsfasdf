package lk.rgd.crs.api.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents an application parameter used to configure or tune the system - stored as key-value pairs
 * e.g. The number of days between the birth and submission of the BDF (i.e. 90 days)
 *
 * @author asankha
 */
@Entity
@Table(name = "app_parameters")
public class AppParameter implements Serializable {

    @Id
    private String name;
    private String value;

    public AppParameter() {}

    public AppParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public AppParameter(String name, int value) {
        this.name = name;
        this.value = Integer.toString(value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
