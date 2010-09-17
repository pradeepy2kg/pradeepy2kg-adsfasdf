package lk.rgd.prs.api.domain;

import lk.rgd.common.api.domain.Country;

import javax.persistence.*;
import java.util.Date;

/**
 * @author asankha
 */
@Entity
@Table(name = "ADDRESS", schema = "PRS")
public class Address {

    /**
     * A unique id for the address entry
     */
    @Id
    private long addressUKey;
    /**
     * The person to whom this address belongs (Note: An address belongs to only one person)
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="personUKey", nullable = false)
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
     * Country - null means Sri Lanka
     */
    @OneToOne(optional = true)
    @JoinColumn(name = "countryId")
    private Country country;

    public Address(String line1) {
        this.line1 = line1;
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
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
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
        if ((line1 != null) && (!"".equals(line1))) {
            buffer.append(line1);
            buffer.append(", " );
        }
        if ((line2 != null) && (!"".equals(line2))) {
            buffer.append(line2);
            buffer.append(", " );
        }

        if ((city != null) && (!"".equals(city))) {
            buffer.append(city);
            // in the very likely case of postcode being absent,, we don't want to look silly by
            // printing a comma in the end.
            if ((postcode != null) && (!"".equals(postcode))) {
                buffer.append(", " );
            }
        }

        if ((postcode != null) && (!"".equals(postcode))) {
            buffer.append(postcode);
        }

        return buffer.toString();
    }
}
