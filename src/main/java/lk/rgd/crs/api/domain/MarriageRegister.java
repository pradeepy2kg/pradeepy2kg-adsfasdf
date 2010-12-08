package lk.rgd.crs.api.domain;

import lk.rgd.crs.web.util.MarriageType;
import lk.rgd.crs.web.util.TypeOfMarriagePlace;

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
        "AND mr.state = :state AND mr.lifeCycleInfo.activeRecord = :active ORDER BY mr.idUKey DESC "),

    @NamedQuery(name = "filter.by.mrDivision.and.state", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE ((mr.mrDivisionOfMaleNotice IS NOT NULL AND mr.mrDivisionOfMaleNotice = :mrDivision) " +
        "OR (mr.mrDivisionOfFemaleNotice IS NOT NULL AND mr.mrDivisionOfFemaleNotice = :mrDivision)) " +
        "AND mr.state = :state AND mr.lifeCycleInfo.activeRecord = :active ORDER BY mr.idUKey DESC "),

    @NamedQuery(name = "filter.by.pinOrNic.and.state", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE ((mr.male.identificationNumberMale IS NOT NULL AND mr.male.identificationNumberMale = :id) " +
        "OR (mr.female.identificationNumberFemale IS NOT NULL AND mr.female.identificationNumberFemale = :id)) " +
        "AND mr.state = :state AND mr.lifeCycleInfo.activeRecord = :active ORDER BY mr.idUKey DESC"),

    @NamedQuery(name = "filter.by.mrDivision.serial.and.state", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE ((mr.mrDivisionOfMaleNotice IS NOT NULL AND mr.mrDivisionOfMaleNotice = :mrDivision) " +
        "OR (mr.mrDivisionOfFemaleNotice IS NOT NULL AND mr.mrDivisionOfFemaleNotice = :mrDivision)) " +
        "AND ((mr.serialOfMaleNotice IS NOT NULL AND mr.serialOfMaleNotice = :serialNo) " +
        "OR (mr.serialOfFemaleNotice IS NOT NULL AND mr.serialOfFemaleNotice = :serialNo)) " +
        "AND mr.state = :state AND mr.lifeCycleInfo.activeRecord = :active ORDER BY mr.idUKey DESC"),

    @NamedQuery(name = "get.notice.by.male.and.female.identification", query = "SELECT mr FROM MarriageRegister mr" +
        " WHERE (mr.male.identificationNumberMale = :male AND mr.female.identificationNumberFemale = :female " +
        " AND mr.lifeCycleInfo.activeRecord IS TRUE AND mr.state=0) ORDER BY mr.idUKey desc")
})
public class MarriageRegister implements Serializable, Cloneable {

    public enum State {
        DATA_ENTRY,
        REG_DATA_ENTRY
    }

    public enum PlaceOfMarriage {
        REGISTRAR_OFFICE,
        DS_OFFICE,
        CHURCH,
        OTHER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDUKEY")
    private long idUKey;

    @Column(name = "STATE", nullable = false)
    private State state;

    @ManyToOne
    @JoinColumn(name = "REG_MRDIVISIONUKEY", nullable = true, insertable = false, updatable = false)
    private MRDivision mrDivision;

    //received date for marriage registry
    @Column(name = "RECEIVED_DATE", nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date receivedDate;

    @Column(name = "TYPE_MARRIAGE", nullable = true)
    private MarriageType typeOfMarriage;

    //TODO : to be removed
    @Column(name = "PLACE_MARRIAGE", nullable = true)
    private PlaceOfMarriage placeOfMarriage;

    @Column(name = "TYPE_MARRIAGE_PLACE", nullable = true)
    private TypeOfMarriagePlace typeOfMarriagePlace;

    @Column(name = "REG_MIN_PIN")
    private long registrarOrMinisterPIN;

    @Column(length = 10, name = "REG_SERIAL", nullable = true)
    private String regSerial;

    @Column(name = "MARRIAGE_DATE", nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfMarriage;

    @Column(name = "REG_SUBMITTED_DATE", nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date registrationDate;

    @Column(name = "REG_PLACE_MARRIAGE_OFFICIAL_LANG", nullable = true)
    private String regPlaceInOfficialLang;

    @Column(name = "REG_PLACE_MARRIAGE_ENGLISH_LANG", nullable = true)
    private String regPlaceInEnglishLang;

    @ManyToOne
    @JoinColumn(name = "REGISTRAR_IDUKEY", nullable = true)
    private Registrar registrar;

    //marriage notice related columns

    @Column(name = "SINGLE_NOTICE", nullable = false)
    private boolean singleNotice;

    //male notice related columns

    @Column(length = 10, name = "NOTICE_SERIAL_MALE")
    private Long serialOfMaleNotice;

    @Column(name = "NOTICE_DATE_M_N")
    @Temporal(value = TemporalType.DATE)
    private Date dateOfMaleNotice;

    @Column(name = "NOTICE_PIN_M_N")
    private long registrarPINOfMaleNotice;

    @ManyToOne
    @JoinColumn(name = "NOTICE_MRDIVISIONUKEY_M", nullable = true, updatable = false)
    private MRDivision mrDivisionOfMaleNotice;


    //party information male
    @Embedded
    private MaleParty male = new MaleParty();

    //female notice related columns

    @Column(length = 10, name = "NOTICE_SERIAL_FEMALE")
    private Long serialOfFemaleNotice;

    @Column(name = "NOTICE_DATE_F_N")
    @Temporal(value = TemporalType.DATE)
    private Date dateOfFemaleNotice;

    @Column(name = "NOTICE_PIN_F_N")
    private long registrarPINOfFemaleNotice;

    @ManyToOne
    @JoinColumn(name = "NOTICE_MRDIVISIONUKEY_F", nullable = true, updatable = false)
    private MRDivision mrDivisionOfFemaleNotice;

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

    public MarriageType getTypeOfMarriage() {
        return typeOfMarriage;
    }

    public void setTypeOfMarriage(MarriageType typeOfMarriage) {
        this.typeOfMarriage = typeOfMarriage;
    }

    //todo: tobe removed
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

    public Long getSerialOfMaleNotice() {
        return serialOfMaleNotice;
    }

    public void setSerialOfMaleNotice(Long serialOfMaleNotice) {
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

    public Long getSerialOfFemaleNotice() {
        return serialOfFemaleNotice;
    }

    public void setSerialOfFemaleNotice(Long serialOfFemaleNotice) {
        this.serialOfFemaleNotice = serialOfFemaleNotice;
    }

    public String getRegSerial() {
        return regSerial;
    }

    public void setRegSerial(String regSerial) {
        this.regSerial = regSerial;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegPlaceInEnglishLang() {
        return regPlaceInEnglishLang;
    }

    public void setRegPlaceInEnglishLang(String regPlaceInEnglishLang) {
        this.regPlaceInEnglishLang = regPlaceInEnglishLang;
    }

    public String getRegPlaceInOfficialLang() {
        return regPlaceInOfficialLang;
    }

    public void setRegPlaceInOfficialLang(String regPlaceInOfficialLang) {
        this.regPlaceInOfficialLang = regPlaceInOfficialLang;
    }

    public Date getDateOfMarriage() {
        return dateOfMarriage;
    }

    public void setDateOfMarriage(Date dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
    }

    public TypeOfMarriagePlace getTypeOfMarriagePlace() {
        return typeOfMarriagePlace;
    }

    public void setTypeOfMarriagePlace(TypeOfMarriagePlace typeOfMarriagePlace) {
        this.typeOfMarriagePlace = typeOfMarriagePlace;
    }

    public Registrar getRegistrar() {
        return registrar;
    }

    public void setRegistrar(Registrar registrar) {
        this.registrar = registrar;
    }

    public boolean isSingleNotice() {
        return singleNotice;
    }

    public void setSingleNotice(boolean singleNotice) {
        this.singleNotice = singleNotice;
    }
}
