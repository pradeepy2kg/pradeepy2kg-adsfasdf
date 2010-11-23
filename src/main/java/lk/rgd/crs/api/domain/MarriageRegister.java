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

    @Column(nullable = false)
    private Long serialNumber;

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date receivedDate;

    @Column(nullable = true)
    private TypeOfMarriage typeOfMarriage;

    @Column(nullable = true)
    private PlaceOfMarriage placeOfMarriage;

    @Column(nullable = true)
    private String strPlaceOfMarriage;

    //party information male
    @Embedded
    private MaleParty male = new MaleParty();

    //party female
    @Embedded
    private FemaleParty female = new FemaleParty();

    @Embedded
    private CRSLifeCycleInfo lifeCycleInfo = new CRSLifeCycleInfo();

    @Column
    private long registrarOrMinisterPIN;

    @OneToOne
    @JoinColumn(name = "mrDivisionUKey", nullable = false, insertable=false, updatable=false)
    private MRDivision mrDivision;

    @OneToOne
    @JoinColumn(name = "idukey", nullable = false, insertable=false, updatable=false)
    private Witness witness1;

    @OneToOne
    @JoinColumn(name = "idukey", nullable = false, insertable=false, updatable=false)
    private Witness witness2;

    @Column(length =10)
    private String serialOfFirstNotice;

    @Column
    @Temporal(value = TemporalType.DATE)
    private Date dateOfFirstNotice;

    @Column
    private long registrarPINOfFirstNotice;

    @OneToOne
    @JoinColumn(name = "mrDivisionUKey", nullable = false, insertable=false, updatable=false)
    private MRDivision mrDivisionOfFirstNotice;

    @OneToOne
    @JoinColumn(name = "idukey", nullable = false, insertable=false, updatable=false)
    private Witness witness1OfFirstNotice;

    @OneToOne
    @JoinColumn(name = "idukey", nullable = false, insertable=false, updatable=false)
    private Witness witness2OfFirstNotice;

    @Column(length =10)
    private String serialOfSecondNotice;

    @Column
    @Temporal(value = TemporalType.DATE)
    private Date dateOfSecondNotice;

    @Column
    private long registrarPINOfSecondNotice;

    @OneToOne
    @JoinColumn(name = "mrDivisionUKey", nullable = false, insertable=false, updatable=false)
    private MRDivision mrDivisionOfSecondNotice;

    @OneToOne
    @JoinColumn(name = "idukey", nullable = false, insertable=false, updatable=false)
    private Witness witness1OfSecondNotice;

    @OneToOne
    @JoinColumn(name = "idukey", nullable = false, insertable=false, updatable=false)
    private Witness witness2OfSecondNotice;

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

    public String getSerialOfFirstNotice() {
        return serialOfFirstNotice;
    }

    public void setSerialOfFirstNotice(String serialOfFirstNotice) {
        this.serialOfFirstNotice = serialOfFirstNotice;
    }

    public Date getDateOfFirstNotice() {
        return dateOfFirstNotice;
    }

    public void setDateOfFirstNotice(Date dateOfFirstNotice) {
        this.dateOfFirstNotice = dateOfFirstNotice;
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
        return registrarPINOfFirstNotice;
    }

    public void setRegistrarPINOfFirstNotice(long registrarPINOfFirstNotice) {
        this.registrarPINOfFirstNotice = registrarPINOfFirstNotice;
    }

    public MRDivision getMrDivisionOfFirstNotice() {
        return mrDivisionOfFirstNotice;
    }

    public void setMrDivisionIdofFirstNotice(MRDivision mrDivisionOfFirstNotice) {
        this.mrDivisionOfFirstNotice = mrDivisionOfFirstNotice;
    }

    public long getRegistrarPINOfSecondNotice() {
        return registrarPINOfSecondNotice;
    }

    public void setRegistrarPINOfSecondNotice(long registrarPINOfSecondNotice) {
        this.registrarPINOfSecondNotice = registrarPINOfSecondNotice;
    }

    public MRDivision getMrDivisionIdofSecondNotice() {
        return mrDivisionOfSecondNotice;
    }

    public void setMrDivisionIdofSecondNotice(MRDivision mrDivisionOfSecondNotice) {
        this.mrDivisionOfSecondNotice = mrDivisionOfSecondNotice;
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

    public Witness getWitness1OfFirstNotice() {
        return witness1OfFirstNotice;
    }

    public void setWitness1OfFirstNotice(Witness witness1OfFirstNotice) {
        this.witness1OfFirstNotice = witness1OfFirstNotice;
    }

    public Witness getWitness2OfFirstNotice() {
        return witness2OfFirstNotice;
    }

    public void setWitness2OfFirstNotice(Witness witness2OfFirstNotice) {
        this.witness2OfFirstNotice = witness2OfFirstNotice;
    }

    public Witness getWitness1OfSecondNotice() {
        return witness1OfSecondNotice;
    }

    public void setWitness1OfSecondNotice(Witness witness1OfSecondNotice) {
        this.witness1OfSecondNotice = witness1OfSecondNotice;
    }

    public Witness getWitness2OfSecondNotice() {
        return witness2OfSecondNotice;
    }

    public void setWitness2OfSecondNotice(Witness witness2OfSecondNotice) {
        this.witness2OfSecondNotice = witness2OfSecondNotice;
    }
}
