package lk.rgd.crs.core.service;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.AssignmentDAO;
import lk.rgd.crs.api.dao.MarriageRegistrationDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.RegistrarManagementService;
import lk.rgd.prs.api.domain.Marriage;
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
    private final RegistrarManagementService registrarManagementService;
    private final AssignmentDAO assignmentDAO;

    public MarriageRegistrationValidator(MarriageRegistrationDAO marriageRegistrationDAO, AppParametersDAO appParametersDAO, PopulationRegistry populationRegistry, RegistrarManagementService registrarManagementService, AssignmentDAO assignmentDAO) {
        this.marriageRegistrationDAO = marriageRegistrationDAO;
        this.appParametersDAO = appParametersDAO;
        this.populationRegistry = populationRegistry;
        this.registrarManagementService = registrarManagementService;
        this.assignmentDAO = assignmentDAO;
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
            try {
                long pin = Long.parseLong(notice.getFemale().getIdentificationNumberFemale());
                party = populationRegistry.findPersonByPIN(pin, user);
                if (party != null) {
                    logger.debug("Female party  found by PIN : {}", pin);
                }
            } catch (NumberFormatException ignore) {
            }
            //;
            if (party == null) {
                handleException("unable to found bride record on PRS , bride PIN : " +
                    notice.getFemale().getIdentificationNumberFemale(), ErrorCodes.UNABLE_TO_FOUND_BRIDE_AT_PRS);
            }
        }
        if (notice.getMale().getIdentificationNumberMale() != null) {
            try {
                long pin = Long.parseLong(notice.getMale().getIdentificationNumberMale());
                party = populationRegistry.findPersonByPIN(pin, user);
                if (party != null) {
                    logger.debug("Male party  found by PIN : {}", pin);
                }
            } catch (NumberFormatException ignore) {
            }
            //
            if (party == null) {
                handleException("unable to found groom record on PRS , groom PIN : " +
                    notice.getMale().getIdentificationNumberMale(), ErrorCodes.UNABLE_TO_FOUND_GROOM_AT_PRS);
            }
        }
        //check other must needed validations
        // todo license req party must be filled default is mail to male
    }

    /**
     * Validate marriage register and return appropriate warnings list
     *
     * @param mr   the marriage register to be validated
     * @param user the user performing the action
     * @return a list of warnings issued against the marriage register
     */
    public List<UserWarning> validateWarningsOfMarriageRegister(MarriageRegister mr, User user) {
        List<UserWarning> warnings = new ArrayList<UserWarning>();

        // select locale for user
        ResourceBundle rb = rb_en;
        if (AppConstants.SINHALA.equals(user.getPrefLanguage())) {
            rb = rb_si;
        } else if (AppConstants.TAMIL.equals(user.getPrefLanguage())) {
            rb = rb_ta;
        }

        // check pin or nic duplicates in marriage register
        final String malePin = mr.getMale().getIdentificationNumberMale();
        final String femalePin = mr.getFemale().getIdentificationNumberFemale();
        if (malePin != null && femalePin != null) {
            checkDuplicatePinOrNic(malePin, femalePin, warnings, rb, "duplicate_male_female_pin");
        }

        //check is registrar is assign to this job
        //we cannot validate this with minister so we only look at registrars
        //following warning only issue when it is come by a notice
        if (mr.getSerialOfMaleNotice() != null || mr.getSerialOfFemaleNotice() != null) {
            try {
                Registrar registrar = registrarManagementService.getRegistrarByPin(Long.parseLong(mr.getRegistrarOrMinisterPIN()), user);
                //assign type
                Assignment.Type assignmentType = null;
                boolean isRegistrarHasPermission = false;
                switch (mr.getTypeOfMarriage()) {
                    case GENERAL:
                        assignmentType = Assignment.Type.GENERAL_MARRIAGE;
                        break;
                    case MUSLIM:
                        assignmentType = Assignment.Type.MUSLIM_MARRIAGE;
                        break;
                    case KANDYAN_BINNA:
                    case KANDYAN_DEEGA:
                        assignmentType = Assignment.Type.KANDYAN_MARRIAGE;
                        break;
                }
                if (registrar != null && registrar.getLifeCycleInfo().isActive()) {
                    Set<Assignment> registrarsAssignments = registrar.getAssignments();
                    //check is registrar is assign to do marriages with give type
                    for (Assignment assignment : registrarsAssignments) {
                        if (assignment.getLifeCycleInfo().isActive() == true && assignment.getType() == assignmentType) {
                            logger.debug("registrar : {} ,is assigned to register : {} , marriages ",
                                registrar.getPin(), assignmentType);
                            //check only active assignments
                            //check is registrar is terminated from the job
                            if (assignment.getTerminationDate() != null &&
                                assignment.getTerminationDate().before(mr.getDateOfMarriage())) {
                                if (logger.isDebugEnabled()) {
                                    logger.debug("registrar : " + registrar.getPin() +
                                        " is terminated form the task registering : " + assignmentType +
                                        " marriages from : " + assignment.getTerminationDate());
                                }
                                //that mean this registrar is terminated by this day
                                warnings.add(new UserWarning(MessageFormat.
                                    format(rb.getString("warn.registrar.terminate.by.date.of.marriage"), Long.toString(registrar.getPin())),
                                    UserWarning.Severity.WARN));
                            }
                            isRegistrarHasPermission = true;
                            break;
                        }
                    }
                    if (!isRegistrarHasPermission) {
                        warnings.add(new UserWarning(MessageFormat.
                            format(rb.getString("warn.registrar.does.not.have.permission.to.do.marriage"), assignmentType, Long.toString(registrar.getPin())),
                            UserWarning.Severity.WARN));
                    }

                }                 //check
            }
            catch (NumberFormatException e) {
                //do nothing this is happen try to find a registrar by PIN
            }
        }
        return warnings;
    }

    public List<UserWarning> checkExistingMarriages(List<UserWarning> userWarnings, MarriageRegister register, User user) {
        // select locale for user
        ResourceBundle rb = rb_en;
        if (AppConstants.SINHALA.equals(user.getPrefLanguage())) {
            rb = rb_si;
        } else if (AppConstants.TAMIL.equals(user.getPrefLanguage())) {
            rb = rb_ta;
        }

        //check grooms existing marriages
        //groom on PRS
        final Person groom = populationRegistry.findUniquePersonByPINorNIC(register.getMale().getIdentificationNumberMale(), user);
        final Person bride = populationRegistry.findUniquePersonByPINorNIC(register.getFemale().getIdentificationNumberFemale(), user);
        if (groom != null) {
            Set<Marriage> groomsMarriages = groom.getMarriages();
            for (Marriage m : groomsMarriages) {
                //check existing marriage with MARRIED
                if (m.getState() == Marriage.State.MARRIED) {
                    userWarnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.existing.marriage.found.for.groom"),
                        (m.getBride().getPin() != null) ? m.getBride().getPin() : m.getBride().getNic()),
                        UserWarning.Severity.WARN));
                    break;
                }
            }
        }
        if (bride != null) {
            Set<Marriage> bridesMarriages = bride.getMarriages();
            for (Marriage m : bridesMarriages) {
                if (m.getState() == Marriage.State.MARRIED) {
                    userWarnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.existing.marriage.found.for.bride"),
                        (m.getGroom().getPin() != null) ? m.getGroom().getPin() : m.getGroom().getNic()),
                        UserWarning.Severity.WARN));
                    break;
                }
            }
        }
        return userWarnings;
    }


    /**
     * Validate minimum requirements of marriage register
     *
     * @param mr the marriage register to be validated
     */
    public void validateMinimalRequirementsOfMarriageRegister(MarriageRegister mr) {
        logger.debug("Validate marriage register for minimal requirements");
        MaleParty male = mr.getMale();
        FemaleParty female = mr.getFemale();
        final boolean condition = mr.getDateOfMarriage() == null || mr.getRegistrarOrMinisterPIN() == null ||
            mr.getTypeOfMarriagePlace() == null || mr.getMrDivision() == null || mr.getRegPlaceInOfficialLang() == null ||
            mr.getRegNameInOfficialLang() == null || mr.getTypeOfMarriage() == null || male.getAgeAtLastBirthDayMale() == 0 ||
            male.getMaleRace() == null || male.getCivilStatusMale() == null || male.getNameInOfficialLanguageMale() == null ||
            male.getResidentAddressMaleInOfficialLang() == null || female.getAgeAtLastBirthDayFemale() == 0 ||
            female.getFemaleRace() == null || female.getCivilStatusFemale() == null ||
            female.getNameInOfficialLanguageFemale() == null || female.getResidentAddressFemaleInOfficialLang() == null ||
            (mr.getSerialNumber() == 0 && mr.getSerialOfMaleNotice() == null && mr.getSerialOfFemaleNotice() == null) ||
            mr.getRegistrationDate() == null || mr.getScannedImagePath() == null;

        if (condition) {
            handleException("Marriage register being processed is incomplete, Check required fields values of record : "
                + mr.getIdUKey(), ErrorCodes.INVALID_DATA);
        }

        if (mr.getSerialNumber() == 0 && (mr.getSerialOfMaleNotice() != null || mr.getSerialOfFemaleNotice() != null)) {
            final long serialNo = mr.getSerialNumber();
            if (serialNo >= 2010000001L && serialNo <= 2099199999L) {

                String s = Long.toString(serialNo);
                if (!s.matches(SERIAL_NUMBER_PATTERN)) {
                    handleException("Marriage register being processed is invalid, Check serial number : " + s,
                        ErrorCodes.INVALID_DATA);
                }
            }
        }
    }

    private final void checkPinOrNicDuplicatesOfMarriageNotice(MarriageRegister mr, List<UserWarning> warnings,
        ResourceBundle rb) {
        final String malePin = mr.getMale().getIdentificationNumberMale();
        final String femalePin = mr.getFemale().getIdentificationNumberFemale();
        final String fatherMalePin = mr.getMale().getFatherIdentificationNumberMale();
        final String fatherFemalePin = mr.getFemale().getFatherIdentificationNumberFemale();

        checkDuplicatePinOrNic(malePin, femalePin, warnings, rb, "duplicate_male_female_pin");
        checkDuplicatePinOrNic(malePin, fatherMalePin, warnings, rb, "duplicate_male_fatherMale_pin");
        checkDuplicatePinOrNic(malePin, fatherFemalePin, warnings, rb, "duplicate_male_fatherFemale_pin");
        checkDuplicatePinOrNic(femalePin, fatherMalePin, warnings, rb, "duplicate_female_fatherMale_pin");
        checkDuplicatePinOrNic(femalePin, fatherFemalePin, warnings, rb, "duplicate_female_fatherFemale_pin");
    }

    private final void checkDuplicatePinOrNic(final String s1, final String s2, final List<UserWarning> warnings,
        ResourceBundle rb, String key) {
        try {
            if (s1.equals(s2)) {
                warnings.add(new UserWarning(MessageFormat.format(rb.getString(key), s1)));
            }
        } catch (NullPointerException expected) {
            // since pin or nic(s1) can be null
        }
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
        checkPinOrNicDuplicatesOfMarriageNotice(existing, warning, rb);
        return warning;
    }

    public List<UserWarning> checkUserWarningsForSecondNoticeApproval(MarriageRegister existing, User user) {
        List<UserWarning> warning = new ArrayList<UserWarning>();
        ResourceBundle rb = rb_en;
        if (AppConstants.SINHALA.equals(user.getPrefLanguage())) {
            rb = rb_si;
        } else if (AppConstants.TAMIL.equals(user.getPrefLanguage())) {
            rb = rb_ta;
        }
        checkSamePinNumber(existing, warning, rb);
        prohibitedRelationship(existing, warning, rb, user);
        return warning;
    }

    public List<UserWarning> checkProhibitedRelationshipsForMarriageRegistration(MarriageRegister register, List<UserWarning> userWarnings, User user) {
        ResourceBundle rb = rb_en;
        if (AppConstants.SINHALA.equals(user.getPrefLanguage())) {
            rb = rb_si;
        } else if (AppConstants.TAMIL.equals(user.getPrefLanguage())) {
            rb = rb_ta;
        }
        prohibitedRelationship(register, userWarnings, rb, user);
        return userWarnings;
    }
    //check age again
    //check previouse marriages
    //prohibited relationships

    //todo One party is a grandchild of a sibling of the other party
    // todo One party was the spouse of a deceased grandchild of the other party
    //todo implement PRS method for find grand children

    private boolean prohibitedRelationship(MarriageRegister register, List<UserWarning> userWarnings,
        ResourceBundle rb, User user) {
        final Person bride = populationRegistry.findPersonByPIN(Long.
            parseLong(register.getFemale().getIdentificationNumberFemale()), user);
        final Person groom = populationRegistry.findPersonByPIN(Long.
            parseLong(register.getMale().getIdentificationNumberMale()), user);

        final Person bridesFather = bride.getFather();
        final Person bridesMother = bride.getMother();
        final Person groomsFather = groom.getFather();
        final Person groomsMother = groom.getMother();
        final List<Person> groomsGrandFathers = populationRegistry.findGrandFather(groom, user);
        final List<Person> bridesGrandMothers = populationRegistry.findGrandMother(bride, user);
        final List<Person> bridesChildren = populationRegistry.findAllChildren(bride, user);
        final List<Person> groomsChildren = populationRegistry.findAllChildren(groom, user);
        boolean prohibited = false;
        //check father mismatched
        if (bridesFather != null && register.getFemale().getFatherIdentificationNumberFemale() != null &&
            !bridesFather.equals(populationRegistry.
                findUniquePersonByPINorNIC(register.getFemale().getFatherIdentificationNumberFemale(), user))) {
            handleException("given father information for bride is mismatched with PRS father information's for bride",
                ErrorCodes.BRIDES_FATHER_IN_PRS_IS_MISMATCHED_WITH_GIVEN_FATHER);
        }
        if (groomsFather != null && register.getMale().getFatherIdentificationNumberMale() != null &&
            !groomsFather.equals(populationRegistry.
                findUniquePersonByPINorNIC(register.getMale().getFatherIdentificationNumberMale(), user))) {
            handleException("given father information for groom is mismatched with PRS father information's for groom",
                ErrorCodes.GROOMS_FATHER_IN_PRS_IS_MISMATCHED_WITH_GIVEN_FATHER);
        }


        //One party is a child of the other party
        //check is  bride is daughter of groom
        //get all available children of the groom
        List<Person> groomChildren = populationRegistry.findAllChildren(groom, user);
        for (Person p : groomChildren) {
            if ((Long.parseLong(register.getFemale().getIdentificationNumberFemale()) == p.getPin()) && p.getGender() == 1) {
                //this means bride is a child of a groom
                userWarnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.prohibited.relation.bride.is.daughter.of.groom"), bride.getPin(), p.getPin()),
                    UserWarning.Severity.ERROR));
                prohibited = true;
                break;
            }
        }
        logger.debug("check prohibited relationship :'bride is daughter of groom' :{}", prohibited);
        //check is groom is sun of bride
        List<Person> brideChildren = populationRegistry.findAllChildren(bride, user);
        for (Person p : brideChildren) {
            if ((Long.parseLong(register.getMale().getIdentificationNumberMale()) == p.getPin()) && (p.getGender() == 0)) {
                userWarnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.prohibited.relation.groom.is.sun.of.bride"), groom.getPin(), p.getPin()),
                    UserWarning.Severity.ERROR));
                prohibited = true;
                break;
            }
        }
        logger.debug("check prohibited relationship :'groom is sun of bride' :{}", prohibited);
        //One party is a grandchild of the other party (as a minimum, a parent‟s father or  mother)
        //check bride and bride's grand father violations
        List<Person> bridesGrandFathers = populationRegistry.findGrandFather(bride, user);
        for (Person p : bridesGrandFathers) {
            if (p.equals(groom)) {
                userWarnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.prohibited.relation.groom.is.grand.father.bride"), groom.getPin(), bride.getPin()),
                    UserWarning.Severity.ERROR));
                prohibited = true;
                break;
            }
        }
        logger.debug("check prohibited relationship :'groom is grand father of bride' :{}", prohibited);

        //check groom and groom's grand mother violations
        List<Person> groomsGrandMothers = populationRegistry.findGrandMother(groom, user);
        for (Person p : groomsGrandMothers) {
            if (p.equals(bride)) {
                userWarnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.prohibited.relation.bride.is.grand.mother.groom"), bride.getPin(), groom.getPin()),
                    UserWarning.Severity.ERROR));
                prohibited = true;
                break;
            }
        }
        logger.debug("check prohibited relationship :'bride is grand mother of groom' :{}", prohibited);

        //One party is a sibling of the other party (Note: two parties are siblings if they have one or both parents in common)
        List<Person> bridesSiblings = populationRegistry.findAllSiblings(bride, user);

        //check groom is sibling of bride
        if (bridesSiblings.contains(groom)) {
            userWarnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.prohibited.relation.bride.groom.are.siblings"), bride.getPin(), groom.getPin()),
                UserWarning.Severity.ERROR));
            prohibited = true;
        }
        logger.debug("check prohibited relationship :'groom and bride are siblings' :{}", prohibited);

        //no need to check bride is sibling of groom this is symmetric

        //One party is a child of a sibling of the other party (i.e. one party is uncle or aunt of other
        //get brides fathers male siblings
        List<Person> bridesUncles = filterPersonSiblings(bridesFather, 0, user);
        //add brides mother male siblings
        bridesUncles.addAll(filterPersonSiblings(bridesMother, 0, user));
        //check grooms is brides uncle
        if (bridesUncles.contains(groom)) {
            userWarnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.prohibited.relation.groom.is.bride.uncle"), groom.getPin(), bride.getPin()),
                UserWarning.Severity.ERROR));
            prohibited = true;
        }
        logger.debug("check prohibited relationship :'groom is bride's uncle' :{}", prohibited);

        //get grooms father's female siblings
        List<Person> groomsUnties = filterPersonSiblings(groomsFather, 1, user);
        //get grooms mother's female siblings
        groomsUnties.addAll(filterPersonSiblings(groomsMother, 1, user));
        //check bride is aunt  of groom
        if (groomsUnties.contains(bride)) {
            userWarnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.prohibited.relation.bride.is.aunt.of.groom"), bride.getPin(), groom.getPin()),
                UserWarning.Severity.ERROR));
            prohibited = true;
        }
        logger.debug("check prohibited relationship :'bride is groom's aunt' :{}", prohibited);

        //A parent of one party was the spouse of the other party
        //One party was the spouse of a deceased parent of the other party    (both cases are handle by follow)

        //that means if bride was previously married with grooms father  then relation ship between bride and groom is illegal
        //and vise versa
        //check brides previous marriages  vs grooms father
        Set<Marriage> bridesPrevMarriages = bride.getMarriages();
        for (Marriage m : bridesPrevMarriages) {
            if (groomsFather != null) {
                if (groomsFather.equals(m.getGroom())) {
                    userWarnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.prohibited.relation.father.of.groom.was.spouse.of.bride"), groomsFather.getPin(), bride.getPin()),
                        UserWarning.Severity.ERROR));
                    prohibited = true;
                    break;
                }
            }
        }
        logger.debug("check prohibited relationship :'father of groom was a spouse of bride' :{}", prohibited);

        //check grooms prev marriages vs brides mother
        Set<Marriage> groomsPrevMarriages = bride.getMarriages();
        for (Marriage m : groomsPrevMarriages) {
            if (bridesMother != null) {
                if (bridesMother.equals(m.getGroom())) {
                    userWarnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.prohibited.relation.mother.of.bride.was.spouse.of.groom"), bridesMother.getPin(), groom.getPin()),
                        UserWarning.Severity.ERROR));
                    prohibited = true;
                    break;
                }
            }
        }
        logger.debug("check prohibited relationship :'mother of bride was a spouse of groom' :{}", prohibited);

        //One party was the spouse of a deceased grandparent of the other party
        //that means if bride was married with grooms grand father then there is a prohibited relationship between bride and groom
        //bride's prev marriages vs groom's grand fathers
        for (Marriage m : bridesPrevMarriages) {
            for (Person p : groomsGrandFathers) {
                if (p.equals(m.getGroom())) {
                    userWarnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.prohibited.relation.grand.father.of.groom.was.spouse.of.bride"), p.getPin(), bride.getPin()),
                        UserWarning.Severity.ERROR));
                    prohibited = true;
                    break;
                }
            }
        }
        logger.debug("check prohibited relationship :'grand father of groom was a spouse of bride' :{}", prohibited);

        //cross check with previouse marriage persons vs grooms grand fathers
        for (Marriage m : groomsPrevMarriages) {
            for (Person p : bridesGrandMothers) {
                if (p.equals(m.getBride())) {
                    userWarnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.prohibited.relation.grand.mother.of.bride.was.spouse.of.groom"), p.getPin(), groom.getPin()),
                        UserWarning.Severity.ERROR));
                    prohibited = true;
                    break;
                }
            }
        }
        logger.debug("check prohibited relationship :'grand mother of bride was a spouse of groom' :{}", prohibited);

        //One party was the spouse of a deceased child of the other party
        //that mean if groom and brides child was married before then groom and bride in prohibited relationship and vise versa
        //brides prev marriages vs grooms children
        for (Marriage m : bridesPrevMarriages) {
            for (Person p : groomsChildren) {
                if (p.equals(m.getGroom())) {
                    userWarnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.prohibited.relation.child.of.groom.was.spouse.of.bride"), p.getPin(), bride.getPin()),
                        UserWarning.Severity.ERROR));
                    prohibited = true;
                    break;
                }
            }
        }
        logger.debug("check prohibited relationship :'child of groom of a spouse of brides' :{}", prohibited);

        //grooms prev marriages vs brides children
        for (Marriage m : groomsPrevMarriages) {
            for (Person p : bridesChildren) {
                if (p.equals(m.getBride())) {
                    userWarnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.prohibited.relation.child.of.bride.was.spouse.of.groom"), p.getPin(), groom.getPin()),
                        UserWarning.Severity.ERROR));
                    prohibited = true;
                    break;
                }
            }
        }
        logger.debug("check prohibited relationship :'child of bride was a spouse of groom' :{}", prohibited);

        return prohibited;

    }

    private List<Person> filterPersonSiblings(Person person, int gender, User user) {
        List<Person> personSiblings = Collections.emptyList();
        if (person != null) {
            personSiblings = populationRegistry.findAllSiblings(person, user);
            List<Person> toBeRemoved = Collections.emptyList();
            if (gender == 0) {
                for (Person p : personSiblings) {
                    if (p.getGender() != 0) {
                        //not male siblings
                        toBeRemoved.add(p);
                    }
                }
            } else if (gender == 1) {
                for (Person p : personSiblings) {
                    if (p.getGender() != 1) {
                        //not female siblings
                        toBeRemoved.add(p);
                    }
                }
            }
            //removing unnecessary siblings
            personSiblings.removeAll(toBeRemoved);
        }
        return personSiblings;
    }


    private void checkSamePinNumber(MarriageRegister existing, List<UserWarning> warnings,
        ResourceBundle rb) {
        //todo mode warnings
        //check both bride and groom has same pin number
        if (existing.getMale().getIdentificationNumberMale().equals(existing.getFemale().getIdentificationNumberFemale())) {
            warnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.both.bride.groom.has.same.pin"),
                existing.getMale().getIdentificationNumberMale()), UserWarning.Severity.WARN));
        }
        //check booth father pin are same
        if (existing.getFemale().getFatherIdentificationNumberFemale() != null && existing.getMale().getFatherIdentificationNumberMale() != null) {
            if (existing.getFemale().getFatherIdentificationNumberFemale().equals(existing.getMale().getFatherIdentificationNumberMale())) {
                warnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.both.father.same"),
                    existing.getFemale().getFatherIdentificationNumberFemale()), UserWarning.Severity.WARN));
            }
        }
        //check brides father and groom are same
        if (existing.getFemale().getFatherIdentificationNumberFemale() != null) {
            if (existing.getFemale().getFatherIdentificationNumberFemale().equals(existing.getMale().getIdentificationNumberMale())) {
                warnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.groom.and.bride.father.same"),
                    existing.getFemale().getFatherIdentificationNumberFemale()), UserWarning.Severity.WARN));
            }
        }
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
        Person person = null;
        if (notice.getMale().getIdentificationNumberMale() != null) {
            person = populationRegistry.findPersonByPIN(Long.parseLong(notice.getMale().getIdentificationNumberMale()), user);
            if (!checkCivilState(person)) {
                userWarnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.male.is.not.legal.for.marry"),
                    notice.getMale().getIdentificationNumberMale()), UserWarning.Severity.WARN));
            }
        }
        //check female party is married before
        if (notice.getFemale().getIdentificationNumberFemale() != null) {
            person = populationRegistry.findPersonByPIN(Long.parseLong(notice.getFemale().getIdentificationNumberFemale()), user);
            if (!checkCivilState(person)) {
                userWarnings.add(new UserWarning(MessageFormat.format(rb.getString("warn.female.is.not.legal.for.marry"),
                    notice.getFemale().getIdentificationNumberFemale()), UserWarning.Severity.WARN));
            }
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
