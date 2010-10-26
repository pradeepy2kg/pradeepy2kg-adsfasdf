package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.domain.DeathAlteration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;

/**
 * basic back end validation class for death alterations
 *
 * @author amith jayasekara
 */
public class DeathAlterationValidator {

    private static final Logger logger = LoggerFactory.getLogger(DeathAlterationValidator.class);

    /**
     * Checks if even one field has been altered
     * @param deathAlteration the death alteration to check for any alterered fields
     */
    public static void validateMinimumConditions(DeathAlteration deathAlteration) {

        DateFormat df = DateTimeUtils.getISO8601Format();
        String dateAlt = null;
        if (deathAlteration != null & deathAlteration.getDeathInfo().getDateOfDeath() != null) {
            dateAlt = df.format(deathAlteration.getDeathInfo().getDateOfDeath());
        }

        if ((dateAlt) != null) {
            return;
        }
        if ((deathAlteration.getDeathInfo().getTimeOfDeath()) != null) {
            return;
        }
        if ((deathAlteration.getDeathInfo().getPlaceOfDeath()) != null) {
            return;
        }
        if ((deathAlteration.getDeathInfo().getPlaceOfDeathInEnglish()) != null) {
            return;
        }
        if ((deathAlteration.getDeathInfo().getPlaceOfDeathInEnglish()) != null) {
            return;
        }
        if ((deathAlteration.getDeathInfo().getCauseOfDeath()) != null) {
            return;
        }
        if ((deathAlteration.getDeathInfo().getIcdCodeOfCause()) != null) {
            return;
        }
        if ((deathAlteration.getDeathInfo().getPlaceOfBurial()) != null) {
            return;
        }
        if ((deathAlteration.getDeathPerson().getDeathPersonPassportNo()) != null) {
            return;
        }
        if ((deathAlteration.getDeathPerson().getDeathPersonNameOfficialLang()) != null) {
            return;
        }
        if ((deathAlteration.getDeathPerson().getDeathPersonNameInEnglish()) != null) {
            return;
        }
        if ((deathAlteration.getDeathPerson().getDeathPersonPermanentAddress()) != null) {
            return;
        }
        if ((deathAlteration.getDeathPerson().getDeathPersonFatherPINorNIC()) != null) {
            return;
        }
        if ((deathAlteration.getDeathPerson().getDeathPersonFatherFullName()) != null) {
            return;
        }
        if ((deathAlteration.getDeathPerson().getDeathPersonMotherPINorNIC()) != null) {
            return;
        }
        if ((deathAlteration.getDeathPerson().getDeathPersonMotherFullName()) != null) {
            return;
        }

        if (deathAlteration.getDeathPerson().getDeathPersonCountry() != null) {
            return;
        }
        if (deathAlteration.getDeathPerson().getDeathPersonAge() != null) {
            return;
        }
        if (deathAlteration.getDeathPerson().getDeathPersonRace() != null) {
            return;
        }

        handleException("Incomplete death alteration - none of the fields have been altered", ErrorCodes.INCOMPLETE_ALTERATION);
    }

    private static void handleException(String msg, int errorCode) {
        logger.error(msg);
        throw new CRSRuntimeException(msg, errorCode);
    }
}
