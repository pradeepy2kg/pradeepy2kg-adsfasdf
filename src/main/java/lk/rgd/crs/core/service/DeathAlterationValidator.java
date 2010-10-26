package lk.rgd.crs.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.api.domain.Country;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.domain.DeathAlteration;
import lk.rgd.crs.api.domain.DeathRegister;

/**
 * basic back end validation class for death alterations
 *
 * @author amith jayasekara
 */
public class DeathAlterationValidator {
    private static final Logger logger = LoggerFactory.getLogger(DeathAlterationValidator.class);


    public static void validateMinimumConditions(DeathAlteration deathAlteration) {
        boolean pass = checkObject(deathAlteration);
        if (!pass) {
            handleException("incomplete death alteration ", ErrorCodes.INCOMPLETE_ALTERATION);
        }
    }

    private static boolean checkObject(DeathAlteration deathAlteration) {

        DateFormat df = DateTimeUtils.getISO8601Format();
        String dateAlt = null;
        if (deathAlteration != null & deathAlteration.getDeathInfo().getDateOfDeath() != null) {
            dateAlt = df.format(deathAlteration.getDeathInfo().getDateOfDeath());
        }

        if ((dateAlt) != null) return true;
        if ((deathAlteration.getDeathInfo().getTimeOfDeath()) != null) return true;
        if ((deathAlteration.getDeathInfo().getPlaceOfDeath()) != null) return true;
        if ((deathAlteration.getDeathInfo().getPlaceOfDeathInEnglish()) != null) return true;
        if ((deathAlteration.getDeathInfo().getPlaceOfDeathInEnglish()) != null) return true;
        if ((deathAlteration.getDeathInfo().getCauseOfDeath()) != null) return true;
        if ((deathAlteration.getDeathInfo().getIcdCodeOfCause()) != null) return true;
        if ((deathAlteration.getDeathInfo().getPlaceOfBurial()) != null) return true;
        if ((deathAlteration.getDeathPerson().getDeathPersonPassportNo()) != null) return true;
        if ((deathAlteration.getDeathPerson().getDeathPersonNameOfficialLang()) != null) return true;
        if ((deathAlteration.getDeathPerson().getDeathPersonNameInEnglish()) != null) return true;
        if ((deathAlteration.getDeathPerson().getDeathPersonPermanentAddress()) != null) return true;
        if ((deathAlteration.getDeathPerson().getDeathPersonFatherPINorNIC()) != null) return true;
        if ((deathAlteration.getDeathPerson().getDeathPersonFatherFullName()) != null) return true;
        if ((deathAlteration.getDeathPerson().getDeathPersonMotherPINorNIC()) != null) return true;
        if ((deathAlteration.getDeathPerson().getDeathPersonMotherFullName()) != null) return true;

        if (deathAlteration.getDeathPerson().getDeathPersonCountry() != null) return true;
        if (deathAlteration.getDeathPerson().getDeathPersonAge() != null) return true;
        if (deathAlteration.getDeathPerson().getDeathPersonRace() != null) return true;

        return false;
    }

    private static void handleException(String msg, int errorCode) {
        logger.error(msg);
        throw new CRSRuntimeException(msg, errorCode);
    }
}
