package lk.rgd.crs.web.action;


import lk.rgd.crs.api.domain.BirthRegister;
import lk.rgd.crs.api.domain.Person;
import lk.rgd.crs.api.service.BirthRegisterService;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.log4j.Logger;
import lk.rgd.crs.web.util.Constant;
import lk.rgd.crs.web.util.EPopDate;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

import com.opensymphony.xwork2.ActionSupport;


/**
 * EntryAction is a struts action class
 *
 * @author indunil Moremada
 * @author Chathranga
 * @author Amith
 * @author Duminda
 */
/**
 * EntryAction is a struts action class
 *
 * @author indunil Moremada
 * @author Chathranga
 * @author Amith
 * @author Duminda
 */
public class BirthRegisterAction extends ActionSupport implements ServletRequestAware, SessionAware {

    private String serialNumber;
    private String childDOB;
    private String year;
    private String month;
    private String day;
    /*childBirthDistrict is the birth place of the child*/
    private String childBirthDistrict;
    private String childBirthDivision;
    private String childBirthPlace;
    private String childFullNameOfficialLang;
    private String childFullNameEnglish;
    private String childGender;
    private float childBirthWeight;
    /*numOfLiveChildren refers to live children*/
    private int numOfLiveChildren;
    private int numOfMultipleBiths;
    private String hospitalOrGNCode;

    private String fatherNIC;
    private String fatherForeignerPassportNo;
    private String fatherForeignerCountry;
    private String fatherFullName;
    private String fatherDOB;
    private String fatherBirthPlace;
    private String fatherRace;
    private String fatherSignature;

    private String motherNIC;
    private String motherPassportNo;
    private String motherCountry;
    private String motherAdmissionNoAndDate;
    private String motherFullName;
    private String motherDOB;
    private String motherBirthPlace;
    private String motherRace;
    private int motherAgeAtBirth;
    private String motherAddress;
    private String motherPhoneNo;
    private String motherEmail;
    private String motherSignature;

    private String placeOfMarriage;
    private String dateOfMarriage;
    /**
     * motherSignature  if not married
     */
    //private String motherSignature;
    // /*fatherSignature  if not married*/
    //private String fatherSignature;

    private String grandFatherFullName;
    private String grandFatherBirthYear;
    private String grandFatherBirthPlace;

    private String greatGrandFatherFullName;
    private String greatGrandFatherBirthYear;
    private String greatGrandFatherBirthPlace;

    private String informantGuardian;
    private String informantName;
    private String informantNIC;
    private String informantPostalAddress;
    private String informantPhoneNo;
    private String informantEmail;
    /**
     * authority is the preson who confirms the birth
     */
    private String authority;
    //private String informantSignatureAndDate;
    private HttpServletRequest request;
    /*Bean which holds the birth registraion information*/
    private BirthRegister birthRegister;
    private Logger log = Logger.getLogger(this.getClass().getName());
    /*pageNo is used to decide the next pageNo of the Birht Registration Form*/
    private int pageNo;
    private BirthRegisterService service;

    private int language;
    private List<Person> myList;
    private Map session;

    public String welcome() {
        return Constant.RETURN_TYPE_SUCCESS;
    }

    public String login() {
        return Constant.RETURN_TYPE_SUCCESS;
    }

    public String pageLoad() {
        return "pageLoad";
    }

    /**
     * method is used for birth registration purposes birthRegister Entity
     * Bean is populated based on the variable values and return type is
     * decided based on the pageNo variable which is  passed from the
     * jsp page
     *
     * @return String which decides next page to be loaded
     */

    public BirthRegisterAction(BirthRegisterService service) {
//        MasterDataLoad masterDataLoad=MasterDataLoad.getInstance();
//        List<String> districtList=masterDataLoad.loadDistricts();
//        log.debug("District List:"+districtList);
//        List<String> countryList=masterDataLoad.loadCountries();

        this.service = service;
    }

