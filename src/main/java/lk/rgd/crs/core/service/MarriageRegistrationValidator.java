package lk.rgd.crs.core.service;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.MarriageRegistrationDAO;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.MarriageNotice;
import lk.rgd.crs.api.domain.MarriageRegister;
import lk.rgd.crs.api.service.MarriageRegistrationService;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.*;

/**
 * @author amith jayasekara
 *         validator class for marriage notice and marriage registration processes
 */
public class MarriageRegistrationValidator {
    private static final String AGE_AT_LAST_BD_FOR_VALID_MARRIAGE = "crs.age_at_last_bd_for_valid_marriage";
    private static final ResourceBundle rb_si =
        ResourceBundle.getBundle("messages/marriage_validation_messages_si", AppConstants.LK_SI);
    private static final ResourceBundle rb_ta =
        ResourceBundle.getBundle("messages/marriage_validation_messages_ta", AppConstants.LK_TA);
    private static final ResourceBundle rb_en =
        ResourceBundle.getBundle("messages/marriage_validation_messages_en", AppConstants.LK_EN);

    private static final Logger logger = LoggerFactory.getLogger(MarriageRegistrationValidator.class);
    private static final String SERIAL_NUMBER_PATTERN = "20([1-9][0-9])[0|1]([0-9]{5})";
    private final MarriageRegistrationDAO marriageRegistrationDAO;
    private final AppParametersDAO appParametersDAO;
    private final PopulationRegistry populationRegistry;

    public MarriageRegistrationValidator(MarriageRegistrationDAO marriageRegistrationDAO, AppParametersDAO appParametersDAO, PopulationRegistry populationRegistry) {
        this.marriageRegistrationDAO = marriageRegistrationDAO;
        this.appParametersDAO = appParametersDAO;
        this.populationRegistry = populationRegistry;
    }

    /**
     * validate marriage notice for adding marriage notice
     *
     * @param notice notice to be validate
     * @param type   type of the notice
     */
    public void validateMarriageNotice(MarriageRegister notice, MarriageNotice.Type type, User user) {
        //to be a valid notice notice must have follow properties
        //must have a valid serial number and a receive data and at least on of the party objects must be filled
        //if the notice is FEMALE notice identification number and date of birth* must be filled and vise-versa for
        // notice type MALE and BOTH
        //todo validate more
        switch (type) {
            case BOTH_NOTICE:
                validateBasicNeeds(notice.getSerialOfMaleNotice(), notice.getDateOfMaleNotice(),
                    notice.getFemale().getIdentificationNumberFemale(), notice.getFemale().getDateOfBirthFemale(),
                    notice.isSingleNotice());
            case MALE_NOTICE:
                validateBasicNeeds(notice.getSerialOfMaleNotice(), notice.getDateOfMaleNotice(),
                    notice.getMale().getIdentificationNumberMale(), notice.getMale().getDateOfBirthMale(),
                    notice.isSingleNotice());
                validateSerialNumber(notice.getSerialOfMaleNotice(), notice.getMrDivisionOfMaleNotice());
                break;
            case FEMALE_NOTICE:
                validateBasicNeeds(notice.getSerialOfFemaleNotice(), notice.getDateOfFemaleNotice(),
                    notice.getFemale().getIdentificationNumberFemale(), notice.getFemale().getDateOfBirthFemale(),
                    notice.isSingleNotice());
                validateSerialNumber(notice.getSerialOfFemaleNotice(), notice.getMrDivisionOfFemaleNotice());
        }
        //check both parties are registered in PRS
        Person party = null;
        if (notice.getFemale().getIdentificationNumberFemale() != null) {
            //TODO: find person by PIN or NIC
            //party = populationRegistry.findPersonByPIN(notice.getFemale().getIdentificationNumberFemale(), user);
            if (party == null) {
                handleException("unable to found bride record on PRS , bride PIN : " +
                    notice.getFemale().getIdentificationNumberFemale(), ErrorCodes.UNABLE_TO_FOUND_BRIDE_AT_PRS);
            }
        }
        if (notice.getMale().getIdentificationNumberMale() != null) {
            //TODO: find person by PIN or NIC
            //party = populationRegistry.findPersonByPIN(notice.getMale().getIdentificationNumberMale(), user);
            if (party == null) {
                handleException("unable to found grrom record on PRS , groom PIN : " +
                    notice.getMale().getIdentificationNumberMale(), ErrorCodes.UNABLE_TO_FOUND_GROOM_AT_PRS);
            }
        }
        //check other must needed validations
        // todo license req party must be filled default is mail to male
    }

