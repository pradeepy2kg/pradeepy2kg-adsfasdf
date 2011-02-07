package lk.rgd.common.api.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
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
@Table(name = "RACES", schema = "COMMON")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Race implements Serializable {

    @Id
    @Column(updatable = false)
    private int raceId;
    @Column(nullable = false, unique = true, updatable = false)
    private String siRaceName;
    @Column(nullable = false, length = 30, unique = true, updatable = false)
    private String enRaceName;
    @Column(nullable = false, length = 30, unique = true, updatable = false)
    private String taRaceName;
    @Column(name="active", columnDefinition="smallint not null default 1")
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