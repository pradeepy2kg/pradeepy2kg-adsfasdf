package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.District;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author amith jayasekara
 *         entity class for marriage notice
 *         note:this class is not complete
 */
@Entity
@Table(name = "MARRIAGE_REGISTER", schema = "CRS")
public class MarriageRegister implements Serializable, Cloneable {

    public enum PlaceOfMarriage {
        REGISTRAR_OFFICE,
        DS_OFFICE,
        CHURCH,
        OTHER
    }

    public enum TypeOfMarriage {
        GENERAL,
        KANDYAN_BINNA,
        KANDYAN_DEEGA
    }

    @Id
    @GeneratedValue
    private long idUKey;

    @Column(name = "SERIAL_NUMBER", nullable = false)
    private Long serialNumber;

    @Column(name = "RECEIVED_DATE", nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date receivedDate;

    @Column(name = "TYPE_MARRIAGE", nullable = true)
    private TypeOfMarriage typeOfMarriage;

    @Column(name = "PLACE_MARRIAGE", nullable = true)
    private PlaceOfMarriage placeOfMarriage;

    //party information male
    @Embedded
    private MaleParty male = new MaleParty();

    //party female
    @Embedded
    private FemaleParty female = new FemaleParty();

    @Embedded
    private CRSLifeCycleInfo lifeCycleInfo = new CRSLifeCycleInfo();

    //witness 1 info
    @Column(name = "W1_IDENTIFICATION_NUMBER", length = 10)
    private String identificationNumberWitness1;

    @Column(name = "W1_WITNESS_FULL_NAME", length = 600)
    private String fullNameWitness1;

    @Column(name = "W1_WITNESS_RANK_PROFESSION", length = 255)
    private String rankOrProfessionWitness1;

    @Column(name = "W1_WITNESS_ADDRESS", length = 255)
    private String addressWitness1;

    //witness 2 info
    @Column(name = "W2_IDENTIFICATION_NUMBER", length = 10)
    private String identificationNumberWitness2;

    @Column(name = "W2_WITNESS_FULL_NAME", length = 600)
    private String fullNameWitness2;

    @Column(name = "W2_WITNESS_RANK_PROFESSION", length = 255)
    private String rankOrProfessionWitness2;

    @Column(name = "W2_WITNESS_ADDRESS", length = 255)
    private String addressWitness2;

    public long getIdUKey() {
        return idUKey;
    }

    public void setIdUKey(long idUKey) {
        this.idUKey = idUKey;
    }

    public Long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public MaleParty getMale() {
        return male;
    }

    public void setMale(MaleParty male) {
        this.male = male;
    }

    public FemaleParty getFemale() {
        return female;
    }

    public void setFemale(FemaleParty female) {
        this.female = female;
    }

    public String getIdentificationNumberWitness1() {
        return identificationNumberWitness1;
    }

    public void setIdentificationNumberWitness1(String identificationNumberWitness1) {
        this.identificationNumberWitness1 = identificationNumberWitness1;
    }

    public String getFullNameWitness1() {
        return fullNameWitness1;
    }

    public void setFullNameWitness1(String fullNameWitness1) {
        this.fullNameWitness1 = fullNameWitness1;
    }

    public String getRankOrProfessionWitness1() {
        return rankOrProfessionWitness1;
    }

    public void setRankOrProfessionWitness1(String rankOrProfessionWitness1) {
        this.rankOrProfessionWitness1 = rankOrProfessionWitness1;
    }

    public String getIdentificationNumberWitness2() {
        return identificationNumberWitness2;
    }

    public void setIdentificationNumberWitness2(String identificationNumberWitness2) {
        this.identificationNumberWitness2 = identificationNumberWitness2;
    }

    public String getFullNameWitness2() {
        return fullNameWitness2;
    }

    public void setFullNameWitness2(String fullNameWitness2) {
        this.fullNameWitness2 = fullNameWitness2;
    }

    public String getRankOrProfessionWitness2() {
        return rankOrProfessionWitness2;
    }

    public void setRankOrProfessionWitness2(String rankOrProfessionWitness2) {
        this.rankOrProfessionWitness2 = rankOrProfessionWitness2;
    }

    public CRSLifeCycleInfo getLifeCycleInfo() {
        return lifeCycleInfo;
    }

    public void setLifeCycleInfo(CRSLifeCycleInfo lifeCycleInfo) {
        this.lifeCycleInfo = lifeCycleInfo;
    }

    public String getAddressWitness1() {
        return addressWitness1;
    }

    public void setAddressWitness1(String addressWitness1) {
        this.addressWitness1 = addressWitness1;
    }

    public String getAddressWitness2() {
        return addressWitness2;
    }

    public void setAddressWitness2(String addressWitness2) {
        this.addressWitness2 = addressWitness2;
    }

    public TypeOfMarriage getTypeOfMarriage() {
        return typeOfMarriage;
    }

    public void setTypeOfMarriage(TypeOfMarriage typeOfMarriage) {
        this.typeOfMarriage = typeOfMarriage;
    }

    public PlaceOfMarriage getPlaceOfMarriage() {
        return placeOfMarriage;
    }

    public void setPlaceOfMarriage(PlaceOfMarriage placeOfMarriage) {
        this.placeOfMarriage = placeOfMarriage;
    }
}
