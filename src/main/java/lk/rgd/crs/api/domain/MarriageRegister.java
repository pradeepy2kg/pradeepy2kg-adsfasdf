package lk.rgd.crs.api.domain;

import lk.rgd.common.util.WebUtils;

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
@NamedQueries({
    // TODO review this query
    @NamedQuery(name = "filter.by.dsDivision.and.state", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE mr.mrDivisionOfMaleNotice IN (SELECT m FROM MRDivision m WHERE (m.dsDivision = mr.mrDivisionOfMaleNotice.dsDivision AND mr.mrDivisionOfMaleNotice.dsDivision = :dsDivision)) " +
        "OR mr.mrDivisionOfFemaleNotice IN (SELECT m FROM MRDivision m WHERE (m.dsDivision = mr.mrDivisionOfFemaleNotice.dsDivision AND mr.mrDivisionOfFemaleNotice.dsDivision = :dsDivision))" +
        "AND mr.state = :state AND mr.lifeCycleInfo.activeRecord = :active ORDER BY mr.dateOfMaleNotice, mr.dateOfFemaleNotice DESC "),

    @NamedQuery(name = "filter.by.mrDivision.and.state", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE ((mr.mrDivisionOfMaleNotice IS NOT NULL AND mr.mrDivisionOfMaleNotice = :mrDivision) " +
        "OR (mr.mrDivisionOfFemaleNotice IS NOT NULL AND mr.mrDivisionOfFemaleNotice = :mrDivision)) " +
        "AND mr.state = :state AND mr.lifeCycleInfo.activeRecord = :active ORDER BY mr.dateOfMaleNotice, mr.dateOfFemaleNotice DESC "),
    
    @NamedQuery(name = "get.notice.by.male.and.female.identification", query = "SELECT mr FROM MarriageRegister mr" +
        " WHERE (mr.male.identificationNumberMale = :male AND mr.female.identificationNumberFemale = :female " +
        " AND mr.lifeCycleInfo.activeRecord IS TRUE AND mr.state=0) ORDER BY mr.idUKey desc")
})
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

    //received date for marriage registry
    @Column(name = "RECEIVED_DATE", nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date receivedDate;

    @Column(name = "TYPE_MARRIAGE", nullable = true)
    private TypeOfMarriage typeOfMarriage;

    @Column(name = "PLACE_MARRIAGE", nullable = true)
    private PlaceOfMarriage placeOfMarriage;

    @Column(name = "STATE", nullable = false)
    private State state;

    @Column(name = "REG_MIN_PIN")
    private long registrarOrMinisterPIN;

    @Column(name = "IS_BOTH")
    private boolean bothPartySubmitted;

    //mr division for marriage register
    @ManyToOne
    @JoinColumn(name = "mrDivisionUKey", nullable = true, insertable = false, updatable = false)
    private MRDivision mrDivision;

    @OneToOne
    @JoinColumn(name = "witness1idukey", nullable = true, insertable = false, updatable = false)
    private Witness witness1 = new Witness();

    @OneToOne
    @JoinColumn(name = "witness2idukey", nullable = true, insertable = false, updatable = false)
    private Witness witness2 = new Witness();

    @Column(length = 10, name = "SERIAL_MALE")
    private String serialOfMaleNotice;

    @Column
    @Temporal(value = TemporalType.DATE)
    private Date dateOfMaleNotice;

    @Column
    private long registrarPINOfMaleNotice;

    //todo remove nullable
    @ManyToOne
    private MRDivision mrDivisionOfMaleNotice;

    @Column(length = 10, name = "SERIAL_FEMALE")
    private String serialOfFemaleNotice;

    @Column
    @Temporal(value = TemporalType.DATE)
    private Date dateOfFemaleNotice;

    @Column
    private long registrarPINOfFemaleNotice;

    @OneToOne
    @JoinColumn(name = "IDUKEY_M_W_1", nullable = true)
    private Witness maleNoticeWitness_1 = new Witness();

    @OneToOne
    @JoinColumn(name = "IDUKEY_M_W_2", nullable = true)
    private Witness maleNoticeWitness_2 = new Witness();

    @OneToOne
    @JoinColumn(name = "IDUKEY_F_W_1", nullable = true)
    private Witness femaleNoticeWitness_1 = new Witness();

    @OneToOne
    @JoinColumn(name = "IDUKEY_F_W_2", nullable = true)
    private Witness femaleNoticeWitness_2 = new Witness();

    //todo remove nullable
    @ManyToOne
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

    public void setSerialOfMaleNotice(String serialOfMaleNotice) {
        this.serialOfMaleNotice = serialOfMaleNotice;
    }

    public Date getDateOfMaleNotice() {
        return dateOfMaleNotice;
    }

    public void setDateOfMaleNotice(Date dateOfMaleNotice) {
        this.dateOfMaleNotice = dateOfMaleNotice;
    }

    public Date getDateOfFemaleNotice() {
        return dateOfFemaleNotice;
    }

    public void setDateOfFemaleNotice(Date dateOfFemaleNotice) {
        this.dateOfFemaleNotice = dateOfFemaleNotice;
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

    public long getRegistrarPINOfFemaleNotice() {
        return registrarPINOfFemaleNotice;
    }

    public void setRegistrarPINOfFemaleNotice(long registrarPINOfFemaleNotice) {
        this.registrarPINOfFemaleNotice = registrarPINOfFemaleNotice;
    }

    public long getRegistrarPINOfMaleNotice() {
        return registrarPINOfMaleNotice;
    }

    public void setRegistrarPINOfMaleNotice(long registrarPINOfMaleNotice) {
        this.registrarPINOfMaleNotice = registrarPINOfMaleNotice;
    }

    public boolean isBothPartySubmitted() {
        return bothPartySubmitted;
    }

    public void setBothPartySubmitted(boolean bothPartySubmitted) {
        this.bothPartySubmitted = bothPartySubmitted;
    }

    public String getSerialOfFemaleNotice() {
        return serialOfFemaleNotice;
    }

    public void setSerialOfFemaleNotice(String serialOfFemaleNotice) {
        this.serialOfFemaleNotice = WebUtils.filterBlanks(serialOfFemaleNotice);
    }

}
