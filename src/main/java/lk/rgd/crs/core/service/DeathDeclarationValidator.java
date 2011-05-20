package lk.rgd.crs.core.service;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.*;

import static lk.rgd.crs.api.domain.BirthDeclaration.State.*;

/**
 * a class to do validations in death declarations
 *
 * @author amith jayasekara
 */

public class DeathDeclarationValidator {

    private static final Logger logger = LoggerFactory.getLogger(DeathDeclarationValidator.class);
    private static final String SERIAL_NUMBER_PATTERN = "20([1-9][0-9])[0|1]([0-9]{5})";

    private final DeathRegisterDAO deathRegisterDAO;
    private final BirthDeclarationDAO birthDeclarationDAO;
    private static PopulationRegistry populationRegistry;

    private static final ResourceBundle rb_si =
        ResourceBundle.getBundle("messages/death_validation_messages", AppConstants.LK_SI);
    private static final ResourceBundle rb_ta =
        ResourceBundle.getBundle("messages/death_validation_messages", AppConstants.LK_TA);
    private static final ResourceBundle rb_en =
        ResourceBundle.getBundle("messages/death_validation_messages", AppConstants.LK_EN);

    public DeathDeclarationValidator(DeathRegisterDAO deathRegisterDAO, PopulationRegistry populationRegistry,
        BirthDeclarationDAO birthDeclarationDAO) {
        this.deathRegisterDAO = deathRegisterDAO;
        this.populationRegistry = populationRegistry;
        this.birthDeclarationDAO = birthDeclarationDAO;
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
                //todo need of this  ????
                handleException("Death declaration record with serial number : " + deathRegister.getDeath().getDeathSerialNo() +
                    " is not complete. Check required field values", ErrorCodes.INVALID_DATA);
            } else {
                handleException("Death declaration record being processed is incomplete " +
                    "Check required field values", ErrorCodes.INVALID_DATA);
            }
        }

    }

    /**
     * validate standard requirements such as
     * inserting same death persons PIN or NIC in two unique entries,
     *
     * @param deathRegisterDAO the DeathRegisterDAO
     * @param deathRegister    the death declaration form to validate
     * @param user             the user initiating the validation.
     * @return a list of warnings issued against the death declaration
     */
    public List<UserWarning> validateStandardRequirements(
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
        List<DeathRegister> deathsWithSamePin = deathRegisterDAO.getDeathRegisterByDeathPersonPINorNIC(deathPersonPinOrNIC);
        List<DeathRegister> deList = new LinkedList<DeathRegister>();
        for (DeathRegister dr : deathsWithSamePin) {
            if (dr.getStatus() == DeathRegister.State.APPROVED) {
                deList.add(dr);
            }
        }
        if (deList.size() > 0) {
            UserWarning w = new UserWarning(MessageFormat.format(
                rb.getString("same_death_person_pin_found_in_previous_recode"), deathPersonPinOrNIC));
            w.setSeverity(UserWarning.Severity.WARN);
            warnings.add(w);
        }
        //check if the father nic or mother nic or death person nic is same
        //check mother and father pin nic same
        String fatherPINorNIC = deathRegister.getDeathPerson().getDeathPersonFatherPINorNIC();
        String motherPINorNIC = deathRegister.getDeathPerson().getDeathPersonMotherPINorNIC();
        String deathPersonPINorNIC = deathRegister.getDeathPerson().getDeathPersonPINorNIC();
        String notifyAuthority = deathRegister.getNotifyingAuthority().getNotifyingAuthorityPIN();
        if (deathPersonPINorNIC != null) {
            if (deathPersonPINorNIC.equals(fatherPINorNIC)) {
                warnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.deathPerson.nic.equals.father.nic"),
                    deathPersonPINorNIC, fatherPINorNIC), UserWarning.Severity.WARN));
            }
            if (deathPersonPINorNIC.equals(motherPINorNIC)) {
                warnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.deathPerson.nic.equals.mother.nic"),
                    deathPersonPINorNIC, motherPINorNIC), UserWarning.Severity.WARN));
            }
            if (deathPersonPINorNIC.equals(notifyAuthority)) {
                warnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.deathPerson.nic.equals.notify.nic"),
                    deathPersonPINorNIC, notifyAuthority), UserWarning.Severity.WARN));
            }
        }
        if (fatherPINorNIC != null && fatherPINorNIC.equals(motherPINorNIC)) {
            warnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.father.nic.equals.mother.nic"),
                fatherPINorNIC)));
        }

        //check PRS record of this person                      
        if (deathRegister.getDeathPerson().getDeathPersonPINorNIC() != null) {
            Person deadPerson = populationRegistry.
                findUniquePersonByPINorNIC(deathRegister.getDeathPerson().getDeathPersonPINorNIC(), user);
            if (deadPerson != null && deadPerson.getDateOfBirth() != null) {
                //check person date of birth
                if (deadPerson.getDateOfBirth().getTime() > deathRegister.getDeath().getDateOfDeath().getTime()) {
                    //now issue warnings
                    warnings.add(new UserWarning(MessageFormat.format(rb.getString("date_of_birth_less_than_date_of_dead"),
                        deathRegister.getDeath().getDateOfDeath(), deadPerson.getDateOfBirth()), UserWarning.Severity.WARN));
                }
            }
        }
        // TODO validate given data using PIN

        // checks whether the death person age is 30 days of less
        if (deathRegister.getDeath().isInfantLessThan30Days()) {
            // see whether birth is already registered if not give warnings
            checkInfantWarnings(deathRegister, warnings, rb);
        }

        return warnings;
    }

    private void checkInfantWarnings(DeathRegister dr, List<UserWarning> warnings, ResourceBundle rb) {
        DeathPersonInfo dp = dr.getDeathPerson();
        if (dp != null && (dp.getDeathPersonDOB() != null || dp.getDeathPersonAgeDate() != null) &&
            dp.getDeathPersonMotherPINorNIC() != null) {

            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();

            if (dp.getDeathPersonDOB() != null) {
                start.setTime(dp.getDeathPersonDOB());
                end.setTime(dp.getDeathPersonDOB());
            } else {
                cal.setTime(dr.getDeath().getDateOfDeath());
                cal.add(Calendar.DATE, -dp.getDeathPersonAgeDate());

                start.setTime(cal.getTime());
                end.setTime(cal.getTime());
            }
            start.add(Calendar.DATE, -10);
            end.add(Calendar.DATE, 10);

            List<BirthDeclaration> existingRecords = birthDeclarationDAO.getByDOBRangeandMotherNICorPIN(
                start.getTime(), end.getTime(), dp.getDeathPersonMotherPINorNIC());

            Date date = dp.getDeathPersonDOB() != null ? dp.getDeathPersonDOB() : cal.getTime();
            if (existingRecords.isEmpty()) {
                warnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.birthDeclaration.not.exist"),
                    DateTimeUtils.getISO8601FormattedString(date),
                    dp.getDeathPersonMotherPINorNIC())));
            } else {
                for (BirthDeclaration b : existingRecords) {
                    switch (b.getRegister().getStatus()) {
                        case DATA_ENTRY:
                            warnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.birthDeclaration.not.approved"),
                                DateTimeUtils.getISO8601FormattedString(date),
                                dp.getDeathPersonMotherPINorNIC(), b.getIdUKey())));
                            break;
                        case ARCHIVED_CANCELLED:
                        case ARCHIVED_REJECTED:
                            warnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.birthDeclaration.not.exist"),
                                DateTimeUtils.getISO8601FormattedString(date),
                                dp.getDeathPersonMotherPINorNIC())));
                            break;
                    }
                }
            }
        }
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
            handleException("invalid serial number " + serialNumber + "unable to add death register ",
                ErrorCodes.INVALID_SERIAL_NUMBER);
        }

        //checking serial number duplication for the division
        DeathRegister existingRecord = deathRegisterDAO.getActiveRecordByBDDivisionAndDeathSerialNo(bdDivision,
            serialNumber);
        if (existingRecord != null) {
            handleException("serial number duplication  " + serialNumber + " for death division " +
                bdDivision.getEnDivisionName() + "unable to add marriage notice",
                ErrorCodes.POSSIBLE_DEATH_REGISTER_SERIAL_NUMBER_DUPLICATION);
        }
    }

}
