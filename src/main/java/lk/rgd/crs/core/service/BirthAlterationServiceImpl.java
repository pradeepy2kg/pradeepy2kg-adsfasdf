package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.domain.Role;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.BirthAlterationDAO;
import lk.rgd.crs.api.dao.BirthDeclarationDAO;
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.BirthAlterationService;
import lk.rgd.crs.core.ValidationUtils;
import lk.rgd.prs.api.dao.PersonDAO;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Handles the birth alteration process
 *
 * @author Indunil Moremada
 * @author Asankha Perera (reviewed, reorganized and completed)
 *         //todo user permission for doing such a function
 */
public class BirthAlterationServiceImpl implements BirthAlterationService {

    private static final Logger logger = LoggerFactory.getLogger(BirthAlterationServiceImpl.class);
    private final BirthAlterationDAO birthAlterationDAO;
    private final BirthDeclarationDAO birthDeclarationDAO;
    private final PersonDAO personDAO;
    private final PopulationRegistry ecivil;
    private final BirthAlterationValidator birthAlterationValidator;

    public BirthAlterationServiceImpl(BirthAlterationDAO birthAlterationDAO, BirthDeclarationDAO birthDeclarationDAO,
        PopulationRegistry ecivil, PersonDAO personDAO, BirthAlterationValidator birthAlterationValidator) {
        this.birthAlterationDAO = birthAlterationDAO;
        this.birthDeclarationDAO = birthDeclarationDAO;
        this.ecivil = ecivil;
        this.personDAO = personDAO;
        this.birthAlterationValidator = birthAlterationValidator;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addBirthAlteration(BirthAlteration ba, User user) {
        //todo amith call to adding validator (check must fill fields)
        //validate can be added a BA for this BC by state
        logger.debug("Adding new birth alteration record on request of : {}", ba.getDeclarant().getDeclarantFullName());
        //      birthAlterationValidator.checkOnGoingAlterationOnThisSection(ba.getBdfIDUKey(), ba.getType(), user);
        ba.setSubmittedLocation(user.getPrimaryLocation());
        ba.setStatus(BirthAlteration.State.DATA_ENTRY);
        // any user (DEO, ADR of any DS office or BD division etc) can add a birth alteration request
        birthAlterationDAO.addBirthAlteration(ba, user);
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateBirthAlteration(BirthAlteration ba, User user) {
        logger.debug("Attempt to edit birth alteration record : {}", ba.getIdUKey());
        validateAccessOfUserToEditOrDelete(ba, user);
        BirthAlteration existing = birthAlterationDAO.getById(ba.getIdUKey());
        validateAccessOfUserToEditOrDelete(existing, user);
        birthAlterationDAO.updateBirthAlteration(ba, user);
        logger.debug("Saved changes made to birth alteration record : {}  in data entry state", ba.getIdUKey());
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteBirthAlteration(long idUKey, User user) {
        logger.debug("Attempt to delete birth alteration record : {}", idUKey);
        checkUserPermission(Permission.EDIT_BIRTH_ALTERATION, ErrorCodes.PERMISSION_DENIED,
            " delete birth alteration ", user);
        BirthAlteration existing = birthAlterationDAO.getById(idUKey);
        if (existing != null) {
            //check is correct state for deleting
            if (existing.getStatus() == BirthAlteration.State.DATA_ENTRY) {
                validateAccessOfUserToEditOrDelete(existing, user);
                birthAlterationDAO.deleteBirthAlteration(idUKey);
            } else {
                handleException("unable to delete requested birth alteration :" + idUKey + " invalid state for delete :"
                    + existing.getStatus(), ErrorCodes.INVALID_STATE_FOR_DELETE_BIRTH_ALTERATION);
            }
        } else {
            handleException("unable to find birth alteration :" + idUKey + " for deleting ",
                ErrorCodes.CAN_NOT_FIND_BIRTH_ALTERATION);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public BirthAlteration getByIDUKey(long idUKey, User user) {
        logger.debug("Loading birth alteration record : {}", idUKey);
        BirthAlteration ba = birthAlterationDAO.getById(idUKey);
        return ba;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void approveBirthAlteration(BirthAlteration ba, Map<Integer, Boolean> fieldsToBeApproved, User user) {
        //todo amith no check for state of the existing alteration
        validateAccessOfUserForApproval(ba, user);
        BirthAlteration existing = birthAlterationDAO.getById(ba.getIdUKey());
        validateAccessOfUserForApproval(existing, user);

        boolean containsApprovedChanges = false;
        for (Map.Entry<Integer, Boolean> e : fieldsToBeApproved.entrySet()) {
            if (Boolean.TRUE.equals(e.getValue())) {
                logger.debug("Setting status as approved for the alteration statement : {}", e.getKey());
                existing.getApprovalStatuses().set(e.getKey(), true);
                containsApprovedChanges = true;
            } else {
                logger.debug("Setting status as rejected for the alteration statement : {}", e.getKey());
                existing.getApprovalStatuses().set(e.getKey(), false);
            }
        }

        if (containsApprovedChanges) {
            logger.debug("Requesting the application of changes to the BC as final for : {}", existing.getIdUKey());
            existing.setStatus(BirthAlteration.State.FULLY_APPROVED);

            // We've saved the alteration record, now lets modify the birth record
            BirthDeclaration bdf = birthDeclarationDAO.getById(existing.getBdfIdUKey());
            switch (existing.getType()) {

                case TYPE_27:
                case TYPE_27A:
                case TYPE_52_1_H: {
                    logger.debug("Alteration is an amendment, inclusion of omission or correction. Type : {}",
                        existing.getType().ordinal());
                    bdf.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_ALTERED);
                    bdf.getLifeCycleInfo().setActiveRecord(false);      // mark old record as a non-active record
                    birthDeclarationDAO.updateBirthDeclaration(bdf, user);

                    // create the new entry as a clone from the existing
                    BirthDeclaration newBDF = null;
                    try {
                        newBDF = bdf.clone();
                    } catch (CloneNotSupportedException e) {
                        handleException("Unable to clone BDF : " + bdf.getIdUKey(), ErrorCodes.ILLEGAL_STATE);
                    }
                    newBDF.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CERT_GENERATED);
                    applyChanges(existing, newBDF, user);
                    birthDeclarationDAO.addBirthDeclaration(newBDF, user);

                    // update the PRS on - Sec 27 name change, 52 1 (h) or (i) - corrections or omissions on
                    // change of name, dob, gender and place of birth (official/english)
                    syncFKOfOtherActiveAlterationsAfterApproval(newBDF.getIdUKey(), bdf.getIdUKey(), user);
                    updatePRSOnAlteration(existing, bdf, user);
                    break;
                }
                case TYPE_52_1_A:
                case TYPE_52_1_B:
                case TYPE_52_1_D:
                case TYPE_52_1_E: {
                    bdf.getRegister().setStatus(BirthDeclaration.State.ARCHIVED_CANCELLED);
                    //todo in this case we have to cancel other alterations as well 
                    birthDeclarationDAO.updateBirthDeclaration(bdf, user);
                    logger.debug("Alteration of type : {} is a cancellation of the existing record : {}",
                        existing.getType().ordinal(), bdf.getIdUKey());
                    removeOtherAlterations(bdf.getIdUKey(), existing.getIdUKey());
                    // cancel any person on the PRS related to this same PIN
                    Person person = personDAO.findPersonByPIN(bdf.getChild().getPin());
                    if (person != null) {
                        person.setStatus(Person.Status.CANCELLED);
                        ecivil.updatePerson(person, user);
                    }
                    break;
                }
            }

        }
        existing.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
        existing.getLifeCycleInfo().setApprovalOrRejectUser(user);
        birthAlterationDAO.updateBirthAlteration(existing, user);

        logger.debug("Updated birth alteration : {}", existing.getIdUKey());
    }

    /**
     * if a alteration approved by 52_1 A, B,D,E then original certificate is no longer active one so the existing
     * DE alterations are no longer valid for that certificate
     */
    private void removeOtherAlterations(long certificateNumber, long currentAlterationId) {
        List<BirthAlteration> birthAlterations = birthAlterationDAO.getBirthAlterationByBirthCertificateNumber(certificateNumber);
        for (BirthAlteration ba : birthAlterations) {
            if (ba.getIdUKey() != currentAlterationId && (ba.getStatus() != BirthAlteration.State.FULLY_APPROVED ||
                ba.getStatus() != BirthAlteration.State.PRINTED)) {
                try {
                    birthAlterationDAO.deleteBirthAlteration(ba.getIdUKey());
                }
                catch (Exception e) {
                    logger.debug("unable to delete birth alteration while cleaning existing birth alteration when an " +
                        "there is a approval for 52_1 A,B,D,E idUKey : {}", ba.getIdUKey());
                    continue;
                }
            }
        }
    }

    private void syncFKOfOtherActiveAlterationsAfterApproval(long newFK, long oldFK, User user) {
        //find existing alterations
        List<BirthAlteration> birthAlterations = birthAlterationDAO.getBirthAlterationByBirthCertificateNumber(oldFK);
        for (BirthAlteration ba : birthAlterations) {
            if (ba.getStatus() == BirthAlteration.State.DATA_ENTRY && ba.getLifeCycleInfo().isActiveRecord()) {
                //updating BA with new FK
                ba.setBdfIdUKey(newFK);
                birthAlterationDAO.updateBirthAlteration(ba, user);
                logger.debug("successfully sync FK of birth alteration idUKey : {} ", ba.getIdUKey());
            }
        }
    }

    private void updatePRSOnAlteration(BirthAlteration ba, BirthDeclaration bdf, User user) {

        final Long pin = bdf.getChild().getPin();
        logger.debug("Updating PRS entries for the birth alteration : {} and PIN : {}", ba.getIdUKey(), pin);

        Person person = personDAO.findPersonByPIN(bdf.getChild().getPin());
        if (person == null) {
            handleException("Cannot locate PRS entry for PIN : " + pin, ErrorCodes.INVALID_PIN);
        }

        switch (ba.getType()) {
            case TYPE_27:
                // name change
                if (ba.getApprovalStatuses().get(Alteration27.CHILD_FULL_NAME_OFFICIAL_LANG)) {
                    person.setFullNameInOfficialLanguage(ba.getAlt27().getChildFullNameOfficialLang());
                }
                if (ba.getApprovalStatuses().get(Alteration27.CHILD_FULL_NAME_ENGLISH)) {
                    person.setFullNameInEnglishLanguage(ba.getAlt27().getChildFullNameEnglish());
                }
                ecivil.updatePerson(person, user);
                logger.debug("Updated the name on the PRS for PIN : {}", pin);
                break;

            case TYPE_27A:
                // including father details if not specified. We do not update mothers name change after marriage
                // as it should be done by the mother with a name change into the PRS before submitting the alteration
                if (ba.getApprovalStatuses().get(Alteration27A.FATHER_NIC_OR_PIN)) {
                    final String fatherPinOrNic = ba.getAlt27A().getFather().getFatherNICorPIN();
                    person.setFatherPINorNIC(fatherPinOrNic);
                    Person father = ecivil.findPersonByPINorNIC(fatherPinOrNic, user);
                    if (father != null) {
                        person.setFather(father);
                        logger.debug("Updated the father of child with PIN : {} to : {}", pin, father.getPin());
                    } else {
                        handleException("Unable to locate father using PIN or NIC : " + fatherPinOrNic, ErrorCodes.INVALID_DATA);
                    }
                }
                ecivil.updatePerson(person, user);
                break;

            case TYPE_52_1_H:
            case TYPE_52_1_I:
                if (ba.getApprovalStatuses().get(Alteration52_1.DATE_OF_BIRTH)) {
                    person.setDateOfBirth(ba.getAlt52_1().getDateOfBirth());
                }
                if (ba.getApprovalStatuses().get(Alteration52_1.PLACE_OF_BIRTH)) {
                    person.setPlaceOfBirth(ba.getAlt52_1().getPlaceOfBirth());
                }
                if (ba.getApprovalStatuses().get(Alteration52_1.GENDER)) {
                    person.setGender(ba.getAlt52_1().getChildGender());
                }

                if (ba.getApprovalStatuses().get(Alteration52_1.MOTHER_NIC_OR_PIN)) {
                    final String motherPinOrNic = ba.getAlt52_1().getMother().getMotherNICorPIN();
                    person.setMotherPINorNIC(motherPinOrNic);
                    Person mother = ecivil.findPersonByPINorNIC(motherPinOrNic, user);
                    if (mother != null) {
                        person.setMother(mother);
                        logger.debug("Updated the mother of child with PIN : {} to : {}", pin, mother.getPin());
                    } else {
                        handleException("Unable to locate mother using PIN or NIC : " + motherPinOrNic, ErrorCodes.INVALID_DATA);
                    }
                }
                ecivil.updatePerson(person, user);
                break;
        }
    }


    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthAlteration> getApprovalPendingByBDDivision
        (BDDivision bdDivision, int pageNo, int noOfRows, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get birth alteration pending approval by BDDivision ID : " + bdDivision.getBdDivisionUKey()
                + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        ValidationUtils.validateAccessToBDDivision(user, bdDivision);
        return populateTransientNameOfficialLanguage(birthAlterationDAO.
            getBulkOfAlterationByBDDivision(bdDivision, pageNo, noOfRows));
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthAlteration> getApprovalPendingByBDDivision
        (BDDivision bdDivision, int pageNo, int noOfRows) {
        //todo need a check for user's accessibility to the alteration record
        if (logger.isDebugEnabled()) {
            logger.debug("Get birth alteration pending approval by BDDivision ID : " + bdDivision.getBdDivisionUKey()
                + " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        return populateTransientNameOfficialLanguage(birthAlterationDAO.
            getBulkOfAlterationByBDDivision(bdDivision, pageNo, noOfRows));
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthAlteration> getApprovalPendingByUserLocationIdUKey(int locationUKey, int pageNo, int noOfRows, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get birth alterations pending approval - by User location id : " + locationUKey +
                " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        return populateTransientNameOfficialLanguage(birthAlterationDAO.
            getBulkOfAlterationByUserLocationIdUKey(locationUKey, pageNo, noOfRows));
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public BirthAlteration getApprovalPendingByIdUKey(Long idUKey, int pageNo, int noOfRows, User user) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get birth alterations pending approval - by idUKey : " + idUKey +
                " Page : " + pageNo + " with number of rows per page : " + noOfRows);
        }
        return birthAlterationDAO.getBulkOfAlterationByIdUKey(idUKey, pageNo, noOfRows);
    }

    /**
     * @inheriteDoc
     */
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<BirthAlteration> getBirthAlterationByBirthCertificateNumber(long idUKey, User user) {
        logger.debug("attempt to get already done birth alterations for birth register idUKey : {}", idUKey);
        return birthAlterationDAO.getBirthAlterationByBirthCertificateNumber(idUKey);
    }

    private List<BirthAlteration> populateTransientNameOfficialLanguage(List<BirthAlteration> birthAlterations) {
        for (BirthAlteration birthAlteration : birthAlterations) {
            birthAlteration.setChildNameInOfficialLanguage(birthDeclarationDAO.getById(birthAlteration.getBdfIdUKey()).
                getChild().getChildFullNameOfficialLang());
        }
        return birthAlterations;
    }

    /**
     * @inheriteDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void rejectBirthAlteration(long idUKey, String comment, User user) {
        logger.debug("attempt to reject birth alteration idUKey : {}", idUKey);
        BirthAlteration birthAlteration = birthAlterationDAO.getById(idUKey);
        if (birthAlteration != null) {
            validateAccessOfUserForApproval(birthAlteration, user);
            //found a record
            if (birthAlteration.getStatus() == BirthAlteration.State.DATA_ENTRY) {
                //in correct state now we can update the record
                birthAlteration.setStatus(BirthAlteration.State.REJECT);
                birthAlteration.setComments(comment);
                birthAlteration.getLifeCycleInfo().setApprovalOrRejectTimestamp(new Date());
                birthAlteration.getLifeCycleInfo().setApprovalOrRejectUser(user);
                birthAlteration.getLifeCycleInfo().setActiveRecord(false);
                birthAlterationDAO.updateBirthAlteration(birthAlteration, user);
            } else {
                handleException("unable to reject birth alteration not in correct state for rejection idUKey :" + idUKey
                    + " current state :" + birthAlteration.getStatus(),
                    ErrorCodes.INVALID_STATE_FOR_REJECT_BIRTH_ALTERATION);
            }
        } else {
            handleException("unable to found a birth alteration record for rejecting idUKey : " + idUKey,
                ErrorCodes.CAN_NOT_FIND_BIRTH_ALTERATION);
        }
    }

    /**
     * @inheriteDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void markBirthAlterationNoticeAsPrinted(long idUKey, User user) {
        logger.debug("attempt to mark birth alteration notice as printed for idUKey : {}", idUKey);
        //any one can mark as printed so  no need to check the permissions only checks the state
        BirthAlteration existing = birthAlterationDAO.getById(idUKey);
        if (existing != null) {
            if (existing.getStatus() == BirthAlteration.State.FULLY_APPROVED) {
                existing.getLifeCycleInfo().setCertificateGeneratedTimestamp(new Date());
                existing.getLifeCycleInfo().setCertificateGeneratedUser(user);
                existing.setStatus(BirthAlteration.State.PRINTED);
                birthAlterationDAO.updateBirthAlteration(existing, user);
            } else {
                handleException("invalid state for mark birth alteration as printed idUKey: " + idUKey + " state :" +
                    existing.getStatus(), ErrorCodes.INVALID_STATE_FOR_MARK_AS_PRINT_BIRTH_ALTERATION);
            }
        } else {
            handleException("unable find birth alteration for  mark  as printed idUKey :" + idUKey,
                ErrorCodes.CAN_NOT_FIND_BIRTH_ALTERATION);
        }
    }

    /**
     * Checks if the user can edit or delete a birth alteration entry before approval by an ARG
     * <p/>
     * A DEO or ADR at the same "SubmissionLocation" can edit or delete an entry at data entry stage. Any other who has
     * access to the BD division of the corresponding BDF has access to edit or delete
     *
     * @param ba   the birth alteration entry
     * @param user the user attempting to update or delete
     */
    private void validateAccessOfUserToEditOrDelete(BirthAlteration ba, User user) {

        if (Role.ROLE_DEO.equals(user.getRole().getRoleId()) || Role.ROLE_ADR.equals(user.getRole().getRoleId())) {
            if (!BirthAlteration.State.DATA_ENTRY.equals(ba.getStatus())) {
                handleException("Birth alteration ID : " + ba.getIdUKey() + " cannot be edited as its not in the " +
                    "Data entry state", ErrorCodes.ILLEGAL_STATE);
            }
            if (ba.getSubmittedLocation().equals(user.getPrimaryLocation())) {
                return;
            }
        } else if (!Role.ROLE_ADMIN.equals(user.getRole().getRoleId())) {
            return;
        }

        ValidationUtils.validateAccessToBDDivision(user,
            birthDeclarationDAO.getById(ba.getBdfIdUKey()).getRegister().getBirthDivision());
    }

    /**
     * Checks if the user can approve the birth alteration entry. Only an ARG level user, in charge of the BDF under
     * consideration can approve an alteration
     * <p/>
     * note :if the section is 27 ADR also can approve the record
     *
     * @param ba   the birth alteration entry
     * @param user the user attempting to approve
     */
    private void validateAccessOfUserForApproval(BirthAlteration ba, User user) {
        if (Role.ROLE_RG.equals(user.getRole().getRoleId())) {
            // RG can approve any record
        } else if ((Role.ROLE_ARG.equals(user.getRole().getRoleId())) ||
            (!(Role.ROLE_DEO.equals(user.getRole().getRoleId())) &&
                (ba.getType() == BirthAlteration.AlterationType.TYPE_27))) {
            ValidationUtils.validateAccessToBDDivision(user,
                birthDeclarationDAO.getById(ba.getBdfIdUKey()).getRegister().getBirthDivision());

            if (!user.isAuthorized(Permission.APPROVE_BIRTH_ALTERATION)) {
                handleException("User : " + user.getUserId() + " is not allowed to approve/reject birth alteration , " +
                    "alteration type : " + ba.getType().name(), ErrorCodes.PERMISSION_DENIED);
            }
        } else {
            handleException("User : " + user.getUserId() + " is not an ARG for alteration approval",
                ErrorCodes.PERMISSION_DENIED);
        }
    }

    private void applyChanges(BirthAlteration ba, BirthDeclaration bdf, User user) {

        switch (ba.getType()) {

            // section 27 - name changes
            case TYPE_27: {
                Alteration27 alt = ba.getAlt27();
                process27Changes(ba, bdf, alt, user);
                break;
            }

            // section 27 A - updates to marriage, mothers name after marriage or
            // {father & great/grand father details if those were not specified in the current BDF}
            case TYPE_27A: {
                Alteration27A alt = ba.getAlt27A();
                process27AChanges(ba, bdf, alt);
                break;
            }

            // section 52 (1) - corrections
            default: {
                Alteration52_1 alt = ba.getAlt52_1();
                process52_1Changes(ba, bdf, alt);
                break;
            }
        }
    }

    private void process27Changes(BirthAlteration ba, BirthDeclaration bdf, Alteration27 alt, User user) {
        // Update name of person on the PRS
        Person person = personDAO.findPersonByPIN(bdf.getChild().getPin());

        if (ba.getApprovalStatuses().get(Alteration27.CHILD_FULL_NAME_OFFICIAL_LANG)) {
            bdf.getChild().setChildFullNameOfficialLang(alt.getChildFullNameOfficialLang());
            person.setFullNameInOfficialLanguage(alt.getChildFullNameOfficialLang());
        }
        if (ba.getApprovalStatuses().get(Alteration27.CHILD_FULL_NAME_ENGLISH)) {
            bdf.getChild().setChildFullNameEnglish(alt.getChildFullNameEnglish());
            person.setFullNameInEnglishLanguage(alt.getChildFullNameEnglish());
        }
        ecivil.updatePerson(person, user);
    }

    private void process27AChanges(BirthAlteration ba, BirthDeclaration bdf, Alteration27A alt) {

        // father details
        if (ba.getApprovalStatuses().get(Alteration27A.FATHER_BIRTH_PLACE)) {
            bdf.getParent().setFatherPlaceOfBirth(alt.getFather().getFatherPlaceOfBirth());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.FATHER_BIRTH_DATE)) {
            bdf.getParent().setFatherDOB(alt.getFather().getFatherDOB());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.FATHER_COUNTRY)) {
            bdf.getParent().setFatherCountry(alt.getFather().getFatherCountry());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.FATHER_PASSPORT)) {
            bdf.getParent().setFatherPassportNo(alt.getFather().getFatherPassportNo());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.FATHER_NIC_OR_PIN)) {
            bdf.getParent().setFatherNICorPIN(alt.getFather().getFatherNICorPIN());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.FATHER_FULLNAME)) {
            bdf.getParent().setFatherFullName(alt.getFather().getFatherFullName());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.FATHER_RACE)) {
            if (bdf.getParent().getFatherRace() != null) {
                handleException("The fathers race cannot be changed under section 27A, when already specified." +
                    " Use Section 52 (1) instead", ErrorCodes.ILLEGAL_STATE);
            } else {
                bdf.getParent().setFatherRace(alt.getFather().getFatherRace());
            }
        }

        // marriage details
        if (ba.getApprovalStatuses().get(Alteration27A.WERE_PARENTS_MARRIED)) {
            bdf.getMarriage().setParentsMarried(alt.getMarriage().getParentsMarried());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.PLACE_OF_MARRIAGE)) {
            bdf.getMarriage().setPlaceOfMarriage(alt.getMarriage().getPlaceOfMarriage());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.DATE_OF_MARRIAGE)) {
            bdf.getMarriage().setDateOfMarriage(alt.getMarriage().getDateOfMarriage());
        }

        // mothers name after marriage
        if (ba.getApprovalStatuses().get(Alteration27A.MOTHER_NAME_AFTER_MARRIAGE)) {
            bdf.getParent().setMotherFullName(alt.getMothersNameAfterMarriage());
        }

        // grand father and great grand father details
        if (ba.getApprovalStatuses().get(Alteration27A.GRAND_FATHER_BIRTH_PLACE)) {
            bdf.getGrandFather().setGrandFatherBirthPlace(alt.getGrandFather().getGrandFatherBirthPlace());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.GRAND_FATHER_BIRTH_YEAR)) {
            bdf.getGrandFather().setGrandFatherBirthYear(alt.getGrandFather().getGrandFatherBirthYear());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.GRAND_FATHER_FULLNAME)) {
            bdf.getGrandFather().setGrandFatherFullName(alt.getGrandFather().getGrandFatherFullName());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.GRAND_FATHER_NIC_OR_PIN)) {
            bdf.getGrandFather().setGrandFatherNICorPIN(alt.getGrandFather().getGrandFatherNICorPIN());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.GREAT_GRAND_FATHER_BIRTH_PLACE)) {
            bdf.getGrandFather().setGreatGrandFatherBirthPlace(alt.getGrandFather().getGreatGrandFatherBirthPlace());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.GREAT_GRAND_FATHER_BIRTH_YEAR)) {
            bdf.getGrandFather().setGreatGrandFatherBirthYear(alt.getGrandFather().getGreatGrandFatherBirthYear());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.GREAT_GRAND_FATHER_FULLNAME)) {
            bdf.getGrandFather().setGreatGrandFatherFullName(alt.getGrandFather().getGreatGrandFatherFullName());
        }
        if (ba.getApprovalStatuses().get(Alteration27A.GREAT_GRAND_FATHER_NIC_OR_PIN)) {
            bdf.getGrandFather().setGreatGrandFatherNICorPIN(alt.getGrandFather().getGreatGrandFatherNICorPIN());
        }
    }

    private void process52_1Changes(BirthAlteration ba, BirthDeclaration bdf, Alteration52_1 alt) {

        // childs details
        if (ba.getApprovalStatuses().get(Alteration52_1.DATE_OF_BIRTH)) {
            bdf.getChild().setDateOfBirth(alt.getDateOfBirth());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.PLACE_OF_BIRTH)) {
            bdf.getChild().setPlaceOfBirth(alt.getPlaceOfBirth());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.PLACE_OF_BIRTH_ENGLISH)) {
            bdf.getChild().setPlaceOfBirthEnglish(alt.getPlaceOfBirthEnglish());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.BIRTH_DIVISION)) {
            bdf.getRegister().setBirthDivision(alt.getBirthDivision());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.GENDER)) {
            bdf.getChild().setChildGender(alt.getChildGender());
        }

        // mothers details
        if (ba.getApprovalStatuses().get(Alteration52_1.MOTHER_NIC_OR_PIN)) {
            bdf.getParent().setMotherNICorPIN(alt.getMother().getMotherNICorPIN());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.MOTHER_COUNTRY)) {
            bdf.getParent().setMotherCountry(alt.getMother().getMotherCountry());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.MOTHER_PASSPORT)) {
            bdf.getParent().setMotherPassportNo(alt.getMother().getMotherPassportNo());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.MOTHER_FULLNAME)) {
            bdf.getParent().setMotherFullName(alt.getMother().getMotherFullName());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.MOTHER_BIRTH_DATE)) {
            bdf.getParent().setMotherDOB(alt.getMother().getMotherDOB());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.MOTHER_AGE_AT_BIRTH)) {
            bdf.getParent().setMotherAgeAtBirth(alt.getMother().getMotherAgeAtBirth());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.MOTHER_RACE)) {
            bdf.getParent().setMotherRace(alt.getMother().getMotherRace());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.MOTHER_BIRTH_PLACE)) {
            bdf.getParent().setMotherPlaceOfBirth(alt.getMother().getMotherPlaceOfBirth());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.MOTHER_ADDRESS)) {
            bdf.getParent().setMotherAddress(alt.getMother().getMotherAddress());
        }

        // informant details
        if (ba.getApprovalStatuses().get(Alteration52_1.INFORMANT_TYPE)) {
            bdf.getInformant().setInformantType(alt.getInformant().getInformantType());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.INFORMANT_NIC_OR_PIN)) {
            bdf.getInformant().setInformantNICorPIN(alt.getInformant().getInformantNICorPIN());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.INFORMANT_NAME)) {
            bdf.getInformant().setInformantName(alt.getInformant().getInformantName());
        }
        if (ba.getApprovalStatuses().get(Alteration52_1.INFORMANT_ADDRESS)) {
            bdf.getInformant().setInformantAddress(alt.getInformant().getInformantAddress());
        }
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }

    private void checkUserPermission(int permissionBit, int errorCode, String msg, User user) {
        if (!user.isAuthorized(permissionBit)) {
            handleException("User : " + user.getUserId() + " is not allowed to " + msg, errorCode);
        }
    }
}
