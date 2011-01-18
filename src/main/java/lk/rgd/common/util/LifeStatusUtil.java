package lk.rgd.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.AppConstants;

/**
 * Created by IntelliJ IDEA.
 * User: widu
 * Date: Oct 27, 2010
 * Time: 5:18:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class LifeStatusUtil {
    private static final Logger logger = LoggerFactory.getLogger(LifeStatusUtil.class);

    public static String getStatusAsString(Integer s) {
        if (s == null) {
            return "U";
        }

        switch (s) {
            case 0:
                return "U";
            case 1:
                return "A";
            case 2:
                return "D";
            case 3:
                return "M";
            case 4:
                return "N";
        }
        throw new IllegalArgumentException("Illegal civil status : " + s);
    }

    public static String getStatusAsString(Person.LifeStatus s) {
        if (s == null) {
            return "U";
        }

        switch (s) {
            case UNKNOWN:
                return "U";
            case ALIVE:
                return "A";
            case DEAD:
                return "D";
            case MISSING:
                return "M";
            case NON_RESIDENT:
                return "N";
        }
        throw new IllegalArgumentException("Illegal civil status : " + s);
    }

    /**
     * Return life status string in selected language
     *
     * @param state    the life status enumeration
     * @param language selected language
     * @return the life status string for selected language and enumeration
     * @see lk.rgd.prs.api.domain.Person.LifeStatus
     */
    public static String getLifeStatus(Person.LifeStatus state, String language) {

        if (AppConstants.SINHALA.equals(language)) {
            switch (state) {
                case UNKNOWN:
                    return "නොදනී ";
                case ALIVE:
                    return "ජීවතුන් අතර ඇත ";
                case DEAD:
                    return "ජීවතුන් අතර නැත  ";
                case MISSING:
                    return "අතුරුදන් ";
                case NON_RESIDENT:
                    return "පදිංචිව නැත ";
            }
        } else if (AppConstants.TAMIL.equals(language)) {
            switch (state) {
                case UNKNOWN:
                    return "Unknown";
                case ALIVE:
                    return "Alive";
                case DEAD:
                    return "Dead";
                case MISSING:
                    return "Missing";
                case NON_RESIDENT:
                    return "Non resident";
            }
        } else if (AppConstants.ENGLISH.equals(language)) {
            switch (state) {
                case UNKNOWN:
                    return "Unknown";
                case ALIVE:
                    return "Alive";
                case DEAD:
                    return "Dead";
                case MISSING:
                    return "Missing";
                case NON_RESIDENT:
                    return "Non resident";
            }
        } else {
            logger.error("Invalid language : {}", language);
            throw new IllegalArgumentException("Invalid language : " + language);
        }
        logger.error("Invalid life status : {}", state);
        throw new IllegalArgumentException("Invalid life status : " + state);
    }
}