    public String birthRegistrationPreProcessor() {
        switch (pageNo) {
            case 1:
                birthRegister = new BirthRegister();
                birthRegister.setChildDOB(new EPopDate().getDate(year + "/" + month + "/" + day));
                birthRegister.setSerialNumber(serialNumber);
                birthRegister.setChildBirthDistrict(childBirthDistrict);
                birthRegister.setChildBirthPlace(childBirthPlace);
                birthRegister.setChildBirthDivision(childBirthDivision);
                birthRegister.setChildFullNameOfficialLang(childFullNameOfficialLang);
                birthRegister.setChildFullNameEnglish(childFullNameEnglish);
                birthRegister.setChildGender(childGender);
                birthRegister.setChildBirthWeight(childBirthWeight);
                birthRegister.setNoOfLiveChildren(numOfLiveChildren);
                birthRegister.setNoOfMultipleBirths(numOfMultipleBiths);
                birthRegister.setHospitalOrGNCode(hospitalOrGNCode);
                session.put("birthRegister", birthRegister);
                return "form2";
            case 2:
                birthRegister = (BirthRegister) session.get("birthRegister");
                birthRegister.setFathersNIC(fatherNIC);
                birthRegister.setFatherForeignerPassportNo(fatherForeignerPassportNo);
                birthRegister.setFatherBirthPlace(fatherBirthPlace);
                birthRegister.setFatherForeignerCountry(fatherForeignerCountry);
                birthRegister.setFatherDOB(new EPopDate().getDate(fatherDOB));
                birthRegister.setFatherFullName(fatherFullName);
                birthRegister.setFatherRace(fatherRace);
                birthRegister.setMotherNIC(motherNIC);
                birthRegister.setMotherPassportNo(motherPassportNo);
                birthRegister.setMotherCountry(motherCountry);
                birthRegister.setMotherAdmissionNoAndDate(motherAdmissionNoAndDate);
                birthRegister.setMotherFullName(motherFullName);
                birthRegister.setMotherDOB(new EPopDate().getDate(motherDOB));
                birthRegister.setMotherBirthPlace(motherBirthPlace);
                birthRegister.setMotherRace(motherRace);
                birthRegister.setMotherAgeAtBirth(motherAgeAtBirth);
                birthRegister.setMotherAddress(motherAddress);
                birthRegister.setMotherEmail(motherEmail);
                session.put("birthRegister", birthRegister);
                return "form3";
            case 3:
                birthRegister = (BirthRegister) session.get("birthRegister");
                birthRegister.setPlaceOfMarriage(placeOfMarriage);
                birthRegister.setDateOfMarriage(new EPopDate().getDate(dateOfMarriage));
                birthRegister.setGrandFatherFullName(grandFatherFullName);
                birthRegister.setGrandFatherBirthYear(grandFatherBirthYear);
                birthRegister.setGrandFatherBirthPlace(grandFatherBirthPlace);
                birthRegister.setGreatGrandFatherBirthPlace(greatGrandFatherBirthPlace);
                birthRegister.setGreatGrandFatherBirthYear(greatGrandFatherBirthYear);
                birthRegister.setGreatGrandFatherFullName(greatGrandFatherFullName);
                birthRegister.setInformant(informantName);
                birthRegister.setInformantNIC(informantNIC);
                birthRegister.setInformantPhoneNo(informantPhoneNo);
                birthRegister.setInformantPostalAddress(informantPostalAddress);
                birthRegister.setInformantEmail(informantEmail);
                /*set the previously taken values to the next jsp*/
                this.setSerialNumber(birthRegister.getSerialNumber());
                this.setChildFullNameEnglish(birthRegister.getChildFullNameEnglish());
                this.setChildGender(birthRegister.getChildGender());
                this.setFatherFullName(birthRegister.getFatherFullName());
                this.setMotherFullName(birthRegister.getMotherFullName());
                this.setChildDOB(new EPopDate().getDateInString(birthRegister.getChildDOB()));
                session.put("birthRegister", birthRegister);
                return "form4";
        }
        return Constant.RETURN_TYPE_ERROR;
    }

    /**
     * creat Birth confermation report in PDF type
     * exception throws if pre compiled .jasper file is not available
     * <p/>
     * eng>>>1
     * sinhala>>2
     * tamil>>>3
     */
    public String getBirtConfermationReport() {
        /** List to use as our JasperReports dataSource.
         * Store people in our dataSource list (normally would come from database).
         */
        language = 1;
        switch (language) {
            case 1: {
                Person p = new Person(new Long(1), "amith", "jayasekara");
                myList = new ArrayList<Person>();
                myList.add(p);
                break;
            }
            case 2: {
                Person p = new Person(new Long(1), "", "");
                myList = new ArrayList<Person>();
                myList.add(p);
                break;
            }
            case 3: {
                Person p = new Person(new Long(1), "amith", "jayasekara");
                myList = new ArrayList<Person>();
                myList.add(p);
                break;
            }
        }
        return Constant.RETURN_TYPE_SUCCESS;
    }