    private void validateSerialNumber(long serialNumber, MRDivision mrDivision) {
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
        MarriageRegister existingRecord = marriageRegistrationDAO.getActiveRecordByMRDivisionAndSerialNo(
            mrDivision, serialNumber);
        if (existingRecord != null) {
            handleException("serial number duplication  " + serialNumber + " for marriage division " +
                mrDivision.getEnDivisionName() + "unable to add marriage notice",
                ErrorCodes.POSSIBLE_MARRIAGE_NOTICE_SERIAL_NUMBER_DUPLICATION);
        }
    }

    /**
     * issue user warnings when approving (fully approving) marriage notice
     * <p> this method issue several user warnings follow are list that this method check
     * <br><b>note : some warnings are only issue when it is about to change state to NOTICE_APPROVED  and some are issue
     * when notice is about to change state in to MALE_APPROVED or FEMALE_APPROVED
     * <br>
     * <ul>
     * <li>age at last birth day must be greater than the data base specified value ex:18 for both male <b>and</b>
     * female </li>
     * <li>check there any previouse marriages to male <b>or</b> female in PRS </li>
     * <li>todo issue more</li>
     * </ul>
     *
     * @param existing existing notice
     * @param type     type of the notice to be approved
     * @return list of warnings
     */
    public List<UserWarning> checkUserWarningsForApproveMarriageNotice(MarriageRegister existing,
        MarriageNotice.Type type, User user) {
        List<UserWarning> warning = new ArrayList<UserWarning>();
        ResourceBundle rb = rb_en;
        if (AppConstants.SINHALA.equals(user.getPrefLanguage())) {
            rb = rb_si;
        } else if (AppConstants.TAMIL.equals(user.getPrefLanguage())) {
            rb = rb_ta;
        }
        validateAgeAtLastBirthDay(existing, type, warning, rb);
        return warning;
    }

    /**
     * validate advance features  validate at approving license party or approving both notice
     */
    public List<UserWarning> advanceWarningsForMarriageNoticeApproval(MarriageRegister existing,
        MarriageNotice.Type type, List<UserWarning> userWarnings, User user) {
        ResourceBundle rb = rb_en;
        if (AppConstants.SINHALA.equals(user.getPrefLanguage())) {
            rb = rb_si;
        } else if (AppConstants.TAMIL.equals(user.getPrefLanguage())) {
            rb = rb_ta;
        }
        //validating previouse active marriages
        checkPreviouseActiveMarriages(existing, userWarnings, rb, user);
        return userWarnings;
    }

