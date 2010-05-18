package lk.rgd.crs.api.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "birth_register")
public class BirthRegister {
    @Id
    private String serialNumber;
    private String confirmSerialNumber;

    private Date childDOB;
    private String childBirthDistrict;           // place of birth
    private String childBirthDivision;           // place of birth
    private String childBirthPlace;              // place of birth
    private String childFullNameOfficialLang;
    private String childFullNameEnglish;
    private String childGender;
    private float childBirthWeight;
    private int noOfLiveChildren;              // according to live birth
    private int noOfMultipleBirths;          // if multiple births, num of children
    private String hospitalOrGNCode;

    private String fathersNIC;
    private String fatherForeignerPassportNo;        // if foreigner
    private String fatherForeignerCountry;           // if foreigner
    private String fatherFullName;
    private Date fatherDOB;
    private String fatherBirthPlace;
    private String fatherRace;

    private String motherNIC;
    private String motherPassportNo;        // if foreigner
    private String motherCountry;           // if foreigner
    private String motherAdmissionNoAndDate;
    private String motherFullName;
    private Date motherDOB;
    private String motherBirthPlace;
    private String motherRace;
    private int motherAgeAtBirth;
    private String motherAddress;
    private String motherPhoneNo;
    private String motherEmail;

    private String placeOfMarriage;
    private Date dateOfMarriage;

    private String grandFatherFullName;     // If grandfather of the child born in Sri Lanka
    private String grandFatherBirthYear;    // If grandfather of the child born in Sri Lanka
    private String grandFatherBirthPlace;   // If grandfather of the child born in Sri Lanka

    //If the father was not born in Sri Lanka and if great grandfather born in Sri Lanka
    private String greatGrandFatherFullName;
    private String greatGrandFatherBirthYear;
    private String greatGrandFatherBirthPlace;

    private String informant;
    private String informantName;
    private String informantNIC;
    private String informantPostalAddress;
    private String informantPhoneNo;
    private String informantEmail;
    private String authority;

    private String birthRegistrationSerialNumber;
    private String year;
    private String month;
    private String day;
    private String dobYear;
    private String dobMonth;
    private String dobDay;
    private String marriedStatus;

    private String confirmantFullName;
    private String confirmYear;
    private String confirmMonth;
    private String confirmDay;
    private String finalizeYear;
    private String finalizeMonth;
    private String finalizeDay;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getChildDOB() {
        return childDOB;
    }

    public void setChildDOB(Date childDOB) {
        this.childDOB = childDOB;
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

    public int getNoOfLiveChildren() {
        return noOfLiveChildren;
    }

    public void setNoOfLiveChildren(int noOfLiveChildren) {
        this.noOfLiveChildren = noOfLiveChildren;
    }

    public int getNoOfMultipleBirths() {
        return noOfMultipleBirths;
    }

    public void setNoOfMultipleBirths(int noOfMultipleBirths) {
        this.noOfMultipleBirths = noOfMultipleBirths;
    }

    public String getHospitalOrGNCode() {
        return hospitalOrGNCode;
    }

    public void setHospitalOrGNCode(String hospitalOrGNCode) {
        this.hospitalOrGNCode = hospitalOrGNCode;
    }

    public String getFathersNIC() {
        return fathersNIC;
    }

    public void setFathersNIC(String fathersNIC) {
        this.fathersNIC = fathersNIC;
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

    public Date getFatherDOB() {
        return fatherDOB;
    }

    public void setFatherDOB(Date fatherDOB) {
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

    public Date getMotherDOB() {
        return motherDOB;
    }

    public void setMotherDOB(Date motherDOB) {
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

    public Date getDateOfMarriage() {
        return dateOfMarriage;
    }

    public void setDateOfMarriage(Date dateOfMarriage) {
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

    public String getInformant() {
        return informant;
    }

    public void setInformant(String informant) {
        this.informant = informant;
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


    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getBirthRegistrationSerialNumber() {
        return birthRegistrationSerialNumber;
    }

    public void setBirthRegistrationSerialNumber(String birthRegistrationSerialNumber) {
        this.birthRegistrationSerialNumber = birthRegistrationSerialNumber;
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

    public String getDobYear() {
        return dobYear;
    }

    public void setDobYear(String dobYear) {
        this.dobYear = dobYear;
    }

    public String getDobMonth() {
        return dobMonth;
    }

    public void setDobMonth(String dobMonth) {
        this.dobMonth = dobMonth;
    }

    public String getDobDay() {
        return dobDay;
    }

    public void setDobDay(String dobDay) {
        this.dobDay = dobDay;
    }

    public String getMarriedStatus() {
        return marriedStatus;
    }

    public void setMarriedStatus(String marriedStatus) {
        this.marriedStatus = marriedStatus;
    }

    public String getConfirmantFullName() {
        return confirmantFullName;
    }

    public void setConfirmantFullName(String confirmantFullName) {
        this.confirmantFullName = confirmantFullName;
    }

    public String getConfirmYear() {
        return confirmYear;
    }

    public void setConfirmYear(String confirmYear) {
        this.confirmYear = confirmYear;
    }

    public String getConfirmMonth() {
        return confirmMonth;
    }

    public void setConfirmMonth(String confirmMonth) {
        this.confirmMonth = confirmMonth;
    }

    public String getConfirmDay() {
        return confirmDay;
    }

    public void setConfirmDay(String confirmDay) {
        this.confirmDay = confirmDay;
    }

    public String getFinalizeYear() {
        return finalizeYear;
    }

    public void setFinalizeYear(String finalizeYear) {
        this.finalizeYear = finalizeYear;
    }

    public String getFinalizeMonth() {
        return finalizeMonth;
    }

    public void setFinalizeMonth(String finalizeMonth) {
        this.finalizeMonth = finalizeMonth;
    }

    public String getFinalizeDay() {
        return finalizeDay;
    }

    public void setFinalizeDay(String finalizeDay) {
        this.finalizeDay = finalizeDay;
    }

    public String getConfirmSerialNumber() {
        return confirmSerialNumber;
    }

    public void setConfirmSerialNumber(String confirmSerialNumber) {
        this.confirmSerialNumber = confirmSerialNumber;
    }
}