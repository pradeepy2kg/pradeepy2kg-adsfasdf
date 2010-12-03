package lk.rgd.prs.api.domain;

import lk.rgd.common.api.domain.Country;
import lk.rgd.common.util.WebUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author asankha
 */
@Entity
@Table(name = "ADDRESS", schema = "PRS")
@NamedQueries({
    @NamedQuery(name = "addresses.by.idukey", query = "SELECT a FROM Address a WHERE a.person.personUKey = :idUKey")
})
public class Address implements Serializable {

    /**
     * A unique id for the address entry
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long addressUKey;
    /**
     * The person to whom this address belongs (Note: An address belongs to only one person)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personUKey", nullable = false)
    private Person person;
    /**
     * Start date at this address
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date startDate;
    /**
     * End date at this address
     */
    @Column(nullable = true)
    @Temporal(value = TemporalType.DATE)
    private Date endDate;
    /**
     * First line
     */
    @Column(length = 255, nullable = false)
    private String line1;
    /**
     * Second line
     */
    @Column(length = 255, nullable = true)
    private String line2;
    /**
     * City
     */
    @Column(length = 60, nullable = true)
    private String city;
    /**
     * Postal code
     */
    @Column(length = 10, nullable = true)
    private String postcode;
    /**
     * Permanent address - if the address is permanent address then true else false
     */
    @Column(nullable = true, columnDefinition = "smallint not null default 0")
    private boolean permanent = false;
    /**
     * Country - null means Sri Lanka
     */
    @OneToOne(optional = true)
    @JoinColumn(name = "countryId")
    private Country country;

    public Address() {
    }

    public Address(String line1) {
        this.line1 = WebUtils.filterBlanksAndToUpper(line1);
    }

    public long getAddressUKey() {
        return addressUKey;
    }

    public void setAddressUKey(long addressUKey) {
        this.addressUKey = addressUKey;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = WebUtils.filterBlanksAndToUpper(line1);
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = WebUtils.filterBlanksAndToUpper(line2);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = WebUtils.filterBlanksAndToUpper(city);
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = WebUtils.filterBlanksAndToUpper(postcode);
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        if ((line1 != null) && (line1.trim().length() > 0)) {
            buffer.append(line1);
        }
        if ((line2 != null) && (line2.trim().length() > 0)) {
            buffer.append(", ");
            buffer.append(line2);
        }

        if ((city != null) && (city.trim().length() > 0)) {
            buffer.append(", ");
            buffer.append(city);
        }

        if ((postcode != null) && (postcode.trim().length() > 0)) {
            buffer.append(", ");
            buffer.append(postcode);
        }

        return buffer.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Address address = (Address) o;

        if (addressUKey != address.addressUKey) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (addressUKey ^ (addressUKey >>> 32));
    }
}
