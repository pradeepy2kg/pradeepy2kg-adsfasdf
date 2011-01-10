package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.WebUtils;
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
    @NamedQuery(name = "filter.notice.by.district", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE (mr.state <= 3 OR mr.state = 7) AND mr.lifeCycleInfo.activeRecord = :active " +
        "AND (mr.mrDivisionOfMaleNotice IN (SELECT m FROM MRDivision m WHERE (m.dsDivision.district = mr.mrDivisionOfMaleNotice.dsDivision.district AND mr.mrDivisionOfMaleNotice.dsDivision.district = :district ))" +
        "OR mr.mrDivisionOfFemaleNotice IN (SELECT m FROM MRDivision m WHERE (m.dsDivision.district = mr.mrDivisionOfFemaleNotice.dsDivision.district AND mr.mrDivisionOfFemaleNotice.dsDivision.district = :district ))) " +
        "ORDER BY mr.idUKey DESC "),

    // TODO review this query
    @NamedQuery(name = "filter.notice.by.dsDivision", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE ((mr.state <= 3) OR (mr.state =7)) AND mr.lifeCycleInfo.activeRecord = :active " +
        "AND (mr.mrDivisionOfMaleNotice IN (SELECT m FROM MRDivision m WHERE (m.dsDivision = mr.mrDivisionOfMaleNotice.dsDivision AND mr.mrDivisionOfMaleNotice.dsDivision = :dsDivision)) " +
        "OR mr.mrDivisionOfFemaleNotice IN (SELECT m FROM MRDivision m WHERE (m.dsDivision = mr.mrDivisionOfFemaleNotice.dsDivision AND mr.mrDivisionOfFemaleNotice.dsDivision = :dsDivision)))" +
        "ORDER BY mr.idUKey DESC "),

    @NamedQuery(name = "filter.notice.by.mrDivision", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE ((mr.mrDivisionOfMaleNotice IS NOT NULL AND mr.mrDivisionOfMaleNotice = :mrDivision) " +
        "OR (mr.mrDivisionOfFemaleNotice IS NOT NULL AND mr.mrDivisionOfFemaleNotice = :mrDivision)) " +
        "AND ((mr.state <= 3) OR (mr.state =7))  AND mr.lifeCycleInfo.activeRecord = :active ORDER BY mr.idUKey DESC "),

    @NamedQuery(name = "filter.notice.by.pinOrNic", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE ((mr.male.identificationNumberMale IS NOT NULL AND mr.male.identificationNumberMale = :id) " +
        "OR (mr.female.identificationNumberFemale IS NOT NULL AND mr.female.identificationNumberFemale = :id)) " +
        "AND ((mr.state <= 3) OR (mr.state =7))  AND mr.lifeCycleInfo.activeRecord = :active ORDER BY mr.idUKey DESC"),

    @NamedQuery(name = "get.notice.by.mrDivision.and.serial", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE ((mr.mrDivisionOfMaleNotice IS NOT NULL AND mr.mrDivisionOfMaleNotice = :mrDivision " +
        "AND mr.serialOfMaleNotice IS NOT NULL AND mr.serialOfMaleNotice = :serialNo) " +
        "OR (mr.mrDivisionOfFemaleNotice IS NOT NULL AND mr.mrDivisionOfFemaleNotice = :mrDivision " +
        "AND mr.serialOfFemaleNotice IS NOT NULL AND mr.serialOfFemaleNotice = :serialNo)) " +
        "AND ((mr.state <= 3) OR (mr.state =7))  AND mr.lifeCycleInfo.activeRecord = :active ORDER BY mr.idUKey DESC"),

    @NamedQuery(name = "get.notice.by.mrDivision.and.registerDate", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE ((mr.mrDivisionOfMaleNotice IS NOT NULL AND mr.mrDivisionOfMaleNotice = :mrDivision " +
        "AND mr.dateOfMaleNotice IS NOT NULL AND mr.dateOfMaleNotice BETWEEN :startDate AND :endDate) " +
        "OR (mr.mrDivisionOfFemaleNotice IS NOT NULL AND mr.mrDivisionOfFemaleNotice = :mrDivision " +
        "AND mr.dateOfFemaleNotice IS NOT NULL AND mr.dateOfFemaleNotice BETWEEN :startDate AND :endDate)) " +
        "AND ((mr.state <= 3) OR (mr.state =7))  AND mr.lifeCycleInfo.activeRecord = :active ORDER BY mr.idUKey DESC "),

    @NamedQuery(name = "get.notice.by.dsDivision.and.registerDate", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE ((mr.state <= 3) OR (mr.state =7))  AND mr.lifeCycleInfo.activeRecord = :active " +
        "AND (((mr.mrDivisionOfMaleNotice IN (SELECT m FROM MRDivision m WHERE (m.dsDivision = mr.mrDivisionOfMaleNotice.dsDivision AND mr.mrDivisionOfMaleNotice.dsDivision = :dsDivision))) " +
        "AND (mr.dateOfMaleNotice IS NOT NULL AND mr.dateOfMaleNotice BETWEEN :startDate AND :endDate)) " +
        "OR ((mr.mrDivisionOfFemaleNotice IN (SELECT m FROM MRDivision m WHERE (m.dsDivision = mr.mrDivisionOfFemaleNotice.dsDivision AND mr.mrDivisionOfFemaleNotice.dsDivision = :dsDivision))) " +
        "AND (mr.dateOfFemaleNotice IS NOT NULL AND mr.dateOfFemaleNotice BETWEEN :startDate AND :endDate))) ORDER BY mr.idUKey DESC "),

    @NamedQuery(name = "get.notice.by.male.and.female.identification", query = "SELECT mr FROM MarriageRegister mr" +
        " WHERE (mr.male.identificationNumberMale = :male AND mr.female.identificationNumberFemale = :female " +
        " AND mr.lifeCycleInfo.activeRecord IS TRUE AND mr.state=0) ORDER BY mr.idUKey desc"),

    // TODO review this query
    @NamedQuery(name = "filter.by.dsDivision.and.state", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE mr.state = :state AND mr.lifeCycleInfo.activeRecord = :active " +
        "AND (mr.mrDivisionOfMaleNotice IN (SELECT m FROM MRDivision m WHERE (m.dsDivision = mr.mrDivisionOfMaleNotice.dsDivision AND mr.mrDivisionOfMaleNotice.dsDivision = :dsDivision)) " +
        "OR mr.mrDivisionOfFemaleNotice IN (SELECT m FROM MRDivision m WHERE (m.dsDivision = mr.mrDivisionOfFemaleNotice.dsDivision AND mr.mrDivisionOfFemaleNotice.dsDivision = :dsDivision)))" +
        "ORDER BY mr.idUKey DESC "),

    @NamedQuery(name = "filter.by.mrDivision.and.state", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE ((mr.mrDivisionOfMaleNotice IS NOT NULL AND mr.mrDivisionOfMaleNotice = :mrDivision) " +
        "OR (mr.mrDivisionOfFemaleNotice IS NOT NULL AND mr.mrDivisionOfFemaleNotice = :mrDivision) " +
        "OR (mr.mrDivision IS NOT NULL AND mr.mrDivision = :mrDivision)) " +
        "AND mr.state = :state AND mr.lifeCycleInfo.activeRecord = :active ORDER BY mr.idUKey DESC "),

    @NamedQuery(name = "filter.by.pinOrNic.and.state", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE ((mr.male.identificationNumberMale IS NOT NULL AND mr.male.identificationNumberMale = :id) " +
        "OR (mr.female.identificationNumberFemale IS NOT NULL AND mr.female.identificationNumberFemale = :id)) " +
        "AND mr.state = :state AND mr.lifeCycleInfo.activeRecord = :active ORDER BY mr.idUKey DESC"),

    @NamedQuery(name = "get.active.by.mrDivision.and.serialNo", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE ((mr.mrDivisionOfMaleNotice IS NOT NULL AND mr.mrDivisionOfMaleNotice = :mrDivision " +
        "AND mr.serialOfMaleNotice IS NOT NULL AND mr.serialOfMaleNotice = :serialNo) " +
        "OR (mr.mrDivisionOfFemaleNotice IS NOT NULL AND mr.mrDivisionOfFemaleNotice = :mrDivision " +
        "AND mr.serialOfFemaleNotice IS NOT NULL AND mr.serialOfFemaleNotice = :serialNo)) " +
        "AND mr.lifeCycleInfo.activeRecord IS TRUE ORDER BY mr.idUKey DESC"),

    @NamedQuery(name = "get.register.by.mrDivision.and.serial", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE ((mr.mrDivisionOfMaleNotice IS NOT NULL AND mr.mrDivisionOfMaleNotice = :mrDivision " +
        "AND mr.serialOfMaleNotice IS NOT NULL AND mr.serialOfMaleNotice = :serialNo) " +
        "OR (mr.mrDivisionOfFemaleNotice IS NOT NULL AND mr.mrDivisionOfFemaleNotice = :mrDivision " +
        "AND mr.serialOfFemaleNotice IS NOT NULL AND mr.serialOfFemaleNotice = :serialNo)) " +
        "AND mr.state = :state AND mr.lifeCycleInfo.activeRecord = :active ORDER BY mr.idUKey DESC"),

    @NamedQuery(name = "findMarriageRegisterByDistrict", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE mr.state = :state AND mr.lifeCycleInfo.activeRecord = :active " +
        "AND mr.mrDivision.dsDivision.district IS NOT NULL AND mr.mrDivision.dsDivision.district = :district " +
        "ORDER BY mr.idUKey DESC"),

    @NamedQuery(name = "findMarriageRegisterByDistrictAndDate", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE mr.state = :state AND mr.lifeCycleInfo.activeRecord = :active " +
        "AND mr.mrDivision.dsDivision.district IS NOT NULL AND mr.mrDivision.dsDivision.district = :district " +
        "AND ((mr.dateOfMarriage IS NOT NULL AND mr.dateOfMarriage BETWEEN :startDate AND :endDate) " +
        "OR (mr.dateOfMaleNotice IS NOT NULL AND mr.dateOfMaleNotice BETWEEN :startDate AND :endDate)" +
        "OR (mr.dateOfFemaleNotice IS NOT NULL AND mr.dateOfFemaleNotice BETWEEN :startDate AND :endDate)) ORDER BY mr.idUKey DESC "),

    @NamedQuery(name = "findMarriageRegisterByState", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE mr.state = :state AND mr.lifeCycleInfo.activeRecord = :active " +
        "ORDER BY mr.idUKey DESC "),

    @NamedQuery(name = "findMarriageRegister", query = "SELECT mr FROM MarriageRegister mr " +
        "WHERE mr.lifeCycleInfo.activeRecord = :active " +
        "ORDER BY mr.idUKey DESC "),

    @NamedQuery(name = "get.mr.by.createdUser", query = "SELECT mr FROM MarriageRegister mr " +
        " WHERE mr.lifeCycleInfo.createdUser =:user AND (mr.lifeCycleInfo.createdTimestamp BETWEEN :startDate AND :endDate)")
})
public class MarriageRegister implements Serializable, Cloneable {
    //todo add divorce related col

    public enum State {
        DATA_ENTRY,    //0
        MALE_NOTICE_APPROVED,  // 1 approve only male notice   still able to edit female notice  (only single notice false)
        FEMALE_NOTICE_APPROVED,    // 2 approve female notice only still able to edit male notice (only single notice false)
        NOTICE_APPROVED,   //3 change to this state when single notice true or when approving second notice(1 st notice is already approved)
        MALE_NOTICE_REJECTED, //4
        FEMALE_NOTICE_REJECTED,//5
        NOTICE_REJECTED,//6
        LICENSE_PRINTED,//7
        REG_DATA_ENTRY,//8
        REGISTRATION_APPROVED,//9
        REGISTRATION_REJECTED, //10
        EXTRACT_PRINTED //11
    }

    public enum LicenseCollectType {
        HAND_COLLECT_MALE,  //male hand collect the license
        MAIL_TO_MALE,       //mail to mail party mailing address
        HAND_COLLECT_FEMALE,
        MAIL_TO_FEMALE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDUKEY")
    private long idUKey;
    /**
     * The preferred language to print the Extract of Marriage Register
     */
    @Column(nullable = true, columnDefinition = "char(2) default 'si'")
    private String preferredLanguage = "si";

    @Column(name = "STATE", nullable = false)
    private State state;

    @ManyToOne
    @JoinColumn(name = "REG_MRDIVISIONUKEY", nullable = true)
    private MRDivision mrDivision;

    @Column(name = "SCANNED_IMAGE", nullable = true)
    private String scannedImagePath;

    //received date for marriage registry
    @Column(name = "RECEIVED_DATE", nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date receivedDate;

    @Column(name = "TYPE_MARRIAGE", nullable = true)
    private MarriageType typeOfMarriage;

    @Column(name = "TYPE_MARRIAGE_PLACE", nullable = true)
    private TypeOfMarriagePlace typeOfMarriagePlace;

    @Column(name = "REG_MIN_PIN")
    private long registrarOrMinisterPIN;

    //colum name - to be renamed to SERIAL_NUMBER
    @Column(length = 10, name = "REG_SERIAL", nullable = true)
    private String serialNumber;

    @Column(name = "MARRIAGE_DATE", nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfMarriage;

    @Column(name = "REG_SUBMITTED_DATE", nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date registrationDate;

    @Column(name = "REGISTRAR_NAME_OFFICIAL_LANG", nullable = true)
    private String regNameInOfficialLang;

    @Column(name = "REGISTRAR_NAME_ENGLISH_LANG", nullable = true)
    private String regNameInEnglishLang;

    @Column(name = "REG_PLACE_MARRIAGE_OFFICIAL_LANG", nullable = true)
    private String regPlaceInOfficialLang;

    @Column(name = "REG_PLACE_MARRIAGE_ENGLISH_LANG", nullable = true)
    private String regPlaceInEnglishLang;

    //marriage notice related columns
    @Column(name = "SINGLE_NOTICE", nullable = false)
    private boolean singleNotice;

    @Column(name = "NOTICE_REJECT_COMMENT", nullable = true)
    private String noticeRejectionComment;

    @Column(name = "REG_REJECT_COMMENT", nullable = true)
    private String registrationRejectComment;

    /**
     * The user printing the license
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LICENSE_PRINT_USER", nullable = true)
    private User licensePrintUser;

    /**
     * The user who cetified the extract of marriage
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXTRACT_CERTIFIED_USER", nullable = true)
    private User extractCertifiedUser;

    /**
     * The timestamp when license is printed for this record
     */
    @Column(nullable = true, name = "LICENSE_PRINT_DATE")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date licensePrintTimestamp;

    /**
     * The place of issue for the original birth certificate - free copy (Stores the Location ID)
     */
    @OneToOne
    @JoinColumn(name = "LICENSE_ISSUE_LOCATION", nullable = true)
    private Location licenseIssueLocation;

    /**
     * The Locations where the extract of marriage can be issued
     */
    @OneToOne
    @JoinColumn(name = "EXTRACT_ISSUED_LOCATION", nullable = true)
    private Location extractIssuedLocation;

    /**
     * The timestamp when the Extract of Marriage printed
     */
    @Column(nullable = true, name = "EXTRACT_PRINTED_DATE")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date extractPrintedTimestamp;

    //male notice related columns

    @Column(name = "LI_REQ_PARTY", nullable = true)
    private LicenseCollectType licenseCollectType;

    @Column(length = 10, name = "NOTICE_SERIAL_MALE")
    private Long serialOfMaleNotice;

    @Column(name = "NOTICE_DATE_M_N")
    @Temporal(value = TemporalType.DATE)
    private Date dateOfMaleNotice;

    @Column(name = "NOTICE_PIN_M_N")
    private long registrarPINOfMaleNotice;

    @ManyToOne
    @JoinColumn(name = "NOTICE_MRDIVISIONUKEY_M", nullable = true)
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
    @JoinColumn(name = "NOTICE_MRDIVISIONUKEY_F", nullable = true)
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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = WebUtils.filterBlanks(serialNumber);
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
        this.regPlaceInEnglishLang = WebUtils.filterBlanks(regPlaceInEnglishLang);
    }

    public String getRegPlaceInOfficialLang() {
        return regPlaceInOfficialLang;
    }

    public void setRegPlaceInOfficialLang(String regPlaceInOfficialLang) {
        this.regPlaceInOfficialLang = WebUtils.filterBlanks(regPlaceInOfficialLang);
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

    public boolean isSingleNotice() {
        return singleNotice;
    }

    public void setSingleNotice(boolean singleNotice) {
        this.singleNotice = singleNotice;
    }

    public String getRegNameInOfficialLang() {
        return regNameInOfficialLang;
    }

    public void setRegNameInOfficialLang(String regNameInOfficialLang) {
        this.regNameInOfficialLang = WebUtils.filterBlanks(regNameInOfficialLang);
    }

    public String getRegNameInEnglishLang() {
        return regNameInEnglishLang;
    }

    public void setRegNameInEnglishLang(String regNameInEnglishLang) {
        this.regNameInEnglishLang = WebUtils.filterBlanks(regNameInEnglishLang);
    }

    public LicenseCollectType getLicenseCollectType() {
        return licenseCollectType;
    }

    public void setLicenseCollectType(LicenseCollectType licenseCollectType) {
        this.licenseCollectType = licenseCollectType;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getNoticeRejectionComment() {
        return noticeRejectionComment;
    }

    public void setNoticeRejectionComment(String noticeRejectionComment) {
        this.noticeRejectionComment = WebUtils.filterBlanks(noticeRejectionComment);
    }

    public String getRegistrationRejectComment() {
        return registrationRejectComment;
    }

    public void setRegistrationRejectComment(String registrationRejectComment) {
        this.registrationRejectComment = registrationRejectComment;
    }

    public Date getLicensePrintTimestamp() {
        return licensePrintTimestamp;
    }

    public void setLicensePrintTimestamp(Date licensePrintTimestamp) {
        this.licensePrintTimestamp = licensePrintTimestamp;
    }

    public User getLicensePrintUser() {
        return licensePrintUser;
    }

    public void setLicensePrintUser(User licensePrintUser) {
        this.licensePrintUser = licensePrintUser;
    }

    public Location getLicenseIssueLocation() {
        return licenseIssueLocation;
    }

    public void setLicenseIssueLocation(Location licenseIssueLocation) {
        this.licenseIssueLocation = licenseIssueLocation;
    }

    public String getScannedImagePath() {
        return scannedImagePath;
    }

    public void setScannedImagePath(String scannedImagePath) {
        this.scannedImagePath = scannedImagePath;
    }

    public User getExtractCertifiedUser() {
        return extractCertifiedUser;
    }

    public void setExtractCertifiedUser(User extractCertifiedUser) {
        this.extractCertifiedUser = extractCertifiedUser;
    }

    public Location getExtractIssuedLocation() {
        return extractIssuedLocation;
    }

    public void setExtractIssuedLocation(Location extractIssuedLocation) {
        this.extractIssuedLocation = extractIssuedLocation;
    }

    public Date getExtractPrintedTimestamp() {
        return extractPrintedTimestamp;
    }

    public void setExtractPrintedTimestamp(Date extractPrintedTimestamp) {
        this.extractPrintedTimestamp = extractPrintedTimestamp;
    }
}


