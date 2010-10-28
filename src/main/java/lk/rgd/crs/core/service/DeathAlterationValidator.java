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
        
    }
/*

    */
/**
     * placeOfDeath ,dateOfDeath ,placeOfBurial are cannot be null .so if Death Alteration object change them to null
     * it may be affect if arg approves those changes so it is better to validate them before add alteration.
     *//*


    private static boolean checkObject(DeathAlteration deathAlteration) {
        DateFormat df = DateTimeUtils.getISO8601Format();
        String dateAlt = null;
        if (deathAlteration != null & deathAlteration.getDeathInfo().getDateOfDeath() != null) {
            dateAlt = df.format(deathAlteration.getDeathInfo().getDateOfDeath());
        }
        if ((dateAlt) != null) return true;
        if ((deathAlteration.getDeathInfo().getPlaceOfDeath()) != null) return true;
        if ((deathAlteration.getDeathInfo().getPlaceOfBurial()) != null) return true;
        return false;
    }
*/

    private static void handleException(String msg, int errorCode) {
        logger.error(msg);
        throw new CRSRuntimeException(msg, errorCode);
    }
}