    /**
     * check is any one has previouse marriage
     */
    private void checkPreviouseActiveMarriages(MarriageRegister notice, List<UserWarning> userWarnings,
        ResourceBundle rb, User user) {
        //check male party is married before
        //TODO: find person by PIN or NIC
        Person person = null; //populationRegistry.findPersonByPIN(notice.getMale().getIdentificationNumberMale(), user);
        if (!checkCivilState(person)) {
            userWarnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.male.is.not.legal.for.marry"),
                notice.getMale().getIdentificationNumberMale()), UserWarning.Severity.WARN));
        }
        //check female party is married before
        //TODO: find person by PIN or NIC
        person = null; //populationRegistry.findPersonByPIN(notice.getFemale().getIdentificationNumberFemale(), user);
        if (!checkCivilState(person)) {
            userWarnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.female.is.not.legal.for.marry"),
                notice.getFemale().getIdentificationNumberFemale()), UserWarning.Severity.WARN));
        }
    }

    private boolean checkCivilState(Person person) {
        boolean canReMarry = false;
        if (person != null) {
            switch (person.getCivilStatus()) {
                case NEVER_MARRIED:
                case WIDOWED:
                case DIVORCED:
                case ANNULLED:
                    canReMarry = true;
                    break;
                case MARRIED:
                case SEPARATED:
            }
        }
        return canReMarry;
    }

    /**
     * there are some relationships that by legally baned form marry
     */
    private void banedMarriages() {
    }

    /**
     * if it is a BOTH_NOTICE to be approved we check both male and female age at last bd and if it is a MALE_NOTICE
     * it would be male party age ata last bd and vise versa
     */
    private void validateAgeAtLastBirthDay(MarriageRegister register, MarriageNotice.Type type,
        List<UserWarning> userWarnings, ResourceBundle rb) {
        int minAgeForValidMarriage = appParametersDAO.getIntParameter(AGE_AT_LAST_BD_FOR_VALID_MARRIAGE);
        switch (type) {
            case BOTH_NOTICE:
                if (register.getMale().getAgeAtLastBirthDayMale() < minAgeForValidMarriage) {
                    userWarnings.add(new UserWarning(rb.getString("warn.male.age.last.bd.is.less.than.expected"),
                        UserWarning.Severity.WARN));
                }
            case FEMALE_NOTICE:
                if (register.getFemale().getAgeAtLastBirthDayFemale() < minAgeForValidMarriage) {
                    userWarnings.add(new UserWarning(rb.getString("warn.female.age.last.bd.is.less.than.expected"),
                        UserWarning.Severity.WARN));
                }
                break;
            case MALE_NOTICE:
                if (register.getMale().getAgeAtLastBirthDayMale() < minAgeForValidMarriage) {
                    userWarnings.add(new UserWarning(rb.getString("warn.male.age.last.bd.is.less.than.expected"),
                        UserWarning.Severity.WARN));
                }
        }
    }

    /**
     * this warning is issued in special case
     * assume male is submitting first and he nominate female is to be capture the license ,
     * then before submitting female notice male notice is being approved,
     * now female is submitting and she declare male should get the license but the cannot happen because LP(license party)
     * must be approved second in that case warning is given for the user for asking should female keep the license
     * or should rollback the approval of male's approval
     *
     * @param existing notice to be check
     * @param type     type of the second notice
     * @return
     */
    public List<UserWarning> validateAddingSecondNoticeAndEdit(MarriageRegister existing, MarriageNotice.Type type, User user) {
        List<UserWarning> warning = new ArrayList<UserWarning>();
        //if second notice is a MALE notice and if its records current state is FEMALE_NOTICE_APPROVED and
        //second notice is nominating that female should get the notice and vise-versa
        ResourceBundle rb = rb_en;
        if (AppConstants.SINHALA.equals(user.getPrefLanguage())) {
            rb = rb_si;
        } else if (AppConstants.TAMIL.equals(user.getPrefLanguage())) {
            rb = rb_ta;
        }
        boolean checkFail = ((type == MarriageNotice.Type.MALE_NOTICE) &&
            (existing.getState() == MarriageRegister.State.FEMALE_NOTICE_APPROVED) &&
            ((existing.getLicenseCollectType() == MarriageRegister.LicenseCollectType.HAND_COLLECT_FEMALE)
                || (existing.getLicenseCollectType() == MarriageRegister.LicenseCollectType.MAIL_TO_FEMALE))) ||
            ((type == MarriageNotice.Type.FEMALE_NOTICE) &&
                (existing.getState() == MarriageRegister.State.MALE_NOTICE_APPROVED) &&
                ((existing.getLicenseCollectType() == MarriageRegister.LicenseCollectType.HAND_COLLECT_MALE)
                    || (existing.getLicenseCollectType() == MarriageRegister.LicenseCollectType.MAIL_TO_MALE)));

        if (checkFail) {
            warning.add(new UserWarning(rb.getString("warn.add.or.rollback.other.party.approval"), UserWarning.Severity.WARN));
            return warning;
        }
        return Collections.emptyList();
    }


    private void validateBasicNeeds(Long serial, Date recDate, String identificationNumber, Date dob, Boolean isSingle) {
        if (serial == null || recDate == null || identificationNumber == null || dob == null || isSingle == null) {
            handleException("marriage notice :serial" + serial + ": is incomplete can not add",
                ErrorCodes.MR_INCOMPLETE_OBJECT);
        }
    }

    // todo validate age and previous marriages 

    private static void handleException(String msg, int errorCode) {
        logger.error(msg);
        throw new CRSRuntimeException(msg, errorCode);
    }

    //TODO to be generalized
    public void validateMarriageRegisterSerialNumber(long serialNumber, MRDivision mrDivision) {
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

        List<MarriageRegister> existingRecord = marriageRegistrationDAO.getMarriageRegisterBySerialAndMRDivision(
            serialNumber, mrDivision);
        if (existingRecord.size() != 0) {
            handleException("serial number duplication  " + serialNumber + " for marriage division " +
                mrDivision.getEnDivisionName(),
                ErrorCodes.DUPLICATE_SERIAL_NUMBER);
        }
    }

}
