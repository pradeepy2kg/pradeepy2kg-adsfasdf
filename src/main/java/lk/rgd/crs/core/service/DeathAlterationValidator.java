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
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.domain.DeathAlteration;
import lk.rgd.crs.api.domain.DeathRegister;

/**
 * basic back end valiation class for death alterations
 *
 * @author amith jayasekara
 */
public class DeathAlterationValidator {
    private static final Logger logger = LoggerFactory.getLogger(DeathAlterationValidator.class);


    public void validateMinimulCondiations(DeathAlteration deathAlteration) {
        boolean pass = compareObjects(deathAlteration);
        if (!pass) {
            handleException("incomplete death alteration ", ErrorCodes.INCOMPLETE_ALTERATION);
        }
    }

    private boolean compareObjects(DeathAlteration deathAlteration) {

        DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        String dateAlt = null;
        if (deathAlteration != null & deathAlteration.getDeathInfo().getDateOfDeath() != null) {
            dateAlt = df.format(deathAlteration.getDeathInfo().getDateOfDeath());
        }

        if (emptyString(dateAlt)) return true;
        if (emptyString(deathAlteration.getDeathInfo().getTimeOfDeath())) return true;
        if (emptyString(deathAlteration.getDeathInfo().getPlaceOfDeath())) return true;
        if (emptyString(deathAlteration.getDeathInfo().getPlaceOfDeathInEnglish())) return true;
        if (emptyString(deathAlteration.getDeathInfo().getPlaceOfDeathInEnglish())) return true;
        if (emptyString(deathAlteration.getDeathInfo().getCauseOfDeath())) return true;
        if (emptyString(deathAlteration.getDeathInfo().getIcdCodeOfCause())) return true;
        if (emptyString(deathAlteration.getDeathInfo().getPlaceOfBurial())) return true;
        if (emptyString(deathAlteration.getDeathPerson().getDeathPersonPassportNo())) return true;
        if (emptyString(deathAlteration.getDeathPerson().getDeathPersonNameOfficialLang())) return true;
        if (emptyString(deathAlteration.getDeathPerson().getDeathPersonNameInEnglish())) return true;
        if (emptyString(deathAlteration.getDeathPerson().getDeathPersonPermanentAddress())) return true;
        if (emptyString(deathAlteration.getDeathPerson().getDeathPersonFatherPINorNIC())) return true;
        if (emptyString(deathAlteration.getDeathPerson().getDeathPersonFatherFullName())) return true;
        if (emptyString(deathAlteration.getDeathPerson().getDeathPersonMotherPINorNIC())) return true;
        if (emptyString(deathAlteration.getDeathPerson().getDeathPersonMotherFullName())) return true;

        if (deathAlteration.getDeathPerson().getDeathPersonCountry() != null) return true;
        if (deathAlteration.getDeathPerson().getDeathPersonAge() != null) return true;
        if (deathAlteration.getDeathPerson().getDeathPersonRace() != null) return true;

        return false;
    }

    private static void handleException(String msg, int errorCode) {
        logger.error(msg);
        throw new CRSRuntimeException(msg, errorCode);
    }

    private boolean emptyString(Object string) {
        if (string != null) {
            String compare = (String) string;
            if (compare.trim().length() == 0) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
