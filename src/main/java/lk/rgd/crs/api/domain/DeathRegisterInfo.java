package lk.rgd.crs.api.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: tharanga
 * Date: Aug 5, 2010
 * Time: 3:57:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeathRegisterInfo implements Serializable {


    //information of death
    private long deathSerialNo;
    private long deathCertificateNo;
    private String placeOfDeath;
    private Date dateOfDeath;
    private Date dateOfRegistration;
    private Date dateOfIssue;
    private String placeOfIssue;
    private String timeOfDeath;
    private int deathDivisionId;
    private String placeOfDeathInEnglish;
    private String placeOfDeathInOfficialLang;
    private boolean causeOfDeathEstablished;
    private boolean infantLessThan30Days;
    private String causeOfDeath;
    private String icdCodeOfCause;
    private String placeOfBurial;

    //information about the person departed
    private String deathPersonPINorNIC;
    private int deathPersonCountryId;
    private String deathPersonPassportNo;
    private String deathPersonAge;
    private int deathPersonGender;
    private int deathPersonRace;
    private String deathPersonNameOfficialLang;
    private String deathPersonNameInEnglish;
    private String deathPersonPermanentAddress;
    private String deathPersonFatherPINorNIC;
    private String deathPersonFatherFullName;
    private String deathPersonMotherPINorNIC;
    private String deathPersonMotherFullName;

    // Declarant Information
    private String declarantNICorPIN;
    private String declarantFullName;
    private String declarantAddress;
    private String declarantPhone;
    private String declarantEMail;
    private int declarantType;

    // Witnesses Information
    private String firstWitnessNICorPIN;
    private String secondWitnessNICorPIN;
    private String firstWitnessFullName;
    private String secondWitnessFullName;
    private String firstWitnessAddress;

    public void setFirstWitnessNICorPIN(String firstWitnessNICorPIN) {
        this.firstWitnessNICorPIN = firstWitnessNICorPIN;
    }

    public void setSecondWitnessNICorPIN(String secondWitnessNICorPIN) {
        this.secondWitnessNICorPIN = secondWitnessNICorPIN;
    }

    public void setFirstWitnessFullName(String firstWitnessFullName) {
        this.firstWitnessFullName = firstWitnessFullName;
    }

    public void setSecondWitnessFullName(String secondWitnessFullName) {
        this.secondWitnessFullName = secondWitnessFullName;
    }

    public void setFirstWitnessAddress(String firstWitnessAddress) {
        this.firstWitnessAddress = firstWitnessAddress;
    }

    public void setSecondWitnessAddress(String secondWitnessAddress) {
        this.secondWitnessAddress = secondWitnessAddress;
    }

    public String getFirstWitnessNICorPIN() {

        return firstWitnessNICorPIN;
    }

    public String getSecondWitnessNICorPIN() {
        return secondWitnessNICorPIN;
    }

    public String getFirstWitnessFullName() {
        return firstWitnessFullName;
    }

    public String getSecondWitnessFullName() {
        return secondWitnessFullName;
    }

    public String getFirstWitnessAddress() {
        return firstWitnessAddress;
    }

    public String getSecondWitnessAddress() {
        return secondWitnessAddress;
    }

    private String secondWitnessAddress;


    public void setDeclarantNICorPIN(String declarantNICorPIN) {
        this.declarantNICorPIN = declarantNICorPIN;
    }

    public void setDeclarantFullName(String declarantFullName) {
        this.declarantFullName = declarantFullName;
    }

    public void setDeclarantAddress(String declarantAddress) {
        this.declarantAddress = declarantAddress;
    }

    public void setDeclarantPhone(String declarantPhone) {
        this.declarantPhone = declarantPhone;
    }

    public void setDeclarantEMail(String declarantEMail) {
        this.declarantEMail = declarantEMail;
    }

    public void setDeclarantType(int declarantType) {
        this.declarantType = declarantType;
    }

    public String getDeclarantNICorPIN() {
        return declarantNICorPIN;
    }

    public String getDeclarantFullName() {
        return declarantFullName;
    }

    public String getDeclarantAddress() {
        return declarantAddress;
    }

    public String getDeclarantPhone() {
        return declarantPhone;
    }

    public String getDeclarantEMail() {
        return declarantEMail;
    }

    public int getDeclarantType() {
        return declarantType;
    }

    public String getPlaceOfDeath() {
        return placeOfDeath;
    }

    public void setPlaceOfDeath(String placeOfDeath) {
        this.placeOfDeath = placeOfDeath;
    }


    public Date getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(Date dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public String getTimeOfDeath() {
        return timeOfDeath;
    }

    public void setTimeOfDeath(String timeOfDeath) {
        this.timeOfDeath = timeOfDeath;
    }

    public int getDeathDivisionId() {
        return deathDivisionId;
    }

    public void setDeathDivisionId(int deathDivisionId) {
        this.deathDivisionId = deathDivisionId;
    }

    public String getPlaceOfDeathInEnglish() {
        return placeOfDeathInEnglish;
    }

    public void setPlaceOfDeathInEnglish(String placeOfDeathInEnglish) {
        this.placeOfDeathInEnglish = placeOfDeathInEnglish;
    }

    public boolean isCauseOfDeathEstablished() {
        return causeOfDeathEstablished;
    }

    public void setCauseOfDeathEstablished(boolean causeOfDeathEstablished) {
        this.causeOfDeathEstablished = causeOfDeathEstablished;
    }

    public boolean isInfantLessThan30Days() {
        return infantLessThan30Days;
    }

    public void setInfantLessThan30Days(boolean infantLessThan30Days) {
        this.infantLessThan30Days = infantLessThan30Days;
    }

    public String getCauseOfDeath() {
        return causeOfDeath;
    }

    public void setCauseOfDeath(String causeOfDeath) {
        this.causeOfDeath = causeOfDeath;
    }

    public String getIcdCodeOfCause() {
        return icdCodeOfCause;
    }

    public void setIcdCodeOfCause(String icdCodeOfCause) {
        this.icdCodeOfCause = icdCodeOfCause;
    }

    public String getPlaceOfBurial() {
        return placeOfBurial;
    }

    public void setPlaceOfBurial(String placeOfBurial) {
        this.placeOfBurial = placeOfBurial;
    }

    public String getDeathPersonPINorNIC() {
        return deathPersonPINorNIC;
    }

    public void setDeathPersonPINorNIC(String deathPersonPINorNIC) {
        this.deathPersonPINorNIC = deathPersonPINorNIC;
    }

    public int getDeathPersonCountryId() {
        return deathPersonCountryId;
    }

    public void setDeathPersonCountryId(int deathPersonCountryId) {
        this.deathPersonCountryId = deathPersonCountryId;
    }

    public String getDeathPersonPassportNo() {
        return deathPersonPassportNo;
    }

    public void setDeathPersonPassportNo(String deathPersonPassportNo) {
        this.deathPersonPassportNo = deathPersonPassportNo;
    }

    public String getDeathPersonAge() {
        return deathPersonAge;
    }

    public void setDeathPersonAge(String deathPersonAge) {
        this.deathPersonAge = deathPersonAge;
    }

    public int getDeathPersonGender() {
        return deathPersonGender;
    }

    public void setDeathPersonGender(int deathPersonGender) {
        this.deathPersonGender = deathPersonGender;
    }

    public int getDeathPersonRace() {
        return deathPersonRace;
    }

    public void setDeathPersonRace(int deathPersonRace) {
        this.deathPersonRace = deathPersonRace;
    }

    public String getDeathPersonName() {
        return deathPersonNameOfficialLang;
    }

    public void setDeathPersonName(String deathPersonName) {
        this.deathPersonNameOfficialLang = deathPersonName;
    }

    public String getDeathPersonNameInEnglish() {
        return deathPersonNameInEnglish;
    }

    public void setDeathPersonNameInEnglish(String deathPersonNameInEnglish) {
        this.deathPersonNameInEnglish = deathPersonNameInEnglish;
    }

    public String getDeathPersonFatherNICNo() {
        return deathPersonFatherPINorNIC;
    }

    public void setDeathPersonFatherNICNo(String deathPersonFatherNICNo) {
        this.deathPersonFatherPINorNIC = deathPersonFatherNICNo;
    }

    public String getDeathPersonFatherFullName() {
        return deathPersonFatherFullName;
    }

    public void setDeathPersonFatherFullName(String deathPersonFatherFullName) {
        this.deathPersonFatherFullName = deathPersonFatherFullName;
    }

    public String getDeathPersonMotherNICNo() {
        return deathPersonMotherPINorNIC;
    }

    public void setDeathPersonMotherNICNo(String deathPersonMotherNICNo) {
        this.deathPersonMotherPINorNIC = deathPersonMotherNICNo;
    }

    public String getDeathPersonMotherFullName() {
        return deathPersonMotherFullName;
    }

    public void setDeathPersonMotherFullName(String deathPersonMotherFullName) {
        this.deathPersonMotherFullName = deathPersonMotherFullName;
    }


    public String getDeathPersonPermanentAddress() {
        return deathPersonPermanentAddress;
    }

    public void setDeathPersonPermanentAddress(String deathPersonPermanentAddress) {
        this.deathPersonPermanentAddress = deathPersonPermanentAddress;
    }

    public long getDeathSerialNo() {
        return deathSerialNo;
    }

    public void setDeathSerialNo(long deathSerialNo) {
        this.deathSerialNo = deathSerialNo;
    }

    public Date getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(Date dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public String getDeclarantType() {
        return declarantType;
    }

    public void setDeclarantType(String declarantType) {
        this.declarantType = declarantType;
    }

    public String getDeclarantEmail() {
        return declarantEmail;
    }

    public void setDeclarantEmail(String declarantEmail) {
        this.declarantEmail = declarantEmail;
    }

    public String getDeclarantPhoneNo() {
        return declarantPhoneNo;
    }

    public void setDeclarantPhoneNo(String declarantPhoneNo) {
        this.declarantPhoneNo = declarantPhoneNo;
    }

    public String getDeclarantAddress() {
        return declarantAddress;
    }

    public void setDeclarantAddress(String declarantAddress) {
        this.declarantAddress = declarantAddress;
    }

    public String getDeclarantName() {
        return declarantName;
    }

    public void setDeclarantName(String declarantName) {
        this.declarantName = declarantName;
    }

    public String getDeclarantPINorNIC() {
        return declarantPINorNIC;
    }

    public void setDeclarantPINorNIC(String declarantPINorNIC) {
        this.declarantPINorNIC = declarantPINorNIC;
    }

    public String getDeathPersonFatherPINorNIC() {
        return deathPersonFatherPINorNIC;
    }

    public void setDeathPersonFatherPINorNIC(String deathPersonFatherPINorNIC) {
        this.deathPersonFatherPINorNIC = deathPersonFatherPINorNIC;
    }

    public String getPlaceOfDeathInOfficialLang() {
        return placeOfDeathInOfficialLang;
    }

    public void setPlaceOfDeathInOfficialLang(String placeOfDeathInOfficialLang) {
        this.placeOfDeathInOfficialLang = placeOfDeathInOfficialLang;
    }

    public long getDeathCertificateNo() {
        return deathCertificateNo;
    }

    public void setDeathCertificateNo(long deathCertificateNo) {
        this.deathCertificateNo = deathCertificateNo;
    }

    public Date getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(Date dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public String getPlaceOfIssue() {
        return placeOfIssue;
    }

    public void setPlaceOfIssue(String placeOfIssue) {
        this.placeOfIssue = placeOfIssue;
    }
}
