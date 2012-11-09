package lk.rgd.prs.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Maintains the last PIN number issued for a specific date of birth
 * As a lastSerial number issued for a dateOfBirth (year + dayOfYear)
 *
 * @author asankha
 */
@Entity
@Table(name = "PIN_NUMBERS", schema = "PRS")
public class PINNumber {

    @Id
    /**
     * Date of birth as  N - Century | NN - Year of birth |  NNN - Birth 'day of the year'.
     * Where Century is
     *  0 for births in 20th century ( January 1st, 1901 TO December 31st, 2000)
     *  1 for births in 21st century ( January 1st, 2001 TO December 31st, 2100)
     */
    private long dateOfBirth;
    @Column(nullable = false)
    private long lastSerial;

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public long getLastSerial() {
        return lastSerial;
    }

    public void setLastSerial(long lastSerial) {
        this.lastSerial = lastSerial;
    }
}
