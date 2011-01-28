package lk.rgd.crs.api.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a Court - e.g. District Court of Colombo
 *
 * @author asankha
 */
@Entity
@Table(name = "COURTS", schema = "CRS")
@NamedQueries({
        @NamedQuery(name = "findAllCourts", query = "SELECT c FROM Court c " +
                "ORDER BY c.enCourtName desc"),
        @NamedQuery(name = "get.court.by.code", query = "SELECT c FROM Court c " +
                "WHERE c.courtId=:courtId")
})
public class Court implements Serializable {

    /**
     * This is a system generated unique key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courtUKey;

    @Column(nullable = false, columnDefinition = "integer default 1")
    private boolean active = true;

    @Column(nullable = false)
    private int courtId;

    @Column(nullable = false, length = 60, updatable = false)
    private String siCourtName;
    @Column(nullable = false, length = 60, updatable = false)
    private String enCourtName;
    @Column(nullable = false, length = 60, updatable = false)
    private String taCourtName;

    public int getCourtUKey() {
        return courtUKey;
    }

    public int getCourtId() {
        return courtId;
    }

    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }

    public String getSiCourtName() {
        return siCourtName;
    }

    public void setSiCourtName(String siCourtName) {
        this.siCourtName = siCourtName;
    }

    public String getEnCourtName() {
        return enCourtName;
    }

    public void setEnCourtName(String enCourtName) {
        this.enCourtName = enCourtName;
    }

    public String getTaCourtName() {
        return taCourtName;
    }

    public void setTaCourtName(String taCourtName) {
        this.taCourtName = taCourtName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Court court = (Court) o;

        if (courtUKey != court.courtUKey) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return courtUKey;
    }
}
