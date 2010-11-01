package lk.rgd.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.AppConstants;
import lk.rgd.prs.api.domain.Person;

/**
 * utility class to help UI to display  localized labels for civil status.
 */
public class CivilStatusUtil {
    private static final Logger logger = LoggerFactory.getLogger(CivilStatusUtil.class);

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
                    case ANNULLED:
                        return "නිෂ්ප්‍රභාකර ඇත";
                    case SEPARATED:
                        return "වෙන්වී ඇත ";
                    case DIVORCED:
                        return "දික්කසාද වී ඇත ";
                    case WIDOWED:
                        return "වැන්දබු";
                }
            } else if (AppConstants.TAMIL.equals(language)) {
                switch (state) {
                    case NEVER_MARRIED:
                        return "திருமணமாகாதவர்";
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
                        return "Never married";
                    case ANNULLED:
                        return "Annulled";
                    case SEPARATED:
                        return "Separated";
                    case DIVORCED:
                        return "Devoced";
                    case WIDOWED:
                        return "Widowed";
                }
            } else {
                logger.error("Invalid language : {}", language);
                throw new IllegalArgumentException("Invalid language : " + language);
            }
            logger.error("Invalid civil status : {}", state);
            throw new IllegalArgumentException("Invalid civil status : " + state);
        }
}
