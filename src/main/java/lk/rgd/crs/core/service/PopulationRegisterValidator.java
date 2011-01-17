package lk.rgd.crs.core.service;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.DateTimeUtils;
import lk.rgd.common.util.NameFormatUtil;
import lk.rgd.common.util.PinAndNicUtils;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.prs.PRSRuntimeException;
import lk.rgd.prs.api.dao.PersonDAO;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.spi.Resolver;
import java.text.MessageFormat;
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
    private static final String POSSIBLE_DUPLICATE = "possible_duplicate";
    private final PersonDAO personDAO;

    public PopulationRegisterValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    /**
     * Validate minimum requirements of existing prs entries already in the prs(automatically added)
     *
     * @param person the person to validate
     */
    public void validateMinimalRequirementsOfExistingPerson(Person person) {
        logger.debug("Validating person for minimal requirements");
        if (checkMinimalCondition(person) || person.getAddresses() == null || person.getAddresses().isEmpty()) {
            handleMinimalRequirements(person);
        }
    }

    /**
     * Validate minimal requirements of prs entries in edit mode or newly added
     *
     * @param person the person to validate
     */
    public void validateMinimalRequirementsOfNewPerson(Person person) {
        if (checkMinimalCondition(person) || person.getPermanentAddress() == null) {
            handleMinimalRequirements(person);
        }
    }

    private boolean checkMinimalCondition(Person person) {
        final boolean condition = person.getDateOfRegistration() == null || person.getRace() == null ||
            person.getDateOfBirth() == null || person.getCivilStatus() == null ||
            isEmptyString(person.getPlaceOfBirth()) || isEmptyString(person.getFullNameInOfficialLanguage()) ||
            isEmptyString(person.getFullNameInEnglishLanguage());
        return condition;
    }

    private void handleMinimalRequirements(Person person) {
        if (person.getPersonUKey() > 0) {
            handleException("PRS entry with personUKey : " + person.getPersonUKey() + " is not complete. " +
                "Check required field values", ErrorCodes.INVALID_DATA);
        } else {
            handleException("PRS entry being processed is incomplete, Check required field values",
                ErrorCodes.INVALID_DATA);
        }
    }

    /**
     * Validate typical requirements for a PRS entry
     *
     * @param personDAO the person DAO
     * @param person    the Person to validate
     * @param user      the user initiating the validation
     * @return a list of warning issued against the Person record
     */
    public List<UserWarning> validateStandardRequirements(PersonDAO personDAO, Person person, User user) {
        // holder for capture warnings
        List<UserWarning> warnings = new ArrayList<UserWarning>();

        // select locale for user
        ResourceBundle rb = rb_en;
        if (AppConstants.SINHALA.equals(user.getPrefLanguage())) {
            rb = rb_si;
        } else if (AppConstants.TAMIL.equals(user.getPrefLanguage())) {
            rb = rb_ta;
        }

        final Long tempPin = person.getTemporaryPin();
        final String nic = person.getNic();
        final long personUKey = person.getPersonUKey();

        // TODO need to add solr search duplicates also
        // check if this is a duplicate entry by temporary pin
        if (tempPin != null) {
            Person p = personDAO.findPersonByTemporaryPIN(person.getTemporaryPin());
            if (p != null && (isRecordInDataEntryOrApproved(p.getStatus())) && personUKey != p.getPersonUKey()) {
                addDuplicateWarning(warnings, rb, p);
            }
        }
        // check if this a duplicate entry by nic
        if (nic != null) {
            for (Person p : personDAO.findPersonsByNIC(person.getNic())) {
                if (isRecordInDataEntryOrApproved(p.getStatus()) && personUKey != p.getPersonUKey()) {
                    addDuplicateWarning(warnings, rb, p);
                }
            }
        }

        // validate person pin or nic
        String pinOrNic = person.getNic();
        if (!PinAndNicUtils.isValidNIC(pinOrNic)) {
            UserWarning w = new UserWarning(MessageFormat.format(rb.getString("invalid_person_nic"), pinOrNic));
            w.setSeverity(UserWarning.Severity.ERROR);
            warnings.add(w);
        }

        // TODO chathuranga uncomment
        // validate person temporary pin
        /* if (!PinAndNicUtils.isValidPIN(tempPin, , user)) {
            UserWarning w = new UserWarning(MessageFormat.format(rb.getString("invalid_person_tempPin"), pinOrNic));
            w.setSeverity(UserWarning.Severity.ERROR);
            warnings.add(w);
        }

        // validate mother pin or nic
        pinOrNic = person.getMotherPINorNIC();
        if (!PinAndNicUtils.isValidPINorNIC(pinOrNic, , user)) {
            UserWarning w = new UserWarning(MessageFormat.format(rb.getString("invalid_mother_pin"), pinOrNic));
            w.setSeverity(UserWarning.Severity.ERROR);
            warnings.add(w);
        }

        // validate father pin or nic
        pinOrNic = person.getFatherPINorNIC();
        if (!PinAndNicUtils.isValidPINorNIC(pinOrNic, , user)) {
            UserWarning w = new UserWarning(MessageFormat.format(rb.getString("invalid_father_pin"), pinOrNic));
            w.setSeverity(UserWarning.Severity.ERROR);
            warnings.add(w);
        }*/

        // TODO more validations to be added

        return warnings;
    }

    private void addDuplicateWarning(List<UserWarning> warnings, ResourceBundle rb, Person p) {
        warnings.add(
            new UserWarning(MessageFormat.format(rb.getString(POSSIBLE_DUPLICATE), p.getPersonUKey(),
                DateTimeUtils.getISO8601FormattedString(p.getDateOfBirth()),
                NameFormatUtil.getDisplayName(p.getFullNameInOfficialLanguage(), 30), p.getStatus())));
    }

    private boolean isRecordInDataEntryOrApproved(Person.Status currentState) {
        return (currentState == Person.Status.SEMI_VERIFIED || currentState == Person.Status.UNVERIFIED ||
            currentState == Person.Status.DATA_ENTRY || currentState == Person.Status.VERIFIED);
    }

    private boolean isEmptyString(String s) {
        return s == null || s.trim().length() == 0;
    }

    private void handleException(String message, int errorCode) {
        logger.error(message);
        throw new PRSRuntimeException(message, errorCode);
    }
}
