package lk.rgd.crs.core.service;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.domain.DeclarantInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * a class to do validations in death declarations
 *
 * @author amith jayasekara
 */

public class DeathDeclarationValidator {

    private static final Logger logger = LoggerFactory.getLogger(DeathDeclarationValidator.class);
    private static final String SERIAL_NUMBER_PATTERN = "20([1-9][0-9])[0|1]([0-9]{5})";

    private final DeathRegisterDAO deathRegisterDAO;

    private static final ResourceBundle rb_si =
        ResourceBundle.getBundle("messages/death_validation_messages", AppConstants.LK_SI);
    private static final ResourceBundle rb_ta =
        ResourceBundle.getBundle("messages/death_validation_messages", AppConstants.LK_TA);
    private static final ResourceBundle rb_en =
        ResourceBundle.getBundle("messages/death_validation_messages", AppConstants.LK_EN);

    public DeathDeclarationValidator(DeathRegisterDAO deathRegisterDAO) {
        this.deathRegisterDAO = deathRegisterDAO;
    }

    /**
     * validate minimul requirement against the filled date in the death declaration form
     *
     * @param deathRegister the death declaration form
     */
    public void validateMinimalRequirements(DeathRegister deathRegister) {
        logger.debug("validating death register for adding");
        boolean primaryCondition = deathRegister.getDeath().getDateOfRegistration() == null ||
            deathRegister.getDeath().getDeathDivision() == null || deathRegister.getDeath().getDateOfDeath() == null ||
            isEmptyString(deathRegister.getDeath().getPlaceOfDeath()) || isEmptyString(deathRegister.getDeclarant().getDeclarantAddress()) ||
            deathRegister.getNotifyingAuthority().getNotifyingAuthoritySignDate() == null ||
            isEmptyString(deathRegister.getNotifyingAuthority().getNotifyingAuthorityName()) ||
            isEmptyString(deathRegister.getNotifyingAuthority().getNotifyingAuthorityAddress()) ||
            isEmptyString(deathRegister.getNotifyingAuthority().getNotifyingAuthorityPIN());
        //validating serial number and duplicate serial numbers
        validateSerialNumber(deathRegister.getDeath().getDeathSerialNo(), deathRegister.getDeath().getDeathDivision());
        //validate declerent
        validateDeathDeclerentForAddingDeathRegistration(deathRegister.getDeclarant());
        if (primaryCondition) {
            if (deathRegister.getIdUKey() > 0) {
                handleException("Death declaration record ID : " + deathRegister.getIdUKey() + " is not complete. " +
                    "Check required field values", ErrorCodes.INVALID_DATA);
            } else if (deathRegister.getDeath().getDeathSerialNo() > 0) {
                //todo needd of this  ????
                handleException("Death declaration record with serial number : " + deathRegister.getDeath().getDeathSerialNo() +
                    " is not complete. Check required field values", ErrorCodes.INVALID_DATA);
            } else {
                handleException("Death declaration record being processed is incomplete " +
                    "Check required field values", ErrorCodes.INVALID_DATA);
            }
        }

    }

    /**
     * validate standeard requrimnets such as
     * inserting same death persons PIN or NIC in two unique entries,
     *
     * @param deathRegisterDAO the DeathRegisterDAO
     * @param deathRegister    the death deaclaration form to validate
     * @param user             the user initiating the validation.
     * @return a list of warnings issued against the death declaration
     */
    public static List<UserWarning> validateStandardRequirements(
        DeathRegisterDAO deathRegisterDAO, DeathRegister deathRegister, User user) {
        // create a holder to capture any warnings
        List<UserWarning> warnings = new ArrayList<UserWarning>();

        // select locale for user
        ResourceBundle rb = rb_en;
        if (AppConstants.SINHALA.equals(user.getPrefLanguage())) {
            rb = rb_si;
        } else if (AppConstants.TAMIL.equals(user.getPrefLanguage())) {
            rb = rb_ta;
        }

        //check is there any duplications in PIN
        String deathPersonPinOrNIC = deathRegister.getDeathPerson().getDeathPersonPINorNIC();
        List deathsWithSamePin = deathRegisterDAO.getDeathRegisterByDeathPersonPINorNIC(deathPersonPinOrNIC);
        if (deathsWithSamePin.size() > 1) {
            UserWarning w = new UserWarning(MessageFormat.format(rb.getString("same_death_person_pin_found_in_previous_recode"), deathPersonPinOrNIC));
            w.setSeverity(UserWarning.Severity.WARN);
            warnings.add(w);
        }

        return warnings;
    }

    private static void handleException(String msg, int errorCode) {
        logger.error(msg);
        throw new CRSRuntimeException(msg, errorCode);
    }

    private static boolean isEmptyString(String s) {
        return s == null || s.trim().length() == 0;
    }

    private void validateDeathDeclerentForAddingDeathRegistration(DeclarantInfo declarantInfo) {
        if (declarantInfo.getDeclarantAddress() == null || declarantInfo.getDeclarantType() == null
            || declarantInfo.getDeclarantFullName() == null) {
            handleException("declerent info on register death , declerent address :" + declarantInfo.getDeclarantAddress() +
                " declerent name :" + declarantInfo.getDeclarantFullName() + " declerent type :" +
                declarantInfo.getDeclarantType(), ErrorCodes.INCOMPLETE_DECLERENT);
        }
    }

    private void validateSerialNumber(long serialNumber, BDDivision bdDivision) {
        // validate registration serial number
        boolean check = false;
        if (serialNumber >= 2010000001L && serialNumber <= 2099199999L) {
            String s = Long.toString(serialNumber);
            if (!s.matches(SERIAL_NUMBER_PATTERN)) {
                check = true;
            }
        } else {
            check = true;
        }
        if (check) {
            handleException("invalid serial number " + serialNumber + "unable to add marriage notice",
                ErrorCodes.INVALID_SERIAL_NUMBER);
        }

        //checking serial number duplication for the division
        DeathRegister existingRecord = deathRegisterDAO.getActiveRecordByBDDivisionAndDeathSerialNo(bdDivision,
            serialNumber);
        if (existingRecord != null) {
            handleException("serial number duplication  " + serialNumber + " for marriage division " +
                bdDivision.getEnDivisionName() + "unable to add marriage notice",
                ErrorCodes.POSSIBLE_DEATH_REGISTER_SERIAL_NUMBER_DUPLICATION);
        }
    }

}
