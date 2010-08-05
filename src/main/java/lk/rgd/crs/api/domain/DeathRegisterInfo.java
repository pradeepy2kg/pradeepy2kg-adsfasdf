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
    private String placeOfDeath;
    private Date dateOfDeath;
    private String timeOfDeath;
    private int deathDivisionId;
    private String placeOfDeathInEnglish;
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
    private String deathPersonName;
    private String deathPersonNameInEnglish;
    private String deathPersonPermanentAddress;
    private String deathPersonFatherPINorNIC;
    private String deathPersonFatherFullName;
    private String deathPersonMotherNICNo;
    private String deathPersonMotherFullName;


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
        return deathPersonName;
    }

    public void setDeathPersonName(String deathPersonName) {
        this.deathPersonName = deathPersonName;
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
        return deathPersonMotherNICNo;
    }

    public void setDeathPersonMotherNICNo(String deathPersonMotherNICNo) {
        this.deathPersonMotherNICNo = deathPersonMotherNICNo;
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
}
