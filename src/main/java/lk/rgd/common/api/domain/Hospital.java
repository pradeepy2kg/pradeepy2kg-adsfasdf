package lk.rgd.common.api.domain;

import lk.rgd.crs.api.domain.BDDivision;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: widu
 * Date: Nov 25, 2013
 * Time: 9:49:14 AM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "HOSPITAL", schema = "COMMON")

@NamedQueries({
 /*   @NamedQuery(
        name = "getHospitalsbyBdDivisionId",
        query = "SELECT h FROM Hospital h WHERE h.bdDivisionUKey.bdDivisionUKey =:bdDivisionId"
    ),*/
    @NamedQuery(
        name = "getHospitalsbyDsDivisionId",
        query = "SELECT h FROM Hospital h WHERE h.dsDivision.dsDivisionUKey = :dsDivisionId"
    ),
    @NamedQuery(
         name = "getHospitalsbyDistrictId",
         query = "SELECT h FROM Hospital h WHERE h.dsDivision.district.districtUKey = :districtId"
    )
})
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Hospital implements Serializable {   

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hospitalUKey;
     /**
     * This is the standard Hospital ID
     */
    @Column(updatable = false, unique = true, nullable = false)
    private int hospitalId;

    @Column(nullable = false, length = 255, unique = true)
    private String hospitalNameSi;
    @Column(nullable = false, length = 255, unique = true)
    private String hospitalNameEn;
    @Column(nullable = false, length = 255, unique = true)
    private String hospitalNameTa;

    @ManyToOne
    @JoinColumn(name = "dsDivisionUKey", nullable = false)
    private DSDivision dsDivision;

    /**
     * A Hospital maybe marked as inactive or active
     * The UI will only show Hospitals that are currently active for every data entry form
     */
    @Column(name = "active", columnDefinition = "smallint not null default 1")
    private boolean active;

    public int getHospitalUKey() {
        return hospitalUKey;
    }

    public void setHospitalUKey(int hospitalUKey) {
        this.hospitalUKey = hospitalUKey;
    }

    public DSDivision getDsDivision() {
        return dsDivision;
    }

    public void setDsDivision(DSDivision dsDivision) {
        this.dsDivision = dsDivision;
    }

    public String getHospitalNameEn() {
        return hospitalNameEn;
    }

    public void setHospitalNameEn(String hospitalNameEn) {
        this.hospitalNameEn = hospitalNameEn;
    }

    public String getHospitalNameSi() {
        return hospitalNameSi;
    }

    public void setHospitalNameSi(String hospitalNameSi) {
        this.hospitalNameSi = hospitalNameSi;
    }

    public String getHospitalNameTa() {
        return hospitalNameTa;
    }

    public void setHospitalNameTa(String hospitalNameTa) {
        this.hospitalNameTa = hospitalNameTa;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }


}
