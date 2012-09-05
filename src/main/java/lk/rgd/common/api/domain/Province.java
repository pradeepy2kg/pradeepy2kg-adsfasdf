package lk.rgd.common.api.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Duminda Dharmakeerthi
 */

@Entity
@Table(name = "PROVINCE", schema = "COMMON")
@NamedQueries({
    @NamedQuery(
        name = "getAllActiveProvinces",
        query = "SELECT p FROM Province p WHERE p.active = 1"
    )
})
public class Province implements Serializable {
    /**
     * This is a system generated unique key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int provinceUKey;


    @Column(nullable = false, length = 30, unique = true, updatable = false)
    private String siProvinceName;
    @Column(nullable = false, length = 30, unique = true, updatable = false)
    private String enProvinceName;
    @Column(nullable = false, length = 30, unique = true, updatable = false)
    private String taProvinceName;

    /**
     * A District maybe marked as inactive if one is split into two, or amalgamated to create a new one
     * The UI will only show Districts that are currently active for every data entry form
     */
    @Column(name = "active", columnDefinition = "smallint not null default 1")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "zonalOfficeUKey", nullable = false, updatable = false)
    private ZonalOffice zonalOffice;
    
    public int getProvinceUKey() {
        return provinceUKey;
    }

    public void setProvinceUKey(int provinceUKey) {
        this.provinceUKey = provinceUKey;
    }

    public String getSiProvinceName() {
        return siProvinceName;
    }

    public void setSiProvinceName(String siProvinceName) {
        this.siProvinceName = siProvinceName;
    }

    public String getEnProvinceName() {
        return enProvinceName;
    }

    public void setEnProvinceName(String enProvinceName) {
        this.enProvinceName = enProvinceName;
    }

    public String getTaProvinceName() {
        return taProvinceName;
    }

    public void setTaProvinceName(String taProvinceName) {
        this.taProvinceName = taProvinceName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
