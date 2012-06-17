package lk.rgd.crs.api.domain;

import lk.rgd.common.util.WebUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class MarriageInfo implements Serializable, Cloneable {

    /**
     * The Enumeration defining the type of married status.
     */
    public enum MarriedStatus {
        UNKNOWN,                /** 0 - married status unknown */
        MARRIED,                 /** 1 - married */
        UNMARRIED,              /** 2 - unmarried */
        NO_SINCE_MARRIED  ,    /** 3 - no but married later - e.g. for births -after child birth */
    }

    /**
     * @see lk.rgd.crs.api.domain.MarriageInfo.MarriedStatus
     */
    @Enumerated
    @Column(nullable = true)
    private MarriedStatus parentsMarried = MarriedStatus.MARRIED;

    /**
     * Parents married status as a String in the preferred language
     */
    @Transient
    private String parentsMarriedPrint;

    /**
     * Place of marriage
     */
    @Column(nullable = true, length = 60)
    private String placeOfMarriage;

    /**
     * Date of marriage
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfMarriage;

    /**
     * If parents are unmarried - Has the mother signed to include fathers details?
     */
    @Column(nullable = true)
    private boolean motherSigned;

    /**
     * If parents are unmarried - Has the father signed to include fathers details?
     */
    @Column(nullable = true)
    private boolean fatherSigned;

    public MarriedStatus getParentsMarried() {
        return parentsMarried;
    }

    public void setParentsMarried(MarriedStatus parentsMarried) {
        this.parentsMarried = parentsMarried;
    }

    public String getParentsMarriedPrint() {
        return parentsMarriedPrint;
    }

    public void setParentsMarriedPrint(String parentsMarriedPrint) {
        this.parentsMarriedPrint = parentsMarriedPrint;
    }

    public String getPlaceOfMarriage() {
        return placeOfMarriage;
    }

    public void setPlaceOfMarriage(String placeOfMarriage) {
        this.placeOfMarriage = WebUtils.filterBlanksAndToUpper(placeOfMarriage);
    }

    public Date getDateOfMarriage() {
        return dateOfMarriage;
    }

    public void setDateOfMarriage(Date dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
    }

    public boolean isMotherSigned() {
        return motherSigned;
    }

    public void setMotherSigned(boolean motherSigned) {
        this.motherSigned = motherSigned;
    }

    public boolean isFatherSigned() {
        return fatherSigned;
    }

    public void setFatherSigned(boolean fatherSigned) {
        this.fatherSigned = fatherSigned;
    }

    @Override
    protected MarriageInfo clone() throws CloneNotSupportedException {
        return (MarriageInfo) super.clone();
    }
}