    /**
     * birthRegisterFinalizer is called by the fourth jsp page of the Birth
     * Registration Form it finalize the birthRegister Entity bean and send
     * it to the Business delegate
     *
     * @return String
     */
    public String birthRegisterFinalizer() {
        birthRegister = (BirthRegister) session.get("birthRegister");
        birthRegister.setAuthority(authority);
        birthRegister.setSerialNumber(serialNumber);
        birthRegister.setFatherFullName(fatherFullName);
        birthRegister.setMotherFullName(motherFullName);
        birthRegister.setChildFullNameEnglish(childFullNameEnglish);
        birthRegister.setChildDOB(new EPopDate().getDate(childDOB));
        service.birthRegistration(birthRegister);
        return Constant.RETURN_TYPE_SUCCESS;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getChildBirthDistrict() {
        return childBirthDistrict;
    }

    public void setChildBirthDistrict(String childBirthDistrict) {
        this.childBirthDistrict = childBirthDistrict;
    }

    public String getChildBirthDivision() {
        return childBirthDivision;
    }

    public void setChildBirthDivision(String childBirthDivision) {
        this.childBirthDivision = childBirthDivision;
    }

    public String getChildBirthPlace() {
        return childBirthPlace;
    }

    public void setChildBirthPlace(String childBirthPlace) {
        this.childBirthPlace = childBirthPlace;
    }

    public String getChildFullNameOfficialLang() {
        return childFullNameOfficialLang;
    }

    public void setChildFullNameOfficialLang(String childFullNameOfficialLang) {
        this.childFullNameOfficialLang = childFullNameOfficialLang;
    }

    public String getChildFullNameEnglish() {
        return childFullNameEnglish;
    }

    public void setChildFullNameEnglish(String childFullNameEnglish) {
        this.childFullNameEnglish = childFullNameEnglish;
    }

    public String getChildGender() {
        return childGender;
    }

    public void setChildGender(String childGender) {
        this.childGender = childGender;
    }

    public float getChildBirthWeight() {
        return childBirthWeight;
    }

    public void setChildBirthWeight(float childBirthWeight) {
        this.childBirthWeight = childBirthWeight;
    }

    public int getNumOfLiveChildren() {
        return numOfLiveChildren;
    }

    public void setNumOfLiveChildren(int numOfLiveChildren) {
        this.numOfLiveChildren = numOfLiveChildren;
    }

    public int getNumOfMultipleBiths() {
        return numOfMultipleBiths;
    }

    public void setNumOfMultipleBiths(int numOfMultipleBiths) {
        this.numOfMultipleBiths = numOfMultipleBiths;
    }

    public String getHospitalOrGNCode() {
        return hospitalOrGNCode;
    }

    public void setHospitalOrGNCode(String hospitalOrGNCode) {
        this.hospitalOrGNCode = hospitalOrGNCode;
    }

    public String getFatherNIC() {
        return fatherNIC;
    }

    public void setFatherNIC(String fatherNIC) {
        this.fatherNIC = fatherNIC;
    }

    public String getFatherForeignerPassportNo() {
        return fatherForeignerPassportNo;
    }

    public void setFatherForeignerPassportNo(String fatherForeignerPassportNo) {
        this.fatherForeignerPassportNo = fatherForeignerPassportNo;
    }

    public String getFatherForeignerCountry() {
        return fatherForeignerCountry;
    }

    public void setFatherForeignerCountry(String fatherForeignerCountry) {
        this.fatherForeignerCountry = fatherForeignerCountry;
    }

    public String getFatherFullName() {
        return fatherFullName;
    }

    public void setFatherFullName(String fatherFullName) {
        this.fatherFullName = fatherFullName;
    }

    public String getFatherDOB() {
        return fatherDOB;
    }

    public void setFatherDOB(String fatherDOB) {
        this.fatherDOB = fatherDOB;
    }

    public String getFatherBirthPlace() {
        return fatherBirthPlace;
    }

    public void setFatherBirthPlace(String fatherBirthPlace) {
        this.fatherBirthPlace = fatherBirthPlace;
    }

    public String getFatherRace() {
        return fatherRace;
    }

    public void setFatherRace(String fatherRace) {
        this.fatherRace = fatherRace;
    }

    public String getMotherNIC() {
        return motherNIC;
    }

    public void setMotherNIC(String motherNIC) {
        this.motherNIC = motherNIC;
    }

    public String getMotherPassportNo() {
        return motherPassportNo;
    }

    public void setMotherPassportNo(String motherPassportNo) {
        this.motherPassportNo = motherPassportNo;
    }

    public String getMotherCountry() {
        return motherCountry;
    }

    public void setMotherCountry(String motherCountry) {
        this.motherCountry = motherCountry;
    }

    public String getMotherAdmissionNoAndDate() {
        return motherAdmissionNoAndDate;
    }

    public void setMotherAdmissionNoAndDate(String motherAdmissionNoAndDate) {
        this.motherAdmissionNoAndDate = motherAdmissionNoAndDate;
    }

    public String getMotherFullName() {
        return motherFullName;
    }

    public void setMotherFullName(String motherFullName) {
        this.motherFullName = motherFullName;
    }

    public String getMotherDOB() {
        return motherDOB;
    }

    public void setMotherDOB(String motherDOB) {
        this.motherDOB = motherDOB;
    }

    public String getMotherBirthPlace() {
        return motherBirthPlace;
    }

    public void setMotherBirthPlace(String motherBirthPlace) {
        this.motherBirthPlace = motherBirthPlace;
    }

    public String getMotherRace() {
        return motherRace;
    }

    public void setMotherRace(String motherRace) {
        this.motherRace = motherRace;
    }

    public int getMotherAgeAtBirth() {
        return motherAgeAtBirth;
    }

    public void setMotherAgeAtBirth(int motherAgeAtBirth) {
        this.motherAgeAtBirth = motherAgeAtBirth;
    }

    public String getMotherAddress() {
        return motherAddress;
    }

    public void setMotherAddress(String motherAddress) {
        this.motherAddress = motherAddress;
    }

    public String getMotherPhoneNo() {
        return motherPhoneNo;
    }

    public void setMotherPhoneNo(String motherPhoneNo) {
        this.motherPhoneNo = motherPhoneNo;
    }

    public String getMotherEmail() {
        return motherEmail;
    }

    public void setMotherEmail(String motherEmail) {
        this.motherEmail = motherEmail;
    }

    public String getPlaceOfMarriage() {
        return placeOfMarriage;
    }

    public void setPlaceOfMarriage(String placeOfMarriage) {
        this.placeOfMarriage = placeOfMarriage;
    }

    public String getDateOfMarriage() {
        return dateOfMarriage;
    }

    public void setDateOfMarriage(String dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
    }

    public String getGrandFatherFullName() {
        return grandFatherFullName;
    }

    public void setGrandFatherFullName(String grandFatherFullName) {
        this.grandFatherFullName = grandFatherFullName;
    }

    public String getGrandFatherBirthYear() {
        return grandFatherBirthYear;
    }

    public void setGrandFatherBirthYear(String grandFatherBirthYear) {
        this.grandFatherBirthYear = grandFatherBirthYear;
    }

    public String getGrandFatherBirthPlace() {
        return grandFatherBirthPlace;
    }

    public void setGrandFatherBirthPlace(String grandFatherBirthPlace) {
        this.grandFatherBirthPlace = grandFatherBirthPlace;
    }

    public String getGreatGrandFatherFullName() {
        return greatGrandFatherFullName;
    }

    public void setGreatGrandFatherFullName(String greatGrandFatherFullName) {
        this.greatGrandFatherFullName = greatGrandFatherFullName;
    }

    public String getGreatGrandFatherBirthYear() {
        return greatGrandFatherBirthYear;
    }

    public void setGreatGrandFatherBirthYear(String greatGrandFatherBirthYear) {
        this.greatGrandFatherBirthYear = greatGrandFatherBirthYear;
    }

    public String getGreatGrandFatherBirthPlace() {
        return greatGrandFatherBirthPlace;
    }

    public void setGreatGrandFatherBirthPlace(String greatGrandFatherBirthPlace) {
        this.greatGrandFatherBirthPlace = greatGrandFatherBirthPlace;
    }

    public String getInformantName() {
        return informantName;
    }

    public void setInformantName(String informantName) {
        this.informantName = informantName;
    }

    public String getInformantNIC() {
        return informantNIC;
    }

    public void setInformantNIC(String informantNIC) {
        this.informantNIC = informantNIC;
    }

    public String getInformantPostalAddress() {
        return informantPostalAddress;
    }

    public void setInformantPostalAddress(String informantPostalAddress) {
        this.informantPostalAddress = informantPostalAddress;
    }

    public String getInformantPhoneNo() {
        return informantPhoneNo;
    }

    public void setInformantPhoneNo(String informantPhoneNo) {
        this.informantPhoneNo = informantPhoneNo;
    }

    public String getInformantEmail() {
        return informantEmail;
    }

    public void setInformantEmail(String informantEmail) {
        this.informantEmail = informantEmail;
    }

    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

    public String getInformantGuardian() {
        return informantGuardian;
    }

    public void setInformantGuardian(String informantGuardian) {
        this.informantGuardian = informantGuardian;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public BirthRegister getBirthRegister() {
        return birthRegister;
    }

    public void setBirthRegister(BirthRegister birthRegister) {
        this.birthRegister = birthRegister;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public List<Person> getMyList() {
        return myList;
    }

    public void setMyList(List<Person> myList) {
        this.myList = myList;
    }

    public void setSession(Map map) {
        this.session = map;
    }

    public Map getSession() {
        return session;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFatherSignature() {
        return fatherSignature;
    }

    public void setFatherSignature(String fatherSignature) {
        this.fatherSignature = fatherSignature;
    }

    public String getMotherSignature() {
        return motherSignature;
    }

    public void setMotherSignature(String motherSignature) {
        this.motherSignature = motherSignature;
    }

    public String getChildDOB() {
        return childDOB;
    }

    public void setChildDOB(String childDOB) {
        this.childDOB = childDOB;
    }
}

