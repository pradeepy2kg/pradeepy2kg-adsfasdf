package lk.rgd.crs.api.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents a Race maintained by the system. A race has a unique ID, and multiple names in
 * different languages that maps to the same ID
 *
 * @author asankha
 */
@Entity
@Table(name = "races")
public class Race implements Serializable {

    @Id
    private int raceId;
    private String siRaceName;
    private String enRaceName;
    private String taRaceName;
    private boolean active;

    public Race() {}

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

    public String getSiRaceName() {
        return siRaceName;
    }

    public void setSiRaceName(String siRaceName) {
        this.siRaceName = siRaceName;
    }

    public String getEnRaceName() {
        return enRaceName;
    }

    public void setEnRaceName(String enRaceName) {
        this.enRaceName = enRaceName;
    }

    public String getTaRaceName() {
        return taRaceName;
    }

    public void setTaRaceName(String taRaceName) {
        this.taRaceName = taRaceName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}