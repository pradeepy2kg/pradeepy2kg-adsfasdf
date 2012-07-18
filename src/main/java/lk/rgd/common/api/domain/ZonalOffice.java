package lk.rgd.common.api.domain;

import lk.rgd.crs.api.domain.Court;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Represents Zonal Offices.
 *
 * @author Duminda Dharmakeerthi
 */
@Entity
@Table(name = "ZONAL_OFFICES", schema = "COMMON")
@NamedQueries({
    @NamedQuery(
        name = "getZonalOfficeByDistrict",
        query = "SELECT z FROM ZonalOffice z WHERE :district MEMBER OF z.districts"
    ),
    @NamedQuery(
        name = "getZonalOfficeByCourt",
        query = "SELECT z FROM ZonalOffice z WHERE :court MEMBER OF z.courts"
    )
})
public class ZonalOffice implements Serializable {

    /**
     * This is a system generated unique key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int zonalOfficeUKey;

    @Embedded
    private BaseLifeCycleInfo lifeCycleInfo = new BaseLifeCycleInfo();

    /**
     * Name of the Zonal office in major 3 languages.
     */
    @Column(nullable = false, length = 120, unique = true)
    private String siZonalOfficeName;
    @Column(nullable = false, length = 120, unique = true)
    private String taZonalOfficeName;
    @Column(nullable = false, length = 120, unique = true)
    private String enZonalOfficeName;

    /**
     * Address of the Zonal office in major 3 languages.
     */
    @Column(nullable = true, length = 255, unique = false)
    private String siZonalOfficeMailAddress;
    @Column(nullable = true, length = 255, unique = false)
    private String taZonalOfficeMailAddress;
    @Column(nullable = true, length = 255, unique = false)
    private String enZonalOfficeMailAddress;

    /**
     * Land Phone Number of the Zonal Office.
     */
    @Column(nullable = true, length = 30)
    private String zonalOfficeLandPhone;

    /**
     * Fax Number of the Zonal Office.
     */
    @Column(nullable = true, length = 30)
    private String zonalOfficeFax;

    /**
     * E-mail of the Zonal Office.
     */
    @Column(nullable = true, length = 50)
    private String zonalOfficeEmail;

    /**
     * Districts assigned to this Zonal Office.
     */
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(schema = "COMMON", name = "ZONAL_OFFICE_DISTRICTS",
        joinColumns = @JoinColumn(name = "zonalOfficeUKey"),
        inverseJoinColumns = @JoinColumn(name = "districtUKey"))
    private Set<District> districts;

    /**
     * Courts related to this Zonal Office.
     */
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(schema = "COMMON", name = "ZONAL_OFFICE_COURTS",
        joinColumns = @JoinColumn(name = "zonalOfficeUKey"),
        inverseJoinColumns = @JoinColumn(name = "courtUKey"))
    private Set<Court> courts;

    public int getZonalOfficeUKey() {
        return zonalOfficeUKey;
    }

    public void setZonalOfficeUKey(int zonalOfficeUKey) {
        this.zonalOfficeUKey = zonalOfficeUKey;
    }

    public BaseLifeCycleInfo getLifeCycleInfo() {
        return lifeCycleInfo;
    }

    public void setLifeCycleInfo(BaseLifeCycleInfo lifeCycleInfo) {
        this.lifeCycleInfo = lifeCycleInfo;
    }

    public String getSiZonalOfficeName() {
        return siZonalOfficeName;
    }

    public void setSiZonalOfficeName(String siZonalOfficeName) {
        this.siZonalOfficeName = siZonalOfficeName;
    }

    public String getTaZonalOfficeName() {
        return taZonalOfficeName;
    }

    public void setTaZonalOfficeName(String taZonalOfficeName) {
        this.taZonalOfficeName = taZonalOfficeName;
    }

    public String getEnZonalOfficeName() {
        return enZonalOfficeName;
    }

    public void setEnZonalOfficeName(String enZonalOfficeName) {
        this.enZonalOfficeName = enZonalOfficeName;
    }

    public String getSiZonalOfficeMailAddress() {
        return siZonalOfficeMailAddress;
    }

    public void setSiZonalOfficeMailAddress(String siZonalOfficeMailAddress) {
        this.siZonalOfficeMailAddress = siZonalOfficeMailAddress;
    }

    public String getTaZonalOfficeMailAddress() {
        return taZonalOfficeMailAddress;
    }

    public void setTaZonalOfficeMailAddress(String taZonalOfficeMailAddress) {
        this.taZonalOfficeMailAddress = taZonalOfficeMailAddress;
    }

    public String getEnZonalOfficeMailAddress() {
        return enZonalOfficeMailAddress;
    }

    public void setEnZonalOfficeMailAddress(String enZonalOfficeMailAddress) {
        this.enZonalOfficeMailAddress = enZonalOfficeMailAddress;
    }

    public String getZonalOfficeLandPhone() {
        return zonalOfficeLandPhone;
    }

    public void setZonalOfficeLandPhone(String zonalOfficeLandPhone) {
        this.zonalOfficeLandPhone = zonalOfficeLandPhone;
    }

    public String getZonalOfficeFax() {
        return zonalOfficeFax;
    }

    public void setZonalOfficeFax(String zonalOfficeFax) {
        this.zonalOfficeFax = zonalOfficeFax;
    }

    public String getZonalOfficeEmail() {
        return zonalOfficeEmail;
    }

    public void setZonalOfficeEmail(String zonalOfficeEmail) {
        this.zonalOfficeEmail = zonalOfficeEmail;
    }

    public Set<District> getDistricts() {
        return districts;
    }

    public void setDistricts(Set<District> districts) {
        this.districts = districts;
    }

    public Set<Court> getCourts() {
        return courts;
    }

    public void setCourts(Set<Court> courts) {
        this.courts = courts;
    }
}
