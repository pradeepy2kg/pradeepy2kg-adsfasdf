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
public class Court implements Serializable {

    /**
     * This is a system generated unique key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courtUKey;

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
}
