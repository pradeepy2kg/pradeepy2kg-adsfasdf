package lk.rgd.crs.core.service;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.api.domain.*;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.DeathRegisterDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.DeathPersonInfo;
import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.service.DeathRegistrationService;
import lk.rgd.crs.core.ValidationUtils;
import lk.rgd.crs.web.WebConstants;
import lk.rgd.prs.api.domain.Address;
import lk.rgd.prs.api.domain.Marriage;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Indunil Moremada
 * @author amith jayasekara
 */
public class DeathRegistrationServiceImpl implements DeathRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(DeathRegistrationService.class);
    private final DeathRegisterDAO deathRegisterDAO;
    private final AppParametersDAO appParametersDAO;
    private final PopulationRegistry ecivil;
    private final UserManager userManager;
    private final DeathDeclarationValidator deathDeclarationValidator;

    DeathRegistrationServiceImpl(DeathRegisterDAO deathRegisterDAO, UserManager userManager, PopulationRegistry ecivil,
        DeathDeclarationValidator deathDeclarationValidator, AppParametersDAO appParametersDAO) {
        this.deathRegisterDAO = deathRegisterDAO;
        this.userManager = userManager;
        this.ecivil = ecivil;
        this.deathDeclarationValidator = deathDeclarationValidator;
        this.appParametersDAO = appParametersDAO;
    }

    /**
     * @inheritDoc
     */
    // todo remove no usage found for that method so we trow Unsupported operation exception until we remove this function
    @Transactional(propagation = Propagation.REQUIRED)
    public void addLateDeathRegistration(DeathRegister deathRegistration, User user) {
        logger.debug("adding late/missing death registration");
        //validate access of the user  to Death division
        ValidationUtils.validateAccessToBDDivision(user, deathRegistration.getDeath().getDeathDivision());
        deathDeclarationValidator.validateMinimalRequirements(deathRegistration);
        if (deathRegistration.getDeathType() != DeathRegister.Type.LATE &&
            deathRegistration.getDeathType() != DeathRegister.Type.MISSING &&
            deathRegistration.getDeathType() != DeathRegister.Type.SUDDEN) {
            handleException("Invalid death type : " + deathRegistration.getDeathType(), ErrorCodes.ILLEGAL_STATE);
        }
        addDeathRegistration(deathRegistration, user);
        logger.debug("added a late/missing registration with idUKey : {} ", deathRegistration.getIdUKey());
        throw new UnsupportedOperationException("this method  {addLateDeathRegistration} does not have a usage if any " +
            "usage found  remove this exception throw ");
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addNewDeathRegistration(DeathRegister deathRegistration, User user) {

        //validate access of the user  to Death division
        ValidationUtils.validateAccessToBDDivision(user, deathRegistration.getDeath().getDeathDivision());
        deathDeclarationValidator.validateMinimalRequirements(deathRegistration);
        addDeathRegistration(deathRegistration, user);
        logger.debug("added a death  registration with idUKey : {}  and type of death : {}",
            deathRegistration.getIdUKey(), deathRegistration.getDeathType());
    }

    /**
     * Checks whether the given death register belongs to a infant
     *
     * @param deathRegister the death register bean
     * @return if infant true else false
     */
    private boolean checkDeathPersonInfant(DeathRegister deathRegister) {
        Date dob = deathRegister.getDeathPerson().getDeathPersonDOB();
        Date dod = deathRegister.getDeath().getDateOfDeath();
        int infantDays = appParametersDAO.getIntParameter(AppParameter.CRS_DEATH_INFANT_DAYS);

        if (dob != null && dod != null) {
            long dobMilli = dob.getTime();
            long dodMilli = dod.getTime();

            long days = (dodMilli - dobMilli) / WebConstants.DAY_IN_MILLISECONDS;
            return days <= infantDays;
        }

        DeathPersonInfo dp = deathRegister.getDeathPerson();
        if ((dp.getDeathPersonAge() == null || dp.getDeathPersonAge() == 0) && dp.getDeathPersonAgeMonth() == null &&
            dp.getDeathPersonAgeDate() != null && dp.getDeathPersonAgeDate() <= infantDays) {
            return true;
        }

        return false;
    }

    private void addDeathRegistration(DeathRegister deathRegistration, User user) {
        validateAccessOfUser(user, deathRegistration);
        // checks age and set it as a death of infant less than 30 days
        deathRegistration.getDeath().setInfantLessThan30Days(checkDeathPersonInfant(deathRegistration));

        //validate minimal requirements
        deathDeclarationValidator.validateMinimalRequirements(deathRegistration);
        // has this serial number been used already?
        DeathRegister existing = deathRegisterDAO.getActiveRecordByBDDivisionAndDeathSerialNo(deathRegistration.getDeath().getDeathDivision(),
            deathRegistration.getDeath().getDeathSerialNo());
        if (existing != null) {
            handleException("can not add death registration " + existing.getIdUKey() +
                " deathRegistration number already exists : " + existing.getStatus(), ErrorCodes.ENTITY_ALREADY_EXIST);
        }
        deathRegistration.setStatus(DeathRegister.State.DATA_ENTRY);
        deathRegisterDAO.addDeathRegistration(deathRegistration, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDeathRegistration(DeathRegister deathRegistration, User user) {
        businessValidations(deathRegistration, user);
        deathRegistration.getDeath().setInfantLessThan30Days(checkDeathPersonInfant(deathRegistration));

        DeathRegister dr = deathRegisterDAO.getById(deathRegistration.getIdUKey());
        if (DeathRegister.State.DATA_ENTRY != dr.getStatus()) {
            handleException("Cannot update death registration " + deathRegistration.getIdUKey() +
                " Illegal state at target : " + dr.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        deathRegisterDAO.updateDeathRegistration(deathRegistration, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public DeathRegister getById(long deathRegisterIdUKey, User user) {
        logger.debug("Load death registration record : {}", deathRegisterIdUKey);
        DeathRegister deathRegister;
        try {
            deathRegister = deathRegisterDAO.getById(deathRegisterIdUKey);
            validateAccessOfUser(user, deathRegister);
            return deathRegister;
        } catch (NullPointerException e) {
            logger.debug("no results found for death id : {}", deathRegisterIdUKey);
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public DeathRegister getWithTransientValuesById(long idUKey, boolean certificateSearch, User user) {
        logger.debug("load death register with idUKey : {} record with transient values", idUKey);
        DeathRegister dr = deathRegisterDAO.getById(idUKey);
        loadValues(dr, certificateSearch, user);
        return dr;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public DeathRegister getById(long deathRegisterIdUKey) {
        logger.debug("Load death registration record : {}", deathRegisterIdUKey);
        DeathRegister deathRegister;
        try {
            deathRegister = deathRegisterDAO.getById(deathRegisterIdUKey);
            return deathRegister;
        } catch (NullPointerException e) {
            logger.debug("no results found for death id : {}", deathRegisterIdUKey);
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    //todo do validation warnings
    public List<UserWarning> approveDeathRegistration(long deathRegisterIdUKey, User user, boolean ignoreWarnings) {
        logger.debug("attempt to approve death registration record : {} ", deathRegisterIdUKey);
        DeathRegister dr = getById(deathRegisterIdUKey, user);
        if (dr.getStatus() != DeathRegister.State.DATA_ENTRY) {
            //state is not data entry so cannot update
            handleException("unable to update death register idUKey :" + deathRegisterIdUKey + " invalid state :" +
                dr.getStatus(), ErrorCodes.INVALID_STATE_FOR_APPROVE_DEATH_REGISTRATION);
        }
        //check user permission to approve death registration
        ValidationUtils.validateAccessToBDDivision(user, dr.getDeath().getDeathDivision());
        //check user permission to approve death registration TODO

        //there are special user access validation is required to approve late death registration
        if (dr.getDeathType() == DeathRegister.Type.LATE) {
            checkUSerPermissionToApproveLateDeathRegistration(dr, user);
        }

        List<UserWarning> warnings = deathDeclarationValidator.validateStandardRequirements(deathRegisterDAO, dr, user);
        if (warnings.isEmpty() || ignoreWarnings) {
            setApprovalStatus(deathRegisterIdUKey, user, DeathRegister.State.APPROVED, null);
        }
        return warnings;
    }

    private void checkUSerPermissionToApproveLateDeathRegistration(DeathRegister dr, User user) {
        /**
         * on section 36 ADR/DR/ARG/RG allows to approve

         if(with in  12 months)
         ADR/DR can approve

         then
         only the ARG and RG has power to approve

         */
        java.util.GregorianCalendar gCal = new GregorianCalendar();
        gCal.setTime(dr.getDeath().getDateOfRegistration());
        gCal.add(Calendar.MONTH, -12);
        Date dateOfRegistration = gCal.getTime();
        Date dateOfDeath = dr.getDeath().getDateOfDeath();
        //check is data of registration -12 month is less than date of death that mean needs ARG /  RG approval
        if (dateOfDeath.before(dateOfRegistration) && (!(user.getRole().getRoleId().equalsIgnoreCase(Role.ROLE_RG) ||
            user.getRole().getRoleId().equalsIgnoreCase(Role.ROLE_ARG)))) {
            handleException("User :" + user.getUserId() + " does not have permission to approve late death registration idUKey :" +
                dr.getIdUKey() + " , registration is over 12 months so need higher approval",
                ErrorCodes.UNABLE_TO_APPROVE_LATE_DEATH_REGISTRATION_NEED_HIGHER_APPROVAL_THAN_DR);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void rejectDeathRegistration(long deathRegisterIdUKey, User user, String comment) {
        logger.debug("attempt to reject death registration record : {}", deathRegisterIdUKey);

        if (comment == null || comment.trim().length() < 1) {
            handleException("A comment is required to reject a birth declaration",
                ErrorCodes.COMMENT_REQUIRED_BDF_REJECT);
        }
        setApprovalStatus(deathRegisterIdUKey, user, DeathRegister.State.REJECTED, comment);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void markDeathCertificateAsPrinted(DeathRegister deathRegister, User user) {
        logger.debug("requested to mark death certificate as printed for the record : {} ", deathRegister.getIdUKey());
        validateAccessOfUser(user, deathRegister);
        if (DeathRegister.State.APPROVED != deathRegister.getStatus()) {
            handleException("Cannot change status , " + deathRegister.getIdUKey() +
                " Illegal state : " + deathRegister.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        deathRegister.setStatus(DeathRegister.State.ARCHIVED_CERT_GENERATED);
        deathRegisterDAO.updateDeathRegistration(deathRegister, user);
    }

    private void setApprovalStatus(long idUKey, User user, DeathRegister.State state, String comment) {
        // check approve permission
        if (!user.isAuthorized(Permission.APPROVE_DEATH)) {
            handleException("User : " + user.getUserId() + " is not allowed to approve/reject death declarations",
                ErrorCodes.PERMISSION_DENIED);
        }
        DeathRegister dr = deathRegisterDAO.getById(idUKey);
        if (DeathRegister.State.DATA_ENTRY == dr.getStatus()) {
            validateAccessOfUser(user, dr);
            dr.setStatus(state);
            dr.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
            dr.getLifeCycleInfo().setApprovalOrRejectUser(user);
            if (state == DeathRegister.State.APPROVED) {
                dr.getLifeCycleInfo().setCertificateGeneratedTimestamp(new Date());
                dr.getLifeCycleInfo().setCertificateGeneratedUser(user);
                registerDeathOnPRS(dr, user);
            }else if(state == DeathRegister.State.REJECTED){
                dr.getDeath().setDeathSerialNo(changeSerialNo(dr.getDeath().getDeathSerialNo()));
            }

        } else {
            handleException("Cannot approve/reject death registration " + dr.getIdUKey() +
                " Illegal state : " + dr.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        //setting comment, this is relative only to death rejection
        dr.setCommnet(comment);
        //updating
        deathRegisterDAO.updateDeathRegistration(dr, user);
    }

    private Long changeSerialNo(Long serialNo) {
        logger.debug("Attempt to change the serial : {} to {}", serialNo, (serialNo + 800000));
        return (serialNo + 800000);
    }

    /**
     * Executed when a death registration is APPROVED
     * Adds the person to the PRS if he did not exist, and if he did existed, marks the life status as DEAD
     */
    private void registerDeathOnPRS(DeathRegister dr, User user) {

        final String pinOrNic = dr.getDeathPerson().getDeathPersonPINorNIC();
        if (pinOrNic == null) {
            logger.warn("Cannot update PRS on registration of death due to unspecified PIN / NIC");
            return;
        }

        Person person = null;
        try {
            long pin = Long.parseLong(pinOrNic);
            person = ecivil.findPersonByPIN(pin, user);
        } catch (NumberFormatException ignore) {
            for (Person p : ecivil.findPersonsByNIC(pinOrNic, user)) {
                // As there can be more than 1 record for NIC, it will compare the names also.
                if (person == null && Person.LifeStatus.ALIVE == p.getLifeStatus() && (Person.Status.VERIFIED == p.getStatus() || Person.Status.SEMI_VERIFIED == p.getStatus())
                    && dr.getDeathPerson().getDeathPersonNameInEnglish().equals(p.getFullNameInEnglishLanguage())) {
                    person = p;
                    // TODO have check this again have to check state is DEAD then give exception
                } else {
                    handleException("Cannot identify dead person on PRS with duplicate NIC : " + pinOrNic,
                        ErrorCodes.PRS_DUPLICATE_NIC);
                }
            }
        }

        if (person != null) {

            // load lazy collections (such as marriage)
            logger.debug("Marking person with IDUKey : {} as dead on the PRS", person.getPersonUKey());
            person = ecivil.getLoadedObjectByUKey(person.getPersonUKey(), user);
            person.setLifeStatus(Person.LifeStatus.DEAD);
            person.setDateOfDeath(dr.getDeath().getDateOfDeath());
            ecivil.updatePerson(person, user);

            // mark currently married spouses as widowed
            for (Marriage m : person.getMarriages()) {
                if (m.getState() == Marriage.State.MARRIED) {
                    if (AppConstants.Gender.MALE.ordinal() == person.getGender()) {
                        logger.debug("Marking person with PIN : {} as widowed", m.getBride().getPin());
                        m.getBride().setCivilStatus(Person.CivilStatus.WIDOWED);
                        ecivil.updatePerson(m.getBride(), user);
                    } else if(AppConstants.Gender.FEMALE.ordinal() == person.getGender()) {
                        logger.debug("Marking person with PIN : {} as widowed", m.getGroom().getPin());
                        m.getGroom().setCivilStatus(Person.CivilStatus.WIDOWED);
                        ecivil.updatePerson(m.getGroom(), user);
                    }
                }
            }

        } else {

            person = new Person();
            person.setFullNameInEnglishLanguage(dr.getDeathPerson().getDeathPersonNameInEnglish());
            person.setFullNameInOfficialLanguage(dr.getDeathPerson().getDeathPersonNameOfficialLang());
            person.setDateOfDeath(dr.getDeath().getDateOfDeath());
            person.setDateOfBirth(dr.getDeathPerson().getDeathPersonDOB());
            person.setLifeStatus(Person.LifeStatus.DEAD);
            person.setGender(dr.getDeathPerson().getDeathPersonGender());
            person.setRace(dr.getDeathPerson().getDeathPersonRace());
            if (pinOrNic != null) {
                person.setNic(pinOrNic);
            }

            // if a unique father/mother is found, link .. else ignore
            person.setFatherPINorNIC(dr.getDeathPerson().getDeathPersonFatherPINorNIC());
            Person father = ecivil.findUniquePersonByPINorNIC(dr.getDeathPerson().getDeathPersonFatherPINorNIC(), user);
            if (father != null) {
                person.setFather(father);
            }

            // if a unique father/mother is found, link .. else ignore
            person.setMotherPINorNIC(dr.getDeathPerson().getDeathPersonMotherPINorNIC());
            Person mother = ecivil.findUniquePersonByPINorNIC(dr.getDeathPerson().getDeathPersonMotherPINorNIC(), user);
            if (mother != null) {
                person.setMother(mother);
            }
            person.setStatus(Person.Status.SEMI_VERIFIED);
            ecivil.addPerson(person, user);

            if (dr.getDeathPerson().getDeathPersonPermanentAddress() != null) {
                Address address = new Address(dr.getDeathPerson().getDeathPersonPermanentAddress());
                address.setPermanent(true);
                person.specifyAddress(address);
                ecivil.addAddress(address, user);
                ecivil.updatePerson(person, user);
            }
            logger.debug("Added person with IDUKey : {} - as dead on the PRS", person.getPersonUKey());
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteDeathRegistration(long deathRegiserIdUKey, User user) {
        logger.debug("attempt to delete death registration record : {}", deathRegiserIdUKey);
        DeathRegister dr = deathRegisterDAO.getById(deathRegiserIdUKey);
        validateAccessOfUser(user, dr);
        if (DeathRegister.State.DATA_ENTRY != dr.getStatus()) {
            handleException("Cannot delete death registraion " + deathRegiserIdUKey +
                " Illegal state : " + dr.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }
        deathRegisterDAO.deleteDeathRegistration(deathRegisterDAO.getById(deathRegiserIdUKey), user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getPaginatedListForState(BDDivision deathDivision, int pageNo, int noOfRows, DeathRegister.State status, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get death registrations with the state : " + status
                + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        ValidationUtils.validateAccessToBDDivision(user, deathDivision);
        return deathRegisterDAO.getPaginatedListForState(deathDivision, pageNo, noOfRows, status);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getByBDDivisionAndRegistrationDateRange(BDDivision deathDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, User user) {
        ValidationUtils.validateAccessToBDDivision(user, deathDivision);
        return deathRegisterDAO.getByBDDivisionAndRegistrationDateRange(deathDivision, startDate, endDate, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getByBDDivisionAndRegistrationDateRangeAndState(BDDivision deathDivision,
        Date startDate, Date endDate, int pageNo, int noOfRows, DeathRegister.State state, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("attempt to get paginated list of death records by bdDivision :" + deathDivision +
                " and date range :" + startDate + "-" + endDate + " and state :" + state);
        }
        ValidationUtils.validateAccessToBDDivision(user, deathDivision);
        return deathRegisterDAO.getByBDDivisionAndRegistrationDateRangeAndState(deathDivision, startDate, endDate, pageNo, noOfRows, state);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getPaginatedListForAll(BDDivision deathDivision, int pageNo, int noOfRows, User user) {
        logger.debug("Get all death registrations   Page : {}  with number of rows per page : {} ", pageNo, noOfRows);
        ValidationUtils.validateAccessToBDDivision(user, deathDivision);
        return deathRegisterDAO.getPaginatedListForAll(deathDivision, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public DeathRegister getByBDDivisionAndDeathSerialNo(BDDivision bdDivision, long deathSerialNo, User user) {
        DeathRegister dr = deathRegisterDAO.getActiveRecordByBDDivisionAndDeathSerialNo(bdDivision, deathSerialNo);
        validateAccessOfUser(user, dr);
        return dr;
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }

    private void businessValidations(DeathRegister deathRegister, User user) {
        validateAccessOfUser(user, deathRegister);
        if (deathRegister.getStatus() != DeathRegister.State.DATA_ENTRY) {
            handleException("can not update death registration " + deathRegister.getIdUKey() +
                " Illegal State : " + deathRegister.getStatus(), ErrorCodes.ILLEGAL_STATE);
        }

    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getPaginatedListForStateByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, DeathRegister.State status, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get death registrations with the state : " + status
                + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        ValidationUtils.validateAccessToDSDivision(dsDivision, user);
        return deathRegisterDAO.getPaginatedListForStateByDSDivision(dsDivision, pageNo, noOfRows, status);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getPaginatedListForAllByDSDivision(DSDivision dsDivision, int pageNo, int noOfRows, User user) {
        logger.debug("Get all death registrations   Page : {}  with number of rows per page : {} ", pageNo, noOfRows);
        ValidationUtils.validateAccessToDSDivision(dsDivision, user);
        return deathRegisterDAO.getPaginatedListForAllByDSDivision(dsDivision, pageNo, noOfRows);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<DeathRegister> getByPinOrNic(String pinOrNic, User user) {
        List<DeathRegister> deathRegisterList = deathRegisterDAO.getDeathRegisterByDeathPersonPINorNIC(pinOrNic);
        if (deathRegisterList.size() > 0) {
            validateAccessOfUser(user, deathRegisterList.get(0));
            return deathRegisterList;
        } else {
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public DeathRegister getActiveRecordByBDDivisionAndSerialNo(BDDivision bdDivision, long serialNo, User user) {

        logger.debug("Get active record by BDDivision ID : {} and Serial No : {}", bdDivision.getBdDivisionUKey(), serialNo);

        DeathRegister ddf = deathRegisterDAO.getActiveRecordByBDDivisionAndDeathSerialNo(bdDivision, serialNo);
        // does the user have access to the DR (i.e. check district and DS division)
        validateAccessOfUser(user, ddf);
        return ddf;
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getActiveRecordsByDSDivisionAndSerialNo(DSDivision dsDivision, long serialNo, User user) {
        logger.debug("Get active death records by DS Division {} and Serial No: {}", dsDivision.getEnDivisionName(), serialNo);
        return deathRegisterDAO.getActiveRecordByDSDivisionAndDeathSerialNo(dsDivision, serialNo);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getArchivedCorrectedEntriesForGivenSerialNo(BDDivision bdDivision, long serialNo, long deathId, User user) {
        logger.debug("Searching for historical records for BD Division : {} and Serial number : {} ",
            bdDivision.getBdDivisionUKey(), serialNo);
        ValidationUtils.validateAccessToBDDivision(user, bdDivision);
        return deathRegisterDAO.getHistoricalRecordsForBDDivisionAndSerialNo(bdDivision, serialNo, deathId);
    }

    private void validateAccessOfUser(User user, DeathRegister dr) {
        if (dr != null) {
            BDDivision bdDivision = dr.getDeath().getDeathDivision();
            ValidationUtils.validateAccessToBDDivision(user, bdDivision);
        }
    }

    private void loadValues(DeathRegister deathRegister, boolean certificateSearch, User user) {
        logger.debug("loading transient values for death record : {}", deathRegister.getIdUKey());
        if ((deathRegister.getOriginalDCIssueUser() == null && deathRegister.getOriginalDCPlaceOfIssue() == null)
            || certificateSearch) {
            //first time marking as print
            deathRegister.setOriginalDCIssueUser(user);
            deathRegister.setOriginalDCPlaceOfIssue(user.getPrimaryLocation());
            deathRegister.setOriginalDCPlaceOfIssueSignPrint(deathRegister.getOriginalDCIssueUser().getPrimaryLocation().
                getLocationSignature(deathRegister.getDeath().getPreferredLanguage()));
            deathRegister.setOriginalDCPlaceOfIssuePrint(deathRegister.getOriginalDCIssueUser().getPrimaryLocation().
                getLocationName(deathRegister.getDeath().getPreferredLanguage()));
            deathRegister.setOriginalDCIssueUserSignPrint(deathRegister.getOriginalDCIssueUser().
                getUserSignature(deathRegister.getDeath().getPreferredLanguage()));
        } else {
            //existing values (reprint)
            deathRegister.setOriginalDCPlaceOfIssueSignPrint(deathRegister.getOriginalDCPlaceOfIssue().
                getLocationSignature(deathRegister.getDeath().getPreferredLanguage()));
            deathRegister.setOriginalDCPlaceOfIssuePrint(deathRegister.getOriginalDCPlaceOfIssue().
                getLocationName(deathRegister.getDeath().getPreferredLanguage()));
            deathRegister.setOriginalDCIssueUserSignPrint(deathRegister.getOriginalDCIssueUser().
                getUserSignature(deathRegister.getDeath().getPreferredLanguage()));
        }

    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getPaginatedDeathRegisterListByDeathDivisionAndRegistrationDateRange(int deathDivisionId,
        Date startDate, Date endDate, boolean active, User user) {
        return Collections.emptyList();
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getPaginatedDeathRegisterListByDistrictAndRegistrationDateRange(int districtId,
        Date startDate, Date endDate, boolean active, User user) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getPaginatedDeathRegisterListByDSDivisionAndRegistrationDateRange(int dsDivisionId,
        Date startDate, Date endDate, boolean active, int pageNo, int numOfRows, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("attempt to get list of death register objects by dsDivision idUKey :" + dsDivisionId +
                " and from :" + startDate + " to : " + endDate + "and active :" + active);
        }
        return deathRegisterDAO.getPaginatedDeathRegisterListByDSDivisionAndRegistrationDateRange(dsDivisionId, startDate,
            endDate, pageNo, numOfRows, active);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getPaginatedDeathRegisterListByDSDivisionAndRegistrationDateRangeAndState(int dsDivisionId,
        Date startDate, Date endDate, boolean active, int pageNo, int numOfRows, DeathRegister.State status, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("attempt to get list of death register objects by dsDivision idUKey :" + dsDivisionId +
                " and from :" + startDate + " to : " + endDate + "and active :" + active + "and state :" + status);
        }
        return deathRegisterDAO.getPaginatedDeathRegisterListByDSDivisionAndRegistrationDateRangeAndState(dsDivisionId, startDate,
            endDate, pageNo, numOfRows, active, status);
    }

    /**
     * @inheritDoc
     */
    public CommonStatistics getCommonDeathCertificateCount(String user) {
        CommonStatistics commonStat = new CommonStatistics();

        int data_entry = deathRegisterDAO.getDeathCertificateCount(DeathRegister.State.DATA_ENTRY, new Date(), new Date());
        int approved = deathRegisterDAO.getDeathCertificateCount(DeathRegister.State.APPROVED, new Date(), new Date());
        int rejected = deathRegisterDAO.getDeathCertificateCount(DeathRegister.State.REJECTED, new Date(), new Date());

        commonStat.setTotalSubmissions(34/*data_entry + approved + rejected*/);
        commonStat.setApprovedItems(/*approved*/15);
        commonStat.setRejectedItems(/*rejected*/12);
        commonStat.setTotalPendingItems(/*data_entry*/2);

        logger.debug("DeathRegistrationService Called!");

        //todo call above methods using appropriate Date range

        commonStat.setArrearsPendingItems(0);
        commonStat.setLateSubmissions(0);
        commonStat.setNormalSubmissions(8);
        commonStat.setThisMonthPendingItems(3);

        return commonStat;
    }

    public CommonStatistics getDeathStatisticsForUser(String user) {

        int data_entry = 0;
        int approved = 0;
        int rejected = 0;

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);

        int data[] = getDeathDeclarationList(userManager.getUserByID(user), cal.getTime(), new Date());
        if (data.length == 3) {
            data_entry = data[0];
            approved = data[1];
            rejected = data[2];
        }

        CommonStatistics commonStat = new CommonStatistics();
        commonStat.setTotalSubmissions(data_entry + approved + rejected);
        commonStat.setApprovedItems(approved);
        commonStat.setRejectedItems(rejected);
        commonStat.setTotalPendingItems(data_entry);

        cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -5);

        data = getDeathDeclarationList(userManager.getUserByID(user), cal.getTime(), new Date());
        if (data.length == 3) {
            data_entry = data[0];
            approved = data[1];
            rejected = data[2];
        }

        commonStat.setArrearsPendingItems(0);
        commonStat.setLateSubmissions(0);
        commonStat.setNormalSubmissions(8);
        commonStat.setThisMonthPendingItems(3);

        return commonStat;
    }

    private int[] getDeathDeclarationList(User user, Date start, Date end) {

        int data[] = {0, 0, 0};
        List<DeathRegister> deathList = deathRegisterDAO.getByCreatedUser(user, start, end);

        for (DeathRegister dr : deathList) {
            DeathRegister.State state = dr.getStatus();
            switch (state) {
                case APPROVED:
                    data[0] += 1;
                    break;
                case REJECTED:
                    data[1] += 1;
                    break;
                case DATA_ENTRY:
                    data[2] += 1;
                    break;
            }
        }
        return data;
    }

    public List<DeathRegister> getByDSDivisionAndStatusAndRegistrationDateRange(DSDivision dsDivision, Date startDate,
        Date endDate, DeathRegister.State state, User user) {
        //TODO check user permission
        return deathRegisterDAO.getDeathRegisterByDivisionAndStatusAndDate(dsDivision, state, startDate, endDate);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getAllRejectedDeathsByUser(User user) {
        logger.debug("Loading rejected death records by {}", user.getUserId());
        return deathRegisterDAO.getAllRejectedDeathsByUser(user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getAllRejectedDeathsByDistrict(District district, User user) {
        logger.debug("Loading rejected death records of district {} by {}", district.getEnDistrictName(), user.getUserId());
        return deathRegisterDAO.getAllRejectedDeathsByDistrict(district);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<DeathRegister> getAllRejectedDeathsByDSDivision(DSDivision dsDivision, User user) {
        logger.debug("Loading rejected death records of dsDivision {} by {}", dsDivision.getEnDivisionName(), user.getUserId());
        return deathRegisterDAO.getAllRejectedDeathsByDSDivision(dsDivision);
    }
}
