package lk.rgd.crs.api.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Ashoka Ekanayaka
 * Entity to hold objection information for a Marriage Notice.
 */
@Entity
@Table(name = "MARRIAGE_OBJECTION", schema = "CRS")

public class MarriageObjection implements Serializable, Cloneable {
    public enum Reason {
        AGE,
        LEGAL,
        RELATION,
        CONSENT,
        OTHER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idukey;

    @Column(nullable = false)
    private String serialNumberOfNotice;

    @Column
    private Date dateOfNotice;

    @Column
    private long registrarPIN;

    @Column(nullable = false)
    private Date acceptedDate;

    @Column(nullable = false)
    private Reason reason;

    @Column
    private long malePartyPIN;

    @Column
    private String malePartyName;

    @Column
    private String malePartyAddress;

    @Column
    private long femalePartyPIN;

    @Column
    private String femalePartyName;

    @Column
    private String femalePartyAddress;

    @Column(nullable = false)
    private long objectingPartyPIN;

    @Column(nullable = false)
    private String objectingPartyName;

    @Column(nullable = false)
    private String objectingPartyAddress;

    @Column(nullable = false)
    private Date signedDate;

    @Column(nullable = false)
    private String witness1PIN;

    @Column(nullable = false)
    private String witness1Name;

    @Column(nullable = false)
    private String witness2PIN;

    @Column(nullable = false)
    private String witness2Name;

    public long getIdukey() {
        return idukey;
    }

    public void setIdukey(long idukey) {
        this.idukey = idukey;
    }

    public String getSerialNumberOfNotice() {
        return serialNumberOfNotice;
    }

    public void setSerialNumberOfNotice(String serialNumberOfNotice) {
        this.serialNumberOfNotice = serialNumberOfNotice;
    }

    public Date getDateOfNotice() {
        return dateOfNotice;
    }

    public void setDateOfNotice(Date dateOfNotice) {
        this.dateOfNotice = dateOfNotice;
    }

    public long getRegistrarPIN() {
        return registrarPIN;
    }

    public void setRegistrarPIN(long registrarPIN) {
        this.registrarPIN = registrarPIN;
    }

    public Date getAcceptedDate() {
        return acceptedDate;
    }

    public void setAcceptedDate(Date acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public long getMalePartyPIN() {
        return malePartyPIN;
    }

    public void setMalePartyPIN(long malePartyPIN) {
        this.malePartyPIN = malePartyPIN;
    }

    public String getMalePartyName() {
        return malePartyName;
    }

    public void setMalePartyName(String malePartyName) {
        this.malePartyName = malePartyName;
    }

    public String getMalePartyAddress() {
        return malePartyAddress;
    }

    public void setMalePartyAddress(String malePartyAddress) {
        this.malePartyAddress = malePartyAddress;
    }

    public long getFemalePartyPIN() {
        return femalePartyPIN;
    }

    public void setFemalePartyPIN(long femalePartyPIN) {
        this.femalePartyPIN = femalePartyPIN;
    }

    public String getFemalePartyName() {
        return femalePartyName;
    }

    public void setFemalePartyName(String femalePartyName) {
        this.femalePartyName = femalePartyName;
    }

    public String getFemalePartyAddress() {
        return femalePartyAddress;
    }

    public void setFemalePartyAddress(String femalePartyAddress) {
        this.femalePartyAddress = femalePartyAddress;
    }

    public long getObjectingPartyPIN() {
        return objectingPartyPIN;
    }

    public void setObjectingPartyPIN(long objectingPartyPIN) {
        this.objectingPartyPIN = objectingPartyPIN;
    }

    public String getObjectingPartyName() {
        return objectingPartyName;
    }

    public void setObjectingPartyName(String objectingPartyName) {
        this.objectingPartyName = objectingPartyName;
    }

    public String getObjectingPartyAddress() {
        return objectingPartyAddress;
    }

    public void setObjectingPartyAddress(String objectingPartyAddress) {
        this.objectingPartyAddress = objectingPartyAddress;
    }

    public Date getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(Date signedDate) {
        this.signedDate = signedDate;
    }

    public String getWitness1PIN() {
        return witness1PIN;
    }

    public void setWitness1PIN(String witness1PIN) {
        this.witness1PIN = witness1PIN;
    }

    public String getWitness1Name() {
        return witness1Name;
    }

    public void setWitness1Name(String witness1Name) {
        this.witness1Name = witness1Name;
    }

    public String getWitness2PIN() {
        return witness2PIN;
    }

    public void setWitness2PIN(String witness2PIN) {
        this.witness2PIN = witness2PIN;
    }

    public String getWitness2Name() {
        return witness2Name;
    }

    public void setWitness2Name(String witness2Name) {
        this.witness2Name = witness2Name;
    }
}
