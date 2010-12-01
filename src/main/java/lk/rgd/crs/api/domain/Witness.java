package lk.rgd.crs.api.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Ashoka Ekanayaka
 *         Entity to hold witness information in Marriages.
 */
@Entity
@Table(name = "WITNESS", schema = "CRS")

public class Witness implements Serializable, Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idukey;

    @Column(name = "IDENTIFICATION_NUMBER", length = 10)
    private String identificationNumber;

    @Column(name = "WITNESS_NAME", length = 600)
    private String fullName;

    @Column(name = "WITNESS_RANK_PROFESSION", length = 255)
    private String rankOrProfession;

    @Column(name = "WITNESS_ADDRESS", length = 255)
    private String address;

    public long geIdUKey() {
        return idukey;
    }

    public void setIdukey(long idukey) {
        this.idukey = idukey;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRankOrProfession() {
        return rankOrProfession;
    }

    public void setRankOrProfession(String rankOrProfession) {
        this.rankOrProfession = rankOrProfession;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
