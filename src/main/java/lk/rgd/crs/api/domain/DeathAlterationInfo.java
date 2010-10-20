package lk.rgd.crs.api.domain;

import lk.rgd.common.util.WebUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * class for death info   for alteration
 * @author amith jayasekara
 */
@Embeddable
public class DeathAlterationInfo implements Serializable {

    @Column(nullable = true, length = 255)
    private String placeOfDeath;

    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfDeath;

    @Column(nullable = true)
    private String timeOfDeath;

    @Column(nullable = true, length = 255)
    private String placeOfDeathInEnglish;

    /**
     * 1-Yes, 0-No
     */
    @Column(nullable = true)
    private boolean causeOfDeathEstablished;

    @Column(nullable = true, length = 600)
    private String causeOfDeath;

    @Column(nullable = true)
    private String icdCodeOfCause;

    @Column(nullable = true)
    private String placeOfBurial;

    public String getPlaceOfDeath() {
        return placeOfDeath;
    }

    public void setPlaceOfDeath(String placeOfDeath) {
        this.placeOfDeath = WebUtils.filterBlanks(placeOfDeath);
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
        this.timeOfDeath = WebUtils.filterBlanks(timeOfDeath);
    }

    public String getPlaceOfDeathInEnglish() {
        return placeOfDeathInEnglish;
    }

    public void setPlaceOfDeathInEnglish(String placeOfDeathInEnglish) {
        this.placeOfDeathInEnglish = WebUtils.filterBlanks(placeOfDeathInEnglish);
    }

    public boolean isCauseOfDeathEstablished() {
        return causeOfDeathEstablished;
    }

    public void setCauseOfDeathEstablished(boolean causeOfDeathEstablished) {
        this.causeOfDeathEstablished = causeOfDeathEstablished;
    }

    public String getIcdCodeOfCause() {
        return icdCodeOfCause;
    }

    public void setIcdCodeOfCause(String icdCodeOfCause) {
        this.icdCodeOfCause = WebUtils.filterBlanks(icdCodeOfCause);
    }

    public String getPlaceOfBurial() {
        return placeOfBurial;
    }

    public void setPlaceOfBurial(String placeOfBurial) {
        this.placeOfBurial = WebUtils.filterBlanks(placeOfBurial);
    }

    public String getCauseOfDeath() {
        return causeOfDeath;
    }

    public void setCauseOfDeath(String causeOfDeath) {
        this.causeOfDeath = WebUtils.filterBlanks(causeOfDeath);
    }
}
