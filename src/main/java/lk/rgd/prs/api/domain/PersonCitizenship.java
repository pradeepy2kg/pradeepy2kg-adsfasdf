package lk.rgd.prs.api.domain;

import lk.rgd.common.api.domain.BaseLifeCycleInfo;
import lk.rgd.common.api.domain.Country;
import lk.rgd.common.util.WebUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a Person citizenship to a Country
 *
 * @author Chathuranga Withana
 */
@Entity
@Table(schema = "PRS", name = "PERSON_CITIZENSHIP")
@IdClass(PersonCitizenshipID.class)
@NamedQueries({
    @NamedQuery(name = "get.citizenship.by.personId", query = "SELECT pc FROM PersonCitizenship pc " +
        "WHERE pc.personUKey = :personUKey ORDER BY pc.country.enCountryName ASC"),
    @NamedQuery(name = "citizenship.by.idukey", query = "SELECT pc FROM PersonCitizenship pc " +
        "WHERE pc.personUKey = :idUKey")
})
public class PersonCitizenship implements Serializable {
    @Id
    private long personUKey;
    @Id
    private int countryId;
    @Id
    @Column(length = 15)
    private String passportNo;

    @ManyToOne
    @JoinColumn(name = "personUKey", insertable = false, updatable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "countryId", insertable = false, updatable = false)
    private Country country;

    @Embedded
    private BaseLifeCycleInfo lifeCycleInfo = new BaseLifeCycleInfo();

    public long getPersonUKey() {
        if (person != null) {
            return person.getPersonUKey();
        }
        return personUKey;
    }

    public void setPersonUKey(long personUKey) {
        this.personUKey = personUKey;
    }

    public int getCountryId() {
        if (country != null) {
            return country.getCountryId();
        }
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
        this.personUKey = person.getPersonUKey();
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
        this.countryId = country.getCountryId();
    }

    public BaseLifeCycleInfo getLifeCycleInfo() {
        return lifeCycleInfo;
    }

    public void setLifeCycleInfo(BaseLifeCycleInfo lifeCycleInfo) {
        this.lifeCycleInfo = lifeCycleInfo;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = WebUtils.filterBlanksAndToUpper(passportNo);
    }
}
