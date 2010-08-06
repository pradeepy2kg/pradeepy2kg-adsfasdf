package lk.rgd.crs.api.domain;

/**
 * @author Duminda Dharmakeerthi
 */
public class WitnessInfo {

    private String witnessNICorPIN;
    private String witnessFullName;
    private String witnessAddress;

    public void setWitnessNICorPIN(String witnessNICorPIN) {
        this.witnessNICorPIN = witnessNICorPIN;
    }

    public void setWitnessFullName(String witnessFullName) {
        this.witnessFullName = witnessFullName;
    }

    public void setWitnessAddress(String witnessAddress) {
        this.witnessAddress = witnessAddress;
    }

    public String getWitnessNICorPIN() {     
        return witnessNICorPIN;
    }

    public String getWitnessFullName() {
        return witnessFullName;
    }

    public String getWitnessAddress() {
        return witnessAddress;
    }
}
