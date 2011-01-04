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

    /**
     * this method validate basic requirements of death alteration such as
     * both objects can't be null and declarent information must be filled.
     * note :only nullble false field in declarent is type . .
     */
    public static void validateMinimumConditions(DeathAlteration deathAlteration) {

        logger.debug("validating death alteration for death register id  :{}", deathAlteration.getDeathRegisterIDUkey());
        boolean pass = (deathAlteration.getDeathInfo() != null | deathAlteration.getDeathPerson() != null) &&
                (deathAlteration.getDeclarant().getDeclarantType() != null);
        if (!pass) {
            handleException("validation failed ,Can't add death alteration ", ErrorCodes.INCOMPLETE_ALTERATION);
        }
    }

    private static void handleException(String msg, int errorCode) {
        logger.error(msg);
        throw new CRSRuntimeException(msg, errorCode);
    }
}
