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
        name = "getAllActiveZonalOffices",
        query = "SELECT z FROM ZonalOffice z WHERE z.active = TRUE"
    ),
    @NamedQuery(
        name = "getAll",
        query = "SELECT z FROM ZonalOffice z"
    )
})
public class ZonalOffice implements Serializable {

    /**
     * This is a system generated unique key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int zonalOfficeUKey;

    @Column(name = "active", columnDefinition = "smallint not null default 1")
    private boolean active;

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

    public int getZonalOfficeUKey() {
        return zonalOfficeUKey;
    }

    public void setZonalOfficeUKey(int zonalOfficeUKey) {
        this.zonalOfficeUKey = zonalOfficeUKey;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
