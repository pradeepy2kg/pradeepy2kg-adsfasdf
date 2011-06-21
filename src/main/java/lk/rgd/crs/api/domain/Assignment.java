package lk.rgd.crs.api.domain;

import lk.rgd.common.api.domain.BaseLifeCycleInfo;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Represents an assignment as a Birth, Death or Marriage Registrar
 *
 * @author asankha
 */
@Entity
@Table(name = "ASSIGNMENT", schema = "CRS")
@NamedQueries({
    // Do NOT try to optimize the below three queries into one
    @NamedQuery(name = "get.assignments.by.state.and.dsdivision", query = "SELECT a FROM Assignment a " +
        " WHERE (a.lifeCycleInfo.active = :active) " +
        " AND (((a.birthDivision is not null) and (a.birthDivision.dsDivision.dsDivisionUKey = :dsDivisionUKey)) " +
        "   OR ((a.deathDivision is not null) and (a.deathDivision.dsDivision.dsDivisionUKey = :dsDivisionUKey)) " +
        "   OR ((a.marriageDivision is not null) and (a.marriageDivision.dsDivision.dsDivisionUKey = :dsDivisionUKey))) "),

    @NamedQuery(name = "get.birth.assignments.by.state.type.and.dsdivision", query = "SELECT a FROM Assignment a " +
        " WHERE a.lifeCycleInfo.active = :active AND a.type = :type " +
        " AND a.birthDivision.dsDivision.dsDivisionUKey = :dsDivisionUKey"),
    @NamedQuery(name = "get.death.assignments.by.state.type.and.dsdivision", query = "SELECT a FROM Assignment a " +
        " WHERE a.lifeCycleInfo.active = :active AND a.type = :type " +
        " AND a.deathDivision.dsDivision.dsDivisionUKey = :dsDivisionUKey"),
    @NamedQuery(name = "get.marriage.assignments.by.state.type.and.dsdivision", query = "SELECT a FROM Assignment a " +
        " WHERE a.lifeCycleInfo.active = :active AND a.type = :type " +
        " AND a.marriageDivision.dsDivision.dsDivisionUKey = :dsDivisionUKey"),
    @NamedQuery(name = "get.by.registrarUKey", query = "SELECT a FROM Assignment a " +
        "WHERE a.registrar.registrarUKey = :registrarUKey"),
    @NamedQuery(name = "get.assignments.by.type.and.division", query = "SELECT a FROM Assignment a " +
        "WHERE a.type = :type AND " +
        "((a.birthDivision IS NOT NULL AND a.birthDivision.bdDivisionUKey = :divisionUKey) OR " +
        "(a.deathDivision IS NOT NULL AND a.deathDivision.bdDivisionUKey = :divisionUKey) OR" +
        "(a.marriageDivision IS NOT NULL AND a.marriageDivision.mrDivisionUKey = :divisionUKey))" +
        "AND a.lifeCycleInfo.active = :active AND a.registrar.acting = :acting"),
    @NamedQuery(name = "get.all.assignments", query = "SELECT a FROM Assignment a WHERE a.lifeCycleInfo.active =:active")
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Assignment implements Serializable {

    /**
     * The types of assignments
     */
    public enum Type {
        BIRTH, DEATH, GENERAL_MARRIAGE, KANDYAN_MARRIAGE, MUSLIM_MARRIAGE
    }

    public enum State {
        INACTIVE, ACTIVE, DELETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long assignmentUKey;

    @Embedded
    private BaseLifeCycleInfo lifeCycleInfo = new BaseLifeCycleInfo();

    /**
     * The Registrar against whom this assignment exists
     */
    @ManyToOne
    @JoinColumn(name = "registrarUKey", nullable = false, updatable = false)
    private Registrar registrar;

    /**
     * The BD division for birth registration
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "birthDivisionUKey", updatable = false)
    private BDDivision birthDivision;
    /**
     * The BD division for death registration
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "deathDivisionUKey", updatable = false)
    private BDDivision deathDivision;
    /**
     * The MR division for marriage registration
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "marriageDivisionUKey", updatable = false)
    private MRDivision marriageDivision;

    /**
     * The type of assignment
     */
    @Enumerated
    @Column(nullable = false)
    private Type type;

    /**
     * The state of the assignment
     */
    @Enumerated
    @Column(nullable = true)
    private State state;

    /**
     * Is this an Additional Registrar assignment ?
     */
    @Column(nullable = false)
    private boolean additional;

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isAdditional() {
        return additional;
    }

    public void setAdditional(boolean additional) {
        this.additional = additional;
    }
}
