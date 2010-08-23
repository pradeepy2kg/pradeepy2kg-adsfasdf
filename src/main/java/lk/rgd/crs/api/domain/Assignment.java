package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.BaseLifeCycleInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Represents an assignment as a Birth, Death or Marriage Registrar
 *
 * @author asankha
 */
public class Assignment implements Serializable {

    /**
     * The types of assignments
     */
    public enum Type {
        MARRIAGE, DEATH, BIRTH
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long assignmentUKey;

    @Embedded
    private BaseLifeCycleInfo lifeCycleInfo = new BaseLifeCycleInfo();

    /**
     * The Registrar against whom this assignment exists
     */
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="registrarUKey", nullable = false, updatable = false)
    private Registrar registrar;

    /**
     * The BD division for birth registration
     */
    @OneToOne
    @JoinColumn(name = "birthDivisionUKey", updatable = false)
    private BDDivision birthDivision;
    /**
     * The BD division for death registration
     */
    @OneToOne
    @JoinColumn(name = "deathDivisionUKey", updatable = false)
    private BDDivision deathDivision;
    /**
     * The MR division for marriage registration
     */
    @OneToOne
    @JoinColumn(name = "marriageDivisionUKey", updatable = false)
    private MRDivision marriageDivision;

    /**
     * The type of assignment
     */
    @Enumerated
    @Column(nullable = false)
    private Type type;
    
    /**
     * Date of appointment
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date appointmentDate;
    /**
     * Date of permanency
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date permanentDate;
    /**
     * Date of termination
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date terminationDate;

    public long getAssignmentUKey() {
        return assignmentUKey;
    }

    public void setAssignmentUKey(long assignmentUKey) {
        this.assignmentUKey = assignmentUKey;
    }

    public Registrar getRegistrar() {
        return registrar;
    }

    public void setRegistrar(Registrar registrar) {
        this.registrar = registrar;
    }

    public BDDivision getBirthDivision() {
        return birthDivision;
    }

    public void setBirthDivision(BDDivision birthDivision) {
        this.birthDivision = birthDivision;
    }

    public BDDivision getDeathDivision() {
        return deathDivision;
    }

    public void setDeathDivision(BDDivision deathDivision) {
        this.deathDivision = deathDivision;
    }

    public MRDivision getMarriageDivision() {
        return marriageDivision;
    }

    public void setMarriageDivision(MRDivision marriageDivision) {
        this.marriageDivision = marriageDivision;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Date getPermanentDate() {
        return permanentDate;
    }

    public void setPermanentDate(Date permanentDate) {
        this.permanentDate = permanentDate;
    }

    public Date getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(Date terminationDate) {
        this.terminationDate = terminationDate;
    }

    public BaseLifeCycleInfo getLifeCycleInfo() {
        return lifeCycleInfo;
    }

    public void setLifeCycleInfo(BaseLifeCycleInfo lifeCycleInfo) {
        this.lifeCycleInfo = lifeCycleInfo;
    }
}
