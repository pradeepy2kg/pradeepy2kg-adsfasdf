package lk.rgd.crs.api.domain;

import javax.persistence.Embeddable;
import javax.persistence.Column;
import java.io.Serializable;

/**
 * @author Duminda Dharmakeerthi
 * @author Indunil Moremada
 */

@Embeddable
public class WitnessInfo implements Serializable {

    @Column(nullable = true)
    private String firstWitnessNICorPIN;

    @Column(nullable = true)
    private String firstWitnessFullName;

    @Column(nullable = true)
    private String firstWitnessAddress;

    @Column(nullable = true)
    private String secondWitnessFullName;

    @Column(nullable = true)
    private String secondWitnessNICorPIN;

    @Column(nullable = true)
    private String secondWitnessAddress;

    public String getSecondWitnessAddress() {
        return secondWitnessAddress;
    }

    public void setSecondWitnessAddress(String secondWitnessAddress) {
        this.secondWitnessAddress = secondWitnessAddress;
    }

    public String getSecondWitnessNICorPIN() {
        return secondWitnessNICorPIN;
    }

    public void setSecondWitnessNICorPIN(String secondWitnessNICorPIN) {
        this.secondWitnessNICorPIN = secondWitnessNICorPIN;
    }

    public String getSecondWitnessFullName() {
        return secondWitnessFullName;
    }

    public void setSecondWitnessFullName(String secondWitnessFullName) {
        this.secondWitnessFullName = secondWitnessFullName;
    }

    public void setFirstWitnessNICorPIN(String firstWitnessNICorPIN) {
        this.firstWitnessNICorPIN = firstWitnessNICorPIN;
    }

    public void setFirstWitnessFullName(String firstWitnessFullName) {
        this.firstWitnessFullName = firstWitnessFullName;
    }

    public void setFirstWitnessAddress(String firstWitnessAddress) {
        this.firstWitnessAddress = firstWitnessAddress;
    }

    public String getFirstWitnessNICorPIN() {
        return firstWitnessNICorPIN;
    }

    public String getFirstWitnessFullName() {
        return firstWitnessFullName;
    }

    public String getFirstWitnessAddress() {
        return firstWitnessAddress;
    }
}
