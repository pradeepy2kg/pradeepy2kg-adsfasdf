package lk.rgd.crs.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.crs.api.domain.BirthAlteration;
import lk.rgd.crs.api.domain.Alteration27;
import lk.rgd.crs.api.domain.Alteration27A;
import lk.rgd.crs.api.domain.Alteration52_1;
import lk.rgd.crs.CRSRuntimeException;

/**
 * @author basic back end validation class for birth alterations
 */
public class BirthAlterationValidator {
    private static final Logger logger = LoggerFactory.getLogger(BirthAlterationValidator.class);

    private static final ResourceBundle rb_si =
            ResourceBundle.getBundle("messages/death_validation_messages", AppConstants.LK_SI);
    private static final ResourceBundle rb_ta =
            ResourceBundle.getBundle("messages/death_validation_messages", AppConstants.LK_TA);
    private static final ResourceBundle rb_en =
            ResourceBundle.getBundle("messages/death_validation_messages", AppConstants.LK_EN);

    public void validateMinimulConditions(BirthAlteration birthAlteration) {
        logger.debug("validater called for birth alteration ");

        int c = 0;
        Alteration27 alt_27 = birthAlteration.getAlt27();
        Alteration27A alt_27_a = birthAlteration.getAlt27A();
        Alteration52_1 alt_52_1 = birthAlteration.getAlt52_1();


    }

    private static void handleException(String msg, int errorCode) {
        logger.error(msg);
        throw new CRSRuntimeException(msg, errorCode);
    }
}
