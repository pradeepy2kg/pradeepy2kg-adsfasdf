package lk.rgd.crs.api.domain;

import lk.rgd.common.util.WebUtils;

import javax.persistence.Column;

/**
 * @author Ashoka Ekanayaka
 * Class to encapsulate fields related to informat information on alteration of a birth certificate
 */
public class AlterationInformatInfo {
    public AlterationInformatInfo(InformantInfo.InformantType type, String name, String NICorPIN, String address) {
        informantType = type;
        informantName = name;
        informantNICorPIN = NICorPIN;
        informantAddress = address;
    }

    public InformantInfo.InformantType getInformantType() {
        return informantType;
    }

    public void setInformantType(InformantInfo.InformantType informantType) {
        this.informantType = informantType;
    }

    public String getInformantName() {
        return informantName;
    }

    public void setInformantName(String informantName) {
        this.informantName = WebUtils.filterBlanks(informantName);
    }

    public String getInformantNICorPIN() {
        return informantNICorPIN;
    }

    public void setInformantNICorPIN(String informantNICorPIN) {
        this.informantNICorPIN = WebUtils.filterBlanks(informantNICorPIN);
    }

    public String getInformantAddress() {
        return informantAddress;
    }

    public void setInformantAddress(String informantAddress) {
        this.informantAddress = WebUtils.filterBlanks(informantAddress);
    }

    @Column(nullable = true)
    private InformantInfo.InformantType informantType;

    @Column(nullable = true, length = 600)
    private String informantName;

    @Column(nullable = true, length = 10)
    private String informantNICorPIN;

    @Column(nullable = true, length = 255)
    private String informantAddress;
}
