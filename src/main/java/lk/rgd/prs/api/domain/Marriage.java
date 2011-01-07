package lk.rgd.prs.api.domain;

import lk.rgd.common.util.WebUtils;
import lk.rgd.crs.web.util.MarriageType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author asankha
 */
@Entity
@Table(name = "MARRIAGE", schema = "PRS")
public class Marriage implements Serializable {

    /**
     * Legal state of the marriage
     */
    public enum State {
        MARRIED,
        ANNULLED,
        DIVORCED
    }

    /**
     * The unique Marriage Identification Number
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long marriageUKey;
    /**
     * The preferred language of for the record
     */
    @Column(nullable = false, columnDefinition = "char(2) default 'si'")
    private String preferredLanguage;
    /**
     * Date of marriage
     */
    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfMarriage;
    /**
     * Date of dissolution
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfDissolution;
    /**
     * Place of marriage
     */
    @Column(nullable = false)
    private String placeOfMarriage;
    /**
     * State of the marriage
     */
    @Column(nullable = false)
    private State state;
    /**
     * Type of the marriage
     */
    @Column(nullable = true)
    private MarriageType typeOfMarriage;
    /**
     * The groom
     */
    @OneToOne
    @JoinColumn(name = "groomUKey")
    private Person groom;
    /**
     * The Bride
     */
    @OneToOne
    @JoinColumn(name = "brideUKey")
    private Person bride;

    public long getMarriageUKey() {
        return marriageUKey;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public Date getDateOfMarriage() {
        return dateOfMarriage;
    }

    public void setDateOfMarriage(Date dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
    }

    public Date getDateOfDissolution() {
        return dateOfDissolution;
    }

    public void setDateOfDissolution(Date dateOfDissolution) {
        this.dateOfDissolution = dateOfDissolution;
    }

    public String getPlaceOfMarriage() {
        return placeOfMarriage;
    }

    public void setPlaceOfMarriage(String placeOfMarriage) {
        this.placeOfMarriage = WebUtils.filterBlanksAndToUpper(placeOfMarriage);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public MarriageType getTypeOfMarriage() {
        return typeOfMarriage;
    }

    public void setTypeOfMarriage(MarriageType typeOfMarriage) {
        this.typeOfMarriage = typeOfMarriage;
    }

    public Person getGroom() {
        return groom;
    }

    public void setGroom(Person groom) {
        this.groom = groom;
    }

    public Person getBride() {
        return bride;
    }

    public void setBride(Person bride) {
        this.bride = bride;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Marriage marriage = (Marriage) o;

        if (marriageUKey != marriage.marriageUKey) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (marriageUKey ^ (marriageUKey >>> 32));
    }
}
