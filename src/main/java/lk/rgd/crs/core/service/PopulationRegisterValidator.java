package lk.rgd.crs.core.service;

import lk.rgd.AppConstants;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.prs.PRSRuntimeException;
import lk.rgd.prs.api.domain.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * A class to contain validations for PRS records
 *
 * @author Chathuranga Withana
 */
public class PopulationRegisterValidator {

    private static final ResourceBundle rb_si =
        ResourceBundle.getBundle("messages/prs_validation_messages", AppConstants.LK_SI);
    private static final ResourceBundle rb_ta =
        ResourceBundle.getBundle("messages/prs_validation_messages", AppConstants.LK_TA);
    private static final ResourceBundle rb_en =
        ResourceBundle.getBundle("messages/prs_validation_messages", AppConstants.LK_EN);

    private static final Logger logger = LoggerFactory.getLogger(PopulationRegisterValidator.class);

    public PopulationRegisterValidator() {
    }

    /**
     * Validate if the record satisfy the minimum requirements for acceptance and storage
     *
     * @param person the Person to validate
     */
    public void validateMinimalRequirements(Person person) {
        // TODO
    }

    /**
     * Validate typical requirements for a PRS entry
     *
     * @param person the Person to validate
     * @param user   the user initiating the validation
     * @return a list of warning issued against the Person record
     */
    public List<UserWarning> validateStandardRequirements(Person person, User user) {
        List<UserWarning> warnings = new ArrayList<UserWarning>();

        // TODO select locale of the user

        return warnings;
    }

    private void handleException(String message, int errorCode) {
        logger.error(message);
        throw new PRSRuntimeException(message, errorCode);
    }
}
