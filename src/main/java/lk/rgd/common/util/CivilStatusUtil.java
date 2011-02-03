package lk.rgd.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.AppConstants;
import lk.rgd.prs.api.domain.Person;

import java.util.Map;
import java.util.HashMap;

/**
 * utility class to help UI to display  localized labels for civil status.
 */
public class CivilStatusUtil {

    private static final Logger logger = LoggerFactory.getLogger(CivilStatusUtil.class);

    public static String getStatusAsString(Integer s) {
        if (s == null) {
            return "U"; // unknown
        }

        switch (s) {
            case 0:
                return "N";
            case 1:
                return "M";
            case 2:
                return "A";
            case 3:
                return "S";
            case 4:
                return "D";
            case 5:
                return "W";
        }
        throw new IllegalArgumentException("Illegal civil status : " + s);
    }

    public static String getStatusAsString(Person.CivilStatus s) {
        if (s == null) {
            return "U"; // unknown
        }

        switch (s) {
            case NEVER_MARRIED:
                return "N";
            case MARRIED:
                return "M";
            case ANNULLED:
                return "A";
            case SEPARATED:
                return "S";
            case DIVORCED:
                return "D";
            case WIDOWED:
                return "W";
        }
        throw new IllegalArgumentException("Illegal civil status : " + s);
    }

    /**
     * Return civil status string in selected language
     *
     * @param state    the civil status enumeration
     * @param language selected language
     * @return the married status string for selected language and enumeration
     * @see lk.rgd.prs.api.domain.Person.CivilStatus
     */
    public static String getCivilStatus(Person.CivilStatus state, String language) {

        if (AppConstants.SINHALA.equals(language)) {
            switch (state) {
                case NEVER_MARRIED:
                    return "අවිවාහක";
                case MARRIED:
                    return "විවාහක";
                case ANNULLED:
                    return "නිෂ්ප්‍රභාකර ඇත";
                case SEPARATED:
                    return "වෙන්වී ඇත ";
                case DIVORCED:
                    return "දික්කසාද වී ඇත ";
                case WIDOWED:
                    return "වැන්දඹු";
            }
        } else if (AppConstants.TAMIL.equals(language)) {
            switch (state) {
                case NEVER_MARRIED:
                    return "திருமணமாகாதவர்";
                case MARRIED:
                    return "திருமணமாணவர் ";
                case ANNULLED:
                    return "தள்ளிவைத்தல்";
                case SEPARATED:
                    return "பிரிந்திருத்தல்";
                case DIVORCED:
                    return "திருமணம் தள்ளுபடி செய்தவர்";
                case WIDOWED:
                    return "விதவை";
            }
        } else if (AppConstants.ENGLISH.equals(language)) {
            switch (state) {
                case NEVER_MARRIED:
                    return "NEVER MARRIED";
                case MARRIED:
                    return "MARRIED";
                case ANNULLED:
                    return "ANNULLED";
                case SEPARATED:
                    return "SEPARATED";
                case DIVORCED:
                    return "DIVORCED";
                case WIDOWED:
                    return "WIDOWED";
            }
        } else {
            logger.error("Invalid language : {}", language);
            throw new IllegalArgumentException("Invalid language : " + language);
        }
        logger.error("Invalid civil status : {}", state);
        throw new IllegalArgumentException("Invalid civil status : " + state);
    }

    public static String getCivilStatusInAllLanguages(Person.CivilStatus state) {
        return getCivilStatus(state, AppConstants.SINHALA) + " / " + getCivilStatus(state, AppConstants.TAMIL) + " / " + getCivilStatus(state, AppConstants.ENGLISH);
    }

    /**
     * Populate civil status (Except MARRIED status) list with its values in all languages
     */
    public static Map<Person.CivilStatus, String> populateCivilStatus() {
        Map<Person.CivilStatus, String> civilStatus = new HashMap<Person.CivilStatus, String>();
        civilStatus.put(Person.CivilStatus.NEVER_MARRIED, CivilStatusUtil.getCivilStatusInAllLanguages(Person.CivilStatus.NEVER_MARRIED));
        civilStatus.put(Person.CivilStatus.DIVORCED, CivilStatusUtil.getCivilStatusInAllLanguages(Person.CivilStatus.DIVORCED));
        civilStatus.put(Person.CivilStatus.WIDOWED, CivilStatusUtil.getCivilStatusInAllLanguages(Person.CivilStatus.WIDOWED));
        civilStatus.put(Person.CivilStatus.ANNULLED, CivilStatusUtil.getCivilStatusInAllLanguages(Person.CivilStatus.ANNULLED));
        return civilStatus;
    }
}
