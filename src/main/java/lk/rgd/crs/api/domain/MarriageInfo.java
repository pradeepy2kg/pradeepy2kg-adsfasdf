package lk.rgd.crs.api.domain;

import lk.rgd.crs.web.util.WebUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class MarriageInfo implements Serializable {
    /**
     * Were parents married at birth - 0 - unknown, 1 - yes, 2 - no, 3 - no but married later 
     */
    @Column(nullable = true)
    private Integer parentsMarried;

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

    public Integer getParentsMarried() {
        return parentsMarried;
    }

    public void setParentsMarried(Integer parentsMarried) {
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
}