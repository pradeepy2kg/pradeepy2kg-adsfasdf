package lk.rgd.crs.web.model;

import java.util.Date;

public class MarriageInfo {
    /** Place of marriage     */
    private String placeOfMarriage;

    /** Were parents married at birth - 0 - no, 1 - yes, 2 - no but married later */
    private int parentsMarried;

    /** Date of marriage */
    private Date dateOfMarriage;

    /** If parents are unmarried - Has the mother signed to include fathers details? */
    private boolean motherSigned;

    /** If parents are unmarried - Has the father signed to include fathers details? */
    private boolean fatherSigned;

    public int getParentsMarried() {
        return parentsMarried;
    }

    public void setParentsMarried(int parentsMarried) {
        this.parentsMarried = parentsMarried;
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