package lk.rgd.crs.api.domain;

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

    public enum State {
        DATA_ENTRY
    }

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
    @Column(name = "IDUKEY")
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

    @Column(name = "STATE", nullable = false)
    private State state;

    @Column
    private long registrarOrMinisterPIN;

    @ManyToOne
    @JoinColumn(name = "mrDivisionUKey", nullable = false, insertable = false, updatable = false)
    private MRDivision mrDivision;

    @OneToOne
    @JoinColumn(name = "idukey", nullable = false, insertable = false, updatable = false)
    private Witness witness1;

    @OneToOne
    @JoinColumn(name = "idukey", nullable = false, insertable = false, updatable = false)
    private Witness witness2;

    @Column(length = 10)
    private String serialOfMaleNotice;

    @Column
    @Temporal(value = TemporalType.DATE)
    private Date dateOfMaleNotice;

    @Column
    private long registrarPINOfMaleNotice;

    @ManyToOne
    @JoinColumn(name = "mrDivisionUKey", nullable = false, insertable = false, updatable = false)
    private MRDivision mrDivisionOfMaleNotice;

    @OneToOne
    @JoinColumn(name = "idukey", nullable = false, insertable = false, updatable = false)
    private Witness maleNoticeWitness_1;

    @Column(length = 10)
    private String serialOfSecondNotice;

    @Column
    @Temporal(value = TemporalType.DATE)
    private Date dateOfSecondNotice;

    @Column
    private long registrarPINOfFemaleNotice;

    @OneToOne
    @JoinColumn(name = "idukey", nullable = false, insertable = false, updatable = false)
    private Witness maleNoticeWitness_2;

    @OneToOne
    @JoinColumn(name = "idukey", nullable = false, insertable = false, updatable = false)
    private Witness femaleNoticeWitness_1;

    @OneToOne
    @JoinColumn(name = "idukey", nullable = false, insertable = false, updatable = false)
    private Witness femaleNoticeWitness_2;

    @ManyToOne
    @JoinColumn(name = "mrDivisionUKey", nullable = false, insertable = false, updatable = false)
    private MRDivision mrDivisionOfFemaleNotice;

    //party information male
    @Embedded
    private MaleParty male = new MaleParty();

    //party female
    @Embedded
    private FemaleParty female = new FemaleParty();

    @Embedded
    private CRSLifeCycleInfo lifeCycleInfo = new CRSLifeCycleInfo();

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

    public CRSLifeCycleInfo getLifeCycleInfo() {
        return lifeCycleInfo;
    }

    public void setLifeCycleInfo(CRSLifeCycleInfo lifeCycleInfo) {
        this.lifeCycleInfo = lifeCycleInfo;
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

    public long getRegistrarOrMinisterPIN() {
        return registrarOrMinisterPIN;
    }

    public void setRegistrarOrMinisterPIN(long registrarOrMinisterPIN) {
        this.registrarOrMinisterPIN = registrarOrMinisterPIN;
    }

    public String getSerialOfMaleNotice() {
        return serialOfMaleNotice;
    }

    public void setSerialOfFirstNotice(String serialOfFirstNotice) {
        this.serialOfMaleNotice = serialOfFirstNotice;
    }

    public Date getDateOfMaleNotice() {
        return dateOfMaleNotice;
    }

    public void setDateOfMaleNotice(Date dateOfMaleNotice) {
        this.dateOfMaleNotice = dateOfMaleNotice;
    }

    public String getSerialOfSecondNotice() {
        return serialOfSecondNotice;
    }

    public void setSerialOfSecondNotice(String serialOfSecondNotice) {
        this.serialOfSecondNotice = serialOfSecondNotice;
    }

    public Date getDateOfSecondNotice() {
        return dateOfSecondNotice;
    }

    public void setDateOfSecondNotice(Date dateOfSecondNotice) {
        this.dateOfSecondNotice = dateOfSecondNotice;
    }

    public MRDivision getMrDivisionId() {
        return mrDivision;
    }

    public void setMrDivisionId(MRDivision mrDivision) {
        this.mrDivision = mrDivision;
    }

    public long getRegistrarPINOfFirstNotice() {
        return registrarPINOfMaleNotice;
    }

    public void setRegistrarPINOfFirstNotice(long registrarPINOfFirstNotice) {
        this.registrarPINOfMaleNotice = registrarPINOfFirstNotice;
    }
    public long getRegistrarPINOfSecondNotice() {
        return registrarPINOfFemaleNotice;
    }

    public void setRegistrarPINOfSecondNotice(long registrarPINOfSecondNotice) {
        this.registrarPINOfFemaleNotice = registrarPINOfSecondNotice;
    }

    public Witness getWitness1() {
        return witness1;
    }

    public void setWitness1(Witness witness1) {
        this.witness1 = witness1;
    }

    public Witness getWitness2() {
        return witness2;
    }

    public void setWitness2(Witness witness2) {
        this.witness2 = witness2;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public MRDivision getMrDivision() {
        return mrDivision;
    }

    public void setMrDivision(MRDivision mrDivision) {
        this.mrDivision = mrDivision;
    }

    public MRDivision getMrDivisionOfMaleNotice() {
        return mrDivisionOfMaleNotice;
    }

    public void setMrDivisionOfMaleNotice(MRDivision mrDivisionOfMaleNotice) {
        this.mrDivisionOfMaleNotice = mrDivisionOfMaleNotice;
    }

    public MRDivision getMrDivisionOfFemaleNotice() {
        return mrDivisionOfFemaleNotice;
    }

    public void setMrDivisionOfFemaleNotice(MRDivision mrDivisionOfFemaleNotice) {
        this.mrDivisionOfFemaleNotice = mrDivisionOfFemaleNotice;
    }

    public Witness getMaleNoticeWitness_1() {
        return maleNoticeWitness_1;
    }

    public void setMaleNoticeWitness_1(Witness maleNoticeWitness_1) {
        this.maleNoticeWitness_1 = maleNoticeWitness_1;
    }

    public Witness getMaleNoticeWitness_2() {
        return maleNoticeWitness_2;
    }

    public void setMaleNoticeWitness_2(Witness maleNoticeWitness_2) {
        this.maleNoticeWitness_2 = maleNoticeWitness_2;
    }

    public Witness getFemaleNoticeWitness_1() {
        return femaleNoticeWitness_1;
    }

    public void setFemaleNoticeWitness_1(Witness femaleNoticeWitness_1) {
        this.femaleNoticeWitness_1 = femaleNoticeWitness_1;
    }

    public Witness getFemaleNoticeWitness_2() {
        return femaleNoticeWitness_2;
    }

    public void setFemaleNoticeWitness_2(Witness femaleNoticeWitness_2) {
        this.femaleNoticeWitness_2 = femaleNoticeWitness_2;
    }
}
