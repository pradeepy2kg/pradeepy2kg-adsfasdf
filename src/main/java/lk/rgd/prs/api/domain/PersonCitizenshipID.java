package lk.rgd.prs.api.domain;

import java.io.Serializable;

/**
 * Represents the primary key class of PersonCitizenship
 *
 * @author Chathuranga Withana
 */
public class PersonCitizenshipID implements Serializable {
    private long personUKey;
    private int countryId;

    public PersonCitizenshipID() {
    }

    public PersonCitizenshipID(long personUKey, int countryId) {
        this.personUKey = personUKey;
        this.countryId = countryId;
    }

    public long getPersonUKey() {
        return personUKey;
    }

    public void setPersonUKey(long personUKey) {
        this.personUKey = personUKey;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}