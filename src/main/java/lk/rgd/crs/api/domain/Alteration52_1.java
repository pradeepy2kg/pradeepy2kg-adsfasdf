package lk.rgd.crs.api.domain;

import javax.persistence.Embedded;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Ashoka Ekanayaka
 * To model the alterations which comes under secion 52_1 in the Act.
 * Registration Info, Mother and Informant.
 */
@Embeddable
public class Alteration52_1 {
    @Embedded
    private BirthRegisterInfo register;

    @Embedded
    private MotherInfo mother;

    @Embedded
    private InformantInfo informant;

    @Column(nullable = true, length = 1000)
    private String natureOfError;

    public BirthRegisterInfo getRegister() {
        return register;
    }

    public void setRegister(BirthRegisterInfo register) {
        this.register = register;
    }

    public MotherInfo getMother() {
        return mother;
    }

    public void setMother(MotherInfo mother) {
        this.mother = mother;
    }

    public InformantInfo getInformant() {
        return informant;
    }

    public void setInformant(InformantInfo informant) {
        this.informant = informant;
    }

    public String getNatureOfError() {
        return natureOfError;
    }

    public void setNatureOfError(String natureOfError) {
        this.natureOfError = natureOfError;
    }
}
