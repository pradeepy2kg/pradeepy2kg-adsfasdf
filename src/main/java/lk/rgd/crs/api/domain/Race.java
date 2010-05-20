package lk.rgd.crs.api.domain;

import lk.rgd.AppConstants;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a Race maintained by the system. A race has a unique ID, and multiple names in
 * different languages that maps to the same ID
 *
 * @author asankha
 */
@Entity
@Table(name = "races")
public class Race {

    @Id
    private int id;
    private int raceId;
    private String raceName;
    private String languageId;

    public Race() {}

    public Race(int raceId, String raceName) {
        this(raceId, raceName, AppConstants.ENGLISH);
    }

    public Race(int raceId, String raceName, String languageId) {
        this.raceId = raceId;
        this.raceName = raceName;
        this.languageId = languageId;
    }

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }
}