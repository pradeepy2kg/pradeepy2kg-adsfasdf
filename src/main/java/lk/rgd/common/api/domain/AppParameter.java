package lk.rgd.common.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents an application parameter used to configure or tune the system - stored as key-value pairs
 * e.g. The number of days between the birth and submission of the BDF (i.e. 90 days)
 *
 * @author asankha
 */
@Entity
@Table(name = "APP_PARAMETERS", schema = "COMMON")
public class AppParameter implements Serializable {

    /**
     * The parameter key that holds the number of days for parental confirmation - before auto confirmation
     */
    public static final String CRS_AUTO_CONFIRMATION_DAYS = "crs.birth.auto_confirmation_days";
    /**
     * The parameter key that holds the number of days printed for user confirmation (e.g. 14 days)
     */
    public static final String CRS_BIRTH_CONFIRMATION_DAYS_PRINTED = "crs.birth.confirmation_days_printed";
    /**
     * The parameter key that holds the number of days for a birth to be considered as a late registration
     */
    public static final String CRS_BIRTH_LATE_REG_DAYS = "crs.birth.late_reg_days";
    /**
     * The parameter key that holds the number of days for a birth to be considered as a belated registration
     */
    public static final String CRS_BELATED_MAX_DAYS = "crs.birth.belated_reg_days";
    /**
     * The number of records returned for a standard BC search
     */
    public static final String CRS_CERTIFICATE_SEARCH_LIMIT = "crs.certificate.search.record.limit";
    /**
     * The parameter key that holds the number of days until a password expires
     */
    public static final String PASSWORD_EXPIRY_DAYS = "rgd.password_expiry_days";
    /**
     * The parameter key that holds the max number of log attempts
     */
    public static final String MAX_NUMBER_OF_LOGIN_ATTEMPTS = "rgd.max.number.of.login.attempts";
    /**
     * Number of days for expiring marriage notice
     */
    public static final String MARRIAGE_NOTICE_EXPIRE_DATE = "crs.notice.expire.days";
    /**
     * The parameter key that holds the number of days after birth who fall in to infant category
     */
    public static final String CRS_DEATH_INFANT_DAYS = "crs.death.infant.days";

    @Id
    @Column(length = 100)
    private String name;
    @Column(nullable = false, length = 100)
    private String value;

    public AppParameter() {
    }

    public AppParameter(String name, String value) {
        this.name = name;

        this.value = value;
    }

    public AppParameter(String name, int value) {
        this.name = name;
        this.value = Integer.toString(value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
