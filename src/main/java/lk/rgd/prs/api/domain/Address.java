package lk.rgd.prs.api.domain;

import java.util.Date;

/**
 * @author asankha
 */
public class Address {

    /**
     * Start date at this address
     */
    private Date startDate;
    /**
     * End date at this address 
     */
    private Date endDate;
    /**
     * First line
     */
    private String line1;
    /**
     * Second line
     */
    private String line2;
    /**
     * City
     */
    private String city;
    /**
     * Postal code
     */
    private String postcode;
    /**
     * Country Code - null means in Sri Lanka
     */
    private String country;
}
