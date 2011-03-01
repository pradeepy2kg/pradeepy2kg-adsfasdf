package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.DSDivision;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author amith jayasekara
 *         Entity class for Grama Niladhari Division with in D.S Division
 */
@Entity
@Table(name = "GN_DIVISIONS", schema = "CRS", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"dsDivisionUKey", "gnDivisionUKey"})
})
//todo add cache control amith :))
public class GNDivision implements Serializable {
    /**
     * system generated unique key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gnDivisionUKey;
    /**
     * id of the gn division
     */
    @Column(updatable = false, nullable = false)
    private int gnDivisionId;

    @ManyToOne
    @JoinColumn(name = "dsDivisionUKey", nullable = false, updatable = false)
    private DSDivision dsDivision;

    @Column(nullable = false, length = 60, updatable = false)
    private String siGNDivisionName;
    @Column(nullable = false, length = 60, updatable = false)
    private String enGNDivisionName;
    @Column(nullable = false, length = 60, updatable = false)
    private String taGNDivisionName;

    /**
     * A G.N Division maybe marked as inactive if one is split into two, or amalgamated to create a new one
     * The UI will only show G.N.Divisions that are currently active for every data entry form
     */
    @Column(name = "active", columnDefinition = "smallint not null default 1")
    private boolean active;

    public GNDivision(int gnDivisionId, DSDivision dsDivision, String siGNDivisionName, String enGNDivisionName,
        String taGNDivisionName, boolean active) {
        this.gnDivisionId = gnDivisionId;
        this.dsDivision = dsDivision;
        this.siGNDivisionName = siGNDivisionName;
        this.enGNDivisionName = enGNDivisionName;
        this.taGNDivisionName = taGNDivisionName;
        this.active = active;
    }

    public GNDivision() {
    }

    public int getGnDivisionUKey() {
        return gnDivisionUKey;
    }

    public void setGnDivisionUKey(int gnDivisionUKey) {
        this.gnDivisionUKey = gnDivisionUKey;
    }

    public int getGnDivisionId() {
        return gnDivisionId;
    }

    public void setGnDivisionId(int gnDivisionId) {
        this.gnDivisionId = gnDivisionId;
    }

    public DSDivision getDsDivision() {
        return dsDivision;
    }

    public void setDsDivision(DSDivision dsDivision) {
        this.dsDivision = dsDivision;
    }

    public String getSiGNDivisionName() {
        return siGNDivisionName;
    }

    public void setSiGNDivisionName(String siGNDivisionName) {
        this.siGNDivisionName = siGNDivisionName;
    }

    public String getEnGNDivisionName() {
        return enGNDivisionName;
    }

    public void setEnGNDivisionName(String enGNDivisionName) {
        this.enGNDivisionName = enGNDivisionName;
    }

    public String getTaGNDivisionName() {
        return taGNDivisionName;
    }

    public void setTaGNDivisionName(String taGNDivisionName) {
        this.taGNDivisionName = taGNDivisionName;
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

        GNDivision that = (GNDivision) o;

        if (gnDivisionUKey != that.gnDivisionUKey) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return gnDivisionUKey;
    }
}
